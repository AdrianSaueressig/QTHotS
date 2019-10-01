package com.honydev.hotsquesttracker.gui;

import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Thomas on 19.10.2018.
 */
public class GameChoiceBoxValue implements Comparable<Object>{
    private static final SimpleDateFormat fromFormat = new SimpleDateFormat("yyyyMMdd_HHmm");
    private static final SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final String key;
    private final String value;
    private String map;

    public GameChoiceBoxValue(String path) {
        this.key = path;
        Path p = Paths.get(path);
        String fileName = p.getFileName().toString();
        this.value = fileNameToValue(fileName);
    }

    public GameChoiceBoxValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey()   {
        return key;
    }

    public String toString() {
        return value;
    }
    
    public String getMap() {
    	return map;
    }

    private String fileNameToValue (String fileName) {
        // remove extension
        fileName = FilenameUtils.removeExtension(fileName);
        String[] fileNameParts = fileName.split("_");
        if (fileNameParts.length < 4) {
        	return fileName;
        }

        String dateString = fileNameParts[0] + "_" +fileNameParts[1];
        String heroName = fileNameParts[2];
        map = fileNameParts[3];

        Date date;
        try {
            date = fromFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return fileName;
        }
        return toFormat.format(date) + " " + heroName;
    }

    @Override
    public int compareTo(Object o) {
        return this.toString().compareTo(o.toString());
    }
}
