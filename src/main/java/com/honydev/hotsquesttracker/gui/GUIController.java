package com.honydev.hotsquesttracker.gui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import com.honydev.hotsquesttracker.data.herodata.HeroData;
import com.honydev.hotsquesttracker.data.herodata.Talent;
import com.honydev.hotsquesttracker.data.screenshot.ScreenshotResult;
import com.honydev.hotsquesttracker.error.AlertMessage;
import com.honydev.hotsquesttracker.thread.SessionRecorderThread;

import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.textfield.TextFields;

public class GUIController{

	private	Logger log = LogManager.getLogger(GUIController.class);
	
	@FXML
	private AnchorPane rootAnchorPane;
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab tabRecord;
	@FXML
	private AnchorPane tabRecordAnchorPane;
	@FXML
	private Label labelTalent1;
	@FXML
	private TextField textFieldTalent1;
	@FXML
	private Label labelTalent2;
	@FXML
	private TextField textFieldTalent2;
	@FXML
	private Label labelTalent3;
	@FXML
	private TextField textFieldTalent3;
	@FXML
	private Label labelTalent4;
	@FXML
	private TextField textFieldTalent4;
	@FXML
	private Label labelHeroName;
	@FXML
	private TextField textFieldHeroName;
	@FXML
	private Label labelMap;
	@FXML
	private TextField textFieldMap;
	@FXML
	private ToggleButton toggleStartStop;
	@FXML 
	private Button buttonShowCSV;
	@FXML
	private TextArea textFieldConsoleOutput;
	@FXML
	private Tab tabStats;
	@FXML
	private AnchorPane tabStatsAnchorPane;
	@FXML
	private LineChart<Integer, Integer> lineChartTalentProgress;
	@FXML
	private NumberAxis lineChartXNumberAxis;
	@FXML
	private NumberAxis lineChartYNumberAxis;
	@FXML
	private ChoiceBox<String> dropDownTalents;
	@FXML
	private ChoiceBox<GameChoiceBoxValue> dropDownGames;
	@FXML
	private Label labelGameDropDown;
	@FXML
	private Label labelTalentDropDown;
	@FXML
	private Label labelStatsMap;
	
	@FXML
	private URL location;
	@FXML
	private ResourceBundle resources;
	
	private SessionRecorderThread sessionRecorderThread = null;

	private Map<String, HeroData> heroDataMap = null;

	private SuggestionProvider<String> talent1Provider = null;
	private SuggestionProvider<String> talent2Provider = null;
	private SuggestionProvider<String> talent3Provider = null;
	private SuggestionProvider<String> talent4Provider = null;

	public GUIController() {}
	
	@FXML
	private void initialize() {
		initializeHeroData();
		initializeTalentsAutocomplete();
		initializeTextFieldHeroName();
		initializeTextFieldMap();
		initializeGameDropdown();
		initializeTalentDropdown();
		dropDownGames.getSelectionModel().selectFirst();

		lineChartTalentProgress.setAnimated(false);
	}

	@FXML
	public void recordSession() {
		if(toggleStartStop.isSelected()) {
			if(textFieldHeroName.getText().isEmpty()) {
				createAlert(AlertMessage.NO_HERO_SELECTED);
				return;
			}
			if(allTalentsAreEmpty()) {
				createAlert(AlertMessage.NO_TALENTS_SELECTED);
				return;
			}
			if(textFieldMap.getText().isEmpty()) {
				createAlert(AlertMessage.NO_MAP_SELECTED);
				return;
			}
			toggleStartStop.setText("Stop");
			sessionRecorderThread = new SessionRecorderThread(textFieldHeroName.getText(),
											textFieldTalent1.getText(), textFieldTalent2.getText(),
											textFieldTalent3.getText(), textFieldTalent4.getText(),
											textFieldMap.getText(), this);
			sessionRecorderThread.start();
		}else {
			sessionRecorderThread.interrupt();
			initializeGameDropdownValues();
			toggleStartStop.setText("Start");
			scrollDown();
		}
		
	}
	
	private void createAlert(AlertMessage message) {
		if(message == null) return;
		
		Alert alert = new Alert(message.getAlertType());
		alert.setTitle(message.getShortText());
		alert.setContentText(message.getLongText());
		alert.setHeaderText(null);
		alert.showAndWait();
		toggleStartStop.setSelected(false);
	}
	
	@FXML
	public void showCSVFolder() {
		try {
			Desktop desktop = Desktop.getDesktop();
			desktop.open(new File("trackingresults"));
			// Runtime.getRuntime().exec("explorer.exe /select," + "trackingresults"); -> opens parent directory and highlights trackingresults folder
		} catch (IOException e) {
			printLnToConsole(e.getMessage());
		}
		// TODO: move folder path to systemproperty file
	}
	
