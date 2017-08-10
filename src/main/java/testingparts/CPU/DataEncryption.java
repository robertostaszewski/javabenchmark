package main.java.testingparts.CPU;

import main.java.controllers.ResultController;
import main.java.utilities.Timer;
import org.apache.commons.lang.RandomStringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Klasa zawierajaca statyczne metody testujace szybkosc szyfrowania losowych danych.
 * Do dzialania wykorzystuje algorytm AES
 */
public class DataEncryption {

    /**
     * Zmienna, do ktorej zostaje dodany hash code zaszyfrowanych danych.
     * Uzywana w celu unikniecia optymalizacji wprowadzanych przez JVM.
     */
    private static int RESULT = 0;

    /**
     * Tablica do ktorej zostaja zapisane zaszyfrowane dane.
     */
    private static byte[] encryptedData;

    /**
     * Funkcja szyfrujaca dane metoda AES. Do dzialania wykorzystuje {@link Cipher}
     *
     * @param key klucz o dlugosci 16 znakow niezbedny do zaszyfrowania oraz odszyfrowania danych
     * @param initVector wektor inicjujacy o dlugosci 16 znakow. Wskazana jest jego losowosc
     * @param data tablica danych do zaszyfrowania
     * @return zaszyfrowane dane, jesli nie zostanie rzucony zaden z wyjatkow: {@link InvalidKeyException},
     * {@link InvalidAlgorithmParameterException}, {@link NoSuchPaddingException}, {@link BadPaddingException},
     * {@link UnsupportedEncodingException}, {@link IllegalBlockSizeException},
     */
    private static byte[] crypto(String key, String initVector, byte[] data){
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            return cipher.doFinal(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

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
     * Mierzy czas szyfrowania danych. Przyjmowae argumenty sa przekazywane do: {@link DataEncryption#crypto(String, String, byte[])}
     *
     * @param key klucz o dlugosci 16 znakow niezbedny do zaszyfrowania oraz odszyfrowania danych
     * @param initVector wektor inicjujacy o dlugosci 16 znakow. Wskazana jest jego losowosc.
     * @param decryptedData tablica danych do zaszyfrowania
     * @return Czas szyfrowania danych (w nanosekundach)
     */
    private static long measureEncryptTime(String key, String initVector, byte[] decryptedData) {
        Timer t = new Timer();
        encryptedData = crypto(key, initVector, decryptedData);
        return t.check();
    }

    /**
     * Pobiera hash code z zasyfrowanych danych i dodaje do {@link DataEncryption#RESULT}
     */
    private static void getIntFromCompressedDataAndAddItToResult(){
        RESULT += encryptedData.hashCode();
    }

    /**
     * Funkcja odpowiedzialna za logike testu. Na podstawie parametru {@code loop} powtarza operacje
     * tworzenia losowej tablicy, generowania losowego klucza i wektora inicjujacego, a nastepnie wykonuje
     * szyfrwanie oraz sumuje czas.
     *
     * @param loop Okresla ilosc powtorzen pomiaru czasu szyfowania danych.
     * @return Laczny czas szyfrowania powtorzony {@code loop} razy.
     */
    private static long encryptTest(int loop){
        long time = 0;
        String chars = "ABCDEFGHIJKLMNOPRST1234567890";
        for (int i = 0; i<loop; i++){
            byte[] b = getRandomByteArrayInSize(1024*1024*5);
            String key = RandomStringUtils.random(16, chars);
            String initVector = RandomStringUtils.random(16, chars);
            time += measureEncryptTime(key, initVector, b);
            getIntFromCompressedDataAndAddItToResult();
        }
        return time;
    }

    /**
     * Wykonuje test przez uruchomienie rundy rozgrzewkowej oraz testowej.
     * Wpisuje wynik do {@link ResultController}
     */
    public static void warmAndTest(){
        long TOTAL_TIME = 0;
        long a = encryptTest(30);
        TOTAL_TIME += encryptTest(180);
        ResultController.setEncryptResult(TOTAL_TIME);
    }
}
