import java.util.Random;
@Author("Kolozs")
@Invariant("The simulation runs for a predefined set of days (initial phase) and continues until extinction phase is complete.\n" +
        "The bees and plants sets always contain only currently active organisms.\n" +
        "The allBees and allPlants sets strictly grow monotonically and contain every organism created.\n")

public class Simulation {

    private final Set bees = new Set();
    private final Set plants = new Set();
    private final Set allBees = new Set();
    private final Set allPlants = new Set();

    private final Random rnd = new Random();

    private static final int Days = 7;
    @Author("Kolozs")
    @PreCondition("The object @this must be valid.")
    @PostCondition("A new Simulation instance is initialized with empty sets for bees and plants.")
    public Simulation() { }

    @Author("Kolozs")
    @PreCondition("The object @this must be valid.")
    @PostCondition("A complete simulation run is executed:\n" +
            "7 days of spawning and daily activities are performed.\n" +
            "The simulation continues until either all bees or all visitable plants are inactive.\n" +
            "Statistics are printed to standard output at the end.")
    public void runOneSim() {
        int day = 1;

        while (day <= Days) {
            generateNewBeesForDay();
            generateNewPlantsForDay();
            performVisitsOfTheDay();

            bees.manageEndOfDay();
            plants.manageEndOfDay();

            day++;
        }
        while (existsActiveBee() && existsVisitablePlant()) {
            performVisitsOfTheDay();

            bees.manageEndOfDay();
            plants.manageEndOfDay();
        }

        printStatistics();
    }
    @Author("Kolozs")
    @PreCondition("The object @this must be valid.")
    @PostCondition("A random number (1-4) of new Bee instances are created and added to both bees and allBees sets.")
    private void generateNewBeesForDay() {
        int count = 1 + rnd.nextInt(4);
        int mix = rnd.nextInt(3);

        for (int i = 0; i < count; i++) {
            Bee newBee;
            if (mix == 0){
                if (rnd.nextBoolean()) newBee = new BeeU();
                else newBee = new BeeV();
            } else if (mix == 1){
                if (rnd.nextBoolean()) newBee = new BeeV();
                else newBee = new BeeW();
            } else {
                if (rnd.nextBoolean()) newBee = new BeeU();
                else newBee = new BeeW();
            }

            bees.addOrganism(newBee);
            allBees.addOrganism(newBee);

        }
    }
    @Author("Kolozs")
    @PreCondition("The object @this must be valid.")
    @PostCondition("A random number (1-4) of new Plant instances are created and added to both plants and allPlants sets.")
    private void generateNewPlantsForDay() {
        int count = 1 + rnd.nextInt(4);
        int mix = rnd.nextInt(3);

        for (int i = 0; i < count; i++) {
            Plant newPlant;
            if (mix == 0){
                if (rnd.nextBoolean()) newPlant = new PlantX();
                else newPlant = new PlantY();
            } else if (mix == 1) {
                if (rnd.nextBoolean()) newPlant = new PlantY();
                else newPlant = new PlantZ();
            } else {
                if (rnd.nextBoolean()) newPlant = new PlantX();
                else newPlant = new PlantZ();
            }

            plants.addOrganism(newPlant);
            allPlants.addOrganism(newPlant);


        }
    }
    @Author("Kolozs")
    @PreCondition("The bees set must contain valid Bee objects.")
    @PostCondition("Every active bee in the bees set has attempted to perform a random number of visits (1-4).\n" +
            "Successful visits update the state of both visiting bee and visited plant.")
    private void performVisitsOfTheDay() {

        for (int i = 0; i < bees.size(); i++) {
            Bee bee = (Bee) bees.getOrganism(i);

            if (bee == null || !bee.isActive()) continue;

            int visits = 1 + rnd.nextInt(4);

            for (int j = 0; j < visits; j++) {
                Plant p = choosePlantFor(bee);
                if (p == null) break;

                bee.visitPlant(p);
            }
        }
    }

