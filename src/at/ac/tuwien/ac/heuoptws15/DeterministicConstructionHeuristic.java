package at.ac.tuwien.ac.heuoptws15;

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

    public String getName(){
        return "Deterministic Construction Heuristic";
    }

}
