package at.ac.tuwien.ac.heuoptws15;

import java.security.KeyManagementException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 30.10.2016.
 */
public class SearchConfiguration {

    KPMPSolution s;
    Neighbourhood n;
    StepFunction f;


    public  void setNeighbourhood(Neighbourhood n){
        this.n = n;
    }

    public  void setStepFunction(StepFunction f){
        this.f = f;
    }

    public  KPMPSolution getNextSolution(KPMPSolution initialSolution){

        n.setSolution(initialSolution);

        KPMPSolution k = n.getNextNeighbour();

        while(k != null) {

            int crossings = k.crossings();

            if (f.acceptSolution(k, crossings)) {
                System.out.println("//////////////////");
                System.out.println("Accepting neighbour with " + crossings + " crossings.");
                System.out.println("//////////////////");
                return k;
            }
            else{
                System.out.println("Not accepting neighbour with " + crossings + " crossings.");
            }
            k= n.getNextNeighbour();
        }


        return f.bestSolution();




    }


}
