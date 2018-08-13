package com.movementinsome.sysmanager.init.task;

import java.sql.SQLException;
import java.util.List;

import com.movementinsome.AppContext;
import com.j256.ormlite.dao.Dao;

public class DataInitThread<T> extends Thread {

	private boolean sucessed = false;
	private Dao<T, Long> dao;
	private List<T> data;
	private Class<T> clazz;

	public boolean isSucessed() {
		// TODO Auto-generated method stub
		return sucessed;
	}

	public DataInitThread(Class<T> clazz, List<T> data) {
		// TODO Auto-generated method stub
		this.clazz = clazz;
		this.data = data;
	}

	@Override
	public void run() {

		try {
			dao = AppContext.getInstance().getAppDbHelper().getDao(clazz);
			dao.deleteBuilder().delete();
			for (int i = 0; i < data.size(); ++i) {
				T vo;
				try {
					vo = clazz.newInstance();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					sucessed = false;
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					sucessed = false;
					e.printStackTrace();
				}
				vo = data.get(i);
				// vo.setId((long) i);
				dao.create(vo);
			}
			sucessed = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			sucessed = false;
			e.printStackTrace();
		}
	}

}
