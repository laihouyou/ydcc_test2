package com.movementinsome.app.pub.activity;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.movementinsome.R;
import com.movementinsome.kernel.activity.FullActivity;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class VideoActivity extends FullActivity implements OnClickListener{    
    
        
   
    private SurfaceView vdRecord;       
    private SurfaceHolder mSurfaceHolder;  
    private Button vdBack;//返回
    private Button vdStartRecord;//录制
    private ImageView vdStartPlay;//播放
    private VideoView vdView;//播放控件
    private TextView vdTime;// 记录时间
    private ImageView vdIgvBg;// 背景
    private Button vdDelRecord;// 删除
    
    private MediaController mc;//控制视频的播放 
    private MediaRecorder recorder;//控制录像
    
    private File dir;  // 所在的文件夹
    private File myRecAudioFile;//录像文件  
    private Intent intent;
  
    private boolean isMR=false; 
    private String path;//存放的文件夹
    private String fileName;//文件名
    Camera c;
   
   
    private Timer timer;//定时器
    private int MAX_TIME=60;//最大时间
    private int time=0;//当前时间
        
    @Override    
    protected void onCreate(Bundle savedInstanceState) {    
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
        	 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        setContentView(R.layout.video);
        vdIgvBg=(ImageView) findViewById(R.id.vdIgvBg);// 背景
        vdView=(VideoView) findViewById(R.id.vdView);//播放控件
        vdRecord = (SurfaceView) findViewById(R.id.vdRecord);    
        vdRecord.setBackgroundColor(Color.BLACK);
		
        mSurfaceHolder = vdRecord.getHolder();       
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        
      
        vdBack=(Button) findViewById(R.id.vdBack);//返回
        vdDelRecord=(Button) findViewById(R.id.vdDelRecord);// 删除
        
        vdStartRecord=(Button) findViewById(R.id.vdStartRecord);//录制
        vdStartPlay=(ImageView) findViewById(R.id.vdStartPlay);//播放
        vdTime=(TextView) findViewById(R.id.vdTime);// 记录时间
        
        mc=new MediaController(this);
    	recorder = new MediaRecorder();
        
        vdStartRecord.setOnClickListener(this);
        vdStartPlay.setOnClickListener(this);
        vdBack.setOnClickListener(this);
        vdDelRecord.setOnClickListener(this);
       
        intent=getIntent();
        path = intent.getStringExtra("path");
        fileName=intent.getStringExtra("fileName");
        MAX_TIME=intent.getIntExtra("MAX_TIME", 60);
        dir = new File(path);    
        if(!dir.exists()){    
            dir.mkdirs();    
        }  
        myRecAudioFile=new File(path+fileName);
        if(myRecAudioFile.exists()){
        	vdDelRecord.setVisibility(View.VISIBLE);//显示删除
        	vdStartRecord.setText("重录");
        	 vdStartPlay.setImageBitmap(createVideoThumbnail(myRecAudioFile.getAbsolutePath()));
        }else{
        	vdDelRecord.setVisibility(View.GONE);//隐藏删除
        	vdStartRecord.setText("录像");
        	vdStartPlay.setVisibility(View.GONE);//隐藏缩略图
        }
    }    

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.vdStartRecord:
			if(isMR){
				vdStop();
			}else{
				vdStartRecord();
			}	
			break;

		case R.id.vdStartPlay:
			vdstartPlay();
			break;
		case R.id.vdBack:
			finish();
			break;
		case R.id.vdDelRecord:
			File del=new File(path+fileName);
			if(del.delete()){
				vdView.stopPlayback();
				//pToastShow("删除成功");
				vdDelRecord.setVisibility(View.GONE);
				vdStartPlay.setVisibility(View.GONE);
				vdStartRecord.setText("录像");
			}
			break;
		default:
			break;
		}
	} 
	private void vdStartRecord(){
		try { 
			
			if(vdView.isPlaying()){
				vdView.stopPlayback();
			}
			vdView.setVisibility(View.GONE);
			vdRecord.setBackgroundColor(Color.TRANSPARENT);
			vdRecord.setVisibility(View.VISIBLE);
			myRecAudioFile.createNewFile();
			recorder.reset();
            recorder.setPreviewDisplay(mSurfaceHolder.getSurface());//预览    
            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//视频源    
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC); //录音源为麦克风    
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//输出格式为mp4    
            recorder.setVideoSize(640, 480);//视频尺寸    
            recorder.setVideoFrameRate(15);//视频帧频率    
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);//视频编码    
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);//音频编码    
          //  recorder.setMaxDuration(100000);//最大期限  
            recorder.setOutputFile(myRecAudioFile.getAbsolutePath());//保存路径    
            recorder.prepare();    
            recorder.start();
            timer=new Timer();
            vdTime.setVisibility(View.VISIBLE);//显示计时器
            timer.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					++time;
					
					Message msg=new Message();
					msg.what=1;
					if(time>MAX_TIME){
						msg.what=2;
					}
					timeHamdler.sendMessage(msg);
				}
			},  0, 1000);
            vdBack.setVisibility(View.GONE);//隐藏返回
            vdStartRecord.setText("停止");
            vdStartPlay.setEnabled(false);
            vdDelRecord.setVisibility(View.GONE);//隐藏删除
            isMR=true;
        } catch (IOException e) {    
            e.printStackTrace();    
        }catch (Exception e) {
			// TODO: handle exception
        	 e.printStackTrace(); 
		}    
	}
	private void vdStop(){
		recorder.stop();  
		timer.cancel();
        isMR=false;
        vdStartRecord.setText("重录");
        vdBack.setVisibility(View.VISIBLE);//显示返回
        vdTime.setVisibility(View.GONE);//隐藏计时器
        vdDelRecord.setVisibility(View.VISIBLE);//显示删除
        vdStartPlay.setVisibility(View.VISIBLE);
        vdStartPlay.setEnabled(true);
        vdStartPlay.setImageBitmap(createVideoThumbnail(path+fileName));
        time=0;
	}
	private void vdstartPlay(){
		File f=new File(path+fileName);
		if(f.exists()){
			vdRecord.setVisibility(View.GONE);
			vdRecord.setVisibility(View.VISIBLE);
			vdRecord.setBackgroundColor(Color.BLACK);
			vdView.setBackgroundColor(Color.TRANSPARENT);
			vdView.setVisibility(View.VISIBLE);
			vdView.setVideoPath(f.getAbsolutePath());
			vdView.setMediaController(mc);
			mc.setMediaPlayer(vdView);
			vdView.requestFocus();
			vdView.start();		
		}		
	}
	
	private void vdStopPlay(){
		
	}
	private Bitmap createVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch(IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        return bitmap;
    }
	//刷新计时
	private Handler timeHamdler=new Handler(){

		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				int s=time%60;
				int m=time/60;
				String s1=s>=10?""+s:"0"+s;
				String m1=m>=10?""+m:"0"+m;
				vdTime.setText(m1+":"+s1);
				break;
			case 2:// 超时停止录制
				vdStop();
				break;

			default:
				break;
			}
		}
		
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		 if (keyCode == KeyEvent.KEYCODE_BACK
                 && event.getRepeatCount() == 0) {
             return true;
         }else if(keyCode == KeyEvent.KEYCODE_HOME){
        	 if(isMR){
        		 vdStop();
        	 }
        	 if(vdView.isPlaying()){
 				vdView.stopPlayback();	
 			}   	
        	 return true;
         }
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(recorder!=null){
			recorder.release();    
	        recorder=null;  
		}
		 if(timer!=null){
			timer.cancel();
		    time=0;
		 }
		super.onDestroy();
	}
    
}
