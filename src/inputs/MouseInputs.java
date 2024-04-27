package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gamestates.Gamestate;
import mainPackage.GamePanel;

/**
 * Die Klasse MouseInputs implementiert das MouseListener- und MouseMotionListener-Interface und reagiert auf Mausereignisse.
 */
public class MouseInputs implements MouseListener, MouseMotionListener {

    private GamePanel gamePanel;

    /**
     * Konstruktor für die MouseInputs-Klasse.
     *
     * @param gamePanel Das GamePanel, das die Mausereignisse empfängt.
     */
    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void mouseDragged(MouseEvent e) {
        // Behandelt das Ziehen der Maus abhängig vom aktuellen Spielzustand.
        switch (Gamestate.state) {
            case PLAYING -> gamePanel.getGame().getPlaying().mouseDragged(e);
            case OPTIONS -> gamePanel.getGame().getGameOptions().mouseDragged(e);
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void mouseMoved(MouseEvent e) {
        // Behandelt die Bewegung der Maus abhängig vom aktuellen Spielzustand.
        switch (Gamestate.state) {
            case MENU -> gamePanel.getGame().getMenu().mouseMoved(e);
            case USER -> gamePanel.getGame().getChangeUser().mouseMoved(e);
            case PLAYING -> gamePanel.getGame().getPlaying().mouseMoved(e);
            case OPTIONS -> gamePanel.getGame().getGameOptions().mouseMoved(e);
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void mouseClicked(MouseEvent e) {
        // Behandelt einen Mausklick abhängig vom aktuellen Spielzustand.
        switch (Gamestate.state) {
            case PLAYING -> gamePanel.getGame().getPlaying().mouseClicked(e);
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void mousePressed(MouseEvent e) {
        // Behandelt das Drücken der Maustaste abhängig vom aktuellen Spielzustand.
        switch (Gamestate.state) {
            case MENU -> gamePanel.getGame().getMenu().mousePressed(e);
            case USER -> gamePanel.getGame().getChangeUser().mousePressed(e);
            case PLAYING -> gamePanel.getGame().getPlaying().mousePressed(e);
            case OPTIONS -> gamePanel.getGame().getGameOptions().mousePressed(e);
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void mouseReleased(MouseEvent e) {
        // Behandelt das Loslassen der Maustaste abhängig vom aktuellen Spielzustand.
        switch (Gamestate.state) {
            case MENU -> gamePanel.getGame().getMenu().mouseReleased(e);
            case USER -> gamePanel.getGame().getChangeUser().mouseReleased(e);
            case PLAYING -> gamePanel.getGame().getPlaying().mouseReleased(e);
            case OPTIONS -> gamePanel.getGame().getGameOptions().mouseReleased(e);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Nicht in Verwendung
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Nicht in Verwendung
    }
}
