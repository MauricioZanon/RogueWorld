package com.rogueworld.gui.gamescreen;

import com.rogueworld.gui.system.RenderSystem;
import com.rogueworld.entities.player.PlayerInfo;

import javafx.beans.property.SimpleFloatProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ResourceBar extends StackPane{
	
	public ResourceBar(String resourceName, Color color, SimpleFloatProperty observedProp) {
		double maxWidth = RenderSystem.getInstance().getSceneWidth() - RenderSystem.getInstance().getSceneHeight();
		setAlignment(Pos.CENTER_LEFT);
		Color backColor = color.darker();
		setStyle("-fx-background-color: " + backColor.toString().replaceAll("0x", "#"));
		
		Rectangle bar = new Rectangle();
		bar.setFill(color);
		bar.setStroke(backColor);
		bar.setStrokeWidth(3);
		bar.setWidth(maxWidth * observedProp.get() / PlayerInfo.MAX_HP.get());
		bar.setHeight(25);
		getChildren().add(bar);
		
		
		Label text = new Label(resourceName);
		text.setFont(Font.font("Courier New", FontWeight.BLACK, bar.getHeight()));
		getChildren().add(text);
		
		setOnMouseEntered(e -> text.setText(Float.toString(observedProp.get())));
		setOnMouseExited(e -> text.setText(resourceName));
		
		PlayerInfo.CUR_HP.addListener((p, oldValue, newValue) -> {
			if(oldValue != newValue) {
				float percentage = (Float)newValue / PlayerInfo.MAX_HP.get();
				bar.setWidth(maxWidth * percentage);
			}
		});
	}
	
}
