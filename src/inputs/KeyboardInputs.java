package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gamestates.Gamestate;
import mainPackage.GamePanel;

/**
 * Die Klasse KeyboardInputs implementiert das KeyListener-Interface und reagiert auf Tastatureingaben.
 */
public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;

    /**
     * Konstruktor für die KeyboardInputs-Klasse.
     *
     * @param gamePanel Das GamePanel, das die Tastatureingaben empfängt.
     */
    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void keyReleased(KeyEvent e) {
        // Behandelt das Loslassen einer Taste abhängig vom aktuellen Spielzustand.
        switch (Gamestate.state) {
            case MENU -> gamePanel.getGame().getMenu().keyReleased(e);
            case PLAYING -> gamePanel.getGame().getPlaying().keyReleased(e);
            case CREDITS -> gamePanel.getGame().getCredits().keyReleased(e);
        }
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void keyPressed(KeyEvent e) {
        // Behandelt das Drücken einer Taste abhängig vom aktuellen Spielzustand.
        switch (Gamestate.state) {
            case MENU -> gamePanel.getGame().getMenu().keyPressed(e);
            case PLAYING -> gamePanel.getGame().getPlaying().keyPressed(e);
            case OPTIONS -> gamePanel.getGame().getGameOptions().keyPressed(e);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Nicht in Verwendung
    }
}
