import java.util.*;
/**
 * Hier finden Sie Information darüber, wer was gemacht hat.
 * 1) Efe Bideci(12333450):
 * - Implementierung der Container-Klassen (ISet, OSet, MSet)
 * - Implementierung der eigenen Listenstruktur (ListGeneric)
 * - Planung und Umsetzung der Generizität (Wildcards, extends/super)
 *
 * 2) Kolozs Komlai: Implementation of Bee, HoneyBee, WildBee, and Num
 *
 * 3) Ozan Bükelek (12421916):
 * Implementation of Test
 *
 */

public class Test {

    private static void initData() {

        nums.add(new Num(0)); // n1
        nums.add(new Num(10)); // n2
        nums.add(new Num(20)); // n3
        nums.add(new Num(50)); // n4
        nums.add(new Num(60)); // n5
        nums.add(new Num(70)); // n6


        wildBees.add(new WildBee("Wildbee-1", 12));  //w1
        wildBees.add(new WildBee("Wildbee-2", 5));  //2
        wildBees.add(new WildBee("Wildbee-3", 11));  //w3
        wildBees.add(new WildBee("Wildbee-4", 10)); // w4
        wildBees.add(new WildBee("Wildbee-5", 9)); // w5
        wildBees.add(new WildBee("Wildbee-6", 8)); // w6


        honeyBees.add(new HoneyBee("Honey-1", "1specie")); //h1
        honeyBees.add(new HoneyBee("Honey-2", "MelliferaCarnica"));  //h2
        honeyBees.add(new HoneyBee("Honey-3", "Carnica"));  //h3
        honeyBees.add(new HoneyBee("Honey-4", "6specie")); //h4
        honeyBees.add(new HoneyBee("Mellifera Carnica", "Mellifera Carnica")); //h5
        honeyBees.add(new HoneyBee("Honey-6", "8specie")); //h6

    }

    private static ArrayList<Num> nums = new ArrayList<>();
    private static ArrayList<WildBee> wildBees = new ArrayList<>();
    private static ArrayList<HoneyBee> honeyBees = new ArrayList<>();


