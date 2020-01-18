package com.rogueworld.gui.system;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RenderSystem {
	
	private static RenderSystem instance = null;
	private Stage primaryStage;
	private Stage secondaryStage;
	
	private RenderSystem() {}
	
	public void initialize(Stage ps) {
		primaryStage = ps;
		configureStage();
	}
	
	public void changeScene(String fxmlFileName, boolean newWindow) {
		Scene scene = loadScene(fxmlFileName);
				
		if(scene != null) {
			if(newWindow) {
				if(secondaryStage != null) secondaryStage.close();
				secondaryStage = new Stage();
				secondaryStage.setScene(scene);
				secondaryStage.show();
			}
			else {
				primaryStage.setScene(scene);
			}
			scene.getRoot().requestFocus();
		}
	}
	
	private Scene loadScene(String fxmlFileName) {
		URL url = null;
		try {
			url = new URL("file:resources/fxml/"+ fxmlFileName);
		} catch (MalformedURLException e1) {}
		try {
			return new Scene(new FXMLLoader(url).load());
		} catch (IOException e) {
			System.out.println("Fall� la inicializaci�n de " + fxmlFileName + ". Cuando pasa esto casi siempre es porque no se puso\n"
					+ " bien la direcion del controller en el FXML o porque hay un error en el metodo initialize del controller,\n"
					+ " en vez de tirar error en ese metodo tira IOException aca");
			e.printStackTrace();
			return null;
		}
	}
	
    public Node loadNode(String fxmlNodeName) {
		URL url = null;
		try {
			url = new URL("file:src/main/java/com/rogueworld/GUI/FXML/"+ fxmlNodeName);
		} catch (MalformedURLException e1) {}
		try {
			return new FXMLLoader(url).load();
		} catch (IOException e) {
			System.out.println("Error al cargar el nodo " + fxmlNodeName);
			e.printStackTrace();
			return null;
		}
    }
	
	public double getSceneHeight() {
		return primaryStage.getScene().getHeight();
	}

	public double getSceneWidth() {
		return primaryStage.getScene().getWidth();
	}

    private void configureStage() {
    	primaryStage.setTitle("Rogue World");
    	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	double width = screenSize.getHeight() + (screenSize.getWidth() - screenSize.getHeight()) / 2;
    	double height = screenSize.getHeight();
    	primaryStage.setWidth(width);
    	primaryStage.setHeight(height);
    }
    
    public void closeSecondaryStage() {
    	secondaryStage.hide();
    }
    
	public static RenderSystem getInstance() {
		if(instance == null) {
			instance = new RenderSystem();
		}
		return instance;
	}
	
	
}
