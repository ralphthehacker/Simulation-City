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

	private int price, quantity;


	/* Constructor in which the attributes are randomely generated */
	public GroceryStore (Position pos, Map map) {
		/* Initialize age to zero and the position to the one passed in */
		age = 0;
		this.pos = pos;

		/* A residence has a price and quality on a scale of 1 to 10 */
		price = new Random().nextInt(5) + 1;

		/* A grocer store initially has enough food for 1000 people */
		quantity = 1000;
	}

	/*TO DO: model how groceries expand/contract over time based on conceptual model */
	public boolean update() {
		return false;
	}
	public void expand() {}
	public void contract() {}
	
	/* Calculates what the basic needs scores are for a business */
	public void calculateBasicNeedsScore() {
		/* Businesses don't provide shelter, fun, or food (they do pay you though) */
		shelterScore = 0;
		funScore = 0;
		foodScore = 0;
	}


    public String printStats()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("Position : " + super.pos) + "\n")
                .append("Company type: " + this.getWorkType() + "\n")
                .append("Location : " + super.getPos() + "\n")
                .append("Net Worth: " +
                        this.getNetWorth() + "\n")
                .append("Working hours: " + this.getMinimumWorkingHours() + "\n")
                .append("Quality: " + this.getWorkQuality() + "\n")
                .append("History: " + this.getProductHistory() + "\n").append("Number of Employees "
                + this.getEmployeeList().size() + "\n");
        int a = 0;
        return sb.toString();

    }

}