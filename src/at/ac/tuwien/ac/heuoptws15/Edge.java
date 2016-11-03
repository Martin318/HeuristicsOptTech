package at.ac.tuwien.ac.heuoptws15;

import java.util.Objects;

/**
 * Created by Martin on 14.10.2016.
 */

public class Edge{

    private final int nameOfFirstVertex;
    private final int nameOfSecondVertex;

    public Edge(int nameOfFirstVertex, int nameOfSecondVertex){

        this.nameOfFirstVertex = nameOfFirstVertex;
        this.nameOfSecondVertex = nameOfSecondVertex;
    }

    public int theSmallerEndPointwithRespectTo(Integer[] orderingComp){
        return Math.min(orderingComp[nameOfFirstVertex],orderingComp[nameOfSecondVertex]);
    }


    public int theLargerEndPointwithRespectTo(Integer[] orderingComp){
        return Math.max(orderingComp[nameOfFirstVertex],orderingComp[nameOfSecondVertex]);
    }

    @Override
    public String toString(){
        return "(" + nameOfFirstVertex + "," + nameOfSecondVertex + ")";
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Edge)) return false;
        Edge o = (Edge) obj;

        return (o.nameOfFirstVertex == this.nameOfFirstVertex) && (o.nameOfSecondVertex == this.nameOfSecondVertex);
    }

    public boolean crosses(Edge other, Integer[] orderingComp){
        int thisSmaller = Math.min(orderingComp[this.nameOfFirstVertex],orderingComp[this.nameOfSecondVertex]);
        int thisBigger = Math.max(orderingComp[this.nameOfFirstVertex],orderingComp[this.nameOfSecondVertex]);

        int otherSmaller = Math.min(orderingComp[other.nameOfFirstVertex],orderingComp[other.nameOfSecondVertex]);
        int otherBigger = Math.max(orderingComp[other.nameOfFirstVertex],orderingComp[other.nameOfSecondVertex]);

        if(thisSmaller < otherSmaller)
            return (otherSmaller < thisBigger) && (thisBigger < otherBigger);
        else
            return (otherSmaller < thisSmaller) && (thisSmaller < otherBigger) && (otherBigger  < thisBigger);
    }


    public int getNameOfFirstVertex(){
        return nameOfFirstVertex;
    }

    public int getNameOfSecondVertex(){
        return  nameOfSecondVertex;
    }


}
