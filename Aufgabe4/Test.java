import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Iterator;

public class Test {
    /**
     * Hier finden Sie Information darüber, wer was gemacht hat.
     * 1) Efe Bideci(12333450):
     * - Entwurf und Implementierung der Grundstruktur.:
     * - Festlegung der gesamten Vererbungshierarchie (Interfaces und 'extends'-Beziehungen).
     * - Zusicherungen von Interfaces.
     * - Implementierung von basis Methoden earlier, later und communal, social.
     *
     * 2) Kolozs Komlai: Zusicherung für Klassen: OsmiaCornuta, LasioglossumCalceatum und Observation
     * Implementierung von Test
     *
     * 3) Ozan Bükelek (12421916): Kommentare über keine Untertypbeziehungen, hat Efe Bideci bei der Aufbau der Grundstruktur geholfen.
     * Zusicherungen für Klassen: HoneyBee, AndrenaBucephala FlowerFly.
     * Implementierung von Methoden sameBee, wild und solitary.
     *
     */

    // Begründungen für keine Untertypbeziehungen finden Sie hier als Kommentar:

    /**
     *
     * 1) FlowerFly und Wasp:
     * "Schwebfliegen gehören zu den Fliegen und sind daher nicht mit Bienen und Wespen verwandt." stand auf dem Blatt.
     * Da diese 2 biologisch nicht verwandt sind, kann einer von denen das Verhalten vom anderen nicht übernehmen, also keine Untertypbeziehung.
     *
     *  2) HoneyBee und SolitaryBee:
     *  "Solitär oder kommunal lebende Honigbienen gibt es nicht." stand auf dem Blatt. Da SolitaryBee keinen Staat bilde aber HoneyBee schon, gibt es einen
     *  Verhaltensunterschied. Deswegen kann man kein Objekt vom HoneyBee verwenden, wo ein Objekt von einem SolitaryBee erwartet wird. Keine Untertypbeziehung.
     *  Keine Untertypbeziehung zwischen SolitaryBee und HoneyBee bedeutet auch keine Untertypbeziehung zwischen HoneyBee und WildBee.
     *
     *  3) HoneyBee und CommunalBee:
     * "Solitär oder kommunal lebende Honigbienen gibt es nicht." stand auf dem Blatt. Also ein Objekt vom
     *  HoneyBee kann nicht verwendet werden, wo ein Objekt vom CommunalBee erwartet wird. Keine Untertypbezieung.
     *
     * 4) BumbleBee und SolitaryBee:
     *  Solitäre Biene bilden keinen Staat, wobei BumbleBee staatbildend sind. Wegen dieses Verhaltensunterschieds
     *  kann kein Objekt vom BumbleBee verwendet werden, wo ein Objekt vom SolitaryBee erwartet wird. Keine Untertypbeziehung.
     *
     *
     * 5) BumbleBee und CommunalBee:
     *  CommunalBee leben zusammen, versorgen aber nur ihren eigenen Nachwuchs. Das ist kein Verhalten eines Staats, und deswegen
     *  kann kein Objekt vom BumbleBee (die staatbildend sind) verwendet werden, wo ein Objekt vom CommunalBee erwartet wird. Keine Untertypbeziehung.
     *
     * 6) LasioglossumCalceatum und CommunalBee:
     * LasioglossumCalceatum bilden ihr eigenes selbstgrabenes Nest und teilen es nicht mit anderen Bienen. Das ist kein Verhalten von CommunalBee.
     * Kein Objekt von LasioglossumCalceatum ist zu verwenden, wo ein Objekt vom CommunalBee erwartet wird. Keine Untertypbeziehung.
     *
     * 7) OsmiaCornuta und CommunalBee:
     * Auf dem Blatt steht, dass jedes Weibchen ihre eigene Nishthöhle sucht. Das ist kein Verhalten vom CommunalBee, wo Nestern geteilt werden.
     * Also kein Objekt von OsmiaCornuta ist zu verwenden, wo ein Objekt vom CommunalBee erwartet wird. Keine Untertypbeziehung.
     *
     * 8) OsmiaCornuta und SocialBee:
     * OsmiaCornuta bildet solitär, das heißt, sie bilden keinen Staat. Da SocialBee Staat bilden, gibt es einen Verhaltensunterschied.
     * Also kein Objekt von OsmiaCornuta ist zu verwenden, wo ein Objekt vom SocialBee erwartet wird. Keine Untertypbeziehung.
     *
     * 9) AndrenaBucephala und SocialBee:
     * AndrenaBucephala lebt communal, ab und zu auch solitär. Da diese beiden Typen nie Staat bilden, gibt es wieder einen Verhaltensunterschied.
     * Also kein Objekt von AndrenaBucephala ist zu verwenden, wo ein Objekt vom SocialBee erwartet wird. Keine Untertypbeziehung.
     *
     * 10) SocialBee und CommunalBee:
     * SocialBee bilden Staaten und leben arbeitsteilig. CommunalBee teilen auch Nester, versorgen aber nur ihren eigenen Nachwuchs, leben also nicht arbeitsteilig.
     * Es gibt wieder einen Verhaltensunterschied. Also kein Objekt von SocialBee ist zu verwenden, wo ein Objekt vom CommunalBee erwartet wird,
     * und umgekehrt kann kein CommunalBee Objekt verwendet werden, wo ein SocialBee erwartet wird. Keine Untertypbeziehung.
     *
     * 11) SocialBee und SolitaryBee:
     * SocialBee bilden Staat wobei SolitaryBee nicht. Aufgrund dessen gibt es einen Verhaltensunterschied.
     * Also kein Objekt von SocialBee ist zu verwenden, wo ein Objekt vom SolitaryBee erwartet wird,
     * und umgekehrt kann kein SolitaryBee Objekt verwendet werden, wo ein SocialBee erwartet wird. Keine Untertypbeziehung.

     */

