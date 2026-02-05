import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Serves as an abstraction for a group of different flower populations (species) within an ecosystem.
 *<p>
 * Abstraction:
 * This class is a composition and manages a collection of FlowerPopulation objects.
 * It bundles operations that affect the entire plant community, such as calculating
 * the total nectar supply or updating all populations.</p>
 */
public class FlowerGroup {
    /**
     * class invariants:
     * populations != null
     * No element of FlowerPopulation in populations is null.
     * Number of elements in populations == populations.size().
     * hhMinTotal, hhMaxTotal and plantNames have all the same array length.
     */

    private List<FlowerPopulation> populations;
    private double[] hhMinTotal = {8 * 200 ,  8 * 200 , 5 * 200 , 6 * 200 , 6 * 200 , 6 * 125 ,  6 * 200 , 6 , 6 * 200 , 6 * 100};

    private double[] hhMaxTotal = {12 * 230, 12 * 180, 10 * 170,  10 * 240, 10 * 190, 10 * 200, 10 * 240, 11 * 90, 10 * 180, 10 * 240};

    private String[] plantNames = {
            "Sunflower", "Lavender", "Petunia", "Strawberry", "Zinnia",
            "Aster", "Marigold", "CherryTree", "AppleTree", "Cosmos" };


    /**
     * Initializes an empty flower group.

     * Postconditions: populations is initialized with an empty list
     * with the consideration of the class invariants.
     */
    //STYLE: Object oriented.
    public FlowerGroup() {
        populations = new ArrayList<>();
    }

    /**
     * Copy constructor. Creates a deep copy of another FlowerGroup.
     * @param other The FlowerGroup to copy.

     * Precondition: other != null.
     * Postcondition: - populations is initialized as a new list containing deep copies
     * of each FlowerPopulation from {@code other} FlowerGroup.
     * - Modifying one FlowerGroup does not affect the other one.
     */
    //STYLE: Object oriented.
    public FlowerGroup(FlowerGroup other) {
        this.populations = new ArrayList<>();
        for (FlowerPopulation fp : other.populations) {
            this.populations.add(new FlowerPopulation(fp));
        }
    }

    /**
     * Adds a new flower population (species) to this group.
     * @param population the FlowerPopulation to be added

     * Precondition: population != null.
     * Postcondition: {@code} population is added to the populations list.
     */
    //STYLE: Object oriented.
    public void addFlowerPop(FlowerPopulation population){
        populations.add(population);
    }



    /**
     * Automatically generates 10 flower populations with random attributes and adds them to {@code this} group...

     * Postconditions:
     * - 10 new FlowerPopulation objects are created and then added to the populations list.
     * -  Each new FlowerPopulation has randomized values for:
     * yy, beeComp, qqi, ppi, ccmin and ccmax.
     *
     * GOOD Procedural: Control flow is simple and easy to understand.
     * WHY: This method İs a straightforward
     * "factory" script. It uses a single "IntStream.range" loop
     * to generate 10 items.
     * All variables used for generation are declared and used
     * locally within the loops scope. There are no complex nested
     * loops, goto or break statements, making the procedural
     * flow clean and maintainable.
     */
    // STYLE: procedural: sequential generation of Flower populations through loop.
    public void autoFlowerPopGen() {

        //for (int i = 0; i < k; i++) {
        IntStream.range(0, 10).forEach(i -> {
            double yy = UtilRandPi.rBetw(10, 200);

            double ffmin = 0.000000000001; //0.000000000001;
            double ffmax = 0.99999999999; // 0.99999999999

            String name =  plantNames[i];

            double hhmin = hhMinTotal[i];
            double hhmax = hhMaxTotal[i];

            double ppi = UtilRandPi.pi(hhmin, hhmax);
            boolean beeComp = 0.2 < UtilRandPi.rBetw(0,1);

            double qqi = UtilRandPi.rBetw(0.005, 0.0066);
            double ccmin = (i % 3 != 0) ? UtilRandPi.rBetw(1, 1.7) : UtilRandPi.rBetw(0.8, 2);
            double ccmax = (i % 3 != 0) ? UtilRandPi.rBetw(1.7, 2.5) : UtilRandPi.rBetw(2, 5);

            FlowerPopulation newPop = new FlowerPopulation(name, yy, ccmin, ccmax, ffmin, ffmax, hhmax, hhmin, qqi, ppi);
            newPop.setBeeComp(beeComp);
            this.addFlowerPop(newPop);
        });
    }

