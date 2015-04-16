import java.util.Random;
import java.util.ArrayList;
import java.util.ArrayDeque;

/**
 * This class represents a business
 * 
 * @author Lawrence Moore
 *
 */
public class Business extends MapConstituent {

	/* In addition to the variables of MapConstituent, we have several additional attributes that describe a business.
	Note that work quality is on a 1-10 scale, and pay and networth are in the thousands
	Something not to forget is how this influences the contentment of a person*/
	private int workQuality, pay, netWorth;

	/* maximum number of employees the company can currently have */
	private int maxNumEmployees;

	/* Keeps track of the change of productivity in the last 30 days.
	If the net productivity meets a threshold growth, then the company growth */

	private ArrayList<Double> productHistory = new ArrayList<Double>();

	/* Keeps track of the last five company decision by the company 
	If the company has declined five times in a row, it goes out of business. 
	+1 will be used to denote expansion.  0 denotes no action.  -1 denotes contraction*/
	private ArrayList<Integer> growthHistory = new ArrayList<Integer>();

	/* List of current employees */
	private ArrayList<Person> employeeList = new ArrayList<Person>();


	/* the variable to store the type of company, which is a public enum */
	private WorkType type;

	/* minimum initial pay and maximum initial pay */
	private final int MIN_PAY = 20, MAX_PAY = 270;

	/* Constructor in which the attributes are randomely generated */
	public Business(Position pos) {
		/* Initialize age to zero and the position to the one passed in */
		age = 0;
		this.pos = pos;

		/* Call the calculateBasicNeedsScore() method to establish the rest of the map constituent attributes */
		calculateBasicNeedsScore();

		Random rand = new Random();

		/* Generate a random work type from the options in the enum */
		type = WorkType.values()[rand.nextInt(WorkType.values().length)];  //just some enum foolery


		/* remember that work quality is out of 10 */
		workQuality = rand.nextInt(10) + 1;

		/* pay is based on work quality and type of job. 
		   Pay ranges from 20 to 270 thousand.  5 is just an adjustment factor*/
		pay = workQuality * (rand.nextInt(5) + 1) * (type.ordinal() + 1) + MIN_PAY;

		/* Start with a max of 100 employees */
		maxNumEmployees = 100;

		/* Initialize networth. It should always be equal to total pay to employees times 5 */
		netWorth = (pay * maxNumEmployees * 5);
	}

	/* Calculates what the basic needs scores are for a business */
	public void calculateBasicNeedsScore() {
		/* Businesses don't provide shelter, fun, or food (they do pay you though) */
		shelterScore = 0;
		funScore = 0;
		foodScore = 0;
	}

	/* Checks if the company wants to hire the person based on skills and personality */
	public boolean willHire(Person person) {
		
		/* Calculate employee score */
		double applicantScore = calculateEmployeeScore(person);

		/* Calculate threshold score to hire.  On a scale of .1 to 10 */
        double payFactor = pay / (double) MIN_PAY;
        payFactor = payFactor > 10 ? 10: payFactor;
		double threshold = ((double) workQuality + payFactor) / 2; //divide by 2 scales since the max value is 20


		/* Check if applicant is eligible to work here */
		if (applicantScore < threshold || employeeList.size() >= maxNumEmployees) {
			return false;
		}
		return true;
	}

	/* Calculates the performance of the employee */
	private double calculateEmployeeScore(Person person) {

		/* get personality */
		Personality personality = person.getPersonality();

		/* get skill and ambition */
		int skill = personality.getSkill();
		int ambition = personality.getAmbition();
		int contentment = personality.getContentment();
		WorkType preferredWork = personality.getPreferredWork();

		/* Get basic needs scores */
		int[] needs = person.getNeeds();

		/* Gauge how much in need the person is */
		double wellBeing = 0;
		for (int i = 0; i < needs.length; i++) {
			wellBeing += needs[i]; 
		}

		/* Scale the score (maximum overall well-being is 3, since each need would 1).  */
		wellBeing = (30 - wellBeing)/ 27;

		/* Calculates enthusiasm multiplier: .5 if not preferred area of work, 1 otherwise */
		double enthusiasm = preferredWork == type ? 1 : 0.5; 

		/* Calculate applicant score. On a scale of 0 to 10. 
		The divide by 2 scales the value appropriately, since the max is 20 */
		double employeeScore = (((double) skill + ambition) * enthusiasm * wellBeing) / (2);

		return employeeScore > 10? 10: employeeScore;
	}

	/* Hires a person 
	Returns a boolean true if there's space */
	public boolean hire(Person person) {
		/* Check if there's room */
		if (employeeList.size() < maxNumEmployees) {
			employeeList.add(person);
			return true;
		}
		return false;
	}

