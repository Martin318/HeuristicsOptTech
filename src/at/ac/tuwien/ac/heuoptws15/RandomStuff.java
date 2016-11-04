package at.ac.tuwien.ac.heuoptws15;

/**
 * Created by cem on 04/11/16.
 */
public class RandomStuff {


    public static int between(int a, int b){
        double one = (double) Math.min(a,b);
        double other = (double) Math.max(a,b);
        return (int) Math.round( Math.random()*(other - one) + one);
    }
}
