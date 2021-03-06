package at.ac.tuwien.ac.heuoptws15;

import java.util.List;

/**
 * Created by Martin on 08.11.2016.
 */
public class GVNS {

    List<Neighbourhood> VND;
    List<Neighbourhood> VNS;


    public GVNS(List<Neighbourhood> VND, List<Neighbourhood> VNS){

        this.VND = VND;
        this.VNS = VNS;

    }


    public KPMPSolution search(KPMPSolution initial){

        KPMPSolution bestSol = initial;

        int k = 0;
        while(k < VNS.size()){

            VNS.get(k).setSolution(bestSol);
            KPMPSolution randomNeighbour = VNS.get(k).getRandomNeighbour(); // SHAKING

//            System.out.print("[");
//
//            for(int i= 0; i< initial.ordering.length; i++){
//                System.out.print(initial.ordering[i] + " ");
//            }
//
//            System.out.println("]");
//
//            System.out.print("[");
//
//            for(int i= 0; i< initial.ordering.length; i++){
//                System.out.print(randomNeighbour.ordering[i] + " ");
//            }
//
//            System.out.println("]");
//
//            System.out.println(initial);
//            System.out.println(randomNeighbour);

//            System.out.println("Non optimized neighbour: " + randomNeighbour.crossings() + " from neighbourhood " + VNS.get(k).getName());


            randomNeighbour = vndSearch(randomNeighbour);

//            System.out.println("Optimized neighbour: " + randomNeighbour.crossings() + " from neighbourhood " + VNS.get(k).getName());


            if(randomNeighbour.crossings() < bestSol.crossings()){
                bestSol = randomNeighbour;
                k=0;
            }
            else{
                k++;
            }
        }

        return bestSol;

    }

    private KPMPSolution vndSearch(KPMPSolution initial){

        int i = 0;
        KPMPSolution bestSol = initial;
        KPMPSolution current;


        while(i < VND.size()){

            FirstImprovementStepFunction s = new FirstImprovementStepFunction(bestSol, bestSol.crossings());
            SearchConfiguration config = new SearchConfiguration();
            config.setStepFunction(s);
            config.setNeighbourhood(VND.get(i));
            current =  config.getNextSolution(initial);
//
//            System.out.println("Best crossings: " + bestSol.crossings());
//            System.out.println("Current crossings: " + current.crossings());


            if(bestSol.crossings() > current.crossings()){
                bestSol = current;
//                System.out.println("Improved solution in VND!!!" + bestSol.crossings());
                i = 0;
            }
            else{
                i++;
            }
        }

        return bestSol;
    }

}
