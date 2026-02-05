/**
 * Represents the specific plant species Z.
 *
 * Plant Z flowers for 10 days. It is the preferred food source for Bee W, serves as a second option food source for Bee V,
 * and is forbidden for Bee U.
 */
@Author("Ozan")
@Invariant("activeDaysLeft is always within the Interval [0, 10].\n" +
        "totalVisitsFromU = 0")
public class PlantZ extends Plant {

    @Author("Ozan")
    @PreCondition("Handled by Plant's constructor logic.")
    @PostCondition("An instance of PlantZ is created, activeDaysLeft is set to 10.")
    /**
     * Constructor for PlantZ.
     */
    public PlantZ(){

        this.activeDaysLeft = 10;
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid." +
            "The provided BeeU instance must not be null")
    @PostCondition("No action is taken, since visits from U are not allowed for Plant Z.")
    /**
     * Accepts a visit from a BeeU instance.
     * @param u The visiting BeeW instance.
     */
    @Override
    public void acceptVisitFromU(BeeU u) {
        return;
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid." +
            "The provided BeeV instance must be valid and not null.")
    @PostCondition("If the plant is active, the plant's V-counter and the bee's Z-counter are incremented." +
            "If the plant is inactive, no counters are incremented.")
    /**
     * Accepts a visit from a BeeV instance.
     * @param v The visiting BeeV instance.
     */
    @Override
    public void acceptVisitFromV(BeeV v) {
        if(this.isActive()){
            incrementVisitedFromV();
            v.incrementVisitsZ();
        }
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid." +
            "The provided BeeW instance must be valid and not null.")
    @PostCondition("If the plant is active, the plant's W-counter and the bee's Z-counter are incremented." +
            "If the plant is inactive, no counters are incremented.")
    /**
     * Accepts a visit from a BeeW instance.
     * @param w The visiting BeeW instance.
     */
    @Override
    public void acceptVisitFromW(BeeW w) {
        if(this.isActive()){
            incrementVisitedFromW();
            w.incrementVisitsZ();
        }
    }


    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns false, as Z is not an option for U.")
    /**
     * Checks if this specific plant species can be visited by Bee U.
     * @return Always false.
     */
    public boolean canBeVisitedByU() { return false; }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns true, as Z is a second option for V.")
    /**
     * Checks if this specific plant species can be visited by Bee V.
     * @return Always true.
     */
    public boolean canBeVisitedByV() { return true; }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns true, as Z is a first option for W.")
    /**
     * Checks if this specific plant species can be visited by Bee W.
     * @return Always true.
     */
    public boolean canBeVisitedByW() { return true; }

}
