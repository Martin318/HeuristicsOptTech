package at.ac.tuwien.ac.heuoptws15;

/**
 * Created by cem on 10/11/16.
 */
public class NNodeSwapNeighbourhood  extends Neighbourhood  {
    int N;
    KPMPSolution orig_sol;
    int nodeIndex[];
    int transferIndex[];

    public NNodeSwapNeighbourhood(int N){
        this.N = N;

    }

    @Override
    public KPMPSolution getNextNeighbour() {
        // TERMINATION CRITERIUM
        if(transferIndex[0] >= orig_sol.ordering.length-1)
            return null;

        // DO A CHANGE
        Integer[] newOrdering = new Integer[orig_sol.ordering.length];
        for (int i = 0; i < orig_sol.ordering.length; i++)
            newOrdering[i] = orig_sol.ordering[i];

        for( int i = 0;  i < N-1; i++){
            newOrdering[transferIndex[i]] = orig_sol.ordering[nodeIndex[i]];
            newOrdering[nodeIndex[i]]     = orig_sol.ordering[transferIndex[i]];
        }

        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.pages.length, newOrdering);


//        System.out.println("Generated neighbour. Swapped edge " + nodeIndex + " with " + transferIndex);


        // UPDATE INDICES


        for( int i = N-2;  i >= 0 ;i-- ){
            transferIndex[i]++;

            if (i != 0 && transferIndex[i] >= orig_sol.ordering.length)
                transferIndex[i] = i+1;
            else
                break;
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

        if (this.N >= orig_sol.ordering.length)
            N = orig_sol.ordering.length -1;
        nodeIndex = new int[N-1];
        transferIndex = new int[N-1];

        for( int i = 0;  i < N-1; i++){
            nodeIndex[i] = i;
            transferIndex[i] = i+1;
        }
    }

    @Override
    public KPMPSolution getRandomNeighbour() {

        // SET WHICH NODES TO SWAP

        int tempNodeIndex[] = new int[N-1];
        int tempTransferIndex[] = new int[N-1];

        for( int i = 0;  i < N-1; i++){
            tempNodeIndex[i] = i;
            tempTransferIndex[i] = RandomStuff.between(i+1,orig_sol.ordering.length-1);
        }

        // DO A CHANGE

        Integer[] newOrdering = new Integer[orig_sol.ordering.length];
        for (int i = 0; i < orig_sol.ordering.length; i++)
            newOrdering[i] = orig_sol.ordering[i];

        for( int i = 0;  i < N-1; i++){
            newOrdering[transferIndex[i]] = orig_sol.ordering[nodeIndex[i]];
            newOrdering[nodeIndex[i]]     = orig_sol.ordering[transferIndex[i]];
        }

        for(int i = 0; i < newOrdering.length; i++){
            System.out.print(newOrdering[i] + " ");

        }

        System.out.println(" Ordering done");


        for(int i = 0; i < newOrdering.length; i++){
            System.out.print(orig_sol.ordering[i] + " ");

        }

        System.out.println(" Ordering done");


        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.pages.length, newOrdering);

        // DUPLICATE SOLUTION

        for (int i = 0; i < orig_sol.pages.length; i++)
            for (Edge e : orig_sol.pages[i].edges)
                solution.addEdge(e,i);


        return solution;
    }

    @Override
    public String getName() {
        return N + "NodeSwap";
    }
}
