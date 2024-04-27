package objects;

import static utilz.Constants.ObjectConstants.*;

import mainPackage.Game;

/**
 * Eine Klasse, die ein Spielobjekt-Container im Spiel repräsentiert.
 * Container sind Objekte mit einer bestimmten Position, Größe und Hitbox.
 */
public class GameContainer extends GameObject {

    /**
     * Konstruktor für die {@code GameContainer}-Klasse.
     *
     * @param x       Die X-Koordinate des Containers.
     * @param y       Die Y-Koordinate des Containers.
     * @param objType Der Typ des Containers.
     */
    public GameContainer(int x, int y, int objType) {
        super(x, y, objType);
        createHitbox();
    }

    /**
     * Erstellt die Hitbox für den Container basierend auf seinem Typ.
     */
    private void createHitbox() {
        if (objType == BOX) {
            initHitbox(25, 18);

            xDrawOffset = (int) (7 * Game.SCALE);
            yDrawOffset = (int) (12 * Game.SCALE);

        } else {
            initHitbox(23, 25);
            xDrawOffset = (int) (8 * Game.SCALE);
            yDrawOffset = (int) (5 * Game.SCALE);
        }

        hitbox.y += yDrawOffset + (int) (Game.SCALE * 2);
        hitbox.x += xDrawOffset / 2;
    }

    /**
     * Aktualisiert den Container. Führt die Animationstaktaktualisierung durch, wenn die Animation aktiviert ist.
     */
    public void update() {
        if (doAnimation)
            updateAnimationTick();
    }
}
