package com.rogueworld.gui.itemmenus;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class ItemExchangeController {

    @FXML public TextFlow itemDesc;
    @FXML public StackPane bottomBar;
    @FXML public TextField searchField;
    @FXML public TreeView<Text> leftItemList;
    @FXML public TreeView<Text> rightItemList;

    @FXML
    public void initialize() {
    	
    }
    
    @FXML 
    public void handlePressedKeyInLefItemList(KeyEvent event) {

    }

    @FXML
    public void handlePressedKeyInRighttemList(KeyEvent event) {

    }

}
