package gamestates;

import java.awt.event.MouseEvent;
import audio.AudioPlayer;
import mainPackage.Game;
import ui.MenuButton;

/**
 * Die Klasse State repräsentiert den allgemeinen Zustand des Spiels.
 */
public class State {

    protected Game game;

    /**
     * Konstruktor für die Klasse State.
     *
     * @param game Die Hauptspielinstanz.
     */
    public State(Game game) {
        this.game = game;
    }

    /**
     * Überprüft, ob die Mausposition innerhalb der Grenzen des Menübuttons liegt.
     *
     * @param e  Das Mausereignis.
     * @param mb Der Menübutton.
     * @return True, wenn die Maus innerhalb der Grenzen des Menübuttons liegt, sonst False.
     */
    public boolean isIn(MouseEvent e, MenuButton mb) {
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
            case MENU -> game.getAudioPlayer().playSong(AudioPlayer.MENU_1);
            case PLAYING -> game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
        }

        Gamestate.state = state;
    }

}
