package at.ac.tuwien.ac.heuoptws15;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 23.11.2016.
 */
public class Main_GA {


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
        System.out.println("Adjacency List: " + instance.getAdjacencyList());
        System.out.println("--------------------");



        GeneticAlgorithm g = new GeneticAlgorithm(1000, new SimpleCrossingSelection(),new SimpleMutate(),new SwapRecombination(), initialPopulation(instance,10000));


        KPMPSolution globalBest = g.execute();

        System.out.println("////// GLOBAL BEST ////////  " + globalBest.crossings() );



        KPMPSolutionWriter w;
        w = new KPMPSolutionWriter(instance.getK());
        globalBest.insertIntoWriter(w);
        try {
            w.write(args[0] + "_GA_Solution");
        } catch (IOException e) {
            System.out.println("Failed to  write file: " + e);
        }


    }


    static List<KPMPSolution> initialPopulation(KPMPInstance instance, int size){


        List<KPMPSolution> initialPopulation = new ArrayList<KPMPSolution>();
        ConstructionHeuristic h = new RandomConstructionHeuristic();

        h.initialize(instance);

        for(int i = 0; i< size; i++){

            initialPopulation.add(h.getNextSolution());

            System.out.println(i + " crossings: " + initialPopulation.get(i).crossings());

        }

        return initialPopulation;
    }
}
