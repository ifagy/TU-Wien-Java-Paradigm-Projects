import java.util.Iterator;

/**
 * Implementierung von OrdSet, basierend auf OSet.
 *
 * @param <E> Der Typ der Einträge, muss Modifiable<X, E> implementieren.
 * @param <X> Der Typ, der für plus/minus Operationen verwendet wird.
 *
 * @invariant Die intern gespeicherte Graphenstruktur der Ordnungsbeziehungen
 * ist immer zyklenfrei.
 * @invariant Alle bestehenden Ordnungsbeziehungen müssen jederzeit
 * konform mit der 'orderRelation' (falls != null) sein.
 */
public class MSet<E extends Modifiable<X, E>,X> implements OrdSet<E, Object> {

    /**
     * Interner Wrapper-Knoten für den Graphen.
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

    /**
     * Die Menge aller Elemente (Knoten im Graph)
     */
    private ListGeneric<Element> hasseElements = new ListGeneric<>();
    /**
     * Das Objekt 'c' zur Überprüfung erlaubter Ordnungsbeziehungen
     */
    private Ordered<? super E, ?> orderRelation = null; // 'c'

    /**
     * Konstruktor für MSet.
     *
     * @param orderRelation Das Objekt 'c' (kann null sein).
     * @post this.orderRelation ist auf orderRelation gesetzt.
     */
    public MSet(Ordered<? super E, ?> orderRelation) {
        this.orderRelation = orderRelation;
    }

