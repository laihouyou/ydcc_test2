/*
 * XmlGui application.
 * Written by Frank Ableson for IBM Developerworks
 * June 2010
 * Use the code as you wish -- no warranty of fitness, etc, etc.
 */
package com.movementinsome.easyform.formengineer;

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
import com.movementinsome.easyform.widgets.ExpGuiMultiselect;
import com.movementinsome.easyform.widgets.ExpGuiMutiLine;
import com.movementinsome.easyform.widgets.ExpGuiMutiLineSelect;
import com.movementinsome.easyform.widgets.ExpGuiNewVideoBox;
import com.movementinsome.easyform.widgets.ExpGuiPickOne;
import com.movementinsome.easyform.widgets.ExpGuiPickOne2;
import com.movementinsome.easyform.widgets.ExpGuiPickOneS;
import com.movementinsome.easyform.widgets.ExpGuiPickOneZH;
import com.movementinsome.easyform.widgets.ExpGuiPickOneZS;
import com.movementinsome.easyform.widgets.ExpGuiPipeBroAnalysisBtn;
import com.movementinsome.easyform.widgets.ExpGuiPwd;
import com.movementinsome.easyform.widgets.ExpGuiRadio;
import com.movementinsome.easyform.widgets.ExpGuiTime;
import com.movementinsome.easyform.widgets.ExpGuiVoiceBox;
import com.movementinsome.easyform.widgets.IXmlGuiFormFieldObject;

// class to handle each individual form
public class XmlGuiFormField {
	String groupid;
	String name;
	String label;
	String type;
	boolean readOnly;
	boolean required;
	int maxLength;
	String defValue = "";
	String options;
	String listKey; //
	String value;
	boolean display;
	String rule;  //所引用规则ID
	public IXmlGuiFormFieldObject obj; // holds the ui implementation , i.e. the EditText for example
	// 子单属性
	String chidrenTableAction;
	String chidrenTableUpdateAction;
	String template;
	Integer qty;
	String id;
	String fId;
	String cId;
	String textId;
	String cTextId;
	String fName;
	String mName;
	String cName;
	String associatedControl;		//关联控件名字  接收从其他控件传来的值
	// 照片最小数量
	Integer minNum;
	// 拍照动作
	String photographAction;
	
	// 巡查项
	String bsCheck;// 值
	String bsIcsName;// 名称
	
	// 时间控件之间比较大小
	String dateId;// 日期控件id(自身)
	String timeId;// 时间控件id(自身)
	String minorDateId;// 日期控件id(较小)
	String minorTimeId;// 时间控件id(较小)
	
	String tableName;

	// getters & setters
	
