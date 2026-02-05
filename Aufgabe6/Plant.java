/**
 * This abstract class serves as the common base for all concrete plant species (PlantZ, PlantX, PlantY).
 *
 * Its main function is to make the management of state variables easier, and to **avoid code redundancy**.
 * It also serves as the initial dispatch point in the **Double Dispatch pattern** to ensure runtime
 * behavior is correctly determined without violating the restriction against dynamic type checking.

 */
@Author("Ozan")
@Invariant("totalVisitsFromV, totalVisitsFromU and totalVisitsFromW are always greater than or equal to 0.")
  //      "activeDaysLeft is within the Interval of 0 to 8-10 based on the type of Organism.")
public abstract class Plant extends Organism {
    private int totalVisitsFromV = 0;
    private int totalVisitsFromU = 0;
    private int totalVisitsFromW = 0;




    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("totalVisitsFromU is incremented by 1.")
    /**
     * Increments the received visit counter from BeeU by one.
     */
    protected void incrementVisitedFromU(){
        totalVisitsFromU++;
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("totalVisitsFromW is incremented by 1.")
    /**
     * Increments the received visit counter from BeeW by one.
     */
    protected void incrementVisitedFromW(){
        totalVisitsFromW++;
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("totalVisitsFromV is incremented by 1.")
    /**
     * Increments the received visit counter from BeeV by one.
     */
    protected void incrementVisitedFromV(){
        totalVisitsFromV++;
    }


    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition(" The returned value equals the counter totalVisitsFromU." +
            "The returned value is greater than or equal to 0." + "The state of the object remains unchanged.")
    /**
     * Returns the total visits the plant received from BeeU.
     * @return The number of visits from U bees.
     */
    public int visitedByU() {
        return totalVisitsFromU;
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("The returned value equals the counter totalVisitsFromV." +
            "The returned value is greater than or equal to 0." + "he state of the object remains unchanged.")
    /**
     * Returns the total visits the plant received from BeeV.
     * @return The number of visits from V bees.
     */
    public int visitedByV() {
        return totalVisitsFromV;
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("The returned value equals the counter totalVisitsFromW." +
            "The returned value is greater than or equal to 0." + "- The state of the object remains unchanged.")
    /**
     * Returns the total visits the plant received from BeeW.
     * @return The number of visits from W bees.
     */
    public int visitedByW() {
        return totalVisitsFromW;
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid. \n " +
            "The provided BeeU instance must be valid and not null.")
    @PostCondition("If the plant is active and the visit is allowed, the plant's " +
            "U-counter and the bee's collected counter are incremented.")
    /**
     * Accepts a visit from a BeeU instance.
     * @param u The visiting BeeU instance.
     */
    public abstract void acceptVisitFromU(BeeU u);

    @Author("Ozan")
    @PreCondition("The object @this must be valid. \n " +
            "The provided BeeV instance must be valid and not null.")
    @PostCondition("If the plant is active and the visit is allowed, the plant's " +
            "V-counter and the bee's collected counter are incremented.")
    /**
     * Accepts a visit from a BeeU instance.
     * @param v The visiting BeeU instance.
     */
    public abstract void acceptVisitFromV(BeeV v);

    @Author("Ozan")
    @PreCondition("The object @this must be valid." +
            "The provided BeeW instance must be valid and not null.")
    @PostCondition("If the plant is active and the visit is allowed, the plant's W-counter and the bee's collected counter are incremented.")
    /**
     * Accepts a visit from a BeeU instance.
     * @param w The visiting BeeU instance.
     */
    public abstract void acceptVisitFromW(BeeW w);

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns true if the plant's species is U's preferred (X) or second (Y) choice." +
            "Returns false if the plant's species is Z." + "The state of the object remains unchanged.")
    /**
     * Checks if this specific plant species can be visited by Bee U based on the given rules.
     * @return True if U can visit; otherwise false.
     */
    public abstract boolean canBeVisitedByU();

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns true if the plant's species is V's preferred (Y) or second (Z) choice." +
            "Returns false if the plant's species is X." + "The state of the object remains unchanged.")
    /**
     * Checks if this specific plant species can be visited by Bee V based on the given rules.
     * @return True if V can visit; otherwise false.
     */
    public abstract boolean canBeVisitedByV();

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns true if the plant's species is W's preferred (Z) or second (X) choice." +
            "Returns false if the plant's species is Y." + "The state of the object remains unchanged.")

    /**
     * Checks if this specific plant species can be visited by Bee W based on the given rules.
     * @return True if W can visit; otherwise false.
     */
    public abstract boolean canBeVisitedByW();

}
