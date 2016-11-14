package at.ac.tuwien.ac.heuoptws15;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by cem on 27/10/16.
 */
public class IncrementalCollisionDetection implements CollisionChecker{
    int crossings = 0;
    int[][] currentActive;
    Integer[] ordering;
    Integer[] orderingComp;
    List<Edge> edges;
    boolean changed = true;

    /**
     * Uses a simple counting of active edges to quickly count the number of crossings
     *
     * @param numVertices   Number of vertices
     * @param ordering      The current ordering
     */
    public IncrementalCollisionDetection(int numVertices, Integer[] ordering){
        this.ordering = ordering;
        this.edges = new ArrayList<>();
        currentActive = new int[numVertices][numVertices];
        Arrays.fill(currentActive,0);

        orderingComp = new Integer[this.ordering.length];
        for (int i = 0; i < this.ordering.length; i++)
            orderingComp[this.ordering[i]] = i;

        // Compute the crossings count immediately
        crossingCount();
    }


    private void crossingCount(){
        crossings = 0;
        Arrays.fill(currentActive,0);
        int currentEndNode = 1;
        List<Edge> temp;


        while ( currentEndNode < currentActive.length && !edges.isEmpty()){
            final int currentEndNode0 = currentEndNode;

            //Edges are added from left to right end node.
            temp = edges.stream().filter(e1 -> e1.theLargerEndPointwithRespectTo(orderingComp) == currentEndNode0).collect(Collectors.toList());
            for(Edge e : temp){
                int alpha = e.theSmallerEndPointwithRespectTo(orderingComp);
                int omega = e.theLargerEndPointwithRespectTo(orderingComp);
                for (int i = 0; i < currentEndNode; i++)
                    crossings += currentActive[i][alpha] +  currentActive[i][omega];
                for(int i = alpha+1; i <= omega-1 && i < currentActive.length; ++i)
                    currentActive[currentEndNode][i]++;
            }

            currentEndNode++;
        }
        changed = false;

    }

    public int getCrossing(){
        if(changed) crossingCount();
        return crossings;
    }


    public void addEdge(Edge e){

        edges.add(e);

        int alpha = e.theSmallerEndPointwithRespectTo(orderingComp);
        int omega = e.theLargerEndPointwithRespectTo(orderingComp);
        for (int i = 0; i < omega; i++)
            crossings += currentActive[i][alpha] +  currentActive[i][omega];
        for (int i = omega+1; i < orderingComp.length; i++)
            crossings += currentActive[i][alpha] +  currentActive[i][omega];

        for(int i = alpha+1; i <= omega-1 && i < currentActive.length; ++i)
            currentActive[omega][i]++;
    }


    public void removeEdge(Edge  e){

        int crossingsOfEdge = 0;

        int alpha = e.theSmallerEndPointwithRespectTo(orderingComp);
        int omega = e.theLargerEndPointwithRespectTo(orderingComp);
        for (int i = 0; i < omega; i++)
            crossingsOfEdge += currentActive[i][alpha] +  currentActive[i][omega];
        for (int i = omega+1; i < orderingComp.length; i++)
            crossingsOfEdge += currentActive[i][alpha] +  currentActive[i][omega];

        crossings -= crossingsOfEdge;
        edges.remove(e);
    }

    public int countAllCrossingsWithNewEdge(Edge e){

        int crosses = 0;

        int alpha = e.theSmallerEndPointwithRespectTo(orderingComp);
        int omega = e.theLargerEndPointwithRespectTo(orderingComp);
        for (int i = 0; i < omega; i++)
            crosses += currentActive[i][alpha] +  currentActive[i][omega];
        for (int i = omega+1; i < orderingComp.length; i++)
            crosses += currentActive[i][alpha] +  currentActive[i][omega];


        return crosses;

    }

    @Override
    public IntegerCollisionDetection clone(){
        throw new IllegalAccessError("Do not use clone!");
    }

}
