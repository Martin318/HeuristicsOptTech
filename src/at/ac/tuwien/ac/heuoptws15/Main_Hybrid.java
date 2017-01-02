package at.ac.tuwien.ac.heuoptws15;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.*;

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

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("i",true,"Number of iterations");
        options.addOption("p",true,"Size of population");
        options.addOption("n",true,"Neighbourhood for local search");
        options.addOption("s",true,"Stepfunction for local search");

        try {
            if (args.length < 4)
                throw new ParseException("invalid arguments");
            CommandLine cmd = parser.parse(options,args);
            instance = KPMPInstance.readInstance(args[0]);
            String s = cmd.getOptionValue('i');
            System.out.println(s);
            iterations = Integer.parseInt(cmd.getOptionValue('i'));
            population_size = Integer.parseInt(cmd.getOptionValue('p'));

            String neighbourhood_name = cmd.getOptionValue('n');

            if(neighbourhood_name.equals("0")) n = new NNodeSwapNeighbourhood(2);
            if(neighbourhood_name.equals("1")) n = new NEdgeFlipNeighbourhood(2);
            if(neighbourhood_name.equals("2")) n = new MNFlipEdgeSwapNodeNeighbourhood(2,2);
            if(neighbourhood_name.equals("3")) n = new NodeNeighbourSwapNeighbourhood();
            if(neighbourhood_name.equals("4")) n = new EdgeNeighbourNeighbourhood(true);

            // StepFunction

            String stepfunction_name = cmd.getOptionValue('s');

            if(stepfunction_name.equals("0")) f = new FirstImprovementStepFunction();
            if(stepfunction_name.equals("1")) f = new RandomStepFunction();
            if(stepfunction_name.equals("2")) f = new BestImprovementStepFunction(null, 0);


        } catch (ParseException e) {

            e.printStackTrace();
            System.exit(1);
        } catch (FileNotFoundException e) {
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
