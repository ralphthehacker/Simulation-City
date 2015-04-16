/**
 * This class represents the simulation class, aka the main class
 * @author lsmoore
 */

public class Simulation {
	
	public static final int HOURLY = 1;
	public static final int DAILY = 1;

	public static void main(String[] args) {
		int totalTimesteps = 100, time = 0;
		// Number of people the simulation should print stats for
		int nPeopleStats = -1;
		// The interval used to print the statistics.
		int statsInterval = HOURLY;
		// Whether to report death or birth during updates
		// TODO: Implement report birth
		boolean reportDeath = true, reportBirth = false;

		// Parse command-line arguments
		// --steps timesteps to run
		// --startingtime starting time
		// --personstats number of people to print stats for.
		//		-1 for entire population
		// --statsinterval every how many timesteps should the
		//		stats be printed
		// --reportDeath reports death if set to true
		// --reportBirth reports birth if set to true
		for (int i = 0; i < args.length; i += 2) {
			if (args[i].equals("--steps")) {
				totalTimesteps = Integer.parseInt(args[i+1]);
			} else if (args[i].equals("--startingtime")) {
				time = Integer.parseInt(args[i+1]);
			} else if (args[i].equals("--personstats")) {
				nPeopleStats = Integer.parseInt(args[i+1]);
			} else if (args[i].equals("--statsinterval")) {
				statsInterval = Integer.parseInt(args[i+1]);
			} else if (args[i].equals("--reportDeath")) {
				reportDeath = args[i+1].equals("true");
			} else if (args[i].equals("--reportBirth")) {
				reportBirth = args[i+1].equals("true");
			}
		}

		Map map = new Map(nPeopleStats);

		// Run the simulation
		for (int i = 0; i < totalTimesteps; i++) {
			// Update the simulation
			map.update(time);
			nPeopleStats = map.getNumberOfPeople();

			// Print the statistics
			if (time%statsInterval == 0) {
				System.out.println("Time = " + time);
				System.out.println("---------");
                if (nPeopleStats != 0) {
                    map.printPeopleStats(nPeopleStats);
                }
			}
			
			// Update the simulation
			map.update(time);
			
			// Print death toll, if any
			// TODO: Print death toll only on time%statsInterval
			if (reportDeath) {
				map.printDeathToll();
			}
			
			// Increment time. If time is midnight, set time to 0
			time = time == 23 ? 0 : time+1;
		}

		/* make the overall map array.  Also have the entertainment places, businesses, and
		houses in their own individual arrays for ease of tracking. */
	}
	
}