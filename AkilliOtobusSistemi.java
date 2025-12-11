import java.util.ArrayList;
import java.util.Random;

/**
 * PROJE ADI: Akıllı Otobüs ve Durak Koordinasyon Sistemi
 * YAZAR: INEXX
 * TARİH: 2025
 * * AÇIKLAMA:
 * Bu proje, toplu taşıma verimliliğini artırmak için geliştirilmiş bir Java simülasyonudur.
 * Duraklardaki sensörlerden ve otobüs içi kameralardan alınan verileri işleyerek,
 * otobüslerin duraklarda durup durmama kararını otonom olarak verir.
 * * TEKNOLOJİLER: Java, OOP, Algoritma, Simülasyon
 */

public class AkilliOtobusSistemi {

    public static void main(String[] args) {
        
        // --- SİSTEM KURULUMU (SETUP) ---
        
        // 1. Durak veritabanını oluştur (Route Management)
        ArrayList<String> duraklar = new ArrayList<>();
        duraklar.add("Merkez");      // Index 0
        duraklar.add("Sanayi");      // Index 1
        duraklar.add("Üniversite");  // Index 2
        duraklar.add("Hastane");     // Index 3

        // 2. Otobüsleri Sahaya Sür (Fleet Initialization)
        
        // Öndeki Otobüs (Karar verilecek araç)
        Otobus otobus1 = new Otobus("101-A", 100);
        otobus1.bulunduguDurak = 2; // Şu an Üniversite durağına yaklaşıyor

        // Arkadaki Otobüs (Destek aracı)
        Otobus otobus2 = new Otobus("101-B", 100);
        otobus2.bulunduguDurak = 1; // Sanayi durağında (1 durak arkadan geliyor)

        // 3. IoT Sistemlerini Başlat (Sensor & Camera Init)
        AkilliDurak suankiDurak = new AkilliDurak(duraklar.get(otobus1.bulunduguDurak));
        KameraSistemi onAracKamerasi = new KameraSistemi();
        KameraSistemi arkaAracKamerasi = new KameraSistemi();

        System.out.println("==========================================");
        System.out.println("   AKILLI OTOBÜS & DURAK SİSTEMİ (v1.0)   ");
        System.out.println("==========================================");
        System.out.println("Lokasyon: " + suankiDurak.durakAdi);

        // --- VERİ TOPLAMA ADIMI (DATA ACQUISITION) ---

        // ADIM 1: Durak Sensörü -> Bekleyen yolcu sayısını ölç
        int duraktaBekleyenSayisi = suankiDurak.bekleyenYolcuSayisiniOlc();
        System.out.println("[IOT - DURAK] Durakta bekleyen yolcu sayısı: " + duraktaBekleyenSayisi);

        // ADIM 2: Görüntü İşleme -> Öndeki aracın doluluğunu tespit et
        // (Simülasyon: Öndeki araç %90 dolu varsayılıyor)
        otobus1.yolcuSayisi = 90;
        int onAracYolcu = onAracKamerasi.yolcuSay(otobus1);
        System.out.println("[IOT - KAMERA] " + otobus1.plaka + " Yolcu: " + onAracYolcu + "/" + otobus1.kapasite);

        // ADIM 3: Telemetri -> Arkadaki aracın doluluğunu kontrol et
        // (Simülasyon: Arkadaki araç %20 dolu, yani boş yer var)
        otobus2.yolcuSayisi = 20;
        int arkaAracYolcu = arkaAracKamerasi.yolcuSay(otobus2);
        System.out.println("[IOT - KAMERA] " + otobus2.plaka + " Yolcu: " + arkaAracYolcu + "/" + otobus2.kapasite);

        // --- KARAR ALGORİTMASI (DECISION MAKING) ---
        System.out.println("\n--- YAPAY ZEKA KARAR MOTORU ---");

        int onAracBosYer = otobus1.kapasite - onAracYolcu;

        // Mantık: Öndeki araç duraktakilerin hepsini alamıyorsa
        if (onAracBosYer < duraktaBekleyenSayisi) {
            System.out.println("Durum Analizi: Öndeki araç kapasitesi yetersiz. (Kalan Boş Yer: " + onAracBosYer + ")");

            // Arkadaki araca bak: Onda yeterli yer var mı?
            int arkaAracBosYer = otobus2.kapasite - arkaAracYolcu;

            if (arkaAracBosYer >= duraktaBekleyenSayisi) {
                // Arkadaki araç hepsini alabilir -> Öndeki araç PAS GEÇSİN
                System.out.println(">>> KARAR: " + otobus1.plaka + " PAS GEÇECEK.");
                System.out.println(">>> SEBEP: Duraktaki " + duraktaBekleyenSayisi + " yolcuyu arkadaki " + otobus2.plaka + " aracı alacak.");
            } else {
                // Arkadaki de alamazsa -> Mecburen DUR
                System.out.println(">>> KARAR: " + otobus1.plaka + " DURACAK.");
                System.out.println(">>> SEBEP: Arkadaki araç da dolu, kapasite optimizasyonu mümkün değil.");
            }

        } else {
            // Öndeki araçta herkese yer varsa -> DUR
            System.out.println(">>> KARAR: " + otobus1.plaka + " DURACAK.");
            System.out.println(">>> SEBEP: Araç kapasitesi tüm yolcular için uygun.");
        }
        System.out.println("==========================================");
    }

