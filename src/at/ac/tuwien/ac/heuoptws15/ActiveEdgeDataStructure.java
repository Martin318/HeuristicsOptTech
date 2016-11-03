package at.ac.tuwien.ac.heuoptws15;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Martin on 17.10.2016.
 */
public class ActiveEdgeDataStructure implements CollisionChecker{
    public Object[] futureActiveVertexPoints;
    public Object[] pastActiveVertexPoints;
    public Integer[] orderingComp;
    public Integer crosssings = 0;

    public ActiveEdgeDataStructure(int numVertices, Integer[] vertexOrdering) {
        this.futureActiveVertexPoints = new Object[numVertices];
        this.pastActiveVertexPoints = new Object[numVertices];

        // Compute the index of each id value
        this.orderingComp = new Integer[vertexOrdering.length];
        for (int i = 0; i < vertexOrdering.length; i++)
            this.orderingComp[vertexOrdering[i]] = i;

        for (int i = 0; i < numVertices; i++) {
            futureActiveVertexPoints[i] = new TreeMap<EdgePoint,Integer>();
            pastActiveVertexPoints[i] = new TreeMap<EdgePoint,Integer>();


        }
    }


    public void addEdge(Edge e) {
        // Get  start and end indices, respecting the vertex ordering.
        int alpha = e.theSmallerEndPointwithRespectTo(orderingComp);
        int omega = e.theLargerEndPointwithRespectTo(orderingComp);

        for (int i = alpha + 1; i < omega; i++) {
             TreeMap<EdgePoint,Integer> currentFutureList = (TreeMap<EdgePoint,Integer>) futureActiveVertexPoints[i];
             TreeMap<EdgePoint,Integer> currentPastList = (TreeMap<EdgePoint,Integer>) pastActiveVertexPoints[i];

             EdgePoint current_forward = new EdgePoint(e,omega, i);
             EdgePoint current_backward = new EdgePoint(e,alpha, i);

             currentFutureList.put(current_forward,current_forward.getRemainingLength());
             currentPastList.put(current_backward,current_backward.getRemainingLength());
        }

        // Count the new crossings introduced
        if(alpha+1 != omega){
            int front = 0;
            int back = 0;

            front = countCrossings(new EdgePoint(e,omega,alpha),(TreeMap<EdgePoint,Integer>) futureActiveVertexPoints[alpha]);
            back = countCrossings(new EdgePoint(e,alpha,omega),(TreeMap<EdgePoint,Integer>) pastActiveVertexPoints[omega]);

            crosssings += back + front;
        }

    }



    public void removeEdge(Edge e) {
        // Get  start and end indices, respecting the vertex ordering.
        int alpha = e.theSmallerEndPointwithRespectTo(orderingComp);
        int omega = e.theLargerEndPointwithRespectTo(orderingComp);

        if(alpha+1 != omega){
            int front;
            int  back;

            front = countCrossings(new EdgePoint(e,omega,alpha),(TreeMap<EdgePoint,Integer>) futureActiveVertexPoints[alpha]);
            back = countCrossings(new EdgePoint(e,alpha,omega),(TreeMap<EdgePoint,Integer>) pastActiveVertexPoints[omega]);

            crosssings -= front + back;
        }

        for (int i = alpha + 1; i < omega; i++) {
            TreeMap<EdgePoint,Integer> currentFutureList = (TreeMap<EdgePoint,Integer>) futureActiveVertexPoints[i];
            TreeMap<EdgePoint,Integer> currentPastList = (TreeMap<EdgePoint,Integer>) pastActiveVertexPoints[i];

            EdgePoint current_forward = new EdgePoint(e,omega, i);
            EdgePoint current_backward = new EdgePoint(e,alpha, i);

            currentFutureList.remove(current_forward,current_forward.getRemainingLength());
            currentPastList.remove(current_backward,current_backward.getRemainingLength());
        }


    }

    public int countAllCrossingsWithNewEdge(Edge e){
        int alpha = e.theSmallerEndPointwithRespectTo(orderingComp);
        int omega = e.theLargerEndPointwithRespectTo(orderingComp);


        int front = countCrossings(new EdgePoint(e,omega,alpha),(TreeMap<EdgePoint,Integer>) futureActiveVertexPoints[alpha]);
        int back = countCrossings(new EdgePoint(e,alpha,omega),(TreeMap<EdgePoint,Integer>) pastActiveVertexPoints[omega]);
        return front+back;
    }



    private Integer countCrossings(EdgePoint e, TreeMap<EdgePoint,Integer> map){
          SortedMap<EdgePoint,Integer> crossings = map.headMap(e,true);
          return  crossings.keySet().stream().filter( e2 -> e2.e.theLargerEndPointwithRespectTo(orderingComp) != e.e.theLargerEndPointwithRespectTo(orderingComp) && e2.e.theSmallerEndPointwithRespectTo(orderingComp) != e.e.theSmallerEndPointwithRespectTo(orderingComp)).collect(Collectors.toList()).size();
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
            int thisAlpha = this.e.theSmallerEndPointwithRespectTo(orderingComp);
            int thisOmega = this.e.theLargerEndPointwithRespectTo(orderingComp);
            int otherAlpha = other.e.theSmallerEndPointwithRespectTo(orderingComp);
            int otherOmega = other.e.theLargerEndPointwithRespectTo(orderingComp);

            if ( this.getRemainingLength() != other.getRemainingLength())
                return this.getRemainingLength() - other.getRemainingLength();
            else if (thisAlpha != otherAlpha)
                return thisAlpha - otherAlpha;
            else
                return thisOmega - otherOmega;
        }

        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof EdgePoint)) return false;
            EdgePoint o = (EdgePoint) obj;

            return (o.e.equals(this.e)) && (o.index_end == this.index_end) && (o.current_index == this.current_index);
        }
    }

    @Override public ActiveEdgeDataStructure clone(){
        ActiveEdgeDataStructure clone = new ActiveEdgeDataStructure(futureActiveVertexPoints.length, orderingComp.clone());

        clone.futureActiveVertexPoints = futureActiveVertexPoints.clone();
        clone.pastActiveVertexPoints = pastActiveVertexPoints.clone();
        clone.crosssings = crosssings;

        return clone;

    }


}
