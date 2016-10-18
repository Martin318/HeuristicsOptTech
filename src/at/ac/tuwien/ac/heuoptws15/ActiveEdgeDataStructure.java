package at.ac.tuwien.ac.heuoptws15;

import java.util.*;

/**
 * Created by Martin on 17.10.2016.
 */
public class ActiveEdgeDataStructure{

    private Object[] futureActiveVertexPoints;
    private Object[] pastActiveVertexPoints;
    private Integer[] vertexOrdering;
    private Integer crosssings = 0;
    private ArrayList<EdgePoint> crossingEdges;

    public ActiveEdgeDataStructure(int numVertices, Integer[] vertexOrdering) {
        this.futureActiveVertexPoints = new Object[numVertices];
        this.pastActiveVertexPoints = new Object[numVertices];
        this.crossingEdges = new ArrayList<EdgePoint>();

        // Compute the index of each id value
        this.vertexOrdering = new Integer[vertexOrdering.length];
        for (int i = 0; i < vertexOrdering.length; i++)
            this.vertexOrdering[vertexOrdering[i]] = i;

        for (int i = 0; i < numVertices; i++) {
            futureActiveVertexPoints[i] = new ArrayList<EdgePoint>();
            pastActiveVertexPoints[i] = new ArrayList<EdgePoint>();


        }
    }

    // Should be done on the fly, do not use this method.

    public void sortElements(){
        for (int i = 0; i < futureActiveVertexPoints.length; i++) {
            ArrayList<EdgePoint> currentFutureList = (ArrayList) futureActiveVertexPoints[i];
            ArrayList<EdgePoint> currentPastList = (ArrayList) pastActiveVertexPoints[i];

            Collections.sort(currentFutureList);
        }
    }


    public void addEdge(Edge e) {
        // Get  start and end indices, respecting the vertex ordering.
        int start = Math.min(vertexOrdering[e.start], vertexOrdering[e.end]);
        int end = Math.max(vertexOrdering[e.start], vertexOrdering[e.end]);


        for (int i = start + 1; i < end; i++) {
            ArrayList<EdgePoint> currentFutureList = (ArrayList) futureActiveVertexPoints[i];
            ArrayList<EdgePoint> currentPastList = (ArrayList) pastActiveVertexPoints[i];

            EdgePoint current_forward = new EdgePoint(e,vertexOrdering[e.end], i);
            EdgePoint current_backward = new EdgePoint(e,vertexOrdering[e.start], i);



            binaryInsert(current_forward, currentFutureList);

            // Vertices going "back"
            binaryInsert(current_backward, currentPastList);
        }

        // Count the new crossings introduced
        int front = countCrossings(new EdgePoint(e,end,start),(ArrayList) futureActiveVertexPoints[start]);
        int back = countCrossings(new EdgePoint(e,start,end),(ArrayList) pastActiveVertexPoints[end]);

        crosssings += back + front;
    }

    private void binaryInsert(EdgePoint e, ArrayList<EdgePoint> list){
        int leftindex = 0;
        int rightindex = list.size()-1;
        int centerindex;
        int compareResult;

        while(leftindex < rightindex) {
            centerindex = leftindex + ((rightindex - leftindex) / 2);
            compareResult = list.get(centerindex).compareTo(e);

            if (compareResult == 0) {
                // Center ist equal
                list.add(centerindex, e);
                return;
            }

            if (compareResult < 0) {
                // Center ist smaller than e
                leftindex = centerindex + 1 ;
            }

            if (compareResult > 0) {
                // Center ist bigger than e
                rightindex = centerindex - 1;
            }


        }
         // leftindex == rightindex
            list.add(leftindex, e);
    }

    private Integer countCrossings(EdgePoint e, ArrayList<EdgePoint> list){
        int leftindex = 0;
        int rightindex = list.size()-1;
        int centerindex = 0;
        int compareResult;

        while(leftindex < rightindex) {
            centerindex = leftindex + ((rightindex - leftindex) / 2);
            compareResult = list.get(centerindex).compareTo(e);

            if (compareResult == 0) {
                int i = 0;
                for (;(centerindex-i) > 0 && (centerindex-i) < list.size() && list.get(centerindex-i).compareTo(e) == 0; i++)
                centerindex = centerindex - i;
                break;
            }

            if (compareResult < 0) {
                // Center ist smaller than e
                leftindex = centerindex + 1 ;
            }

            if (compareResult > 0) {
                // Center ist bigger than e
                rightindex = centerindex - 1;
            }
        }

        for(int i = 0; i < centerindex; i++)
            crossingEdges.add(new EdgePoint(e.e,list.get(i).e.start,list.get(i).e.end));

        return centerindex;
    }

    public  Integer getCrossing(){
        return this.crosssings;
    }

    public void outputCrossings(){
        System.out.println("Active Edge Array found crossings:");
        for ( EdgePoint e : crossingEdges){
            if (e.index_end < e.e.start)
                System.out.println("(" + e.index_end +" " + e.current_index  + ") hat ein Crossing mit "+ "(" + e.e.start +" " + e.e.end + ")" );
            else
                System.out.println("(" + e.e.start +" " + e.e.end  + ") hat ein Crossing mit "+ "(" + e.index_end +" " + e.current_index + ")" );
        }

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
            return this.getRemainingLength() - other.getRemainingLength();
        }
    }


}
