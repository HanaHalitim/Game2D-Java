package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utilz.LoadSave;
import static utilz.Constants.UI.URMButtons.*;

/**
 * Die Klasse UrmButton repräsentiert eine Schaltfläche im Pausenmenü mit Urm-Grafiken.
 * Es enthält Bilder für verschiedene Zustände der Schaltfläche, einschließlich Mausüber, Mausklick und Standard.
 */
public class UrmButton extends PauseButton {

    private BufferedImage[] imgs;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed;

    /**
     * Konstruktor für die UrmButton-Klasse.
     *
     * @param x        Die x-Position der Schaltfläche.
     * @param y        Die y-Position der Schaltfläche.
     * @param width    Die Breite der Schaltfläche.
     * @param height   Die Höhe der Schaltfläche.
     * @param rowIndex Der Index der Zeile für die Schaltflächenbilder.
     */
    public UrmButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * URM_DEFAULT_SIZE, rowIndex * URM_DEFAULT_SIZE, URM_DEFAULT_SIZE, URM_DEFAULT_SIZE);
    }

    /**
     * Aktualisiert den Zustand der Schaltfläche basierend auf Mausereignissen.
     */
    public void update() {
        index = 0;
        if (mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;
    }

    /**
     * Zeichnet die Schaltfläche auf die Grafikoberfläche.
     *
     * @param g Die Grafikoberfläche, auf die die Schaltfläche gezeichnet wird.
     */
    public void draw(Graphics g) {
        g.drawImage(imgs[index], x, y, URM_SIZE, URM_SIZE, null);
    }

    /**
     * Setzt die Zustände der Mausereignisse zurück.
     */
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    /**
     * Überprüft, ob die Maus über der Schaltfläche liegt.
     *
     * @return true, wenn die Maus über der Schaltfläche liegt, andernfalls false.
     */
    public boolean isMouseOver() {
        return mouseOver;
    }

    /**
     * Setzt den Zustand für das Überfahren der Maus über der Schaltfläche.
     *
     * @param mouseOver Der Zustand für das Überfahren der Maus über der Schaltfläche.
     */
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    /**
     * Überprüft, ob die Maus auf die Schaltfläche geklickt wurde.
     *
     * @return true, wenn die Maus auf die Schaltfläche geklickt wurde, andernfalls false.
     */
    public boolean isMousePressed() {
        return mousePressed;
    }

    /**
     * Setzt den Zustand für das Klicken auf die Schaltfläche.
     *
     * @param mousePressed Der Zustand für das Klicken auf die Schaltfläche.
     */
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
}
