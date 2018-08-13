package com.movementinsome.app.mytask;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.movementinsome.R;
import com.movementinsome.app.pub.view.CreateDynamicView;
import com.movementinsome.kernel.activity.FullActivity;

import java.util.ArrayList;

public class ShowMsgActivity extends FullActivity{

	private ArrayList<String>msgData;
	private int arrayId;
	private ImageView show_msg_back;
	private ScrollView show_msg_msg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_msg_activity);
		msgData=getIntent().getStringArrayListExtra("msgData");
		arrayId=getIntent().getIntExtra("arrayId", 0);
		
		show_msg_back=(ImageView) findViewById(R.id.show_msg_back);
		show_msg_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		show_msg_msg=(ScrollView) findViewById(R.id.show_msg_msg);
		if(msgData!=null&&arrayId!=0){
			show_msg_msg.addView(new CreateDynamicView(this).dynamicAddView(msgData, arrayId));
		}
	}
}
