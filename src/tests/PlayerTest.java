package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entities.Player;

public class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        // Initialize a new Player object before each test
        player = new Player(0, 0, 0, 0, null);
    }

    @Test
    void testChangeHealthPositiveValue() {
        player.changeHealth(10); // Change health by a positive value
        assertEquals(110, player.getCurrentHealth()); // Assert that the health is now 110
    }

    @Test
    void testChangeHealthNegativeValue() {
        player.changeHealth(-5); // Change health by a negative value
        assertEquals(95, player.getCurrentHealth()); // Assert that the health is now 95
    }

    @Test
    void testChangeHealthZeroValue() {
        player.changeHealth(0); // Change health by zero
        assertEquals(100, player.getCurrentHealth()); // Assert that the health remains unchanged (100)
    }

    // Add more test methods as needed

}
