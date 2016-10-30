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
    List<Edge> sortedEdges;

    /**
     * Uses a simple counting of active edges to quickly count the number of crossings
     *
     * @param numVertices   Number of vertices
     * @param ordering      The current ordering
     */
    public IntegerCollisionDetection(int numVertices, Integer[] ordering){
        this.ordering = ordering;
        this.sortedEdges = new ArrayList<>();
        currentActive = new int[numVertices];
        Arrays.fill(currentActive,0);

        orderingComp = new Integer[this.ordering.length];
        for (int i = 0; i < this.ordering.length; i++)
            orderingComp[this.ordering[i]] = i;

        // Compute the crossings count immediately
        crossingCount();
    }

    public IntegerCollisionDetection(int numVertices, Integer[] ordering, List<Edge> edges){
        this.ordering = ordering;
        this.sortedEdges = edges;
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

        while ( currentEndNode < currentActive.length && !sortedEdges.isEmpty()){
            final int currentEndNode0 = ordering[currentEndNode];

            //Edges are added from left to right end node.
            temp = sortedEdges.stream().filter( e1 -> e1.end == currentEndNode0).collect(Collectors.toList());
            for(Edge e : temp){
                crossings += tempArray[orderingComp[e.start]] +  tempArray[orderingComp[e.end]];
                for(int i = orderingComp[e.start]+1; i <= orderingComp[e.end]-1 && i < currentActive.length; ++i)
                    currentActive[i]++;
            }
            tempArray = currentActive.clone();
            currentEndNode++;
        }
    }

    public int getCrossing(){
        return crossings;
    }


    public void addEdge(Edge e){

        sortedEdges.add(e);
        crossingCount();
    }

    public void removeEdge(Edge  e){
        sortedEdges.remove(e);
        crossingCount();
    }

    public int countAllCrossingsWithNewEdge(Edge e){
        sortedEdges.add(e);

        int oldCrossing = crossings;
        crossingCount();
        int newCrossing = crossings;
        crossings = oldCrossing;

        sortedEdges.remove(e);
        return newCrossing - oldCrossing;
    }

    @Override
    public IntegerCollisionDetection clone(){
        throw new RuntimeException("Not Implemented");
    }

}
