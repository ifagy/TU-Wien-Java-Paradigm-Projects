public interface Ordered<E, R> {

    /**
     * Determines whether x in @this before y comes.
     *
     * Postconditions:
     * - The result depends only on @this, x and y.
     * - @this, x and y are intact.
     * - The returned value is null, when x doesn't come
     *  before y in @this. Otherwise, anything unequal null can be returned.
     * @param x first element of type E.
     * @param y second element of type E.
     * @return a non-null result of type R, if x comes before y, otherwise null.
     */
    R before (E x, E y);

    /**
     * Modifies @this so that, after execution, x precedes y in the order.
     *
     * Postconditions:
     * - The order is changed in @this such that x comes before y.
     * - If preconditions are not met, an {@code IllegalArgumentException} might be thrown, depending on the implementation.
     *
     * @param x The element that should come before y.
     * @param y The element that x should precede.
     */
    void setBefore(E x, E y);
}
