package at.ac.tuwien.ac.heuoptws15;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Martin on 14.10.2016.
 */
public class ConstructionHeuristicsMain {

    public static void main(String[] args){


        // Specify file path as execution arugment

        try {
            if(args.length > 0){
                KPMPInstance instance = KPMPInstance.readInstance(args[0]);


                System.out.println("--------------------");

                System.out.println("Sucessfully read instance:");
                System.out.println("Vertices: " + instance.getNumVertices());
                System.out.println("Book Pages: " + instance.getK());
                System.out.println("Adjacency List: " + instance.getAdjacencyList());

                System.out.println("--------------------");


                ArrayList<ConstructionHeuristic> constructionHeuristics = new ArrayList<ConstructionHeuristic>();

                constructionHeuristics.add(new DeterministicConstructionHeuristic());
                constructionHeuristics.add(new RandomizedConstructionHeuristic());

                for(ConstructionHeuristic h : constructionHeuristics){
                    h.initialize(instance);
                }


                for(ConstructionHeuristic h : constructionHeuristics){
                    System.out.println(h.getName());
                    System.out.println("The heuristic returned the following solution(s):");
                    KPMPSolution s = h.getNextSolution();

                    while(s != null){
                        System.out.println(s);
                        s = h.getNextSolution();
                    }

                    System.out.println("--------------------");

                }




            }
            else{
                System.out.println("Specify file path as first execution argument.");
            }
        }
        catch (FileNotFoundException f){
            System.out.println("File not found.");
        }

    }

}
