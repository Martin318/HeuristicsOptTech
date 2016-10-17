package at.ac.tuwien.ac.heuoptws15;

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


}
