package main.java;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

public class TextFont {
    private static Font font;

    public static void loadGameFont(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            File fontFile = new File("res/gameFont.ttf");
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, fontFile));
            font = new Font("Press Start 2P", Font.PLAIN, 20);
            //printAvailableFonts();
        } catch (FontFormatException | IOException e) {
            System.out.println("The font game could not be registered.");
            e.printStackTrace();
        }
    }

    public static Font getFont(){
        return font;
    }

    @SuppressWarnings("unsued")
    private static void printAvailableFonts(){
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for(String s : ge.getAvailableFontFamilyNames()){
            System.out.println(s);
        }
    }
}
