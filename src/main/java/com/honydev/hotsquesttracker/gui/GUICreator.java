package com.honydev.hotsquesttracker.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GUICreator extends Application {
	
	private	Logger log = LogManager.getLogger(GUICreator.class);

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		try{
			Parent root = FXMLLoader.load(getClass().getResource("/GUI.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setTitle("QTHots - Heroes of the Storm TalentTracker");
			stage.getIcons().add(new Image("file:chogall_edited.png"));
			stage.show();
		} catch(Exception e) {
			log.error(e,e);
		}
	}

}
