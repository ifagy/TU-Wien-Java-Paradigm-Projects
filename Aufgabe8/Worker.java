import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * Main class for a Child Process.
 * Manages the bee population, coordinates the k threads for evaluation,
 * performs the recruitment phase sequentially, and pipes results back to ExecuteBA.
 */
public class Worker {

    public static ArrayList<FlowerPatch> blocks;
    public static final Object sem = new Object();
    public static volatile boolean newBlocksSem = false;
    public static int workerFinishedThreadsCount = 0;
    public static int curIndex = 0;
    public static final ArrayList<FlowerPatch> nextBlock = new ArrayList<>();

    public static volatile boolean finished = false;

    private static PrintWriter logger;

    // error detection easier
    public static void logging(String msg) {
        if (logger != null) {
            logger.println(System.currentTimeMillis() + " : " + msg); //SO THAT WE UNDERSTAND CA. WHERE THE ERROR WAS!!!
            logger.flush();
        }
    }

    public static void main(String[] args) {

        int id = Integer.parseInt(args[0]);
        int a = Integer.parseInt(args[1]);
        int f_id = Integer.parseInt(args[2]);
        int c = Integer.parseInt(args[3]);
        int t = Integer.parseInt(args[4]);
        int n = Integer.parseInt(args[5]);
        int m = Integer.parseInt(args[6]);
        int e = Integer.parseInt(args[7]);
        int p = Integer.parseInt(args[8]);
        int q = Integer.parseInt(args[9]);
        int g = Integer.parseInt(args[10]);
        double s = Double.parseDouble(args[11]);
        int r = Integer.parseInt(args[12]);
        int b = Integer.parseInt(args[13]);
        int k = Integer.parseInt(args[14]);

        try {
            logger = new PrintWriter(new FileWriter("worker_" + id + ".log"), true);
            logging("Worker started with the id of: " + id + ", and Threads : " + k);

            try (Scanner scanner = new Scanner(System.in)) {

                scanner.useLocale(Locale.US); //for the format difference ,/.
                double[][] bounds = new double[a][2];

                for (int i = 0; i < a; i++) {
                    if (scanner.hasNextDouble()) {
                        bounds[i][0] = scanner.nextDouble();
                        bounds[i][1] = scanner.nextDouble();
                    }
                }
                // Initial state: Randomly distributed scout bees
                blocks = Stream.generate(() -> createInitialRandom(a, bounds)).limit(n).collect(Collectors.toCollection(ArrayList::new));


                for (int i = 0; i < k; i++) {

                    WorkerRun workerRun = new WorkerRun(a, f_id, c, t, n, m, e, p, q, g, s, r, b, k, bounds);
                    Thread thread = new Thread(workerRun);
                    thread.start();

                }

                for (int iteration = 0; iteration < t; iteration++) {

                    synchronized (sem) {
                        nextBlock.clear();
                        curIndex = 0;
                        workerFinishedThreadsCount = 0;
                        newBlocksSem = true;
                        sem.notifyAll();

                        while (workerFinishedThreadsCount < k) {
                            try {
                                sem.wait();
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }

                        newBlocksSem = false;
                    }

                    //recruit
                    ArrayList<FlowerPatch> newBlocks = new ArrayList<>(nextBlock);
                    newBlocks.sort((p1, p2) -> Functions.callCompareById(c).apply(p1.value(), p2.value()));
                    blocks.clear();
                    //nextBlock.clear();

                    //elite
                    for (int i = 0; i < e && i < newBlocks.size(); i++) {
                        FlowerPatch eliteBee = newBlocks.get(i);
                        blocks.add(eliteBee);
                        blocks.addAll(recruitNeighbors(eliteBee, a, p, s, bounds));

                    }
                    //m-e
                    for (int i = e; i < m && i < newBlocks.size(); i++) {
                        FlowerPatch bestBee = newBlocks.get(i);
                        blocks.add(bestBee);
                        blocks.addAll(recruitNeighbors(bestBee, a, q, s, bounds));
                    }

                    //random g
                    blocks.addAll(Stream.generate(() -> createInitialRandom(a, bounds)).limit(g).collect(Collectors.toCollection(ArrayList::new)));

                    //pipe result
                    // nextBlock.clear();


                }
                synchronized (sem) {
                    finished = true;
                    sem.notifyAll();
                }

                if (!nextBlock.isEmpty()) {
                    nextBlock.sort((p1, p2) -> Functions.callCompareById(c).apply(p1.value(), p2.value()));
                    nextBlock.stream().limit(r).forEach(fp -> System.out.println("RESULT: " + fp.value() + ": " + Arrays.toString(fp.coordinates())));
                } else {

                    blocks.stream().filter(fp -> !Double.isNaN(fp.value())).
                            sorted((p1, p2) -> Functions.callCompareById(c).apply(p1.value(), p2.value())).limit(r).
                            forEach(fp -> System.out.println("RESULT: " + fp.value() + ": " + Arrays.toString(fp.coordinates())));


                }
                System.out.flush();
            }


        } catch (Exception ex) {
            ex.printStackTrace();
            if (logger != null) {
                ex.printStackTrace(logger);
            }
        } finally {
            if (logger != null) {
                logger.close();
            }
        }


    }

    private static final Random rand = new Random();

    /** Creates a random bee within the process boundaries. */
    private static FlowerPatch createInitialRandom(int a, double[][] bounds) {
        double[] coords = IntStream.range(0, a)       // create array[params.a]
                .mapToDouble(i -> {                             // fill array with random parameters
                    double min = bounds[i][0];
                    double max = bounds[i][1];
                    return min + (max - min) * rand.nextDouble();
                })
                .toArray();
        return new FlowerPatch(Double.NaN, coords);

    }

    /** Generates neighbor bees around a specific patch. */
    private static List<FlowerPatch> recruitNeighbors(FlowerPatch patch, int a, int beeNum, double s, double[][] bounds) {
        List<FlowerPatch> res = new ArrayList<>();
        for (int i = 0; i < beeNum; i++) {
            double[] cords = generateCoordinates(patch, a, s, bounds);
            res.add(new FlowerPatch(Double.NaN, cords));
        }

        return res;
    }

    /** Helper to calculate neighbor coordinates within bounds. */
    private static double[] generateCoordinates(FlowerPatch patch, int a, double s, double[][] bounds) {
        return IntStream.range(0, a).mapToDouble(i -> {
            double center = patch.coordinates()[i];
            double d = bounds[i][1] - bounds[i][0];
            double r = s * d;

            double min = Math.max(bounds[i][0], center - r);
            double max = Math.min(bounds[i][1], center + r);

            return min + (max - min) * rand.nextDouble();
        }).toArray();
    }

    private static List<FlowerPatch> sortPatch(List<FlowerPatch> patch, int c) {
        return patch.stream()   //make it stream
                .sorted((patch1, patch2) -> Functions.callCompareById(c).apply(patch1.value(), patch2.value())) // sort using c
                .toList();
    }

}
