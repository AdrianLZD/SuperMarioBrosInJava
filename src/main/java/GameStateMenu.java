package main.java;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class GameStateMenu extends GameState {

    private static final String[] options = { "PLAY", "CREDITS", "EXIT" };

    private BufferedImage logo;
    private Font textFont;
    private Color textColor;
    private int currentOption;

    public GameStateMenu() {
        super();
        textFont = TextFont.getFont();
        definePanelSize(WindowManager.windowWidth, WindowManager.WINDOW_HEIGHT);
    }

    @Override
    protected void loadImages() {
        getBackground("menu");
        logo = SpriteAssets.getLogo();
    }

    @Override
    protected void paintElements(Graphics g) {
        paintBackground(g);
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
            case KeyEvent.VK_ENTER:
                applySelectedOption();
                break;
            default:
                break;
        }

    }

    private void applySelectedOption() {
        switch (currentOption) {
            case 0:
                requestNextLevel();
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
        }
    }

    @Override
    protected void keyReleased(int k) {
    }

    @Override
    protected void tick() {

    }

    @Override
    protected void spawnMario() {
        // No need to spawn mario
    }

    @Override
    public void requestNextLevel() {
        GameState newGameState = new GameStateLevel3();
        gameRunner.setCurrentGameState(newGameState);
     }
}
