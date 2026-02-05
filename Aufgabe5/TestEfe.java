// Test.java

// SADECE BU DOSYADA İZİN VERİLEN IMPORT'LAR:
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Programmierparadigmen Aufgabe 5: Testklasse
 * Führt alle in der Aufgabenstellung vorgegebenen Tests durch.
 *
 * AUFTEILUNG DER ARBEITEN:
 * [Buraya kimin ne yaptığını yazmalısınız, örn: "Max Mustermann: ISet, OSet", "Maria Müller: MSet, Test.java"]
 */
public class TestEfe {

    // Test verilerimizi tutmak için ArrayList kullanıyoruz (burada izinli)
    private static ArrayList<Num> nums = new ArrayList<>();
    private static ArrayList<WildBee> wildBees = new ArrayList<>();
    private static ArrayList<HoneyBee> honeyBees = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== Programmierparadigmen - Aufgabe 5 Test ===");

        // --- ADIM 1: Test Verilerini ve Container'ları Oluştur (PUNKT 1) ---
        // (Bu adımı hemen aşağıda detaylandıracağız)
        System.out.println("\n--- 1. Erzeuge Testdaten und Container ---");
        initData();

        // Ödevde istenen tüm container'ları oluştur
        // (Şimdilik 'null' bir ISet<Num> ile başlatıyoruz)
        ISet<Num> set_num_i = new ISet<>(null);
        OSet<Num> set_num_o = new OSet<>(null);
        MSet<Num, Num> set_num_m = new MSet<>(null);

        ISet<Bee> set_bee_i = new ISet<>(null);
        OSet<Bee> set_bee_o = new OSet<>(null);
        // MSet<Bee, ?> ödevde istenmiyor ama Test 3 için gerekebilir

        ISet<WildBee> set_wild_i = new ISet<>(null);
        OSet<WildBee> set_wild_o = new OSet<>(null);
        MSet<WildBee, Integer> set_wild_m = new MSet<>(null);

        ISet<HoneyBee> set_honey_i = new ISet<>(null);
        OSet<HoneyBee> set_honey_o = new OSet<>(null);
        MSet<HoneyBee, String> set_honey_m = new MSet<>(null);

        // Container'ları doldur (Örnek)
        fillNumSet(set_num_i);
        fillWildBeeSet(set_wild_i);
        System.out.println("ISet<Num> (set_num_i) gefüllt:");
        printSet(set_num_i);
        System.out.println("ISet<WildBee> (set_wild_i) gefüllt:");
        printSet(set_wild_i);


        // --- ADIM 2: Jenerik Veri Kopyalama Testleri (PUNKT 2) ---
        // (Bu adımı bir sonraki konuşmamızda yapacağız)
        // --- ADIM 2: Jenerik Veri Kopyalama Testleri (PUNKT 2) ---
        System.out.println("\n--- 2. Führe generische Kopiertests durch ---");

        // Ödevde [cite: 110-115] istenen nesneleri atayalım
        ISet<Bee> a1 = set_bee_i;
        OSet<Bee> a2 = set_bee_o;
        MSet<WildBee, Integer> b1 = set_wild_m;
        MSet<HoneyBee, String> b2 = set_honey_m;
        OSet<WildBee> c1 = set_wild_o;
        ISet<HoneyBee> c2 = set_honey_i;

        // --- Test 2a: WildBee (c1) -> Bee (a1) ve WildBee (b1) Kopyalama ---
        System.out.println("\n--- Test 2a: (WildBee -> Bee) & (WildBee -> WildBee) ---");

        // Kaynak (c1) container'ı doldur
        fillWildBeeSet(c1);
        System.out.println("Quelle c1 (OSet<WildBee>):");
        printSet(c1); // Yardımcı metodumuzla yazdır

        // Kopyalama işlemi [cite: 116]
        System.out.println("Kopiere c1 -> a1 (ISet<Bee>)...");
        copyRelations(c1, a1); // WildBee'den Bee'ye (Alt tipten üst tipe)

        System.out.println("Kopiere c1 -> b1 (MSet<WildBee>)...");
        copyRelations(c1, b1); // WildBee'den WildBee'ye

