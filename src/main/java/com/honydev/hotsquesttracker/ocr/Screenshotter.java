package com.honydev.hotsquesttracker.ocr;

import com.honydev.hotsquesttracker.ocr.screenshotarea.ScreenshotAreaQuests;
import com.honydev.hotsquesttracker.ocr.screenshotarea.ScreenshotAreaTimer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 09.10.2018.
 */
public class Screenshotter {

    /**
     * Takes a screenshot and returns a BufferedImage of the ingame timer
     * @return OCR-ready BufferedImage of the ingame Timer
     * @throws AWTException - thrown when "new Robot()" fails
     */
    public static BufferedImage getTimerImage() throws AWTException {
        Rectangle timerScreenshotArea = getTimerScreenshotArea();
        BufferedImage image = new Robot().createScreenCapture(timerScreenshotArea);
        image = convertImageForOCR(image);
        return image;
    }

    /**
     * Takes a Screenshot and returns 4 Buffered Images, one for each quest indicator
     * @return OCR-ready BufferedImages of all 4 quest indicators
     * @throws AWTException - thrown when "new Robot()" fails
     */
    public static List<BufferedImage> getQuestImages() throws AWTException {
        List<BufferedImage> imageList = new ArrayList<>();
        // take on screenshot and split it into images
        Rectangle questScreenshotArea = getQuestScreenshotArea();
        BufferedImage questAreaImage = new Robot().createScreenCapture(questScreenshotArea);

        int width = (int) questScreenshotArea.getWidth() / 4;

        for(int i = 0; i < 4; i++) {
            BufferedImage subImage = questAreaImage.getSubimage(i * width, 0, width, questAreaImage.getHeight());
            subImage = convertImageForOCR(subImage);
            imageList.add(subImage);
        }

        return imageList;
    }

    /**
     * Returns an area for the current screensize in order to screenshot quests
     * @return Rectangle
     */
    private static Rectangle getQuestScreenshotArea() {
        double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        if (screenHeight == 1440) {
            return ScreenshotAreaQuests.WQHD.asRectangle();
        } else if (screenHeight == 1080) {
            return ScreenshotAreaQuests.FULLHD.asRectangle();
        } else {
            System.out.println("screen height is " + screenHeight);
            return ScreenshotAreaQuests.DEFAULT.asRectangle();
        }
    }

    /**
     * Returns an area for the current screensize in order to screenshot the ingame timer
     * @return Rectangle
     */
    private static Rectangle getTimerScreenshotArea() {
        double screenHeight = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        if (screenHeight == 1440) {
            return ScreenshotAreaTimer.WQHD.asRectangle();
        } else if (screenHeight == 1080) {
            return ScreenshotAreaTimer.FULLHD.asRectangle();
        }  else {
            System.out.println("screen height is " + screenHeight);
            return ScreenshotAreaTimer.DEFAULT.asRectangle();
        }
    }

    /**
     * Creates an OCR-ready BufferedImage from the given image source. New image has image type 5
     * @param image - source image
     * @return - OCR-ready BufferedImage
     */
    private static BufferedImage convertImageForOCR(BufferedImage image) {
        return createImageWithType(image, 5);
    }

    /**
     * Creates a new Buffered image from a given source image and with a new image type
     * @param image - source image
     * @param imageType - type of new image
     * @return - new BufferedImage with old image content and new image type
     */
    private static BufferedImage createImageWithType(BufferedImage image, int imageType) {
        BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(), imageType);
        convertedImage.getGraphics().drawImage(image, 0, 0, null);
        return convertedImage;
    }


    /**
     * Loads the given filename as a bufferedImage
     * @return BufferedImage
     * */
    public static BufferedImage loadScreenshotFromFile(String filename) {
        File coloredImageFile = new File(filename);
        BufferedImage coloredImage;
        try {
            coloredImage = ImageIO.read(coloredImageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return new BufferedImage(0, 0, 0);
        }
        return coloredImage;
    }
}
