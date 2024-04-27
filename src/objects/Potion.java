package objects;

import mainPackage.Game;

/**
 * Die Klasse Potion repräsentiert einen Trank im Spiel.
 * Diese Klasse erweitert die GameObject-Klasse.
 */
public class Potion extends GameObject {

    private float hoverOffset;
    private int maxHoverOffset, hoverDir = 1;

    /**
     * Konstruktor für die Potion-Klasse.
     *
     * @param x       Die X-Koordinate des Tranks.
     * @param y       Die Y-Koordinate des Tranks.
     * @param objType Der Typ des Tranks.
     */
    public Potion(int x, int y, int objType) {
        super(x, y, objType);
        doAnimation = true;

        initHitbox(7, 14);

        xDrawOffset = (int) (3 * Game.SCALE);
        yDrawOffset = (int) (2 * Game.SCALE);

        maxHoverOffset = (int) (10 * Game.SCALE);
    }

    /**
     * Aktualisiert den Trank, einschließlich der Animation und der Schwebebewegung.
     */
    public void update() {
        updateAnimationTick();
        updateHover();
    }

    private void updateHover() {
        hoverOffset += (0.075f * Game.SCALE * hoverDir);

        if (hoverOffset >= maxHoverOffset)
            hoverDir = -1;
        else if (hoverOffset < 0)
            hoverDir = 1;

        hitbox.y = y + hoverOffset;
    }
}
