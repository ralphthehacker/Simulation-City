import java.util.Random;

/**
 * This class represents an individual agent in the world
 * Agents have three basic needs: food, shelter, and fun
 * An agent also a personality
 * If an agent has a child, they must spend more on food each month until the child turns 18
 * An agent has a current state, which essentially means where the agent is at any give ninstance
 * @author Lawrence Moore
 *
 */

public class Person {

	/* These needs are on a 1 to 10 scale, with 10 being the most dire need */
	private int foodNeed, shelterNeed, funNeed;
    private int money;

	/* People start out 18 */
	private int age = 18;

	private Personality personality;
	private boolean hasChild;
	private int childAge;
	private State state;

	public Person() {
		personality = new Personality();
		hasChild = false;
		childAge = 0;
		money = 100;
		state = State.SLEEP;
	}


	// Update the person's needs and/or state
	public void update(int time) {

		if (state.equals(State.SLEEP)) {
			shelterNeed = Math.min(shelterNeed + 1, 10);
			foodNeed = Math.max(foodNeed - 1, 1);
		}
		
		state = StateMachine.getNextState(this);
	}


    public int[] getNeeds()
    //** Returns an array with a person's needs. //
    {
        int[] attributes ={foodNeed,shelterNeed,funNeed};
        return attributes;
    }
    public void setNeeds(int[] needs)
    //** Resets a person's needs to the desired attributes.//
    {
        foodNeed = needs[0];
        shelterNeed = needs[1];
        funNeed = needs[2];
    }

    //*
    // Getters and setters
    //
    // *//

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
}