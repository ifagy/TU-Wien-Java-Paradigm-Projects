/**
 * Represents the specific plant species X.
 *
 * Plant X flowers for 9 days. It is the preferred food source for Bee U, serves as a second option food source for Bee W,
 * and is forbidden for Bee V.
 */
@Author("Ozan")
@Invariant("activeDaysLeft is always within the Interval [0, 9].\n" +
        "totalVisitsFromV = 0")
public class PlantX extends Plant {

    @Author("Ozan")
    @PreCondition("Handled by Plant's constructor logic.")
    @PostCondition("An instance of PlantX is created, activeDaysLeft is set to 9.")
    /**
     * Constructor for PlantX.
     */
    public PlantX() {
        this.activeDaysLeft = 9;

    }


    @Author("Ozan")
    @PreCondition("The object @this must be valid." +
            "The provided BeeU instance must be valid and not null.")
    @PostCondition("If the plant is active, the plant's U-counter and the bee's X-counter are incremented." +
            "If the plant is inactive, no counters are incremented.")
    /**
     * Accepts a visit from a BeeU instance.
     * @param u The visiting BeeU instance.
     */
    @Override
    public void acceptVisitFromU(BeeU u) {
        if(this.isActive()){
            incrementVisitedFromU();
            u.incrementVisitsX();
        }
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid." +
            "The provided BeeV instance must not be null.")
    @PostCondition("No action is taken, since visits from V are not allowed for Plant X.")
    /**
     * Accepts a visit from a BeeV instance.
     * @param v The visiting BeeV instance.
     */
    @Override
    public void acceptVisitFromV(BeeV v) {
        return;
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid." +
            "The provided BeeW instance must be valid and not null.")
    @PostCondition("If the plant is active, the plant's W-counter and the bee's X-counter are incremented." +
            "If the plant is inactive, no counters are incremented.")
    /**
     * Accepts a visit from a BeeW instance.
     * @param w The visiting BeeW instance.
     */
    @Override
    public void acceptVisitFromW(BeeW w) {
        if(this.isActive()){
            incrementVisitedFromW();
            w.incrementVisitsX();
        }
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns true, as X is a first option for U.")
    /**
     * Checks if this specific plant species can be visited by Bee U.
     * @return Always true.
     */
    public boolean canBeVisitedByU() { return true; }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns false, as X is not an option for V.")
    /**
     * Checks if this specific plant species can be visited by Bee V.
     * @return Always false.
     */
    public boolean canBeVisitedByV() { return false; }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns true, as Z is a second option for W.")
    /**
     * Checks if this specific plant species can be visited by Bee W.
     * @return Always true.
     */
    public boolean canBeVisitedByW() { return true; }

}
