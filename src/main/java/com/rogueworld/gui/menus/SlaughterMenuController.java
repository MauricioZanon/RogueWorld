package com.rogueworld.gui.menus;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import com.rogueworld.actions.actions.Slaughter;
import com.rogueworld.gui.system.RenderSystem;
import com.rogueworld.application.Main;
import com.rogueworld.entities.components.ButcherC;
import com.rogueworld.entities.components.FieldDressC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.components.SkillsC.Skill;
import com.rogueworld.entities.components.SkinC;
import com.rogueworld.entities.components.ToolC.Tool;
import com.rogueworld.entities.factories.EntityFactory;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class SlaughterMenuController {

    @FXML public StackPane bottomBar;
    @FXML public TextField searchField;
    @FXML public ListView<String> entitiesList;
    @FXML public ListView<String> slaughterOptionList;
    @FXML public TextFlow descField;
    
    private List<Entity> validEntities = new ArrayList<>();
    
    public void initialize() {
    	fillEntitiesList();
    	
    	entitiesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    		fillOptionsList();
    	});
    	
    	slaughterOptionList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    		writeDescription();
    	});
    	
    	entitiesList.requestFocus();
    }
    
    @FXML
    public void fillEntitiesList() {
    	Set<Tile> tiles = Map.getAdjacentTiles(Main.player.get(PositionC.class).getTile());
    	tiles.add(Main.player.get(PositionC.class).getTile());
    	
    	Predicate<Entity> isSlaughterable = e -> e.type == Type.CORPSE && 
    			(e.has(FieldDressC.class) || e.has(SkinC.class) || e.has(ButcherC.class));
    	
    	for(Tile tile : tiles) {
    		validEntities.addAll(tile.getEntities(isSlaughterable));
    	}
    	
    	validEntities.forEach(e -> entitiesList.getItems().add(e.name));
    }
    
    private void fillOptionsList() {
    	int selectedIndex = entitiesList.getSelectionModel().getSelectedIndex();
    	if(selectedIndex == -1) return;
    	
    	Entity e = validEntities.get(selectedIndex);
    	
    	if(e.has(FieldDressC.class)) slaughterOptionList.getItems().add("Field dress");
    	if(e.has(SkinC.class)) slaughterOptionList.getItems().add("Skin");
    	if(e.has(ButcherC.class)) slaughterOptionList.getItems().add("Butcher");
    }
    
    private void writeDescription() {
    	ObservableList<Node> texts = descField.getChildren();
    	texts.clear();
    	
    	Text items = null;
    	Text tools = null;
    	Text skills = null;
    	
    	Entity selectedEntity = validEntities.get(entitiesList.getSelectionModel().getSelectedIndex());
    	String selectedOption = slaughterOptionList.getSelectionModel().getSelectedItem();
    	if(selectedOption.equals("Field dress")) {
    		FieldDressC c = selectedEntity.get(FieldDressC.class);
    		items = createItemsText(c.items);
    		tools = createToolsText(c.neededTools);
    		skills = createSkillsText(c.neededSkills);
    	}
    	else if(selectedOption.equals("Skin")) {
    		SkinC c = selectedEntity.get(SkinC.class);
    		items = createItemsText(c.items);
    		tools = createToolsText(c.neededTools);
    		skills = createSkillsText(c.neededSkills);
    	}
    	else if(selectedOption.equals("Butcher")) {
    		ButcherC c = selectedEntity.get(ButcherC.class);
    		items = createItemsText(c.items);
    		tools = createToolsText(c.neededTools);
    		skills = createSkillsText(c.neededSkills);
    	}
    	
    	Text obtainedItemsText = new Text("OBTAINED ITEMS: ");
    	obtainedItemsText.setStyle("-fx-font-weight: bold");
    	obtainedItemsText.setFill(Color.AQUA);
    	texts.add(obtainedItemsText);
    	texts.add(items);
    	
    	Text neededToolsText = new Text("TOOLS: ");
    	neededToolsText.setStyle("-fx-font-weight: bold");
    	neededToolsText.setFill(Color.AQUA);
    	texts.add(neededToolsText);
    	texts.add(tools);

    	Text neededSkillsText = new Text("SKILLS: ");
    	neededSkillsText.setStyle("-fx-font-weight: bold");
    	neededSkillsText.setFill(Color.AQUA);
    	texts.add(neededSkillsText);
    	texts.add(skills);
    }
    
    private Text createItemsText(String items) {
    	StringBuilder sb = new StringBuilder();
    	
    	String[] list = items.split(" ");
    	for (int i = 0; i < list.length; i++) {
    		String[] itemInfo = list[i].split("-");
    		String name = EntityFactory.create(Integer.parseInt(itemInfo[1])).name;
    		sb.append(name);
    		sb.append("(");
    		sb.append(itemInfo[0]);
    		sb.append(") ");
    	}
    	sb.append("\n");
    	
    	Text t = new Text(sb.toString());
    	t.setFill(Color.WHITE);
    	t.setStyle("-fx-font-weight: bold");
    	
    	return t;
    }
    
    private Text createToolsText(Set<Tool> tools) {
    	StringBuilder sb = new StringBuilder();
    	tools.forEach(f -> {
    		sb.append(f);
    		sb.append(" ");
    	});
    	sb.append("\n");
    	
    	Text t = new Text(sb.toString());
    	t.setFill(Color.WHITE);
    	t.setStyle("-fx-font-weight: bold");
    	
    	return t;
    }
    
    private Text createSkillsText(EnumMap<Skill, Integer> skills) {
    	StringBuilder sb = new StringBuilder();
    	skills.entrySet().forEach(s -> {
    		sb.append(s.getKey());
    		sb.append(":");
    		sb.append(s.getValue());
    		sb.append(" ");
    	});
    	sb.append("\n");
    	
    	Text t = new Text(sb.toString());
    	t.setFill(Color.WHITE);
    	t.setStyle("-fx-font-weight: bold");
    	
    	return t;
    }
    
    

    @FXML
    public void handleKeyPressedInEntitiesList(KeyEvent event) {
    	switch(event.getCode()) {
		case F:
			searchField.setVisible(true);
			searchField.requestFocus();
			break;
		case DOWN:
		case NUMPAD2:
			entitiesList.getSelectionModel().selectNext();
			break;
		case UP:
		case NUMPAD8:
			entitiesList.getSelectionModel().selectPrevious();
			break;
    	case ESCAPE:
    		RenderSystem.getInstance().closeSecondaryStage();
    		break;
    	case ENTER:
    	case NUMPAD6:
    	case RIGHT:
    		slaughterOptionList.requestFocus();
    		slaughterOptionList.getSelectionModel().select(0);
    		break;
		default:
			break;
    	}
    	event.consume();
    }

    @FXML
    public void handleKeyPressedInOptionsList(KeyEvent event) {
    	switch(event.getCode()) {
		case F:
			searchField.setVisible(true);
			searchField.requestFocus();
			break;
		case DOWN:
		case NUMPAD2:
			slaughterOptionList.getSelectionModel().selectNext();
			break;
		case UP:
		case NUMPAD8:
			slaughterOptionList.getSelectionModel().selectPrevious();
			break;
    	case ESCAPE:
    		RenderSystem.getInstance().closeSecondaryStage();
    		break;
    	case ENTER:
    		String selectedOption = slaughterOptionList.getSelectionModel().getSelectedItem();
    		Entity entity = validEntities.get(entitiesList.getSelectionModel().getSelectedIndex());
			Slaughter.execute(entity, selectedOption);
			
    		RenderSystem.getInstance().closeSecondaryStage();
    		break;
    	case NUMPAD4:
    	case LEFT:
    		entitiesList.requestFocus();
    		break;
		default:
			break;
    	}
    	event.consume();
    }

}
