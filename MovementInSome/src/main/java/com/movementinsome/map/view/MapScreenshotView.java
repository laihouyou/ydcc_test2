package com.movementinsome.map.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.map.utils.MapUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MapScreenshotView extends AlertDialog {

	private Button map_shot_Back;// 返回
	private Button map_shot_save;// 保存
	private ImageView map_shot_show;// 显示截图
	private String storePath = AppContext.getInstance().getAppStoreMedioPath();
	private MyMapView map;
	private Bitmap bitmap;
	private Context context;
	
	public MapScreenshotView(Context context,MyMapView map) {
		super(context);
		// TODO Auto-generated constructor stub
		this.map=map;
		this.context=context;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_screenshot_view);
		init();
	}
	private void init(){
		map_shot_Back=(Button) findViewById(R.id.map_shot_Back);// 返回
		map_shot_Back.setOnClickListener(myMainBtnClickListener);
		map_shot_save=(Button) findViewById(R.id.map_shot_save);// 保存
		map_shot_save.setOnClickListener(myMainBtnClickListener);
		map_shot_show=(ImageView) findViewById(R.id.map_shot_show);// 显示截图
		bitmap=MapUtil.getViewBitmap(map.getMapView());
		map_shot_show.setImageBitmap(bitmap);
		
	}
	android.view.View.OnClickListener myMainBtnClickListener = new android.view.View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.map_shot_Back:
				dismiss();
				bitmap.recycle();
				break;
			case R.id.map_shot_save:
				mapviewshot(bitmap);
				bitmap.recycle();
				break;

			default:
				break;
			}
		}
		
	};
	//地图截屏
	public void mapviewshot(Bitmap bitmap) {
        //System.out.println("进入截屏方法");
        Date date=new Date();
        SimpleDateFormat dateformat1=new SimpleDateFormat("yyyyMMdd_hhmmss");
        String timeString=dateformat1.format(date);
      
        String filename=storePath+timeString+".png";
        
        File file_2=new File(storePath);
        if (!file_2.exists()){
            System.out.println("path 文件夹 不存在--开始创建");
            file_2.mkdirs();
        }
        //filename=getfilepath(filename);//判断是否有同一秒内的截图，有就改名字
        //存储于sd卡上
        System.out.println("获得的filename--"+filename);
        
        
        File file=new File(filename);    
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(context, "保存成功", Toast.LENGTH_LONG).show();
            dismiss();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