	public String getName() {
		return name;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTextId() {
		return textId;
	}
	public void setTextId(String textId) {
		this.textId = textId;
	}
	public String getcTextId() {
		return cTextId;
	}
	public void setcTextId(String cTextId) {
		this.cTextId = cTextId;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public String getDateId() {
		return dateId;
	}

	public void setDateId(String dateId) {
		this.dateId = dateId;
	}

	public String getTimeId() {
		return timeId;
	}
	public void setTimeId(String timeId) {
		this.timeId = timeId;
	}
	
	public String getMinorDateId() {
		return minorDateId;
	}
	public void setMinorDateId(String minorDateId) {
		this.minorDateId = minorDateId;
	}
	public String getMinorTimeId() {
		return minorTimeId;
	}
	public void setMinorTimeId(String minorTimeId) {
		this.minorTimeId = minorTimeId;
	}
	public String getPhotographAction() {
		return photographAction;
	}
	public void setPhotographAction(String photographAction) {
		this.photographAction = photographAction;
	}
	public String getBsCheck() {
		return bsCheck;
	}
	public void setBsCheck(String bsCheck) {
		this.bsCheck = bsCheck;
	}
	public String getBsIcsName() {
		return bsIcsName;
	}
	public void setBsIcsName(String bsIcsName) {
		this.bsIcsName = bsIcsName;
	}
	public Integer getMinNum() {
		return minNum;
	}
	public void setMinNum(Integer minNum) {
		this.minNum = minNum;
	}
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
	}
	public String getfName() {
		return fName;
	}
	public void setfName(String fName) {
		this.fName = fName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getfId() {
		return fId;
	}
	public void setfId(String fId) {
		this.fId = fId;
	}
	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public String getChidrenTableAction() {
		return chidrenTableAction;
	}

	public void setChidrenTableAction(String chidrenTableAction) {
		this.chidrenTableAction = chidrenTableAction;
	}

	public String getChidrenTableUpdateAction() {
		return chidrenTableUpdateAction;
	}

	public void setChidrenTableUpdateAction(String chidrenTableUpdateAction) {
		this.chidrenTableUpdateAction = chidrenTableUpdateAction;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public String getDefValue() {
		return defValue;
	}

	public void setDefValue(String defValue) {
		this.defValue = defValue;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getValue() {
		if (obj != null){
			return (String)getData();
		}else{
			return value;
		}
	}

	public void setValue(String object) {
		this.value = object;
	}

	public String getListKey() {
		return listKey;
	}

	public void setListKey(String listKey) {
		this.listKey = listKey;
	}
	
	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}
	
	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}
	

/*	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}*/

	public String getGroupid() {
		return groupid;
	}

	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}

	public IXmlGuiFormFieldObject getObj() {
		return obj;
	}

	public void setObj(IXmlGuiFormFieldObject obj) {
		this.obj = obj;
	}

	public String getAssociatedControl() {
		return associatedControl == null ? "" : associatedControl;
	}

	public void setAssociatedControl(String associatedControl) {
		this.associatedControl = associatedControl;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Field Name: " + this.name + "\n");
		sb.append("Field Label: " + this.label + "\n");
		sb.append("Field Type: " + this.type + "\n");
		sb.append("Required? : " + this.required + "\n");
		sb.append("Options : " + this.options + "\n");
		sb.append("Value : " + (String) this.getData() + "\n");

		return sb.toString();
	}

	public String getFormattedResult() {
		return this.name + "= [" + (String) this.getData() + "]";

	}
	public String getJsonPgVules() {
		return (String) this.getData();
	}

	public String getJsonResult() {
		return '"'+this.name +'"'+":"+ '"'+(String) this.getData() + '"';
	}
	
	public String getJsonPg() {
		return (String) this.getData();
	}
	
	public String getJsonArrayResult(){
		return '"'+this.name +'"'+":"+(String) this.getData();
	}
	
	public Object getData() {
		if (type.equals("text") || type.equals("numeric")
				|| type.equals("number")) {
			if (obj != null) {
				ExpGuiEditBox b = (ExpGuiEditBox) obj;
				return b.getValue();
			}
		} else if (type.equals("password")) {
			if (obj != null) {
				ExpGuiPwd b = (ExpGuiPwd) obj;
				return b.getValue();
			}
		} else if (type.equals("memo")) {
			if (obj != null) {
				ExpGuiMutiLine b = (ExpGuiMutiLine) obj;
				return b.getValue();
			}
		} else if (type.equals("radio")) {
			if (obj != null) {
				ExpGuiRadio b = (ExpGuiRadio) obj;
				return b.getValue();
			}
		} else if (type.equals("check")) {
			if (obj != null) {
				ExpGuiCheck b = (ExpGuiCheck) obj;
				return b.getValue();
			}
		} else if (type.equals("checklist")) {
			if (obj != null) {
				ExpGuiCheckList b = (ExpGuiCheckList) obj;
				return b.getValue();
			}
		} else if (type.equals("mselect")) {
			if (obj != null) {
				ExpGuiMultiselect b = (ExpGuiMultiselect) obj;
				return b.getValue();
			}
		} else if (type.equals("time")) {
			if (obj != null) {
				ExpGuiTime b = (ExpGuiTime) obj;
				return b.getValue();
			}
		} else if (type.equals("date")) {
			if (obj != null) {
				ExpGuiDate b = (ExpGuiDate) obj;
				return b.getValue();
			}
		} else if (type.equals("datetime")) {
			if (obj != null) {
				ExpGuiDateTime b = (ExpGuiDateTime) obj;
				return b.getValue();
			}
		} else if  (type.equals("voice")){
			if (obj !=null ) {
				ExpGuiVoiceBox c = (ExpGuiVoiceBox) obj;
				return c.getValue();
			}
		} else if (type.equals("choice")) {
			if (obj != null) {
				ExpGuiPickOne po = (ExpGuiPickOne) obj;
				return po.getValue();
			}
		} else if (type.equals("choicezh")) {
			if (obj != null) {
				ExpGuiPickOneZH po = (ExpGuiPickOneZH) obj;
				return po.getValue();
			}
		} else if (type.equals("choice_zs")) {
			if (obj != null) {
				ExpGuiPickOneZS po = (ExpGuiPickOneZS) obj;
				return po.getValue();
			}
		}else if (type.equals("choices")) {
			if (obj != null) {
				ExpGuiPickOneS po = (ExpGuiPickOneS) obj;
				return po.getValue();
			}
		}  else if (type.equals("choice2")) {
			if (obj != null) {
				ExpGuiPickOne2 po = (ExpGuiPickOne2) obj;
				return po.getValue();
			}
		} else if (type.equals("camera")) {
			if (obj != null) {
				ExpGuiCamera c = (ExpGuiCamera) obj;
				return c.getValue();
			}
		}else if (type.equals("mapScreenshot")) {
			if (obj != null) {
				ExpGuiMapScreenshot c = (ExpGuiMapScreenshot) obj;
				return c.getValue();
			}
		} else if (type.equals("system")) {
			return value;
		} else if (type.equals("maptext")) {
			ExpGuiMapEditText b = (ExpGuiMapEditText) obj;
			return b.getValue();
		} else if (type.equals("edit_check")) {
			ExpGuiEditSelectBox b = (ExpGuiEditSelectBox) obj;
			return b.getValue();
		}else if (type.equals("memo_check")) {
			ExpGuiMutiLineSelect b = (ExpGuiMutiLineSelect) obj;
			return b.getValue();
		}else if (type.equals("childrentable")) {
			ExpGuiChildrenTableList b = (ExpGuiChildrenTableList) obj;
			return b.getValue();
		}else if(type.equals("map_loc")){
			ExpGuiLocBtn b = (ExpGuiLocBtn) obj;
			return b.getValue();
		}else if(type.equals("code")){
			ExpGuiEditCodeBox b = (ExpGuiEditCodeBox) obj;
			return b.getValue();
		}else if(type.equals("pipeBroAnalysisBtn")){
			ExpGuiPipeBroAnalysisBtn b = (ExpGuiPipeBroAnalysisBtn) obj;
			return b.getValue();
		}else if(type.equals("textCallUp")){
			ExpGuiEditBoxCallUp b = (ExpGuiEditBoxCallUp) obj;
			return b.getValue();
		}else if(type.equals("new_video")){
			ExpGuiNewVideoBox v = (ExpGuiNewVideoBox) obj;
			return v.getValue();
		}

		// todo, add other UI elements here
		return null;
	}

}