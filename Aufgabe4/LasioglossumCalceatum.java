import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class LasioglossumCalceatum implements SocialBee, SolitaryBee {

    /**
     * class invariants:
     * LCId > 0
     */
    private final int LCId;
    private boolean isSocial; // true-> Social, false-> Solitary
    private boolean isWild;
    private LocalDateTime date;
    private String comment;
    private boolean removed = false;


    /**
     * Constructor to create a LasioglossumCalceatum object.
     *
     * Preconditions:
     *  date != null
     *  comment != null
     *  LCId > 0
     *
     * Postconditions: An object of LasioglossumCalceatum is created.
     *
     * @param date : date of the observation made.
     * @param comment : comment of the observation
     * @param LCId : a unique id for an instance of a LasioglossumCalceatum.
     * @param isSocial : specification for whether an instance of SocialBee or not.
     * @param isWild : specification for whether an instance of WildBee or not.
     */
    public LasioglossumCalceatum(LocalDateTime date, String comment, int LCId, boolean isSocial, boolean isWild) {
        this.date = date;
        this.comment = comment;
        Test.observations.add(this);

        this.LCId = LCId;
        this.isSocial = isSocial;
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
     *  All elements of the iterator are instances of LasioglossumCalceatum
     *  All elements are valid observations
     *  All elements have the same LCId as this object and isSocial == true
     *
     * @return Iterator<SocialBee> of all valid LasioglossumCalceatum observations with isSocial == true and same LCId
     */
    @Override
    public Iterator<SocialBee> social() {

        List<SocialBee> thisBeeObs = new ArrayList<>();
        for (Observation o : Test.observations) {
            if (o instanceof LasioglossumCalceatum && o.valid()
                    && ((LasioglossumCalceatum) o).LCId == this.LCId
                    && ((LasioglossumCalceatum) o).isSocial) {
                thisBeeObs.add((SocialBee) o);
            }
        }

        return thisBeeObs.iterator();
    }

    /**
     * Postconditions:
     *  All elements of the iterator are instances of LasioglossumCalceatum
     *  All elements are valid observations
     *  All elements have the same LCId as this object and isSocial == false
     *
     * @return Iterator<SolitaryBee> of all valid LasioglossumCalceatum observations with isSocial == false and same LCId
     */
    @Override
    public Iterator<SolitaryBee> solitary() {
        List<SolitaryBee> thisBeeObs = new ArrayList<>();
        for (Observation o : Test.observations) {
            if (o instanceof LasioglossumCalceatum && o.valid()
                    && ((LasioglossumCalceatum) o).LCId == this.LCId
                    && !((LasioglossumCalceatum) o).isSocial) {
                thisBeeObs.add((SolitaryBee) o);
            }
        }

        return thisBeeObs.iterator();
    }

    /**
     * Postconditions:
     *  All elements of the iterator are instances of LasioglossumCalceatum
     *  All elements are valid observations
     *  All elements have the same LCId as this object and isWild value equal to the parameter isWild
     *
     * @param isWild the wild status to filter by
     * @return Iterator<WildBee> of all valid LasioglossumCalceatum observations with matching wild status and same LCId
     */
    @Override
    public Iterator<WildBee> wild(boolean isWild) {

        List<WildBee> thisBeeObs = new ArrayList<>();
        for (Observation o : Test.observations) {
            if (o instanceof LasioglossumCalceatum && o.valid()
                    && ((LasioglossumCalceatum) o).LCId == this.LCId
                    && ((LasioglossumCalceatum) o).isWild == isWild) {
                thisBeeObs.add((WildBee) o);
            }
        }
        return thisBeeObs.iterator();
    }

    /**
     * Postconditions:
     *  Sorted ascending by getDate()
     *  All elements are instances of LasioglossumCalceatum
     *  All elements are valid observations and have the same LCId as this object
     *
     * @return Iterator<Bee> of all valid LasioglossumCalceatum observations with the same LCId
     */
    @Override
    public Iterator<Bee> sameBee() {
        List<LasioglossumCalceatum> calceatums = new ArrayList<>();
        for (Observation o : Test.observations) {
            if (o instanceof LasioglossumCalceatum && o.valid() && ((LasioglossumCalceatum) o).LCId == this.LCId) {
                calceatums.add((LasioglossumCalceatum) o);
            }
        }
        calceatums.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        return new ArrayList<Bee>(calceatums).iterator();
    }


    /**
     * Preconditions:
     *  endFrom >= startFrom
     *  endFrom != null, startFrom != null
     *
     * Postconditions:
     *  Sorted descending if descending == true, ascending if descending == false by getDate()
     *  All elements are instances of LasioglossumCalceatum
     *  All elements are valid observations within the time window [startFrom, endFrom]
     *  All elements have the same LCId as this object
     *
     * @param descending sort order (true = descending, false = ascending)
     * @param startFrom start of time window (inclusive)
     * @param endFrom end of time window (inclusive)
     * @return Iterator<Bee> of all valid LasioglossumCalceatum observations with the same LCId within the time window
     */
    @Override
    public Iterator<Bee> sameBee(boolean descending, LocalDateTime startFrom, LocalDateTime endFrom) {
        ArrayList<LasioglossumCalceatum> calceatums = new ArrayList<>();
        for (Observation o : Test.observations) {
            if (o instanceof LasioglossumCalceatum && o.valid() && (!o.getDate().isBefore(startFrom)
                    && !o.getDate().isAfter(endFrom)) && ((LasioglossumCalceatum) o).LCId == this.LCId) {
                calceatums.add((LasioglossumCalceatum) o);
            }
        }
        if (descending) {
            calceatums.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
        } else {
            calceatums.sort((o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        }
        return new ArrayList<Bee>(calceatums).iterator();
    }
}
