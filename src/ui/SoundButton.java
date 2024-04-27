package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utilz.LoadSave;
import static utilz.Constants.UI.PauseButtons.*;

/**
 * Die Klasse SoundButton repräsentiert eine Schaltfläche im Pausenmenü zur Steuerung von Audioeinstellungen.
 * Es enthält Schaltflächenbilder für unterschiedliche Audiozustände, wie stummgeschaltet oder aktiv.
 */
public class SoundButton extends PauseButton {

    private BufferedImage[][] soundImgs;
    private boolean mouseOver, mousePressed;
    private boolean muted;
    private int rowIndex, colIndex;

    /**
     * Konstruktor für die SoundButton-Klasse.
     *
     * @param x      Die x-Position der Schaltfläche.
     * @param y      Die y-Position der Schaltfläche.
     * @param width  Die Breite der Schaltfläche.
     * @param height Die Höhe der Schaltfläche.
     */
    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        loadSoundImgs();
    }

    private void loadSoundImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTONS);
        soundImgs = new BufferedImage[2][3];
        for (int j = 0; j < soundImgs.length; j++)
            for (int i = 0; i < soundImgs[j].length; i++)
                soundImgs[j][i] = temp.getSubimage(i * SOUND_SIZE_DEFAULT, j * SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT, SOUND_SIZE_DEFAULT);
    }

    /**
     * Aktualisiert den Zustand der Schaltfläche basierend auf Mausereignissen und stummschalten.
     */
    public void update() {
        if (muted)
            rowIndex = 1;
        else
            rowIndex = 0;

        colIndex = 0;
        if (mouseOver)
            colIndex = 1;
        if (mousePressed)
            colIndex = 2;
    }

    /**
     * Setzt die Zustände der Mausereignisse zurück.
     */
    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    /**
     * Zeichnet die Schaltfläche auf die Grafikoberfläche.
     *
     * @param g Die Grafikoberfläche, auf die die Schaltfläche gezeichnet wird.
     */
    public void draw(Graphics g) {
        g.drawImage(soundImgs[rowIndex][colIndex], x, y, width, height, null);
    }

    /**
     * Überprüft, ob die Maus über der Schaltfläche liegt.
     *
     * @return true, wenn die Maus über der Schaltfläche liegt, andernfalls false.
     */
    public boolean isMouseOver() {
        return mouseOver;
    }

    /**
     * Setzt den Zustand für das Überfahren der Maus über der Schaltfläche.
     *
     * @param mouseOver Der Zustand für das Überfahren der Maus über der Schaltfläche.
     */
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    /**
     * Überprüft, ob die Maus auf die Schaltfläche geklickt wurde.
     *
     * @return true, wenn die Maus auf die Schaltfläche geklickt wurde, andernfalls false.
     */
    public boolean isMousePressed() {
        return mousePressed;
    }

    /**
     * Setzt den Zustand für das Klicken auf die Schaltfläche.
     *
     * @param mousePressed Der Zustand für das Klicken auf die Schaltfläche.
     */
    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    /**
     * Überprüft, ob der Ton stummgeschaltet ist.
     *
     * @return true, wenn der Ton stummgeschaltet ist, andernfalls false.
     */
    public boolean isMuted() {
        return muted;
    }

    /**
     * Setzt den Stummschaltzustand für die Schaltfläche.
     *
     * @param muted Der Stummschaltzustand für die Schaltfläche.
     */
    public void setMuted(boolean muted) {
        this.muted = muted;
    }
}
