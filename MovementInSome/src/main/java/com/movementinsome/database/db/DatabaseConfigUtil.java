package com.movementinsome.database.db;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

/**
 * Database helper class used to manage the creation and upgrading of your
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {

	

	public static void main(String[] args) throws Exception {
		System.out.println("### begin...");
		//writeConfigFile("ormlite_config.txt", classes);
		System.out.println("### end...");
	}
}
