package objects;

import java.util.Random;

/**
 * Eine Klasse, die Hintergrundbäume repräsentiert.
 * Diese Bäume haben eine animierte Textur für eine realistischere Umgebungsdarstellung.
 */
public class BackgroundTree {

    private int x, y, type, aniIndex, aniTick;

    /**
     * Konstruktor für die {@code BackgroundTree}-Klasse.
     *
     * @param x    Die X-Koordinate des Baumes.
     * @param y    Die Y-Koordinate des Baumes.
     * @param type Der Typ des Baumes.
     */
    public BackgroundTree(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;

        // Setzt den aniIndex auf einen zufälligen Wert, um Variationen für die Bäume zu erhalten.
        Random r = new Random();
        aniIndex = r.nextInt(4);
    }

    /**
     * Aktualisiert die Animation des Baumes.
     */
    public void update() {
        aniTick++;
        if (aniTick >= 35) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= 4)
                aniIndex = 0;
        }
    }

    /**
     * Gibt den Index der Animation zurück.
     *
     * @return Der Index der Animation.
     */
    public int getAniIndex() {
        return aniIndex;
    }

    /**
     * Setzt den Index der Animation.
     *
     * @param aniIndex Der zu setzende Index der Animation.
     */
    public void setAniIndex(int aniIndex) {
        this.aniIndex = aniIndex;
    }

    /**
     * Gibt die X-Koordinate des Baumes zurück.
     *
     * @return Die X-Koordinate des Baumes.
     */
    public int getX() {
        return x;
    }

    /**
     * Setzt die X-Koordinate des Baumes.
     *
     * @param x Die zu setzende X-Koordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gibt die Y-Koordinate des Baumes zurück.
     *
     * @return Die Y-Koordinate des Baumes.
     */
    public int getY() {
        return y;
    }

    /**
     * Setzt die Y-Koordinate des Baumes.
     *
     * @param y Die zu setzende Y-Koordinate.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gibt den Typ des Baumes zurück.
     *
     * @return Der Typ des Baumes.
     */
    public int getType() {
        return type;
    }

    /**
     * Setzt den Typ des Baumes.
     *
     * @param type Der zu setzende Typ.
     */
    public void setType(int type) {
        this.type = type;
    }
}
