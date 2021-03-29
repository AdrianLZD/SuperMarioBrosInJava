package main.java;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;

public class SpriteAssets {

    private static final int BACKGROUND_MENU = 0, 
                             BACKGROUND_LVL1 = 1, 
                             BACKGROUND_LVL2 = 2, 
                             BACKGROUND_LVL3 = 3,
                             BACKGROUND_LVL4 = 4;

    public static final int PREVIOUS_WINDOW_HEIGHT = 896;

    private static BufferedImage gameLogo;
    private static BufferedImage miniCoin;
    private static BufferedImage[] backgrounds;
    private static BufferedImage[] blockSprites;
    private static BufferedImage[] marioSprites;
    private static BufferedImage[] pickUps;

    static {
        try {
            loadGameLogo();
            loadMiniCoin();
            loadBackgrounds();
            loadBlockSprites();
            loadMarioSprites();
            loadPickUpSprites();
            System.gc();
        } catch (IOException e) {
            ErrorLogger.logErrorMessage("The sprites of the game could not be loaded.", e);
        }
    }

    private static void loadGameLogo() throws IOException {
        File logoFile = new File("res/backgrounds/logo.png");
        gameLogo = ImageIO.read(logoFile);
    }

    private static void loadMiniCoin() throws IOException{
        File miniCoinFile = new File("res/objects/oCoinMini.png");
        miniCoin = ImageIO.read(miniCoinFile);
    }

    private static void loadBackgrounds() throws IOException {
        backgrounds = new BufferedImage[5];
        backgrounds[BACKGROUND_MENU] = ImageIO.read(new File("res/backgrounds/menu.png"));
        backgrounds[BACKGROUND_LVL1] = ImageIO.read(new File("res/backgrounds/lvl1.png"));
        backgrounds[BACKGROUND_LVL2] = ImageIO.read(new File("res/backgrounds/lvl2.png"));
        backgrounds[BACKGROUND_LVL3] = ImageIO.read(new File("res/backgrounds/lvl3.png"));
        backgrounds[BACKGROUND_LVL4] = ImageIO.read(new File("res/backgrounds/lvl4.png"));

        rescaleSprites(backgrounds);
    }


    private static void loadBlockSprites() throws IOException {
        blockSprites = new BufferedImage[Block.BLOCK_COUNT];
        blockSprites[Block.EMPTY] = null;
        blockSprites[Block.GROUND] = ImageIO.read(new File("res/blocks/bGround.png"));
        blockSprites[Block.BREAKABLE] = ImageIO.read(new File("res/blocks/bBreakable.png"));
        blockSprites[Block.MISTERY] = ImageIO.read(new File("res/blocks/bMistery.png"));
        blockSprites[Block.SOLID] = ImageIO.read(new File("res/blocks/bSolid.png"));
        blockSprites[Block.PIPE_B_LEFT] = ImageIO.read(new File("res/blocks/bPipeBottomLeft.png"));
        blockSprites[Block.PIPE_B_RIGHT] = ImageIO.read(new File("res/blocks/bPipeBottomRight.png"));
        blockSprites[Block.PIPE_T_LEFT] = ImageIO.read(new File("res/blocks/bPipeTopLeft.png"));
        blockSprites[Block.PIPE_T_RIGHT] = ImageIO.read(new File("res/blocks/bPipeTopRight.png"));
        blockSprites[Block.USED] = ImageIO.read(new File("res/blocks/bUsed.png"));
        blockSprites[Block.FLAG_POST] = ImageIO.read(new File("res/blocks/bFlagPost.png"));
        blockSprites[Block.FLAG_TOP] = ImageIO.read(new File("res/blocks/bFlagTop.png"));

        blockSprites[Block.BREAKABLE_ANIM1] = ImageIO.read(new File("res/blocks/bBroken1.png"));
        blockSprites[Block.BREAKABLE_ANIM2] = ImageIO.read(new File("res/blocks/bBroken2.png"));
        blockSprites[Block.BREAKABLE_ANIM3] = ImageIO.read(new File("res/blocks/bBroken3.png"));

        rescaleSprites(blockSprites, Block.SIZE, Block.SIZE);
    }

