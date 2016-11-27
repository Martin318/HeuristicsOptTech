package at.ac.tuwien.ac.heuoptws15;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cem on 23/11/16.
 *
 * Implements a straight-forward fitness proportion selection. The higher the individuals fitness, the more likely
 * it is to be selected
 *
 */
public class FitnessProportionSelection extends SelectionOperator {
    @Override
    public List<KPMPSolution> select(List<KPMPSolution> population) {

        int fitnessSum = population.stream().mapToInt(e -> GeneticAlgorithm.fitness(e)).sum();
        List<KPMPSolution> chosen = new ArrayList<>();

        while (chosen.size() < (population.size() / 2))
            for (KPMPSolution sol : population){
                // decide if selecting
                double chanceToSelect = GeneticAlgorithm.fitness(sol) / (double) fitnessSum;

                if( Math.random() < chanceToSelect)
                    chosen.add(sol);
            }

        return chosen;
    }
}
