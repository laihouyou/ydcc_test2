package com.movementinsome.easyform.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class MyDialog {

	public IDSTATE button;
	private Context context;

	public enum BUTTONID {
		IDYESNO, // 1,2
		IDOKCANCEL; // 3,4
	}
	
	public enum IDSTATE{
		IDYES,
		IDNO,
		IDOK,
		IDCANCEL
	}

	public MyDialog(Context context){
		this.context = context;
	}
	
	public void dialogMsgBox(String title,String content,BUTTONID buttonId){
		if (buttonId == BUTTONID.IDYESNO){		
			new AlertDialog.Builder(context)
					.setTitle(title)
					.setMessage(content)
					.setPositiveButton("是",new OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
	                          dialog.dismiss();
	                          button = IDSTATE.IDYES;
                          }})
                    .setNegativeButton("否", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
	                          dialog.dismiss();
	                          button = IDSTATE.IDNO;
                        }}).show();
		}else{
			new AlertDialog.Builder(context)
			.setTitle("title")
			.setMessage(content)
			.setPositiveButton("确定",new OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                      button = IDSTATE.IDOK;
                  }})
            .setNegativeButton("取消", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                      button = IDSTATE.IDCANCEL;
                }}).show();
		}
//		return button;
	}
}
