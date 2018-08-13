package com.movementinsome.kernel.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;
import android.widget.ImageView;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.kernel.activity.FullActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Camera extends FullActivity {

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	
	private static final int CAPTURE_REDIO_ACTIVITY_REQUEST_CODE = 300;

	private String store_path = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_activity);

		Intent intent = null;
		switch (getIntent().getExtras().getInt("code")){
		case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			break;
		case CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE:
			File file = new File(AppContext.getInstance().getAppStoreMedioPath() + "118.mp4");
			Uri uri = Uri.fromFile(file);
			intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			break;
		case CAPTURE_REDIO_ACTIVITY_REQUEST_CODE:
			intent = new Intent(Media.RECORD_SOUND_ACTION);
			//intent.putExtra(Media.,store_path);
			break;
		}
	
		startActivityForResult(intent, getIntent().getExtras().getInt("code"));

	}

	@Override
	protected void onResume() {
		super.onResume();
		/*         */
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if ("".equals(store_path)) {
			store_path = AppContext.getInstance().getAppStoreMedioPath();
		}

		//if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

			if (resultCode == RESULT_OK) {
				File file = new File(store_path);
				String fileName = store_path + "/112.jpg";
				Bundle bundle = data.getExtras();
				
				switch (requestCode){
				case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
					// Image captured and saved to fileUri specified in the Intent
					
					Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
					FileOutputStream bout = null;
	
					file.mkdirs();// 创建文件夹
	
					try {
						bout = new FileOutputStream(fileName);
						
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bout);// 把数据写入文件
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} finally {
						try {
							bout.flush();
							bout.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					((ImageView) findViewById(R.id.imageView))
							.setImageBitmap(bitmap);// 将图片显示在ImageView里
					break;
/*				case CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE:
					AssetFileDescriptor videoAsset;
					FileInputStream fis = null;
					try {
						videoAsset = getContentResolver().openAssetFileDescriptor(
								data.getData(), "r");
						fis = videoAsset.createInputStream();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					// FileOutputStream bout = null;

					file.mkdirs();// 创建文件夹
					fileName = store_path + "/112.mp4";

					FileOutputStream fos;
					try {
						fos = new FileOutputStream(fileName);
						byte[] buf = new byte[1024];
						int len;
						try {
							while ((len = fis.read(buf)) > 0) {
								fos.write(buf, 0, len);
							}
							fis.close();
							fos.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;*/
				case CAPTURE_REDIO_ACTIVITY_REQUEST_CODE:
					bundle = data.getExtras();
					Toast.makeText(this, data.getDataString(), 10);
					break;
				}

			} else if (resultCode == RESULT_CANCELED) {

				// User cancelled the image capture

			} else {

				// Image capture failed, advise user

			}

		//}

/*		if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {

			if (resultCode == RESULT_OK) {

				// Video captured and saved to fileUri specified in the Intent

				AssetFileDescriptor videoAsset;
				FileInputStream fis = null;
				try {
					videoAsset = getContentResolver().openAssetFileDescriptor(
							data.getData(), "r");
					fis = videoAsset.createInputStream();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				File file = new File(store_path);
				// FileOutputStream bout = null;

				file.mkdirs();// 创建文件夹
				String fileName = store_path + "/112.mp4";

				FileOutputStream fos;
				try {
					fos = new FileOutputStream(fileName);
					byte[] buf = new byte[1024];
					int len;
					try {
						while ((len = fis.read(buf)) > 0) {
							fos.write(buf, 0, len);
						}
						fis.close();
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (resultCode == RESULT_CANCELED) {

				// User cancelled the video capture

			} else {

				// Video capture failed, advise user

			}

		}*/
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 */

}