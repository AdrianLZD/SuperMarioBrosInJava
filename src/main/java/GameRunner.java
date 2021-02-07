package main.java;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class GameRunner extends JPanel implements KeyListener {

    private static final long serialVersionUID = 001L;

    public static final GameRunner instance = new GameRunner();

    private WindowManager window;
    private MainThread mainThread;
    private GameState currentGameState;

    private GameRunner() {
        super();
        addKeyListener(this);
        setVisible(true);
        setFocusable(true);
    }

    public void definePanelSize(Dimension dimension) {
        setPreferredSize(dimension);
    }

    public void paintComponent(Graphics g) {
        currentGameState.paintElements(g);
        g.setClip(window.getXPaintingPosition(), getY(), window.getWidth(), window.getHeight());
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

    public void tick(){
        repaint();
        currentGameState.tick();
    }

    public void setCurrentGameState(GameState gameState){
        currentGameState = gameState;
    }

    public void initializeGameRunner(WindowManager wManager){
        attachRunnerToMainThread();
        GameState menuState = new GameStateMenu();
        setCurrentGameState(menuState);
        window = wManager;
    }

    private void attachRunnerToMainThread() {
        mainThread = new MainThread("MarioThread");
        mainThread.gameRunner = this;
    }


    public void moveHorizontalScroll(int newPosition){
        window.moveHorizontalScroll(newPosition);
    }
}
