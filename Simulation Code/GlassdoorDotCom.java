import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

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


    //TODO: Edge case: map isn't updated
    private  Map map;
    private  ArrayList<Person> prospectiveCandidates;
    private  Business[] businesses;

    public  GlassdoorDotCom(Map input_map)
    {
        map = input_map;
        businesses = map.getBusinesses();
        //determineProspectiveCandidates();//Determines who is interested in new jobs

    }



    //Todo: Update list of prospective candidates and make businesses employ their strategies
    public ArrayList<Person> scout(Business corporation, int number_of_people)// This method will be called from map.update
    {
        ArrayList<Person> my_list = new ArrayList<Person>();
        return null;


    }


    //TODO: Test
    public Business getAJob(Person p)
    {
        // Determine what is the best job for this individual
        Business bestBusiness = determineBestFittingJob(p);
        if(bestBusiness == p.getWorkplace())
        {
            return bestBusiness;
        }

        //If a business is interested in this person, make it hire her
        if (null != bestBusiness && (!(bestBusiness.getEmployeeList().contains(p))))
        {
            System.out.println(p.getName() + " just got a job at " + bestBusiness);
            offerHire(bestBusiness, p);
        } else
        {
            System.out.println(p.getName() + " couldn't get a job");
        }


        return bestBusiness;
    }


    //*
    // This method makes businesses hire a prospect
    // *
    private void offerHire(Business company, Person person)
    {
        if (person != null)
        {
            company.hire(person);
        } else
        {
            throw new ExceptionInInitializerError("Person is non existent");
        }
    }
    //*
    // Determines the best fitting jobs for a person
    // *
    private Business determineBestFittingJob(Person p)
    {

        //First get all the business who could hire this person
        ArrayList<Business> prospects = getInterestedBusinesses(p);
        Business bestBusiness = getBestJob(prospects,p);


        return bestBusiness;
    }


    private Business getBestJob(ArrayList<Business> jobs, Person person)
    {
        //TODO: Uncomment after viz works
        //if(jobs.size() == 0){throw new InstantiationError("No companies want to hire " + person);}
        if(null == person){throw new InstantiationError(person + " does not exist. Value = null");}
        double bestScore = 0;
        Business bestJob = null;
        for(Business possibleEmployer : jobs)
        {
            //Weigh in the company's quality and pay
            double companyScore = possibleEmployer.getQuality() * possibleEmployer.getPayRate();

            //And consider if the kind of job offered interests the person
            companyScore *= (person.getPersonality().getPreferredWork() == possibleEmployer.getWorkType() ? 1 : 0.5);

                /* See if this is the best workplace found so far */
            if (companyScore > bestScore) {
                bestScore = companyScore;
                bestJob = possibleEmployer;
            }

        }

        //if(bestJob == null){throw new InternalError("T");}
        return bestJob;
    }

    //*
    //  Determine what businesses could be interested in hiring a candidate
    //
    // *
    private ArrayList<Business> getInterestedBusinesses(Person p)
    {
        ArrayList<Business> interestedBusinesses = new ArrayList<Business>();

        //Compute and return the businesses who could possibly be interested in this person
        interestedBusinesses = computeInterestOnWorker(interestedBusinesses,p);

        //Get for every business, see who would be interested

        return interestedBusinesses;
    }

    //Get all businesses and see if they want to hire our guy
    private ArrayList<Business> computeInterestOnWorker(ArrayList<Business> storeBusinessesHere, Person p)
    {
        ArrayList<Business> businesses_list = new ArrayList<Business>(Arrays.asList(businesses));
        int i = 0;
        while (i < businesses_list.size()) {
            Business business = (Business)businesses_list.get(i);

            //TODO: Edge case, check metric to compute if a company can do better with this person
            /* check if the work will hire the person */
            if (business.willHire(p))
            {
                storeBusinessesHere.add(business);
            }
            i++;
        }
        return storeBusinessesHere;

    }


    //TODO: Consider excluding since the agents already do this by themselves
    //TODO: When everything else works, allow businesses to haggle for happy workers
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