    private static void loadMarioSprites() throws IOException{
        marioSprites = new BufferedImage[Animator.MARIO_SPRITE_COUNT];
        marioSprites[Animator.M_DEAD] = ImageIO.read(new File("res/mario/mDead.png"));
        marioSprites[Animator.M_SMALL_RIGHT_IDLE] = ImageIO.read(new File("res/mario/mSmallRightIdle.png"));
        marioSprites[Animator.M_SMALL_RIGHT_WALK1] = ImageIO.read(new File("res/mario/mSmallRightWalk1.png"));
        marioSprites[Animator.M_SMALL_RIGHT_WALK2] = ImageIO.read(new File("res/mario/mSmallRightWalk2.png"));
        marioSprites[Animator.M_SMALL_RIGHT_JUMP] = ImageIO.read(new File("res/mario/mSmallRightJump.png"));
        marioSprites[Animator.M_SMALL_RIGHT_FLAG] = ImageIO.read(new File("res/mario/mSmallRightFlag.png"));
        marioSprites[Animator.M_SMALL_RIGHT_TRANSITION] = ImageIO.read(new File("res/mario/mSmallRightTransition.png"));
        marioSprites[Animator.M_SMALL_LEFT_IDLE] = ImageIO.read(new File("res/mario/mSmallLeftIdle.png"));
        marioSprites[Animator.M_SMALL_LEFT_WALK1] = ImageIO.read(new File("res/mario/mSmallLeftWalk1.png"));
        marioSprites[Animator.M_SMALL_LEFT_WALK2] = ImageIO.read(new File("res/mario/mSmallLeftWalk2.png"));
        marioSprites[Animator.M_SMALL_LEFT_JUMP] = ImageIO.read(new File("res/mario/mSmallLeftJump.png"));
        marioSprites[Animator.M_SMALL_LEFT_FLAG] = ImageIO.read(new File("res/mario/mSmallLeftFlag.png"));
        marioSprites[Animator.M_SMALL_LEFT_TRANSITION] = ImageIO.read(new File("res/mario/mSmallLefttransition.png"));

        marioSprites[Animator.M_BIG_RIGHT_IDLE] = ImageIO.read(new File("res/mario/mBigRightIdle.png"));
        marioSprites[Animator.M_BIG_RIGHT_WALK1] = ImageIO.read(new File("res/mario/mBigRightWalk1.png"));
        marioSprites[Animator.M_BIG_RIGHT_WALK2] = ImageIO.read(new File("res/mario/mBigRightWalk2.png"));
        marioSprites[Animator.M_BIG_RIGHT_JUMP] = ImageIO.read(new File("res/mario/mBigRightJump.png"));
        marioSprites[Animator.M_BIG_RIGHT_FLAG] = ImageIO.read(new File("res/mario/mBigRightFlag.png"));
        marioSprites[Animator.M_BIG_LEFT_IDLE] = ImageIO.read(new File("res/mario/mBigLeftIdle.png"));
        marioSprites[Animator.M_BIG_LEFT_WALK1] = ImageIO.read(new File("res/mario/mBigLeftWalk1.png"));
        marioSprites[Animator.M_BIG_LEFT_WALK2] = ImageIO.read(new File("res/mario/mBigLeftWalk2.png"));
        marioSprites[Animator.M_BIG_LEFT_JUMP] = ImageIO.read(new File("res/mario/mBigLeftJump.png"));
        marioSprites[Animator.M_BIG_LEFT_FLAG] = ImageIO.read(new File("res/mario/mBigLeftFlag.png"));

        marioSprites[Animator.M_FIRE_RIGHT_IDLE] = ImageIO.read(new File("res/mario/mFireRightIdle.png"));
        marioSprites[Animator.M_FIRE_RIGHT_WALK1] = ImageIO.read(new File("res/mario/mFireRightWalk1.png"));
        marioSprites[Animator.M_FIRE_RIGHT_WALK2] = ImageIO.read(new File("res/mario/mFireRightWalk2.png"));
        marioSprites[Animator.M_FIRE_RIGHT_JUMP] = ImageIO.read(new File("res/mario/mFireRightJump.png"));
        marioSprites[Animator.M_FIRE_RIGHT_FLAG] = ImageIO.read(new File("res/mario/mFireRightFlag.png"));
        marioSprites[Animator.M_FIRE_LEFT_IDLE] = ImageIO.read(new File("res/mario/mFireLeftIdle.png"));
        marioSprites[Animator.M_FIRE_LEFT_WALK1] = ImageIO.read(new File("res/mario/mFireLeftWalk1.png"));
        marioSprites[Animator.M_FIRE_LEFT_WALK2] = ImageIO.read(new File("res/mario/mFireLeftWalk2.png"));
        marioSprites[Animator.M_FIRE_LEFT_JUMP] = ImageIO.read(new File("res/mario/mFireLeftJump.png"));
        marioSprites[Animator.M_FIRE_LEFT_FLAG] = ImageIO.read(new File("res/mario/mFireLeftFlag.png"));

        rescaleSprites(marioSprites);
    }

