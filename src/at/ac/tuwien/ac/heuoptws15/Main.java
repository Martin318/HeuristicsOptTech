package at.ac.tuwien.ac.heuoptws15;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Martin on 14.10.2016.
 */
public class Main {

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


            ArrayList<ConstructionHeuristic> constructionHeuristics = new ArrayList<ConstructionHeuristic>();

            constructionHeuristics.add(new DeterministicConstructionHeuristic());
            constructionHeuristics.add(new RandomizedConstructionHeuristic(0.5));
            //constructionHeuristics.add(new RandomConstructionHeuristic());



            for (ConstructionHeuristic h : constructionHeuristics) {
                h.initialize(instance);
            }

            KPMPSolution bestSol = null;
            int bestSolutionValue = Integer.MAX_VALUE;

            for (ConstructionHeuristic h : constructionHeuristics) {
                long start = System.currentTimeMillis();
                System.out.println(h.getName());
                KPMPSolution s = h.getNextSolution();

                int counter = 0;

                while (s != null && (counter < 10 || System.currentTimeMillis() - start < 5000)) {
                    counter++;

                    int crossings = s.crossings();

                    System.out.println(h.getName() + " " + crossings);


                    if (crossings < bestSolutionValue) {
                        bestSolutionValue = crossings;
                        bestSol = s;

                    }

                    s = h.getNextSolution();
                }

            }

            // BEST initial solution found!

            System.out.println("--------------------");
            System.out.println("Best initial solution");
            System.out.println(bestSolutionValue + " " + KPMPSolution.actualCrossings(bestSol));
            System.out.println("--------------------");


            // STAGE 1  //

            // CYCLE NEIGHBOURHOOD //

            SearchConfiguration search = new SearchConfiguration();

            search.setNeighbourhood(new NodeNeighbourSwapNeighbourhood());
            search.setStepFunction(new FirstImprovementStepFunction(bestSol, bestSolutionValue));

            KPMPSolution currentSol = search.getNextSolution(bestSol);

            int currentSolCrossings = currentSol.crossings();
            System.out.println(" first search result " + currentSolCrossings + ", actual value" + KPMPSolution.actualCrossings(currentSol));

            while (currentSolCrossings < bestSolutionValue) {

                bestSol = currentSol;
                bestSolutionValue = currentSolCrossings;

                System.out.println("Local Search improved solution:");
                System.out.println(bestSolutionValue);

                KPMPSolutionWriter w;
                w = new KPMPSolutionWriter(instance.getK());
                bestSol.insertIntoWriter(w);
                try {
                    w.write(args[0] + "_solution");
                } catch (IOException e) {
                    System.out.println("Failed to  write file: " + e);
                }

                currentSol = search.getNextSolution(bestSol);
                currentSolCrossings = currentSol.crossings();

                System.out.println("search result " + currentSolCrossings + ", actual value" + KPMPSolution.actualCrossings(currentSol));
                if (currentSolCrossings != KPMPSolution.actualCrossings(currentSol)){
                    KPMPSolution.actualCrossings(currentSol);
                    System.out.println("damn!");
                }

            }






    }

}
