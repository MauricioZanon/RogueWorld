package com.rogueworld.entities.factories;

import com.rogueworld.entities.main.Entity;

public class NPCFactory extends EntityFactory{
	
//	protected static HashMap<String, Entity> NPCs = new HashMap<>();
	protected static Entity[] NPCsByID = null;
	
	private NPCFactory() {}
	
	protected static Entity createRandomNPC(){
		return createNPC(rng.nextInt(NPCsByID.length-1));
	}
//	
//	public static Entity createNPC(String name) {
//		if(NPCs.keySet().contains(name)) {
//			Entity npc = NPCs.get(name).clone();
//			addBasicComponents(npc);
//			return npc;
//		}
//		else {
//			return null;
//		}
//	}
	
	protected static Entity createNPC(int ID) {
		if(ID >=NPCsByID.length || ID < 0) {
			System.out.println("ID de NPC incorrecta, se pidió " + ID);
			return null;
		}else {
			return NPCsByID[ID].clone();
		}
	}
	
	protected static Entity get(int ID) {
		if(ID >=NPCsByID.length || ID < 0) {
			System.out.println("ID de NPC incorrecta, se pidió " + ID);
			return null;
		}else {
			 return NPCsByID[ID];
		}
	}
	
	
}
