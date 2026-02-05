import java.util.List;

public final class Location {
    private final List<Double> args;

    public Location(List<Double> args) {
        if (args == null || args.isEmpty()) {
            throw new IllegalArgumentException("Please give at least one location argument");
        }
        this.args = List.copyOf(args);
    }

    public List<Double> getArgs() {
        return args;
    }
}
