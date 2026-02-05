/**
 * HoneyBee represents observations of honey bees
 * Implements Modifiable<String, HoneyBee>
 */
public class HoneyBee extends Bee implements Modifiable<String, HoneyBee>{
    private final String sort;

    /**
     * Creates a new honey bee observation
     *
     * Precondition: desc != null, sort != null
     * Postcondition: this.sort() == sort, and this.toString() contains both description and species/bread
     *
     * @param desc The description of the observation
     * @param sort The species/breed of the observed bee
     */
    public HoneyBee(String desc, String sort) {
        super(desc);
        this.sort = sort;
    }

    /**
     * Returns the species/breed of the observed bee
     *
     * Postcondition: return value != null
     *
     * @return The species/breed as a string
     */
    public String sort() {
        return sort;
    }

    /**
     * {@inheritDoc}
     * Creates a new HoneyBee with extended string
     *
     * Precondition: x != null
     * Postcondition: if x is not empty then return value.sort() == this.sort() + x
     *                if x is empty then return value == this
     *                return value != null
     *
     * @param x The string to be added
     * @return A new HoneyBee instance with extended string, or this if no change
     */
    @Override
    public HoneyBee add(String x) {
        if (x == null || x.length() == 0) return this;
        return new HoneyBee(this.toString(), this.sort + x);
    }

    /**
     * {@inheritDoc}
     * Creates a new HoneyBee with cleaned string
     *
     * Precondition: x != null
     * Postcondition: if x is contained in this.sort() then return value.sort() == this.sort().replace(x, "")
     *                if x is not contained in this.sort() then return value == this
     *                return value != null
     *
     * @param x The string to be removed
     * @return A new HoneyBee instance with cleaned string, or this if no change
     */
    @Override
    public HoneyBee subtract(String x) {
        if (x == null || x.length() == 0) return this;
        if (!this.sort.contains(x)) return this;
        String newSort = this.sort.replace(x, "");
        return new HoneyBee(this.toString(), newSort);
    }

    /**
     * Returns a textual representation of this honey bee observation
     *
     * Postcondition: return value != null, and contains the species/breed and description
     * @return A description of the honey bee observation
     */
    @Override
    public String toString() {
        return "HoneyBee[sort=" + sort + "]";
    }
}
