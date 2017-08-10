package main.java.testingparts.CPU;

import main.java.controllers.ResultController;
import main.java.utilities.Timer;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Klasa zawierajaca w sobie zestawy testow operacji matematycznych na liczbach typu {@link long}
 */
public class LongTest {

    /**
     * Zmienna, do ktorej zostaja przypisywane wyniki,
     * w celu unikniecia optymalizacji wprowadzanych przez JVM.
     */
    private static long RESULT = 50;

    /**
     * Zmienna w ktorej trzymana jest suma czasu wykonywanych testow.
     */
    private static long TOTAL_TIME = 0;

    /**
     * Generuje tablice losowych liczb typu long o podanym rozmiarze.<br>
     * Zakres generownych liczb miesci sie w granicach:
     * od {@link Long#MIN_VALUE} do {@link Long#MAX_VALUE}.
     *
     * @param arraySize - rozmir generownej tablicy
     * @return tablica z wylosowanymi liczbami
     */
    private static long[] generateRandomLongArray(int arraySize){
        return ThreadLocalRandom
                .current()
                .longs(arraySize, Long.MIN_VALUE, Long.MAX_VALUE)
                .parallel()
                .map(i -> {
                    if(i == 0) i += 1L;
                    return i;
                })
                .toArray();
    }

    /**
     * @param randomIntArraySize
     * @param loops
     * @param loopTime
     * @return
     */
    private static double countOneOperationTime(int randomIntArraySize, int loops, double loopTime){
        return loopTime/(randomIntArraySize*loops);
    }

    /**
     * Funkcja dodajaca do zmiennej {@link LongTest#RESULT} liczby z tablicy podanej w argumencie.<br>
     * Wynik przypisywany jest do {@link LongTest#RESULT}
     *
     * @param longs Tablica liczb do dodania
     */
    private static void add(long[] longs){
        for (long i : longs) {
            RESULT += i;
        }
    }

    /**
     * Funkcja odejmujaca od zmiennej {@link LongTest#RESULT} liczby z tablicy podanej w argumencie. <br>
     * Wynik przypisywany jest do {@link LongTest#RESULT}
     *
     * @param longs Tablica liczb do odjecia
     */
    private static void subtract(long[] longs){
        for (long i : longs) {
            RESULT -= i;
        }
    }

    /**
     * Funkcja mnozaca zmienna {@link LongTest#RESULT} przez liczby z tablicy podanej w argumencie.<br>
     * Wynik przypisywany jest do {@link LongTest#RESULT}
     *
     * @param longs Tablica czynnikow mnozenia
     */
    private static void multiply(long[] longs){
        for (long i : longs) {
            RESULT *= i;
        }
    }

    /**
     * Funkcja dzielaca stala {@link Long#MAX_VALUE} przez liczby z tablicy podanej w argumencie.<br>
     * Wynik jest dodawany i przypisywany do {@link LongTest#RESULT}
     *
     * @param longs Tablica dzielnikow
     */
    private static void divide(long[] longs){
        for (long i : longs) {
            RESULT += Long.MAX_VALUE/i;
        }
    }

    /**
     * Mierzy czas wykoywania konkretnych operacji matematycznych na liczbach typu long.
     * Przez okreslona ilosc razy generuje rozne tablice liczb na ktorych sa wykonywane operacje.
     *
     * @param loops Ilosc powtorzen wykonywanych operacji na jednej tablicy liczb
     * @param arraySize Wielkosc generwanej tablicy
     * @param testable Interfejs wskazujacy na operacje dodawania, odejmowania, mnozenia lub dzielenia
     * @return Calkowity czas trwania wykonywanych operacji matematycznych
     * @see LongTest#add(long[])
     * @see LongTest#subtract(long[])
     * @see LongTest#multiply(long[])
     * @see LongTest#divide(long[])
     */
    private static long measure(int loops, int arraySize, MathInterface testable){
        long time = 0;
        for (int loop = 0; loop < loops; loop++) {
            long[] longArray = generateRandomLongArray(arraySize);
            Timer t = new Timer();
            testable.operate(longArray);
            time += t.check();
        }
        return time;
    }

