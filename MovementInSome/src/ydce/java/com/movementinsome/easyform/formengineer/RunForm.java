/*
 * XmlGui application.
 * Written by Frank Ableson for IBM Developerworks
 * June 2010
 * Use the code as you wish -- no warranty of fitness, etc, etc.
 */
package com.movementinsome.easyform.formengineer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.util.DensityUtil;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.caice.async.PointEditAsync;
import com.movementinsome.caice.async.PointSubAsync;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.vo.PointCameraVO;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.database.vo.DynamicFormVO;
import com.movementinsome.database.vo.PicFileInfoVO;
import com.movementinsome.database.vo.ReturnMessage;
import com.movementinsome.easyform.ateps.view.AtepsView;
import com.movementinsome.easyform.util.XmlFiledRuleOperater;
import com.movementinsome.easyform.widgets.ExpGuiCamera;
import com.movementinsome.easyform.widgets.ExpGuiCheck;
import com.movementinsome.easyform.widgets.ExpGuiCheckList;
import com.movementinsome.easyform.widgets.ExpGuiChildrenTableList;
import com.movementinsome.easyform.widgets.ExpGuiDate;
import com.movementinsome.easyform.widgets.ExpGuiDateTime;
import com.movementinsome.easyform.widgets.ExpGuiEditBox;
import com.movementinsome.easyform.widgets.ExpGuiEditBoxCallUp;
import com.movementinsome.easyform.widgets.ExpGuiEditCodeBox;
import com.movementinsome.easyform.widgets.ExpGuiEditSelectBox;
import com.movementinsome.easyform.widgets.ExpGuiLocBtn;
import com.movementinsome.easyform.widgets.ExpGuiMapEditText;
import com.movementinsome.easyform.widgets.ExpGuiMapScreenshot;
import com.movementinsome.easyform.widgets.ExpGuiMultimedia;
import com.movementinsome.easyform.widgets.ExpGuiMultiselect;
import com.movementinsome.easyform.widgets.ExpGuiMutiLine;
import com.movementinsome.easyform.widgets.ExpGuiMutiLineSelect;
import com.movementinsome.easyform.widgets.ExpGuiNewVideoBox;
import com.movementinsome.easyform.widgets.ExpGuiPgTextBox;
import com.movementinsome.easyform.widgets.ExpGuiPickOne;
import com.movementinsome.easyform.widgets.ExpGuiPickOne2;
import com.movementinsome.easyform.widgets.ExpGuiPickOneS;
import com.movementinsome.easyform.widgets.ExpGuiPickOneZH;
import com.movementinsome.easyform.widgets.ExpGuiPickOneZS;
import com.movementinsome.easyform.widgets.ExpGuiPipeBroAnalysisBtn;
import com.movementinsome.easyform.widgets.ExpGuiPwd;
import com.movementinsome.easyform.widgets.ExpGuiRadio;
import com.movementinsome.easyform.widgets.ExpGuiTime;
import com.movementinsome.easyform.widgets.ExpGuiTitle;
import com.movementinsome.easyform.widgets.ExpGuiVoiceBox;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.kernel.util.MyDateTools;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class RunForm extends FullActivity {
	/** Called when the activity is first created. */
	String tag = RunForm.class.getName();
//	int RETURN_CODE = 141025;
	XmlGuiForm theForm;
	ProgressDialog progressDialog;
	Handler progressHandler;
	
	String id; //记录ID
	//模板
	String template = "";
	//传入表单参数
	HashMap iparams;
	// 传入字段下拉值
	HashMap options;
	String pid; //记录父ID

    //业务模板使用
	String taskNum = "";// 任务编号
	String taskCategory = "";// 表单类型
	String tableName = ""; //业务表名
	String guid = "";
	boolean isUpdate = false;
	boolean delete=false;// 是否删除数据
	boolean isChildrenTable=false;// 是不是子单
	String childrenTableUpdateAction;// 子单更新
	String childrendata;// 子单数据
	int childrenTablePosition;// 子单序号
	boolean isAddChildrenTable;// 是否添加子单 
	
	int opState = 0;//当前记录的状态0:浏览状态,1:编辑状态,2:已提交

	//记录值
	DynamicFormVO dynFormVo = null;
	ArrayList<DynamicFormVO> dynFormVos ;
	Dao<DynamicFormVO, Long> dynamicFormDao = null;
	ViewFlipper viewFlipper;
	// 步骤列表
	AtepsView atepsView;
	ExpGuiTitle expGuiTitle;
	boolean isRequired = true;
	boolean isShowDeleteBnt;

	private Bundle pointLineBundle;

	private ProjectVo projectVo;
	private SavePointVo savePointVo;
	private String facName;

	private boolean isPointLinkLine; 	//判断是不是点连线，是则需要修改上一个点的表号
	private Map<String,String> labelValueMap;

	private ExpGuiEditBox expGuiEditBox;
	private List<ExpGuiEditBox> expGuiEditBoxList;

	private BroadcastReceiver childReqReceiver = new BroadcastReceiver(){    
        public void onReceive(Context context, Intent intent) {          	
            String req = intent.getStringExtra("req");  
            String value = intent.getStringExtra("value");
			facName=intent.getStringExtra(OkHttpParam.FAC_NAME);
            String isRequiredStr= intent.getStringExtra("isRequired");
            if(isRequiredStr!=null){
            	if("true".equals(isRequiredStr)){
            		isRequired = true;
            	}else if("false".equals(isRequiredStr)){
            		isRequired = false;
            	}
            }
            
            if ("onChanged".equalsIgnoreCase(req)){
            	opState = 1;
            }else{
            	XmlFiledRuleOperater.ruleOperater(theForm, req, value);
            }
        }      
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		expGuiEditBoxList=new ArrayList<>();
		labelValueMap=new HashMap<>();
		//获取参数
		Intent startingIntent = getIntent();
		if (startingIntent == null) {
			Log.e(tag, "No Intent?  We're not supposed to be here...");
			finish();
			return;
		}
		// 注册广播
		IntentFilter filter = new IntentFilter();
		filter.addAction("RunForm");
		registerReceiver(childReqReceiver, filter);
		template = startingIntent.getStringExtra("template");
		isChildrenTable = startingIntent.getBooleanExtra("isChildrenTable", false);
		childrenTableUpdateAction = startingIntent.getStringExtra("childrenTableUpdateAction");
		childrendata = startingIntent.getStringExtra("childrendata");
		childrenTablePosition=startingIntent.getIntExtra("childrenTablePosition", 0);
		isAddChildrenTable=startingIntent.getBooleanExtra("isAddChildrenTable", false);
		
		//id = startingIntent.getStringExtra("id");//"a41b0e86-dcdd-4ae9-9972-2666429f9b70";
		id = startingIntent.getStringExtra("id");
		pid = startingIntent.getStringExtra("pid");
		taskNum = startingIntent.getStringExtra("taskNum");// 任务编号
		taskCategory = startingIntent.getStringExtra("taskCategory");// 表单类型
		tableName = startingIntent.getStringExtra("tableName");//业务表名
		delete = startingIntent.getBooleanExtra("delete", false);
		isUpdate = startingIntent.getBooleanExtra("isUpdate", false);
		iparams = (HashMap) startingIntent.getSerializableExtra("iParams");
		options = (HashMap) startingIntent.getSerializableExtra("options");
		guid = startingIntent.getStringExtra("guid");
		isShowDeleteBnt = startingIntent.getBooleanExtra("isShowDeleteBnt", true);
		isPointLinkLine = startingIntent.getBooleanExtra("isPointLinkLine", false);
		pointLineBundle=startingIntent.getBundleExtra("pointLineBundle");
		projectVo = (ProjectVo) startingIntent.getSerializableExtra("projectVo");
		savePointVo = (SavePointVo) startingIntent.getSerializableExtra(OkHttpParam.SAVEPOINTVO);

		//获取数据
		try {
			dynamicFormDao = AppContext.getInstance().getAppDbHelper().getDao(DynamicFormVO.class);
			if (id != null){
				if (isPointLinkLine){
					if (dynamicFormDao.queryForEq("id", id)!=null&&dynamicFormDao.queryForEq("id", id).size()>0){
						dynFormVo = dynamicFormDao.queryForEq("id", id).get(0);
						PointCameraVO pointCameraVO= JSON.parseObject(dynFormVo.getContent(),PointCameraVO.class);
						pointCameraVO.setFacName((String) iparams.get(OkHttpParam.FAC_NUM));
						pointCameraVO.setHappenAddr((String) iparams.get("happenAddr"));
						pointCameraVO.setImplementorName((String) iparams.get("implementorName"));
						pointCameraVO.setLongitudeWg84((String) iparams.get(OkHttpParam.LONGITUDE_WG84));
						pointCameraVO.setLatitudeWg84((String) iparams.get(OkHttpParam.LATITUDE_WG84));
						pointCameraVO.setCamera("");
						pointCameraVO.setMapx((String) iparams.get("mapx"));
						pointCameraVO.setMapy((String) iparams.get("mapy"));
						dynFormVo.setContent(new Gson().toJson(pointCameraVO));
					}
				}else {
					if (dynamicFormDao.queryForEq("id", id)!=null&&dynamicFormDao.queryForEq("id", id).size()>0){
						dynFormVo = dynamicFormDao.queryForEq("id", id).get(0);
					}
				}
			}else if (pid != null){
		        HashMap<String,Object> arg = new HashMap<String,Object>();
		        arg.put("pid", pid);
		        arg.put("form", template);
		        
		        List<DynamicFormVO> lst = dynamicFormDao.queryForFieldValues(arg);
		        if (lst.size()>0){
		        	dynFormVo = lst.get(0);
		        }
			}else{
				opState = 1;//进来为空记录,则当前表单为编辑状态
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//展示数据
		if (GetFormData(template,dynFormVo)) {
			DisplayForm();
		} else {
			Log.e(tag, "Couldn't parse the Form.");
			AlertDialog.Builder bd = new AlertDialog.Builder(this);
			AlertDialog ad = bd.create();
			ad.setTitle("Error");
			ad.setMessage("打开功能模块出错！");
			ad.show();
		}
		
		// 添加监听器

/*		theForm.setDataSetChangeListener(new DataSetSupervioer() {
            public void onChange() {
                // 填写一些前置操作，如更新数据
               // DisplayModel(); // 重新绘制
                // 填写一些后置操作，如更新状态
            }
        });*/
	}

	@Override
	protected void onDestroy() {
		// 注销位置追踪信息接收
		this.unregisterReceiver(childReqReceiver);
		for (int i = 0; i < expGuiEditBoxList.size(); i++) {
			EventBus.getDefault().unregister(expGuiEditBoxList.get(i));
		}
		super.onDestroy();
	}
	
	private void returnData4Parent(boolean isUploadTable){
    	HashMap<String,String> oParams = new HashMap();
    	String out = null;
    	try {
    		out = XmlFiledRuleOperater.getFormOutParams(this, template);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if (pid != null){
	    	if (out != null){
	    		for(String o:out.split("\\,")){
	    			if (!"".equals(o))
	    			    oParams.put(o, theForm.findField(theForm.findFieldByParam(o))==null?theForm.findParamDefValue(o):theForm.findField(theForm.findFieldByParam(o)).getValue());
	    		}
	    	}
	    	
	    	Intent intent = new Intent();
	    	intent.putExtra("taskNum",taskNum);// 任务编号
	    	intent.putExtra("taskCategory",taskCategory);// 表单类型
	    	intent.putExtra("tableName",tableName);//业务表名
	    	intent.putExtra("isUpdate",isUpdate);
	    	intent.putExtra("pid",pid);
	    	intent.putExtra("template",template);
	    	if(theForm.getJsonchoiceResults()!=null){
	    		intent.putExtra("pgDate",theForm.getJsonchoiceResults());
	    	}
	    	/*if(theForm.getAteps()!=null
	    			&&theForm.getAteps().size()>0){*/
	    		if(isUploadTable){
		    		intent.putExtra("uploadTable","1");
	    		}else{
	    			intent.putExtra("uploadTable","0");
	    		}
	    	//}
	    		if(theForm.getAteps()!=null
		    			&&theForm.getAteps().size()>0){
					int c=0;
					for(int i=0;i<theForm.getAteps().size();++i){
						if("2".equals(theForm.getAteps().get(i).getState())){
							++c;
						}
					}
					if(c==theForm.getAteps().size()){
						intent.putExtra("isFullyCommit","true");
					}
	    		}
	    	if (dynFormVo != null){
		    	if (dynFormVo.getStatus() == 1){
		    		intent.putExtra("opState", 1); //0:未提交,1:已提交
		    	}else {
		    		intent.putExtra("opState", 0); //0:未提交,1:已提交
		    	}
	    	}else{
	    		intent.putExtra("opState", 0); //0:未提交,1:已提交
	    	}
	    	intent.putExtra("oParams", oParams);
	    	setResult(R.string.return_code,intent);
    	}
    	if ( (pid==null||this.delete)&&(dynFormVo != null && dynFormVo.getStatus() == 1)){//如果不是上级业务调用并且已经数据已经上传成功，则删除记录
    		delete(dynFormVo);
    	}
	}
	
	private void delete(DynamicFormVO dynFormVo){
		try {
			String c = dynFormVo.getContent();
			try {
				JSONObject json = new JSONObject(c);
				String cameras =  json.getString("camera");
				if(cameras!=null){
					String[] p= cameras.split(",");
					for(int i=0;i<p.length;++i){
						File f= new File(p[i]);
						f.delete();
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dynamicFormDao.delete(dynFormVo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//保存记录
	private boolean saveOrUpdate(String content,String isedit) throws SQLException{
		
		if (isedit.equals(MapMeterMoveScope.ISEDIT)){		//编辑模式
			if (dynFormVo!=null){
				dynFormVo.setContent(content);
				dynFormVo.setUpdateDate(new Date());
				if (AppContext.getInstance().getCurLocation() != null){
					dynFormVo.setGpsCoord(AppContext.getInstance().getCurLocation().getCurGpsPosition());
					dynFormVo.setMapCoord(AppContext.getInstance().getCurLocation().getCurMapPosition());
				}
				dynFormVo.setStatus(0);
				dynamicFormDao.update(dynFormVo);
			}
		}else if (isedit.equals(MapMeterMoveScope.GATHER)){		//采集模式
			dynFormVo = new DynamicFormVO();
			dynFormVo.setId(UUID.randomUUID().toString());  //用GUID作为主要键，防止重装系统后上传到后台查询时候有记录重复
			dynFormVo.setPid("".equals(pid)?null:pid);
			dynFormVo.setImei(AppContext.getInstance().getPhoneIMEI());
			dynFormVo.setForm(template);
			dynFormVo.setVersion("1");
			dynFormVo.setCreateDate(new Date());
			dynFormVo.setUsId(AppContext.getInstance().getCurUser().getUserId());
			dynFormVo.setUsUsercode(AppContext.getInstance().getCurUser().getUserName());
			dynFormVo.setUsNameZh(AppContext.getInstance().getCurUser().getUserAlias());
			if (AppContext.getInstance().getCurLocation()!=null){
				dynFormVo.setGpsCoord(AppContext.getInstance().getCurLocation().getCurGpsPosition());
				dynFormVo.setMapCoord(AppContext.getInstance().getCurLocation().getCurMapPosition());
			}
			if(guid!=null){
				dynFormVo.setGuid(guid);
			}else{
				dynFormVo.setGuid(UUID.randomUUID().toString());
			}
			dynFormVo.setContent(content);
			dynFormVo.setStatus(0);
			dynFormVo.setParentTable(tableName);
			Long.valueOf(dynamicFormDao.create(dynFormVo));
			id = dynFormVo.getId();
		}
		opState = 0;//记录保存后为浏览状态
		return true;
	}
	
	//保存记录
	private boolean updateCommitStatus(int status,String msg) throws SQLException{
		
		if (dynFormVo != null) {
			dynFormVo.setStatus(status);
			dynFormVo.setMemo(msg);
			dynamicFormDao.update(dynFormVo);
			opState = status;
		}
		return true;
	}
/*	//读取记录值
	private void readFormValueById(String id){
		
	}
		
	//读取记录值
	private void readFormValueByPid(String pid){
		
	}*/
	

	private InputStream getXFormTemplate(String template){
		String fName = AppContext.getInstance().getAppConfigPath()+"xFormTemplate/"+template;
		File file = new File(fName);
		if (file.exists()){
			InputStream in = null;
			try {
				in = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return in;
		}else{
			try {
				return getResources().getAssets().open(template);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
	}
	
	/**
	 * 获取并组织数据
	 * @param template
	 * @param dynamicFormVO
	 * @return
	 */
	private boolean GetFormData(String template,DynamicFormVO dynamicFormVO) {
		try {
			org.json.JSONObject dataJson = null;
			if(isChildrenTable){
				if(childrendata!=null){
					dataJson = new org.json.JSONObject(childrendata); 
				}
			}else{
				if (dynamicFormVO != null){
					dataJson = new org.json.JSONObject(dynamicFormVO.getContent()); 
				}
			}
			
			InputStream is = getXFormTemplate(template);//getResources().getAssets().open(template);// url.openConnection().getInputStream();
			if (null == is){
				return false;
			}
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder db = factory.newDocumentBuilder();
			Document dom = db.parse(is);
			Element root = dom.getDocumentElement();
			NodeList forms = root.getElementsByTagName("form");
			if (forms.getLength() < 1) {
				// nothing here??
				//Log.e(tag, "No form, let's bail");
				return false;
			}
			Node form = forms.item(0);
			theForm = new XmlGuiForm();

			// process form level
			NamedNodeMap map = form.getAttributes();
			theForm.setFormNumber(map.getNamedItem("id").getNodeValue());
			theForm.setFormName(map.getNamedItem("name").getNodeValue());
			if (map.getNamedItem("submitTo") != null)
				theForm.setSubmitTo(map.getNamedItem("submitTo").getNodeValue());
			else
				theForm.setSubmitTo("loopback");
			theForm.setPage(map.getNamedItem("page").getNodeValue());
			theForm.setTable(map.getNamedItem("table").getNodeValue());
			//获取输入输出参数设置
			NodeList params = root.getElementsByTagName("param");
			if (params.getLength() > 0) {
				for (int i = 0; i < params.getLength(); i++) {
					Node paramNode = params.item(i);
					NamedNodeMap attrParam = paramNode.getAttributes();
					XmlGuiFormParam  tempParam = new XmlGuiFormParam();
					tempParam.setName(attrParam.getNamedItem("name").getNodeValue());
					tempParam.setField(attrParam.getNamedItem("field")==null?"":attrParam.getNamedItem("field").getNodeValue());
					tempParam.setType(attrParam.getNamedItem("type").getNodeValue());
					tempParam.setRequired(attrParam.getNamedItem("required").getNodeValue().equals("Y")?true:false);
					tempParam.setDefValue(attrParam.getNamedItem("defValue").getNodeValue());	
					tempParam.setIo(attrParam.getNamedItem("io").getNodeValue());
					theForm.getParams().add(tempParam);
				}
			}
			
			//获取规则信息
			NodeList rules = root.getElementsByTagName("rule");
			for(int i=0;i<rules.getLength();i++){
				Node ruleNode = rules.item(i);
				NamedNodeMap attrRule = ruleNode.getAttributes();
				
				XmlGuiRule tempRule = new XmlGuiRule();
				tempRule.setId(attrRule.getNamedItem("id").getNodeValue());
				
				NodeList dos = ruleNode.getChildNodes();
				for(int j=0;j<dos.getLength();j++){
					Node doNode = dos.item(j);
					if (doNode.getNodeType() == Node.ELEMENT_NODE) { // 判断当前结点是否是元素结点
						NamedNodeMap attrDo = doNode.getAttributes();
						tempRule.getDos().put(attrDo.getNamedItem("filed").getNodeValue(), attrDo.getNamedItem("value").getNodeValue());
					}
				}
				theForm.getRuls().add(tempRule);
			}
			
			// 获取分页信息
			NodeList pages = root.getElementsByTagName("page");
			for (int i = 0; i <pages.getLength(); i++){
				Node pageNode = pages.item(i);
				NamedNodeMap attrPage = pageNode.getAttributes();
				
				XmlGuiFormPage tempPage = new XmlGuiFormPage();
				tempPage.setPageNumber(attrPage.getNamedItem("id").getNodeValue());
				tempPage.setPageName(attrPage.getNamedItem("name").getNodeValue());
				// 获取所有的分组
				NodeList groups = pageNode.getChildNodes();
				for(int j = 0; j < groups.getLength();j++){
					Node groupNode = groups.item(j);
					if (groupNode.getNodeType() == Node.ELEMENT_NODE) { // 判断当前结点是否是元素结点
						NamedNodeMap attrGroup = groupNode.getAttributes();
						
						XmlGuiFormGroup tempGroup = new XmlGuiFormGroup();
						tempGroup.setGroupNumber(attrGroup.getNamedItem("id").getNodeValue());
						tempGroup.setGroupName(attrGroup.getNamedItem("name").getNodeValue());
						tempGroup.setDisplay(attrGroup.getNamedItem("display").getNodeValue().equals("Y")?true:false);
						tempGroup.setType(attrGroup.getNamedItem("type").getNodeValue());//common为这一般处理组，map为地图信息处理组

						// 获取所有的表单字段
						NodeList fields = groupNode.getChildNodes();
						for (int k = 0; k < fields.getLength(); k++) {
							Node fieldNode = fields.item(k);
							if (fieldNode.getNodeType() == Node.ELEMENT_NODE) { // 判断当前结点是否是元素结点
								NamedNodeMap attr = fieldNode.getAttributes();
								XmlGuiFormField tempField = new XmlGuiFormField();
								
								tempField.setGroupid(attrGroup.getNamedItem("id").getNodeValue());
								tempField.setDisplay(tempGroup.isDisplay());   //能不能显示，取解于分组的设置是否为显示
								tempField.setName(attr.getNamedItem("name").getNodeValue());
								tempField.setLabel(attr.getNamedItem("label").getNodeValue());
								tempField.setType(attr.getNamedItem("type").getNodeValue());
								if (attr.getNamedItem("required").getNodeValue().equals("Y"))
									tempField.setRequired(true);
								else
									tempField.setRequired(false);
								if (attr.getNamedItem("readOnly").getNodeValue().equals("Y"))
									tempField.setReadOnly(true);
								else
									tempField.setReadOnly(false);
								tempField.setMaxLength(attr.getNamedItem("maxLength")
										.getNodeValue().equals("") ? 0 : Integer.parseInt(attr
										.getNamedItem("maxLength").getNodeValue()));
								tempField.setDefValue(attr.getNamedItem("defValue").getNodeValue());
								if (attr.getNamedItem("options") != null){
									tempField.setOptions(attr.getNamedItem("options").getNodeValue());

								}
								if (attr.getNamedItem("listKey") != null){
									tempField.setListKey(attr.getNamedItem("listKey").getNodeValue());
									if (!"".equalsIgnoreCase(attr.getNamedItem("listKey").getNodeValue())){
										if(attr.getNamedItem("listTableName") != null
												&&!"".equalsIgnoreCase(attr.getNamedItem("listTableName").getNodeValue())){
											
										}else{
											tempField.setOptions(XmlFiledRuleOperater.getDicInfo(attr.getNamedItem("listKey").getNodeValue()));
										}
									}
								}
								if(attr.getNamedItem("qty")!=null){
									tempField.setQty(Integer.parseInt(attr.getNamedItem("qty").getNodeValue()));
								}
								if (attr.getNamedItem("rule") != null)
									tempField.setRule(attr.getNamedItem("rule").getNodeValue());
								if (attr.getNamedItem("childrenTableAction") != null)
									tempField.setChidrenTableAction(attr.getNamedItem("childrenTableAction").getNodeValue());
								if (attr.getNamedItem("childrenTableUpdateAction") != null)
									tempField.setChidrenTableUpdateAction(attr.getNamedItem("childrenTableUpdateAction").getNodeValue());
								if (attr.getNamedItem("template") != null)
									tempField.setTemplate(attr.getNamedItem("template").getNodeValue());
								if (attr.getNamedItem("tableName") != null){
									tempField.setTableName(attr.getNamedItem("tableName").getNodeValue());
								}
								//theForm.getFields().add(tempField);
								//如果为类型为“system",则为系统自定义处理信息,不作为显示使用,但是需要处理
								if (attr.getNamedItem("id") != null){
									tempField.setId(attr.getNamedItem("id").getNodeValue());
								}
								if (attr.getNamedItem("fId") != null){
									tempField.setfId(attr.getNamedItem("fId").getNodeValue());
								}
								if (attr.getNamedItem("cId") != null){
									tempField.setcId(attr.getNamedItem("cId").getNodeValue());
								}
								if (attr.getNamedItem("textId") != null){
									tempField.setTextId(attr.getNamedItem("textId").getNodeValue());
								}
								if (attr.getNamedItem("cTextId") != null){
									tempField.setcTextId(attr.getNamedItem("cTextId").getNodeValue());
								}
								if (attr.getNamedItem("fName") != null){
									tempField.setfName(attr.getNamedItem("fName").getNodeValue());
								}
								if (attr.getNamedItem("mName") != null){
									tempField.setmName(attr.getNamedItem("mName").getNodeValue());
								}
								if (attr.getNamedItem("cName") != null){
									tempField.setcName(attr.getNamedItem("cName").getNodeValue());
								}
								if (attr.getNamedItem("associatedControl") != null){
									tempField.setAssociatedControl(attr.getNamedItem("associatedControl").getNodeValue());
								}
								if(attr.getNamedItem("minNum") != null){
									String s = attr.getNamedItem("minNum").getNodeValue();
									if(!"".equals(s)){
										tempField.setMinNum(Integer.parseInt(s));
									}
								}
								if (attr.getNamedItem("bsCheck") != null){
									tempField.setBsCheck(attr.getNamedItem("bsCheck").getNodeValue());
								}
								if (attr.getNamedItem("bsIcsName") != null){
									tempField.setBsIcsName(attr.getNamedItem("bsIcsName").getNodeValue());
								}
								if (attr.getNamedItem("photographAction") != null){
									tempField.setPhotographAction(attr.getNamedItem("photographAction").getNodeValue());
								}
								
								if (attr.getNamedItem("dateId") != null){
									tempField.setDateId(attr.getNamedItem("dateId").getNodeValue());
								}
								if (attr.getNamedItem("timeId") != null){
									tempField.setTimeId(attr.getNamedItem("timeId").getNodeValue());
								}
								if (attr.getNamedItem("minorDateId") != null){
									tempField.setMinorDateId(attr.getNamedItem("minorDateId").getNodeValue());
								}
								if (attr.getNamedItem("minorTimeId") != null){
									tempField.setMinorTimeId(attr.getNamedItem("minorTimeId").getNodeValue());
								}
								
								if (dataJson != null){
									try{
										Object v=dataJson.get(tempField.getName());
										if(v!=null){
											tempField.setValue(v+"");
										}else{
											tempField.setValue(null);
										}
									}catch(org.json.JSONException ex){
										tempField.setValue(null);
									}
								}

								//在这里动态的添加表单的显示的中文以及对应的数据库字段
								labelValueMap.put(tempField.getName(),tempField.getLabel());

								tempGroup.getFields().add(tempField);
							}
						}
						tempPage.getGroups().add(tempGroup);
					}
				}
				theForm.getPages().add(tempPage);
			}
			// 步骤列表
			NodeList atepss = root.getElementsByTagName("ateps");
			for (int i = 0; i <atepss.getLength(); i++){
				Node atepsNode = atepss.item(i);
				NamedNodeMap attrAteps = atepsNode.getAttributes();
				XmlGuiFormAteps xmlGuiFormAteps=new XmlGuiFormAteps();
				xmlGuiFormAteps.setGroupid(attrAteps.getNamedItem("groupid").getNodeValue());
				xmlGuiFormAteps.setName(attrAteps.getNamedItem("name").getNodeValue());
				xmlGuiFormAteps.setState(attrAteps.getNamedItem("state").getNodeValue());
				if(attrAteps.getNamedItem("startState")!=null){
					xmlGuiFormAteps.setStartState(attrAteps.getNamedItem("startState").getNodeValue());
				}
				if(attrAteps.getNamedItem("finishState")!=null){
					xmlGuiFormAteps.setFinishState(attrAteps.getNamedItem("finishState").getNodeValue());
				}
				if (dataJson != null){
					try{
						xmlGuiFormAteps.setState(dataJson.getString(xmlGuiFormAteps.getGroupid()));
					}catch(org.json.JSONException ex){
						xmlGuiFormAteps.setState("0");
					}
				}
				theForm.getAteps().add(xmlGuiFormAteps);
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 生成页面
	 * @return
	 */
	private boolean DisplayForm() {

		try {
			expGuiTitle=new ExpGuiTitle(this, theForm.getFormName());
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( 
					ViewGroup.LayoutParams.MATCH_PARENT, 
					ViewGroup.LayoutParams.MATCH_PARENT);
			RelativeLayout.LayoutParams paramsRll = new RelativeLayout.LayoutParams( 
					ViewGroup.LayoutParams.MATCH_PARENT, 
					ViewGroup.LayoutParams.MATCH_PARENT);
			viewFlipper=new ViewFlipper(this);
			viewFlipper.setLayoutParams(params);
			// 表单页面
			LinearLayout llOut=new LinearLayout(this);
			
			llOut.setOrientation(LinearLayout.VERTICAL);
			llOut.setLayoutParams(params);
			llOut.addView(expGuiTitle);
			
			RelativeLayout rl = new RelativeLayout(this);
			rl.setLayoutParams(params);
			final LinearLayout ll = new LinearLayout(this);
			ll.setBackgroundResource(R.color.white);
			ll.setLayoutParams(params);
			ll.setPadding(10, 10, 10, 30);
			
			ll.setOrientation(android.widget.LinearLayout.VERTICAL);
			// walk thru our form elements and dynamically create them,
			// leveraging our mini library of tools.
			//int i;
			List<Map<String, Object>>data=new ArrayList<Map<String,Object>>();
			int fields=0;
			for (int k = 0; k <theForm.getPages().size(); k++){
				//分布晢时不处理
				XmlGuiFormPage page = theForm.getPages().get(k);
				
				for(int j = 0; j < page.getGroups().size(); j++){
					
					XmlGuiFormGroup group = page.getGroups().get(j);
					TextView label = new TextView(this);
					SpannableStringBuilder style=new SpannableStringBuilder(group.getGroupName());       
				    style.setSpan(new BackgroundColorSpan(Color.BLUE),0,group.getGroupName().length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					//label.setText(group.getGroupName());
					
				    style.setSpan(new BackgroundColorSpan(Color.WHITE),0,group.getGroupName().length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					//label.setText(group.getGroupName());
				    label.setTextColor(Color.BLACK);
				    label.setHeight(DensityUtil.dip2px(this, 45));
				    label.setGravity(Gravity.CENTER_VERTICAL);
				    //广水项目的
				    label.setText(style); 
				    
					LinearLayout llGroup = new LinearLayout(this);
					
					theForm.setAtapsData(llGroup, page.getGroups().get(j).getGroupNumber());
					
					if (!group.isDisplay()){
						llGroup.setVisibility(View.GONE);
					}else{
						ll.addView(llGroup);
					}
					llGroup.setOrientation(android.widget.LinearLayout.VERTICAL);
					
					// llGroup.addView(label);
			
					for (int i = 0; i < group.getFields().size(); i++) {	
						//if (tempField.getType().equals("system")) {
						++fields;
						if (dynFormVo == null){
							XmlFiledRuleOperater.sysTemFieldOp(iparams,group.getFields().elementAt(i));
						}
						if(options!=null){
							XmlFiledRuleOperater.setSysTemFieldOptions(options, group.getFields().elementAt(i));
						}
						LinearLayout spacing = new LinearLayout(this);
						spacing.setLayoutParams(new LinearLayout.LayoutParams( 
								ViewGroup.LayoutParams.MATCH_PARENT, 10));
						llGroup.addView(spacing);
						String associatedControl=group.getFields().elementAt(i).getAssociatedControl();
						if (group.getFields().elementAt(i).getType().equals("text")) {
							if (associatedControl != null && !associatedControl.equals("")) {
								expGuiEditBox = new ExpGuiEditBox(this,
										(group.getFields().elementAt(i).isRequired() ? "*"
												: "")
												+ group.getFields().elementAt(i).getLabel(),
										"", group.getFields().elementAt(i).getValue(),
										group.getFields().elementAt(i).getRule() == null ? ""
												: group.getFields().elementAt(i).getRule()
										, group.getFields().elementAt(i).isReadOnly()
										, group.getFields().elementAt(i).getTableName()
										, group.getFields().elementAt(i).getId()
										, group.getFields().elementAt(i).getAssociatedControl()
								);
								expGuiEditBoxList.add(expGuiEditBox);
								llGroup.addView(expGuiEditBox);
							} else {
								group.getFields().elementAt(i).obj = new ExpGuiEditBox(this,
										(group.getFields().elementAt(i).isRequired() ? "*"
												: "")
												+ group.getFields().elementAt(i).getLabel(),
										"", group.getFields().elementAt(i).getValue(),
										group.getFields().elementAt(i).getRule() == null ? ""
												: group.getFields().elementAt(i).getRule()
										, group.getFields().elementAt(i).isReadOnly()
										, group.getFields().elementAt(i).getTableName()
										, group.getFields().elementAt(i).getId()
										, group.getFields().elementAt(i).getAssociatedControl()
								);
								llGroup.addView((View) group.getFields().elementAt(i).obj);
							}
						}
						if (group.getFields().elementAt(i).getType().equals("textCallUp")) {
							group.getFields().elementAt(i).obj = new ExpGuiEditBoxCallUp(this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
											"",group.getFields().elementAt(i).getValue(),
											group.getFields().elementAt(i).getRule() == null?""
													:group.getFields().elementAt(i).getRule()
													,group.getFields().elementAt(i).isReadOnly());
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						}else if (group.getFields().elementAt(i).getType().equals("memo")) {
							group.getFields().elementAt(i).obj = new ExpGuiMutiLine(this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
									"",group.getFields().elementAt(i).getValue(),
									group.getFields().elementAt(i).getRule() == null?""
											:group.getFields().elementAt(i).getRule()
											,group.getFields().elementAt(i).isReadOnly());
							// ((XmlGuiMutiLine)group.getFields().elementAt(i).obj).makeNumeric();
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						} else if (group.getFields().elementAt(i).getType().equals("password")) {
							group.getFields().elementAt(i).obj = new ExpGuiPwd(this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
									"",group.getFields().elementAt(i).getValue(),
									group.getFields().elementAt(i).getRule() == null?""
											:group.getFields().elementAt(i).getRule()
											,group.getFields().elementAt(i).isReadOnly());
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						} else if (group.getFields().elementAt(i).getType().equals("numeric")) {
							if (associatedControl != null && !associatedControl.equals("")) {
								expGuiEditBox = new ExpGuiEditBox(this,
										(group.getFields().elementAt(i).isRequired() ? "*"
												: "")
												+ group.getFields().elementAt(i).getLabel(),
										"", group.getFields().elementAt(i).getValue(),
										group.getFields().elementAt(i).getRule() == null ? ""
												: group.getFields().elementAt(i).getRule()
										, group.getFields().elementAt(i).isReadOnly()
										, group.getFields().elementAt(i).getTableName()
										, group.getFields().elementAt(i).getId()
										, group.getFields().elementAt(i).getAssociatedControl()
								);
								expGuiEditBoxList.add(expGuiEditBox);
								expGuiEditBox
										.makeNumeric();
								llGroup.addView(expGuiEditBox);
							} else {
								group.getFields().elementAt(i).obj = new ExpGuiEditBox(this,
										(group.getFields().elementAt(i).isRequired() ? "*"
												: "")
												+ group.getFields().elementAt(i).getLabel(),
										"", group.getFields().elementAt(i).getValue(),
										group.getFields().elementAt(i).getRule() == null ? ""
												: group.getFields().elementAt(i).getRule()
										, group.getFields().elementAt(i).isReadOnly()
										, group.getFields().elementAt(i).getTableName()
										, group.getFields().elementAt(i).getId()
										, group.getFields().elementAt(i).getAssociatedControl()
								);
								((ExpGuiEditBox) group.getFields().elementAt(i).obj)
										.makeNumber();
								llGroup.addView((View) group.getFields().elementAt(i).obj);
							}
						} else if (group.getFields().elementAt(i).getType().equals("number")) {
							if (associatedControl != null && !associatedControl.equals("")) {
								expGuiEditBox = new ExpGuiEditBox(this,
										(group.getFields().elementAt(i).isRequired() ? "*"
												: "")
												+ group.getFields().elementAt(i).getLabel(),
										"", group.getFields().elementAt(i).getValue(),
										group.getFields().elementAt(i).getRule() == null ? ""
												: group.getFields().elementAt(i).getRule()
										, group.getFields().elementAt(i).isReadOnly()
										, group.getFields().elementAt(i).getTableName()
										, group.getFields().elementAt(i).getId()
										, group.getFields().elementAt(i).getAssociatedControl()
								);
								expGuiEditBoxList.add(expGuiEditBox);
								expGuiEditBox
										.makeNumeric();
								llGroup.addView(expGuiEditBox);
							} else {
								group.getFields().elementAt(i).obj = new ExpGuiEditBox(this,
										(group.getFields().elementAt(i).isRequired() ? "*"
												: "")
												+ group.getFields().elementAt(i).getLabel(),
										"", group.getFields().elementAt(i).getValue(),
										group.getFields().elementAt(i).getRule() == null ? ""
												: group.getFields().elementAt(i).getRule()
										, group.getFields().elementAt(i).isReadOnly()
										, group.getFields().elementAt(i).getTableName()
										, group.getFields().elementAt(i).getId()
										, group.getFields().elementAt(i).getAssociatedControl()
								);
								((ExpGuiEditBox) group.getFields().elementAt(i).obj)
										.makeNumber();
								llGroup.addView((View) group.getFields().elementAt(i).obj);
							}
						} else if (group.getFields().elementAt(i).getType().equals("radio")) {
							group.getFields().elementAt(i).obj = new ExpGuiRadio(this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
									group.getFields().elementAt(i).getOptions(),group.getFields().elementAt(i).getValue(),
									group.getFields().elementAt(i).getRule() == null?""
											:group.getFields().elementAt(i).getRule()
											,group.getFields().elementAt(i).isReadOnly());
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						}else if(group.getFields().elementAt(i).getType().equals("new_video")){
							group.getFields().elementAt(i).obj = new ExpGuiNewVideoBox(this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
									"",group.getFields().elementAt(i).getValue(),
									group.getFields().elementAt(i).getRule() == null?""
											:group.getFields().elementAt(i).getRule()
									,group.getFields().elementAt(i).isReadOnly()
									,group.getFields().elementAt(i).getTableName()
									,group.getFields().elementAt(i).getId());
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						} else if (group.getFields().elementAt(i).getType().equals("voice")) {
							group.getFields().elementAt(i).obj = new ExpGuiVoiceBox(this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
											"",group.getFields().elementAt(i).getValue(),
											group.getFields().elementAt(i).getRule() == null?""
													:group.getFields().elementAt(i).getRule()
													,group.getFields().elementAt(i).isReadOnly()
													,group.getFields().elementAt(i).getTableName()
													,group.getFields().elementAt(i).getId());
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						}else if (group.getFields().elementAt(i).getType().equals("camera")) {
							group.getFields().elementAt(i).obj = new ExpGuiCamera(this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
									group.getFields().elementAt(i).getOptions(),group.getFields().elementAt(i).getValue(),
									group.getFields().elementAt(i).getRule() == null?""
											:group.getFields().elementAt(i).getRule(),group.getFields().elementAt(i).getLabel()
											,group.getFields().elementAt(i).getQty()
											,group.getFields().elementAt(i).isReadOnly(),group.getFields().elementAt(i).getPhotographAction());
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						} else if (group.getFields().elementAt(i).getType().equals("mapScreenshot")) {
							group.getFields().elementAt(i).obj = new ExpGuiMapScreenshot(this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
									group.getFields().elementAt(i).getOptions(),group.getFields().elementAt(i).getValue(),
									group.getFields().elementAt(i).getRule() == null?""
											:group.getFields().elementAt(i).getRule(),group.getFields().elementAt(i).getLabel()
											,group.getFields().elementAt(i).getQty()
											,group.getFields().elementAt(i).isReadOnly());
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						} else if (group.getFields().elementAt(i).getType().equals("check")) {
							group.getFields().elementAt(i).obj = new ExpGuiCheck(this,
							(group.getFields().elementAt(i).isRequired() ? "*"
									: "")
									+ group.getFields().elementAt(i).getLabel(),
							group.getFields().elementAt(i).getOptions(),group.getFields().elementAt(i).getValue(),
							group.getFields().elementAt(i).getRule() == null?""
									:group.getFields().elementAt(i).getRule()
									,group.getFields().elementAt(i).isReadOnly());
							/*group.getFields().elementAt(i).obj =new ExpGuiMultiselect(this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
									group.getFields().elementAt(i).getOptions());*/
							llGroup.addView((View) group.getFields().elementAt(i).obj);

						}else if (group.getFields().elementAt(i).getType().equals("checklist")) {
							group.getFields().elementAt(i).obj = new ExpGuiCheckList(this,
									group.getFields().elementAt(i).getOptions(),group.getFields().elementAt(i).getValue(),
									group.getFields().elementAt(i).getRule() == null?""
											:group.getFields().elementAt(i).getRule()
											,group.getFields().elementAt(i).isReadOnly(),group.getFields().elementAt(i).getBsIcsName()
											,group.getFields().elementAt(i).getBsCheck());
									
									llGroup.addView((View) group.getFields().elementAt(i).obj);

						} else if (group.getFields().elementAt(i).getType().equals("mselect")){
							group.getFields().elementAt(i).obj =new ExpGuiMultiselect(llGroup,this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
									group.getFields().elementAt(i).getOptions(),group.getFields().elementAt(i).getValue(),
									group.getFields().elementAt(i).getRule() == null?""
											:group.getFields().elementAt(i).getRule()
											,group.getFields().elementAt(i).isReadOnly()
											,group.getFields().elementAt(i).getId()
											,group.getFields().elementAt(i).getfId()
											,group.getFields().elementAt(i).getfName()
											,group.getFields().elementAt(i).getcName()
											,group.getFields().elementAt(i).getmName());
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						} else if (group.getFields().elementAt(i).getType().equals("time")) {
							group.getFields().elementAt(i).obj = new ExpGuiTime(this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
									group.getFields().elementAt(i).getOptions(),group.getFields().elementAt(i).getValue(),
									group.getFields().elementAt(i).getRule() == null?""
											:group.getFields().elementAt(i).getRule()
											,group.getFields().elementAt(i).isReadOnly());
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						} else if (group.getFields().elementAt(i).getType().equals("date")) {
							group.getFields().elementAt(i).obj = new ExpGuiDate(this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
									group.getFields().elementAt(i).getOptions(),group.getFields().elementAt(i).getValue(),
									group.getFields().elementAt(i).getRule() == null?""
											:group.getFields().elementAt(i).getRule()
											,group.getFields().elementAt(i).isReadOnly());
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						} else if (group.getFields().elementAt(i).getType().equals("datetime")) {
							group.getFields().elementAt(i).obj = new ExpGuiDateTime(ll,this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
									group.getFields().elementAt(i).getOptions(),group.getFields().elementAt(i).getValue(),
									group.getFields().elementAt(i).getRule() == null?""
											:group.getFields().elementAt(i).getRule()
											,group.getFields().elementAt(i).isReadOnly(),group.getFields().elementAt(i).getPhotographAction()
											,group.getFields().elementAt(i).getDateId()
											,group.getFields().elementAt(i).getTimeId()
											,group.getFields().elementAt(i).getMinorDateId()
											,group.getFields().elementAt(i).getMinorTimeId());
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						} else if (group.getFields().elementAt(i).getType().equals("choice")) {
							group.getFields().elementAt(i).obj = new ExpGuiPickOne(llGroup,this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
									group.getFields().elementAt(i).getOptions(),group.getFields().elementAt(i).getValue(),
									group.getFields().elementAt(i).getRule() == null?""
											:group.getFields().elementAt(i).getRule()
											,group.getFields().elementAt(i).isReadOnly()
											,group.getFields().elementAt(i).getId(),group.getFields().elementAt(i).getfId()
											,group.getFields().elementAt(i).getcId(),group.getFields().elementAt(i).getfName()
											,group.getFields().elementAt(i).getcName()
											,group.getFields().elementAt(i).getmName()
											,group.getFields().elementAt(i).getName()
							);
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						} else if (group.getFields().elementAt(i).getType().equals("choicezh")) {
							group.getFields().elementAt(i).obj = new ExpGuiPickOneZH(llGroup,this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
									group.getFields().elementAt(i).getOptions(),group.getFields().elementAt(i).getValue(),
									group.getFields().elementAt(i).getRule() == null?""
											:group.getFields().elementAt(i).getRule()
											,group.getFields().elementAt(i).isReadOnly()
											,group.getFields().elementAt(i).getId(),group.getFields().elementAt(i).getfId()
											,group.getFields().elementAt(i).getcId(),group.getFields().elementAt(i).getfName()
											,group.getFields().elementAt(i).getcName()
											,group.getFields().elementAt(i).getmName());
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						} else if (group.getFields().elementAt(i).getType().equals("pgtext")) {
							group.getFields().elementAt(i).obj = new ExpGuiPgTextBox(this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
											"",group.getFields().elementAt(i).getValue(),
											group.getFields().elementAt(i).getRule() == null?""
													:group.getFields().elementAt(i).getRule()
													,group.getFields().elementAt(i).isReadOnly()
													,group.getFields().elementAt(i).getTableName()
													,group.getFields().elementAt(i).getId()
													,theForm);
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						} else if (group.getFields().elementAt(i).getType().equals("choice_zs")) {
							group.getFields().elementAt(i).obj = new ExpGuiPickOneZS(ll,this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
									group.getFields().elementAt(i).getOptions(),group.getFields().elementAt(i).getValue(),
									group.getFields().elementAt(i).getRule() == null?""
											:group.getFields().elementAt(i).getRule()
											,group.getFields().elementAt(i).isReadOnly()
											,group.getFields().elementAt(i).getId(),group.getFields().elementAt(i).getfId()
											,group.getFields().elementAt(i).getcId(),group.getFields().elementAt(i).getfName()
											,group.getFields().elementAt(i).getcName()
											,group.getFields().elementAt(i).getmName());
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						}else if (group.getFields().elementAt(i).getType().equals("choices")) {
							group.getFields().elementAt(i).obj = new ExpGuiPickOneS(ll,this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
									group.getFields().elementAt(i).getOptions(),group.getFields().elementAt(i).getValue(),
									group.getFields().elementAt(i).getRule() == null?""
											:group.getFields().elementAt(i).getRule()
											,group.getFields().elementAt(i).isReadOnly()
											,group.getFields().elementAt(i).getId(),group.getFields().elementAt(i).getfId()
											,group.getFields().elementAt(i).getcId(),group.getFields().elementAt(i).getfName()
											,group.getFields().elementAt(i).getcName());
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						}else if (group.getFields().elementAt(i).getType().equals("choice2")) {
							group.getFields().elementAt(i).obj = new ExpGuiPickOne2(llGroup,this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
									group.getFields().elementAt(i).getOptions(),group.getFields().elementAt(i).getValue(),
									group.getFields().elementAt(i).getRule() == null?""
											:group.getFields().elementAt(i).getRule()
											,group.getFields().elementAt(i).isReadOnly()
											,group.getFields().elementAt(i).getId(),group.getFields().elementAt(i).getfId()
											,group.getFields().elementAt(i).getcId(),group.getFields().elementAt(i).getfName()
											,group.getFields().elementAt(i).getcName()
											,group.getFields().elementAt(i).getmName()
											,group.getFields().elementAt(i).getcTextId());
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						} else if (group.getFields().elementAt(i).getType().equals("pipeBroAnalysisBtn")) {
							group.getFields().elementAt(i).obj = new ExpGuiPipeBroAnalysisBtn(this, group.getFields().elementAt(i).getLabel(),
									group.getFields().elementAt(i).getValue());
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						} else if (group.getFields().elementAt(i).getType().equals("maptext")) {
							group.getFields().elementAt(i).obj = new ExpGuiMapEditText(this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
									group.getFields().elementAt(i).getDefValue(),group.getFields().elementAt(i).getValue(),
									group.getFields().elementAt(i).getRule() == null?""
											:group.getFields().elementAt(i).getRule()
											,group.getFields().elementAt(i).isReadOnly());
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						} else if (group.getFields().elementAt(i).getType().equals("childrentable")) {
							group.getFields().elementAt(i).obj = new ExpGuiChildrenTableList(this, 
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(), 
											group.getFields().elementAt(i).getValue(), 
											group.getFields().elementAt(i).getChidrenTableAction(), 
											group.getFields().elementAt(i).getChidrenTableUpdateAction(),
											group.getFields().elementAt(i).getTemplate()
											,group.getFields().elementAt(i).isReadOnly()
											,taskNum==null?"":taskNum);
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						} else if (group.getFields().elementAt(i).getType().equals("edit_check")) {
							group.getFields().elementAt(i).obj = new ExpGuiEditSelectBox(this,
									(group.getFields().elementAt(i).isRequired() ? "*"
											: "")
											+ group.getFields().elementAt(i).getLabel(),
											"",
											group.getFields().elementAt(i).getValue(),
									group.getFields().elementAt(i).getOptions(),
									group.getFields().elementAt(i).getRule() == null?""
											:group.getFields().elementAt(i).getRule()
											,group.getFields().elementAt(i).isReadOnly());
									
									llGroup.addView((View) group.getFields().elementAt(i).obj);

						 } else if (group.getFields().elementAt(i).getType().equals("memo_check")) {
								group.getFields().elementAt(i).obj = new ExpGuiMutiLineSelect(llGroup,this,
										(group.getFields().elementAt(i).isRequired() ? "*"
												: "")
												+ group.getFields().elementAt(i).getLabel(),
												"",
												group.getFields().elementAt(i).getValue(),
										group.getFields().elementAt(i).getOptions(),
										group.getFields().elementAt(i).getRule() == null?""
												:group.getFields().elementAt(i).getRule()
												,group.getFields().elementAt(i).isReadOnly()
												,group.getFields().elementAt(i).getId()
												,group.getFields().elementAt(i).getfId()
												,group.getFields().elementAt(i).getfName()
												,group.getFields().elementAt(i).getcName()
												,group.getFields().elementAt(i).getmName()
												,group.getFields().elementAt(i).getTextId());
										
										llGroup.addView((View) group.getFields().elementAt(i).obj);

							 } else if (group.getFields().elementAt(i).getType().equals("map_loc")) {
							 group.getFields().elementAt(i).obj = new ExpGuiLocBtn(this
									 , group.getFields().elementAt(i).getLabel()
									 , group.getFields().elementAt(i).getValue()
									 , group.getFields().elementAt(i).getQty()
									 ,group.getFields().elementAt(i).isReadOnly());
							 llGroup.addView((View) group.getFields().elementAt(i).obj);
						 }else if(group.getFields().elementAt(i).getType().equals("code")){
							 group.getFields().elementAt(i).obj = new ExpGuiEditCodeBox(this,
										(group.getFields().elementAt(i).isRequired() ? "*"
												: "")
												+ group.getFields().elementAt(i).getLabel(),
												"",group.getFields().elementAt(i).getValue(),
												group.getFields().elementAt(i).getRule() == null?""
														:group.getFields().elementAt(i).getRule()
														,group.getFields().elementAt(i).isReadOnly());
							llGroup.addView((View) group.getFields().elementAt(i).obj);
						 }else if(group.getFields().elementAt(i).getType().equals("multimedia")){
							 group.getFields().elementAt(i).obj = new ExpGuiMultimedia(this,group.getFields().elementAt(i).getValue());
							 RelativeLayout.LayoutParams paramsRl = new RelativeLayout.LayoutParams( 
										ViewGroup.LayoutParams.MATCH_PARENT, 
										ViewGroup.LayoutParams.WRAP_CONTENT);
							 paramsRl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
							((View)group.getFields().elementAt(i).obj).setLayoutParams(paramsRl);
							((View)group.getFields().elementAt(i).obj).setId(R.id.id_20001);
							rl.addView((View)group.getFields().elementAt(i).obj);
							paramsRll.addRule(RelativeLayout.ABOVE, ((View)group.getFields().elementAt(i).obj).getId());
						 }
					}
					
				}
			}
			ViewGroup sv = new LinearLayout(this);
			if(fields>4){
				sv=new ScrollView(this);
			}
			rl.setBackgroundResource(R.drawable.bg);
			sv.setLayoutParams(paramsRll);
			sv.setPadding(0, 10, 0, 60);
			sv.addView(ll);
			rl.addView(sv);
			llOut.addView(rl);
			if(theForm.getAteps().size()>0){
				/*int c=0;
				for(int i=0;i<theForm.getAteps().size();++i){
					if("2".equals(theForm.getAteps().get(i).getState())){
						++c;
					}
				}*/
				atepsView=new AtepsView();
				viewFlipper.addView(atepsView.getAtepsView(this , viewFlipper , theForm , theForm.getFormName(),taskCategory,tableName,taskNum));
				atepsView.getAtapsCommit().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//如果处理于编辑状态需要先保存
						//if (opState == 1){
							// just display the results to the screen
							saveForm(MapMeterMoveScope.ISEDIT);
						//}
						if (!submitForm(true)) {
							return;
						}
					}
				});
				atepsView.getAteps_back().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						returnData4Parent(false);
						finish();
					}
				});
			}
			viewFlipper.addView(llOut);
			

			expGuiTitle.getBackBtn().setOnClickListener(new OnClickListener() {// 返回
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finishForm();
				}
			});
//			if(isChildrenTable){
//				expGuiTitle.getCommitBtn().setVisibility(View.GONE);
//			}else{
//				expGuiTitle.getCommitBtn().setVisibility(View.VISIBLE);
//			}
				expGuiTitle.getCommitBtn().setVisibility(View.GONE);
//			expGuiTitle.getCommitBtn().setOnClickListener(new OnClickListener() {//提交
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					if(isRequired){
//						if (!CheckForm()) {
//							AlertDialog.Builder bd = new AlertDialog.Builder(ll
//									.getContext());
//							AlertDialog ad = bd.create();
//							ad.setTitle("错误");
//							ad.setMessage("请完整录入所有带(*)的内容");
//							ad.show();
//							return;
//
//						}
//						if(!checkPictureCount()){
//							AlertDialog.Builder bd = new AlertDialog.Builder(ll
//									.getContext());
//							AlertDialog ad = bd.create();
//							ad.setTitle("错误");
//							ad.setMessage("照片数量不够");
//							ad.show();
//							return;
//						}
//						if(!checkDateTime()){
//							AlertDialog.Builder bd = new AlertDialog.Builder(RunForm.this);
//							AlertDialog ad = bd.create();
//							ad.setTitle("错误");
//							ad.setMessage("时间不正确，请查看填写的时间");
//							ad.show();
//							return;
//						}
//					}
//					//如果处理于编辑状态需要先保存
//					//if (opState == 1){
//						// just display the results to the screen
//						saveForm();
//				//	}
//					if (!submitForm(true)) {
//						return;
//					}
//				}
//			});
//			if(isShowDeleteBnt){
//				expGuiTitle.getSaveBtn().setVisibility(View.VISIBLE);
//			}else{
//				expGuiTitle.getSaveBtn().setVisibility(View.GONE);
//			}

			if (savePointVo!=null){
				if (savePointVo.getUserId().equals(AppContext.getInstance().getCurUser().getUserId())){
					if (projectVo !=null){
						if (projectVo.getProjectStatus().equals("2")){			//已完成工程
							expGuiTitle.getSaveBtn().setVisibility(View.GONE);		//隐藏保存按钮
						}else {
							expGuiTitle.getSaveBtn().setVisibility(View.VISIBLE);		//显示保存按钮
						}
					}
				}else {
					expGuiTitle.getSaveBtn().setVisibility(View.GONE);		//隐藏保存按钮
				}
			}


			expGuiTitle.getSaveBtn().setOnClickListener(new OnClickListener() {// 保存
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// check if this form is Valid

					if (!CheckForm()) {
						AlertDialog.Builder bd = new AlertDialog.Builder(ll
								.getContext());
						AlertDialog ad = bd.create();
						ad.setTitle("错误");
						ad.setMessage("请完整录入所有带(*)的内容");
						ad.show();
						return;

					}

                    String drawType = "";
                    String isedit = "";
                    if (iparams != null) {
                        drawType = (String) iparams.get("drawType");
                        isedit = (String) iparams.get("isEdit");
                    }

                    try {
                        Dao<SavePointVo, Long> savePointVoLongDao =
                                AppContext.getInstance().getAppDbHelper().getDao(SavePointVo.class);
//                        Dao<SaveLineVo, Long> saveLineVoLongDao = AppContext.getInstance().getAppDbHelper().getDao(SaveLineVo.class);
                        if (facName != null && !facName.equals("")) {
                            List<SavePointVo> savePointVos = savePointVoLongDao.queryForEq("facName", facName);
                            if (savePointVos != null && savePointVos.size() > 0) {
                                Toast.makeText(RunForm.this, "物探点号或管线编号重复,请重新输入", Toast.LENGTH_SHORT).show();
                                return;
                            }
//                            List<SaveLineVo> saveLineVos = saveLineVoLongDao.queryForEq("pipName", facName);
//                            if (saveLineVos != null && saveLineVos.size() > 0) {
//                                Toast.makeText(RunForm.this, "物探点号或管线编号重复,请重新输入", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
					if (drawType==null||isedit==null||drawType.equals("")||isedit.equals("")){
						Toast.makeText(getApplicationContext(),"绘制类型或者是否编辑为null",Toast.LENGTH_SHORT).show();
						return;
					}

					//保存原来的哦VO里面
					saveForm(isedit);

					//将 labelValueMap 放入到
					iparams.put(OkHttpParam.LABEL_VALUE_MAP,labelValueMap);

					if (isedit.equals(MapMeterMoveScope.ISEDIT)){		//是编辑模式
						// 设施点
						PointEditAsync pointEditAsync=new PointEditAsync(RunForm.this
								,theForm,iparams,savePointVo,dynFormVo,drawType);
						pointEditAsync.execute();

//						if (drawType.equals(MapMeterMoveScope.POINT)){
//
////							if (SaveData.EditPointDate(RunForm.this,theForm.getJsonResults(),iparams,pointLineBundle)){
////								AlertDialog.Builder bd = new AlertDialog.Builder(ll
////										.getContext());
////								AlertDialog ad = bd.create();
////								ad.setTitle("成功");
////								ad.setMessage("修改数据成功");
////								ad.show();
////								Intent intent=new Intent();
////								intent.putExtra(OkHttpParam.PROJECT_STATUS,true);
////								setResult(5,intent);
////								RunForm.this.finish();
////							}else {
////								AlertDialog.Builder bd = new AlertDialog.Builder(ll
////										.getContext());
////								AlertDialog ad = bd.create();
////								ad.setTitle("错误");
////								ad.setMessage("修改数据失败");
////								ad.show();
////							}
//						}else if (drawType.equals(MapMeterMoveScope.LINE)){			//线
//							if (SaveData.EditLine(theForm.getJsonResults(),pointLineBundle,RunForm.this)){
//								AlertDialog.Builder bd = new AlertDialog.Builder(ll
//										.getContext());
//								AlertDialog ad = bd.create();
//								ad.setTitle("成功");
//								ad.setMessage("修改数据成功");
//								ad.show();
//								Intent intent=new Intent();
//								intent.putExtra(OkHttpParam.PROJECT_STATUS,true);
//								setResult(5,intent);
//								RunForm.this.finish();
//							}else {
//								AlertDialog.Builder bd = new AlertDialog.Builder(ll
//										.getContext());
//								AlertDialog ad = bd.create();
//								ad.setTitle("错误");
//								ad.setMessage("修改数据失败");
//								ad.show();
//							}
//						}
					}else if (isedit.equals(MapMeterMoveScope.GATHER)){
						PointSubAsync pointSubAsync=new PointSubAsync(RunForm.this
								,iparams,drawType,dynFormVo,theForm);
						pointSubAsync.execute("");
//						if (drawType.equals(MapMeterMoveScope.POINT)){		//保存采集 设施点
//
//							PointSubAsync pointSubAsync=new PointSubAsync(RunForm.this
//									,iparams,drawType,dynFormVo,theForm);
//							pointSubAsync.execute("");
//
//						}else if (drawType.equals(MapMeterMoveScope.LINE)){			//线
//
//							LineSubAsync lineSubAsync=new LineSubAsync(RunForm.this,theForm,iparams,id,dynFormVo);
//							lineSubAsync.execute("");
//						}
					}


//					if(isChildrenTable){
//						Intent intent=new Intent();
//						intent.putExtra("childrendata", theForm.getJsonResults());
//						intent.putExtra("isAddChildrenTable", isAddChildrenTable);
//						intent.putExtra("childrenTablePosition", childrenTablePosition);
//
//						intent.setAction(childrenTableUpdateAction);
//						sendBroadcast(intent);
//						finish();
//						return;
//					}
//					/*if (!CheckForm()) {
//						AlertDialog.Builder bd = new AlertDialog.Builder(ll
//								.getContext());
//						AlertDialog ad = bd.create();
//						ad.setTitle("错误");
//						ad.setMessage("请完整录入所有带(*)的内容");
//						ad.show();
//						return;
//
//					}*/
//					saveForm();
//					if (Constant.COMPLAINANT_FORM.equals(taskCategory)) {
//						submitForm(false);
//					}
				}
			});

			//数据转为json
			Button jsonBtn = new Button(this);
			jsonBtn.setLayoutParams(new LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));

			ll.addView(jsonBtn);

			jsonBtn.setText("json显示");
			jsonBtn.setVisibility(View.GONE);
			jsonBtn.setOnClickListener(new Button.OnClickListener() {
				public void onClick(View v) {
					String formResults = theForm.getJsonResults();// theForm.getFormattedResults();
					Log.i(tag, formResults);
					AlertDialog.Builder bd = new AlertDialog.Builder(ll
							.getContext());
					AlertDialog ad = bd.create();
					ad.setTitle("Results");
					ad.setMessage(formResults);
					ad.show();
				}
			});
			
			setContentView(viewFlipper);
			setTitle(theForm.getFormName());

			return true;

		} catch (Exception e) {
			Log.e(tag, "Error Displaying Form");
			return false;
		}
	}
	private void finishForm(){
		if(isChildrenTable){
			finish();
			return;
		}
		/*if (opState == 1){*/
			new AlertDialog.Builder(RunForm.this)
			.setTitle("提示")
			.setMessage("是否退出")
			.setPositiveButton("否",new DialogInterface.OnClickListener()  {
				@Override
				public void onClick(DialogInterface dialog,
						int which) {
					// TODO Auto-generated method stub
					//returnData4Parent(false);
					/*if(theForm.getAteps().size()>0&&theForm.getGroupidShow()!=null){
						viewFlipper.showPrevious();
						theForm.setGroupidShow(null);
					}else{
						finish();
					}*/
				}
				})
            .setNegativeButton("是", new DialogInterface.OnClickListener() {
            	@Override
				public void onClick(DialogInterface dialog,
						int which) {
					// TODO Auto-generated method stub
					/*if (!CheckForm()) {
						AlertDialog.Builder bd = new AlertDialog.Builder(RunForm.this);
						AlertDialog ad = bd.create();
						ad.setTitle("错误");
						ad.setMessage("请完整录入所有带(*)的内容");
						ad.show();
						return;

					}*/

					Intent intent=new Intent();
					intent.putExtra(OkHttpParam.PROJECT_STATUS,false);
					setResult(5,intent);
					finish();

//					//如果处理于编辑状态需要先保存
//					if (opState == 1&&pid!=null){
//						saveForm(MapMeterMoveScope.ISEDIT);
//					}
//					if(theForm.getAteps().size()>0){
//						/*theForm.updateAtapsData();
//						atepsView.updateList(theForm);*/
//						if(theForm.getGroupidShow()!=null){
//							viewFlipper.showPrevious();
//							if(progressDialog!=null){
//								progressDialog.dismiss();
//							}
//							theForm.setGroupidShow(null);
//						}else{
//							Intent intent=new Intent();
//							intent.putExtra("isSubmit",false);
//							setResult(5,intent);
//							finish();
//						}
//					}else{
//						Intent intent=new Intent();
//						intent.putExtra("isSubmit",false);
//						setResult(5,intent);
//						finish();
//					}
					//submitForm();
					/*if (submitForm()){
						returnData4Parent();
						if(theForm.getAteps().size()>0){
							viewFlipper.showPrevious();
						}else{
							finish();
						}
					}*/
				}
				}).show();
			

		/*}else{
			returnData4Parent(false);
			if(theForm.getAteps().size()>0&&theForm.getGroupidShow()!=null){
				viewFlipper.showPrevious();
				theForm.setGroupidShow(null);
			}else{
				finish();
			}
		}*/
	}
	
	private boolean saveForm(String isedit){
		String formResults = theForm.getJsonResults();// theForm.getFormattedResults();
		try {
			JSONObject jo= new JSONObject(formResults);
			// 保存表名,
			jo.put("table00", theForm.getTable());
			jo.put("taskCategory00", taskCategory);
			jo.put("taskNum00", taskNum);
			formResults = jo.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (RunForm.this.saveOrUpdate(formResults,isedit)){
				/*AlertDialog.Builder bd = new AlertDialog.Builder(this);
				AlertDialog ad = bd.create();
				ad.setTitle("保存");
				ad.setMessage("记录保存成功");
				ad.show();*/
				Toast.makeText(this, "记录保存成功", Toast.LENGTH_LONG).show();
				return true;
			}else{
				AlertDialog.Builder bd = new AlertDialog.Builder(this);
				AlertDialog ad = bd.create();
				ad.setTitle("保存");
				ad.setMessage("记录保存失败");
				ad.show();
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			AlertDialog.Builder bd = new AlertDialog.Builder(this);
			AlertDialog ad = bd.create();
			ad.setTitle("保存");
			ad.setMessage("记录保存出错:"+e.getMessage());
			ad.show();
			return false;
		}
	}
	
	private boolean CheckForm() {
		try {
			int i;
			boolean good = true;
			for (i = 0; i < theForm.getFields().size(); i++) {
				if(theForm.getAteps().size()>0&&theForm.getGroupidShow()!=null){
					if(theForm.getGroupidShow().equals(theForm.getFields().get(i).getGroupid())){
						String fieldValue = (String) theForm.getFields().elementAt(i)
						.getData();
						Log.i(tag, theForm.getFields().elementAt(i).getName() + " is ["
						+ fieldValue + "]");
						if (theForm.getFields().elementAt(i).isRequired()&&!"system".equalsIgnoreCase(theForm.getFields().elementAt(i).getType())) {
							if (fieldValue == null) {
								good = false;
							} else {
								if (fieldValue.trim().length() == 0) {
									good = false;
								}
							}
						}
					}
				}else{
					String fieldValue = (String) theForm.getFields().elementAt(i)
					.getData();
					Log.i(tag, theForm.getFields().elementAt(i).getName() + " is ["
					+ fieldValue + "]");
					if (theForm.getFields().elementAt(i).isRequired()&&!"system".equalsIgnoreCase(theForm.getFields().elementAt(i).getType())) {
						if (fieldValue == null) {
							good = false;
						} else {
							if (fieldValue.trim().length() == 0) {
								good = false;
							}
						}
					}	
				}
			}
			return good;
		} catch (Exception e) {
			Log.e(tag, "Error in CheckForm()::" + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	//提交数据到服务器
	private boolean submitForm(final boolean isFinish) {
		try {
			boolean ok = true;
			this.progressDialog = ProgressDialog.show(this,
					theForm.getFormName(), "正在提交记录", true, false);
			this.progressHandler = new Handler() {

				@Override
				public void handleMessage(Message msg) {
					// process incoming messages here
					switch (msg.what) {
					case 0:
						// update progress bar
						progressDialog.setMessage("" + (String) msg.obj);
						break;
					case 1:
						//progressDialog.dismiss();
						if(isFinish){
							for (int i = 0; i < theForm.getAteps().size(); ++i) {
								if (theForm.getGroupidShow() != null) {
									if (theForm.getGroupidShow().equals(
										theForm.getAteps().get(i).getGroupid())) {
										theForm.getAteps().get(i).setState("2");
										break;
									}
								}else{
									theForm.getAteps().get(i).setState("2");
								}
							}
							String formResults = theForm.getJsonResults();
							dynFormVo.setContent(formResults);
							try {
								dynamicFormDao.update(dynFormVo);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							returnData4Parent(true);
							if(theForm.getAteps().size()>0){
								theForm.updateAtapsData(RunForm.this,taskNum,tableName,taskCategory);
								atepsView.updateList(theForm);
								if(theForm.getGroupidShow()!=null){
									viewFlipper.showPrevious();	
									progressDialog.dismiss();
									theForm.setGroupidShow(null);
								}else{
									finish();
								}
							}else{
									finish();
							}
						}else{
							progressDialog.dismiss();
						}
						Toast.makeText(RunForm.this, "提交成功", Toast.LENGTH_LONG).show();
						break;
					case 2:
						AlertDialog.Builder bd = new AlertDialog.Builder(RunForm.this);
						AlertDialog ad = bd.create();
						ad.setTitle("错误");
						ad.setMessage("数据提交不成功");
						ad.show();
						progressDialog.dismiss();
						break;
					}
					super.handleMessage(msg);
				}

			};

			Thread workthread = new Thread(new TransmitFormData(theForm));

			workthread.start();

			while (!this.progressDialog.isShowing()){
				;
			}
			return ok;
		} catch (Exception e) {
			Log.e(tag, "Error in submitForm()::" + e.getMessage());
			e.printStackTrace();
			// tell user we failed....
			Message msg = new Message();
			msg.what = 1;
			this.progressHandler.sendMessage(msg);

			return false;
		}

	}
	private class TransmitFormData implements Runnable {
		XmlGuiForm _form;
		Message msg;

		TransmitFormData(XmlGuiForm form) {
			this._form = form;
		}

		public void run() {

			ReturnMessage rm = null;
			try {
				msg = new Message();
				msg.what = 0;
				msg.obj = ("正在提交数据...");
				progressHandler.sendMessage(msg);

				if (dynFormVo != null){
					String url = AppContext.getInstance().getServerUrl();//+MyPublicData.DYMANICFROMUPLOAD;
					//SpringUtil su = new SpringUtil(RunForm.this);
					String result = SpringUtil.dymanicFormUpload(url,dynFormVo.toJson(theForm.getTable(),theForm.getJsonNoAttachResults(),taskCategory,taskNum));
					
					org.json.JSONObject reltJson = null;
					if (result != null || "".equals(result)){
						reltJson = new org.json.JSONObject(result); 
					}
					
					if (reltJson != null ){
						if ("1".equals(reltJson.getString("code"))){

							subGame(url,theForm,dynFormVo);
							
							String Voice = theForm.getJsonVoiceResults();
							if(!"{}".equals(Voice)){
								String serverUrl2 = AppContext.getInstance().getFileServerUrl();//+MyPublicData.FILEATTACHUPLOAD;
								org.json.JSONObject voiceJson = new org.json.JSONObject(Voice);
								Iterator it2 = voiceJson.keys();
								while (it2.hasNext()){
									String o2 = (String) it2.next();
									String fileName2 = voiceJson.getString(o2);
									String guid=dynFormVo.getGuid();
									String label = "录音";
									PicFileInfoVO fpiVo2 = XmlFiledRuleOperater.getPictureLog(guid, label, fileName2);
									if(fpiVo2.getIsUpload()!=null&&"true".equals(fpiVo2.getIsUpload())){
										
									}else{
										File file = new File(fileName2);
										String rlt2 = SpringUtil.attachFileUpload(serverUrl2, fpiVo2.toJson(), file);
										
										if(rlt2.equals("1")){
											JSONObject jo= new JSONObject();
											jo.put("tableName",theForm.getTable());
											jo.put("moiNum", dynFormVo.getId());
											jo.put("imei", dynFormVo.getImei());
											jo.put("status", "1");
											jo.put("voice", fpiVo2.getPfiName());
											SpringUtil.imgStatusUpload(url, jo.toString());
										}
									}
								}
							}
							updateCommitStatus(1,"");
							msg = new Message();
							msg.what = 0;
							msg.obj = ("数据提交成功!");
							progressHandler.sendMessage(msg);

							msg = new Message();
							msg.what = 1;
							progressHandler.sendMessage(msg);
							return;
						}else if ("-1".equals(reltJson.getString("code"))) {
							updateCommitStatus(2,reltJson.getString("msg"));
							msg = new Message();
							msg.what = 0;
							msg.obj = ("数据提交失败:"+reltJson.getString("msg"));
							progressHandler.sendMessage(msg);

							msg = new Message();
							msg.what = 2;
							progressHandler.sendMessage(msg);
							return;
						}
					}else{
						updateCommitStatus(2,reltJson.getString("msg"));
						
						msg = new Message();
						msg.what = 0;
						msg.obj = ("数据提交成失败:"+reltJson.getString("msg"));
						progressHandler.sendMessage(msg);	
						
						msg = new Message();
						msg.what = 2;
						progressHandler.sendMessage(msg);
						return;
					}
				}
				
			} catch (Exception e) {
				Log.d(tag, "数据提交传输异常: " + e.getMessage());
				msg = new Message();
				msg.what = 0;
				msg.obj = ("数据提交传输异常");
				progressHandler.sendMessage(msg);
			}
			msg = new Message();
			msg.what = 2;
			progressHandler.sendMessage(msg);
		}

	}

	public static void subGame(String url,XmlGuiForm theForm,DynamicFormVO dynFormVo) throws JSONException {
		String attach = theForm.getJsonAttachResults();
		if (!"{}".equals(attach)){
            JSONObject attachJson = new JSONObject(attach);

            //FileService fileService = AppContext.getInstance().getFileServer();

            String serverUrl = AppContext.getInstance().getFileServerUrl();//+MyPublicData.FILEATTACHUPLOAD;
//            String serverUrl = "http://172.16.1.20:8086/fileService";
            Iterator it = attachJson.keys();
            int uploadImgNum=0;
            int uploadImgSucceedNum=0;
            String images="";
            while (it.hasNext()){
                String o = (String) it.next();
                String v = attachJson.getString(o);
                String[] fileNames = v.split("\\,");
                uploadImgNum+=fileNames.length;
                for(String fileName :fileNames){
                    String guid=dynFormVo.getGuid();
                    String label = null;
                    if(fileName.contains(":childrentable:")){
                        String str[]=fileName.split(":");
                        fileName = str[0];
                        guid=str[2];
                        label = "照片";
                    }else{
                        label = theForm.findField(o).getLabel();
                    }
                    PicFileInfoVO fpiVo = XmlFiledRuleOperater.getPictureLog(guid, label, fileName);
                    /*if (fileService.isCompress()){
                        if (fileService.getType().equalsIgnoreCase("size")){

                        Bitmap bit = BitmapCompress.compressImageBySize(fileName, Integer.valueOf(fileService.getConfig()));

                        }else if (fileService.getType().equalsIgnoreCase("scale")){

                        }
                    }*/
                    if(fpiVo.getIsUpload()!=null&&"true".equals(fpiVo.getIsUpload())){
                        ++uploadImgSucceedNum;
                    }else{
                        File file = new File(fileName);
                        String rlt = SpringUtil.attachFileUpload(serverUrl, fpiVo.toJson(), file);
                        if (SpringUtil.FAILURE.equals(rlt)){
                            String rlt1 = SpringUtil.attachFileUpload(serverUrl, fpiVo.toJson(), file);
                            if(SpringUtil.SUCCESS.equals(rlt)){
                                String s=(images.equals("") ? fpiVo.getPfiName():","+fpiVo.getPfiName());
                                images+=s;
                                ++uploadImgSucceedNum;
                            }
                        }else{
                            String s=(images.equals("") ? fpiVo.getPfiName():","+fpiVo.getPfiName());
                            images+=s;
                            ++uploadImgSucceedNum;
                        }
                    }
                }

            }
            if(uploadImgNum==uploadImgSucceedNum&&!"".equals(images)){
                JSONObject jo= new JSONObject();
                jo.put("tableName",theForm.getTable());
                jo.put("moiNum", dynFormVo.getId());
                jo.put("imei", dynFormVo.getImei());
                jo.put("status", "1");
                jo.put("images", images);
                SpringUtil.imgStatusUpload(url, jo.toString());
            }
        }
	}

	public void zxingCall(){
		int SCANNIN_GREQUEST_CODE = 1;
		
		Intent intent = new Intent();
		intent.setClass(this, CaptureActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
	}
	/*	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}*/
	
	@SuppressLint("NewApi")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//finish();
			finishForm();
		//	expGuiTitle.getBackBtn().callOnClick();
			return true;
		}
		// 拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	private boolean checkPictureCount(){
		String attach = theForm.getJsonAttachResults2();
		if (!"{}".equals(attach)){
			try {
				org.json.JSONArray attachJson = new org.json.JSONArray(attach);
				for(int i=0;i<attachJson.length();++i){
					String label = attachJson.getJSONObject(i).getString("label");
					String minNum = attachJson.getJSONObject(i).getString("minNum");
					String data = attachJson.getJSONObject(i).getString("DATA_TYPE");
					String [] s= data.split(",");
					int x=s.length;
					if(minNum!=null){
						int m=Integer.parseInt(minNum);
						if(m>x){
							Toast.makeText(this, label+"还差"+(m-x)+"张照片", Toast.LENGTH_LONG).show();
							return false;
						}
					}
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
		return true;
	}
	private boolean checkDateTime(){
		try {
			int i;
			boolean good = true;
			for (i = 0; i < theForm.getFields().size(); i++) {
				if(theForm.getAteps().size()>0&&theForm.getGroupidShow()!=null){
					if(theForm.getGroupidShow().equals(theForm.getFields().get(i).getGroupid())){
						String fieldValue0 = (String) theForm.getFields().elementAt(i).getData();
						
						if ("datetime".equalsIgnoreCase(theForm.getFields().elementAt(i).getType())) {
							String minorDateId = theForm.getFields().elementAt(i).getMinorDateId();
							String minorTimeId = theForm.getFields().elementAt(i).getMinorTimeId();
							if(minorDateId!=null&&!"".equals(minorDateId)
								&&minorTimeId!=null&&!"".equals(minorTimeId)){
								if(fieldValue0==null||"".equals(fieldValue0)){
									good = false;
								}else{
									for (int j = 0; j < theForm.getFields().size(); j++) {
										if(minorDateId.equals(theForm.getFields().elementAt(j).getDateId())
												&&minorTimeId.equals(theForm.getFields().elementAt(j).getTimeId())){
											String fieldValue1 = (String) theForm.getFields().elementAt(j).getData();
											Date d1 = MyDateTools.string2Date(fieldValue1, "yyyy-MM-dd HH:mm:ss");
											Date d0 = MyDateTools.string2Date(fieldValue0, "yyyy-MM-dd HH:mm:ss");
											if(d1!=null&&d0!=null&&d0.getTime()<d1.getTime()){
												good = false;
											}
										}
									}
								}
							}
							
						}
					}
				}else{
					String fieldValue0 = (String) theForm.getFields().elementAt(i).getData();
					
					if ("datetime".equalsIgnoreCase(theForm.getFields().elementAt(i).getType())) {
						String minorDateId = theForm.getFields().elementAt(i).getMinorDateId();
						String minorTimeId = theForm.getFields().elementAt(i).getMinorTimeId();
						if(minorDateId!=null&&!"".equals(minorDateId)
							&&minorTimeId!=null&&!"".equals(minorTimeId)){
							if(fieldValue0==null||"".equals(fieldValue0)){
								good = false;
							}else{
								for (int j = 0; j < theForm.getFields().size(); j++) {
									if(minorDateId.equals(theForm.getFields().elementAt(j).getDateId())
											&&minorTimeId.equals(theForm.getFields().elementAt(j).getTimeId())){
										String fieldValue1 = (String) theForm.getFields().elementAt(j).getData();
										Date d1 = MyDateTools.string2Date(fieldValue1, "yyyy-MM-dd HH:mm:ss");
										Date d0 = MyDateTools.string2Date(fieldValue0, "yyyy-MM-dd HH:mm:ss");
										if(d1!=null&&d0!=null&&d0.getTime()<d1.getTime()){
											good = false;
										}
									}
								}
							}
						}
						
					}	
				}
			}
			return good;
		} catch (Exception e) {
			Log.e(tag, "Error in CheckForm()::" + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}