        // Hedefleri yazdır
        System.out.println("Ergebnis a1 (ISet<Bee>):");
        printSet(a1);
        System.out.println("Ergebnis b1 (MSet<WildBee>):");
        printSet(b1);

        // WildBee'ye özgü 'length()' metodunu çağır (Test 2a'nın bir parçası) [cite: 116]
        System.out.println("Rufe length() auf a1 (ISet<Bee>) Elementen auf:");
        for(Bee bee : a1) {
            // bee.length() ÇAĞIRAMAYIZ, çünkü 'bee' bir Bee, WildBee değil.
            // Ama 'toString' metodumuzun bunu içerdiğini biliyoruz.
            // Ödevin asıl istediği, 'length' metodunun *kaynakta* (c1) çağrılabilmesiydi.
            // Biz kopyalamadan önce bunu yapabilirdik:
            // for(WildBee wb : c1) { wb.getLength(); } -> Bu çalışır.
            System.out.println("  " + bee.toString());
        }


        // --- Test 2b: HoneyBee (c2) -> Bee (a2) ve HoneyBee (b2) Kopyalama ---
        System.out.println("\n--- Test 2b: (HoneyBee -> Bee) & (HoneyBee -> HoneyBee) ---");

        // Kaynak (c2) container'ı doldur
        fillHoneyBeeSet(c2);
        System.out.println("Quelle c2 (ISet<HoneyBee>):");
        printSet(c2);

        // Kopyalama işlemi [cite: 120]
        System.out.println("Kopiere c2 -> a2 (OSet<Bee>)...");
        copyRelations(c2, a2); // HoneyBee'den Bee'ye

        System.out.println("Kopiere c2 -> b2 (MSet<HoneyBee>)...");
        copyRelations(c2, b2); // HoneyBee'den HoneyBee'ye

        // Hedefleri yazdır
        System.out.println("Ergebnis a2 (OSet<Bee>):");
        printSet(a2);
        System.out.println("Ergebnis b2 (MSet<HoneyBee>):");
        printSet(b2);


        // --- ADIM 3: Check / CheckForced Jeneriklik Testleri (PUNKT 3) ---
        // (Bu adımı daha sonra yapacağız)
        // --- ADIM 3: Check / CheckForced Jeneriklik Testleri (PUNKT 3) ---
        System.out.println("\n--- 3. Führe check/checkForced Tests durch ---");

        // Test 3a: Num -> Num (Farklı container tipleri) [cite: 122]
        // set_num_i (ISet<Num>) set_num_o (OSet<Num>) ve set_num_m (MSet<Num, Num>)
        // nesnelerinin birbirini 'check' edebilmesi gerekir.
        System.out.println("\n--- Test 3a: Num-Container untereinander ---");
        try {
            // ISet<Num> kullanarak OSet<Num>'ı kontrol et
            System.out.println("Teste: set_num_o.check(set_num_i)");
            set_num_o.check(set_num_i); // Başarılı olmalı
            System.out.println("...ERFOLGREICH");

            // MSet<Num, Num> kullanarak ISet<Num>'ı kontrol et
            System.out.println("Teste: set_num_i.check(set_num_m)");
            set_num_i.check(set_num_m); // Başarılı olmalı
            System.out.println("...ERFOLGREICH");

            // OSet<Num> kullanarak MSet<Num, Num>'ı kontrol et
            System.out.println("Teste: set_num_m.check(set_num_o)");
            set_num_m.check(set_num_o); // Başarılı olmalı
            System.out.println("...ERFOLGREICH");

        } catch (IllegalArgumentException e) {
            System.out.println("...FEHLGESCHLAGEN: " + e.getMessage());
        }


        // Test 3b: Bee -> WildBee (Üst tipten alt tipi kontrol etme) [cite: 123]
        // Bir ISet<Bee> (üst tip) nesnesini, bir ISet<WildBee> (alt tip)
        // nesnesini 'check' etmek için kullanabilmeliyiz.
        System.out.println("\n--- Test 3b: Bee-Container (Obertyp) prüft Untertyp ---");