	private boolean allTalentsAreEmpty() {
		return textFieldTalent1.getText().isEmpty()
			&& textFieldTalent2.getText().isEmpty()
			&& textFieldTalent3.getText().isEmpty()
			&& textFieldTalent4.getText().isEmpty();
	}

	public void printLnToConsole(String toPrint) {
		textFieldConsoleOutput.setText(textFieldConsoleOutput.getText() + toPrint + "\r\n");
		System.out.println(toPrint);
	}
	
	public void printLnToConsole(Object toPrint) {
		textFieldConsoleOutput.setText(textFieldConsoleOutput.getText() + String.valueOf(toPrint) + "\r\n");
		System.out.println(toPrint);
	}
	
	public void printToConsole(String toPrint) {
		textFieldConsoleOutput.setText(textFieldConsoleOutput.getText() + toPrint);
		System.out.print(toPrint);
	}

	public void scrollDown() {
		textFieldConsoleOutput.setScrollTop(Double.MAX_VALUE);
	}

	private void initializeTextFieldHeroName() {
		TextFields.bindAutoCompletion(textFieldHeroName, heroDataMap.keySet());
		textFieldHeroName.textProperty().addListener(this::onHeroNameInput);
	}

	private void initializeTextFieldMap () {
		TextFields.bindAutoCompletion(textFieldMap, DataLoader.getMapNames());
	}

	private void onHeroNameInput(ObservableValue<?> observableValue, String oldValue, String newValue) {
		HeroData heroData = heroDataMap.get(newValue);
		if (heroData == null) {
			clearTalentAutocomplete();
			return;
		}

		List<Talent> quests = heroData.getQuests();
		List<String> questNames = quests
				.stream()
				.map(talent -> "Tier " + talent.getTier() + " - " + talent.getName())
				.collect(Collectors.toList());

		bindTalentsAutoComplete(questNames);
	}

	private void initializeTalentsAutocomplete() {
		Set<String> autoCompletions = new HashSet<>();
		talent1Provider = SuggestionProvider.create(autoCompletions);
		talent1Provider.setShowAllIfEmpty(true);
		talent2Provider = SuggestionProvider.create(autoCompletions);
		talent2Provider.setShowAllIfEmpty(true);
		talent3Provider = SuggestionProvider.create(autoCompletions);
		talent3Provider.setShowAllIfEmpty(true);
		talent4Provider = SuggestionProvider.create(autoCompletions);
		talent4Provider.setShowAllIfEmpty(true);

		new AutoCompletionTextFieldBinding<>(textFieldTalent1, talent1Provider);
		new AutoCompletionTextFieldBinding<>(textFieldTalent2, talent2Provider);
		new AutoCompletionTextFieldBinding<>(textFieldTalent3, talent3Provider);
		new AutoCompletionTextFieldBinding<>(textFieldTalent4, talent4Provider);

		List<TextField> talentTextFields = new ArrayList<>();
		talentTextFields.add(textFieldTalent1);
		talentTextFields.add(textFieldTalent2);
		talentTextFields.add(textFieldTalent3);
		talentTextFields.add(textFieldTalent4);

		// open talent suggestion on textfield selection if text field is empty and hero is selected
		talentTextFields.forEach(textField -> {
			textField.focusedProperty().addListener(e -> {
				if (e instanceof ReadOnlyBooleanProperty) {
					ReadOnlyBooleanProperty event = (ReadOnlyBooleanProperty) e;
					boolean wasSelected = event.getValue();
					boolean isEmpty = textField.getText().isEmpty();
					boolean isKnownHero = heroDataMap.containsKey(textFieldHeroName.getText());
					if (wasSelected && isEmpty && isKnownHero) {
						// TODO: this is a bit of a hack
						// open talent suggestions
						textField.setText(" ");
						textField.setText("");
					}
				}
			});
		});
	}

	private void bindTalentsAutoComplete(Collection<String> collection) {
		clearTalentAutocomplete();
		talent1Provider.addPossibleSuggestions(collection);
		talent2Provider.addPossibleSuggestions(collection);
		talent3Provider.addPossibleSuggestions(collection);
		talent4Provider.addPossibleSuggestions(collection);
	}

	private void clearTalentAutocomplete() {
		talent1Provider.clearSuggestions();
		talent2Provider.clearSuggestions();
		talent3Provider.clearSuggestions();
		talent4Provider.clearSuggestions();
	}

	private void initializeGameDropdown() {
		initializeGameDropdownValues();
		dropDownGames.setOnAction(this::onGameSelect);
	}