	/* Fires the worst employee */
	private void fire() {
		/* Set up variables to parse array of employees for the worst performing individual */
		Person minPerson = null;
		double minScore = 100;
		double score = 0;

		for (Person employee: employeeList) {
			/* Find the persons score */
			score = calculateEmployeeScore(employee);

			/* If the calculated scores is less than all others, update worst employee */
			if (score < minScore) {
				minPerson = employee;
				minScore = score;
			}
		}

		if (minPerson != null) {
			employeeList.remove(minPerson);
			minPerson.getFired();
		}
	}

	/* Calculates the overall productivity of the company by tallying the productivity of the employees */
	private double calculateProductivity() {
		double score = 0;

		/* find the productivity score of each employee (which is on a scal from 0 to 10) */
		for (Person person: employeeList) {
			score += calculateEmployeeScore(person);
		}

		/* return the overall average productivity score per employee, on a scal from 0 to 10 */
		if (employeeList.size() > 0) {
			return score / employeeList.size();
		}
		return score;

	}


	/* This method is called by simulator once every 24 timesteps.
	The function return true if the business does not shut down.
	Otherwise, it returns true */
	public boolean update() {
		/* calculate the average productivity of the employee*/
		boolean companyStatus = true;
		double productivityOfTheDay = calculateProductivity();

		/* If we already have a full month of growth history */
		if (productHistory.size() == 30) {
			/* Decide on what to do: grow, contract, or do nothing. 
			DecideFuture returns false if the company shuts down; true otherwise*/
            companyStatus = decideFuture();

			/* Now we reset the list */
            while (productHistory.size() != 0) {
                productHistory.remove(0);
            }
        }
		/* Add today's productivity score to the tally */
		productHistory.add(productivityOfTheDay);
		return companyStatus;
	}

	/*Decides the what action to take: expand, contract, or do nothing.
	If the company contracts for five straight months, it shuts down. 
	In this case, the function return false.  
	Otherwise, it returns true */
	private boolean decideFuture() {
		/* First, calculate the net productivity of the company */
		double netProductivity = 0;
		for (Double product: productHistory) {
			netProductivity += product;
		}

		int averageScoreToGrow = 8;
		int averageScoreToContract = 5;

		/*As a reminder, the growth history represents the actions taken in the last five months
		+1 means it expanded.  -1 means it contracted.  0 means it did nothing */

		/* See if the company can expand*/
		if ( (netProductivity/ productHistory.size()) >= averageScoreToGrow) {
			expand();
			growthHistory.add(0, +1);
		} else if ( (netProductivity/ productHistory.size()) < averageScoreToContract) {
			contract();
			growthHistory.add(0, -1);
		} else {
			growthHistory.add(0, 0);
		}

		/* Gauge the overall trend of company by looking at the growthHistory.
		If the company has contracted all five previous months, it must shut down */

        if (growthHistory.size() >= 5) {

            int[] lastFiveMonths = new int[5];

            //First we find the overall trend of the last five months
            int trend = 0;
            for (int i = 0; i < 5; i++) {
                trend += growthHistory.get(i);
            }

            /* Check if all were contractions (sorry for the ugly code) */
            if (trend == -5) {
                shutdownCompany();
                return false;
            }
            growthHistory.remove(growthHistory.size() - 1);
        }
        return true;
	}

	/* When a business expands, it increases its networth, hires more people, increases pay */
	private void expand() 
	{
		/* Generic constants */
		double netWorthMultiplyer = 1.1;
		int employeeNumIncrease = 10;
		double payIncrease = 1.05;
		double chanceOfQualityIncrease = .2;

		/* Edits value properly */
		netWorth *= netWorthMultiplyer;
		maxNumEmployees += employeeNumIncrease;
		pay *= payIncrease;

		/* Increase the quality according to some chance */
		Random rand = new Random();
		if ( rand.nextDouble() <= chanceOfQualityIncrease) {
			workQuality++;
		}
	}

	/* When a business contracts, it decreases its networth, fires more people, decreases pay */
	private void contract() {
		/* Generic constants */
		double netWorthMultiplyer = 1.1;
		int employeeNumDecrease = 10;
		double payIncrease = 1.05;
		double chanceOfQualityIncrease = .2;

		/* Edits value properly */
		netWorth /= netWorthMultiplyer;
		maxNumEmployees -= employeeNumDecrease;
		pay /= payIncrease;

		/* Fire these people */
		int i = 0;
		while (i < employeeNumDecrease && employeeList.size() > 0) {
			fire();
		}

		/* Increase the quality according to some chance */
		Random rand = new Random();
		if (rand.nextDouble() <= chanceOfQualityIncrease) {
			workQuality--;
		}
	}


	/* In case the employee leaves the company */
	public void leaveCompany(Person person) {
		employeeList.remove(person);
	}

	/* Close down the company, which means fire everyone */
	public void shutdownCompany() {
		while (employeeList.size() > 0) {
			fire();
		}
	}


	/* Getters */
	public int getPayRate() {
		return pay;
	}

    public int getQuality() { return workQuality; }

	public WorkType getWorkType() {
		return type;
	}
}