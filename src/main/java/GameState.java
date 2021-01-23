package main.java;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public abstract class GameState {
    protected static BufferedImage background;
    protected static boolean imagesLoaded;
    protected GameRunner gameRunner;

    public GameState() {
        if(!imagesLoaded){
            loadImages();
            imagesLoaded = true;
        }
        gameRunner = GameRunner.instance;
    }

    protected abstract void loadImages();

    protected abstract void keyPressed(int k);

    protected abstract void keyReleased(int k);

    protected void loadBackground(String backgroundName) {
        String backgroundFilePath = "res/backgrounds/" + backgroundName + ".png";
        File backgroundFile = new File(backgroundFilePath);
        try {
            background = ImageIO.read(backgroundFile);
        } catch (IOException e) {
            String errorMessage = "The " + backgroundName + " background was not found.";
            JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    protected void paintElements(Graphics g){
        paintBackground(g);
    }

    protected void paintBackground(Graphics g){

        //offset calculated taking into account images desired position
        //and an arbitrary value previously used in the app
        int backgroundOffset = -((896-WindowManager.WINDOW_HEIGHT)+31);
        g.drawImage(background, 0, backgroundOffset, gameRunner);
    }

    

}
