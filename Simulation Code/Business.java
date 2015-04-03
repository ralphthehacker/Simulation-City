import java.util.Random;

/**
 * This class represents a business
 * 
 * @author Lawrence Moore
 *
 */
public class Business extends MapConstituent {

	/* In addition to the variables of MapConstituent, we have several additional attributes that describe a business.
	Note that work quality is on a 1-10 scale, and pay and networth are in the thousands
	Something not to forget is how this influences the contentment of a person*/
	private int workQuality, pay, numEmployees, netWorth;


	/* the variable to store the type of company, which is a public enum */
	private WorkType type;

	/* Constructor in which the attributes are randomely generated */
	public Business(Position pos) {
		/* Initialize age to zero and the position to the one passed in */
		age = 0;
		this.pos = pos;

		/* Call the calculateBasicNeedsScore() method to establish the rest of the map constituent attributes */
		calculateBasicNeedsScore();

		/* Generate a random work type from the options in the enum */
		type = WorkType.values()[randInt(WorkType.values)];  //just some enum foolery

		Random rand = new Random();

		//remember that work quality is out of 10
		workQuality = rand.nextInt(10) + 1;

		/* pay is based on work quality and type of job. 
		   Pay ranges from 20 to 270 thousand.  5 and 20 below are just adjustement factos*/
		pay = workQuality * rand.nextInt(5) * type.ordinal() + 20;

		/* Start with anywhere from 100 to 200 employees */
		numEmployees = rand.nextInt(100) + 100;

	}

	/*TO DO: model how business expand/contract over time based on conceptual model */
	public void timeElapse() {}
	public void expand() {}
	public void contract() {}

	/*TO DO: implement fire, hire, and other business success related functions */
	public void hire() {}
	public void fire() {}
	public void calculateProductivity() {}
	public void decideFuture() {}

	public void calculateBasicNeedsScore() {
		/* Businesses don't provide shelter, fun, or food (they do pay you though)*/
		shelterScore = 0;
		funScore = 0;
		foodScore = 0;
	}
}