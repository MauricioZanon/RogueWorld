package com.rogueworld.entities.factories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

import com.rogueworld.ai.states.IdleState;
import com.rogueworld.ai.states.MeleeCombatState;
import com.rogueworld.ai.states.RangedCombatState;
import com.rogueworld.ai.states.StateType;
import com.rogueworld.ai.states.WanderingState;
import com.rogueworld.entities.components.AIC;
import com.rogueworld.entities.components.BackColorC;
import com.rogueworld.entities.components.BodyC;
import com.rogueworld.entities.components.BodyC.BodyPart;
import com.rogueworld.entities.components.BreakC;
import com.rogueworld.entities.components.ButcherC;
import com.rogueworld.entities.components.ContainerC;
import com.rogueworld.entities.components.CoverageC;
import com.rogueworld.entities.components.FieldDressC;
import com.rogueworld.entities.components.GraphicC;
import com.rogueworld.entities.components.HealthC;
import com.rogueworld.entities.components.LightSourceC;
import com.rogueworld.entities.components.MaterialC;
import com.rogueworld.entities.components.MaterialC.Material;
import com.rogueworld.entities.components.MovementC;
import com.rogueworld.entities.components.MovementC.MovementType;
import com.rogueworld.entities.components.OccupiesC;
import com.rogueworld.entities.components.SkillsC;
import com.rogueworld.entities.components.SkillsC.Skill;
import com.rogueworld.entities.components.SkinC;
import com.rogueworld.entities.components.StatusEffectsC;
import com.rogueworld.entities.components.ToolC;
import com.rogueworld.entities.components.ToolC.Tool;
import com.rogueworld.entities.components.TransitableC;
import com.rogueworld.entities.components.UsesC;
import com.rogueworld.entities.components.UsesC.UseType;
import com.rogueworld.entities.components.VisionC;
import com.rogueworld.entities.main.Att;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Flag;
import com.rogueworld.entities.main.Type;
import com.rogueworld.utils.rng.RNG;

import org.sqlite.SQLiteConfig;

import javafx.scene.paint.Color;

public abstract class EntityFactory{
	
	private static HashMap<String, Integer> entitiesByName = new HashMap<>();
	protected static RNG rng = RNG.getInstance();
	
	public static Entity create(String name) {
		return create(entitiesByName.get(name));
	}
	
	/** Devuelve la entidad prototipo usada para crear a las demás entidades del mismo nombre */
	public static Entity getBase(int ID) {
		if(ID < 1000) {
			return NPCFactory.get(ID);
		}else if(ID < 2000) {
			return TerrainFactory.get(ID);
		}else if (ID < 3000) {
			return FeatureFactory.get(ID);
		}else {
			return ItemFactory.get(ID);
		}
	}
	
	public static Entity create(int ID) {
		if(ID < 1000) {
			return NPCFactory.createNPC(ID);
		}else if(ID < 2000) {
			return TerrainFactory.get(ID);
		}else if (ID < 3000) {
			return FeatureFactory.createFeature(ID);
		}else {
			return ItemFactory.createItem(ID);
		}
	}
	
	public static Entity createRandom(Type type) {
		switch(type.getSuperType()) {
		case ACTOR:
			return NPCFactory.createRandomNPC();
		case ITEM:
			return ItemFactory.createRandomItem(type);
		default:
			System.out.println("Se pidio crear una entidad random de tipo invalido");
			return null;
		}
	}
	
