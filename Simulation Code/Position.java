/**
 * This class represents an (x, y) in the city map
 * 
 * @author Lawrence Moore
 *
 */

public class Position {
	/* The local variables representing the x and y position, respectively */
	private int x;
	private int y;

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

	@Override
	public boolean equals(Object o) {
		if (o == null) return false;
		else if (!(o instanceof Position)) return false;
		else {
			Position p = (Position) o;
			return x == p.x && y == p.y;
		}
	}
}