import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Parameter Transfer Object (Record).
 * Encapsulates all configuration parameters for the Bees Algorithm
 * to simplify the constructor of ExecuteBA and Worker.
 */
public record Params(int a, int f,
                     int cCompare,
                     int t, int n, int m, int e, int p, int q, double s, int r, int b, int k, double[][][] w) {
}
