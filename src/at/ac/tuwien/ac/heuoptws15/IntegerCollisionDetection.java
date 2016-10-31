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
    List<Edge> edges;

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

    public IntegerCollisionDetection(int numVertices, Integer[] ordering, List<Edge> edges){
        this.ordering = ordering;
        this.edges = edges;
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
            final int currentEndNode0 = ordering[currentEndNode];

            //Edges are added from left to right end node.
            temp = edges.stream().filter(e1 -> e1.end == currentEndNode0).collect(Collectors.toList());
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
        crossingCount();
        return crossings;
    }


    public void addEdge(Edge e){

        edges.add(e);
    }

    public void removeEdge(Edge  e){
        edges.remove(e);
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
        IntegerCollisionDetection clone = new IntegerCollisionDetection(ordering.length, ordering);

        clone.edges = new ArrayList<>(edges);

        return clone;
    }

}