    /**
     * class invariant: observations != null
     */
    public static List<Observation> observations = new ArrayList<Observation>();
    public static void main(String[] args) {
        // Generate observations over several days for the same and different individuals
        LocalDateTime now = LocalDateTime.now();
        HoneyBee hb1 = new HoneyBee(now.minusDays(5), "hb1 first", 100);
        HoneyBee hb2 = new HoneyBee(now.minusDays(3), "hb1 second", 100);
        HoneyBee hb3 = new HoneyBee(now.minusDays(1), "hb1 third", 100);

        OsmiaCornuta oc1 = new OsmiaCornuta(now.minusDays(7), "oc1 first", 200, true);
        OsmiaCornuta oc2 = new OsmiaCornuta(now.minusDays(2), "oc1 second", 200, false);

        LasioglossumCalceatum lc_s1 = new LasioglossumCalceatum(now.minusDays(4), "lc social obs", 300, true, true);
        LasioglossumCalceatum lc_sol1 = new LasioglossumCalceatum(now.minusDays(3), "lc solitary obs", 300, false, true);
        LasioglossumCalceatum lc_s2 = new LasioglossumCalceatum(now.minusHours(10), "lc social recent", 300, true, false);

        // --- Test 1: Social iterator for HoneyBee should return all valid observations with same HBId ---
        System.out.println("Test_1: HoneyBee.social() returns all social observations for same individual");
        Iterator<SocialBee> itHb = hb1.social();
        int countHbSocial = 0;
        while (itHb.hasNext()) {
            itHb.next();
            countHbSocial++;
        }
        printResult("HoneyBee social count == 3", countHbSocial == 3);

        // --- Test 2: sameBee ordering (ascending) ---
        System.out.println("Test_2: HoneyBee.sameBee() ascending order by date");
        Iterator<Bee> sameAsc = hb1.sameBee();
        LocalDateTime prev = null;
        boolean ascending = true;
        int idx = 0;
        while (sameAsc.hasNext()) {
            Bee b = sameAsc.next();
            LocalDateTime d = ((Observation)b).getDate();
            if (idx > 0 && prev != null && d.isBefore(prev)) ascending = false;
            prev = d;
            idx++;
        }
        printResult("HoneyBee sameBee ascending order and size 3", ascending && idx == 3);

        // --- Test 3: sameBee within time window (descending) ---
        System.out.println("Test_3: Lasioglossum.sameBee with window and descending order");
        LocalDateTime start = now.minusDays(5);
        Iterator<Bee> lcWindow = lc_s1.sameBee(true, start, now);
        prev = null;
        boolean descending = true;
        idx = 0;
        while (lcWindow.hasNext()) {
            Bee b = lcWindow.next();
            LocalDateTime d = ((Observation)b).getDate();
            if (idx > 0 && prev != null && d.isAfter(prev)) descending = false;
            prev = d;
            idx++;
        }
        printResult("Lasioglossum sameBee window size 3 and descending", descending && idx == 3);

        // --- Test 4: Lasioglossum social() vs solitary() filter based on isSocial flag ---
        System.out.println("Test_4: Lasioglossum social/solitary filtering");
        int socialCount = 0;
        Iterator<SocialBee> itLcSocial = lc_s1.social();
        while (itLcSocial.hasNext()) {
            itLcSocial.next();
            socialCount++;
        }
        int solitaryCount = 0;
        Iterator<SolitaryBee> itLcSolitary = lc_s1.solitary();
        while (itLcSolitary.hasNext()) {
            itLcSolitary.next();
            solitaryCount++;
        }
        printResult("Lasioglossum social == 2 and solitary == 1", socialCount == 2 && solitaryCount == 1);

        // --- Test 5: Wild filter on OsmiaCornuta ---
        System.out.println("Test_5: OsmiaCornuta.wild(true) vs wild(false)");
        int wildTrue = 0;
        Iterator<WildBee> itWildTrue = oc1.wild(true);
        while (itWildTrue.hasNext()) {
            itWildTrue.next();
            wildTrue++;
        }
        int wildFalse = 0;
        Iterator<WildBee> itWildFalse = oc1.wild(false);
        while (itWildFalse.hasNext()) {
            itWildFalse.next();
            wildFalse++;
        }
        printResult("OsmiaCornuta wild(true) == 1 and wild(false) == 1", wildTrue == 1 && wildFalse == 1);

        // --- Test 6: Observation.remove() affects valid() and iterators ---
        System.out.println("Test_6: remove() makes observation invalid and excluded from iterators");
        hb2.remove();
        itHb = hb1.social();
        countHbSocial = 0;
        while (itHb.hasNext()) {
            itHb.next();
            countHbSocial++;
        }
        printResult("After remove: HoneyBee social count == 2", countHbSocial == 2);

        // --- Test 7: Observation.later()/earlier() ---
        System.out.println("Test_7: Observation.later() and earlier() correctness");
        Iterator<Observation> laterFromOc1 = oc1.later();
        int laterCount = 0;
        LocalDateTime lastSeen = null;
        boolean laterSorted = true;
        while (laterFromOc1.hasNext()) {
            Observation o = laterFromOc1.next();
            if (lastSeen != null && o.getDate().isBefore(lastSeen)) {
                laterSorted = false;
                break;
            }
            lastSeen = o.getDate();
            laterCount++;
        }
        printResult("Observation.later() sorted ascending and non-empty", laterSorted && laterCount > 0);

        // --- Test 8: Polymorphie und Ersetzbarkeit ---
        System.out.println("Test_8: Polymorphie, Verwendung von Obertypen");
        Bee bee1 = new HoneyBee(now.minusDays(1), "Polymorphie Test", 400);
        Bee bee2 = new OsmiaCornuta(now.minusDays(1), "Polymorphie Test", 400, true);

        Iterator<Bee> iter1 = bee1.sameBee();
        Iterator<Bee> iter2 = bee2.sameBee();
        printResult("Polymorphie: sameBee() für verschiedene Bee-Typen", iter1 != null && iter2 != null);

        // --- Test 9: Leerer Iterator für nicht existierende IDs ---
        System.out.println("Test_9: Leere Iteratoren für nicht existierende IDs");
        Bee dumyBee = new HoneyBee(now, "Test Bee", 10000);
        Iterator<Bee> emptyIt = dumyBee.sameBee();
        boolean foundNonExistent = false;
        while (emptyIt.hasNext()) {
            Bee b = emptyIt.next();
            if (((Observation)b).getComment().contains("not existing")) {
                foundNonExistent = true;
            }
        }
        printResult("Leerer Iterator für nicht existierende ID", !foundNonExistent);

        // --- Test 10: Alle Typen einmal erzeugen (Vollständigkeit) ---
        System.out.println("Test_10: Verwendung aller Typen zur Ersetzbarkeit");
        Observation ob1 = new FlowerFly(now.minusDays(8), "Generic Observation", 1010);

        HoneyBee pollinator1 = new HoneyBee(now.minusDays(1), "Pollinator HoneyBee", 1100);
        FlowerFly pollinator2 = new FlowerFly(now.minusDays(2), "Pollinator FlowerFly", 1101);

        HoneyBee wasp1 = new HoneyBee(now.minusDays(1), "Wasp HoneyBee", 1200);
        OsmiaCornuta wasp2 = new OsmiaCornuta(now.minusDays(1), "Wasp Osmia", 1201, true);
        LasioglossumCalceatum wasp3 = new LasioglossumCalceatum(now.minusDays(1), "Wasp Lasioglossum", 1202, true, true);

        WildBee wild1 = new OsmiaCornuta(now.minusDays(1), "WildBee Osmia", 1200, true);
        WildBee wild2 = new LasioglossumCalceatum(now.minusDays(1), "WildBee Osmia", 1301, false, true);

        SocialBee social1 = new HoneyBee(now.minusDays(1), "SOcialBee HoneyBee", 1400);
        SocialBee social2 = new BumbleBee(now.minusDays(4), "BumbleBee im Staat", 444, false);

        SolitaryBee solitary1 = new OsmiaCornuta(now.minusDays(1), "SolitaryBee Osmia", 1500, true);
        SolitaryBee solitary2 = new LasioglossumCalceatum(now.minusDays(1), "SolitaryBee Lasioglossum", 1501, false, true);

        CommunalBee communal = new AndrenaBucephala(now.minusDays(2), "Andrena communal", 666, true, false);

        boolean allMethodsWork = true;

        allMethodsWork &= (ob1.later() != null);
        allMethodsWork &= (ob1.earlier() != null);

        allMethodsWork &= (wild1.wild(true) != null);
        allMethodsWork &= (wild2.wild(false) != null);

        allMethodsWork &= (social1.social() != null);
        allMethodsWork &= (social2.social() != null);

        allMethodsWork &= (solitary1.solitary() != null);
        allMethodsWork &= (solitary2.solitary() != null);

        allMethodsWork &= (communal.communal() != null);

        allMethodsWork &= (social1.sameBee() != null);

        allMethodsWork &= (solitary1.sameBee() != null);

        allMethodsWork &= ob1.valid();
        allMethodsWork &= (ob1.getDate() != null);
        allMethodsWork &= (ob1.getComment() != null);

        allMethodsWork &= pollinator1.sameBee() != null;
        allMethodsWork &= pollinator2.later() != null;

        allMethodsWork &= wasp1.sameBee() != null;
        allMethodsWork &= wasp2.solitary() != null;
        allMethodsWork &= wasp3.social() != null;


        printResult("Alle Typen und Interfaces laufen korrekt", allMethodsWork);

        // --- Test 11: FlowerFly als Pollinator ---
        System.out.println("Test_11: Flowerfly als Pollinator");
        FlowerFly ff1 = new FlowerFly(now.minusDays(3), "Flowerfly Test", 1600);
        boolean observationMethodsWork = ff1.valid() && ff1.getDate() != null && ff1.getComment() != null;

        Iterator<Observation> laterIter = ff1.later();
        Iterator<Observation> earlierIter = ff1.earlier();
        boolean temporalMethodsWork = (laterIter != null && earlierIter != null);

        boolean canStoreInList = false;
        for (Observation obs : Test.observations) {
            if (obs.equals(ff1)) {
                canStoreInList = true;
                break;
            }
        }

        printResult("FlowerFly kann als Pollinator und Observation verwendet werden",
                observationMethodsWork && temporalMethodsWork && canStoreInList);

        // --- Test 12: later() und earlier() bei BumbleBee (mit September-Sterben) ---
        System.out.println("Test_12: BumbleBee Jahresverlauf Simulation (September-Sterben)");
        BumbleBee bb1 = new BumbleBee(LocalDateTime.of(2025, 3, 15, 10, 0), "Frühjahr Königin", 800, true);
        BumbleBee bb2 = new BumbleBee(LocalDateTime.of(2025, 9, 15, 12, 0), "September Arbeiterinnen", 800, false);
        BumbleBee bb3 = new BumbleBee(LocalDateTime.of(2025, 10, 10, 9, 0), "Überwinternde Jungkönigin", 800,true);

        Iterator<Observation> laterBb = bb1.later();
        int bbCount = 0;
        while (laterBb.hasNext()) {
            laterBb.next();
            bbCount++;
        }
        printResult("BumbleBee später Beobachtungen > 0 (Lebenszyklus sichtbar)", bbCount > 0);

    }

    private static void printResult(String msg, boolean ok) {
        System.out.println((ok ? "SUCCESS " : "Fail: ") + msg);
    }
}
