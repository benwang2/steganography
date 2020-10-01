# STEGOSAURUS

A program with a basic GUI that uses Steganography to encrypt data and decrypt data within an image.

| **Original Image** | **Modified Image** |
| --- | --- |
| ![Apple](https://raw.githubusercontent.com/benwang2/steganography/master/apple.png) | ![Encoded](https://raw.githubusercontent.com/benwang2/steganography/master/apple_encoded.png) |


The colors of the pixels are edited in accordance with the length of the string. Each character will edit 3 pixels, editing pixels starting from the top left, going to the right, and upon reaching the end of the row, going to the next row.

Pixels are encoded with binary data by using odd and even number values to modify the RGB the pixel that can not be perceived with the human eye. To represent a "1", the r, g, or b, value is made to be an odd value, whether that is leaving the color as it is or increasing/decreasing it by 1. The same thing occurs with a "0", however, the target is to make the color value even. ASCII characters can be represented in binary with 8 bits - this leaves 1 bit when using 3 pixels for a character. The 9th bit is used to indicate whether to continue to read or to stop reading the pixels in the image. In Steganographable.java, there is a variable named TESTING_MODE. When this boolean is set to true, encoded data will be visible, as odds and evens will be represented with more extreme numbers (0 and 255).
