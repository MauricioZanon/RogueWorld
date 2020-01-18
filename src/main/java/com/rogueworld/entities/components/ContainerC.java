package com.rogueworld.entities.components;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;

import com.rogueworld.entities.factories.EntityFactory;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;

/**
 * Guarda items, sirve para crear entidades que contengan items, tanto features como actores
 */
public class ContainerC extends Component{
	
	/** <Nombre del item, lista> */
	public Map<String, ArrayDeque<Entity>> items = new HashMap<>();
	
	public ContainerC() {
		isShared = false;
	}

	public void add(Entity newItem) {
		if(newItem == null) return;
		
		String itemName = newItem.name;
		if(!items.keySet().contains(itemName)) {
			items.put(itemName, new ArrayDeque<>());
		}
		items.get(itemName).add(newItem);
	}
	
	public void addAll(Collection<Entity> items) {
		items.forEach(i -> add(i));
	}
	
	/** Devuelve todos los items del Tipo type */
	public ArrayDeque<Entity> get(Type type){
		ArrayDeque<Entity> returnedList = new ArrayDeque<>();
		for(ArrayDeque<Entity> itemList : items.values()) {
			if(itemList.getFirst().type.is(type)) {
				returnedList.addAll(itemList);
			}
		}
		return returnedList;
	}
	
	/** Devuelve un item con el nombre itemName */
	public Entity get(String itemName) {
		if(items.keySet().contains(itemName.toLowerCase())) {
			return items.get(itemName.toLowerCase()).getFirst();
		}
		else {
			return null;
		}
	}
	
	/** Devuelve una lista con todos los items */
	public List<Entity> getAll(){
		List<Entity> returnedList = new ArrayList<>();
		for(ArrayDeque<Entity> itemList : items.values()) {
			returnedList.addAll(itemList);
		}
		return returnedList;
	}
	
	/** Remueve y devuelve la cantidad indicada del item pedido */
	public ArrayDeque<Entity> remove(String itemName, int quantity) {
		ArrayDeque<Entity> returnedList = new ArrayDeque<>();
		if(items.keySet().contains(itemName)) {
			ArrayDeque<Entity> itemList = items.get(itemName);
			while(!itemList.isEmpty() && returnedList.size() < quantity) {
				returnedList.add(items.get(itemName).removeFirst());
			}
		}
		if(items.get(itemName).isEmpty()) {
			items.remove(itemName);
		}
		return returnedList;
	}
	
	/** Remueve y devuelve la cantidad indicada de los items que cumplan la condicion */
	public ArrayDeque<Entity> remove(Predicate<Entity> cond, int quantity) {
		ArrayDeque<Entity> result = new ArrayDeque<>();
		for(Entry<String, ArrayDeque<Entity>> entry : items.entrySet()) {
			if(cond.test(entry.getValue().getFirst())) {
				while(result.size() < quantity && !entry.getValue().isEmpty()) {
					result.add(entry.getValue().removeFirst());
				}
				if(entry.getValue().isEmpty()) {
					items.remove(entry.getKey());
				}
				if(result.size() >= quantity) {
					return result;
				}
			}
		}
		return result;
	}

	/**
	 * Quita todos los items con este nombre del container
	 * @return los items removidos
	 */
	public ArrayDeque<Entity> remove(String itemName) {
		if(items.keySet().contains(itemName)) {
			ArrayDeque<Entity> result = items.remove(itemName);
			return result;
		}
		else {
			return null;
		}
	}
	
	public ArrayDeque<Entity> removeAll(){
		ArrayDeque<Entity> result = new ArrayDeque<>();
		for(ArrayDeque<Entity> itemList : items.values()) {
			result.addAll(itemList);
		}
		items.clear();
		return result;
	}

	public ArrayDeque<Entity> removeAll(Predicate<Entity> cond) {
		ArrayDeque<Entity> result = new ArrayDeque<>();
		for(Entry<String, ArrayDeque<Entity>> entry : items.entrySet()) {
			if(cond.test(entry.getValue().getFirst())) {
				result.addAll(entry.getValue());
				items.remove(entry.getKey());
			}
		}
		return result;
	}
	
//	/** Devuelve uno de cada item en el container */
//	public List<Entity> getOcurrences(Type type){
//		List<Entity> result = new ArrayList<>();
//		items.values().forEach(list -> result.add(list.getFirst()));
//		result.removeIf(i -> !i.type.is(type));
//		
//		return result;
//	}
	
	public int getQuantity(String itemName) {
		return items.containsKey(itemName) ? items.get(itemName).size() : 0;
	}
	
	public boolean isEmpty() {
		return items.isEmpty();
	}
	
	public boolean contains(String itemName) {
		return items.keySet().contains(itemName);
	}
	
	public boolean contains(String itemName, int quantity) {
		return items.keySet().contains(itemName) && getQuantity(itemName) >= quantity;
	}

	@Override
	public ContainerC clone() {
		ContainerC comp = new ContainerC();
		for(Entity item : getAll()) {
			comp.add(item.clone());
		}
		return comp;
	}


	@Override
	public void serialize(StringBuilder sb) {
		sb.append("CON:");
		for(Entity item : getAll()) {
			sb.append(item.ID + "&");
		}
	}
	
	@Override
	public void deserialize(String info) {
		String[] itemIDs = info.split("&");
		for(int i = 0; i < itemIDs.length; i++) {
			add(EntityFactory.create(Integer.parseInt(itemIDs[i])));
		}
	}

	@Override
	public boolean equals(Component comp) {
		ContainerC c = (ContainerC) comp;
		return c.getAll().equals(getAll());
	}


}
