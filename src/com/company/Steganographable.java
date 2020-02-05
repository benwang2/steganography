package com.company;

import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.*;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Steganographable {
    private static boolean TESTING_MODE = true;
    /*Image image;
    PixelReader reader;
    WritableImage writable;
    PixelWriter writer;*/

    public Steganographable(Image newImage){

    }

    public static void main(String[] args) throws IOException {
        JFXPanel hack = new JFXPanel();
        Image img = new Image("com/company/blank.png");
        ImageView viewer = new ImageView();
        viewer.setImage(img);

        encodeImg(img, "Hello, World!");

        //String decoded = decodeImg(new Image("com/company/encoded.png"));
        //System.out.println(decoded);
    }

    public static ArrayList<Integer> encodeBit(char bit){
        String encoded = Integer.toBinaryString((int)bit);
        ArrayList bits = new ArrayList<Integer>();
        for (int i = 0; i<encoded.length(); i++){
            String bitString = encoded.substring(i, i+1);
            int bitValue = (Integer.valueOf(
                    bitString
            )).intValue();
            bits.add(bitValue);
        }
        for (int i=0; i<8-encoded.length(); i++){
            bits.add(0,0);
        }

        //System.out.println(bit);
        //System.out.println(bits.toString());
        return bits;
    }

    public static String decodeBit(String bit){
        return (String.valueOf((char)Integer.parseInt(bit, 2)));
    }

    public static int round(double num){
        if (num > Math.floor(num)+.5){
            return (int)(Math.ceil(num));
        }
        return (int)(Math.floor(num));
    }

    public static int setEven(int num){ // even is 0, odd is 255
        if (!TESTING_MODE){
            if (num==255){
                return (num%2==0?num:num-1);
            } else if (num==1){
                return (num%2==0?num:num+1);
            }
            return (num%2==0?num:num-1);
        } else {
            return 0;
        }
    }

    public static int setOdd(int num){ // even is 0, odd is 255
        if (!TESTING_MODE) {
            if (num == 1) {
                return num;
            }
            return (num % 2 == 1 ? num : num - 1);
        } else {
            return 255;
        }
    }

    public static void encodeImg(Image newImg, String message) throws IOException {
        Image image = newImg;
        PixelReader reader = image.getPixelReader();

        WritableImage writable = new WritableImage(reader, (int)image.getWidth(), (int)image.getHeight());
        PixelWriter writer = writable.getPixelWriter();

        int reqPix = message.length()*3;
        int[] picCoordX = new int[reqPix];
        int[] picCoordY = new int[reqPix];
        int pixIndex = 1;

        for (int y = 0; y<image.getHeight(); y++){
            for (int x = 1; x<image.getWidth(); x++){
                if (pixIndex < reqPix) {
                    picCoordX[pixIndex] = x;
                    picCoordY[pixIndex] = y;
                    pixIndex++;
                }
            }
        }
        pixIndex = 0;

        for (int i=0; i<message.length(); i++){
            ArrayList bits = encodeBit(message.charAt(i));
            int bitIndex = 0;

            for (int pix=0; pix<3; pix++) {
                Color pixelRGB = reader.getColor(picCoordX[pixIndex], picCoordY[pixIndex]);
                int[] pixColors = new int[3];
                pixColors[0] = round(pixelRGB.getRed() * 255);
                pixColors[1] = round(pixelRGB.getGreen() * 255);
                pixColors[2] = round(pixelRGB.getBlue() * 255);

                for (int bit = 0; bit < 3; bit++) {
                    if (bitIndex < 8) {
                        pixColors[bit] = (
                                bits.get(bitIndex).equals(0)
                                        ?
                                        setEven(pixColors[bit])
                                        :
                                        setOdd(pixColors[bit])
                        );
                    } else {
                        pixColors[2] = (pixIndex == reqPix - 1 ? setEven(pixColors[2]) : setOdd(pixColors[2]));
                        // The blue value of the third pixel in each segment
                        // Indicates whether to stop or not
                        // Even = Stop
                        // Odd  = Continue
                    }
                    bitIndex++;
                }

                Color newColor = Color.rgb(pixColors[0], pixColors[1], pixColors[2]);
                //System.out.println("Pixel"+pixIndex+":: "+newColor.toString());
                writer.setColor(picCoordX[pixIndex], picCoordY[pixIndex], newColor);
                pixIndex++;
            }

        }

        File file =  new File("encoded.png");
        RenderedImage encodedImg = SwingFXUtils.fromFXImage(writable, null);
        ImageIO.write(encodedImg,"png",file);
    }

    public static String decodeImg(Image encoded){
        String msg = "";
        boolean eom = false;
        int pixIndex = 0;

        int totalPixels = (int)Math.floor(Math.floor(encoded.getWidth() * encoded.getHeight()/3)*3);
        int[] picCoordX = new int[(int)(encoded.getWidth() * encoded.getHeight())];
        int[] picCoordY = new int[(int)(encoded.getWidth() * encoded.getHeight())];

        Image image = encoded;
        PixelReader reader = image.getPixelReader();

        for (int y = 0; y<image.getHeight(); y++){
            for (int x = 0; x<image.getWidth(); x++){
                picCoordX[pixIndex] = x;
                picCoordY[pixIndex] = y;
                pixIndex++;
            }
        }

        pixIndex = 0;

        while (pixIndex+3 < totalPixels){
            String binString = "";
            for (int pix=0; pix<3; pix++){
                Color pixelRGB = reader.getColor(picCoordX[pixIndex], picCoordY[pixIndex]);
                int pixR = round(pixelRGB.getRed()*255);
                int pixG = round(pixelRGB.getGreen()*255);
                int pixB = round(pixelRGB.getBlue()*255);
                binString = binString
                        +
                        String.valueOf(pixR%2)
                        +
                        String.valueOf(pixG%2)
                        +
                        String.valueOf(pixB%2);
                if (pix==2 && pixB%2==0){
                    //System.out.println("Found blue escape pixel at Pixel"+pixIndex);
                    pixIndex = totalPixels;
                }
                pixIndex++;
            }
            msg = msg +decode(binString.substring(0,8));
        }

        return msg;
    }

    public static String decode(String binString){
        int charCode = Integer.parseInt(binString, 2);
        String decoded = new Character((char)charCode).toString();
        return decoded;
    }

}
