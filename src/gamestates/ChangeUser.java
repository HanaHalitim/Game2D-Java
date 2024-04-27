package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JTextField;
import mainPackage.Game;
import ui.UserButton;
import utilz.LoadSave;

/**
 * Die ChangeUser-Klasse repräsentiert den Zustand zum Ändern des Benutzernamens im Spiel.
 */
public class ChangeUser extends State1 implements Statemethods {

    private UserButton[] buttons = new UserButton[1];
    private BufferedImage backgroundImg, backgroundImgPink;
    private int menuX, menuY, menuWidth, menuHeight;

    JTextField userName;
    String UserName;
    private Playing playing;

    /**
     * Konstruktor für den Zustand zum Ändern des Benutzernamens.
     *
     * @param game     Die Hauptspielinstanz.
     * @param playing  Die Instanz des aktuellen Spielzustands.
     */
    public ChangeUser(Game game, Playing playing) {
        super(game);
        this.playing = playing;
        userName = new JTextField(20);
        userName.setBounds((int) (335 * Game.SCALE), (int) (165 * Game.SCALE), 165, 25);
        game.getGamePanel().add(userName);
        loadButtons();
        loadBackground();
        backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
    }

    /**
     * Setzt den Benutzernamen.
     *
     * @param userName Der neue Benutzername.
     */
    public void setUserName(String userName) {
        this.UserName = userName;
        if (playing != null) {
            playing.setUserName(userName);
        }
    }

    /**
     * Gibt den aktuellen Benutzernamen zurück.
     *
     * @return Der aktuelle Benutzername.
     */
    public String getUserName() {
        return UserName;
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.USER);
        menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (25 * Game.SCALE);
    }

    private void loadButtons() {
        buttons[0] = new UserButton(Game.GAME_WIDTH / 2, (int) (270 * Game.SCALE), 1, Gamestate.MENU, userName, this);
    }

    @Override
    public void update() {
        for (UserButton mb : buttons)
            mb.update();
        userName.setVisible(true);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImgPink, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);

        for (UserButton mb : buttons)
            mb.draw(g);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (UserButton mb : buttons) {
            if (isIn(e, mb)) {
                mb.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (UserButton mb : buttons) {
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

    private void resetButtons() {
        for (UserButton mb : buttons)
            mb.resetBools();
        userName.setVisible(false);
    }

    /**
     * Gibt das Textfeld für den Benutzernamen zurück.
     *
     * @return Das Textfeld für den Benutzernamen.
     */
    public JTextField getTextField() {
        return userName;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (UserButton mb : buttons)
            mb.setMouseOver(false);

        for (UserButton mb : buttons)
            if (isIn(e, mb)) {
                mb.setMouseOver(true);
                break;
            }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
    }
}
