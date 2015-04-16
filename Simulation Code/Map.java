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
	public static final int STARTING_POPULATION = 100;
    public static final int SIZE_OF_GRID = STARTING_POPULATION * 5;
	private Residence[] residences;
	private Business[] businesses;
	private MapConstituent[] layout;
	private ArrayList<Person> population;
	// People who died in the current timestep. Updated on update().
	ArrayList<Person> deadPeople = new ArrayList<Person>();

    /* A hashmap of the positions inhabited.  True if the position is inhabited; false otherwise */
    HashMap<Position, Boolean> positionsInhabited = new HashMap<Position, Boolean>();

	/*TO DO: generate different aspects of the map randomely */
	public Map() {
		residences = new Residence[STARTING_POPULATION];
		businesses = new Business[STARTING_POPULATION/10];
		population = new ArrayList<Person>(STARTING_POPULATION);
		deadPeople = new ArrayList<Person>();

		/*Creates list of random residences */
		for (int i = 0; i < STARTING_POPULATION; i++) {
			residences[i] = new Residence(generateRandomPosition());
		}

		/* Creates list of random businesses */
		for (int i = 0; i < STARTING_POPULATION/10; i++) {
			businesses[i] = new Business(generateRandomPosition());
		}

		/* Creates the individual population */
		for (int i = 0; i < STARTING_POPULATION; i++) {
			population.add(i,Person.createRandomPerson(residences[i], businesses, this));
		}
	}

    public Map(int numPopulation) {
        residences = new Residence[numPopulation];
        businesses = new Business[numPopulation/10];
        population = new ArrayList<Person>(numPopulation);

		/*Creates list of random residences */
        for (int i = 0; i < numPopulation; i++) {
            residences[i] = new Residence(generateRandomPosition());
        }

		/* Creates list of random businesses */
        for (int i = 0; i < numPopulation/10; i++) {
            businesses[i] = new Business(generateRandomPosition());
        }

		/* Creates the individual population */
        for (int i = 0; i < numPopulation; i++) {
            population.add(i,Person.createRandomPerson(residences[i], businesses, this));
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
	public void update(int time) {
		// At the beginning of every day, update businesses
		if (time == 0) {
			updateBusinesses();
		}
		
		// Every hour, update the population
		updatePeople(time);
	}
	
	private void updateBusinesses() {
		for (Business b : businesses) {
			b.update();
		}
	}
	
	private void updatePeople(int time) {

		ArrayList<Person> deadPeople = new ArrayList<Person>();
		deadPeople.clear();

		// Every hour, update persons
        for (int i = 0; i < population.size(); i++) {
        	boolean status = population.get(i).update(time);

            if (status == Person.DEAD) {
                deadPeople.add(population.get(i));
            }
        }
        
        // Remove the dead people from the population
        for (int i = 0; i < deadPeople.size(); i++) {
        	population.remove(deadPeople.get(i));
        }
	}

    /* Allows a person to add children to the map */
    public void addPerson() {
        /* Incomplete, in that it always adds just the first house to the person.  update when residences is done */
        population.add(Person.createRandomPerson(residences[1],businesses, this));
    }

    public int getNumberOfPeople() {
        return population.size();
    }
	
	/**
	 * 
	 * @param nPeople The number of people to print the stats for. Use -1 to
	 * print the statuses of the entire population.
	 */
	public void printPeopleStats(int nPeople) {
		if (nPeople == -1) {
			nPeople = population.size();
		} else if (nPeople > population.size()) {
			throw new RuntimeException("ERROR: Population size is " +
					population.size() + " < " + nPeople);
		}
		
		for (int i = 0; i < nPeople; i++) {
			System.out.println("Person " + i);
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
}