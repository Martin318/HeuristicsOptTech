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
            constructionHeuristics.add(new RandomConstructionHeuristic());



            for (ConstructionHeuristic h : constructionHeuristics){
                h.initialize(instance);

                KPMPSolution bestSol = null;
                int bestSolutionValue = Integer.MAX_VALUE;


               System.out.println(h.getName());
               KPMPSolution s = h.getNextSolution();

               int counter = 0;

               long start = System.currentTimeMillis();
               do{
                   counter++;
                   int crossings = s.crossings();
                   if (crossings < bestSolutionValue) {
                        bestSolutionValue = crossings;
                        bestSol = s;
                   }

                   s = h.getNextSolution();
               } while (h.getName() == "Randomized Construction Heuristic" &&
                       (counter < 10 || System.currentTimeMillis() - start < 5000) );

               // BEST initial solution found!

               System.out.println("--------------------");
               System.out.println("Best initial solution");
               System.out.println(bestSolutionValue + " " + KPMPSolution.actualCrossings(bestSol));
               System.out.println("--------------------");

               ArrayList<StepFunction> stepFunctions = new ArrayList<StepFunction>();

               stepFunctions.add(new FirstImprovementStepFunction(bestSol,bestSolutionValue));
               stepFunctions.add(new BestImprovementStepFunction(bestSol,bestSolutionValue));
               stepFunctions.add(new RandomStepFunction());

               for( StepFunction step : stepFunctions){

                   KPMPSolution bestSolStep = bestSol;
                   int bestSolutionValueStep = bestSolutionValue;

                   ArrayList<Neighbourhood> neighbourhoods = new ArrayList<Neighbourhood>();

                   neighbourhoods.add(new NodeNeighbourSwapNeighbourhood());
                   neighbourhoods.add(new OneNodeSwapNeighbourhood());
                   neighbourhoods.add(new OneEdgeFlipNeighbourhood());

                   for (Neighbourhood hood : neighbourhoods){

                       KPMPSolution bestSolHood = bestSolStep;
                       int bestSolutionValueHood = bestSolutionValueStep;

                       // STAGE 1  //

                       // CYCLE NEIGHBOURHOOD //

                       SearchConfiguration search = new SearchConfiguration();

                       search.setNeighbourhood(hood);
                       search.setStepFunction(step);

                       KPMPSolution currentSol = search.getNextSolution(bestSolHood);

                       int currentSolCrossings = currentSol.crossings();

                       while (currentSolCrossings < bestSolutionValueHood) {
                           bestSolHood = currentSol;
                           bestSolutionValueHood = currentSolCrossings;

//                         System.out.println("Local Search improved solution:");
//                         System.out.println(bestSolutionValue);

                           currentSol = search.getNextSolution(bestSol);
                           currentSolCrossings = currentSol.crossings();
                       }

                       String currentName = h.getName() + step.getName() + hood.getName();
                       System.out.println( currentName + " result: " + bestSolutionValueHood);

                       KPMPSolutionWriter w;
                       w = new KPMPSolutionWriter(instance.getK());
                       bestSolHood.insertIntoWriter(w);
                       try {
                           w.write(args[0] + "_" +currentName);
                       } catch (IOException e) {
                           System.out.println("Failed to  write file: " + e);
                       }

                   }

               }

            }




















    }

}
