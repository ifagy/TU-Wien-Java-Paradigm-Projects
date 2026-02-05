import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class FlowerFly implements Pollinator {
    /**
     * class invariants:
     * FFId > 0
     */
    private final int FFId;
    private LocalDateTime date;
    private String comment;
    private boolean removed = false;

/**
 * Constructor to create a FlowerFly object.
 *
 * Preconditions:
 * - date != null
 * - comment != null
 * - FFId > 0
 *
 * Postconditions: An object of FlowerFly is created.
 *
 * @param date : date of the observation made.
 * @param comment : comment of the observation
 * @param FFId : a unique id for an instance of a FlowerFly.
 **/
    public FlowerFly(LocalDateTime date, String comment, int FFId){
        this.date = date;
        this.comment = comment;
        Test.observations.add(this);
        this.FFId = FFId;
    }

    @Override
    public void remove() {
        removed = true;
    }

    @Override
    public boolean valid() {
        return !removed;
    }

    @Override
    public Iterator<Observation> later(){
        List<Observation> observedLater = new ArrayList<>();
        for(Observation o :  Test.observations){
            if( o.getDate().isAfter(this.date) && o.valid()){
                observedLater.add(o);
            }
        }
        observedLater.sort(Comparator.comparing(Observation::getDate));
        return observedLater.iterator();
    }

    @Override
    public Iterator<Observation> earlier(){
        List<Observation> observedEarlier = new ArrayList<>();
        for(Observation o :  Test.observations){
            if( o.getDate().isBefore(this.date) && o.valid()){
                observedEarlier.add(o);
            }
        }
        observedEarlier.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
        return observedEarlier.iterator();
    }

    @Override
    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public List<Observation> getObservations() {
        return Test.observations;
    }
}
