package objects;

/**
 * Eine Klasse, die ein Grasobjekt im Spiel repräsentiert.
 */
public class Grass {

    /** Die X-Koordinate des Grasobjekts. */
    private int x;

    /** Die Y-Koordinate des Grasobjekts. */
    private int y;

    /** Der Typ des Grasobjekts. */
    private int type;

    /**
     * Konstruktor für die {@code Grass}-Klasse.
     *
     * @param x    Die X-Koordinate des Grasobjekts.
     * @param y    Die Y-Koordinate des Grasobjekts.
     * @param type Der Typ des Grasobjekts.
     */
    public Grass(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    /**
     * Gibt die X-Koordinate des Grasobjekts zurück.
     *
     * @return Die X-Koordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Gibt die Y-Koordinate des Grasobjekts zurück.
     *
     * @return Die Y-Koordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Gibt den Typ des Grasobjekts zurück.
     *
     * @return Der Typ des Grasobjekts.
     */
    public int getType() {
        return type;
    }
}
