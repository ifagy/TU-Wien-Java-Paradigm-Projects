import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class AndrenaBucephala implements CommunalBee {

    /**
     * class invariants:
     * ABId > 0
     */
    private boolean isCommunal;
    private boolean isWild;
    private final int ABId;
    private LocalDateTime date;
    private String comment;
    private boolean removed = false;

    /**
     * Constructor to create a AndrenaBucephala object.
     *
     * Preconditions:
     * - date != null
     * - comment != null
     * - ABId > 0
     *
     * Postconditions: An object of AndrenaBucephala is created.
     *
     * @param date : date of the observation made.
     * @param comment : comment of the observation
     * @param ABId : a unique id for an instance of a AndrenaBucephala.
     * @param isCommunal : specification for whether an instance of CommunalBee or not.
     * @param isWild : specification for whether an instance of WildBee or not.
     */
    public AndrenaBucephala(LocalDateTime date, String comment, int ABId,  boolean isCommunal,  boolean isWild){
        this.date = date;
        this.comment = comment;
        Test.observations.add(this);

        this.ABId = ABId;
        this.isCommunal = isCommunal;
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
     * Postconditions: All elements of the iterator are elements of AndrenaBucephala are valid and has isCommunal == true with the same ABID.
     *
     * @return Iterator<CommunalBee> of all valid AndrenaBucephala observations with isCommunal == true with the same ABId.
     */
    @Override
    public Iterator<CommunalBee> communal() {
        List<CommunalBee> communalBees = new ArrayList<>();
        for(Observation o: Test.observations){
            if(o instanceof AndrenaBucephala && ((AndrenaBucephala) o).isCommunal && o.valid() && this.ABId == ((AndrenaBucephala) o).ABId){ // "double" check: kann auch solitär leben
                communalBees.add((CommunalBee) o);
            }
        }
        return communalBees.iterator();
    }

    /**
     * Postconditions:
     * - Sorted ascending by getDate()
     * - Each element in the iterator is instanceof AndrenaBucephala and is valid.
     * @return Iterator<Bee> of all valid AndrenaBucephala observations with the same ABId.
     */
    @Override
    public Iterator<Bee> sameBee() {
        List<AndrenaBucephala> bucephalas = new ArrayList<>();
        for(Observation o :Test.observations){
            if(o instanceof AndrenaBucephala && o.valid() && ((AndrenaBucephala) o).ABId == this.ABId ){
                bucephalas.add((AndrenaBucephala)o);
            }
        }
        bucephalas.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        return new ArrayList<Bee>(bucephalas).iterator();
    }


    /**
     * Preconditions:
     * - endFrom >= startFrom
     * - endFrom != null, startFrom != null
     * Postconditions:
     * - Sorted descending if descending == true, ascending if descending == false by getDate().
     * - Each element in the iterator is instanceof Andrenabucephala and is valid.
     * @return Iterator<Bee> of all valid Andrenabucephala observations with the same ABId within the given interval  of time.
     */
    @Override
    public Iterator<Bee> sameBee(boolean descending, LocalDateTime startFrom, LocalDateTime endFrom) {
        List<AndrenaBucephala> bucephalas = new ArrayList<>();
        for(Observation o : Test.observations){
            if(o instanceof AndrenaBucephala && o.valid() && (!o.getDate().isBefore(startFrom)
                    && !o.getDate().isAfter(endFrom)) && ((AndrenaBucephala) o).ABId == this.ABId){ //the days startFrom and endFrom are also within the acceptance interval
                bucephalas.add((AndrenaBucephala)o);
            }
        }
        if (descending){
        bucephalas.sort( (o1, o2) -> o2.getDate().compareTo(o1.getDate()));}
        else{
            bucephalas.sort( (o1, o2) -> o1.getDate().compareTo(o2.getDate()));}

        return new ArrayList<Bee>(bucephalas).iterator();
    }


    /**
     * Postcondtions: All elements of the iterator are elements of AndrenaBucephala are valid and solitary with the same ABID.
     * @return Iterator<SolitaryBee> of all AndrenaBucephela observations with the same ABId that are solitary.
     */
    @Override
    public Iterator<SolitaryBee> solitary() {
        List<SolitaryBee> solitaryBees = new ArrayList<>();
        for(Observation o : Test.observations){
            if(!(o instanceof SocialBee) && o instanceof SolitaryBee &&
                    o.valid() && o instanceof AndrenaBucephala && !((AndrenaBucephala) o).isCommunal && this.ABId == ((AndrenaBucephala) o).ABId){
                solitaryBees.add((SolitaryBee) o);
            }
        }
        return solitaryBees.iterator();
    }

    /**
     * Postconditions: Each element is an instance of AndrenaBucephala and is valid, has ABId == this.ABId,
     * and isWild of the object equals the parameter isWild.
     * @return Iterator<Bee> of all valid AndrenaBucephala observations.
     */
    @Override
    public Iterator<WildBee> wild(boolean isWild) {
        List<WildBee> wildOrNotBees = new ArrayList<>();
            for(Observation o : Test.observations){
                if(o.valid() && o instanceof AndrenaBucephala && ((AndrenaBucephala) o).isWild == isWild ){
                    wildOrNotBees.add((WildBee) o);
                }
            }
            return wildOrNotBees.iterator();
    }
}
