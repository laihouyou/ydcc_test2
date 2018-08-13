package com.movementinsome.kernel.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.movementinsome.AppContext;
import com.movementinsome.kernel.initial.model.FileService;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;

public class BitmapCompress {

	/**
	 * 按质量对图片进行压缩
	 * @param image
	 * @param fileSize(单位kb)
	 * @return
	 */
	public static Bitmap compressImageBySize(Bitmap image, int fileSize) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		
		byte tmp[] = baos.toByteArray();
		while (baos.toByteArray().length / 1024 > fileSize) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			options -= 10;// 每次都减少10
			if (options>0){
				baos.reset();// 重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                tmp = baos.toByteArray();
				
				if(baos!=null){
					try {
						baos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else{
				break;
			}
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	
	/**
	 * 按质量对图片进行压缩
	 * @param image
	 * @param fileSize(单位kb)
	 * @return
	 */
	public static Bitmap compressImageBySize(String srcPath, int fileSize) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		int options = 100;
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = false;
        Bitmap image = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
        
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		
		byte tmp[] = baos.toByteArray();
		while (tmp.length / 1024 > fileSize) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			options -= 10;// 每次都减少10
			if (options>0){
				baos.reset();// 重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
				tmp = baos.toByteArray();
				
				if(baos!=null){
					try {
						baos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else{
				break;
			}
		}
		
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		
		File dirFile = new File(srcPath);  
        //检测图片是否存在
        if(dirFile.exists()){  
            dirFile.delete();  //删除原图片
        } 
          
        FileOutputStream fos;
		try {
			fos = new FileOutputStream(srcPath);
			byte[] buf = new byte[1024];
			int len;
			try {
				while ((len = isBm.read(buf)) > 0) {
					fos.write(buf, 0, len);
				}
				isBm.close();
				fos.flush();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	
	public static ByteArrayInputStream compressImageBySizeStream(Bitmap image, int fileSize) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		
		image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		
		byte tmp[] = baos.toByteArray();
		while (tmp.length / 1024 > fileSize) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			options -= 10;// 每次都减少10
			if (options>0){
				baos.reset();// 重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
				tmp = baos.toByteArray();
				
				if(baos!=null){
					try {
						baos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else{
				break;
			}

		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		//Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return isBm;
	}
	
	public static ByteArrayInputStream compressImageBySizeStream(String srcPath, int fileSize) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		int options = 100;
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = false;
        Bitmap image = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
        
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		
		byte tmp[] = baos.toByteArray();
			
		while (tmp.length / 1024 > fileSize) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			options -= 10;// 每次都减少10
			if (options>0){
				baos.reset();// 重置baos即清空baos
				image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
				tmp = baos.toByteArray();
				
				if(baos!=null){
					try {
						baos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else{
				break;
			}
		}
		
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		
		if(baos!=null){
			try {
				baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return isBm;
	}
	/**
	 * 图片按比例大小压缩方法（根据Bitmap图片压缩）
	 * @param image
	 * @param hh   长
	 * @param ww   宽
	 * @return
	 */
	public static Bitmap compressImageScale(Bitmap image,int fileSize,float hh,float ww) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();        
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出    
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        //float hh = 800f;//这里设置高度为800f
        //float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        if (null != bitmap){
        	return compressImageBySize(bitmap,fileSize);//压缩好比例大小后再进行质量压缩
        }else{
        	return null;
        }
    }
	
	/**
	 * 按图片按比例大小压缩方法（根据路径获取图片并压缩）
	 * @param srcPath
	 * @param hh   长
	 * @param ww   宽
	 * @return
	 */
	public static Bitmap compressImageScale(String srcPath,int fileSize,float hh,float ww) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        //float hh = 800f;//这里设置高度为800f
        //float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //be = (int) ((w / STANDARD_WIDTH + h/ STANDARD_HEIGHT) / 2);
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        if (null != bitmap){
        	return compressImageBySize(bitmap,fileSize);//压缩好比例大小后再进行质量压缩
        }else{
        	return null;
        }
    }
	
	public static ByteArrayInputStream compressImageScaleStream(String srcPath,int fileSize,float hh,float ww) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        //float hh = 800f;//这里设置高度为800f
        //float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //be = (int) ((w / STANDARD_WIDTH + h/ STANDARD_HEIGHT) / 2);
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
	    FileService fileService = AppContext.getInstance().getFileServer();
	    if(fileService.getCamreaMessage()!=null
	    		&&!("").equals(fileService.getCamreaMessage())
	    		&&fileService.getLabelLocation()!=null
	    		&&!("").equals(fileService.getLabelLocation())){
	    	String[] camreaMessage=fileService.getCamreaMessage().split(",");
	    	String text = "";
	    	for (int i = 0; i < camreaMessage.length; i++) {
	    		if(i+1==camreaMessage.length){
	    			if(camreaMessage[i].equals("dataTime")){
						text += MyDateTools.getDate("date_time");
					}else if(camreaMessage[i].equals("addr")){
						text += AppContext.getInstance().getCurLocation().getAddr();
					}else if(camreaMessage[i].equals("userName")){
						text += "上报人："+AppContext.getInstance().getCurUser().getUserAlias();
					}
	    		}else{
	    			if(camreaMessage[i].equals("dataTime")){
						text += MyDateTools.getDate("date_time")+",";
					}else if(camreaMessage[i].equals("addr")){
						text += AppContext.getInstance().getCurLocation().getAddr()+",";
					}else if(camreaMessage[i].equals("userName")){
						text += "上报人："+AppContext.getInstance().getCurUser().getUserAlias()+",";
					}
	    		}
			}
	        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_4444); 
	        Bitmap newbitmap = createBitmap(bitmap, null, text,newBitmap,fileService.getLabelLocation());
	        if (null != newbitmap){
	        	return compressImageBySizeStream(newbitmap,fileSize);//压缩好比例大小后再进行质量压缩
	        }else{
	        	return null;
	        }
        }else{
		    if (null != bitmap){
	        	return compressImageBySizeStream(bitmap,fileSize);//压缩好比例大小后再进行质量压缩
	        }else{
	        	return null;
	        }
	    }
    }
	
	/**
     * 进行添加水印图片和文字
     * 
     * @param src
     * @param waterMak
	 * @param labelLocation 
     * @return
     */
    public static  Bitmap createBitmap(Bitmap src, Bitmap waterMak, String title,Bitmap newBitmap, String labelLocation) { 
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
        	String[] text = title.split(",");
        	if(text.length>=1){
	        	if(text[0]!=null){
	        		Paint textPaint = new Paint(); 
	                textPaint.setColor(Color.YELLOW); 
	                textPaint.setTextSize(24); 
	                String familyName = "宋体"; 
	                Typeface typeface = Typeface.create(familyName, 
	                        Typeface.BOLD_ITALIC); 
	                textPaint.setTypeface(typeface); 
	     //           textPaint.setTextAlign(Align.CENTER); 
	                if(labelLocation.equals("rightDown")){
	                	mCanvas.drawText(text[0], w-250, h - 85, textPaint);
	                }else if(labelLocation.equals("leftDown")){
	                	mCanvas.drawText(text[0], 25, h - 85, textPaint);
	                }else if(labelLocation.equals("rightUp")){
	                	mCanvas.drawText(text[0], w-250, 40, textPaint);
	                }else if(labelLocation.equals("leftUp")){
	                	mCanvas.drawText(text[0], 25, 40, textPaint);
	                }
	        	}
	        }
        	if(text.length>=2){
        		if(text[1]!=null){
	            	Paint textPaint = new Paint(); 
	                textPaint.setColor(Color.YELLOW); 
	                textPaint.setTextSize(24); 
	                String familyName = "宋体"; 
	                Typeface typeface = Typeface.create(familyName, 
	                        Typeface.BOLD_ITALIC); 
	                textPaint.setTypeface(typeface); 
	     //           textPaint.setTextAlign(Align.CENTER); 
	                if(labelLocation.equals("rightDown")){
	                	mCanvas.drawText(text[1], w-250, h - 55, textPaint);
	                }else if(labelLocation.equals("leftDown")){
	                	mCanvas.drawText(text[1], 25, h - 55, textPaint);
	                }else if(labelLocation.equals("rightUp")){
	                	mCanvas.drawText(text[1], w-250, 70, textPaint);
	                }else if(labelLocation.equals("leftUp")){
	                	mCanvas.drawText(text[1], 25, 70, textPaint);
	                }
	            }
        	}
        	if(text.length>=3){
        		if(text[2]!=null){
	            	Paint textPaint = new Paint(); 
	                textPaint.setColor(Color.YELLOW); 
	                textPaint.setTextSize(24); 
	                String familyName = "宋体"; 
	                Typeface typeface = Typeface.create(familyName, 
	                        Typeface.BOLD_ITALIC); 
	                textPaint.setTypeface(typeface); 
	     //           textPaint.setTextAlign(Align.CENTER); 
	                if(labelLocation.equals("rightDown")){
	                	mCanvas.drawText(text[2], w-250, h - 25, textPaint);
	                }else if(labelLocation.equals("leftDown")){
	                	mCanvas.drawText(text[2], 25, h - 25, textPaint);
	                }else if(labelLocation.equals("rightUp")){
	                	mCanvas.drawText(text[2], w-250, 100, textPaint);
	                }else if(labelLocation.equals("leftUp")){
	                	mCanvas.drawText(text[2], 25, 100, textPaint);
	                }
	            }
        	}
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
