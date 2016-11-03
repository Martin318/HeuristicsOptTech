package at.ac.tuwien.ac.heuoptws15;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Martin on 14.10.2016.
 */
public class RandomizedConstructionHeuristic extends ConstructionHeuristic {

    private KPMPInstance instance;
    private boolean createdSolution;
    private double alpha;


    public void initialize(KPMPInstance instance){
        this.instance = instance;
        this.createdSolution = false;
    }

    public KPMPSolution getNextSolution(){

        return createRandomizedSolution();


    }

    public RandomizedConstructionHeuristic(double alpha){
        this.alpha = Math.max(Math.min(alpha,0.0),1.0);
    }


    /**
     *  A simple heuristic that first determines the spine order on degree count
     *  and then assigns edges by first fit.
     *
     * @return  A possible solution
     */
    private KPMPSolution createRandomizedSolution(){
        KPMPSolution s;

        Integer[] ordering = new Integer[instance.getNumVertices()];


        for (int i= 0; i< instance.getNumVertices(); i++){
            ordering[i] = i;
      //      System.out.println("Degree of vertex " + i + " is " + getDegree(i));
        }

        int[] offsets = new int[instance.getNumVertices()];

        for( int i = 0; i < instance.getNumVertices(); i++)
            offsets[i] = (int) alpha*RandomWithin(0,instance.getNumVertices()-1);


        Arrays.sort(ordering, (int1, int2) -> {
            Integer firstDegree = getDegree(int1) + offsets[int1];
            Integer secondDegree = getDegree(int2)+ offsets[int2];
            return firstDegree.compareTo(secondDegree);
        });

        Integer[] copyOrdering = ordering.clone();
     //   System.out.println("vertices "+ instance.getNumVertices());

        // shift the highest values to the middle, by magic
        for (int i= 0; i< instance.getNumVertices(); i++) {
            int magic = (instance.getNumVertices() / 2) + (int) (Math.pow(-1.0,i)* (up(i) / 2));
            ordering[magic] = copyOrdering[i];
        }

    //    System.out.println("Ordering done.");

        s = new KPMPSolution(instance.getNumVertices(), instance.getK(),ordering);
        ///

        ArrayList<Edge> edges = new ArrayList<>();

        // Currently chooses greedily the first fitting assignment
        for (int x = 0; x < instance.getNumVertices(); x++)
            for (int y = x; y < instance.getNumVertices(); y++)
                if (instance.getAdjacencyMatrix()[x][y] == true) {
                    Edge temp= new Edge(x, y);
                    edges.add(temp);
                }

        Collections.shuffle(edges);
        for(Edge e : edges)
            s.addEdge(e,s.nextFreePage(e));

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


    private int RandomWithin(int a, int b){
        int lower = Math.min(a,b);
        int higher = Math.max(a,b);

        return (int) (lower + Math.random()*(higher - lower));
    }

    public String getName(){
        return "Randomized Construction Heuristic";
    }


}
