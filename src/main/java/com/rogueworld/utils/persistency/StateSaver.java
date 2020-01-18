package com.rogueworld.utils.persistency;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.rogueworld.application.Main;
import com.rogueworld.entities.components.HealthC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.main.Type;
import com.rogueworld.world.chunk.Chunk;
import com.rogueworld.world.map.Map;
import com.rogueworld.world.tile.Tile;
import com.rogueworld.world.time.Clock;
import com.rogueworld.world.world.WorldBuilder;

/**
 * TODO: guardar el heightMap del World y los lugares de las locations
 */
public class StateSaver implements Runnable{
	
	private static StateSaver instance;
	private BlockingQueue<String> chunksToSave = new ArrayBlockingQueue<>(128);
	
	public volatile Thread savingThread = new Thread(this);
	
	private StateSaver() {
		File saveFile = new File("../resources/Saves/" + WorldBuilder.getName() + ".db");
		if(!saveFile.exists()) { 
			createNewSaveFile();
		}
	}
	
	private Connection connect() {
		try {
			return DriverManager.getConnection("jdbc:sqlite:resources/Saves/" + WorldBuilder.getName() + ".db");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return null;
		} 
    }
	
	private void close(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {}
	}
	
	private PreparedStatement createChunkSaveStatement(Connection con) {
		PreparedStatement statement = null;
		try {
			statement = con.prepareStatement("REPLACE INTO Chunks VALUES(?, ?);");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return statement;
	}
	
	private void closeStatement(PreparedStatement statement) {
		if(statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void createNewSaveFile() {
		try {
			File dbFile = new File("../resources/Saves/" + WorldBuilder.getName() + ".db");
			dbFile.createNewFile();
		} catch (IOException e1) {}

		Connection con = connect();
		try {
			PreparedStatement createWorldTable = con.prepareStatement("CREATE TABLE IF NOT EXISTS Chunks( " + 
																	"ChunkCoord TEXT PRIMARY KEY UNIQUE NOT NULL, " + 
																	"Entities  TEXT NOT NULL);");
			createWorldTable.execute();
			closeStatement(createWorldTable);
			PreparedStatement createPlayerTable = con.prepareStatement("CREATE TABLE IF NOT EXISTS Player( " +
																		"Position TEXT NOT NULL, " +
																		"HP TEXT);");
			createPlayerTable.execute();
			closeStatement(createPlayerTable);
			PreparedStatement createWorldInfoTable = con.prepareStatement("CREATE TABLE IF NOT EXISTS WorldInfo( " +
																		"Name TEXT NOT NULL," +
																		"Date TEXT NOT NULL DEFAULT [9:0:0]);");
			createWorldInfoTable.execute();
			closeStatement(createWorldInfoTable);
			
			close(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	Connection con = connect();
	PreparedStatement statement = null;
	
	public void save(String chunkPos, String entities) {
		try {
			if(con == null || con.isClosed()) {
				con = connect();
			}
			if(statement == null || statement.isClosed()) {
				statement = createChunkSaveStatement(con);
			}
		} catch (SQLException e1) {
		}
		boolean failed = false;
		do {
			failed = false;
			try {
				statement.setString(1, chunkPos);
				statement.setString(2, entities);
				statement.addBatch();
				statement.executeBatch();
			} catch (SQLException e) {
				failed = true;
			}
		}while(failed);
	}
	
	public void save(Chunk chunk) {
		save(chunk.getPosAsString(), Serializer.serialize(chunk));
	}
	
	public void saveGameState() {
		savePlayerState();
		saveWorldState();
	}
	
	public void savePlayerState() {
		Entity player = Main.player;
		StringBuilder sb = new StringBuilder();
		
		sb.append(player.get(PositionC.class).toString());
		String playerPos = sb.toString();
		String playerHP = String.valueOf(player.get(HealthC.class).getCurHP());
		
		try {
			Connection con = connect();
			PreparedStatement resetStatement = con.prepareStatement("DELETE FROM Player");
			resetStatement.executeUpdate();
			resetStatement.close();
			
        	PreparedStatement statement = con.prepareStatement("REPLACE INTO Player VALUES(?, ?)");
        	statement.setString(1, playerPos);
        	statement.setString(2, playerHP);
        	statement.executeUpdate();
        	
        	statement.close();
        	close(con);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
   	    }
	}
	
	public void saveWorldState() {
		
		try {
			PreparedStatement resetStatement = con.prepareStatement("DELETE FROM WorldInfo");
			resetStatement.executeUpdate();
			resetStatement.close();
			
			PreparedStatement pstm = con.prepareStatement("REPLACE INTO WorldInfo VALUES (?, ?);");
			pstm.setString(1, WorldBuilder.getName());
			pstm.setString(2, Clock.getDate());
			pstm.executeUpdate();
			pstm.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		FIXME buscar otra forma de evitar que el player se guarde con todo lo demas sin tener que sacarlo y volverlo a 
//				colocar en el tile
		Tile playerTile = Main.player.get(PositionC.class).getTile();
		playerTile.remove(Type.ACTOR);
		for(Chunk chunk : Map.getChunksInMemory().values()) {
			addChunkToSaveList(chunk);
		}
		playerTile.put(Main.player);
		System.out.println("chunks #: " + Map.getChunksInMemory().size());
	}
	
	public void addChunkToSaveList(Chunk chunk) {
		String entities = Serializer.serialize(chunk);
		if(entities.length() > Chunk.SIZE*Chunk.SIZE) {
			chunksToSave.add(String.join(" ", chunk.getPosAsString(), entities));
		}
		if(!Map.getChunksInMemory().containsValue(chunk)){
			chunk.dump();
		}
	}

	@Override
	public void run() {
		while(savingThread.isAlive()) {
			
			Iterator<String> iter = chunksToSave.iterator();
			while(iter.hasNext()) {
				String[] chunkInfo = iter.next().split(" ");
				save(chunkInfo[0], chunkInfo[1]);
				System.out.println("saved " + chunkInfo[0]);
				iter.remove();
			}

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
		}
		
	}
	
	public static StateSaver getInstance() {
		if(instance == null){
			instance = new StateSaver();
		}
		return instance;
	}
	
}
