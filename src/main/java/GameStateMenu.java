package main.java;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class GameStateMenu extends GameState {

    private static final String[] options = { "PLAY", "CREDITS", "EXIT" };

    private BufferedImage logo;
    private Font textFont;
    private Color textColor;
    private int currentOption;

    public GameStateMenu() {
        super();
        textFont = TextFont.getFont();
    }

    @Override
    protected void loadImages() {
        loadBackground("menu");
        loadLogo();
    }

    private void loadLogo() {
        String logoFilePath = "res/backgrounds/logo.png";
        File logoFile = new File(logoFilePath);
        try {
            logo = ImageIO.read(logoFile);
        } catch (IOException e) {
            ErrorLogger.logErrorMessage("The logo was not found", e);
        }
    }

    @Override
    protected void paintElements(Graphics g) {
        super.paintElements(g);
        paintLogo(g);
        paintMenuOptions(g);

    }

    private void paintLogo(Graphics g) {
        int xLogoCoordinate = (WindowManager.windowWidth - logo.getWidth()) / 2;
        g.drawImage(logo, xLogoCoordinate, 80, GameRunner.instance);
    }

    private void paintMenuOptions(Graphics g) {
        for (int i = 0; i < options.length; i++) {
            getSelectedOption(i);
            g.setFont(textFont);
            g.setColor(textColor);
            g.drawString(options[i], WindowManager.windowWidth / 2 - 100, 470 + i * 60);
        }
    }

    private void getSelectedOption(int iterator) {
        if (iterator == currentOption) {
            textFont = textFont.deriveFont(Font.BOLD, 30);
            textColor = Color.GREEN;
        } else {
            textFont = textFont.deriveFont(Font.PLAIN, 30);
            textColor = Color.WHITE;
        }
    }

    @Override
    protected void keyPressed(int k) {
        switch (k) {
            case KeyEvent.VK_DOWN:
                if (currentOption++ == options.length - 1)
                    currentOption = 0;
                break;

            case KeyEvent.VK_UP:
                if (currentOption-- == 0)
                    currentOption = options.length - 1;
                break;
            case KeyEvent.VK_0:
                if (currentOption-- == 0)
                    currentOption = options.length - 1;
                break;

            default:
                break;
        }

    }

    @Override
    protected void keyReleased(int k) {
    }
}
