package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

import java.awt.geom.Rectangle2D;

import gamestates.Playing;

import static utilz.Constants.Directions.*;
import static utilz.Constants.*;

import mainPackage.Game;

/**
 * Die abstrakte Klasse Enemy repräsentiert einen generischen Gegner im Spiel.
 * Sie erbt von der Entity-Klasse und stellt gemeinsame Eigenschaften und Methoden für verschiedene Gegnertypen bereit.
 */
public abstract class Enemy extends Entity {
    
    /**
     * Der Typ des Gegners.
     */
    protected int enemyType;

    /**
     * Gibt an, ob dies die erste Aktualisierung des Gegners ist.
     */
    protected boolean firstUpdate = true;

    /**
     * Die Laufrichtung des Gegners (LINKS oder RECHTS).
     */
    protected int walkDir = LEFT;

    /**
     * Die vertikale Position des Gegners in Bezug auf die Kacheln im Level.
     */
    protected int tileY;

    /**
     * Die maximale Distanz, in der der Gegner angreifen kann.
     */
    protected float attackDistance = Game.TILES_SIZE;

    /**
     * Gibt an, ob der Gegner aktiv ist.
     */
    protected boolean active = true;

    /**
     * Gibt an, ob der Angriff bereits überprüft wurde.
     */
    protected boolean attackChecked;

    /**
     * Der horizontale Versatz für den Angriffsbereich.
     */
    protected int attackBoxOffsetX;

