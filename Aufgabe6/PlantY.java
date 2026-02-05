/**
 * Represents the specific plant species Y.
 *
 * Plant Y flowers for 8 days. It is the preferred food source for Bee V, serves as a second option food source for Bee U,
 * and is forbidden for Bee W.
 */
@Author("Ozan")
@Invariant("activeDaysLeft is always within the Interval [0, 8].\n" +
        "totalVisitsFromW = 0")
  //      " activeDaysLeft is always within the Interval [0, 8].")
public class PlantY extends Plant {


    @Author("Ozan")
    @PreCondition("Handled by Plant's constructor logic.")
    @PostCondition("An instance of PlantY is created, activeDaysLeft is set to 8.")
    /**
     * Constructor for PlantY.
     */
    public PlantY(){
        this.activeDaysLeft = 8;
    }


    @Author("Ozan")
    @PreCondition("The object @this must be valid." +
            "The provided BeeU instance must be valid and not null.")
    @PostCondition("If the plant is active, the plant's U-counter and the bee's Y-counter are incremented." +
            "If the plant is inactive, no counters are incremented.")
    /**
     * Accepts a visit from a BeeU instance.
     * @param u The visiting BeeU instance.
     */
    @Override
    public void acceptVisitFromU(BeeU u) {
        if(this.isActive()){
            incrementVisitedFromU();
            u.incrementVisitsY();
        }
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid." +
            "The provided BeeV instance must be valid and not null.")
    @PostCondition("If the plant is active, the plant's V-counter and the bee's Y-counter are incremented." +
            "If the plant is inactive, no counters are incremented.")
    /**
     * Accepts a visit from a BeeV instance.
     * @param v The visiting BeeV instance.
     */
    @Override
    public void acceptVisitFromV(BeeV v) {
        if(this.isActive()){
            incrementVisitedFromV();
            v.incrementVisitsY();
        }
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid." +
            "The provided BeeW instance must not be null.")
    @PostCondition("No action is taken, since visits from W are not allowed for Plant Y.")
    /**
     * Accepts a visit from a BeeW instance.
     * @param w The visiting BeeW instance.
     */
    @Override
    public void acceptVisitFromW(BeeW w) {
        return;
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns true, as Y is a second option for U.")
    /**
     * Checks if this specific plant species can be visited by Bee U.
     * @return Always true.
     */
    public boolean canBeVisitedByU() { return true; }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("")
    /**
     * Checks if this specific plant species can be visited by Bee V.
     * Postcondition: Returns true, as Y is a first option for V.
     * @return Always true.
     */
    public boolean canBeVisitedByV() { return true; }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns false, as Y is not an option for W.")
    /**
     * Checks if this specific plant species can be visited by Bee W.
     * @return Always false.
     */
    public boolean canBeVisitedByW() { return false; }

}
