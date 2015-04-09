import java.util.ArrayList;
import java.util.InputMismatchException;

/**
 * Created by ralphblanes on 4/9/15.
 * A static class that can be used to do vector arithmetic and maybe do some of the math for our algorithms in the future
 */
public class LinearAlgebraModule {

    //Takes in a vector and normalizes it so that the sum of all elements in a vector adds up to one
    // Where to use: Making the probabilities add to one, making attribute information relevant
    public static ArrayList normalizeVector(ArrayList vector)
    {
        //TODO: Finish this function. Consider putting all of those vector functions in an object later so that all classes can use it
        return new ArrayList();

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

        //Normalizing the result so we get a number from 0 to 1 as a result
        normalizeVector(resultVector);

        //Calculating the same value and storing it in dotValue
        for (int i = 0; i < attributes.size(); i++)
        {
            dotValue += resultVector.get(i);
        }

        return dotValue;
    }



}
