package main.java;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Score {
    private static Score instance;
    private static BufferedImage miniCoin;
    private static BufferedImage marioSprite;
    private Font textFont;
    private int points;
    private int coins;
    private int time;
    private int world;
    private int level;
    private int lives;
    private int middleScreen;
    
    public Score(){
        instance = this;
        world = 1;
        level = 1;
        lives = 3;
        textFont = TextFont.getFont();
        textFont = textFont.deriveFont(Font.PLAIN, 24);
        if(miniCoin==null){
            miniCoin = SpriteAssets.getMiniCoin();
        }
        if(marioSprite==null){
            marioSprite = SpriteAssets.getMarioSprite(Animator.M_SMALL_RIGHT_IDLE);
        }
    }

    public void addToPoints(int toAdd){
        points += toAdd;
    }

    public void addToCoins(int toAdd) {
        coins += toAdd;
    }

    public void increaseLives(){
        lives++;
    }

    public void decreaseLives(){
        lives--;
    }

    public void restartTime() {
        time = 0;
    }

    public void setWorld(int w){
        world = w;
    }

    public void setLevel(int l){
        level = l;
    }

    public void paint(Graphics g, int offset){
        middleScreen = WindowManager.windowWidth / 2 + offset;
        g.setFont(textFont);
        g.setColor(Color.WHITE);
        g.drawString("MARIO", middleScreen - 300, 40);
        g.drawString(String.format("%06d", points), middleScreen - 300, 65);
        g.drawImage(miniCoin, middleScreen - 120, 40, GameRunner.instance);
        g.drawString(String.format("x%02d", coins), middleScreen - 100, 65);
        g.drawString("WORLD", middleScreen + 30, 40);
        g.drawString(String.format("%d-%d", world, level), middleScreen + 53, 65);
        g.drawString("TIME", middleScreen + 205, 40);
        g.drawString(String.format("%03d", time), middleScreen + 230, 65);
    }

    public void paintFullDetails(Graphics g, int lvlId){
        g.setColor(Color.BLACK);
        g.fillRect(middleScreen-WindowManager.windowWidth/2, 0, WindowManager.windowWidth, WindowManager.WINDOW_HEIGHT);
        g.setFont(textFont);
        g.setColor(Color.WHITE);
        g.drawString(String.format("WORLD  %d-%d", world, level), middleScreen - 130, 300);
        g.drawImage(marioSprite, middleScreen - 100, 350, GameRunner.instance);
        g.drawString(String.format("x  %d", lives), middleScreen-10, 405);    
    }

    public static Score getInstance(){
        return instance;
    }
}
