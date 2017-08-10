package main.java.testingparts.GPU;

/**
 * Klasa symulujaca obiekt szescianu, niezbedny do testu karty graficznej.
 */
class Cube {

    /**
     * Tablica posiadajaca dane o kazdej ze scian szescianu.
     */
    private final Wall[] walls = new Wall[6];

    /**
     * Kontruktor pobierajacy informacje o punktach kazdej ze scian oraz jej kolorze.
     * Na ich podstawie tworzona jest bryla.
     *
     * @param points tablica punktow kazdej ze scian
     * @param color kolor kazdej sciany
     */
    public Cube (float[][][] points, float[][] color){
        for (int i = 0; i<6; i++){
            float[] rightTop = points[i][0];
            float[] leftTop = points[i][1];
            float[] rightBottom = points[i][2];
            float[] leftBottom = points[i][3];
            walls[i] = new Wall(rightTop, leftTop, rightBottom, leftBottom, color[i]);
        }
    }

    /**
     * Zwraca tablice obiektow {@link Wall}, w celu poprawnego naryzsowania.
     *
     * @return Tablica obiektow {@link Wall} z ktorych zbudowana jest bryla.
     */
    public Wall[] getWalls(){
        return walls;
    }
}
