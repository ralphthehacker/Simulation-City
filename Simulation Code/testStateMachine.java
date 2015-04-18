import java.io.IOException;

/**
 * Created by ralphblanes on 4/10/15.
 */
public class testStateMachine {
    public static void main(String[] args) throws IOException {
//        Business[] businesses = {new Business(new Position(1, 1))};
//        Residence res = new Residence(new Position(2, 2));
//        //Person p = Person.createRandomPerson(res, businesses);
//        for(int i = 1; i<24;i++)
//
//        {
//
//
//            System.out.println("Initial Time");
//            System.out.println(i);
//            System.out.println("Initial state");
//            //System.out.println(p.getState());
//            //p.update(i);
//            System.out.println("Final state");
//            //System.out.println(p.getState());
//            System.out.println("");
//
//        }

        RandomNameGenerator gen = new RandomNameGenerator("/Users/ralphblanes/Documents/PROJECTS/City_Sim/final_proj/cx-4230-final-project/Simulation Code/syllables");
        System.out.print(gen.compose(3));
    }
}
