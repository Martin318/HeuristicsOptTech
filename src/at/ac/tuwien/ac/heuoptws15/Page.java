package at.ac.tuwien.ac.heuoptws15;

import java.util.ArrayList;

/**
 * Created by Martin on 14.10.2016.
 */

public class Page{

    public ArrayList<Edge> edges;

    public Page(){
        this.edges = new ArrayList<Edge>();
    }

    @Override
    public Page clone(){
        Page p = new Page();
        p.edges = (ArrayList<Edge>)this.edges.clone();
        return p;
    }

}