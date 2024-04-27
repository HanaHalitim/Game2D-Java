package entities;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.*;
import static utilz.Constants.Directions.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gamestates.Playing;
import mainPackage.Game;
import utilz.LoadSave;

/**
 * Die Player-Klasse repräsentiert den Spieler im Spiel.
 */
public class Player extends Entity {

    private BufferedImage[][] animations;
    private boolean moving = false; // Gibt an, ob sich der Charakter bewegt
    private boolean attacking = false; // Gibt an, ob der Charakter angreift
    private boolean left, right, jump; // Bewegungsflags
    private int[][] lvlData; // Level-Daten (Spiellevel)
    private float xDrawOffset = 21 * Game.SCALE; // X-Achsen-Zeichenoffset
    private float yDrawOffset = 4 * Game.SCALE; // Y-Achsen-Zeichenoffset

    // Springen / Schwerkraft
    private float jumpSpeed = -2.25f * Game.SCALE; // Geschwindigkeit für das Springen
    private float fallSpeedAfterCollision = 0.5f * Game.SCALE; // Fallgeschwindigkeit nach der Kollision

    // StatusBarUI
    private BufferedImage statusBarImg; // Bild für die Statusleiste

    // Gesundheitsbalken
    private int statusBarWidth = (int) (192 * Game.SCALE);
    private int statusBarHeight = (int) (58 * Game.SCALE);
    private int statusBarX = (int) (10 * Game.SCALE);
    private int statusBarY = (int) (10 * Game.SCALE);
    private int healthBarWidth = (int) (150 * Game.SCALE);
    private int healthBarHeight = (int) (4 * Game.SCALE);
    private int healthBarXStart = (int) (34 * Game.SCALE);
    private int healthBarYStart = (int) (14 * Game.SCALE);
    private int healthWidth = healthBarWidth;

    // Power-Balken
    private int powerBarWidth = (int) (104 * Game.SCALE);
    private int powerBarHeight = (int) (2 * Game.SCALE);
    private int powerBarXStart = (int) (44 * Game.SCALE);
    private int powerBarYStart = (int) (34 * Game.SCALE);
    private int powerWidth = powerBarWidth;
    private int powerMaxValue = 200; // Maximale Energie
    private int powerValue = powerMaxValue; // Aktueller Energiestand

    // Weitere Variablen
    private int flipX = 0; // X-Achse spiegeln
    private int flipW = 1; // W-Achse spiegeln
    private boolean attackChecked; // Flag für Angriffsüberprüfung
    private Playing playing; // Verweis auf den Spielkontext
    private int tileY = 0; // Kachelposition auf der Y-Achse
    private boolean powerAttackActive; // Gibt an, ob der Power-Angriff aktiv ist
    private int powerAttackTick; // Zähler für den Power-Angriff
    private int powerGrowSpeed = 15; // Geschwindigkeit, mit der die Energie wächst
    private int powerGrowTick; // Zähler für das Wachsen der Energie

