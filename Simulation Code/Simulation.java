import java.io.IOException;

/**
 * This class represents the simulation class, aka the main class
 * @author lsmoore
 */

public class Simulation {
	
	public static final int HOURLY = 1;
	public static final int DAILY = 1;

	public static void main(String[] args) throws IOException {
		int totalTimesteps = 24*30, time = 0;
		// Number of people in the simulation
		int nPeople = 50;
		// The interval used to print the statistics.
		int statsInterval = HOURLY;
		// Whether to report death or birth during updates
		// TODO: Implement report birth
		boolean reportDeath = true, reportBirth = false;

		// Parse command-line arguments
		// --steps timesteps to run
		// --startingtime starting time
		// --people number of people
		// --statsinterval every how many timesteps should the
		//		stats be printed
		// --reportDeath reports death if set to true
		// --reportBirth reports birth if set to true

		for (int i = 0; i < args.length; i += 2) {
			if (args[i].equals("--steps"))
            {
				totalTimesteps = Integer.parseInt(args[i+1]);

			} else if (args[i].equals("--startingtime"))
            {
				time = Integer.parseInt(args[i+1]);

			} else if (args[i].equals("--people"))
            {
				nPeople = Integer.parseInt(args[i+1]);

			} else if (args[i].equals("--statsinterval"))
            {
				statsInterval = Integer.parseInt(args[i+1]);

			} else if (args[i].equals("--reportDeath"))
            {
				reportDeath = args[i+1].equals("true");

			} else if (args[i].equals("--reportBirth")) {
				reportBirth = args[i+1].equals("true");
			}
		}

		Map map = new Map(nPeople);

		// Run the simulation
		for (int i = 0; i < totalTimesteps; i++) {
			// Print the statistics
			if (time%statsInterval == 0) {

                //Visualize the world

			}


			// Update the simulation

            map.update(time);

            //Visualize
            System.out.println("Time = " + time);
            System.out.println("---------");
            map.visualizeWorld();
            if (time == 0) {
            	map.printAverageMoney();
            }

            //Test case
            int count = 0;
            for(Person p : map.getPopulation()) {
                for (Business b : map.getBusinesses()) {
                    if(b.getEmployeeList().contains(p))
                    {
                        count++;
                    }

                }
                if(count > 1) {
                    System.out.println(p.getName() + " is working for more than one business");
                }
                count = 0;
            }


			// Print death toll, if any
			// TODO: Print death toll only on time%statsInterval
			if (reportDeath) {
				map.printDeathToll();
			}

			if (i == (24*30 - 1)) {
				map.printBusinessGrowthOverTime();
			}
			
			// Increment time. If time is midnight, set time to 0
			time = time == 23 ? 0 : time+1;
		}

		/* make the overall map array.  Also have the entertainment places, businesses, and
		houses in their own individual arrays for ease of tracking. */
	}
	
}