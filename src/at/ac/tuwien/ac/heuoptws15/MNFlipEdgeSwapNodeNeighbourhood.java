package at.ac.tuwien.ac.heuoptws15;

import java.util.ArrayList;

/**
 * CREATED BY CEM ON 11/11/16.
 */
public class MNFlipEdgeSwapNodeNeighbourhood  extends Neighbourhood {

    int N;
    int M;
    KPMPSolution orig_sol;
    int pageIndex[];
    int pagesizes[];
    int transferIndex[];
    int edgeSize;
    int slotIndex[];        // The chosen edges at any given iteration

    int nodeTransferIndex[];

    public MNFlipEdgeSwapNodeNeighbourhood( int M, int N) {
        this.M = M+1;
        this.N = N;
    }

    public void setSolution(KPMPSolution sol){
        orig_sol = sol;


        // SET UP THE EDGE STUFF


        pagesizes = new int[orig_sol.numPages];
        edgeSize = 0;

        for (int i = 0; i < orig_sol.numPages;i++){
            edgeSize += sol.getEdges(i).size();

            pagesizes[i] = sol.getEdges(i).size() + (( i > 0)?pagesizes[i-1]:0);
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

        // SET UP THE NODE STUFF

        if (this.M >= orig_sol.ordering.length)
            M = orig_sol.ordering.length -1;

        nodeTransferIndex = new int[M-1];

        for( int i = 0;  i < M-1; i++)
            nodeTransferIndex[i] = i+1;


    }

    @Override
    public KPMPSolution getRandomNeighbour() {

        // SET WHERE TO FLIP EDGES

        int tempSlotIndex[] = new int[N];
        int tempTransferIndex[] = new int[N];
        ArrayList<Integer> previousValues = new ArrayList<>();

        for( int i = 0;  i < N; i++){
            do{
                tempSlotIndex[i] = RandomStuff.between( 0,edgeSize -1);
            } while( previousValues.contains(tempSlotIndex[i]));
            previousValues.add(tempSlotIndex[i]);
            do{
                tempTransferIndex[i] = RandomStuff.between( 0,orig_sol.numPages-1);
            } while ( tempTransferIndex[i] == pageIndex[tempSlotIndex[i]]);
        }

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

        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.numPages, newOrdering);

        // DUPLICATE SOLUTION

        for (int i = 0; i < orig_sol.numPages; i++)
            for (Edge e : orig_sol.getEdges(i))
                solution.addEdge(e,i);

        // DO A CHANGE  DOS

        // EDGE BIT

        for(int i = 0; i < N; i++){
            int currentPage = pageIndex[tempSlotIndex[i]];
            int index = tempSlotIndex[i] - ((currentPage > 0)?pagesizes[currentPage-1]:0);
            Edge original = orig_sol.getEdges(currentPage).get(index);
            Edge e = new Edge(original.getNameOfFirstVertex(),original.getNameOfSecondVertex());
            solution.removeEdge(e,currentPage);
            solution.addEdge(e,tempTransferIndex[i]);
        }


        return solution;
    }

    public KPMPSolution getNextNeighbour() {

        if(orig_sol.numPages == 1)
            return null; // NO edge swap possible

        // TERMINATION CRITERIUM EDGE

        if(slotIndex[0] >= edgeSize )
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
            newOrdering[i] = temp;
        }


        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.numPages, newOrdering);

        // DUPLICATE SOLUTION

        for (int i = 0; i < orig_sol.numPages; i++)
            for (Edge e : orig_sol.getEdges(i))
                solution.addEdge(e,i);

        // DO A CHANGE  DOS
        // EDGE BIT

        for(int i = 0; i < N; i++){
            int currentPage = pageIndex[slotIndex[i]];
            int index = slotIndex[i] - ((currentPage > 0)?pagesizes[currentPage-1]:0);
            Edge original = orig_sol.getEdges(currentPage).get(index);
            Edge e = new Edge(original.getNameOfFirstVertex(),original.getNameOfSecondVertex());
            solution.removeEdge(e,currentPage);
            solution.addEdge(e,transferIndex[i]);
        }

        // UPDATE THE INDICES TO INDICATE THE NEXT ELEMENT IN THE ORDERING W.R.T. THE ORIGINAL SOLUTION


        for( int i = N-1;  i >= 0 ;i-- ){       // only iterated if both transfer and slot are reset
            transferIndex[i]++;
            if (transferIndex[i] == pageIndex[slotIndex[i]]) // skip edge's current page
                transferIndex[i]++;
            if (transferIndex[i] >= orig_sol.numPages){  // transferIndex reset
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
        return M + " " +  N + " FlipEdgeSwapNode";
    }


}
