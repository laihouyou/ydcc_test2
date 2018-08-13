package com.movementinsome.kernel.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.movementinsome.AppContext;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.database.vo.FileItem;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUtils {

	/**
	 * 创建文件
	 * 
	 * @throws IOException
	 */
	public static File creatStoreFile(String fileName) throws IOException {
		File file = new File(fileName);
		file.createNewFile();
		return file;
	}
	
	/**
	 * 创建目录
	 * 
	 * @param dirName
	 */
	public static File creatStoreDir(String dirName) {
		File dir = new File(dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public static boolean isFileExist(String fileName){
		File file = new File(fileName);
		return file.exists();
	}
	
	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 */
	public static File write2SDFromInput(String path,String fileName,InputStream input){
		File file = null;
		OutputStream output = null;
		try{
			creatStoreDir(path);
			file = creatStoreFile(path + fileName);
			
			output = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int n = 0;
			while ((n = input.read(buffer)) > 0) {
				output.write(buffer, 0, n);
			}
			output.flush();
			input.close();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				output.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return file;
	}
	
	public static InputStream readFile(String path) {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inputStream;
	}

	private static void readTxtFile(String filepath){
		try {
			Scanner in=new Scanner(new File(filepath));
			while(in.hasNext()){
				String str=in.nextLine();
				System.out.println(str);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void writeFile(String outPath, InputStream inputStream) {
		try {
			FileOutputStream fo = new FileOutputStream(outPath);
			byte[] buffer = new byte[1024];
			int n = 0;
			while ((n = inputStream.read(buffer)) > 0) {
				fo.write(buffer, 0, n);
			}
			fo.close();
			inputStream.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
    public static void forceMkdir(File directory) throws IOException {
        if (directory.exists()) {
            if (!directory.isDirectory()) {
                String message =
                    "File "
                        + directory
                        + " exists and is "
                        + "not a directory. Unable to create directory.";
                throw new IOException(message);
            }
        } else {
            if (!directory.mkdirs()) {
                // Double-check that some other thread or process hasn't made
                // the directory in the background
                if (!directory.isDirectory())
                {
                    String message =
                        "Unable to create directory " + directory;
                    throw new IOException(message);
                }
            }
        }
    }

	/**
	 * 获取文件扩展名
	 * @param file
	 * @return
	 */
	private static String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		} else {
			return "";
		}
	}

	public static boolean isTxt(File file){
		if (file.exists()){
			String ectension=getFileExtension(file);
			if (ectension.equals(".txt")){
				return true;
			}
		}
		return false;
	}

	public static String getPath(Context context, Uri uri) throws URISyntaxException {
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;
			try {
				cursor = context.getContentResolver().query(uri, projection, null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
				// Eat it  Or Log it.
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return null;
	}

	public static List<String> readText(File file) throws IOException {
		List<String> lineStrList=new ArrayList<>();
		if (file.exists()){
			// 建立一个输入流对象reader
			InputStreamReader reader=new InputStreamReader(new FileInputStream(file));
			BufferedReader bufferedReader=new BufferedReader(reader);
			String line="";
			line=bufferedReader.readLine();
			while (line!=null){
				line=bufferedReader.readLine();

				lineStrList.add(line);
			}
		}
		return lineStrList;
	}

	public static Bitmap getEQCodeBitmap(ProjectVo projectVo) throws JSONException {
		Bitmap[] bitmap=new Bitmap[1];
		String fileName=projectVo.getProjectId()+".png";
		//1,获取本地文本 对比
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(OkHttpParam.SHARED_PROJECT_ID, projectVo.getProjectId());
//		jsonObject.put(OkHttpParam.PROJECT_TYPE, projectVo.getProjectType());
//		jsonObject.put(OkHttpParam.PROJECT_NAME, projectVo.getProjectName());
		jsonObject.put(OkHttpParam.SHARE_CODE, projectVo.getProjectShareCode());
//		jsonObject.put(OkHttpParam.QICAN_STR, projectVo.getMoveBaseVo().getQicanStr());
//		jsonObject.put(OkHttpParam.USER_ID, projectVo.getMoveBaseVo().getUserId());
//		jsonObject.put(OkHttpParam.USED_NAME, projectVo.getMoveBaseVo().getUserName());
		SharedPreferences pPrefere = null; // 定义数据存储
		SharedPreferences.Editor pEditor; // sharedpreferred数据提交
		if (pPrefere == null) {
			pPrefere = AppContext.getInstance().getSharedPreferences(Constant.SPF_NAME, 3);
		}
		pEditor = pPrefere.edit();
		String qicanStr = pPrefere.getString(OkHttpParam.QICAN_STR, "");
		//2.对比文本,如果相同则说明本地存有二维码图片,直接获取显示
		//  不相同则说明本地没有二维码图片,需要生成
		if (qicanStr.equals(jsonObject.toString())) {
			//2.1  获取本地二维码图片,显示
			if (FileUtils.isFileExist(OkHttpParam.BITMAP_PATH +fileName)) {
				//2.1.1  本地图片存在
				bitmap[0] = BitmapFactory.decodeFile(OkHttpParam.BITMAP_PATH + fileName);
				return bitmap[0];
			} else {
				//2.1.2  本地图片不存在
				//2.1.2.1  -->  2.2
				bitmap[0] = CodeUtils.createImage(jsonObject.toString(), 200, 200, null);
				boolean isOK = BitmaptoCard.saveBmpToSd(OkHttpParam.BITMAP_PATH, bitmap[0], fileName, 100);
				pEditor.putString(Constant.SPF_NAME, jsonObject.toString()).commit();
				return bitmap[0];
			}
		} else {
			//2.2  生成二维码图片 , 保存图片,保存文本
			bitmap[0] = CodeUtils.createImage(jsonObject.toString(), 200, 200, null);
			boolean isOK = BitmaptoCard.saveBmpToSd(OkHttpParam.BITMAP_PATH, bitmap[0], fileName, 100);
			pEditor.putString(Constant.SPF_NAME, jsonObject.toString()).commit();
			return bitmap[0];
		}
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getPickFilePath(Intent data){
		int sdk= Build.VERSION.SDK_INT;
		String pickFilePath="";
		Uri selectedImage = data.getData();//data是onActivityResult返回的intent
		String picturePath  = selectedImage.getPath();
		File file=new File(picturePath);
		if (!file.exists()){
			try{
				if(sdk < 4.4) {
					//2. 4.4以下版本使用第一种方式
					try {
						String[] filePathColumns={MediaStore.Images.Media.DATA} ;
						Cursor c = AppContext.getInstance().getContentResolver().query(selectedImage, filePathColumns, null,null, null) ;
						c.moveToFirst() ;
						int columnIndex = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//getColumnIndex(filePathColumns[0]);
						pickFilePath= c.getString(columnIndex) ;
						c.close() ;
					} catch (Exception e) {
						pickFilePath = selectedImage.getPath();
					}
				}else {
					//3. 4.4及以上使用第二种方式
					try {
						final String docId = DocumentsContract.getDocumentId(selectedImage);
						final String[] split = docId.split(":");
						final String type = split[0];
						Uri contentUri = null;
						if ("primary".equalsIgnoreCase(type)) {
							pickFilePath = Environment.getExternalStorageDirectory() + "/" + split[1];
						} else {
							if ("image".equals(type)) {
								contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
							} else if ("video".equals(type)) {
								contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
							} else if ("audio".equals(type)) {
								contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
							}
							final String selection = "_id=?";
							final String[] selectionArgs = new String[]{
									split[1]
							};
							pickFilePath = FileUtils.getDataColumn(AppContext.getInstance(), contentUri, selection, selectionArgs);
						}
					} catch (Exception e) {
						pickFilePath = selectedImage.getPath();
					}
				}
			}catch(Exception e){
				try{
					if(sdk  >= 4.4) {
						//4. 4.4及以上使用第二种方式抛出异常，则用第一种方式再解析一次
						try {
							String[] filePathColumns={MediaStore.Images.Media.DATA} ;
							Cursor c = AppContext.getInstance().getContentResolver().query(selectedImage, filePathColumns, null,null, null) ;
							c.moveToFirst() ;
							int columnIndex = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//getColumnIndex(filePathColumns[0]);
							pickFilePath= c.getString(columnIndex) ;
							c.close() ;
						} catch (Exception e2) {
							pickFilePath = selectedImage.getPath();
						}
					}
				}catch (Exception e1){
					e1.printStackTrace();
				}
			}
		}
		return pickFilePath;
	}

	private static String getDataColumn(Context context, Uri uri, String selection,
										String[] selectionArgs) {
		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null){
				cursor.close();}
		}
		return null;
	}

	/**
	 * 获取手机里的所有图片
	 */
	public static List<FileItem> getFileItemList(){
		List<FileItem> fileItemList=new ArrayList<>();
		Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		ContentResolver contentResolver = AppContext.getInstance().getContentResolver();
		String[] projection = new String[]{
				MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DISPLAY_NAME,
				MediaStore.Images.ImageColumns.SIZE, MediaStore.Images.ImageColumns.DATE_ADDED
		};
		Cursor cursor = contentResolver.query(imageUri, projection, null, null,
				MediaStore.Images.Media.DATE_ADDED + " desc");

		if (cursor == null) {
			return fileItemList;
		}
		if (cursor.getCount() != 0) {
			while (cursor.moveToNext()) {
				String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
				String fileName =
						cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
				String size = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
				String date = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));

				FileItem item = new FileItem(path, fileName, size, date);
				item.setType(FileItem.Type.Image);
				fileItemList.add(item);
			}
		}
		cursor.close();
		return fileItemList;
	}
}
