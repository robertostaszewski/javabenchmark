package main.java.testingparts.CPU;

import main.java.controllers.ResultController;
import main.java.utilities.Timer;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Klasa zawierajaca w sobie zestawy testow operacji matematycznych na liczbach typu {@link int}
 */
public class IntTest {

    /**
     * Zmienna, do ktorej zostaja przypisywane wyniki,
     * w celu unikniecia optymalizacji wprowadzanych przez JVM.
     */
    private static int RESULT = 50;

    /**
     * Zmienna w ktorej trzymana jest suma czasu wykonywanych testow.
     */
    private static long TOTAL_TIME = 0;

    /**
     * Generuje tablice losowych liczb typu int o podanym rozmiarze.<br>
     * Zakres generownych liczb miesci sie w granicach:
     * od {@link Integer#MIN_VALUE} do {@link Integer#MAX_VALUE}.
     *
     * @param arraySize - rozmir generownej tablicy
     * @return tablica z wylosowanymi liczbami
     */
    private static int[] generateRandomIntArray(int arraySize){
        return ThreadLocalRandom
                .current()
                .ints(arraySize, Integer.MIN_VALUE, Integer.MAX_VALUE)
                .parallel()
                .map(i -> {
                    if(i == 0) i++;
                    return i;
                })
                .toArray();
    }

    /**
     * Funkcja dodajaca do zmiennej {@link IntTest#RESULT} liczby z tablicy podanej w argumencie.<br>
     * Wynik przypisywany jest do {@link IntTest#RESULT}
     *
     * @param ints Tablica liczb do dodania
     */
    private static void add(int[] ints){
        for (int i : ints) {
            RESULT += i;
        }
    }

    /**
     * Funkcja odejmujaca od zmiennej {@link IntTest#RESULT} liczby z tablicy podanej w argumencie. <br>
     * Wynik przypisywany jest do {@link IntTest#RESULT}
     *
     * @param ints Tablica liczb do odjecia
     */
    private static void subtract(int[] ints){
        for (int i : ints) {
            RESULT -= i;
        }
    }

    /**
     * Funkcja mnozaca zmienna {@link IntTest#RESULT} przez liczby z tablicy podanej w argumencie.<br>
     * Wynik przypisywany jest do {@link IntTest#RESULT}
     *
     * @param ints Tablica czynnikow mnozenia
     */
    private static void multiply(int[] ints){
        for (int i : ints) {
            RESULT *= i;
        }
    }

    /**
     * Funkcja dzielaca stala {@link Integer#MAX_VALUE} przez liczby z tablicy podanej w argumencie.<br>
     * Wynik jest dodawany i przypisywany do {@link IntTest#RESULT}
     *
     * @param ints Tablica dzielnikow
     */
    private static void divide(int[] ints){
        for (int i : ints) {
            RESULT += Integer.MAX_VALUE/i;
        }
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
     * Mierzy czas wykoywania konkretnych operacji matematycznych na liczbach typu int.
     * Przez okreslona ilosc razy generuje rozne tablice liczb na ktorych sa wykonywane operacje.
     *
     * @param loops Ilosc powtorzen wykonywanych operacji na jednej tablicy liczb
     * @param arraySize Wielkosc generwanej tablicy
     * @param testable Interfejs wskazujacy na operacje dodawania, odejmowania, mnozenia lub dzielenia
     * @return Calkowity czas trwania wykonywanych operacji matematycznych
     * @see IntTest#add(int[])
     * @see IntTest#subtract(int[])
     * @see IntTest#multiply(int[])
     * @see IntTest#divide(int[])
     */
    private static long measure(int loops, int arraySize, MathInterface testable){
        long time = 0;
        for (int loop = 0; loop < loops; loop++) {
            int[] ints = generateRandomIntArray(arraySize);
            Timer t = new Timer();
            testable.operate(ints);
            time += t.check();
        }
        return time;
    }

    /**
     * Interfejs pozwalajacy dynamicznie wybierac operacje matematyczne
     */
    private interface MathInterface {

        /**
         * Funkcja wskazujaca liczby typu int na ktorych wykonywane sa dynamiczne operacje matematyczne
         *
         * @param ints Tablica liczb na ktorych wykonywane sa operacje
         */
        void operate(int[] ints);
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
                a = measure(warmupLoops, arraySize, IntTest::add);
                TOTAL_TIME += measure(testLoops, arraySize, IntTest::add);
                return a;
            }
        },

        /**
         * Fragment testujacy odejmowanie
         */
        SUBTRACT {
            @Override
            public long test(int warmupLoops, int testLoops, int arraySize) {
                a = measure(warmupLoops, arraySize, IntTest::subtract);
                TOTAL_TIME += measure(testLoops, arraySize, IntTest::subtract);
                return a;
            }
        },

        /**
         * Fragment testujacy mnozenie
         */
        MULTIPLY {
            @Override
            public long test(int warmupLoops, int testLoops, int arraySize) {
                a = measure(warmupLoops, arraySize, IntTest::multiply);
                TOTAL_TIME += measure(testLoops, arraySize, IntTest::multiply);
                return a;
            }
        },

        /**
         * Fragment testujacy dzielenie
         */
        DIVIDE {
            @Override
            public long test(int warmupLoops, int testLoops, int arraySize) {
                a = measure(warmupLoops, arraySize, IntTest::divide);
                TOTAL_TIME += measure(testLoops, arraySize, IntTest::divide);
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
         * Wynik czasu trwania kazdej operacji sumowany jest ze zmienna {@link IntTest#TOTAL_TIME}.
         *
         * @param warmupLoops Okresla ilosc powtorzen operacji rozgrzewkowych na tablicy intow
         * @param testLoops Okresla ilosc powtorzen operacji testowych na tablicy intow
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
        ResultController.setIntResult(TOTAL_TIME);
    }

}
