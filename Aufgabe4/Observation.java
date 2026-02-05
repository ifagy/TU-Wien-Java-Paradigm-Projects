
import java.util.Iterator;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a single observation in the context of the project. [cite: 57]
 * This is the supertype for all other observation types. [cite: 57]
 * All data (date, time, comment) is retrievable via methods. [cite: 59]
 */
public interface Observation {

    /**
     * Postconditions:
     *  The observation is marked as removed
     *  valid() will return false after calling this method
     */
    void remove();

    /**
     * @return true if the observation has not been removed, false otherwise
     */
    boolean valid();

    /**
     * Postconditions:
     *  The iterator contains only valid observations and is sorted in ascending order by date
     *  The iterator contains only observations with date after this observation's date
     *
     * @return Iterator<Observation> of all valid observations that occurred after this observation
     */
    Iterator<Observation> later();

    /**
     * Postconditions:
     *  The iterator contains only valid observations and is sorted in descending order by date
     *  The iterator contains only observations with date before this observation's date
     *
     * @return Iterator<Observation> of all valid observations that occurred before this observation
     */
    Iterator<Observation> earlier();

    /**
     * @return the date and time of this observation
     */
    LocalDateTime getDate();

    /**
     * @return the comment describing this observation
     */
    String getComment();

    /**
     * @return the list of all observations in the system
     */
    List<Observation> getObservations();
}
