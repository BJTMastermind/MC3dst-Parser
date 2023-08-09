package test.bjtmastermind.mc3dstparser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.bjtmastermind.mc3dstparser.ColorFormat;
import me.bjtmastermind.mc3dstparser.MC3dstFile;


public class Main {

    public static void main(String[] args) {
        MC3dstFile mc3dstFile = new MC3dstFile();
        try {
            // /*Type 0*/ mc3dstFile.parse("/home/bjtmastermind/Desktop/My Desktop/Games/Minecraft/Minecraft 3DS Edition/Minecraft/ExtractedRomFS/resourcepacks/vanilla/images/blocks/diamond_ore.3dst");
            // /*Type 0*/ mc3dstFile.parse("/home/bjtmastermind/Desktop/My Desktop/Games/Minecraft/Minecraft 3DS Edition/Minecraft/ExtractedRomFS/resourcepacks/vanilla/images/lava_still.3dst");
            // /*Type 1*/ mc3dstFile.parse("/home/bjtmastermind/Desktop/My Desktop/Games/Minecraft/Minecraft 3DS Edition/Minecraft/ExtractedRomFS/resourcepacks/vanilla/images/misc/enchanted_item_glint.3dst");
            // /*Type 2*/ mc3dstFile.parse("/home/bjtmastermind/Desktop/My Desktop/Games/Minecraft/Minecraft 3DS Edition/Minecraft/ExtractedRomFS/skins/Base/steve.3dst");
            mc3dstFile.parse("/home/bjtmastermind/Desktop/assembled_output.3dst");

            // BufferedImage image = ImageIO.read(new File("/home/bjtmastermind/Desktop/test_image.png"));
            // mc3dstFile.assemble(image, ColorFormat.ABGR, "/home/bjtmastermind/Desktop/assembled_output.3dst");

            mc3dstFile.extractToPNG("/home/bjtmastermind/Desktop/assembled_output.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
