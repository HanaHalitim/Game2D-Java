package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import mainPackage.Game;

public class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @Test
    void testGameInitialization() {
        assertNotNull(game.getGamePanel());
        assertNotNull(game.getGameWindow());
        assertNotNull(game.getMenu());
        assertNotNull(game.getPlaying());
        assertNotNull(game.getCredits());
        assertNotNull(game.getGameOptions());
        assertNotNull(game.getAudioOptions());
        assertNotNull(game.getAudioPlayer());
        assertNotNull(game.getChangeUser());
    }
    
    @Test
    public void testGetUserHighscore() {

        // Replace "YOUR_USERNAME" with an actual username from your game
        String usernameToTest = "YOUR_USERNAME";

        // Call the method to get the high score
        int highscore = game.getUserHighscore(usernameToTest);

        // Add your assertions here based on the expected high score
        // Replace 0 with the expected high score for the given username
        assertEquals(0, highscore);
    }
    
    

    
}
