package com.rogueworld.gui.itemmenus;

import com.rogueworld.gui.system.RenderSystem;
import com.rogueworld.application.Main;
import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.main.Entity;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Este controller se usa para todos los menus que sirvan solo para 
 * elegir un item para realizar una acci�n (Quaff, Wear, etc)
 */
public class ItemSelectionController {

	@FXML public BorderPane root;
	@FXML public TreeView<Text> itemList;
    @FXML public Label title;
    @FXML public TextFlow itemDesc;
//	public StackPane searchBar;
    
    @FXML
    public void initialize() {
    	title.setText(MenuDataHolder.title);
    	
    	itemList.setRoot(new TreeItem<Text>());
    	MenuUtils.fillItemList(itemList, MenuDataHolder.containers[0].items);
    	
    	itemList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    		Entity selectedEntity = MenuUtils.getSelectedItem(itemList, Main.player.get(ContainerC.class));
			MenuUtils.refreshItemDesc(selectedEntity, itemDesc.getChildren());
		});
    	
//    	searchBar = (StackPane) RenderSystem.getInstance().loadNode("SearchBar.fxml");
//    	root.setBottom(searchBar);
    	
    }

    @FXML
    public void handlePressedKeyInItemList(KeyEvent event) {
    	switch(event.getCode()) {
		case F:
//			searchBar.requestFocus();
			break;
		case DOWN:
		case NUMPAD2:
			itemList.getSelectionModel().selectNext();
			break;
		case UP:
		case NUMPAD8:
			itemList.getSelectionModel().selectPrevious();
			break;
		case ESCAPE:
			RenderSystem.getInstance().closeSecondaryStage();
			break;
		case ENTER:
			if (itemList.getSelectionModel().getSelectedIndex() > 0) {
				if(MenuDataHolder.closeOnAction) {
					RenderSystem.getInstance().closeSecondaryStage();
					MenuDataHolder.action.accept(MenuUtils.getSelectedItem(itemList, Main.player.get(ContainerC.class)));
				}else {
					int selectionIndex = itemList.getSelectionModel().getSelectedIndex();
					MenuDataHolder.action.accept(MenuUtils.getSelectedItem(itemList, MenuDataHolder.containers[0]));
					MenuUtils.fillItemList(itemList, MenuDataHolder.containers[0].items);
					itemList.getSelectionModel().select(selectionIndex);
				}
			}
			break;
		default:
			break;
		}
		event.consume();
    }

}
