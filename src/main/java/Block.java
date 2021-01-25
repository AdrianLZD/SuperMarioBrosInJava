package main.java;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Block extends Rectangle{

    private static final long serialVersionUID = 1L;
    
    public static final int EMPTY = 0,
                            GROUND = 1,
                            BREAKABLE = 2,
                            MISTERY = 3,
                            SOLID = 4,
                            PIPE_B_LEFT = 5,
                            PIPE_B_RIGHT = 6,
                            PIPE_T_LEFT = 7,
                            PIPE_T_RIGHT = 8,
                            USED = 9,
                            FLAG_POST = 10,
                            FLAG_TOP = 11;
    public static final int BLOCK_COUNT = 12;
    public static final int SIZE = WindowManager.WINDOW_HEIGHT/14;

    private BufferedImage currentSprite;
    private int id;

    public Block(Point position, int id){
        this.id = id;
        currentSprite = SpriteAssets.getBlockSprite(id);
        setBounds(position.x, position.y, SIZE, SIZE);
    }

    public void paintBlock(Graphics g){
        if(id!=EMPTY){
            g.drawImage(currentSprite, x, y, GameRunner.instance);
        }
    }
}
