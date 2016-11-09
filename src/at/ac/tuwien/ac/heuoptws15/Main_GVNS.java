package at.ac.tuwien.ac.heuoptws15;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Martin on 14.10.2016.
 */
public class Main_GVNS {

    public static void main(String[] args){


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

        ArrayList<ConstructionHeuristic> constructionHeuristics = new ArrayList<>();

        constructionHeuristics.add(new DeterministicConstructionHeuristic());
        constructionHeuristics.add(new RandomizedConstructionHeuristic(0.5));
        constructionHeuristics.add(new RandomConstructionHeuristic());

        KPMPSolution bestSolution = null;
        int bestSolutionValue = Integer.MAX_VALUE;

        for (ConstructionHeuristic h : constructionHeuristics) {
            h.initialize(instance);


            System.out.println(h.getName());
            KPMPSolution s = h.getNextSolution();

            int counter = 0;

            long start = System.currentTimeMillis();
            do {
                counter++;
                int crossings = s.crossings();
                if (crossings < bestSolutionValue) {
                    bestSolutionValue = crossings;
                    bestSolution = s;
                }

                s = h.getNextSolution();
            } while (h.getName() == "Randomized Construction Heuristic" &&
                    (counter < 10 || System.currentTimeMillis() - start < 5000));

        }
        // BEST initial solution found!

        System.out.println("--------------------");
        System.out.println("Best initial solution");
        System.out.println(bestSolutionValue);
        System.out.println("--------------------");


        ArrayList<Neighbourhood> n1 = new ArrayList<Neighbourhood>();
        ArrayList<Neighbourhood> n2 = new ArrayList<Neighbourhood>();

        n1.add(new OneEdgeFlipNeighbourhood());
        n1.add(new NodeNeighbourSwapNeighbourhood());

        n2.add(new OneEdgeFlipNeighbourhood());
        n2.add(new OneNodeSwapNeighbourhood());


        GVNS g = new GVNS(n1,n2);

        for(int i = 0; i< 100000; i++){
            bestSolution = g.search(bestSolution);
            System.out.println("GVNS Result: " + bestSolution.crossings());

        }

        System.out.println("GVNS Result: " + bestSolution.crossings());





        KPMPSolutionWriter w;
        w = new KPMPSolutionWriter(instance.getK());
        bestSolution.insertIntoWriter(w);
        try {
            w.write(args[0] + "_GVNS_Solution");
        } catch (IOException e) {
            System.out.println("Failed to  write file: " + e);
        }

    }

}
