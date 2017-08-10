package main.java.utilities;

/**
 * Klasa reprezentujaca licznik czasu.
 * pomaga w pomiarze czasu tetsu.
 */
public class Timer {

	/**
	 * Zmienna w ktorej zapamietany jest czas uruchomienia testu
	 */
	private long start = 0;

	/**
	 * Konstruktor. Uruchamia metode {@link Timer#play()}
	 */
	public Timer() {
		play(); 
	}

	/**
     * Zwraca czas trwania tetsu przez odjecie
     * aktualnego czasu procesora od zmiennej {@link Timer#start}.
     *
	 * @return czas trwnia testu (w nanosekundach)
	 */
	public long check() {
		return (System.nanoTime()-start);
	}

	/**
	 * Przypisuje zmiennej {@link Timer#start} obecny czas procesora.
	 */
	private void play() {
		start = System.nanoTime(); 
	}
}
