import java.util.Iterator;

/**
 * An observation of a bee of a species that *can* live solitarily.
 */
public interface SolitaryBee extends WildBee{
    /**
     * @return An iterator over every observation of the same individual
     * that indicates a solitary (non-communal, non-social) lifestyle.
     * (Returns all observations if the species is *never* communal or social).
     */
    public Iterator<SolitaryBee> solitary();
}
