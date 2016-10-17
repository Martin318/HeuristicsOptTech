package at.ac.tuwien.ac.heuoptws15;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Martin on 14.10.2016.
 */
public class DeterministicConstructionHeuristic extends ConstructionHeuristic {
    private KPMPInstance instance;
    private boolean createdSolution;


    public void initialize(KPMPInstance instance){
        this.instance = instance;
        this.createdSolution = false;
    }

    public KPMPSolution getNextSolution(){

        if(!createdSolution){
            createdSolution = true;
            return createDeterministicSolution();
        }
        return null;

    }

    /**
     *  A simple heuristic that first determines the spine order on degree count
     *  and then assigns edges by first fit.
     *
     * @return  A possible solution
     */
    private KPMPSolution createDeterministicSolution(){
        KPMPSolution s = new KPMPSolution(instance.getNumVertices(), instance.getK());

        Integer[] ordering = new Integer[instance.getNumVertices()];


        for (int i= 0; i< instance.getNumVertices(); i++){
            ordering[i] = i;
            System.out.println("Degree of vertex " + i + " is " + getDegree(i));
        }

        Arrays.sort(ordering, (int1, int2) -> { return getDegree(int1).compareTo(getDegree(int2));});

        Integer[] copyOrdering = ordering.clone();
        System.out.println("vertices "+ instance.getNumVertices());

        // shift the highest values to the middle, by magic
        for (int i= 0; i< instance.getNumVertices(); i++) {
            int magic = (instance.getNumVertices() / 2) + (int) (Math.pow(-1.0,i)* (up(i) / 2));
            ordering[magic] = copyOrdering[i];
        }

        s.ordering = ordering;

        System.out.println("Ordering done.");

        ///


        ActiveEdgeDataStructure a = new ActiveEdgeDataStructure(instance.getNumVertices(), ordering);

        for (int x = 0; x < instance.getNumVertices(); x++) {
            for (int y = x; y < instance.getNumVertices(); y++)
                if (instance.getAdjacencyMatrix()[x][y] == true) {
                    Edge temp = new Edge(y, x);

                    a.addEdge(temp);

                }
        }







        System.out.println("Active Edge Array creation done.");




        // Currently chooses greedily the first fitting assignment
        for (int x = 0; x < instance.getNumVertices(); x++) {
            for (int y = x; y < instance.getNumVertices(); y++)
                if (instance.getAdjacencyMatrix()[x][y] == true) {
                    Edge temp;
                    if (s.smallerInOrdering(x, y))
                        temp = new Edge(x, y);
                    else
                        temp = new Edge(y, x);

                    s.pages[s.nextFreePage(temp)].edges.add(temp);
                }
        }

        return s;
    }

    private int up(int n ){
        if (n == 0)
            return 0;

        return (n % 2 == 0)? n : n+1;
    }

    /**
     * Computes the degree by using the adjacency matrix
     *
     * @param vertex
     * @return
     */
    public Integer getDegree(int vertex){
        Integer degree = 0;

        boolean[][] adjacenyMatrix = this.instance.getAdjacencyMatrix();

        for(int i = 0; i < this.instance.getNumVertices(); i++)
            if (adjacenyMatrix[vertex][i])
                degree++;

        return degree;
    }

    public String getName(){
        return "Deterministic Construction Heuristic";
    }



}
