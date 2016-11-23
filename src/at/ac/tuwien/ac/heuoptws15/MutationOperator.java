package at.ac.tuwien.ac.heuoptws15;

import java.util.List;

/**
 * Created by Martin on 23.11.2016.
 */
public abstract class MutationOperator {


    public abstract List<KPMPSolution> mutate(List<KPMPSolution> population);
}
