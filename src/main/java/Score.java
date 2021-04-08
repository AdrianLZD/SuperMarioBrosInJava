package main.java;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Score {
    private static Score instance;
    private static BufferedImage miniCoin;
    private Font textFont;
    private int points;
    private int coins;
    private int time;
    private int world;
    private int level;
    private int middleScreen;
    
    public Score(){
        instance = this;
        world = 1;
        level = 1;
        textFont = TextFont.getFont();
        textFont = textFont.deriveFont(Font.PLAIN, 24);
        if(miniCoin==null){
            miniCoin = SpriteAssets.getMiniCoin();
        }
    }

    public void addToPoints(int toAdd){
        points += toAdd;
    }

    public void addToCoins(int toAdd) {
        coins += toAdd;
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
        g.setFont(textFont);
        g.setColor(Color.WHITE);
        middleScreen = WindowManager.windowWidth / 2 + offset;
        paintScore(g);
        paintCoins(g);  
        paintWorld(g);
        paintTime(g);
    }

    private void paintScore(Graphics g){
        g.drawString("MARIO",  middleScreen - 300, 40);
        g.drawString(String.format("%06d", points), middleScreen - 300, 65);
    }

    private void paintCoins(Graphics g){
        g.drawImage(miniCoin, middleScreen - 120, 40, GameRunner.instance);
        g.drawString(String.format("x%02d", coins), middleScreen - 100, 65);
    }

    private void paintWorld(Graphics g){
        g.drawString("WORLD", middleScreen + 30, 40);
        g.drawString(String.format("%d-%d",world,level), middleScreen + 53, 65);
    }

    private void paintTime(Graphics g){
        g.drawString("TIME", middleScreen + 205, 40);
        g.drawString(String.format("%03d", time), middleScreen+230, 65);
    }

    public static Score getInstance(){
        return instance;
    }
}
