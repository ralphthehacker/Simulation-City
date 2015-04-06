import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

/**
 * This class represents the different states of an individual in the map
 * A personality consists of a person's skill, ambition, contentment, and preffered type of work
 * @author Lawrence Moore
 *
 */

public class StateMachine {

    // The two dictionaries responsible for keeping track of all the members of this simulation
    private Hashtable<Person,State> peopleStates;
    private Hashtable<MapConstituent,State> mapStates;
	public static State getNextState(State currentState, Person person) {

		// TO DO: write out state machine with transitions based on probability
		return State.SLEEP;
	}

    public StateMachine(ArrayList<Person> allPeople, ArrayList<MapConstituents> members, Map map)
    {

    }

}