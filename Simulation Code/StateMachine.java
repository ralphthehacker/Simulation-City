import sun.jvm.hotspot.jdi.DoubleValueImpl;

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
    public static int time;

    public static State getNextState(Person person, int time) {
        person = person;
        StateMachine.time = time;

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

        HashMap<State,Double> newStateDictionary = new HashMap<State, Double>();// Creating the new map with updated probabilities


        // We will now create a vector of attributes [N0,N2...N(N-1)] that corresponds to the values every single attribute
        // After that, we will pass this vector of attributes to the calculateProbabilityWithAttributes function.
        // What this function does is simple. It basically takes the dot product of our current attribute values
        // with every state's coefficient vector. In a more simple language, this basically weighs our person's priorities
        // and other extraneous factors to see how much it matters for an agent to stay on that state. For example, the time
        // period and his ambition/ amount of money matter a lot to determine whether an agent will transition to go/leave work.
        // At the same time, this agent may be craving for food or for entertainment, which is also a very important adversarial
        // factor to determine the probability of transition


        //Initial attributes considered.:
        //A0 = The time of day in which a state transition is called. (int from 0-23)
        //A1 = The amount of money a person currently has weighed by the personality factors
        //A2 = A person's need for food
        //A3 = A person's need for Shelter
        //A4 = A person's need for entertainment
        //A5 = A person's ambition. I know that this factor is already weighed in A1, but ambitious people work more expecting future gains
        //      Think of A5 as that 1 hour of sleep that was replaced by a working hour simply because a person wants success



        ArrayList<Integer> attributeVector = new ArrayList<Integer>();//Our vector
        attributeVector.add(StateMachine.time); //A0
        attributeVector.add(person.getMoneyImportance()); //A1
        attributeVector.add(person.getNeeds()[0]); //A2
        attributeVector.add(person.getNeeds()[1]); //A3
        attributeVector.add(person.getNeeds()[2]); //A4
        attributeVector.add(person.getPersonality().getAmbition()); //A5

        Set<Map.Entry<State,Double>> frontier = stateDictionary.entrySet(); // The frontier of all <State,Probability> pairs

        //For every (State,Probability) pair in the frontier of all possible states
        for(Map.Entry<State,Double> pair : frontier) {

            // Getting the current pair's state and probability
            State state = pair.getKey();


            //Now we pass the vector and determine the transition
            Double newProbability = calculateProbabilityWithAttributes(state,attributeVector);

            //And add it to the probability distribuition
            newStateDictionary.put(state,newProbability);



        }
        return newStateDictionary;
    }


    public static Double calculateProbabilityWithAttributes(State state, ArrayList<Integer> attributes)

    {
        //TODO: Make sure it is necessary to normalize the initial vector

        //Normalizing the original attributes
        attributes = LinearAlgebraModule.normalizeVector(attributes);

        //Get the importance coefficients for the current state
        ArrayList<Double> coefficients = getCoefficientsForState(state);

        //Get the final weighed probability
        Double probabilityValue = LinearAlgebraModule.dotProductProbability(attributes,coefficients);

        // And return this probability
        return probabilityValue;

    }

    ///This helper function determines the importance coefficient for a determined state. It's worth mentioning that this vector is normalized.
    public static ArrayList<Double> getCoefficientsForState(State state)

    //TODO: Write down all the possible attribute weights for all status. Consider adding randomness

    {
        //Initializing the vector
        ArrayList<Double> coefficients = new ArrayList<Double>();


        //Determine the coefficients in this conditional statement
        if(state == State.SLEEP)
        {


        } else if(state == State.BREAKFAST_HOME){


        } else if(state == State.BREAKFAST_OUT){


        } else if(state == State.WORK){



        } else if(state == State.DINNER_OUT){


        } else if(state == State.SHOP){



        } else {
            if (state == State.DINNER_HOME) {
            }
        }

        //Normalizing it
        ArrayList<Double> normalizedCoefficients = LinearAlgebraModule.normalizeVector(coefficients);
        return normalizedCoefficients;


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
        //TODO: CONSIDER ADDING THIS LATER TO THE CALCULATE PROBABILITIES FUNCTION TO PREVENT BUGS
        public impossibleProbabilityException(String message,State curState,Person curPerson) {
            super("The probability of a variable has exceeded 100% for " + curPerson.toString() + " at state " + curState.toString());
        }
    }
}