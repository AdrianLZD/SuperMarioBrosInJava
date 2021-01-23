package main.java;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class GameRunner extends JPanel implements KeyListener {

    private static final long serialVersionUID = 001L;

    public static final GameRunner instance = new GameRunner();

    private MainThread mainThread;
    private GameState currentGameState;


    public GameRunner() {
        super();
        //setBackground(Color.BLACK);
        addKeyListener(this);
        setVisible(true);
        setFocusable(true);
        definePanelSize();

        mainThread = new MainThread("MarioThread");
        currentGameState = new GameStateMenu();
    }

    private void definePanelSize() {
        int panelWidth = (int) WindowManager.findCurrentScreenDimension().getWidth();
        Dimension panelDimension = new Dimension(panelWidth, WindowManager.WINDOW_HEIGHT);
        setPreferredSize(panelDimension);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        currentGameState.paintElements(g);
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            mainThread.resumeThread();
        } else if (e.getKeyCode() == KeyEvent.VK_K) {
            mainThread.pauseThread();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void attachRunnerToMainThread(){
        mainThread.gameRunner = this;
    }
}
