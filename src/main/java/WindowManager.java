package main.java;

import javax.swing.*;

import java.awt.*;

public class WindowManager extends JFrame {
    private static final long serialVersionUID = 001L;

    public static final int WINDOW_HEIGHT = 800;
    public static final String APP_NAME = "Super Mario Bros In Java";
    public static int windowWidth;

    private int movementOffset;
    private int xScrollPosition;

    public WindowManager() {
        super(APP_NAME);
        findWindowSize();
        movementOffset = windowWidth / 3;
        startGameRunner();

        setJFrameRules();
    }

    private void findWindowSize() {
        Dimension screenDimension = findCurrentScreenDimension();
        windowWidth = (int) screenDimension.getWidth();
    }

    private void startGameRunner() {
        GameRunner gameRunner = GameRunner.instance;
        gameRunner.initializeGameRunner(this);

        add(gameRunner);
    }

    private void setJFrameRules() {
        setSize(windowWidth, WINDOW_HEIGHT);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private Dimension findCurrentScreenDimension() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        return toolkit.getScreenSize();
    }

    public void moveHorizontalScroll(int xNewPosition) {
        if (xNewPosition > movementOffset) {
            xScrollPosition = -(xNewPosition - movementOffset);
        }
    }

    public int getXScrollPosition() {
        return xScrollPosition;
    }

}