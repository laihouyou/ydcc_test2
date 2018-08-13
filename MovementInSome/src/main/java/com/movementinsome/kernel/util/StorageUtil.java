package com.movementinsome.kernel.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.Environment;

public class StorageUtil {
	// private static String cmd = "mount";
	private static String cmd = "cat /proc/mounts";
	private static String format = "sdcard";
	private static String sdCard = Environment.getExternalStorageDirectory()
			.getAbsolutePath();

	/**
	 * 獲取外置SDCard的絕對路徑
	 * 
	 * @return
	 */
	public static String getExternalPath() {
		BufferedReader read = null;
		String external_SDCard = sdCard;
		Runtime runtime = Runtime.getRuntime();
		try {
			Process process = runtime.exec(cmd);
			read = new BufferedReader(new InputStreamReader(
					process.getInputStream()));
			String line;
			while ((line = read.readLine()) != null) {
				// if (line.toLowerCase().contains(format) &&
				// line.contains(".android-secure")) {
				if (line.toLowerCase().contains(format)) {
					String[] array = line.split(" ");
					if (array != null && array.length >= 5) {
						String temp = array[1].replace("/.android_secure", "");
						if (!sdCard.equals(temp)) {
							external_SDCard = temp;
						}
					}
				}
			}
		} catch (Exception e) {
			external_SDCard = sdCard;
			e.printStackTrace();
		} finally {
			try {
				read.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			read = null;
		}
		return external_SDCard;
	}

}
