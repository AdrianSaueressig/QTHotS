package com.honydev.hotsquesttracker.gui;

import javafx.util.StringConverter;

public class SecondsToTimeConverter {
    private SecondsToTimeConverter() {

    }

    public static StringConverter<Number> getConverter() {
        return new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                if (object == null) {
                    return null;
                }
                int intValue = object.intValue();
                int minutes = intValue / 60;
                int seconds = intValue % 60;
                return String.format("%02d:%02d", minutes, seconds);
            }

            @Override
            public Number fromString(String string) {
                return null;
            }
        };
    }

}
