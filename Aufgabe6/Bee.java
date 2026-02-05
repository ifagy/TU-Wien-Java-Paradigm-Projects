/**
 * This abstract class serves as the common base for all concrete bee species (BeeU, BeeV, BeeW).
 *
 * Its main function is to make the management of state variables easier, and to **avoid code redundancy**.
 * It also serves as the initial dispatch point in the **Double Dispatch pattern** to ensure runtime
 * behavior is correctly determined without violating the restriction against dynamic type checking.

 */
@Author("Ozan")
@Invariant("- totalVisitsY, totalVisitsX and totalVisitsZ are always greater than or equal to 0. ")
       // "activeDaysLeft is within the Interval of 0 to 8-10 based on the type of Organism.")
public abstract class Bee extends Organism {

    /**
     * invariants:
     * - totalVisitsY, totalVisitsX and totalVisitsZ are always greater than or equal to 0.
     */

    private int totalVisitsY = 0;
    private int totalVisitsX = 0;
    private int totalVisitsZ = 0;


    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("totalVisitsY is incremented by 1. \n " +
            "all class invariants are satisfied.")
    /**
     * Increments the counter for collected visits from Plant species Y by one.
     */
    protected void incrementVisitsY(){
        totalVisitsY++;
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid")
    @PostCondition("totalVisitsX is incremented by 1. \n " +
            " all class invariants are satisfied.")
    /**
     * Increments the counter for collected visits from Plant species X by one.
     */
    protected void incrementVisitsX(){
        totalVisitsX++;
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("totalVisitsZ is incremented by 1. \n" +
            "all class invariants are satisfied.")
    /**
     * Increments the counter for collected visits from Plant species Z by one.
     */
    protected void incrementVisitsZ(){
        totalVisitsZ++;
    }


    @Author("Ozan")
    @PostCondition("The returned value is the total number of visits to species X plants, " +
            "which is greater than or equal to 0.")
    /**
     * Returns the total count of successful flower visits made to plants of species X.
     * @return The number of visits to species X plants.
     */

    public int collectedFromX() {
        return totalVisitsX;
    }

    @Author("Ozan")
    @PostCondition("The returned value is the total number of visits to species Y plants, which is greater than or equal to 0.")
    /**
     * Returns the total count of successful flower visits made to plants of species Y.
     * @return The number of visits to species Y plants.
     */
    public int collectedFromY() {
        return totalVisitsY;
    }

    @Author("Ozan")
    @PostCondition("The returned value is the total number of visits to species Z plants, which is greater than or equal to 0.")
    /**
     * Returns the total count of successful flower visits made to plants of species Z.
     * @return The number of visits to species Z plants.
     */

    public int collectedFromZ() {
        return totalVisitsZ;
    }

    @Author("Ozan")
    @PreCondition("The provided Plant instance is valid and not null. \n" +
    "If the bee is active (isActive() == true), the corresponding acceptVisitFrom-method " +
            "on the Plant instance is called, with the specific bee type.")
    @PostCondition("If the bee is inactive, no further action is taken.")
    /**
     * Initiates the visit process to a plant.
     *  @param plant The plant instance to be visited.
     */
    public abstract void visitPlant(Plant plant);

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns true if the specific bee species has X as its preferred food source. \n" +
            "The state of the object remains unchanged.")
    /**
     * Checks if this bee species (U, V, or W) prefers Plant species X.
     * @return True if X is the preferred plant; otherwise false.
     */
    public abstract boolean prefersX();

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns true if the specific bee species has Y as its preferred food source. \n" +
            " The state of the object remains unchanged.")
    /**
     * Checks if this bee species (U, V, or W) prefers Plant species Y.
     * @return True if Y is the preferred plant; otherwise false.
     */
    public abstract boolean prefersY();

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("Returns true if the specific bee species has Z as its preferred food source. \n" +
            "The state of the object remains unchanged.")
    /**
     * Checks if this bee species (U, V, or W) prefers Plant species Z.
     * @return True if Z is the preferred plant; otherwise false.
     */
    public abstract boolean prefersZ();
}
