package at.ac.tuwien.ac.heuoptws15;

/**
 * Created by Martin on 30.10.2016.
 */
public abstract class StepFunction {


    public abstract boolean acceptSolution(KPMPSolution s, int crossings);

    public abstract void initialize(KPMPSolution s, int crossings);


    public abstract KPMPSolution bestSolution();

    public abstract String getName();
}
