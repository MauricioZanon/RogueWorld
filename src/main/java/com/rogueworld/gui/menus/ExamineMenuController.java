package com.rogueworld.gui.menus;

import java.util.HashMap;
import java.util.Map;

import com.rogueworld.actions.actions.Examine;
import com.rogueworld.actions.actions.Use;
import com.rogueworld.gui.system.RenderSystem;
import com.rogueworld.application.Main;
import com.rogueworld.entities.components.UsesC;
import com.rogueworld.entities.components.UsesC.UseType;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.utils.text.StringUtils;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class ExamineMenuController {

	@FXML public BorderPane root;
	@FXML public ListView<String> entitiesList;
    @FXML public ListView<String> actionList;
//	public StackPane searchBar;
    
    private Map<String, Entity> usableEntities = new HashMap<>();

    public void initialize() {
    	for(Entity e : Examine.examinedTile.getEntities(e -> e.has(UsesC.class))) {
    		usableEntities.put(StringUtils.toTitle(e.name), e);
    	}
    	usableEntities.keySet().forEach(n -> entitiesList.getItems().add(n));
    	
    	entitiesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			refreshActionList();
		});
    	
    	actionList.focusedProperty().addListener((observable, oldValue, newValue) -> {
    		if(newValue) {
    			actionList.getSelectionModel().select(0);
    		}
    	});
    	
    	entitiesList.getSelectionModel().clearAndSelect(0);
    	
//    	searchBar = (StackPane) RenderSystem.getInstance().loadNode("SearchBar.fxml");
//    	root.setBottom(searchBar);
    	
    }
    
    private void refreshActionList() {
    	actionList.getItems().clear();
    	Entity selectedEntity = usableEntities.get(entitiesList.getSelectionModel().getSelectedItem());
    	selectedEntity.get(UsesC.class).uses.forEach(u -> actionList.getItems().add(u.toString()));
    	
    }
    
    @FXML
    public void handlePressedKeyInEntitiesList(KeyEvent event) {
    	switch(event.getCode()) {
		case F:
//			searchBar.requestFocus();
			break;
		case DOWN:
		case NUMPAD2:
			System.out.println(entitiesList.getSelectionModel().getSelectedIndex());
			entitiesList.getSelectionModel().selectNext();
			System.out.println(entitiesList.getSelectionModel().getSelectedIndex());
			break;
		case UP:
		case NUMPAD8:
			entitiesList.getSelectionModel().selectPrevious();
			break;
		case RIGHT:
		case NUMPAD6:
		case ENTER:
			actionList.requestFocus();
			break;
		case ESCAPE:
			RenderSystem.getInstance().closeSecondaryStage();
			break;
		default:
			break;
		}
		event.consume();
    }
    
    @FXML
    public void handleKeyPressedInActionList(KeyEvent event) {
    	switch(event.getCode()) {
		case DOWN:
		case NUMPAD2:
			actionList.getSelectionModel().selectNext();
			break;
		case UP:
		case NUMPAD8:
			actionList.getSelectionModel().selectPrevious();
			break;
		case LEFT:
		case NUMPAD4:
			entitiesList.requestFocus();
			break;
    	case ENTER:
    		RenderSystem.getInstance().closeSecondaryStage();
    		Entity selectedEntity = usableEntities.get(entitiesList.getSelectionModel().getSelectedItem());
    		if(selectedEntity != null) {
    			UseType selectedUse = UseType.valueOf(actionList.getSelectionModel().getSelectedItem());
    			Use.execute(selectedEntity, Main.player, selectedUse);
    		}
    		break;
    	case ESCAPE:
			RenderSystem.getInstance().closeSecondaryStage();
			break;
		default:
			break;
    	}
    	event.consume();

    }


}
