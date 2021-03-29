package main.java;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

public class GameRunner extends JPanel implements KeyListener {

    private static final long serialVersionUID = 001L;

    public static final GameRunner instance = new GameRunner();

    private WindowManager window;
    private MainThread mainThread;
    private GameState currentGameState;
    private Score scoreManager;

    private Image dbImage;
    private Graphics dbGraphics;

    private int cameraOffset;
    private int cameraX;
    private int imageWidth;
    private int imageHeight;
    private boolean imageUpdate;

    private GameRunner() {
        super();
        addKeyListener(this);
        setVisible(true);
        setFocusable(true);
    }

    public void initializeGameRunner(WindowManager wManager) {
        attachRunnerToMainThread();
        setCurrentGameState(new GameStateMenu());
        scoreManager = new Score();
        window = wManager;
        cameraOffset = WindowManager.windowWidth / 3;
    }

    public void definePanelSize(Dimension dimension) {
        setPreferredSize(dimension);
        imageWidth = (int) dimension.getWidth();
        imageHeight = (int) dimension.getHeight();
        imageUpdate = true;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(dbImage==null || imageUpdate){
            dbImage = createImage(imageWidth, imageHeight);
            dbGraphics = dbImage.getGraphics();
            imageUpdate = false;
        }
        
        //g.translate(-cameraX, 0);      
        currentGameState.paintElements(dbGraphics);
        scoreManager.paint(dbGraphics, cameraX);  
        
        g.setClip(0, getY(), window.getWidth(), window.getHeight());    
        g.drawImage(dbImage, -cameraX, 0, this);  
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

    private void attachRunnerToMainThread() {
        mainThread = new MainThread("MarioThread");
        mainThread.gameRunner = this;
    }


    public void moveHorizontalScroll(int newPosition){
        if (newPosition > cameraOffset) {
            cameraX = newPosition - cameraOffset;
        }
    }
}
