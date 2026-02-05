import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import java.util.ArrayList; //Kontext B
import java.util.HashMap; //Kontext B
import java.util.List; //Kontext B
import java.util.Map; //Kontext B

/**
 * Hier finden Sie Information darüber, wer was gemacht hat.
 * 1) Efe Bideci(12333450):
 * - Kontext B. <3
 *
 * 2) Kolozs Komlai (12341330): Simulation (Implementation/Zusicherungen) und Test (Kontext A)
 *
 * 3) Ozan Bükelek (12421916):
 * - Alles von Kontext A ausser Simulation und Test
 */



@ClassesToTest({Test.class, Simulation.class, Organism.class, Bee.class, BeeU.class,
        BeeV.class, BeeW.class, Plant.class, PlantX.class, PlantY.class, PlantZ.class, Set.class,
        ClassesToTest.class, Author.class, Invariant.class, HistoryConstraint.class, PreCondition.class, PostCondition.class})
public class Test {
    public static void main(String[] args) {

        runSimulation(1);
        runSimulation(2);
        runSimulation(3);
        runSimulation(4);

        extractData();


    }


    @Author("Kolozs")
    @PreCondition("The provided simulation number 'nr' is a positive integer.")
    @PostCondition("Eine neue Simulation wird gestartet, mit runOneSim() komplett ausgeführt und die Start/Ende-Nachrichten werden gedruckt.")
    private static void runSimulation(int nr) {
        System.out.println("----------------------------------------");
        System.out.println("Simulation #" + nr + " startet...");
        System.out.println("----------------------------------------");

        Simulation s = new Simulation();
        s.runOneSim();

        System.out.println("Simulation #" + nr + " beendet.");
        System.out.println();

    }

    @Author("Efe")
    static class Stats {
        int numTyp; //4 interface class annaottaiton
        int numMetKon; //5  metod kosntukrot
        int numZusicherung; //6
        List<String> impList = new ArrayList<>();

    }

