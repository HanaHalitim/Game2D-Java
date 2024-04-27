package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Die Klasse AudioPlayer ermöglicht das Abspielen von Hintergrundmusik und Soundeffekten in einem Spiel.
 */
public class AudioPlayer {

    /** Konstante für das Menü-Song */
    public static int MENU_1 = 0;
    /** Konstante für Level 1-Song */
    public static int LEVEL_1 = 1;
    /** Konstante für Level 2-Song */
    public static int LEVEL_2 = 2;

    /** Konstanten für Soundeffekte */
    public static int DIE = 0;
    public static int JUMP = 1;
    public static int GAMEOVER = 2;
    public static int LVL_COMPLETED = 3;
    public static int ATTACK_ONE = 4;
    public static int ATTACK_TWO = 5;
    public static int ATTACK_THREE = 6;

    private Clip[] songs, effects;
    private int currentSongId;
    private float volume = 0.5f;
    private boolean songMute, effectMute;
    private Random rand = new Random();

    /**
     * Konstruktor für die AudioPlayer-Klasse. Lädt Songs und startet das Menü-Song.
     */
    public AudioPlayer() {
        loadSongs();
        loadEffects();
        playSong(MENU_1);
    }

    /**
     * Lädt die Hintergrundmusik-Songs in das Array songs.
     */
    private void loadSongs() {
        String[] names = { "menu", "level1", "level2" };
        songs = new Clip[names.length];
        for (int i = 0; i < songs.length; i++)
            songs[i] = getClip(names[i]);
    }

    /**
     * Lädt die Soundeffekte in das Array effects und aktualisiert die Lautstärke der Effekte.
     */
    private void loadEffects() {
        String[] effectNames = { "die", "jump", "gameover", "lvlcompleted", "attack1", "attack2", "attack3" };
        effects = new Clip[effectNames.length];
        for (int i = 0; i < effects.length; i++)
            effects[i] = getClip(effectNames[i]);

        updateEffectsVolume();
    }

    /**
     * Lädt einen Audio-Clip aus einer Datei.
     *
     * @param name Der Name der Audio-Datei ohne Erweiterung.
     * @return Ein Clip-Objekt, das die geladene Audio-Datei repräsentiert.
     */
    private Clip getClip(String name) {
        URL url = getClass().getResource("/audio/" + name + ".wav");
        AudioInputStream audio;

        try {
            audio = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(audio);
            return c;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Setzt die Lautstärke für sowohl Hintergrundmusik als auch Soundeffekte.
     *
     * @param volume Der Lautstärkewert, der zwischen 0.0 und 1.0 liegen sollte.
     */
    public void setVolume(float volume) {
        this.volume = volume;
        updateSongVolume();
        updateEffectsVolume();
    }

    /**
     * Stoppt die aktuell abgespielte Hintergrundmusik.
     */
    public void stopSong() {
        if (songs[currentSongId].isActive())
            songs[currentSongId].stop();
    }

    /**
     * Setzt den Hintergrundmusik-Song basierend auf dem Level-Index.
     *
     * @param lvlIndex Der Index des Levels.
     */
    public void setLevelSong(int lvlIndex) {
        if (lvlIndex % 2 == 0)
            playSong(LEVEL_1);
        else
            playSong(LEVEL_2);
    }


    /**
     * Stoppt die aktuelle Hintergrundmusik und spielt den Soundeffekt für Level abgeschlossen (lvl completed).
     */
    public void lvlCompleted() {
        stopSong();
        playEffect(LVL_COMPLETED);
    }

    /**
     * Spielt einen zufälligen Angriffssoundeffekt ab.
     */
    public void playAttackSound() {
        int start = 4;
        start += rand.nextInt(3);
        playEffect(start);
    }

    /**
     * Spielt den angegebenen Soundeffekt ab.
     *
     * @param effect Der Index des Soundeffekts, der abgespielt werden soll.
     */
    public void playEffect(int effect) {
        if (effects[effect].getMicrosecondPosition() > 0)
            effects[effect].setMicrosecondPosition(0);
        effects[effect].start();
    }

    /**
     * Spielt den angegebenen Hintergrundmusik-Song ab.
     *
     * @param song Der Index des Songs, der abgespielt werden soll.
     */
    public void playSong(int song) {
        stopSong();

        currentSongId = song;
        updateSongVolume();
        songs[currentSongId].setMicrosecondPosition(0);
        songs[currentSongId].loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Schaltet die Stummschaltung der Hintergrundmusik um.
     */
    public void toggleSongMute() {
        this.songMute = !songMute;
        for (Clip c : songs) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(songMute);
        }
    }

    /**
     * Schaltet die Stummschaltung der Soundeffekte um. Wenn die Stummschaltung aufgehoben wird, wird der Sprung-Soundeffekt abgespielt.
     */
    public void toggleEffectMute() {
        this.effectMute = !effectMute;
        for (Clip c : effects) {
            BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
            booleanControl.setValue(effectMute);
        }
        if (!effectMute)
            playEffect(JUMP);
    }

    /**
     * Aktualisiert die Lautstärke der aktuellen Hintergrundmusik basierend auf dem eingestellten Lautstärkewert.
     */
    private void updateSongVolume() {
        FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * volume) + gainControl.getMinimum();
        gainControl.setValue(gain);
    }

    /**
     * Aktualisiert die Lautstärke aller Soundeffekte basierend auf dem eingestellten Lautstärkewert.
     */
    private void updateEffectsVolume() {
        for (Clip c : effects) {
            FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }


}