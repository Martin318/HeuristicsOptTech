package at.ac.tuwien.ac.heuoptws15;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cem on 29/10/16.
 */
public class OneEdgeFlipNeighbourhood extends Neighbourhood {

    @Override
    public String getName() {
        return "OneEdgeFlip";
    }

    KPMPSolution orig_sol;
    int pageindex = 0;
    int edgeindex = 0;
    int transferIndex = 1;

    public void setSolution(KPMPSolution sol){
        orig_sol = sol;
        pageindex = 0;
        edgeindex = 0;
        transferIndex = 1;
    }

    @Override
    public KPMPSolution getRandomNeighbour() {


        int tempPageIndex = RandomStuff.between(0,orig_sol.pages.length-1);
        int tempTransferIndex;
        do {
            tempTransferIndex = RandomStuff.between(0,orig_sol.pages.length-1);
        } while ( tempPageIndex == tempTransferIndex);

        int tempEdgeIndex = RandomStuff.between(0,orig_sol.pages[tempPageIndex].edges.size()-1 );

        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.pages.length, orig_sol.ordering);

        // DUPLICATE SOLUTION

        for (int i = 0; i < orig_sol.pages.length; i++)
            for (Edge e : orig_sol.pages[i].edges)
                solution.addEdge(e,i);

        // DO A CHANGE


        Edge e = solution.pages[tempPageIndex].edges.get(tempEdgeIndex);
        solution.removeEdge(e,tempPageIndex);
        solution.addEdge(e,tempTransferIndex);


        return solution;
    }

    public KPMPSolution getNextNeighbour() {

        if(orig_sol.pages.length == 1)
            return null; // NO edge swap possible

        // TERMINATION criterium

        if(transferIndex >= orig_sol.pages.length || pageindex >= orig_sol.pages.length)
            return null;


        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.pages.length, orig_sol.ordering);

        // DUPLICATE SOLUTION

        for (int i = 0; i < orig_sol.pages.length; i++)
            for (Edge e : orig_sol.pages[i].edges)
                solution.addEdge(e,i);

        // DO A CHANGE


        Edge e = solution.pages[pageindex].edges.get(edgeindex);
        solution.removeEdge(e,pageindex);
        solution.addEdge(e,transferIndex);


        // UPDATE pageIndices

        edgeindex++;

        if(edgeindex == orig_sol.pages[pageindex].edges.size()){
            edgeindex = 0;
            transferIndex ++;
            if(transferIndex == pageindex)
                transferIndex ++;
        }

        if(transferIndex == orig_sol.pages.length){
            pageindex ++;
            transferIndex = 0;
        }



        return solution;
    }


}
