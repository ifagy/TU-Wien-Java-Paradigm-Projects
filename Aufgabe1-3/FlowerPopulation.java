import java.util.List;
import java.util.Random;
import static java.lang.Math.*;

/**
 * Represents the abstraction of a single flower species as an object.
 *
 * Abstraction:
 * Each instance of this class models a population of a specific flower species and manages
 * its specific properties such as growth strength (yi), fraction in bloom (bi), and seed quality (si).
 * The class encapsulates the complex biological processes of growth, flowering, and
 * reproduction in response to environmental conditions.
 */

/**
 * GOOD OOP: Klassenzusammenhalt ist hoch.
 * WHY: This class has a single, well-defined responsibility:
 * It encapsulates the entire state (bzw. yi, bi, si, stress)
 * and behavior (updateDaily, updateRestPhase)
 * of one single flower population.
 */

public class FlowerPopulation {
    // STYLE: object-oriented: Encapsulates the state and behavior of a single flower population; (yi, bi, si, qi, pi)

    /**
     * INVARIANT:
     * yi >= 0.0
     * 0.0 <= bi <= 1.0
     * 0.0 <= si <= 1.0
     * cMinus < cPlus
     * fMinus < fPlus
     * hMinus < hPlus
     * 0 < qi < 1/15
     * 0 < pi < 1/(hPlus-hMinus)
     * -1.0 <= moistureStress <= 1.0
     * stressDays >= 0
     */

    private boolean beeComp = false; // Bee pollinization bonus compatibility
    private double yi; // Growth power
    private double bi = 0.0; // Fraction currently in bloom
    private double si = 0.0; // Quality of seed development

    private double cPlus, cMinus; // Reproduction boundaries
    private double fPlus, fMinus; // Soil moisture limits
    private double hPlus, hMinus; // Flowering sunlight limits

    private double qi; // Flowering intensity
    private double pi; // Pollination probability
    private String name;

    private double totalPollinationTracker; // for calculating growth bonus at restPhase
    private double beePollinationTracker; // for calculating growth bonus restPhase

    private int stressDays = 0; //the number of stess days
    private boolean lastTooDry = false; // the last stress day was too dry
    private boolean lastTooWet = false; // the last stress day was too wet
    private double moistureStress = 0.0; // stress from dry or wet conditions
    private final Random random = new Random();
    /**
     * Constructor to create a new flower population with specific biological parameters.
     *
     * Precondition: Ensured by class invariants
     * Postcondition: A consistent flower population has been created
     *                bi, si = 0.0 at start
     *                moistureStress, stressDays = 0 no initial stress
     *
     * @param name   species name
     * @param yi     initial growth strength
     * @param cMinus minimum reproduction factor
     * @param cPlus  maximum reproduction factor
     * @param fMinus lower soil moisture limit
     * @param fPlus  upper soil moisture limit
     * @param hPlus  upper sunlight limit for flowering
     * @param hMinus lower sunlight limit for flowering
     * @param qi     flowering rate parameter
     * @param pi     pollination probability per day
     */
    public FlowerPopulation(String name, double yi, double cMinus, double cPlus,
                            double fMinus, double fPlus, double hPlus, double hMinus,
                            double qi, double pi) {
        this.yi = yi;
        this.cMinus = cMinus;
        this.cPlus = cPlus;
        this.hPlus = hPlus;
        this.hMinus = hMinus;
        this.fMinus = fMinus;
        this.fPlus = fPlus;
        this.qi = qi;
        this.pi = pi;
        this.name = name;
    }

    /**
     * Copy constructor to create a deep copy of another FlowerPopulation instance.
     * Precondition: Ensured by class invariants
     * Postcondition: deep copy of the original population has been created
     *
     * BAD: Copy constructor does not copy all fields
     * CAUSE: These fields were added later and forgotten in copy constructor
     * SOLVE: Complete copy of all fields
     *
     * @param other The instance to copy.
     */
    //STYLE: Object oriented.
    public FlowerPopulation(FlowerPopulation other) {
        this.yi = other.yi;
        this.bi = other.bi;
        this.si = other.si;
        this.cPlus = other.cPlus;
        this.cMinus = other.cMinus;
        this.fPlus = other.fPlus;
        this.fMinus = other.fMinus;
        this.hPlus = other.hPlus;
        this.hMinus = other.hMinus;
        this.qi = other.qi;
        this.pi = other.pi;
        this.totalPollinationTracker = other.totalPollinationTracker;
        this.beePollinationTracker = other.beePollinationTracker;
        this.beeComp = other.beeComp;
        this.name = other.name;
    }

