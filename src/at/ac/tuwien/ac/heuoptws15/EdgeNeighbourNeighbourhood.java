package at.ac.tuwien.ac.heuoptws15;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cem on 29/10/16.
 */
public class EdgeNeighbourNeighbourhood extends Neighbourhood {

    @Override
    public String getName() {
        return "OneEdgeFlip";
    }

    KPMPSolution orig_sol;
    int pageindex = 0;
    int edgeindex = 0;

    public void setSolution(KPMPSolution sol){
        orig_sol = sol;
        pageindex = 0;
        edgeindex = 0;
    }

    @Override
    public KPMPSolution getRandomNeighbour() {


        int tempPageIndex = RandomStuff.between(0,orig_sol.pages.length-1);
//        int tempTransferIndex;
//        do {
//            tempTransferIndex = RandomStuff.between(0,orig_sol.pages.length-1);
//        } while ( tempPageIndex == tempTransferIndex);

        int tempEdgeIndex = RandomStuff.between(0,orig_sol.pages[tempPageIndex].edges.size()-1 );

        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.pages.length, orig_sol.ordering);

        // DUPLICATE SOLUTION

        for (int i = 0; i < orig_sol.pages.length; i++)
            for (Edge e : orig_sol.pages[i].edges)
                solution.addEdge(e,i);

        // DO A CHANGE


        Edge e = solution.pages[tempPageIndex].edges.get(tempEdgeIndex);
        solution.removeEdge(e,tempPageIndex);
        solution.addEdge(e,(tempPageIndex + 1)  % solution.pages.length);


        return solution;
    }

    public KPMPSolution getNextNeighbour() {

        while(pageindex < orig_sol.pages.length && orig_sol.pages[pageindex].edges.size() == 0){
            pageindex++;
        }

        if(pageindex == orig_sol.pages.length){
            return null;
        }


        if(orig_sol.pages.length == 1)
            return null; // NO edge swap possible

        // TERMINATION criterium

        if(pageindex >= orig_sol.pages.length)
            return null;


        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.pages.length, orig_sol.ordering);

        // DUPLICATE SOLUTION

        for (int i = 0; i < orig_sol.pages.length; i++)
            for (Edge e : orig_sol.pages[i].edges)
                solution.addEdge(e,i);

        // DO A CHANGE

        Edge e = solution.pages[pageindex].edges.get(edgeindex);
        solution.removeEdge(e, pageindex);
        solution.addEdge(e, (pageindex + 1) % solution.pages.length );


        // UPDATE pageIndices

        edgeindex++;


        if(edgeindex == orig_sol.pages[pageindex].edges.size()){
            edgeindex = 0;
            pageindex ++;
        }

        return solution;
    }


}
