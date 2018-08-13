package com.movementinsome.easyform.widgets.michooser.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.database.vo.FileItem;
import com.movementinsome.easyform.util.CameraLogUtil;
import com.movementinsome.easyform.widgets.AddCameraView;
import com.movementinsome.easyform.widgets.michooser.adapter.AlbumGridViewAdapter;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.kernel.initial.model.FileService;
import com.movementinsome.kernel.util.BitmapCompress;
import com.movementinsome.kernel.util.FileUtils;

import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@SuppressLint("NewApi")
public class AlbumActivity extends FullActivity{
	
	private GridView gridView;
	private ArrayList<String> dataList = new ArrayList<String>();
	private HashMap<String,ImageView> hashMap = new HashMap<String,ImageView>();
	private ArrayList<String> selectedDataList = new ArrayList<String>();
	private String cameraDir = "/DCIM/";
	private ProgressBar progressBar;
	private AlbumGridViewAdapter gridImageAdapter;
	private LinearLayout selectedImageLayout;
	private Button okButton;
	private HorizontalScrollView scrollview;
	private final int CAMERA=100;
	private final int SELECT_FOLDER=10;
	public static AddCameraView addCameraView;
	private String imageDir;
	private String newImageName;
	private String bizType; //图片的业务类型
	private Integer qty;
	private ImageView album_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		bizType = bundle.getString("bizType");
		qty = intent.getIntExtra("qty", 8);
		selectedDataList = (ArrayList<String>)bundle.getSerializable("dataList");
		imageDir= AppContext.getInstance().getAppStoreMedioPath();//Environment.getExternalStorageDirectory().getAbsolutePath()+cameraDir;

