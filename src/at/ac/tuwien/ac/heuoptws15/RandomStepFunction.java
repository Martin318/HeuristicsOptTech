package at.ac.tuwien.ac.heuoptws15;

/**
 * Created by cem on 04/11/16.
 */
public class RandomStepFunction extends StepFunction {

    @Override
    public boolean acceptSolution(KPMPSolution s, int crossings) {
        return false;
    }

    @Override
    public void initialize(KPMPSolution s, int crossings) {

    }

    @Override
    public KPMPSolution bestSolution() {
        return null;
    }

    @Override
    public String getName() {
        return "RandomStep";
    }
}
