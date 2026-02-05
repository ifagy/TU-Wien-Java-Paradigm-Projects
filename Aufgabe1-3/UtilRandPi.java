import java.util.Random;

/**
 * Provides a collection of static utility functions.
 * <p>
 * Abstraction:
 * This class represents an abstraction at the module level. It is not instantiated
 * but bundles purely functional units for generating random values that are
 * needed at various points in the simulation.</p>
 */

public class UtilRandPi {

    /**
     * Calculates a random pollination probability(p) value with given {@code h0, h1}.
     *
     * @param h0 lower sunlight limit for flowering.
     * @param h1 upper sunlight limit for flowering
     * @return a random value between 0 and 1/(h1-h0).
     * <p>
     * Precondition:
     * # h1 > h0
     * <p>
     * Postcondition:
     * # result >= 0 && result < 1/(h1-h0)
     */
    public static double pi(double h0, double h1) {
        return rBetw(0, (1 / (h1 - h0)));

    }

    /**
     * Generates a random double value within a specified interval.
     *
     * @param min the lower bound of the interval.
     * @param max the upper bound of the interval.
     * @return a random number between min (inclusive) and max (exclusive).
     * <p>
     * Precondition:
     * # max >= min
     * <p>
     * Postcondition:
     * # result >= min && result < max
     */
    public static double rBetw(double min, double max) {
        Random rand = new Random();

        return min + (max - min) * rand.nextDouble();
    }

    /**
     * Generates a random double value with using Gaussian distribution within given borders.
     *
     * @param mean the peak of the distribution (Erwartungswert).
     * @param lowerBound the minimum allowed value (untere Schranke).
     * @param upperBound the maximum allowed value (obere Schranke).
     * @return a normally distributed random number within the specified bounds.
     * <p>
     * Precondition:
     * # lowerBound <= mean <= upperBound
     * <p>
     * Postcondition:
     * # result >= lowerBound && result <= upperBound
     */
    public static double rNormal(double mean, double lowerBound, double upperBound) {

        Random rand = new Random();

        // Help to limit the standard deviation
        double maxDistance = Math.max(mean - lowerBound, upperBound - mean);

        if (maxDistance <= 0.0) {
            return mean;
        }

        double stdDev = maxDistance / 3.0;
        double res;
        do {
            res = rand.nextGaussian() * stdDev + mean;
        } while (res < lowerBound || res > upperBound);

        return res;
    }

}


