package at.ac.tuwien.ac.heuoptws15;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Martin on 17.10.2016.
 */
public class ActiveEdgeDataStructure implements CollisionChecker{
    private Object[] futureActiveVertexPoints;
    private Object[] pastActiveVertexPoints;
    private Integer[] vertexOrdering;
    private Integer crosssings = 0;

    public ActiveEdgeDataStructure(int numVertices, Integer[] vertexOrdering) {
        this.futureActiveVertexPoints = new Object[numVertices];
        this.pastActiveVertexPoints = new Object[numVertices];

        // Compute the index of each id value
        this.vertexOrdering = new Integer[vertexOrdering.length];
        for (int i = 0; i < vertexOrdering.length; i++)
            this.vertexOrdering[vertexOrdering[i]] = i;

        for (int i = 0; i < numVertices; i++) {
            futureActiveVertexPoints[i] = new TreeMap<EdgePoint,Integer>();
            pastActiveVertexPoints[i] = new TreeMap<EdgePoint,Integer>();


        }
    }


    public void addEdge(Edge e) {
        // Get  start and end indices, respecting the vertex ordering.
        int start = Math.min(vertexOrdering[e.start], vertexOrdering[e.end]);
        int end = Math.max(vertexOrdering[e.start], vertexOrdering[e.end]);
        Edge actualE = new Edge(start,end);


        for (int i = start + 1; i < end; i++) {
             TreeMap<EdgePoint,Integer> currentFutureList = (TreeMap<EdgePoint,Integer>) futureActiveVertexPoints[i];
             TreeMap<EdgePoint,Integer> currentPastList = (TreeMap<EdgePoint,Integer>) pastActiveVertexPoints[i];

             EdgePoint current_forward = new EdgePoint(actualE,end, i);
             EdgePoint current_backward = new EdgePoint(actualE,start, i);


             currentFutureList.put(current_forward,current_forward.getRemainingLength());
             currentPastList.put(current_backward,current_backward.getRemainingLength());
        }

        // Count the new crossings introduced
        if(start+1 != end){
            int front = 0;
            int back = 0;

            front = countCrossings(new EdgePoint(actualE,end,start),(TreeMap<EdgePoint,Integer>) futureActiveVertexPoints[start]);
            back = countCrossings(new EdgePoint(actualE,start,end),(TreeMap<EdgePoint,Integer>) pastActiveVertexPoints[end]);

            crosssings += back + front;
        }
    }

    public int countAllCrossingsWithNewEdge(Edge e){
        int start = Math.min( vertexOrdering[e.start],vertexOrdering[e.end]);
        int end = Math.max(vertexOrdering[e.end],vertexOrdering[start]);
        Edge actualE = new Edge(start,end);

        int front = countCrossings(new EdgePoint(actualE,end,start),(TreeMap<EdgePoint,Integer>) futureActiveVertexPoints[start]);
        int back = countCrossings(new EdgePoint(actualE,start,end),(TreeMap<EdgePoint,Integer>) pastActiveVertexPoints[end]);
        return front+back;
    }



    private Integer countCrossings(EdgePoint e, TreeMap<EdgePoint,Integer> map){
          SortedMap<EdgePoint,Integer> crossings = map.headMap(e,true);
          return  crossings.keySet().stream().filter( e2 -> e2.e.end != e.e.end && e2.e.start != e.e.start).collect(Collectors.toList()).size();
    }



    public  int getCrossing(){
        return this.crosssings;
    }


    private class EdgePoint implements  Comparable<EdgePoint>{
        private Edge e;
        private int index_end;
        private int current_index;
        public EdgePoint(Edge e, int index_end, int current_index) {
            this.e = e;
            this.index_end = index_end;
            this.current_index = current_index;

        }


        // length should be positive, in the "forward list" this is no problem, but for the backward list we need the abs.
        public int getRemainingLength(){
            return Math.abs(index_end - current_index);

        }


        public int compareTo(EdgePoint other){
            if ( this.getRemainingLength() != other.getRemainingLength())
                return this.getRemainingLength() - other.getRemainingLength();
            else if (this.e.start != other.e.start)
                return this.e.start - other.e.start;
            else
                return this.e.end - other.e.end;
        }
    }


}
