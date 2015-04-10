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
	private int workQuality, pay, numEmployees, netWorth;

	/* Keeps track of the change of productivity in the last 30 days.
	If the net productivity meets a threshold growth, then the company growth */

	private ArrayList<Integer> productHistory = new ArrayList<Integer>();

	/* Keeps track of the last five growth decision by the company 
	If the company has declined five times in a row, it goes out of business */
	private ArrayDeque<Integer> growthHistory = new ArrayDeque<Integer>();

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
		   Pay ranges from 20 to 270 thousand.  5 is just an adjustement factor*/
		pay = workQuality * rand.nextInt(5) * type.ordinal() + MIN_PAY;

		/* Start with zero employees (The person class is in charge of getting people hired */
		numEmployees = 0;

		/* Initialize networth. It should always be equal to total pay to employees times 5 */
		networh = (pay * numEmployees * 5)
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

		/* Calculate threshold score to hire.  On a scale of .2 to 10 */
		double threshold = workQuality * (pay / MAX_PAY);


		/* Check if applicant is elligible to work here */
		if (applicantScore >= threshold) {
			return true;
		}
		return false;
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
		double wellBeing = 0
		for (int i = 0; i < needs.length; i++) {
			wellBeing += needs[i]; 
		}

		/* Scale the score */
		wellBeing /= 30;

		/* Calculates enthusiasm multiplyer: .5 if not prefered area of work, 1 otherwise */
		double enthusiasm = preferredWork == type ? 1 : 0.5; 

		/* Calculate applicant score. On a scale of 0 to 10. 
		The divide by 100 scales the value appropiately*/
		double employeeScore = (skill * ambition * contentment * enthusiasm * wellBeing) / (100);

		return employeeScore;
	}

	/* Hires a person */
	public void hire(Person person) {
		employeeList.add(person);
		numEmployees++;
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
		employeeList.remove(minPerson);
		minPerson.getFired();
	}

	/* Calculates the overall productivity of the company by tallying the productivity of the employees */
	private double calculateProductivity() {

	}

	/*TO DO: model how business expand/contract over time based on conceptual model */
	private void expand() {
		//increase networth, hire more people, increase pay
	}
	private void contract() {
		//do the opposite of above -q
	}

	/*TO DO: implement fire, hire, and other business success related functions */
	private void calculateProductivity() {}
	private void decideFuture() {}

	/* This method is called by simulator once every 24 timesteps */
	public void timeElapse(int time) {
		//TO DO: finish mofo
		double productivityOfTheDay = calculateProductivity();
		//edit growthHistory
		//Decide future dictates whether to expand, contract, or do neither
		decideFuture();


	}


	/* Getters */
	public void getPayRate() {
		return pay;
	}
}