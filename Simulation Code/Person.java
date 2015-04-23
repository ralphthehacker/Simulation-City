import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
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
    public static final int HEALTH_DAYS_TO_TRACK = 24;
    public static final boolean DEAD = true;
    public static final boolean ALIVE = false;
    /* These needs are on a 1 to 10 scale, with 10 being the most dire need */
    private int foodNeed, shelterNeed, funNeed;
    private int money;
    private int work_hours;//TODO: add this up into the contentment equation
    private int stateTimeLock; // This variable represents the number of clock cycles that a certain agent is locked to after entering it.

    private RandomNameGenerator rg;
    private String name;
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

    public static Person createRandomPerson(Residence residence, Business[] workplace, Map map) throws IOException {
        Random random = new Random();

        /* Instantiate the person */
        Person p = new Person(residence, workplace, map);

        /* Generate a random amount of money */
        p.money = (int) (300 * random.nextDouble());

        return p;
    }

    /* The constructor of the Person class; takes in residence and workplac */

    public Person(Residence residence, Business[] workplaces, Map map) throws IOException {
        personality = new Personality();
        hasChild = false;
        childAge = 0;
        money = 100;
        amountOfFood = 3;
        state = State.SLEEP;
        this.stateTimeLock = StateMachine.getLockTime(State.SLEEP,this);//A person starts free
        this.residence = residence;//Setting a person to own a house
        this.residence.setOwner(this);//TODO:Make this more readable later
        position = residence.getPosition();
        this.map = map;
        //TODO: SWITCH THIS TO SYLLABLES WHEN PUSHING
        //rg = new RandomNameGenerator("/Users/ralphblanes/Documents/PROJECTS/City_Sim/final_proj/cx-4230-final-project/Simulation Code/syllables");
        rg = new RandomNameGenerator("syllables");

        //Creates a person's name
        this.name = this.rg.compose(new Random().nextInt(2)+2);

        foodNeed = 1;
        shelterNeed = 1;
        funNeed = 1;

        /* find place of work by iterating through all workplaces.
        Ultimately chooses based on amount of money and type of work */
        this.setWorkplace(this.getMap().getGlassdoor().getAJob(this));// Makes this person look for a job among the available jobs
        if(workplace != null)
        {
            this.setWorkHours(this.getWorkplace().getMinimumWorkingHours());
        }
        //TODO: Edge case = Perpetually Unemployed guy
    }


    // Update the person's needs and/or state
    // Returns Person.DEAD if person dies. Otherwise,
    // returns Person.ALIVE
    //TODO: I don't really like to use this method to check death.I will write some proper method later
    public boolean update(int time) throws IOException {

        /* Increase age by one day.  Handle babies*/
        handleReproduction(time);

        // Handle the long time heath of this person
        handleHealthChanges();


        // Slowly decrement all needs.
        foodNeed = Math.min(foodNeed + 1, 10);
        shelterNeed = Math.min(shelterNeed + 1, 10);
        funNeed = Math.min(funNeed + 1, 10);

        //TODO: Uncomment this to make individuals look for jobs and encapsulate it onto handleStateUpdate() when it works
        //TODO: This is bugging, handle duplicates


        //Determines a person's current state and updates her attributes depending on the state
        handleStateUpdates();

        this.updateContentment();

        //Pass current person to the state machine to determine the next state
        this.state = StateMachine.getNextState(this,time);

        return checkHealth();
    }

    // Makes a person browse prospective jobs on glassdoor
    public void lookForJobs()
    {
        Business newJob = this.getMap().getGlassdoor().getAJob(this);

        //If this person got a better job, quit the past job
        if(newJob != this.getWorkplace() && newJob != null && this.getWorkplace() != null)
        {
            this.getWorkplace().leaveCompany(this);
            System.out.println(this.getName() + " has left his job");
        }
        this.setWorkplace(newJob);
    }

    //Updates a person's attributes based on her state
    public void handleStateUpdates()
    {
        if(this.isLookingForJobs())
            if (!this.getState().equals(State.SLEEP))
            {
                //If a person is unemployed or unhappy with her job, she can look for a new job
                if(this.isLookingForJobs())
                {
                    this.lookForJobs();
                }
            }


        if (state.equals(State.SLEEP))
        {
            shelterNeed = Math.max(shelterNeed - 2, 0);
            funNeed = Math.max(funNeed - 1, 0); // Used to cancel increment in person.update()
        }

        else if (state.equals(State.BREAKFAST_HOME))
        {
            //If a house has food, eat it
            if (residence.hasFood())
            {
            	residence.useFood(1);
            	foodNeed = Math.max(foodNeed - 6, 0);
            }  else //Else, buy more
            {
                //Buy more groceries
                handleGroceryShopping();
                //Eat if you had money to buy them
                if(residence.hasFood())
                {
                    residence.useFood(1);
                    foodNeed = Math.max(foodNeed - 6, 0);
                }

            }

        }

        else if (state.equals(State.BREAKFAST_OUT))
        {
            boolean status = handleEatingOut(EntertainmentType.restaurant);
            if (!status) {
                System.out.println("Person " + name + "couldn't find a restaurant to shop");
            }

        }

        else if (state.equals(State.WORK))
        {
            if (this.isEmployed()) {
                System.out.println(this.name + " got paid");
                money += workplace.getPayRate();
            }
        }
        else if (state.equals(State.DINNER_OUT))
        {
//            money -= 10;
//            foodNeed = Math.max(foodNeed - 6,  0);
//            funNeed = Math.max(funNeed - 7, 0);
            boolean status = handleEatingOut(EntertainmentType.restaurant);
            if (!status) {
                System.out.println("Person " + name + "couldn't find a restaurant to shop");
            }

        }
        else if (state.equals(State.SHOP))
        {
            /* see if any shops of that type are available and go there.  If status is true, they found somewhere to go */
            boolean status = handleEntertainmentShopping();
            if (!status) {
                System.out.println("Person " + name + "couldn't find a place to shop");
            }

        }
        else if(state.equals(State.DINNER_HOME))
        {
            if (residence.hasFood()) {
            	residence.useFood(1);
            	foodNeed = Math.max(foodNeed - 6, 0);
            }else
            {
                //Buy more groceries
                handleGroceryShopping();
                //Eat if you had money to buy them
                if(residence.hasFood())
                {
                residence.useFood(1);
                foodNeed = Math.max(foodNeed - 6, 0);
                }
            }
        }


    }


    //*
    // Determines the best possible grocery store for this person. It takes into account the cost and
    // quality. It also picks the first grocery store that fulfills all requirements
    // */
    public void handleGroceryShopping()
    {
        //Get all groceries
        ArrayList<GroceryStore> stores = this.getMap().getGroceries();


        //Initializing attributes to determine best store
        GroceryStore bestStore = null;
        int bestPrice = 20000;

        //Determine how many groceries a person needs to buy
        int amountNecessary = 10 - this.getResidence().getFoodSupply();

        for(GroceryStore store : stores)
        {
            //If the grocery store has enough food
            if(store.getQuantity() >= amountNecessary && store.getPrice() < bestPrice)
            {
                bestPrice = store.getPrice();
                bestStore = store;
            }
        }

        //Check if a store was selected
        if(bestStore == null){throw new ExceptionInInitializerError("No stores are available");}

        //Check if a person has money to buy groceries
        if(amountNecessary*bestPrice <= this.getMoney())
        {
            bestStore.handleBuyer(this,amountNecessary);
        } else
        {
            //If a person can't even buy one item, don't purchase anything. She needs to make more money to eat
            if(bestPrice > this.getMoney())
            {
                return;

            }   else //Buy as many items as you can.
            {
                bestStore.handleBuyer(this, (this.getMoney()/bestPrice));
            }
        }
    }


    // TODO: remember to call update on all the entertainment places once every 24 hours
    /* Handles shopping.  Returns true if the person finds a place to shop at. */
    private boolean handleEntertainmentShopping() {
        ArrayList<Entertainment> funPlaces = this.getMap().getEntertainmentPlaces();

        /* A store is affordable if its cost is one half the total money a person has */
        ArrayList<Entertainment> affordablePlaces = new ArrayList<Entertainment>();
        for (Entertainment place: funPlaces) {
            if (place.getPrice() < money / 2) {
                affordablePlaces.add(place);
            }
        }

        /* Pick a place and shop there */
        if (affordablePlaces.size() > 0) {
            return pickEntertainmentPlace(affordablePlaces);
        }
        return false;
    }

    private boolean handleEatingOut(EntertainmentType type) {
        ArrayList<Entertainment> funPlaces = this.getMap().getEntertainmentPlaces();

        /* A store is affordable if its cost is one half the total money a person has */
        ArrayList<Entertainment> affordablePlaces = new ArrayList<Entertainment>();
        for (Entertainment place : funPlaces) {
            if (place.getEntertainmentType() == type && place.getPrice() < money / 2) {
                affordablePlaces.add(place);
            }
        }

         /* Pick a place and shop there */
        if (affordablePlaces.size() > 0) {
            return pickEntertainmentPlace(affordablePlaces);
        }
        return false;
    }

    private boolean pickEntertainmentPlace(ArrayList<Entertainment> affordablePlaces) {
        Random rand = new Random();
        Entertainment placeToShop = affordablePlaces.get(rand.nextInt(affordablePlaces.size()));

            /* Make sure we can visit the place based on number of people who have already bought stuff there.
            * Aka, a game store has only 100 games.  If 100 people have already shopped there in the last 24 hours,
            * they have nothing left */
        int counter = 0;
        while (!placeToShop.canVisit() && counter < affordablePlaces.size()) {
            placeToShop = affordablePlaces.get(counter);
            counter++;
        }

            /* Visit the place assuming we can shop there */
        if (placeToShop.canVisit()) {
            placeToShop.visit();
                /* This is the format of the basic needs array: {foodScore, funScore, shelterScore} */
            int[] basicNeedsScore = placeToShop.getBasicNeedsScores();
            foodNeed = Math.max(foodNeed - basicNeedsScore[0]/2, 0);
            funNeed = Math.max(funNeed - basicNeedsScore[1]/2, 0);
            shelterNeed = Math.max(shelterNeed - basicNeedsScore[2]/2, 0);
            money -= placeToShop.getPrice();
            keepNeedsOn1to10Scale();
            return true;
        }
        return false;
    }

    /* keeps values on a 1 to 10 scale */
    private void keepNeedsOn1to10Scale() {
        if (foodNeed > 10)
            foodNeed = 10;
        if (funNeed > 10)
            funNeed = 10;
        if (shelterNeed > 10)
            shelterNeed = 10;
    }

    public void handleHealthChanges()
    {
        if (healthStatus.size() < HEALTH_DAYS_TO_TRACK) {
            //Add the basic needs status to the Beginning of the list
            healthStatus.add(0, healthScore());
        } else {
            //get rid of the oldest score
            healthStatus.remove(HEALTH_DAYS_TO_TRACK - 1);
            //Add newest to the front
            healthStatus.add(0, healthScore());
        }
    }


    //*
    // Takes in a time and creates new people. Function also responsilbe for aging children
    // *
    public void handleReproduction(int time) throws IOException {
        if (time == 0) {
            age++;
            if (babyMakingTime()) {
                hasChild = true;
                childAge = 1;
            }

            if (hasChild && childAge >= 18) {
                map.addPerson();
                hasChild = false;
            } else if (hasChild) {
                childAge++;
            }
        }

    }

    /* Calculate the overall need of the person, weighting dire needs more */
    private int healthScore() {
        double overallScore = 0;
        /* Overly complicated method to calculate needs.  I commented it out in case we want to reuse it in some way */
//        double foodMultiplier, funMultiplier, shelterMultiplier;
//
//        /* Calculate Multipliers based on necessity */
//        foodMultiplier = setMultiplier(foodNeed);
//        funMultiplier = setMultiplier(funNeed);
//        shelterMultiplier = setMultiplier(shelterNeed);
//
//        /* Calculate the overall score, weighted by how bad their needs */
//        overallScore = ((foodNeed * foodMultiplier + funNeed * funMultiplier + shelterNeed*shelterMultiplier));
//        overallScore /= (foodMultiplier + funMultiplier + shelterMultiplier);
//
//        /* the score is on a scale of 1 to 10, where a lower score means a healthier person*/
//        return (int) overallScore;
        return foodNeed;
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

    //*
    // This method keeps track of the health of the people on the grid
    // *
    private boolean checkHealth() {
        /* First, sum up and average the health over the past five days */
        if (healthStatus.size() >= HEALTH_DAYS_TO_TRACK) {
            int overallHealth = 0;
            for (Integer healthPerDay : healthStatus) {
                overallHealth += healthPerDay;
            }
            overallHealth /= healthStatus.size();

            /* If the person consistently has an average of 8 or more, they die.  The method returns true */
            int cutoff = 8;
            if (overallHealth >= cutoff) {
                return DEAD;
            }

            /* The person can also die of old age.  Their chance is linearly related to their age. 10000 is just a factor to use */
            Random rand = new Random();
            if (rand.nextInt(10000) < age) {
                return DEAD;
            }
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
        return (ambition);
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


    public boolean isLookingForJobs()
    {

        //If a person is unemployed then she's looking for jobs
        if(!isEmployed())
        {
            return true;
        }
        else // Else, she'll only seek jobs if she isn't content
        {
            if(this.getPersonality().getContentment() > 5)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }

    //TODO: Put this on the update method
    private void updateContentment() {
        double jobFactor;
        double homeFactor;
        double payFactor = 0;
        if (!isEmployed())
        {
            jobFactor = 0;
        } else {
            payFactor = getPayScale(workplace.getPayRate());
            jobFactor = (workplace.getQuality() + payFactor) * (personality.getPreferredWork() == workplace.getWorkType() ? 1 : 0.5);
        }
        /* work quality is on a 1 to 10 scale. The pay factor is on a 1 to 10 scale */


        /* Scale job factor properly (it ranges from 2 to 20) */
        jobFactor = jobFactor/2;
        if (jobFactor < 1)
                jobFactor = 1;

        /* Get the quality of the home */
        homeFactor = residence.getQuality();

        /* Combine the two (ranges from 1 to 100 */
        double overallFactor = jobFactor * homeFactor;

        /* Now consider ambition by dividing the combined score by the ambition */
        overallFactor /= personality.getAmbition();

        /* Make sure it doesn't exceed 10 */
        if (overallFactor > 10)
            overallFactor = 10;

        personality.setContentment((int) overallFactor);

    }

    // A person is looking for a job if she is unemployed or not content with her current situation
    private boolean isLookingForAJob()
    {
        return (!this.isEmployed()) || this.getPersonality().getContentment() <= 5;
    }
    private double getPayScale(int pay) {
        double scale;
        /* Pay is anywhere from 20 (thousand) onward */
        if (pay <= 30) {
            scale = 1 + ((pay - 10.0) / 10);
        } else if (pay <= 50) {
            scale = 2 + ((pay - 30.0)/20);
        } else if (pay <= 70) {
            scale = 3 + ((pay - 50.0)/20);
        } else if (pay <= 100) {
            scale = 4 + ((pay - 70.0)/30);
        } else if (pay <= 150) {
            scale = 5 + ((pay - 100.0)/50);
        } else if (pay <= 200) {
            scale = 6 + ((pay - 150.0) / 50);
        } else if (pay <= 275) {
            scale = 7 + ((pay - 200.0) / 75);
        } else if (pay <= 350) {
            scale = 8 + ((pay - 275.0) / 75);
        } else if (pay <= 500) {
            scale = 9 + ((pay - 350.0) / 150);
        } else {
            scale = 10;
        }
        return scale;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Name: " + name + "\n")
                .append("State: " + state + "\n")
                .append("Money: " + money + "\n")
                .append("Food Need: " + foodNeed + "\n")
                .append("Shelter Need: " + shelterNeed + "\n")
                .append("Fun Need: " + funNeed + "\n")
                .append("Working at: " + workplace + "\n")
                .toString();
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
        if((this.getMoney() - money) < 0){throw new IllegalStateException("Negative money operation");}
        this.money = money;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean hasChild() {
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

    public Business getWorkplace() {
        return workplace;
    }

    public void setWorkplace(Business workplace) {
        this.workplace = workplace;
    }

    public Residence getResidence() {
        return residence;
    }

    public void setResidence(Residence residence) {
        this.residence = residence;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public int getWorkHours() {
        return work_hours;
    }

    public void setWorkHours(int work_hours) {
        this.work_hours = work_hours;
    }

    public boolean isEmployed()
    {
        return (this.getWorkplace() != null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}