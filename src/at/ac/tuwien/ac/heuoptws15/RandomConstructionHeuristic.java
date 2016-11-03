package at.ac.tuwien.ac.heuoptws15;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Martin on 03.11.2016.
 */
public class RandomConstructionHeuristic extends ConstructionHeuristic {

    private KPMPInstance instance;


    public void initialize(KPMPInstance instance){
        this.instance = instance;
    }

    public KPMPSolution getNextSolution(){

        return createRandomSolution();

    }


    /**
     *  A simple heuristic that first determines the spine order on degree count
     *  and then assigns edges by first fit.
     *
     * @return  A possible solution
     */
    private KPMPSolution createRandomSolution() {
        KPMPSolution s;


        ArrayList<Integer> order = new ArrayList<Integer>();

        for (int i = 0; i < instance.getNumVertices(); i++) {
            order.add(i);
        }

        Collections.shuffle(order);

        Integer[] ordering = new Integer[instance.getNumVertices()];

        for(int i = 0; i< instance.getNumVertices(); i++){
            ordering[i] = order.get(i);
        }


        s = new KPMPSolution(instance.getNumVertices(), instance.getK(), ordering);
        ///

        ArrayList<Edge> edges = new ArrayList<>();

        // Currently chooses greedily the first fitting assignment
        for (int x = 0; x < instance.getNumVertices(); x++)
            for (int y = x; y < instance.getNumVertices(); y++)
                if (instance.getAdjacencyMatrix()[x][y] == true) {
                    Edge temp = new Edge(x, y);
                    s.addEdge(temp, x % s.pages.length);

                }

        return s;
    }



    private int RandomWithin(int a, int b){
        int lower = Math.min(a,b);
        int higher = Math.max(a,b);

        return ThreadLocalRandom.current().nextInt(lower, higher + 1);
    }


    public String getName(){
        return "Random Construction Heuristic";
    }


}
