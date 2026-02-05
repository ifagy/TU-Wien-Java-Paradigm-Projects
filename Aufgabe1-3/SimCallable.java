import java.util.concurrent.Callable;
import java.util.stream.IntStream;

/**
 * STYLE: parallel/Nebenläuifg: This class implements the Callable interface
 * to wrap a single, complete simulation run (e.g. 25 years) as an independent, executable task.
 * <p>
 * Abstraction:
 * This class is an object-oriented abstraction for a unit of parallel work.
 * It encapsulates one entire `YearlySim` run so it can be submitted to
 * an ExecutorService.
 *
 * <p>Parallel Goal & Method:</p>
 * The concrete goal is to achieve a performance speedup by executing
 * many independent simulations in parallel on a thread pool, rather than running them sequentially.
 * This class achieves this by holding its own instance of a `YearlySim` object
 * and running its full life-cycle within the `call()` method.
 *
 * <p>Interaction with Program:</p>
 * The rest of the program specifically "Test.java" supports this parallel goal.
 * The client code in "Test.java" creates a new deep
 * copy of the "FlowerGroup" for each simulation run.
 * This "FlowerGroup" copy is then passed to the "YearlySim" object,
 * which is in turn passed to this `SimCallable` task.
 * <p>
 * This design is ideal as it completely avoids shared mutable state between tasks. Each parallel thread operates on its own isolated data,
 * preventing data races and ensuring the parallel execution is safe and correct.
 */

public class SimCallable implements Callable<YearlySim> {

    private final YearlySim sim;
    private final int simLength;


    /**
     * Precondition:
     * # sim != null
     * # simLengthYear > 0
     * <p>
     * Postcondition:
     * # this.sim == sim
     * # this.simLength == simLengthYear
     */
    SimCallable(YearlySim sim, int simLengthYear) {
        this.sim = sim;
        this.simLength = simLengthYear;
    }

    /**
     * Precondition:
     * # The object must have been initialized with a valid sim and simLength.
     * <p>
     * Postcondition:
     * # sim.simVegetationPhase() and sim.simRestingPhase() are called a total of 'simLength' times.
     * # The returned YearlySim objects state is modified, having been simulated for 'simLength' years.
     */
    @Override
    public YearlySim call() {
        IntStream.rangeClosed(1, simLength).forEach(i->{
            sim.simVegetationPhase();
            sim.simRestingPhase();
        });
        return sim;
    }
}
