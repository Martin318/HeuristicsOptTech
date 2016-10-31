package at.ac.tuwien.ac.heuoptws15;

import java.util.Objects;

/**
 * Created by Martin on 14.10.2016.
 */

public class Edge{

    public int start;
    public int end;

    public Edge(int start, int end){

        this.start = start;
        this.end = end;
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

    public boolean crosses(Edge e, Integer[] orderingComp){

        if(orderingComp[this.start] < orderingComp[e.start] && orderingComp[e.start] < orderingComp[this.end]) return true;
        if(orderingComp[e.start] < orderingComp[this.start] && orderingComp[this.start] < orderingComp[e.end]) return true;

        return false;

    }


}
