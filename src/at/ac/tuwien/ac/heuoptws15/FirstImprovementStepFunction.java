package at.ac.tuwien.ac.heuoptws15;

import java.io.FileNotFoundException;

/**
 * Created by Martin on 30.10.2016.
 */
public class FirstImprovementStepFunction extends  StepFunction {

    int bestSolutionValue;
    KPMPSolution bestSol;

    public FirstImprovementStepFunction(KPMPSolution initial, int crossings){
        bestSol = initial;
        bestSolutionValue = crossings;
    }


    public boolean acceptSolution(KPMPSolution solution, int crossings){

        if(crossings < bestSolutionValue){
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
        return "FirstImprovment";
    }
}