    /**
     * Interfejs pozwalajacy dynamicznie wybierac operacje matematyczne
     */
    private interface MathInterface {

        /**
         * Funkcja wskazujaca liczby typu long na ktorych wykonywane sa dynamiczne operacje matematyczne
         *
         * @param longs Tablica liczb na ktorych wykonywane sa operacje
         */
        void operate(long[] longs);
    }

    /**
     * Typ wyliczeniowy zawierajacy w sobie fragmety kodu Pozwalajacego wskazac,
     * jakie operacje matematyczne maja zostac przetestowane
     */
    private enum WarmAndMeasure{

        /**
         * Fragment testujacy dodawanie
         */
        ADD {
            @Override
            public long test(int warmupLoops, int testLoops, int arraySize) {
                a = measure(warmupLoops, arraySize, LongTest::add);
                TOTAL_TIME += measure(testLoops, arraySize, LongTest::add);
                return a;
            }
        },

        /**
         * Fragment testujacy odejmowanie
         */
        SUBTRACT {
            @Override
            public long test(int warmupLoops, int testLoops, int arraySize) {
                a = measure(warmupLoops, arraySize, LongTest::subtract);
                TOTAL_TIME += measure(testLoops, arraySize, LongTest::subtract);
                return a;
            }
        },

        /**
         * Fragment testujacy mnozenie
         */
        MULTIPLY {
            @Override
            public long test(int warmupLoops, int testLoops, int arraySize) {
                a = measure(warmupLoops, arraySize, LongTest::multiply);
                TOTAL_TIME += measure(testLoops, arraySize, LongTest::multiply);
                return a;
            }
        },

        /**
         * Fragment testujacy dzielenie
         */
        DIVIDE {
            @Override
            public long test(int warmupLoops, int testLoops, int arraySize) {
                a = measure(warmupLoops, arraySize, LongTest::divide);
                TOTAL_TIME += measure(testLoops, arraySize, LongTest::divide);
                return a;
            }
        };

        /**
         * zmienna do ktorej zostaja przypisane czasy trwania operacji rozgrzewkowych
         */
        long a = 0;

        /**
         * Odpowiada za wykonanie kodu testujacego jedna operacje matematyczna,
         * z uwzglednieniem czesci rozgrzewkowej i testujacej.
         * Wynik czasu trwania kazdej operacji sumowany jest ze zmienna {@link LongTest#TOTAL_TIME}.
         *
         * @param warmupLoops Okresla ilosc powtorzen operacji rozgrzewkowych na tablicy longow
         * @param testLoops Okresla ilosc powtorzen operacji testowych na tablicy longow
         * @param arraySize Okresla wielkosc generowanej losowej tablicy na ktorej wykonywane
         *                  sa operacje w jednym powtorzeniu petli
         * @return zwracana jest zmienna {@link WarmAndMeasure#a} w celu zapobiegniecia niepozadanym operacjom JVM
         */
        public abstract long test(int warmupLoops, int testLoops, int arraySize);
    }

    /**
     * Wykonuje wszystkie dostepne operacje: dodawanie, odejmowanie, mnozenie, dzielenie.
     * Sume czasu trwania wykonywania wszystkich dzialan dodaje do {@link ResultController}
     */
    public static void measureAll(){
        TOTAL_TIME = 0;
        WarmAndMeasure.ADD.test(50, 300, 1_250_000);
        WarmAndMeasure.SUBTRACT.test(50, 300, 1_250_000);
        WarmAndMeasure.MULTIPLY.test(50, 300, 1_250_000);
        WarmAndMeasure.DIVIDE.test(50, 300, 1_250_000);
        ResultController.setLongResult(TOTAL_TIME);
    }
}
