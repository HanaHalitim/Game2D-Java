package gamestates;

// Enumerationsklasse "Gamestate" zur Repräsentation verschiedener Spielzustände
public enum Gamestate {

    // Mögliche Spielzustände
    PLAYING, // Spiel läuft
    MENU,    // Menü
    OPTIONS, // Optionen
    QUIT,    // Beenden
    CREDITS, // Credits
    USER;    // Benutzerdefinierter Zustand (kann angepasst werden)

    // Statische Variable zur Speicherung des aktuellen Spielzustands
    public static Gamestate state = USER; // Initialisiert mit dem Benutzerzustand

}
