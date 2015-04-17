import java.util.ArrayList;

/**
 * Created by ralphblanes on 4/16/15.
 *
 * This class is responsible for handling negotiations between businesses and people
 * It allows people to look for jobs in other available businesses and also allows businesses to scout candidates from near
 *
 *
 */
public class GlassdoorDotCom
{

    private  Map map;
    private  ArrayList<Person> prospectiveCandidates;
    private  Business[] businesses;

    public  GlassdoorDotCom(Map input_map)
    {
        map = input_map;
        businesses = map.getBusinesses();
        determineProspectiveCandidates();//Determines who is interested in new jobs

    }



    //Todo: Update list of prospective candidates and make businesses employ their strategies
    public ArrayList<Person> scout(Business corporation, int number_of_people)// This method will be called from map.update
    {
        ArrayList<Person> my_list = new ArrayList<Person>();
        return null;


    }


    //TODO: Finish
    public Business getAJob(Person p)
    {
        Business bestBusiness = determineBestFittingJob(Person p);
        return null;
    }

    //TODO: Finish
    private Business determineBestFittingJob()
    {
        //First get all the business who could hire this person
        // Edge cases
        return null;
    }


    //Determine people who are interested in new jobs
    private  void determineProspectiveCandidates()
    {
        //Check people in the population and see who wants to look for jobs
        for (Person dude : this.map.getPopulation())
        {
            if (! dude.getState().equals(State.SLEEP))//A person can look for jobs as long as she ain't sleeping
            {
                if (dude.getPersonality().getContentment() <= 6) //This metric counts if a person is unemployed or if she wants out
                {
                    prospectiveCandidates.add(dude);// Add this to the prospective candidates list
                }
            }

        }
    }



}
