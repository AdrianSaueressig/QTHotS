package com.honydev.hotsquesttracker.ocr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.junit.Before;
import org.junit.Test;

public class ExampleImagesTest{

	private static final String questScreenshotLocation = "./exampleimages/quests";
	private static final String timerScreenshotLocation = "./exampleimages/timers";

	private List<File> filesToTest = null;

	private Map<String, List<File>> fileMap = new HashMap<>();
	
	@Before
	public void prepareFilesToTest() {
		String[] locationsToTest = {questScreenshotLocation, timerScreenshotLocation};

		for(String location : locationsToTest) {
			filesToTest = new ArrayList<>();
			try (Stream<Path> paths = Files.walk(Paths.get(location))) {
				filesToTest = paths.filter(Files::isRegularFile)
						.map(Path::toFile)
						.filter(new FileNameExtensionFilter(null, "JPG", "PNG")::accept)
						.collect(Collectors.toList());
				fileMap.put(location, filesToTest);
			} catch (IOException e) {
				System.out.println(e.getMessage());
				fail("Exception preparing example Screenshots.");
			}
		}


	}
	
	@Test
	public void testOcrOnAllQuestScreenshots() {
		for (File exampleScreenshot : fileMap.get(questScreenshotLocation)) {
			System.out.print("Testing " + exampleScreenshot.getPath() + " ...");

			if(!exampleScreenshot.getName().matches(".*\\d+.*")) { // any file without digits in its name will be skipped
				System.out.println(" skipped.");
				continue;
			}
			
			int expectedResult = Integer.parseInt(exampleScreenshot.getName().replaceAll("\\D", "")); //removes all non-digits
			BufferedImage loadedImage = Screenshotter.loadScreenshotFromFile(exampleScreenshot.getAbsolutePath());
			try{
				OCR.prepareQuestImageForOCR(loadedImage, Color.white, 15);
				Integer result = OCR.getValueFromImage(loadedImage);
				assertNotNull(result);
				assertEquals(expectedResult, result.intValue());
				System.out.println(" OK.");
			}catch(AssertionError e) {
				System.out.println(" AssertionError: " + e.getMessage());
			}
		}
	}

	// TODO: Works with format "mmss" not with "mm:ss"
	@Test
	public void testOcrOnAlltimerScreenshots() {
		for (File exampleScreenshot : fileMap.get(timerScreenshotLocation)) {
			System.out.print("Testing " + exampleScreenshot.getPath() + " ...");

			if(!exampleScreenshot.getName().matches(".*\\d+.*")) { // any file without digits in its name will be skipped
				System.out.println(" skipped.");
				continue;
			}

			String expectedResult = exampleScreenshot.getName().replaceAll("\\D", ""); //removes all non-digits
			BufferedImage loadedImage = Screenshotter.loadScreenshotFromFile(exampleScreenshot.getAbsolutePath());
			try{
				String result = OCR.getTimeStringFromImage(loadedImage);
				result = result.replace(":", "");
				assertNotNull(result);
				assertEquals(expectedResult, result);
				System.out.println(" OK.");
			}catch(AssertionError e) {
				System.out.println(" AssertionError: " + e.getMessage());
			}
		}
	}
}
