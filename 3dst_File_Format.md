# Minecraft: New 3DS Edition - 3dst File Format

| | |
|-|-|
| Original Documentation | [BJTMastermind](https://github.com/BJTMastermind) |
| Updated Information | [Cracko298](https://github.com/Cracko298/MC-3DST-Documentation) |

All data is in little endian byte order.

### Heading

| Name | Size (in bytes) | Description |
|------|-----------------|-------------|
| Header | 4 | Identifies the 3dst file. Always `33 44 53 54` = `3DST` |
| Version | 4 | The version of the 3dst format. Always `03 00 00 00` |
| [Color Type](#color-type-info) | 4 | The type of color format used for this texture. |
| Width | 4 | The width of this image as closest power of 2 |
| Height | 4 | The height of this image as closest power of 2 |
| Original Width | 4 | The original width of this image |
| Original Height | 4 | The original height of this image |
| Mip Map Level | 4 | Unsure what this affects, seems to always be `01 00 00 00` |

### Image Data

**\*Remaining bytes are read from the end of the file back to offset 33 (0x21)\***

(repeat per (Width * Height))

**If Color Type is 0**

| Name | Size (in bytes) | Description |
|------|-----------------|-------------|
| Red | 1 | The pixels Red value |
| Green | 1 | The pixels Green value |
| Blue | 1 | The pixels Blue value |
| Alpha | 1 | The pixels Alpha value |

**If Color Type is 1**

| Name | Size (in bytes) | Description |
|------|-----------------|-------------|
| Red | 1 | The pixels Red value |
| Green | 1 | The pixels Green value |
| Blue | 1 | The pixels Blue value |

**If Color Type is 2**

| Name | Size (in BITS) | Description |
|------|----------------|-------------|
| Alpha | 1 | The pixels Alpha value |
| Blue | 5 | The pixels Blue value |
| Green | 5 | The pixels Green value |
| Red | 5 | The pixels Red value |

### Pixel Layout

```
- Cubes      = A group of pixels in a 2x2 grid
- Blocks     = A group of Cubes in a 2x2 grid (4x4 of pixels)
- Chunks     = A group of Blocks in a 2x2 grid (8x8 of pixels)

There are a total of 4 Chunks for a 16x16 image. This changes depending on the size of the image,
the 3dst image width and height must be a power of 2 due to this pixel layout.
```

Minecraft's 3DST files don't store pixels how you would think, normally starting from the top left and going across to the end then repeating for the next row of pixels but that is not how .3dst images do things the pixels are stored in 8x8 pixel chunks with 4 4x4 pixel blocks with 4 2x2 pixel cubes each so your normal first 4 pixels in the top row are now the first 2 pixels of the first row + the first 2 pixels of the row below it.

Pixel layout for a 16x16 image:

<img width=512 src=https://github.com/BJTMastermind/MC3dst-Parser/assets/18742837/143ded3a-12b0-4f37-a1f1-27be8c3c2f89>

`Green = Chunk, Blue = Block, Yellow = Cube`

Numbers indicating the array index of where the pixels belong to create the original image

<h3 id="color-type-info">Color Type Info</h3>

* Color Type `00 00 00 00` (0) = 4 byte ETC2_RGBA8 (formatted ARGB) texture
* Color Type `01 00 00 00` (1) = 3 byte RGB texture
* Color Type `02 00 00 00` (2) = 2 byte HighColor (16-bit ABGR 5551) texture.
