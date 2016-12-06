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


        int tempPageIndex = RandomStuff.between(0,orig_sol.numPages-1);
        int tempTransferIndex;
        do {
            tempTransferIndex = RandomStuff.between(0,orig_sol.numPages-1);
        } while ( tempPageIndex == tempTransferIndex);

        int tempEdgeIndex = RandomStuff.between(0,orig_sol.getEdges(tempPageIndex).size()-1 );

        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.numPages, orig_sol.ordering);

        // DUPLICATE SOLUTION

        for (int i = 0; i < orig_sol.numPages; i++)
            for (Edge e : orig_sol.getEdges(i))
                solution.addEdge(e,i);

        // DO A CHANGE


        Edge e = solution.getEdges(tempPageIndex).get(tempEdgeIndex);
        solution.removeEdge(e,tempPageIndex);
        solution.addEdge(e,tempTransferIndex);


        return solution;
    }

    public KPMPSolution getNextNeighbour() {

        while(pageindex < orig_sol.numPages && orig_sol.getEdges(pageindex).size() == 0){
            pageindex++;
        }

        if(pageindex == orig_sol.numPages){
            return null;
        }


        if(orig_sol.numPages == 1)
            return null; // NO edge swap possible

        // TERMINATION criterium

        if(transferIndex >= orig_sol.numPages || pageindex >= orig_sol.numPages)
            return null;


        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.numPages, orig_sol.ordering);

        // DUPLICATE SOLUTION

        for (int i = 0; i < orig_sol.numPages; i++)
            for (Edge e : orig_sol.getEdges(i))
                solution.addEdge(e,i);

        // DO A CHANGE

        Edge e = solution.getEdges(pageindex).get(edgeindex);
        solution.removeEdge(e, pageindex);
        solution.addEdge(e, transferIndex);


        // UPDATE pageIndices

        edgeindex++;


        if(edgeindex == orig_sol.getEdges(pageindex).size()){
            edgeindex = 0;
            transferIndex ++;
            if(transferIndex == pageindex)
                transferIndex ++;
        }

        if(transferIndex == orig_sol.numPages){
            pageindex ++;
            transferIndex = 0;
        }



        return solution;
    }


}
