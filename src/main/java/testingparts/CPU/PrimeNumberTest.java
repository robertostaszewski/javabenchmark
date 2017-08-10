package main.java.testingparts.CPU;

import main.java.controllers.ResultController;
import main.java.utilities.Timer;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Klasa zawierajaca statyczne metody testujace szybkosc znajdowania liczb pierwszych.
 * Do znajdowania wykorzystuje operacje porownania
 */
public class PrimeNumberTest {

    /**
     * Zmienna, do ktorej zostaja dodane znalezione liczby pierwsze.
     * Używana w celu unikniecia optymalizacji wprowadzanych przez JVM.
     */
    private static int RESULT = 0;

    /**
     * Generuje tablice losowych liczb typu int o podanym rozmiarze.<br>
     * Zakres generownych liczb miesci sie w granicach:
     * od 0 do 10 000.
     * Rozmiar generowanej tablicy to 100 000.
     *
     * @return tablica z wylosowanymi liczbami
     */
    private static int[] generateNumbers(){
        return ThreadLocalRandom.current().ints(100_000,0, 10000).toArray();
    }

    /**
     * Funkcja sprawdzajaca czy liczba jest pierwsza.
     * Sprawdzenie odbywa sie przez wykoywanie dzielenia modulo,
     * przez kazda liczbe z zakresu od 2 do sprawdzanej liczby,
     * oraz porownanie wyniku z zerem.
     *
     * @param number Sprawdzana liczba.
     * @return Zwracana jest prawda jesli liczba jest liczba pierwsza, w przeciwnym razie falsz.
     */
    private static boolean isPrime(int number) {
        for(int i=2;i<number;i++) {
            if(number%i==0)
                return false;
        }
        return true;
    }

    /**
     * Funkcja testujaca wydajnosc znajdowania liczb pierwszych.
     * Generuje tablice z losowymi liczbami, ktore nastepnie sprawdza.
     * Ilosc powtorzen tej operacji okresla przyjmowany argument.
     *
     * @param loop okresla ilosc powtorzen wykonywania operacji znajdowania liczb pierwszych
     * @return calkowity czas wykonania wszytskich operacji (w nanosekundach)
     */
    private static long primeNumberTest(int loop){
        long time = 0;
        for(int i=0; i<loop; i++) {
            int[] array = generateNumbers();
            Timer t = new Timer();
            for (int number : array) {
                if (isPrime(number))
                    RESULT += number;
            }
            time += t.check();
        }

        return time;
    }

    /**
     * Wykonuje test przez uruchomienie rundy rozgrzewkowej oraz testowej.
     * Wpisuje wynik do {@link ResultController}
     */
    public static void warmAndTest(){
        long TOTAL_TIME = 0;
        double a = primeNumberTest(5);
        TOTAL_TIME += primeNumberTest(30);
        ResultController.setPrimeNumberResult(TOTAL_TIME);
    }

}
