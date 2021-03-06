package at.ac.tuwien.ac.heuoptws15;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by cem on 27/10/16.
 */
public class IntegerCollisionDetection implements CollisionChecker{
    int crossings = 0;
    int[] currentActive;
    Integer[] ordering;
    Integer[] orderingComp;
    public List<Edge> edges;
    boolean changed = true;

    /**
     * Uses a simple counting of active edges to quickly count the number of crossings
     *
     * @param numVertices   Number of vertices
     * @param ordering      The current ordering
     */
    public IntegerCollisionDetection(int numVertices, Integer[] ordering){
        this.ordering = ordering;
        this.edges = new ArrayList<>();
        currentActive = new int[numVertices];
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
        int currentEndNode = 0;
        List<Edge> temp;
        int[] tempArray = currentActive.clone();

        while ( currentEndNode < currentActive.length && !edges.isEmpty()){
            final int currentEndNode0 = currentEndNode;

            //Edges are added from left to right end node.
            temp = edges.stream().filter(e1 -> e1.theLargerEndPointwithRespectTo(orderingComp) == currentEndNode0).collect(Collectors.toList());
            for(Edge e : temp){
                int alpha = e.theSmallerEndPointwithRespectTo(orderingComp);
                int omega = e.theLargerEndPointwithRespectTo(orderingComp);
                crossings += tempArray[alpha] +  tempArray[omega];
                for(int i = alpha+1; i <= omega-1 && i < currentActive.length; ++i)
                    currentActive[i]++;
            }
            tempArray = currentActive.clone();
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
        changed = true;
    }

    public void removeEdge(Edge  e){
        edges.remove(e);
        changed = true;
    }

    public int countAllCrossingsWithNewEdge(Edge e){

        int crosses = 0;

        for(Edge old : edges){
            if(e.crosses(old,orderingComp)){
                crosses++;
            }
        }
        return crosses;

    }

    @Override
    public IntegerCollisionDetection clone(){
        throw new IllegalAccessError("Do not use clone!");
    }

}
