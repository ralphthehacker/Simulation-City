import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents an individual agent in the world
 * Agents have three basic needs: food, shelter, and fun
 * An agent also a personality
 * If an agent has a child, they must spend more on food each month until the child turns 18
 * An agent has a current state, which essentially means where the agent is at any give instance
 * Don't forget to take into account when a person is unemployed 
 * @author Lawrence Moore
 *
 */

public class Person {

    public static final int ADULT_AGE = 18;
    public static final boolean DEAD = true;
    public static final boolean ALIVE = false;
    /* These needs are on a 1 to 10 scale, with 10 being the most dire need */
    private int foodNeed, shelterNeed, funNeed;
    private int money;
    private int stateTimeLock; // This variable represents the number of clock cycles that a certain agent is locked to after entering it.

    /* People start out 18 */
    private int age = 18;

    private Personality personality;
    private boolean hasChild;
    private int childAge;
    private State state;
    private int amountOfFood;

    private Business workplace;
    private Residence residence;
    private Position position;
    private Map map;

    /* Keeps track of the last five days (100 total hours) of a person's 3 basic needs*/
    private ArrayList<Integer> healthStatus = new ArrayList<Integer>(100);

    /* Creates a random person by calling the constructor below */

    public static Person createRandomPerson(Residence residence, Business[] workplace, Map map) {
        Random random = new Random();

        /* Instantiate the person */
        Person p = new Person(residence, workplace, map);

        /* Generate a random amount of money */
        p.money = (int) (300 * random.nextDouble());

        return p;
    }

    /* The constructor of the Person class; takes in residence and workplac */

    public Person(Residence residence, Business[] workplaces, Map map) {
        personality = new Personality();
        hasChild = false;
        childAge = 0;
        money = 100;
        amountOfFood = 3;
        state = State.SLEEP;
        this.stateTimeLock = StateMachine.getLockTime(State.SLEEP);//A person starts free
        this.residence = residence;
        position = residence.getPosition();
        this.map = map;

        /* find place of work by iterating through all workplaces.
        Ultimately chooses based on amount of money and type of work */
        int i = 0;

        Business bestWork = null;
        double bestCompanyScore = 0;
        double companyScore = 0;
        
        while (i < workplaces.length) {
            Business work = workplaces[i];

            /* check if the work will hire the person */
            if (work.willHire(this)) {
                companyScore = work.getPayRate() * (personality.getPreferredWork() == work.getWorkType() ? 1 : 0.5);

                /* See if this is the best workplace found so far */
                if (companyScore > bestCompanyScore) {
                    bestCompanyScore = companyScore;
                    bestWork = work;
                }
            }

            /* Assuming a workplace that will hire the person is found, hire them */
            if (bestWork != null) {
                bestWork.hire(this);
                this.workplace = bestWork;
            }
            i++;
        }
    }


    // Update the person's needs and/or state
    // Returns Person.DEAD if person dies. Otherwise,
    // returns Person.ALIVE
    public boolean update(int time) {

        /* Increase age by one day.  Handle babies*/
        if (time == 0) {
            age++;
            if (babyMakingTime()) {
                hasChild = true;
                childAge = 1;
            }

            if (hasChild && childAge >= 18) {
                map.addPerson();
                hasChild = false;
            } else {
                childAge++;
            }
        }

        if (healthStatus.size() < 100) {
            //Add the basic needs status to the Beginning of the list
            healthStatus.add(0, healthScore());
        } else {
            //get rid of the oldest score
            healthStatus.remove(99);
            //Add newest to the front
            healthStatus.add(0, healthScore());
        }

        // Slowly decrement all needs.
        foodNeed = Math.min(foodNeed + 1, 10);
        shelterNeed = Math.min(shelterNeed + 1, 10);
        funNeed = Math.min(funNeed + 1, 10);

        if (state.equals(State.SLEEP)) {
            shelterNeed = Math.max(shelterNeed - 2, 0);
        } else if (state.equals(State.BREAKFAST_HOME)) {
            // TODO: Decrease food supply home
            foodNeed = Math.max(foodNeed - 6, 0);
        } else if (state.equals(State.BREAKFAST_OUT)) {
            money -= 10;
            foodNeed = Math.max(foodNeed - 6, 0);
            funNeed = Math.max(funNeed - 7, 0);
        } else if (state.equals(State.WORK)) {
            // TODO: If person is unemployed, look for job
            money += workplace.getPayRate();
        } else if (state.equals(State.DINNER_OUT)) {
            money -= 10;
            foodNeed = Math.max(foodNeed - 6,  0);
            funNeed = Math.max(funNeed - 7, 0);
        } else if (state.equals(State.SHOP)) {
            // TODO: Increase food supply at home instead
            money -= 5;
            foodNeed = Math.max(foodNeed - 6, 0);
        } else if(state.equals(State.DINNER_HOME)) {
            // TODO: Decrease food supply at home
            foodNeed = Math.max(foodNeed - 6, 0);
        }

        // TODO: Uncomment next line when StateMachine works
        this.state = StateMachine.getNextState(this,time);

        return checkHealth();
    }

