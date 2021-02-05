package main.java;

import java.awt.event.KeyEvent;

public class MarioController {
    private final int maxJumpTime = 32;
    private final int walkSpeed = 5;
    private final int jumpSpeed = 8;
    private final int gravity = PhysicObject.getGravity();

    private Mario mario;
    
    private int horizontalVelocity;
    private int verticalVelocity;
    private int jumpTime;
    private boolean moveRight;
    private boolean moveLeft;
    private boolean jumping;
    

    public MarioController(Mario mario){
        this.mario = mario;
        verticalVelocity = gravity;
    }

    public void keyPressed(int k){
        if (k == KeyEvent.VK_UP) {
            upKeyPressed();
        } else if (k == KeyEvent.VK_LEFT) {
            leftKeyPressed();
        } else if (k == KeyEvent.VK_RIGHT) {
            rightKeyPressed();
        }
    }

    private void upKeyPressed() {
        if(mario.isGrounded()) {
            activateJump();
        }
    }

    private void activateJump(){
        jumpTime = 0;
        jumping = true;
        mario.setGrounded(false);
        mario.setLocation(mario.x, mario.y-jumpSpeed);
    }

    private void leftKeyPressed() {
        moveLeft = true;
    }

    private void rightKeyPressed() {
        moveRight = true;
	}    

    public void keyReleased(int k) {
        if (k == KeyEvent.VK_UP) {
            upKeyReleased();
        } else if (k == KeyEvent.VK_LEFT) {
            leftKeyReleased();
        } else if (k == KeyEvent.VK_RIGHT) {
            rightKeyReleased();
        }
    }

    private void upKeyReleased() {
        deactivateJump();
    }

    private void deactivateJump() {
        jumping = false;
    }

    private void leftKeyReleased() {
        moveLeft = false;
    }

    private void rightKeyReleased() {
        moveRight = false;
    }

    public void tick(){
        if(jumping)
            jump();

        applyVelocities();
    }

    public void jump(){
        if(jumpTime > maxJumpTime){
            deactivateJump();
        }
        jumpTime++;
    }

    private void applyVelocities() {
        
        if(jumping){
            //System.out.println("jump");
            verticalVelocity = -jumpSpeed;
        }else if(mario.isGrounded()){
            //System.out.println("ground");
            verticalVelocity = 0;
        }else{
            //System.out.println("falling");
            verticalVelocity = gravity;
        }
        

        if(moveRight)
            horizontalVelocity = walkSpeed;
        else if(moveLeft)
            horizontalVelocity = -walkSpeed;
        else
            horizontalVelocity = 0;
        
        mario.setLocation(mario.x + horizontalVelocity, mario.y + verticalVelocity);
        
    }

    public boolean isMoving(){
        return moveRight || moveLeft;
    }

    

    
}
