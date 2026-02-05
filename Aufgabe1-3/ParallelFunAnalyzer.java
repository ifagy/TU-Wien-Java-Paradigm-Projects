import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ParallelFunAnalyzer
 * Performs parallel and functional analysis of multiple YearlySim results
 * Uses parallel streams, functional mapping, reductions, and statistical summaries
 *
 * Abstraction: This class abstracts the analysis of multiple YearlySim results and provides
 * parallel and functional evaluation methods. It encapsulates the statistical aggregation of
 * bee health, flower growth, and climate zones using Java Parallel Streams and functional operations
 *
 * STYLE: functional/parallel: Combines functional programming with parallel execution
 * */
public class ParallelFunAnalyzer {

    /**
     *  Analyze parallel averages and extremes for all simulations
     * @param sims List of completed YearlySim objects
     * @return Formatted statistical result
     *
     * Precondition: sims != null && !sims.isEmpty
     *               All YearlySim objects in the list are in valid state
     *
     * Postcondition: Returns a non-null String with statistical summary
     *                Empty input list returns No simulation to analyze
     *                All statistics (min <= avg <= max) are calculated correctly
     */
    public static String analyzeFunParallel(List<YearlySim> sims){
        if (sims == null || sims.isEmpty()) return "No simulations to analyze.";

        /// STYLE: functional/parallel - Parallel streams with stateless operations
        // These three parallel streams operate independently and are referentially transparent
        // because they don't modify external state and always produce the same results for the same input

        DoubleSummaryStatistics beeStats = sims.parallelStream().mapToDouble(YearlySim::getBeePopulationSize).summaryStatistics();

        DoubleSummaryStatistics flowerStats = sims.parallelStream().mapToDouble(YearlySim::getTotalYi).summaryStatistics();

        DoubleSummaryStatistics healthStats = sims.parallelStream().mapToDouble(YearlySim::getAverageBeeHealth).summaryStatistics();

        return String.format("\n=== Parallel Summary (Across %d Simulations) ===\n" +
                "Bee Population     ->  min: %.2f | avg: %.2f | max: %.2f\n" +
                "Flower Growth      ->  min: %.2f | avg: %.2f | max: %.2f\n" +
                "Bee Health avg     ->  min: %.2f | avg: %.2f | max: %.2f\n",
                sims.size(),
                beeStats.getMin(), beeStats.getAverage(), beeStats.getMax(),
                flowerStats.getMin(), flowerStats.getAverage(), flowerStats.getMax(),
                healthStats.getMin(), healthStats.getAverage(), healthStats.getMax());


    }


    /**
     * Groups simulations by climate zone (based ond daily humidity)
     * and calculates the average bee population per zone
     *
     * @param sims List of YearlySim objects
     * @return Map with climate zone as key and average bee population as value
     *
     * Precondition: sims != null && !sims.isEmpty
     *               All YearlySim objects have a valid weather data 0 <= humidity <= 1
     *               / bee population non negativ
     *
     * Postcondition: Returns a non-null Map contains the keys Arid, Temperate, Humid
     *
     */

    public static Map<String, Double> groupByClimateZone(List<YearlySim> sims) {

        // STYLE: functional/parallel - Concurrent grouping with reduction
        // groupingByConcurrent enables parallel grouping while averagingDouble
        // serves as a reduction operation. The function is referentially transparent
        // because it only operates on immutable properties of YearlySim objects
        return sims.parallelStream().collect(Collectors.groupingByConcurrent(sim -> {
            double humidity = sim.getWeather().getHumidity();
            if (humidity < 0.3) return "Arid";
            else if (humidity < 0.6) return "Temperate";
            else return "Humid";
        }, Collectors.averagingDouble(YearlySim::getBeePopulationSize)));
    }

