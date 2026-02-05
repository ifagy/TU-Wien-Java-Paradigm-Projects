import java.util.Iterator;
/**
 * An observation of a bee from a communal species.
 * @invariant All communal bees can also live solitarily.
 */

public interface CommunalBee  extends SolitaryBee{
    /**
     * @return An iterator over every observation of the same individual
     * that indicates a communal lifestyle.
     */
    public Iterator<CommunalBee> communal();
}
