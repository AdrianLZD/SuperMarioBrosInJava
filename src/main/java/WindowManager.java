package main.java;

import javax.swing.*;

import java.awt.*;

public class WindowManager extends JFrame {
    private static final long serialVersionUID = 001L;

    public static final int WINDOW_HEIGHT = 800;
    public static final String APP_NAME = "Super Mario Bros In Java";
    public static int windowWidth;

    private int movementOffset;
    private int xPaintingPosition;
    private JScrollPane scrollPane;

    
    public WindowManager() {
        super(APP_NAME);
        findWindowSize();
        movementOffset = windowWidth / 3;
        startGameRunner();
        initializeScrollPane();

        setJFrameRules();
    }

    private void findWindowSize(){
        Dimension screenDimension = findCurrentScreenDimension();
        windowWidth = (int) screenDimension.getWidth();
     }

    private void startGameRunner() {
        GameRunner gameRunner = GameRunner.instance;
        gameRunner.initializeGameRunner(this);

        add(gameRunner);
    }

    private void setJFrameRules(){
        setSize(windowWidth, WINDOW_HEIGHT);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    
    private void initializeScrollPane(){
        scrollPane = new JScrollPane(GameRunner.instance);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        InputMap inputMap = scrollPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "do-nothing");
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "do-nothing");
        inputMap.put(KeyStroke.getKeyStroke("UP"), "do-nothing");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "do-nothing");

        add(scrollPane);
    }

    private Dimension findCurrentScreenDimension() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        return toolkit.getScreenSize();
    }

    public void moveHorizontalScroll(int xNewPosition){
        if(xNewPosition > movementOffset){
            scrollPane.getHorizontalScrollBar().setValue(xNewPosition - movementOffset);
        }
        xPaintingPosition = scrollPane.getHorizontalScrollBar().getValue();
    }

    public int getXPaintingPosition(){
        return xPaintingPosition;
    }

}