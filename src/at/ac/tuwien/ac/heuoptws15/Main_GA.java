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



        GeneticAlgorithm g = new GeneticAlgorithm(100, null,null,null, initialPopulation());


        KPMPSolution globalBest = g.execute();



        KPMPSolutionWriter w;
        w = new KPMPSolutionWriter(instance.getK());
        globalBest.insertIntoWriter(w);
        try {
            w.write(args[0] + "_GVNS_Solution");
        } catch (IOException e) {
            System.out.println("Failed to  write file: " + e);
        }


    }


    static List<KPMPSolution> initialPopulation(){

        // TODO: Use some combination of heuristics to generate a population.

        return null;
    }
}