    /**
     * Konstruktor für den Spieler.
     *
     * @param x       Die X-Koordinate des Spielers.
     * @param y       Die Y-Koordinate des Spielers.
     * @param width   Die Breite des Spielers.
     * @param height  Die Höhe des Spielers.
     * @param playing Die Instanz des Spielzustands, zu dem der Spieler gehört.
     */
    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        this.state = IDLE; // Spielerstatus auf IDLE (stillstehend) setzen.
        this.maxHealth = 100; // Maximale Gesundheit des Spielers festlegen.
        this.currentHealth = maxHealth; // Aktuelle Gesundheit auf maximale Gesundheit setzen.
        this.walkSpeed = Game.SCALE * 1.0f; // Laufgeschwindigkeit des Spielers festlegen.
        loadAnimations(); // Animationen für den Spieler laden.
        initHitbox(20, 27); // Kollisionshitbox initialisieren.
        initAttackBox(); // Angriffshitbox initialisieren.
    }

    /**
     * Setzt die Position des Spielers auf den angegebenen Spawnpunkt.
     *
     * @param spawn Der Spawnpunkt, auf den der Spieler gesetzt werden soll.
     */
    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x; // Setzt die x-Position der Kollisionshitbox auf die Spielerposition.
        hitbox.y = y; // Setzt die y-Position der Kollisionshitbox auf die Spielerposition.
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (35 * Game.SCALE), (int) (20 * Game.SCALE));
        resetAttackBox(); // Setzt die Angriffshitbox zurück.
    }

    /**
     * Aktualisiert die Gesundheits- und Power-Leiste des Spielers.
     */
	public void update() {
	    // Aktualisiert die Gesundheits- und Power-Leiste des Spielers.
	    updateHealthBar();
	    updatePowerBar();

	    // Überprüft, ob die aktuelle Gesundheit des Spielers 0 erreicht oder unterschritten hat.
	    if (currentHealth <= 0) {
	        // Wenn der Spieler noch nicht als tot markiert ist.
	        if (state != DEAD) {
	            state = DEAD; // Setzt den Zustand auf DEAD (Tod).
	            aniTick = 0;
	            aniIndex = 0;
	            playing.setPlayerDying(true); // Markiert den Spieler als sterbend.
	            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE); // Spielt den Sterbe-Sound ab.

	            // Überprüft, ob der Spieler in der Luft gestorben ist.
	            if (!IsEntityOnFloor(hitbox, lvlData)) {
	                inAir = true;
	                airSpeed = 0;
	            }
	        } else if (aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= ANI_SPEED - 1) {
	            // Wenn die Todesanimation abgeschlossen ist.
	            playing.setGameOver(true); // Markiert das Spiel als beendet.
	            playing.getGame().getAudioPlayer().stopSong(); // Stoppt die Hintergrundmusik.
	            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER); // Spielt den Game-Over-Sound ab.
	        } else {
	            updateAnimationTick();

	            // Fall, wenn der Spieler in der Luft ist.
	            if (inAir)
	                if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
	                    hitbox.y += airSpeed;
	                    airSpeed += GRAVITY;
	                } else
	                    inAir = false;
	        }

	        return;
	    }

	 // Aktualisiert die Angriffshitbox des Spielers.
	    updateAttackBox();

	    // Überprüft den Zustand des Spielers.
	    if (state == HIT) {
	        // Falls der Spieler getroffen wurde.
	        if (aniIndex <= GetSpriteAmount(state) - 3)
	            pushBack(pushBackDir, lvlData, 1.25f); // Führt einen Rückstoß durch.
	        updatePushBackDrawOffset(); // Aktualisiert den Rückstoß-Zeichenoffset.
	    } else {
	        updatePos(); // Aktualisiert die Position des Spielers.
	    }

	    // Überprüft, ob der Spieler sich bewegt.
	    if (moving) {
	        checkPotionTouched(); // Überprüft, ob der Spieler eine Potion berührt hat.
	        checkSpikesTouched(); // Überprüft, ob der Spieler Spikes berührt hat.
	        checkInsideWater(); // Überprüft, ob der Spieler sich unter Wasser befindet.
	        tileY = (int) (hitbox.y / Game.TILES_SIZE); // Aktualisiert die Y-Position des Tiles, auf dem der Spieler steht.
	        
	        // Überprüft, ob der Power-Angriff aktiv ist.
	        if (powerAttackActive) {
	            powerAttackTick++;
	            if (powerAttackTick >= 35) {
	                powerAttackTick = 0;
	                powerAttackActive = false; // Deaktiviert den Power-Angriff nach einer bestimmten Zeitspanne.
	            }
	        }
	    }

	    // Überprüft, ob der Spieler angreift (normal oder Power-Angriff).
	    if (attacking || powerAttackActive)
	        checkAttack(); // Überprüft, ob der Spieler ein Ziel getroffen hat.

	    updateAnimationTick(); // Aktualisiert den Animationsticker.
	    setAnimation(); // Setzt die Animation des Spielers.
	}
	
	
	//method to get the current health TEST
    public int getCurrentHealth() {
        return currentHealth;
    }
	
	
	    // Überprüft, ob der Spieler unter Wasser ist und setzt die Gesundheit auf 0, wenn ja.
    private void checkInsideWater() {
        if (IsEntityInWater(hitbox, playing.getLevelManager().getCurrentLevel().getLevelData()))
            currentHealth = 0;
    }

    // Überprüft, ob der Spieler Spikes berührt hat.
    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this);
    }

    // Überprüft, ob der Spieler eine Potion berührt hat.
    private void checkPotionTouched() {
        playing.checkPotionTouched(hitbox);
    }

    private void checkAttack() {
        // Überprüft, ob ein Angriff überprüft werden soll.
        if (attackChecked || aniIndex != 1)
            return;
        attackChecked = true;

        // Überprüft, ob ein Power-Angriff aktiv ist.
        if (powerAttackActive)
            attackChecked = false;

        // Überprüft Kollisionen mit Feinden und Objekten und spielt den Angriffssound ab.
        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
        playing.getGame().getAudioPlayer().playAttackSound();
    }

    private void setAttackBoxOnRightSide() {
        // Setzt die Angriffshitbox auf der rechten Seite des Spielers.
        attackBox.x = hitbox.x + hitbox.width - (int) (Game.SCALE * 5);
    }

    private void setAttackBoxOnLeftSide() {
        // Setzt die Angriffshitbox auf der linken Seite des Spielers.
        attackBox.x = hitbox.x - hitbox.width - (int) (Game.SCALE * 10);
    }

    private void updateAttackBox() {
        // Aktualisiert die Position der Angriffshitbox basierend auf der Bewegung des Spielers.
        if (right && left) {
            if (flipW == 1) {
                setAttackBoxOnRightSide();
            } else {
                setAttackBoxOnLeftSide();
            }

        } else if (right || (powerAttackActive && flipW == 1))
            setAttackBoxOnRightSide();
        else if (left || (powerAttackActive && flipW == -1))
            setAttackBoxOnLeftSide();

        attackBox.y = hitbox.y + (Game.SCALE * 10);
    }

    private void updateHealthBar() {
        // Aktualisiert die Gesundheitsleiste basierend auf der aktuellen Gesundheit des Spielers.
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    private void updatePowerBar() {
        // Aktualisiert die Power-Leiste basierend auf der Power des Spielers.
        powerWidth = (int) ((powerValue / (float) powerMaxValue) * powerBarWidth);

        powerGrowTick++;
        if (powerGrowTick >= powerGrowSpeed) {
            powerGrowTick = 0;
            changePower(1);
        }
    }

    public void render(Graphics g, int lvlOffset) {
        // Zeichnet den Spieler auf dem Bildschirm.
        g.drawImage(animations[state][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX, (int) (hitbox.y - yDrawOffset + (int) (pushDrawOffset)), width * flipW, height, null);
        // drawHitbox(g, lvlOffset);
        // drawAttackBox(g, lvlOffset);
        drawUI(g);
    }

    private void drawUI(Graphics g) {
        // Zeichnet die UI-Elemente wie Gesundheits- und Power-Leiste.
        // Hintergrund für die UI
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

        // Gesundheitsleiste
        g.setColor(Color.red);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);

        // Power-Leiste
        g.setColor(Color.yellow);
        g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);
    }

    private void updateAnimationTick() {
        // Aktualisiert den Animationsticker und überprüft, ob eine Animation abgeschlossen ist.
        aniTick++;
        if (aniTick >= ANI_SPEED) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(state)) {
                aniIndex = 0;
                attacking = false;
                attackChecked = false;
                if (state == HIT) {
                    newState(IDLE);
                    airSpeed = 0f;
                    if (!IsFloor(hitbox, 0, lvlData))
                        inAir = true;
                }
            }
        }
    }

    private void setAnimation() {
        // Setzt die Animation des Spielers basierend auf seinem Zustand und seinen Aktionen.
        int startAni = state;

        if (state == HIT)
            return;

        if (moving)
            state = RUNNING;
        else
            state = IDLE;

        if (inAir) {
            if (airSpeed < 0)
                state = JUMP;
            else
                state = FALLING;
        }

        if (powerAttackActive) {
            state = ATTACK;
            aniIndex = 1;
            aniTick = 0;
            return;
        }

        if (attacking) {
            state = ATTACK;
            if (startAni != ATTACK) {
                aniIndex = 1;
                aniTick = 0;
                return;
            }
        }
        if (startAni != state)
            resetAniTick();
    }

    private void resetAniTick() {
        // Setzt den Animationszähler zurück.
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {
        // Aktualisiert die Position des Spielers.

        moving = false;

        if (jump)
            jump();

        if (!inAir)
            if (!powerAttackActive)
                if ((!left && !right) || (right && left))
                    return;

        float xSpeed = 0;

        if (left && !right) {
            xSpeed -= walkSpeed;
            flipX = width;
            flipW = -1;
        }
        if (right && !left) {
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }

        if (powerAttackActive) {
            if ((!left && !right) || (left && right)) {
                if (flipW == -1)
                    xSpeed = -walkSpeed;
                else
                    xSpeed = walkSpeed;
            }

            xSpeed *= 3;
        }

        if (!inAir)
            if (!IsEntityOnFloor(hitbox, lvlData))
                inAir = true;

        if (inAir && !powerAttackActive) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }

        } else
            updateXPos(xSpeed);
        moving = true;
    }

    private void jump() {
        // Führt einen Sprung aus.

        if (inAir)
            return;
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        // Setzt den Zustand "in der Luft" zurück.

        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        // Aktualisiert die X-Position des Spielers unter Berücksichtigung der Kollisionen.

        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
            hitbox.x += xSpeed;
        else {
            hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
            if (powerAttackActive) {
                powerAttackActive = false;
                powerAttackTick = 0;
            }
        }
    }

    public void changeHealth(int value) {
        // Ändert die Gesundheit des Spielers.

        if (value < 0) {
            if (state == HIT)
                return;
            else
                newState(HIT);
        }

        currentHealth += value;
        currentHealth = Math.max(Math.min(currentHealth, maxHealth), 0);
    }

    public void changeHealth(int value, Enemy e) {
        // Ändert die Gesundheit des Spielers unter Berücksichtigung eines Gegners.

        if (state == HIT)
            return;
        changeHealth(value);
        pushBackOffsetDir = UP;
        pushDrawOffset = 0;

        if (e.getHitbox().x < hitbox.x)
            pushBackDir = RIGHT;
        else
            pushBackDir = LEFT;
    }

    public void kill() {
        // Setzt die Gesundheit des Spielers auf null.

        currentHealth = 0;
    }

    public void changePower(int value) {
        // Ändert den Power-Wert des Spielers.

        powerValue += value;
        powerValue = Math.max(Math.min(powerValue, powerMaxValue), 0);
    }

    private void loadAnimations() {
        // Lädt die Animationen des Spielers.

        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[7][8];
        for (int j = 0; j < animations.length; j++)
            for (int i = 0; i < animations[j].length; i++)
                animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);

        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    public void loadLvlData(int[][] lvlData) {
        // Lädt die Level-Daten des aktuellen Levels und prüft, ob der Spieler in der Luft ist.

        this.lvlData = lvlData;
        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void resetDirBooleans() {
        // Setzt die Richtungs-Booleans zurück.

        left = false;
        right = false;
    }

    public void setAttacking(boolean attacking) {
        // Setzt den Angriffs-Status des Spielers.

        this.attacking = attacking;
    }

    public boolean isLeft() {
        // Gibt zurück, ob der Spieler nach links bewegt wird.

        return left;
    }

    public void setLeft(boolean left) {
        // Setzt den linken Richtungs-Boolean.

        this.left = left;
    }

    public boolean isRight() {
        // Gibt zurück, ob der Spieler nach rechts bewegt wird.

        return right;
    }

    public void setRight(boolean right) {
        // Setzt den rechten Richtungs-Boolean.

        this.right = right;
    }

    public void setJump(boolean jump) {
        // Setzt den Sprung-Boolean.

        this.jump = jump;
    }

    public void resetAll() {
        // Setzt alle relevanten Zustände und Variablen des Spielers zurück.

        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        airSpeed = 0f;
        state = IDLE;
        currentHealth = maxHealth;
        powerAttackActive = false;
        powerAttackTick = 0;
        powerValue = powerMaxValue;

        hitbox.x = x;
        hitbox.y = y;
        resetAttackBox();

        if (!IsEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    private void resetAttackBox() {
        // Setzt die Angriffsbox des Spielers zurück, basierend auf der aktuellen Blickrichtung.

        if (flipW == 1)
            setAttackBoxOnRightSide();
        else
            setAttackBoxOnLeftSide();
    }

    public int getTileY() {
        // Gibt die aktuelle Y-Position des Tiles zurück, auf dem sich der Spieler befindet.

        return tileY;
    }

    public void powerAttack() {
        // Führt einen Power-Angriff aus, wenn möglich.

        if (powerAttackActive)
            return;
        if (powerValue >= 60) {
            powerAttackActive = true;
            changePower(-60);
        }
    }
}