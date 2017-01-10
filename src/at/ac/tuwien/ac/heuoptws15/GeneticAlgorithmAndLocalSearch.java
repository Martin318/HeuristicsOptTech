package at.ac.tuwien.ac.heuoptws15;

import java.util.List;

/**
 * Created by cem on 21/12/16.
 */
public class GeneticAlgorithmAndLocalSearch extends GeneticAlgorithm {
    private SearchConfiguration search = new SearchConfiguration();
    private StepFunction step;
    private Neighbourhood hood;


    public GeneticAlgorithmAndLocalSearch(int generations, SelectionOperator s, MutationOperator m,
                                          RecombinationOperator r, List<KPMPSolution> initialPopulation,
                                          Neighbourhood hood, StepFunction step) {
        super(generations, s, m, r, initialPopulation);
        this.step = step;
        this.hood = hood;
    }


    public KPMPSolution execute(){
        List<KPMPSolution> currentPopulation = initialPop;
        KPMPSolution globalBest = getBestSolution(currentPopulation);


            long start = System.currentTimeMillis();
            try {
                for(int i = 0; i< generations; i++){
                    System.out.println( "Generation " + i + " start!");

                    List<KPMPSolution> selection = selectionOp.select(currentPopulation);
                    List<KPMPSolution> recombined = recombinationOp.recombine(selection);
                    List<KPMPSolution> mutated = mutationOp.mutate(recombined);

                    selection.addAll(mutated);

                    currentPopulation = selection; // Generation Replacement

                    KPMPSolution currentBest = getBestSolution(currentPopulation);

                    System.out.println("Best of this population: " + currentBest.crossings());

                    if(currentBest != null && currentBest.crossings() < globalBest.crossings()){
                        globalBest = currentBest;
                        System.out.println("Global best improved to " + globalBest.crossings());
                    }

                    if ( System.currentTimeMillis() - start >= 450000 )
                        return globalBest;
                }

            } catch (OutOfMemoryError e){
                currentPopulation = null;
                System.out.println("Memory limit reached, finishing early");
            }

            int currentCrossings = globalBest.crossings();

            step.initialize(globalBest, currentCrossings);
            search.setStepFunction(step);
            search.setNeighbourhood(hood);

            KPMPSolution next = search.getNextSolution(globalBest);

            while (next != null && next.crossings() < currentCrossings){
                System.out.println("New crossing count" + next.crossings());
                currentCrossings = next.crossings();
                globalBest = next;

                next = search.getNextSolution(globalBest);
                if ( System.currentTimeMillis() - start >= 900000 )
                    return globalBest;
            }

        return globalBest;
    }
}