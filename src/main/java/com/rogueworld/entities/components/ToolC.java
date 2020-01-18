package com.rogueworld.entities.components;

import java.util.EnumMap;

public class ToolC extends Component{
	
	/**
	 * La utilidad de la herramienta, mientras mas alto el integer mejor es
	 */
	public EnumMap<Tool, Integer> properties = new EnumMap<>(Tool.class);
	
	public ToolC() {
		isShared = true;
	}

	@Override
	public ToolC clone() {
		ToolC c = new ToolC();
		properties.entrySet().forEach(e -> c.properties.put(e.getKey(), e.getValue()));
		return c;
	}

	@Override
	public void serialize(StringBuilder sb) {}
	
	@Override
	public void deserialize(String info) {}
	
	@Override
	public boolean equals(Component comp) {
		return properties.equals(((ToolC) comp).properties);
	}
	
	public enum Tool{
		BOLT_TURNING,
		CUTTING,
		CHISELING,
		HAMMERING,
		FLAT_SURFACE,
		PRYING,
		SAWING,
		SCREW_DRIVING,
		SEWING,
		TREE_CUTTING,
		TYING,
	}

}