    private static void loadPickUpSprites() throws IOException{
        pickUps = new BufferedImage[PickUp.PICKUP_COUNT];
        pickUps[PickUp.COIN] = ImageIO.read(new File("res/objects/oCoin.png"));
        pickUps[PickUp.MOOSHROOM] = ImageIO.read(new File("res/objects/oShroom.png"));
        pickUps[PickUp.FLOWER] = ImageIO.read(new File("res/objects/oFlower.png"));
        pickUps[PickUp.LIFE] = ImageIO.read(new File("res/objects/oShroomLife.png"));
        pickUps[PickUp.STAR] = ImageIO.read(new File("res/objects/oStar.png"));

        rescaleSprites(pickUps);
    }

    private static void rescaleSprites(BufferedImage[] images) {
        for (int i = 0; i < images.length; i++) {
            if(images[i]!=null){
                int newHeight = WindowManager.WINDOW_HEIGHT * images[i].getHeight() / PREVIOUS_WINDOW_HEIGHT;
                int newWidth = images[i].getWidth() * newHeight / images[i].getHeight();
                images[i] = createRescaledImage(images[i], newWidth, newHeight);
            }
        }
    }

    private static void rescaleSprites(BufferedImage[] toResize, int width, int height) {
        for (int i = 0; i < toResize.length; i++) {
            if (toResize[i] != null) {
                toResize[i] = createRescaledImage(toResize[i], width, height);
            }
        }
    }

    private static BufferedImage createRescaledImage(Image original, int newWidth, int newHeight) {
        BufferedImage rescaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Image image = original.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
        rescaledImage.getGraphics().drawImage(image, 0, 0, null);
        return rescaledImage;
    }

    public static BufferedImage getMarioSprite(int id){
        return marioSprites[id];
    }

    public static BufferedImage getLogo() {
        return gameLogo;
    }

    public static BufferedImage getMiniCoin(){
        return miniCoin;
    }

    public static BufferedImage getBackground(String name) {
        switch (name) {
            case "menu":
                return backgrounds[BACKGROUND_MENU];
            case "lvl1":
                return backgrounds[BACKGROUND_LVL1];
            case "lvl2":
                return backgrounds[BACKGROUND_LVL1];
            case "lvl3":
                return backgrounds[BACKGROUND_LVL1];
            case "lvl4":
                return backgrounds[BACKGROUND_LVL1];
            default:
                throw new NoSuchElementException();
        }

    }

    public static BufferedImage getBlockSprite(int id) {
        return blockSprites[id];
    }

    public static BufferedImage getPickUpSprite(int id){
        return pickUps[id];
    }

}
