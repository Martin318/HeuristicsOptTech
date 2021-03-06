package at.ac.tuwien.ac.heuoptws15;

import java.util.List;

/**
 * Created by Martin on 23.11.2016.
 */
public class GeneticAlgorithm {

    protected  SelectionOperator selectionOp;
    protected MutationOperator mutationOp;
    protected RecombinationOperator recombinationOp;
    protected int generations;
    protected List<KPMPSolution> initialPop;

    public GeneticAlgorithm(int generations, SelectionOperator s, MutationOperator m, RecombinationOperator r, List<KPMPSolution> initialPopulation){
        this.selectionOp = s;
        this.mutationOp = m;
        this.recombinationOp = r;
        this.generations = generations;
        this.initialPop = initialPopulation;

    }

    /**
     * Computes the maximally possible number of crossings for a given edge list size
     *
     * @param edgeSize  number of edges in a solution
     * @return integer indicating the maximal number of possible crossings     *
     */
    private static int maxCrossing(int edgeSize){
        return (edgeSize * (edgeSize - 1)) / 2;
    }


    public static int fitness(KPMPSolution sol){
        int edgeListSize = 0;

        for(int i = 0; i < sol.numPages;i++)
            edgeListSize += sol.activeEdge[i].edges.size();

        return maxCrossing(edgeListSize) - sol.crossings();
    }

    public KPMPSolution execute(){


        List<KPMPSolution> currentPopulation = initialPop;

        KPMPSolution globalBest = getBestSolution(currentPopulation);


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

        }

        return globalBest;
    }


    protected KPMPSolution getBestSolution(List<KPMPSolution> l){

        KPMPSolution best = null;
        int bestSolValue = Integer.MAX_VALUE;


        for(KPMPSolution s : l){

            if(s.crossings() < bestSolValue){
                best = s;
                bestSolValue = s.crossings();
            }

        }

        return best;

    }


}
