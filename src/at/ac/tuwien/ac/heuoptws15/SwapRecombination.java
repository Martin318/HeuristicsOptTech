package at.ac.tuwien.ac.heuoptws15;

import java.util.*;

/**
 * Created by cem on 24/11/16.
 */
public class SwapRecombination extends  RecombinationOperator {

    @Override
    public List<KPMPSolution> recombine(List<KPMPSolution> population) {
        int length = population.size();

        Collections.shuffle(population);

        List<KPMPSolution> first = population.subList(0,length / 2);
        List<KPMPSolution> second = population.subList(length / 2,length );

        List<KPMPSolution> children = new ArrayList<>();


        for( int i = 0; i < first.size();i++)
            children.add(mating(first.get(i),second.get(i)));

        return children;
    }

    private KPMPSolution mating(KPMPSolution parentOne, KPMPSolution parentTwo){
        KPMPSolution chosen;
        if (Math.random() > 0.5)
            chosen = parentOne;
        else
            chosen = parentTwo;
        Integer[] ordering = new Integer[chosen.ordering.length];
        for(int i = 0; i < chosen.ordering.length;i++)
            ordering[i] = new Integer(chosen.ordering[i]);

        KPMPSolution child = new KPMPSolution(parentOne.ordering.length,parentOne.pages.length,ordering);

        HashMap<Edge,Integer> edgesOne = new HashMap<>();
        int counter = 0;
        for (Page page : parentOne.pages){
            for (Edge edge : page.edges)
                edgesOne.put(edge,counter);
            counter++;
        }

        HashMap<Edge,Integer> edgesTwo = new HashMap<>();
        counter = 0;
        for (Page page : parentTwo.pages){
            for (Edge edge : page.edges)
                edgesTwo.put(edge,counter);
            counter++;
        }

        int halfwaypoint = edgesOne.entrySet().size() / 2;

        counter = 0;
        for (Map.Entry<Edge,Integer> entry : edgesOne.entrySet()){
            Edge e = entry.getKey();
            int page;
            if ( counter > halfwaypoint)
                page = entry.getValue();
            else{
                page = edgesTwo.get(e);
            }


            child.addEdge(new Edge(e.getNameOfFirstVertex(),e.getNameOfSecondVertex()),page);
            counter++;
        }


        return  child;
    }

}
