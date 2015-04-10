import java.util.Random;

/**
 * This class represents a grocery store
 * A grocery store has a quality and price component, as well as the quantity of the produce.  
 * The first two combined dictate how much the store influences the contentment of a person. 
 * Also something to consider is how a grocery store interacts with the dynamic of the enviornment.  
 * If the store consistently runs out of food, it increases, or if it has too much,
 * it closes.  Something to consider down the line
 * 
 * @author Lawrence Moore
 *
 */
public class GroceryStore extends MapConstituent {

	private int price, quality, quantity;


	/* Constructor in which the attributes are randomely generated */
	public GroceryStore (Position pos, Map map) {
		/* Initialize age to zero and the position to the one passed in */
		age = 0;
		this.pos = pos;

		/* A residence has a price and quality on a scale of 1 to 10 */
		quality = new Random().nextInt(10) + 1;
		price = new Random().nextInt(10) + 1;

		/* A grocer store initially has enough food for 1000 people */
		quantity = 1000;
	}

	/*TO DO: model how groceries expand/contract over time based on conceptual model */
	public boolean update {}
	public void expand() {}
	public void contract() {}

	public void calculateBasicNeedsScore() {
		/* Food sotres don't provide fun or shelter */
		shelterScore = 0;
		foodScore = 0;

		/* the food score is proportional to the quality and price of the home */
		shelterScore = quality * price ;
	}

}