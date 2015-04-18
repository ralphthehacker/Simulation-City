import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Created by ralphblanes on 4/9/15.
 * A static class that can be used to do vector arithmetic and maybe do some of the math for our algorithms in the future
 */
public class LinearAlgebraModule {

    //Takes in a vector and normalizes it so that the sum of all elements in a vector adds up to one
    // Where to use: Making the probabilities add to one, making attribute information relevant
    public static ArrayList normalizeVector(ArrayList<Double> vector)
    {
        //TODO: Finish this function. Consider putting all of those vector functions in an object later so that all classes can use it
        Double magnitude = getVectorMagnitude(vector);
        ArrayList<Double> newList =  new ArrayList<Double>();
        for (Double d :vector)
        {
            Double newVal = 100*d / magnitude;
            if(newVal <= 0.0)
            {
                newVal = 0.0; // If the weighing results in a negative value than that state will never be selected
            }
            newList.add(LinearAlgebraModule.round(newVal, 4));// Rounding to two decimal places
        }
//        System.out.println("Normalizing");
//        System.out.println(newList);
        return newList;

    }


    //Calculates the dotproduct of two vectors and returns a probability
    public static Double dotProductProbability(ArrayList<Integer> attributes, ArrayList<Double> coefficients) {
        //Making sure the number of attributes and coefficients
        if (attributes.size() != coefficients.size()) {
            throw new InputMismatchException("The two vectors are not the same length");
        }

        ArrayList<Double> resultVector = new ArrayList<Double>(); //Making an vector to store the result and normalize it so it is a
        // number between 0 and 1
        Double dotValue = 0.0;// Counter


        //Getting the elements at the same dimension and adding it to a new vector
        for (int i = 0; i < attributes.size(); i++)
        {
            resultVector.add(attributes.get(i) * coefficients.get(i));
        }


        //Calculating the same value and storing it in dotValue
        for (int i = 0; i < attributes.size(); i++)
        {
            dotValue += resultVector.get(i);
        }

        return dotValue;
    }

    // This calculates the magnitude of a vector
    public static Double getVectorMagnitude(ArrayList<Double> vector)
    {
        Double sum = 0.0;
        for(Double n : vector)
        {
            sum = n*n + sum;
        }
        return Math.sqrt(sum);

    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    public static void printVector(ArrayList attributeVector,String s)
    {
        System.out.println(s + " is " + attributeVector);
    }
}
