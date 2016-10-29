package at.ac.tuwien.ac.heuoptws15;

import java.util.*;

/**
 * Created by Martin on 14.10.2016.
 */
public class KPMPSolution {

    public Integer[] ordering;
    private Integer[] orderingComp;
    private Page[] pages;
    private CollisionChecker[] activeEdge;


    public KPMPSolution(int numVertices, int numPages, Integer[] ordering){

        this.ordering = ordering;

        pages = new Page[numPages];
        activeEdge = new FastCollisionDetection[numPages];

        for(int i = 0; i < numPages; i++){
            pages[i] = new Page();
            activeEdge[i] = new FastCollisionDetection(numVertices,ordering);

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
    public boolean smallerInOrdering(int a, int b){
        if (orderingComp == null) {
            orderingComp = new Integer[this.ordering.length];
            for (int i = 0; i < this.ordering.length; i++)
                orderingComp[this.ordering[i]] = i;
        }

        return orderingComp[a] < orderingComp[b];
    }



    public int crossings2(){
        FastCollisionDetection[] detection = new FastCollisionDetection[pages.length];

        int count = 0;

        for(int i = 0; i < pages.length; i++){
            detection[i] = new FastCollisionDetection(ordering.length,ordering,pages[i].edges);
            count += detection[i].getCrossing();
        }

        return count;
    }



    /**
     * A bit slow since looking for edges itself takes linear time.
     * If possible in constant time (use of actual arrays?) this would run in linear time
     *
     * @param solution A possible solution
     * @return Number of crossings found
     */
    public static int crossings(KPMPSolution solution){
        if (solution == null )
            return 0;
        int crossingFound = 0;

       for(CollisionChecker active : solution.activeEdge){
            crossingFound += active.getCrossing();
        }

        return crossingFound;
    }



    public int nextFreePage( Edge edge) {

        int crossingsMinValue = Integer.MAX_VALUE;
        int crossingsMinIndex = 0;
        int currentCrossings;


        for(int i = 0; i < this.pages.length; i++){
            currentCrossings = activeEdge[i].countAllCrossingsWithNewEdge(edge);

            if(currentCrossings == 0)
                return i;


            if(currentCrossings < crossingsMinValue){
                crossingsMinValue = currentCrossings;
                crossingsMinIndex = i;
            }
        }

        return crossingsMinIndex;
    }


    public void addEdge(Edge e, int page){
        pages[page].edges.add(e);
        activeEdge[page].addEdge(e);
    }


    public void insertIntoWriter(KPMPSolutionWriter w){

        w.setSpineOrder(Arrays.asList(ordering));


        for(int i = 0; i< pages.length; i++){

            for(Edge e : pages[i].edges){
                w.addEdgeOnPage(e.start,e.end,i);
            }


        }

    }





}