    /**
     * Finds the simulation with the best balance between bee population and flower growth
     *
     * @param sims List of YearlySim objects
     * @return YearlySim object with maximum bee population * flower growth
     *
     * Precondition: sims != null && !sims.isEmpty
     *               All YearlySim objects have non-negativ population and growth values
     *
     * Postcondition: Returns the YearlySim with the highest fitness score, or null if list is empty
     *                Fitness score = beePopulation * flowerGrowth
     */
    public static YearlySim findOptimalSim(List<YearlySim> sims) {

        // STYLE: functional/parallel - Reduction with associative operation
        // The reduction operation is associative and can therefore be safely parallelized.
        // The function is referentially transparent because it only operates on immutable
        // properties of YearlySim objects and has no side effects
        return sims.parallelStream().reduce((a, b) -> {
            double scoreA = a.getBeePopulationSize() * a.getTotalYi();
            double scoreB = b.getBeePopulationSize() * b.getTotalYi();
            return scoreA > scoreB ? a : b;
        }).orElse(null);
    }

    /**
     * Performs all analyses and returns a full report
     *
     * @param sims List of YearlySim objects
     * @return String containing summary statistics, climate zone grouping, and optimal simulation
     *
     * Precondition: sims != null && !sims.isEmpty
     *               Each YearlySim object in sims has valid values
     *
     * Postcondition: Returns a non-null String containing formatted results from:
     *                analyzeFunParallel(sim), groupByClimateZone(sim), findOptimalSim(sims)
     *
     */
    public static String fullParallelFunReport(List<YearlySim> sims) {

        // STYLE: functional - Composition of pure functions
        // The three functions are called independently and their results are combined.
        // Since each function is referentially transparent, this composition is also referentially transparent
        String baseStats = analyzeFunParallel(sims);
        Map<String, Double> cliamteZones = groupByClimateZone(sims);
        YearlySim best = findOptimalSim(sims);

        StringBuilder sb = new StringBuilder(baseStats);
        sb.append("\n--- Climate Zone Grouping (avg BeePop per Zone) ---\n");
        cliamteZones.forEach((zone, avg) -> sb.append(String.format("%s: %.2f\n", zone, avg)));

        sb.append(analyzePlantNames(sims));

        if (best != null) {
            sb.append(String.format("\nOptimal Simulation -> BeePop: %.2f, FlowerYi: %.2f\n", best.getBeePopulationSize(), best.getTotalYi()));

        }

        return sb.toString();
    }

    /**
     * Analyzes statistics for each flower type across all given simulations
     *
     * @param sims List of YearlySim objects
     * @return A formatted String containing min, avg, max yi-values for each flower species
     *
     * Precondition: sims != null && !sims.isEmpty
     *               Each YearlySim object in sims has valid values
     *               yi >= 0 and names not null
     *
     * Postcondition: Returns a non-null String containing a formatted list of flower species statistics
     *                Empty input list returns No plants to analyze
     *
     */
    public static String analyzePlantNames(List<YearlySim> sims) {
        if (sims == null || sims.isEmpty()) return "No plants to analyze";

        FlowerGroup fg = sims.getFirst().getFlowerGroup();
        List<String> plantNames = fg.getPopulations().stream()
                .map(FlowerPopulation::getName)
                .toList();

        StringBuilder result = new StringBuilder("\n--- Flower Type Statistics ---\n");


        // STYLE: functional/parallel - Nested parallel streams with stateless mapping
        // For each plant, a parallel stream is created over all simulations.
        // The inner mapping operation is referentially transparent because it only
        // reads and processes immutable data without side effects
        plantNames.forEach(name -> {
            DoubleSummaryStatistics stats = sims.parallelStream().mapToDouble(sim -> sim.getFlowerGroup().getPopulations().stream()
                    .filter(fp -> fp.getName().equals(name))
                    .mapToDouble(FlowerPopulation::getYi).average().orElse(0.0)).summaryStatistics();

            result.append(String.format("%-12s ->  min: %.2f | avg: %.2f | max: %.2f\n",
                    name, stats.getMin(), stats.getAverage(), stats.getMax()));
        });

        return result.toString();
    }

}