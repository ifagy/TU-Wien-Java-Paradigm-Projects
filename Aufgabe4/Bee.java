import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * An observation of a bee of any species.
 * @invariant All bees are Wasps.
 * @invariant All bees act as pollinators.
 */
public interface Bee extends Wasp, Pollinator{

    /**
     * Returns an iterator over all observations of the same individual.
     * @return Iterator sorted ascending by observation time. [cite: 19]
     */
    public Iterator<Bee> sameBee(); //ascending
    public Iterator<Bee> sameBee(boolean descending, LocalDateTime startFrom, LocalDateTime endFrom); //ascending or descending
}
