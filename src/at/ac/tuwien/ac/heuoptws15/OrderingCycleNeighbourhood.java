package at.ac.tuwien.ac.heuoptws15;

import java.util.Arrays;

/**
 * Created by Martin on 02.11.2016.
 */
public class OrderingCycleNeighbourhood extends Neighbourhood {

    @Override
    public String getName() {
        return "OrderCycle";
    }

    KPMPSolution orig_sol;
    int cycleIndex;

    public void setSolution(KPMPSolution sol) {
        this.orig_sol = sol;
        cycleIndex = 1;
    }

    @Override
    public KPMPSolution getRandomNeighbour() {
        return null;
    }

    public KPMPSolution getNextNeighbour() {

        // TERMINATION criterium

        if (cycleIndex == orig_sol.ordering.length) {
            return null;
        }

        // DO A CHANGE

        Integer[] newOrdering = new Integer[orig_sol.ordering.length];

        for (int i = 0; i < orig_sol.ordering.length; i++) {
            newOrdering[i] = orig_sol.ordering[(i + cycleIndex) % orig_sol.ordering.length];
          //    newOrdering[i] = orig_sol.ordering[i];
        }

        System.out.println("Generated neighbour. Cycled ordering positively by " + cycleIndex + " places");


        cycleIndex++;







        KPMPSolution solution = new KPMPSolution(orig_sol.ordering.length, orig_sol.pages.length, newOrdering);

        // DUPLICATE SOLUTION

        for (int i = 0; i < orig_sol.pages.length; i++) {
            for (Edge e : orig_sol.pages[i].edges) {

                solution.addEdge(e, i);
            }
        }


        return solution;


    }
}