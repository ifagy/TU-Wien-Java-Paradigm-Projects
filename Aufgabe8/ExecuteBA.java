import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.*;

/**
 * Orchestrator class for the Parallel Bees Algorithm.
 * Responsible for starting child processes, distributing search space (w),
 * and collecting results via Pipelines.
 */
public class ExecuteBA {


    private final int a; // Parameter dimension
    private final int f; // function id
    //private final double[] minCordValues; // Lower bounds    in w
    //private final double[] maxCordValues; // Upper bounds    in w
    private final int c; // Comparator logic id ( -1: left is better)
    private final int t; // Max iterations
    private final int n; // Scout bees
    private final int m; // Selected patches
    private final int e; // Elite patches
    private final int p; // Elite recruits
    private final int q; // Non-elite recruits
    private final int g; // Global search bees (n-m)
    private final double s; // Neighborhood size ratio
    private final int r; // Result count
    private final int b; //b Anzahl der Bienen in einem Block    >0
    private final int k; // Anzahl der Threads in einem Prozess  >0
    private final double[][][] w; // [NumOfProcess][NumOfVar]{Min,Max}


    public ExecuteBA(Params params) {
        this.a = params.a(); // 1
        this.f = params.f(); // 2
        this.c = params.cCompare(); //3
        this.t = params.t(); //4
        this.n = params.n(); //5
        this.m = params.m(); //6
        this.e = params.e(); //7
        this.p = params.p(); //8
        this.q = params.q(); //9
        this.g = n - m;      //10
        this.s = params.s(); //11
        this.r = params.r(); //12

        this.b = params.b(); //13
        this.k = params.k(); //14
        this.w = params.w();

        if (n % b != 0 || m % b != 0 || e % b != 0 || p % b != 0 || q % b != 0) {
            throw new IllegalArgumentException("n, m, e, p, q muss vielfaches von \"b\" sein!");
        }

    }

    /**
     * Executes the algorithm by spawning processes.
     * @return List of best FlowerPatches found across all processes.
     */
    public List<FlowerPatch> execute() {

        List<Process> processes = new ArrayList<>();
        List<BufferedReader> readers = new ArrayList<>();
        //create Processes and pipes
        try {
            String classpath = System.getProperty("java.class.path"); //for the next process
            for (int i = 0; i < w.length; i++) {

                //crate childProcess with arguments except w, w wird mit pipe weitergeleitet.
                ProcessBuilder pb = new ProcessBuilder(
                        "java",
                        "-cp",
                        classpath,
                        "Worker",
                        String.valueOf(i),
                        String.valueOf(a),
                        String.valueOf(f),
                        String.valueOf(c),
                        String.valueOf(t),
                        String.valueOf(n),
                        String.valueOf(m),
                        String.valueOf(e),
                        String.valueOf(p),
                        String.valueOf(q),
                        String.valueOf(g),
                        String.valueOf(s),
                        String.valueOf(r),
                        String.valueOf(b),
                        String.valueOf(k)
                );
                Process p = pb.start();
                processes.add(p);

                /* #CHANGED TO AVOID -cp problems
                StringBuilder sb = new StringBuilder();

                sb.append("java -cp \"" + classpath + "\" Worker " + i + " " + a + " " + f + " " + c + " " + t + " " + n + " " + m + " " + e +
                        " " + p + " " + q + " " + g + " " + s + " " + r + " " + b + " " + k); //with the path now
                Process p = Runtime.getRuntime().exec(sb.toString());
                processes.add(p);
                 */

                //sending boundaries with pipe to process
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()))) {
                    double[][] bounds = w[i];
                   //writer.write(bounds.length + "\n");
                    for (double[] bo : bounds) {
                        writer.write(bo[0] + " " + bo[1] + "\n");
                    }

                    writer.flush();
                }

                //create reader
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                readers.add(reader);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        //collect results
        //print results

        List<FlowerPatch> allResults = new ArrayList<>();

        for (BufferedReader reader : readers) {
            try {
                String line;
                while ((line = reader.readLine()) != null) {

                    if (!line.startsWith("RESULT:")) continue;

                    String payload = line.substring(7); // Alles nach "RESULT:"
                    String[] parts = payload.split(":", 2);
                    if (parts.length < 2) continue;

                    double val = Double.parseDouble(parts[0].trim());
                    String coordPart = parts[1].replace("[", "").replace("]", "");
                    double[] coords = Arrays.stream(coordPart.split(","))
                            .map(String::trim)
                            .mapToDouble(Double::parseDouble)
                            .toArray();

                    allResults.add(new FlowerPatch(val, coords));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        for (Process process : processes) {
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        allResults.sort((p1, p2) -> Functions.callCompareById(c).apply(p1.value(), p2.value()));
        if (allResults.isEmpty()) return new ArrayList<>();
        return allResults.stream().limit(r).toList();
    }
}

