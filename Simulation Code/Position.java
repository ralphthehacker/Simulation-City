/**
 * This class represents an (x, y) in the city map
 * 
 * @author Lawrence Moore
 *
 */

public class Position {
	/* The local variables representing the x and y position, respectively */
	int x;
	int y;

	/* The constructor, which instantiates the position according to the paramters */
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/* A getter method which returns x */
	public int getX() {
		return x;
	}

	/* A getter method which returns y */
	public int getY() {
		return y;
	}
}