    /**
     * Calculates soil moisture stress
     * @param f           current soil moisture
     *
     * Precondition: 0 <= f <= 1
     *               o < fMinus < fPlus < 1
     *
     * Postcondition: moistureStress has been updated
     *                stressDays has been adjusted based on stress duration
     *                lastTooDry and lastTooWet have been updated
     */
    //STYLE: Object oriented (object behaviour) and procedural (sequential logic).
    private void updateMoistureStress(double f) {

        // soil moisture stress calculation
        double deviation = 0.0;
        boolean tooDry = false;
        boolean tooWet = false;
        boolean extreme = false;

        if (f <= fMinus / 2.0) {
            deviation = (fMinus - f) / fMinus;
            tooDry = true;
            extreme = true;
        } else if (f < fMinus) {
            deviation = (fMinus - f) / fMinus;
            tooDry = true;
        } else if (f >= fPlus * 2.0) {
            deviation = (f - fPlus) / fPlus;
            tooWet = true;
            extreme = true;
        } else if (f > fPlus) {
            deviation = (f - fPlus) / fPlus;
            tooWet = true;
        }

        //Track stress of same type
        if (tooDry || tooWet) {
            if ((tooDry && lastTooDry) || (tooWet && lastTooWet)) {
                stressDays++;
            } else {
                stressDays = 1;
            }
        } else {
            stressDays = max(0, stressDays - 1);
        }

        double stressFactor = 0.05;
        if (extreme) stressFactor *= 1.5;

        if (tooDry){
            if (moistureStress < 0) moistureStress = 0; // reset opposite stress
            moistureStress = min(moistureStress + deviation * stressFactor * (1 + 0.1 * stressDays), 1.0);
        } else if (tooWet) {
            if (moistureStress > 0) moistureStress = 0; // reset opposite stress
            moistureStress = max(moistureStress - deviation * stressFactor * (1 + 0.1 * stressDays), -1.0);
        } else {
            //recovery slowly too zero
            if (moistureStress > 0) moistureStress = max(0, moistureStress - 0.05);
            else if (moistureStress < 0) {
                moistureStress = min(0, moistureStress + 0.05);
            }
        }

        lastTooWet = tooWet;
        lastTooDry = tooDry;
    }


    /**
     * Updates the state of the flower population for a single simulation day.
     *
     * Precondition: 0 <= f <= 1
     *               h >= 0
     *               d >= 0
     *               pollinators != null
     *               n >= 0
     *               currentDay >= 1
     *               All Pollinator objects in the list are valid
     *
     * Postcondition: yi has been adjusted based on moisture stress
     *                bi has been updated based on sunlight hours
     *                si has been increased based on pollination
     *                moistureStress has been updated
     *                totalPollinationTracker and beePollinationTracker have been updated
     *
     * BAD: Too many parameters
     * CAUSE: Gradual extension of simulation without refactoring
     * SOLVE: Could be encapsulated in an Environment object
     *
     * GOOD: Dynamic binding: instanceof check allows extensibility with new pollinator types
     *
     * @param f           current soil moisture
     * @param h           cumulative sunlight hours in current season
     * @param d           sunlight hours of the current day (day length)
     * @param pollinators A list of all active pollinator populations
     * @param n           total nectar supply from all flowers
     * @param currentDay  current day index (unused but kept for compatibility)
     */
    //STYLE : Object oriented (object behaviour) and procedural implementation wise(if - else, loops)
    public void updateDaily(double f, double h, double d, List<Pollinator> pollinators, double n, int currentDay) {

        // update soil moisture stress
        updateMoistureStress(f);

        // Continuous exponential reduction for realism
        yi *= exp(-0.1 * abs(moistureStress));
        // yi = max(0.0, yi);

        // Flowering fraction dynamics based on cumulative sunlight
        double optimalH = (hPlus + hMinus) / 2.0;
        double sigma = (hPlus - hMinus) / 3.0;
        double sunlightEffect = exp(-pow(h - optimalH, 2) / (2 * pow(sigma, 2)));

        if (h >= hMinus && h < hPlus) {
            bi = min(1.0, max(0.0, bi + qi * sunlightEffect * (d + 3)));
        } else if (h >= hPlus){
            bi = min(1.0, max(0.0, bi - qi * sunlightEffect *  (d + 3)));
        }

            // Part A: calculate si via pollination success and track bee pollination share
        double totalPollinationPower = 0.0;
        double totalBeePollinationPower = 0.0;

        for (Pollinator p : pollinators) {
            double pPower = p.getPopulationSize() * p.getPollinationPower();
            totalPollinationPower += pPower;
            if (p instanceof BeePopulation) {
                totalBeePollinationPower += pPower;
            }
        }

        double pollinationSuccessRate = 1.0;
        if (n > 0.0) {
            if (totalPollinationPower < n) {
                pollinationSuccessRate = totalPollinationPower / n;
            }
        } else {
            pollinationSuccessRate = 0.0;
        }

        double dailyPollinationAmount = pi * bi * (d + 1.0) * pollinationSuccessRate;
        dailyPollinationAmount *= (0.95 + 0.1 * random.nextDouble()); // slight random factor

        si = min(1.0, si + dailyPollinationAmount);

        double beePolFraction = 0.0;
        if (totalPollinationPower > 0.0) {
            beePolFraction = totalBeePollinationPower / totalPollinationPower;
        }

        totalPollinationTracker += dailyPollinationAmount;
        beePollinationTracker += dailyPollinationAmount * beePolFraction;
    }

