package at.ac.tuwien.ac.heuoptws15;

import java.util.List;

/**
 * Created by Martin on 24.11.2016.
 */
public class SimpleMutate extends MutationOperator {

    public List<KPMPSolution> mutate(List<KPMPSolution> population){

        // Mutate approx. 10% of the individuals.

        for(int i = 0; i< population.size()/10; i++){

            // The winner of the mutation lottery.
            KPMPSolution s = population.get(RandomStuff.between(0,population.size()));

            doMutation(s);

        }




        return population;
    }

    private void doMutation(KPMPSolution s){


        // Move an edge to one other edge.

        int pageIndex = RandomStuff.between(0, s.pages.length);

        int edgeIndex = RandomStuff.between(0, s.pages[pageIndex].edges.size());


        if(s.pages[pageIndex].edges.size() != 0){


            Edge e = s.pages[pageIndex].edges.get(edgeIndex);

            s.pages[pageIndex].edges.remove(e);

            int moveIndex =  RandomStuff.between(0,s.pages.length-1);

            s.pages[moveIndex].edges.add(e);


        }



    }





}
