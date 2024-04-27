package objects;

import static utilz.Constants.ANI_SPEED;
import static utilz.Constants.ObjectConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import mainPackage.Game;

/**
 * Eine abstrakte Basisklasse, die ein generisches Spielobjekt repräsentiert.
 * Die Klasse enthält grundlegende Eigenschaften und Methoden, die von spezifischen Spielobjekten
 * erweitert werden können.
 */
public class GameObject {

	 /** Die X-Koordinate des Spielobjekts. */
    protected int x;

    /** Die Y-Koordinate des Spielobjekts. */
    protected int y;

    /** Der Typ des Spielobjekts. */
    protected int objType;

    /** Die Begrenzungsbox des Spielobjekts für Kollisionen. */
    protected Rectangle2D.Float hitbox;

    /** Ein Flag, das angibt, ob eine Animation für das Spielobjekt aktiviert ist. */
    protected boolean doAnimation;

    /** Ein Flag, das angibt, ob das Spielobjekt aktiv ist. */
    protected boolean active = true;

    /** Der Animations-Tick für das Spielobjekt. */
    protected int aniTick;

    /** Der Animations-Index für das Spielobjekt. */
    protected int aniIndex;

    /** Der X-Zeichenoffset für das Zeichnen des Spielobjekts. */
    protected int xDrawOffset;

    /** Der Y-Zeichenoffset für das Zeichnen des Spielobjekts. */
    protected int yDrawOffset;

	public GameObject(int x, int y, int objType) {
		this.x = x;
		this.y = y;
		this.objType = objType;
	}

	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANI_SPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(objType)) {
				aniIndex = 0;
				if (objType == BARREL || objType == BOX) {
					doAnimation = false;
					active = false;
				} else if (objType == CANNON_LEFT || objType == CANNON_RIGHT)
					doAnimation = false;
			}
		}
	}
/**
     * Setzt das Spielobjekt auf seinen Ausgangszustand zurück.
     * Wird von erbenden Klassen verwendet.
     */
	public void reset() {
		aniIndex = 0;
		aniTick = 0;
		active = true;

		if (objType == BARREL || objType == BOX || objType == CANNON_LEFT || objType == CANNON_RIGHT)
			doAnimation = false;
		else
			doAnimation = true;
	}

/**
     * Initialisiert die Begrenzungsbox für das Spielobjekt.
     * Wird von erbenden Klassen verwendet.
     *
     * @param width  Die Breite der Begrenzungsbox.
     * @param height Die Höhe der Begrenzungsbox.
     */
	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
	}

/**
     * Zeichnet die Begrenzungsbox des Spielobjekts für Debugging-Zwecke.
     *
     * @param g          Der Grafikkontext.
     * @param xLvlOffset Der X-Verschiebungsversatz des Levels.
     */
	public void drawHitbox(Graphics g, int xLvlOffset) {
		g.setColor(Color.PINK);
		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
	}

	public int getObjType() {
		return objType;
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setAnimation(boolean doAnimation) {
		this.doAnimation = doAnimation;
	}

	public int getxDrawOffset() {
		return xDrawOffset;
	}

	public int getyDrawOffset() {
		return yDrawOffset;
	}

	public int getAniIndex() {
		return aniIndex;
	}

	public int getAniTick() {
		return aniTick;
	}

}