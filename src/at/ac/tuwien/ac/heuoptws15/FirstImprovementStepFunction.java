package at.ac.tuwien.ac.heuoptws15;

import java.io.FileNotFoundException;

/**
 * Created by Martin on 30.10.2016.
 */
public class FirstImprovementStepFunction extends  StepFunction {
    boolean initialized = false;

    int bestSolutionValue;
    KPMPSolution bestSol;

    public FirstImprovementStepFunction(KPMPSolution initial, int crossings){
        bestSol = initial;
        bestSolutionValue = crossings;
        initialized = true;
    }

    public FirstImprovementStepFunction(){

    }

    public void initialize(KPMPSolution initial, int crossings){
        bestSol = initial;
        bestSolutionValue = crossings;
        initialized = true;
    }


    public boolean acceptSolution(KPMPSolution solution, int crossings){
        if(!initialized) {
           return false;
        }

        if(crossings < bestSolutionValue){
            bestSolutionValue = crossings;
            bestSol = solution;

            return true;
        }

        return false;

    }

    public KPMPSolution bestSolution(){
        if(!initialized) {
            return null;
        }

        return bestSol;
    }

    @Override
    public String getName() {
        return "FirstImprovment";
    }
}
