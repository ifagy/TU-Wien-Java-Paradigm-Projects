import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Represents the abstraction of the entire simulation flow over one year.
 *<p>
 * Abstraction:
 * This class acts as a Controller that manages the interactions between the
 * different components of the ecosystem (Weather, BeePopulation, FlowerGroup).
 * It encapsulates the logic for executing the vegetation and resting phases.</p>
 *
 * GOOD:  Objektkopplung ist schwach.
 * WHY: This class does not depend on concrete pollinator classes.
 * It only knows about the Pollinator interface.
 * Because it's coupled to an interface rather than a concrete implementation like BeePopulation,
 * we can add new pollinator types that implement the Pollinator
 * interface without a need to change a single line of code in YearlySim.java.
 */




public class YearlySim {
    /**
     * class invariants:
     * sunlightHistory  != NULL && humidityHistory != NULL && (sunlightHistory.size() == humidityHistory.size())
     * a != NULL
     * pollinators != NULL
     * f != NULL
     */

    private List<Double> sunlightHistory = new ArrayList<>();
    private List<Double> humidityHistory =  new ArrayList<>();

    private Weather a;
    private List<Pollinator> pollinators;
    private FlowerGroup f;


    /**
     * Constructor to initialize a new annual simulation.
     * Creates the necessary simulation components based on the starting parameters.
     *
     * @param f         the initial humidity.
     * @param pollinators A list containing all pollinator populations.
     * @param fg        the flower group to be used in the simulation.
     *
     * Preconditions: f <= 1 && f >= 0, latitude ∈ [-PI/2, PI/2], rest is ensured by the class invariants.
     * Postconditions: Initializes an object of YearlySim according to pollinators, fg and latitude.
     */
    //STYLE: Object oriented.

    public YearlySim(double f, List<Pollinator>pollinators, FlowerGroup fg, double latitude) {
         this.a = new Weather(f,latitude);
         this.pollinators = pollinators;
         this.f = fg;
    }

    /**
     * Executes the simulation of the vegetation phase (240 days).
     * <p>
     * In each daily step, the weather is updated, the flowers and bees
     * interact, and their respective states are adjusted.</p>

     * Postconditions:
     *  # The method simVegetationPhaseOneDay() is  carried out 240 times.
     *  # The state of Weather, FlowerGroup, and Pollinators is advanced by 240 days.
     */
    // STYLE: procedural – Executes the vegetation phase in a step-by-step loop over 240 days, applying daily updates sequentially.
    public void simVegetationPhase() {
        IntStream.range(0, 240).forEach(i ->{
            simVegetationPhaseOneDay();
        });
    }

    /**
     * Executes the simulation of one single day.
     * <p>
     * The weather is updated, the flowers and bees interact, and their respective states are adjusted.</p>

     * Postconditions:
     * - The daily values for the sunlight and humidity are added to sunlightHistory and humidityHistory.
     * - The value for the totalSunlight is incremented by the daily value of sunlight.
     * - All FlowerPopulation in f are updated according to the values of humidity, sunlight, pollinators and the day.
     * - States of the pollinators is updated according to vegetation_One_Day().
     *
     * GOOD Prozedurale: Control flow is clear and easy to follow.
     * WHY: This method executes the logic for a single
     * day in a clear, sequential, top-down manner.
     * 1. Update weather
     * 2. Update all flowers based on the new weather
     * 3. Calculate total nectar/demand
     * 4. Update all pollinators based on nectar/demand
     * The logic is not nested deeply. Each major step is
     * clearly delegated to another object's method (e.g., "a.nextDayWeather()",
     * "f.updateAll()"), which keeps this procedure's flow
     * simple and readable.
     */
    //STYLE: procedural, sequential updates.
    public void simVegetationPhaseOneDay() {
        a.nextDayWeather();
        double sunlight = a.getSunlight();
        double humidity = a.getHumidity();
        double totalSunlight = a.getTotalSunlight();

        sunlightHistory.add(sunlight);
        humidityHistory.add(humidity);

        f.updateAll(humidity, totalSunlight, sunlight, pollinators, a.getDay());

        double totalNectar = f.getTotalNectar();
        double totalDemand = pollinators.stream().mapToDouble(Pollinator::getDemand).sum();

        /**
         * GOOD OOP: Dynamic binding simplifies code and improves maintainability.
         * WHY: This stream loop iterates over a List<Pollinator>.
         * It calls 'p.vegetation_One_Day()' on each element
         * without knowing or caring about the concrete class (whether it's
         * a 'BeePopulation' or a 'GenericPollinator').
         * If we added a "ButterflyPollinator", we don't have to modify this loop, thanks to dynamic binding.
         */

        pollinators.stream().forEach(p -> {
                    double nForThisP = 0.0;
                    if (totalDemand > 0) {
                        double demandFraction = p.getDemand() / totalDemand;
                        nForThisP = (totalNectar >= totalDemand ? p.getDemand() : totalNectar * demandFraction);
                    }
                    p.vegetation_One_Day(nForThisP);
                });


        /*for(Pollinator p : pollinators){
            double nForThisP = 0.0;
            if (totalDemand > 0) {
                double demandFraction = p.getDemand() / totalDemand;
                if (totalNectar >= totalDemand) {
                    // Surplus
                    nForThisP = p.getDemand();
                } else {
                    // Deficit Proportional share
                    nForThisP = totalNectar * demandFraction;
                }
           // }
            p.vegetation_One_Day(nForThisP);

        }


    }

         */
    }

