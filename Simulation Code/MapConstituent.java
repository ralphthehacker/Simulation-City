/**
 * This abstract class represents the constituents of the map
 * 
 * @author Lawrence Moore
 *
 */
public abstract class MapConstituent {

	/* First, we establish all the necessary variable to represent a compenent of a map */
	int age;
	Position pos;

	/* These scores are on a 1 to 10 scale */
	int foodScore, shelterScore, funScore;

	/* These are the methods that elements of the will have */
	public abstract void timeElapse();
	public abstract void expand();
	public abstract void contract();
	public abstract void calculateBasicNeedsScore();
}