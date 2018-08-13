/*
 * XmlGui application.
 * Written by Frank Ableson for IBM Developerworks
 * June 2010
 * Use the code as you wish -- no warranty of fitness, etc, etc.
 */
package com.movementinsome.easyform.formengineer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.movementinsome.app.server.TaskFeedBackAsyncTask;


import android.content.Context;
import android.widget.LinearLayout;

public class XmlGuiForm {

	private String formNumber;
	private String formName;
	private String submitTo;
	private String page;  //one/many
	private String table;  //动态表单的存储表名
	private String groupidShow;// 当前显示
	
	public Vector<XmlGuiFormParam> params;
	public Vector<XmlGuiRule> ruls;
	public Vector<XmlGuiFormPage> pages;
	public Vector<XmlGuiFormAteps> ateps;
	private List<Map<String, Object>>atepsData=new ArrayList<Map<String,Object>>();
	
	//public Vector<XmlGuiFormField> fields;
	
	public XmlGuiForm()
	{
		this.params = new Vector<XmlGuiFormParam>();
		this.pages = new Vector<XmlGuiFormPage>();
		this.ruls = new Vector<XmlGuiRule>();
		this.ateps=new Vector<XmlGuiFormAteps>();
		//this.fields = new Vector<XmlGuiFormField>();
		formNumber = "";
		formName = "";
		submitTo = "loopback"; // ie, do nothing but display the results
		page = "one";//one ,mony
	}
	// getters & setters
	public String getFormNumber() {
		return formNumber;
	}

	public void setFormNumber(String formNumber) {
		this.formNumber = formNumber;
	}


	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	
	public String getSubmitTo() {
		return submitTo;
	}

	public void setSubmitTo(String submitTo) {
		this.submitTo = submitTo;
	}

	public Vector<XmlGuiFormField> getFields() {
		Vector<XmlGuiFormField> fields = new Vector<XmlGuiFormField>();
		
		for(XmlGuiFormPage page:this.pages){
			for(XmlGuiFormGroup group:page.getGroups()){
				fields.addAll(group.getFields());
			}
		}
		return fields;
	}

/*	public void setFields(Vector<XmlGuiFormField> fields) {
		this.fields = fields;
	}
*/
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	
	//表名字段输出统计为大写，与后台统一
	public String getTable() {
		return table.toUpperCase();
	}
	public void setTable(String table) {
		this.table = table;
	}
	public Vector<XmlGuiFormParam> getParams() {
		return params;
	}
	public void setParams(Vector<XmlGuiFormParam> params) {
		this.params = params;
	}
	public Vector<XmlGuiFormPage> getPages() {
		return pages;
	}
	public void setPages(Vector<XmlGuiFormPage> pages) {
		this.pages = pages;
	}
	
	public Vector<XmlGuiRule> getRuls() {
		return ruls;
	}
	public void setRuls(Vector<XmlGuiRule> ruls) {
		this.ruls = ruls;
	}
	public Vector<XmlGuiFormAteps> getAteps() {
		return ateps;
	}
	public void setAteps(Vector<XmlGuiFormAteps> ateps) {
		this.ateps = ateps;
	}
	
	public String getGroupidShow() {
		return groupidShow;
	}
	public void setGroupidShow(String groupidShow) {
		this.groupidShow = groupidShow;
	}
	//通过字段名找到相关的Field对象
	public XmlGuiFormField findField(String fieldName){
		if (fieldName == null)
			return null;
		
		ListIterator<XmlGuiFormField> li = this.getFields().listIterator();
		while (li.hasNext()) {
			XmlGuiFormField field = li.next();
			if (field.getName().equals(fieldName)){
				return field;
			}
		}
		return null;
	}
	
	//通过参数名找到相关的Field名称
	public String findFieldByParam(String paramName){
		ListIterator<XmlGuiFormParam> li = this.getParams().listIterator();
		while (li.hasNext()) {
			XmlGuiFormParam param = li.next();
			if (param.getName().equals(paramName)){
				return param.getField();
			}
		}
		return null;
	}
	
