package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import mainPackage.Game;
import ui.AudioOptions;
import ui.PauseButton;
import ui.UrmButton;
import utilz.LoadSave;
import static utilz.Constants.UI.URMButtons.*;

/**
 * Die GameOptions-Klasse repräsentiert den Spieloptionen-Zustand im Spiel.
 */

public class GameOptions extends State implements Statemethods {

	private AudioOptions audioOptions;
	private BufferedImage backgroundImg, optionsBackgroundImg;
	private int bgX, bgY, bgW, bgH;
	private UrmButton menuB;

	public GameOptions(Game game) {
		super(game);
		loadImgs();
		loadButton();
		audioOptions = game.getAudioOptions();
	}

	/**
	 * Initialisiert den UrmButton für das Menü in den Game Options.
	 * Die Position und Größe des Buttons werden basierend auf der Skalierung des Spiels gesetzt.
	 */
	private void loadButton() {
		int menuX = (int) (387 * Game.SCALE);
		int menuY = (int) (325 * Game.SCALE);

		menuB = new UrmButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);
	}

	/**
	 * Lädt die Hintergrundbilder für den Optionsbildschirm.
	 */
	private void loadImgs() {
	    backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
	    optionsBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_MENU);

	    bgW = (int) (optionsBackgroundImg.getWidth() * Game.SCALE);
	    bgH = (int) (optionsBackgroundImg.getHeight() * Game.SCALE);
	    bgX = Game.GAME_WIDTH / 2 - bgW / 2;
	    bgY = (int) (33 * Game.SCALE);
	}

	/**
	 * Aktualisiert den Optionsbildschirm.
	 */
	@Override
	public void update() {
	    menuB.update();
	    audioOptions.update();
	}

	/**
	 * Zeichnet die Grafiken für den Optionsbildschirm.
	 *
	 * @param g Das Graphics-Objekt zum Zeichnen.
	 */
	@Override
	public void draw(Graphics g) {
	    g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
	    g.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH, null);

	    menuB.draw(g);
	    audioOptions.draw(g);
	}

	/**
	 * Behandelt das Verschieben der Maus.
	 *
	 * @param e Das Ereignisobjekt für die Mausbewegung.
	 */
	public void mouseDragged(MouseEvent e) {
	    audioOptions.mouseDragged(e);
	}

	/**
	 * Behandelt das Drücken der Maustaste.
	 *
	 * @param e Das Ereignisobjekt für das Maustastendrücken.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
	    if (isIn(e, menuB)) {
	        menuB.setMousePressed(true);
	    } else
	        audioOptions.mousePressed(e);
	}

	/**
	 * Behandelt das Loslassen der Maustaste.
	 *
	 * @param e Das Ereignisobjekt für das Maustastenloslassen.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
	    if (isIn(e, menuB)) {
	        if (menuB.isMousePressed())
	            Gamestate.state = Gamestate.MENU;
	    } else
	        audioOptions.mouseReleased(e);
	    menuB.resetBools();
	}

	/**
	 * Behandelt die Bewegung der Maus.
	 *
	 * @param e Das Ereignisobjekt für die Mausbewegung.
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
	    menuB.setMouseOver(false);

	    if (isIn(e, menuB))
	        menuB.setMouseOver(true);
	    else
	        audioOptions.mouseMoved(e);
	}

	/**
	 * Behandelt das Drücken einer Taste auf der Tastatur.
	 *
	 * @param e Das Ereignisobjekt für das Tastendrücken.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
	    if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
	        Gamestate.state = Gamestate.MENU;
	}

	/**
	 * Behandelt das Loslassen einer Taste auf der Tastatur.
	 *
	 * @param e Das Ereignisobjekt für das Tastenloslassen.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
	    // TODO Auto-generated method stub
	}

	/**
	 * Behandelt das Klicken der Maus.
	 *
	 * @param e Das Ereignisobjekt für das Mausklicken.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
	    // TODO Auto-generated method stub
	}

	/**
	 * Überprüft, ob die Mausposition innerhalb eines bestimmten Bereichs liegt.
	 *
	 * @param e Das Ereignisobjekt für die Mausbewegung.
	 * @param b Der PauseButton, der überprüft wird.
	 * @return true, wenn die Maus innerhalb des Bereichs von PauseButton liegt, sonst false.
	 */
	private boolean isIn(MouseEvent e, PauseButton b) {
	    return b.getBounds().contains(e.getX(), e.getY());
	}

}