package at.ac.tuwien.ac.heuoptws15;

import sun.java2d.pipe.SolidTextRenderer;

import javax.swing.plaf.ActionMapUIResource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Martin on 14.10.2016.
 */
public class KPMPSolution implements Cloneable{

    public Integer[] ordering;
    private Integer[] orderingComp;
    Page[] pages;
    public CollisionChecker[] activeEdge;


    public KPMPSolution(int numVertices, int numPages, Integer[] ordering){

        this.ordering = ordering;

        pages = new Page[numPages];
        activeEdge = new IntegerCollisionDetection[numPages];

        for(int i = 0; i < numPages; i++){
            pages[i] = new Page();
            activeEdge[i] = new IntegerCollisionDetection(numVertices,ordering);

        }

        orderingComp = new Integer[this.ordering.length];
        for (int i = 0; i < this.ordering.length; i++)
            orderingComp[this.ordering[i]] = i;

    }

  /*  public KPMPSolution clone(){
        KPMPSolution clone = new KPMPSolution(this.ordering.length,this.pages.length,this.ordering);
        clone.pages = new Page[pages.length];
        for(int i = 0; i< pages.length; i++) clone.pages[i] = pages[i].clone();
        clone.orderingComp = this.orderingComp.clone();
        clone.activeEdge = activeEdge.clone();
        for(int i = 0; i< activeEdge.length; i++) clone.activeEdge[i] = activeEdge[i].clone();
        return clone;
    }*/

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

        return orderingComp[a] < orderingComp[b];
    }


//
//    public int crossings2(){
//        IntegerCollisionDetection[] detection = new IntegerCollisionDetection[pages.length];
//
//        int count = 0;
//
//        for(int i = 0; i < pages.length; i++){
//            detection[i] = new IntegerCollisionDetection(ordering.length,ordering,pages[i].edges);
//            count += detection[i].getCrossing();
//        }
//
//        return count;
//    }


//    public static int ActualCrossings(KPMPSolution solution){
//        if (solution == null )
//            return 0;
//        int crossingFound = 0;
//
//        for(Page page : solution.pages)
//            for(Edge e1 : page.edges){
//                List<Edge> crossed  = page.edges.stream()
//                        .filter(e2 -> solution.smallerInOrdering(e1.start,e2.start) &&
//                                solution.smallerInOrdering(e2.start,e1.end)   &&
//                                solution.smallerInOrdering(e1.end,e2.end ) )
//                        .collect(Collectors.toList());
//                crossingFound += crossed.size();
//            }
//
//
//        return crossingFound;
//    }


    /**
     * A bit slow since looking for edges itself takes linear time.
     * If possible in constant time (use of actual arrays?) this would run in linear time
     *
     * @return Number of crossings found
     */
    public int crossings(){

        int crossingFound = 0;

       for(CollisionChecker active : activeEdge){
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

    public void removeEdge(Edge e, int page){
        pages[page].edges.remove(e);
        activeEdge[page].removeEdge(e);
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

