/*
 * XmlGui application.
 * Written by Frank Ableson for IBM Developerworks
 * June 2010
 * Use the code as you wish -- no warranty of fitness, etc, etc.
 */
package com.movementinsome.easyform.formengineer;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.movementinsome.R;
import com.movementinsome.kernel.activity.FullActivity;

import java.io.IOException;

/*
 * TAG_DATETIME 时间日期

TAG_FLASH 闪光灯

TAG_GPS_LATITUDE 纬度

TAG_GPS_LATITUDE_REF 纬度参考

TAG_GPS_LONGITUDE 经度

TAG_GPS_LONGITUDE_REF 经度参考

TAG_IMAGE_LENGTH 图片长

TAG_IMAGE_WIDTH 图片宽

TAG_MAKE 设备制造商

TAG_MODEL 设备型号

TAG_ORIENTATION 方向

TAG_WHITE_BALANCE 白平衡
 */

public class XmlGui extends FullActivity {
	final String tag = XmlGui.class.getName();
	
    private ImageView iv;
    private int WindowWidth;
    private int WindowHeight;
    

    private class MyParam{
    	public String pid = "abcdefg";
    	public int uid = 1000;
    	MyParam(){
    		;
    	}
		public String getPid() {
			return pid;
		}
		public void setPid(String pid) {
			this.pid = pid;
		}
		public int getUid() {
			return uid;
		}
		public void setUid(int uid) {
			this.uid = uid;
		}
    }
    
    private MyParam myParam = new MyParam();
    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        iv = (ImageView) findViewById(R.id.iv);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowHeight = wm.getDefaultDisplay().getHeight();
        WindowWidth = wm.getDefaultDisplay().getWidth();
        
        
/*        Button btnRunForm = (Button) this.findViewById(R.id.btnRunForm);
        btnRunForm.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View v)
        	{        		
        		EditText formNumber = (EditText) findViewById(R.id.formNumber);
        		Log.i(tag,"Attempting to process Form # [" + formNumber.getText().toString() + "]");
        		HashMap params = null;
        		try {
					params = setFormParam();
				} catch (EvalError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        		Intent newFormInfo = new Intent(XmlGui.this,RunForm.class);
        		newFormInfo.putExtra("formNumber", formNumber.getText().toString());
        		if (params != null)
        			newFormInfo.putExtra("iParams", params);    			
        		startActivity(newFormInfo);
        	}
        });*/
    }
    
/*    private HashMap setFormParam() throws EvalError{
    	
    	//String apid = "19999999";
		//int uid = 100;
		
    	Map params = new HashMap();
    	Interpreter i = new Interpreter();
    	i.set("pid", myParam.pid);
    	i.set("uid", myParam.uid);
    	    	
		params.put("pid", i.get("pid"));
		params.put("uid", i.get("uid"));

		return (HashMap) params;
    }*/
    public void load(View v) throws IOException
    {
        // 图片解析配置
        Options options = new Options();
        // 不去真的解析图片 获取图片头部信息
        options.inJustDecodeBounds = true;
        Bitmap mitmap = BitmapFactory.decodeFile("/sdcard/DCIM/Camera/IMG_20140917_191312.jpg", options);
        int height = options.outHeight;
        int width = options.outWidth;
        System.out.println("宽" + width + "高" + height);
 
        // 得到手机屏幕宽高比
        int scaleX=width/WindowHeight;
        int scaleY=height/WindowHeight;
        int scale=1;
        if(scaleX>scaleY && scaleY>=1)
        {
            scale=scaleX;
        }
         
        if(scaleY>scaleX && scaleX>=1)
        {
            scale=scaleY;
        }
         
        options.inJustDecodeBounds=false;
        options.inSampleSize=scale;
        mitmap = BitmapFactory.decodeFile("/sdcard/DCIM/Camera/IMG_20140917_191312.jpg", options);
        iv.setImageBitmap(mitmap);
         readEXIF("/sdcard/DCIM/Camera/IMG_20140917_191312.jpg");
    }
     
    @SuppressLint("NewApi")
	private void readEXIF(String path) throws IOException
    {
        ExifInterface exif=new ExifInterface(path);
        //获取信息
        String time=exif.getAttribute(ExifInterface.TAG_DATETIME);
        String model=exif.getAttribute(ExifInterface.TAG_MODEL);
        String iso=exif.getAttribute(ExifInterface.TAG_ISO);
         
        //设置信息 tag可以自定义
        exif.setAttribute(ExifInterface.TAG_EXPOSURE_TIME, 100+"");
        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, "22");
        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, "113");
        exif.saveAttributes();
        String ex_time=exif.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
         
        AlertDialog.Builder builder=new Builder(this);
        builder.setTitle("EXIF信息");
        builder.setMessage(time+"\n"+model+"\n"+iso+"\n"+ex_time);
        builder.setPositiveButton("取消",new OnClickListener()
        {
             
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("确定", new OnClickListener()
        {
             
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
             
        });
        builder.create().show();
    }
} 