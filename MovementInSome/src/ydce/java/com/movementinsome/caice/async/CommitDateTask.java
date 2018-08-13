package com.movementinsome.caice.async;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.model.LatLng;
import com.movementinsome.AppContext;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.util.Bd09toArcgis;
import com.movementinsome.caice.util.DateUtil;
import com.movementinsome.caice.util.GPSUtils;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.kernel.initial.model.Attribute;
import com.movementinsome.kernel.initial.model.Movetype;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommitDateTask extends AsyncTask<String, Void, String> {
	private Context context;
	private ProgressDialog progressDialog;
	private String fileNameStr;
	private List<SavePointVo> savePointVoList;
	private Movetype movetype;
	private String pointType;
    private String projectName;
    private String filePath="";

	public CommitDateTask(Context context
            ,List<SavePointVo> savePointVoList,Movetype movetype,String pointType,String projectName) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.savePointVoList=savePointVoList;
		this.movetype=movetype;
		this.pointType=pointType;
		this.projectName=projectName;
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
        String result = "Ok";
        filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/" + "taskRecord" + "/" + AppContext.getInstance().getCurUserName() +
                "/" + projectName + "/" + "doc" + "/" + DateUtil.getNow();
        try {

            if (savePointVoList.size() > 0) {
                for (int i = 0; i < savePointVoList.size(); i++) {
                    SavePointVo savePointVo = savePointVoList.get(i);
                    JSONObject context = JSON.parseObject(savePointVo.getContext());     //装有表单填写的字段的json

                    if (movetype.getType().equals(MapMeterMoveScope.LINE)) {
                        String facNameList = savePointVo.getFacNameList();
                        String[] facNames = facNameList.split(",");
                        if (facNames.length > 0) {
                            context.put(OkHttpParam.FIRST_FAC_NAME, facNames[0]);
                            context.put(OkHttpParam.END_FAC_NAME, facNames[facNames.length - 1]);
                        }
                    }
                    context.put(OkHttpParam.UPLOAD_TIME, savePointVo.getFacPipBaseVo().getUploadTime());
                    context.put(OkHttpParam.UPLOAD_NAME,
                            AppContext.getInstance().getCurUser().getUserAlias()
                            +AppContext.getInstance().getCurUser().getJobNumber()
                    );

                    if (pointType.equals(OkHttpParam.WGS84)) {
                        String longitudeWgs84 = savePointVo.getLongitudeWg84();
                        String latitudeWgs84 = savePointVo.getLatitudeWg84();

                        if (longitudeWgs84 != null && !longitudeWgs84.equals("") && latitudeWgs84 != null && !latitudeWgs84.equals("")) {
                            Double longitudeWgs84_double = Double.parseDouble(longitudeWgs84);
                            Double latitudeWgs84_double = Double.parseDouble(latitudeWgs84);

                            String longitudeWg84_du = GPSUtils.getDegrees(longitudeWgs84_double);
                            String longitudeWg84_fen = GPSUtils.getMinutes(longitudeWgs84_double);
                            String longitudeWg84_miao = GPSUtils.getSeconds(longitudeWgs84_double);

                            String latitudeWg84_du = GPSUtils.getDegrees(latitudeWgs84_double);
                            String latitudeWg84_fen = GPSUtils.getMinutes(latitudeWgs84_double);
                            String latitudeWg84_miao = GPSUtils.getSeconds(latitudeWgs84_double);

                            context.put(OkHttpParam.LONGITUDEWG84_DU, longitudeWg84_du);
                            context.put(OkHttpParam.LONGITUDEWG84_FEN, longitudeWg84_fen);
                            context.put(OkHttpParam.LONGITUDEWG84_MIAO, longitudeWg84_miao);

                            context.put(OkHttpParam.LATITUDEWG84_DU, latitudeWg84_du);
                            context.put(OkHttpParam.LATITUDEWG84_FEN, latitudeWg84_fen);
                            context.put(OkHttpParam.LATITUDEWG84_MIAO, latitudeWg84_miao);

                            context.put(OkHttpParam.LONGITUDE_WG84, longitudeWgs84);
                            context.put(OkHttpParam.LATITUDE_WG84, latitudeWgs84);
                        } else {
                            if (!savePointVo.getLongitude().equals("") && !savePointVo.getLatitude().equals("")) {
                                LatLng latLng = new LatLng(Double.parseDouble(savePointVo.getLatitude())
                                        , Double.parseDouble(savePointVo.getLongitude()));
                                Map<String, Double> map_ = Bd09toArcgis.bd09ToWg84(latLng);

                                context.put(OkHttpParam.LONGITUDE_WG84, map_.get("lon"));
                                context.put(OkHttpParam.LATITUDE_WG84, map_.get("lat"));
                            }
                        }

                    } else if (pointType.equals("arc")) {

//                    Toast.makeText(this, "单表导出只允许导出WGS84坐标", Toast.LENGTH_SHORT).show();
                        return "1";
                    } else if (pointType.equals("")) {
//                    Toast.makeText(this, "请选择坐标类型", Toast.LENGTH_SHORT).show();
                        return "2";
                    }

                    List<Attribute> attributeList = movetype.getAttribute();
                    if (attributeList != null && attributeList.size() > 0) {
                        Map<String, String> pointMap = new HashMap<>(savePointVoList.size());
                        pointMap.put("$" + OkHttpParam.UPLOAD_NAME + "$", context.getString(OkHttpParam.UPLOAD_NAME));
                        pointMap.put("$" + OkHttpParam.UPLOAD_TIME + "$", DateUtil.currDay());
                        for (int j = 0; j < attributeList.size(); j++) {
                            if (attributeList.get(j).getSourceValue().equals("json")) {
                                pointMap.put("$" + attributeList.get(j).getValue() + "$", context.getString(attributeList.get(j).getValue()));
                            }
//                            else if (attributeList.get(j).getSourceValue().equals("entity")) {
//                                if (attributeList.get(j).getType() != null && attributeList.get(j).getType().equals("date")) {
//                                    String con = JSON.toJSONString(savePointVo);
//                                    JSONObject jsonObject = JSON.parseObject(con);
//                                    String dateTimeStr = (String) jsonObject.get(attributeList.get(j).getValue());
//                                    String[] dateTimeList = dateTimeStr.split(" ");
//                                    pointMap.put(attributeList.get(j).getValue(), dateTimeList[0]);
//                                } else if (attributeList.get(j).getType() != null && attributeList.get(j).getType().equals("time")) {
//                                    String con = JSON.toJSONString(savePointVo);
//                                    JSONObject jsonObject = JSON.parseObject(con);
//                                    String dateTimeStr = (String) jsonObject.get("uploadTime");
//                                    String[] dateTimeList = dateTimeStr.split(" ");
//                                    pointMap.put(attributeList.get(j).getValue(), dateTimeList[1]);
//                                } else if (attributeList.get(j).getType() != null && attributeList.get(j).getType().equals("firstFacNum")) {
//                                    String dateStr = savePointVo.getFacNameList();
//                                    if (dateStr != null && !dateStr.equals("")) {
//                                        String[] dateTimeList = dateStr.split(",");
//                                        if (dateTimeList != null && dateTimeList.length > 0) {
//                                            pointMap.put(attributeList.get(j).getValue(), dateTimeList[0]);
//                                        }
//                                    }
//                                } else if (attributeList.get(j).getType() != null && attributeList.get(j).getType().equals("endFacNum")) {
//                                    String dateStr = savePointVo.getFacNameList();
//                                    if (dateStr != null && !dateStr.equals("")) {
//                                        String[] dateTimeList = dateStr.split(",");
//                                        if (dateTimeList != null && dateTimeList.length > 0) {
//                                            pointMap.put(attributeList.get(j).getValue(), dateTimeList[dateTimeList.length - 1]);
//                                        }
//                                    }
//                                } else {
//                                    String con = JSON.toJSONString(savePointVo);
//                                    JSONObject jsonObject = JSON.parseObject(con);
//                                    pointMap.put(attributeList.get(j).getValue(), (String) jsonObject.get(attributeList.get(j).getValue()));
//                                }
//                            }
                        }
                        text(pointMap);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = "error";
        }

        return result;
    }

    private void text(Map<String, String> textMap) throws IOException {

        //从assets读取我们的Word模板
        InputStream is = context.getAssets().open("CensusTab.doc");
        //创建生成的文件
        File newFile = new File(
                filePath+"/"+  Calendar.getInstance().getTime().getTime()  + ".doc"
        );
        writeDoc(is, newFile, textMap);
    }

    /**
     * newFile 生成文件
     * map 要填充的数据
     */
    public void writeDoc(InputStream in, File newFile, Map<String, String> map) throws IOException {

            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }

        HWPFDocument hdt = new HWPFDocument(in);
        // Fields fields = hdt.getFields();
        // 读取word文本内容
        Range range = hdt.getRange();
        // System.out.println(range.text());

        // 替换文本内容
        for (Map.Entry<String, String> entry : map.entrySet()) {
            range.replaceText(entry.getKey(), entry.getValue());
        }
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        FileOutputStream out = new FileOutputStream(newFile, true);
        hdt.write(ostream);
        // 输出字节流
        out.write(ostream.toByteArray());
        out.close();
        ostream.close();
    }

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
		if(result!=null&&result.equals("Ok")){
			Toast.makeText(context,"导出到"+filePath+"成功", Toast.LENGTH_SHORT).show();
		}else if(result!=null&&result.equals("0")){
			Toast.makeText(context,"选择的日期内没有相应的任务", Toast.LENGTH_SHORT).show();
		}else if(result!=null&&result.equals("1")){
            Toast.makeText(context,"单表导出只允许导出WGS84坐标", Toast.LENGTH_SHORT).show();
        }else if(result!=null&&result.equals("2")){
            Toast.makeText(context,"请选择坐标类型", Toast.LENGTH_SHORT).show();
        }else{
			Toast.makeText(context,"导出失败", Toast.LENGTH_SHORT).show();
			File file = new File(fileNameStr);
			if(file.exists()){
				file.delete();
			}
		}
	}
}
