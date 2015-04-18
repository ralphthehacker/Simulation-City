import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * This class represents the overall map
 * 
 * @author Lawrence Moore
 *
 */
public class Map {
	/* A map contains a 2-D array of all the map consituents */
	public static final int DEFAULT_STARTING_POPULATION = 100;
    public static final int SIZE_OF_GRID = DEFAULT_STARTING_POPULATION * 5;
	private Residence[] residences;
	private Business[] businesses;
    private GlassdoorDotCom glassdoor;
	private MapConstituent[] layout;
	private ArrayList<Person> population;
	// People who died in the current timestep. Updated on update().
	ArrayList<Person> deadPeople = new ArrayList<Person>();

    /* A hashmap of the positions inhabited.  True if the position is inhabited; false otherwise */
    HashMap<Position, Boolean> positionsInhabited = new HashMap<Position, Boolean>();

    HashMap<Person, Boolean> peopleInMap = new HashMap<Person, Boolean>();


    /*TO DO: generate different aspects of the map randomely */
	public Map() throws IOException {
        this(DEFAULT_STARTING_POPULATION);
	}

    public Map(int numPopulation) throws IOException {
        residences = new Residence[numPopulation];
        businesses = new Business[numPopulation];
        population = new ArrayList<Person>(numPopulation);


        //TODO: WTF's with that final boolean? Make people have a field called dead or alive



		/*Creates list of random residences */
        for (int i = 0; i < numPopulation; i++) {
            residences[i] = new Residence(generateRandomPosition());
        }

		/* Creates list of random businesses */
        for (int i = 0; i < businesses.length; i++) {
            businesses[i] = new Business(generateRandomPosition());
        }

        //Now instantiate the GlassDoor
        glassdoor = new GlassdoorDotCom(this);

        //TODO: Coupling error with glassdoor
		/* Creates the individual population */
        for (int i = 0; i < numPopulation; i++) {
            population.add(i,Person.createRandomPerson(residences[i], businesses, this));
        }

        //Putting people in an iterable map for printing
        for(Person person : this.population)
        {
            this.peopleInMap.put(person,true);
        }

        System.out.println("All businesses");
        System.out.println(businesses);
    }

    private Position generateRandomPosition() {
        /* To generate keep track of random positions generated */
        Random rand = new Random();
        Position randomPosition = new Position(rand.nextInt(SIZE_OF_GRID), rand.nextInt(SIZE_OF_GRID));

        /* while the position is already taken, generate new positions */
        while (positionsInhabited.keySet().contains(randomPosition) && positionsInhabited.get(randomPosition)) {
            randomPosition = new Position(rand.nextInt(SIZE_OF_GRID), rand.nextInt(SIZE_OF_GRID));
        }

        return randomPosition;
    }

	/* Update the map with time */
	public void update(int time) throws IOException {
		// At the beginning of every day, update businesses
		if (time == 0) {
			updateBusinesses();
		}
		
		// Every hour, update the population
		updatePeople(time);
        //And the glassdoor
        updateGlassDoor();

	}

	//TODO: Businesses shouldn't be static! Allow them to hire people and fire based on revenue
	private void updateBusinesses() {
		for (Business b : businesses) {
			b.update();
		}
	}


	private void updatePeople(int time) throws IOException {
		deadPeople.clear();

		// Every hour, update persons
        //TODO: Cover edge cases: Remove people who died from their jobs and possessions
        for (int i = 0; i < population.size(); i++) {
        	boolean status = population.get(i).update(time);

            if (status == Person.DEAD) {
                deadPeople.add(population.get(i));

            }
        }
        
        // Remove the dead people from the population
        handleDeath(deadPeople);
	}


    //*
    // This method is used to handle all death scenarios.It removes a person from her job and home
    // If a person has a child, the child takes over the house but only starts "living" when she reaches legal age
    // *
    public void handleDeath(ArrayList<Person> casualties) throws IOException {
        for(Person person :casualties)
        {
            if (null!=person.getWorkplace()) {
                person.getWorkplace().handleDeath(person);//Remove person from her former job
            }
            person.getResidence().handleDeath();// Remove person from her former house
            population.remove(person);//Remove a people from life

        }

    }

    public void orphanage(){}
    /* Allows a person to add children to the map */
    public void addPerson() throws IOException {
        /* Incomplete, in that it always adds just the first house to the person.  update when residences is done */
        population.add(Person.createRandomPerson(residences[1],businesses, this));
    }

    public int getNumberOfPeople() {
        return population.size();
    }


	// Print the stats of the entire population
	public void printPeopleStats() {

        System.out.println("POPULATION STATUS:");
		for (int i = 0; i < population.size(); i++) {
			System.out.println(population.get(i));
			System.out.println();
		}
	}
	
	public void printDeathToll() {
		if (deadPeople.size() > 0) {
			System.out.println(deadPeople.size() +
					(deadPeople.size() == 1 ? " person" : " people") +
					" died!");
		}
	}

    public Residence[] getResidences() {
        return residences;
    }

    public void setResidences(Residence[] residences) {
        this.residences = residences;
    }

    public Business[] getBusinesses() {
        return businesses;
    }

    public void setBusinesses(Business[] businesses) {
        this.businesses = businesses;
    }

    public ArrayList<Person> getPopulation() {
        return population;
    }

    public void setPopulation(ArrayList<Person> population) {
        this.population = population;
    }

    public GlassdoorDotCom getGlassdoor() {
        return glassdoor;
    }

    public void setGlassdoor(GlassdoorDotCom glassdoor) {
        this.glassdoor = glassdoor;
    }

    public void updateGlassDoor()
    {
        this.glassdoor = new GlassdoorDotCom(this);
    }

    public void visualizeWorld()
    {
        //Print the businesses and their status

        //Print people and their states
        for(java.util.Map.Entry<Person,Boolean> p : this.peopleInMap.entrySet())
        {
            System.out.println(p.getKey().toString());
            System.out.println();
        }
        //Why an array why just why aaaaaack
        for (int i = 0 ; i < this.businesses.length ; i++ )
        {
            //System.out.println(businesses[i].getRosterAndCompanyStats());
        }
    }
}