	public static void loadEntities(){
		Connection con = connect();
		ResultSet entitiesRS = executeQuery("SELECT * FROM BasicData ORDER BY ID DESC;", con);
		try {
			while(entitiesRS.next()) {
				int ID = entitiesRS.getInt("ID");
				Entity entity = createBaseEntity(entitiesRS);
				
				ResultSet RS = executeQuery("SELECT * FROM GraphicComponents WHERE ID='" + ID + "'", con);
				if(!RS.isClosed()) {
					addGraphicComponent(entity, RS);
					RS.close();
				}
				
				RS = executeQuery("SELECT * FROM HealthComponents WHERE ID='" + ID + "'", con);
				if(!RS.isClosed()) {
					addHealthComponent(entity, RS);
					RS.close();
				}
				
				RS = executeQuery("SELECT * FROM MovementComponents WHERE ID='" + ID + "'", con);
				if(!RS.isClosed()) {
					addMovementComponent(entity, RS);
					RS.close();
				}
				
				RS = executeQuery("SELECT * FROM TransitableComponents WHERE ID='" + ID + "'", con);
				if(!RS.isClosed()) {
					addTransitableComponent(entity, RS);
					RS.close();
				}
				
				RS = executeQuery("SELECT * FROM BreakComponents WHERE ID='" + ID + "'", con);
				if(!RS.isClosed()) {
					addBreakComponent(entity, RS);
					RS.close();
				}

				RS = executeQuery("SELECT * FROM AIComponents WHERE ID='" + ID + "'", con);
				if(!RS.isClosed()) {
					addAIComponent(entity, RS);
					RS.close();
				}

				RS = executeQuery("SELECT * FROM ContainerComponents WHERE ID='" + ID + "'", con);
				if(!RS.isClosed()) {
					addContainerComponent(entity, RS);
					RS.close();
				}
				
				RS = executeQuery("SELECT * FROM CoverageComponents WHERE ID='" + ID + "'", con);
				if(!RS.isClosed()) {
					addCoverageComponent(entity, RS);
					RS.close();
				}
				
				RS = executeQuery("SELECT * FROM BodyComponents WHERE ID='" + ID + "'", con);
				if(!RS.isClosed()) {
					addBodyComponent(entity, RS);
					RS.close();
				}
				
				RS = executeQuery("SELECT * FROM FieldDressComponents WHERE ID='" + ID + "'", con);
				if(!RS.isClosed()) {
					addFieldDressComponent(entity, RS);
					RS.close();
				}
				
				RS = executeQuery("SELECT * FROM SkinComponents WHERE ID='" + ID + "'", con);
				if(!RS.isClosed()) {
					addSkinComponent(entity, RS);
					RS.close();
				}
				
				RS = executeQuery("SELECT * FROM ButcherComponents WHERE ID='" + ID + "'", con);
				if(!RS.isClosed()) {
					addButcherComponent(entity, RS);
					RS.close();
				}
				
				RS = executeQuery("SELECT * FROM ToolComponents WHERE ID='" + ID + "'", con);
				if(!RS.isClosed()) {
					addToolComponent(entity, RS);
					RS.close();
				}
				
				RS = executeQuery("SELECT * FROM UsesComponents WHERE ID='" + ID + "'", con);
				if(!RS.isClosed()) {
					addUsesComponent(entity, RS);
					RS.close();
				}
				
				
				int index = ID;
				String name = entity.name;
				entitiesByName.put(name, ID);
				switch(entity.type.getSuperType()) {
				case ACTOR:
					entity.addComponent(new VisionC());
					entity.addComponent(new StatusEffectsC());
					entity.addComponent(new SkillsC());
					if(NPCFactory.NPCsByID == null) {
						NPCFactory.NPCsByID = new Entity[index+1];
					}
					NPCFactory.NPCsByID[index] = entity;
					break;
				case FEATURE:
					index -= 2000;
					if(FeatureFactory.featuresByID == null) {
						FeatureFactory.featuresByID = new Entity[index+1];
					}
					FeatureFactory.featuresByID[index] = entity;
					break;
				case ITEM:
					Type type = entity.type;
					if(type.is(Type.WEAPON) || type.is(Type.MUNITION)) {
						index -= 4000;
						if(ItemFactory.weaponsByID == null) {
							ItemFactory.weaponsByID = new Entity[index+1];
						}
						ItemFactory.weaponsByID[index] = entity;
					}else if(type.is(Type.ARMOR)) {
						index -= 3000;
						if(ItemFactory.armorsByID == null) {
							ItemFactory.armorsByID = new Entity[index+1];
						}
						ItemFactory.armorsByID[index] = entity;
					}else if (type.is(Type.POTION)) {
						index -= 5000;
						if(ItemFactory.potionsByID == null) {
							ItemFactory.potionsByID = new Entity[index+1];
						}
						ItemFactory.potionsByID[index] = entity;
					}else if(type.is(Type.TOOL)) {
						index -= 6000;
						if(ItemFactory.toolsByID == null) {
							ItemFactory.toolsByID = new Entity[index+1];
						}
						ItemFactory.toolsByID[index] = entity;
					}else if(type.is(Type.MATERIAL)) {
						index -= 7000;
						if(ItemFactory.materialsByID == null) {
							ItemFactory.materialsByID = new Entity[index+1];
						}
						ItemFactory.materialsByID[index] = entity;
					}
					break;
				case TERRAIN:
					index -= 1000;
					if(TerrainFactory.terrainsByID == null) {
						TerrainFactory.terrainsByID = new Entity[index+1];
					}
					TerrainFactory.terrainsByID[index] = entity;
					break;
				default:
					System.out.println("tipo de entidad no identificado " + entity.type);
					break;
					
				}
			}
			close(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static ResultSet executeQuery(String query, Connection con) {
		try {
			return con.prepareStatement(query).executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static Connection connect() {
		Connection conn = null;
		try {
			SQLiteConfig config = new SQLiteConfig();  
			config.enforceForeignKeys(true);
			conn = DriverManager.getConnection("jdbc:sqlite:resources/Data/Entities.db", config.toProperties());
		} catch (SQLException e) {
			System.out.println("load connection failed " + e.getMessage());
		} 
		return conn;
    }
	
	private static void close(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {}
	}
	
	private static Entity createBaseEntity(ResultSet rs) throws SQLException {
		int ID = rs.getInt("ID");
		String name = rs.getString("Name");
		Type type = Type.valueOf(rs.getString("Type"));
		Entity e = new Entity(type, ID, name);
		
		e.description = rs.getString("Description");
		
		String flagsString = rs.getString("Flags");
		if(flagsString != null && !flagsString.equals("")) {
			String[] flagsArray = flagsString.split(" ");
			for(int i = 0; i < flagsArray.length; i++) {
				e.addFlag(Flag.valueOf(flagsArray[i]));
			}
		}
		
		if(e.is(Flag.LIGHT_SOURCE)) {
			e.addComponent(new LightSourceC());
		}
		
		String attributes = rs.getString("Attributes");
		if(attributes != null) {
			String [] attArray = attributes.split(" ");
			for(int i = 0; i < attArray.length; i++) {
				String[] att = attArray[i].split(":");
				e.setAttribute(Att.valueOf(att[0]), Float.parseFloat(att[1]));
			}
		}
		
		String material = rs.getString("Allowed materials");
		if(material != null && !material.equals("")) {
			MaterialC c = new MaterialC();
			String[] materials = material.split(" ");
			for(int i = 0; i < materials.length; i++) {
				c.allowedMaterials.add(Material.valueOf(materials[i]));
			}
		}
		
		return e;
	}
	
	private static void addGraphicComponent(Entity e, ResultSet RS) throws SQLException {
		String ASCII = RS.getString("ASCII");
		String frontColor = RS.getString("frontColor");
		
		GraphicC comp = new GraphicC();
		comp.ASCII = ASCII;
		if(frontColor != null) {
			double[] frontArray = Arrays.stream(frontColor.split(" ")).mapToDouble(Double::parseDouble).toArray();
			comp.color = new Color(frontArray[0], frontArray[1], frontArray[02], frontArray[3]);
		}
		e.addComponent(comp);

		String backColor = RS.getString("backColor");
		if(backColor != null) {
			double[] backArray = Arrays.stream(backColor.split(" ")).mapToDouble(Double::parseDouble).toArray();
			Color baseColor = new Color(backArray[0], backArray[1], backArray[02], backArray[3]);
			BackColorC c = new BackColorC();
			for(int i = 0; i < c.colors.length; i++) {
				c.colors[i] = rng.getAproximateColor(baseColor);
			}
			e.addComponent(c);
			
		}
	}
	
	private static void addHealthComponent(Entity e, ResultSet RS) throws SQLException {
		HealthC comp = new HealthC();
		comp.setMaxHP(RS.getFloat("maxHP"));
		comp.setCurHP(RS.getFloat("maxHP"));
		comp.setHPreg(RS.getFloat("HPreg"));
		e.addComponent(comp);
	}
	
	private static void addMovementComponent(Entity e, ResultSet RS) throws SQLException {
		MovementC comp = new MovementC();
		comp.movementType = MovementType.valueOf(RS.getString("movementType"));
		e.addComponent(comp);
	}
	
	private static void addTransitableComponent(Entity e, ResultSet RS) throws SQLException {
		TransitableC comp = new TransitableC();
		String acceptedMovString = RS.getString("acceptedMovement");
		if(!acceptedMovString.equals("")) {
			String[] movTypes = acceptedMovString.split(" ");
			for(int i = 0; i < movTypes.length; i++) {
				String[] mov = movTypes[i].split("-");
				comp.add(MovementType.valueOf(mov[0]), Float.parseFloat(mov[1]));
			}
		}
		e.addComponent(comp);
	}
	
	private static void addBreakComponent(Entity e, ResultSet RS) throws SQLException {
		String items = RS.getString("Items");
		if(items != null) {
			BreakC c = new BreakC();
			c.items = items;
			e.addComponent(c);
		}
	}
	
	private static void addAIComponent(Entity e, ResultSet RS) throws SQLException {
		AIC AI = new AIC();
		e.addComponent(AI);
		
		String idleState = RS.getString("Idle AI");
		if(idleState != null && !idleState.equals("")) {
			switch(idleState) {
			case "Idle":
				AI.addState(StateType.IDLE, new IdleState(e));
				break;
			case "Wandering":
				AI.addState(StateType.IDLE, new WanderingState(e));
				break;
			}
		}
		
		String combatState = RS.getString("Combat AI");
		if(combatState != null && !combatState.equals("")) {
			switch(combatState) {
			case "Melee":
				AI.addState(StateType.COMBAT, new MeleeCombatState(e));
				break;
			case "Ranged":
				AI.addState(StateType.COMBAT, new RangedCombatState(e));
				break;
			}
		}
		
		AI.setState(StateType.IDLE);
	}
	
	private static void addContainerComponent(Entity e, ResultSet RS) throws SQLException {
		String itemsString = RS.getString("Items");
		if(itemsString != null && !itemsString.equals("")) {
			ContainerC c = new ContainerC();
			c.addAll(ItemFactory.getItems(itemsString));
			e.addComponent(c);
		}

	}
	
	private static void addCoverageComponent(Entity e, ResultSet RS) throws SQLException {
		String coversString = RS.getString("Covers");
		if(coversString != null && !coversString.equals("")) {
			CoverageC cov = new CoverageC();
			String[] covers = coversString.split(" ");
			for(int i = 0; i < covers.length; i++) {
				cov.covers.add(BodyPart.valueOf(covers[i]));
			}
			e.addComponent(cov);
		}
		
		String occupiesString = RS.getString("Occupies");
		if(occupiesString != null && !occupiesString.equals("")) {
			OccupiesC occ = new OccupiesC();
			String[] occupies = occupiesString.split(" ");
			for(int i = 0; i < occupies.length; i++) {
				occ.occupies.add(BodyPart.valueOf(occupies[i]));
			}
			e.addComponent(occ);
		}
	}
	
	private static void addBodyComponent(Entity e, ResultSet RS) throws SQLException {
		BodyC c = new BodyC();
		
		String[] partsString = RS.getString("BodyParts").split(" ");
		for(int i = 0; i < partsString.length; i++) {
			c.add(BodyPart.valueOf(partsString[i]));
		}
		
		String equipmentInfo = RS.getString("Equipment");
		if(equipmentInfo != null && !equipmentInfo.equals("")) {
			String equipmentStrings[] = RS.getString("Equipment").split(" ");
			for(int i = 0; i < equipmentStrings.length; i++) {
				c.equip(create(Integer.parseInt(equipmentStrings[i])));
			}
		}
		
		e.addComponent(c);
	}
	
	
	private static void addFieldDressComponent(Entity e, ResultSet RS) throws SQLException {
		FieldDressC c = new FieldDressC();
		c.items = RS.getString("Items");
		
		String skills[] = RS.getString("Skills").split(" ");
		for(int i = 0; i < skills.length; i++) {
			String[] skillInfo = skills[i].split("-");
			c.neededSkills.put(Skill.valueOf(skillInfo[0]), Integer.parseInt(skillInfo[1]));
		}
		
		String[] tools = RS.getString("Tools").split(" ");
		for(int i = 0; i < tools.length; i++) {
			c.neededTools.add(Tool.valueOf(tools[i]));
		}
		
		e.addComponent(c);
	}
	
	private static void addSkinComponent(Entity e, ResultSet RS) throws SQLException {
		SkinC c = new SkinC();
		c.items = RS.getString("Items");
		
		String skills[] = RS.getString("Skills").split(" ");
		for(int i = 0; i < skills.length; i++) {
			String[] skillInfo = skills[i].split("-");
			c.neededSkills.put(Skill.valueOf(skillInfo[0]), Integer.parseInt(skillInfo[1]));
		}
		
		String[] tools = RS.getString("Tools").split(" ");
		for(int i = 0; i < tools.length; i++) {
			c.neededTools.add(Tool.valueOf(tools[i]));
		}
		
		e.addComponent(c);
	}
	
	private static void addButcherComponent(Entity e, ResultSet RS) throws SQLException {
		ButcherC c = new ButcherC();
		c.items = RS.getString("Items");
		
		String skills[] = RS.getString("Skills").split(" ");
		for(int i = 0; i < skills.length; i++) {
			String[] skillInfo = skills[i].split("-");
			c.neededSkills.put(Skill.valueOf(skillInfo[0]), Integer.parseInt(skillInfo[1]));
		}
		
		String[] tools = RS.getString("Tools").split(" ");
		for(int i = 0; i < tools.length; i++) {
			c.neededTools.add(Tool.valueOf(tools[i]));
		}
		
		e.addComponent(c);
	}
	
	private static void addToolComponent(Entity e, ResultSet RS) throws SQLException {
		ToolC c = new ToolC();
		
		String props[] = RS.getString("Properties").split(" ");
		for(int i = 0; i < props.length; i++) {
			String[] propInfo = props[i].split("-");
			c.properties.put(Tool.valueOf(propInfo[0]), Integer.parseInt(propInfo[1]));
		}
		
		e.addComponent(c);
	}
	
	private static void addUsesComponent(Entity e, ResultSet RS) throws SQLException {
		UsesC c = new UsesC();
		String[] uses = RS.getString("Uses").split(" ");
		for(int i = 0; i < uses.length; i++) {
			c.uses.add(UseType.valueOf(uses[i]));
		}
		
		String useOnBump = RS.getString("UseOnBump");
		if(useOnBump != null && !useOnBump.equals("")) {
			c.useOnBump = UseType.valueOf(useOnBump);
		}
		
		String quickUse = RS.getString("QuickUse");
		if(quickUse != null && !quickUse.equals("")) {
			c.quickUse = UseType.valueOf(quickUse);
		}
		
		e.addComponent(c);
	}
	
	
	
	
	
}