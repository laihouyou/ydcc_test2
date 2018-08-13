package com.movementinsome.easyform.util;

import android.content.Context;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.tasks.identify.IdentifyResult;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.movementinsome.AppContext;
import com.movementinsome.database.vo.PicFileInfoVO;
import com.movementinsome.database.vo.TpconfigVO;
import com.movementinsome.easyform.formengineer.XmlGuiForm;
import com.movementinsome.easyform.formengineer.XmlGuiFormField;
import com.movementinsome.easyform.formengineer.XmlGuiRule;
import com.movementinsome.kernel.util.MyDateTools;

import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XmlFiledRuleOperater {

	private static Dao<TpconfigVO, Long> tpconfigDao;

	static {
		try {
			tpconfigDao = AppContext.getInstance().getAppDbHelper()
					.getDao(TpconfigVO.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void ruleOperater(XmlGuiForm form, String ruleId, Object value) {
		if ("mapfac".equalsIgnoreCase(ruleId)) {
			IdentifyResult mMapIden = AppContext.getInstance().getmMapIden();
			for (XmlGuiRule xgr : form.getRuls()) {
				if (ruleId.equalsIgnoreCase(xgr.getId())) {
					for (Map.Entry<String, String> entry : xgr.getDos()
							.entrySet()) {
						XmlGuiFormField xgff = form.findField(entry.getKey());
						if (xgff != null) {
							String[] attrs = entry.getValue().split("\\,");
							for (String attr : attrs) {
								if ("LayerName".equalsIgnoreCase(attr)) {
									xgff.getObj().autoChangValue(
											mMapIden.getLayerName());
									break;
								} else if ("shape".equalsIgnoreCase(attr)) {
									if (mMapIden.getGeometry() instanceof Point) {
										Point point = (Point) mMapIden
												.getGeometry();
										xgff.getObj().autoChangValue(
												point.getX() + " "
														+ point.getY());
									} else if (mMapIden.getGeometry() instanceof Polyline) {
										Polyline pline = (Polyline) mMapIden
												.getGeometry();
										xgff.getObj().autoChangValue(
												pline.getPoint(0).getX()
														+ " "
														+ pline.getPoint(0)
																.getY());
									} else if (mMapIden.getGeometry() instanceof Polygon) {
										// Polygon pgon = (Polygon)
										// mMapIden.getGeometry();
										Envelope env = new Envelope();
										mMapIden.getGeometry().queryEnvelope(
												env);
										xgff.getObj().autoChangValue(
												env.getCenter().getX()
														+ " "
														+ env.getCenter()
																.getY());
									}
								} else {
									if (mMapIden.getAttributes().get(attr) != null) {
										String v = mMapIden.getAttributes()
												.get(attr).toString();
										if (!"".equals(v)) {
											xgff.getObj().autoChangValue(v);
											break;
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public static void sysTemFieldOp(HashMap iparams,
			XmlGuiFormField xmlGuiFormField) {
		String value = xmlGuiFormField.getDefValue();
		Object rlt = null;
		String[] defs = value.split("\\,");
		for (String v:defs){
			// 带“：”为参数引用，不带这个则为系统自定算法
			if (v.contains(":")) {
				if (iparams != null) {
					if (v.indexOf(":")==0){
						rlt = iparams.get(v.substring(1));
						if (xmlGuiFormField.getValue()==null || "".equals(xmlGuiFormField.getValue())){
							if (rlt instanceof String) {
								xmlGuiFormField.setValue(rlt.toString());
							} else if (rlt instanceof Integer) {
								xmlGuiFormField.setValue(String.valueOf(rlt));
							}
						}
					}
				}
			} else {
				if (xmlGuiFormField.getValue()==null || "".equals(xmlGuiFormField.getValue())){
					// 当前日期
					if ("date()".equalsIgnoreCase(v)) {
						java.text.SimpleDateFormat dFormat = new SimpleDateFormat(
								"yyyy-MM-dd");
						java.util.Date date = new Date();
						xmlGuiFormField.setValue(dFormat.format(date));
					} else if ("time()".equalsIgnoreCase(v)) {
						java.text.SimpleDateFormat dFormat = new SimpleDateFormat(
								"HH:mm:ss");
						java.util.Date date = new Date();
						xmlGuiFormField.setValue(dFormat.format(date));
					} else if ("datetime()".equalsIgnoreCase(v)) {
						java.text.SimpleDateFormat dFormat = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						java.util.Date date = new Date();
						xmlGuiFormField.setValue(dFormat.format(date));
					} else if ("username()".equalsIgnoreCase(v)) {
						xmlGuiFormField.setValue(AppContext.getInstance().getCurUser()
								.getUserAlias());
					} else if ("userId()".equalsIgnoreCase(v)) {
						xmlGuiFormField.setValue(AppContext.getInstance().getCurUser()
								.getUserId());
					}else if ("userNum()".equalsIgnoreCase(v)) {
						xmlGuiFormField.setValue(AppContext.getInstance().getCurUser()
								.getUserName());
					} else if ("userPhone()".equalsIgnoreCase(v)) {
						xmlGuiFormField.setValue(AppContext.getInstance().getCurUser()
								.getPhone());
					} else if ("userteam()".equalsIgnoreCase(v)) {
						xmlGuiFormField.setValue(AppContext.getInstance().getCurUser()
								.getDeptName());
					}else if ("userteamId()".equalsIgnoreCase(v)) {
						xmlGuiFormField.setValue(AppContext.getInstance().getCurUser()
								.getDeptId()+"");
					}else if ("userteamNum()".equalsIgnoreCase(v)) {
						xmlGuiFormField.setValue(AppContext.getInstance().getCurUser()
								.getDeptNum());
					}else if ("userGroupName()".equalsIgnoreCase(v)) {
						xmlGuiFormField.setValue(AppContext.getInstance().getCurUser()
								.getGroupName());
					}else if ("userGroupNum()".equalsIgnoreCase(v)) {
						xmlGuiFormField.setValue(AppContext.getInstance().getCurUser()
								.getGroupNum());
					}else if ("userGroupId()".equalsIgnoreCase(v)) {
						xmlGuiFormField.setValue(AppContext.getInstance().getCurUser()
								.getGroupId()+"");
					}else if ("addr()".equalsIgnoreCase(v)) {
						if (AppContext.getInstance().getCurLocation() != null)
							xmlGuiFormField.setValue(AppContext.getInstance()
									.getCurLocation().getAddr());
					} else if ("zoning()".equalsIgnoreCase(v)) {
						if (AppContext.getInstance().getCurLocation() != null)
							xmlGuiFormField.setValue(AppContext.getInstance()
									.getCurLocation().getZoning());
					}else if ("zoning(区)".equalsIgnoreCase(v)) {
						if (AppContext.getInstance().getCurLocation() != null)
							xmlGuiFormField.setValue(AppContext.getInstance()
									.getCurLocation().getZoning().replace("区", ""));
					}
					else if ("mapcoord()".equalsIgnoreCase(v)) {
						if (AppContext.getInstance().getCurLocation() != null)
							xmlGuiFormField.setValue(AppContext.getInstance()
									.getCurLocation().getCurMapPosition());
					} else if ("guid()".equalsIgnoreCase(v)) {
						xmlGuiFormField.setValue(UUID.randomUUID().toString());
					} else if ("imei()".equalsIgnoreCase(v)) {
						xmlGuiFormField.setValue(AppContext.getInstance().getPhoneIMEI());
					} else if ("mapfac()".equalsIgnoreCase(v)) {
						;
					} else if ("mapcoord(N)".equalsIgnoreCase(v)) {
						;
					} else if ("mapcoordbound()".equalsIgnoreCase(v)){
						;
					}
					/*
					 * else if ("weather()".equalsIgnoreCase(value)){
					 * xmlGuiFormField.setValue
					 * (SmartWeatherUrlUtil.getInterfaceURL("101280101", "forecast1d"));
					 * }
					 */
					else {
						xmlGuiFormField.setValue(v);
					}
				}
			}
		}
		return;
	}
	public static void setSysTemFieldOptions(HashMap options,
			XmlGuiFormField xmlGuiFormField) {
		String optionsStr = xmlGuiFormField.getOptions();
		Object rlt = options.get(optionsStr);
		if(rlt!=null){
			if (rlt instanceof String) {
				xmlGuiFormField.setOptions(rlt.toString());
			}
		}
	}
	public static PicFileInfoVO getPictureLog(String guid, String bizType,
			String imgName) {
		String log = CameraLogUtil.getInstance().getLog(imgName);
		PicFileInfoVO result = new PicFileInfoVO();
		if (log != null) {
			try {
				org.json.JSONObject logJson = new org.json.JSONObject(log);
				result.setGuid(guid);
				result.setBusinessType(bizType);
			//	if (AppContext.getInstance().getCurLocation() != null) {
					result.setPosition(logJson.getString("position"));
					result.setGpsPosition(logJson.getString("gpsPosition"));
					result.setPhotographedDateStr(logJson.getString("photographedDate"));
					result.setPfiName(logJson.getString("pfiName"));
					result.setPfiSize(logJson.getDouble("pfiSize"));
					result.setPfiType(logJson.getString("pfiType"));
					result.setOperUName(AppContext.getInstance().getCurUser()
							.getUserAlias());
					try{
						result.setIsUpload(logJson.getString("isUpload"));
					}catch(Exception e){
						
					}
					result.setOperDateStr(MyDateTools.date2String(new Date()));
			//	}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			;

		} else {
			result.setGuid(guid);
			result.setBusinessType(bizType);
			if (AppContext.getInstance().getCurLocation() != null) {
				result.setPosition(AppContext.getInstance().getCurLocation()
						.getCurMapPosition());
				result.setGpsPosition(AppContext.getInstance().getCurLocation()
						.getCurGpsPosition());
			}
			result.setPhotographedDateStr(MyDateTools.date2String(CameraLogUtil.getInstance()
					.getFileModifyDate(imgName)));
			result.setPfiName(CameraLogUtil.getInstance().getFileName(
					imgName));
			try {
				result.setPfiSize((double) CameraLogUtil.getInstance()
						.getFileSize(imgName));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				result.setPfiSize(0.00);
				e.printStackTrace();
			}
			result.setPfiType(CameraLogUtil.getInstance().getFileExtend(
					imgName));
			result.setOperUName(AppContext.getInstance().getCurUser()
					.getUserAlias());
			result.setOperDateStr(MyDateTools.date2String(new Date()));
			
		}
		return result;
	}

	public static PicFileInfoVO getPictureLog2(String guid, String bizType,
			String imgName) {
		String log = CameraLogUtil.getInstance().getLog(imgName);
		PicFileInfoVO result = new PicFileInfoVO();
		if (log != null) {
			try {
				org.json.JSONObject logJson = new org.json.JSONObject(log);
				result.setGuid(guid);
				result.setBusinessType(logJson.getString("businessType"));
			//	if (AppContext.getInstance().getCurLocation() != null) {
					result.setPosition(logJson.getString("position"));
					result.setGpsPosition(logJson.getString("gpsPosition"));
					result.setPhotographedDateStr(logJson.getString("photographedDate"));
					result.setPfiName(logJson.getString("pfiName"));
					result.setPfiSize(logJson.getDouble("pfiSize"));
					result.setPfiType(logJson.getString("pfiType"));
					result.setOperUName(AppContext.getInstance().getCurUser()
							.getUserAlias());
					try{
						result.setIsUpload(logJson.getString("isUpload"));
					}catch(Exception e){

					}
					result.setOperDateStr(MyDateTools.date2String(new Date()));
			//	}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			;

		} else {
			result.setGuid(guid);
			result.setBusinessType(bizType);
			if (AppContext.getInstance().getCurLocation() != null) {
				result.setPosition(AppContext.getInstance().getCurLocation()
						.getCurMapPosition());
				result.setGpsPosition(AppContext.getInstance().getCurLocation()
						.getCurGpsPosition());
			}
			result.setPhotographedDateStr(MyDateTools.date2String(CameraLogUtil.getInstance()
					.getFileModifyDate(imgName)));
			result.setPfiName(CameraLogUtil.getInstance().getFileName(
					imgName));
			try {
				result.setPfiSize((double) CameraLogUtil.getInstance()
						.getFileSize(imgName));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				result.setPfiSize(0.00);
				e.printStackTrace();
			}
			result.setPfiType(CameraLogUtil.getInstance().getFileExtend(
					imgName));
			result.setOperUName(AppContext.getInstance().getCurUser()
					.getUserAlias());
			result.setOperDateStr(MyDateTools.date2String(new Date()));

		}
		return result;
	}

	/**
	 * 通过分类名获取到选择项示值
	 * 
	 * @param name
	 * @return
	 * @throws SQLException
	 */

	public static String getDicInfo(String name) throws SQLException {
		String result = "";
		if (name != null && !"".equalsIgnoreCase(name)) {
			/*
			 * QueryBuilder<TpconfigVO, Long> queryBuilder =
			 * tpconfigDao.queryBuilder(); queryBuilder.where().eq("name",
			 * name); PreparedQuery<TpconfigVO> preparedQuery =
			 * queryBuilder.prepare();
			 * 
			 * List<TpconfigVO> configVos = tpconfigDao.query(preparedQuery);
			 */

			/*List<TpconfigVO> configVos = tpconfigDao.queryForEq("tableName",
					name);*/
			QueryBuilder<TpconfigVO,Long> queryBuilder = tpconfigDao.queryBuilder();
			Where<TpconfigVO, Long> where = queryBuilder.orderBy("ordnum", true).where();
			where.eq("tableName", name);
			List<TpconfigVO> configVos = tpconfigDao.query(queryBuilder.prepare());
			for (TpconfigVO tpconfig : configVos) {
				if ("".equals(result)) {
					result = tpconfig.getPvalue();
				} else {
					result += "|" + tpconfig.getPvalue();
				}
			}
		}

		return result;
	}

	/**
	 * 通过分类名和选项值获取选择值的编码
	 * 
	 * @param name
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	public static String getDicByValue(String name, String value)
			throws SQLException {
		String result = "";
		if (name != null && !"".equalsIgnoreCase(name)) {
			/*
			 * QueryBuilder<TpconfigVO, Long> queryBuilder =
			 * tpconfigDao.queryBuilder(); queryBuilder.where().eq("name",
			 * name); PreparedQuery<TpconfigVO> preparedQuery =
			 * queryBuilder.prepare();
			 * 
			 * List<TpconfigVO> configVos = tpconfigDao.query(preparedQuery);
			 */
			HashMap<String, Object> arg = new HashMap<String, Object>();
			arg.put("tableName", name);
			arg.put("palue", value);

			List<TpconfigVO> configVos = tpconfigDao.queryForFieldValues(arg);

			if (configVos != null && configVos.size() > 0) {
				result = configVos.get(0).getPvaluenum();
			}
		}

		return result;
	}

	/**
	 * 通过分类名和选项编码获取选择值
	 * 
	 * @param name
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	public static String getDicByNum(String name, String value)
			throws SQLException {
		String result = "";
		if (name != null && !"".equalsIgnoreCase(name)) {
			/*
			 * QueryBuilder<TpconfigVO, Long> queryBuilder =
			 * tpconfigDao.queryBuilder(); queryBuilder.where().eq("name",
			 * name); PreparedQuery<TpconfigVO> preparedQuery =
			 * queryBuilder.prepare();
			 * 
			 * List<TpconfigVO> configVos = tpconfigDao.query(preparedQuery);
			 */
			HashMap<String, Object> arg = new HashMap<String, Object>();
			arg.put("tableName", name);
			arg.put("pvaluenum", value);

			List<TpconfigVO> configVos = tpconfigDao.queryForFieldValues(arg);

			if (configVos != null && configVos.size() > 0) {
				result = configVos.get(0).getPvalue();
			}
		}

		return result;
	}

	/**
	 * 根据模板名称获取模板需要的传入参数
	 * 
	 * @param template
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static String getFormInParams(Context context, String template)
			throws IOException, ParserConfigurationException, SAXException {
		String result = "";

		InputStream is = context.getResources().getAssets().open(template);// url.openConnection().getInputStream();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = factory.newDocumentBuilder();
		Document dom = db.parse(is);
		Element root = dom.getDocumentElement();
		NodeList params = root.getElementsByTagName("param");
		if (params.getLength() < 1) {
			// nothing here??
			return null;
		} else {
			for (int i = 0; i < params.getLength(); i++) {
				Node paramNode = params.item(i);
				NamedNodeMap attrParam = paramNode.getAttributes();

				String[] io = attrParam.getNamedItem("io").getNodeValue().split("\\,");
				if (io != null) {
					boolean input = false;
					for (String s : io) {
						if ("i".equalsIgnoreCase(s)) {
							input = true;
							break;
						}
					}
					if (input) {
						if ("".equals(result)) {
							result = attrParam.getNamedItem("name")
									.getNodeValue();
						} else {
							result += ","
									+ attrParam.getNamedItem("name")
											.getNodeValue();
						}
					}
				}
			}

		}

		return result;
	}

	/**
	 * 根据模板名称获取模板需要的传入参数
	 * 
	 * @param template
	 * @return
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public static String getFormOutParams(Context context, String template)
			throws IOException, ParserConfigurationException, SAXException {
		String result = "";

		InputStream is = context.getResources().getAssets().open(template);// url.openConnection().getInputStream();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = factory.newDocumentBuilder();
		Document dom = db.parse(is);
		Element root = dom.getDocumentElement();
		NodeList params = root.getElementsByTagName("param");
		if (params.getLength() < 1) {
			// nothing here??
			return null;
		} else {
			for (int i = 0; i < params.getLength(); i++) {
				Node paramNode = params.item(i);
				NamedNodeMap attrParam = paramNode.getAttributes();

				String[] io = attrParam.getNamedItem("io").getNodeValue().split("\\,");
				if (io != null) {
					boolean input = false;
					for (String s : io) {
						if ("o".equalsIgnoreCase(s)) {
							input = true;
							break;
						}
					}
					if (input) {
						if ("".equals(result)) {
							result = attrParam.getNamedItem("name")
									.getNodeValue();
						} else {
							result += ","
									+ attrParam.getNamedItem("name")
											.getNodeValue();
						}
					}
				}
			}

		}

		return result;
	}
}
