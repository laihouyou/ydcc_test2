package com.movementinsome.caice.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.caice.util.CreateFiles;
import com.movementinsome.caice.util.DirTraversal;
import com.movementinsome.caice.util.ExcelUtils;
import com.movementinsome.caice.util.ZipUtils;
import com.movementinsome.caice.vo.MoveDateVO;
import com.movementinsome.kernel.initial.model.Movetype;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.movementinsome.caice.util.DateUtil.LONG_DATE_FORMAT;
import static com.movementinsome.caice.util.DateUtil.getBetweenDates;

public class CommitDateTask2 extends AsyncTask<String, Void, String> {
	private Context context;
	private ProgressDialog progressDialog;
	private String fileNameStr;
	private List<Map<String,String>> pointmapList;
	private String projectName;
	private String start_time;
	private String end_time;
	private List<Date> dateList;
	private Movetype movetype;
	private File file;

	public CommitDateTask2(Context context, List<Map<String,String>> pointmapList,
						   Movetype movetype,
						   String projectName,
						   String start_time,String end_time) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.pointmapList=pointmapList;
		this.projectName=projectName;
		this.start_time=start_time;
		this.end_time=end_time;
		this.movetype=movetype;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog = new ProgressDialog(context);
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("导出数据中，勿退出...");
		progressDialog.show();
		
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generted method stub
		String result="Ok";
		try {
			List<List<String>> moveDataStrList=new ArrayList<>();
			CreateFiles createFiles = new CreateFiles();
			List<MoveDateVO> moveDateList = new ArrayList<>();
			SimpleDateFormat formater = new SimpleDateFormat(LONG_DATE_FORMAT);

			SimpleDateFormat formater1 = new SimpleDateFormat(LONG_DATE_FORMAT);

			if (!start_time.equals("") && !end_time.equals("")) {

				Date ksDate = formater1.parse(start_time);
				Date jsDate = formater1.parse(end_time);
				dateList = getBetweenDates(ksDate, jsDate);
			}

			for (int i = 0; i < pointmapList.size(); i++) {
				try {
					MoveDateVO moveDateVO = new MoveDateVO();
					Date uploadTime = formater1.parse(pointmapList.get(i).get(movetype.getAttribute().get(0).getValue()));

					if (dateList != null && dateList.size() > 0) {
						for (int j = 0; j < dateList.size(); j++) {
							if (uploadTime != null) {
								if (uploadTime.equals(dateList.get(j))) {
									moveDateVO.setTime(formater.parse(pointmapList.get(i).get(movetype.getAttribute().get(0).getValue())).getTime());
									String moveData = "";
									List<String> moveDatas=new ArrayList<>();
									for (int k = 0; k < movetype.getAttribute().size(); k++) {
										String value = movetype.getAttribute().get(k).getValue();
										if (k == movetype.getAttribute().size() - 1) {
											if (pointmapList.get(i).get(value) == null ||
													pointmapList.get(i).get(value).equals("")) {
												moveData += "无值";
												moveDatas.add("无值");
											} else {
												moveData += pointmapList.get(i).get(value);
												moveDatas.add(pointmapList.get(i).get(value));
											}
										} else {
											if (pointmapList.get(i).get(value) == null ||
													pointmapList.get(i).get(value).equals("")) {
												moveData += "无值    ";
												moveDatas.add("无值");
											} else {
												moveData += pointmapList.get(i).get(value) + "   ";
												moveDatas.add(pointmapList.get(i).get(value));
											}
										}
									}
									moveDateVO.setMoveDate(moveData);
									moveDateList.add(moveDateVO);

									moveDataStrList.add(moveDatas);
								}
							}
						}
					} else {
						moveDateVO.setTime(formater.parse(pointmapList.get(i).get(movetype.getAttribute().get(0).getValue())).getTime());
						String moveData = "";
						List<String> moveDatas=new ArrayList<>();
						for (int k = 0; k < movetype.getAttribute().size(); k++) {
							String value = movetype.getAttribute().get(k).getValue();
							if (k == movetype.getAttribute().size() - 1) {
								if (pointmapList.get(i).get(value) == null ||
										pointmapList.get(i).get(value).equals("")) {
									moveData += "无值";
									moveDatas.add("无值");
								} else {
									moveData += pointmapList.get(i).get(value);
									moveDatas.add(pointmapList.get(i).get(value));
								}
							} else {
								if (pointmapList.get(i).get(value) == null ||
										pointmapList.get(i).get(value).equals("")) {
									moveData += "无值    ";
									moveDatas.add("无值");
								} else {
									moveData += pointmapList.get(i).get(value) + "   ";
									moveDatas.add(pointmapList.get(i).get(value));
								}
							}
						}
						moveDateVO.setMoveDate(moveData);
						moveDateList.add(moveDateVO);

						moveDataStrList.add(moveDatas);
					}

				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			if (moveDateList.size() > 0) {
				Collections.sort(moveDateList, new Comparator<MoveDateVO>() {
					@Override
					public int compare(MoveDateVO o1, MoveDateVO o2) {
						return o1.getTime().compareTo(o2.getTime());
					}
				});

				//在这里把表头加进去
				String tabHader = "";
				List<String> tabTitle=new ArrayList<>();
				for (int k = 0; k < movetype.getAttribute().size(); k++) {
//					if (k == movetype.getAttribute().size() - 1) {
//						tabHader += movetype.getAttribute().get(k).getName();
//					} else {
//						tabHader += movetype.getAttribute().get(k).getName() + "   ";
//					}
					tabTitle.add(movetype.getAttribute().get(k).getName());
				}

				String [] tabTitleData= tabTitle.toArray(new String[tabTitle.size()]);

				MoveDateVO moveDateVO = new MoveDateVO();
				moveDateVO.setMoveDate(tabHader);
				moveDateList.add(0, moveDateVO);

				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
				Date curDate = new Date(System.currentTimeMillis());//获取当前时间
				String str = formatter.format(curDate);
				File fileName = new File("/sdcard/taskRecord2/" + AppContext.getInstance().getCurUserName() + "/" + str );
//				String name = str + "" + "MoveTable";
				fileNameStr = fileName.getPath() +"/"+ str + ".xls";
				createFiles.CreateFiles2(fileName, "");

//				for (int i = 0; i < moveDateList.size(); i++) {
//					createFiles.print2(moveDateList.get(i).getMoveDate());
//				}

				ExcelUtils.initExcel(fileNameStr,tabTitleData);
				String rest=ExcelUtils.writeObjListToExcel(moveDataStrList,fileNameStr,context);
				if (rest.equals("ok")){
					//把图片批量复制到一个文件夹内
					if (moveDateList.size()>1){
						for (int i=0;i<moveDataStrList.size();i++){
							String gamePathList=moveDataStrList.get(i).get(moveDataStrList.get(i).size()-1);
							if (gamePathList!=null&&!gamePathList.equals("无值")){
								String[] gamePathStr=gamePathList.split(",");
								if (gamePathStr.length>0){
									for (String gamePath:gamePathStr){
										if (gamePath!=null&&!gamePath.equals("")){
											String[] gamePathDate=gamePath.split("/");
											String gameName="";
											if (gamePathDate!=null&&gamePathDate.length>0){
												gameName=gamePathDate[gamePathDate.length-1];
											}
											File file=new File(fileName.getPath()+"/photographs");
											if (!file.exists()){
												file.mkdirs();
											}
											CreateFiles.mCopyFile(gamePath,file.getPath()+"/"+gameName);
										}
									}
								}
							}
						}
						LinkedList<File> files = DirTraversal.listLinkedFiles(fileName.getPath());
						file = DirTraversal.getFilePath( "/sdcard/taskRecord/" + AppContext.getInstance().getCurUserName()+"/",str+".zip");


						if (fileName.exists()){
							int s= ZipUtils.zipFiles(files,file);
							if (s==0){
								CreateFiles.deleteDirectory(fileName.getPath());
							}
						}
					}
				}else if (rest.equals("no")){

				}

			} else {
				return "0";
			}

		}catch (Exception e){
			e.printStackTrace();
			return "1";
		}

		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
		if(result!=null&&result.equals("Ok")){
			Toast.makeText(context,"导出成功,数据保存在"+"/n"+file.getPath(), Toast.LENGTH_SHORT).show();
		}else if(result!=null&&result.equals("0")){
			Toast.makeText(context,"所选日期没有数据", Toast.LENGTH_SHORT).show();
		}else if(result!=null&&result.equals("1")){
			Toast.makeText(context,"导出失败", Toast.LENGTH_SHORT).show();
		}
	}
}
