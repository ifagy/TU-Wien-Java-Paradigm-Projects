// Test.java
import java.util.List;

/*
  Bideci Efe: ExecuteBA(logic, signals, creating child, Pipes to child), Worker, WorkerRun, FlowerPatch, Params, Functions.

  Komlai Kolozs: Test and änderungen in ExecuteBA, Worker

  Bükelek Ozan: Parallele Berechnung und Piping (Änderungen gemacht), Logging
 */

/*
    Arbeitsaufteilung und Zusammenarbeit

    Nach Ausgabe der jeweiligen Aufgabenstellung haben wir die Aufgaben gemeinsam gelesen und sich mit den Anforderungen vertraut gemacht.
    In der Regel wurde innerhalb weniger Tage besprochen, welche Teilbereich zu bearbeiten sind und welche Aufgaben von welchen Gruppenmitgliedern übernommen werden.

    Bei den ersten ein bis zwei Aufgaben erfolgte die Aufgabenzuteilung koordiniert durch ein Mitglied, um eine möglichst gleichmäßige und faire Anfangsverteilung sicherzustellen.
    Dadurch könnten alle Mitglieder vergleichbare Arbeitsanteile übernehmen.

    Nach den zweite, dritte Aufgaben mit zunehmender Erfahrung und Zusammenarbeit wurde die Aufgabenverteilung flexibler gestaltet. Ab diesem Zeitpunkt konnten sich die Mitglieder für einzelne Teilaufgaben selbst einteilen.
    Um weiterhin eine ausgewogene Arbeitsverteilung zu gewährleisten, wurde bei der Auswahl der Aufgaben darauf geachtet, dass nicht immer dieselben Personen zuerst wählen.

    Falls sich im Verlauf einzelner Aufgaben herausstellte, dass der Arbeitsaufwand nicht vollständig gleichmäßig verteilt war, wurde dies offen in der Gruppe besprochen und entsprechend angepasst.
    Konnte ein Ungleichgewicht nicht sofort ausgeglichen werden, übernahme betroffene Mitglieder bei nachfolgenden Aufgaben einen größeren Anteil der Arbeit, sodass sich die Gesamtbelastung über alle Aufgaben hinweg ausglich.

    Insgesamt war die Arbeitsverteilung über alle Aufgaben 1-8 weitgehend gleichmäßig. Änderungen der Gruppenzusammensetzung, längerfristige Ausfälle oder Unterbrechungen einzelner Mitglieder gab es nicht.
 */

public class Test {

    public static void main(String[] args) {



        runTest("Test 1: 4 Prozesse, k=1", 4, 1);

        runTest("Test 2: 1 Prozess, k=6", 1, 6);

        runTest("Test 3: 2 Prozesse, k=3", 2, 3);
    }

    private static void runTest(String title, int processes, int threads) {

        System.out.println("\n==== " + title + " ====");

        int a = 2;
        int f = 0;
        int c = 0;
        int t = 800;
        int n = 40;
        int m = 20;
        int e = 5;
        int p = 10;
        int q = 5;
        double s = 0.1;
        int r = 5;
        int b = 5;
        int k = threads;

        double[][][] w = new double[processes][a][2];
        for (int i = 0; i < processes; i++) {
            w[i][0] = new double[]{-5 + i, 0 + i};
            w[i][1] = new double[]{-5, 5};
        }

        Params params = new Params(a, f, c, t, n, m, e, p, q, s, r, b, k, w);
        ExecuteBA ba = new ExecuteBA(params);

        ba.execute().forEach(System.out::println);


    }
}