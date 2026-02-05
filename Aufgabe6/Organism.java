/**
 * The common abstract base class for all simulated organisms (Bees and Plants).
 * This class centralizes the simulation objects' state and methods to enable maintenance
 * in the custom Set class without using dynamic type checks.
 */
@Author("Ozan")
@Invariant("activeDaysLeft is within the Interval of 0 to 8-10 based on the type of Organism.")
public abstract class Organism {
    protected int activeDaysLeft;



    @Author("Ozan")
    @PreCondition(" The object @this must be valid.")
    @PostCondition("If the organism was active (activeDaysLeft > 0), activeDaysLeft is decremented by 1." +
            "activeDaysLeft must be greater than or equal to zero.")
    /**
     * Reduces the remaining active duration of the organism by one day, if it is currently active.
     */
    public void endOfDay(){
        if(activeDaysLeft > 0){
            activeDaysLeft--;
        }
    }

    @Author("Ozan")
    @PreCondition("The object @this must be valid.")
    @PostCondition("The returned value is true if activeDaysLeft > 0." +
            "The state of the object remains unchanged.")
    /**
     * Checks if the organism is still active.
     * @return True if the organism is active; otherwise, false.
     */
    public boolean isActive(){
        return activeDaysLeft > 0;
    }
}
