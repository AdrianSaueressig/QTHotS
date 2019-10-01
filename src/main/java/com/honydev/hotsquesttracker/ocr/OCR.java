package com.honydev.hotsquesttracker.ocr;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Thomas on 09.10.2018.
 */
public class OCR {
    private static ITesseract instance = null;

    public static ITesseract getInstance() {
        if (instance != null) {
            return instance;
        }

        instance = new Tesseract();  // JNA Interface Mapping
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        instance.setOcrEngineMode(0);
        instance.setDatapath("tessdata"); // path to tessdata directory
        instance.setTessVariable("tessedit_char_whitelist", ":0123456789");

        return instance;
    }
/*
    public static void prepareQuestImageForOCR(BufferedImage coloredImage, int rgbColorToKeep) {
        prepareQuestImageForOCR(coloredImage, new Color(rgbColorToKeep), 0);
    }
*/
    /**
     * Turns all non-white pixels black
     * @param coloredImage
     */
    public static void prepareQuestImageForOCR(BufferedImage coloredImage, Color rgbColorToKeep, int rgbRange) {
        if (coloredImage == null) {
            System.out.println("LOG: colored image is null");
            return;
        }
        int width = coloredImage.getWidth();
        int height = coloredImage.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color currentPixelColor = new Color(coloredImage.getRGB(x, y));

                if (!areColorsInRange(currentPixelColor, rgbColorToKeep, rgbRange)) {
                    coloredImage.setRGB(x, y, Color.black.getRGB());
                } else {
                    coloredImage.setRGB(x, y, Color.white.getRGB());
                }
            }
        }
    }

    private static boolean areColorsInRange(Color color1, Color color2, int range) {
        if (Math.abs(color1.getRed() - color2.getRed()) > range) {
            return false;
        }
        if (Math.abs(color1.getGreen() - color2.getGreen()) > range) {
            return false;
        }
        if (Math.abs(color1.getBlue() - color2.getBlue()) > range) {
            return false;
        }
        return true;
    }

    /**
     * Takes screenshot of quest progress and returns its value as a string. Returns empty String if no digit found on the image
     * @param image
     * @return
     */
    public static String getDigitStringFromImage(BufferedImage image) {
        ITesseract tesseract = getInstance();
        instance.setTessVariable("tessedit_char_whitelist", "0123456789");
        String value = "";
        try {
            value = tesseract.doOCR(image);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        return value.trim();
    }

    /**
     * Takes screenshot and returns its value as a time string (mm:ss). Returns empty String if no digit found on the image
     * @param image
     * @return
     */
    public static String getTimeStringFromImage(BufferedImage image) {
        ITesseract tesseract = getInstance();
        instance.setTessVariable("tessedit_char_whitelist", "-:0123456789");
        String value = "";
        try {
            value = tesseract.doOCR(image);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        return value.trim();
    }

    /**
     * Takes screenshot of quest progress and returns its value. Returns null if there is no digit found on the image
     * @param image
     * @return
     */
    public static Integer getValueFromImage(BufferedImage image) {
        Integer value = null;
        String stringValue = getDigitStringFromImage(image);
        stringValue = stringValue.replaceAll("\\s","");
        try {
            value = Integer.parseInt(stringValue);
        } catch (Exception ex) {
            // System.out.println("Exception parsing Integer from String: " + ex.getMessage());
        }
        return value;
    }

    /**
     * Takes image of the ingame timer and tries to match a gametime value in seconds. Returns -1 if it fails
     * @param image
     * @return
     */
    public static Integer getSecondsFromImage(BufferedImage image) {
        Integer total = 0;
        // Color timerColor = new Color(189, 179, 232);
        // OCR.prepareQuestImageForOCR(image, timerColor.getRGB());
        String timeString = getTimeStringFromImage(image);
        System.out.println("Timer Digits: " + timeString);
        timeString = timeString.replaceAll("\\s",""); //replaces all whitespace?
        if (timeString.equals("") || timeString.length() < 3 || timeString.length() > 6) {
            return null;
        }
        boolean isNegative = timeString.startsWith("-");
        if (isNegative) {
            timeString = timeString.substring(1);
        }
        String[] timerValues = timeString.split(":");
        if (timerValues.length < 2) {
            return null;
        }
        String secondsString = timerValues[1]; 
        String minutesString = timerValues[0];
        try {
            int seconds = Integer.parseInt(secondsString);
            total += seconds;
            int minutes = Integer.parseInt(minutesString);
            total += (minutes * 60);
        } catch (Exception ex) {
            return null;
        }
        return isNegative ? -1 * total : total;
    }
}
