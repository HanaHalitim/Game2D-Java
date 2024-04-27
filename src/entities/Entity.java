package entities;

import static utilz.Constants.Directions.DOWN;
import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.Directions.UP;
import static utilz.HelpMethods.CanMoveHere;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import mainPackage.Game;

/**
 * Die abstrakte Klasse Entity repräsentiert eine allgemeine Entität im Spiel.
 */
public abstract class Entity {

    protected float x, y;
    protected int width, height;
    protected Rectangle2D.Float hitbox;
    protected int aniTick, aniIndex;
    protected int state;
    protected float airSpeed;
    protected boolean inAir = false;
    protected int maxHealth;
    protected int currentHealth;
    protected Rectangle2D.Float attackBox;
    protected float walkSpeed;

    protected int pushBackDir;
    protected float pushDrawOffset;
    protected int pushBackOffsetDir = UP;

    /**
     * Konstruktor für die Entity-Klasse.
     *
     * @param x      Die X-Koordinate der Entität.
     * @param y      Die Y-Koordinate der Entität.
     * @param width  Die Breite der Entität.
     * @param height Die Höhe der Entität.
     */
    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Aktualisiert den Zeichenversatz für den Rückschlageffekt.
     */
    protected void updatePushBackDrawOffset() {
        float speed = 0.95f;
        float limit = -30f;

        if (pushBackOffsetDir == UP) {
            pushDrawOffset -= speed;
            if (pushDrawOffset <= limit)
                pushBackOffsetDir = DOWN;
        } else {
            pushDrawOffset += speed;
            if (pushDrawOffset >= 0)
                pushDrawOffset = 0;
        }
    }

    /**
     * Führt einen Rückschlag für die Entität durch.
     *
     * @param pushBackDir Die Richtung des Rückschlags (LEFT oder RIGHT).
     * @param lvlData     Die Daten des aktuellen Levels.
     * @param speedMulti  Der Geschwindigkeitsmultiplikator für den Rückschlag.
     */
    protected void pushBack(int pushBackDir, int[][] lvlData, float speedMulti) {
        float xSpeed = 0;
        if (pushBackDir == LEFT)
            xSpeed = -walkSpeed;
        else
            xSpeed = walkSpeed;

        if (CanMoveHere(hitbox.x + xSpeed * speedMulti, hitbox.y, hitbox.width, hitbox.height, lvlData))
            hitbox.x += xSpeed * speedMulti;
    }

    /**
     * Zeichnet das Angriffsquadrat der Entität.
     *
     * @param g          Das Graphics-Objekt zum Zeichnen.
     * @param xLvlOffset Der Versatz in der horizontalen Richtung des Levels.
     */
    protected void drawAttackBox(Graphics g, int xLvlOffset) {
        g.setColor(Color.red);
        g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
    }

    /**
     * Zeichnet das Trefferquadrat der Entität.
     *
     * @param g          Das Graphics-Objekt zum Zeichnen.
     * @param xLvlOffset Der Versatz in der horizontalen Richtung des Levels.
     */
    protected void drawHitbox(Graphics g, int xLvlOffset) {
        g.setColor(Color.PINK);
        g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }

    /**
     * Initialisiert das Trefferquadrat der Entität.
     *
     * @param width  Die Breite des Trefferquadrats.
     * @param height Die Höhe des Trefferquadrats.
     */
    protected void initHitbox(int width, int height) {
        hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
    }

    /**
     * Gibt das Trefferquadrat der Entität zurück.
     *
     * @return Das Trefferquadrat der Entität.
     */
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    /**
     * Gibt den aktuellen Zustand der Entität zurück.
     *
     * @return Der aktuelle Zustand der Entität.
     */
    public int getState() {
        return state;
    }

    /**
     * Gibt den Animationsindex der Entität zurück.
     *
     * @return Der Animationsindex der Entität.
     */
    public int getAniIndex() {
        return aniIndex;
    }

    /**
     * Setzt einen neuen Zustand für die Entität.
     *
     * @param state Der neue Zustand.
     */
    protected void newState(int state) {
        this.state = state;
        aniTick = 0;
        aniIndex = 0;
    }
}
