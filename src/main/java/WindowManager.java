package main.java;

import javax.swing.JFrame;

import java.awt.Dimension;
import java.awt.Toolkit;

public class WindowManager extends JFrame {
    private static final long serialVersionUID = 001L;

    public static final int WINDOW_HEIGHT = 800;
    public static final String APP_NAME = "Super Mario Bros In Java";

    public static int windowWidth;

    public WindowManager() {
        super(APP_NAME);
        
        add(GameRunner.instance);
        startGameRunner();
        Dimension screenDimension = findCurrentScreenDimension();
        setFrameSize(screenDimension);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        windowWidth = (int)screenDimension.getWidth();
    }

    private void startGameRunner(){
        GameRunner gameRunner = GameRunner.instance;
        add(gameRunner);
        gameRunner.initializeGameRunner();
    }


    private void setFrameSize(Dimension dimension) {
        int width = (int) dimension.getWidth();
        this.setSize(width, WINDOW_HEIGHT);
    }

    public static Dimension findCurrentScreenDimension() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        return toolkit.getScreenSize();
    }
}