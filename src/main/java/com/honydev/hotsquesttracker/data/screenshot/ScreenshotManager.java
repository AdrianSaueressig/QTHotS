package com.honydev.hotsquesttracker.data.screenshot;

import com.honydev.hotsquesttracker.ocr.OCR;
import com.honydev.hotsquesttracker.ocr.Screenshotter;

import java.awt.*;
import java.util.List;
import java.awt.image.BufferedImage;

/**
 * Created by Thomas on 09.10.2018.
 */
public class ScreenshotManager {

    public static ScreenshotResult getNewResult() {
        long start = System.currentTimeMillis();

        Integer gameSeconds = null;
        Integer[] values = new Integer[]{};

        try {
            BufferedImage timerImage = Screenshotter.getTimerImage();

            List<BufferedImage> questImages = Screenshotter.getQuestImages();
            gameSeconds = OCR.getSecondsFromImage(timerImage);
            values = questImages
                    .stream()
                    .map(image -> {
                        OCR.prepareQuestImageForOCR(image, Color.white, 15);
                        return OCR.getValueFromImage(image);
                    })
                    .toArray(Integer[]::new);

            // Arrays.stream(values).forEach(v -> System.out.println("Value from image: "  + v));
        } catch (AWTException e) {
            e.printStackTrace();
        }

        // System.out.println("Game seconds: " + gameSeconds);
        System.out.println("Total time spent to create ScreenshotResult: " + (System.currentTimeMillis() - start) + "ms");
        return  new ScreenshotResult(values, gameSeconds);
    }
}
