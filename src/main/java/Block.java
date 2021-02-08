package main.java;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Block extends Rectangle {

    private static final long serialVersionUID = 1L;

    public static final int EMPTY = 0;
    public static final int GROUND = 1;
    public static final int BREAKABLE = 2;
    public static final int MISTERY = 3;
    public static final int SOLID = 4;
    public static final int PIPE_B_LEFT = 5;
    public static final int PIPE_B_RIGHT = 6;
    public static final int PIPE_T_LEFT = 7;
    public static final int PIPE_T_RIGHT = 8;
    public static final int USED = 9;
    public static final int FLAG_POST = 10;
    public static final int FLAG_TOP = 11;

    public static final int BREAKABLE_ANIM1 = 13;
    public static final int BREAKABLE_ANIM2 = 14;
    public static final int BREAKABLE_ANIM3 = 15;

    public static final int BLOCK_COUNT = 16;

    public static final int SIZE = WindowManager.WINDOW_HEIGHT / 14;

    protected BufferedImage currentSprite;
    
    private byte id;

    public Block(Point position, int id) {
        this.id = (byte) id;
        currentSprite = SpriteAssets.getBlockSprite(id);
        setBounds(position.x, position.y, SIZE, SIZE);
    }

    public void paintBlock(Graphics g) {
        if (id != EMPTY) {
            g.drawImage(currentSprite, x, y, GameRunner.instance);
        }
    }

    public int getId() {
        return id;
    }

    public void deactivateBlock(){
        currentSprite = SpriteAssets.getBlockSprite(EMPTY);
        id = 0;
    }

}
