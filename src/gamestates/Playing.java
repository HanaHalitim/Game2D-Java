package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;


import java.util.List;


import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import mainPackage.Game;
import mainPackage.GameTimer;
import objects.ObjectManager;
import ui.GameCompletedOverlay;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;
import effects.Rain;

import static mainPackage.Game.GAME_WIDTH;



/**
 * Die Klasse Playing repräsentiert den Spielzustand, wenn der Spieler spielt.
 */
public class Playing extends State implements Statemethods {

	//just to show text... 10sec
	private boolean displayMessage;
    private long messageDisplayStartTime;
    private final long MESSAGE_DISPLAY_DURATION = 15_000; // 10 seconds
    
	private Player player;
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private ObjectManager objectManager;
	private PauseOverlay pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	private GameCompletedOverlay gameCompletedOverlay;
	private LevelCompletedOverlay levelCompletedOverlay;
	private Rain rain;

	private boolean paused = false;

	private int xLvlOffset;
	private int leftBorder = (int) (0.25 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.75 * Game.GAME_WIDTH);
	private int maxLvlOffsetX;

	private BufferedImage backgroundImg;

	private Random rnd = new Random();

	private boolean gameOver;
	private boolean lvlCompleted;
	private boolean gameCompleted;
	private boolean playerDying;
	private boolean drawRain;
	
	private String userName;
	private int levelIndex = 0;
	
	private GameTimer gameTimer;
	private boolean gameEnded = false;

