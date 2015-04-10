import java.util.Random;

/**
 * This class represents an individual agent in the world
 * Agents have three basic needs: food, shelter, and fun
 * An agent also a personality
 * If an agent has a child, they must spend more on food each month until the child turns 18
 * An agent has a current state, which essentially means where the agent is at any give ninstance
 * Don't forget to take into account when a person is unemployed 
 * @author Lawrence Moore
 *
 */

public class Person {

    public static int ADULT_AGE = 18;
	/* These needs are on a 1 to 10 scale, with 10 being the most dire need */
	private int foodNeed, shelterNeed, funNeed;
    private int money;

	/* People start out 18 */
	private int age = 18;

	private Personality personality;
	private boolean hasChild;
	private int childAge;
	private State state;

    private Business workplace;
    private Residence residence;

    /* Creates a random person by calling the constructor below */

    public static Person createRandomPerson(Residence residence, Business[] workplace) {
        Random random = new Random();
        Person p = new Person(residence, workplace);

        /* No need to make babies just yet */

        // p.hasChild = random.nextBoolean();

        //  /* Set the child age to 1 */
        // if (p.hasChild) {
        //     p.childAge = 1;
        // }

        p.money = (int) (300 * random.nextDouble());

        return p;
    }

    /* The constructor of the Person class; takes in residence and workplac */

	public Person(Residence residence, Business[] workplace) {
		personality = new Personality();
		hasChild = false;
		childAge = 0;
		money = 100;
		state = State.SLEEP;
        this.residence = residence;

        /* find place of work by iterating through all workplaces */
        int i = 0;

        // TODO: Do not always take the first company in the list that hires.
        // This gives a disadvantage to companies that come later in the list.
        boolean workFound = false;
        while (i < workplace.length && !workFound) {
            Business work = workplace[i];

            /* check if the work will hire the person */
            if (work.willHire(this)) {
                work.hire(this);
                this.workplace = work;
                workFound = true;
            }
            i++;
        }

	}


	// Update the person's needs and/or state
	public void update(int time) {
		
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
		//state = StateMachine.getNextState(this,time);
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

}