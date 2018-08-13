package com.movementinsome.app.pub.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.kernel.activity.FullActivity;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class RecordActivity extends FullActivity implements OnClickListener{

	    private static final String LOG_TAG = "AudioRecordTest"; 
	    private Resources rs;
	    //语音文件保存路径   
	    private String path; 
	    //文件名
	    private String fileName;
	    //界面控件   
	    private Button RdStartRecord;   //录音
	    private Button RdStopRecord; // 停止录音
	    private Button RdStartPlay;    //播放
	    private Button RdStopPlay;    //停止播放
	    private Button RdDelRecord;// 删除
	    
	    private SeekBar RdSeek;//播放进度
	    private Timer upDataSeektimer;
	    
	    private ImageView RdBack;// 返回
	    private ImageView RdBackground;// 背景
	    private TextView RdTime;// 计时
	    private int MAX_TIME=30;// 最大录制时间（秒）
	    private int MAX_S=MAX_TIME%60;
	    private int MAX_M=MAX_TIME/60;
	    private Timer timer;//定时器
	    private int time=0;//记录当前时间
	    private Intent intent;
	   
		
	    
	    
	      
	    //语音操作对象   
	    private MediaPlayer mPlayer = null;  
	    private MediaRecorder mRecorder = null;  
	    /** Called when the activity is first created. */  
	    @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState); 
	        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
	        setContentView(R.layout.record);
	        rs=getResources();
	        intent=getIntent();
	        MAX_TIME=intent.getIntExtra("MAX_TIME", 60);
	        path=intent.getStringExtra("path");
	        fileName=intent.getStringExtra("fileName");
	        
	        MAX_S=MAX_TIME%60;
	        MAX_M=MAX_TIME/60;
	        RdTime=(TextView)findViewById(R.id.RdTime);// 计时
	        RdBack=(ImageView) findViewById(R.id.RdBack);// 返回
		    RdBackground=(ImageView) findViewById(R.id.RdBackground);// 背景
		    RdDelRecord=(Button) findViewById(R.id.RdDelRecord);// 删除
	        //开始录音   
	        RdStartRecord = (Button)findViewById(R.id.RdStartRecord);  
	        //结束录音   
	        RdStopRecord = (Button)findViewById(R.id.RdStopRecord); 
	        RdStopRecord.setEnabled(false);
	        RdStopRecord.setTextColor(Color.rgb(200, 200, 200));
	        //开始播放   
	        RdStartPlay = (Button)findViewById(R.id.RdStartPlay);  
	        //结束播放   
	        RdStopPlay = (Button)findViewById(R.id.RdStopPlay);
	        RdStopPlay.setEnabled(false);
	        RdStopPlay.setTextColor(Color.rgb(200, 200, 200)); 
	        
	        RdSeek=(SeekBar) findViewById(R.id.RdSeek);
	        RdSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromUser) {
					// TODO Auto-generated method stub
					if(mPlayer!=null){
						mPlayer.seekTo(progress);
					}		
				}
			});
	        mPlayer = new MediaPlayer(); 
			mPlayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					stopPlay();
				}
			});
			mRecorder = new MediaRecorder();
	        File file=new File(path);
	        if(!file.exists()){
	        	file.mkdirs();
	        }
	        file=new File(path+fileName);
	        if(file.exists()){
	        	RdStartRecord.setText("重录");
	        	RdDelRecord.setVisibility(View.VISIBLE);
	        }else{
	        	RdStartPlay.setEnabled(false);
	        	RdDelRecord.setVisibility(View.GONE);
	        	RdStartPlay.setTextColor(Color.rgb(200, 200, 200)); 
	        }
	        RdBack.setOnClickListener(this);
	        RdStartRecord.setOnClickListener(this);
	        RdStopRecord.setOnClickListener(this);
	        RdStartPlay.setOnClickListener(this);
	        RdStopPlay.setOnClickListener(this);
	        RdDelRecord.setOnClickListener(this);
	    }  
	    private Handler rdHandler=new Handler(){
	    	public void handleMessage(Message msg) {
	    		switch (msg.what) {
				case 1:
					if(time>MAX_TIME){
						stopRecord();
						return;
					}
					int s=time%60;
					int m=time/60;
					String s1=s<10?"0"+s:""+s;
					String m1=m<10?"0"+m:""+m;
					String M=MAX_M<10?"0"+MAX_M:""+MAX_M;
					String S=MAX_S<10?"0"+MAX_S:""+MAX_S;
					
					RdTime.setText(m1+":"+s1+"/"+M+":"+S);
					break;
				case 2:
					if(mPlayer.isPlaying()){
						RdSeek.setProgress(mPlayer.getCurrentPosition());
					}			
					break;

				default:
					break;
				}
	    	};
	    };
	    @Override
	    protected void onDestroy() {
	    	// TODO Auto-generated method stub	
	    	if(timer!=null){
	    		timer.cancel();
	    		time=0;
			}
	    	if(upDataSeektimer!=null){
	    		upDataSeektimer.cancel();
	    	}
	    	if(mRecorder!=null){
	    		 mRecorder.release();
				 mRecorder=null;
	    	}
	    	if(mPlayer!=null){
	    		mPlayer.release();
				mPlayer=null;
	    	}	
	    	super.onDestroy();
	    }
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.RdStartRecord:
				startRecord();
				break;
			case R.id.RdStopRecord:
				stopRecord();
				break;	
			case R.id.RdStartPlay:
				startPlay();		
				break;	
			case R.id.RdStopPlay:
				stopPlay();		
				break;
			case R.id.RdBack:
				finish();
				break;
			case R.id.RdDelRecord:
				File dleFile=new File(path+fileName);
				if(dleFile.delete()){
					//pToastShow("删除成功");
					RdDelRecord.setVisibility(View.GONE);
					RdStartRecord.setText("录音");
					RdStartPlay.setEnabled(false);
		        	RdStartPlay.setTextColor(Color.rgb(200, 200, 200)); 
				}
				break;

			default:
				break;
			}
		} 
		private void startRecord(){
			 mRecorder.reset();
             mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  
             mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);  
             mRecorder.setOutputFile(path+fileName);  
             mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);            
             try {  
                   mRecorder.prepare();  
             } catch (IOException e) {  
            	 //pToastShow("录制失败");  
             }  
             mRecorder.start(); 
             timer=new Timer();
             timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					++time;
					Message msg=new Message();
					msg.what=1;
					rdHandler.sendMessage(msg);
				}
			}, 0, 1000);
             RdTime.setVisibility(View.VISIBLE);//显示计时
             RdBackground.setImageResource(R.drawable.record);//改变背景
             RdBack.setVisibility(View.GONE);//隐藏返回
             RdDelRecord.setVisibility(View.GONE);//隐藏删除
          // 开始录音后：禁用录音按钮、播放和释放停止录音按钮
             RdStopRecord.setEnabled(true);
             RdStopRecord.setTextColor(rs.getColor(R.color.white));
             RdStartRecord.setEnabled(false);
             RdStartRecord.setTextColor(Color.rgb(200, 200, 200));
       
    	     RdStartPlay.setEnabled(false);
    	     RdStartPlay.setTextColor(Color.rgb(200, 200, 200));
		}
		private void stopRecord(){
			 mRecorder.stop();  
			 timer.cancel();
			 time=0;
			 RdTime.setVisibility(View.GONE);//隐藏计时
			 RdBack.setVisibility(View.VISIBLE);//恢复返回
			 RdDelRecord.setVisibility(View.VISIBLE);//显示删除
             RdBackground.setImageResource(R.drawable.recorder);//改变背景
          //停止录音后：禁用停止录音按钮和释放录音按钮、播放
             RdStopRecord.setEnabled(false);
             RdStopRecord.setTextColor(Color.rgb(200, 200, 200));
             RdStartRecord.setEnabled(true);
             RdStartRecord.setText("重录");
             RdStartRecord.setTextColor(rs.getColor(R.color.white));
             RdStartPlay.setEnabled(true);
             RdStartPlay.setTextColor(rs.getColor(R.color.white));
		}
		private void startPlay(){
			
       try{ 
    	    mPlayer.reset();
            mPlayer.setDataSource(path+fileName);  
            mPlayer.prepare();  
            mPlayer.start();
            RdSeek.setMax(mPlayer.getDuration());
            upDataSeektimer=new Timer();
            upDataSeektimer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					rdHandler.sendEmptyMessage(2);
				}
			}, 0, 500);
            RdSeek.setVisibility(View.VISIBLE);
            
            RdBackground.setImageResource(R.drawable.player);//改变背景
         // 开始播放后：禁用播放按钮、录音和释放停止播放按钮
            RdStopPlay.setEnabled(true);
	        RdStopPlay.setTextColor(rs.getColor(R.color.white));
	        RdStartPlay.setEnabled(false);
	        RdStartPlay.setTextColor(Color.rgb(200, 200, 200));
            RdStartRecord.setEnabled(false);
            RdStartRecord.setTextColor(Color.rgb(200, 200, 200));
            RdBack.setVisibility(View.GONE);//隐藏返回
            RdDelRecord.setVisibility(View.GONE);//隐藏删除
        }catch(IOException e){  
            //pToastShow("播放失败");
        }  
		}
		private void stopPlay(){
			RdSeek.setProgress(0);
			upDataSeektimer.cancel();
			mPlayer.stop();
			RdSeek.setVisibility(View.GONE);
			
			
			RdBack.setVisibility(View.VISIBLE);//恢复返回
			RdDelRecord.setVisibility(View.VISIBLE);//恢复删除
            RdBackground.setImageResource(R.drawable.recorder);//改变背景
          //停止播放后：禁用播放录音按钮和释放播放按钮、录音
            RdStopPlay.setEnabled(false);
	        RdStopPlay.setTextColor(Color.rgb(200, 200, 200));
	        RdStartPlay.setEnabled(true);
	        RdStartPlay.setTextColor(rs.getColor(R.color.white));
	        RdStartRecord.setEnabled(true);
	        RdStartRecord.setTextColor(rs.getColor(R.color.white));
		}
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			 if (keyCode == KeyEvent.KEYCODE_BACK
	                 && event.getRepeatCount() == 0) {
	             return true;
	         }
			return super.onKeyDown(keyCode, event);
		}
}
