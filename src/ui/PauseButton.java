package ui;

import java.awt.Rectangle;

/**
 * Die Klasse PauseButton repräsentiert eine generische Schaltfläche im Pausemenü.
 * Es enthält Positionsinformationen und die Begrenzungen (Bounds) der Schaltfläche.
 */
public class PauseButton {

    protected int x, y, width, height;
    protected Rectangle bounds;

    /**
     * Konstruktor für die PauseButton-Klasse.
     *
     * @param x      Die x-Position der Schaltfläche.
     * @param y      Die y-Position der Schaltfläche.
     * @param width  Die Breite der Schaltfläche.
     * @param height Die Höhe der Schaltfläche.
     */
    public PauseButton(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        createBounds();
    }

    private void createBounds() {
        bounds = new Rectangle(x, y, width, height);
    }

    /**
     * Gibt die x-Position der Schaltfläche zurück.
     *
     * @return Die x-Position der Schaltfläche.
     */
    public int getX() {
        return x;
    }

    /**
     * Setzt die x-Position der Schaltfläche.
     *
     * @param x Die neue x-Position der Schaltfläche.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Gibt die y-Position der Schaltfläche zurück.
     *
     * @return Die y-Position der Schaltfläche.
     */
    public int getY() {
        return y;
    }

    /**
     * Setzt die y-Position der Schaltfläche.
     *
     * @param y Die neue y-Position der Schaltfläche.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gibt die Breite der Schaltfläche zurück.
     *
     * @return Die Breite der Schaltfläche.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Setzt die Breite der Schaltfläche.
     *
     * @param width Die neue Breite der Schaltfläche.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gibt die Höhe der Schaltfläche zurück.
     *
     * @return Die Höhe der Schaltfläche.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Setzt die Höhe der Schaltfläche.
     *
     * @param height Die neue Höhe der Schaltfläche.
     */
    public void setHeight(int height) {
        this.height = height;
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
     * Setzt die Begrenzungen (Bounds) der Schaltfläche.
     *
     * @param bounds Die neuen Begrenzungen der Schaltfläche als Rectangle.
     */
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
}
