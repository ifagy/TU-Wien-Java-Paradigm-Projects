// STYLE: Objektorientierter Stil – Die Simulation berücksichtigt unterschiedliche Bestäuberarten
// als modular erweiterbare Klassen.
public interface Pollinator {

    /**
     * Updates the pollinator's state for one day, based on
     * the nectar received.
     * @param nectarAcquired The nectar received.
     * <p>
     * Precondition:
     * # nectarAcquired >= 0 (Acquired nectar cannot be negative)
     * <p>
     * Postcondition:
     * # The pollinator's internal state is updated based on the acquired nectar.
     */
    public void vegetation_One_Day(double nectarAcquired);

    /**
     * Simulates the population change during the resting phase (winter).
     * <p>
     *  Precondition: (None)
     * <p>
     *  Postcondition:
     *  # The pollinator's population is updated based on a winter survival rate.
     */
    public void winterComes();

    /**
     * Returns the current population size.
     * @return the population size.
     * <p>
     * Precondition: (None)
     * <p>
     * Postcondition:
     * # result >= 0 (Population cannot be negative)
     * # result == current population size
     */
    public double getPopulationSize();

    /**
     * Returns the nectar demand of this population (used for competition).
     * @return the nectar demand.
     * <p>
     * Precondition: (None)
     * <p>
     * Postcondition:
     * # result >= 0 (Demand cannot be negative)
     * # result == current nectar demand of the population
     */
    public double getDemand();

    /**
     * Returns the relative pollination effectiveness (1.0 for wild bees).
     * @return the effectiveness factor.
     * <p>
     * Precondition: (None)
     * <p>
     * Postcondition:
     * # result >= 0 (Effectiveness cannot be negative)
     */
    public double getPollinationPower();




}
