package com.movementinsome.app.mytask.handle;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.database.vo.WsComplainantFormVO;
import com.movementinsome.kernel.util.JsonAnalysisUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;

public class WsDownloadHandle  implements DownloadBaseHandle{

	private String result;
	private InsTablePushTaskVo insTablePushTaskVo;
	public WsDownloadHandle(String result,InsTablePushTaskVo insTablePushTaskVo){
		this.result=result;
		this.insTablePushTaskVo=insTablePushTaskVo;
	}
	@Override
	public String handle() {
		// TODO Auto-generated method stub
		if (result != null) {
			try {
				JSONObject downloadObject = new JSONObject(result);
				String content0 = (String) downloadObject.get("content");
				String tableName = downloadObject.getString("tableName");
				JSONArray content = new JSONArray(content0);
				String code = downloadObject.getString("code");
				if("1".equals(code)){
					if (Constant.URGENT.equals(tableName)
							&&Constant.COMPLAINANT_FORM.equals(insTablePushTaskVo.getTaskCategory())){
						if (content != null && content.length() > 0) {
							Dao<WsComplainantFormVO, Long> wsComplainantFormVODao = AppContext
							.getInstance().getAppDbHelper()
							.getDao(WsComplainantFormVO.class);
							for (int i = 0; i < content.length(); ++i) {
								WsComplainantFormVO wsComplainantFormVO =new WsComplainantFormVO();
								JsonAnalysisUtil
								.setJsonObjectData(content
										.getJSONObject(i), wsComplainantFormVO);
								wsComplainantFormVODao.create(wsComplainantFormVO);
							}
							updatePush(insTablePushTaskVo);
							return "1";
						}else{
							// 没有相应的处理类型
							return "0";
						}
					}else{
						return "3";
					}
				}else{
					return "0";
				}
				
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return "0";
			}
			
		}else{
			return null;
		}
	}
	private static void updatePush(InsTablePushTaskVo insTablePushTaskVo) {
		try {
			Dao<InsTablePushTaskVo, Long> insTablePushTaskDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsTablePushTaskVo.class);

			insTablePushTaskVo.setIsDownload("1");
			insTablePushTaskDao.update(insTablePushTaskVo);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
