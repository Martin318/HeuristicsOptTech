package at.ac.tuwien.ac.heuoptws15;

import java.util.List;

/**
 * Created by cem on 27/10/16.
 */
public abstract class Neighbourhood {
    public abstract KPMPSolution getNextNeighbour();

    public abstract void setSolution(KPMPSolution sol);

    public abstract KPMPSolution getRandomNeighbour();

    public abstract String getName();

}
