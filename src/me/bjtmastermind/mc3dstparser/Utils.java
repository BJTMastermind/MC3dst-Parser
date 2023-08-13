package me.bjtmastermind.mc3dstparser;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class Utils {

    static BufferedImage ABGRToImage(int[] imageData, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < imageData.length; i++) {
            int pixel = imageData[i];
            int a = (pixel >> 0) & 0xFF;
            int b = (pixel >> 8) & 0xFF;
            int g = (pixel >> 16) & 0xFF;
            int r = (pixel >> 24) & 0xFF;

            int argb = (a << 24) | (r << 16) | (g << 8) | (b << 0);
            pixels[i] = argb;
        }
        return img;
    }

    static BufferedImage BGRToImage(BigInteger[] imageData, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
        for (int i = 0, j = 0; i < imageData.length; i++) {
            int val = imageData[i].intValueExact();
            int b = (val >> 16) & 0xFF;
            int g = (val >> 8) & 0xFF;
            int r = (val >> 0) & 0xFF;

            int argb = (r << 16) | (g << 8) | (b << 0);
            pixels[j++] = argb;
        }
        return img;
    }

    static HashMap<Integer, int[]> generateGridCoords(int width, int height) {
        HashMap<Integer, int[]> gridCoords = new HashMap<>();
        int gridX = 0;
        int gridY = 0;
        for (int y : generateColIndexes(width, height)) {
            for (int x : generateRowIndexes(width)) {
                gridCoords.put(x+y, new int[] {gridX, gridY});
                gridX++;
                if (gridX >= width) {
                    gridX = 0;
                }
            }
            gridY++;
        }
        return gridCoords;
    }

    static BufferedImage rescramble(BufferedImage src, boolean descramble) {
        BufferedImage out = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        HashMap<Integer, int[]> gridCoordsMappings = Utils.generateGridCoords(src.getWidth(), src.getHeight());
        int index = 0;
        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                int[] gridCoords = gridCoordsMappings.get(index);
                if (!descramble) {
                    out.setRGB(x, y, src.getRGB(gridCoords[0], gridCoords[1]));
                } else {
                    out.setRGB(gridCoords[0], gridCoords[1], src.getRGB(x, y));
                }
                index++;
            }
        }
        return out;
    }

    static BufferedImage resizeToPowerOfTwo(BufferedImage src) {
        BufferedImage out = new BufferedImage(Utils.toClosestPowerOfTwo(src.getWidth()), Utils.toClosestPowerOfTwo(src.getHeight()), src.getType());
        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                out.setRGB(x, y, src.getRGB(x, y));
            }
        }
        return out;
    }

    static BufferedImage RGBA5551ToImage(short[] imageData, int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < imageData.length; i++) {
            short pixel = imageData[i];
            int r5 = pixel >> 11;
            int g5 = (pixel >> 6) & 0b11111;
            int b5 = (pixel >> 1) & 0b11111;
            int a1 = pixel & 0b1;

            int r8 = Math.round((r5 / 31f) * 255f);
            int g8 = Math.round((g5 / 31f) * 255f);
            int b8 = Math.round((b5 / 31f) * 255f);
            int a8 = Math.round((a1 / 1f) * 255f);

            int argb = (a8 << 24) | (r8 << 16) | (g8 << 8) | (b8 << 0);
            pixels[i] = argb;
        }
        return img;
    }

    static int toClosestPowerOfTwo(int num) {
        if ((num != 0) && ((num & (num - 1)) == 0)) {
            return num;
        }
        num--;
        num |= num >> 1;
        num |= num >> 2;
        num |= num >> 4;
        num |= num >> 8;
        num |= num >> 16;
        num++;
        return num;
    }

    static BufferedImage verticalFlipImage(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage flipped = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = flipped.createGraphics();
        g2d.drawImage(image, 0, h, w, -h, null);
        g2d.dispose();
        return flipped;
    }

    private static ArrayList<Integer> generateColIndexes(int width, int height) {
        int[] yAddBy = {2, 6, 2, 22, 2, 6, 2, ((width * height) / (height / 2) * 4) - 42};
        ArrayList<Integer> indexList = new ArrayList<>();
        indexList.add(0);
        int lastIdx = 0;
        for (int i = 0; i < height - 1; i++) {
            indexList.add(lastIdx + yAddBy[wrap(i)]);
            lastIdx = indexList.get(indexList.size() - 1);
        }
        return indexList;
    }

    private static ArrayList<Integer> generateRowIndexes(int width) {
        int[] xAddBy = {1, 3, 1, 11, 1, 3, 1, 43};
        ArrayList<Integer> indexList = new ArrayList<>();
        indexList.add(0);
        int lastIdx = 0;
        for (int i = 0; i < width - 1; i++) {
            indexList.add(lastIdx + xAddBy[wrap(i)]);
            lastIdx = indexList.get(indexList.size() - 1);
        }
        return indexList;
    }

    private static int wrap(int num) {
        return num >= 0 ? num % 8 : (num % 8 + 8);
    }
}
