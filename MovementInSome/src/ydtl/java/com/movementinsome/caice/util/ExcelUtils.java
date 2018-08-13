package com.movementinsome.caice.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelUtils {
	public static WritableFont arial14font = null;

	public static WritableCellFormat arial14format = null;
	public static WritableFont arial10font = null;
	public static WritableCellFormat arial10format = null;
	public static WritableFont arial12font = null;
	public static WritableCellFormat arial12format = null;

	public final static String UTF8_ENCODING = "UTF-8";
	public final static String GBK_ENCODING = "GBK";


	/**
	 * 单元格的格式设置 字体大小 颜色 对齐方式、背景颜色等...
	 */
	public static void format() {
		try {
			arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
			arial14font.setColour(jxl.format.Colour.LIGHT_BLUE);
			arial14format = new WritableCellFormat(arial14font);
			arial14format.setAlignment(jxl.format.Alignment.CENTRE);
			arial14format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			arial14format.setBackground(jxl.format.Colour.VERY_LIGHT_YELLOW);

			arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
			arial10format = new WritableCellFormat(arial10font);
			arial10format.setAlignment(jxl.format.Alignment.CENTRE);
			arial10format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			arial10format.setBackground(Colour.GRAY_25);

			arial12font = new WritableFont(WritableFont.ARIAL, 10);
			arial12format = new WritableCellFormat(arial12font);
			arial10format.setAlignment(jxl.format.Alignment.CENTRE);//对齐格式
			arial12format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); //设置边框

		} catch (WriteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化Excel
	 * @param fileName
	 * @param colName
     */
	public static void initExcel(String fileName, String[] colName) {
		format();
		WritableWorkbook workbook = null;
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			workbook = Workbook.createWorkbook(file);
			WritableSheet sheet = workbook.createSheet("点线成果表", 0);
			//创建标题栏
			sheet.addCell((WritableCell) new Label(0, 0, fileName,arial14format));
			for (int col = 0; col < colName.length; col++) {
				sheet.addCell(new Label(col, 0, colName[col], arial10format));
			}
			sheet.setRowView(0,340); //设置行高

			workbook.write();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T>  String  writeObjListToExcel(List<T> objList,String fileName, Context c) {
		if (objList != null && objList.size() > 0) {
			WritableWorkbook writebook = null;
			InputStream in = null;
			try {
				WorkbookSettings setEncode = new WorkbookSettings();
				setEncode.setEncoding(UTF8_ENCODING);
				in = new FileInputStream(new File(fileName));
				Workbook workbook = Workbook.getWorkbook(in);
				writebook = Workbook.createWorkbook(new File(fileName), workbook);
				WritableSheet sheet = writebook.getSheet(0);

//				sheet.mergeCells(0,1,0,objList.size()); //合并单元格
//				sheet.mergeCells()


				for (int j = 0; j < objList.size(); j++) {
					ArrayList<String> list = (ArrayList<String>) objList.get(j);
					for (int i = 0; i < list.size(); i++) {
						sheet.addCell(new Label(i, j + 1, list.get(i), arial12format));

						if (list.get(i).length() <= 5) {
							sheet.setColumnView(i, list.get(i).length() + 8); //设置列宽
						} else {
							sheet.setColumnView(i, list.get(i).length() + 5); //设置列宽
						}
					}

//					//插入图片数据
//					final double cellSpace = 0.02;//图片之间的间隔 占比
//
//					double picWidthMax = 0;
//					double picHeightSum = 0;//空出图片 离上下边框的距离
//					String imagePathStr=list.get(list.size()-1);
//					String [] pictureFilePaths = null;
//					if (imagePathStr!=null&&!imagePathStr.equals("")&&!imagePathStr.equals("无值")){
//						pictureFilePaths=imagePathStr.split(",");
//					}
//					if (pictureFilePaths!=null){
////							ImgFile[] imgFiles = new ImgFile[pictureFilePaths.length];
//
//						double widthStart = cellSpace;//开始宽度
//						double heightStart = cellSpace;//开始高度
//						//插入图片
//						for (String fileStr : pictureFilePaths) {
////							File file=new File(fileStr);
//							File file=new File("/storage/emulated/0/DCIM/IMG_2017-12-01_cab7d7c8-15cf-4745-91d8-e1f29e6f1c34.png");
//							if (file.exists()){
//								// 读入图片 ,
////								BufferedImage picImage = ImageIO.read(file);
////								ByteArrayOutputStream pngByteArray = new ByteArrayOutputStream();
////								//将其他图片格式写成png的形式
////								ImageIO.write(picImage,"PNG", pngByteArray);
//
//								double heigthFact = picHeightSum;//实际高度
//								double widthFact =  picWidthMax;
//								//图片高度压缩了cellSpace+moreHeight,目的是为了该图片高度不超出单元格
//								if (heightStart + heigthFact >= 1) {
//									double moreHeight = heightStart + heigthFact - 1.00;
//									heigthFact -= moreHeight;
//									heigthFact -= cellSpace;
//								}
//								//图片宽度压缩了cellSpace,目的是为了该图片宽度不超出单元格
//								if (widthFact >= 1) {
//									widthFact -= cellSpace;
//								}
//								//生成图片对象
//								WritableImage image = new WritableImage( 4,  4,
//										3, 3, file);
//								//将图片对象插入到sheet
//								sheet.addImage(image);
//								//开始高度累加，获取下一张图片的起始高度（相对该单元格）
//								heightStart += heigthFact;
//								heightStart += cellSpace;//图片直接间隔为cellSpace
//
//							}
//						}
//					}

					sheet.setRowView(j + 1, 350); //设置行高
				}


				writebook.write();
				return "ok";
			} catch (Exception e) {
				e.printStackTrace();
				return "no";
			} finally {
				if (writebook != null) {
					try {
						writebook.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		return "ok";
	}

	//----------------------------------读------------------------------------

//	public static List<BillObject> read2DB(File f, Context con) {
//		ArrayList<BillObject> billList = new ArrayList<BillObject>();
//		try {
//			Workbook course = null;
//			course = Workbook.getWorkbook(f);
//			Sheet sheet = course.getSheet(0);
//
//			Cell cell = null;
//			for (int i = 1; i < sheet.getRows(); i++) {
//				BillObject tc = new BillObject();
//				cell = sheet.getCell(1, i);
//				tc.setFood(cell.getContents());
//				cell = sheet.getCell(2, i);
//				tc.setClothes(cell.getContents());
//				cell = sheet.getCell(3, i);
//				tc.setHouse(cell.getContents());
//				cell = sheet.getCell(4, i);
//				tc.setVehicle(cell.getContents());
//				Log.d("gaolei", "Row"+i+"---------"+tc.getFood() + tc.getClothes()
//						+ tc.getHouse() + tc.getVehicle());
//				billList.add(tc);
//
//			}
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return billList;
//	}
//
//	public static Object getValueByRef(Class cls, String fieldName) {
//		Object value = null;
//		fieldName = fieldName.replaceFirst(fieldName.substring(0, 1), fieldName
//				.substring(0, 1).toUpperCase());
//		String getMethodName = "get" + fieldName;
//		try {
//			Method method = cls.getMethod(getMethodName);
//			value = method.invoke(cls);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return value;
//	}
}
