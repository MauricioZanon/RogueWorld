// package itemmenus;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.TextField;
//import javafx.scene.input.KeyEvent;
//import javafx.scene.layout.StackPane;
//
//public class SearchBarController {
//
//    @FXML public StackPane searchBar;
//    @FXML public TextField searchField;
//    
//    public void initialize(){
//    	
//    	searchBar.focusedProperty().addListener((observable, oldValue, newValue) -> {
//    		if(newValue) {
//    			searchField.requestFocus();
//    			searchField.clear();
//    		}
//    	});
//    	
//    	searchField.textProperty().addListener((observable, oldValue, newValue) -> {
//    		MenuUtils.fillItemList();
//    	});
//    	
//    	MenuUtils.filter = MenuUtils.filter.and(e -> e.name.contains(searchField.getText()));
//    	
//    }
//    
//    @FXML
//    public void handleKeyPressed(KeyEvent event) {
//    	switch(event.getCode()) {
//    	case ESCAPE:
//    		searchField.clear();
//    		searchField.setVisible(false);
//    		event.consume();
//    		break;
//    	default:
//    		break;
//    	}
//    }
//    
//    
//
//}
