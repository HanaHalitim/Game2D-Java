package objects;

import mainPackage.Game;

/**
 * Die Klasse Spike repräsentiert einen Stachel im Spiel.
 */
public class Spike extends GameObject {

    /**
     * Konstruktor für die Spike-Klasse.
     *
     * @param x       Die X-Koordinate des Stachels.
     * @param y       Die Y-Koordinate des Stachels.
     * @param objType Der Objekttyp des Stachels.
     */
    public Spike(int x, int y, int objType) {
        super(x, y, objType);
        initHitbox(32, 16);
        xDrawOffset = 0;
        yDrawOffset = (int) (Game.SCALE * 16);
        hitbox.y += yDrawOffset;
    }
}