    @Author("Kolozs")
    @PreCondition("The provided Bee instance must be valid and not null.")
    @PostCondition("Returns a compatible Plant instance (preferred or alternative) if one is available and active.\n" +
            "Returns null if no suitable active plant is found.")
    private Plant choosePlantFor(Bee bee){
        Plant pref = choosePreferredPlant(bee);
        if (pref != null) return pref;

        return chooseAlternativePlant(bee);
    }

    @Author("Kolozs")
    @PreCondition("The provided Bee instance must be valid and not null.")
    @PostCondition("Returns a random active plant of the bee's preferred species if available.\n" +
            "Returns null otherwise.")
    private Plant choosePreferredPlant(Bee bee){
        if (bee.prefersX()) return choosePlantUsingFlags(true, false, false);
        if (bee.prefersY()) return choosePlantUsingFlags(false, true, false);
        if (bee.prefersZ()) return choosePlantUsingFlags(false, false, true);
        return null;
    }

    @Author("Kolozs")
    @PreCondition("The provided Bee instance must be valid and not null.")
    @PostCondition("Returns a random active plant of the bee's alternative species if available.\n" +
            "Returns null otherwise.")
    private Plant chooseAlternativePlant(Bee bee) {
        if (bee.prefersX()) return choosePlantUsingFlags(false, true, false);
        if (bee.prefersY()) return choosePlantUsingFlags(false, false, true);
        if (bee.prefersZ()) return choosePlantUsingFlags(true, false, false);
        return null;
    }

    @Author("Kolozs")
    @PreCondition("The boolean flags (u, v, w) indicate which plant species are acceptable candidates.")
    @PostCondition("Returns a randomly selected active plant from plants set that matches one of the true flags.\n" +
            "Returns null if no matching active plant exists.")
    private Plant choosePlantUsingFlags(boolean u, boolean v, boolean w) {
        int count = 0;

        for (int i = 0; i < plants.size(); i++) {
            Plant p = (Plant) plants.getOrganism(i);
            if (p != null && p.isActive()) {
                boolean ok = (u && p.canBeVisitedByU()) ||
                        (v && p.canBeVisitedByV()) ||
                        (w && p.canBeVisitedByW());
                if (ok) count++;
            }
        }
        if (count == 0) return null;

        int target = rnd.nextInt(count);
        int seen = 0;

        for (int i = 0; i < plants.size(); i++) {
            Plant p = (Plant) plants.getOrganism(i);
            if (p != null && p.isActive()){
                boolean ok = (u && p.canBeVisitedByU()) ||
                        (v && p.canBeVisitedByV()) ||
                        (w && p.canBeVisitedByW());
                if (ok) {
                    if (seen == target) return p;
                    seen++;
                }
            }
        }
        return null;
    }
    @Author("Kolozs")
    @PreCondition("The bees set is valid.")
    @PostCondition("Returns true if at least one bee in the bees set is currently active.\n" +
            "Return false otherwise.")
    private boolean existsActiveBee() {
        for (int i = 0; i < bees.size(); i++) {
            Bee b = (Bee) bees.getOrganism(i);
            if (b != null && b.isActive()) return true;
        }
        return false;
    }

    @Author ("Kolozs")
    @PreCondition("The bees set is valid.")
    @PostCondition("Returns true if at least one active bee prefers plant species X (implies Bee typ U).")
    private boolean existsActiveBeePrefersX() {
        for (int i = 0; i < bees.size(); i++) {
            Bee b = (Bee) bees.getOrganism(i);
            if (b != null && b.isActive() && b.prefersX()) return true;
        }
        return false;
    }


    @Author ("Kolozs")
    @PreCondition("The bees set is valid.")
    @PostCondition("Returns true if at least one active bee prefers plant species Y (implies Bee typ V).")
    private boolean existsActiveBeePrefersY() {
        for (int i = 0; i < bees.size(); i++) {
            Bee b = (Bee) bees.getOrganism(i);
            if (b != null && b.isActive() && b.prefersY()) return true;
        }
        return false;
    }

    @Author ("Kolozs")
    @PreCondition("The bees set is valid.")
    @PostCondition("Returns true if at least one active bee prefers plant species Z (implies Bee typ W).")
    private boolean existsActiveBeePrefersZ() {
        for (int i = 0; i < bees.size(); i++) {
            Bee b = (Bee) bees.getOrganism(i);
            if (b != null && b.isActive() && b.prefersZ()) return true;
        }
        return false;
    }

