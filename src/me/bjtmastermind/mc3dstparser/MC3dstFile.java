package me.bjtmastermind.mc3dstparser;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class MC3dstFile {
    String filename;
    int header;
    int version;
    ColorFormat type;
    int width;
    int height;
    int originalWidth;
    int originalHeight;
    BufferedImage image;

    public void assemble(BufferedImage image, ColorFormat format, String outputFilepath) {
        int imgOrigWidth = image.getWidth();
        int imgOrigHeight = image.getHeight();
        int imgWidth = Utils.toClosestPowerOfTwo(imgOrigWidth);
        int imgHeight = Utils.toClosestPowerOfTwo(imgOrigHeight);
        BufferedImage scrambledImage = Utils.rescramble(Utils.verticalFlipImage(Utils.resizeToPowerOfTwo(image)), false);

        int multiplier = 0;
        switch (format) {
            case ETC2_RGBA8:
                multiplier = 4;
                break;
            case RGB:
                multiplier = 3;
                break;
            case ABGR1555:
                multiplier = 2;
                break;
            default:
                System.err.println("Detected unknown texture type!");
        }
        int arraySize = 32 + (imgWidth * imgHeight) * multiplier;
        ByteBuffer rawOutput = ByteBuffer.wrap(new byte[arraySize]).order(ByteOrder.LITTLE_ENDIAN);
        WritableByteChannel channel = Channels.newChannel(new DataOutputStream(new ByteArrayOutputStream()));

        try {
            // Heading
            rawOutput.putInt(0x54534433); // reversed. Flips the right way when written to file.
            rawOutput.putInt(0x03);
            rawOutput.putInt(format.value());
            rawOutput.putInt(imgWidth);
            rawOutput.putInt(imgHeight);
            rawOutput.putInt(imgOrigWidth);
            rawOutput.putInt(imgOrigHeight);
            rawOutput.putInt(0x01);

            // Image Data
            loopEscape:
            for (int y = 0; y < imgHeight; y++) {
                for (int x = 0; x < imgWidth; x++) {
                    Color pixel = new Color(scrambledImage.getRGB(x, y), (format != ColorFormat.RGB));
                    switch (format) {
                        case ETC2_RGBA8:
                            rawOutput.put((byte) pixel.getAlpha());
                            rawOutput.put((byte) pixel.getBlue());
                            rawOutput.put((byte) pixel.getGreen());
                            rawOutput.put((byte) pixel.getRed());
                            break;
                        case RGB:
                            rawOutput.put((byte) pixel.getBlue());
                            rawOutput.put((byte) pixel.getGreen());
                            rawOutput.put((byte) pixel.getRed());
                            break;
                        case ABGR1555:
                            int a1 = pixel.getAlpha() > 0 ? 1 : 0;
                            int r5 = Math.round((pixel.getRed() / 255f) * 31f);
                            int g5 = Math.round((pixel.getGreen() / 255f) * 31f);
                            int b5 = Math.round((pixel.getBlue() / 255f) * 31f);
                            rawOutput.putShort((short) ((r5 << 11) | (g5 << 6) | (b5 << 1) | (a1 << 0)));
                            break;
                        default:
                            System.err.println("Invaild Texture type "+format.value()+" found!");
                            break loopEscape;
                    }
                }
            }
            channel.write(rawOutput);
            File output = new File(outputFilepath);
            Files.write(output.toPath(), rawOutput.array());
            System.out.println("Assembled File Successful!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void extractToPNG(String outputPath) {
        if (this.filename == null) {
            System.err.println("Can't extract image because a 3dst file has not been parsed yet.\nUse the parse method to parse a 3dst image before calling this function.");
            return;
        }
        try {
            ImageIO.write(this.image, "PNG", new File(outputPath));
            System.out.println("Extracted File to "+outputPath);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public MC3dstFile parse(String filepath) throws IOException {
        this.filename = filepath;
        byte[] bytes = Files.readAllBytes(Paths.get(new File(filepath).toURI()));

        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);

        // Heading
        this.header = buffer.getInt();
        if (this.header != 0x54534433 /*3DST*/) {
            System.out.println("Invaild header found! found: "+this.header);
            return null;
        }
        this.version = buffer.getInt();
        this.type = ColorFormat.formatFromId(buffer.getInt());
        this.width = buffer.getInt();
        this.height = buffer.getInt();
        this.originalWidth = buffer.getInt();
        this.originalHeight = buffer.getInt();
        buffer.getInt(); // skip Unknown integer '01 00 00 00'

        // Image Data
        switch (this.type) {
            case ETC2_RGBA8: {
                int[] imageBinary = new int[(this.width*this.height)];
                for (int i = 0; i < (this.width*this.height); i++) {
                    imageBinary[i] = buffer.getInt();
                }
                this.image = Utils.ETC2RGBA8ToImage(imageBinary, this.width, this.height);
                break;
            }
            case RGB: {
                BigInteger[] imageBinary = new BigInteger[(this.width*this.height)];
                for (int i = 0; i < imageBinary.length; i++) {
                    imageBinary[i] = new BigInteger(new byte[] {buffer.get(), buffer.get(), buffer.get()});
                }
                this.image = Utils.RGBToImage(imageBinary, this.width, this.height);
                break;
            }
            case ABGR1555: {
                short[] imageBinary = new short[(this.width*this.height)];
                for (int i = 0; i < (this.width*this.height); i++) {
                    imageBinary[i] = buffer.getShort();
                }
                this.image = Utils.ABGR1555ToImage(imageBinary, this.width, this.height);
                break;
            }
            default:
                System.err.println("Detected unknown texture type!");
                return null;
        }
        this.image = Utils.verticalFlipImage(Utils.rescramble(this.image, true));
        return this;
    }

    public void replace(BufferedImage newImage, ColorFormat format) {
        if (this.filename == null) {
            System.err.println("Can't replace image because a 3dst file has not been parsed yet.\nUse assemble method to create a new 3dst image.");
            return;
        }
        assemble(newImage, format, this.filename);
    }
}
