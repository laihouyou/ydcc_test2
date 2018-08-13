package com.movementinsome.easyform.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

import com.movementinsome.AppContext;
import com.movementinsome.kernel.util.MyDateTools;

public class CameraLogUtil {

/*	private String bizType;
	private String direction;
	private String fileName;
	private String logName;*/

/*	public CameraLogUtil(String bizType, String direction, String fileName) {
		this.bizType = bizType;
		this.direction = direction;
		this.fileName = fileName;
	}

*/
/*	public CameraLogUtil() {
		throw new RuntimeException("要用getInstance()获取对象");
	}*/
	
	private static CameraLogUtil cameraLogUtil = null;
	
	public static CameraLogUtil getInstance(){
		if (cameraLogUtil == null){
			cameraLogUtil = new CameraLogUtil();
		}
		return cameraLogUtil;
	}
	
	public void writeLog(String bizType, String direction, String fileName) {
		CameraLog cameraLog = new CameraLog();
		cameraLog.setBusinessType(bizType);
		cameraLog.setPhotographedDate(new Date());
		cameraLog.setPfiName(fileName);
		cameraLog.setPfiType("JPG");
		File file = new File(direction + "/" + fileName);
		cameraLog.setPfiSize((double) file.length());
/*		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			cameraLog.setPfiSize((double) fis.available());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		if (AppContext.getInstance().getCurLocation() != null) {
			cameraLog.setGpsPosition(AppContext.getInstance().getCurLocation().getCurGpsPosition());
			cameraLog.setPosition(AppContext.getInstance().getCurLocation().getCurMapPosition());
		}

		String logName = fileName.replace("jpg", "log");
		cameraLog.save(direction, logName);
	}

	/**
	 * 根据照片文件(全路径)读取图片日志文件
	 * 
	 * @param imgName
	 * @return
	 */
	public String getLog(String imgName) {
		String logName ="";
		if(imgName.endsWith(".jpg")){
			logName = imgName.replace("jpg", "log");
		}else if(imgName.endsWith(".png")){
			logName = imgName.replace("png", "log");
		}else if(imgName.endsWith(".JPG")){
			logName = imgName.replace("JPG", "log");
		}else if(imgName.endsWith(".PNG")){
			logName = imgName.replace("PNG", "log");
		}
		FileReader fileread;
		File file = new File(logName);
		try {
			fileread = new FileReader(file);
			BufferedReader bfr = new BufferedReader(fileread);
			try {
				String log = bfr.readLine();
				fileread.close();
				bfr.close();
				return log;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取文件名
	 * @param fileName 含路径
	 * @return 返回不含路径的文件名
	 */
	public String getFileName(String fileName){
		File file = new File(fileName);
		return file.getName();  
	}
	/**
	 * 获取文件扩展名
	 * @param fileName
	 * @return
	 */
	public String getFileExtend(String fileName){
		File file = new File(fileName);
		String tempName=file.getName();  
		return tempName.substring(tempName.lastIndexOf(".")+1).toUpperCase();
	}
	
	/**
	 * 获文件编辑日期
	 * @param fileName 含路径
	 * @return
	 */
	public Date getFileModifyDate(String fileName){
		File file = new File(fileName);
		Date date = new java.util.Date(file.lastModified());
        return date;
	}
	/**
	 * 获取文件大小
	 * @param fileName 含路径
	 * @return
	 * @throws Exception
	 */
	public long getFileSize(String fileName) throws Exception {
		File file = new File(fileName);
		return file.length();		
	}
	
	public long getFileSize(File f) throws Exception {
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	private String FormetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	private class CameraLog implements Serializable {
		// 业务类型
		private String businessType;
		// 本地空间位置
		private String position;
		// GPS空间位置
		private String gpsPosition;
		// 拍照时间
		private Date photographedDate;
		// 文件名
		private String pfiName;
		// 文件大小
		private Double pfiSize;
		// 文件类型（PNG、GIF、JPG）
		private String pfiType;

		public String getBusinessType() {
			return businessType;
		}

		public void setBusinessType(String businessType) {
			this.businessType = businessType;
		}

		public String getPosition() {
			return position;
		}

		public void setPosition(String position) {
			this.position = position;
		}

		public String getGpsPosition() {
			return gpsPosition;
		}

		public void setGpsPosition(String gpsPosition) {
			this.gpsPosition = gpsPosition;
		}

		public Date getPhotographedDate() {
			return photographedDate;
		}

		public void setPhotographedDate(Date photographedDate) {
			this.photographedDate = photographedDate;
		}

		public String getPfiName() {
			return pfiName;
		}

		public void setPfiName(String pfiName) {
			this.pfiName = pfiName;
		}

		public Double getPfiSize() {
			return pfiSize;
		}

		public void setPfiSize(Double pfiSize) {
			this.pfiSize = pfiSize;
		}

		public String getPfiType() {
			return pfiType;
		}

		public void setPfiType(String pfiType) {
			this.pfiType = pfiType;
		}

		public String toJson() {
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			sb.append("businessType:" + '"' + this.businessType + '"' + ",");
			sb.append("position:" + '"' + this.position + '"' + ",");
			sb.append("gpsPosition:" + '"' + this.gpsPosition + '"' + ",");
			sb.append("photographedDate:" + '"'
					+ MyDateTools.date2String(this.photographedDate)+ '"' + ",");
			sb.append("pfiName:" + '"' + this.pfiName + '"' + ",");
			sb.append("pfiSize:" + '"' + this.pfiSize.toString() + '"' + ",");
			sb.append("pfiType:" + '"' + this.pfiType.toString() + '"');
			sb.append("}");
			return sb.toString();
		}

		public void save(String direction, String logName) {
			FileWriter fw = null;
			BufferedWriter bw = null;
			String datetime = "";
			try {
				fw = new FileWriter(direction + "/" + logName, true);//
				// 创建FileWriter对象，用来写入字符流
				bw = new BufferedWriter(fw); // 将缓冲对文件的输出
				bw.write(this.toJson() + "\n"); // 写入文件
				bw.newLine();
				bw.flush(); // 刷新该流的缓冲
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					bw.close();
					fw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
				}
			}
		}
	}
}
