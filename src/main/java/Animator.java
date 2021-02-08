package main.java;

import java.awt.image.BufferedImage;

public class Animator {

    public static final int EMPTY = 0;

    public static final int ANIMATION_SPEED = 6;

    public static final int M_DEAD = 1;
    public static final int M_SMALL_RIGHT_IDLE = 2;
    public static final int M_SMALL_RIGHT_WALK1 = 3;
    public static final int M_SMALL_RIGHT_WALK2 = 4;
    public static final int M_SMALL_RIGHT_JUMP = 5;
    public static final int M_SMALL_RIGHT_FLAG = 6;
    public static final int M_SMALL_RIGHT_TRANSITION = 7;
    public static final int M_SMALL_LEFT_IDLE = 8;
    public static final int M_SMALL_LEFT_WALK1 = 9;
    public static final int M_SMALL_LEFT_WALK2 = 10;
    public static final int M_SMALL_LEFT_JUMP = 11;
    public static final int M_SMALL_LEFT_FLAG = 12;
    public static final int M_SMALL_LEFT_TRANSITION = 13;

    public static final int M_BIG_RIGHT_IDLE = 14;
    public static final int M_BIG_RIGHT_WALK1 = 15;
    public static final int M_BIG_RIGHT_WALK2 = 16;
    public static final int M_BIG_RIGHT_JUMP = 17;
    public static final int M_BIG_RIGHT_FLAG = 18;
    public static final int M_BIG_LEFT_IDLE = 19;
    public static final int M_BIG_LEFT_WALK1 = 20;
    public static final int M_BIG_LEFT_WALK2 = 21;
    public static final int M_BIG_LEFT_JUMP = 22;
    public static final int M_BIG_LEFT_FLAG = 23;

    public static final int M_FIRE_RIGHT_IDLE = 24;
    public static final int M_FIRE_RIGHT_WALK1 = 25;
    public static final int M_FIRE_RIGHT_WALK2 = 26;
    public static final int M_FIRE_RIGHT_JUMP = 27;
    public static final int M_FIRE_RIGHT_FLAG = 28;
    public static final int M_FIRE_LEFT_IDLE = 29;
    public static final int M_FIRE_LEFT_WALK1 = 30;
    public static final int M_FIRE_LEFT_WALK2 = 31;
    public static final int M_FIRE_LEFT_JUMP = 32;
    public static final int M_FIRE_LEFT_FLAG = 33;

    public static final int MARIO_SPRITE_COUNT = 34;

    public static BufferedImage getMarioSprite(int id){
        return SpriteAssets.getMarioSprite(id);
    }

    public static BufferedImage getBlockSprite(int id){
        return SpriteAssets.getBlockSprite(id);
    }
}
