package at.ac.tuwien.ac.heuoptws15;

/**
 * Created by Martin on 14.10.2016.
 */
public abstract class ConstructionHeuristic {


    // Initialize the construction heuristic with the instance.
    public abstract void initialize(KPMPInstance instance);


    // Returns next constructed Solution or Null if no further Solution could be constructed.
    // Important: Only provide NEW individual solutions.
    public abstract KPMPSolution getNextSolution();


    // Returns the name of the construction heuristic
    public abstract String getName();


}
