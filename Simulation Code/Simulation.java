/**
 * This class represents the simulation class, aka the main class
 * @author lsmoore
 */

public class Simulation {

	public static final int TIMESTEPS = 100;
	public static final int

	public static void main(String[] args) {
		int totalTimesteps, startingTime;

		// Parse command-line arguments
		for (int i = 0; i < args.length; i += 2) {
			if (args[i].equals("-ts")) {
				totalTimesteps = Integer.parseInt(args[i+1]);
			} else if (args[i].equals("-st")) {
				startingTime = Integer.parseInt(args[i+1]);
			}
		}

		Map map = new Map();

		// Run the simulation
		for (int i = 0; i < totalTimesteps; i++) {
			map.update();
		}

		/* make the overall map array.  Also have the entertainment places, businesses, and
		houses in their own individual arrays for ease of tracking. */
	}
}