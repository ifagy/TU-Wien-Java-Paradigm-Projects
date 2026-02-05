import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class OsmiaCornuta implements SolitaryBee{

    /**
     * class invariants:
     * OCId > 0
     */
    private final int OCId;
    private boolean isWild;
    private LocalDateTime date;
    private String comment;
    private boolean removed = false;

    /**
     * Constructor to create a OsmiaCornuta object
     *
     * Preconditions:
     *  date != null
     *  comment != 0
     *  OCId > 0
     *
     * Postconditions: An object of OsmiaCornuta is created.
     *
     * @param date : date of the observation made.
     * @param comment : comment of the observation
     * @param OCId : a unique id for an instance of a OsmiaCornuta.
     * @param isWild : specification for whether an instance of WildBee or not.
     */
    public OsmiaCornuta(LocalDateTime date, String comment, int OCId, boolean isWild) {
        this.date = date;
        this.comment = comment;
        Test.observations.add(this);

        this.OCId = OCId;
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
     * Postconditions:
     *  All elements of the iterator are instances of OsmiaCornuta
     *  All elements are valid observations and have the same OCId as this object
     *
     * @return Iterator<SolitaryBee> of all valid OsmiaCornuta observations with the same OCId
     */
    @Override
    public Iterator<SolitaryBee> solitary() {
        List<SolitaryBee> thisBeeObs = new ArrayList<>();
        for(Observation o : Test.observations){
            if(o instanceof OsmiaCornuta && o.valid() && ((OsmiaCornuta) o).OCId == this.OCId){
                thisBeeObs.add((SolitaryBee) o);
            }
        }

        return thisBeeObs.iterator();
    }

    /**
     * Postconditions:
     *  All elements of the iterator are instances of OsmiaCornuta
     *  All elements are valid observations
     *  All elements have the same OCId as this object and isWalid value equal to the parameter isWild
     *
     * @param isWild the wild status filter by
     * @return Iterator<WildBee> of all valid OsmiaCornuta observation with matching wild status and same OCId
     */
    @Override
    public Iterator<WildBee> wild(boolean isWild) {
        List<WildBee> thisBeeObs = new ArrayList<>();

        for(Observation o : Test.observations){
            if(o instanceof OsmiaCornuta && o.valid() && ((OsmiaCornuta) o).OCId == this.OCId
                    && ((OsmiaCornuta) o).isWild == isWild ){
                thisBeeObs.add((WildBee) o);
            }
        }

        return thisBeeObs.iterator();
    }

    /**
     * Postconditions:
     *  All elements are instances of OsmiaCornuta
     *  All elements are valid observations and have the same OCId as this object
     *  Sorted ascending by getDate()
     *
     * @return Iterator<Bee> of all valid OsmiaCornuta observations with the same OCId
     */
    @Override
    public Iterator<Bee> sameBee() {
        List<OsmiaCornuta> cornutas = new ArrayList<>();
        for(Observation o :Test.observations){
            if(o instanceof OsmiaCornuta && o.valid() && ((OsmiaCornuta) o).OCId == this.OCId ){
                cornutas.add((OsmiaCornuta) o);
            }
        }
        cornutas.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        return new ArrayList<Bee>(cornutas).iterator();
    }

    /**
     * Preconditions:
     *  endFrom >= startFrom
     *  endFrom != null, startFrom != null
     *
     * Postconditions:
     *  Sorted descending if descending == true, ascending if descending == false by getDate()
     *  All elements are instances of OsmiaCornuta within the time window [startFrom, endFrom]
     *  All elements are valid observations and have the same OCId as this object
     *
     * @param descending sort order (true = descending, false = ascending)
     * @param startFrom start of time window
     * @param endFrom end of time window
     * @return Iterator<Bee> of all valid OsmiaCornuta observations with the same OCId within the time window
     */
    @Override
    public Iterator<Bee> sameBee(boolean descending, LocalDateTime startFrom, LocalDateTime endFrom) {
        List<OsmiaCornuta> cornutas = new ArrayList<>();
        for(Observation o : Test.observations){
            if(o instanceof OsmiaCornuta && o.valid() && (!o.getDate().isBefore(startFrom)
                    && !o.getDate().isAfter(endFrom)) && ((OsmiaCornuta) o).OCId == this.OCId ){
                cornutas.add((OsmiaCornuta) o);
            }
        }

        if(descending){
        cornutas.sort( (o1, o2) -> o2.getDate().compareTo(o1.getDate()));}
        else{
            cornutas.sort( (o1, o2) -> o1.getDate().compareTo(o2.getDate()));}

        return new ArrayList<Bee>(cornutas).iterator();
    }
}
