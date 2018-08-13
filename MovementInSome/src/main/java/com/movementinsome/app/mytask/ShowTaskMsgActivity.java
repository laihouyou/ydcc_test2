package com.movementinsome.app.mytask;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.activity.WebCheckPhotoActivity;
import com.movementinsome.app.pub.asynctask.AttachmentTask;
import com.movementinsome.app.pub.asynctask.VoicViewTask;
import com.movementinsome.app.pub.view.CreateDynamicView;
import com.movementinsome.kernel.activity.FullActivity;

import java.io.File;
import java.io.IOException;

public class ShowTaskMsgActivity extends FullActivity{

	private ImageView show_msg_back;
	private ScrollView show_msg_msg;
	private Button check_photo;
	private Button check_luyin;
	private String guid;
	private Handler sHandler;
    private MediaPlayer mPlayer = null;
    private String mFileName;
    private String attachment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_msg_activity);
		show_msg_back=(ImageView) findViewById(R.id.show_msg_back);
		check_photo=(Button) findViewById(R.id.check_photo);
		check_luyin=(Button) findViewById(R.id.check_luyin);
		
		String names=getIntent().getStringExtra("names");
		String values =getIntent().getStringExtra("values");
		guid=getIntent().getStringExtra("guid");
		
		sHandler= new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
						  mPlayer = new MediaPlayer();
				          File file = new File(mFileName);
				          if (file.exists()) {

				              try {
				                  mPlayer.reset();
				                  mPlayer.setDataSource(file.getAbsolutePath());
				                  mPlayer.prepare();
				                  mPlayer.start();
				              } catch (IllegalArgumentException e) {
				                  e.printStackTrace();
				              } catch (IllegalStateException e) {
				                  e.printStackTrace();
				              } catch (IOException e) {
				                  e.printStackTrace();
				              }
				          }
					break;
					
				case 2:
					String[] nameFile=attachment.split(",");
					VoicViewTask voicViewTask= new VoicViewTask(ShowTaskMsgActivity.this,guid,sHandler,nameFile[0]);
					voicViewTask.execute(nameFile[1]);
				break;
					
				default:
					break;
				}
			}
		};
		
		if(guid!=null){
			check_photo.setVisibility(View.VISIBLE);
			check_luyin.setVisibility(View.VISIBLE);
		}
		
		check_photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				String serverUrl = AppContext.getInstance().getFileServerUrl()+"/img.jsp?findKey="+guid;
				intent.putExtra("serverUrl", serverUrl);
				intent.setClass(ShowTaskMsgActivity.this, WebCheckPhotoActivity.class);
				startActivity(intent);
			}
		});
		
		check_luyin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url="http://59.33.37.170:9003/fileService/rest/fileKuManage/findPicFileInfoByLinkNum?linkNum="+guid;
				String url2 = AppContext.getInstance().getServerUrl()+"fileKuManage/findPicFileInfoByLinkNum?linkNum=";
				AttachmentTask attachmentTask = new AttachmentTask(ShowTaskMsgActivity.this, guid,sHandler);
				attachmentTask.execute(url);
			}
		});
		
		show_msg_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		show_msg_msg=(ScrollView) findViewById(R.id.show_msg_msg);
		if(names!=null&&values!=null){
			show_msg_msg.addView(new CreateDynamicView(this).dynamicAddView2(names, values));
		}
	}

	public String getmFileName() {
		return mFileName;
	}

	public void setmFileName(String mFileName) {
		this.mFileName = mFileName;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	
	
	
}
