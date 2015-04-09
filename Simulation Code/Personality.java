import java.util.Random;

/**
 * This class represents the personality of an individual
 * A personality consists of a person's skill, ambition, contentment, and preffered type of work
 * @author Lawrence Moore
 *
 */

public class Personality {


    /* As always, skill, ambition, and contentment are on a 1 to 10 scale */
	private int skill, ambition, contentment;

	/* preferredWork is an array of types of work, where they are listed in increasing enthusasim.
	Thus, the first type listed is the least preferrable, while the last is the most */
	private WorkType[] preferredWork;

	public Personality() {
		Random rand = new Random();

		/*randomely assign skill and ambition */
		skill = rand.nextInt(10) + 1;
		ambition = rand.nextInt(10) + 1;
		
		/* Start contentment at 5 by default */
		contentment = 5;

		//TO DO: initialize the preferred work array
	}

    public int getSkill() {
        return skill;
    }

    public void setSkill(int skill) {
        this.skill = skill;
    }

    public int getAmbition() {
        return ambition;
    }

    public void setAmbition(int ambition) {
        this.ambition = ambition;
    }

    public int getContentment() {
        return contentment;
    }

    public void setContentment(int contentment) {
        this.contentment = contentment;
    }

    public WorkType[] getPreferredWork() {
        return preferredWork;
    }

    public void setPreferredWork(WorkType[] preferredWork) {
        this.preferredWork = preferredWork;
    }

}