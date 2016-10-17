package at.ac.tuwien.ac.heuoptws15;

import com.sun.security.auth.SolarisNumericUserPrincipal;
import sun.java2d.pipe.SolidTextRenderer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.concurrent.SynchronousQueue;

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


                long start = System.currentTimeMillis();

                for(ConstructionHeuristic h : constructionHeuristics){
                    h.initialize(instance);
                }


                for(ConstructionHeuristic h : constructionHeuristics){
                    System.out.println(h.getName());
                    System.out.println("The heuristic returned the following solution(s):");
                    KPMPSolution s = h.getNextSolution();

                    while(s != null){
                        System.out.println(s);
                        System.out.println("With following No of crossings: " + KPMPSolution.crossings(s));
                        s = h.getNextSolution();
                    }

                    System.out.println("--------------------");

                }


                System.out.println("Progamm finished in " + (System.currentTimeMillis() - start) + " Milliseconds." );




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
