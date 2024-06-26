package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.IsFloor;

import gamestates.Playing;

/**
 * Die Klasse Crabby repräsentiert einen Krabben-Gegner im Spiel.
 */
public class Crabby extends Enemy {

    /**
     * Konstruktor für ein neues Crabby-Objekt.
     *
     * @param x Die x-Koordinate der Krabbe.
     * @param y Die y-Koordinate der Krabbe.
     */
    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitbox(22, 19);
        initAttackBox(82, 19, 30);
    }

    /**
     * Aktualisiert den Zustand und die Animation der Krabbe basierend auf dem Level-Daten und dem aktuellen Spielzustand.
     *
     * @param lvlData  Die Level-Daten als 2D-Array.
     * @param playing  Der aktuelle Spielzustand.
     */
    public void update(int[][] lvlData, Playing playing) {
        updateBehavior(lvlData, playing);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateBehavior(int[][] lvlData, Playing playing) {
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir) {
            inAirChecks(lvlData, playing);
        } else {
            switch (state) {
                case IDLE:
                    if (IsFloor(hitbox, lvlData))
                        newState(RUNNING);
                    else
                        inAir = true;
                    break;
                case RUNNING:
                    if (canSeePlayer(lvlData, playing.getPlayer())) {
                        turnTowardsPlayer(playing.getPlayer());
                        if (isPlayerCloseForAttack(playing.getPlayer()))
                            newState(ATTACK);
                    }
                    move(lvlData);

                    if (inAir)

                    break;
                case ATTACK:
                    if (aniIndex == 0)
                        attackChecked = false;
                    if (aniIndex == 3 && !attackChecked)
                        checkPlayerHit(attackBox, playing.getPlayer());
                    break;
                case HIT:
                    if (aniIndex <= GetSpriteAmount(enemyType, state) - 2)
                        pushBack(pushBackDir, lvlData, 2f);
                    updatePushBackDrawOffset();
                    break;
            }
        }
    }
}
