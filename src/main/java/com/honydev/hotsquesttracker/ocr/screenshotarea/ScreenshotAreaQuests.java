package com.honydev.hotsquesttracker.ocr.screenshotarea;

import java.awt.*;
import java.util.ArrayList;

public enum ScreenshotAreaQuests {

    FULLHD(217, 1047, 36, 20),
    WQHD(289, 1401, 48, 22),
    DEFAULT(217, 1047, 36, 20);     // TODO: don't use default values. Calc values instead

    private int x;
    private int y;
    private int width;
    private int height;

    ScreenshotAreaQuests(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ArrayList<Rectangle> asRectangleList() {
        ArrayList<Rectangle> rectangles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            rectangles.add(new Rectangle(x + (i * width), y, width,height));
        }
        return rectangles;
    }
    
    public Rectangle asRectangle() {
        return new Rectangle(x, y, width * 4, height);
    }
}
