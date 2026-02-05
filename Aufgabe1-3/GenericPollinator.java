import java.util.Random;

public class GenericPollinator implements Pollinator {
    /**
     * Invariants:
     * # populationSize >= 0
     * # demandR >= 0.1 && demandR <= 1.2
     * # pollinationPower >= 0 && pollinationPower <= 1
     */

    private double populationSize;

    private double demandR;
    private double pollinationPower= UtilRandPi.rNormal(0.6,0,1);

    /**
     * Creates a new population of "other" pollinators.
     * @param populationSize The fixed size of this population.
     * <p>
     * Precondition:
     * # populationSize >= 0
     * <p>
     * Postcondition:
     * # this.populationSize == populationSize
     * # demandR is set to a random value in the range [0.1, 1.2]
     * # pollinationPower is set to a random value in the range [0, 1]
     */
    //STYLE: Object oriented.
    public GenericPollinator(double populationSize) {

        this.populationSize = populationSize;
        this.demandR =UtilRandPi.rNormal(0.7,0.1,1.2);
    }

    /**
     * Creates a new population of "other" pollinators.
     * @param populationSize The fixed size of this population.
     * @param pollinationPower The fixed pollination power of this population.
     * <p>
     * Precondition:
     * # populationSize >= 0
     * # pollinationPower >= 0
     * <p>
     * Postcondition:
     * # this.populationSize == populationSize
     * # this.pollinationPower == pollinationPower
     * # demandR is set to a random value in the range [0.1, 1.2]
     */
    //STYLE: Object oriented.
    public GenericPollinator(double populationSize, double pollinationPower) {
        this.populationSize = populationSize;
        this.pollinationPower = pollinationPower;
    }


    /**
     * Precondition:
     * # nectarAcquired >= 0 (Inherited from Pollinator interface)
     * <p>
     * Postcondition:
     * # populationSize is updated by a random factor based on the ratio (nectarAcquired / getDemand()).
     */
    //STYLE: procedural (if - else cases).
    @Override
    public void vegetation_One_Day(double nectarAcquired) {
        double k=0.23;
        double ratio = nectarAcquired / getDemand();
        if (ratio >=1.2) populationSize *=  UtilRandPi.rNormal(1.2+k,1.1+k,1.3+k);
        else if(ratio>1) populationSize *= UtilRandPi.rNormal(1.1+k,1+k,1.2+k);
        else if (ratio>0.8) populationSize *= UtilRandPi.rNormal(0.85+k,0.8+k,1+k);
        else populationSize = UtilRandPi.rNormal(0.65+k,0.5+k,0.8+k);




    }

    /**
     * Precondition: (None)
     * <p>
     * Postcondition:
     * - populationSize is reduced by multiplying with a random
     * survival ratio in the range [0.1, 0.3].
     */
    //STYLE: procedural, sequential computation with randomization.
    @Override
    public void winterComes() {
        Random rand = new Random();
        double live_ratio = 0.1 + rand.nextDouble() * 0.2;
        populationSize=  populationSize*live_ratio;
    }

    //Getters: STYLE: Object oriented.

    /**
     * Precondition: (None)
     * <p>
     * Postcondition:
     * # #result == this.populationSize
     * # result >= 0
     */
    @Override
    public double getPopulationSize() {
        return populationSize;
    }


    /**
     * Precondition: (None)
     * <p>
     * Postcondition:
     * # result == this.populationSize * this.demandR
     * # result >= 0
     */
    @Override
    public double getDemand() {
        return populationSize*demandR;
    }

    /**
     * Precondition: (None)
     * <p>
     * Postcondition:
     * # result == this.pollinationPower
     * # result >= 0
     */
    @Override
    public double getPollinationPower() {
        return pollinationPower;
    }

}
