package main.java.testingparts.CPU;

import main.java.controllers.ResultController;
import main.java.utilities.Timer;

import java.util.Arrays;
import java.util.Random;

/**
 * Klasa zawierajaca statyczne metody testujace szybkosc sortowania liczb z losowej tablicy.
 * Do sortowania wykorzystywany jest algorytm quicksort
 */
public class Quicksort {

    /**
     * Zmienna, do ktorej zostaje dodany hash code posortowanej tablicy.
     * Używana w celu unikniecia optymalizacji wprowadzanych przez JVM.
     */
    private static int RESULT = 0;

    /**
     * Funkcja zmieniajaca kolejnosc tablicy.<br>
     * Wartosci mniejsze od punktu znajdującego się w równej odleglosci
     * wzgledem indeksu lewego i prawego sa po lewej stronie.<br>
     * Wartosci wieksze od punktu znajdującego się w równej odleglosci
     * wzgledem indeksu lewego i prawego sa po prawej stronie.<br>
     *
     * @param array Tablica na ktorej wykonywane sa operacje
     * @param left Indeks o mniejszej wartosci, wzgledem ktorego wykonywana jest operacja
     * @param right Indeks o wiekszej wartosci, wzgledem ktorego wykonywana jest operacja
     * @return Indeks pierwszego elementu z czesci elementow o wiekszych wartosciach
     */
    private static int partition(int array[], int left, int right) {
        int indexLeft = left, indexRight = right;
        int tmp;
        int pivot = array[(left + right) / 2];

        while (indexLeft <= indexRight) {
            while (array[indexLeft] < pivot)
                indexLeft++;
            while (array[indexRight] > pivot)
                indexRight--;
            if (indexLeft <= indexRight) {
                tmp = array[indexLeft];
                array[indexLeft] = array[indexRight];
                array[indexRight] = tmp;
                indexLeft++;
                indexRight--;
            }
        }
        return indexLeft;
    }

    /**
     * Funkcja rekurencyjna ktora najpierw wywoluje funkcje {@link Quicksort#partition(int[], int, int)}
     * dla calego zakresu podanej tablicy, a nastepnie wzgledem zwroconych wartosci
     *
     * @param array tablica do posortowania
     * @param left mniejszy indeks dla ktorego jest wywolywana funkcja
     * @param right wiekszy indeks dla ktorego jest wywolywana funkcja
     * @return zwracana wartoscia jest posortowana tablica
     */
    private static int[] quickSort(int array[], int left, int right) {
        int index = partition(array, left, right);
        if (left < index - 1)
            quickSort(array, left, index - 1);
        if (index < right)
            quickSort(array, index, right);
        return array;
    }

    /**
     * Funkcja generujaca tablice z losowymi wartosciami liczb typu int
     *
     * @return Tablica losowych liczb
     */
    private static int[] generateArray(){
        Random random = new Random();
        int [] array = new int[30];
        for(int i = 0; i<array.length; i++){
            array[i] = random.nextInt();
        }
        return array;
    }

    /**
     * Funkcja tesujaca szybkosc sortowania metoda quicksort, przez generacje tablicy
     * z losowymi wartosciami
     *
     * @param loop okresla jak wiele razy zostanie wygenerowana tablica do posortowania
     * @return calkowity czas wykonania wszytskich operacji (w nanosekundach)
     */
    private static long quicksortTest(int loop){
        long time = 0;
        for (int i = 0; i<loop; i++){
            int[] toSort = generateArray(), sorted;
            Timer t = new Timer();
            sorted = quickSort(toSort, 0, toSort.length-1);
            Arrays.stream(sorted).forEach(val -> System.out.print(val + ", "));
            time += t.check();
            RESULT += sorted.hashCode();
        }
        return time;
    }

    /**
     * Wykonuje test przez uruchomienie rundy rozgrzewkowej oraz testowej.
     * Wpisuje wynik do {@link ResultController}
     */
    public static void warmAndTest(){
        long TOTAL_TIME = 0;
        double a = quicksortTest(10);
        TOTAL_TIME += quicksortTest(60);
        ResultController.setQuickstartResult(TOTAL_TIME);
    }
}

