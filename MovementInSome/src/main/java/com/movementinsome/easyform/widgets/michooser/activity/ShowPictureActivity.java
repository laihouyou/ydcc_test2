package com.movementinsome.easyform.widgets.michooser.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.movementinsome.R;
import com.movementinsome.easyform.widgets.AddCameraView;
import com.movementinsome.easyform.widgets.AddMapScreenshotView;
import com.movementinsome.easyform.widgets.michooser.adapter.ImageAdapter;
import com.movementinsome.kernel.activity.FullActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class ShowPictureActivity extends FullActivity implements ViewFactory,
OnClickListener,OnItemSelectedListener,OnGestureListener{

	private TextView showTvTitle;
	private Button showBtBack;
	private Button showBtDel;
	private ImageSwitcher showImageSwitcher;
	private Gallery showPictureGly;
	private Intent intent;
	private List<String> listPath;
	//private List<Bitmap> listBitmap;
	private List<String> list;
	private int delId;
	private GestureDetector detector;
	private Matrix matrix;
	private Bitmap bm;
	private float ratio=1;
	public static AddCameraView addCameraView;
	public static AddMapScreenshotView addMapScreenshotView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_picture_activity);
		detector = new GestureDetector(this);
		matrix=new Matrix();
		intent = getIntent();
		listPath = new ArrayList<String>();
	//	listBitmap = new ArrayList<Bitmap>();
		
		listPath = intent.getStringArrayListExtra("listPath");
		ShowInitinterface();
		setDate();
		delId = intent.getIntExtra("id", 0);
		showPictureGly.setSelection(delId);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	/**
	 *初始化界�?控件
	 */
	public void ShowInitinterface(){
		
		showBtDel = (Button)findViewById(R.id.showBtDel);
		showBtBack = (Button)findViewById(R.id.showBtBack);
		showTvTitle = (TextView) findViewById(R.id.showTvTitle);
		showPictureGly = (Gallery) findViewById(R.id.showPictureGly);
		showImageSwitcher = (ImageSwitcher)findViewById(R.id.showImageSwitcher);
		
		showBtDel.setOnClickListener(this);
		showBtBack.setOnClickListener(this);
		showImageSwitcher.setOnClickListener(this);
		showPictureGly.setOnItemSelectedListener(this);
	}
	
	/**
	 * 控件初始值设�?
	 */
	public void setDate() {
		if (listPath != null) {
			showImageSwitcher.setFactory(this);
			// 设定载入Switcher的模�?*/
			showImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
					android.R.anim.fade_in));
			// 设定输出Switcher的模�?*/
			showImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
					this, android.R.anim.fade_out));
			update();
		}
	}
	
	/**
	 * 跟新
	 */
	private void update(){
		ImageAdapter adapter = new ImageAdapter(this, listPath);
		showPictureGly.setAdapter(adapter);
	}
	
	/**
	 * 设置bitmap数组
	 */
	/*public void setBitmapList(){
		if (listPath != null) {
			int count = listPath.size();
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 3;
			for (int i = 0; i < count; i++) {
				String path = listPath.get(i).toString();
				Bitmap bm = BitmapFactory.decodeFile(path, options);
				listBitmap.add(bm);
			}
		}else{
			Toast.makeText(this, "·��������Ѹ���Ϣ����������", Toast.LENGTH_LONG).show();
		}
	}*/
	
	/**
	 * 获取图片路径数组
	 * @param Path 图片根目�?
	 * @return
	 */
	public List<String> findImage(String Path){
		list = new ArrayList<String>();
		File file = new File(Path);
		if (file.exists() && file.canRead()){ // 判断是否能读
			File[] files = new File[] {};
			files = file.listFiles();
			if (files.length == 0)
				return null;// 返回,如果�?��都为空的时�?，就直接返回�?
			int count = files.length;
			for (int i = 0; i < count; i++) {
				if (files[i].getAbsolutePath().toLowerCase().contains("jpg")) {
					// 如果是jpg和png就加入list�?
					list.add(files[i].getAbsolutePath());
				}
				// 不扫描DCIM里面的文件，因为是缓�?
				if (files[i].isDirectory() && files[i].canRead()
						&& !files[i].getName().equals("DCIM")) {
					// 如果是目�?进入第二�?
					findImage(files[i].getAbsolutePath());
				}
			}
		} else {
			return null;
		}
		return list;
	}

	@Override
	public View makeView() {
		// TODO Auto-generated method stub
		ImageView iv = new ImageView(this);  
		iv.setBackgroundColor(0xFF000000);  
		iv.setScaleType(ImageView.ScaleType.FIT_CENTER);  
		iv.setLayoutParams(new ImageSwitcher.LayoutParams(  
				LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));  
		return iv;   
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.showBtBack:
			/*if(listBitmap!=null){
				for(Bitmap bt:listBitmap){
					if(bt!=null){
						bt.recycle();
					}
				}
			}*/
			if (bm != null) {
				 bm.recycle();
			}
			if(addMapScreenshotView!=null){
				addMapScreenshotView.updateUI(listPath);
			}else if(addCameraView!=null){
				addCameraView.updateUI(listPath);
			}
			finish();
			break;
		case R.id.showBtDel:
			
			/*File file = new File(listPath.get(delId));
			if(file.delete()){*/
				Toast.makeText(this, "删除成功", Toast.LENGTH_LONG).show();
				listPath.remove(delId);
				/*listBitmap.get(delId).recycle();
				listBitmap.remove(delId);*/
				if(listPath.size() <= 0){
					showBtDel.setEnabled(false);
					showBtDel.setTextColor(Color.rgb(200, 200, 200));
					 showImageSwitcher.removeAllViews();
					 showImageSwitcher.setBackgroundResource(0);
				}
				update();
		/*	}else{
				Toast.makeText(this, "删除异常，请将本情况反馈给我�?, Toast.LENGTH_LONG).show();
			}*/
			break;
		default:
			break;
		}
		
	}
	
	
	
	@SuppressLint("NewApi")
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		delId = arg2;
		String path = listPath.get(arg2).toString();
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDensity = 160;
		options.inSampleSize=2;

		Drawable drawable  = null;
		bm = BitmapFactory.decodeFile(path, options);
		if (bm != null) {
		  drawable = new BitmapDrawable(this.getResources(), bm);
		  showImageSwitcher.setImageDrawable(drawable);
		  showTvTitle.setText(path);
		}
	}
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		/*if(listBitmap!=null){
			for(Bitmap bt:listBitmap){
				bt.recycle();
			}
		}*/
		if(addMapScreenshotView!=null){
			addMapScreenshotView.updateUI(listPath);
		}else if(addCameraView!=null){
			addCameraView.updateUI(listPath);
		}
		finish();
		addMapScreenshotView=null;
		addCameraView=null;
		if (bm != null) {
			 bm.recycle();
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return this.detector.onTouchEvent(event);
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override  
	public boolean dispatchTouchEvent(MotionEvent ev) {  
		detector.onTouchEvent(ev);  
		return super.dispatchTouchEvent(ev);  
	}
	
}
