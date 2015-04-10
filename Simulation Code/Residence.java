import java.util.Random;

/**
 * This class represents a resedential area
 * 
 * @author Lawrence Moore
 *
 */
public class Residence extends MapConstituent {

	/* In addition to the variables of MapConstituent, we have several additional attributes that describe a residence.
	Note that interest is a percentage value, and morgage left is in the thousands.
	Something not to forget is how this influences the contentment of a person*/
	private final int INTEREST = 8;
	private int morgageLeft, worth, quality;


	/* Constructor in which the attributes are randomely generated */
	public Residence (Position pos) {
		/* Initialize age to zero and the position to the one passed in */
		age = 0;
		this.pos = pos;

		/* A residence has quality on a scale of 1 to 10 */
		quality = new Random().nextInt(10) + 1; 

		/* Calculate the worth of a house based on the promixity to high quality buildings 
		and the actual quality of the house. The quality is divided by five so that the house 
		house gaines value if nicer than five, and loses if less than*/
		worth = (quality / 5);

		/* assume no money is put down */
		morgageLeft = worth;

		/* Call the calculateBasicNeedsScore() method to establish the rest of the map constituent attributes */
		calculateBasicNeedsScore();

	}

	/*TO DO: model how residences expand/contract over time based on conceptual model */
	public boolean update() {}
	public void expand() {}
	public void contract() {}

	public void calculateBasicNeedsScore() {
		/* Residences don't provide fun or food */
		funScore = 0;
		foodScore = 0;

		/* the shelter is proportional to the quality and price of the home */
		shelterScore = quality * worth ;
	}
}