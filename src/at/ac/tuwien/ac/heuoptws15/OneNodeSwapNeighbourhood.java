package at.ac.tuwien.ac.heuoptws15;

/**
 * Created by cem on 02/11/16.
 */
public class OneNodeSwapNeighbourhood extends Neighbourhood {

    KPMPSolution orig_sol;
    int nodeIndex = 0;
    int transferIndex = 1;


    @Override
    public KPMPSolution getNextNeighbour() {
        // TERMINATION criterium
        if(nodeIndex >= orig_sol.ordering.length-1)
            return null;

        // DO A CHANGE

        Integer[] newOrdering = new Integer[orig_sol.ordering.length];

        for (int i = 0; i < orig_sol.ordering.length; i++)
            newOrdering[i] = orig_sol.ordering[i];

        newOrdering[transferIndex] = orig_sol.ordering[nodeIndex];
        newOrdering[nodeIndex] = orig_sol.ordering[transferIndex];


        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.pages.length, newOrdering);


//        System.out.println("Generated neighbour. Swapped edge " + nodeIndex + " with " + transferIndex);


        // UPDATE INDICES

        transferIndex++;

        if (transferIndex >= orig_sol.ordering.length){
            nodeIndex++;
            transferIndex = nodeIndex +1;
        }



        // DUPLICATE SOLUTION

        for (int i = 0; i < orig_sol.pages.length; i++)
            for (Edge e : orig_sol.pages[i].edges)
                solution.addEdge(e,i);


        return solution;
    }

    @Override
    public void setSolution(KPMPSolution sol) {
        orig_sol = sol;
        nodeIndex = 0;
        transferIndex = 1;
    }

    @Override
    public KPMPSolution getRandomNeighbour() {
        // DO A CHANGE

        int tempNodeIndex = RandomStuff.between(0,orig_sol.ordering.length-1);
        int tempTransferIndex;
        do {
            tempTransferIndex = RandomStuff.between(0,orig_sol.ordering.length-1);
        } while (tempTransferIndex == tempNodeIndex);

        Integer[] newOrdering = new Integer[orig_sol.ordering.length];

        for (int i = 0; i < orig_sol.ordering.length; i++)
            newOrdering[i] = orig_sol.ordering[i];

        newOrdering[tempNodeIndex] = orig_sol.ordering[tempTransferIndex];
        newOrdering[tempTransferIndex] = orig_sol.ordering[tempNodeIndex];


        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.pages.length, newOrdering);

        // DUPLICATE SOLUTION

        for (int i = 0; i < orig_sol.pages.length; i++)
            for (Edge e : orig_sol.pages[i].edges)
                solution.addEdge(e,i);


        return solution;
    }

    @Override
    public String getName() {
        return "OneNodeSwap";
    }
}
