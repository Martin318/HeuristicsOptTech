package at.ac.tuwien.ac.heuoptws15;

/**
 * CREATED BY CEM ON 11/11/16.
 */
public class MNFlipEdgeSwapNodeNeighbourhood  extends Neighbourhood {

    int N;
    int M;
    KPMPSolution orig_sol;
    int pageindex[];
    int transferIndex[];

    int nodeTransferIndex[];

    public MNFlipEdgeSwapNodeNeighbourhood( int M, int N) {
        this.M = M;
        this.N = N;
    }

    public void setSolution(KPMPSolution sol){
        orig_sol = sol;


        // SET UP THE EDGE STUFF


        int edgeSize = 0;
        int pagesizes[] = new int[orig_sol.pages.length];
        int i = 0;
        for (Page p : orig_sol.pages){
            edgeSize += p.edges.size();
            if ( i > 0)
                pagesizes[i] = pagesizes[i-1] + p.edges.size();
            else
                pagesizes[0] = p.edges.size();
            i++;
        }
        if (this.N > edgeSize)
            N = edgeSize;
        transferIndex = new int[N];
        pageindex = new int[N];


        int k = 0;
        for( i = 0; i < N; i++){
            if (i >= pagesizes[k])
                k++;
            pageindex[i] = k;    // CURRENT PAGE OF EDGE # (``NUMBER'') i
            transferIndex[i] = ( k == 0)? 1 : 0;   // THE FIRST PAGE TO TRANSFER TO
        }

        // SET UP THE NODE STUFF

        if (this.M >= orig_sol.ordering.length)
            M = orig_sol.ordering.length -1;

        nodeTransferIndex = new int[M-1];

        for( i = 0;  i < M-1; i++)
            nodeTransferIndex[i] = i+1;


    }

    @Override
    public KPMPSolution getRandomNeighbour() {

        // SET WHERE TO FLIP EDGES


        int tempTransferIndex[] = new int[N];

        for( int i = 0;  i < N; i++)
            tempTransferIndex[i] = RandomStuff.between(( pageindex[i] == 0)? 1 : 0,orig_sol.pages.length-1);


        // SET WHICH NODES TO SWAP

        int tempNodeTransferIndex[] = new int[M-1];

        for( int i = 0;  i < M-1; i++)
            tempNodeTransferIndex[i] = RandomStuff.between(i+1,orig_sol.ordering.length-1);


        // DO A CHANGE  UNO

        // NODE BIT


        Integer[] newOrdering = new Integer[orig_sol.ordering.length];
        for (int i = 0; i < orig_sol.ordering.length; i++)
            newOrdering[i] = orig_sol.ordering[i];

        for( int i = 0;  i < M-1; i++){
            int temp = newOrdering[tempNodeTransferIndex[i]];
            newOrdering[tempNodeTransferIndex[i]] = newOrdering[i];
            newOrdering[i] = temp;
        }

        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.pages.length, newOrdering);

        // DUPLICATE SOLUTION

        for (int i = 0; i < orig_sol.pages.length; i++)
            for (Edge e : orig_sol.pages[i].edges)
                solution.addEdge(e,i);

        // DO A CHANGE  DOS

        // EDGE BIT

        for(int i = 0; i < N; i++){
            Edge original = orig_sol.pages[pageindex[i]].edges.get(i);
            Edge e = new Edge(original.getNameOfFirstVertex(),original.getNameOfSecondVertex());
            solution.removeEdge(e,pageindex[i]);
            solution.addEdge(e,tempTransferIndex[i]);
        }


        return solution;
    }

    public KPMPSolution getNextNeighbour() {

        if(orig_sol.pages.length == 1)
            return null; // NO edge swap possible

        // TERMINATION criterium EDGE

        if(transferIndex[0] >= orig_sol.pages.length )
            return null;

        // TERMINATION CRITERIUM NODE
        if(nodeTransferIndex[0] >= orig_sol.ordering.length-1)
            return null;


        // DO A CHANGE  UNO
        // NODE BIT


        Integer[] newOrdering = new Integer[orig_sol.ordering.length];
        for (int i = 0; i < orig_sol.ordering.length; i++)
            newOrdering[i] = orig_sol.ordering[i];


        for( int i = 0;  i < M-1; i++) {
            int temp = newOrdering[nodeTransferIndex[i]];
            newOrdering[nodeTransferIndex[i]] = newOrdering[i];
        }


        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.pages.length, newOrdering);

        // DUPLICATE SOLUTION

        for (int i = 0; i < orig_sol.pages.length; i++)
            for (Edge e : orig_sol.pages[i].edges)
                solution.addEdge(e,i);

        // DO A CHANGE  DOS
        // EDGE BIT

        for(int i = 0; i < N; i++){
            Edge original = orig_sol.pages[pageindex[i]].edges.get(i);
            Edge e = new Edge(original.getNameOfFirstVertex(),original.getNameOfSecondVertex());
            solution.removeEdge(e,pageindex[i]);
            solution.addEdge(e,transferIndex[i]);
        }

        // UPDATE THE INDICES TO INDICATE THE NEXT ELEMENT IN THE ORDERING W.R.T. THE ORIGINAL SOLUTION


        for( int i = N-1;  i >= 0 ;i-- ){
            transferIndex[i]++;

            if (i != 0 && transferIndex[i] >= orig_sol.pages.length)
                transferIndex[i] = ( pageindex[i] == 0)? 1 : 0;
            else
                break;
        }

        for( int i = M-2;  i >= 0 ;i-- ){
            nodeTransferIndex[i]++;

            if (i != 0 && nodeTransferIndex[i] >= orig_sol.ordering.length)
                nodeTransferIndex[i] = i+1;
            else
                break;
        }




        return solution;
    }


    @Override
    public String getName() {
        return M + N + "FlipEdgeSwapNode";
    }


}
