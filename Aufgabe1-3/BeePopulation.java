import java.util.Random;
import static java.lang.Math.*;

/**
 * Describes the abstraction of a bee colony as an object.
 *<p>
 * Abstraction:
 * Each instance of this class represents a distinct bee colony with a specific
 * population size. The class encapsulates the logic for the growth and reduction of the
 * bee population depending on external factors like food supply and seasons.</p>
 */

public class BeePopulation implements Pollinator {
    /**
     * class invariants:
     * populationSize >= 0
     * random != null
     * healthStatus >= 0 && healthStatus <= 1
     * accumulatedStress >= 0 && accumulatedStress <= 1
     */

    private double populationSize;
    private final Random random = new Random();

    private double healthStatus = 1.0;
    private  double accumulatedStress = 0.0;

    /**
     * Initializes a new bee colony with a given initial population size.
     ** @param populationSize the initial number of bees.
     *
     * Preconditions: - Ensured by class invariants.
     * Postconditions: - Object's populationSize is set to the given value.
     */
    //STYLE: Object oriented
    public BeePopulation(double populationSize) {
        this.populationSize = populationSize;
    }


    /**
     * Simulates the daily change in population based on the
     * nectar *received* in competition.
     * @param nectarAcquired the amount of nectar received.

     * Preconditions: - nectarAcquired >= 0

     * Postconditions:
     * - Population size is updated according to the nectar amount / demand ratio.
     * - Health status is updated according to the nectar amount / demand ratio with the consideration of class invariants.
     */
    // STYLE: procedural: vegetation_One_Day calculates the daily updates step by step with if-else statments.
    @Override
    public void vegetation_One_Day(double nectarAcquired) {
        double demand = getDemand();
        if (demand <= 0) return;

        // Ratio of nectar received to demand
        double ratio = nectarAcquired / demand;

        // Growth rate depends on the ratio
        double changeFactor;
        if (ratio >= 1.0) {
            changeFactor = 0.025 + random.nextGaussian() * 0.0005;
        } else if (ratio > 0.5) {
            changeFactor = (ratio - 1.0) * 0.05; // smaller Reduction
        } else if (ratio <= 0.3) {
            changeFactor = -0.05 - random.nextDouble() * 0.05; // up to -10%
        } else {
            changeFactor = -0.03 - random.nextDouble() * 0.03; // up to -6%
        }
        double healthModifier = 0.5 + 0.5 * healthStatus;

        populationSize *= (1.0 + changeFactor * healthModifier);
        // populationSize = max(0.0, populationSize);

        updateHealthStatus(ratio);
    }

    /**
     * Updates the health status of the bee population.
     * Continuous function based on food supply.
     * @param nectarRatio the amount of nectar received.
     *
     * Preconditions: - nectarRatio >= 0
     * Postconditions: Updates the health status of the bee population according to the nectarRatio, with the consideration of class invariants.
     */
    // STYLE: Object oriented (object behaviour) and procedural (sequentially)
    private void updateHealthStatus (double nectarRatio) {
        double targetHealth;
        if (nectarRatio >= 1.0) {
            targetHealth = 1.0;
        } else {
            targetHealth = 1.0 - exp(-2.0 * nectarRatio);
        }

        healthStatus = 0.9 * healthStatus + 0.1 * targetHealth;

        if (nectarRatio < 0.7) {
            accumulatedStress += 0.01 * (0.8 - nectarRatio);
        } else {
            accumulatedStress *= 0.95;
        }

        accumulatedStress = min(1.0, max(0.0, accumulatedStress));
    }

    /**
     * Simulates the reduction of the population during the resting phase (winter).
     *
     * Postconditions:
     * - Population is reduced according to the changeRate, healthStatus and accumulatedStress, with the consideration of class invariants.
     * - The accumulatedStress is set to 0.
     */
    public void winterComes() {
        double changeRate = 0.20 + random.nextGaussian() * 0.05;
        changeRate = max(0.10, min(0.30, changeRate));

        double healthModifier = 0.5 + 0.5 * healthStatus;
        double stressPenalty = 1.0 - 0.3 * accumulatedStress;

        double SurvivalRate = changeRate * healthModifier * stressPenalty;
        populationSize *= SurvivalRate;

        accumulatedStress = 0.0;
    }

    /**
     * @return the number(double) of bees in the population.
     *
     * Postcondition: The returned value is equal to the current populationSize.
     */
    public double getPopulationSize() {
        //if (populationSize < 1.0) {return 0.0;}
        return populationSize;
    }

    //Getters: STYLE: Object oriented.
    @Override
    public double getDemand() {
        return this.populationSize;
    }

    /**
     * @return the pollination power of the population.
     *
     * Postcondition: The returned value is equal to the half of the current health status of the bee population plus 0.5
     */
    @Override
    public double getPollinationPower() {
        return 0.5 + 0.5 * healthStatus;
    }

    /**
     * @return the health Status of the population.
     *
     * Postcondition: The returned value is equal to the current health status of the bee population.
     */
    public double getHealthStatus(){
        return healthStatus;
    }

}