    // --- NESNE MODELLERİ (DOMAIN CLASSES) ---

    // Otobüs Nesnesi
    static class Otobus {
        String plaka;
        int kapasite;
        int yolcuSayisi;
        int bulunduguDurak;

        public Otobus(String plaka, int kapasite) {
            this.plaka = plaka;
            this.kapasite = kapasite;
            this.yolcuSayisi = 0;
            this.bulunduguDurak = 0;
        }
    }

    // Kamera Sistemi (Mock/Simülasyon Sınıfı)
    static class KameraSistemi {
        public int yolcuSay(Otobus otobus) {
            // Gerçek hayatta burada OpenCV veya YOLO ile görüntü işleme yapılır.
            return otobus.yolcuSayisi;
        }
    }

    // Akıllı Durak (IoT Sensör Simülasyonu)
    static class AkilliDurak {
        String durakAdi;

        public AkilliDurak(String ad) {
            this.durakAdi = ad;
        }

        public int bekleyenYolcuSayisiniOlc() {
            // Sensör verisini simüle etmek için Random kullanıyoruz
            Random random = new Random();
            return random.nextInt(15) + 5; // 5 ile 20 arası rastgele yolcu
        }
    }
}

/* * =============================================================
 * GITHUB README.md DOSYASI İÇİN İÇERİK TASLAĞI
 * (Bunu kopyalayıp GitHub'da README.md dosyana yapıştırabilirsin)
 * =============================================================
 * * # 🚌 Smart Bus & Stop Coordination System (Akıllı Otobüs Sistemi)
 * * This project simulates an Intelligent Transportation System (ITS) designed to optimize public transport efficiency.
 * It uses simulated IoT sensors and cameras to make real-time decisions for buses to stop or skip stations based on capacity.
 * * ## 🚀 Features (Özellikler)
 * - **Smart Decision Making:** Prevents overcrowding by distributing passengers to empty buses.
 * - **IoT Simulation:** Simulates bus cameras and bus stop sensors.
 * - **OOP Design:** Modular structure using Bus, Station, and Sensor classes.
 * - **Efficiency:** Reduces travel time by skipping stops when the following bus is available.
 * * ## 🛠️ How it Works? (Nasıl Çalışır?)
 * 1. The system detects the number of passengers waiting at the stop.
 * 2. It checks the capacity of the arriving bus (Front Bus).
 * 3. It checks the capacity and location of the next bus (Back Bus).
 * 4. **Algorithm Decision:** * - If the front bus is full AND the back bus is empty -> **SKIP STOP**
 * - If the front bus has space -> **STOP**
 * * ## 💻 Tech Stack
 * - Java
 * - Object-Oriented Programming (OOP)
 * - Algorithm & Logic Design
 * * ---
 * Developed by INEXX
 */
