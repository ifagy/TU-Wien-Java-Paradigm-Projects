import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Implementierung von OrdSet.
 * Verwendet eine interne Graphenstruktur (basierend auf Adjazenzlisten),
 * um die partiellen Ordnungsbeziehungen zu speichern.
 *
 * @param <E> Der Typ der Einträge im Container.
 *
 * @invariant Die intern gespeicherte Graphenstruktur der Ordnungsbeziehungen
 * ist immer zyklenfrei.
 * @invariant Alle bestehenden Ordnungsbeziehungen (Kanten im Graph) müssen
 * jederzeit konform mit der 'orderRelation' (falls != null) sein.
 * (Dies wird durch check, checkForced und setBefore sichergestellt).
 */
public class ISet<E> implements OrdSet<E, Iterator<E>> {
    /**
     * Interner Wrapper-Knoten für den Graphen.
     * Speichert den Wert (Element E) und eine Liste von Elementen,
     * die in der Ordnung *nach* diesem Element kommen (Adjazenzliste).
     */
    private class Element {
        private E value;
        private ListGeneric<Element> relatesTo = new ListGeneric<>();

        Element(E value) {
            this.value = value;
        }

        public void addRelation(Element e) {
            relatesTo.add(e);
        }

        public E getValue() {
            return value;
        }

        public ListGeneric<Element> getRelatesTo() {
            return relatesTo;
        }

    }

    /** Die Menge aller Elemente (Knoten im Graph) */
    private ListGeneric<Element> hasseElements = new ListGeneric<>();
    /** Das Objekt 'c' zur Überprüfung erlaubter Ordnungsbeziehungen*/
    private Ordered<? super E, ?> orderRelation = null; // 'c'

    /**
     * Konstruktor für ISet.
     *
     * @param orderRelation Das Objekt 'c', das für die initiale Überprüfung
     * verwendet wird (kann null sein).
     * @post this.orderRelation ist auf orderRelation gesetzt.
     */
    public ISet(Ordered<? super E, ?> orderRelation) {
        this.orderRelation = orderRelation;
    }

    /**
     * {@inheritDoc}
     *
     * @return Ein Iterator über alle Elemente z, für die gilt:
     * (this.before(x, z) != null) UND (this.before(z, y) != null).
     * Gibt null zurück, falls x nicht vor y kommt (this.before(x, y) == null).
     * @post Die 'remove'-Methode des zurückgegebenen Iterators ist nicht implementiert.
     */
    @Override
    public Iterator<E> before(E x, E y) {
        if (x == y) {
            return null;
        }
        //not reachable
        if (!isReachable(x,y)) {
            return null;
        }

        ListGeneric<E> res = new ListGeneric<>();
        for (Element ez : hasseElements) {
            E z = ez.getValue();
            if (z==x || z==y) {continue;}

            if (isReachable(x,z) && isReachable(z,y)){
                res.add(z);
            }
        }
        return res.iterator();
    }

    /**
     * Private Hilfsmethode (Tiefensuche), um die transitive Erreichbarkeit
     * von x nach y im Graphen zu prüfen. (Stellt fest, ob x vor y kommt).
     *
     * @param x Startknoten (Wert)
     * @param y Endknoten (Wert)
     * @return true, wenn ein Pfad von x nach y existiert (x == y eingeschlossen),
     * sonst false.
     */
    private boolean isReachable(E x, E y) {
        if(x == y){return true;}
        Element ex = find(x);

        if (ex == null) {
            return false;
        }

        for (Element neighbor : ex.getRelatesTo()) {
            if (isReachable(neighbor.getValue(), y)) {
                return true;
            }
        }
        return false;
    }

    /*
    returns a path x nach y
    private ListGeneric<Element> beforeRekursive(E x, E y, ListGeneric<Element> path) {

        Element ex = find(x);
        if (ex.getRelatesTo().size() == 0 && x != y) {
            return null;
        }
        if (x == y) {
            return path;
        }
        for (Element e : ex.getRelatesTo()) {
            ListGeneric<Element> newPath = new ListGeneric<>(path);
            newPath.add(e);

            ListGeneric<Element> foundPath = beforeRekursive(e.getValue(), y, newPath);
            if (foundPath != null) {
                return foundPath;
            }
        }
        return null;

    }


     */


