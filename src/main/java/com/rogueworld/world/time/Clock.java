package com.rogueworld.world.time;

import java.util.Calendar;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;

public class Clock {
	
	/**
	 * TODO: Hacer otra clase que maneje el clima y agregarle estaciones y estados clim�ticos
	 * TODO: Test
	 */
	private static Calendar calendar = Calendar.getInstance();
	
	private static float surfaceLightLevel = 1f;
	public static final SimpleStringProperty hourProperty = new SimpleStringProperty();
	
	private Clock() {}
	
	public static void initialize() {
		calendar.clear();
		calendar.set(Calendar.HOUR_OF_DAY, 17);
		calendar.set(Calendar.MINUTE, 45);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		calendar.set(Calendar.MONTH, 0);
	}
	
	public static void setDate(String date) {
		String[] dateArray = date.split(":");
		calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[0]));
		calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[1]));
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(dateArray[2]));
		calendar.set(Calendar.MINUTE, Integer.parseInt(dateArray[3]));
		calendar.set(Calendar.SECOND, Integer.parseInt(dateArray[4]));
		
		recalculateLightLevel();
		Platform.runLater(() -> hourProperty.set(getHour()));
	}
	
	public static String getDate() {
		return String.join(":", new String[] {
			String.valueOf(calendar.get(Calendar.MONTH)),
			String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)),
			String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)),
			String.valueOf(calendar.get(Calendar.MINUTE)),
			String.valueOf(calendar.get(Calendar.SECOND))
		});
	}
	
	public static String getHour() {
		int minutes = calendar.get(Calendar.MINUTE);
		int seconds = calendar.get(Calendar.SECOND);
		return calendar.get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d:%02d", minutes, seconds);
	}

	public static void advanceTime(float seconds) {
		calendar.add(Calendar.MILLISECOND, (int) (seconds*1000));
		recalculateLightLevel();
		Platform.runLater(() -> hourProperty.set(getHour()));
	}
	
	private static void recalculateLightLevel() {
		float minutes = calendar.get(Calendar.MINUTE) + calendar.get(Calendar.HOUR_OF_DAY)*60;
		if(minutes >= 300 && minutes < 480) {
			surfaceLightLevel = (float) (0.00635199f * Math.pow(1.01059529f, minutes));
		}else if(minutes >= 1080 && minutes < 1260) {
			surfaceLightLevel = (float) (2.14833262E+37f * Math.pow(minutes, -12.30691935f));
		}else if(minutes >= 1260 || minutes < 300) {
			surfaceLightLevel = 0.1f;
		}else {
			surfaceLightLevel = 1f;
		}
	}

	public static float getSurfaceLightLevel() {
		return surfaceLightLevel;
	}
	
}