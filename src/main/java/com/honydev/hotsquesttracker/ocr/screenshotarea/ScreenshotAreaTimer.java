package com.honydev.hotsquesttracker.ocr.screenshotarea;

import java.awt.*;

public enum ScreenshotAreaTimer {
    FULLHD(931, 0, 58, 17),
    WQHD(1245, 0, 70, 25),
    DEFAULT(931, 0, 58, 17);    // TODO: don't use default values. Calc values instead

    private int x;
    private int y;
    private int width;
    private int height;

    ScreenshotAreaTimer(int x, int y, int width, int height) {
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

    public Rectangle asRectangle() {
        return new Rectangle(x, y, width, height);
    }
}
