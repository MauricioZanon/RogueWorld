package com.rogueworld.gui.gamescreen;

import java.util.HashSet;
import java.util.Set;

import com.rogueworld.gui.system.RenderSystem;
import com.rogueworld.application.Main;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.world.tile.Tile;

public class DrawUtils {
	
	protected static int tileQuantity = 45;
	protected static int tileSize;
	protected static double screenSize = RenderSystem.getInstance().getSceneHeight();
	protected static Tile selectedTile = null;
	
	public static boolean fullRedraw = true;
	public static Set<Tile> tilesToDraw = new HashSet<>();
	
	/**
	 * @return Devuelve la posici�n del @Tile que se dibuja en la esquina superior izquierda del canvas
	 */
	protected static PositionC getPos00() {
		PositionC pos00 = Main.player.get(PositionC.class).clone();
		int tileQuantity = (int) (RenderSystem.getInstance().getSceneHeight()/tileSize);
	    pos00.coord[0] -= tileQuantity/2;
	    pos00.coord[1] -= tileQuantity/2;
	    
	    return pos00;
	}
	
	/**
	 * @return Devuelve un array con dos doubles x e y, ambos son múltiplo de tileSize y representan la posición
	 * 		   del canvas en el que se debe dibujar un @Tile
	 */
	protected static double[] getDiscretePosInCanvas(PositionC pos) {
		PositionC pos00 = DrawUtils.getPos00();
		double x = (pos.coord[0] - pos00.coord[0])*DrawUtils.tileSize;
		double y = (pos.coord[1] - pos00.coord[1])*DrawUtils.tileSize;
		
		return new double[] {x, y};
	}

}
