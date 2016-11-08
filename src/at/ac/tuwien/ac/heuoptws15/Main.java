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

            ArrayList<ConstructionHeuristic> constructionHeuristics = new ArrayList<>();

//            constructionHeuristics.add(new DeterministicConstructionHeuristic());
//            constructionHeuristics.add(new RandomizedConstructionHeuristic(0.5));
            constructionHeuristics.add(new RandomConstructionHeuristic());

            KPMPSolution bestSolGlobal = null;
            int bestSolutionValueGlobal = Integer.MAX_VALUE;
            String bestSolname = "";


            for (ConstructionHeuristic h : constructionHeuristics){
                h.initialize(instance);

                KPMPSolution bestSolConstruction = null;
                int bestSolutionValueConstruction = Integer.MAX_VALUE;


               System.out.println(h.getName());
               KPMPSolution s = h.getNextSolution();

               int counter = 0;

               long start = System.currentTimeMillis();
               do{
                   counter++;
                   int crossings = s.crossings();
                   if (crossings < bestSolutionValueConstruction) {
                        bestSolutionValueConstruction = crossings;
                        bestSolConstruction = s;
                   }

                   s = h.getNextSolution();
               } while (h.getName() == "Randomized Construction Heuristic" &&
                       (counter < 10 || System.currentTimeMillis() - start < 60000) );

               // BEST initial solution found!

               System.out.println("--------------------");
               System.out.println("Best initial solution");
               System.out.println(bestSolutionValueConstruction);
               System.out.println("--------------------");

               ArrayList<StepFunction> stepFunctions = new ArrayList<>();

               stepFunctions.add(new FirstImprovementStepFunction(bestSolConstruction,bestSolutionValueConstruction));
//               stepFunctions.add(new BestImprovementStepFunction(bestSolConstruction,bestSolutionValueConstruction));
//               stepFunctions.add(new RandomStepFunction());

               for( StepFunction step : stepFunctions){

                   KPMPSolution bestSolStep = bestSolConstruction;
                   int bestSolutionValueStep = bestSolutionValueConstruction;

                   ArrayList<Neighbourhood> neighbourhoods = new ArrayList<>();

                   neighbourhoods.add(new NodeNeighbourSwapNeighbourhood());
                   neighbourhoods.add(new OneNodeSwapNeighbourhood());
                       neighbourhoods.add(new OneEdgeFlipNeighbourhood());

                   for (Neighbourhood hood : neighbourhoods){
                       String currentName = h.getName() + step.getName() + hood.getName();

                       KPMPSolution bestSolHood = bestSolStep;
                       int bestSolutionValueHood = bestSolutionValueStep;


                       if(bestSolutionValueHood < bestSolutionValueGlobal){
                           bestSolGlobal = bestSolHood;
                           bestSolutionValueGlobal = bestSolutionValueHood;
                           bestSolname = currentName;
                       }

                       // STAGE 1  //

                       // CYCLE NEIGHBOURHOOD //

                       SearchConfiguration search = new SearchConfiguration();

                       search.setNeighbourhood(hood);
                       search.setStepFunction(step);

                       KPMPSolution currentSol = search.getNextSolution(bestSolHood);

                       int currentSolCrossings = currentSol.crossings();
                       start = System.currentTimeMillis();

                       while (currentSolCrossings < bestSolutionValueHood  && System.currentTimeMillis() - start < 60000) {
                           if(currentSolCrossings < bestSolutionValueGlobal){
                               bestSolGlobal = currentSol;
                               bestSolutionValueGlobal = currentSolCrossings;
                               bestSolname = currentName;
                           }

                           bestSolHood = currentSol;
                           int diff = bestSolutionValueHood - currentSolCrossings;
                           bestSolutionValueHood = currentSolCrossings;

                           System.out.println("Local Search improved solution:");
                           System.out.print(bestSolutionValueHood);
                           System.out.println( " (- " + diff + ")");

                           KPMPSolutionWriter w;
                           w = new KPMPSolutionWriter(instance.getK());
                           bestSolHood.insertIntoWriter(w);
                           try {
                               w.write(args[0] + currentName);
                           } catch (IOException e) {
                               System.out.println("Failed to  write file: " + e);
                           }


                           currentSol = search.getNextSolution(bestSolConstruction);
                           currentSolCrossings = currentSol.crossings();
                       }


                       System.out.println( currentName + " result: " + bestSolutionValueHood);
                   }
               }

            }


            KPMPSolutionWriter w;
            w = new KPMPSolutionWriter(instance.getK());
            bestSolGlobal.insertIntoWriter(w);
            try {
                w.write(args[0] + "_Global_" + bestSolname);
            } catch (IOException e) {
                System.out.println("Failed to  write file: " + e);
            }

    }

}
