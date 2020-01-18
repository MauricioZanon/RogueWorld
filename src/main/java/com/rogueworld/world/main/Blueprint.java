package com.rogueworld.world.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.rogueworld.utils.rng.RNG;
import com.rogueworld.world.world.Direction;

public class Blueprint {
	
	private char[][] array;
	private HashMap<Direction, List<Integer[]>> anchors;
	
	private static RNG rng = RNG.getInstance();

	public Blueprint(char[][] cs){
		array = cs;
		resetAnchors();
	}

	public char[][] getArray() {
		return array;
	}
	
	private void resetAnchors() {
		anchors = new HashMap<>();
		anchors.put(Direction.S, new ArrayList<>());
		anchors.put(Direction.W, new ArrayList<>());
		anchors.put(Direction.N, new ArrayList<>());
		anchors.put(Direction.E, new ArrayList<>());
	}
	
	private void refreshAnchors() {
		resetAnchors();
		for(int i = 0; i < array.length; i++) {
			for(int j = 0; j < array[0].length; j++) {
				if(array[i][j] == 'u') {
					if(j == 0 || array[i][j-1] == ' ') {
						Integer[] coords = {i, j};
						anchors.get(Direction.S).add(coords); //S
					}
					else if(j == array[0].length-1 || array[i][j+1] == ' ') {
						Integer[] coords = {i, j};
						anchors.get(Direction.N).add(coords); //N
					}
					else if(i == 0 || array[i-1][j] == ' ') {
						Integer[] coords = {i, j};
						anchors.get(Direction.E).add(coords); //E
					}
					else if(i == array.length-1 || array[i+1][j] == ' ') {
						Integer[] coords = {i, j};
						anchors.get(Direction.W).add(coords); //W
					}
				}
			}
		}
	}
	
	public List<Integer[]> getAnchors(Direction dir){
		return anchors.get(dir);
	}
	
	public int[] getStairsAnchor() {
		for(int i = 0; i < array.length; i++) {
			for(int j = 0; j < array[0].length; j++) {
				if(array[i][j] == '<') {
					int[] coords = {i, j};
					return coords;
				}
			}
		}
		return new int[2];
	}
	
	public void rotate(Direction dir) {
		while(getAnchors(dir).size() == 0 || rng.nextBoolean()) {
			rotate();
		}
	}
	
	public void rotate(int rotations) {
		for(int i = 0; i < rotations; i++) {
			rotate();
		}
	}
	
	private void rotate() {
		int ii = 0;
		int jj = 0;
		char[][] rotatedArray = new char[array[0].length][array.length];
		for(int i = 0; i < array[0].length; i++){
			for(int j = array.length-1; j >= 0; j--){
				rotatedArray[ii][jj] = array[j][i];
				jj++;
			}
			jj = 0;
			ii++;
		}
		array = rotatedArray;
		refreshAnchors();
	}
	
	@Override
	public String toString(){
		String string = "";
		for(int i = 0; i < array[0].length;i++){
			for(int j = 0; j < array.length; j++){
				string += array[j][i];
			}
			string += "\n";
		}
		return string;
	}
}