    /**
     * {@inheritDoc}
     *
     * @pre x != y.
     * @pre (this.orderRelation == null) ODER (this.orderRelation.before(x, y) != null).
     * @pre this.before(y, x) == null (Zyklenfreiheit).
     * @post x und y sind im Container vorhanden (ggf. neu eingefügt).
     * @post Eine Kante (Ordnungsbeziehung) von x nach y existiert.
     * @throws IllegalArgumentException wenn eine der Vorbedingungen verletzt wird.
     */
    @Override
    public void setBefore(E x, E y) throws IllegalArgumentException {
        if (x == y) {
            throw new IllegalArgumentException("x and y cannot be the same");
        }
        Element ex = findOrCreate(x);
        Element ey = findOrCreate(y);
        if (this.orderRelation != null && this.orderRelation.before(x, y) == null) {
            throw new IllegalArgumentException("This relation is not supported by this.orderRelation.");
        }
        if (this.before(y, x) != null) {
            throw new IllegalArgumentException("This relation is not supported by this.orderRelation.");
        }

        ex.addRelation(ey);

    }

    /**
     * Hilfsmethode: Findet ein 'Element' (Wrapper) anhand seines Werts 'x'.
     * Erstellt ein neues 'Element', fügt es zu 'hasseElements' hinzu und
     * gibt es zurück, falls es noch nicht existiert.
     *
     * @param x Der Wert, der gefunden oder erstellt werden soll.
     * @return Den existierenden oder neuen 'Element'-Wrapper.
     * @post 'x' ist im Container (hasseElements) enthalten.
     */
    private Element findOrCreate(E x) {
        Element res = find(x);
        if (res != null) {
            return res;
        }

        res = new Element(x);
        hasseElements.add(res);
        return res;
    }

    /**
     * Hilfsmethode: Findet ein 'Element' (Wrapper) anhand seines Werts 'x'
     * mittels Objektidentität (==).
     *
     * @param x Der zu suchende Wert.
     * @return Das 'Element' oder null, falls nicht gefunden.
     */
    private Element find(E x) {
        for (Element e : hasseElements) {
            if (e.getValue() == x) {
                return e;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @post 'c' wird als neue 'orderRelation' gesetzt, WENN alle
     * bestehenden Beziehungen in 'this' auch in 'c' gültig sind.
     * @throws IllegalArgumentException wenn eine bestehende Beziehung
     * (x -> y) ungültig ist (c.before(x, y) == null).
     */
    @Override
    public <C extends Ordered<? super E, ?>> void check(C c) throws IllegalArgumentException {

        for (Element elementX : hasseElements) {
            E valueX = elementX.getValue();
            for (Element elementY : elementX.getRelatesTo()) {
                E valueY = elementY.getValue();
                if (c.before(valueX, valueY) == null) {
                    throw new IllegalArgumentException("This relation is not supported by this.orderRelation.");
                }
            }
        }
        orderRelation = c;

    }

    /**
     * {@inheritDoc}
     *
     * @post 'c' wird auf jeden Fall als neue 'orderRelation' gesetzt.
     * @post Alle bestehenden Beziehungen (x -> y), die von 'c' NICHT
     * erlaubt werden (c.before(x, y) == null), werden entfernt.
     */
    @Override
    public <C extends Ordered<? super E, ?>> void checkForced(C c) {
        for (Element elementX : hasseElements) {
            E valueX = elementX.getValue();
            for (Element elementY : elementX.getRelatesTo()) {
                E valueY = elementY.getValue();
                if (c.before(valueX, valueY) == null) {
                    elementX.getRelatesTo().remove(elementY);
                }
            }
        }
        orderRelation = c;

    }

    /**
     * {@inheritDoc}
     * @return Die Anzahl der Einträge (Elemente) im Container.
     */
    @Override
    public int size() {
        return hasseElements.size();
    }

    /**
     * {@inheritDoc}
     * @return Ein Iterator, der in beliebiger Reihenfolge über alle
     * Einträge im Container läuft.
     * @post Die 'remove'-Methode des Iterators ist nicht implementiert.
     */
    @Override
    public Iterator<E> iterator() {
        ListGeneric<E> listE = new ListGeneric<>();
        for (Element e : hasseElements) {
            listE.add(e.getValue());
        }
        return listE.iterator();
    }


}
