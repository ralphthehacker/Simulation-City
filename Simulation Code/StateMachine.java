import javax.sound.sampled.Line;
import java.awt.*;
import java.util.*;
import java.util.Map;

/**
 * This class represents the different states of an individual in the map
 * A personality consists of a person's skill, ambition, contentment, and preferred type of work
 * @author Ralph Blanes
 *
 */

public class StateMachine {
    public static int time;
    public static HashMap<Person,Boolean> timeLock;

    public static State getNextState(Person person, int time) {
        //Getting current time
        StateMachine.time = time;

        if(person.isUnlocked()) //If the person is available, compute the next state. Else, iterate and update a person's
        //lock parameters
        {
            //Current person's state
            State currentState = person.getState();
            // Determines the probabilities of the next states
            HashMap<State, Double> possibleStates = determinePossibleStates(currentState, person);
            if (possibleStates.size() == 0)
            {
                int a =0;
            };
            State nextState = getBestState(possibleStates);// Picks a state based on the probability distribution
            person.setStateTimeLock(StateMachine.getLockTime(nextState,person));// And updates the timeLock
            return nextState;
        }   else
        {
            person.setStateTimeLock(person.getStateTimeLock()-1);//Decrement the lock by one
            return person.getState();//And return the current state
        }

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

            /*if(state == null) {System.out.println("A null state is being passed");}
            if(person.getState().equals(state) == false ){System.out.println("A different person is being compared with this state");}
            if(person == null){System.out.println("A null person is being passed");}*/
            //Could have done this in a more elegant way. This works good enough so why?
            throw new ExceptionInInitializerError();
        }

        HashMap<State,Double> stateDictionary = new HashMap<State, Double>();

        //We will now check the current state, put all transitions in the dictionary and then calculate the probability of
        // transition. FOR MORE INFO ON STATE TRANSITIONS PLEASE CHECK THE PAPER
        if(state == State.SLEEP)
        {
            //The initial probabilities don't really matter since they will be changed in the Transition function
            stateDictionary.put(State.BREAKFAST_HOME,1.0);
            stateDictionary.put(State.BREAKFAST_OUT,1.0);


        } else if(state == State.BREAKFAST_HOME){

            stateDictionary.put(State.WORK,1.0);

        } else if(state == State.BREAKFAST_OUT){

            stateDictionary.put(State.WORK,1.0);

        } else if(state == State.WORK){

            stateDictionary.put(State.DINNER_OUT,1.0);
            stateDictionary.put(State.SHOP,1.0);
            stateDictionary.put(State.DINNER_HOME,1.0);

        } else if(state == State.DINNER_OUT){

            stateDictionary.put(State.SLEEP,1.0);
            stateDictionary.put(State.SHOP,1.0);


        } else if(state == State.SHOP){

            stateDictionary.put(State.SLEEP,1.0);
            stateDictionary.put(State.DINNER_OUT,1.0);
            stateDictionary.put(State.DINNER_HOME,1.0);


        } else if(state == State.DINNER_HOME){
            stateDictionary.put(State.SLEEP,1.0);
            stateDictionary.put(State.SHOP,1.0);

        }
        //It is guaranteed that the dictionary will have at least one transition

        HashMap<State,Double> finalMap = calculateTransitionFunction(stateDictionary,person);
        int a = 0 ;
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
        //A2 = A person's need for food.
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

        ///TODO: Erase this after debugging

        Set<Map.Entry<State,Double>> frontier = stateDictionary.entrySet(); // The frontier of all <State,Probability> pairs

        //For every (State,Probability) pair in the frontier of all possible states
        for(Map.Entry<State,Double> pair : frontier) {
//            System.out.println("Current state being weighed is " + pair.getKey());
            // Getting the current pair's state and probability
            State state = pair.getKey();


            //Now we pass the vector and determine the transition
            Double newProbability = calculateProbabilityWithAttributes(state,attributeVector);



            //And add it to the probability distribuition
            newStateDictionary.put(state,newProbability);

        }
        //TODO comment this line if you want to see the probabilities
//        System.out.println("Before");
//        System.out.println(newStateDictionary);


        //This line removes irrelevant values from the distribution
        newStateDictionary = weighProbabilities(newStateDictionary);
//        System.out.println("The distribution is");
//        System.out.println(newStateDictionary);

        if(newStateDictionary.size() == 0)
        {
            int a = 0;
        }
//        System.out.println("After");
//        System.out.println(newStateDictionary);

