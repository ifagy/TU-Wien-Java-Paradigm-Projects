/**
 * Hier finden sie Information darüber, wer was gemacht hat:
 * 1) Efe Bideci 12333450:
 *
 *  *Aufgabe1* Test.java; Abstraction Comments ; Classes-> BeePopulation, util_rand_pi ;
 * Methoden -> FlowerGroup.autoFlowerPopGen , YearlySim.getTotalYi.
 *
 *  *Aufagbe2*
 *  # Vervollstandigung der Simulaiton durch Nahrungskonkurrenz.
 *  # Implementiert GenericPollinators als partiellen, weniger effektiven Ersatz für Wildbienen.
 *  # Notwendige Anpassungen in anderen Klassen.
 *  # neu method UtilRanPi.rNormal
 *
 *  *Aufgabe3*
 * # Implementierung des nebenläufigen/parallelen Teils (ExecutorService und Futures in "Test", Klasse "SimCallable").
 * # Zusicherungen (Design-By-Contract) für Klassen: "Pollinator", "GenericPollinator", "SimCallable" und "UtilRandPi".
 * # Verfassen von 3 "GOOD:" und 3 "BAD:" Kommentaren für den OOP Teil. +2 GOOD Procedural Kommentaren
 *
 *
 * 2) Kolozs Komlai: Aufgabe_1: Klassen: FlowerPopulation, FlowerGroup
 *                   Aufgabe_2: Modell verfeinert FlowerPopulation,
 *                   BeePopulation and Weather (stressstatus, healthstatus, temperature, Niederschlag, Klimafeuchtigkeit normal/Gaussian Verteilung)
 *                   Aufgabe_3: ParallelFunAnalyzer Klasse mit ParallelStreams und funktionale Operationen, wurde es in Test aufgerufen,
 *                              und Zusicherung und Analyse für Klasse: FlowerPopulation, ParallelFunAnalyzer
 *
 * 3) Ozan Bükelek: (Aufgabe 1)Klassen Weather, YearlySim ; FOUND the appropriate parameter settings.
 * (Aufgabe 2) :1) Verbesserung über abhängige Tageslänge vom Ort und Jahreszeit und 2) Verbesserung über Blütezeiten realer Blütenpflanzen 3)Paradigmenstellen geschrieben.
 *
 * (Aufgabe 3): Klasse FlowerGroup, YearlySim und die Schleifen in der Klasse Test mittels funktionaler Programmierung programmiert.
 * Erweiterung von YearlySim (yearlyWeatherStats, getAverageBeeHealth) und Flowergroup (getAverageYi) für statistische Informationen. Und die Kennzeichen mit STYLE:
 * Zusicherungen (Design-By-Contract) für Klassen: BeePopulation, Weather, YearlySim und FlowerGroup.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Test {
    public static void main(String[] args) throws ExecutionException, InterruptedException{
        final double[] statistics = new double[1];

        FlowerGroup flowerGroup1 = new FlowerGroup();
        FlowerGroup flowerGroup2 = new FlowerGroup();
        FlowerGroup flowerGroup3 = new FlowerGroup();

        flowerGroup1.autoFlowerPopGen();
        flowerGroup2.autoFlowerPopGen();
        flowerGroup3.autoFlowerPopGen();


        int simLengthYear = 25;
        double latitude = 200; //55

        List<FlowerGroup> flowerGroupss = new ArrayList<>();
        flowerGroupss.add(flowerGroup1);
        flowerGroupss.add(flowerGroup2);
        flowerGroupss.add(flowerGroup3);


        /**
         * STYLE: parallel/Nebenläufig: This code block(3Flower Groups/10Simulations/25jear each) runs 30
         * independent simulations in parallel.
         *
         * #Goal#
         * The goal is to achieve a performance speedup by utilizing all
         * available CPU cores to execute the 30 simulation runs concurrently,
         * rather than sequentially.
         *
         * #How it is Achieved#
         * 1. An 'ExecutorService' (thread pool) is created with a fixed number
         * of threads matching the available processor cores.
         * 2. The code loops 30 times, creating a new 'SimCallable' task for
         * each simulation run.
         * 3. Each task is submitted to the pool, and its 'Future' is
         * stored in a list.
         * 4. After all tasks are submitted, the code iterates over the 'futures'
         * list, calling 'future.get()' to retrieve results. This
         * blocks the main thread until each simulation is complete, ensuring
         * all results are collected and printed in an order to prevent interleaved and unreadable console output.
         */


        //Nebenläufig
        int coreNumber = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(coreNumber);
        List<Future<YearlySim>> futures = new ArrayList<>();

        IntStream.range(0, flowerGroupss.size()).forEach(k -> {
            System.out.println("=============================================");
            System.out.printf("     STARTING SIMULATIONS FOR GROUP %d%n", (k + 1));
            System.out.println("=============================================");



            IntStream.rangeClosed(1, 10).forEach(j -> {
                FlowerGroup currentGroup = new FlowerGroup(flowerGroupss.get(k));

                List<Pollinator> pollinators = new ArrayList<>();
                pollinators.add(new BeePopulation(10000));
                int numberOfOtherPolSpicies = 200;
                IntStream.range(0, numberOfOtherPolSpicies).forEach(l ->{
                    pollinators.add(new GenericPollinator((double) 50000 / numberOfOtherPolSpicies));
                });

                YearlySim sim = new YearlySim(UtilRandPi.rBetw(0, 1), pollinators, currentGroup, latitude);

                //Nebenläufig
                Callable<YearlySim>  simCallable = new SimCallable(sim, simLengthYear );
                Future<YearlySim> simFuture = executorService.submit(simCallable);
                futures.add(simFuture);

            });

        });
        System.out.print("All 30 Sim in pool and running parallel\n");
        for (Future<YearlySim> future : futures) {
            int runIndex = futures.indexOf(future);
            try {
                YearlySim simRes = future.get();
                if (runIndex %10==0){
                    System.out.println("=============================================");
                    System.out.printf("    SIMULATIONS RESULTS FOR GROUP %d%n", 1+(runIndex/10)%3);
                    System.out.println("=============================================");
                }

                System.out.printf("--- Group %d, Run %d Final Results ---\n",  1+(runIndex/10)%3 , (runIndex %10)+1 );
                System.out.printf("Final Bee Population after 25 years: %.2f\n", simRes.getBeePopulationSize());
                System.out.printf("Final GenericPollinators Population after 25 years: %.2f\n", simRes.getPollinatorsPopulationSize()- simRes.getBeePopulationSize());
                System.out.printf("Final Total Flower Growth (yi): %.2f\n", simRes.getTotalYi());
                double[] finalYi = simRes.getFlowerYi();


                /**
                 * BAD OOP: Objektkopplung is too strong.
                 * WHY: To get a flower's name, it calls
                 * "simRes.getFlowerGroup().getPopulations().get(v).getName()".
                 * This chain of getters means "Test.java" knows the internal
                 * structure of "YearlySim", "FlowerGroup", AND
                 * "FlowerPopulation".
                 * WHY IT HAPPENED: This was the quickest way to print results.
                 * BETTER SOLUTION: "YearlySim" or "FlowerGroup" should provide
                 * a method like "getFormattedFlowerResults()" that returns a
                 * simple String or a List of Strings. This would hide the internal
                 * structure from the "Test" class, weakening the coupling.
                 */
                IntStream.range(0, finalYi.length).forEach( v ->{
                    System.out.printf("     " + simRes.getFlowerGroup().getPopulations().get(v).getName() + ": %.2f\n", finalYi[v]);
                });
                System.out.println("---------------------------------");

            } catch (InterruptedException | ExecutionException e) {
                System.err.printf("Group %d Simulaiton %d error: %s\n", (1+runIndex/10)%3,(runIndex %10)+1, e.getMessage());
            }

        }
        //Nebenläufig
        executorService.shutdown();

        System.out.println("\n####################################################");
        System.out.println(" PARALLEL OVERALL EVALUATION OF ALL SIMULATIONS");
        System.out.println("####################################################");

        // Sammle alle Simulationsergebnisse aus den Futures
        List<YearlySim> allResults = futures.stream().map(f ->{
            try {return f.get();} catch (Exception e) {return null;}
        }).filter(Objects::nonNull).collect(Collectors.toList());

        System.out.println(ParallelFunAnalyzer.fullParallelFunReport(allResults));


        System.out.println("\n");
        System.out.println("####################################################");
        System.out.println("     ANNUAL INTERIM RESULTS");
        System.out.println("####################################################");

        // 1. Annual intermediate results for one simulation run
        System.out.println("\n--- ANNUAL INTERIM RESULTS FOR ONE RUN (Group 1, 25 Years) ---");
        FlowerGroup verificationGroup = new FlowerGroup(flowerGroupss.get(0)); // Use a copy of group 1
        List<Pollinator> pollinators = new ArrayList<>();
        pollinators.add(new BeePopulation(10000));
        int numberOfOtherPolSpicies = 30;

        for (int l = 0; l < numberOfOtherPolSpicies; l++) {
            pollinators.add(new GenericPollinator((double) 10000 / numberOfOtherPolSpicies));
        }


        YearlySim annualSim = new YearlySim(UtilRandPi.rBetw(0, 1), pollinators, verificationGroup, latitude);
        IntStream.rangeClosed(1, 25).forEach( year ->{
        //for (int year = 1; year <= 25; year++) {
            annualSim.simVegetationPhase();
            annualSim.simRestingPhase();

            String weatherStats = annualSim.yearlyWeatherStats();
            System.out.println("----------YEARLY STATS----------");
            System.out.println("End of Year " + year + " - Weather Stats: " + weatherStats);

            double averageYiForAll = flowerGroupss.stream()
                            .mapToDouble(FlowerGroup::getAverageYi)
                                    .average().orElse(0.0);
            statistics[0] = averageYiForAll;

            double averageBeeHealth = annualSim.getAverageBeeHealth();
            System.out.println("End of Year " + year + " - Average Bee Health: " + averageBeeHealth);

            System.out.println("----------YEARLY STATS----------");


            System.out.printf("End of Year %d - Bee Population: %.2f\n", year, annualSim.getBeePopulationSize());
            System.out.printf("End of Year %d - GenericPollinators Population: %.2f\n", year, annualSim.getPollinatorsPopulationSize()- annualSim.getBeePopulationSize());
            System.out.printf("End of Year %d - Flower Population (yi): %.2f\n", year,annualSim.getTotalYi());
            IntStream.range(0,verificationGroup.size()).forEach( i -> {
            //for (int i = 0; i < verificationGroup.size(); i++) {
                System.out.printf("End of Year %d - Species [%d] Final Population: %.2f ||| ", year, i,
                        verificationGroup.getPopulations().get(i).getYi());
            });
            System.out.println();
        });

        System.out.println("\n");
        System.out.println("####################################################");
        System.out.println("--- ONE YEAR DAILY INTERIM RESULTS (GROUP 1, 1 Years) ---)");
        System.out.println("####################################################");

        pollinators = new ArrayList<>();
        pollinators.add(new BeePopulation(10000));
        int numberOfOtherPolSpicies2 = 30;
        for (int l = 0; l < numberOfOtherPolSpicies2; l++) {
            pollinators.add(new GenericPollinator((double) 10000 / numberOfOtherPolSpicies));
        }
        FlowerGroup dailyGroup = new FlowerGroup(flowerGroupss.get(0));
        YearlySim dailySim = new YearlySim(UtilRandPi.rBetw(0, 1), pollinators, dailyGroup, latitude);
        IntStream.rangeClosed(1, 240).forEach( day -> {
            Weather weather = dailySim.getWeather();
            FlowerGroup flowers = dailySim.getFlowerGroup();

            dailySim.simVegetationPhaseOneDay();

            System.out.printf("\n[Year 1, Day %d]\n", day);
            System.out.printf("  Weather -> Humidity: %.4f, Sunlight Today: %.2f, Sunlight Total: %.2f\n",
                    weather.getHumidity(), weather.getSunlight(), weather.getTotalSunlight());
            System.out.printf("  Bees    -> Population: %.2f\n", dailySim.getBeePopulationSize());
            System.out.printf(" GenericPollinators  -> Population: %.2f\n", dailySim.getPollinatorsPopulationSize()- dailySim.getBeePopulationSize());
            System.out.println("  Flowers :");
            System.out.printf("  Total Flower Populations (yi) -> %.2f\n", dailySim.getTotalYi());

            IntStream.range(0, flowers.getPopulations().size()).forEach( i -> {

                FlowerPopulation fp = flowers.getPopulations().get(i);
                System.out.printf("     " + fp.getName() + " -> yi: %.2f, bi: %.4f, si: %.4f\n",
                        fp.getYi(), fp.getBi(), fp.getSi());
            });
        });
        System.out.println();
        System.out.println("Small insightful statistics of the average growth factor of all 3 groups = " + statistics[0]);

        System.out.println("\n### END OF DETAILED VERIFICATION OUTPUT ###");

    }
}
