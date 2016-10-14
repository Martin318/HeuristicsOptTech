package at.ac.tuwien.ac.heuoptws15;

import java.security.spec.ECGenParameterSpec;
import java.util.*;
import java.util.stream.*;

/**
 * Created by Martin on 14.10.2016.
 */
public class KPMPSolution {

    public Integer[] ordering;

    public Page[] pages;

    public KPMPSolution(int numVertices, int numPages){

        ordering = new Integer[numVertices];

        pages = new Page[numPages];

        for(int i = 0; i < numPages; i++){
            pages[i] = new Page();
        }

    }

    @Override
    public String toString(){
        String s = "Vertex ordering: [";

        for(int i=0; i<ordering.length; i++){
            if(i != 0) s += ", ";
            s += ordering[i];
        }

        s += "] \n";

        s += "Pages: \n";

        for(int i=0; i<pages.length; i++){
            s += "Page: " + i + " ";

            for(Edge e : pages[i].edges){

                s +=  e;
                s += ", ";
            }


            s += "\n";
        }

        return s;
    }

    /**
     * To check if the indices are smaller in the order,
     * otherwise changing spine order would not matter
     *
     * @param a must be smaller numVertex (not checked)
     * @param b must be smaller numVertex (not checked)
     * @return  an exception if a or b are too large
     */
    private boolean smallerInOrdering(int a, int b){
        return this.ordering[a] < this.ordering[b];
    }

    /**
     * A bit slow since looking for edges itself takes linear time.
     * If possible in constant time (use of actual arrays?) this would run in linear time
     *
     * @param solution A possible solution
     * @return Number of crossings found
     */
    public static int crossings(KPMPSolution solution){
        if (solution == null ||  solution.ordering.length <= 3) //no possible crossings if less than four vertices, by def
            return 0;
        int crossingFound = 0;

        for(Page page : solution.pages){
            for(Edge e1 : page.edges){
                List<Edge> crossed  = page.edges.stream()
                        .filter(e2 -> solution.smallerInOrdering(e1.start,e2.start) &&
                                      solution.smallerInOrdering(e2.start,e1.end)   &&
                                      solution.smallerInOrdering(e1.end,e2.end ) )
                        .collect(Collectors.toList());
                crossingFound += crossed.size();
            }
        }

        return crossingFound;


    }

}

