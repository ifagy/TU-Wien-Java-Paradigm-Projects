/**
 * Represents the specific bee species V. It has an activity duration of 8 days.
 */
@Author("Ozan")
@Invariant("activeDaysLeft is always within the Interval [0, 8]. \n" +
        "totalVisitsX = 0")
  //      " activeDaysLeft is always within the Interval [0, 8].")
public class BeeV extends Bee {
    @Author("Ozan")
    @PreCondition("Handled by Bee's constructor logic.")
    @PostCondition("An instance of BeeV is created, activeDaysLeft is set to 8.")
    /**
     * Constructor for BeeV.
     */
    public BeeV() {
        this.activeDaysLeft = 8;
    }


    @Author("Ozan")
    @PreCondition("The object @this must be valid." +
            "The provided Plant instance is valid and not null.")
    @PostCondition("If the bee is active, the method {@code plant.acceptVisitFromV(this)} is called." +
            "If the bee is inactive, no action is taken.")
    /**
     * Initiates the visit process to a plant by calling the V-specific acceptance method
     * on the plant.
     * @param plant The plant instance to be visited.
     */
    @Override
    public void visitPlant(Plant plant) {
        if(this.isActive()){
            plant.acceptVisitFromV(this);
        }
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns false, since V doesn't prefer X." +
            "The state of the object remains unchanged.")
    /**
     * Checks if Bee V prefers Plant species X.
     * @return Always false.
     */
    public boolean prefersX() {return false;}

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns true, since V prefers Y." +
            "The state of the object remains unchanged.")
    /**
     * Checks if Bee V prefers Plant species Y.
     * @return Always true.
     */
    public boolean prefersY() {return true;}

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns false, since V doesn't prefer Z." +
            "The state of the object remains unchanged.")
    /**
     * Checks if Bee V prefers Plant species Z.
     * @return Always false.
     */
    public boolean prefersZ() {return false;}
}
