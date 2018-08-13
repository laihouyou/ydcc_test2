package com.movementinsome.caice.util;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.kernel.initial.model.CoordParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
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
	public static <T> void writeObjListToExcel(List<T> objList,String fileName, Context c) {
		if (objList != null && objList.size() > 0) {
			WritableWorkbook writebook = null;
			InputStream in = null;
			try {
				WorkbookSettings setEncode = new WorkbookSettings();
				setEncode.setEncoding(UTF8_ENCODING);
				in = new FileInputStream(new File(fileName));
				Workbook workbook = Workbook.getWorkbook(in);
				writebook = Workbook.createWorkbook(new File(fileName),workbook);
				WritableSheet sheet = writebook.getSheet(0);

//				sheet.mergeCells(0,1,0,objList.size()); //合并单元格
//				sheet.mergeCells()

				for (int j = 0; j < objList.size(); j++) {
					ArrayList<String> list = (ArrayList<String>) objList.get(j);
					for (int i = 0; i < list.size(); i++) {
						sheet.addCell(new Label(i, j + 1, list.get(i),arial12format));
						if (list.get(i).length() <= 5){
							sheet.setColumnView(i,list.get(i).length()+8); //设置列宽
						}else {
							sheet.setColumnView(i,list.get(i).length()+5); //设置列宽
						}
					}
					sheet.setRowView(j+1,350); //设置行高
				}

				writebook.write();
//				Toast.makeText(c, "导出到手机存储中文件夹Record成功", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
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
	}

	public static String readExcle(String filepath, int index, ProjectVo projectVo) throws IOException, BiffException, SQLException {
		Dao<SavePointVo,Long> savePointVoDao= AppContext.getInstance().getAppDbHelper().getDao(SavePointVo.class);
		List<SavePointVo> savePointVoList=new ArrayList<>();
		InputStream is=new FileInputStream(filepath);
		Workbook workbook=Workbook.getWorkbook(is);
		workbook.getNumberOfSheets();
		// 获得第一个工作表对象
		Sheet sheet = workbook.getSheet(index);
		if (index== 0){		//第一张表	点表
			int Rows = sheet.getRows();
			int error_pos=0;
			for (int i = 1; i < Rows; ++i) {
				try {
					//表号
					String facName = (sheet.getCell(0, i)).getContents();
					//设备名
					String implementorName = (sheet.getCell(1, i)).getContents();
					//X 坐标
					String mapxStr = (sheet.getCell(2, i)).getContents();
					//Y坐标
					String mapyStr = (sheet.getCell(3, i)).getContents();
					//地面高程
					String groundElevation = (sheet.getCell(4, i)).getContents();
					//埋深
					String burialDepthStr = (sheet.getCell(5, i)).getContents();
					//口径
					String caliberStr = (sheet.getCell(6, i)).getContents();
					//类型
					String type = (sheet.getCell(7, i)).getContents();
					//安装时间
					String ukVoiceName = (sheet.getCell(8, i)).getContents();
					//发生地址
					String happenAddr = (sheet.getCell(9, i)).getContents();
					//行政区
					String administrativeRegion = (sheet.getCell(10, i)).getContents();

					double mapX=Double.parseDouble(mapxStr);
					double mapY=Double.parseDouble(mapyStr);
					CoordParam coordParam= projectVo.getCoordTransform();
					Map<String,Object> map=new HashMap();
					map.put(OkHttpParam.PROJECT_ID, projectVo.getProjectId());
					map.put(OkHttpParam.FAC_NAME,facName);
					List<SavePointVo> savePointVos=savePointVoDao.queryForFieldValues(map);
//					if (savePointVos.size()==0&&coordParam!=null){
//						SavePointVo savePointVo=new SavePointVo();
//						savePointVo.setFacId(UUIDUtil.getUUID());
//						savePointVo.setFacName(facName);
//						savePointVo.setImplementorName(implementorName);
//						savePointVo.setFacType(type);
//
//
//
//						savePointVo.setGroundElevation(groundElevation);
//						savePointVo.setBurialDepth(burialDepthStr);
//						savePointVo.setCaliber(caliberStr);
//						savePointVo.setUploadTime(ukVoiceName);
//						savePointVo.setHappenAddr(happenAddr);
//						savePointVo.setAdministrativeRegion(administrativeRegion);
//
//						savePointVo.setMapx(mapX+"");
//						savePointVo.setMapy(mapY+"");
//						String bdPoint= ArcgisToBd09.toBd09Position(coordParam,null,mapX,mapY);
//						String[] bd09Point=bdPoint.split(" ");
//						final LatLng latLng=new LatLng(Double.parseDouble(bd09Point[1])
//								, Double.parseDouble(bd09Point[0]));
//						savePointVo.setLongitude(latLng.longitude+"");
//						savePointVo.setLatitude(latLng.latitude+"");
//						Map<String,Double> map_wgs84=Bd09toArcgis.bd09ToWg84(latLng);
//						savePointVo.setLongitudeWg84(map_wgs84.get("lon")+"");
//						savePointVo.setLatitudeWg84(map_wgs84.get("lat")+"");
//
//						savePointVo.setDataType(MapMeterMoveScope.POINT);
//						savePointVo.setGatherType(AppContext.getInstance().getString(R.string.manual_import));
//
//						//公共属性
//						savePointVo.setUploadName(AppContext.getInstance().getCurUserName());
//						savePointVo.setUserId(AppContext.getInstance().getCurUser().getUserId());
//						savePointVo.setUploadTime(DateUtil.getNow());   //上传时间
//						savePointVo.setProjectId(projectVo.getProjectId());
//						savePointVo.setProjectName(projectVo.getProjectName());
////						savePointVo.setIsProjectShare(projectVo.getIsProjectShare());
//						savePointVo.setProjectType(projectVo.getProjectType());
//						savePointVo.setShareCode(projectVo.getProjectShareCode());
//
//						//测试代码
//						Map<String, Object> map1 = new HashMap<>();
//						map1.put(OkHttpParam.IS_PRESENT, "1");    ////提交成功
//						map1.put(OkHttpParam.IS_COMPILE, "0");
//						PoiOperation.PoiCreate(savePointVo, map);
//						//测试代码
//
//						savePointVoList.add(savePointVo);
//
////						if (savePointVoList.size()>=100){
////							if (OkHttpRequest.SubmitPointListCreate(savePointVoList,AppContext.getInstance())){
////								savePointVoList.clear();
////							}
////						}
//					}else {
//						throw new Exception();
//					}

				}catch (Exception e){
					e.printStackTrace();
					error_pos+=1;
				}
			}
			return (Rows-error_pos)+AppContext.getInstance().getString(R.string.imported_successfully)
					+AppContext.getInstance().getString(R.string.comma)
					+error_pos+AppContext.getInstance().getString(R.string.imported_error);
		}else if (index==1){		//第二张表	线表
			int Rows = sheet.getRows();
			int error_pos=0;
			for (int i = 1; i < Rows; ++i) {
				try {
					//本点号
					String this_period = (sheet.getCell(0, i)).getContents();
					//下点号
					String under_the_dot = (sheet.getCell(1, i)).getContents();
					//管线类型
					String pipType = (sheet.getCell(2, i)).getContents();
					//口径
					String caliberStr = (sheet.getCell(3, i)).getContents();
					//管材
					String tubularProduct = (sheet.getCell(4, i)).getContents();
					//敷设类型
					String layingType = (sheet.getCell(5, i)).getContents();
					//安装时间
					String impdate = (sheet.getCell(6, i)).getContents();
					//所在道路
					String happenAddr = (sheet.getCell(7, i)).getContents();
					//行政区
					String administrativeRegion = (sheet.getCell(8, i)).getContents();

					CoordParam coordParam= projectVo.getCoordTransform();
					Map<String,Object> map_this_period=new HashMap();
					map_this_period.put(OkHttpParam.PROJECT_ID, projectVo.getProjectId());
					map_this_period.put(OkHttpParam.FAC_NAME,this_period);
					map_this_period.put(OkHttpParam.GATHER_TYPE,AppContext.getInstance().getString(R.string.manual_import));
					List<SavePointVo> savePointVos_this_period=savePointVoDao.queryForFieldValues(map_this_period);

					Map<String,Object> map_under_the_dot=new HashMap();
					map_under_the_dot.put(OkHttpParam.PROJECT_ID, projectVo.getProjectId());
					map_under_the_dot.put(OkHttpParam.FAC_NAME,under_the_dot);
					map_under_the_dot.put(OkHttpParam.GATHER_TYPE,AppContext.getInstance().getString(R.string.manual_import));
					List<SavePointVo> savePointVos_under_the_dot=savePointVoDao.queryForFieldValues(map_under_the_dot);

//					if (savePointVos_this_period.size()==1&&savePointVos_under_the_dot.size()==1&&coordParam!=null){
//
//						String pointList=savePointVos_this_period.get(0).getLongitude()
//								+" "
//								+savePointVos_this_period.get(0).getLatitude()
//								+","
//								+savePointVos_under_the_dot.get(0).getLongitude()
//								+" "
//								+savePointVos_under_the_dot.get(0).getLatitude();
//
//						String ids=savePointVos_this_period.get(0).getId()
//								+","
//								+savePointVos_under_the_dot.get(0).getId();
//
//						String pipName=savePointVos_this_period.get(0).getFacName()
//								+"_"
//								+savePointVos_under_the_dot.get(0).getFacName();
//
//						LatLng latLng_this_period=new LatLng(
//								Double.parseDouble(savePointVos_this_period.get(0).getLatitude()),
//								Double.parseDouble(savePointVos_this_period.get(0).getLongitude()));
//						LatLng latLng_under_the_dot=new LatLng(
//								Double.parseDouble(savePointVos_under_the_dot.get(0).getLatitude()),
//								Double.parseDouble(savePointVos_under_the_dot.get(0).getLongitude()));
//						double lineLength= 0;
//						lineLength=Arith.add(lineLength, BaiduMapUtil.getDistanceOfMeter(
//								latLng_this_period.latitude,
//								latLng_this_period.longitude,
//								latLng_under_the_dot.latitude,
//								latLng_under_the_dot.longitude));;
//
//						SavePointVo savePointVo=new SavePointVo();
//						savePointVo.setId(UUIDUtil.getUUID());
//						savePointVo.setPipName(pipName);
//						savePointVo.setPipMaterial(tubularProduct);
//						savePointVo.setPipType(pipType);
//						savePointVo.setLayingType(layingType);
//						savePointVo.setPointList(pointList);
//						savePointVo.setIds(ids);
//						savePointVo.setCaliber(caliberStr);
//						savePointVo.setHappenAddr(happenAddr);
//						savePointVo.setAdministrativeRegion(administrativeRegion);
//						savePointVo.setPipelineLinght(lineLength);
//
//						savePointVo.setDataType(MapMeterMoveScope.LINE);
//						savePointVo.setGatherType(AppContext.getInstance().getString(R.string.manual_import));
//
//						//公共属性
//						savePointVo.setUploadName(AppContext.getInstance().getCurUserName());
//						savePointVo.setUserId(AppContext.getInstance().getCurUser().getUserId());
//						savePointVo.setUploadTime(DateUtil.getNow());   //上传时间
//						savePointVo.setProjectId(projectVo.getProjectId());
//						savePointVo.setProjectName(projectVo.getProjectName());
////						savePointVo.setIsProjectShare(projectVo.getIsProjectShare());
//						savePointVo.setProjectType(projectVo.getProjectType());
//						savePointVo.setShareCode(projectVo.getProjectShareCode());
//
//						savePointVoList.add(savePointVo);
//						if (savePointVoList.size()>=100){
//							if (OkHttpRequest.SubmitPointListCreate(savePointVoList,AppContext.getInstance())){
//								savePointVoList.clear();
//							}
//						}
//					}else {
//						throw new Exception();
//					}

				}catch (Exception e){
					e.printStackTrace();
					error_pos+=1;
				}
			}
			return (Rows-error_pos)+AppContext.getInstance().getString(R.string.imported_successfully)
					+AppContext.getInstance().getString(R.string.comma)
					+error_pos+AppContext.getInstance().getString(R.string.imported_error);
		}
		workbook.close();

		return AppContext.getInstance().getString(R.string.imported_error2);
	}


}
