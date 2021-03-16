package main.java;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class PickUp extends Rectangle {
    private static final long serialVersionUID = 1L;

    public enum Type {
        COIN(1), COIN_MULTIPLE(2), POWER(3), LIFE(4);
        private int id;

        private Type(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
        public static Type typeById(int id){
            for(Type t : values()){
                if(t.id == id){
                    return t;
                }
            }
            return null;
        }
    }

    public static final int COIN = 0;
    public static final int MOOSHROOM = 1;
    public static final int FLOWER = 2;
    public static final int LIFE = 3;
    public static final int STAR = 4;
    public static final int PICKUP_COUNT = 5;

    private BufferedImage sprite;
    private Type type;
    private boolean active;


    public PickUp(Type type){
        this.type = type;
        findSprite();
        
    }

    public void paintPickUp(Graphics g){
        if(active){
            g.drawImage(sprite, x, y, GameRunner.instance);
        }
    }

    public void spawnPickUp(Point p){
        x = p.x;
        y = p.y;
        active = true;
    }

    private void findSprite(){
        if(type.equals(Type.COIN) || type.equals(Type.COIN_MULTIPLE)){
            sprite = SpriteAssets.getPickUpSprite(PickUp.COIN);
        }else if(type.equals(Type.POWER)){
            sprite = SpriteAssets.getPickUpSprite(PickUp.MOOSHROOM);
        }else if(type.equals(Type.LIFE)){
            sprite = SpriteAssets.getPickUpSprite(PickUp.LIFE);
        }
        
    }


}
