package com.rogueworld.entities.components;

import javafx.scene.paint.Color;

/** Este component tiene el ascii y el color de las entidades*/
public class GraphicC extends Component{
	
	public String ASCII;
	public Color color;
	
	public GraphicC() {
		isShared = true;
	}

	@Override
	public GraphicC clone() {
		GraphicC c = new GraphicC();
		c.ASCII = ASCII;
		c.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity());
		return c;
	}

	@Override
	public void serialize(StringBuilder sb) {}
	
	@Override
	public void deserialize(String info) {}

	@Override
	public boolean equals(Component comp) {
		GraphicC c = (GraphicC) comp;
		return ASCII.equals(c.ASCII) && color.equals(c.color);
	}

}