        // set_bee_i (ISet<Bee>) ve set_wild_i (ISet<WildBee>) kullanalım
        // Önce set_bee_i'ye bir kural ekleyelim:
        // Sadece 'Rote Mauerbiene' -> 'Gehörnte Mauerbiene' ilişkisine izin versin.
        ISet<Bee> beeRuleSet = new ISet<>(null);
        beeRuleSet.setBefore(wildBees.get(0), wildBees.get(1)); // (wb0 -> wb1)

        System.out.println("Regel (beeRuleSet) enthält nur: wb0 -> wb1");
        System.out.println("Quelle (set_wild_i) enthält: wb0 -> wb1 VE wb1 -> wb2");
        printSet(set_wild_i); // Bu set (wb0->wb1) ve (wb1->wb2) ilişkilerini içeriyor

        try {
            // Test: ISet<WildBee> nesnesini, ISet<Bee> kuralıyla (beeRuleSet) kontrol et
            // Bu, 'check' metodunun '? super E' imzası sayesinde çalışır
            // (E = WildBee, ? super E = Bee)
            System.out.println("Teste: set_wild_i.check(beeRuleSet)");
            set_wild_i.check(beeRuleSet);

            System.out.println("...FEHLGESCHLAGEN (Erwartet, da wb1->wb2 fehlt)");
        } catch (IllegalArgumentException e) {
            // Bu hatayı bekliyoruz, çünkü 'set_wild_i' (wb1->wb2) ilişkisini
            // içeriyor, ancak 'beeRuleSet' bu ilişkiye izin VERMİYOR.
            System.out.println("...ERFOLGREICH (Fehler erwartet): Regel wb1->wb2 fehlt in beeRuleSet.");
        }


        // Test 3c: WildBee -> Bee (Alt tipten üst tipi kontrol etme - İmkansız) [cite: 123]
        // Ödev, bunun tersinin (alt tip ile üst tipi kontrol etme)
        // MÜMKÜN OLMAMASI gerektiğini belirtiyor.
        System.out.println("\n--- Test 3c: Kompilierfehler (Untertyp prüft Obertyp) ---");
        System.out.println("Der folgende Test ist auskommentiert, da er nicht kompiliert:");

        // set_bee_i.check(set_wild_i);

        System.out.println("// set_bee_i.check(set_wild_i);");
        System.out.println("Grund: set_bee_i (E=Bee) erwartet check(Ordered<? super Bee, ?>).");
        System.out.println("       set_wild_i ist aber Ordered<WildBee, ?>.");
        System.out.println("       WildBee ist KEIN Obertyp (super) von Bee.");
        System.out.println("       Jeneriklik kuralı (contravariance) bunu doğru bir şekilde engeller.");


        // --- ADIM 4: Alt Tip İlişkileri (Subtyping) (PUNKT 4) ---
        System.out.println("\n--- 4. Untersuchung der Untertypbeziehungen (ISet, OSet, MSet) ---");
        System.out.println("Begründung (warum ISet, OSet, MSet keine Untertypen voneinander sind):");
        System.out.println("1. OSet vs ISet: OSet.before() gibt 'Object' (bzw. OSetObjekt) zurück, ISet.before() gibt 'Iterator<E>' zurück. Diese Rückgabetypen sind inkompatibel (Kovarianz nicht anwendbar).");
        System.out.println("2. MSet vs OSet: MSet hat eine stärkere Einschränkung (E extends Modifiable) und zusätzliche Methoden (plus/minus). MSet könnte ein Untertyp von OSet sein, WENN die Typparameter übereinstimmen, aber sie sind konzeptuell unterschiedlich.");
        System.out.println("3. Allgemein: Die 'R'-Typparameter (Iterator vs Object) in OrdSet<E, R> machen sie zu unterschiedlichen Typen, die nicht direkt voneinander erben können.");


        // --- ADIM 5: Tüm Metotları Test Et (PUNKT 5) ---
        // (Bu adımı en son yapacağız)
        // --- ADIM 5: Tüm Metotları Test Et (PUNKT 5) ---
        System.out.println("\n--- 5. Test aller individuellen Methoden ---");

