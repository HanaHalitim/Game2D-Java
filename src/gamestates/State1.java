package gamestates;

import java.awt.event.MouseEvent;
import audio.AudioPlayer;
import mainPackage.Game;
import ui.UserButton;

/**
 * Die Klasse State1 repräsentiert einen alternativen Spielzustand.
 */
public class State1 {

    protected Game game;

    /**
     * Konstruktor für die Klasse State1.
     *
     * @param game Die Hauptspielinstanz.
     */
    public State1(Game game) {
        this.game = game;
    }

    /**
     * Überprüft, ob die Mausposition innerhalb der Grenzen des Benutzerbuttons liegt.
     *
     * @param e  Das Mausereignis.
     * @param mb Der Benutzerbutton.
     * @return True, wenn die Maus innerhalb der Grenzen des Benutzerbuttons liegt, sonst False.
     */
    public boolean isIn(MouseEvent e, UserButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }

    /**
     * Gibt die Hauptspielinstanz zurück.
     *
     * @return Die Hauptspielinstanz.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Setzt den Spielzustand basierend auf dem übergebenen Gamestate-Enum.
     *
     * @param state Der gewünschte Spielzustand.
     */
    @SuppressWarnings("incomplete-switch")
    public void setGamestate(Gamestate state) {
        switch (state) {
            case USER -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
        }

        Gamestate.state = state;
    }

}
