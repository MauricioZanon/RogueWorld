package com.rogueworld.world.main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.rogueworld.utils.rng.RNG;
import com.rogueworld.world.xpreader.REXLoader;
import com.rogueworld.world.world.Direction;

public abstract class RoomFactory {
	
	private static HashMap<String, Set<Blueprint>> roomLists = new HashMap<>();
	
	private static final String ROOMS_PATH = "../RogueWorld/resources/Rooms and buildings";
	private static RNG rng = RNG.getInstance();

	static{
		try {
			File[] roomDirectories = new File(ROOMS_PATH).listFiles();
			for(File dir : roomDirectories) {
				loadRooms(dir);
			}
		} catch (IOException e) {e.printStackTrace();}
	}

	private static void loadRooms(File roomsPath) throws IOException {
		File[] roomFiles = roomsPath.listFiles();
		String directoryName = roomsPath.getName();
		roomLists.put(directoryName, new HashSet<Blueprint>());
		for(File f : roomFiles) {
			roomLists.get(directoryName).add(new Blueprint(REXLoader.laod(f.getAbsolutePath())));
		}
	}
	
	/**
	 * 
	 * @param type
	 * @param dir La dirección en la cual se debe construir la habitación, por ejemplo, si se quiere construir una habitación
	 * 			a partir de un punto de anclaje que esté en una pared oeste de una habitación ya construida entonces
	 * 			la habitación devuelta deberá tener un punto de anclaje en una pared este
	 * 
	 *          ##########
	 *          #........#
	 *          #........u <- anclaje en pared este
	 *          #........#
	 *          #...######
	 *          #...#     
	 *          #...#     
	 *          #...#     
	 *          #...#     
	 *          ##u##   
	 * @return una habitación del tipo pedido con un punto de anclaje en la @Direction dir opuesta
	 */
	public static Blueprint createRoom(String type, Direction dir) {
		if(!roomLists.keySet().contains(type)) return null;
		Blueprint room = rng.getRandom(roomLists.get(type));
		room.rotate(dir);
		return room;
	}
	
	//TODO: hacer que roten las primeras habitaciones de los dungeons
	public static Blueprint createRoom(String type) {
		if(!roomLists.keySet().contains(type)) return null;
		Blueprint room = rng.getRandom(roomLists.get(type));
		return room;
	}
}
