import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class BumbleBee implements SocialBee, WildBee{
    /**
     * class invariants:
     * BBId > 0
     */
    private final int BBId;
    private boolean isWild;
    private LocalDateTime date;
    private String comment;
    private boolean removed = false;

    /**
     * Constructor to create a BumbleBee object.
     *
     * Preconditions:
     * - date != null
     * - comment != null
     * - BBId > 0
     *
     * Postconditions: An object of BumbleBee is created.
     *
     * @param date : date of the observation made.
     * @param comment : comment of the observation
     * @param BBId : a unique id for an instance of a BumbleBee.
     * @param isWild : specification for whether an instance of WildBee or not.
     */
    public BumbleBee(LocalDateTime date, String comment, int BBId, boolean isWild) {
        this.date = date;
        this.comment = comment;
        Test.observations.add(this);

        this.BBId = BBId;
        this.isWild = isWild;
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
     * Postconditions: All elements of the iterator are elements of BumbleBee and SocialBee and are valid.
     *
     * @return Iterator<SocialBee> of all valid BumbleBee observations with the same BBId
     */
    @Override
    public Iterator<SocialBee> social() {
        List<SocialBee> thisBeeObs = new ArrayList<>();

        for(Observation o : Test.observations){
            if(o instanceof BumbleBee && o.valid() && ((BumbleBee) o).BBId == this.BBId){
                thisBeeObs.add((SocialBee) o);
            }
        }

        return thisBeeObs.iterator();
    }

    /**
     * Postconditions:
     * - Sorted ascending by getDate()
     * - Each element in the iterator is instanceof BumbleBee and is valid.
     * @return Iterator<Bee> of all valid BumbleBee observations with the same BBId
     */
    @Override
    public Iterator<Bee> sameBee() {
        List<BumbleBee> bumbles = new ArrayList<>();
        for(Observation o :Test.observations){
            if(o instanceof BumbleBee && o.valid() && ((BumbleBee) o).BBId == this.BBId ){
                bumbles.add((BumbleBee) o);
            }
        }
        bumbles.sort( (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        return new ArrayList<Bee>(bumbles).iterator();

    }


    /**
     * Preconditions:
     * - endFrom >= startFrom
     * - endFrom != null, startFrom != null
     * Postconditions:
     * - Sorted descending if descending == true, ascending if descending == false by getDate().
     * - Each element in the iterator is instanceof BumbleBee and is valid.
     * @return Iterator<Bee> of all valid BumbleBee observations with the same BBId within the given interval  of time.
     */
    @Override
    public Iterator<Bee> sameBee(boolean descending, LocalDateTime startFrom, LocalDateTime endFrom) {
        List<BumbleBee> bumbles = new ArrayList<>();
        for(Observation o : Test.observations){
            if(o instanceof BumbleBee && o.valid() && (!o.getDate().isBefore(startFrom)
                    && !o.getDate().isAfter(endFrom)) &&  ((BumbleBee) o).BBId == this.BBId ){
                bumbles.add((BumbleBee) o);
            }
        }
        if(descending){
        bumbles.sort( (o1, o2) -> o2.getDate().compareTo(o1.getDate()));}
        else{
            bumbles.sort( (o1, o2) -> o1.getDate().compareTo(o2.getDate()));}


        return new ArrayList<Bee>(bumbles).iterator();
    }

    /**
     * Postconditions: Each element is an instance of BumbleBee and is valid, has BBId == this.BBId,
     * and isWild of the object equals the parameter isWild.
     * @return Iterator<Bee> of all valid BumbleBee observations.
     */
    @Override
    public Iterator<WildBee> wild(boolean isWild) {
        List<WildBee> thisBeeObs = new ArrayList<>();
        for(Observation o : Test.observations){
            if(o instanceof BumbleBee && o.valid() && ((BumbleBee) o).BBId == this.BBId
                    && ((BumbleBee) o).isWild == isWild ){
                thisBeeObs.add((WildBee) o);
            }
        }

        return thisBeeObs.iterator();

    }
}
