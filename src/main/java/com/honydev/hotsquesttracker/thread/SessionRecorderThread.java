package com.honydev.hotsquesttracker.thread;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.honydev.hotsquesttracker.data.screenshot.ScreenshotManager;
import com.honydev.hotsquesttracker.data.screenshot.ScreenshotResult;
import com.honydev.hotsquesttracker.gui.GUIController;
import com.honydev.hotsquesttracker.properties.PropertyManager;
import com.honydev.hotsquesttracker.properties.QTProperties;

public class SessionRecorderThread extends Thread {

	private String heroName;
	private String talent1;
	private String talent2;
	private String talent3;
	private String talent4;
	private String map;
	private GUIController guiController;
	
	public SessionRecorderThread(String heroName, String talent1, String talent2, String talent3, String talent4, String map, GUIController guiController) {
		super("Recording Session for hero " + heroName);
		this.heroName = heroName;
		this.talent1 = talent1;
		this.talent2 = talent2;
		this.talent3 = talent3;
		this.talent4 = talent4;
		this.map = map;
		this.guiController = guiController;
	}
	
	@Override
	public void run() {
		guiController.printLnToConsole("Now recording talents for " + heroName + "...");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmm");
    	Date date = new Date();
    	String timestampForCSV = formatter.format(date);
    	
    	String resultFolder = PropertyManager.getValue(QTProperties.TRACKINGRESULTS_PATH) + "/";
    	
    	new File(resultFolder).mkdirs(); // creates folder if needed.
    	File csvDataFile = new File(resultFolder + timestampForCSV + "_" + heroName + "_" + map + ".csv");
    	
    	try {
    		String csvLine = "timestamp," + talent1 + "," + talent2 + "," + talent3 + "," + talent4 + "\n";
			write(csvDataFile, csvLine);
		} catch (IOException e) {
			guiController.printLnToConsole(e);
		}
    	
        while (!interrupted()) {
            ScreenshotResult result = ScreenshotManager.getNewResult();
            if (result.getGameSeconds() != null && !result.onlyNullValues()) {
                try {
                	String csvLine = result.toCSVLine();
                	guiController.printToConsole(csvLine);
                    write(csvDataFile, csvLine);
                    guiController.scrollDown();
                } catch (Exception ex) {
                	guiController.printLnToConsole(ex);
                }
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            	// thats fine, most likely the stop button has been pressed
            	break;
            }
        }
        guiController.printLnToConsole("Recording Session finished.");
	}
	
	private static void write(File file, String toWrite) throws IOException {
    	FileWriter fr = new FileWriter(file, true);
        fr.write(toWrite);
        fr.close();
    }

}
