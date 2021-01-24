package main.java;

import java.awt.Point;
import java.awt.Rectangle;

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
    public static final int SIZE = 64;

    private int id;

    public Block(Point position, int id){
        this.id = id;
        setBounds(position.x, position.y, SIZE, SIZE);
    }

    public void paintBlock(){
        if(id!=EMPTY){
            //TODO paint block
        }
    }
}
