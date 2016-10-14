package at.ac.tuwien.ac.heuoptws15;

/**
 * Created by Martin on 14.10.2016.
 */
public class RandomizedConstructionHeuristic extends ConstructionHeuristic {

    private KPMPInstance instance;


    public void initialize(KPMPInstance instance){
        this.instance = instance;
    }

    public KPMPSolution getNextSolution(){

        // TODO implement Heuristic

        return null;
    }


    public String getName(){
        return "Randomized Construction Heuristic";
    }


}
