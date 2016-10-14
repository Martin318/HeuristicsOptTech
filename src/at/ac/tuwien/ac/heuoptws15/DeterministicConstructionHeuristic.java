package at.ac.tuwien.ac.heuoptws15;

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


        // TODO find good ordering
        int[] ordering = new int[instance.getNumVertices()];


        for (int i= 0; i< instance.getNumVertices(); i++){
            ordering[i] = i;
        }


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

    /**
     * Computes the degree by using the adjaceny matrix
     *
     * @param vertex
     * @return
     */
    public int getDegree(int vertex){
        int degree = 0;

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
