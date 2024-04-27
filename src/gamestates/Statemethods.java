package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Das Interface Statemethods definiert die Methoden, die von verschiedenen Spielzustandsklassen implementiert werden müssen.
 */
public interface Statemethods {

    /**
     * Aktualisiert den Spielzustand.
     */
    public void update();

    /**
     * Zeichnet den Spielzustand auf die Grafikoberfläche.
     *
     * @param g Die Grafikoberfläche.
     */
    public void draw(Graphics g);

    /**
     * Wird aufgerufen, wenn eine Maustaste geklickt wird.
     *
     * @param e Das Mausereignis.
     */
    public void mouseClicked(MouseEvent e);

    /**
     * Wird aufgerufen, wenn eine Maustaste gedrückt wird.
     *
     * @param e Das Mausereignis.
     */
    public void mousePressed(MouseEvent e);

    /**
     * Wird aufgerufen, wenn eine Maustaste losgelassen wird.
     *
     * @param e Das Mausereignis.
     */
    public void mouseReleased(MouseEvent e);

    /**
     * Wird aufgerufen, wenn die Maus bewegt wird.
     *
     * @param e Das Mausereignis.
     */
    public void mouseMoved(MouseEvent e);

    /**
     * Wird aufgerufen, wenn eine Taste auf der Tastatur gedrückt wird.
     *
     * @param e Das Tastaturereignis.
     */
    public void keyPressed(KeyEvent e);

    /**
     * Wird aufgerufen, wenn eine Taste auf der Tastatur losgelassen wird.
     *
     * @param e Das Tastaturereignis.
     */
    public void keyReleased(KeyEvent e);
}
