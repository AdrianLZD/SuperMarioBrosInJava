package main.java;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import java.awt.Dimension;
import java.awt.Toolkit;

public class WindowManager extends JFrame {
    private static final long serialVersionUID = 001L;

    public static final int WINDOW_HEIGHT = 800;
    public static final String APP_NAME = "Super Mario Bros In Java";

    public static int windowWidth;

    private JScrollPane scrollPane;

    public WindowManager() {
        super(APP_NAME);
        findWindowSize();
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
        gameRunner.initializeGameRunner();

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
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        InputMap inputMap = scrollPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        //inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "do-nothing");
        //inputMap.put(KeyStroke.getKeyStroke("LEFT"), "do-nothing");
        inputMap.put(KeyStroke.getKeyStroke("UP"), "do-nothing");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "do-nothing");

        add(scrollPane);
    }

    public static Dimension findCurrentScreenDimension() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        return toolkit.getScreenSize();
    }
}