	/**
     * Konstruktor für die Klasse Playing.
     *
     * @param game Die Hauptspielinstanz.
     */
	public Playing(Game game) {
		super(game);
		this.gameTimer = new GameTimer();
		displayMessage = true;
        messageDisplayStartTime = System.currentTimeMillis();
	}
	/**
     * Methode, um das Spielende zu behandeln.
     */
	public void GameEnd() {
	    game.getGameWindow().getJFrame().addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowClosing(WindowEvent e) {
	            gameTimer.endGame();  // End the timer
	            System.out.println("You are in level : " + levelManager.getLevelIndex() + 1);
	            System.exit(0);  // Exit the application
	        }
	    });
	}

	 /**
     * Methode, um den Benutzernamen zu setzen und die Spielklasse zu initialisieren.
     *
     * @param userName Der Benutzername des Spielers.
     */
	public void setUserName(String userName) {
        this.userName = userName;
        this.levelIndex = getLevelIndex(userName);
        this.levelManager = new LevelManager(game, levelIndex);
        GameEnd();
        initClasses(); // Initialize the classes here
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
        game.getGamePanel().requestFocusInWindow();
        calcLvlOffset(); // Calculate the level offset here
        loadStartLevel(); // Load the start level here
        setDrawRainBoolean();
    }
	
	/**
     * Methode zum Lesen des Level-Indexes aus der Datei "user_levels.txt".
     *
     * @param userName Der Benutzername des Spielers.
     * @return Der Level-Index des Spielers.
     */
	private int getLevelIndex(String userName) {
	    try {
	        Path path = Paths.get("user_levels.txt");
	        List<String> lines = Files.readAllLines(path);
	        for (String line : lines) {
	            String[] parts = line.split(" \\| ");
	            if (parts.length >= 2 && parts[0].equals(userName)) {
	                return Integer.parseInt(parts[1].trim());
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return -1; // Return -1 or another value if the user is not found
	}	

	public void loadNextLevel() {
		levelManager.setLevelIndex(levelManager.getLevelIndex() + 1);
		// Write the new level index to the file
        writeLevel(userName, levelManager.getLevelIndex());
		levelManager.loadNextLevel();
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());
		resetAll();
	}

	private void loadStartLevel() {
		enemyManager.loadEnemies(levelManager.getCurrentLevel());
		objectManager.loadObjects(levelManager.getCurrentLevel());
	}

	private void calcLvlOffset() {
		maxLvlOffsetX = levelManager.getCurrentLevel().getLvlOffset();
	}

	private void initClasses() {
		gameTimer.startGame();

		if (levelIndex != 0) {
			levelManager = new LevelManager(game, levelIndex);
		}else {
			levelManager = new LevelManager(game, 0);
		}
		enemyManager = new EnemyManager(this);
		objectManager = new ObjectManager(this);

		player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE), this);
		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		player.setSpawn(levelManager.getCurrentLevel().getPlayerSpawn());

		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);
		levelCompletedOverlay = new LevelCompletedOverlay(this);
		gameCompletedOverlay = new GameCompletedOverlay(this);

		rain = new Rain();
	}

	@Override
	public void update() {
		
		
		
		if (paused) {
			pauseOverlay.update();
		}
		else if (lvlCompleted)
			levelCompletedOverlay.update();
		else if (gameCompleted) {
			if (!gameEnded) {  // Only call endGame() if it hasn't been called yet
                gameTimer.endGame();
                gameEnded = true;  // Set the flag to true
            }
			gameCompletedOverlay.update();
		}
		else if (gameOver) {
			gameOverOverlay.update();
		}
		else if (playerDying)
			player.update();
		else {
			if (drawRain)
				rain.update(xLvlOffset);
			levelManager.update();
			objectManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			player.update();
			enemyManager.update(levelManager.getCurrentLevel().getLevelData());
			checkCloseToBorder();
		}
		
		if (displayMessage) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - messageDisplayStartTime >= MESSAGE_DISPLAY_DURATION) {
                displayMessage = false;
                // Other logic after the message display duration...
            }
		}
	}

	private void checkCloseToBorder() {
		int playerX = (int) player.getHitbox().x;
		int diff = playerX - xLvlOffset;

		if (diff > rightBorder)
			xLvlOffset += diff - rightBorder;
		else if (diff < leftBorder)
			xLvlOffset += diff - leftBorder;

		xLvlOffset = Math.max(Math.min(xLvlOffset, maxLvlOffsetX), 0);
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

		if (drawRain)
			rain.draw(g, xLvlOffset);

		levelManager.draw(g, xLvlOffset);
		objectManager.draw(g, xLvlOffset);
		enemyManager.draw(g, xLvlOffset);
		player.render(g, xLvlOffset);
		objectManager.drawBackgroundTrees(g, xLvlOffset);

		if (paused) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		} else if (gameOver)
			gameOverOverlay.draw(g);
		else if (lvlCompleted)
			levelCompletedOverlay.draw(g);
		else if (gameCompleted)
			gameCompletedOverlay.draw(g);
		
		
		//show message  links rechts usw
		if (displayMessage) {
      
            int lineHeight = 20; // Adjust this value based on your font size and spacing
            int centerX = GAME_WIDTH / 2;
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 15)); // Adjust 30 to your preferred font size
            g.drawString("Bewegung:     <- S | D ->", centerX - 100, 30);
            g.drawString("Springen:     Space Taste", centerX - 100, 30 + lineHeight);
            g.drawString("Angreifen:     Mausklick", centerX - 100, 30 + 2*lineHeight);


        }

	}

	public void setGameCompleted() {
		gameCompleted = true;
	}

	public void resetGameCompleted() {
		gameCompleted = false;
	}

	public void resetAll() {
		gameOver = false;
		paused = false;
		lvlCompleted = false;
		playerDying = false;
		drawRain = false;

		setDrawRainBoolean();

		player.resetAll();
		enemyManager.resetAllEnemies();
		objectManager.resetAllObjects();
	}

	private void setDrawRainBoolean() {
		// This method makes it rain 20% of the time you load a level.
		if (rnd.nextFloat() >= 0.8f)
			drawRain = true;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void checkObjectHit(Rectangle2D.Float attackBox) {
		objectManager.checkObjectHit(attackBox);
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHit(attackBox);
	}

	public void checkPotionTouched(Rectangle2D.Float hitbox) {
		objectManager.checkObjectTouched(hitbox);
	}

	public void checkSpikesTouched(Player p) {
		objectManager.checkSpikesTouched(p);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!gameOver) {
			if (e.getButton() == MouseEvent.BUTTON1)
				player.setAttacking(true);
			else if (e.getButton() == MouseEvent.BUTTON3)
				player.powerAttack();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!gameOver && !gameCompleted && !lvlCompleted)
			switch (e.getKeyCode()) {
			case KeyEvent.VK_S:
				player.setLeft(true);
				break;
			case KeyEvent.VK_D:

				player.setRight(true);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(true);
				break;
			case KeyEvent.VK_ESCAPE:
				paused = !paused;
			}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver && !gameCompleted && !lvlCompleted)
			switch (e.getKeyCode()) {
			case KeyEvent.VK_S:
				player.setLeft(false);
				break;
			case KeyEvent.VK_D:
				player.setRight(false);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(false);
				break;
			}
	}

	public void mouseDragged(MouseEvent e) {
		if (!gameOver && !gameCompleted && !lvlCompleted)
			if (paused)
				pauseOverlay.mouseDragged(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (gameOver)
			gameOverOverlay.mousePressed(e);
		else if (paused)
			pauseOverlay.mousePressed(e);
		else if (lvlCompleted)
			levelCompletedOverlay.mousePressed(e);
		else if (gameCompleted)
			gameCompletedOverlay.mousePressed(e);

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (gameOver)
			gameOverOverlay.mouseReleased(e);
		else if (paused)
			pauseOverlay.mouseReleased(e);
		else if (lvlCompleted)
			levelCompletedOverlay.mouseReleased(e);
		else if (gameCompleted)
			gameCompletedOverlay.mouseReleased(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (gameOver)
			gameOverOverlay.mouseMoved(e);
		else if (paused)
			pauseOverlay.mouseMoved(e);
		else if (lvlCompleted)
			levelCompletedOverlay.mouseMoved(e);
		else if (gameCompleted)
			gameCompletedOverlay.mouseMoved(e);
	}

	public void setLevelCompleted(boolean levelCompleted) {
		game.getAudioPlayer().lvlCompleted();
		if (levelManager.getLevelIndex() + 1 >= levelManager.getAmountOfLevels()) {
			// No more levels
			gameCompleted = true;
			levelManager.setLevelIndex(0);
			levelManager.loadNextLevel();
			resetAll();
			return;
		}
		this.lvlCompleted = levelCompleted;
	}

	public void setMaxLvlOffset(int lvlOffset) {
		this.maxLvlOffsetX = lvlOffset;
	}

	public void unpauseGame() {
		paused = false;
	}

	public void windowFocusLost() {
		player.resetDirBooleans();
	}
	
	public int readLevel(String userName) {
	    try {
	        Path path = Paths.get("user_levels.txt");
	        List<String> lines = Files.readAllLines(path);
	        for (String line : lines) {
	            String[] parts = line.split(" \\| ");
	            if (parts[0].equals(userName)) {
	            	if (!parts[1].trim().isEmpty()) {
	                    return Integer.parseInt(parts[1].trim());
	                }
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    // Return 0 if user not found
	    System.out.println("User Not Found");
	    return 0;
	}

	public void writeLevel(String userName, int level) {
	    try {
	        Path path = Paths.get("user_levels.txt");
	        List<String> lines = Files.readAllLines(path);
	        int userIndex = -1;
	        for (int i = 0; i < lines.size(); i++) {
	            String[] parts = lines.get(i).split(" \\| ");
	            if (parts[0].equals(userName)) {
	                userIndex = i;
	                break;
	            }
	        }
	        if (userIndex != -1) {
	            // If user found, update level
	            lines.set(userIndex, userName + " | " + level);
	            Files.write(path, lines);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}



	public Player getPlayer() {
		return player;
	}

	public EnemyManager getEnemyManager() {
		return enemyManager;
	}

	public ObjectManager getObjectManager() {
		return objectManager;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

	public void setPlayerDying(boolean playerDying) {
		this.playerDying = playerDying;
	}
}