public interface Modifiable<X, T> {
    /**
     * Returns a new object that extends @this by the parameter x, while
     * keeping @this and x intact.
     *
     * Postconditions:
     * - @this and x remain intact.
     * - @this is extended by x.
     * - if @this is not extendable by x, the result is identical of @this.
     * @param x the parameter (of type X) used to extend @this.
     * @return a new object of type T.
     */
    T add(X x);

    /**
     * Returns a new object @this from which x is removed, while
     * keeping @this and x intact.
     *
     * Postconditions:
     * - @this and x remain intact.
     * - x is removed from @this.
     * - if x is not removable from @this, the result is identical of @this.
     * @param x the parameter (of type X) which is to be removed.
     * @return a new object of type T.
     */
    T subtract(X x);
}
