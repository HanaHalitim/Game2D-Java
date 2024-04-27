package mainPackage;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import inputs.KeyboardInputs;
import inputs.MouseInputs;
import static mainPackage.Game.GAME_HEIGHT;
import static mainPackage.Game.GAME_WIDTH;

/**
 * Die GamePanel-Klasse stellt das Hauptpanel des Spiels dar, auf dem Grafiken gezeichnet werden.
 * Sie erweitert die JPanel-Klasse und behandelt Maus- und Tastatureingaben.
 */
public class GamePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private MouseInputs mouseInputs;
	private Game game;

	/**
	 * Konstruiert ein GamePanel-Objekt.
	 * Initialisiert die Maus-Eingabebehandlung, setzt den Layout-Manager auf null für manuelle Komponentenpositionierung,
	 * und fügt Listener für Tastatur- und Mausereignisse hinzu.
	 *
	 * @param game Das Spielobjekt.
	 */
	public GamePanel(Game game) {
		setLayout(null);
		mouseInputs = new MouseInputs(this);
		this.game = game;
		setPanelSize();
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}

	/**
	 * Legt die Größe des Panels fest.
	 */
	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);
	}

	/**
	 * Überschreibt die paintComponent-Methode von JPanel, um das Spiel zu rendern.
	 *
	 * @param g Das Graphics-Objekt.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		game.render(g);
	}

	/**
	 * Gibt das Spielobjekt zurück.
	 *
	 * @return Das Spielobjekt.
	 */
	public Game getGame() {
		return game;
	}
}
