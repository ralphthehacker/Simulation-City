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

//TODO: Food refills, customer history,etc.





public class GroceryStore extends MapConstituent {

	private int price, quantity;


	/* Constructor in which the attributes are randomely generated */
	public GroceryStore (Position pos, Map map) {
		/* Initialize age to zero and the position to the one passed in */
		age = 0;
		this.pos = pos;

		/* A residence has a price and quality on a scale of 1 to 10 */
		price = new Random().nextInt(9) + 1;
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

        sb.append("Position : " + this.pos + "\n")
        .append("Amount of Food : " + this.getQuantity() + "\n")
                .append("Food Price : " + this.getPrice() + "\n");
        int a = 0;
        return sb.toString();

    }

    public void handleBuyer(Person person, int amount_necessary)
    {
        if (null == person) {throw new IllegalArgumentException("Null buyer");}

        if(this.getQuantity() < amount_necessary)
        {
            this.setQuantity(1000);//Refills stock
        }
        
        //Get a person's house
        Residence house = person.getResidence();

        //Charge a person
        person.setMoney(person.getMoney() - amount_necessary * this.getPrice());

        //Refill the house
        house.addFood(amount_necessary);

        //Update store's stock
        this.setQuantity(this.getQuantity()-amount_necessary);

        System.out.println(person.getName() + " just bought " + amount_necessary +  " groceries");

        //Done
        return;


    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroceryStore)) return false;

        GroceryStore that = (GroceryStore) o;

        if (price != that.price) return false;
        if (quantity != that.quantity) return false;
        if(this.pos.equals(that.getPos())) {return false;}

        return true;
    }

    @Override
    public int hashCode() {
        int result = price;
        result = 31 * result + quantity;
        return result;
    }
}