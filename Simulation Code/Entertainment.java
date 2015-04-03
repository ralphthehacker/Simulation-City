import java.util.Random;

/**
 * This class represents an entertainment establishment 
 * A grocery store has three attributes: occupancy, type, and price 
 * 
 * @author Lawrence Moore
 *
 */
public class Entertainment extends MapConstituent {

	private int occupancy, price;
	private EntertainmentType type;

	/* Constructor in which the attributes are randomely generated */
	public Entertainment(Position pos, Map map) {
		/* Initialize age to zero and the position to the one passed in */
		age = 0;
		this.pos = pos;

		/* Generate a random entertainment type from the options in the enum */
		type = EntertainmentType.values()[randInt(EntertainmentType.values)];  //just some enum foolery

		/* price is on a scale from 1 to 10 */
		price = new Random().nextInt(10) + 1;

		/* An establishment initially has room for 100 people */
		occupancy = 100;
	}

	/*TO DO: model how groceries expand/contract over time based on conceptual model */
	public void timeElapse() {}
	public void expand() {}
	public void contract() {}

	public void calculateBasicNeedsScore() {
		/* entertainment doesn't provide shelter */
		shelterScore = 0;

		/* the food score and fun depends on the type */
		
		//TO DO: come up with a decent scheme to assign values
	}

}