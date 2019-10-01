package com.honydev.hotsquesttracker.data.screenshot;

import java.util.Arrays;

/**
 * Created by Thomas on 09.10.2018.
 */
public class ScreenshotResult {
    private Integer[] questValues;
    private Integer gameSeconds;

    public ScreenshotResult(Integer[] questValues, Integer gameSeconds) {
        this.questValues = questValues;
        this.gameSeconds = gameSeconds;
    }

    public Integer[] getQuestValues() {
        return questValues;
    }

    public Integer getGameSeconds() {
        return gameSeconds;
    }

    @Override
    public String toString() {
        return "ScreenshotResult{" +
                "questValues=" + Arrays.toString(questValues) +
                ", gameSeconds=" + gameSeconds +
                '}';
    }

    public boolean onlyNullValues () {
        for (Integer value : questValues) {
            if (value != null) return false;
        }
        return true;
    }

    public String toCSVLine() {
        StringBuilder line = new StringBuilder();
        line.append(getGameSeconds());
        for (Integer value : questValues) {
            line.append("," + value);   //TODO: There is probably a better way to do this in Java
        }
        line.append("\n");
        return line.toString();
    }
}
