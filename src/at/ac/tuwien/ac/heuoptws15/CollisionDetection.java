package at.ac.tuwien.ac.heuoptws15;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by cem on 27/10/16.
 */
public class CollisionDetection {
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
     * @param edges         A list of edges (for a page)
     */
    public CollisionDetection(int numVertices,  Integer[] ordering, List<Edge> edges){
        this.ordering = ordering;
        this.sortedEdges = edges;
        currentActive = new int[numVertices];
        Arrays.fill(currentActive,0);

        // should be a speedup for large lists, slower on small lists
        // maybe depending on size of lists, this should be skipped?
        Collections.sort(sortedEdges, (Edge e1,Edge e2) -> {
            if(e1.start != e2.start)
                return (smallerInOrdering(e1.start,e2.start)?(-1):(1));
            else if (e1.end != e2.end)
                return (smallerInOrdering(e1.end,e2.end)?(-1):(1));
            else
                return 0;
        });


        // Compute the crossings count immediately
        crossingCount();
    }


    private void crossingCount(){
        int currentEndNode = 0;
        List<Edge> temp;
        int[] tempArray = currentActive.clone();

        while ( currentEndNode < currentActive.length){
            final int currentEndNode0 = ordering[currentEndNode];

            //Edges are added from left to right end node.
            temp = sortedEdges.stream().filter( e1 -> e1.end != currentEndNode0).collect(Collectors.toList());
            for(Edge e : temp){
                crossings =+ tempArray[orderingComp[e.start]] + tempArray[orderingComp[e.end]];
                for(int i = orderingComp[e.start]+1; i <= orderingComp[e.end]-1 && i < currentActive.length; ++i)
                    currentActive[i]++;
            }
            tempArray = currentActive.clone();
            currentEndNode++;
        }
    }

    public int getCrossings(){
        return crossings;
    }


    public boolean smallerInOrdering(int a, int b){
        if (orderingComp == null) {
            orderingComp = new Integer[this.ordering.length];
            for (int i = 0; i < this.ordering.length; i++)
                orderingComp[this.ordering[i]] = i;
        }

        return orderingComp[a] < orderingComp[b];
    }

}
