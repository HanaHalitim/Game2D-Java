package ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.Gamestate;
import gamestates.Playing;
import mainPackage.Game;
import utilz.LoadSave;
import static utilz.Constants.UI.URMButtons.*;

/**
 * Die Klasse PauseOverlay repräsentiert das Overlay, das während des Pausenmenüs angezeigt wird.
 * Es enthält Hintergrundbilder, Schaltflächen für das Fortsetzen, Neustarten und Zurückkehren zum Hauptmenü,
 * sowie eine AudioOptions-Komponente für Audioeinstellungen.
 */
public class PauseOverlay {

    private Playing playing;
    private BufferedImage backgroundImg;
    private int bgX, bgY, bgW, bgH;
    private AudioOptions audioOptions;
    private UrmButton menuB, replayB, unpauseB;

    /**
     * Konstruktor für die PauseOverlay-Klasse.
     *
     * @param playing Das aktuelle Playing-Objekt, zu dem das Overlay gehört.
     */
    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        audioOptions = playing.getGame().getAudioOptions();
        createUrmButtons();
    }

    private void createUrmButtons() {
        int menuX = (int) (313 * Game.SCALE);
        int replayX = (int) (387 * Game.SCALE);
        int unpauseX = (int) (462 * Game.SCALE);
        int bY = (int) (325 * Game.SCALE);

        menuB = new UrmButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
        replayB = new UrmButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
        unpauseB = new UrmButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        bgW = (int) (backgroundImg.getWidth() * Game.SCALE);
        bgH = (int) (backgroundImg.getHeight() * Game.SCALE);
        bgX = Game.GAME_WIDTH / 2 - bgW / 2;
        bgY = (int) (25 * Game.SCALE);
    }

    /**
     * Aktualisiert die Komponenten des Pausen-Overlays.
     */
    public void update() {
        menuB.update();
        replayB.update();
        unpauseB.update();
        audioOptions.update();
    }

    /**
     * Zeichnet das Pausen-Overlay auf die Grafikoberfläche.
     *
     * @param g Die Grafikoberfläche, auf die das Overlay gezeichnet wird.
     */
    public void draw(Graphics g) {
        // Hintergrund
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);

        // UrmButtons
        menuB.draw(g);
        replayB.draw(g);
        unpauseB.draw(g);

        // AudioOptions
        audioOptions.draw(g);
    }

    /**
     * Wird aufgerufen, wenn die Maus gezogen wird.
     *
     * @param e Das MouseEvent-Objekt, das Informationen über das Mausereignis enthält.
     */
    public void mouseDragged(MouseEvent e) {
        audioOptions.mouseDragged(e);
    }

    /**
     * Wird aufgerufen, wenn die Maus gedrückt wird.
     *
     * @param e Das MouseEvent-Objekt, das Informationen über das Mausereignis enthält.
     */
    public void mousePressed(MouseEvent e) {
        if (isIn(e, menuB))
            menuB.setMousePressed(true);
        else if (isIn(e, replayB))
            replayB.setMousePressed(true);
        else if (isIn(e, unpauseB))
            unpauseB.setMousePressed(true);
        else
            audioOptions.mousePressed(e);
    }

    /**
     * Wird aufgerufen, wenn die Maus losgelassen wird.
     *
     * @param e Das MouseEvent-Objekt, das Informationen über das Mausereignis enthält.
     */
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, menuB)) {
            if (menuB.isMousePressed()) {
                playing.resetAll();
                playing.setGamestate(Gamestate.MENU);
                playing.unpauseGame();
            }
        } else if (isIn(e, replayB)) {
            if (replayB.isMousePressed()) {
                playing.resetAll();
                playing.unpauseGame();
            }
        } else if (isIn(e, unpauseB)) {
            if (unpauseB.isMousePressed())
                playing.unpauseGame();
        } else
            audioOptions.mouseReleased(e);

        menuB.resetBools();
        replayB.resetBools();
        unpauseB.resetBools();
    }

    /**
     * Wird aufgerufen, wenn die Maus bewegt wird.
     *
     * @param e Das MouseEvent-Objekt, das Informationen über das Mausereignis enthält.
     */
    public void mouseMoved(MouseEvent e) {
        menuB.setMouseOver(false);
        replayB.setMouseOver(false);
        unpauseB.setMouseOver(false);

        if (isIn(e, menuB))
            menuB.setMouseOver(true);
        else if (isIn(e, replayB))
            replayB.setMouseOver(true);
        else if (isIn(e, unpauseB))
            unpauseB.setMouseOver(true);
        else
            audioOptions.mouseMoved(e);
    }

    private boolean isIn(MouseEvent e, PauseButton b) {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
