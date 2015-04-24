 import java.io.IOException;
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
    private Person owner; //TODO: Implement feature
    private int foodSupply;

	/* Constructor in which the attributes are randomely generated */
    // Businesses are used to calculate the worth of this residence
	public Residence (Position pos, Business[] businesses) {
		/* Initialize age to zero and the position to the one passed in */
		age = 0;
		this.pos = pos;

		/* A residence has quality on a scale of 1 to 10 */
		quality = new Random().nextInt(10) + 1; 

		/* Calculate the worth of a house based on the promixity to high quality buildings 
		and the actual quality of the house. The quality is divided by five so that the house 
		house gaines value if nicer than five, and loses if less than*/
		int locationMultiplier = getLocationFactor(businesses);
		worth = (locationMultiplier * quality) / 5;

		/* assume no money is put down */
		morgageLeft = worth;
		
		foodSupply = 0;

		/* Call the calculateBasicNeedsScore() method to establish the rest of the map constituent attributes */
		calculateBasicNeedsScore();

	}

	/*TO DO: model how residences expand/contract over time based on conceptual model */
	public boolean update() {
		return false;}
	public void expand() {}
	public void contract() {}
	
	private int getLocationFactor(Business[] businesses) {
		int locationSum = 0;
		for (Business b: businesses) {
			Position anotherPos = b.getPos();
			int distance = this.pos.manhattanDistanceFrom(anotherPos);
			locationSum += b.getNetWorth() / distance;
		}
		return locationSum;
	}

	public void calculateBasicNeedsScore() {
		/* Residences don't provide fun or food */
		funScore = 0;
		foodScore = 0;

		/* the shelter is proportional to the quality and price of the home */
		shelterScore = quality * worth ;
	}

    // If a person has a child, make the child inherit the possessions
    public void handleDeath() throws IOException {
        if (owner.hasChild())
        {
            owner = new Person(owner.getResidence(),owner.getMap().getBusinesses(),owner.getMap());
            // TODO: How to handle the children? Check other bugs before figuring this out
        }  else
        {
            owner = null;// Else, the house is empty and is available for other agents to buy
        }
    }

    public Person getOwner() {
        return owner;
    }

    public int getQuality() {
        return quality;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }
    public boolean isOwned()
    {
        return (null != this.getOwner());
    }

    public Position getPosition() {
        return pos;
    }

	public int getFoodSupply() {
		return foodSupply;
	}
	
	public boolean hasFood() {
		return foodSupply != 0;
	}
	
	public void addFood(int amount) {
		foodSupply += amount;
	}
	
	public void useFood(int amount) {
		foodSupply -= amount;
	}
	
	public int getWorth() {
		return worth;
	}
	
	public int getRent() {
		return worth/12;
	}
}