    @Author("Kolozs")
    @PreCondition("The plants set is valid.")
    @PostCondition("Returns true if there is at least one active plant that can be visited by currently active bees (matching preference rules).\n" +
            "Returns false otherwise.")
    private boolean existsVisitablePlant() {
        for (int i = 0; i < plants.size(); i++) {
            Plant p = (Plant) plants.getOrganism(i);
            if (p == null || !p.isActive()) continue;

            boolean visitable = (p.canBeVisitedByU() && existsActiveBeePrefersX()) ||
                                (p.canBeVisitedByV() && existsActiveBeePrefersY()) ||
                                (p.canBeVisitedByW() && existsActiveBeePrefersZ());

            if (visitable) return true;
        }
        return false;
    }

    @Author("Kolozs")
    @PreCondition("The allBees and allPlants sets contain the history of all created organisms.")
    @PostCondition("Statistical data regarding total visits and averages per plant/bee are printed to standerd output.\n" +
            "The state of the simulation objects remains unchanged.")
    private void printStatistics() {
        System.out.println("----- Simulationsergebnis -----");

        int totalU = 0, totalV = 0, totalW = 0;
        int countU = 0, countV = 0, countW = 0;

        for (int i = 0; i < allBees.size(); i++) {
            Bee bee = (Bee) allBees.getOrganism(i);
            if (bee == null) continue;

            if (bee.prefersX()) {
                totalU += bee.collectedFromX() + bee.collectedFromY() + bee.collectedFromZ();
                countU++;
            } else if (bee.prefersY()) {
                totalV += bee.collectedFromX() + bee.collectedFromY() + bee.collectedFromZ();
                countV++;
            } else if (bee.prefersZ()) {
                totalW += bee.collectedFromX() + bee.collectedFromY() + bee.collectedFromZ();
                countW++;
            }
        }


        int totalX = 0, totalY = 0, totalZ = 0;
        int countX = 0, countY = 0, countZ = 0;

        for (int i = 0; i < allPlants.size(); i++) {
            Plant plant = (Plant) allPlants.getOrganism(i);
            if (plant == null) continue;

            boolean u = plant.canBeVisitedByU();
            boolean v = plant.canBeVisitedByV();
            boolean w = plant.canBeVisitedByW();

            int visits = plant.visitedByU() + plant.visitedByV() + plant.visitedByW();

            if (u && !v && w) {
                totalX += visits;
                countX++;
            }

            if (u && v && !w){
                totalY += visits;
                countY++;
            }

            if (!u && v && w){
                totalZ += visits;
                countZ++;
            }
        }

        double avgU_perPlant = (countX == 0 || countU == 0) ? 0.0 : (double) totalU / (double) countX;
        double avgV_perPlant = (countY == 0 || countV == 0) ? 0.0 : (double) totalV / (double) countY;
        double avgW_perPlant = (countZ == 0 || countW == 0) ? 0.0 : (double) totalW / (double) countZ;

        System.out.println("Bee U total= " + totalU + " avg_perPlants= " + String.format("%.2f", avgU_perPlant));
        System.out.println("Bee V total= " + totalV + " avg_perPlants= " + String.format("%.2f", avgV_perPlant));
        System.out.println("Bee W total= " + totalW + " avg_perPlants= " + String.format("%.2f", avgW_perPlant));

        double avgX_perBee = (countU == 0 || countX == 0) ? 0.0 : (double) totalX / (double) countU;
        double avgY_perBee = (countV == 0 || countY == 0) ? 0.0 : (double) totalY / (double) countV;
        double avgZ_perBee = (countW == 0 || countZ == 0) ? 0.0 : (double) totalZ / (double) countW;

        System.out.println("Plant X total= " + totalX + " avg_perBee= " + String.format("%.2f", avgX_perBee));

        System.out.println("Plant Y total= " + totalY + " avg_perBee= " + String.format("%.2f", avgY_perBee));

        System.out.println("Plant Z total= " + totalZ + " avg_perBee= " + String.format("%.2f", avgZ_perBee));


    }

}