    public static void main(String[] args) {
        System.out.println("--- TEST ---");

        // Step 1: Object Creation and Initialization
        System.out.println("\n## 1. Erzeuge TestObjekten und Daten");
        initData();


        ISet<Num> num_iset = new ISet<>(null);
        OSet<Num> num_oset = new OSet<>(null);
        MSet<Num, Num> num_mset = new MSet<>(null);

        ISet<Bee> bee_iset = new ISet<>(null);
        OSet<Bee> bee_oset = new OSet<>(null);

        ISet<WildBee> wild_iset = new ISet<>(null);
        OSet<WildBee> wild_oset = new OSet<>(null);
        MSet<WildBee, Integer> wild_mset = new MSet<>(null);

        ISet<HoneyBee> honey_iset = new ISet<>(null);
        OSet<HoneyBee> honey_oset = new OSet<>(null);
        MSet<HoneyBee, String> honey_mset = new MSet<>(null);


        // --- Constraints ---



        // --- Container Creation (ISet/OSet/MSet) ---

        // ISet<Num>
        // KORREKTUR: Befüllung für check-Kompatibilität (n1->n3, n2->n4)
        num_iset.setBefore(nums.get(0), nums.get(3));
        num_iset.setBefore(nums.get(2), nums.get(4));
        num_iset.setBefore(nums.get(4), nums.get(5));
        System.out.println("- Created: ISet<Num> (Size: " + num_iset.size() + ")");

        // OSet<Num>
        // KORREKTUR: Befüllung für check-Kompatibilität (n1->n3, n2->n4)
        num_oset.setBefore(nums.get(1), nums.get(3));
        num_oset.setBefore(nums.get(2), nums.get(4));
        System.out.println("- Created: OSet<Num> (Size: " + num_oset.size() + ")");

        // MSet<Num, Num>
        num_mset.setBefore(nums.get(1), nums.get(3));
        num_mset.setBefore(nums.get(2), nums.get(4));
        System.out.println("- Created: MSet<Num, Num> (Size: " + num_mset.size() + ")");

        // ISet<Bee>
        bee_iset.setBefore(wildBees.get(1), honeyBees.get(1));
        System.out.println("- Created: ISet<Bee> (Size: " + bee_iset.size() + ")");

        // OSet<Bee>
        bee_oset.setBefore(wildBees.get(2), honeyBees.get(2));
        System.out.println("- Created: OSet<Bee> (Size: " + bee_oset.size() + ")");

        // ISet<WildBee>
        // Behält die Kante bei (wb1->wb2), akzeptiert Fehler in Test 3d.
        try {
            wild_iset.setBefore(wildBees.get(1), wildBees.get(2));
        } catch (IllegalArgumentException e) {
        }
        System.out.println("- Created: ISet<WildBee> (Size: " + wild_iset.size() + ")");

        // OSet<WildBee>
        wild_oset.setBefore(wildBees.get(2), wildBees.get(1));
        System.out.println("- Created: OSet<WildBee> (Size: " + wild_oset.size() + ")");

        // ISet<HoneyBee>
        honey_iset.setBefore(honeyBees.get(1), honeyBees.get(2));
        System.out.println("- Created: ISet<HoneyBee> (Size: " + honey_iset.size() + ")");

        // OSet<HoneyBee>
        honey_oset.setBefore(honeyBees.get(2), honeyBees.get(1));
        System.out.println("- Created: OSet<HoneyBee> (Size: " + honey_oset.size() + ")");

        // MSet<WildBee, Integer>
        wild_mset.setBefore(wildBees.get(1), wildBees.get(2));
        System.out.println("- Created: MSet<WildBee, Integer> (Size: " + wild_mset.size() + ")");

        // MSet<HoneyBee, String>

        honey_mset.setBefore(honeyBees.get(1), honeyBees.get(2));
        System.out.println("- Created: MSet<HoneyBee, String> (Size: " + honey_mset.size() + ")");

        // --- Aliases for Step 2 ---
        ISet<Bee> a1 = bee_iset;
        OSet<Bee> a2 = bee_oset;
        MSet<WildBee, Integer> b1 = wild_mset;
        MSet<HoneyBee, String> b2 = honey_mset;
        OSet<WildBee> c1 = wild_oset;
        ISet<HoneyBee> c2 = honey_iset;

        // Create new objects to be added to the source containers
        WildBee newWb = new WildBee("NewWorker", 6);
        HoneyBee newHb = new HoneyBee("NewApis", "Mellifica");

        // Add new objects to source containers
        try {
            c1.setBefore(wildBees.get(5), wildBees.get(2));
            c2.setBefore(honeyBees.get(5), honeyBees.get(1));
        } catch (IllegalArgumentException e) {
            System.err.println("Error adding to c1/c2: " + e.getMessage());
        }
        System.out.println("\n(Additional elements added to c1/c2: c1 Size: " + c1.size() + ", c2 Size: " + c2.size() + ")");


        // Step 2: Reading, Checking, and Copying Order Relations
        System.out.println("\n## 2. Copying Order Relations");

        // --- Part 1: c1 (OSet<WildBee>) to a1 (ISet<Bee>) and b1 (MSet<WildBee, Integer>) ---
        System.out.println("\n-- c1 (OSet<WildBee>) -> a1 (ISet<Bee>) & b1 (MSet<WildBee, Integer>) --");

        Iterator<WildBee> iterC1 = c1.iterator();
        ListGeneric<WildBee> c1Elements = new ListGeneric<>();

        while (iterC1.hasNext()) {
            WildBee currentWb = iterC1.next();
            c1Elements.add(currentWb);
            // Call length()
            System.out.println("  Entry in c1 (WildBee): " + currentWb.toString() + ", Length: " + currentWb.length() + "mm");
        }

        // Find and copy relations
        Iterator<WildBee> iterX = c1Elements.iterator();
        while (iterX.hasNext()) {
            WildBee x = iterX.next();
            Iterator<WildBee> iterY = c1Elements.iterator();
            while (iterY.hasNext()) {
                WildBee y = iterY.next();

                // Get before result (Object for OSet)
                Object c1BeforeResult = c1.before(x, y);

                if (c1BeforeResult != null) {
                    System.out.println("  Relation in c1 found: " + x.toString() + " -> " + y.toString());

                    // Copy to a1 (ISet<Bee>)
                    try {
                        a1.setBefore(x, y);
                        System.out.println("    -> Copied to a1 (ISet<Bee>)");
                    } catch (IllegalArgumentException e) {
                        System.out.println("    -> Failed to copy to a1 (Constraint violation)");
                    }

                    // Copy to b1 (MSet<WildBee, Integer>)
                    try {
                        b1.setBefore(x, y);
                        System.out.println("    -> Copied to b1 (MSet<WildBee, Integer>)");
                    } catch (IllegalArgumentException e) {
                        System.out.println("    -> Failed to copy to b1 (Constraint violation)");
                    }
                }
            }
        }

        // --- Part 2: c2 (ISet<HoneyBee>) to a2 (OSet<Bee>) and b2 (MSet<HoneyBee, String>) ---
        System.out.println("\n-- c2 (ISet<HoneyBee>) -> a2 (OSet<Bee>) & b2 (MSet<HoneyBee, String>) --");

        Iterator<HoneyBee> iterC2 = c2.iterator();
        ListGeneric<HoneyBee> c2Elements = new ListGeneric<>();

        while (iterC2.hasNext()) {
            HoneyBee currentHb = iterC2.next();
            c2Elements.add(currentHb);
            // Call sort()
            System.out.println("  Entry in c2 (HoneyBee): " + currentHb.toString() + ", Sort: " + currentHb.sort());
        }

        // Find and copy relations
        Iterator<HoneyBee> iterX2 = c2Elements.iterator();
        while (iterX2.hasNext()) {
            HoneyBee x = iterX2.next();
            Iterator<HoneyBee> iterY2 = c2Elements.iterator();
            while (iterY2.hasNext()) {
                HoneyBee y = iterY2.next();

                // Get before result (Iterator<HoneyBee> for ISet)
                Object c2BeforeResult = c2.before(x, y);

                if (c2BeforeResult != null) {
                    System.out.println("  Relation in c2 found: " + x.toString() + " -> " + y.toString());

                    // Copy to a2 (OSet<Bee>)
                    try {
                        a2.setBefore(x, y);
                        System.out.println("    -> Copied to a2 (OSet<Bee>)");
                    } catch (IllegalArgumentException e) {
                        System.out.println("    -> Failed to copy to a2 (Constraint violation)");
                    }

                    // Copy to b2 (MSet<HoneyBee, String>)
                    try {
                        b2.setBefore(x, y);
                        System.out.println("    -> Copied to b2 (MSet<HoneyBee, String>)");
                    } catch (IllegalArgumentException e) {
                        System.out.println("    -> Failed to copy to b2 (Constraint violation)");
                    }
                }
            }
        }
        // WICHTIG: Die Größe sollte hier 3 sein, wenn Korrektur 1 in ISet/OSet angewendet wurde
        System.out.println("  Result: a1 Size: " + a1.size() + ", b1 Size: " + b1.size() + ", a2 Size: " + a2.size() + ", b2 Size: " + b2.size());


        // Step 3: check and checkForced with different argument types
        System.out.println("\n## 3. check/checkForced with Varying Arguments");

        System.out.println("\n-- Test OSet<Num>.check(C c) --");

        // 3.a) OSet<Num>.check(ISet<Num>)
        try {
            num_oset.check(num_iset);
            System.out.println("  1. OSet<Num>.check(ISet<Num>): OK (Num -> Num)");
        } catch (IllegalArgumentException e) {
            System.out.println("  1. OSet<Num>.check(ISet<Num>): ERROR (Constraint violated)");
        }

        // 3.b) OSet<Num>.check(MSet<Num, Num>)
        try {
            num_oset.check(num_mset);
            System.out.println("  2. OSet<Num>.check(MSet<Num, Num>): OK (Num -> Num)");
        } catch (IllegalArgumentException e) {
            System.out.println("  2. OSet<Num>.check(MSet<Num, Num>): ERROR (Constraint violated)");
        }

        // 3.c) OSet<Num>.check(OSet<Bee>) - Compiler must prevent this.

        System.out.println("\n-- Test Subtype Checks (WildBee/HoneyBee vs Bee) --");

        // 3.d) ISet<WildBee>.check(ISet<Bee>) - Erwartet FEHLER aufgrund gespeicherter Kante wb1->wb2
        try {
            wild_iset.check(bee_iset);
            System.out.println("  4. ISet<WildBee>.check(ISet<Bee>): OK (Bee is super WildBee)");
        } catch (IllegalArgumentException e) {
            System.out.println("  4. ISet<WildBee>.check(ISet<Bee>): ERROR (Constraint violated)");
        }

        // 3.e) OSet<HoneyBee>.checkForced(OSet<Bee>)
        try {
            honey_oset.checkForced(bee_iset);
            System.out.println("  5. OSet<HoneyBee>.checkForced(OSet<Bee>): OK (Bee is super HoneyBee)");
        } catch (IllegalArgumentException e) {
            System.out.println("  5. OSet<HoneyBee>.checkForced(OSet<Bee>): ERROR (Unexpected exception)");
        }

        // 3.f) OSet<HoneyBee>.check(MSet<HoneyBee, String>)
        MSet<HoneyBee, String> mSetHoneyBeeStringForConstraint = honey_mset;
        try {
            honey_oset.check(mSetHoneyBeeStringForConstraint);
            System.out.println("  6. OSet<HoneyBee>.check(MSet<HoneyBee, String>): OK (Exact type match)");
        } catch (IllegalArgumentException e) {
            System.out.println("  6. OSet<HoneyBee>.check(MSet<HoneyBee, String>): ERROR (Constraint violated)");
        }

        System.out.println("\n-- Test Reverse Check (Compiler must prevent) --");
        // 3.g) ISet<Bee>.check(ISet<WildBee>) - Compiler MUST fail here!

        // Step 4: Subtype Relationships between ISet, OSet, and MSet
        System.out.println("\n## 4. Subtype Relationships (ISet, OSet, MSet)");

        System.out.println("\n-- Justification for Missing Subtype Relationships --");

        System.out.println("  - ISet<E> and OSet<E> are not subtypes of each other.");
        System.out.println("    They implement OrdSet<E, R> with conflicting R types: ISet<E> (R = Iterator<E>) and OSet<E> (R = Object).");

        System.out.println("  - OSet<E> and MSet<E, X> are not subtypes of each other.");
        System.out.println("    MSet has a stricter bound on E (E extends Modifiable<X, E>) than OSet (E is unbounded).");

        // Step 5: Verify Full Functionality (including plus/minus)
        System.out.println("\n## 5. Full Functionality Verification");

        // Test Modifiable (add/subtract)
        System.out.println("\n-- Test Modifiable Functionality --");
        Num n5 = nums.get(1).add(nums.get(2));
        System.out.println("  Num.add(10 + 20): " + n5.toString() + " (Expected 30)");
        WildBee wb3 = wildBees.get(1).add(10);
        System.out.println("  WildBee.add(5mm + 10): " + wb3.length() + "mm (Expected 15)");
        HoneyBee hb3 = honeyBees.get(4).subtract("Mellifera");
        System.out.println("  HoneyBee.subtract(Mellifera): " + hb3.sort() + " (Expected Carnica)");

        // Test MSet (plus/minus)
        System.out.println("\n-- Test MSet (plus/minus) --");
        System.out.println("  b1 before plus(1): wb1 length: " + wildBees.get(1).length() + ", wb2 length: " + wildBees.get(2).length());

        b1.plus(1);
        System.out.println("  b1 after plus(1): New Size " + b1.size());

        System.out.println("\n  b2 before minus(\"l\"): hb1 sort: " + honeyBees.get(1).sort() + ", hb2 sort: " + honeyBees.get(2).sort());
        b2.minus("l");
        System.out.println("  b2 Size after minus(\"l\"): " + b2.size());

        // Test OSet.before().add()/subtract()
        System.out.println("\n-- Test OSet/MSet Sub-Container Modifiability --");

        Object oSetObjektC1 = c1.before(wildBees.get(2), wildBees.get(1));
        if (oSetObjektC1 != null) {
            System.out.println("  c1.before(wb2, wb1) returned an Object.");
            System.out.println("  Further testing of internal add()/subtract() is prevented by the ban on casts.");
        }


        // Step 6: Optional Checks
        System.out.println("\n## 6. Optional Checks (Cycle Prevention)");

        // Try to create a cycle n5 -> n2 -> n4 -> n5 in iSetNum
        try {
            num_iset.setBefore(nums.get(5), nums.get(2));
            System.out.println("  ISet<Num>.setBefore(n4, n1): ERROR (Cycle not prevented)");
        } catch (IllegalArgumentException e) {
            System.out.println("  ISet<Num>.setBefore(n4, n1): OK (Cycle prevented: " + e.getMessage() + ")");
        }

    }
}