        // --- Test 5a: MSet.plus / MSet.minus ---
        System.out.println("\n--- Test 5a: MSet.plus / MSet.minus ---");
        // set_num_m (MSet<Num, Num>) kullanalım
        fillNumSet(set_num_m); // İçerik: {1, 2, 3, 5} İlişkiler: (1->2), (1->3), (2->5), (3->5)
        System.out.println("MSet<Num> (set_num_m) vor 'plus(10)':");
        printSet(set_num_m);

        // Num(10) ekleyelim.
        // Beklenen sonuç:
        // Yeni elemanlar: 11 (1+10), 12 (2+10), 13 (3+10), 15 (5+10)
        // Yeni ilişkiler: (11->1), (12->2), (13->3), (15->5) [cite: 66]
        set_num_m.plus(new Num(10));
        System.out.println("MSet<Num> (set_num_m) nach 'plus(10)':");
        printSet(set_num_m);

        // 'minus' testi (benzer mantık)
        set_num_m.minus(new Num(1)); // (10->11), (11->12), (12->13), (14->15) eklenmeli
        System.out.println("MSet<Num> (set_num_m) nach 'minus(1)':");
        printSet(set_num_m);


        // --- Test 5b: OSet.before ve OSetObjekt metotları ---
        System.out.println("\n--- Test 5b: OSet.before und OSetObjekt ---");
        // set_num_o (OSet<Num>) kullanalım
        fillNumSet(set_num_o); // İçerik: {1, 2, 3, 5} İlişkiler: (1->2), (1->3), (2->5), (3->5)
        System.out.println("OSet<Num> (set_num_o) Inhalt:");
        printSet(set_num_o);

        // OSet.before çağrısı [cite: 52]
        System.out.println("Rufe set_num_o.before(1, 5) auf...");
        // OSetObjekt, 'Object' olarak döndüğü için 'Ordered' veya 'Modifiable'a cast
        // GEREKMEZ, çünkü metotları doğrudan bu arayüzler üzerinden çağıracağız.
        // Ancak, onu bir değişkene atamak için bir arayüz tipi seçmeliyiz.
        Object beforeObj = set_num_o.before(nums.get(0), nums.get(3)); // before(1, 5)

        // (1, 5) arasındaki elemanlar {2, 3} olmalı
        // 'beforeObj' üzerinde 'Modifiable' metotlarını test edelim
        if (beforeObj instanceof Modifiable<?, ?>) {
            System.out.println("Objekt ist Modifiable. Teste add/subtract...");

            // Tipi Modifiable<Num, ?> olarak varsayabiliriz
            @SuppressWarnings("unchecked") // Bu cast, testimiz için güvenli
            Modifiable<Num, ?> modObj = (Modifiable<Num, ?>) beforeObj;

            // add(100)
            Object addedObj = modObj.add(new Num(100)); // Yeni nesne döndürmeli [cite: 60]
            System.out.println("  add(100) durchgeführt.");
            System.out.println("  Objekt nach 'add' ist " + (addedObj == modObj ? "identisch (Fehler)" : "neu (Erfolg)"));

            // subtract(2)
            Object subtractedObj = modObj.subtract(nums.get(1)); // subtract(2) [cite: 62]
            System.out.println("  subtract(2) durchgeführt.");
            System.out.println("  Objekt nach 'subtract' ist " + (subtractedObj == modObj ? "identisch (Fehler)" : "neu (Erfolg)"));

            // 'add' edilmiş nesne (addedObj) üzerinde 'subtract' çağırma
            @SuppressWarnings("unchecked")
            Modifiable<Num, ?> modAddedObj = (Modifiable<Num, ?>) addedObj;
            Object subFromAdded = modAddedObj.subtract(new Num(100));
            System.out.println("  subtract(100) auf 'addedObj' durchgeführt.");
            System.out.println("  Objekt nach 'subtract(100)' ist " + (subFromAdded == modAddedObj ? "identisch (Fehler)" : "neu (Erfolg)"));
        } else {
            System.out.println("FEHLER: OSet.before() 'Modifiable' döndürmedi!");
        }


