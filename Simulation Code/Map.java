import java.util.ArrayList;

/**
 * This class represents the overall map
 * 
 * @author Lawrence Moore
 *
 */
public class Map {
	/* A map contains a 2-D array (in row major column, like a GBA game for simplicty) of all the map consituents */
	public static final int STARTING_POPULATION = 100;
	private Residence[] residences;
	private Business[] businesses;
	private MapConstituent[] layout;
	private ArrayList<Person> population;

	/*TO DO: generate different aspects of the map randomely */
	public Map() {
		residences = new Residence[STARTING_POPULATION];
		businesses = new Business[STARTING_POPULATION/10];
		population = new ArrayList<Person>(STARTING_POPULATION);
		
		/*Creates list of random residences */
		for (int i = 0; i < residences.length; i++) {
			residences[i] = new Residence(new Position(0, 0));
		}

		/* Creates list of random businesses */
		for (int i = 0; i < businesses.length; i++) {
			businesses[i] = new Business(new Position(0, 0));
		}

		/* Creates the individual population */
		for (int i = 0; i < population.size(); i++) {
			population.add(i,Person.createRandomPerson(residences[i], businesses));
		}
	}

	/* Update the map with time */
	public void update(int time) {
		// At the beginning of every day, update businesses
		if (time == 0) {
			for (Business b : businesses) {
				b.update();
			}
		}

        // status variable to indicate if a person dies
        boolean status;
		// Every hour, update persons
		for (Person p : population) {
			status = p.update(time);

            //if the person dies, update return true
            if (status) {
                population.remove(p);
            }
		}
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
}