    /* Calculate the overall need of the person, weighting dire needs more */
    private int healthScore() {
        int overallScore = 0;
        double foodMultiplier, funMultiplier, shelterMultiplier;


        foodMultiplier = setMultiplier(foodNeed);
        funMultiplier = setMultiplier(funNeed);
        shelterMultiplier = setMultiplier(shelterNeed);

        /* Scale the result properly */
        return (int) ((foodNeed * foodMultiplier + funNeed * funMultiplier + shelterNeed*shelterMultiplier)
                / ((foodMultiplier + funMultiplier + shelterMultiplier) * 30));
    }

    /* Helper method which scales basic need scores appropriately */
    private double setMultiplier(int need) {
        double multiplier = 1;
        if (need > 7) {
            multiplier = 2;
        } else if (need < 3) {
            multiplier = 0.5;
        }
        return multiplier;
    }

    private boolean checkHealth() {
        /* First, sum up and average the health over the past five days */
        int overallHealth = 0;
        for (Integer healthPerDay: healthStatus) {
            overallHealth += healthPerDay;
        }
        overallHealth /= healthStatus.size();

        /* If the person consistently has an average of 25 or more, they die.  The method returns true */
        int cutoff = 25;
        if (overallHealth > cutoff) {
            return DEAD;
        }

        /* The person can also die of old age.  Their chance is linearly related to their age.  At 200, they die for sure. */
        Random rand = new Random();
        if (rand.nextInt(200) < age) {
            return DEAD;
        }
        return ALIVE;
    }

    /* Decides if the person wants to make a baby */
    public boolean babyMakingTime() {
        Random rand = new Random();
        if (!hasChild && personality.getContentment() > 8 && rand.nextDouble() > .7) {
            return true;
        }
        return false;
    }


    public int getMoneyImportance()
    {
        //Just setting up and wondering how to make this accurate yet with enough variability between individuals
        int money = this.getMoney();
        int ambition = personality.getAmbition();
        int skill = personality.getSkill();
        int contentment = personality.getContentment();
        //TODO: DETERMINE HOW PERSONALITY PARAMETERS RELATE TO THE MONEY IMPORTANCE. ERASE THIS WHEN DONE
        return 1;
    }

    /* Returns an array with a person's needs. */
    public int[] getNeeds()
    {
        int[] attributes ={foodNeed,shelterNeed,funNeed};
        return attributes;
    }

    /* Resets a person's needs to the desired attributes. */
    public void setNeeds(int[] needs)
    {
        foodNeed = needs[0];
        shelterNeed = needs[1];
        funNeed = needs[2];
    }

    public void getFired() {
        workplace = null;
    }

    /* Getters and setters */

    public Personality getPersonality() {
        //** Pretty much self explanatory *//
        return this.personality;
    }



    public void setPersonality(Personality personality) {
        this.personality = personality;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public int getChildAge() {
        return childAge;
    }

    public void setChildAge(int childAge) {
        this.childAge = childAge;
    }

    public boolean hasWork() {
        return workplace != null;
    }

    public int getStateTimeLock() {
        return stateTimeLock;
    }

    public void setStateTimeLock(int stateTimeLock) {
        this.stateTimeLock = stateTimeLock;
    }
    public Boolean isUnlocked()
    {
        return (this.getStateTimeLock() == 0);
    }
    @Override
    public String toString() {
    	return new StringBuilder()
    		.append("State: " + state + "\n")
    		.append("Money: " + money + "\n")
    		.append("Food Need: " + foodNeed + "\n")
    		.append("Shelter Need: " + shelterNeed + "\n")
    		.append("Fun Need: " + funNeed)
    		.toString();
    }
}