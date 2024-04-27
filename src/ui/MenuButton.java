package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import utilz.LoadSave;
import static utilz.Constants.UI.Buttons.*;

/**
 * Die Klasse MenuButton repräsentiert eine Schaltfläche im Hauptmenü.
 * Es enthält Positionsinformationen, Bilder für verschiedene Zustände und behandelt Mausinteraktionen.
 */
public class MenuButton {
    private int xPos, yPos, rowIndex, index;
    private int xOffsetCenter = B_WIDTH / 2;
    private Gamestate state;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;

    /**
     * Konstruktor für die MenuButton-Klasse.
     *
     * @param xPos      Die x-Position der Schaltfläche.
     * @param yPos      Die y-Position der Schaltfläche.
     * @param rowIndex  Der Index der Zeile, in der sich die Schaltfläche im Sprite-Atlas befindet.
     * @param state     Der Gamestate, der durch das Klicken auf die Schaltfläche ausgelöst wird.
     */
    public MenuButton(int xPos, int yPos, int rowIndex, Gamestate state) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImgs();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
    }

    private void loadImgs() {
        imgs = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
    }

    /**
     * Zeichnet die Schaltfläche auf den Bildschirm.
     *
     * @param g Die Graphics-Instanz zum Zeichnen.
     */
    public void draw(Graphics g) {
        g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
    }

    /**
     * Aktualisiert den Zustand der Schaltfläche basierend auf Mausinteraktionen.
     */
    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;
    }

    /**
     * Überprüft, ob die Maus über der Schaltfläche ist.
     *
     * @return true, wenn die Maus über der Schaltfläche ist, sonst false.
     */
    public boolean isMouseOver() {
        return mouseOver;
    }

    /**
     * Setzt den Zustand, ob die Maus über der Schaltfläche ist.
     *
     * @param mouseOver true, wenn die Maus über der Schaltfläche ist, sonst false.
     */
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    /**
     * Überprüft, ob die Maus auf die Schaltfläche gedrückt wurde.
     *
     * @return true, wenn die Maus auf die Schaltfläche gedrückt wurde, sonst false.
     */
    public boolean isMousePressed() {
        return mousePressed;
    }

    /**
     * Setzt den Zustand, ob die Maus auf die Schaltfläche gedrückt wurde.
     *
     * @param mousePressed true, wenn die Maus auf die Schaltfläche gedrückt wurde, sonst false.
     */
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    /**
     * Gibt die Begrenzungen (Bounds) der Schaltfläche zurück.
     *
     * @return Die Begrenzungen der Schaltfläche als Rectangle.
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * Setzt den aktuellen Gamestate entsprechend der Schaltfläche.
     */
    public void applyGamestate() {
        Gamestate.state = state;
    }

    /**
     * Setzt die Mausinteraktions-Booleans zurück.
     */
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    /**
     * Gibt den Gamestate der Schaltfläche zurück.
     *
     * @return Der Gamestate der Schaltfläche.
     */
    public Gamestate getState() {
        return state;
    }
}
