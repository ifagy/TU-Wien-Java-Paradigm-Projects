import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import static java.util.Arrays.stream;

/**
 * Hier finden Sie Information darüber, wer was gemacht hat.
 * 1) Efe Bideci(12333450):
 * FlowerPatch, CoreLogic BA, BA.apply() , BA.sortPatch(), BA.creatRandom()
 *
 * 2) Kolozs Komlai (12341330): Test
 *
 * 3) Ozan Bükelek (12421916):
 * Location, nextPatch() and all its helper functions
 */
public class Test {

    // Functional Form: Output formatter
    private static final Function<FlowerPatch, String> formatPatch = patch ->
            String.format("Wert: %12.8f | Koordinaten: %s", patch.value(),
                    stream(patch.coordinates()).mapToObj(d -> String.format("%8.4f", d)).
                            collect(Collectors.joining(", ", "[", "]")));

    // Functional Forms: Comparison logic
    private static final BiFunction<Double, Double, Integer> maxCompare = (a, b) -> Double.compare(b, a);
    private static final BiFunction<Double, Double, Integer> minCompare = (a, b) -> Double.compare(a, b);

    public static void main(String[] args) {
        /*
         * =================================================================================
         * IMPERATIVE / FUNCTIONAL BOUNDARY
         * The code below contains side effects (System.out) and orchestrates the tests.
         * The core logic invoked (BA.apply) remains pure and functional.
         * =================================================================================
         */

        testSinFunction();
        testParaboloidFunction();
        testRastriginFunction();
    }

    // Test 1 -- Sinus -- Analyse
    private static void testSinFunction() {
        System.out.println("Test 1: Sinus-Funktion - Maxima Suche");
        System.out.println("----------------------------------------");

        Function<double[], Double> sinFunc = coords -> Math.sin(Math.toRadians(coords[0]));

        BA params = new BA(1, sinFunc, new double[]{-1800.0}, new double[]{1800.0}, maxCompare,
                8000, 1500, 200, 50, 100, 50, 0.2, 10);

        List<Location> expected = List.of(new Location(List.of(90.0)));

        long startTime = System.currentTimeMillis();
        List<FlowerPatch> result = BA.apply(params);
        long endTime = System.currentTimeMillis();

        System.out.println("Berechnungszeit: " + (endTime - startTime) + " ms");
        System.out.println("Gefundene Maxima (sin(X) in Grad):");

        result.stream().map(formatPatch).forEach(System.out::println);

        analyzeAngleResults(result, expected.get(0), 5.0);

        System.out.println();
    }

    // Test 2 -- Paraboloid -- Analyse
    private static void testParaboloidFunction() {
        System.out.println("Test 2: Paraboloid-Funktion - Minima Suche");
        System.out.println("----------------------------------------");

        Function<double[], Double> paraboloidFunc = coords -> {
            double x = coords[0];
            double y = coords[1];
            return Math.pow(x * x - 1, 2) + 0.5 * y * y;
        };

        BA params = new BA(2, paraboloidFunc, new double[]{-3.0, -3.0}, new double[]{3.0, 3.0},
                minCompare, 7000, 1500, 200, 50, 100, 50, 0.15, 8);

        List<Location> expected = List.of(new Location(List.of(1.0, 0.0)), new Location(List.of(-1.0, 0.0)));

        long startTime = System.currentTimeMillis();
        List<FlowerPatch> results = BA.apply(params);
        long endTime = System.currentTimeMillis();

        System.out.println("Berechnungszeit: " + (endTime - startTime) + " ms");
        System.out.println("Gefundene Minima des Paraboloids:");

        results.stream().map(formatPatch).forEach(System.out::println);

        analyzeDistanceResults(results, expected, 1e-2);

        System.out.println();
    }

