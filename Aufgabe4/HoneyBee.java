import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class HoneyBee implements SocialBee{
    /**
     * class invariants:
     * HBId > 0
     */
    private final int HBId;

    private LocalDateTime date;
    private String comment;
    private boolean removed = false;


    /**
     * Constructor to create a HoneyBee object.
     *
     * Preconditions:
     * - date != null
     * - comment != null
     * - HBId > 0
     *
     * Postconditions: An object of HoneyBee is created.
     *
     * @param date : date of the observation made.
     * @param comment : comment of the observation
     * @param HBId : a unique id for an instance of a HoneyBee.
     */
    public HoneyBee(LocalDateTime date, String comment, int HBId){
        this.date = date;
        this.comment = comment;
        Test.observations.add(this);
        this.HBId = HBId;
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

    /**
     * Postconditions: All elements of the iterator are elements of HoneyBee, SocialBee and are valid.
     *
     * @return Iterator<SocialBee> of all valid HoneyBee observations with the same BBId
     */
    @Override
    public Iterator<SocialBee> social() {
        List<SocialBee> thisBeeObs = new ArrayList<>();

        for(Observation o : Test.observations){
            if(o instanceof HoneyBee && o.valid() && ((HoneyBee) o).HBId == this.HBId){
                thisBeeObs.add((SocialBee) o);
            }
        }

        return thisBeeObs.iterator();

    }

    /**
     * Postconditions:
     * - Sorted ascending by getDate()
     * - Each element in the iterator is instanceof HoneyBee and is valid.
     * @return Iterator<Bee> of all valid HoneyBee observations with the same BBId
     */
    @Override
    public Iterator<Bee> sameBee() {
        List<HoneyBee> honeyBees = new ArrayList<>();
        for(Observation o : Test.observations){
            if(o instanceof HoneyBee && o.valid() && ((HoneyBee) o).HBId == this.HBId){
                honeyBees.add((HoneyBee)o);
            }
        }
        honeyBees.sort( (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        return new ArrayList<Bee>(honeyBees).iterator();
    }


    /**
     * Preconditions:
     * - endFrom >= startFrom
     * - endFrom != null, startFrom != null
     * Postconditions:
     * - Sorted descending if descending == true, ascending if descending == false by getDate().
     * - Each element in the iterator is instanceof HoneyBee and is valid.
     * @return Iterator<Bee> of all valid HoneyBee observations with the same BBId within the given interval  of time.
     */
    @Override
    public Iterator<Bee> sameBee(boolean descending, LocalDateTime startFrom, LocalDateTime endFrom) {
        ArrayList<HoneyBee> honeyBees = new ArrayList<>();
        for(Observation o : Test.observations){
            if(o instanceof HoneyBee && o.valid() && (!o.getDate().isBefore(startFrom)
                    && !o.getDate().isAfter(endFrom)) && ((HoneyBee) o).HBId == this.HBId){
                honeyBees.add((HoneyBee)o);
            }
        }
        if(descending){
            honeyBees.sort( (o1, o2) -> o2.getDate().compareTo(o1.getDate()));}
        else{
        honeyBees.sort( (o1, o2) -> o1.getDate().compareTo(o2.getDate()));}
        return new ArrayList<Bee>(honeyBees).iterator();
    }
}
