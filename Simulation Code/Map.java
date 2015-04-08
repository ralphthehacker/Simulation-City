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
		
		for (int i = 0; i < residences.length; i++) {
			residences[i] = new Residence(new Position(0, 0));
		}
		for (int i = 0; i < businesses.length; i++) {
			businesses[i] = new Business(new Position(0, 0));
		}
		for (int i = 0; i < population.length; i++) {
			population[i] = Person.createRandomPerson(residences[i], businesses[i%businesses.length]);
		}
	}

	/* Update the map with time */
	public void update(int time) {
		//To be done when everything is connected and time is implemented
		for (Person p : population) {
			p.update(time);
		}
	}
}