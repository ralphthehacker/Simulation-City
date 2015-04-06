/**
 * This class represents the overall map
 * 
 * @author Lawrence Moore
 *
 */
public class Map {
	/* A map contains a 2-D array (in row major column, like a GBA game for simplicty) of all the map consituents */
	public static final int STARTING_POPULATION = 100;
	private MapConstituent[] layout;
	private Person[] population;

	/*TO DO: generate different aspects of the map randomely */
	public Map() {
		population = new Person[STARTING_POPULATION];
		for (int i = 0; i < population.length; i++) {
			population[i] = new Person();
		}
	}

	/* Update the map with time */
	public void update() {
		//To be done when everything is connected and time is implemented
		for (Person p : population) {
			p.timeElapse();
		}
	}
}