    @Author("Efe")
    @PostCondition("Prints extracted Data to terminal \"KontextB\"")
    private static void extractData(){
        System.out.println("########################");
        System.out.println("#######-KONTEXT B-######");
        System.out.println("########################\n");
        System.out.println();
        ClassesToTest testClasses = Test.class.getAnnotation(ClassesToTest.class); //1 -18class total
        Map<String, Stats> statss = new HashMap<>(); //2, 4 ,5 ,6

        //4,5,6 - fills the statts.
        for (Class<?> c : testClasses.value()) {
            Author author;
            Stats std_stat;

            //4 class annaottion interface / author num
            if (c.isAnnotationPresent(Author.class)) {
                author = c.getAnnotation(Author.class);
                statss.putIfAbsent(author.value(), new Stats());
                std_stat = statss.get(author.value());
                std_stat.numTyp++; //4
                std_stat.impList.add(c.getSimpleName());

                if (c.isAnnotationPresent(Invariant.class)) {
                    std_stat.numZusicherung++; //6
                }
                if (c.isAnnotationPresent(HistoryConstraint.class)) {
                    std_stat.numZusicherung++; //6
                }
            }

            Method[] methods = c.getDeclaredMethods();
            Constructor<?>[] constructors = c.getDeclaredConstructors();
            //5 klass-> metod konturktor num
            if (!c.isInterface()) {

                for (Method m : methods) {
                    if (!m.isAnnotationPresent(Author.class)) {
                        continue;
                    }
                    author = m.getAnnotation(Author.class);
                    statss.putIfAbsent(author.value(), new Stats());
                    std_stat = statss.get(author.value());
                    std_stat.numMetKon++; //5

                }

                for (Constructor<?> m : constructors) {
                    if (!m.isAnnotationPresent(Author.class)) {
                        continue;
                    }
                    author = m.getAnnotation(Author.class);
                    statss.putIfAbsent(author.value(), new Stats());
                    std_stat = statss.get(author.value());
                    std_stat.numMetKon++; //5
                }
            }

            //6 klass,interface -> Zusicherung Num
            if (!c.isAnnotation()) {
                for (Method m : methods) {
                    if (m.isAnnotationPresent(PostCondition.class)) {
                        author = m.getAnnotation(Author.class);
                        statss.putIfAbsent(author.value(), new Stats());
                        std_stat = statss.get(author.value());
                        std_stat.numZusicherung++; //6
                    }
                    if (m.isAnnotationPresent(PreCondition.class)) {
                        author = m.getAnnotation(Author.class);
                        statss.putIfAbsent(author.value(), new Stats());
                        std_stat = statss.get(author.value());
                        std_stat.numZusicherung++; //6
                    }
                }

                for (Constructor<?> m : constructors) {
                    if (m.isAnnotationPresent(PostCondition.class)) {
                        author = m.getAnnotation(Author.class);
                        statss.putIfAbsent(author.value(), new Stats());
                        std_stat = statss.get(author.value());
                        std_stat.numZusicherung++; //6
                    }
                    if (m.isAnnotationPresent(PreCondition.class)) {
                        author = m.getAnnotation(Author.class);
                        statss.putIfAbsent(author.value(), new Stats());
                        std_stat = statss.get(author.value());
                        std_stat.numZusicherung++; //6
                    }
                }
            }
        }


        System.out.println("$$$1-1-1-1-1-1-1-1-1-1$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("Namen aller zur Lösung dieser Aufgabe selbst geschriebenen Klassen, Interfaces und Annotationen. ");
        System.out.println("-----------------------------------------------------------------");
        for (int i = 0; i < 18; i = i + 3) {
            System.out.printf("| %s, %s, %s  \n", testClasses.value()[i], testClasses.value()[i + 1], testClasses.value()[i + 2]);
        }
        System.out.println("-----------------------------------------------------------------\n");


        System.out.println("$$$2-2-2-2-2-2--2-2-2-2-2$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("Wer was gemacht");
        System.out.println("-----------------------------------------------------------------");
        for (String s : statss.keySet()) {
            System.out.println("|" + s + ":" + statss.get(s).impList);
        }
        System.out.println("-----------------------------------------------------------------\n");


        System.out.println("$$$3-3-3-3-3-3-3-3-3-3-3-3-$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("Signaturen aller Methoden und Konstruktoren sowie alle dafür geltenden Zusicherungen");
        System.out.println("-----------------------------------------------------------------");
        for (Class<?> c : testClasses.value()) {
            if (c.isAnnotation()) {
                continue;
            }

            System.out.println(">>>>>>>>>>>>>>" + c + "<<<<<<<<<<<<<<<<<<<<");
            //----------------------------------
            System.out.print("|Invariants[Self]: ");
            if (c.isAnnotationPresent(Invariant.class)) {
                System.out.print(c.getAnnotation(Invariant.class).value());
            } else System.out.println("No Invariants");
            Class<?> iterClass = c.getSuperclass();
            while (iterClass != null && iterClass != Object.class) {
                if (iterClass.isAnnotationPresent(Invariant.class)) {
                    System.out.print("\n|--Geerbte Invariants from Obertyp \"" + iterClass + "\": ");
                    System.out.print(iterClass.getAnnotation(Invariant.class).value());
                }
                iterClass = iterClass.getSuperclass();
            }
            System.out.println("\n----------------");
            System.out.print("History-Constraints[Self]: ");
            if (c.isAnnotationPresent(HistoryConstraint.class)) {
                System.out.print(c.getAnnotation(HistoryConstraint.class).value());
            } else System.out.println("No HistoryConstraints");
            iterClass = c;
            while (iterClass.getSuperclass() != null && iterClass.getSuperclass() != Object.class) {
                iterClass = c.getSuperclass();
                if (iterClass.isAnnotationPresent(HistoryConstraint.class)) {
                    System.out.print("\n|--Geerbte Invariants from Obertyp \"" + iterClass + "\": ");
                    System.out.print(iterClass.getAnnotation(HistoryConstraint.class).value());
                }
                iterClass = iterClass.getSuperclass();
            }
            System.out.println("----------------");
            System.out.printf(">>>%s Constructor<<<\n", c.getSimpleName());
            for (Constructor<?> m : c.getDeclaredConstructors()) {
                System.out.println("Signature: " + m);
                System.out.println("PreConditions: " + (m.getAnnotation(PreCondition.class) != null
                        ? m.getAnnotation(PreCondition.class).value() : "No PreCondition"));
                System.out.println("PostConditions: " + (m.getAnnotation(PostCondition.class) != null
                        ? m.getAnnotation(PostCondition.class).value() : "No PostCondition"));
            }
            System.out.println("----------------");

            System.out.printf(">>>Methods In %s<<<\n", c.getSimpleName());
            //PreCon
            for (Method m : c.getDeclaredMethods()) {

                System.out.println("*Signature: " + m);

                System.out.println("|PreConditions[Self]: " + (m.getAnnotation(PreCondition.class) != null
                        ? m.getAnnotation(PreCondition.class).value() : "No PreCondition"));

                iterClass = c.getSuperclass();
                while(iterClass.getSuperclass() != null && iterClass.getSuperclass() != Object.class) {

                    try {
                        Method superMethod = iterClass.getDeclaredMethod(m.getName(), m.getParameterTypes());
                        if (superMethod.getAnnotation(PreCondition.class) != null) {
                            System.out.println("|-Geerbte PreCondition from \""+iterClass+"\": "+superMethod.getAnnotation(PreCondition.class).value());
                        }

                    } catch (NoSuchMethodException _){}

                    iterClass = iterClass.getSuperclass();
                }

                System.out.println("|PostConditions[Self]: " + (m.getAnnotation(PostCondition.class) != null
                        ? m.getAnnotation(PostCondition.class).value() : "No PostCondition"));

                iterClass = c.getSuperclass();
                while(iterClass.getSuperclass() != null && iterClass.getSuperclass() != Object.class) {

                    try {
                        Method superMethod = iterClass.getDeclaredMethod(m.getName(), m.getParameterTypes());
                        if (superMethod.getAnnotation(PostCondition.class) != null) {
                            System.out.println("|-Geerbte PostCondition from \""+iterClass+"\": "+superMethod.getAnnotation(PostCondition.class).value());
                        }

                    } catch (NoSuchMethodException _){}

                    iterClass = iterClass.getSuperclass();
                }

                System.out.println();
            }



            System.out.println();

        }

        System.out.println("-----------------------------------------------------------------");


        System.out.println("$$$4-4-4-4-4-4-4--4-4-4--4$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println(" Anzahl der Klassen, Interfaces und Annotationen");
        System.out.println("-----------------------------------------------------------------");
        for (String s : statss.keySet()) {
            System.out.println("|" + s + ":" + statss.get(s).numTyp);
        }
        System.out.println("-----------------------------------------------------------------\n");


        System.out.println("$$$5-5-5-5-5-5--5-5-5-5-5-5-$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("Anzahl der Methoden und Konstruktoren in den Klassen");
        System.out.println("-----------------------------------------------------------------");
        for (String s : statss.keySet()) {
            System.out.println("|" + s + ":" + statss.get(s).numMetKon);
        }
        System.out.println("-----------------------------------------------------------------\n");


        System.out.println("$$$6-6--6-6-6-6-6-6-6-6-6-6-6$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println("Anzahl der Zusicherungen in den Klassen und Interfaces (samt Methoden)");
        System.out.println("-----------------------------------------------------------------");
        for (String s : statss.keySet()) {
            System.out.println("|" + s + ":" + statss.get(s).numZusicherung);
        }
        System.out.println("-----------------------------------------------------------------\n");

    }


}







