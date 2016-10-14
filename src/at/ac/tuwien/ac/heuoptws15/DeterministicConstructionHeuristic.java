package at.ac.tuwien.ac.heuoptws15;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.Arrays;
import java.util.Comparator;

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

    private KPMPSolution createDeterministicSolution(){
        // TODO implement real Heuristic


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

        // TODO find good assignment of edges

        int pageIndex = 0;

        for (int y = 0; y < instance.getNumVertices(); y++){
            for (int x = 0; x < instance.getNumVertices(); x++){

                if(instance.getAdjacencyMatrix()[x][y] == true){
                    s.pages[pageIndex % instance.getK()].edges.add(new Edge(x,y));
                    pageIndex++;


                }
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
