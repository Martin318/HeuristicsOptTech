package at.ac.tuwien.ac.heuoptws15;

import java.util.*;

/**
 * Created by Martin on 17.10.2016.
 */
public class ActiveEdgeDataStructure{

    private Object[] futureActiveVertexPoints;
    private Object[] pastActiveVertexPoints;
    private Integer[] vertexOrdering;


    public ActiveEdgeDataStructure(int numVertices, Integer[] vertexOrdering) {

        this.futureActiveVertexPoints = new Object[numVertices];

        this.pastActiveVertexPoints = new Object[numVertices];

        // Compute the index of each id value
        this.vertexOrdering = new Integer[vertexOrdering.length];
        for (int i = 0; i < vertexOrdering.length; i++)
            this.vertexOrdering[vertexOrdering[i]] = i;

        for (int i = 0; i < numVertices; i++) {
            futureActiveVertexPoints[i] = new TreeSet<EdgePoint>();
            pastActiveVertexPoints[i] = new TreeSet<EdgePoint>();


        }
    }


    public void addEdge(Edge e) {


        // Get  start and end indices, respecting the vertex ordering.

        int start = Math.min(vertexOrdering[e.start], vertexOrdering[e.end]);
        int end = Math.max(vertexOrdering[e.start], vertexOrdering[e.end]);


        for (int i = start + 1; i < end; i++) {
            TreeSet<EdgePoint> currentFutureList = (TreeSet) futureActiveVertexPoints[i];
            TreeSet<EdgePoint> currentPastList = (TreeSet) pastActiveVertexPoints[i];

            currentFutureList.add(new EdgePoint(e, vertexOrdering[e.start], vertexOrdering[e.end], i));

            // Vertices going "back"
            currentPastList.add(new EdgePoint(e, vertexOrdering[e.end], vertexOrdering[e.start], i));

        }

    }

    private class EdgePoint implements  Comparable<EdgePoint>{

        private Edge e;
        private int index_start;
        private int index_end;
        private int current_index;



        public EdgePoint(Edge e, int index_start, int index_end, int current_index) {

            this.e = e;
            this.index_start = index_start;
            this.index_end = index_end;
            this.current_index = current_index;

        }



        // length should be positive, in the "forward list" this is no problem, but for the backward list we need the abs.

        public int getRemainingLength(){

            return Math.abs(index_end - current_index);

        }


        public int compareTo(EdgePoint other){
            return this.getRemainingLength() - other.getRemainingLength();
        }
    }


}
