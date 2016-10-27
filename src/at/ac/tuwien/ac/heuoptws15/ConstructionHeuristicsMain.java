package at.ac.tuwien.ac.heuoptws15;

import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.security.auth.SolarisNumericUserPrincipal;
import sun.java2d.pipe.SolidTextRenderer;

import java.io.FileNotFoundException;
import java.io.IOException;
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
                constructionHeuristics.add(new RandomizedConstructionHeuristic(0.5));


                long start = System.currentTimeMillis();

                for(ConstructionHeuristic h : constructionHeuristics){
                    h.initialize(instance);
                }

                KPMPSolutionWriter w;



                for(ConstructionHeuristic h : constructionHeuristics){
                    System.out.println(h.getName());
           //         System.out.println("The heuristic returned the following solution(s):");
                    KPMPSolution s = h.getNextSolution();

                    int bestSolutionValue = Integer.MAX_VALUE;
                    int counter = 0;

                    while(s != null && counter < 10) {

                        counter ++;

                        int crossings = KPMPSolution.crossings(s);

                        System.out.println(crossings);
                        System.out.println("Other Crossing count: " + s.crossings2());
                        if(s.crossings2() != crossings){
                            s.crossings2();
                        }


                        System.out.println();

                        if (crossings < bestSolutionValue) {

                            bestSolutionValue = crossings;

                           // System.out.println(s);
                           // System.out.println("With following No of crossings: " + crossings);
                            w = new KPMPSolutionWriter(instance.getK());
                            s.insertIntoWriter(w);
                            try {
                                w.write(args[0] + "_solution");
                            } catch (IOException e) {
                                System.out.println("Failed to  write file: " + e);
                            }
                        }

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
