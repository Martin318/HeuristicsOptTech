package at.ac.tuwien.ac.heuoptws15;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Martin on 25.11.2016.
 */
public class SimpleCrossingSelection extends  SelectionOperator{

    public List<KPMPSolution> select(List<KPMPSolution> population){

        Collections.sort(population);

        List<KPMPSolution> selected = new ArrayList<KPMPSolution>();

        for(int i = 0; i< population.size()/2; i++){

            selected.add(population.get(i));

            System.out.println("Selected individual with " + population.get(i).crossings()  + " crossings.");
        }

        return selected;




    }
}
