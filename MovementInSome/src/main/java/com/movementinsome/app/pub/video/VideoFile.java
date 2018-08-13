package com.movementinsome.app.pub.video;

import android.annotation.SuppressLint;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

@SuppressLint("NewApi")
public class VideoFile {
	private String              path;
	private final String		mFilename;

	public VideoFile(String filename) {
		this.mFilename = filename;
	}

	public String getFullPath() {
		return getFile().getAbsolutePath();
	}

	public File getFile() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			try {
				path = Environment.getExternalStorageDirectory()
						.getCanonicalPath().toString()
						+ "/DCIM/VIDEO";
				File files = new File(path);
				if (!files.exists()) {
					files.mkdir();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new File(path, generateFilename());
	}

	private String generateFilename() {
		if (isValidFilename()) return mFilename;

		return Utils.setRecordVideoFileName();
	}

	private boolean isValidFilename() {
		if (mFilename == null) return false;
		if (mFilename.isEmpty()) return false;

		return true;
	}
}
