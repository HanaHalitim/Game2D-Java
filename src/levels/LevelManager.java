package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import mainPackage.Game;
import utilz.LoadSave;

/**
 * Die Klasse LevelManager verwaltet die verschiedenen Level im Spiel und steuert den Wechsel zwischen ihnen.
 */
public class LevelManager {

    private Game game;
    private BufferedImage[] levelSprite;
    private BufferedImage[] waterSprite;
    private ArrayList<Level> levels;
    private int lvlIndex = 0, aniTick, aniIndex;

    /**
     * Konstruktor für die LevelManager-Klasse.
     *
     * @param game               Die Spielinstanz, zu der der LevelManager gehört.
     * @param initialLevelIndex Der Index des initialen Levels.
     */
    public LevelManager(Game game, int initialLevelIndex) {
        this.game = game;
        this.lvlIndex = initialLevelIndex;
        importOutsideSprites();
        createWater();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    private void createWater() {
        waterSprite = new BufferedImage[5];
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.WATER_TOP);
        for (int i = 0; i < 4; i++)
            waterSprite[i] = img.getSubimage(i * 32, 0, 32, 32);
        waterSprite[4] = LoadSave.GetSpriteAtlas(LoadSave.WATER_BOTTOM);
    }

    /**
     * Lädt das nächste Level und aktualisiert die Spielobjekte entsprechend.
     */
    public void loadNextLevel() {
        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLevelData());
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.getAllLevels();
        for (BufferedImage img : allLevels)
            levels.add(new Level(img));
    }

    private void importOutsideSprites() {
        BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
        levelSprite = new BufferedImage[48];
        for (int j = 0; j < 4; j++)
            for (int i = 0; i < 12; i++) {
                int index = j * 12 + i;
                levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
            }
    }

    /**
     * Zeichnet das aktuelle Level auf die Grafikoberfläche.
     *
     * @param g        Die Grafikoberfläche.
     * @param lvlOffset Der horizontale Offset des Levels.
     */
    public void draw(Graphics g, int lvlOffset) {
        for (int j = 0; j < Game.TILES_IN_HEIGHT; j++)
            for (int i = 0; i < levels.get(lvlIndex).getLevelData()[0].length; i++) {
                int index = levels.get(lvlIndex).getSpriteIndex(i, j);
                int x = Game.TILES_SIZE * i - lvlOffset;
                int y = Game.TILES_SIZE * j;
                if (index == 48)
                    g.drawImage(waterSprite[aniIndex], x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
                else if (index == 49)
                    g.drawImage(waterSprite[4], x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
                else
                    g.drawImage(levelSprite[index], x, y, Game.TILES_SIZE, Game.TILES_SIZE, null);
            }
    }

    /**
     * Aktualisiert den LevelManager, einschließlich der Wasseranimation.
     */
    public void update() {
        updateWaterAnimation();
    }

    private void updateWaterAnimation() {
        aniTick++;
        if (aniTick >= 40) {
            aniTick = 0;
            aniIndex++;

            if (aniIndex >= 4)
                aniIndex = 0;
        }
    }

    /**
     * Gibt das aktuelle Level zurück.
     *
     * @return Das aktuelle Level.
     */
    public Level getCurrentLevel() {
        return levels.get(lvlIndex);
    }

    /**
     * Gibt die Anzahl der im LevelManager enthaltenen Level zurück.
     *
     * @return Die Anzahl der Level.
     */
    public int getAmountOfLevels() {
        return levels.size();
    }

    /**
     * Gibt den Index des aktuellen Levels zurück.
     *
     * @return Der Index des aktuellen Levels.
     */
    public int getLevelIndex() {
        return lvlIndex;
    }

    /**
     * Setzt den Index des aktuellen Levels.
     *
     * @param lvlIndex Der Index des Levels, der gesetzt werden soll.
     */
    public void setLevelIndex(int lvlIndex) {
        this.lvlIndex = lvlIndex;
    }
}
