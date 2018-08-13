package com.movementinsome.app.pub.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.kernel.activity.FullActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


/*
 * 该照相(CameraActivity)类：
 * 该类使用说明：
 * 必须（Intent）传递3个参数
 * 0.taskNumber			任务编号
 * 1.fileName			任务文件夹名称
 * 2.imageName			照片名称
 * 3.cameraNumber		拍照的数目（最大值4）
 * 
 * 必须获取pPrefere 用户名（创建对应用户文件夹）
 * 
 * 图片拼接完整路径 = SD卡路径 + 系统文件夹 + 用户名文件夹 + 任务文件夹 + 照片名称 + 时间唯一值
 * imageFilePath =  pScardzzSystem + imageUserName + fileName + imageName + nameId;
 * 
 */


public class CameraActivity extends FullActivity implements OnClickListener {

	private SurfaceHolder surfaceHolder;//照相加载控件
	private SurfaceView surfaceView;	//照相显示控件
	private Camera camera;				//照相控件

	private Button cbtnSave;			//保存
	private Button cbtnTaskPicture;		//拍照
	private Button cbtnSetParameter;	//设置

	private ImageView cImageExit;		//返回

	private TextView cTvImagePath;		//图片路径名称
	private Bitmap bitMapRotate; 		//拍照并压缩后的图片
	private Intent intent;				//接收外界传递的值，创建文件夹，照片名称
	private ProgressDialog progress;	//照片处理等待提示

	private int orientation;			//拍照照片的旋转角度
	private boolean flgCamera;			//是否点击拍照
	private boolean sdCardExist;		//判断SD 卡是否存在
	private String startType;			//到场拍照：计划维修，还是紧急维修
	private String imageFilePath = "";	//拼接后完整路径
	private String imageUserName = "";	//用户登录名（创建文件夹）
	private String fileName = "demo";	//文件夹名称
	private String imageName = "demo";	//照片名称
	private int CAMERA_WIDTH=960;
	private int CAMERA_HEIGHT=720;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*设定无标题*/
		//requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.camera);

