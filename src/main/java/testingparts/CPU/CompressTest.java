package main.java.testingparts.CPU;

import main.java.controllers.ResultController;
import main.java.utilities.Timer;
import com.sauljohnson.huff.HuffmanCompressor;

import java.util.Random;

/**
 * Klasa zawierajaca statyczne metody testujace szybkosc kompresli losowych danych.
 * Do dzialania wykorzystuje algorytm Huffman'a.
 */
public class CompressTest {

    /**
     * Zmienna, do ktorej zostaje dodany hash code zaszyfrowanych danych.
     * Uzywana w celu unikniecia optymalizacji wprowadzanych przez JVM.
     */
    private static int RESULT;

    /**
     * Funkcja zwracajaca losowa tablice bajtow.
     *
     * @param size Rozmiar tablicy.
     * @return Tablica losowych danych.
     */
    private static byte[] getRandomByteArrayInSize(int size){
        Random random = new Random();
        byte[] b = new byte[size];
        random.nextBytes(b);
        return b;
    }

    /**
     * Mierzy czas kompresji danych. Przyjmowany argument jest przekazywany do: {@link HuffmanCompressor#compress(byte[])}.
     *
     * @param data Tablica danych do kompresji.
     * @return Czas szyfrowania danych (w nanosekundach).
     */
    private static long measureCompressTime(byte[] data){
        HuffmanCompressor compressor = new HuffmanCompressor();
        Timer t = new Timer();
        RESULT += compressor.compress(data).getData().length;
        return t.check();
    }

    /**
     * Funkcja odpowiedzialna za logike testu. Na podstawie parametru {@code loop} powtarza operacje
     * tworzenia losowej tablicy o rozmiarze 1 mb, nastepnie kompresji danych.
     *
     * @param loop Okresla ilosc powtorzen pomiaru czasu kompresji danych.
     * @return Laczny czas kompresji powtorzony {@code loop} razy.
     */
    private static long compressTest(int loop) {
        long time = 0;
        for (int i = 0; i<loop; i++) {
            byte[] data = getRandomByteArrayInSize(1024 * 1024);
            time += measureCompressTime(data);
        }
        return time;

    }

    /**
     * Wykonuje test przez uruchomienie rundy rozgrzewkowej oraz testowej.
     * Wpisuje wynik do {@link ResultController}
     */
    public static void warmAndTest(){
        long TOTAL_TIME = 0;
        double a = compressTest(5);
        TOTAL_TIME += compressTest(30);
        ResultController.setCompressResult(TOTAL_TIME);
    }
}