    /**
     * Executes the simulation of the resting phase (e.g., winter).
     * <p>
     * During this phase, the bee population is reduced, the flower populations
     * prepare for the next year, and the weather is reset.</p>

     * Postconditions:
     * - All pollinator populations are updated according to winterComes().
     * - All flower populations in 'f' are updated according to updateRestPhaseAll().
     * The object of Weather a resets its totalSunlight to 0 according to weatherTotalSunLightReset().
     */
    //STYLE: procedural, sequential execution.
    public void simRestingPhase() {
        pollinators.forEach(Pollinator::winterComes);
        f.updateRestPhaseAll();
        a.weatherTotalSunLightReset();
    }


    /**
     * STYLE: functional:
     * Referential Transparency: maintained, no internal or external modification.
     * Integration: Used in classes Test to see the remaining amount of bees
     * and in ParallelFunAnalyzer for statistical purposes.

     *  Returns the current population of *only* the wild bees.
     * @return the wild bee population.

     * Postconditions:
     * - Returns the population size of the FIRST BeePopulation found in pollinators.
     * - If there are no BeePopulation, 0 is returned.
     *
    /**
     * BAD OOP: Lack of dynamic binding made the code more complex.
     * WHY: This method breaks encapsulation. The YearlySim class
     * should not know that a special type of Pollinator called
     * BeePopulation exists.
     * HOW IT HAPPENED: We needed a way to report on only bees, and
     * the Pollinator interface was not designed to
     * provide this information at first place.
     * BETTER SOLUTION: The Pollinator interface could be extended
     * with a "getSpeciesType()" to allow
     * this class to get the data it needs polymorphically, without
     * using 'instanceof' or casting. And getBeePopulationsSize() could be
     * replaced with a new method getPopulationSize(SpecieType specie).
     */

    public double getBeePopulationSize(){
        /*
        for (Pollinator p : pollinators) {
            // instanceof checks the subtype relationship
            if (p instanceof BeePopulation) {
                return p.getPopulationSize();
            }
        }
        return 0.0;

         */
        return pollinators.stream()
                .filter(p -> p instanceof BeePopulation)
                .mapToDouble(Pollinator::getPopulationSize)
                .findFirst()
                .orElse(0);
    }

    /**
     * STYLE: functional
     * Referential Transparency: maintained: no internal or external modification.
     * Integration: Used in Test to access the population size.

     * Returns the current population of all Pollinators.
     * @return total Pollinator population.

     * Postconditions: Returns the sum of the population sizes of all pollinators in pollinators.
     */
    public double getPollinatorsPopulationSize(){

        return pollinators.stream().mapToDouble(Pollinator::getPopulationSize).sum();
    }

    /**
     * STYLE: functional
     * Referential Transparency: maintained, no internal or external modification.
     * Integration: Used in Test to access the yi value
     * and in YearlySim to compute the total amount of yi.

     * Returns the current growth strengths (yi) of all flower populations.<p>
     * Values below 1 are treated as 0, representing the extinction of a species.</p>
     * @return an array of double values containing the growth strength of each flower species.

     * Postconditions:
     * - Returns an array with the values of yi (double) for each FlowerPopulation.
     * - If a FlowerPopulation ' s yi < 1, then its value in the array is set to 0, otherwise yi.
     * - The length of the returned array is equal to f.size().
     */
    public double[] getFlowerYi() {
        /*
        double[] yi = new double[f.size()];
        for (int i = 0; i < f.size(); i++) {
            if (f.getPopulations().get(i).getYi()<1) yi[i] = 0;
            else yi[i] = f.getPopulations().get(i).getYi();
        }
        return yi;

         */
        return f.getPopulations().stream().
                mapToDouble(fp -> fp.getYi() < 1? 0 : fp.getYi()).
                toArray();
    }

