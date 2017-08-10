package main.java.testingparts.GPU;

/**
 * Klasa Posiadajaca niezbeden informacje o jednej scianie rysowanej bryly.
 */
public class Wall {

    /**
     * Zwraca wspolrzedne prawego gornego rogu
     *
     * @return Wspolrzedne prawego gornego rogu
     */
    public float[] getRightTop() {
        return rightTop;
    }

    /**
     * Wspolrzedne prawego gornego rogu
     */
    private float[] rightTop = new float[3];

    /**
     * Zwraca wspolrzedne lewgeo gornego rogu
     *
     * @return Wspolrzedne lewgeo gornego rogu
     */
    public float[] getLeftTop() {
        return leftTop;
    }

    /**
     * Wspolrzedne lewgeo gornego rogu
     */
    private float[] leftTop = new float[3];

    /**
     * Zwraca wspolrzedne prawego dolnego rogu
     *
     * @return Wspolrzedne prawego dolnego rogu
     */
    public float[] getRightBottom() {
        return rightBottom;
    }

    /**
     * Wspolrzedne prawego dolnego rogu
     */
    private float[] rightBottom = new float[3];

    /**
     * Zwraca wspolrzedne lewgeo dolnego rogu
     *
     * @return Wspolrzedne lewgeo dolnego rogu
     */
    public float[] getLeftBottom() {
        return leftBottom;
    }

    /**
     * Wspolrzedne lewgeo dolnego rogu
     */
    private float[] leftBottom = new float[3];

    /**
     * Zwraca wartosc koloru jako tablice wartosci RGB.
     *
     * @return Tablica wartosci koloru RGB.
     */
    public float[] getColor() {
        return color;
    }

    /**
     * Tablica wartosci koloru RGB.
     */
    private float[] color = new float[3];

    /**
     * Konstruktor klasy. jako parametry przyjmuje informacje o punktach polozenia i kolorze.
     *
     * @param rightTop Wspolrzedne prawego gornego rogu
     * @param leftTop Wspolrzedne lewego gornego rogu
     * @param rightBottom Wspolrzedne prawego dolnego rogu
     * @param leftBottom Wspolrzedne lewego dolnego rogu
     * @param color Tablica wartosci koloru RGB
     */
    Wall (float[] rightTop, float[] leftTop, float[] rightBottom, float[] leftBottom, float[] color){
        this.rightTop = rightTop;
        this.leftTop = leftTop;
        this.rightBottom = rightBottom;
        this.leftBottom = leftBottom;
        this.color = color;
    }


}
