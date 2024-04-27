package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utilz.LoadSave;
import static utilz.Constants.UI.VolumeButtons.*;

/**
 * Die Klasse VolumeButton repräsentiert eine Schaltfläche zur Steuerung der Lautstärke.
 * Der Benutzer kann die Lautstärke über diese Schaltfläche anpassen.
 */
public class VolumeButton extends PauseButton {

    private BufferedImage[] imgs;
    private BufferedImage slider;
    private int index = 0;
    private boolean mouseOver, mousePressed;
    private int buttonX, minX, maxX;
    private float floatValue = 0f;

    /**
     * Konstruktor für die VolumeButton-Klasse.
     *
     * @param x      Die x-Position der Schaltfläche.
     * @param y      Die y-Position der Schaltfläche.
     * @param width  Die Breite der Schaltfläche.
     * @param height Die Höhe der Schaltfläche.
     */
    public VolumeButton(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_WIDTH, height);
        bounds.x -= VOLUME_WIDTH / 2;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        minX = x + VOLUME_WIDTH / 2;
        maxX = x + width - VOLUME_WIDTH / 2;
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        imgs = new BufferedImage[3];
        for (int i = 0; i < imgs.length; i++)
            imgs[i] = temp.getSubimage(i * VOLUME_DEFAULT_WIDTH, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);

        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
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
        g.drawImage(slider, x, y, width, height, null);
        g.drawImage(imgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);
    }

    /**
     * Ändert die x-Position der Schaltfläche und aktualisiert den Float-Wert entsprechend.
     *
     * @param x Die neue x-Position der Schaltfläche.
     */
    public void changeX(int x) {
        if (x < minX)
            buttonX = minX;
        else if (x > maxX)
            buttonX = maxX;
        else
            buttonX = x;
        updateFloatValue();
        bounds.x = buttonX - VOLUME_WIDTH / 2;
    }

    private void updateFloatValue() {
        float range = maxX - minX;
        float value = buttonX - minX;
        floatValue = value / range;
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

    /**
     * Gibt den Float-Wert zurück, der den aktuellen Wert der Lautstärke darstellt.
     *
     * @return Der Float-Wert, der den aktuellen Wert der Lautstärke darstellt.
     */
    public float getFloatValue() {
        return floatValue;
    }
}
