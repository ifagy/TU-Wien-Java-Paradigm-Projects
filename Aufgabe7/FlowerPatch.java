import java.util.Arrays;

// f(coordinates)= value oder f: coordinates->value
public record FlowerPatch(double value, double... coordinates) {

    @Override
    public String toString() {
        return "[Val:" + value + "] | [Coordiantes:" + Arrays.toString(coordinates) + "]";
    }

}
