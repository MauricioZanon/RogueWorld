package com.rogueworld.gui.gamescreen;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Console{
	
	public static ObservableList<Node> messages = null; //Se inicializa en GameScreenController
	
	private static Font font = Font.font("courier new", FontWeight.BLACK, 14);
	
	private Console() {}
	
	/**
	 * Agrega un mensaje a la consola
	 * @param text El texto que se quiere agregar, debe estar separado con - para cada parte del texto
	 * @param colors Los colores que se usan, se usan en orden para cada parte del texto
	 */
	public static void addMessage(String text, Color... colors) {
		String[] splittedText = text.split("-");
		for(int i = 0; i < splittedText.length; i++) {
			addToMessageList(createTextNode(splittedText[i], colors[i]));
		}
	}
	
	public static void addMessage(String text) {
		addToMessageList(createTextNode(text, Color.WHITE));
	}
	
	private static void addToMessageList(Text textNode) {
		Platform.runLater(() -> messages.add(textNode));
	}
	
	private static Text createTextNode(String message, Color color) {
		Text text = new Text(message);
		text.setFont(font);
		text.setFill(color);
		text.setOnMouseEntered(e -> {
			//TODO si es el nombre de una entidad aparece un texto flotante con su descripcion
		});
		return text;
	}

}