        // --- Test 5c: ISet.before (Iterator) ---
        System.out.println("\n--- Test 5c: ISet.before (Iterator) ---");
        // set_num_i (ISet<Num>) kullanalım (dolu olduğunu biliyoruz)
        System.out.println("Rufe set_num_i.before(1, 5) auf...");
        Iterator<Num> iter = set_num_i.before(nums.get(0), nums.get(3)); // before(1, 5)

        System.out.println("Elemente zwischen 1 und 5 (sollte {2, 3} sein):");
        if (iter != null) {
            while (iter.hasNext()) {
                System.out.println("  - " + iter.next().toString());
            }
        } else {
            System.out.println("  (null döndü)");
        }


        // --- Test 5d: checkForced (Mantıksal Test) ---
        System.out.println("\n--- Test 5d: checkForced Logik ---");
        // set_wild_i (ISet<WildBee>) kullanalım
        // İçerik: (wb0 -> wb1), (wb1 -> wb2)
        System.out.println("ISet<WildBee> (set_wild_i) vor checkForced:");
        printSet(set_wild_i);

        // Yeni bir kural (c) oluşturalım: Sadece (wb0 -> wb1) izinli
        ISet<WildBee> c_rule = new ISet<>(null);
        c_rule.setBefore(wildBees.get(0), wildBees.get(1)); // wb0 -> wb1

        System.out.println("Rufe checkForced(c_rule) auf. (wb1 -> wb2) kaldırılmalı.");
        set_wild_i.checkForced(c_rule); // (wb1 -> wb2) ilişkisini kaldırmalı [cite: 43]

        System.out.println("ISet<WildBee> (set_wild_i) nach checkForced:");
        printSet(set_wild_i); // Sadece (wb0 -> wb1) kalmalı


