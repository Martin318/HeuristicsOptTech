package at.ac.tuwien.ac.heuoptws15;

/**
 * Created by cem on 29/10/16.
 */
public interface CollisionChecker {

    int countAllCrossingsWithNewEdge(Edge edge);
    void addEdge(Edge edge);
    int getCrossing();

}
