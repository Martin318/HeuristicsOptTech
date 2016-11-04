package at.ac.tuwien.ac.heuoptws15;

/**
 * Created by Martin on 31.10.2016.
 */
public class FuzzyFirstImprovementStepFunction extends StepFunction {

    int bestSolutionValue;
    KPMPSolution bestSol;
    float fuzzyFactor;

    public FuzzyFirstImprovementStepFunction(KPMPSolution initial, int crossings, float fuzzyFactor){
        bestSol = initial;
        bestSolutionValue = crossings;
        this.fuzzyFactor = fuzzyFactor;
    }


    public boolean acceptSolution(KPMPSolution solution, int crossings){

        if(crossings < bestSolutionValue || ((0.0 + bestSolutionValue ) / (0.00001 + crossings) > fuzzyFactor)){
            bestSolutionValue = crossings;
            bestSol = solution;

            return true;
        }

        return false;

    }

    public KPMPSolution bestSolution(){
        return bestSol;
    }

    @Override
    public String getName() {
        return "FuzzyImprovement .. or is it?";
    }
}