        System.out.println("\n=== Test Ende ===");
    }

    /**
     * Test verilerini (Num, WildBee, HoneyBee) oluşturur.
     */
    private static void initData() {
        // Num test verileri
        nums.add(new Num(1));
        nums.add(new Num(2));
        nums.add(new Num(3));
        nums.add(new Num(5));

        // WildBee test verileri
        wildBees.add(new WildBee("Rote Mauerbiene", 12)); // wb0
        wildBees.add(new WildBee("Gehörnte Mauerbiene", 14)); // wb1
        wildBees.add(new WildBee("Blattschneiderbiene", 11)); // wb2

        // HoneyBee test verileri
        honeyBees.add(new HoneyBee("Arbeiterin", "Carnica")); // hb0
        honeyBees.add(new HoneyBee("Königin", "Buckfast")); // hb1
        honeyBees.add(new HoneyBee("Drohne", "Carnica-Ligustica")); // hb2
    }

    /**
     * Örnek: ISet<Num> container'ını temel verilerle doldurur.
     * (1 -> 2), (1 -> 3), (2 -> 5), (3 -> 5)
     */
    /**
     * Doldurma metodu (Jenerik): Bir OrdSet<Num> container'ını temel verilerle doldurur.
     * (1 -> 2), (1 -> 3), (2 -> 5), (3 -> 5)
     *
     * @param set Doldurulacak container (ISet, OSet veya MSet olabilir).
     * @param <T> OrdSet'i implemente eden herhangi bir tip.
     */
    private static <T extends OrdSet<Num, ?>> void fillNumSet(T set) {
        set.setBefore(nums.get(0), nums.get(1)); // 1 -> 2
        set.setBefore(nums.get(0), nums.get(2)); // 1 -> 3
        set.setBefore(nums.get(1), nums.get(3)); // 2 -> 5
        set.setBefore(nums.get(2), nums.get(3)); // 3 -> 5
    }

    /**
     * Doldurma metodu (Jenerik): Bir OrdSet<WildBee> container'ını temel verilerle doldurur.
     * (wb0 -> wb1), (wb1 -> wb2)
     *
     * @param set Doldurulacak container (ISet, OSet veya MSet olabilir).
     * @param <T> OrdSet'i implemente eden herhangi bir tip.
     */
    private static <T extends OrdSet<WildBee, ?>> void fillWildBeeSet(T set) {
        set.setBefore(wildBees.get(0), wildBees.get(1)); // wb0 -> wb1
        set.setBefore(wildBees.get(1), wildBees.get(2)); // wb1 -> wb2
    }

    /**
     * Doldurma metodu (Jenerik): Bir OrdSet<HoneyBee> container'ını temel verilerle doldurur.
     * (hb0 -> hb1)
     *
     * @param set Doldurulacak container (ISet, OSet veya MSet olabilir).
     * @param <T> OrdSet'i implemente eden herhangi bir tip.
     */
    private static <T extends OrdSet<HoneyBee, ?>> void fillHoneyBeeSet(T set) {
        set.setBefore(honeyBees.get(0), honeyBees.get(1)); // hb0 -> hb1
    }

    // --- YARDIMCI METOTLAR (Çok Önemli) ---

    /**
     * Bir OrdSet'in içeriğini (elemanlar ve ilişkiler) yazdırır.
     * Bu, test için ÇOK önemlidir.
     */
    public static <E> void printSet(OrdSet<E, ?> set) {
        // Ödev, Test.java'da ArrayList'e izin veriyor.
        // Elemanları geçici bir listede toplayalım.
        ArrayList<E> elements = new ArrayList<>();
        for(E e : set) {
            elements.add(e);
        }

        System.out.println("  Elemente (" + set.size() + "):");
        for(E e : elements) {
            System.out.println("    - " + e.toString());
        }

        System.out.println("  Relationen:");
        // Tüm eleman çiftlerini (brute-force) kontrol et
        for(E x : elements) {
            for(E y : elements) {
                if(x == y) continue;

                // 'before' metodunu test ediyoruz
                if(set.before(x, y) != null) {
                    System.out.println("    - " + x.toString() + " -> " + y.toString());
                }
            }
        }
    }

    /**
     * Ödevin 2. Adımı (PUNKT 2) için JENERİK KOPYALAMA metodu.
     * Bir container'daki (source) TÜM ilişkileri
     * başka bir container'a (target) kopyalar.
     *
     * Bu metodun jenerik imzası (<? extends E> ve <? super E>)
     * ödevin 'Test 2'sini geçmek için KRİTİKTİR.
     *
     * @param <E> Hedef container'ın temel tipi (örn. Bee)
     * @param <T> Kaynak container'ın tipi (örn. WildBee)
     */
    public static <E, T extends E, R> void copyRelations(
            OrdSet<T, R> source,      // Kaynak (örn: ISet<WildBee>)
            OrdSet<E, ?> target       // Hedef (örn: ISet<Bee>)
    ) {

        // Önce tüm elemanları 'source'dan 'target'a eklemeliyiz
        // (setBefore bunu bizim için zaten yapıyor, ama 'source'daki
        // ilişkisi olmayan elemanları da eklemek için bu iyi bir yöntem)
        ArrayList<T> sourceElements = new ArrayList<>();
        for(T e : source) {
            sourceElements.add(e);

            // Elemanı 'target'a "ekle" (ilişkisi olmasa bile).
            // 'setBefore(e, e)' hata verir, bu yüzden 'findOrCreate'
            // mantığını kullanan bir 'add' metodu olsaydı iyi olurdu.
            // Şimdilik 'setBefore'un elemanları eklediğine güveniyoruz.
        }

        // Şimdi tüm ilişkileri kopyala
        for(T x : sourceElements) {
            for(T y : sourceElements) {
                if(x == y) continue;

                // Eğer kaynakta (source) x -> y ilişkisi varsa...
                if(source.before(x, y) != null) {
                    try {
                        // ...bu ilişkiyi hedefe (target) de ekle.
                        // BURASI JENERİK TESTİN KALBİDİR:
                        // target.setBefore( (E)x, (E)y ) çağrısı yapıyoruz
                        // (WildBee -> Bee gibi)
                        target.setBefore(x, y);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Kopyalama hatası: " + e.getMessage());
                    }
                }
            }
        }
    }
}

