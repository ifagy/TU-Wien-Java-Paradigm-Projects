import java.util.Iterator;

public interface WildBee extends Bee{

    /**
     * Returns observations of the same individual based on its breeding(Wild, Zucht) status.
     * @param isWild (true = from Zucht, false = not from Zucht).
     * @return An iterator for observations that match the specified
     * breeding status.
     */
    public Iterator<WildBee> wild(boolean isWild);
}