		intent = new Intent();
		intent = getIntent();
		cameraInitinterface();

	}

	/**
	 * 获取控件ID和属性
	 */
	private void cameraInitinterface(){

		surfaceView=(SurfaceView)findViewById(R.id.cameraSurfaceview);
		cTvImagePath = (TextView)findViewById(R.id.cTvImagePath);
		cbtnSave=(Button)findViewById(R.id.cbtnSave);
		cbtnTaskPicture=(Button)findViewById(R.id.cbtnTaskPicture);
		cbtnSetParameter = (Button)findViewById(R.id.cbtnSetParameter);
		cImageExit = (ImageView)findViewById(R.id.cImageExit);

		progress = new ProgressDialog(this);
		progress.setMessage("正在压缩处理照片,请等待……");
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);

		cbtnSetParameter.setOnClickListener(this);
		cImageExit.setOnClickListener(this);
		cbtnSave.setOnClickListener(this);
		cbtnTaskPicture.setOnClickListener(this);


		/*
		 * 设定控件属性的使用跟状态
		 */
		flgCamera = true;
		//类型(如果是维修的，拍‘维修后’照片时保存完工时间)
		startType = intent.getStringExtra("startType")+"";				//紧急或计划
		fileName = intent.getStringExtra("fileName")+"";				//表单对应文件夹
		imageName = intent.getStringExtra("imageName")+"";				//图片名称前标志
		imageUserName = AppContext.getInstance().getCurUser().getUserName();// pPrefere.getString("userName", "null") + "/";	//用户名称文件夹
		//判断sd卡是否存在
		sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); 
		if(!sdCardExist){
			//pToastShow("未检测到SD卡，不可拍照");
		}			

		surfaceHolder = surfaceView.getHolder(); // Camera interface to instantiate components
		surfaceHolder.addCallback(surfaceCallback); // Add a callback for the SurfaceHolder
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

	}


	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

		public void surfaceCreated(SurfaceHolder holder) {
			try {
				camera = Camera.open(); // Turn on the camera
				if(camera == null){
					int cametacount=Camera.getNumberOfCameras();
					camera=Camera.open(cametacount-1);
				}
				if(camera == null){
					//pToastShow("打开照相设备失败");
				}
				camera.setPreviewDisplay(holder); // Set Preview
			} catch (IOException e) {
				if(camera != null){
					camera.release();// release camera
				}
				camera = null;
			}
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			Camera.Parameters parameters = null;
			try {
				if(camera != null){
					parameters = camera.getParameters(); // Camera parameters to obtain
					parameters.setPictureFormat(PixelFormat.JPEG);// Setting Picture Format
					parameters.setPictureSize(CAMERA_WIDTH, CAMERA_HEIGHT);
					camera.setDisplayOrientation(90); 
					camera.setParameters(parameters); // Setting camera parameters
					camera.startPreview(); // Start Preview
				}
			} catch (Exception e) {
				// TODO: handle exception
				String e1=e.toString();
				//pToastShow("无法设置相机参数");
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
			if(camera != null){
				camera.stopPreview();// stop preview
				camera.release(); // Release camera resources
				camera = null;
			}
		}
	};

	private void takePic() {
		try{
			//camera.stopPreview();// stop the preview
			camera.takePicture(null, null, pictureCallback); // picture
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//pToastShow("拍照异常");
			return;
		}
	}


	Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			progress.show();
			new SavePictureTask().execute(data);

		}
	};

	class SavePictureTask extends AsyncTask<byte[], String, String> {
		@Override
		protected String doInBackground(final byte[]... params) {

			BitmapFactory.Options options = new BitmapFactory.Options(); 
			options.inSampleSize = 1; 
			options.inDither = false; 
			options.inPurgeable = true; 
			options.inInputShareable = true; 
			options.inTempStorage = new byte[32 * 1024];
			Bitmap bMap = BitmapFactory.decodeByteArray(params[0], 0, params[0].length, options);
			//不旋转
			//			bitMapRotate = Bitmap.createScaledBitmap(bMap, 480, 
			//					640, true); 
			//旋转
			if(bMap.getHeight() < bMap.getWidth()){ 
				orientation = 90; 
			} else { 
				orientation = 0;  
			} 
			if (orientation != 0) { 
				Matrix matrix = new Matrix(); 
				float wScale=(float) (CAMERA_WIDTH*1.0/bMap.getWidth());
				float hScale=(float) (CAMERA_HEIGHT*1.0/bMap.getHeight());
				wScale=wScale<0.01 ? 0.01f:wScale;
				hScale=hScale<0.01 ? 0.01f : hScale;
				wScale=wScale<5 ? wScale : 5f;
				hScale=hScale<5 ? hScale : 5f;
				matrix.setScale( wScale,hScale);	
			//	Bitmap newBitmap=null;
				matrix.postRotate(orientation); 
				if (bitMapRotate != null) {
						bitMapRotate.recycle();
						bitMapRotate = null;
					}
					bitMapRotate = Bitmap.createBitmap(bMap, 0, 0, bMap.getWidth(), 
							bMap.getHeight(), matrix, true); 
					if(bMap!=null){
						bMap.recycle();
						bMap = null;
					}
				  /* newBitmap = Bitmap.createBitmap(bitMapRotate.getWidth(), bitMapRotate.getHeight(), Config.ARGB_4444); 
					bitMapRotate=createBitmap(bitMapRotate, null, MyPublicData.MyDate("date")+" "+MyPublicData.MyDate("time"),newBitmap);*/
				
			} else {
					bitMapRotate = Bitmap.createScaledBitmap(bMap, CAMERA_HEIGHT, 
							CAMERA_WIDTH, true); 
			}
			return "1";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(progress != null)
				progress.dismiss();
				cbtnSave.setVisibility(View.VISIBLE);
				cbtnTaskPicture.setEnabled(true);
				cbtnTaskPicture.setText("重    拍");
		}
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (bitMapRotate != null) {
			bitMapRotate.recycle();
			bitMapRotate = null;
		}
	}

	/**
	 * 拼接图片路径和名称
	 */
	private void getImagePath(){
		String nameId = DateFormat.format("hhmmss", new Date()).toString()+ ".jpg";
		imageFilePath = AppContext.getInstance().getStoragePath() + imageUserName + "/" + fileName + "/";
		File file = new File(imageFilePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		imageFilePath += imageName + nameId;
		cTvImagePath.setText(imageName + nameId);
	}

	/**
	 * 保存拍照的图片
	 * @throws IOException 
	 */
	private void saveCameraImage(){
		try {
			camera.startPreview();
			File picture = new File(imageFilePath);
			FileOutputStream fos = new FileOutputStream(picture.getPath()); // Get file output stream
			bitMapRotate.compress(Bitmap.CompressFormat.JPEG, 70, fos);
			if (bitMapRotate != null) {
				bitMapRotate.recycle();
				bitMapRotate = null;
			}
			fos.close();
			//pToastShow("保存成功");
			
			finish();
		} catch (Exception e) {
			// TODO: handle exception
			//pToastShow("保存失败："+imageFilePath);
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		cTvImagePath.setText("");
		switch (v.getId()) {
		case R.id.cbtnSave://保存
			saveCameraImage();
			break;
		case R.id.cImageExit://退出
			//pToastConcel();
			finish();
			break;
		case R.id.cbtnTaskPicture://拍照
			if(!sdCardExist){
				//pToastShow("未检测到SD卡，请插入SD卡后重新拍照");
			}else{
				if(flgCamera){
					flgCamera = false;
					cbtnTaskPicture.setEnabled(false);
					getImagePath();
					takePic();
				}else{
					camera.startPreview();
					flgCamera = true;
					cbtnTaskPicture.setText("拍    照");
					cbtnSave.setVisibility(View.GONE);
				}
			}
			break;
		default:
			break;
		}

	}
	/**
     * 进行添加水印图片和文字
     * 
     * @param src
     * @param waterMak
     * @return
     */
    public  Bitmap createBitmap(Bitmap src, Bitmap waterMak, String title,Bitmap newBitmap) { 
        if (src == null) { 
            return src; 
        } 
        // 获取原始图片与水印图片的宽与高 
        int w = src.getWidth(); 
        int h = src.getHeight(); 
       
        Canvas mCanvas = new Canvas(newBitmap); 
        // 往位图中开始画入src原始图片 
        mCanvas.drawBitmap(src, 0, 0, null); 
        // 在src的右下角添加水印 
        Paint paint = new Paint(); 
        //paint.setAlpha(100); 
        if(waterMak!=null){
       	 int ww = waterMak.getWidth(); 
            int wh = waterMak.getHeight(); 
            mCanvas.drawBitmap(waterMak, w - ww - 5, h - wh - 5, paint); 
       }  
        // 开始加入文字 
        if (null != title) { 
            Paint textPaint = new Paint(); 
            textPaint.setColor(Color.YELLOW); 
            textPaint.setTextSize(24); 
            String familyName = "宋体"; 
            Typeface typeface = Typeface.create(familyName, 
                    Typeface.BOLD_ITALIC); 
            textPaint.setTypeface(typeface); 
 //           textPaint.setTextAlign(Align.CENTER); 
            mCanvas.drawText(title, w-260, h - 25, textPaint); 
        } 
        mCanvas.save(Canvas.ALL_SAVE_FLAG); 
        mCanvas.restore(); 
        if(waterMak!=null){
        	waterMak.recycle();
        }
        src.recycle();
        return newBitmap; 
    }


}
