import java.util.ArrayList;

/**
 * Represents a single Thread within a Process.
 * It is responsible for processing blocks of FlowerPatches (evaluating the function).
 * Implements the logic where threads dynamically pull blocks of work until the iteration is done.
 */
public class WorkerRun implements Runnable {




    private final int b;
    private final int k;
    private final int f;
    public WorkerRun(int a ,int f, int c, int t, int n, int m, int e, int p, int q, int g, double s, int r, int b, int k, double[][] bounds) {
        this.b = b;
        this.k = k;
        this.f = f;
    }

    @Override
    public void run() {

        while (true) {

            // Threads wait here until the Main Thread (Worker.java) signals that
            // the new population (newBlocksSem) is ready for evaluation.
            synchronized (Worker.sem) {
                while (!Worker.newBlocksSem) {

                    if(Worker.finished){return;}

                    try {
                        Worker.sem.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
                if(Worker.finished){return;}
            }

            //k.A
            while (true) {
                int startIndex = -1;
                synchronized (Worker.sem) {
                    if (Worker.curIndex < Worker.blocks.size()) {
                        startIndex = Worker.curIndex;
                        Worker.curIndex += b;
                    } else {
                        break;
                    }
                }

                if (startIndex != -1) {
                    ArrayList<FlowerPatch> processedBlock = processBlock(startIndex);
                    synchronized (Worker.nextBlock) {
                        Worker.nextBlock.addAll(processedBlock);
                    }
                }
            }

            synchronized (Worker.sem) {
                Worker.workerFinishedThreadsCount++;

                if (Worker.workerFinishedThreadsCount == k) {
                    Worker.newBlocksSem = false;
                    Worker.sem.notifyAll();
                }
            }


        }
    }

    /**
     * Processes a specific block of bees starting at startIndex.
     * Evaluates the function f(x) if the value is NaN (indicating a new/moved bee).
     */
    private ArrayList<FlowerPatch> processBlock(int startIndex) {
        int endIndex = Math.min(startIndex + b, Worker.blocks.size());
        ArrayList<FlowerPatch> processedFP = new ArrayList<FlowerPatch>();

        for (int i = startIndex; i < endIndex; i++) {
            FlowerPatch fp = Worker.blocks.get(i);

            if(Double.isNaN(fp.value())) {
                double val = Functions.callFuncById(f).apply(fp.coordinates());
                processedFP.add(new FlowerPatch(val, fp.coordinates()));
            }else{
                processedFP.add(fp);
            }
        }
        return processedFP;
    }
}
