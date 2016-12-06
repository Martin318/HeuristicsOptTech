package at.ac.tuwien.ac.heuoptws15;

/**
 * Created by Martin on 04.11.2016.
 */
public class NodeNeighbourSwapNeighbourhood extends Neighbourhood{

    KPMPSolution orig_sol;
    int nodeIndex = 1;


    @Override
    public KPMPSolution getNextNeighbour() {
        // TERMINATION criterium
        if(nodeIndex >= orig_sol.ordering.length)
            return null;

        // DO A CHANGE

        Integer[] newOrdering = new Integer[orig_sol.ordering.length];

        for (int i = 0; i < orig_sol.ordering.length; i++)
            newOrdering[i] = orig_sol.ordering[i];

        newOrdering[nodeIndex-1] = orig_sol.ordering[nodeIndex];
        newOrdering[nodeIndex] = orig_sol.ordering[nodeIndex-1];


        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.numPages, newOrdering);


        //System.out.println("Generated neighbour. Swapped edge " + (nodeIndex-1) + " with " + (nodeIndex));


        // UPDATE INDICES

        nodeIndex++;


        // DUPLICATE SOLUTION

        for (int i = 0; i < orig_sol.numPages; i++)
            for (Edge e : orig_sol.getEdges(i))
                solution.addEdge(e,i);

        return solution;
    }

    @Override
    public void setSolution(KPMPSolution sol) {
        orig_sol = sol;
        nodeIndex = 1;
    }

    @Override
    public KPMPSolution getRandomNeighbour() {
        // DO A CHANGE

        int tempNodeIndex = RandomStuff.between(1,orig_sol.ordering.length-1);

        Integer[] newOrdering = new Integer[orig_sol.ordering.length];

        for (int i = 0; i < orig_sol.ordering.length; i++)
            newOrdering[i] = orig_sol.ordering[i];

        newOrdering[tempNodeIndex-1] = orig_sol.ordering[tempNodeIndex];
        newOrdering[tempNodeIndex] = orig_sol.ordering[tempNodeIndex-1];


        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.numPages, newOrdering);


        // UPDATE INDICES

        // DUPLICATE SOLUTION
        for (int i = 0; i < orig_sol.numPages; i++)
            for (Edge e : orig_sol.getEdges(i))
                solution.addEdge(e,i);


        return solution;
    }

    @Override
    public String getName() {
        return "NodeNeighbourSwap";
    }
}
