package at.ac.tuwien.ac.heuoptws15;

import java.util.List;

/**
 * Created by Martin on 24.11.2016.
 */
public class SimpleMutate extends MutationOperator {

    public List<KPMPSolution> mutate(List<KPMPSolution> population){

        // Mutate approx. 33% of the individuals.

        for(int i = 0; i< population.size()/3; i++){

            // The winner of the mutation lottery.
            KPMPSolution s = population.get(RandomStuff.between(0,population.size()-1));

//            System.out.println("Doing mutation on individual with "+ s.crossings() + " crossings.");

            doMutation(s);

//            System.out.println("After mutation: "+ s.crossings() + " crossings.");



        }




        return population;
    }

    private void doMutation(KPMPSolution s){

        if(s.pages.length == 1 ) return; // NOTHING TO DO HERE.


        // Move an edge to one other edge.

        int pageIndex = RandomStuff.between(0, s.pages.length-1);

        int edgeIndex = RandomStuff.between(0, s.pages[pageIndex].edges.size()-1);


        if(s.pages[pageIndex].edges.size() != 0){


            Edge e = s.pages[pageIndex].edges.get(edgeIndex);

            s.removeEdge(e,pageIndex);



            int moveIndex = pageIndex;

            while(moveIndex == pageIndex) {
                moveIndex = RandomStuff.between(0, s.pages.length - 1);
            }

            s.addEdge(e,moveIndex);


        }



    }





}
