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
	private Person[] population;

	/*TO DO: generate different aspects of the map randomely */
	public Map() {
		residences = new Residence[STARTING_POPULATION];
		businesses = new Business[STARTING_POPULATION/10];
		population = new Person[STARTING_POPULATION];
		
		/*Creates list of random residences */
		for (int i = 0; i < residences.length; i++) {
			residences[i] = new Residence(new Position(0, 0));
		}

		/* Creates list of random businesses */
		for (int i = 0; i < businesses.length; i++) {
			businesses[i] = new Business(new Position(0, 0));
		}

		/* Creates the individual population */
		for (int i = 0; i < population.length; i++) {
			population[i] = Person.createRandomPerson(residences[i], businesses);
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
		
		// Every hour, update persons
		for (Person p : population) {
			p.update(time);
		}
	}
	
	/**
	 * 
	 * @param nPeople The number of people to print the stats for. Use -1 to
	 * print the statuses of the entire population.
	 */
	public void printPeopleStats(int nPeople) {
		if (nPeople == -1) {
			nPeople = population.length;
		} else if (nPeople > population.length) {
			throw new RuntimeException("ERROR: Population size is " +
					population.length + " < " + nPeople);
		}
		
		for (int i = 0; i < nPeople; i++) {
			System.out.println("Person " + i);
			printPersonStats(population[i]);
			System.out.println();
		}
	}
	
	private void printPersonStats(Person p) {
		System.out.println("State: " + p.getState());
		System.out.println("Money: " + p.getMoney());
		System.out.println("Food Need: " + p.getNeeds()[0]);
		System.out.println("Shelter Need: " + p.getNeeds()[1]);
		System.out.println("Fun Need: " + p.getNeeds()[2]);
	}
}