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

    private GameRunner() {
        super();
        // setBackground(Color.BLACK);
        addKeyListener(this);
        setVisible(true);
        setFocusable(true);
    }

    public void definePanelSize(Dimension dimension) {
        setPreferredSize(dimension);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        currentGameState.paintElements(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        currentGameState.keyPressed(e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_W) {
            mainThread.resumeThread();
        } else if (e.getKeyCode() == KeyEvent.VK_K) {
            mainThread.pauseThread();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        currentGameState.keyReleased(e.getKeyCode());
    }

    public void setCurrentGameState(GameState gameState){
        currentGameState = gameState;
    }

    public void initializeGameRunner(){
        attachRunnerToMainThread();
        GameState menuState = new GameStateMenu();
        setCurrentGameState(menuState);
    }

    private void attachRunnerToMainThread() {
        mainThread = new MainThread("MarioThread");
        mainThread.gameRunner = this;
    }

    
}
