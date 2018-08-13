package com.movementinsome.database.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;
import com.movementinsome.caice.vo.CityVo;
import com.movementinsome.caice.vo.HistoryCommitVO;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SaveLineVo;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.database.vo.BasTBaseMaterialVO;
import com.movementinsome.database.vo.BasTBaseMechanicalVO;
import com.movementinsome.database.vo.BsEmphasisInsArea;
import com.movementinsome.database.vo.BsInsContentSettingVO;
import com.movementinsome.database.vo.BsInsFacInfo;
import com.movementinsome.database.vo.BsInsTypeSettingVO;
import com.movementinsome.database.vo.BsLeakInsArea;
import com.movementinsome.database.vo.BsPnInsTask;
import com.movementinsome.database.vo.BsRoutineInsArea;
import com.movementinsome.database.vo.BsRushRepairWorkOrder;
import com.movementinsome.database.vo.BsSupervisionPoint;
import com.movementinsome.database.vo.CoordinateVO;
import com.movementinsome.database.vo.DrainageVO;
import com.movementinsome.database.vo.DynamicFormVO;
import com.movementinsome.database.vo.HistoryTrajectoryVO;
import com.movementinsome.database.vo.HolidayManage;
import com.movementinsome.database.vo.InsCheckBuilding;
import com.movementinsome.database.vo.InsCheckFacRoad;
import com.movementinsome.database.vo.InsDatumInaccurate;
import com.movementinsome.database.vo.InsDredgePTask;
import com.movementinsome.database.vo.InsFacMaintenanceData;
import com.movementinsome.database.vo.InsGroupVO;
import com.movementinsome.database.vo.InsKeyPointPatrolData;
import com.movementinsome.database.vo.InsPatrolAreaData;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsPatrolOnsiteRecordVO;
import com.movementinsome.database.vo.InsPlanTaskVO;
import com.movementinsome.database.vo.InsSiteManage;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.database.vo.InsTableSaveDataVo;
import com.movementinsome.database.vo.MyReceiveMsgVO;
import com.movementinsome.database.vo.SecDepartmentVO;
import com.movementinsome.database.vo.SecUserBasicInfoVO;
import com.movementinsome.database.vo.TaskUploadStateVO;
import com.movementinsome.database.vo.TpconfigVO;
import com.movementinsome.database.vo.WorkTimeRecords;
import com.movementinsome.database.vo.WsComplainantFormVO;
import com.pop.android.common.util.ToastUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class AppDataBaseHelper extends OrmLiteSqliteOpenHelper {

	public static final Class<?>[] classes = new Class[] {
			SecDepartmentVO.class, InsGroupVO.class, SecUserBasicInfoVO.class,
			TpconfigVO.class, BasTBaseMaterialVO.class, BasTBaseMechanicalVO.class,
			InsTablePushTaskVo.class, DynamicFormVO.class,
			InsTableSaveDataVo.class, InsPlanTaskVO.class,
			InsPatrolDataVO.class, TaskUploadStateVO.class,
			InsSiteManage.class, InsKeyPointPatrolData.class,
			InsFacMaintenanceData.class, InsPatrolOnsiteRecordVO.class,
			InsDredgePTask.class, DrainageVO.class, CoordinateVO.class,
			BsInsContentSettingVO.class, BsInsTypeSettingVO.class ,WsComplainantFormVO.class
			,MyReceiveMsgVO.class,HolidayManage.class,WorkTimeRecords.class
			,BsPnInsTask.class,BsEmphasisInsArea.class,BsInsFacInfo.class,BsLeakInsArea.class
			,BsRoutineInsArea.class,BsSupervisionPoint.class,BsRushRepairWorkOrder.class
			,HistoryTrajectoryVO.class,InsPatrolAreaData.class
			,InsCheckFacRoad.class,InsCheckBuilding.class,InsDatumInaccurate.class
			, SavePointVo.class, ProjectVo.class, SaveLineVo.class, HistoryCommitVO.class
			, CityVo.class
	};
	// name of the database file for your application -- change to something
	// appropriate for your app
	private static final String DATABASE_NAME = "gddstAppDb.db";
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 5;
	/*
	 * private String DATABASE_PATH = Environment .getExternalStorageDirectory()
	 * + "/mapApp/database/gddstAppDb.db";
	 */
	// the DAO object we use to access the SimpleData table
	// private Dao<SimpleData, Integer> simpleDao = null;
	private static final AtomicInteger usageCounter = new AtomicInteger(0);

	// we do this so there is only one helper
	private static AppDataBaseHelper helper = null;
	private Context context;

	public AppDataBaseHelper(Context context) {
		/*
		 * super(context, DATABASE_NAME, null, DATABASE_VERSION,
		 * R.raw.ormlite_config);
		 */
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	/**
	 * Get the helper, possibly constructing it if necessary. For each call to
	 * this method, there should be 1 and only 1 call to {@link #close()}.
	 */
	public static synchronized AppDataBaseHelper getHelper(Context context) {
		if (helper == null) {
			helper = new AppDataBaseHelper(context);
		}
		usageCounter.incrementAndGet();
		return helper;
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		// TODO Auto-generated method stub
		if (classes.length == 0) {
			return;
		}
		for (Class<?> clazz : classes) {
			try {
				Log.d("DB", "-----创建DB begin");
				TableUtils.createTable(connectionSource, clazz);
				Log.d("DB", "-----创建DB end");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if (classes.length == 0) {
			return;
		}
		if(oldVersion<2){
			try {
				this.getDao(ProjectVo.class).executeRaw("ALTER TABLE ProjectVo ADD autoNumberLine integer;");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(oldVersion<3){
			try {
				this.getDao(ProjectVo.class).executeRaw("ALTER TABLE ProjectVo ADD isPresent text;");
				this.getDao(ProjectVo.class).executeRaw("ALTER TABLE ProjectVo ADD isCompile text;");
				this.getDao(ProjectVo.class).executeRaw("ALTER TABLE ProjectVo ADD usedId text;");

				//设施VO增加的字段
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD isPresent text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD isCompile text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD dataType text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD pipName text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD tubularProduct text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD pipType text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD layingType text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD drawNum text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD pipelineLinght text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD endMarkerId text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD pointList text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD ids text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD sIds text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD facNums text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD firstFacNum text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD endFacNum text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD context text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD usedId text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD guid text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD longitudeWg84 text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD latitudeWg84 text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD mapx text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD mapy text;");

//				ToastUtils.showToast(context,"数据库升级成功");

//				try {
//					DateBaseUpdate.LineDataSwitchPointData(context);
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (oldVersion < 4) {
			try {

				//设施VO增加的字段
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD locationJson text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD newShareCode text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD longitudeGcj02 text;");
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD latitudeGcj02 text;");

				this.getDao(ProjectVo.class).executeRaw("ALTER TABLE ProjectVo ADD qicanStr text;");
				this.getDao(ProjectVo.class).executeRaw("ALTER TABLE ProjectVo ADD newShareCode text;");

				//增加表
				TableUtils.createTable(connectionSource, CityVo.class);

//				DateBaseUpdate.Bd09ToGcj02();

				ToastUtils.showToast(context, "数据库升级成功");

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (oldVersion < 5) {
			try {
				//设施VO增加的字段
				this.getDao(SavePointVo.class).executeRaw("ALTER TABLE SavePointVo ADD points text;");

				ToastUtils.showToast(context, "数据库升级成功");

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ToastUtils.showToast(context, "数据库升级失败");
			}
		}

	}

	/*
	 * @Override public synchronized SQLiteDatabase getWritableDatabase() {
	 * //LogUtil.e(DatabaseHelper.class.getName(), "getWritableDatabase()");
	 * super.getWritableDatabase(); return
	 * SQLiteDatabase.openDatabase(DATABASE_NAME, null,
	 * SQLiteDatabase.OPEN_READWRITE); }
	 * 
	 * @Override public synchronized SQLiteDatabase getReadableDatabase() {
	 * //LogUtil.e(DatabaseHelper.class.getName(), "getReadableDatabase()");
	 * super.getReadableDatabase(); return
	 * SQLiteDatabase.openDatabase(DATABASE_NAME, null,
	 * SQLiteDatabase.OPEN_READONLY); }
	 */

	public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz)
			throws SQLException, SQLException {
		// lookup the dao, possibly invoking the cached database config
		Dao<T, ?> dao = DaoManager.lookupDao(connectionSource, clazz);
		if (dao == null) {
			// try to use our new reflection magic
			DatabaseTableConfig<T> tableConfig;
			tableConfig = DatabaseTableConfig
					.fromClass(connectionSource, clazz);

			if (tableConfig == null) {
				/**
				 * TODO: we have to do this to get to see if they are using the
				 * deprecated annotations like {@link DatabaseFieldSimple}.
				 */
				dao = (Dao<T, ?>) DaoManager.createDao(connectionSource, clazz);
			} else {
				dao = (Dao<T, ?>) DaoManager.createDao(connectionSource,
						tableConfig);

			}
		}

		@SuppressWarnings("unchecked")
		D castDao = (D) dao;
		return castDao;
	}

	public <T> int save(Dao<T, ?> dao, T t) throws SQLException {
		return dao.create(t);
	}

	public <T> List<T> query(Dao<T, ?> dao, PreparedQuery<T> preparedQuery)
			throws SQLException {
		return dao.query(preparedQuery);
	}

	public <T> List<T> query(Dao<T, ?> dao, String attributeName,
			String attributeValue) throws SQLException {
		QueryBuilder<T, Integer> queryBuilder = (QueryBuilder<T, Integer>) dao
				.queryBuilder();
		queryBuilder.where().eq(attributeName, attributeValue);
		PreparedQuery<T> preparedQuery = queryBuilder.prepare();
		return query(dao, preparedQuery);
	}

	public <T> List<T> query(Dao<T, ?> dao, String[] attributeNames,
			String[] attributeValues) throws SQLException {

		QueryBuilder<T, Integer> queryBuilder = (QueryBuilder<T, Integer>) dao
				.queryBuilder();
		Where<T, Integer> wheres = queryBuilder.where();
		for (int i = 0; i < attributeNames.length; i++) {
			wheres.eq(attributeNames[i], attributeValues[i]);
		}
		PreparedQuery<T> preparedQuery = queryBuilder.prepare();
		return query(dao, preparedQuery);
	}

	public <T> List<T> queryAll(Dao<T, ?> dao) throws SQLException {
		// QueryBuilder<T, Integer> queryBuilder = getDao().queryBuilder();
		// PreparedQuery<T> preparedQuery = queryBuilder.prepare();
		// return query(preparedQuery);
		return dao.queryForAll();
	}

	public <T> T queryById(Dao<T, ?> dao, String idName, String idValue)
			throws SQLException {
		List<T> lst = query(dao, idName, idValue);
		if (null != lst && !lst.isEmpty()) {
			return lst.get(0);
		} else {
			return null;
		}
	}

	public <T> int delete(Dao<T, ?> dao, PreparedDelete<T> preparedDelete)
			throws SQLException {
		return dao.delete(preparedDelete);
	}

	public <T> int delete(Dao<T, ?> dao, T t) throws SQLException {
		return dao.delete(t);
	}

	public <T> int delete(Dao<T, ?> dao, List<T> lst) throws SQLException {
		return dao.delete(lst);
	}

	public <T> int delete(Dao<T, ?> dao, String[] attributeNames,
			String[] attributeValues) throws SQLException {
		List<T> lst = query(dao, attributeNames, attributeValues);
		if (null != lst && !lst.isEmpty()) {
			return delete(dao, lst);
		}
		return 0;
	}

	public <T> int deleteById(Dao<T, ?> dao, String idName, String idValue)
			throws SQLException {
		T t = queryById(dao, idName, idValue);
		if (null != t) {
			return delete(dao, t);
		}
		return 0;
	}

	public <T> int update(Dao<T, ?> dao, T t) throws SQLException {
		return dao.update(t);
	}

	public <T> boolean isTableExsits(Dao<T, ?> dao) throws SQLException {
		return dao.isTableExists();
	}

	public <T> long countOf(Dao<T, ?> dao) throws SQLException {
		return dao.countOf();
	}

	public <T> List<T> query(Dao<T, ?> dao, Map<String, Object> map)
			throws SQLException {
		QueryBuilder<T, Integer> queryBuilder = (QueryBuilder<T, Integer>) dao
				.queryBuilder();
		if (!map.isEmpty()) {
			Where<T, Integer> wheres = queryBuilder.where();
			Set<String> keys = map.keySet();
			ArrayList<String> keyss = new ArrayList<String>();
			keyss.addAll(keys);
			for (int i = 0; i < keyss.size(); i++) {
				if (i == 0) {
					wheres.eq(keyss.get(i), map.get(keyss.get(i)));
				} else {
					wheres.and().eq(keyss.get(i), map.get(keyss.get(i)));
				}
			}
		}
		PreparedQuery<T> preparedQuery = queryBuilder.prepare();
		return query(dao, preparedQuery);
	}

	public <T> List<T> query(Dao<T, ?> dao, Map<String, Object> map,
			Map<String, Object> lowMap, Map<String, Object> highMap)
			throws SQLException {
		QueryBuilder<T, Integer> queryBuilder = (QueryBuilder<T, Integer>) dao
				.queryBuilder();
		Where<T, Integer> wheres = queryBuilder.where();
		if (!map.isEmpty()) {
			Set<String> keys = map.keySet();
			ArrayList<String> keyss = new ArrayList<String>();
			keyss.addAll(keys);
			for (int i = 0; i < keyss.size(); i++) {
				if (i == 0) {
					wheres.eq(keyss.get(i), map.get(keyss.get(i)));
				} else {
					wheres.and().eq(keyss.get(i), map.get(keyss.get(i)));
				}
			}
		}
		if (!lowMap.isEmpty()) {
			Set<String> keys = lowMap.keySet();
			ArrayList<String> keyss = new ArrayList<String>();
			keyss.addAll(keys);
			for (int i = 0; i < keyss.size(); i++) {
				if (map.isEmpty()) {
					wheres.gt(keyss.get(i), lowMap.get(keyss.get(i)));
				} else {
					wheres.and().gt(keyss.get(i), lowMap.get(keyss.get(i)));
				}
			}
		}

		if (!highMap.isEmpty()) {
			Set<String> keys = highMap.keySet();
			ArrayList<String> keyss = new ArrayList<String>();
			keyss.addAll(keys);
			for (int i = 0; i < keyss.size(); i++) {
				wheres.and().lt(keyss.get(i), highMap.get(keyss.get(i)));
			}
		}
		PreparedQuery<T> preparedQuery = queryBuilder.prepare();
		return query(dao, preparedQuery);
	}
}
