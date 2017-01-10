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
        // Specify file path as execution argument

        int iterations = -1;
        int population_size = -1;

        Neighbourhood n = null;
        StepFunction f = null;

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        options.addOption("i",true,"Number of iterations");
        options.addOption("p",true,"Size of population");
        options.addOption("n",true,"Neighbourhood for local search");
        options.addOption("s",true,"Step-function for local search");
        options.addOption("np",true, "Parameter for certain neighbourhoods");
        options.addOption("m",true, "Parameter for certain neighbourhoods");

        try {
            if(args.length < 1)
                throw  new ParseException("No arguments");
            CommandLine cmd = parser.parse(options,args,false);

            // Making sure all needed things are defined
            for(Option o : options.getOptions()){
                if (o.getOpt().equals("np") || o.getOpt().equals("m"))  // optional options
                    continue;
                if(!cmd.hasOption(o.getOpt()))
                    throw new ParseException("Option " + o.getOpt() + " is missing");
            }


            instance = KPMPInstance.readInstance(args[0]);
            iterations = Integer.parseInt(cmd.getOptionValue('i'));
            population_size = Integer.parseInt(cmd.getOptionValue('p'));
            if (instance.getNumVertices() >= 200)
                population_size /= 5;


            // Neighbourhood
            int neighbourhood = Integer.parseInt(cmd.getOptionValue("n"));

            // Make sure parameters of chosen neighbourhood are defined
            Integer n_param = null;
            Integer m_param = null;

            if( neighbourhood == 0 || neighbourhood == 1 || neighbourhood == 2 )
                if (!cmd.hasOption("np"))
                    throw new ParseException("Option np is missing");
                else
                    n_param  = Integer.parseInt(cmd.getOptionValue("np"));

            if( neighbourhood == 2 )
                if (!cmd.hasOption('m'))
                    throw new ParseException("Option m is missing");
                else
                    m_param = Integer.parseInt(cmd.getOptionValue("m"));



            switch (neighbourhood){
                case 0:
                    n = new NNodeSwapNeighbourhood(n_param);
                    break;
                case 1:
                    n = new NEdgeFlipNeighbourhood(n_param);
                    break;
                case 2:
                    n = new MNFlipEdgeSwapNodeNeighbourhood(m_param,n_param);
                    break;
                case 3:
                    n = new NodeNeighbourSwapNeighbourhood();
                    break;
                case 4:
                    n = new EdgeNeighbourNeighbourhood(true);
                    break;
                default:
                    System.out.println("No neighbourhood selected");
                    System.exit(1);
                    break;
            }


            // StepFunction
            switch (Integer.parseInt(cmd.getOptionValue('s'))){
                case 0:
                    f = new FirstImprovementStepFunction();
                    break;
                case 1:
                    f = new RandomStepFunction();
                    break;
                case 2:
                    f = new BestImprovementStepFunction(null, 0);
                    break;
                default:
                    System.out.println("No step-function selected");
                    System.exit(1);
                    break;
            }


        } catch (ParseException e) {
            e.printStackTrace();
            formatter.printHelp("HybridHeuristic", "Start the heuristic on a given instance", options, "", true);
            System.exit(1);
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.exit(1);
        }



        System.out.println("--------------------");
        System.out.println("Successfully read instance:");
        System.out.println("Vertices: " + instance.getNumVertices());
        System.out.println("Book Pages: " + instance.getK());
//      System.out.println("Adjacency List: " + instance.getAdjacencyList());
        System.out.println("--------------------");


        GeneticAlgorithm g = new GeneticAlgorithmAndLocalSearch(iterations, new SimpleCrossingSelection(),new SimpleMutate(),
                new SwapRecombination(), initialPopulation(instance,population_size),n,f);


        KPMPSolution globalBest = g.execute();

        System.out.println("Global Best Found:");
        System.out.println(globalBest.crossings());



//        KPMPSolutionWriter w;
//        w = new KPMPSolutionWriter(instance.getK());
//        globalBest.insertIntoWriter(w);
//        try {
//            w.write(args[0] + "_Hybrid_Solution");
//        } catch (IOException e) {
//            System.out.println("Failed to  write file: " + e);
//        }

    }


    static List<KPMPSolution> initialPopulation(KPMPInstance instance, int size){


        List<KPMPSolution> initialPopulation = new ArrayList<KPMPSolution>();
        ConstructionHeuristic h = new RandomConstructionHeuristic();

        h.initialize(instance);

        for(int i = 0; i< size; i++){
            KPMPSolution s = h.getNextSolution();
            initialPopulation.add(s);
            if (i % 100 == 0)
                System.out.println(i );
        }

        return initialPopulation;
    }
}
