import java.util.Arrays;

/**
 * Immutable Data Record representing a Bee/FlowerPatch.
 * Holds the coordinates (parameters) and the calculated value (fitness).
 * Double.NaN for 'value' indicates it has not been evaluated yet.
 */
public record FlowerPatch(double value, double... coordinates) {

    @Override
    public String toString() {
        return "[Val:" + value + "] | [Coordiantes:" + Arrays.toString(coordinates) + "]";
    }

}
