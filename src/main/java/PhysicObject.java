package main.java;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class PhysicObject extends Rectangle {

    private static final long serialVersionUID = 1L;
    private static final int gravity = 3;
    private static final int collisionOffset = 1;

    private static ArrayList<Block> mapBlocks = new ArrayList<>();

    protected boolean gravityOn;
    protected boolean grounded;

    protected void tick() {
        if (gravityOn) {
            applyGravity();
            checkIfGrounded();
        }
    }

    protected void applyGravity() {
        setLocation(x, y + gravity);
    }

    protected void checkIfGrounded() {
        if (checkBlockDownCollision()) {
            gravityOn = false;
            grounded = true;
        }
    }

    protected boolean checkBlockDownCollision() {
        Point leftBottom = new Point(x - collisionOffset, y + (int) getHeight() + collisionOffset);
        Point rightBottom = new Point(x + (int) getWidth() + collisionOffset, y + (int) getHeight() + collisionOffset);
        for (Block block : mapBlocks) {
            if (pointCollision(leftBottom, block) || pointCollision(rightBottom, block)) {
                System.out.println("true");
                System.out.println(leftBottom);
                System.out.println(rightBottom);
                return true;

            }
        }
        System.out.println("false");
        return false;
    }

    protected boolean pointCollision(Point point, PhysicObject pObject) {
        return pObject.contains(point);
    }

    protected boolean pointCollision(Point point, Block block) {
        return block.contains(point);
    }

    protected boolean isColliding(PhysicObject pObject) {
        return intersects(pObject);
    }

    public static void addMapBlock(Block block) {
        mapBlocks.add(block);
    }

}
