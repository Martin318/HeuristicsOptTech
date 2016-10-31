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

    public boolean crosses(Edge other, Integer[] orderingComp){
        int a,b,c,d;
        a = orderingComp[this.start];
        b = orderingComp[other.start];
        c = orderingComp[this.end];
        d = orderingComp[other.end];                

        return ( a < b && b < c && c < d );
    }


}
