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

    public  void setInitialSolution(KPMPSolution s){
        this.s = s;
    }

    public  void setNeighbourhood(Neighbourhood n){
        this.n = n;
    }

    public  void setStepFunction(StepFunction f){
        this.f = f;
    }

    public  KPMPSolution getNextSolution(){

        List<KPMPSolution> neighbours = n.getNeighbours(s);

        for(KPMPSolution k: neighbours) {
            if (f.acceptSolution(k, k.crossings())) {
                return k;
            }
        }

        return f.bestSolution();




    }


}
