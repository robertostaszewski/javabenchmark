package main.java.testingparts.RAM;

import main.java.controllers.ResultController;
import main.java.utilities.Timer;

import java.util.Random;

/**
 * Klasa zawierajaca zestawy metod testowych wykonywanych przy pomiarach wydajnosci pamieci RAM.
 */
public class TestMemoryAccessPatterns
{
    /**
     * Rozmiar zmiennej typu long wyrazony w bajtach
     */
    private static final int LONG_SIZE = 8;

    /**
     * Rozmiar jednej strony wyrazony w bajtach
     */
    private static final int PAGE_SIZE = 2 * 1024 * 1024;

    /**
     * Rozmiar jednego gigabajta wyrazony w bajtach
     */
    private static final int ONE_GIG = 1024 * 1024 * 1024;

    /**
     * Rozmiar dwoch gigabajtow wyrazony w bajtach
     */
    private static final long TWO_GIG = 2L * ONE_GIG;

    /**
     * Rozmiar tablicy longow o rozmiarze {@link TestMemoryAccessPatterns#TWO_GIG}
     */
    private static final int ARRAY_SIZE = (int)(TWO_GIG / LONG_SIZE);

    /**
     * Ilosc zmiennych typu long przypadajca na rozmiar strony
     */
    private static final int WORDS_PER_PAGE = PAGE_SIZE / LONG_SIZE;

    /**
     * Maska bitowa dla elementow w calym zajetym obszarze
     */
    private static final int ARRAY_MASK = ARRAY_SIZE - 1;

    /**
     * Maska bitowa dla elementow w obszarze strony
     */
    private static final int PAGE_MASK = WORDS_PER_PAGE - 1;

    /**
     * Okresla do ktorej komorki nalezy sie odwolac podczas testowania
     * losowych stron pamieci
     */
    private static final int PRIME_INC = 600000;

    /**
     * Zaalokowana pamiec
     */
    private static final long[] memory = new long[ARRAY_SIZE];

    /**
     * Zmienna w ktorej zapisany jest czas trwania wykonywanych operacji
     */
    private static long TOTAL_TIME = 0;

    static
    {
        Random random = new Random();
        for (int i = 0; i < ARRAY_SIZE; i++)
        {
            memory[i] = random.nextLong();
        }
    }

    /**
     * Typ wyliczeniowy wskazujacy w jaki sposob nalezy przetestowac pamiec RAM
     */
    private enum StrideType
    {
        /**
         * Testowanie w sposob liniowy
         */
        LINEAR_WALK
                {
                    public int next(final int pageOffset, final int wordOffset, final int pos)
                    {
                        return (pos + 1) & ARRAY_MASK;
                    }
                },

        /**
         * Testowanie w sposob losowy (z pamieci strony)
         */
        RANDOM_PAGE_WALK
                {
                    public int next(final int pageOffset, final int wordOffset, final int pos)
                    {
                        return pageOffset + ((pos + PRIME_INC) & PAGE_MASK);
                    }
                },

        /**
         * Testowanie w sposob losowy (z pamieci stosu)
         */
        RANDOM_HEAP_WALK
                {
                    public int next(final int pageOffset, final int wordOffset, final int pos)
                    {
                        return (pos + PRIME_INC) & ARRAY_MASK;
                    }
                };

        /**
         * Funkcja wskazujaca na nastepna komorke do ktorej sie odwoujemy.
         *
         * @param pageOffset wskazuje gdzie konczy sie strona
         * @param wordOffset wskazuje na slowo ze strony
         * @param pos indeks poprzedniej pozycji z {@link TestMemoryAccessPatterns#memory}
         * @return nastepny indeks w {@link TestMemoryAccessPatterns#memory
         */
        public abstract int next(int pageOffset, int wordOffset, int pos);
    }

    /**
     * Wykonuje test pamieci RAM
     */
    public void test()
    {
        for (int i = 0; i < 2; i++)
        {
            TOTAL_TIME += perfTest(StrideType.LINEAR_WALK);
        }
        ResultController.setRamLinearWalkResult(TOTAL_TIME);
        TOTAL_TIME = 0;
        for (int i = 0; i < 2; i++)
        {
            TOTAL_TIME += perfTest(StrideType.RANDOM_HEAP_WALK);
        }
        ResultController.setRamRandomPageWalkResult(TOTAL_TIME);
        TOTAL_TIME = 0;
        for (int i = 0; i < 2; i++)
        {
            TOTAL_TIME += perfTest(StrideType.RANDOM_PAGE_WALK);
        }
        ResultController.setRamRandomHeapWalkResult(TOTAL_TIME);
        TOTAL_TIME = 0;
    }

    /**
     * Najpierw wykonuje kod majacy za zadanie usunac niechciane wlasciwosci maszyny wirtualnej,
     * nastpenie wywoluje {@link TestMemoryAccessPatterns#test()}
     */
    public void warmAndTest(){
        for (int i = 0; i < 1; i++)
        {
            TOTAL_TIME += perfTest(StrideType.LINEAR_WALK);
        }
        for (int i = 0; i < 1; i++)
        {
            TOTAL_TIME += perfTest(StrideType.RANDOM_HEAP_WALK);
        }
        for (int i = 0; i < 1; i++)
        {
            TOTAL_TIME += perfTest(StrideType.RANDOM_PAGE_WALK);
        }
        TOTAL_TIME = 0;
        test();
    }

    /**
     * Funkcja dokonujaca pomiaru czasu trwania odwolan do komorek pamieci zaleznie od
     * wybranego sposobu przegladania.
     *
     * @param strideType sposob przejscia po pamieci
     * @return czas trwania wszystkich operacji
     */
    private static long perfTest(final StrideType strideType)
    {
        Timer t = new Timer();

        int pos = -1;
        long result = 0;
        for (int pageOffset = 0; pageOffset < ARRAY_SIZE; pageOffset += WORDS_PER_PAGE)
        {
            for (int wordOffset = pageOffset, limit = pageOffset + WORDS_PER_PAGE;
                 wordOffset < limit;
                 wordOffset++)
            {
                pos = strideType.next(pageOffset, wordOffset, pos);
                result += memory[pos];
            }
        }

        final long duration = t.check();


        return duration;
    }
}