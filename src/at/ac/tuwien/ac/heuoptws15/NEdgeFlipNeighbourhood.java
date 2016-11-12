package at.ac.tuwien.ac.heuoptws15;

import java.util.ArrayList;

/**
 * CREATED BY CEM ON 11/11/16.
 */
public class NEdgeFlipNeighbourhood  extends Neighbourhood {

    int N;
    KPMPSolution orig_sol;
    int pageIndex[];     // for every edge in orig solution, its corresponding page
    int pagesizes[];
    int edgeSize;

    int slotIndex[];        // The chosen edges at any given iteration
    int transferIndex[];    // The chosen page to which each chosen edge is flipped to

    public NEdgeFlipNeighbourhood( int N) {
        this.N = N;
    }

    public void setSolution(KPMPSolution sol){
        this.orig_sol = sol;


        pagesizes = new int[orig_sol.pages.length];
        edgeSize = 0;

        for (int i = 0; i < orig_sol.pages.length;i++){
            Page p = orig_sol.pages[i];
            edgeSize += p.edges.size();

            pagesizes[i] = p.edges.size() + (( i > 0)?pagesizes[i-1]:0);
        }

        if (this.N > edgeSize)
            N = edgeSize;
        pageIndex = new int[edgeSize];
        transferIndex = new int[N];
        slotIndex = new int[N];


        for( int k = 0, i = 0; i < edgeSize; i++){
          if (i >= pagesizes[k])
              k++;
           pageIndex[i] = k;    // CURRENT PAGE OF EDGE # (``NUMBER'') i
        }

        for( int i = 0; i < N; i++){
            slotIndex[i] = i;
            transferIndex[i] = ( i <  pagesizes[0])? 1 : 0;   // THE FIRST PAGE TO TRANSFER TO
        }


    }

    @Override
    public KPMPSolution getRandomNeighbour() {

        // SET WHICH NODES TO SWAP

        int tempSlotIndex[] = new int[N];
        int tempTransferIndex[] = new int[N];
        ArrayList<Integer> previousValues = new ArrayList<>();

        for( int i = 0;  i < N; i++){
            do{
                tempSlotIndex[i] = RandomStuff.between( 0,edgeSize -1);
            } while( previousValues.contains(tempSlotIndex[i]));
            previousValues.add(tempSlotIndex[i]);
            do{
                tempTransferIndex[i] = RandomStuff.between( 0,orig_sol.pages.length-1);
            } while ( tempTransferIndex[i] == pageIndex[tempSlotIndex[i]]);
        }

        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.pages.length, orig_sol.ordering);

        // DUPLICATE SOLUTION

        for (int i = 0; i < orig_sol.pages.length; i++)
            for (Edge e : orig_sol.pages[i].edges)
                solution.addEdge(e,i);

        // DO A CHANGE

        for(int i = 0; i < N; i++){
            int currentPage = pageIndex[tempSlotIndex[i]];
            int index = tempSlotIndex[i] - ((currentPage > 0)?pagesizes[currentPage-1]:0);
            Edge original = orig_sol.pages[currentPage].edges.get(index);
            Edge e = new Edge(original.getNameOfFirstVertex(),original.getNameOfSecondVertex());
            System.out.println("Edge " + e + "von page " + currentPage + "auf page " + tempTransferIndex[i]);

            solution.removeEdge(e,currentPage);
            solution.addEdge(e,tempTransferIndex[i]);
        }


        return solution;
    }

    public KPMPSolution getNextNeighbour() {

        if(orig_sol.pages.length == 1)
            return null; // NO edge swap possible

        // TERMINATION criterium

        if(slotIndex[0] >= edgeSize )
            return null;


        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.pages.length, orig_sol.ordering);

        // DUPLICATE SOLUTION

        for (int i = 0; i < orig_sol.pages.length; i++)
            for (Edge e : orig_sol.pages[i].edges)
                solution.addEdge(e,i);

        // DO A CHANGE

        for(int i = 0; i < N; i++){
            int currentPage = pageIndex[slotIndex[i]];
            int index = slotIndex[i] - ((currentPage > 0)?pagesizes[currentPage-1]:0);
            Edge original = orig_sol.pages[currentPage].edges.get(index);
            Edge e = new Edge(original.getNameOfFirstVertex(),original.getNameOfSecondVertex());
            solution.removeEdge(e,currentPage);
            solution.addEdge(e,transferIndex[i]);
        }

        // UPDATE THE INDICES TO INDICATE THE NEXT ELEMENT IN THE ORDERING W.R.T. THE ORIGINAL SOLUTION


        for( int i = N-1;  i >= 0 ;i-- ){       // only iterated if both transfer and slot are reset
            transferIndex[i]++;
            if (transferIndex[i] == pageIndex[slotIndex[i]]) // skip edge's current page
                transferIndex[i]++;
            if (transferIndex[i] >= orig_sol.pages.length){  // transferIndex reset
                transferIndex[i] = ( pageIndex[slotIndex[i]] == 0)? 1 : 0;
                slotIndex[i]++;
                if (i != 0 && slotIndex[i] >= edgeSize)          // slotIndex reset, not done at i = 0 to trigger termination
                    slotIndex[i] = i;
                else
                    break;
            }
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
