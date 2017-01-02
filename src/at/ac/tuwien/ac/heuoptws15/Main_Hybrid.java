package at.ac.tuwien.ac.heuoptws15;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cem on 21.12.2016.
 */
public class Main_Hybrid {


    public static void main(String[] args) {


        KPMPInstance instance = null;
        // Specify file path as execution arugment

        int iterations = -1;
        int population_size = -1;

        Neighbourhood n = null;
        StepFunction f = null;


        try {
            if (args.length > 4) {
                instance = KPMPInstance.readInstance(args[0]);
                iterations = Integer.parseInt(args[1]);
                population_size = Integer.parseInt(args[2]);

                // Neighourhood name

                String neighbourhood_name = args[3];

                if(neighbourhood_name.equals("0")) n = new NNodeSwapNeighbourhood(2);
                if(neighbourhood_name.equals("1")) n = new NEdgeFlipNeighbourhood(2);
                if(neighbourhood_name.equals("2")) n = new MNFlipEdgeSwapNodeNeighbourhood(2,2);
                if(neighbourhood_name.equals("3")) n = new NodeNeighbourSwapNeighbourhood();
                if(neighbourhood_name.equals("4")) n = new EdgeNeighbourNeighbourhood(true);


                if(n == null) throw new RuntimeException("Neighbourhood not set!");

                // StepFunction

                String stepfunction_name = args[4];


                if(stepfunction_name.equals("0")) f = new FirstImprovementStepFunction();
                if(stepfunction_name.equals("1")) f = new RandomStepFunction();
                if(stepfunction_name.equals("2")) f = new BestImprovementStepFunction(null, 0);

                if(f == null) throw new RuntimeException("Step function not set!");



            }
            else {
                System.out.println("Specify file path as first execution argument, number of iterations, population size, neighbourhood-name, step-function-name.");
                System.exit(1);
            }

        } catch (FileNotFoundException fi) {
            System.out.println("File not found.");
            System.exit(1);
        }




        System.out.println("--------------------");
        System.out.println("Sucessfully read instance:");
        System.out.println("Vertices: " + instance.getNumVertices());
        System.out.println("Book Pages: " + instance.getK());
//        System.out.println("Adjacency List: " + instance.getAdjacencyList());
        System.out.println("--------------------");



        GeneticAlgorithm g = new GeneticAlgorithmAndLocalSearch(iterations, new SimpleCrossingSelection(),new SimpleMutate(),
                new SwapRecombination(), initialPopulation(instance,population_size),n,f);


        KPMPSolution globalBest = g.execute();

        System.out.println("////// GLOBAL BEST ////////  " + globalBest.crossings() );



        KPMPSolutionWriter w;
        w = new KPMPSolutionWriter(instance.getK());
        globalBest.insertIntoWriter(w);
        try {
            w.write(args[0] + "_Hybrid_Solution");
        } catch (IOException e) {
            System.out.println("Failed to  write file: " + e);
        }


    }


    static List<KPMPSolution> initialPopulation(KPMPInstance instance, int size){


        List<KPMPSolution> initialPopulation = new ArrayList<KPMPSolution>();
        ConstructionHeuristic h = new RandomConstructionHeuristic();

        h.initialize(instance);

        for(int i = 0; i< size; i++){
            KPMPSolution s = h.getNextSolution();
            initialPopulation.add(s);
            initialPopulation.add(s);

//            System.out.println(i + " crossings: " + initialPopulation.get(i).crossings());

        }

        return initialPopulation;
    }
}
