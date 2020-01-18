package com.rogueworld.gui.menus;

import com.rogueworld.gui.system.RenderSystem;
import com.rogueworld.application.Main;

import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;

public class MainScreenController {

	public void initialize(){
		System.out.println("Metodo initialize de main screen controller");
	}
	
	@FXML
	public void startNewGame() {
		Main.newGame();
	}
	
	@FXML
	public void openNewCharacterMenu() {
		RenderSystem.getInstance().changeScene("NewPlayerMenu.fxml", true);
	}
	
	@FXML
	public void loadGame() {
		Main.loadGame();
	}
	
	@FXML
	public void exitGame() {
		System.exit(0);
	}
    
	@FXML
	public void handlePressedKey(KeyEvent key) {
		switch(key.getCode()) {
		case C:
			openNewCharacterMenu();
			break;
		case L:
			Main.loadGame();
			break;
		case N:
			Main.newGame();
        	break;
		case ESCAPE:
			System.exit(0);
			break;
		default:
			break;
		}
		key.consume();
	}

}
