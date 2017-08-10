package main.java.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Klasa odpowiadajaca za zapamietanie uzyskanego czasu, oraz przetworzenie go na wynik.
 */
public class ResultController {

    /**
     * Obiekt zawierajacy informacje o czasie trwania
     * odpowiedniego testu mierzacego wydajnosc procesora
     */
    private static final Map<String, Long> cpuResults = new HashMap<>();

    /**
     * Obiekt zawierajacy informacje o czasie trwania
     * odpowiedniego testu mierzacego wydajnosc pamieci Ram
     */
    private static final Map<String, Long> ramResults = new HashMap<>();

    /**
     * Obiekt zawierajacy informacje o czasie trwania
     * odpowiedniego testu mierzacego wydajnosc dysku
     */
    private static final Map<String, Long> diskResults = new HashMap<>();

    /**
     * Obiekt zawierajacy informacje o czasie trwania
     * testu mierzacego wydajnosc karty graficznej
     */
    private static long gpuResults;

    /**
     * Ustawia laczny czas trwania operacji matematycznych na iczbach typu int
     *
     * @param time Laczny czas trwania operacji matematycznych
     */
    public static void setIntResult(long time){
        cpuResults.put("intResult", time);
    }

    /**
     * Ustawia laczny czas trwania operacji matematycznych na iczbach typu long
     *
     * @param time Laczny czas trwania operacji matematycznych
     */
    public static void setLongResult(long time){
        cpuResults.put("longResult", time);
    }

    /**
     * Ustawia laczny czas trwania operacji matematycznych na iczbach typu double
     *
     * @param time Laczny czas trwania operacji matematycznych
     */
    public static void setDoubleResult(long time){
        cpuResults.put("DoubleResult", time);
    }

    /**
     * Ustawia laczny czas trwania operacji sortowania metoda quicksort
     *
     * @param time Laczny czas trwania operacji
     */
    public static void setQuickstartResult(long time){
        cpuResults.put("QuicksortResult", time);
    }

    /**
     * Ustawia laczny czas trwania operacji kompresji danych
     *
     * @param time Laczny czas trwania operacji
     */
    public static void setCompressResult(long time){
        cpuResults.put("compressResult", time);
    }

    /**
     * Ustawia laczny czas trwania operacji szyfrowania danych
     *
     * @param time Laczny czas trwania operacji
     */
    public static void setEncryptResult(long time){
        cpuResults.put("encryptResult", time);
    }

    /**
     * Ustawia laczny czas trwania operacji znajdowania liczb pierwszych
     *
     * @param time Laczny czas trwania operacji
     */
    public static void setPrimeNumberResult(long time){
        cpuResults.put("primeNumberResult", time);
    }

    /**
     * Ustawia laczny czas trwania operacji czytania danych z pliku
     *
     * @param time Laczny czas trwania operacji
     */
    public static void setDiskReadResult(long time){
        diskResults.put("readResult", time);
    }

    /**
     * Ustawia laczny czas trwania operacji zapisu danych do pliku
     *
     * @param time Laczny czas trwania operacji
     */
    public static void setDiskWriteResult(long time){
        diskResults.put("writetResult", time);
    }

    /**
     * Ustawia laczny czas trwania operacji liniowego odwolywania sie do komorek pamieci ram
     *
     * @param time Laczny czas trwania operacji
     */
    public static void setRamLinearWalkResult(long time){
        ramResults.put("linearResult", time);
    }

    /**
     * Ustawia laczny czas trwania operacji losowego odwolywania sie do komorek pamieci ram w obszarze strony
     *
     * @param time Laczny czas trwania operacji
     */
    public static void setRamRandomPageWalkResult(long time){
        ramResults.put("randomPageResult", time);
    }

    /**
     * Ustawia laczny czas trwania operacji losowego odwolywania sie do komorek pamieci ram w obszarze stosu
     *
     * @param time Laczny czas trwania operacji
     */
    public static void setRamRandomHeapWalkResult(long time){
        ramResults.put("randomHeapResult", time);
    }

    /**
     * Ustawia srednia liczbe klatek na sekunde uzyskana podczas wyswietlania animacji
     *
     * @param fps Serdnia liczba klatek na sekunde
     */
    public static void setGpuResult(long fps){
        gpuResults = fps;
    }

    /**
     * Zwraca wynik uzyskany przez procesor. Obliczany za pomoca danych w {@link ResultController#cpuResults}
     *
     * @return uzyskany wynik
     */
    public static long getCpuScore(){
        double sumOfTime = 0.0;
        for(double time : cpuResults.values()) sumOfTime += time;
        double cpuSlope = -33E-7;
        double cpuReferenceTime = 3.5E10;
        long cpuReferenceScore = 50000;
        return (sumOfTime > 0) ? (long) (cpuSlope * sumOfTime + (cpuReferenceScore - cpuSlope * cpuReferenceTime)) : Long.MIN_VALUE;
    }

    /**
     * Zwraca wynik uzyskany przez karte graficzna. Obliczany za pomoca danych w {@link ResultController#gpuResults}
     *
     * @return uzyskany wynik
     */
    public static long getGpuScore(){
        long gpuSlope = 10;
        long gpuReferenceFps = 1700;
        long gpuReferenceScore = 50000;
        return (gpuResults > 0) ? (gpuSlope * gpuResults + (gpuReferenceScore - gpuSlope * gpuReferenceFps)) : Long.MIN_VALUE;
    }

    /**
     * Zwraca wynik uzyskany przez pamiec ram. Obliczany za pomoca danych w {@link ResultController#ramResults}
     *
     * @return uzyskany wynik
     */
    public static long getRamScore(){
        double sumOfTime = 0.0;
        for(double time : ramResults.values()) sumOfTime += time;
        double ramSlope = -33E-7;
        double ramReferenceTime = 3E10;
        long ramReferenceScore = 50000;
        return (sumOfTime > 0) ? (long) (ramSlope * sumOfTime + (ramReferenceScore - ramSlope * ramReferenceTime)) : Long.MIN_VALUE;
    }


    /**
     * Zwraca wynik uzyskany przez dysk. Obliczany za pomoca danych w {@link ResultController#diskResults}
     *
     * @return uzyskany wynik
     */
    public static long getDiskScore(){
        double sumOfTime = 0.0;
        for(double time : diskResults.values()) sumOfTime += time;
        double diskSlope = -33E-7;
        double diskReferenceTime = 3E9;
        long diskReferenceScore = 50000;
        return (sumOfTime > 0) ? (long) (diskSlope * sumOfTime + (diskReferenceScore - diskSlope * diskReferenceTime)) : Long.MIN_VALUE;
    }

    /**
     * Zwraca wynik okreslany jako suma poszczegolnych wynikow. Jesli test na dany podzespol nie zostal wykonany
     * to nie jest tu uwzgledniony.
     *
     * @return suma poszczegolnych testow
     */
    public static long getTotalScore(){
        List<Long> results = new ArrayList<>();
        long sum = 0;
        results.add(getCpuScore());
        results.add(getDiskScore());
        results.add(getGpuScore());
        results.add(getRamScore());
        for(long result : results){
            if (result != Long.MIN_VALUE)
                sum += result;
        }
        return sum;
    }

    /**
     * Odpowiada za wyczyszczenie wynikow przed ponownym wywolaniem testow.
     */
    public static void reset(){
        cpuResults.clear();
        gpuResults = 0;
        ramResults.clear();
        diskResults.clear();
    }

}
