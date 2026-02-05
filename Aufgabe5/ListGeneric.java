import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * Eigene generische Listenimplementierung.
 * Ersetzt die verbotene Verwendung von java.util.Collection-Klassen.
 * Basiert auf einer einfach verketteten Liste.
 *
 * @param <A> Der Typ der Elemente, die in dieser Liste gespeichert werden.
 */
public class ListGeneric<A> implements Iterable<A> {

    /**
     * Interner Knoten der verketteten Liste.
     * Kapselt den Wert und die Referenz auf den nächsten Knoten.
     */
    private class Node {
        private A value;
        private Node nextNode = null;

        private Node(A a) {
            value = a;
        }

    }

    private int size = 0;
    private Node head, tail;

    /**
     * Erstellt eine neue, leere Liste.
     *
     * @post this.size() == 0
     */
    public ListGeneric() {
    }

    /**
     * Erstellt eine neue Liste, die eine flache Kopie der übergebenen Liste ist.
     *
     * @param list Die zu kopierende Liste.
     * @post this enthält alle Elemente aus 'list' in derselben Reihenfolge.
     */
    public ListGeneric(ListGeneric<A> list) {
        for (A e : list) {
            this.add(e);
        }
    }


    /**
     * Fügt ein neues Element am Ende der Liste hinzu.
     *
     * @param newNodeVal Das hinzuzufügende Element.
     * @return Das hinzugefügte Element.
     * @post Das Element befindet sich am Ende der Liste.
     * @post size() ist um 1 erhöht.
     */
    public A add(A newNodeVal) {
        if (head == null) {
            head = new Node(newNodeVal);
            tail = head;
        } else {
            tail.nextNode = new Node(newNodeVal);
            tail = tail.nextNode;
        }
        size++;
        return newNodeVal;

    }

    /**
     * Gibt die Anzahl der Elemente in der Liste zurück.
     *
     * @return die Anzahl der Elemente.
     * @post size() >= 0
     */
    public int size() {
        return size;
    }

    /**
     * Entfernt das erste Vorkommen des angegebenen Elements aus dieser Liste,
     * falls es vorhanden ist. Verwendet Objektidentität (==) für den Vergleich.
     *
     * @param value Das zu entfernende Element.
     * @post Wenn 'value' gefunden wurde, ist das erste Vorkommen entfernt
     * und size() ist um 1 reduziert.
     * @post Wenn 'value' nicht gefunden wurde, bleibt die Liste unverändert.
     */
    public void remove(A value) {

        if (head == tail) {
            head = null;
            tail = null;
            size--;
            return;
        }
        if (head.value == value) {
            head = head.nextNode;
            if (head == null) {
                tail = null;
            }
            size--;
            return;
        }
        Node current = head;
        while (current.nextNode != null) {
            if (current.nextNode.value==value) {
                current.nextNode = current.nextNode.nextNode;
                size--;
                if (current.nextNode == null) {
                    tail = current;
                }
                return;
            }
            current = current.nextNode;
        }

    }

    /**
     * Sucht nach dem ersten Vorkommen eines Elements in der Liste mittels Objektidentität (==).
     *
     * @param find Das zu suchende Element.
     * @return Das gefundene Element oder null, wenn es nicht in der Liste ist.
     */
    public A find(A find) {
        Iterator<A> itr = this.iterator();

        while (itr.hasNext()) {
            A current = itr.next();
            if (current == find) {
                return current;
            }
        }
        return null;
    }

    /**
     * Gibt einen Iterator über die Elemente in dieser Liste in der richtigen Reihenfolge zurück.
     *
     * @return ein Iterator.
     * @post Die 'remove'-Methode des Iterators ist nicht implementiert und wirft
     * eine {@code UnsupportedOperationException}.
     */

    public Iterator<A> iterator() {
        return new Iterator<A>() {

            Node current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public A next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                A res = current.value;
                current = current.nextNode;
                return res;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove wird nicht unterstützt.");
            }
        };
    }

}
