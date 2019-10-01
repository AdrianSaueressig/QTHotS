package com.honydev.hotsquesttracker.gui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.honydev.hotsquesttracker.data.herodata.HeroData;
import com.honydev.hotsquesttracker.data.screenshot.ScreenshotResult;
import com.honydev.hotsquesttracker.properties.PropertyManager;
import com.honydev.hotsquesttracker.properties.QTProperties;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Thomas on 14.10.2018.
 */
public class DataLoader {
	
	private	static Logger log = LogManager.getLogger(DataLoader.class);
	
    private static final String heroDataFile = "gameData/heroData.json";
    private static final String mapDataFile = "gameData/mapData.json";

    public static List<String> getTrackingResultFilePaths() {
        try (Stream<Path> paths = Files.walk(Paths.get(PropertyManager.getValue(QTProperties.TRACKINGRESULTS_PATH)))) {
            List<String> fileNames = paths.filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(new FileNameExtensionFilter(null, "csv")::accept)
                    .map(File::getPath)
                    .collect(Collectors.toList());
            return fileNames;
        } catch (IOException e) {
            log.error(e,e);
        }
        return null;
    }

    public static List<ScreenshotResult> getResults(String fileName) {
        List<ScreenshotResult> results = new ArrayList<>();

        CSVParser csvParser = null;
        try {
            Reader reader = Files.newBufferedReader(Paths.get(fileName));
            csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

            results = csvParser.getRecords()
                    .stream()
                    .skip(1)
                    .map(DataLoader::csvRecordToResult)
                    .collect(Collectors.toList());
            csvParser.close();
        } catch (IOException e) {
        	log.error(e,e);
            if(csvParser != null) {
            	try {
					csvParser.close();
				} catch (IOException e1) {
					log.error(e1,e1);
				}
            }
        }
        return results;
    }

    public static int getIndexOfTalentName(String fileName, String talentName) {
        try {
            CSVRecord firstLineRecord = getCSVHeaders(fileName);
            int size = firstLineRecord.size();
            for (int i = 0; i < size; i++) {
                if (firstLineRecord.get(i).equalsIgnoreCase(talentName)) {
                    return i;
                }
            }

        } catch (Exception ex) {
        	log.error(ex,ex);
        }
        return 0;
    }

    public static List<String> getTalentNames(String fileName) {
        List<String> talentNames = new ArrayList<>();
        try {
            for (String talentName : getCSVHeaders(fileName)) {
                talentNames.add(talentName);
            }
            talentNames = talentNames.stream().filter(name -> {
                return !name.isEmpty() && !name.equalsIgnoreCase("timestamp");
            }).collect(Collectors.toList());

        } catch (Exception ex) {
        	log.error(ex,ex);
        }
        return talentNames;
    }

    public static String getTalentNameByIndex(String fileName, int index) throws IOException {
        CSVRecord csvRecord = getCSVHeaders(fileName);
        return csvRecord.get(index);
    }

    private static CSVRecord getCSVHeaders(String fileName) throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get(fileName));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
        CSVRecord result = csvParser.getRecords().get(0);
        csvParser.close();
        return result;
    }

    private static ScreenshotResult csvRecordToResult(CSVRecord csvRecord) {
        String timeStamp = csvRecord.get(0);
        String talent1 = csvRecord.get(1);
        String talent2 = csvRecord.get(2);
        String talent3 = csvRecord.get(3);
        String talent4 = csvRecord.get(4);

        return new ScreenshotResult(
                new Integer[]{
                        csvValueToInt(talent1),
                        csvValueToInt(talent2),
                        csvValueToInt(talent3),
                        csvValueToInt(talent4)
                },
                csvValueToInt(timeStamp)
        );
    }

    private static Integer csvValueToInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            // could not convert String to int, probably because value is String "null"
        	log.error(ex,ex);
        }
        return null;
    }

    public static List<HeroData> getHeroData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<HeroData> heroDataList = objectMapper.readValue(new File(heroDataFile), new TypeReference<List<HeroData>>(){});
            return heroDataList;
        } catch (Exception e) {
        	log.error(e,e);
        }
        return new ArrayList<>();
    }


    public static List<String> getMapNames () {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<String> mapNameList = objectMapper.readValue(new File(mapDataFile), new TypeReference<List<String>>(){});
            return mapNameList;
        } catch (Exception e) {
        	log.error(e,e);
        }

        return new ArrayList<>();
    }
}
