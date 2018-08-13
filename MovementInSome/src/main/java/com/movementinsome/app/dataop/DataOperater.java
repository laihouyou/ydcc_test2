package com.movementinsome.app.dataop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.initial.model.Module;

public class DataOperater {

	protected Context context;
	protected InsTablePushTaskVo insTablePushTaskVo;
	protected String guid;
	protected List<Module> lstModule;
	
	public DataOperater(Context context, InsTablePushTaskVo insTablePushTaskVo,
			String guid, List<Module> lstModule) {
		this.context = context;
		this.insTablePushTaskVo = insTablePushTaskVo;
		this.guid = guid;
		this.lstModule = lstModule;
	}

	public List<Map<String, Object>> getListData(List list, String key) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (list != null) {
			for (int i = 0; i < list.size(); ++i) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put(key, list.get(i));
				data.add(m);
			}
		}
		return data;
	}

	public String getCurDate(){
		SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(System.currentTimeMillis());

		return f.format(date);
	}

}
