package mainPackage;

/**
 * Die GameTimer-Klasse ist verantwortlich für die Messung der Gesamtdauer des Spiels.
 * Dies erreicht sie, indem sie die Startzeit des Spiels aufzeichnet und die Differenz berechnet,
 * wenn das Spiel endet.
 */
public class GameTimer {
    private long gameStart;

    /**
     * Zeichnet die aktuelle Zeit in Millisekunden seit der Epoche auf (00:00:00 UTC am 1. Januar 1970),
     * was den Start des Spiels markiert.
     */
    public void startGame() {
        gameStart = System.currentTimeMillis();
    }

    /**
     * Berechnet die Gesamtdauer des Spiels in Millisekunden und gibt sie auf der Konsole aus.
     * Dies wird erreicht, indem die aufgezeichnete Startzeit von der aktuellen Zeit abgezogen wird.
     */
    public void endGame() {
    	long totalTime = System.currentTimeMillis() - gameStart;
        System.out.println("Gesamte Spielzeit: " + totalTime + " ms");
    }
}