    /**
     * {@inheritDoc}
     *
     * @return Ein Objekt, das Ordered<E, Boolean> UND Modifiable<E, OSetObjekt>
     * implementiert, wenn x vor y kommt.
     * Dieses Objekt enthält alle Elemente z, für die gilt:
     * (this.isReachable(x, z)) UND (this.isReachable(z, y)).
     * Gibt null zurück, falls x nicht vor y kommt.
     */
    @Override
    public Object before(E x, E y) {

        if(x == y) {return null;}

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

        return new MSetObjekt(res);
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

    /**
     * Interne Implementierung des von MSet.before() zurückgegebenen Objekts.
     */
    private class MSetObjekt implements Ordered<E, Boolean>, Modifiable<E, MSetObjekt> {

        /** Speichert die Elemente (Werte, nicht Knoten) dieser Teilmenge */
        private ListGeneric<E> hasseElementsO = new ListGeneric<>();

        /**
         * Privater Konstruktor.
         * @param hasseElementsx_y Die von OSet.before() berechnete Teilmenge.
         */
        public MSetObjekt(ListGeneric<E> hasseElementsx_y) {

            this.hasseElementsO = hasseElementsx_y;
        }

        /**
         * {@inheritDoc} (Modifiable)
         * Erstellt eine neue Teilmenge, die 'x' enthält.
         *
         * @param x Das hinzuzufügende Element.
         * @return 'this', wenn 'x' bereits enthalten ist,
         * sonst ein *neues* OSetObjekt mit dem hinzugefügten 'x'.
         * @post Das Original (this) bleibt unverändert.
         */
        @Override
        public MSetObjekt add(E x) {
            if (hasseElementsO.find(x) != null) {
                return this;
            }

            ListGeneric<E> hasseElementsAdded = new ListGeneric<>(hasseElementsO);
            hasseElementsAdded.add(x);

            return new MSetObjekt(hasseElementsAdded);
        }

        /**
         * {@inheritDoc} (Modifiable)
         * Erstellt eine neue Teilmenge, die 'x' nicht enthält.
         *
         * @param x Das zu entfernende Element.
         * @return 'this', wenn 'x' nicht enthalten ist,
         * sonst ein *neues* OSetObjekt ohne 'x'.
         * @post Das Original (this) bleibt unverändert.
         */
        @Override
        public MSetObjekt subtract(E x) {
            if (hasseElementsO.find(x) == null) {
                return this;
            }
            ListGeneric<E> hasseElementsSubtracted = new ListGeneric<>(hasseElementsO);
            hasseElementsSubtracted.remove(x);
            return new MSetObjekt(hasseElementsSubtracted);
        }

        /**
         * {@inheritDoc} (Ordered)
         * Prüft die Ordnung *innerhalb* dieser Teilmenge.
         *
         * @return 'true', wenn x und y in dieser Teilmenge sind UND
         * x im Haupt-OSet vor y kommt.
         * Gibt in allen anderen Fällen 'null' zurück.
         */
        @Override
        public Boolean before(E x, E y) {
            if (hasseElementsO.find(x) != null && hasseElementsO.find(y) != null) {

                return MSet.this.isReachable(x, y) ? true : null;
            }
            return null;
        }


        /**
         * {@inheritDoc} (Ordered)
         * Setzt die Ordnung im Haupt-OSet.
         *
         * @pre x und y müssen in dieser Teilmenge (Container) sein.
         * @pre Alle anderen Vorbedingungen von OSet.setBefore() müssen
         * ebenfalls gelten.
         * @throws IllegalArgumentException wenn x oder y nicht in dieser
         * Teilmenge sind.
         */
        @Override
        public void setBefore (E x, E y) throws IllegalArgumentException {
            if (hasseElementsO.find(x) == null || hasseElementsO.find(y) == null) {
                throw new IllegalArgumentException("Elements not in this Container.");
            }

            MSet.this.setBefore(x, y);

        }
    }

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException wenn eine der Vorbedingungen verletzt wird.
     * @pre x != y.
     * @pre (this.orderRelation = = null) ODER (this.orderRelation.before(x, y) != null).
     * @pre this.before(y, x) == null (Zyklenfreiheit).
     * @post x und y sind im Container vorhanden (ggf. neu eingefügt).
     * @post Eine Kante (Ordnungsbeziehung) von x nach y existiert.
     */
    @Override
    public void setBefore(E x, E y) throws IllegalArgumentException {
        if (x == y) {
            throw new IllegalArgumentException("x and y cannot be the same");
        }
        if (this.orderRelation != null && this.orderRelation.before(x, y) == null) {
            throw new IllegalArgumentException("This relation is not supported by this.orderRelation.");
        }
        if (this.before(y, x) != null) {
            throw new IllegalArgumentException("This relation is not supported by this.orderRelation.");
        }

        Element ex = findOrCreate(x);
        Element ey = findOrCreate(y);
        ex.addRelation(ey);

    }

    /**
     * Hilfsmethode: Findet ein 'Element' (Wrapper) anhand seines Werts 'x'.
     * Erstellt ein neues 'Element', fügt es zu 'hasseElementsO' hinzu und
     * gibt es zurück, falls es noch nicht existiert.
     *
     * @param x Der Wert, der gefunden oder erstellt werden soll.
     * @return Den existierenden oder neuen 'Element'-Wrapper.
     * @post 'x' ist im Container (hasseElementsO) enthalten.
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
     * @throws IllegalArgumentException wenn eine bestehende Beziehung
     *                                  (x -> y) ungültig ist (c.before(x, y) == null).
     * @post 'c' wird als neue 'orderRelation' gesetzt, WENN alle
     * bestehenden Beziehungen in 'this' auch in 'c' gültig sind.
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
     *
     * @return Die Anzahl der Einträge (Elemente) im Container.
     */
    @Override
    public int size() {
        return hasseElements.size();
    }

    /**
     * {@inheritDoc}
     *
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

    /**
     * Führt für jedes Element 'e' im Container die Operation
     * this.setBefore(e.add(x), e) aus.
     *
     * @param x Der Parameter für die 'add'-Operation.
     */
    public void plus(X x) {
        Iterator<E> iter = this.iterator();

        while (iter.hasNext()) {
            E e = iter.next();
            E addedElement = e.add(x);
            if (addedElement != e) {
                try {
                    this.setBefore(addedElement, e);
                } catch (IllegalArgumentException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }

    /**
     * Führt für jedes Element 'e' im Container die Operation
     * this.setBefore(e.subtract(x), e) aus.
     *
     * @param x Der Parameter für die 'subtract'-Operation.
     */
    public void minus(X x) {
        Iterator<E> iter = this.iterator();

        while (iter.hasNext()) {
            E e = iter.next();
            E subtractedElement = e.subtract(x);
            if (subtractedElement != e) {
                try {
                    this.setBefore(subtractedElement, e);
                } catch (IllegalArgumentException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }
}
