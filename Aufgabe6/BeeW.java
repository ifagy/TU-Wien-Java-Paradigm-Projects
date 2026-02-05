/**
 * Represents the specific bee species W. It has an activity duration of 10 days.
 */
@Author("Ozan")
@Invariant(" activeDaysLeft is always within the Interval [0, 10].\n" +
        "totalVisitsY = 0")
  //      " activeDaysLeft is always within the Interval [0, 10].")
public class BeeW extends Bee {

    @Author("Ozan")
    @PreCondition("Handled by Bee's constructor logic.")
    @PostCondition("An instance of BeeW is created, activeDaysLeft is set to 10.")
    /**
     * Constructor for BeeW.
     */
    public BeeW() {
        this.activeDaysLeft = 10;
    }


    @Author("Ozan")
    @PreCondition("The object @this must be valid." +
            "The provided Plant instance is valid and not null.")
    @PostCondition("If the bee is active, the method {@code plant.acceptVisitFromW(this)} is called." +
            "If the bee is inactive, no action is taken.")
    /**
     * Initiates the visit process to a plant by calling the W-specific acceptance method
     * on the plant.
     * @param plant The plant instance to be visited.
     */
    @Override
    public void visitPlant(Plant plant) {
        if(this.isActive()){
            plant.acceptVisitFromW(this);
        }
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns false, since W doesn't prefer X." +
            "The state of the object remains unchanged.")
    /**
     * Checks if Bee W prefers Plant species X.
     * @return Always false.
     */
    public boolean prefersX() {return false;}

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns false, since W doesn't prefer Y." +
            "The state of the object remains unchanged.")
    /**
     * Checks if Bee W prefers Plant species Y.
     * @return Always false.
     */
    public boolean prefersY() {return false;}

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns true, since V prefers Z." +
            "The state of the object remains unchanged.")
    /**
     * Checks if BeeW prefers Plant species Z.
     * @return Always true.
     */
    public boolean prefersZ() {return true;}
}
