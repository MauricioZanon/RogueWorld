package com.rogueworld.entities.factories;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;

public class ItemFactory extends EntityFactory{
	
	//TODO: buscar una mejor forma de separar los tipos de items
	
	protected static Entity[] weaponsByID = null;
	protected static Entity[] armorsByID = null;
	protected static Entity[] potionsByID = null;
	protected static Entity[] toolsByID = null;
	protected static Entity[] materialsByID = null;
	
	private ItemFactory() {}
	
	protected static Entity createItem(int ID) {
		try {
			if(ID >= 7000) {
				return materialsByID[ID-7000].clone();
			}else if(ID >= 6000) {
				return toolsByID[ID-6000].clone();
			}else if(ID >= 5000) {
				return potionsByID[ID-5000].clone();
			}else if(ID >= 4000) {
				return weaponsByID[ID-4000].clone();
			}else if(ID >= 3000) {
				return armorsByID[ID-3000].clone();
			}
		} catch (IndexOutOfBoundsException e) {
			System.err.println("ItemFactory: Se dio una ID inválida " + ID);
		}
		return null;
	}
	
	protected static Entity get(int ID) {
		try {
			if(ID >= 7000) {
				return materialsByID[ID-7000];
			}else if(ID >= 6000) {
				return toolsByID[ID-6000];
			}else if(ID >= 5000) {
				return potionsByID[ID-5000];
			}else if(ID >= 4000) {
				return weaponsByID[ID-4000];
			}else if(ID >= 3000) {
				return armorsByID[ID-3000];
			}
		} catch (IndexOutOfBoundsException e) {
			System.err.println("ItemFactory: Se dio una ID inválida " + ID);
		}
		return null;
	}
	
	protected static Entity createRandomItem(Type type) {
		if(type.is(Type.MATERIAL)) {
			return rng.getRandom(materialsByID).clone();
		}
		else if(type.is(Type.POTION)) {
			return rng.getRandom(potionsByID).clone();
		}
		else if(type.is(Type.TOOL)) {
			return rng.getRandom(toolsByID).clone();
		}
		else if(type.is(Type.ARMOR)) {
			return rng.getRandom(armorsByID).clone();
		}
		else {
			return rng.getRandom(weaponsByID).clone();
		}
	}
	
	
	//TODO método de prueba, borrar cuando ya no haga falta
	protected static ArrayDeque<Entity> getTwoOfEach(){
		ArrayDeque<Entity> list = new ArrayDeque<>();
//		weapons.values().forEach(i -> {
//			list.add(i.clone());
//			list.add(i.clone());
//		});
//		armors.values().forEach(i -> {
//			list.add(i.clone());
//			list.add(i.clone());
//		});
//		potions.values().forEach(i -> {
//			list.add(i.clone());
//			list.add(i.clone());
//		});
//		tools.values().forEach(i -> {
//			list.add(i.clone());
//			list.add(i.clone());
//		});
//		materials.values().forEach(i -> {
//			list.add(i.clone());
//			list.add(i.clone());
//		});
		return list;
	}
	
	/**
	 * Devuelve un Set de Entidades según el String que se de como parámetro
	 * @param items Debe ser un String con esta forma:
	 * 			3.6-7001-45
	 * 			Este String simboliza un item con ID 7001, puede haber [3-6] copias y tiene 45% de posibilidades de crearse
	 * 			Cada item debe estar separado por un espacio en blanco
	 * @return
	 */
	public static Set<Entity> getItems(String itemsString){
		Set<Entity> result = new HashSet<>();
		String[] items = itemsString.split(" ");
		for(int i = 0; i < items.length; i++) {
			String[] dropInfo = items[i].split("-");
			if(rng.nextInt(100) <= Integer.parseInt(dropInfo[2])) {
				int quantity = readQuantity(dropInfo[0]);
				int itemID = Integer.parseInt(dropInfo[1]);
				for(int j = 0; j < quantity; j++) {
					result.add(createItem(itemID));
				}
			}
		}
		return result;
	}
	
	private static int readQuantity(String string) {
		String[] quantitiesString = string.split("\\.");
		if(quantitiesString.length == 1) {
			return Integer.parseInt(quantitiesString[0]);
		}else {
			return rng.nextInt(Integer.parseInt(quantitiesString[0]), Integer.parseInt(quantitiesString[1])+1);
		}
	}
	
}
