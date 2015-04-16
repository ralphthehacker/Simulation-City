import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents an entertainment establishment
 * @author Lawrence Moore
 *
 */
public class Entertainment extends MapConstituent {

	private int maxGoods, price, quality, numCustomersOfDay;
	private EntertainmentType type;
    private final static int INITIAL_GOODS = 10;

    private ArrayList<Double> occupancyHistory = new ArrayList<Double>();

	/* Constructor in which the attributes are randomly generated */
	public Entertainment(Position pos, Map map) {
		Random rand = new Random();
		/* Initialize age to zero and the position to the one passed in */
		age = 0;
		this.pos = pos;

		/* Generate a random entertainment type from the options in the enum */
		type = EntertainmentType.values()[rand.nextInt(EntertainmentType.values().length)];  //just some enum foolery

		/* price is on a scale from 1 to 100 */
		price = rand.nextInt(100) + 1;

        /* Quality is inversely related to the price */
        quality = 100 - price;

		/* An establishment initially has room for 100 people */
		maxGoods = INITIAL_GOODS;
        numCustomersOfDay = 0;
	}

	/* Called every 24 hours, dictates how the restaurant grows or detracts in the past */
	public boolean update() {
        if (occupancyHistory.size() >= 30) {
            decideFuture();
            //reset the occupancy history
            occupancyHistory = new ArrayList<Double>();
        } else {
            occupancyHistory.add(numCustomersOfDay/(double)maxGoods);
        }

        numCustomersOfDay = 0;
		return false;
	}

    /* Decides whether to expand or contract or do neither*/
    private void decideFuture() {
        /* Represents the average popularity by averaging the number of customers on any given day vs the number of goods */
        double averagePopularity;
        double growthThreshold = .8;
        double contractThreshold = .4;

        averagePopularity = 0;
        for (Double ratio: occupancyHistory) {
            averagePopularity += ratio;
        }
        averagePopularity /= occupancyHistory.size();

        if (averagePopularity > growthThreshold) {
            expand();
        } else if (averagePopularity < contractThreshold) {
            contract();
        }
        return;
    }

	private void expand() {
        double maxGoodsMultiplyer = 1.1;
        maxGoods *= maxGoodsMultiplyer;

        /* Increase the quality according to some random chance */
        Random rand = new Random();
        if (rand.nextDouble() > .8) {
            quality++;
        }

    }
	private void contract() {
        double maxGoodsMultiplyer = 1.1;
        maxGoods /= maxGoodsMultiplyer;

        /* Decrease the quality according to some random chance */
        Random rand = new Random();
        if (rand.nextDouble() > .8) {
            quality--;
        }
    }

	public void calculateBasicNeedsScore() {
        /* If we change the scale */
        int scalingFactor = 1;
        double qualityMultiplyer = quality / 50.0;

		/* the food score and fun depends on the type */
		if (type == EntertainmentType.restaurant) {
            foodScore = (int) (8 * scalingFactor * qualityMultiplyer);
            funScore = (int) (3 * scalingFactor * qualityMultiplyer);
            shelterScore = 0;
        } else if (type == EntertainmentType.movie) {
            foodScore = (int) (5 * scalingFactor * qualityMultiplyer);
            funScore = (int) (6 * scalingFactor * qualityMultiplyer);
            shelterScore = 0;
        } else if (type == EntertainmentType.games) {
            foodScore = 0;
            funScore = (int) (10 * scalingFactor * qualityMultiplyer);
            shelterScore = 0;
        } else if (type == EntertainmentType.adult) {
            foodScore = 0;
            funScore = (int) (8 * scalingFactor * qualityMultiplyer);
            shelterScore = (int) (3 * scalingFactor * qualityMultiplyer);
        } else {
            System.out.println("Problem with entertainment type");
        }
	}

    /* Takes into account when people visit */
    public void visit() {
        numCustomersOfDay++;
    }

    /* Tells the state machine if a person can visit */
    public boolean canVisit() {
        return numCustomersOfDay < maxGoods;
    }


    /* Returns the basic needs score of each entertaniment establishment */
    public int[] getBasicNeedsScores() {
        int[] needsArr = {foodScore, funScore, shelterScore};
        return needsArr;
    }

    public int getPrice() { return price; };

    public int getQuality() { return quality; };


}