    /**
     * Updates the plant during the rest phase.
     *
     * Precondition: 0 <= si <= 1
     * yi >= 0
     * cMinus and cPlus are valid reproduction factors
     *
     * Postcondition:
     * yi has been updated based on seed quality and reproduction factor
     * bi and si has been reset for new year
     * totalPollinationTracker and beePollinationTracker has been reset too
     *
     * GOOD: Flexible design through beeComp flag: enables different reproduction strategies
     *
     * BAD: Stress state are not reset
     * CAUSE: Incomplete implementation of annual cycle
     * SOLVE: moistureStress, lastTooDry, lastTooWet also could be reset
     */
    //STYLE : Object oriented (object behaviour) and procedural implementation wise(if - else)
    public void updateRestPhase() {

        // Gaussian randomness for reproduction variability
        double gaussian = random.nextGaussian();
        double c = cMinus + (cPlus - cMinus) * (0.5 + 0.15 * gaussian);
        c = max(cMinus, min(cPlus, c));


        if (!beeComp) {
            yi = yi * si * c;
        } else {
            double qualityBonus = 0.0;
            if (totalPollinationTracker > 0.0) {
                qualityBonus = beePollinationTracker / totalPollinationTracker;
            }
            double growth_bonus = 1.0 + (qualityBonus * 1.6);
            yi = yi * si * c * growth_bonus;
        }

        // Reset for next year
        bi = 0.0;
        si = 0.0;
        totalPollinationTracker = 0.0;
        beePollinationTracker = 0.0;
    }

    //Setters and getters: STYLE: Object oriented.

    /**
     * Sets bee compatibility. Default value is false.
     *
     * Postcondition: beeComp has been set to the specified value
     *
     * @param beeComp true if wild bee compatibility should be considered; false otherwise
     *
     * BAD: Getter methods violate Information Hiding principle
     *      External classes can see internal states and misuse them for calculations
     * CAUSE: Convenience in data acccess
     * SOLVE: Targeted query methods or return immutable DTOs
     */
    public void setBeeComp(boolean beeComp) {
        this.beeComp = beeComp;
    }

    /**
     * Returns the current growth power of this plant species.
     *
     * Postcondition: returns >= 0
     *
     * @return growthPower value yi
     */
    public double getYi() {
        return yi;
    }

    /**
     * Returns the current flowering fraction of this plant species.
     *
     * Postcondition: 0 <= returns <= 1
     *
     * @return flowering fraction bi
     */
    public double getBi() {
        return bi;
    }

    /**
     * Returns the current Quality of seeds of this plant species.
     *
     * Postcondtion: 0 <= returns <= 1
     *
     * @return Quality of seed (si)
     */
    public double getSi() {
        return si;
    }

    /**
     * Returns the name of this plant species
     *
     * Postcondition: returns != null && !returns.isEmpty()
     *
     * @return species name
     */
    public String getName() {
        return name;
    }
}
