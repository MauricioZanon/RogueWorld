package com.rogueworld.gui.menus;

import com.rogueworld.gui.system.RenderSystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.TextFlow;

public class NewPlayerMenuController {

    @FXML public TabPane tabs;
    @FXML public ListView<String> raceList;
    @FXML public TextFlow raceDesc;
    @FXML public ListView<String> profList;
    @FXML public TextFlow profDesc;
    @FXML public ListView<String> sceneList;
    @FXML public TextFlow sceneDesc;
    @FXML public TextField nameField;
    @FXML public ChoiceBox<String> sexChoice;

    public void initialize() {
    	sexChoice.getItems().add("Male");
    	sexChoice.getItems().add("Female");
    }
    
    @FXML
	public void handlePressedKey(KeyEvent key) {
		switch(key.getCode()) {
		case TAB:
			if(key.isShiftDown()) {
				tabs.getSelectionModel().selectPrevious();
			}else {
				tabs.getSelectionModel().selectNext();
			}
			break;
		case ESCAPE:
			RenderSystem.getInstance().closeSecondaryStage();
			break;
		default:
			break;
		}
		key.consume();
	}

    @FXML
    public void createPlayer(ActionEvent event) {
    	System.out.println("create player");
    }

    @FXML
    public void randomizeName(ActionEvent event) {
    	System.out.println("random name");
    }

}
