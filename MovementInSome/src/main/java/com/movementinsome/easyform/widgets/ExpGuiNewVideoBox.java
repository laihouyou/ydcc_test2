/*
 * XmlGui application.
 * Written by Frank Ableson for IBM Developerworks
 * June 2010
 * Use the code as you wish -- no warranty of fitness, etc, etc.
 */
package com.movementinsome.easyform.widgets;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.util.DensityUtil;
import com.movementinsome.app.pub.util.WidgetSetUtil;
import com.movementinsome.app.pub.video.PlayVideo;
import com.movementinsome.app.pub.video.VideoCaptureActivity;

import java.io.File;

public class ExpGuiNewVideoBox extends LinearLayout implements IXmlGuiFormFieldObject {
	TextView label;
	ImageView imageVideo;
	Context context;
	String value;
	String rule;
	// 访问后台获取唯一编号(上传参数)
	String tableName;
	String id;
	private String videopath="";//视频地址
	
	public ExpGuiNewVideoBox(final Context context, String labelText, String initialText
			, String value, String rule, boolean readOnly, String tableName, String id) {
		super(context);
		this.context = context;
		this.value = value;
		this.rule = rule;
		this.tableName = tableName;
		this.id = id;
		
		label = new TextView(context);
		label.setText(labelText);
		label.setEms(context.getResources().getInteger(R.integer.label_ems));
		label.setGravity(Gravity.CENTER_VERTICAL);
		label.setTextColor(Color.BLACK);
		label.setLayoutParams(new LayoutParams(200
				,ViewGroup.LayoutParams.MATCH_PARENT));
		imageVideo = new ImageView(context);
		imageVideo.setPadding(12, 0, 0, 0);
		imageVideo.setLayoutParams(new LayoutParams(DensityUtil.dip2px(context, 69)
				,DensityUtil.dip2px(context, 69)));
		Bitmap bitmap = null;
		if(value != null && !value.equals("")){
			videopath = value;
			bitmap=getVideoThumbnail(videopath);
		}else{
			bitmap =BitmapFactory.decodeResource(getResources(), R.drawable.add_video_icon);
		}
		imageVideo.setImageBitmap(bitmap);
		imageVideo.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (videopath.trim().isEmpty()) {
							AppContext.getInstance().setmHandle(mHandler);
							Intent intents = new Intent(context, VideoCaptureActivity.class);
							intents.putExtra(VideoCaptureActivity.EXTRA_OUTPUT_FILENAME,"");
							context.startActivity(intents);
						} else {
							try {
								Intent it2 = new Intent(context, PlayVideo.class);
								it2.putExtra("path",videopath);
								context.startActivity(it2);
							} catch (ActivityNotFoundException e) {
							}
		
						}
					}
				});
		
		imageVideo.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if(videopath != null && !videopath.equals("")){
					File file = new File(videopath);
					boolean isFe = file.delete();
					if(isFe){
						videopath="";
						Bitmap bitmap =BitmapFactory.decodeResource(getResources(), R.drawable.add_video_icon);
						imageVideo.setImageBitmap(bitmap);
					}
					Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
				}
				return true;
			}
		});
		
		new WidgetSetUtil().RunformLintyoutSet(this,context,false);
		this.addView(label);
		this.addView(imageVideo);
	}
	
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==2){
				videopath = AppContext.getInstance().getVideopath();
				if(videopath != null && !videopath.equals("")){
					imageVideo.setImageBitmap(getVideoThumbnail(videopath));
				}
			}
		};
	};

	public ExpGuiNewVideoBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public String getValue()
	{
		if(videopath != null && !videopath.equals("")){
			return videopath;
		}else{
			return "";
		}
	}

	@Override
	public void autoChangValue(String value) {
		// TODO Auto-generated method stub
		label.setText(value);
	}

	@Override
	public void onChanged() {
		// TODO Auto-generated method stub
		Intent intent = new Intent("RunForm");
		intent.putExtra("req", "onChanged");  
		intent.putExtra("value","");
		context.sendBroadcast(intent);
	}
	
	public static Bitmap getVideoThumbnail(String filePath) {  
	    Bitmap bitmap = null;  
	    MediaMetadataRetriever retriever = new MediaMetadataRetriever();  
	    try {  
	        retriever.setDataSource(filePath);  
	        bitmap = retriever.getFrameAtTime(); 
	        int w = bitmap.getWidth();
	
			int h = bitmap.getHeight();
	
			Matrix matrix = new Matrix();
	
			float scaleWidth = 1;
	
			float scaleHeight =2;
	
			matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
	
			Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
	
			return newbmp;
	    }catch(IllegalArgumentException e) {  
	        e.printStackTrace();  
	    }catch (RuntimeException e) {  
	        e.printStackTrace();  
	    }finally {  
	        try {  
	            retriever.release();  
	        }   
	        catch (RuntimeException e) {  
	            e.printStackTrace();  
	        }  
	    }  
	    return bitmap;  
	}  

}
