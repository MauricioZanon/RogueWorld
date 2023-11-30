package com.rogueworld.application;

import java.io.File;

import com.rogueworld.ai.eventsystem.EventSystem;
import com.rogueworld.gui.system.RenderSystem;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.crafts.RecipeList;
import com.rogueworld.entities.factories.EntityFactory;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.player.PlayerBuilder;
import com.rogueworld.utils.persistency.StateLoader;
import com.rogueworld.utils.persistency.StateSaver;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.time.Clock;
import com.rogueworld.world.world.WorldBuilder;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application { 
	
	public static Entity player;
	
	// Correr mvn clean javafx:run
	public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
    	EntityFactory.loadEntities();
    	RecipeList.loadRecipes();
    	Clock.initialize();
    	
    	RenderSystem.getInstance().initialize(primaryStage);
    	RenderSystem.getInstance().changeScene("MainMenuScreen.fxml", false);
    	primaryStage.show();
    }
    
    public static void newGame() {
    	String worldName = "world";
    	File saveFile = new File("resources/Saves/" + worldName + ".db");
    	if(saveFile.exists()) {
    		boolean wasDeleted = saveFile.delete();
    		System.out.println("previows save file deleted: " + wasDeleted);
    	}
    	
    	WorldBuilder.createWorld(worldName);
    	
    	StateSaver.getInstance().savingThread.start();
    	
    	player = PlayerBuilder.createBasePlayer();
    	Map.getTile(0, 0, 0).put(player);
    	Map.refresh();
    	
    	RenderSystem.getInstance().changeScene("GameScreen.fxml", false);
    	
    	startGameLoop();
    	
    }
    
    public static void loadGame() {
    	player = PlayerBuilder.createBasePlayer();
    	StateSaver.getInstance().savingThread.start();
    	
    	StateLoader.getInstance().loadWorldState();
    	StateLoader.getInstance().loadPlayer();
    	
    	player.get(PositionC.class).getTile().put(player);
    	Map.refresh();
    	RenderSystem.getInstance().changeScene("GameScreen.fxml", false);
    	startGameLoop();
    }
    
    public static void startGameLoop() {
    	new Thread(() -> {
    		while(!Thread.currentThread().isInterrupted()) {
    			if(!EventSystem.isPlayersTurn()) {
    				long tiempo = System.currentTimeMillis();
    				EventSystem.update();
    				
    				long sleepTime = 70 - (System.currentTimeMillis() - tiempo);
    				if(sleepTime > 0) {
    					try {
    						Thread.sleep(sleepTime);
    					} catch (InterruptedException e) {}
    				}
    			}
    		}
    	}).start();
    }
    
}
