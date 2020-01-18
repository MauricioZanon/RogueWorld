package com.rogueworld.utils.persistency;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import com.rogueworld.application.Main;
import com.rogueworld.entities.components.HealthC;
import com.rogueworld.entities.components.PositionC;
import com.rogueworld.entities.main.Entity;
import com.rogueworld.entities.player.PlayerInfo;
import com.rogueworld.world.chunk.Chunk;
import com.rogueworld.world.time.Clock;
import com.rogueworld.world.world.WorldBuilder;

public class StateLoader {
	
	private static StateLoader instance = null;
	
	private StateLoader() {}
	
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
	
	public Chunk loadChunk(String chunkPos) {
		Connection con = connect();
		try {
			PreparedStatement statement = con.prepareStatement("SELECT Entities FROM Chunks WHERE ChunkCoord = '" + chunkPos + "'");
			ResultSet rs = statement.executeQuery();
			if(!rs.isClosed()) {
				String chunkString = rs.getString("Entities");
				rs.close();
				statement.close();
				close(con);
				System.out.println("loaded " + chunkPos);
				return Deserializer.deserialize(chunkPos, chunkString);
			}
		} catch (SQLException e) {}
		
		int[] coord = Arrays.stream(chunkPos.split(":")).mapToInt(Integer::parseInt).toArray();
		if(coord[2] == 0) {
			return WorldBuilder.createOverworldChunk(coord[0], coord[1]);
		}else {
			return new Chunk(chunkPos);
		}
	}
	
	public void loadPlayer() {
		Entity player = Main.player;
		
		Connection con = connect();
		try {
			PreparedStatement statement = con.prepareStatement("SELECT * FROM Player");
			ResultSet rs = statement.executeQuery();
			
			String position = rs.getString("Position");
			PositionC pos = new PositionC();
			pos.coord = Arrays.stream(position.split(":")).mapToInt(Integer::parseInt).toArray();
			player.addComponent(pos);
			
			float curHP = Float.parseFloat(rs.getString("HP"));
			player.get(HealthC.class).setCurHP(curHP);
			PlayerInfo.CUR_HP.set(curHP);
			
			statement.close();
			rs.close();
			close(con);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Main.player = player;
	}
	
	public void loadWorldState() {
		Connection con = connect();
		try {
			PreparedStatement statement = con.prepareStatement("SELECT Date FROM WorldInfo");
			ResultSet rs = statement.executeQuery();
			Clock.setDate(rs.getString("Date"));
			
			rs.close();
			statement.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static StateLoader getInstance() {
		if(instance == null) {
			instance = new StateLoader();
		}
		return instance;
	}
	
}
