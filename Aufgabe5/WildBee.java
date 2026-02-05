/**
 * WildBee represents observations of wild bees.
 * Implements Modifiable<Integer, WildBee>.
 */
public class WildBee extends Bee implements Modifiable<Integer, WildBee> {
    private final int length;

    /**
     * Creates a new wild bee observation.
     *
     * Precondition: desc != null, length > 0
     * Postcondition: this.length() == length, this.toString() contains both description and length
     *
     * @param desc The description of the observation
     * @param length The estimated length of the bee in millimeters
     */
    public WildBee(String desc, int length) {
        super(desc);
        this.length = length;
    }

    /**
     * Returns the estimated length of the bee in millimeters.
     *
     * Postcondition: return value > 0
     *
     * @return The length in millimeters
     */
    public int length() {
        return length;
    }

    /**
     * {@inheritDoc}
     * Creates a new WildBee with length increased by x.
     *
     * Precondition: x != null
     * Postcondition: if x > 0 then return value.length() == this.length() + x
     *                if x <= 0 then return value == this
     *                return value != null
     *
     * @param x The value by which to increase the length
     * @return A new WildBee instance with adjusted length, or this if no change.
     */
    @Override
    public WildBee add(Integer x) {
        if (x == null || x <= 0) return this;
        return new WildBee(this.toString(), this.length + x);
    }

    /**
     * {@inheritDoc}
     * Creates a new WildBee with length decreased by x.
     *
     * Precondition: x != null
     * Postcondition: if x > 0 AND x < this.length() then return value.length() == this.length() - x
     *                if x <= 0 OR x >= this.length() then return value == this
     *                return value != null
     *
     * @param x The value by which to decrease the length
     * @return A new WildBee instance with adjusted length, or this if no change
     */
    @Override
    public WildBee subtract(Integer x) {
        if (x == null || x <= 0) return this;
        if (x >= this.length) return this;
        return new WildBee(this.toString(), this.length - x);
    }

    /**
     * Returns a textual representation of this wild bee observation
     *
     * Postcondition: return value != null and return value contains the length and description
     *
     * @return A description of the wild bee observation
     */
    @Override
    public String toString() {
        return "WildBee[len=" + length + "mm]";
    }
}
