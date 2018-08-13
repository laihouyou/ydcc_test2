package com.movementinsome.database.vo;


import java.io.Serializable;

/**
 * 返回信息对象
 * 
 * @author GZTPY
 * 
 */
public class ReturnMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8638386536770331803L;
	private int code;// 正数为成功能，负数为失败
	private String msg;// 返回信息描述
	private String jsonData;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}
	
}
