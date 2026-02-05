/**
 * Num serves testing purposes and contains an immutable number
 * Implements Modifiable<Num, Num>
 */
public class Num implements Modifiable<Num, Num>{
    private final int value;

    /**
     * Creates a new Num instance with specified value
     *
     * Postcondition: this.toString() returns the value as text
     *                this.add() and this.subtract() operate on this value
     * @param value The numeric value
     */
    public Num(int value) {
        this.value = value;
    }

    /**
     * Returns the numeric value
     *
     * Postcondition: return value == the value passed to constructor
     *
     * @return The integer value
     */
    public int value() {
        return value;
    }

    /**
     * {@inheritDoc}
     * Gibt eine *neue* Instanz von Num zurück, die die Summe
     * der Zahlen in 'this' und 'y' enthält.
     *
     * @param y Das Num-Objekt, dessen Wert addiert werden soll.
     * @return Ein neues Num-Objekt mit dem Wert (this.value + y.value).
     * @post Das Ergebnis ist niemals 'this'.
     */
    @Override
    public Num add(Num y) {
        if (y == null) return this;
        return new Num(this.value + y.value);
    }

    /**
     * {@inheritDoc}
     * Gibt eine *neue* Instanz von Num zurück, die die Differenz
     * der Zahlen in 'this' und 'y' enthält.
     *
     * @param y Das Num-Objekt, dessen Wert subtrahiert werden soll.
     * @return Ein neues Num-Objekt mit dem Wert (this.value - y.value).
     * @post Das Ergebnis ist niemals 'this'.
     */
    @Override
    public Num subtract(Num y) {
        if (y == null) return this;
        return new Num(this.value - y.value);
    }

    /**
     * Returns the numeric value as text
     *
     * Postcondition: return value != null
     *                return value is the string representation of the value
     *
     * @return The number as string
     */
    @Override
    public String toString() {
        return Integer.toString(value);
    }

    /**
     * Gibt den internen Wert zurück.
     * @return die gespeicherte Zahl.
     */
    public int getValue() {
        return this.value;
    }
}
