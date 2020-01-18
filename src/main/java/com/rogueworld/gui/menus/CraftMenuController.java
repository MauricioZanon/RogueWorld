package com.rogueworld.gui.menus;

import java.util.ArrayList;
import java.util.List;

import com.rogueworld.actions.actions.Craft;
import com.rogueworld.gui.system.RenderSystem;
import com.rogueworld.entities.crafts.CraftRecipe;
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

public class CraftMenuController {

    @FXML public HBox categories;
    @FXML public ListView<Text> craftablesList;
    @FXML public TextFlow description;
    @FXML public TextField searchField;
    @FXML public TextFlow requirements;
    
    private List<Text> availableCategories = new ArrayList<>();
    private List<CraftRecipe> shownRecipes;
    private int categoryIndex = 0;

    public void initialize() {
    	Text weaponsText = new Text("Weapons");
    	weaponsText.setFont(Font.font("courier new", FontWeight.BLACK, 22));
    	weaponsText.setFill(Color.WHITE);
    	availableCategories.add(weaponsText);
    	
    	Text armorText = new Text("Armor");
    	armorText.setFont(Font.font("courier new", FontWeight.BLACK, 22));
    	armorText.setFill(Color.WHITE);
    	availableCategories.add(armorText);
    	
    	Text clothesText = new Text("Clothes");
    	clothesText.setFont(Font.font("courier new", FontWeight.BLACK, 22));
    	clothesText.setFill(Color.WHITE);
    	availableCategories.add(clothesText);
    	
    	Text jewelsText = new Text("Jewels");
    	jewelsText.setFont(Font.font("courier new", FontWeight.BLACK, 22));
    	jewelsText.setFill(Color.WHITE);
    	availableCategories.add(jewelsText);
    	
    	Text foodText = new Text("Food");
    	foodText.setFont(Font.font("courier new", FontWeight.BLACK, 22));
    	foodText.setFill(Color.WHITE);
    	availableCategories.add(foodText);
    	
    	Text magicItemsText = new Text("Magic items");
    	magicItemsText.setFont(Font.font("courier new", FontWeight.BLACK, 22));
    	magicItemsText.setFill(Color.WHITE);
    	availableCategories.add(magicItemsText);
    	
    	Text miscText = new Text("Misc");
    	miscText.setFont(Font.font("courier new", FontWeight.BLACK, 22));
    	miscText.setFill(Color.WHITE);
    	availableCategories.add(miscText);
    	
    	categories.getChildren().addAll(availableCategories);
    	
    	changeSelectedCategory(0);
    }
    
    @FXML
    public void handlePressedKey(KeyEvent event) {
		int selectedIndex = craftablesList.getSelectionModel().getSelectedIndex();
		
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
    		if(selectedIndex == craftablesList.getItems().size()-1) {
    			craftablesList.getSelectionModel().select(0);
    		}else {
    			craftablesList.getSelectionModel().select(selectedIndex+1);
    		}
    		refreshRequirements();
    		break;
    	case UP:
    	case NUMPAD8:
    		if(selectedIndex < 1) {
    			craftablesList.getSelectionModel().select(craftablesList.getItems().size()-1);
    		}else {
				craftablesList.getSelectionModel().select(selectedIndex-1);
    		}
    		refreshRequirements();
    		break;
    	case ENTER:
    		if(selectedIndex != -1) {
    			CraftRecipe selectedRecipe = shownRecipes.get(craftablesList.getSelectionModel().getSelectedIndex());
    			if(selectedRecipe.isCraftable()) {
    				Craft.execute(selectedRecipe);
    				RenderSystem.getInstance().closeSecondaryStage();
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
    	
    	refreshCraftablesList(selectedCategory.getText());
    }
    
    private void refreshCraftablesList(String category) {
    	craftablesList.getItems().clear();
    	shownRecipes = RecipeList.craftRecipes.get(category);
    	
    	if(shownRecipes != null) {
    		for(CraftRecipe r : shownRecipes) {
    			Text text = new Text(r.name);
    			if(r.isCraftable()) {
    				text.setFill(Color.WHITE);
    			}else {
    				text.setFill(Color.GREY);
    			}
    			craftablesList.getItems().add(text);
    		}
    	}
    	
    }
    
    private void refreshRequirements() {
    	requirements.getChildren().clear();

		if(craftablesList.getSelectionModel().getSelectedIndex() < 0) // Si no hay nada seleccionado...
			return;
		
    	CraftRecipe selectedRecipe = shownRecipes.get(craftablesList.getSelectionModel().getSelectedIndex());
    	
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
    	
    	String[] workStations = selectedRecipe.workStations.split("-");
    	if(workStations.length > 1) {
    		Text workStationsTitle = new Text("\nWork stations\n");
    		workStationsTitle.setFont(Font.font("courier new", FontWeight.BOLD, 16));
    		workStationsTitle.setUnderline(true);
    		workStationsTitle.setFill(Color.AQUAMARINE);
    		requirements.getChildren().add(workStationsTitle);
    		
    		for(int i = 0; i < workStations.length; i++) {
    			Text t = new Text(workStations[i] + "\n");
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
    }

}
