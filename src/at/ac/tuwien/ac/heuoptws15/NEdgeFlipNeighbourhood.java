package at.ac.tuwien.ac.heuoptws15;

/**
 * CREATED BY CEM ON 11/11/16.
 */
public class NEdgeFlipNeighbourhood  extends Neighbourhood {

    int N;
    KPMPSolution orig_sol;
    int pageindex[];
    int pagesizes[];
    int transferIndex[];

    public NEdgeFlipNeighbourhood( int N) {
        this.N = N;
    }

    public void setSolution(KPMPSolution sol){
        orig_sol = sol;

        int edgeSize = 0;
        pagesizes = new int[orig_sol.pages.length];
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
           transferIndex[i] = ( k == 0)? 1 : 0;   // THE FIRST PAGE TO TRANFER TO
        }

    }

    @Override
    public KPMPSolution getRandomNeighbour() {

        // SET WHICH NODES TO SWAP


        int tempTransferIndex[] = new int[N];

        for( int i = 0;  i < N-1; i++)
            tempTransferIndex[i] = RandomStuff.between(( pageindex[i] == 0)? 1 : 0,orig_sol.pages.length-1);



        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.pages.length, orig_sol.ordering);

        // DUPLICATE SOLUTION

        for (int i = 0; i < orig_sol.pages.length; i++)
            for (Edge e : orig_sol.pages[i].edges)
                solution.addEdge(e,i);

        // DO A CHANGE

        for(int i = 0; i < N; i++){
            int index;
            if ( pageindex[i] > 0)
                index = i - pagesizes[pageindex[i]-1];
            else
                index = i;
            Edge original = orig_sol.pages[pageindex[i]].edges.get(index);
            Edge e = new Edge(original.getNameOfFirstVertex(),original.getNameOfSecondVertex());
            solution.removeEdge(e,pageindex[i]);
            solution.addEdge(e,tempTransferIndex[i]);
        }


        return solution;
    }

    public KPMPSolution getNextNeighbour() {

        if(orig_sol.pages.length == 1)
            return null; // NO edge swap possible

        // TERMINATION criterium

        if(transferIndex[0] >= orig_sol.pages.length )
            return null;


        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.pages.length, orig_sol.ordering);

        // DUPLICATE SOLUTION

        for (int i = 0; i < orig_sol.pages.length; i++)
            for (Edge e : orig_sol.pages[i].edges)
                solution.addEdge(e,i);

        // DO A CHANGE

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



        return solution;
    }


    @Override
    public String getName() {
        return N + "EdgeFlip";
    }


}
