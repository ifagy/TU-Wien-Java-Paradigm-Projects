public interface OrdSet <E, R> extends Ordered<E,R>, java.lang.Iterable<E> {

    /**
     * Modifies @this so that, after execution, x precedes y in the order.
     * Preconditions:
     * - x and y must not be identical.
     * - If the constraint object c is not null (c != null), then c.before(x, y) must not return null. [cite: 37]
     * - this.before(y, x) must return null
     * Postconditions:
     * - elements which are already present will not be inserted again.
     * - if there was no order relation between parameters, the relationship is established.
     * - in the case where any precondition is violated, {@code IllegalArgumentException} is thrown.
     *
     * @param x The element that should come before y.
     * @param y The element that x should precede.
     * @throws IllegalArgumentException if a precondition is violated.
     */
    void setBefore(E x, E y) throws IllegalArgumentException;
    <C extends Ordered<? super E, ?>> void check(C c) throws IllegalArgumentException;

    <C extends Ordered<? super E, ?>> void checkForced(C c);

    int size();
}
