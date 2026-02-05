import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Functional definition of the Bees Algorithm context.
 * Represents the immutable configuration and the functional pipeline
 * for the optimization process.
 */
public class BA {

    // Parameters defining the optimization context and algorithm constraints
    private final int a; // Parameter dimension
    private final Function<double[], Double> f; // Objective function
    private final double[] minCordValues; // Lower bounds
    private final double[] maxCordValues; // Upper bounds
    private final BiFunction<Double, Double, Integer> cCompare; // Comparator logic ( -1: left is better)
    private final int t; // Max iterations
    private final int n; // Scout bees
    private final int m; // Selected patches
    private final int e; // Elite patches
    private final int p; // Elite recruits
    private final int q; // Non-elite recruits
    private final int g; // Global search bees (n-m)
    private final double s; // Neighborhood size ratio
    private final int r; // Result count

    // Represents a transition function that evolves one generation of patches to the next.
    @FunctionalInterface
    interface StepFunction extends Function<List<FlowerPatch>, List<FlowerPatch>> {}

    public BA(int a, Function<double[], Double> f,
              double[] minCordValues, double[] maxCordValues,
              BiFunction<Double, Double, Integer> cCompare,
              int t, int n, int m, int e, int p, int q, double s, int r) {
        this.a = a;
        this.f = f;
        this.minCordValues = minCordValues;
        this.maxCordValues = maxCordValues;
        this.cCompare = cCompare;
        this.t = t;
        this.n = n;
        this.m = m;
        this.e = e;
        this.p = p;
        this.q = q;
        this.s = s;
        this.r = r;

        this.g = n - m;
    }

    private static final Random rand = new Random();

    /**
     * Entry point for the Bees Algorithm execution pipeline.
     * Transforms an initial random population through t evolutionary steps.
     *
     * @param params Configuration context
     * @return List of best found solutions
     */
    public static List<FlowerPatch> apply(BA params) {

        // Initial state: Randomly distributed scout bees
        List<FlowerPatch> initialSearch = Stream.generate(() -> createRandom(params)).limit(params.n).toList();



        // Definition of the evolution step as a functional form
        StepFunction stepFunction = current -> nextPatch(current, params);
        //todo nextPatch()

        // Functional Iteration: State t -> State t+1
        List<FlowerPatch> endSearch = Stream.iterate(initialSearch, stepFunction::apply)
                .limit(params.t + 1)
                .reduce((cur, next) -> next)
                .orElse(initialSearch);

        // Final projection: Sorted and limited results
        return sortPatch(endSearch, params).stream().limit(params.r).toList();
    }



    /**
     * Transition logic mapping a current population to the next generation.
     * Selects elite/best sites, performs local recruitment, and mixes with global exploration.
     */
    private static List<FlowerPatch> nextPatch(List<FlowerPatch> currentPatch, BA params) {
        // Slicing the population based on fitness
        List<FlowerPatch> mBestPatches = mBest(currentPatch, params);
        List<FlowerPatch> elitePatches = elitePatch(mBestPatches, params);
        List<FlowerPatch> restPatches = restOfM(mBestPatches, params);


        // Local Exploitation: Recruit bees for intensive search around promising patches
        List<FlowerPatch> recElite = elitePatches.stream().map(patch -> localRecruitmentSearch(patch, params.p, params)).toList();
        List<FlowerPatch> recRest = restPatches.stream().map(patch -> localRecruitmentSearch(patch, params.q, params)).toList();

        // Global Exploration: Random search to avoid local optima
        List<FlowerPatch> globalSearch = Stream.generate(() -> createRandom(params)).limit(params.g).toList();

        // Aggregation of all sub-populations
        return Stream.of(recElite, recRest, globalSearch).flatMap(List::stream).toList();


    }

    /**
     * Projection of a patch center to a random point within the defined neighborhood radius (s).
     */
    private static double[] generateCoordinations(FlowerPatch patch, BA params) {
        return IntStream.range(0, params.a).mapToDouble(i -> {
            double center = patch.coordinates()[i];
            double d = params.maxCordValues[i] - params.minCordValues[i];
            double r = params.s * d;

            double min = Math.max(params.minCordValues[i], center - r);
            double max = Math.min(params.maxCordValues[i], center + r);

            return min + (max - min) * rand.nextDouble();
        }).toArray();
    }

    /**
     * Local optimization process for a specific patch.
     * Returns the best solution found within the neighborhood of the seed patch.
     */
    private static FlowerPatch localRecruitmentSearch(FlowerPatch patch, int beeNum, BA params) {
        List<FlowerPatch> recruit = Stream.generate(() -> {
                    double[] coords = generateCoordinations(patch, params);
                    double val = params.f.apply(coords);
                    return new FlowerPatch(val, coords);
                })
                .limit(beeNum).toList();

        //concat for 2 simultanious streams
        return Stream.concat(recruit.stream(), Stream.of(patch))
                .min((patch1, patch2) -> params.cCompare.apply(patch1.value(), patch2.value()))
                .orElse(patch); //edge case: if patch is empty.
    }



    /**
     * Filter/Projection:
     * Selects the subset of size m with the highest fitness values.
     */
    private static List<FlowerPatch> mBest(List<FlowerPatch> currentPatch, BA params) {
        return sortPatch(currentPatch, params).stream().limit(params.m).toList();
    }

    /**
     * Partitioning:
     * Extracts the top e (elite) elements from the already sorted best patches.
     */
    private static List<FlowerPatch> elitePatch(List<FlowerPatch> sortedPatch, BA params) {
        return sortedPatch.subList(0, params.e);
    }

    /**
     * Partitioning:
     * Extracts the remaining (m-e) elements from the sorted best patches.
     */
    private static List<FlowerPatch> restOfM(List<FlowerPatch> sortedPatch, BA params) {
        return sortedPatch.subList(params.e, params.m);
    }


    /**
     * Ordering of patches based on the objective function and comparison logic.
     */
    private static List<FlowerPatch> sortPatch(List<FlowerPatch> patch, BA params) {
        return patch.stream()   //make it stream
                .sorted((patch1, patch2) -> params.cCompare.apply(patch1.value(), patch2.value())) // sort using c
                .toList();
    }

    /**
     * Creation of a random solution within the global search space bounds.
     */
    private static FlowerPatch createRandom(BA params) {
        double[] coords = IntStream.range(0, params.a)       // create array[params.a]
                .mapToDouble(i -> {                             // fill array with random parameters
                    double min = params.minCordValues[i];
                    double max = params.maxCordValues[i];
                    return min + (max - min) * rand.nextDouble();
                })
                .toArray();

        double val = params.f.apply(coords); // get the result for that coordinate
        return new FlowerPatch(val, coords);

    }
}
