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


        for( int i = 0; i < first.size();i++) {
            KPMPSolution s = mating(first.get(i), second.get(i));
            children.add(s);
//            System.out.println("Created child with " + s.crossings() + " crossings");

            s = mating(first.get(i), second.get(i));
            children.add(s);
//            System.out.println("Created child with " + s.crossings() + " crossings");



        }
        return children;
    }

    private KPMPSolution mating(KPMPSolution parentOne, KPMPSolution parentTwo){
//        KPMPSolution chosen;
//        if (Math.random() > 0.5)
//            chosen = parentOne;
//        else
//            chosen = parentTwo;
//        Integer[] ordering = new Integer[chosen.ordering.length];
//        for(int i = 0; i < chosen.ordering.length;i++)
//            ordering[i] = new Integer(chosen.ordering[i]);


        Integer[] ordering;
        if (Math.random() > 0.5)
            ordering = cycleSwap(parentOne.ordering,parentTwo.ordering);
        else
            ordering = cycleSwap(parentTwo.ordering,parentOne.ordering);

        KPMPSolution child = new KPMPSolution(parentOne.ordering.length,parentOne.numPages,ordering);

        HashMap<Edge,Integer> edgesOne = new HashMap<>();
        int counter = 0;
        for (int i = 0; i < parentOne.numPages; i++){
            for (Edge edge : parentOne.getEdges(i))
                edgesOne.put(edge,counter);
            counter++;
        }

        HashMap<Edge,Integer> edgesTwo = new HashMap<>();
        counter = 0;
        for (int i = 0; i < parentTwo.numPages; i++){
            for (Edge edge : parentTwo.getEdges(i))
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


    /**
     *  Detects the first permutation cycle from mapping from ordering A to ordering B and applies it to orderingA
     *
     * @param orderingA
     * @param orderingB
     * @return  the ordering resulting from mapping the first permutation cycle to ordering A
     */
    private Integer[] cycleSwap(Integer[] orderingA, Integer[] orderingB){

        Integer[] orderingCompA = new Integer[orderingA.length];
        for (int i = 0; i < orderingA.length; i++)
            orderingCompA[orderingA[i]] = i;

        Integer[] swap = Arrays.copyOf(orderingA,orderingA.length);

        //find start of possible cycle
        int positionA = orderingA[0];
        int positionB= orderingB[0];

        int i = 0;
        while( i < orderingA.length-1 && positionA == positionB  ){
            i++;
            positionA = orderingA[i];
            positionB = orderingB[i];
        }

        if (orderingA[i] == orderingB[i])  // orderings are identical
            return orderingA;

        int begin = positionA;

        do{
            swap[i] = positionB;
            i = orderingCompA[positionB];
            positionB = orderingB[i];
        } while ( ( begin != positionB));
        swap[i] = begin;


        return swap;
    }

}
