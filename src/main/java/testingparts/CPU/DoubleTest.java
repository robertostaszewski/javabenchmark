package main.java.testingparts.CPU;

import main.java.controllers.ResultController;
import main.java.utilities.Timer;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Klasa zawierajaca w sobie zestawy testow operacji matematycznych na liczbach typu {@link double}
 */
public class DoubleTest {

    /**
     * Zmienna, do ktorej zostaja przypisywane wyniki,
     * w celu unikniecia optymalizacji wprowadzanych przez JVM.
     */
    private static double RESULT = 50.0;

    /**
     * Zmienna w ktorej trzymana jest suma czasu wykonywanych testow.
     */
    private static long TOTAL_TIME = 0;


    /**
     * Generuje tablice losowych liczb typu int o podanym rozmiarze.<br>
     * Zakres generownych liczb miesci sie w granicach:
     * od -{@link Double#MAX_VALUE} do {@link Double#MAX_VALUE}.
     *
     * @param arraySize - rozmir generownej tablicy
     * @return tablica z wylosowanymi liczbami
     */
    private static double[] generateRandomDoubleArray(int arraySize){
        return ThreadLocalRandom
                .current()
                .doubles(arraySize, -Double.MAX_VALUE, Double.MAX_VALUE)
                .parallel()
                .map(i -> {
                    if(i == 0) i += 1.0;
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
     * Funkcja dodajaca do zmiennej {@link DoubleTest#RESULT} liczby z tablicy podanej w argumencie.<br>
     * Wynik przypisywany jest do {@link DoubleTest#RESULT}
     *
     * @param doubles Tablica liczb do dodania
     */
    private static void add(double[] doubles){
        for (double i : doubles) {
            RESULT += i;
        }
    }

    /**
     * Funkcja odejmujaca od zmiennej {@link DoubleTest#RESULT} liczby z tablicy podanej w argumencie. <br>
     * Wynik przypisywany jest do {@link DoubleTest#RESULT}
     *
     * @param doubles Tablica liczb do odjecia
     */
    private static void subtract(double[] doubles){
        for (double i : doubles) {
            RESULT -= i;
        }
    }

    /**
     * Funkcja mnozaca zmienna {@link DoubleTest#RESULT} przez liczby z tablicy podanej w argumencie.<br>
     * Wynik przypisywany jest do {@link DoubleTest#RESULT}
     *
     * @param doubles Tablica czynnikow mnozenia
     */
    private static void multiply(double[] doubles){
        for (double i : doubles) {
            RESULT *= i;
        }
    }

    /**
     * Funkcja dzielaca stala {@link Double#MAX_VALUE} przez liczby z tablicy podanej w argumencie.<br>
     * Wynik jest dodawany i przypisywany do {@link DoubleTest#RESULT}
     *
     * @param doubles Tablica dzielnikow
     */
    private static void divide(double[] doubles){
        for (double i : doubles) {
            RESULT += Double.MAX_VALUE/i;
        }
    }

    /**
     * Mierzy czas wykoywania konkretnych operacji matematycznych na liczbach typu double.
     * Generuje rozne tablice liczb na ktorych sa wykonywane operacje.
     *
     * @param loops Okresla ile razy bedzie generowana nowa tablica
     * @param arraySize Wielkosc generwanej tablicy
     * @param testable Interfejs wskazujacy na operacje dodawania, odejmowania, mnozenia lub dzielenia
     * @return Calkowity czas trwania wykonywanych operacji matematycznych
     * @see DoubleTest#add(double[])
     * @see DoubleTest#subtract(double[])
     * @see DoubleTest#multiply(double[])
     * @see DoubleTest#divide(double[])
     */
    private static long measure(int loops, int arraySize, MathInterface testable){
        long time = 0;
        for (int loop = 0; loop < loops; loop++) {
            double[] doubles = generateRandomDoubleArray(arraySize);
            Timer t = new Timer();
            testable.operate(doubles);
            time += t.check();
        }
        return time;
    }

    /**
     * Interfejs pozwalajacy dynamicznie wybierac operacje matematyczne
     */
    private interface MathInterface {
        /**
         * Funkcja wskazujaca liczby typu double na ktorych wykonywane sa dynamiczne operacje matematyczne
         *
         * @param doubles Tablica liczb na ktorych wykonywane sa operacje
         */
        void operate(double[] doubles);
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
                a = measure(warmupLoops, arraySize, DoubleTest::add);
                TOTAL_TIME += measure(testLoops, arraySize, DoubleTest::add);
                return a;
            }
        },

        /**
         * Fragment testujacy odejmowanie
         */
        SUBTRACT {
            @Override
            public long test(int warmupLoops, int testLoops, int arraySize) {
                a = measure(warmupLoops, arraySize, DoubleTest::subtract);
                TOTAL_TIME += measure(testLoops, arraySize, DoubleTest::subtract);
                return a;
            }
        },

        /**
         * Fragment testujacy mnozenie
         */
        MULTIPLY {
            @Override
            public long test(int warmupLoops, int testLoops, int arraySize) {
                a = measure(warmupLoops, arraySize, DoubleTest::multiply);
                TOTAL_TIME += measure(testLoops, arraySize, DoubleTest::multiply);
                return a;
            }
        },

        /**
         * Fragment testujacy dzielenie
         */
        DIVIDE {
            @Override
            public long test(int warmupLoops, int testLoops, int arraySize) {
                a = measure(warmupLoops, arraySize, DoubleTest::divide);
                TOTAL_TIME += measure(testLoops, arraySize, DoubleTest::divide);
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
         * Wynik czasu trwania kazdej operacji sumowany jest ze zmienna {@link DoubleTest#TOTAL_TIME}.
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
        ResultController.setDoubleResult(TOTAL_TIME);
    }

}
