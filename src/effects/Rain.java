package effects;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import mainPackage.Game;
import utilz.LoadSave;

/**
 * Die Klasse Rain repräsentiert einen Regeneffekt im Spiel.
 */
public class Rain {

    private Point2D.Float[] drops;
    private Random rand;
    private float rainSpeed = 1.25f;
    private BufferedImage rainParticle;

    /**
     * Konstruktor für ein neues Rain-Objekt. Initialisiert den Zufallszahlengenerator, die Regentropfen-Array und das Bild für die Regentropfen.
     */
    public Rain() {
        rand = new Random();
        drops = new Point2D.Float[1000];
        rainParticle = LoadSave.GetSpriteAtlas(LoadSave.RAIN_PARTICLE);
        initDrops();
    }

    private void initDrops() {
        for (int i = 0; i < drops.length; i++)
            drops[i] = getRndPos();
    }

    private Point2D.Float getRndPos() {
        return new Point2D.Float((int) getNewX(0), rand.nextInt(Game.GAME_HEIGHT));
    }

    /**
     * Aktualisiert die Position der Regentropfen basierend auf der Regengeschwindigkeit und setzt Tropfen zurück, wenn sie den unteren Bildschirmrand erreichen.
     *
     * @param xLvlOffset Der horizontale Offset des Levels.
     */
    public void update(int xLvlOffset) {
        for (Point2D.Float p : drops) {
            p.y += rainSpeed;
            if (p.y >= Game.GAME_HEIGHT) {
                p.y = -20;
                p.x = getNewX(xLvlOffset);
            }
        }
    }

    private float getNewX(int xLvlOffset) {
        float value = (-Game.GAME_WIDTH) + rand.nextInt((int) (Game.GAME_WIDTH * 3f)) + xLvlOffset;
        return value;
    }

    /**
     * Zeichnet die Regentropfen auf die Grafik.
     *
     * @param g           Die Grafik, auf die gezeichnet werden soll.
     * @param xLvlOffset  Der horizontale Offset des Levels.
     */
    public void draw(Graphics g, int xLvlOffset) {
        for (Point2D.Float p : drops)
            g.drawImage(rainParticle, (int) p.getX() - xLvlOffset, (int) p.getY(), 1, 3, null);
    }

}