    /**
     * Konstruktor für die Enemy-Klasse.
     *
     * @param x         Die X-Position des Gegners.
     * @param y         Die Y-Position des Gegners.
     * @param width     Die Breite des Gegners.
     * @param height    Die Höhe des Gegners.
     * @param enemyType Der Typ des Gegners.
     */
    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;

        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        walkSpeed = Game.SCALE * 0.35f;
    }

    /**
     * Aktualisiert die Position des Angriffsbereichs basierend auf der aktuellen Laufrichtung.
     */
    protected void updateAttackBox() {
        attackBox.x = hitbox.x - attackBoxOffsetX;
        attackBox.y = hitbox.y;
    }

    /**
     * Aktualisiert die Position des Angriffsbereichs, wenn die Laufrichtung umgekehrt ist.
     */
    protected void updateAttackBoxFlip() {
        if (walkDir == RIGHT)
            attackBox.x = hitbox.x + hitbox.width;
        else
            attackBox.x = hitbox.x - attackBoxOffsetX;

        attackBox.y = hitbox.y;
    }

    /**
     * Initialisiert den Angriffsbereich mit der angegebenen Breite, Höhe und Versatz.
     *
     * @param w                 Die Breite des Angriffsbereichs.
     * @param h                 Die Höhe des Angriffsbereichs.
     * @param attackBoxOffsetX Der Versatz des Angriffsbereichs in X-Richtung.
     */
    protected void initAttackBox(int w, int h, int attackBoxOffsetX) {
        attackBox = new Rectangle2D.Float(x, y, (int) (w * Game.SCALE), (int) (h * Game.SCALE));
        this.attackBoxOffsetX = (int) (Game.SCALE * attackBoxOffsetX);
    }

    /**
     * Überprüft, ob der Gegner nicht auf dem Boden steht, und setzt inAir entsprechend.
     *
     * @param lvlData Das Level-Daten-Array.
     */
    protected void firstUpdateCheck(int[][] lvlData) {
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
        firstUpdate = false;
    }

    /**
     * Überprüft und verarbeitet den Zustand, wenn sich der Gegner in der Luft befindet.
     *
     * @param lvlData  Das Level-Daten-Array.
     * @param playing  Die Spielsitzung, die die Objektverwaltung enthält.
     */
    protected void inAirChecks(int[][] lvlData, Playing playing) {
        // Zustandsüberprüfung und Verarbeitung in der Luft
        if (state != HIT && state != DEAD) {
            updateInAir(lvlData);
            playing.getObjectManager().checkSpikesTouched(this);
            if (IsEntityInWater(hitbox, lvlData))
                hurt(maxHealth);
        }
    }

    /**
     * Aktualisiert die vertikale Position des Gegners, wenn er sich in der Luft bewegt.
     *
     * @param lvlData Das Level-Daten-Array.
     */
    protected void updateInAir(int[][] lvlData) {
        // Aktualisierung der vertikalen Position in der Luft
        if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.y += airSpeed;
            airSpeed += GRAVITY;
        } else {
            inAir = false;
            hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
            tileY = (int) (hitbox.y / Game.TILES_SIZE);
        }
    }

    /**
     * Bewegt den Gegner horizontal basierend auf seiner Laufrichtung und Geschwindigkeit.
     *
     * @param lvlData Das Level-Daten-Array.
     */
    protected void move(int[][] lvlData) {
        // Horizontale Bewegung basierend auf der Laufrichtung und Geschwindigkeit
        float xSpeed = (walkDir == LEFT) ? -walkSpeed : walkSpeed;

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            if (IsFloor(hitbox, xSpeed, lvlData)) {
                hitbox.x += xSpeed;
                return;
            }

        changeWalkDir();
    }

    /**
     * Dreht den Gegner in Richtung des Spielers.
     *
     * @param player Der Spieler.
     */
    protected void turnTowardsPlayer(Player player) {
        // Drehung des Gegners in Richtung des Spielers
        walkDir = (player.hitbox.x > hitbox.x) ? RIGHT : LEFT;
    }

    /**
     * Überprüft, ob der Spieler im Sichtbereich des Gegners ist.
     *
     * @param lvlData Das Level-Daten-Array.
     * @param player  Der Spieler.
     * @return true, wenn der Spieler im Sichtbereich ist; andernfalls false.
     */
    protected boolean canSeePlayer(int[][] lvlData, Player player) {
        // Überprüfung, ob der Spieler im Sichtbereich ist
        int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE);
        if (playerTileY == tileY)
            if (isPlayerInRange(player)) {
                if (IsSightClear(lvlData, hitbox, player.hitbox, tileY))
                    return true;
            }
        return false;
    }

    /**
     * Überprüft, ob der Spieler innerhalb der Angriffsreichweite des Gegners ist.
     *
     * @param player Der Spieler.
     * @return true, wenn der Spieler in Reichweite ist; andernfalls false.
     */
    protected boolean isPlayerInRange(Player player) {
        // Überprüfung, ob der Spieler in Reichweite ist
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        return absValue <= attackDistance * 5;
    }

    /**
     * Überprüft, ob der Spieler für den Angriff nahe genug ist, basierend auf dem Gegnertyp.
     *
     * @param player Der Spieler.
     * @return true, wenn der Spieler nah genug ist; andernfalls false.
     */
    protected boolean isPlayerCloseForAttack(Player player) {
        // Überprüfung, ob der Spieler für den Angriff nahe genug ist, basierend auf dem Gegnertyp
        int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
        switch (enemyType) {
            case CRABBY -> {
                return absValue <= attackDistance;
            }
            case SHARK -> {
                return absValue <= attackDistance * 2;
            }
        }
        return false;
    }

    /**
     * Fügt dem Gegner Schaden zu und aktualisiert den Zustand entsprechend.
     *
     * @param amount Die Menge des zugefügten Schadens.
     */
    public void hurt(int amount) {
        // Zufügen von Schaden und Aktualisierung des Zustands
        currentHealth -= amount;
        if (currentHealth <= 0)
            newState(DEAD);
        else {
            newState(HIT);
            if (walkDir == LEFT)
                pushBackDir = RIGHT;
            else
                pushBackDir = LEFT;
            pushBackOffsetDir = UP;
            pushDrawOffset = 0;
        }
    }

    /**
     * Überprüft, ob der Spieler von einem Angriff des Gegners getroffen wurde.
     *
     * @param attackBox Der Angriffsbereich des Gegners.
     * @param player    Der Spieler.
     */
    protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
        // Überprüfung, ob der Spieler von einem Angriff des Gegners getroffen wurde
        if (attackBox.intersects(player.hitbox))
            player.changeHealth(-GetEnemyDmg(enemyType), this);
        else {
            if (enemyType == SHARK)
                return;
        }
        attackChecked = true;
    }

    /**
     * Aktualisiert den Animations-Tick und den Animations-Index basierend auf der aktuellen Animation.
     */
    protected void updateAnimationTick() {
        // Aktualisierung des Animations-Ticks und des Animations-Index
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyType, state)) {
                if (enemyType == CRABBY || enemyType == SHARK) {
                    aniIndex = 0;

                    switch (state) {
                        case ATTACK, HIT -> state = IDLE;
                        case DEAD -> active = false;
                    }
                } else if (enemyType == PINKSTAR) {
                    if (state == ATTACK)
                        aniIndex = 3;
                    else {
                        aniIndex = 0;
                        if (state == HIT) {
                            state = IDLE;
                        } else if (state == DEAD)
                            active = false;
                    }
                }
            }
        }
    }

    /**
     * Ändert die Laufrichtung des Gegners.
     */
    protected void changeWalkDir() {
        // Änderung der Laufrichtung des Gegners
        if (walkDir == LEFT)
            walkDir = RIGHT;
        else
            walkDir = LEFT;
    }
    /**
     * Setzt den Gegner auf die Ausgangsposition zurück.
     */
    public void resetEnemy() {
        // Zurücksetzen der Position, Gesundheit und Zustände des Gegners
        hitbox.x = x;
        hitbox.y = y;
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(IDLE);
        active = true;
        airSpeed = 0;

        pushDrawOffset = 0;
    }

    /**
     * Gibt den Wert für die horizontale Spiegelung des Gegners basierend auf der Laufrichtung zurück.
     *
     * @return Der Wert für die horizontale Spiegelung.
     */
    public int flipX() {
        // Wert für horizontale Spiegelung basierend auf Laufrichtung
        if (walkDir == RIGHT)
            return width;
        else
            return 0;
    }

    /**
     * Gibt den Wert für die horizontale Spiegelung des Gegners basierend auf der Laufrichtung zurück.
     *
     * @return Der Wert für die horizontale Spiegelung.
     */
    public int flipW() {
        // Wert für horizontale Spiegelung basierend auf Laufrichtung
        if (walkDir == RIGHT)
            return -1;
        else
            return 1;
    }

    /**
     * Überprüft, ob der Gegner aktiv ist.
     *
     * @return true, wenn der Gegner aktiv ist; andernfalls false.
     */
    public boolean isActive() {
        // Überprüfung, ob der Gegner aktiv ist
        return active;
    }

    /**
     * Gibt den Wert für den Versatz des Angriffsbereichs zurück.
     *
     * @return Der Wert für den Versatz des Angriffsbereichs.
     */
    public float getPushDrawOffset() {
        // Wert für den Versatz des Angriffsbereichs
        return pushDrawOffset;
    }
}