    /**
     * STYLE: functional
     * Referential Transparency: maintained, no modification of internal state.
     * Integration: Used in YearlySim to simulate vegetation phase and
     * in FlowerGroup to update all FlowerPopulations at once.

     * Calculates the total nectar resource (n = Σ (yi * bi)).
     * @return total nectar availability

     * Postconditions: Returns the sum of (yi * bi) for each FlowerPopulation in populations.
     */
    public double getTotalNectar() {
        //double totNec = 0.0;

        //for (FlowerPopulation fp : populations){
        // totNec += fp.getYi() * fp.getBi();
        //}
        //return totNec;
        return populations.stream().mapToDouble
                (fp -> fp.getYi() * fp.getBi()).sum();
    }

    /**
     * STYLE: functional
     * Referential Transparency: partially maintained, internal updates, but external state intact.
     * Integration: used in YearlySim to simulate the vegetation phase.

     * Updates all flower populations for one simulation day.
     * @param f soil moisture.
     * @param h cumulative sunlight hours.
     * @param d day length.
     * @param currentDay the current day of the simulation.

     * Preconditions:
     * - f >= 0 && f <= 1.
     * - h >= 0.
     * - d >= 0.
     *  currentDay  ∈ [1,240].

     * Postconditions:
     * - Each FlowerPopulation in populations is updated according to updateDaily() with the required parameters.
     * - Total nectar is calcuated and then used for the update.
     */
    public void updateAll(double f, double h, double d, List<Pollinator> pollinators, int currentDay){
        double totNec = getTotalNectar();
        //for (FlowerPopulation fp : populations){
           // fp.updateDaily(f, h, d, pollinators, totNec,  currentDay);
        //}
        populations.stream().forEach
                (fp -> fp.updateDaily(f,h,d,pollinators,totNec,currentDay));
    }

    /**
     * STYLE: functional
     * Referential Transparency: partially maintained: internal updates, but external state intact.
     * Integration: Used in YearlySim to simulate resting phase.

     * Executes the resting phase update for all flower populations within this {@code FlowerGroup}.

     * Postconditions: Each FlowerPopulation in populations is updated according to updateRestPhase().
     */
    public void updateRestPhaseAll(){
        populations.stream().forEach(FlowerPopulation::updateRestPhase);
        //for (FlowerPopulation fp: populations){
           // fp.updateRestPhase();
       // }
    }

    /**
     * Returns the number of flower species in {@code this} group.
     * @return species count

     * Postcondition: The returned value is equal to the current size of the populations.
     */
    //STYLE: Object oriented.
    public  int size() {
        return populations.size();
    }

    /**
     * Returns the list of all flower populations.
     * @return list of flower populations

     * Postconditions: The returned list contains all the FlowerPopulation objects currently in {@code this}.
     */
    //STYLE: Object oriented.
    public List<FlowerPopulation> getPopulations() {
        return populations;
    }

    /**
     * STYLE : functional programming
     * Referential Transparency: maintained, no modification of internal state.
     * Integration: used in Test class for statistical purposes.

     * Calculates and returns the average growth strength (yi) of all FlowerPopulations in {@code this}.
     * @return the average yi value of all FlowerPopulation objects, or 0.0 if there are no FlowerPopulation.

     * Postconditions:
     * - Returns the average yi values of all FlowerPopulation in {@code this}.
     * - If the list is empty, 0.0 returned.
     */
    public double getAverageYi(){
        return populations.stream()
                .mapToDouble(FlowerPopulation :: getYi)
                .average()
                .orElse(0.0);
    }
}