		init();
		initListener();
		
	}

	private void init() {
		
		progressBar = (ProgressBar)findViewById(R.id.progressbar);
		progressBar.setVisibility(View.GONE);
		gridView = (GridView)findViewById(R.id.myGrid);
		album_back = (ImageView) findViewById(R.id.album_back);
		album_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		gridImageAdapter = new AlbumGridViewAdapter(this, dataList,selectedDataList);
		gridView.setAdapter(gridImageAdapter);
		refreshData();
		selectedImageLayout = (LinearLayout)findViewById(R.id.selected_image_layout);
		okButton = (Button)findViewById(R.id.ok_button);
		scrollview = (HorizontalScrollView)findViewById(R.id.scrollview);
		
		initSelectImage();
		
	}

	private void initSelectImage() {
		if(selectedDataList==null)
			return;
		for(final String path:selectedDataList){
			ImageView imageView = (ImageView) LayoutInflater.from(AlbumActivity.this).inflate(R.layout.choose_imageview, selectedImageLayout,false);
			selectedImageLayout.addView(imageView);			
			hashMap.put(path, imageView);
//			ImageManager2.from(AlbumActivity.this).displayImage(imageView, path,R.drawable.camera,100,100);
            Glide.with(AlbumActivity.this)
                    .load(path)
                    .error(R.drawable.camera)
                    .into(imageView);
			imageView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					removePath(path);
					gridImageAdapter.notifyDataSetChanged();
				}
			});
		}
		okButton.setText("完成("+selectedDataList.size()+"/"+qty+")");
	}

	private void initListener() {
		
		gridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {
			
			@Override
			public void onItemClick(final ToggleButton toggleButton, int position, final String path,boolean isChecked) {
				
				if (path.contains("default")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					newImageName=getPhotoFileName();
					File file = new File(imageDir+newImageName);
					Uri uri = Uri.fromFile(file);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
					startActivityForResult(intent, CAMERA);
					return;
				}
				if(selectedDataList.size()>=qty){
					toggleButton.setChecked(false);
					if(!removePath(path)){
						Toast.makeText(AlbumActivity.this, "只能选择"+qty+"张图片", Toast.LENGTH_SHORT).show();
					}
					return;
				}
					
				if(isChecked){
					if(!hashMap.containsKey(path)){
						ImageView imageView = (ImageView) LayoutInflater.from(AlbumActivity.this).inflate(R.layout.choose_imageview, selectedImageLayout,false);
						selectedImageLayout.addView(imageView);
						imageView.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								
								int off = selectedImageLayout.getMeasuredWidth() - scrollview.getWidth();  
							    if (off > 0) {  
							    	  scrollview.smoothScrollTo(off, 0); 
							    } 
								
							}
						}, 100);
						
						hashMap.put(path, imageView);
						selectedDataList.add(path);
//						ImageManager2.from(AlbumActivity.this).displayImage(imageView, path,R.drawable.camera,100,100);
                        Glide.with(AlbumActivity.this)
                                .load(path)
                                .error(R.drawable.camera)
                                .into(imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								toggleButton.setChecked(false);
								removePath(path);
								
							}
						});
						okButton.setText("完成("+selectedDataList.size()+"/"+qty+")");
					}
				}else{
					removePath(path);
				}
				
				
				
			}
		});
		
		okButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (addCameraView != null)
					addCameraView.updateUI(selectedDataList);
				finish();
			}
		});
		
	}
	
	private boolean removePath(String path){
		if(hashMap.containsKey(path)){
			selectedImageLayout.removeView(hashMap.get(path));
			hashMap.remove(path);
			removeOneData(selectedDataList,path);
			okButton.setText("完成("+selectedDataList.size()+"/8)");
			return true;
		}else{
			return false;
		}
	}
	
	private void removeOneData(ArrayList<String> arrayList,String s){
		for(int i =0;i<arrayList.size();i++){
			if(arrayList.get(i).equals(s)){
				arrayList.remove(i);
				return;
			}
		}
	}
	
    private void refreshData(){
    	
    	new AsyncTask<Void, Void, ArrayList<String>>(){
    		
    		@Override
    		protected void onPreExecute() {
    			progressBar.setVisibility(View.VISIBLE);
    			super.onPreExecute();
    		}

			@Override
			protected ArrayList<String> doInBackground(Void... params) {
				/*ArrayList<String> tmpList = new ArrayList<String>();
				ArrayList<String> listDirlocal =  listAlldir( new File(cameraDir));
                ArrayList<String> listDiranjuke = new ArrayList<String>();
                listDiranjuke.addAll(listDirlocal);
                
                for (int i = 0; i < listDiranjuke.size(); i++){
                    listAllfile( new File( listDiranjuke.get(i) ),tmpList);
                }*/
				return getDirAllFile2(imageDir);
			}
			
			protected void onPostExecute(ArrayList<String> tmpList) {
				
				if(AlbumActivity.this==null||AlbumActivity.this.isFinishing()){
					return;
				}
				progressBar.setVisibility(View.GONE);
				dataList.clear();
				dataList.add("camera_default");
				dataList.addAll(tmpList);
				gridImageAdapter.notifyDataSetChanged();
				return;
				
			};
    		
    	}.execute();
    	
    }
    
    private ArrayList<String>  listAlldir(File nowDir){
        ArrayList<String> listDir = new ArrayList<String>();
        nowDir = new File(Environment.getExternalStorageDirectory() + nowDir.getPath());
        if(!nowDir.isDirectory()){
            return listDir;
        }
                
        File[] files = nowDir.listFiles();

        for (int i = 0; i < files.length; i++){
            if(files[i].getName().substring(0,1).equals(".")){
               continue; 
            }
            File file = new File(files[i].getPath()); 
            if(file.isDirectory()){
                listDir.add(files[i].getPath());
            }
        }              
        return listDir;
    }
    
    private ArrayList<String>  listAllfile( File baseFile,ArrayList<String> tmpList){
        if(baseFile != null && baseFile.exists()){
            File[] file = baseFile.listFiles();
            for(File f : file){
                if(f.getPath().endsWith(".jpg") || f.getPath().endsWith(".png")){
                    tmpList.add(f.getPath());
                }
            }
        }         
        return tmpList;
    }
    
    @Override
    public void onBackPressed() {
    	finish();
//    	super.onBackPressed();
    }
    
    @Override
    public void finish() {
    	// TODO Auto-generated method stub
    	super.finish();
//    	ImageManager2.from(AlbumActivity.this).recycle(dataList);
    }
    
    @Override
    protected void onDestroy() {
    	
    	super.onDestroy();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode, resultCode, data);
    	 if(resultCode == RESULT_OK){
    	 	switch (requestCode){
				case CAMERA:
					new FileHandleTask(this).execute();
					break;
//				case SELECT_FOLDER:
//					String picturePath= FileUtils.getPickFilePath(data);
//					selectedDataList.add(picturePath);
//					try{
//						addCameraView.updateUI(selectedDataList);
//						finish();
//					}catch (Exception e) {
//						// TODO: handle exception
//					}
//					break;
			}
    	 }
    }
    
    public ArrayList<String> getDirAllFile(String imageDir){
		ArrayList<String> listFile = new ArrayList<String>();
		File nowDir = new File(imageDir);
		 if(!nowDir.isDirectory()){// 不是文件夹返回          
			 return listFile;
	        }
		 File[] files = nowDir.listFiles();
		
		//先判断下有没有权限，如果没有权限的话，就不执行了
		if(null == files)
			return listFile;
		 for (int i = 0; i < files.length; i++){
			if (files[i].getName().substring(0, 1).equals(".")) {
				continue;
			}
			if (files[i].isFile()) {
				if(files[i].getPath().endsWith(".jpg") 
					 || files[i].getPath().endsWith(".png")
					 ||files[i].getPath().endsWith(".JPG") 
					 || files[i].getPath().endsWith(".PNG")){
					String log = CameraLogUtil.getInstance().getLog(files[i].getAbsolutePath());
					if(log!=null){
						try {
							org.json.JSONObject logJson = new org.json.JSONObject(log);
							String isUpload = logJson.getString("isUpload");
							if(isUpload!=null&&"true".equals(isUpload)){
								String s= files[i].getAbsolutePath();
							}else{
								listFile.add(files[i].getAbsolutePath());
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							listFile.add(files[i].getAbsolutePath());
						}
					}else{
						listFile.add(files[i].getAbsolutePath());
					}
                }
			}else{
				listFile.addAll(getDirAllFile(files[i].getAbsolutePath()));
			}
		 }
			
		return listFile;
	}

	public ArrayList<String> getDirAllFile2(String imageDir) {
		ArrayList<String> listFile = new ArrayList<String>();
		List<FileItem> fileItemList = FileUtils.getFileItemList();

		for (int i = 0; i < fileItemList.size(); i++) {
			FileItem fileItem = fileItemList.get(i);
			String log = CameraLogUtil.getInstance().getLog(fileItem.getFilePath());
			if (log != null) {
				try {
					org.json.JSONObject logJson = new org.json.JSONObject(log);
					String isUpload = logJson.getString("isUpload");
					if (isUpload != null && "true".equals(isUpload)) {
						String s = fileItem.getFilePath();
					} else {
						listFile.add(fileItem.getFilePath());
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					listFile.add(fileItem.getFilePath());
				}
			} else {
				listFile.add(fileItem.getFilePath());
			}
		}

		return listFile;
	}
    //用当前时间给取得的图片命名
    private String getPhotoFileName() {
    	Date date = new Date(System.currentTimeMillis());
    	SimpleDateFormat dateFormat = new SimpleDateFormat( "'IMG'_yyyy-MM-dd");    
    	return dateFormat.format(date)+"_"+UUID.randomUUID().toString()+ ".jpg";
    }


	class FileHandleTask extends AsyncTask<String, Void, String>{
    	private Context context;			//context对象应用
    	private ProgressDialog progre;		//进度条显示
    	public FileHandleTask(Context context){
    		this.context=context;
    		progre = new ProgressDialog(context);
			progre.setCancelable(false);
			progre.setCanceledOnTouchOutside(false);
			progre.setMessage("正在处理图片,耐心等候……");
			progre.show();
    	}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
		 FileService fileService = AppContext.getInstance().getFileServer();
   		 ByteArrayInputStream isBm = null;
   		 if (fileService.isCompress()){
   			if (fileService.getType().equalsIgnoreCase("size")){
						
   				isBm = BitmapCompress.compressImageBySizeStream(imageDir+newImageName, Integer.valueOf(fileService.getConfig()));
							
				}else if (fileService.getType().equalsIgnoreCase("scale")){
					int fileSize = Integer.valueOf(fileService.getConfig().split("\\,")[0]);
					float hh,ww;
					hh = Float.valueOf(fileService.getConfig().split("\\,")[1].split("\\*")[0]);
					ww = Float.valueOf(fileService.getConfig().split("\\,")[1].split("\\*")[1]);
					isBm = BitmapCompress.compressImageScaleStream(imageDir+newImageName, fileSize,hh,ww);
				}
   			
   			File dirFile = new File(imageDir+newImageName);  
   	        //检测图片是否存在
   	        if(dirFile.exists()){  
   	            dirFile.delete();  //删除原图片
   	        } 
   	          
   	        FileOutputStream fos;
   			try {
   				fos = new FileOutputStream(imageDir+newImageName);
   				byte[] buf = new byte[1024];
   				int len;
   				try {
   					if(isBm!=null){
	   					while ((len = isBm.read(buf)) > 0) {
	   						fos.write(buf, 0, len);
	   					}
	   					isBm.close();
	   					fos.flush();
	   					fos.close();
   					}
   				} catch (IOException e) {
   					// TODO Auto-generated catch block
   					e.printStackTrace();
   				} 
   			} catch (FileNotFoundException e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
   			}
   		 }
   		 
   		 //处理拍照记录
   		 CameraLogUtil.getInstance().writeLog(bizType,imageDir,newImageName); 
   		 //String log = CameraLogUtil.getInstance().getLog(imageDir+newImageName);
   		 //System.out.println(log);
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(progre!=null){
				progre.dismiss();
			}
			selectedDataList.add(imageDir+newImageName);
	   		 try{
	   			 addCameraView.updateUI(selectedDataList);
	   			 finish();
	   		 }catch (Exception e) {
					// TODO: handle exception
			}
		}
    }

	class SelectFolderTask extends AsyncTask<String, Void, String>{
    	private Context context;			//context对象应用
    	private ProgressDialog progre;		//进度条显示
		private String finePath;
    	public SelectFolderTask(Context context,String finePath){
    		this.context=context;
    		progre = new ProgressDialog(context);
			progre.setCancelable(false);
			progre.setCanceledOnTouchOutside(false);
			progre.setMessage("正在处理图片,耐心等候……");
			progre.show();
			this.finePath=finePath;
    	}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
		 FileService fileService = AppContext.getInstance().getFileServer();
   		 ByteArrayInputStream isBm = null;
   		 if (fileService.isCompress()){
   			if (fileService.getType().equalsIgnoreCase("size")){

   				isBm = BitmapCompress.compressImageBySizeStream(finePath, Integer.valueOf(fileService.getConfig()));

				}else if (fileService.getType().equalsIgnoreCase("scale")){
					int fileSize = Integer.valueOf(fileService.getConfig().split("\\,")[0]);
					float hh,ww;
					hh = Float.valueOf(fileService.getConfig().split("\\,")[1].split("\\*")[0]);
					ww = Float.valueOf(fileService.getConfig().split("\\,")[1].split("\\*")[1]);
					isBm = BitmapCompress.compressImageScaleStream(finePath, fileSize,hh,ww);
				}

//   			File dirFile = new File(imageDir+newImageName);
//   	        //检测图片是否存在
//   	        if(dirFile.exists()){
//   	            dirFile.delete();  //删除原图片
//   	        }

   	        FileOutputStream fos;
   			try {
   				fos = new FileOutputStream(imageDir+newImageName);
   				byte[] buf = new byte[1024];
   				int len;
   				try {
   					if(isBm!=null){
	   					while ((len = isBm.read(buf)) > 0) {
	   						fos.write(buf, 0, len);
	   					}
	   					isBm.close();
	   					fos.flush();
	   					fos.close();
   					}
   				} catch (IOException e) {
   					// TODO Auto-generated catch block
   					e.printStackTrace();
   				}
   			} catch (FileNotFoundException e) {
   				// TODO Auto-generated catch block
   				e.printStackTrace();
   			}
   		 }

   		 //处理拍照记录
   		 CameraLogUtil.getInstance().writeLog(bizType,imageDir,newImageName);
   		 //String log = CameraLogUtil.getInstance().getLog(imageDir+newImageName);
   		 //System.out.println(log);
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(progre!=null){
				progre.dismiss();
			}
			selectedDataList.add(imageDir+newImageName);
	   		 try{
	   			 addCameraView.updateUI(selectedDataList);
	   			 finish();
	   		 }catch (Exception e) {
					// TODO: handle exception
			}
		}
    }
}