	private void initializeGameDropdownValues() {
		List<String> paths = DataLoader.getTrackingResultFilePaths();
		if (paths == null) return;
		List<GameChoiceBoxValue> choiceBoxValues = paths
				.stream()
				.map(GameChoiceBoxValue::new)
				.sorted(Collections.reverseOrder())
				.collect(Collectors.toList());

		dropDownGames.setItems(FXCollections.observableArrayList(choiceBoxValues));
	}

	private void initializeTalentDropdown() {
		dropDownTalents.setOnAction(this::onTalentSelect);
	}

	private XYChart.Series<Integer, Integer> getDataSeries(String trackingResultFilePath, int index) {
		XYChart.Series<Integer, Integer> series = new XYChart.Series<>();

		List<ScreenshotResult> results = DataLoader.getResults(trackingResultFilePath);
		for (ScreenshotResult result : results) {
			if (index >= result.getQuestValues().length) {
				return series;
			}
			Integer gameSeconds = result.getGameSeconds();
			Integer questValue = result.getQuestValues()[index];
			if (questValue != null) {
				series.getData().add(new XYChart.Data<>(gameSeconds, questValue));
			}
		}

		return series;
	}

	private XYChart.Series<Integer,Integer> getQuestGoalSeries(XYChart.Series<Integer, Integer> series) {
		XYChart.Series<Integer, Integer> questGoalSeries = new XYChart.Series<>();
		questGoalSeries.setName("Quest Goal");
		Optional<XYChart.Data<Integer, Integer>> max = series.getData().stream().max(Comparator.comparingInt(XYChart.Data::getXValue));
		if (max.isPresent()) {
			XYChart.Data<Integer, Integer> lastData = max.get();
			int maxSeconds = lastData.getXValue();
			int questGoal = 20;	// TODO: get actual quest goal
			questGoalSeries.getData().add(new XYChart.Data<>(0, questGoal));
			questGoalSeries.getData().add(new XYChart.Data<>(maxSeconds, questGoal));
		}
		return questGoalSeries;
	}

	private void setGraphData(String path, int talentIndex, String talentName) {
		XYChart.Series<Integer, Integer> series = getDataSeries(path, talentIndex);
		XYChart.Series<Integer, Integer> questGoalSeries = getQuestGoalSeries(series);

		series.setName(talentName);
		lineChartTalentProgress.setCreateSymbols(false);
		lineChartTalentProgress.getData().setAll(series, questGoalSeries);
		lineChartXNumberAxis.setLabel("Time in Seconds!");
		lineChartXNumberAxis.setTickLabelFormatter(SecondsToTimeConverter.getConverter());
		lineChartYNumberAxis.setLabel("Quest Stacks");
	}

	private void onGameSelect(ActionEvent event) {
		if(!(event.getSource() instanceof ChoiceBox<?>)) {
			return;
		}
		ChoiceBox<GameChoiceBoxValue> choiceBox = (ChoiceBox<GameChoiceBoxValue>) event.getSource();
		if (choiceBox.getValue() == null) {
			System.out.println("selecting first");
			if (choiceBox.getItems().size() > 0) {
				// TODO: smells like infinite loop
				choiceBox.getSelectionModel().selectFirst();
			}
			return;
		}
		String gamePath = choiceBox.getValue().getKey();
		// load data into talent dropdown
		List<String> talentNames = DataLoader.getTalentNames(gamePath);
		dropDownTalents.setItems(FXCollections.observableArrayList(talentNames));
		dropDownTalents.getSelectionModel().selectFirst();
		
		labelStatsMap.setText("Map: " + choiceBox.getValue().getMap());
	}

	private void onTalentSelect(ActionEvent event) {
		String gamePath = dropDownGames.getValue().getKey();
		if (gamePath == null) {
			System.out.println("no game was selected");
			return;
		}
		if(!(event.getSource() instanceof ChoiceBox<?>)) {
			return;
		}
		
		ChoiceBox<String> choiceBox = (ChoiceBox<String>) event.getSource();
		if (choiceBox.getValue() == null) {
			// game was selected, so talent dropdown updated
			choiceBox.getSelectionModel().selectFirst();
		}

		String talentName = choiceBox.getValue();
		int index = DataLoader.getIndexOfTalentName(gamePath, talentName);
		if (index == 0) {
			System.out.println("talent does not exist in file");
			return;
		}
		setGraphData(gamePath, index - 1, talentName);
	}

	private void initializeHeroData() {
		List<HeroData> heroDataList = DataLoader.getHeroData();
		Map<String, HeroData> dataMap = new HashMap<>();

		for (HeroData heroData : heroDataList) {
			dataMap.put(heroData.getName(), heroData);
		}

		heroDataMap = dataMap;
	}

}
