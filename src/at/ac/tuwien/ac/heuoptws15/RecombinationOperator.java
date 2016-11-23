package at.ac.tuwien.ac.heuoptws15;

import java.util.List;

/**
 * Created by Martin on 23.11.2016.
 */
public abstract class RecombinationOperator {

    public abstract List<KPMPSolution> recombine(List<KPMPSolution> population);
}
