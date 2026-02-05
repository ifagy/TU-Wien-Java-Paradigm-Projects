/**
 * Represents the specific bee species U. It has an activity duration of 9 days.
 */
@Author("Ozan")
@Invariant(" activeDaysLeft is always within the Interval [0, 9]. \n"  +
        "totalVisitsZ = 0")
public class BeeU extends Bee {

    /**
     * Constructor for BeeU.
     */
    @Author("Ozan")
    @PreCondition("handled by Bee's constructor logic")
    @PostCondition("An instance of BeeU is created, activeDaysLeft is set to 9")
    public BeeU() {

        this.activeDaysLeft = 9;
    }

    /**
     * Initiates the visit process to a plant by calling the U-specific acceptance method
     * on the plant.
     * @param plant The plant instance to be visited.
     */
    @Override
    @Author("Ozan")
    @PreCondition("-The object (this) must be valid.\n -The provided Plant instance is valid and not null. ")
    @PostCondition("-If the bee is active, the method {@code plant.acceptVisitFromU(this)} is called.\n " +
            "-If the bee is inactive, no action is taken.")
    public void visitPlant(Plant plant) {
        if(this.isActive()){
            plant.acceptVisitFromU(this);
        }
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns true, since U prefers X." +
            "The state of the object remains unchanged.")
    /**
     * Checks if Bee U prefers Plant species X.
     * @return Always true.
     */
    public boolean prefersX() {return true;}

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns false, since U doesn't prefer Y." +
            "The state of the object remains unchanged.")
    /**
     * Checks if Bee U prefers Plant species Y.
     * @return Always false.
     */
    public boolean prefersY() {return false;}

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns false, since U doesn't prefer Z." +
            "The state of the object remains unchanged.")
    /**
     * Checks if Bee U prefers Plant species Z.
     * @return Always false.
     */

    public boolean prefersZ() {return false;}
}
