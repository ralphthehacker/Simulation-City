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

	//time elapse method
	public void timeElapse() {
		if (state.equals(State.SLEEP)) {
			shelterNeed = Math.min(shelterNeed + 1, 10);
			foodNeed = Math.max(foodNeed - 1, 1);
		}
	}
}