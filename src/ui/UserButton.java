package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gamestates.ChangeUser;
import gamestates.Gamestate;
import utilz.LoadSave;
import static utilz.Constants.UI.Buttons.*;

import javax.swing.JTextField;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;

/**
 * Die Klasse UserButton repräsentiert eine Schaltfläche im Benutzermenü.
 * Diese Schaltfläche ermöglicht den Benutzern, ihren Namen einzugeben und zu ändern.
 */
public class UserButton {
	private int xPos, yPos, rowIndex, index;
	private int xOffsetCenter = B_WIDTH / 2;
	private Gamestate state;
	private BufferedImage[] imgs;
	private boolean mouseOver, mousePressed;
	private Rectangle bounds;
	private JTextField userNameField;
	
	private ChangeUser changeUser;

	/**
     * Konstruktor für die UserButton-Klasse.
     *
     * @param xPos           Die x-Position der Schaltfläche.
     * @param yPos           Die y-Position der Schaltfläche.
     * @param rowIndex       Der Index der Zeile für die Schaltflächenbilder.
     * @param state          Der Spielzustand, der angewendet wird, wenn die Schaltfläche geklickt wird.
     * @param userNameField  Das Textfeld für die Eingabe des Benutzernamens.
     * @param changeUser     Die Instanz von ChangeUser, um den Benutzernamen zu ändern.
     */
	public UserButton(int xPos, int yPos, int rowIndex, Gamestate state, JTextField userNameField, ChangeUser changeUser) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rowIndex = rowIndex;
		this.state = state;
		if (userNameField == null) {
	        throw new IllegalArgumentException("userNameField is null");
	    }
		this.userNameField = userNameField;
		this.changeUser = changeUser;
		loadImgs();
		initBounds();
	}

	private void initBounds() {
		bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT);
	}

	private void loadImgs() {
		imgs = new BufferedImage[3];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.LOGIN);
		for (int i = 0; i < imgs.length; i++)
			imgs[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
	}
	/**
     * Zeichnet die Schaltfläche auf die Grafikoberfläche.
     *
     * @param g Die Grafikoberfläche, auf die die Schaltfläche gezeichnet wird.
     */
	public void draw(Graphics g) {
		g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, B_WIDTH, B_HEIGHT, null);
	}

	public void update() {
	    index = 0;
	    if (mouseOver)
	        index = 1;
	    if (mousePressed)
	        index = 2;
	}
	
	/**
     * Überprüft, ob die Maus über der Schaltfläche liegt.
     *
     * @return true, wenn die Maus über der Schaltfläche liegt, andernfalls false.
     */
	public boolean isMouseOver() {
		return mouseOver;
	}

	
	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	 /**
     * Wird aufgerufen, um den Spielzustand anzuwenden, wenn die Schaltfläche geklickt wird.
     */
	public void applyGamestate() {
		if (!userNameField.getText().trim().isEmpty())
			Gamestate.state = state;
		if (state == Gamestate.MENU) {
            String userName = userNameField.getText();
            if (!userName.trim().isEmpty())
            	writeToFile(userName);
            	changeUser.setUserName(userName);
        }
	}
	
	public void writeToFile(String userName) {
	    try {
	        Path path = Paths.get("user_levels.txt");
	        // Check if file does not exist, then create it
	        if (!Files.exists(path)) {
	            Files.createFile(path);
	        }

	        List<String> lines = Files.readAllLines(path);
	        boolean userFound = false;
	        for (String line : lines) {
	            if (line.startsWith(userName + " | ")) {
	                userFound = true;
	                break;
	            }
	        }
	        if (!userFound) {
	            lines.add(userName + " | 0");
	            Files.write(path, lines);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}



	/**
     * Setzt die Zustände der Mausereignisse zurück.
     */
	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}
	/**
     * Gibt den aktuellen Spielzustand zurück.
     *
     * @return Der aktuelle Spielzustand.
     */
	public Gamestate getState() {
		return state;
	}

}