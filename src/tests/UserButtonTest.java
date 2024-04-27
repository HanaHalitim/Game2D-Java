package tests;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.*;
import java.util.List;

import java.io.*;

import org.junit.jupiter.api.Test;

import gamestates.ChangeUser;
import gamestates.Gamestate;
import gamestates.Playing;
import mainPackage.Game;
import ui.UserButton;

// Class to test UserButton
class UserButtonTest {
    @Test
    public void testWriteToFile() {
        // Create an instance of your Game and Playing classes
        // You'll need to replace the parameters with appropriate values
        Game game = new Game();
        Playing playing = new Playing(game);

        // Create an instance of your ChangeUser class
        ChangeUser changeUser = new ChangeUser(game, playing);

        // Create an instance of your UserButton class
        UserButton userButton = new UserButton(Game.GAME_WIDTH / 2, (int) (270 * Game.SCALE), 1, Gamestate.MENU, changeUser.getTextField(), changeUser);

        // Test with a user that doesn't exist in the file
        String newUser = "TestUser";
        userButton.writeToFile(newUser);
        assertTrue(userExistsInFile(newUser), "New user should be added to the file");

        // Test with a user that already exists in the file
        String existingUser = "hana";  // Replace this with a user that exists in your file
        userButton.writeToFile(existingUser);
        assertTrue(userExistsInFile(existingUser), "Existing user should still be in the file");
    }

    private boolean userExistsInFile(String userName) {
        try {
            Path path = Paths.get("user_levels.txt");
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.startsWith(userName + " | ")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
