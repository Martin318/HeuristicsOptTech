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




        // BEST initial solution found!




        ArrayList<Neighbourhood> n1 = new ArrayList<Neighbourhood>();
        ArrayList<Neighbourhood> n2 = new ArrayList<Neighbourhood>();

        n1.add(new EdgeNeighbourNeighbourhood(true));
        n1.add(new EdgeNeighbourNeighbourhood(false));
        n1.add(new NodeNeighbourSwapNeighbourhood());

        for(int n = 1; n < 21 ; n++) {
            n2.add(new MNFlipEdgeSwapNodeNeighbourhood(n,n));
            // n2.add(new OneNodeSwapNeighbourhood());
        }

        GVNS g = new GVNS(n1,n2);

        int lastCrossings = Integer.MAX_VALUE;
        int noImprovementCounter = 0;
        int maxOuterLoopIterations = 10;

        KPMPSolution globalBest = null;

        int currentIterations = 0;
        ArrayList<Integer> results = new ArrayList<>();

        do {



            System.out.println("Starting initial solution search! " + currentIterations + " of "  + maxOuterLoopIterations );


            KPMPSolution bestSolution = initialSolution(instance);

            if(globalBest == null){
                globalBest = bestSolution;
            }

            int bestSolutionValue = bestSolution.crossings();

            System.out.println("Best initial solution: " + bestSolutionValue);

            System.out.println("Global is " + globalBest.crossings());


            System.out.println("Starting GVNS search!");

            noImprovementCounter = 0;

            while (noImprovementCounter < 20) {
                bestSolution = g.search(bestSolution);
                System.out.println("GVNS Result: " + bestSolution.crossings());

                if(bestSolution.crossings() < globalBest.crossings()){
                    globalBest = bestSolution;
                    System.out.println("Global best improved to " + bestSolution.crossings());

                }

                if (bestSolution.crossings() == lastCrossings) {
                    noImprovementCounter++;
                    //   System.out.println("No improvement!");
                } else {
                    noImprovementCounter = 0;
                    //  System.out.println("!!!!!Improvement!!!!!");

                }

                lastCrossings = bestSolution.crossings();


            }

            System.out.println("Ending GVNS search!");


            results.add(bestSolution.crossings());
            currentIterations++;
        }while (currentIterations < maxOuterLoopIterations);
        //while(System.currentTimeMillis() < System.currentTimeMillis() +1 && globalBest.crossings() > 0);
        double mean = 0.0D;
        double stddev = 0.0D;

        System.out.println("Results: ");
        for(Integer i : results){
            mean += i;
            System.out.print(i + " " );
        }

        mean = mean / results.size();

        for(Integer i : results)
            stddev += (i - mean)*(i - mean); // ( x_i - mean)^2

        stddev = stddev * (1 / results.size() -1);

        stddev = Math.sqrt(Math.abs(stddev));

        System.out.println(" Global best " + globalBest.crossings());
        System.out.println(" Mean is " + mean);
        System.out.println(" Sample standard deviation " + stddev);




        KPMPSolutionWriter w;
        w = new KPMPSolutionWriter(instance.getK());
        globalBest.insertIntoWriter(w);
        try {
            w.write(args[0] + "_GVNS_Solution");
        } catch (IOException e) {
            System.out.println("Failed to  write file: " + e);
        }

    }


    public static KPMPSolution initialSolution(KPMPInstance instance){

        ArrayList<ConstructionHeuristic> constructionHeuristics = new ArrayList<>();

        constructionHeuristics.add(new DeterministicConstructionHeuristic());
        constructionHeuristics.add(new RandomizedConstructionHeuristic(0.5));
        // constructionHeuristics.add(new RandomConstructionHeuristic());

        KPMPSolution bestSolution = null;
        int bestSolutionValue = Integer.MAX_VALUE;

        for (ConstructionHeuristic h : constructionHeuristics) {
            h.initialize(instance);


     //      System.out.println(h.getName());
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

        return bestSolution;
    }

}
