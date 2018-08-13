/*
 * XmlGui application.
 * Written by Frank Ableson for IBM Developerworks
 * June 2010
 * Use the code as you wish -- no warranty of fitness, etc, etc.
 */
package com.movementinsome.easyform.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.util.DensityUtil;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ExpGuiVoiceBox extends LinearLayout implements IXmlGuiFormFieldObject {
	private MediaRecorder mRecorder = null;
	private static final String TAG = "TestRecordAndPlayActivity";
    private static String mFileName = null; // 定义文件名
    private MediaPlayer mPlayer = null;
    private String voiceName;
	TextView label;
	Button luyinBox;
	Button bofangBox;
	Button deletBox;
	Context context;
	String value;
	String rule;
	// 访问后台获取唯一编号(上传参数)
	String tableName;
	String id;
	
	public ExpGuiVoiceBox(final Context context,String labelText,String initialText
			,String value,String rule,boolean readOnly,String tableName,String id) {
		super(context);
		this.context = context;
		this.value = value;
		this.rule = rule;
		this.id = id;
		
		label = new TextView(context);
		label.setText(labelText);
		label.setEms(context.getResources().getInteger(R.integer.label_ems));
		label.setGravity(Gravity.CENTER_VERTICAL);
		label.setTextColor(Color.BLACK);
//		label.setLayoutParams(new LayoutParams(
//				ViewGroup.LayoutParams.FILL_PARENT,
//				DensityUtil.dip2px(context, 45),1));
		
		luyinBox = new Button(context);
		luyinBox.setTextSize(14);
		luyinBox.setText("录音");
		luyinBox.setTextColor(Color.BLACK);
		luyinBox.setGravity(Gravity.CENTER);
		luyinBox.setBackgroundResource(R.drawable.value_compile_k);
		luyinBox.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				DensityUtil.dip2px(context, 45),1));
		
		bofangBox = new Button(context);
		bofangBox.setTextSize(14);
		bofangBox.setText("播放");
		bofangBox.setTextColor(Color.BLACK);
		bofangBox.setGravity(Gravity.CENTER);
		bofangBox.setBackgroundResource(R.drawable.value_compile_k);
		bofangBox.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				DensityUtil.dip2px(context, 45),1));
		
		deletBox = new Button(context);
		deletBox.setTextSize(14);
		deletBox.setText("删除");
		deletBox.setTextColor(Color.BLACK);
		deletBox.setGravity(Gravity.CENTER);
		deletBox.setBackgroundResource(R.drawable.value_compile_k);
		deletBox.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				DensityUtil.dip2px(context, 45),1));
		
		luyinBox.setEnabled(true);
		luyinBox.setTextColor(Color.BLACK);
        bofangBox.setEnabled(false);
        bofangBox.setTextColor(Color.GRAY);
        deletBox.setEnabled(false);
        deletBox.setTextColor(Color.GRAY);
		
		this.addView(label);
		this.addView(luyinBox);
		this.addView(bofangBox);
		this.addView(deletBox);
		
		luyinBox.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					 startRecording();
					 Toast.makeText(context, "开始录音,放开按钮停止录音。" ,Toast.LENGTH_SHORT).show();
				}else if(event.getAction()==MotionEvent.ACTION_UP){
					stopRecording();
					Toast.makeText(context, "录音完成" ,Toast.LENGTH_SHORT).show();
					luyinBox.setEnabled(false);
					luyinBox.setTextColor(Color.GRAY);
		            bofangBox.setEnabled(true);
		            bofangBox.setTextColor(Color.BLACK);
		            deletBox.setEnabled(true);
		            deletBox.setTextColor(Color.BLACK);
				}
				return true;
			}
		});
		
		bofangBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "播放录音" ,Toast.LENGTH_SHORT).show();
				startPlaying();
			}
		});
		
		deletBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					File file2 = new File(mFileName);
					file2.delete();
					Toast.makeText(context, "删除录音成功", Toast.LENGTH_SHORT).show();
					luyinBox.setEnabled(true);
					luyinBox.setTextColor(Color.BLACK);
		            bofangBox.setEnabled(false);
		            bofangBox.setTextColor(Color.GRAY);
		            deletBox.setEnabled(false);
		            deletBox.setTextColor(Color.GRAY);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		});
	}
	
	private void startRecording() {
		// TODO Auto-generated method stub
		getPhotoFileName();
        mRecorder = new MediaRecorder();

        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 设置麦克风
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT); // 输出文件格式
        mRecorder.setOutputFile(mFileName); // 输出文件路径

        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT); // 音频文件编码
        try {
            mRecorder.prepare();
            mRecorder.start();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
	}
	
	private void stopRecording() {
        if (null != mRecorder) {
        	try {
        		mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
        }
    }
	
	private void startPlaying() {

//      if (null == mPlayer) {
          mPlayer = new MediaPlayer();
          Log.i(TAG, "this is play mFileName=" + mFileName);
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

//          }
      }

  }
	
	private String getFileName() {
		// TODO Auto-generated method stub
		File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/record");
        file.mkdirs();// 创建文件夹
		mFileName = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/record/test.ogg";

        return mFileName;
	}
	
    private String getPhotoFileName() {    
    	File file = new File(
    	        AppContext.getInstance().getAppStoreMedioVoicPath());
        file.mkdirs();// 创建文件夹
    	Random rd = new Random();
    	Date date = new Date(System.currentTimeMillis());   
    	SimpleDateFormat dateFormat = new SimpleDateFormat( "'IMG'_yyyy-MM-dd"); 
    	voiceName = dateFormat.format(date)+String.valueOf(rd.nextInt()) + ".ogg";
    	mFileName = AppContext.getInstance().getAppStoreMedioVoicPath()+voiceName;
    	return mFileName;  
    } 

	public ExpGuiVoiceBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void makeNumeric()
	{
		DigitsKeyListener dkl = new DigitsKeyListener(true,true);
//		txtBox.setKeyListener(dkl);
	}
	public void makeNumber(){
//		txtBox.setInputType(InputType.TYPE_CLASS_NUMBER);
	}
	public String getValue()
	{
		return mFileName;
	}
	
	public void setValue(String v)
	{
		luyinBox.setText(v);
	}

	@Override
	public void autoChangValue(String value) {
		// TODO Auto-generated method stub
		luyinBox.setText(value);
	}

	@Override
	public void onChanged() {
		// TODO Auto-generated method stub
		Intent intent = new Intent("RunForm");
		intent.putExtra("req", "onChanged");  
		intent.putExtra("value","");
		context.sendBroadcast(intent);
	}

}
