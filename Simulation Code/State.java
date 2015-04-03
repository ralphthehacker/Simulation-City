import java.util.Random;

/**
 * This class represents the different states of an individual in the map
 * A personality consists of a person's skill, ambition, contentment, and preffered type of work
 * @author Lawrence Moore
 *
 */

public class State {

	/* As always, skill, ambition, and contentment are on a 1 to 10 scale */
	private TypesOfStates currState;


	public State() {
		/* Start off right: sleeping */
		currState = TypesOfStates.sleep;
	}

	public void updateState(Personality personality) {

		// TO DO: write out state machine with transitions based on probability
	}

}