	public String findParamDefValue(String paramName){
		ListIterator<XmlGuiFormParam> li = this.getParams().listIterator();
		while (li.hasNext()) {
			XmlGuiFormParam param = li.next();
			if (param.getName().equals(paramName)){
				return param.getDefValue();
			}
		}
		return null;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("XmlGuiForm:\n");
		sb.append("Form Number: " + this.formNumber + "\n");
		sb.append("Form Name: " + this.formName + "\n");
		sb.append("Submit To: " + this.submitTo + "\n");
		if (this.getFields() == null) return sb.toString();
		ListIterator<XmlGuiFormField> li = this.getFields().listIterator();
		while (li.hasNext()) {
			sb.append(li.next().toString());
		}
		
		return sb.toString();
	}
	
	public String getFormattedResults()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Results:\n");
		if (this.getFields() == null) return sb.toString();
		ListIterator<XmlGuiFormField> li = this.getFields().listIterator();
		while (li.hasNext()) {
			XmlGuiFormField ff = li.next();
			if (ff!=null)
				sb.append(ff.getFormattedResult() + "\n");
		}
		
		return sb.toString();
	}
	
	//产生FORM中所有字段的json内容
	public String getJsonResults()
	{
		StringBuilder sb = new StringBuilder();
		if (this.getFields() == null) return sb.toString();
		ListIterator<XmlGuiFormField> li = this.getFields().listIterator();
		sb.append("{");
		while (li.hasNext()) {
			XmlGuiFormField ff = li.next();
			if (ff!=null){
				if(ff.getType().equals("childrentable")){
					sb.append(ff.getJsonArrayResult() + ",");
				}else if (ff.getType().equals("maptext")&&(ff.getDefValue().contains("mapcoord()")
						||ff.getDefValue().contains("mapcoord(N)"))){
					if(!"".equals((String) ff.getData())){
						String json = '"'+ff.getName() +'"'+":"+ '"'+"Point("+(String) ff.getData() +")"+ '"';
						sb.append(json + ",");
					}
				}else if (ff.getType().equals("maptext")&&ff.getDefValue().contains("mapcoordbound()")){
					if(!"".equals((String) ff.getData())&&!((String) ff.getData()).contains("POLYGON")){
						String json = '"'+ff.getName() +'"'+":"+ '"'+"Point("+(String) ff.getData() +")"+ '"';
						sb.append(json + ",");
					}else{
						sb.append(ff.getJsonResult() + ",");
					}
				}else if(ff.getType().equals("checklist")){
					sb.append(ff.getJsonArrayResult() + ",");
				}else{
					sb.append(ff.getJsonResult() + ",");
				}
			}	
		}
		ListIterator<XmlGuiFormAteps> atepsLi=this.getAteps().listIterator();
		while (atepsLi.hasNext()) {
			XmlGuiFormAteps xmlGuiFormAteps=atepsLi.next();
			if (xmlGuiFormAteps!=null)
				sb.append(xmlGuiFormAteps.getJsonResult() + ",");
		}
		if (sb.lastIndexOf(",")==(sb.length()-1)){
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		sb.append("}");
		return sb.toString();
	}
		
	//产和的json内容不包含照片等附件信息
	public String getJsonNoAttachResults()
	{
		StringBuilder sb = new StringBuilder();
		if (this.getFields() == null) return sb.toString();
		ListIterator<XmlGuiFormField> li = this.getFields().listIterator();
		sb.append("{");
		while (li.hasNext()) {
			XmlGuiFormField ff = li.next();
			if(ateps.size()>0&&groupidShow!=null){
				if(groupidShow.equals(ff.getGroupid())||(!ff.isDisplay())){
					if (ff!=null&&!"camera".equalsIgnoreCase(ff.getType())
							&&!"mapScreenshot".equalsIgnoreCase(ff.getType())){
						if (ff.getType().equals("maptext")&&(ff.getDefValue().contains("mapcoord()")||ff.getDefValue().contains("mapcoord(N)"))){
							String json = '"'+ff.getName() +'"'+":"+ '"'+"Point("+(String) ff.getData() +")"+ '"';
							sb.append(json + ",");
						}else if (ff.getType().equals("maptext")&&ff.getDefValue().contains("mapcoordbound()")){
							if(!((String) ff.getData()).contains("POLYGON")){
								String json = '"'+ff.getName() +'"'+":"+ '"'+"Point("+(String) ff.getData() +")"+ '"';
								sb.append(json + ",");
							}else{
								sb.append(ff.getJsonResult() + ",");
							}
						}else if(ff.getType().equals("childrentable")){
							sb.append(ff.getJsonArrayResult() + ",");
						}else if(ff.getType().equals("checklist")){
							sb.append(ff.getJsonArrayResult() + ",");
						}else{
							sb.append(ff.getJsonResult() + ",");
						}
					}
				}
			}else{
				if (ff!=null&&!"camera".equalsIgnoreCase(ff.getType())&&!"mapScreenshot".equalsIgnoreCase(ff.getType())){
					if (ff.getType().equals("maptext")&&(ff.getDefValue().contains("mapcoord()")||ff.getDefValue().contains("mapcoord(N)"))){
						String json = '"'+ff.getName() +'"'+":"+ '"'+"Point("+(String) ff.getData() +")"+ '"';
						sb.append(json + ",");
					}else if (ff.getType().equals("maptext")&&ff.getDefValue().contains("mapcoordbound()")){
						if(!((String) ff.getData()).contains("POLYGON")){
							String json = '"'+ff.getName() +'"'+":"+ '"'+"Point("+(String) ff.getData() +")"+ '"';
							sb.append(json + ",");
						}else{
							sb.append(ff.getJsonResult() + ",");
						}
					}else if(ff.getType().equals("childrentable")){
						sb.append(ff.getJsonArrayResult() + ",");
					}else if(ff.getType().equals("checklist")){
						sb.append(ff.getJsonArrayResult() + ",");
					}else{
						sb.append(ff.getJsonResult() + ",");
					}
				}
			}
		}
		if (sb.lastIndexOf(",")==(sb.length()-1)){
			sb.deleteCharAt(sb.lastIndexOf(","));
		}
		sb.append("}");
		return sb.toString();
	}
	
	//只产生类型为照片等附件的json
	public String getJsonAttachResults()
	{
		StringBuilder sb = new StringBuilder();
		if (this.getFields() == null) return sb.toString();
		ListIterator<XmlGuiFormField> li = this.getFields().listIterator();
		sb.append("{");
		int c =0;
		while (li.hasNext()) {
			XmlGuiFormField ff = li.next();
			if(ateps.size()>0&&groupidShow!=null){
				if(groupidShow.equals(ff.getGroupid())){
					if (ff!=null&&("camera".equalsIgnoreCase(ff.getType())||"mapScreenshot".equalsIgnoreCase(ff.getType()))){
						if (!"\"\"".equals(ff.getJsonResult().split("\\:")[1])){
							if ("{".equals(sb.toString())){
								sb.append(ff.getJsonResult());
							}else{
								sb.append(","+ff.getJsonResult() );
							}
						}
					}else if(ff!=null&&"childrentable".equalsIgnoreCase(ff.getType())){
						String data=(String) ff.getData();
						try {
							JSONArray jr= new JSONArray(data);
							for(int i=0;i<jr.length();++i){
								JSONObject jo=jr.getJSONObject(i);
								Iterator<String> iterator =jo.keys();
								while (iterator.hasNext()){
									String s0 = iterator.next();
									if(s0.contains("camera")){
										String camera = null;
										String guid=null;
										try{
											camera = jo.getString(s0);
											guid = jo.getString("guid");
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										if(camera!=null&&guid!=null){
											if ("{".equals(sb.toString())){
												sb.append('"'+"camera"+c+'"'+":"+'"');
											}else{
												sb.append(","+'"'+"camera"+c+'"'+":"+'"');
											}
											++c;
											String s[]=camera.split(",");
											String ss="";
											for(int j=0;j<s.length;++j){
												ss+=ss.equals("") ? (s[j]+":childrentable:"+guid):(","+s[j]+":childrentable:"+guid);
											}
											sb.append(ss+'"');
										}
									}
								}
							}
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}else{
				if (ff!=null&&("camera".equalsIgnoreCase(ff.getType())||"mapScreenshot".equalsIgnoreCase(ff.getType()))){
					if (!"\"\"".equals(ff.getJsonResult().split("\\:")[1])){
						if ("{".equals(sb.toString())){
							sb.append(ff.getJsonResult());
						}else{
							sb.append(","+ff.getJsonResult() );
						}
					}
				}else if(ff!=null&&"childrentable".equalsIgnoreCase(ff.getType())){
					String data=(String) ff.getData();
					try {
						JSONArray jr= new JSONArray(data);
						for(int i=0;i<jr.length();++i){
							JSONObject jo=jr.getJSONObject(i);
							Iterator<String> iterator =jo.keys();
							while (iterator.hasNext()){
								String s0 = iterator.next();
								if(s0.contains("camera")){
									String camera = null;
									String guid=null;
									try{
										camera = jo.getString(s0);
										guid = jo.getString("guid");
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									if(camera!=null&&guid!=null){
										if ("{".equals(sb.toString())){
											sb.append('"'+"camera"+c+'"'+":"+'"');
										}else{
											sb.append(","+'"'+"camera"+c+'"'+":"+'"');
										}
										++c;
										String s[]=camera.split(",");
										String ss="";
										for(int j=0;j<s.length;++j){
											ss+=ss.equals("") ? (s[j]+":childrentable:"+guid):(","+s[j]+":childrentable:"+guid);
										}
										sb.append(ss+'"');
									}
								}
							}
							
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
/*		if (sb.lastIndexOf(",")==(sb.length()-1)){
			sb.deleteCharAt(sb.lastIndexOf(","));
		}*/
		sb.append("}");
		return sb.toString();
	}
	
	//只产生类型为录音等附件的json
	public String getJsonVoiceResults()
	{
		StringBuilder sb = new StringBuilder();
		if (this.getFields() == null) return sb.toString();
		ListIterator<XmlGuiFormField> li = this.getFields().listIterator();
		sb.append("{");
		int c =0;
		while (li.hasNext()) {
			XmlGuiFormField ff = li.next();
				if (ff!=null&&("voice".equalsIgnoreCase(ff.getType()))){
					if (!"\"\"".equals(ff.getJsonResult().split("\\:")[1])){
						if ("{".equals(sb.toString())){
							sb.append(ff.getJsonResult());
						}else{
							sb.append(","+ff.getJsonResult() );
						}
					}
				}
			}
			
		
/*		if (sb.lastIndexOf(",")==(sb.length()-1)){
			sb.deleteCharAt(sb.lastIndexOf(","));
		}*/
		sb.append("}");
		return sb.toString();
	}
	
	//只产生排查系统判断专用
		public String getJsonchoiceResults()
		{
			StringBuilder sb = new StringBuilder();
			if (this.getFields() == null) return sb.toString();
			ListIterator<XmlGuiFormField> li = this.getFields().listIterator();
			int c =0;
			while (li.hasNext()) {
				XmlGuiFormField ff = li.next();
					if (ff!=null&&("choice".equalsIgnoreCase(ff.getType()))){
						if (!"\"\"".equals(ff.getJsonResult().split("\\:")[1])){
							if(ff.getName().equals("buildingType")){
								sb.append(ff.getJsonPg());
							}
						}
					}
				}
				
			
	/*		if (sb.lastIndexOf(",")==(sb.length()-1)){
				sb.deleteCharAt(sb.lastIndexOf(","));
			}*/
			return sb.toString();
		}
	
	//只产生类型为评估下拉值
	public String getJsonValue()
	{
		StringBuilder sb = new StringBuilder();
		if (this.getFields() == null) return sb.toString();
		ListIterator<XmlGuiFormField> li = this.getFields().listIterator();
		int c =0;
		while (li.hasNext()) {
			XmlGuiFormField ff = li.next();
				if (ff!=null&&("choicezh".equalsIgnoreCase(ff.getType()))){
					if (!"\"\"".equals(ff.getJsonResult().split("\\:")[1])){
						if ("".equals(sb.toString())){
							sb.append(ff.getJsonPgVules());
						}else{
							sb.append("*"+ff.getJsonPgVules() );
						}
					}
				}
			}
			
		
/*		if (sb.lastIndexOf(",")==(sb.length()-1)){
			sb.deleteCharAt(sb.lastIndexOf(","));
		}*/
		return sb.toString();
	}
	
	public String getJsonAttachResults2()
	{
		
		JSONArray jr = new JSONArray();
		
		if (this.getFields() == null) return jr.toString();
		ListIterator<XmlGuiFormField> li = this.getFields().listIterator();
		while (li.hasNext()) {
			XmlGuiFormField ff = li.next();
			if(ateps.size()>0&&groupidShow!=null){
				if(groupidShow.equals(ff.getGroupid())){
					if (ff!=null&&("camera".equalsIgnoreCase(ff.getType())||"mapScreenshot".equalsIgnoreCase(ff.getType()))){
						JSONObject jo = new JSONObject();
						try {
							jo.put("label", ff.getLabel());
							jo.put("minNum", ff.getMinNum());
							jo.put("data", ff.getData());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						jr.put(jo);
					}
				}
			}else{
				if (ff!=null&&("camera".equalsIgnoreCase(ff.getType())||"mapScreenshot".equalsIgnoreCase(ff.getType()))){
					if (!"\"\"".equals(ff.getJsonResult().split("\\:")[1])){
						JSONObject jo = new JSONObject();
						try {
							jo.put("label", ff.getLabel());
							jo.put("minNum", ff.getMinNum());
							jo.put("data", ff.getData());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						jr.put(jo);
					}
				}
			}
			
		}
		
		return jr.toString();
	}
	
	public String getFormEncodedData()
	{
		try {
		int i = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("Results:\n");
		if (this.getFields() == null) return sb.toString();
		ListIterator<XmlGuiFormField> li = this.getFields().listIterator();
		while (li.hasNext()) {
			if (i != 0) sb.append("&");
			XmlGuiFormField thisField = li.next();
			sb.append(thisField.name + "=");
			String encstring = new String();
			URLEncoder.encode((String) thisField.getData(),encstring);
			sb.append(encstring);
		}

		return sb.toString();
		}
		catch (Exception e) {
			return "ErrorEncoding";
		}
	}
	// 步骤列表数据
	public void setAtapsData(LinearLayout llGroup,String groupNumber){
		for(int i=0;i<ateps.size();++i){
			if(ateps.get(i).getGroupid()!=null
					&&groupNumber!=null
					&&ateps.get(i).getGroupid().equals(groupNumber)){
				Map<String, Object>m=new HashMap<String, Object>();
				m.put("groupid", ateps.get(i).getGroupid());
				m.put("name", ateps.get(i).getName());
				m.put("state", ateps.get(i).getState());
				m.put("startState", ateps.get(i).getStartState());
				m.put("finishState", ateps.get(i).getFinishState());
				m.put("llGroup", llGroup);
				atepsData.add(m);
			}
		}
	}
	public List<Map<String, Object>> getAtapsData(){
		return atepsData;
	}
	public void updateAtapsData(Context context,String taskNum,String tableName,String taskCategory){
		for(int i=0;i<atepsData.size();++i){
			if(groupidShow==null){
				atepsData.get(i).put("state", "2");
			}else{
				if(groupidShow.equals(atepsData.get(i).get("groupid"))){
					atepsData.get(i).put("state", "2");
					uploadState(context, taskNum, tableName, taskCategory, i);
					break;
				}
			}
		}
	}
	private void uploadState(Context context,String taskNum,String tableName,String taskCategory,int i){
		if(atepsData.get(i).get("finishState")!=null&&!atepsData.get(i).get("finishState").equals("")){
			TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
					context,
					false,
					false,
					taskNum,
					(String)atepsData.get(i).get("finishState"),
					null, tableName,
					taskCategory, null,
					null, null);
			taskFeedBackAsyncTask.execute();
		}
	}
	
/*    public interface DataSetSupervioer {
        public void onChange();
    }

    private DataSetSupervioer dataSetChangeListener;

    public void setDataSetChangeListener(DataSetSupervioer dataSetChangeListener) {
        this.dataSetChangeListener = dataSetChangeListener;
    }

    public void notifyDataSetChange() {
        if (null != dataSetChangeListener) {
            dataSetChangeListener.onChange();
        }
    }*/
	
}
