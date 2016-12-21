package at.ac.tuwien.ac.heuoptws15;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cem on 21.12.2016.
 */
public class Main_Hybrid {


    public static void main(String[] args) {


        KPMPInstance instance = null;
        // Specify file path as execution arugment
        try {
            if (args.length > 0)
                instance = KPMPInstance.readInstance(args[0]);
            else {
                System.out.println("Specify file path as first execution argument.");
                System.exit(1);
            }

        } catch (FileNotFoundException f) {
            System.out.println("File not found.");
            System.exit(1);
        }




        System.out.println("--------------------");
        System.out.println("Sucessfully read instance:");
        System.out.println("Vertices: " + instance.getNumVertices());
        System.out.println("Book Pages: " + instance.getK());
//        System.out.println("Adjacency List: " + instance.getAdjacencyList());
        System.out.println("--------------------");



        GeneticAlgorithm g = new GeneticAlgorithmAndLocalSearch(100, new SimpleCrossingSelection(),new SimpleMutate(),
                new SwapRecombination(), initialPopulation(instance,500),new NodeNeighbourSwapNeighbourhood(),new FirstImprovementStepFunction());


        KPMPSolution globalBest = g.execute();

        System.out.println("////// GLOBAL BEST ////////  " + globalBest.crossings() );



        KPMPSolutionWriter w;
        w = new KPMPSolutionWriter(instance.getK());
        globalBest.insertIntoWriter(w);
        try {
            w.write(args[0] + "_Hybrid_Solution");
        } catch (IOException e) {
            System.out.println("Failed to  write file: " + e);
        }


    }


    static List<KPMPSolution> initialPopulation(KPMPInstance instance, int size){


        List<KPMPSolution> initialPopulation = new ArrayList<KPMPSolution>();
        ConstructionHeuristic h = new RandomConstructionHeuristic();

        h.initialize(instance);

        for(int i = 0; i< size; i++){
            KPMPSolution s = h.getNextSolution();
            initialPopulation.add(s);
            initialPopulation.add(s);

//            System.out.println(i + " crossings: " + initialPopulation.get(i).crossings());

        }

        return initialPopulation;
    }
}
