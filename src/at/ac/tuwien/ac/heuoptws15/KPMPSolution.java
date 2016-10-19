package at.ac.tuwien.ac.heuoptws15;

import java.security.spec.ECGenParameterSpec;
import java.util.*;
import java.util.stream.*;

/**
 * Created by Martin on 14.10.2016.
 */
public class KPMPSolution {

    public Integer[] ordering;

    private Integer[] orderingComp;

    private Page[] pages;

    private ActiveEdgeDataStructure[] activeEdge;

    public KPMPSolution(int numVertices, int numPages, Integer[] ordering){

        this.ordering = ordering;

        pages = new Page[numPages];
        activeEdge = new ActiveEdgeDataStructure[numPages];

        for(int i = 0; i < numPages; i++){
            pages[i] = new Page();
            activeEdge[i] = new ActiveEdgeDataStructure(numVertices,ordering);
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
        System.out.println("Crossings found: ");
//        for(Page page : solution.pages)
//            for(Edge e1 : page.edges){
//                List<Edge> crossed  = page.edges.stream()
//                        .filter(e2 -> solution.smallerInOrdering(e1.start,e2.start) &&
//                                      solution.smallerInOrdering(e2.start,e1.end)   &&
//                                      solution.smallerInOrdering(e1.end,e2.end ) )
//                        .collect(Collectors.toList());
////                for(Edge e : crossed){
////                    if( e.start < e1.start)
////                        System.out.println("(" + e.start +" " + e.end + ") hat ein Crossing mit "+ "(" + e1.start +" " + e1.end + ")" );
////                    else
////                        System.out.println("(" + e1.start +" " + e1.end + ") hat ein Crossing mit "+ "(" + e.start +" " + e.end + ")" );
////                }
//                crossingFound += crossed.size();
//            }

        for(ActiveEdgeDataStructure active : solution.activeEdge){
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

            if(currentCrossings == 0){
                return i;
            }

            if(currentCrossings < crossingsMinValue){
                crossingsMinValue = currentCrossings;
                crossingsMinIndex = i;
            }


        }

        return crossingsMinIndex;

    }

    /**
     *  Returns the index of the first page in which this edge fits,
     *  if not such page exists, returns the page with least edges
     *
     * @param edge
     * @return
     */
    public int nextFreePage_OLD( Edge edge){
        int pageMinSize = this.pages[0].edges.size();
        int pageMinIndex = 0;
        for(int i = 0; i < this.pages.length; i++){
                if( this.pages[i].edges.size() < pageMinSize){
                    pageMinSize = this.pages[i].edges.size();
                    pageMinIndex = i;
                }

                Boolean NoneBefore = this.pages[i].edges.stream()
                        .noneMatch(e2 ->    this.smallerInOrdering(e2.start,edge.start) &&
                                            this.smallerInOrdering(edge.start,e2.end)   &&
                                            this.smallerInOrdering(e2.end,edge.end ) );

                Boolean NoneAfter = this.pages[i].edges.stream()
                        .noneMatch(e2 ->    this.smallerInOrdering(edge.start,e2.start) &&
                                            this.smallerInOrdering(e2.start,edge.end)   &&
                                            this.smallerInOrdering(edge.end,e2.end ) );
                if (NoneBefore && NoneAfter)
                    return i;
        }

        return pageMinIndex;
    }


    public void addEdge(Edge e, int page){
        pages[page].edges.add(e);
        activeEdge[page].addEdge(e);
    }




}

