package at.ac.tuwien.ac.heuoptws15;

import java.util.Objects;

/**
 * Created by Martin on 14.10.2016.
 */

public class Edge{

    private int start;
    private int end;

    public Edge(int start, int end){

        this.start = start;
        this.end = end;
    }

    public int theSmallerEndPointwithRespectTo(Integer[] orderingComp){
        return Math.min(orderingComp[start],orderingComp[end]);
    }


    public int theLargerEndPointwithRespectTo(Integer[] orderingComp){
        return Math.max(orderingComp[start],orderingComp[end]);
    }

    @Override
    public String toString(){
        return "(" + start + "," + end + ")";
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Edge)) return false;
        Edge o = (Edge) obj;

        return (o.start == this.start) && (o.end == this.end);
    }

    public boolean crosses(Edge other, Integer[] orderingComp){
        int thisSmaller = Math.min(orderingComp[this.start],orderingComp[this.end]);
        int thisBigger = Math.max(orderingComp[this.start],orderingComp[this.end]);

        int otherSmaller = Math.min(orderingComp[other.start],orderingComp[other.end]);
        int otherBigger = Math.max(orderingComp[other.start],orderingComp[other.end]);

        if(thisSmaller < otherSmaller)
            return (otherSmaller < thisBigger) && (thisBigger < otherBigger);
        else
            return (otherSmaller < thisSmaller) && (thisSmaller < otherBigger) && (otherBigger  < thisBigger);
    }


}
