package at.ac.tuwien.ac.heuoptws15;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cem on 29/10/16.
 */
public class OneEdgeFlipNeighbourhood extends Neighbourhood {
    @Override
    public List<KPMPSolution> getNeighbours(KPMPSolution solution) {
        List<KPMPSolution> neighbours = new ArrayList<KPMPSolution>();

        for(int i = 0; i < solution.pages.length; i++)
            for(Edge edge : solution.pages[i].edges)
              for(int j = i; j < solution.pages.length; j++){
                  KPMPSolution sol = solution.clone();
                  sol.removeEdge(edge,i);
                  sol.addEdge(edge,j);
                  neighbours.add(sol);
              }

        return neighbours;

    }
}
