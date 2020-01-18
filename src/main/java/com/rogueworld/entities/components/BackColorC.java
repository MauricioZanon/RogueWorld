package com.rogueworld.entities.components;

import javafx.scene.paint.Color;

/**Este component tiene el color de fondo de las entidades que lo necesiten*/
public class BackColorC extends Component{

	public Color[] colors = new Color[10];
	
	public BackColorC() {
		isShared = true;
	}
	
	@Override
	public BackColorC clone() {
		BackColorC c = new BackColorC();
		for(int i = 0; i < colors.length; i++) {
			c.colors[i] = colors[i];
		}
		return c;
	}

	@Override
	public void serialize(StringBuilder sb) {}

	@Override
	public void deserialize(String info) {}

	@Override
	public boolean equals(Component comp) {
		return true;
	}

}
