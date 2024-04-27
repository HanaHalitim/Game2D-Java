package utilz;

import java.awt.geom.Rectangle2D;

import mainPackage.Game;
import objects.Projectile;


/**
 * Diese Klasse enthält Hilfsmethoden für verschiedene Überprüfungen im Spiel.
 */
public class HelpMethods {

	/**
     * Überprüft, ob ein Objekt zu den gegebenen Koordinaten bewegen kann.
     *
     * @param x        Die x-Koordinate des Objekts.
     * @param y        Die y-Koordinate des Objekts.
     * @param width    Die Breite des Objekts.
     * @param height   Die Höhe des Objekts.
     * @param lvlData  Die Level-Daten.
     * @return         {@code true}, wenn das Objekt sich bewegen kann, sonst {@code false}.
     */
	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
		if (!IsSolid(x, y, lvlData))
			if (!IsSolid(x + width, y + height, lvlData))
				if (!IsSolid(x + width, y, lvlData))
					if (!IsSolid(x, y + height, lvlData))
						return true;
		return false;
	}

	/**
     * Überprüft, ob ein Kachel fest ist.
     *
     * @param x        Die x-Koordinate der Kachel.
     * @param y        Die y-Koordinate der Kachel.
     * @param lvlData  Die Level-Daten.
     * @return         {@code true}, wenn die Kachel fest ist, sonst {@code false}.
     */
	private static boolean IsSolid(float x, float y, int[][] lvlData) {
		int maxWidth = lvlData[0].length * Game.TILES_SIZE;
		if (x < 0 || x >= maxWidth)
			return true;
		if (y < 0 || y >= Game.GAME_HEIGHT)
			return true;
		float xIndex = x / Game.TILES_SIZE;
		float yIndex = y / Game.TILES_SIZE;

		return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
	}

	/**
     * Überprüft, ob ein Projektil das Level trifft.
     *
     * @param p       Das Projektil.
     * @param lvlData Die Level-Daten.
     * @return        {@code true}, wenn das Projektil das Level trifft, sonst {@code false}.
     */
	public static boolean IsProjectileHittingLevel(Projectile p, int[][] lvlData) {
		return IsSolid(p.getHitbox().x + p.getHitbox().width / 2, p.getHitbox().y + p.getHitbox().height / 2, lvlData);
	}

    /**
     * Überprüft, ob ein Objekt auf dem Boden ist.
     *
     * @param hitbox  Die Hitbox des Objekts.
     * @param lvlData Die Level-Daten.
     * @return        {@code true}, wenn das Objekt auf dem Boden ist, sonst {@code false}.
     */
	public static boolean IsEntityInWater(Rectangle2D.Float hitbox, int[][] lvlData) {
		// Überprüft nur, ob die Entität das obere Wasser berührt. Kann das untere Wasser nicht erreichen, wenn das obere Wasser nicht berührt wurde.
		if (GetTileValue(hitbox.x, hitbox.y + hitbox.height, lvlData) != 48)
			if (GetTileValue(hitbox.x + hitbox.width, hitbox.y + hitbox.height, lvlData) != 48)
				return false;
		return true;
	}

	/**
     * Ruft den Wert einer Kachel ab.
     *
     * @param xPos    Die x-Koordinate der Kachel.
     * @param yPos    Die y-Koordinate der Kachel.
     * @param lvlData Die Level-Daten.
     * @return        Der Wert der Kachel.
     */
	private static int GetTileValue(float xPos, float yPos, int[][] lvlData) {
		int xCord = (int) (xPos / Game.TILES_SIZE);
		int yCord = (int) (yPos / Game.TILES_SIZE);
		return lvlData[yCord][xCord];
	}

	/**
     * Überprüft, ob eine Kachel fest ist.
     *
     * @param xTile   Die x-Koordinate der Kachel.
     * @param yTile   Die y-Koordinate der Kachel.
     * @param lvlData Die Level-Daten.
     * @return        {@code true}, wenn die Kachel fest ist, sonst {@code false}.
     */
	public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
		int value = lvlData[yTile][xTile];

		switch (value) {
		case 11, 48, 49:
			return false;
		default:
			return true;
		}

	}

	/**
     * Ruft die x-Koordinate einer Entität neben einer Wand ab.
     *
     * @param hitbox  Die Hitbox der Entität.
     * @param xSpeed  Die Geschwindigkeit der Entität.
     * @return        Die x-Koordinate der Entität neben der Wand.
     */
	public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
		if (xSpeed > 0) {
			// Rechts
			int tileXPos = currentTile * Game.TILES_SIZE;
			int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
			return tileXPos + xOffset - 1;
		} else
			// Links
			return currentTile * Game.TILES_SIZE;
	}

	/**
     * Ruft die y-Koordinate einer Entität unter einem Dach oder über einem Boden ab.
     *
     * @param hitbox  Die Hitbox der Entität.
     * @param airSpeed  Die Geschwindigkeit der Entität.
     * @return        Die y-Koordinate der Entität unter dem Dach oder über dem Boden.
     */
	public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
		if (airSpeed > 0) {
			// Fallen - Boden berühren
			int tileYPos = currentTile * Game.TILES_SIZE;
			int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
			return tileYPos + yOffset - 1;
		} else
			// Springen
			return currentTile * Game.TILES_SIZE;

	}

	/**
     * Überprüft, ob eine Entität auf dem Boden ist.
     *
     * @param hitbox  Die Hitbox der Entität.
     * @param lvlData Die Level-Daten.
     * @return        {@code true}, wenn die Entität auf dem Boden ist, sonst {@code false}.
     */
	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
			if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
				return false;
		return true;
	}

	/**
     * Überprüft, ob ein Boden fest ist.
     *
     * @param hitbox  Die Hitbox des Bodens.
     * @param xSpeed  Die Geschwindigkeit des Bodens.
     * @param lvlData Die Level-Daten.
     * @return        {@code true}, wenn der Boden fest ist, sonst {@code false}.
     */
	public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
		if (xSpeed > 0)
			return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
		else
			return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
	}

	/**
     * Überprüft, ob ein Boden fest ist.
     *
     * @param hitbox  Die Hitbox des Bodens.
     * @param lvlData Die Level-Daten.
     * @return        {@code true}, wenn der Boden fest ist, sonst {@code false}.
     */
	public static boolean IsFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
			if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
				return false;
		return true;
	}

	/**
     * Überprüft, ob eine Kanone einen Spieler sehen kann.
     *
     * @param lvlData Die Level-Daten.
     * @param firstHitbox  Die Hitbox des ersten Objekts.
     * @param secondHitbox  Die Hitbox des zweiten Objekts.
     * @param yTile   Die y-Koordinate der Kachel.
     * @return        {@code true}, wenn die Kanone den Spieler sehen kann, sonst {@code false}.
     */
	public static boolean CanCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
		int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
		int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

		if (firstXTile > secondXTile)
			return IsAllTilesClear(secondXTile, firstXTile, yTile, lvlData);
		else
			return IsAllTilesClear(firstXTile, secondXTile, yTile, lvlData);
	}

	/**
     * Überprüft, ob alle Kacheln frei sind.
     *
     * @param xStart  Die Start-x-Koordinate.
     * @param xEnd    Die End-x-Koordinate.
     * @param y       Die y-Koordinate.
     * @param lvlData Die Level-Daten.
     * @return        {@code true}, wenn alle Kacheln frei sind, sonst {@code false}.
     */
	public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData) {
		for (int i = 0; i < xEnd - xStart; i++)
			if (IsTileSolid(xStart + i, y, lvlData))
				return false;
		return true;
	}

	/**
     * Überprüft, ob alle Kacheln begehbar sind.
     *
     * @param xStart  Die Start-x-Koordinate.
     * @param xEnd    Die End-x-Koordinate.
     * @param y       Die y-Koordinate.
     * @param lvlData Die Level-Daten.
     * @return        {@code true}, wenn alle Kacheln begehbar sind, sonst {@code false}.
     */
	public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
		if (IsAllTilesClear(xStart, xEnd, y, lvlData))
			for (int i = 0; i < xEnd - xStart; i++) {
				if (!IsTileSolid(xStart + i, y + 1, lvlData))
					return false;
			}
		return true;
	}

	/**
     * Überprüft, ob die Sicht frei ist.
     *
     * @param lvlData Die Level-Daten.
     * @param enemyBox Die Hitbox des Feindes.
     * @param playerBox Die Hitbox des Spielers.
     * @param yTile Die y-Koordinate der Kachel.
     * @return {@code true}, wenn die Sicht frei ist, sonst {@code false}.
     */
	public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float enemyBox, Rectangle2D.Float playerBox, int yTile) {
		int firstXTile = (int) (enemyBox.x / Game.TILES_SIZE);

		int secondXTile;
		if (IsSolid(playerBox.x, playerBox.y + playerBox.height + 1, lvlData))
			secondXTile = (int) (playerBox.x / Game.TILES_SIZE);
		else
			secondXTile = (int) ((playerBox.x + playerBox.width) / Game.TILES_SIZE);

		if (firstXTile > secondXTile)
			return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
		else
			return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
	}

	/**
     * Überprüft, ob die Sicht frei ist (alte Methode).
     *
     * @param lvlData Die Level-Daten.
     * @param firstHitbox Die Hitbox des ersten Objekts.
     * @param secondHitbox Die Hitbox des zweiten Objekts.
     * @param yTile Die y-Koordinate der Kachel.
     * @return {@code true}, wenn die Sicht frei ist, sonst {@code false}.
     */
	public static boolean IsSightClear_OLD(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
		int firstXTile = (int) (firstHitbox.x / Game.TILES_SIZE);
		int secondXTile = (int) (secondHitbox.x / Game.TILES_SIZE);

		if (firstXTile > secondXTile)
			return IsAllTilesWalkable(secondXTile, firstXTile, yTile, lvlData);
		else
			return IsAllTilesWalkable(firstXTile, secondXTile, yTile, lvlData);
	}
}
