package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import mainPackage.Game;
import ui.MenuButton;
import utilz.LoadSave;

/**
 * Die Klasse Menu repräsentiert den Hauptmenüzustand des Spiels.
 */
public class Menu extends State implements Statemethods {

    /** Array für die Menüschaltflächen */
    private MenuButton[] buttons = new MenuButton[5];

    /** Hintergrundbilder */
    private BufferedImage backgroundImg, backgroundImgPink;

    /** Koordinaten und Größe des Menüs */
    private int menuX, menuY, menuWidth, menuHeight;

    /**
     * Konstruktor für die Menu-Klasse.
     *
     * @param game Die Hauptspielinstanz.
     */
    public Menu(Game game) {
        super(game);
        loadButtons(); // Menüschaltflächen initialisieren
        loadBackground(); // Hintergrundbilder laden
        backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
    }

    /**
     * Lädt das Hintergrundbild für das Menü.
     */
    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (25 * Game.SCALE);
    }

    /**
     * Initialisiert die Menüschaltflächen.
     */
    private void loadButtons() {
        buttons[0] = new MenuButton(Game.GAME_WIDTH / 2, (int) (130 * Game.SCALE), 0, Gamestate.PLAYING);
        buttons[1] = new MenuButton(Game.GAME_WIDTH / 2, (int) (180 * Game.SCALE), 1, Gamestate.OPTIONS);
        buttons[2] = new MenuButton(Game.GAME_WIDTH / 2, (int) (230 * Game.SCALE), 3, Gamestate.CREDITS);
        buttons[3] = new MenuButton(Game.GAME_WIDTH / 2, (int) (280 * Game.SCALE), 4, Gamestate.USER);
        buttons[4] = new MenuButton(Game.GAME_WIDTH / 2, (int) (330 * Game.SCALE), 2, Gamestate.QUIT);
    }

    /**
     * Aktualisiert den Menüzustand.
     */
    @Override
    public void update() {
        for (MenuButton mb : buttons)
            mb.update();
    }

    /**
     * Zeichnet das Menü auf den Bildschirm.
     *
     * @param g Die Grafik-Kontext.
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImgPink, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);

        for (MenuButton mb : buttons)
            mb.draw(g);
    }

    /**
     * Reaktion auf die Maustaste "Gedrückt"-Ereignis.
     *
     * @param e Das MouseEvent-Objekt.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                mb.setMousePressed(true);
            }
        }
    }

    /**
     * Reaktion auf die Maustaste "Losgelassen"-Ereignis.
     *
     * @param e Das MouseEvent-Objekt.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                if (mb.isMousePressed())
                    mb.applyGamestate();
                if (mb.getState() == Gamestate.PLAYING)
                    game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex());
                break;
            }
        }
        resetButtons();
    }

    /**
     * Setzt die Zustände der Menüschaltflächen zurück.
     */
    private void resetButtons() {
        for (MenuButton mb : buttons)
            mb.resetBools();
    }

    /**
     * Reaktion auf das Maus-Bewegungs-Ereignis.
     *
     * @param e Das MouseEvent-Objekt.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : buttons)
            mb.setMouseOver(false);

        for (MenuButton mb : buttons)
            if (isIn(e, mb)) {
                mb.setMouseOver(true);
                break;
            }
    }

    /**
     * Reaktion auf die Tasten "Gedrückt"-Ereignis.
     *
     * @param e Das KeyEvent-Objekt.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        // Keine Aktion erforderlich
    }

    /**
     * Reaktion auf das Maus-Klick-Ereignis.
     *
     * @param e Das MouseEvent-Objekt.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // Keine Aktion erforderlich
    }

    /**
     * Reaktion auf die Tasten "Losgelassen"-Ereignis.
     *
     * @param e Das KeyEvent-Objekt.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        // Keine Aktion erforderlich
    }
}
