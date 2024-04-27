package mainPackage;

import java.awt.Graphics;

import audio.AudioPlayer;
import gamestates.ChangeUser;
import gamestates.Credits;
import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.Playing;
import ui.AudioOptions;

/**
 * Die Game-Klasse steuert den Spielablauf und verwaltet verschiedene Spielzustände.
 */
public class Game implements Runnable {

    private GamePanel gamePanel;
    private GameWindow gameWindow;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Playing playing;
    private Menu menu;
    private Credits credits;
    private GameOptions gameOptions;
    private AudioOptions audioOptions;
    private AudioPlayer audioPlayer;
    private ChangeUser changeuser;
    
    int level;

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 1f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    private final boolean SHOW_FPS_UPS = true;

    /**
     * Konstruiert ein Game-Objekt.
     */
    public Game() {
        gamePanel = new GamePanel(this);
        initClasses();
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocusInWindow();
        startGameLoop();
    }

    /**
     * Initialisiert verschiedene Spielzustände und Einstellungen.
     */
    private void initClasses() {
        playing = new Playing(this);
        changeuser = new ChangeUser(this, playing);
        audioOptions = new AudioOptions(this);
        audioPlayer = new AudioPlayer();
        menu = new Menu(this);
        credits = new Credits(this);
        gameOptions = new GameOptions(this);
    }

    /**
     * Startet die Spielschleife.
     */
    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * Aktualisiert den Zustand des Spiels.
     */
    public void update() {
        switch (Gamestate.state) {
            case MENU -> menu.update();
            case PLAYING -> playing.update();
            case OPTIONS -> gameOptions.update();
            case CREDITS -> credits.update();
            case USER -> changeuser.update();
            case QUIT -> System.exit(0);
        }
    }

    /**
     * Rendert das Spiel.
     *
     * @param g Das Graphics-Objekt.
     */
    @SuppressWarnings("incomplete-switch")
    public void render(Graphics g) {
        switch (Gamestate.state) {
            case MENU -> menu.draw(g);
            case PLAYING -> playing.draw(g);
            case OPTIONS -> gameOptions.draw(g);
            case CREDITS -> credits.draw(g);
            case USER -> changeuser.draw(g);
        }
    }

    /**
     * Implementiert die run-Methode für den Spiel-Thread.
     */
    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {

            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                deltaF--;
            }

            if (SHOW_FPS_UPS)
                if (System.currentTimeMillis() - lastCheck >= 1000) {
                    lastCheck = System.currentTimeMillis();
                }
        }
    }

    /**
     * Behandelt den Verlust des Fokus vom Spiel-Fenster.
     * Wenn das Spiel im Zustand "PLAYING" ist, werden die Richtungsbooleans des Spielers zurückgesetzt.
     */
    public void windowFocusLost() {
        if (Gamestate.state == Gamestate.PLAYING)
            playing.getPlayer().resetDirBooleans();
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public Credits getCredits() {
        return credits;
    }

    public GameOptions getGameOptions() {
        return gameOptions;
    }
    
    public ChangeUser getChangeUser() {
        return changeuser;
    }

    public AudioOptions getAudioOptions() {
        return audioOptions;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }
    
    public GameWindow getGameWindow() {
        return gameWindow;
    }
    
    public GamePanel getGamePanel() {
        return gamePanel;
    }
    
    /**
     * Ruft die Höchstpunktzahl eines Benutzers ab.
     *
     * @param USERNAME Der Benutzername, für den die Höchstpunktzahl abgefragt wird.
     * @return Die Höchstpunktzahl des Benutzers.
     */

    public int getUserHighscore(String USERNAME) {
        int level = playing.readLevel(USERNAME);
        return level;
    }
}
