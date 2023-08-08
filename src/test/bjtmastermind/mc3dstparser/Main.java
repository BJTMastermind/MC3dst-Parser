package test.bjtmastermind.mc3dstparser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.bjtmastermind.mc3dstparser.MC3dstFile;


public class Main {

    public static void main(String[] args) {
        MC3dstFile n3dstFile = new MC3dstFile();
        try {
            // /*Type 0*/ n3dstFile.parse("/home/bjtmastermind/Desktop/My Desktop/Games/Minecraft/Minecraft 3DS Edition/Minecraft/ExtractedRomFS/resourcepacks/vanilla/images/blocks/diamond_ore.3dst");
            // /*Type 0*/ n3dstFile.parse("/home/bjtmastermind/Desktop/My Desktop/Games/Minecraft/Minecraft 3DS Edition/Minecraft/ExtractedRomFS/resourcepacks/vanilla/images/lava_still.3dst");
            // /*Type 1*/ n3dstFile.parse("/home/bjtmastermind/Desktop/My Desktop/Games/Minecraft/Minecraft 3DS Edition/Minecraft/ExtractedRomFS/resourcepacks/vanilla/images/misc/enchanted_item_glint.3dst");
            // /*Type 2*/ n3dstFile.parse("/home/bjtmastermind/Desktop/My Desktop/Games/Minecraft/Minecraft 3DS Edition/Minecraft/ExtractedRomFS/skins/Base/steve.3dst");
            n3dstFile.parse("/home/bjtmastermind/Desktop/assembled_output.3dst");

            // BufferedImage image = ImageIO.read(new File("/home/bjtmastermind/Desktop/test1.png"));
            // n3dstFile.assemble(image, ColorFormat.RGBA5551, "/home/bjtmastermind/Desktop/assembled_output.3dst");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
