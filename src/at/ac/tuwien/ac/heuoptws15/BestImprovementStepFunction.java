package at.ac.tuwien.ac.heuoptws15;

/**
 * Created by Martin on 30.10.2016.
 */
public class BestImprovementStepFunction extends StepFunction {

    int bestSolutionValue;
    KPMPSolution bestSol;

    public BestImprovementStepFunction(KPMPSolution initial, int crossings){
        bestSol = initial;
        bestSolutionValue = crossings;
    }

    public boolean acceptSolution(KPMPSolution solution, int crossings){

        if(crossings < bestSolutionValue){
            bestSolutionValue = crossings;
            bestSol = solution;

        }

        return false;

    }


    public KPMPSolution bestSolution(){
        return bestSol;
    }
}
