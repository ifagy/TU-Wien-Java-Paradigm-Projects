/**
 * Base class for bee observations.
 */
public class Bee {
    private final String desc;

    /**
     * Creates a new bee observation.
     *
     * Precondition: desc != null
     * Postcondition: this.toString() returns a description containing desc
     *
     * @param desc The description of the observation.
     */
    public Bee(String desc) {
        this.desc = desc;
    }

    /**
     * Returns a textual representation of the bee observation.
     *
     * Postcondition: return value != null, and contains the description of this observation
     *
     * @return A description of the observation.
     */
    @Override
    public String toString() {
        return "Bee[" + desc + "]";
    }
}
