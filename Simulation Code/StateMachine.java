import java.awt.*;
import java.util.*;
import java.util.Map;

/**
 * This class represents the different states of an individual in the map
 * A personality consists of a person's skill, ambition, contentment, and preferred type of work
 * @author Lawrence Moore
 *
 */

public class StateMachine {



	public static State getNextState(Person person) {

		// TODO: write out state machine with transitions based on probability
        //TODO: comment this more throughly later

		State currentState = person.getState();//Current person's state
        HashMap<State,Double> possibleStates = determinePossibleStates(currentState,person);// Determines the probabilities of the next states
        State nextState = getBestState(possibleStates);// Picks a state
        return nextState;


    }


    //*
    // @param: state, the person's current state
    // returns: A dictionary containing the states you can transition to from the current state with the respective
    // probabilities
    // *//


    public static HashMap<State,Double> determinePossibleStates(State state, Person person)
    {
        // The simplest way to do this is by making if statements to represent the possible decisions
        // Every state can loop back to itself or transition to a new state. Since every person can only be in one
        // state at a time we can instantiate an original dictionary and append the transitions to it
        if(state == null || person.getState().equals(state) == false || person == null)
        {
            //This is a sanity check. It makes sure we are passing the right parameters and that we have coherent values

            if(state == null) {System.out.println("A null state is being passed");}
            if(person.getState().equals(state) == false ){System.out.println("A different person is being compared with this state");}
            if(person == null){System.out.println("A null person is being passed");}
            //Could have done this in a more elegant way. This works good enough so why?
            throw new ExceptionInInitializerError();
        }

        HashMap<State,Double> stateDictionary = new HashMap<State, Double>();

        //We will now check the current state, put all transitions in the dictionary and then calculate the probability of
        // transition. FOR MORE INFO ON STATE TRANSITIONS PLEASE CHECK THE PAPER
        if(state == State.SLEEP)
        {
            //The initial probabilities don't really matter since they will be changed in the Transition function
            stateDictionary.put(State.SLEEP,1.0); // The current state also need to be added in i.e. One can sleep straight for 6 hours
            stateDictionary.put(State.BREAKFAST_HOME,1.0);
            stateDictionary.put(State.BREAKFAST_OUT,1.0);


        } else if(state == State.BREAKFAST_HOME){

            stateDictionary.put(State.WORK,1.0);

        } else if(state == State.BREAKFAST_OUT){

            stateDictionary.put(State.WORK,1.0);

        } else if(state == State.WORK){

            stateDictionary.put(State.WORK,1.0);
            stateDictionary.put(State.DINNER_OUT,1.0);
            stateDictionary.put(State.SHOP,1.0);
            stateDictionary.put(State.DINNER_HOME,1.0);

        } else if(state == State.DINNER_OUT){

            stateDictionary.put(State.SLEEP,1.0);

        } else if(state == State.SHOP){

            stateDictionary.put(State.SLEEP,1.0);
            stateDictionary.put(State.DINNER_OUT,1.0);
            stateDictionary.put(State.DINNER_HOME,1.0);


        } else if(state == State.DINNER_HOME){
            stateDictionary.put(State.SLEEP,1.0);
        }
        //It is guaranteed that the dictionary will have at least one transition

        HashMap<State,Double> finalMap = calculateTransitionFunction(stateDictionary,person);
        return finalMap;

    }



    public static HashMap<State,Double> calculateTransitionFunction(HashMap<State,Double> stateDictionary, Person person)
    {
        if(null == stateDictionary)
        {
            System.out.println("No states were passed to the transition function");
            throw new ExceptionInInitializerError();
        }
        if (stateDictionary.size() == 1){
            return stateDictionary;} // If there's only one state transition, return that with 100% chance
        HashMap<State,Double> newStateDictionary = new HashMap<State, Double>();

        //**TODO: Make the math model after lunch.
        // Possible bugs: Probability > 1, Stuck at the same state, going to bed at 12PM,
        // sleeping/working 1 hour, Working forever,Inhuman schedule
        //
        // **//
        return newStateDictionary;
    }

    //*
    // Gets the state with the highest probability in the dictionary
    // *
    public static State getBestState(HashMap<State,Double> stateDictionary)
    {
        //How to: 100 bins are created. For every key, a number of bins is filled based on the probability of the State
        // times 100. Then, the bins are randomly sampled to determine the result

        int pointer = 0; // this will allow us to know where to start filling the bins
        State[] bins = new State[100];
        for(Map.Entry<State,Double> entry : stateDictionary.entrySet()) {
            int number_of_bins = entry.getValue().intValue()*100; //Getting the number of bins to be filled
            for(int j = pointer; j < number_of_bins;j++) //Adding a state to the bins
            {
                bins[j] = entry.getKey(); // Placing the current state in the bin
            }
            pointer += number_of_bins; // Moving the pointer forward

        }
        int randomSample = new Random().nextInt(100); //Random selection
        return bins[randomSample]; //Returns the selected state
    }
    class impossibleProbabilityException extends Exception
    {

        public impossibleProbabilityException(String message,State curState,Person curPerson) {
            super("The probability of a variable has exceeded 100% for " + curPerson.toString() + " at state " + curState.toString());
        }
    }
}