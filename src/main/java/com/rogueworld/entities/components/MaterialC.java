package com.rogueworld.entities.components;

import java.util.EnumSet;

import com.rogueworld.utils.rng.RNG;


//TODO eliminar todos los equipos que tengan el material en el nombre y hacer que su calidad dependa del material
public class MaterialC extends Component{
	
	public EnumSet<Material> allowedMaterials = EnumSet.noneOf(Material.class);
	public Material material = null;
	
	private static RNG rng = RNG.getInstance();
	
	public MaterialC() {
		isShared = false;
	}

	@Override
	public MaterialC clone() {
		MaterialC c = new MaterialC();
		c.allowedMaterials.addAll(allowedMaterials);
		c.material = rng.getRandom(allowedMaterials);
		return c;
	}

	@Override
	public void serialize(StringBuilder sb) {
		sb.append("MAT:" + material);
	}
	
	@Override
	public void deserialize(String info) {
		material = Material.valueOf(info);
	}
	
	@Override
	public boolean equals(Component comp) {
		MaterialC c = (MaterialC) comp;
		return material == c.material && allowedMaterials.equals(c.allowedMaterials);
	}
	
	public enum Material{
		SILK(0.1f, 0.1f, 1, false, false, true),
		CLOTH(0.4f, 0.4f, 1, false, false, true),
		WOOL(0.6f, 0.6f, 1, false, false, true),
		LEATHER(1, 1, 0.5f, false, false, false),
		
		TIN(3, 3, 0, true, true, false),
		GOLD(4, 4, 0, true, true, false),
		SILVER(4, 4, 0, true, true, false),
		COPPER(5, 5, 0, true, true, false),
		IRON(8, 10, 0, true, true, false),
		STEEL(8, 15, 0, true, true, false),
		OBSIDIAN(10, 30, 0, false, false, false),
		
		CHARCOAL(3, 2, 50, false, false, true),
		WOOD(5, 5, 10, false, false, true),
		GLASS(5, 1, 0, false, false, false),
		STONE(7, 7, 0, false, false, false),
		CLAY(2, 2, 0, false, false, false),
		
		PLANT(1, 0, 0, false, false, true),
		BONE(3, 5, 0, false, false, false),
		FLESH(1, 1, 0, false, false, false);
		
		/** Modificador para el peso de las entidades hechas con este material */
		public float weightMod = 1;
		public float hardness = 1;
		/** La eficiencia como combustible de las entidades de este material */
		public float fuelMod = 0;
		public boolean melts = false;
		public boolean rusts = false;
		public boolean inflammable = false;

		Material(float weightMod, float hardness, float fuelMod, boolean melts, boolean rusts, boolean inflammable) {
			this.weightMod = weightMod;
			this.hardness = hardness;
			this.fuelMod = fuelMod;
			this.melts = melts;
			this.rusts = rusts;
			this.inflammable = inflammable;
		}
		
	}

}
