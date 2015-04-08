/**
 * This class represents the simulation class, aka the main class
 * @author lsmoore
 */

public class Simulation {

	public static void main(String[] args) {
		int totalTimesteps = 100, time = 0;

		// Parse command-line arguments
		// -ts timesteps
		// -st starting time
		for (int i = 0; i < args.length; i += 2) {
			if (args[i].equals("-ts")) {
				totalTimesteps = Integer.parseInt(args[i+1]);
			} else if (args[i].equals("-st")) {
				time = Integer.parseInt(args[i+1]);
			}
		}

		Map map = new Map();

		// Run the simulation
		for (int i = 0; i < totalTimesteps; i++) {
			map.update(time);
			
			// Increment time. If time is midnight, set time to 0
			time = time == 23 ? 0 : time+1;
		}

		/* make the overall map array.  Also have the entertainment places, businesses, and
		houses in their own individual arrays for ease of tracking. */
	}
}