    /**
     * STYLE: functional
     * Referential Transparency: maintained, no internal or external modification.
     * Integration: In Test to have access to total amount of yi
     * and in ParallelFunAnalyzer for statistical purposes.

     Sums the yi value for every single FlowerPopulation and returns it.
     * @return sum of the yi for all FlowerPopulation.

     * Postconditions:
     * - If the yi value of a FlowerPopulation < 1, 0 is added, otherwise the value of yi.
     * - Returns the sum of the yi values for all FlowerPopulations.
     */
    public double getTotalYi() {
        double[] yiArray = getFlowerYi() ;
        /*
        double sum = 0;
        for (int i = 0; i < yiArray.length; i++) {
            sum += yiArray[i];
        }
        return sum;

         */
        return f.getPopulations().stream()
                .mapToDouble(fp ->
                        fp.getYi() < 1? 0: fp.getYi()).sum() ;
    }

    //Getters: STYLE: Object oriented.
    /**
     * Returns the weather of the day.
     * @return weather of the current day.

     * Postconditions: Returns the current Weather object used in the simulation.
     */
    public Weather getWeather() {
        return a;
    }

    /**
     * Returns the bee population.
     * @return beepopulation.

     * Postconditions:
     * - Returns the FIRST BeePopulation instance of the pollinators.
     * - If there are no instance of BeePopulation, null is returned.
     */
    public  BeePopulation getBeePopulation() {
        for (Pollinator p : pollinators) {
            // instanceof checks the subtype relationship
            if (p instanceof BeePopulation) {
                return (BeePopulation) p;
            }
        }
        return null;
    }
    /**
     * Returns the flower group.
     * @return flower group.

     * Postconditions: Returns the current flower group object f of the simulation.
     */
    public FlowerGroup getFlowerGroup() {
        return f;
    }

    /**
     * STYLE: functional
     * Referential Transparency: maintained, no external or internal modification.
     * Integration: Used in Test to inform about yearly weather statistics.

     * Calculates and returns a summary of the average weather statistics
     * for humidity and sunlight recorded in the vegetation phase.
     * @return a String containing the statistics of the average humidity
     * and average sunlight values for the vegetation phase.

     * Postconditions:
     * - Calculates the average weather statistics for humidity and sunlight recorded in the vegetation phase.
     * - If no data available, both averages are given as 0.0.
     * - Returns a String in format: "Average Humidity: X , Average Sunlight: X".
     *
     * BAD OOP: Klassenzusammenhalt is not ideal.
     * WHY: The primary responsibility of "YearlySim" is to
     * control the simulation flow (e.g., 'simVegetationPhase').
     * This method, along with "getAverageBeeHealth()", adds a
     * secondary, unrelated responsibility: "data analysis and reporting".
     * HOW IT HAPPENED: It was convenient to add reporting logic
     * directly into the class that had access to the data (e.g.
     * "sunlightHistory" and "humidityHistory" lists).
     * BETTER SOLUTION: All analysis and reporting logic should be
     * moved to a dedicated "SimulationAnalyzer" class. "YearlySim" should only
     * provide the raw data (e.g., "getSunlightHistory()")
     * and not be responsible for calculating averages itself.
     */

    public String yearlyWeatherStats(){
        double avgSun = sunlightHistory.stream().mapToDouble(Double::doubleValue)
                .average().orElse(0.0);
        double avgHumidity = humidityHistory.stream().mapToDouble(Double::doubleValue)
                .average().orElse(0.0);
        return String.format("Average Humidity: %.1f, Average Sunlight: %.3f",  avgHumidity, avgSun);

    }

    /**
     * STYLE: functional
     * Referential Transparency: maintained, no external or internal modification.
     * Integration: Used in Test to inform about yearly average BeeHealth and
     * in ParallelFunAnalyzer for statistical purposes.

     * Calculates and returns the average BeeHealth of the BeePopulation in the pollinators.
     * @return a double as the average BeeHealth of the BeePopulation in the pollinators.

     * Postconditions:
     * - Calculates the average BeeHealth value of all BeePopulation instances of pollinators.
     * - If there is no BeePopulation, average is given as 0.0.
     */
    public double getAverageBeeHealth(){
        return pollinators.stream()
                .filter(p -> p instanceof BeePopulation)
                .map(p -> ((BeePopulation)p).getHealthStatus())
                .mapToDouble(Double:: doubleValue)
                .average()
                .orElse(0.0);
    }
}