    // Test 3 -- Rastrigin -- Analyse
    private static void testRastriginFunction() {
        System.out.println("Test 3: Rastrigin_Funktion (3D) - Minima Suche");
        System.out.println("----------------------------------------");

        Function<double[], Double> rastriginFunc = coords -> {
            double sum = 30.0;
            for (double xi : coords) {
                sum += Math.pow(xi, 2) - 10 * Math.cos(2 * Math.PI * xi);
            }
            return sum;
        };

        BA params = new BA(3, rastriginFunc, new double[]{-5.12, -5.12, -5.12}, new double[]{5.12, 5.12, 5.12},
                minCompare, 7000, 1500, 300, 60, 120, 60, 0.1, 12);

        List<Location> expected = List.of(new Location(List.of(0.0, 0.0, 0.0)));

        long startTime = System.currentTimeMillis();
        List<FlowerPatch> results = BA.apply(params);
        long endTime = System.currentTimeMillis();

        System.out.println("Berechnungszeit: " + (endTime - startTime) + " ms");
        System.out.println("Gefundene Minima der Rastrigin-Funktion:");

        results.stream().map(formatPatch).forEach(System.out::println);

        analyzeDistanceResults(results, expected, 1e-2);

        System.out.println("\n---- Alle Tests abgeschlossen ---");

    }

    /**
     * Analysiert 1D-Winkel Ergebnisse: prüft, ob gefundene Koordinaten nahe zum erwarteten Winkel sind
     *
     * @param results   Liste der FlowerPatch Ergebnisse
     * @param expected  Liste der erwarteten Locations
     * @param toleranceDeg  Toleranz in Grad
     */
    private static void analyzeAngleResults(List<FlowerPatch> results, Location expected, double toleranceDeg) {
        double expectedAngle = expected.getArgs().get(0);
        System.out.println("\nAnalyse: Erwarteter Winkel: " + expectedAngle + "°");

        for (int i = 0; i < Math.min(10, results.size()); i++) {
            double found = results.get(i).coordinates()[0];
            double diff = angularDistance(found, expectedAngle);
            System.out.printf("Rang %2d: gefunden= %8.4f°, Abstand(mod360)= %6.3f° %s%n",
                    i + 1, found, diff, (diff <= toleranceDeg ? "<-- OK" : ""));
        }
    }

    /**
     * Euklidische Distanzanalyse: vergleicht Top-Ergebnisse mit erwarteten Location
     *
     * @param results   Liste der FlowerPatch Ergebnisse
     * @param expected  Liste der erwarteten Locations
     * @param tol   Toleranz
     */
    private static void  analyzeDistanceResults(List<FlowerPatch> results, List<Location> expected, double tol){
        System.out.println("\nAnalyse: Abstand der Top-Ergebnisse zu erwarteten Optima:");

        for (int i = 0; i < Math.min(10, results.size()); i++) {
            double[] found = results.get(i).coordinates();
            double bestDist = Double.POSITIVE_INFINITY;
            for (Location loc : expected) {
                double[] exp = toDoubleArray(loc);
                double d = euclideanDistance(found, exp);
                if (d < bestDist){
                    bestDist = d;
                }
            }
            System.out.printf("Rang %2d: Wert= %12.8f | Distanz zum nächsten Optimum = %.6f %s%n",
                    i + 1, results.get(i).value(), bestDist, (bestDist <= tol ? "<-- OK" : ""));
        }
    }

    /**
     * Converts a Location object into an array
     *
     * @param loc   containing the expected coordinates
     * @return  a double[] containing the same coordinates in the same order
     */
    private static double[] toDoubleArray(Location loc) {
        return loc.getArgs().stream().mapToDouble(Double::doubleValue).toArray();
    }

    /**
     * Computes the Euclidean distance between two points in n-dimensional space
     *
     * @param a first coordinate vector
     * @param b second coordinate vector
     * @return the Euclidean distance between a und b
     */
    private static double euclideanDistance(double[] a, double[] b) {
        double sum = 0.0;
        for (int i = 0; i < Math.min(a.length, b.length); i++) {
            double d = a[i] - b[i];
            sum += d * d;
        }
        return Math.sqrt(sum);
    }

    /**
     * Normalizes an angle in degrees to the interval 0-360
     *
     * @param deg   an angle in degrees
     * @return  the normalized angle in the range 0-360
     */
    private static double norm360(double deg){
        double r = deg % 360.0;
        if (r < 0) r += 360.0;
        return r;
    }

    /**
     * Computes the smallest angular distance between two angles in degrees
     *
     * @param aDeg  first angel in degrees
     * @param bDeg  second angle in degrees
     * @return  the minimal distance between the two angles in degrees
     */
    private static double angularDistance(double aDeg, double bDeg){
        double a = norm360(aDeg);
        double b = norm360(bDeg);
        double diff = Math.abs(a - b);
        return Math.min(diff, 360.0 - diff);
    }

}
