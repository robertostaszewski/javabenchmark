package main.java.testingparts.HardDrive;

import main.java.controllers.ResultController;
import main.java.utilities.Timer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Klasa zawierajaca zestawy metod testowych wykonywanych przy pomiarach wydajnosci dysku.
 */
public final class MeasureIOPerformance {

    /**
     * Wielkosc jednego pliku operacyjnego wyrazona w bajtach
     */
    private static final int SIZE_GB = 230 * 1024 * 1024;

    /**
     * Rozmiar bufora uzywanego do operacji zapisu i odczytu
     */
    private static final int BLOCK_SIZE = 2 * 1024 * 1024;

    /**
     * Ilosc blokow o rozmiarze {@link MeasureIOPerformance#BLOCK_SIZE}, aby uzykac {@link MeasureIOPerformance#SIZE_GB}
     */
    private static final int blocks = SIZE_GB / BLOCK_SIZE;

    /**
     * Czas trwania jedej operacji zapisu lub odczytu
     */
    private static long TOTAL_TIME = 0;

    /**
     * Kontener w ktorym znajuda sie wyniki zapisu do pliku
     */
    private static final List<Long> writeResults = new ArrayList<>();

    /**
     * Kontener w ktorym znajuda sie wyniki odczytu z pliku
     */
    private static final List<Long> readResults = new ArrayList<>();

    /**
     * Pliki do zapisane
     */
    private static File[] writeFiles = new File[3];

    /**
     * Pliki do odczytu
     */
    private static File[] readFiles = new File[3];

    /**
     * Ciag bajtow z danymi do zapisu lub odczytu, o rozmiarze {@link MeasureIOPerformance#BLOCK_SIZE}
     */
    static final byte[] buffer = new byte[BLOCK_SIZE];


    /**
     * Mierzy czas trwania jednej operacji zapisu i odczytu,
     * nastepnie dodaje go do {@link MeasureIOPerformance#writeResults}, lub
     * {@link MeasureIOPerformance#readResults}
     *
     * @param i Okresla plik na ktorym zostaja wykonane operacje {@link MeasureIOPerformance#readFiles},
     *          {@link MeasureIOPerformance#writeFiles}.
     */
    private static void measure(int i){
        writeFiles[i] = new File("src/resources/temp_file_to_delete_" +
                ThreadLocalRandom.current().nextInt());
        writeFiles[i].deleteOnExit();
        write(writeFiles[i]);
        writeResults.add(TOTAL_TIME);
        TOTAL_TIME = 0;
        read(readFiles[i]);
        readResults.add(TOTAL_TIME);
        TOTAL_TIME = 0;
    }

    /**
     * Sumuje czasy w {@link MeasureIOPerformance#writeResults} i {@link MeasureIOPerformance#readResults},
     * Nastepenie dodaje je do {@link ResultController}, oraz czysci {@link MeasureIOPerformance#writeResults} i
     * {@link MeasureIOPerformance#readResults}.
     */
    private static void addToResultController(){
        long writeTime = 0, readTime = 0;
        for(long time : writeResults) writeTime += time;
        for(long time : readResults) readTime += time;
        ResultController.setDiskReadResult(readTime);
        ResultController.setDiskWriteResult(writeTime);
        writeResults.clear();
        readResults.clear();
    }

    /**
     * Wskazuje jakie pliki nalezy odczytac a nastepnie wywoluje {@link MeasureIOPerformance#measure(int)} trzy razy
     */
    public static void test() {
        Arrays.setAll(readFiles, n -> new File("src/resources/readTest" + n));
        for (int i = 0; i < 3; i++) {
            measure(i);
        }
        addToResultController();
        Arrays.stream(writeFiles).forEach(file -> file.delete());
    }



    /**
     * Funkcja testujaca zapis
     *
     * @param file plik, do ktorego ma odbyc sie zapis
     */
    public static void write(File file){
        Random random = new Random();
        random.nextBytes(buffer);
        try (FileOutputStream out = new FileOutputStream(file)) {
            for (int i = 0; i < blocks; i++) {
                Timer t = new Timer();
                out.write(buffer);
                TOTAL_TIME += t.check();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * funkcja testujaca odczyt
     *
     * @param file plik z ktorego dane sa odczytane
     */
    public static void read(File file){
        int checksum =  0;
        int temp = 0;
        try (FileInputStream in = new FileInputStream(file)) {
            for (int i = 0; i < blocks; i++) {
                Timer t = new Timer();
                temp = in.read(buffer);
                TOTAL_TIME += t.check();
                checksum += buffer.hashCode();
                if (temp == -1)
                    break;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
