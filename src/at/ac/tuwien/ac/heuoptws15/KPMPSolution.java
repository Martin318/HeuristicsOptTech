package at.ac.tuwien.ac.heuoptws15;

import java.util.ArrayList;

/**
 * Created by Martin on 14.10.2016.
 */
public class KPMPSolution {

    public int[] ordering;

    public Page[] pages;

    public KPMPSolution(int numVertices, int numPages){

        ordering = new int[numVertices];

        pages = new Page[numPages];

        for(int i = 0; i < numPages; i++){
            pages[i] = new Page();
        }

    }

    @Override
    public String toString(){
        String s = "Vertex ordering: [";

        for(int i=0; i<ordering.length; i++){
            if(i != 0) s += ", ";
            s += ordering[i];
        }

        s += "] \n";

        s += "Pages: \n";

        for(int i=0; i<pages.length; i++){
            s += "Page: " + i + " ";

            for(Edge e : pages[i].edges){

                s +=  e;
                s += ", ";
            }


            s += "\n";
        }

        return s;
    }

}

