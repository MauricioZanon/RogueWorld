package com.rogueworld.gui.gamescreen;

import com.rogueworld.world.tile.Tile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class LightningDrawer {
	
	private static GraphicsContext gc;
	private static int tileSize;
	
	protected static void initialize(GraphicsContext context) {
		gc = context;
		gc.setFill(new Color(0.1, 0.1, 0.1, 1));
		tileSize = DrawUtils.tileSize;
	}
	
	protected static void draw(Tile tile, int x, int y) {
		gc.setGlobalAlpha(1 - tile.getLightLevel());
		gc.fillRect(x, y, tileSize, tileSize);
	}
	
	protected static void reset() {
		gc.clearRect(0, 0, DrawUtils.screenSize, DrawUtils.screenSize);
	}
}
