package com.movementinsome.sysmanager.init;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.XmlPull;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.caice.save.SaveCity;
import com.movementinsome.database.vo.BasTBaseMaterialVO;
import com.movementinsome.database.vo.BasTBaseMechanicalVO;
import com.movementinsome.database.vo.BsInsContentSettingVO;
import com.movementinsome.database.vo.BsInsTypeSettingVO;
import com.movementinsome.database.vo.HolidayManage;
import com.movementinsome.database.vo.InitDataVO;
import com.movementinsome.database.vo.InsGroupVO;
import com.movementinsome.database.vo.SecDepartmentVO;
import com.movementinsome.database.vo.SecUserBasicInfoVO;
import com.movementinsome.database.vo.TpconfigVO;
import com.movementinsome.kernel.initial.model.Solution;
import com.movementinsome.kernel.initial.model.Sub;
import com.movementinsome.kernel.util.DeCompressUtil;
import com.movementinsome.kernel.util.FileUtils;
import com.movementinsome.kernel.util.MD5;
import com.movementinsome.kernel.util.NetUtils;
import com.movementinsome.sysmanager.init.downloadoffmap.Downloadoffmap;
import com.movementinsome.sysmanager.init.task.DataInitThread;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class IniCheck {
	
	private static String _SPF_NAME="sysIniData";
	private static String _HTTP = "http://";
	private static String _APPCONTEXT = "/fisds";
	//private static String _KEY = "?key="+MD5.getConfigKey();
	private static String _XFORM_PWD="89225300";
	public static String _NO_UP_FLAG="未更新";
	public static String _HAD_UP_FLAG="已更新";
	
	private static String _DOWNLOAD_README_URL = "/download/readme.xml";
	private static String _DOWNLOAD_SYSCONFIG_URL = "/download/Config/solution.xml";
	private static String _DOWNLOAD_SYSCONFIG_SUB_URL = "/download/Config/";
	private static String _DOWNLOAD_XFORMTEMPLATE_URL = "/download/Template/xFormTemplate.zip";
	private static String _DOWNLOAD_LOCALMAP_URL = "/download/LocalMap";
	private static String _DOWNLOAD_UPAPK_URL = "/download/UpApk";
	
	private static String sysconfigWifi;//系统配置
	private static String sysconfigDataconn;
	private static String sysconfigFoce;
	
	private static String xformWifi;//表单模块
	private static String xformDataconn;
	private static String xformFoce;
	
	private static String upackageWifi;//升级包
	private static String upackageDataconn;
	private static String upackageFoce;
	
	private static String offlineWifi;//离线地图
	private static String offlineDataconn;
	private static String offlineFoce;
	
	private static String dictinfoWifi;//数据字典
	private static String dictinfoDataconn;
	private static String dictinfoFoce;
	public static List<String>listOfflineFile = new ArrayList<String>();//离线文件名（全部）
	public static List<String>listOfflineName = new ArrayList<String>();//离线中文文件名（全部）
	
	public static Map<String, String>listOfflineMsg = new HashMap<String, String>();//离线地图信息（全部）
	
	public static List<String>listOfflineFileUpdate = new ArrayList<String>();//离线文件名（升级）
	public static List<String>listOfflineNameUpdate = new ArrayList<String>();//离线中文文件名（升级）
	private static String offlineFile;
	
	public static boolean sysChange = false;
	public static boolean xformChange = false;
	public static boolean upackageChange = false;
	public static boolean offlineChange = false;
	public static boolean dictChange = false;
	
	
	//检查初始化情况
	public static void doCheckIni(Context context,String appUrl,Handler mHandler) {
		String urlStr = "";
		if (appUrl.indexOf(_HTTP)<0){
			urlStr = _HTTP+appUrl+_APPCONTEXT;
		}else{
			urlStr = appUrl;
		}
		mHandler.sendEmptyMessage(5);
		
		CofigDownloader cofigDownloader = new CofigDownloader();
		InputStream file;
		try {
			file = cofigDownloader.getInputStream(urlStr + _DOWNLOAD_README_URL+"?key="+MD5.getConfigKey());
			if (null != file) {
				doReadMeXml(context,file);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		mHandler.sendEmptyMessage(6);
		//doDifIni(context,urlStr,mHandler);
	}
	public static boolean doCheckIni2(Context context,String appUrl,Handler mHandler) {
		String urlStr = "";
		if (appUrl.indexOf(_HTTP)<0){
			urlStr = _HTTP+appUrl+_APPCONTEXT;
		}else{
			urlStr = appUrl;
		}
		
		CofigDownloader cofigDownloader = new CofigDownloader();
		InputStream file;
		try {
			file = cofigDownloader.getInputStream(urlStr + _DOWNLOAD_README_URL+"?key="+MD5.getConfigKey());
			
			if (null != file) {
				doReadMeXml(context,file);
			}
			return true;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
	}

	//完整升级
	public static void doFullIni(){
		
	}
	
	//差异升级
	public static void doDifIni(Context context,String url,Handler mHandler){
		if (sysChange){
			upSysConfig(context,url,mHandler);
		}
		if (xformChange){
			upXform(context,url,mHandler);
		}
		if (offlineChange){
			upOfflineMap(context,url,mHandler);
		}
		if (dictChange){
			upDictInfo(context,url,mHandler);
		}
	}
	
	//按指定步骤升级
	public static boolean doByStep(Context context,String url,int step,Handler mHandler){
		switch (step){
			case 0:
				return doCheckIni2(context, url,mHandler);
			case 1:
				return upSysConfig(context,url,mHandler);
			case 2:
				return upXform(context,url,mHandler);
			case 3:
				return upOfflineMap(context,url,mHandler);
			case 4:
				return upDictInfo(context,url,mHandler);
			case 5:
				return true;
			default:
				return false;
				 	
		}
	}
	
	//下载及更新系统参数设置
	public static boolean upSysConfig(Context context,String url,Handler mHandler) {
		if(sysconfigFoce==null||"true".equals(sysconfigFoce)){
			mHandler.sendEmptyMessage(7);
			Bundle bundle = new Bundle();
			Message msg = new Message();
			
			String urlStr = url+_DOWNLOAD_SYSCONFIG_URL+"?key="+MD5.getConfigKey();
			CofigDownloader cofigDownloader = new CofigDownloader();
			int succ = cofigDownloader.downfileOver(urlStr, AppContext.getInstance().getAppConfigPath(), "solution.xml");		
			if (succ >=0){
				InputStream file = null;
				
				try {
					file = FileUtils.readFile(AppContext.getInstance().getAppConfigFileName());
					XmlPull xmlPull =  new XmlPull();
					Solution solution = xmlPull.readSolutionConfig(file);
					if (solution != null){
						for(Sub sub:solution.getSubs()){
							urlStr =url+_DOWNLOAD_SYSCONFIG_SUB_URL+sub.getFile()+"?key="+MD5.getConfigKey();
							cofigDownloader.downfileOver(urlStr, AppContext.getInstance().getAppConfigPath(), sub.getFile());
						}
					}
					//msgSend("系统参数下载成功……", 3);
					// 设置默认系统
					if(solution!=null){
						AppContext.getInstance().iniDefSolution(solution.getDefatult());
					}
					//加载及解析设置
					AppContext.getInstance().initSolution();
					bundle.putString("msg", "下载系统配置成功");
					msg.what=6;
					msg.setData(bundle);
					mHandler.sendMessage(msg);

					SaveCity.SaveCityList();
					return true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if (sysChange){
						rePreferencePara(context, "sysconfig");
					}
					bundle.putString("msg", "下载系统配置失败");
					msg.what=6;
					msg.setData(bundle);
					mHandler.sendMessage(msg);
					return false;
				}			
			}
			if (sysChange){
				rePreferencePara(context, "sysconfig");
			}
			bundle.putString("msg", "系统配置解析有误");
			msg.what=6;
			msg.setData(bundle);
			mHandler.sendMessage(msg);
			return true;
		}else {
			return false;
		}
	}

	static boolean upApp(Context context,String url){
		
		String urlStr = url+_DOWNLOAD_UPAPK_URL+"?key="+MD5.getConfigKey();
		CofigDownloader cofigDownloader = new CofigDownloader();
		int succ = cofigDownloader.downfileOver(urlStr, AppContext.getInstance().getAppConfigPath(), "solution.xml");	
		return false;
	}
	//下载更新离线地图
	public synchronized static boolean upOfflineMap(Context context,String url,Handler mHandler) {
		if(offlineFoce==null || "true".equals(offlineFoce)){
			String filepath = AppContext.getInstance().getAppStoreMapPath() + offlineFile;  
			File file = new File(filepath);
			String downloadUrl = url+_DOWNLOAD_LOCALMAP_URL+"/"+offlineFile+"?key="+MD5.getConfigKey();
			int threadNum = 10;
			Downloadoffmap downloadoffmap = new Downloadoffmap(mHandler);
			downloadoffmap.download(downloadUrl, threadNum, filepath );
			while(downloadoffmap.isFail()){
				try {
					if(downloadoffmap.getFinishThreadNum()== threadNum){
						// 下载成功记录版本
						if(!Downloadoffmap.isPause){
							setPreferencePara(context, "offline_"+offlineFile, listOfflineMsg.get("offline_"+offlineFile), _NO_UP_FLAG);
						}
						/*if (offlineChange){
							for(String fileName : IniCheck.listOfflineFileUpdate){
								if(!fileName.equals(offlineFile)){
									IniCheck.rePreferencePara(context, "offline_"+fileName);
								}
							}
						}*/
						return true;
					}
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			/*if (offlineChange){
				for(String fileName : IniCheck.listOfflineFileUpdate){
					IniCheck.rePreferencePara(context, "offline_"+fileName);
				}
			}*/
			Message message = new Message();
			message.what = 2;
			mHandler.sendMessage(message);
		}else{
			int downConnType = -1;
			if ("true".equals(offlineWifi)&&"true".equals(offlineDataconn)){
				downConnType = 3;
			}else if ("true".equals(offlineWifi)){
				downConnType = 1;
			}else if ("true".equals(offlineDataconn)){
				downConnType = 2;
			}
			
			//if("true".equals(offlineWifi)){
			if (downConnType !=-1 &&(downConnType >= NetUtils.getAPNType(context))){
			//boolean isWifi = isWifi(context);
				//if (isWifi==true) {
					// 如果不是WIFI情况下不下载
					String filepath = AppContext.getInstance().getAppStoreMapPath() + offlineFile;  
					File file = new File(filepath);
					String downloadUrl = url+_DOWNLOAD_LOCALMAP_URL+"/"+offlineFile+"?key="+MD5.getConfigKey();
					int threadNum = 10;
					Downloadoffmap downloadoffmap = new Downloadoffmap(mHandler);
					downloadoffmap.download(downloadUrl, threadNum, filepath );
					while(downloadoffmap.isFail()){
						try {
							if(downloadoffmap.getFinishThreadNum()== threadNum){
								// 下载成功记录版本
								if(!Downloadoffmap.isPause){
									setPreferencePara(context, "offline_"+offlineFile, listOfflineMsg.get("offline_"+offlineFile), _NO_UP_FLAG);
								}
								/*if (offlineChange){
									for(String fileName : IniCheck.listOfflineFileUpdate){
										if(!fileName.equals(offlineFile)){
											IniCheck.rePreferencePara(context, "offline_"+fileName);
										}
									}
								}*/
								return true;
							}
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				//}
				/*if (offlineChange){
					for(String fileName : IniCheck.listOfflineFileUpdate){
						IniCheck.rePreferencePara(context, "offline_"+fileName);
					}
				}*/
				Message message = new Message();
				message.what = 2;
				mHandler.sendMessage(message);
			}else{
				return false;
			}
		}
		
		return false;
	}

	private static void selectDialog(Context context) {
		// TODO Auto-generated method stub
		String[] items = (String[]) listOfflineFile.toArray();
		new AlertDialog.Builder(context).setTitle("地图选择")
			.setItems(items, new DialogInterface.OnClickListener() {
					@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					// EditTextWb.setText(items[which]);
						}
				}).show();
	}
	// 下载及初始化数据字典
	public synchronized static boolean upDictInfo(Context context, String url1,Handler mHandler) {
		// TODO Auto-generated method stub
		if (dictinfoFoce==null||"true".equals(dictinfoFoce)) {
			mHandler.sendEmptyMessage(9);
			// 开始事务
			try {
				String url = AppContext.getInstance().getInitServerUrl();
				InitDataVO initData = SpringUtil.downloadFile(url, AppContext
						.getInstance().getPhoneIMEI(), url1, AppContext.getInstance()
						.getVersionName(),mHandler);
				if (initData != null) {
					// msgSend("数据下载成功……", 3);
					// InitDataVO initData = result.getBody();
					// msgSend("存储初始化数据…", 1);
					DataInitThread idit[] = { null, null, null, null, null,
							null, null };

					if (initData.getTpconfigs() != null) {
						idit[0] = new DataInitThread<TpconfigVO>(
								TpconfigVO.class, initData.getTpconfigs());
						idit[0].start();
					}
					// 存储部门列表
					if (initData.getDeptList() != null) {
						idit[1] = new DataInitThread<SecDepartmentVO>(
								SecDepartmentVO.class, initData.getDeptList());
						idit[1].start();
					}
					// 存储班组
					if (initData.getGroupList() != null) {
						idit[2] = new DataInitThread<InsGroupVO>(
								InsGroupVO.class, initData.getGroupList());
						idit[2].start();
					}
					// 用户列表
					if (initData.getUserList() != null) {
						idit[3] = new DataInitThread<SecUserBasicInfoVO>(
								SecUserBasicInfoVO.class,
								initData.getUserList());
						idit[3].start();
					}

					if (initData.getMaterialList() != null) {
						idit[4] = new DataInitThread<BasTBaseMaterialVO>(
								BasTBaseMaterialVO.class,
								initData.getMaterialList());
						idit[4].start();
					}

					if (initData.getMechanicals() != null) {
						idit[5] = new DataInitThread<BasTBaseMechanicalVO>(
								BasTBaseMechanicalVO.class,
								initData.getMechanicals());
						idit[5].start();
					}

					if (initData.getHolidayList() != null) {
						idit[6] = new DataInitThread<HolidayManage>(
								HolidayManage.class, initData.getHolidayList());
						idit[6].start();
					}
					// 巡检项目类型和内容
					if (initData.getTypeSettingList() != null) {
						Dao<BsInsTypeSettingVO, Long> bsInsTypeSettingDao = AppContext
								.getInstance().getAppDbHelper()
								.getDao(BsInsTypeSettingVO.class);
						bsInsTypeSettingDao.deleteBuilder().delete();
						Dao<BsInsContentSettingVO, Long> bsInsContentSettingDao = AppContext
								.getInstance().getAppDbHelper()
								.getDao(BsInsContentSettingVO.class);
						bsInsContentSettingDao.deleteBuilder().delete();
						for (BsInsTypeSettingVO bsInsTypeSettingVO : initData
								.getTypeSettingList()) {
							int x = bsInsTypeSettingDao
									.create(bsInsTypeSettingVO);
							if (bsInsTypeSettingVO.getBsInsContentSettings() != null) {
								for (BsInsContentSettingVO bsInsContentSettingVO : bsInsTypeSettingVO
										.getBsInsContentSettings()) {
									int y = bsInsContentSettingDao
											.create(bsInsContentSettingVO);
								}
							}

						}
					}
						boolean isfinished = false;
						while (!isfinished) {
							// isfinished = true;
							// 当前所有线程下载总量
							int j = 0;
							for (int i = 0; i < 7; i++) {
								if (idit[i] != null) {
									if (idit[i].isSucessed()) {
										j++;
									}
								} else {
									j++;
								}
							}
							if (j == 7) {
								break;
							}
						}
						mHandler.sendEmptyMessage(10);
						return true;
				} else {
					if (dictChange){
						rePreferencePara(context, "dictinfo");
					}
					mHandler.sendEmptyMessage(10);
					return false;
				}
			} catch (Exception ex) {
				if (dictChange){
					rePreferencePara(context, "dictinfo");
				}
				mHandler.sendEmptyMessage(10);
				return false;
			}
		}
		return false;
	}
	
	
	//下载及初始化表单模板
	public static boolean upXform(Context context,String url,Handler mHandler) {
		if(xformFoce == null || "true".equals(xformFoce)){
			mHandler.sendEmptyMessage(8);
			Bundle bundle = new Bundle();
			Message msg = new Message();
			
			String urlStr = url+_DOWNLOAD_XFORMTEMPLATE_URL+"?key="+MD5.getConfigKey();
			File file = new File(AppContext.getInstance().getAppConfigPath()+ "xFormTemplate.zip");
			if (file.exists()){
				file.delete();
			}
			CofigDownloader cofigDownloader = new CofigDownloader();
			int succ = cofigDownloader.downfileOver(urlStr, AppContext.getInstance().getAppConfigPath(), "xFormTemplate.zip");		
			if (succ >=0){
				try {
					
					DeCompressUtil.unZip(AppContext.getInstance().getAppConfigPath()+"xFormTemplate.zip", 
							AppContext.getInstance().getAppConfigPath());	
					bundle.putString("msg", "下载表单配置成功");
					msg.what=6;
					msg.setData(bundle);
					mHandler.sendMessage(msg);
					return true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if (xformChange){
						rePreferencePara(context, "xform");
					}
					bundle.putString("msg", "下载表单配置失败");
					msg.what=6;
					msg.setData(bundle);
					mHandler.sendMessage(msg);
					return false;
				}
			}
			if (xformChange){
				rePreferencePara(context, "xform");
			}
			bundle.putString("msg", "下载表单配置解释失败");
			msg.what=6;
			msg.setData(bundle);
			mHandler.sendMessage(msg);
			return true;
		}
		return false;
	}

	static boolean checkUpackage() {
		return false;
	}

	static boolean checkOffLineMap() {
		return false;
	}

	//初始资料变更检查
	private static void doReadMeXml(Context context,InputStream inStream) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();

			try {

				Document document = builder.parse(inStream);
				Element element = document.getDocumentElement();
				
				if (element.getElementsByTagName("sysconfig")!=null){  //name="系统配置" memo="" version="1.0" update="2015/05/26 15:49:00" 
					//NodeList locateNode = element.getElementsByTagName("sysconfig");
					NodeList sysNode = element.getElementsByTagName("sysconfig");
					for (int i = 0; i < sysNode.getLength(); i++) {
						if (sysNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
							Element childElement = (Element) sysNode.item(i);
							String name = childElement.getAttribute("name");
							String memo = childElement.getAttribute("memo");
							String version = childElement.getAttribute("version");
							String update = childElement.getAttribute("update");
							
							sysconfigWifi = childElement.getAttribute("wifi");
							sysconfigDataconn = childElement.getAttribute("dataconn");
							sysconfigFoce = childElement.getAttribute("foce");
							
							//从内存当中读取系统配置参数的缓存
							String sysconfig = getPreferencePara(context,"sysconfig");
							if ("".equals(sysconfig)){ //如果没有该参数记录,认为有改变，需要重新下载
								setPreferencePara(context,"sysconfig",name+","+memo+","+version+","+update,_NO_UP_FLAG);
								sysChange = true;
							}else{
								String values[] = sysconfig.split("\\,");
								if (Float.parseFloat(values[2]) < Float.parseFloat(version)){
									setPreferencePara(context,"sysconfig",name+","+memo+","+version+","+update,_NO_UP_FLAG);
									sysChange = true;
								}else{
									sysChange = false;
								}
							}
						}
					}
				}
				if (element.getElementsByTagName("xform") != null){//name="表单模板" memo="" version="1.0" update="2015/05/26 15:49:00"
					NodeList xformNode = element.getElementsByTagName("xform");
					for (int i = 0; i < xformNode.getLength(); i++) {
						if (xformNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
							Element childElement = (Element) xformNode.item(i);
							String name = childElement.getAttribute("name");
							String memo = childElement.getAttribute("memo");
							String version = childElement.getAttribute("version");
							String update = childElement.getAttribute("update");
							
							xformWifi = childElement.getAttribute("wifi");
							xformDataconn = childElement.getAttribute("dataconn");
							xformFoce = childElement.getAttribute("foce");
							
							//从内存当中读取系统配置参数的缓存
							String xformconfig = getPreferencePara(context,"xform");
							if ("".equals(xformconfig)){ //如果没有该参数记录,认为有改变，需要重新下载
								setPreferencePara(context,"xform",name+","+memo+","+version+","+update,_NO_UP_FLAG);
								xformChange = true;
							}else{
								String values[] = xformconfig.split("\\,");
								if (Float.parseFloat(values[2]) < Float.parseFloat(version)){
									setPreferencePara(context,"xform",name+","+memo+","+version+","+update,_NO_UP_FLAG);
									xformChange = true;
								}else{
									xformChange = false;
								}
							}
						}
					}
				}
				if (element.getElementsByTagName("upackage") != null){ //name="升级包" memo="" version="1.0" update="2015/05/26 15:49:00"
					NodeList upkgNode = element.getElementsByTagName("upackage");
					for (int i = 0; i < upkgNode.getLength(); i++) {
						if (upkgNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
							Element childElement = (Element) upkgNode.item(i);
							String name = childElement.getAttribute("name");
							String memo = childElement.getAttribute("memo");
							String version = childElement.getAttribute("version");
							String update = childElement.getAttribute("update");
							String minCode = childElement.getAttribute("minCode");
							
							upackageWifi = childElement.getAttribute("wifi");
							upackageDataconn = childElement.getAttribute("dataconn");
							upackageFoce = childElement.getAttribute("foce");
							
							//从内存当中读取系统配置参数的缓存
							String upkgconfig = getPreferencePara(context,"upackage");
							try{
								AppContext.getInstance().setAPP_MIN_CODE(Integer.parseInt(minCode));
							}catch(Exception e){
								AppContext.getInstance().setAPP_MIN_CODE(0);
							}
							if ("".equals(upkgconfig)){ //如果没有该参数记录,认为有改变，需要重新下载
								setPreferencePara(context,"upackage",name+","+memo+","+version+","+update,_NO_UP_FLAG);
								upackageChange = true;
							}else{
								String values[] = upkgconfig.split("\\,");
								if (Float.parseFloat(values[2]) < Float.parseFloat(version)){
									setPreferencePara(context,"upackage",name+","+memo+","+version+","+update,_NO_UP_FLAG);
									upackageChange = true;
								}else{
									upackageChange = false;
								}
							}
						}
					}
				}
				if (element.getElementsByTagName("offline") != null){
					listOfflineFile.removeAll(listOfflineFile);
					listOfflineName.removeAll(listOfflineName);
					listOfflineFileUpdate.removeAll(listOfflineFileUpdate);
					listOfflineNameUpdate.removeAll(listOfflineNameUpdate);
					listOfflineMsg.clear();
					NodeList offlineNode = element.getElementsByTagName("offline");
					int cnt=0;
					for (int i = 0; i < offlineNode.getLength(); i++) {
						if (offlineNode.item(i).getNodeType() == Node.ELEMENT_NODE) { // 判断当前结点是否是元素结点
							
							Element offlineElement = (Element) offlineNode
									.item(i);
							offlineWifi = offlineElement.getAttribute("wifi");
							offlineDataconn = offlineElement.getAttribute("dataconn");
							offlineFoce = offlineElement.getAttribute("foce");
							NodeList mapNode = offlineNode.item(i).getChildNodes();
							for(int j=0;j<mapNode.getLength();++j){
								if(mapNode.item(j).getNodeType() == Node.ELEMENT_NODE){
									//map name="汉阳地形图" file="hy.rar" memo="" version="1.0" update="2015/05/26 15:49:00"/
									Element mapElement = (Element) mapNode.item(j);
									String name = mapElement.getAttribute("name");
									listOfflineName.add(name);
									String file = mapElement.getAttribute("file");
									listOfflineFile.add(file);
									String memo = mapElement.getAttribute("memo");
									String version = mapElement.getAttribute("version");
									String update = mapElement.getAttribute("update");
									listOfflineMsg.put("offline_"+file, name+","+memo+","+version+","+update);
									String offlineconfig = getPreferencePara(context,"offline_"+file);
									if ("".equals(offlineconfig)){ //如果没有该参数记录,认为有改变，需要重新下载
										//setPreferencePara(context,"offline_"+file,name+","+memo+","+version+","+update,_NO_UP_FLAG);
										cnt++;
										listOfflineFileUpdate.add(file);
										listOfflineNameUpdate.add(name);
									}else{
										String values[] = offlineconfig.split("\\,");
										if (Float.parseFloat(values[2]) < Float.parseFloat(version)){
											//setPreferencePara(context,"offline_"+file,name+","+memo+","+version+","+update,_NO_UP_FLAG);
											cnt++;
											listOfflineFileUpdate.add(file);
											listOfflineNameUpdate.add(name);
										}
									}
								}
							}
							
						}
						offlineChange = (cnt>0?true:false);
					}
					System.out.println("offline");
				}
				if (element.getElementsByTagName("dictinfo") != null){
					NodeList dictNode = element.getElementsByTagName("dictinfo");
					for (int i = 0; i < dictNode.getLength(); i++) {
						if (dictNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
							Element childElement = (Element) dictNode.item(i);
							String name = childElement.getAttribute("name");
							String memo = childElement.getAttribute("memo");
							String version = childElement.getAttribute("version");
							String update = childElement.getAttribute("update");
							
							dictinfoWifi = childElement.getAttribute("wifi");
							dictinfoDataconn = childElement.getAttribute("dataconn");
							dictinfoFoce = childElement.getAttribute("foce");
							
							//从内存当中读取系统配置参数的缓存
							String dictconfig = getPreferencePara(context,"dictinfo");
							if ("".equals(dictconfig)){ //如果没有该参数记录,认为有改变，需要重新下载
								setPreferencePara(context,"dictinfo",name+","+memo+","+version+","+update,_NO_UP_FLAG);
								dictChange = true;
							}else{
								String values[] = dictconfig.split("\\,");
								if (Float.parseFloat(values[2]) < Float.parseFloat(version)){
									setPreferencePara(context,"dictinfo",name+","+memo+","+version+","+update,_NO_UP_FLAG);
									dictChange = true;
								}else{
									dictChange = false;
								}
							}
						}
					}
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		} catch (Exception ex) {

		}
	}
	
	
	public static String getPreferencePara(Context context,String param){
		SharedPreferences mySharedPreferences = context.getSharedPreferences(_SPF_NAME, 3); 
		return mySharedPreferences.getString(param, "");
	}
	
	public static void setPreferencePara(Context context,String param,String value,String flag){
		SharedPreferences mySharedPreferences = context.getSharedPreferences(_SPF_NAME, 3); 
		Editor pEditor = mySharedPreferences.edit();

		if (flag.equals(_NO_UP_FLAG)){
			value += _NO_UP_FLAG;
		}else if (flag.equals(_HAD_UP_FLAG)){
			value = mySharedPreferences.getString(param, "");
			value.replace(_NO_UP_FLAG, _HAD_UP_FLAG);
		}
		pEditor.putString(param, value).commit();
	}
	public static void rePreferencePara(Context context,String param){
		SharedPreferences mySharedPreferences = context.getSharedPreferences(_SPF_NAME, 3); 
		Editor pEditor = mySharedPreferences.edit();
		pEditor.remove(param);
		pEditor.commit();
	}
	
/*	public static boolean isWifi(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return networkInfo.isConnected();
	}*/
	public static String getOfflineFile() {
		return offlineFile;
	}
	public static void setOfflineFile(String offlineFile) {
		IniCheck.offlineFile = offlineFile;
	}
	
}
