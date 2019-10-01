package com.honydev.hotsquesttracker.properties;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class PropertyManager {
	private	static Logger log = LogManager.getLogger(PropertyManager.class);
	
	private static final String PROPERTIES_FILE = "settings.properties";
	private static Properties sysProps = null;
	
	{
		loadProperties();
	}
	
	private PropertyManager() {
		
	}
	
	private static void loadProperties() {
		sysProps = new Properties();

		// InputStream in = PropertyManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
		InputStream in = null;
		
		try {
			in = new FileInputStream(PROPERTIES_FILE);
			sysProps.load(in);
		} catch (IOException e) {
			log.error(e,e);
		}finally {
			try {
				if(in != null) {
					in.close();
				}
			} catch (IOException e) {
				log.error(e,e);
			}
		}
	}
	
	public static void setProperty(QTProperties property, String value){
		if(sysProps == null) { // shouldnt happen
			loadProperties();
		}
		sysProps.setProperty(property.getKey(), value == null ? property.getDefaultValue() : value);

		OutputStream out = null;
		try {
			out = new FileOutputStream(PROPERTIES_FILE);
			sysProps.store(out, null);
		} catch (IOException e) {
			log.error(e,e);
		}finally{
			if(out != null) {
				try {
					out.close();
				} catch (IOException e) {
					log.error(e,e);
				}
			}
		}
	}
	
	public static Properties getProperties() {
		if(sysProps == null) { // shouldnt happen
			loadProperties();
		}
		return sysProps;
	}
	
	public static String getValue(QTProperties prop) {
		if(sysProps == null) { // shouldnt happen
			loadProperties();
		}
		return sysProps.getProperty(prop.getKey(), prop.getDefaultValue());
	}
}