        return newStateDictionary;
    }

    //This function is used to
    public static Double calculateProbabilityWithAttributes(State state, ArrayList<Integer> attributes)

    {
        //TODO: Make sure it is necessary to normalize the initial vector

        //No need to normalize the initial attributes.They are all in the same scale from 1 to 10
        //Get the importance coefficients for the current state
        ArrayList<Double> coefficients = getCoefficientsForState(state);

//        LinearAlgebraModule.printVector(coefficients,"Coefficients");

        //Get the final weighed probability

        Double probabilityValue = LinearAlgebraModule.dotProductProbability(attributes,coefficients);

        if (probabilityValue < 0)
        {
            probabilityValue = Math.pow((1/probabilityValue),2);
        } else
        {
            probabilityValue = (Math.pow(probabilityValue,2));
        }


        // And return this probability
        return probabilityValue;


    }

    ///This helper function determines the importance coefficient for a determined state. It's worth mentioning that this vector is normalized.
    // The coefficient can either be negative or positive. Positive coefficients facilitate a transition while negative coefficients make a
    // transition less likely
    public static ArrayList<Double> getCoefficientsForState(State state)

    //TODO: Write down all the possible attribute weights for all status. Consider adding randomness

    {
        //Initializing the vector
        ArrayList<Double> coefficients = new ArrayList<Double>();

        Double theta0,theta1,theta2,theta3,theta4,theta5;
        //t0 = How much the time of day in which a state transition is called affects this state transition.
        //t1 = How much a person's money importance affects this state transition
        //t2 = How much a person's need for food affects this state transition
        //t3 = How much a person's need for shelter affects this state transition
        //t4 = How much a person's need for entertainment affects this state transition
        //t5 = How much extra ambition matters in this state. Will always be either 0.1,0 or -0.1


        //Determine the coefficients in this conditional statement.They are all going to be also on a scale of 0 to 10
        if(state.equals(State.SLEEP))
        {
            //Using time to determine the weight.
            if (StateMachine.time >=23 || StateMachine.time <= 7)
                {theta0 = 25.0;}
            else
                {theta0 = 0.1;}  // Very unlikely for someone to sleep outside of the range
            theta1 = -2.5;// If a person needs more money she will sleep less
            theta2 = 0.5;//Not exactly important
            theta3 = 5.0;// The more you need shelter, the more likely you are to fall asleep
            theta4 = 0.5;//Not exactly important
            theta5 = -1.0;// More ambition, less sleep


        } else if(state.equals(State.BREAKFAST_HOME)){
            if (StateMachine.time >=6 && StateMachine.time <= 10)
                {theta0 = 4.0;}
            else
                {theta0 = 0.1;}
            theta1 = 1.0;//If a person values money, she'll want to stay home instead of going out and waste money
            theta2 = 9.0;// Most important factor
            theta3 = 0.0;// Neutral
            theta4 = -2.0;// Eating out is a novelty
            theta5 = 1.0;// Ambitious individuals don't want to waste money

        } else if(state.equals(State.BREAKFAST_OUT)){
            if (StateMachine.time >=6 && StateMachine.time <= 10)
                {theta0 = 4.0;}
            else
                {theta0 = 0.1;}
            theta1 = -1.0;//If a person values money, she'll want to stay home instead of going out and waste money
            theta2 = 9.0;// Most important factor
            theta3 = 0.0;// Neutral
            theta4 = 2.0;// Eating out is a novelty
            theta5 = -1.0;// Ambitious individuals don't want to waste money

        } else if(state.equals(State.WORK)){
            if (StateMachine.time >=9 && StateMachine.time <= 17)
                {theta0 = 25.0;}
            else
                {theta0 = 2.0;}
            theta1 = 4.0; // If you like to make money you will want to stay and work
            theta2 = -2.0; // More hunger = Less work
            theta3 = 0.0; // Doesn't matter
            theta4 = -2.0; // More bored = less work
            theta5 = 1.0; // More ambition = more work

        } else if(state.equals(State.DINNER_OUT)){
            if (StateMachine.time >=17 && StateMachine.time <= 22)
                {theta0 = 7.0;}
            else
                {theta0 = 0.1;}
            theta1 = -1.0;//If a person values money, she'll want to stay home instead of going out and waste money
            theta2 = 6.0;// Most important factor
            theta3 = 0.0;// Neutral
            theta4 = 2.0;// Eating out is a novelty
            theta5 = -1.0;// Ambitious individuals don't want to waste money
        } else if(state.equals(State.SHOP)){
            //A person can sho anytime after work
            if ((StateMachine.time >=17 && StateMachine.time <= 22))
                {theta0 = 7.0;}
            else
                {theta0 = 0.1;}
            theta1 = -3.0;// Don't want to spend money
            theta2 = 0.0;
            theta3 = 0.0;
            theta4 = 5.0;// The more entertainment you need the more likely you are to shop
            theta5 = -1.0;// Most ambitious people delay reward even though some ambitious people unwind a lot.

        } else {
            if (state.equals(State.DINNER_HOME)) {
                if (StateMachine.time >=5 && StateMachine.time <= 9){theta0 = 7.0;}else{theta0 = 0.1;}
                theta1 = 1.0;//If a person values money, she'll want to stay home instead of going out and waste money
                theta2 = 6.0;// Most important factor
                theta3 = 0.0;// Neutral
                theta4 = -2.0;// Eating out is a novelty
                theta5 = 1.0;// Ambitious individuals don't want to waste money
            } else
            {
                throw new InputMismatchException("An unknown state is being passed to the state machine");
            }
        }
        //Adding the parameters to the vector
        coefficients.add(theta0);
        coefficients.add(theta1);
        coefficients.add(theta2);
        coefficients.add(theta3);
        coefficients.add(theta4);
        coefficients.add(theta5);

        return coefficients;


    }

    //Eliminates impossible transitions(negative values) and weighs the probability of the other alternatives
    public static HashMap<State,Double> weighProbabilities(HashMap<State,Double> map)
    {
        //Preprocessing: Checking if any attribute is negative;

        int removals = 0;
//        System.out.println("New weighing");
//        System.out.println(map);
        ArrayList<State> negativeVals = new ArrayList<State>();
        for(State key : map.keySet())
        {
            State test = key;
            //If the probability is negative, remove that state from the map. It is irrelevant
            if(map.get(test) < 0.0)
            {
                removals++;

                negativeVals.add(key);//Adding the key to the set of negative valued keys
                // Just removing directly results in a concurrency error
            }
        }
        for(State key : negativeVals)//Removing the irrelevant values
        {
            map.remove(key);
        }





        HashMap<State,Double> newMap = new HashMap<State, Double>();
        ArrayList<Double> vals = new ArrayList<Double>();
        ArrayList<State> states = new ArrayList<State>();


        //Getting current values
        for(Map.Entry<State,Double> entry : map.entrySet())
        {
            states.add(entry.getKey());
            vals.add(entry.getValue());
        }

        //Weighing those values
        ArrayList<Double> newvals = LinearAlgebraModule.normalizeVector(vals);
        for(int i = 0; i < vals.size() ; i++)
        {
            newMap.put(states.get(i), newvals.get(i));
        }

        if(newMap.size()==0)
        {
            String s;
        }
//        System.out.println(newMap);
//        System.out.println("------------------------------------------------");


        return newMap;
    }


    //*
    // Gets the state with the highest probability in the dictionary
    // *
    public static State getBestState(HashMap<State,Double> stateDictionary)
    {
        //How to: 100 bins are created. For every key, a number of bins is filled based on the probability of the State
        // times 100. Then, the bins are randomly sampled to determine the result

        int pointer = 0; // this will allow us to know where to start filling the bins
        ArrayList<State> bins = new ArrayList<State>();
        for(Map.Entry<State,Double> entry : stateDictionary.entrySet())
        {
            State curState = entry.getKey();
            int number_of_bins = entry.getValue().intValue(); //Getting the number of bins to be filled
            for(int j = pointer; j < number_of_bins + pointer;j++) //Adding a state to the bins
            {
                bins.add(curState); // Placing the current state in the bin
            }
            pointer += number_of_bins; // Moving the pointer forward

        }

        int randomSample = new Random().nextInt(bins.size()); //Random selection
        return bins.get(randomSample); //Returns the selected state
    }
    public static int getLockTime(State state, Person person) {
        if(state == State.SLEEP)//Sleep at least 6 hours
        {
            //The initial probabilities don't really matter since they will be changed in the Transition function
            return 7;


        }  else if(state == State.WORK){//Work for the necessary amount of time
            return person.getWorkHours();

        }  else// Any other agent only locks you for one hour

            return 1;



    }



    class impossibleProbabilityException extends Exception
    {
        //TODO: CONSIDER ADDING THIS LATER TO THE CALCULATE PROBABILITIES FUNCTION TO PREVENT BUGS
        public impossibleProbabilityException(String message,State curState,Person curPerson) {
            super("The probability of a variable has exceeded 100% for " + curPerson.toString() + " at state " + curState.toString());
        }
    }
}