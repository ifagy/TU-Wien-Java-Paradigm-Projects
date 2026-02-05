import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Utility class providing mathematical functions to optimize and comparators.
 */
public class Functions {


    /**
     * Returns the function f(x) based on ID.
     */
    public static Function<double[], Double> callFuncById(int id) {
        switch (id) {
            //sin
            case 0:
                return coords -> Math.sin(Math.toRadians(coords[0]));

            //ParaboloidFunction
            case 1:
                return coords -> {
                    double x = coords[0];
                    double y = coords[1];
                    return Math.pow(x * x - 1, 2) + 0.5 * y * y;
                };

            //RastriginFunction
            default:
                return coords -> {
                    double sum = 30.0;
                    for (double xi : coords) {
                        sum += Math.pow(xi, 2) - 10 * Math.cos(2 * Math.PI * xi);
                    }
                    return sum;
                };

        }
    }

    /**
     * Returns a comparator for optimization logic (Minimization or Maximization).
     */
    public static BiFunction<Double, Double, Integer> callCompareById(int id) {
        switch (id) {
            //minCompare
            case 0:
                return (a, b) -> Double.compare(a, b);

            //maxCompare
            case 1:
                return (a, b) -> Double.compare(b, a);
            default:
                return (a, b) -> Double.compare(b, a);


        }
    }
}
