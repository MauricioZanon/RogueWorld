package com.rogueworld.gui.menus;

import java.util.ArrayList;
import java.util.List;

import com.rogueworld.actions.actions.Build;
import com.rogueworld.gui.gamescreen.InputConfig;
import com.rogueworld.gui.system.RenderSystem;
import com.rogueworld.entities.crafts.BuildRecipe;
import com.rogueworld.entities.crafts.RecipeList;
import com.rogueworld.utils.text.StringUtils;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class BuildMenuController {
	
    @FXML public HBox categories;
    @FXML public ListView<Text> buildablesList;
    @FXML public TextFlow description;
    @FXML public TextField searchField;
    @FXML public TextFlow requirements;

    private List<Text> availableCategories = new ArrayList<>();
    private List<BuildRecipe> shownRecipes;
    private int categoryIndex = 0;

    public void initialize() {
    	Text wallsText = new Text("Walls");
    	wallsText.setFont(Font.font("courier new", FontWeight.BLACK, 22));
    	wallsText.setFill(Color.WHITE);
    	availableCategories.add(wallsText);
    	
    	Text floorsText = new Text("Floors");
    	floorsText.setFont(Font.font("courier new", FontWeight.BLACK, 22));
    	floorsText.setFill(Color.WHITE);
    	availableCategories.add(floorsText);
    	
    	Text featuresText = new Text("Features");
    	featuresText.setFont(Font.font("courier new", FontWeight.BLACK, 22));
    	featuresText.setFill(Color.WHITE);
    	availableCategories.add(featuresText);
    	
    	Text trapsText = new Text("Traps");
    	trapsText.setFont(Font.font("courier new", FontWeight.BLACK, 22));
    	trapsText.setFill(Color.WHITE);
    	availableCategories.add(trapsText);
    	
    	categories.getChildren().addAll(availableCategories);
    	
    	changeSelectedCategory(0);
    }
    
    @FXML
    public void handlePressedKey(KeyEvent event) {
		int selectedIndex = buildablesList.getSelectionModel().getSelectedIndex();
		
    	switch(event.getCode()) {
    	case ESCAPE:
    		RenderSystem.getInstance().closeSecondaryStage();
    		break;
    	case LEFT:
    	case NUMPAD4:
    		changeSelectedCategory(-1);
    		refreshRequirements();
    		break;
    	case RIGHT:
    	case NUMPAD6:
    		changeSelectedCategory(1);
    		refreshRequirements();
    		break;
    	case DOWN:
    	case NUMPAD2:
    		if(selectedIndex == buildablesList.getItems().size()-1) {
    			buildablesList.getSelectionModel().select(0);
    		}else {
    			buildablesList.getSelectionModel().select(selectedIndex+1);
    		}
    		refreshRequirements();
    		break;
    	case UP:
    	case NUMPAD8:
    		if(selectedIndex < 1) {
    			buildablesList.getSelectionModel().select(buildablesList.getItems().size()-1);
    		}else {
				buildablesList.getSelectionModel().select(selectedIndex-1);
    		}
    		refreshRequirements();
    		break;
    	case ENTER:
    		if(selectedIndex != -1) {
    			BuildRecipe selectedRecipe = shownRecipes.get(buildablesList.getSelectionModel().getSelectedIndex());
    			if(selectedRecipe.isBuildable()) {
    				Build.recipe = selectedRecipe;
    				RenderSystem.getInstance().closeSecondaryStage();
    				InputConfig.setBuildInput();
    			}
    		}
    		break;
    	default:
    		break;
    	}
    	
    	event.consume();
    }

	private void changeSelectedCategory(int variation) {
    	Text selectedCategory = availableCategories.get(categoryIndex);
    	selectedCategory.setFill(Color.WHITE);
    	
    	categoryIndex += variation;
    	if(categoryIndex < 0) categoryIndex = availableCategories.size()-1;
    	else if(categoryIndex > availableCategories.size()-1) categoryIndex = 0;
    	
    	selectedCategory = availableCategories.get(categoryIndex);
    	selectedCategory.setFill(Color.YELLOW);
    	
    	refreshbuildablesList(selectedCategory.getText());
    }
    
    private void refreshbuildablesList(String category) {
    	buildablesList.getItems().clear();
    	shownRecipes = RecipeList.buildRecipes.get(category);
    	
    	if(shownRecipes != null) {
    		for(BuildRecipe r : shownRecipes) {
    			Text text = new Text(r.name);
    			if(r.isBuildable()) {
    				text.setFill(Color.WHITE);
    			}else {
    				text.setFill(Color.GREY);
    			}
    			buildablesList.getItems().add(text);
    		}
    	}
    	
    }
    
    private void refreshRequirements() {
    	requirements.getChildren().clear();

		if(buildablesList.getSelectionModel().getSelectedIndex() < 0) // Si no hay nada seleccionado...
			return;
		
		BuildRecipe selectedRecipe = shownRecipes.get(buildablesList.getSelectionModel().getSelectedIndex());
    	
    	Text recipeName = new Text(StringUtils.toTitle(selectedRecipe.name) + "\n\n");
    	recipeName.setFont(Font.font("courier new", FontWeight.BLACK, 20));
    	recipeName.setUnderline(true);
    	recipeName.setFill(Color.WHITE);
    	requirements.getChildren().add(recipeName);
    	
    	String[] materials = selectedRecipe.materials.split("&");
    	if(materials.length > 1) {
    		Text materialsTitle = new Text("Materials\n");
    		materialsTitle.setFont(Font.font("courier new", FontWeight.BOLD, 16));
    		materialsTitle.setUnderline(true);
    		materialsTitle.setFill(Color.AQUAMARINE);
    		
    		requirements.getChildren().add(materialsTitle);
    		for(int i = 0; i < materials.length; i++) {
    			Text t = new Text(materials[i].replace("|", " or ").replace("_", " ") + "\n");
    			t.setFont(Font.font("courier new", FontWeight.BOLD, 14));
    			t.setFill(Color.WHITE);
    			requirements.getChildren().add(t);
    		}
    	}
    	
    	String[] tools = selectedRecipe.tools.split("-");
    	if(tools.length > 1) {
    		Text toolsTitle = new Text("\nTools\n");
    		toolsTitle.setFont(Font.font("courier new", FontWeight.BOLD, 16));
    		toolsTitle.setUnderline(true);
    		toolsTitle.setFill(Color.AQUAMARINE);
    		requirements.getChildren().add(toolsTitle);
    		
    		for(int i = 0; i < tools.length; i++) {
    			Text t = new Text(tools[i] + "\n");
    			t.setFont(Font.font("courier new", FontWeight.BOLD, 14));
    			t.setFill(Color.WHITE);
    			requirements.getChildren().add(t);
    		}
    	}
    	
    	String[] skills = selectedRecipe.skills.split(" ");
    	if(skills.length > 1) {
    		Text skillsTitle = new Text("\nSkills\n");
    		skillsTitle.setFont(Font.font("courier new", FontWeight.BOLD, 16));
    		skillsTitle.setUnderline(true);
    		skillsTitle.setFill(Color.AQUAMARINE);
    		requirements.getChildren().add(skillsTitle);
    		
    		for(int i = 0; i < skills.length; i++) {
    			Text t = new Text(StringUtils.toTitle(skills[i]) + "\n");
    			t.setFont(Font.font("courier new", FontWeight.BOLD, 14));
    			t.setFill(Color.WHITE);
    			requirements.getChildren().add(t);
    		}
    	}
    	
    	String[] onTerrain = selectedRecipe.onTerrain;
    	if(onTerrain.length > 0) {
    		Text onTerrainTitle = new Text("\nNeeds to be over\n");
    		onTerrainTitle.setFont(Font.font("courier new", FontWeight.BOLD, 16));
    		onTerrainTitle.setUnderline(true);
    		onTerrainTitle.setFill(Color.AQUAMARINE);
    		requirements.getChildren().add(onTerrainTitle);
    		
    		for(int i = 0; i < onTerrain.length; i++) {
    			Text t = new Text(StringUtils.toTitle(onTerrain[i]) + "\n");
    			t.setFont(Font.font("courier new", FontWeight.BOLD, 14));
    			t.setFill(Color.WHITE);
    			requirements.getChildren().add(t);
    		}
    	}
    	
    	String[] nextToTerrain = selectedRecipe.nextToTerrain;
    	if(nextToTerrain.length > 0) {
    		Text nextToTerrainTitle = new Text("\nNeeds to be next to\n");
    		nextToTerrainTitle.setFont(Font.font("courier new", FontWeight.BOLD, 16));
    		nextToTerrainTitle.setUnderline(true);
    		nextToTerrainTitle.setFill(Color.AQUAMARINE);
    		requirements.getChildren().add(nextToTerrainTitle);
    		
    		for(int i = 0; i < nextToTerrain.length; i++) {
    			Text t = new Text(StringUtils.toTitle(nextToTerrain[i]) + "\n");
    			t.setFont(Font.font("courier new", FontWeight.BOLD, 14));
    			t.setFill(Color.WHITE);
    			requirements.getChildren().add(t);
    		}
    	}
    }

}