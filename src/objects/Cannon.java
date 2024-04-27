package objects;

import mainPackage.Game;

/**
 * Eine Klasse, die eine Kanone im Spiel repräsentiert.
 * Kanonen sind Objekte mit einer bestimmten Position und Größe.
 */
public class Cannon extends GameObject {

    private int tileY;

    /**
     * Konstruktor für die {@code Cannon}-Klasse.
     *
     * @param x       Die X-Koordinate der Kanone.
     * @param y       Die Y-Koordinate der Kanone.
     * @param objType Der Typ der Kanone.
     */
    public Cannon(int x, int y, int objType) {
        super(x, y, objType);
        tileY = y / Game.TILES_SIZE;
        initHitbox(40, 26);
        // hitbox.x -= (int) (1 * Game.SCALE);
        hitbox.y += (int) (6 * Game.SCALE);
    }

    /**
     * Aktualisiert die Kanone. Führt die Animationstaktaktualisierung durch, wenn die Animation aktiviert ist.
     */
    public void update() {
        if (doAnimation)
            updateAnimationTick();
    }

    /**
     * Gibt die Zeile (Y-Koordinate im Raster) zurück, in der sich die Kanone befindet.
     *
     * @return Die Y-Koordinate der Zeile, in der sich die Kanone befindet.
     */
    public int getTileY() {
        return tileY;
    }

}
