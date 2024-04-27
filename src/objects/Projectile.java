package objects;

import java.awt.geom.Rectangle2D;

import mainPackage.Game;

import static utilz.Constants.Projectiles.*;

/**
 * Die Klasse Projectile repräsentiert ein Projektil im Spiel.
 */
public class Projectile {
    private Rectangle2D.Float hitbox;
    private int dir;
    private boolean active = true;

    /**
     * Konstruktor für die Projectile-Klasse.
     *
     * @param x   Die X-Koordinate des Projektils.
     * @param y   Die Y-Koordinate des Projektils.
     * @param dir Die Richtung, in die sich das Projektil bewegt (-1 für links, 1 für rechts).
     */
    public Projectile(int x, int y, int dir) {
        int xOffset = (int) (-3 * Game.SCALE);
        int yOffset = (int) (5 * Game.SCALE);

        if (dir == 1)
            xOffset = (int) (29 * Game.SCALE);

        hitbox = new Rectangle2D.Float(x + xOffset, y + yOffset, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
        this.dir = dir;
    }

    /**
     * Aktualisiert die Position des Projektils basierend auf der Bewegungsrichtung und der Geschwindigkeit.
     */
    public void updatePos() {
        hitbox.x += dir * SPEED;
    }

    /**
     * Setzt die Position des Projektils auf die angegebenen Koordinaten.
     *
     * @param x Die neue X-Koordinate.
     * @param y Die neue Y-Koordinate.
     */
    public void setPos(int x, int y) {
        hitbox.x = x;
        hitbox.y = y;
    }

    /**
     * Gibt die Hitbox des Projektils zurück.
     *
     * @return Die Hitbox des Projektils.
     */
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    /**
     * Legt fest, ob das Projektil aktiv ist oder nicht.
     *
     * @param active Der Zustand, ob das Projektil aktiv ist oder nicht.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Überprüft, ob das Projektil aktiv ist oder nicht.
     *
     * @return true, wenn das Projektil aktiv ist, sonst false.
     */
    public boolean isActive() {
        return active;
    }
}
