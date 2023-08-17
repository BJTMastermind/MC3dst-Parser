# Minecraft 3dst Parser

A Minecraft: 3DS Edition 3dst Image File Parser and Assembler

## How To Use

* Download `MC3dst-Parser-v<version>.jar` from [Releases](https://github.com/BJTMastermind/MC3dst-Parser/releases) tab.
* Add the library into your project.
* Import `me.bjtmastermind.mc3dstparser.MC3dstFile` to use.

Example Code:

```java
// Create a new instance of the MC3dstFile
MC3dstFile mc3dstFile = new MC3dstFile();

// Use the parse method to parse a 3dst image
mc3dstFile.parse("/path/to/image.3dst");
// Use the extractPNG method to generate a .png file from the parsed 3dst image
mc3dstFile.extractPNG("/path/to/output.png");

BufferedImage image = ImageIO.read(new File("/path/to/replacementImage.png"));
// Use the replace method to replace the image in a parsed 3dst image from a .png file
// Choose your desired ColorFormat of ABGR, BGR, or RGBA5551
mc3dstFile.replace(image, ColorFormat.ABGR); // this will override the existing 3dst image data

// Create a new 3dst image from scratch
mc3dstFile.assemble(image, ColorFormat.ABGR, "/path/to/output.3dst");
```

## Minimum Java Version

* Java 8

## 3dst File Format

* See [3dst\_File\_Format.md](./3dst_File_Format.md)