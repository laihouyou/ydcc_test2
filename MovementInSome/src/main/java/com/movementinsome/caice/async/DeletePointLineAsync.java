package com.movementinsome.caice.async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.movementinsome.app.pub.Constant;
import com.movementinsome.caice.okhttp.OkHttpRequest;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.database.vo.DynamicFormVO;
import com.movementinsome.map.MapViewer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**管线删除异步任务
 * Created by zzc on 2017/6/15.
 */

public class DeletePointLineAsync extends AsyncTask<String, Void, String>{
    private Activity context;
    private ProgressDialog progressDialog;
    private List<SavePointVo> savePointVoList;      //一个工程里所有的点线数据，没有按天分开
    private List<DynamicFormVO> dynamicFormList;

    private String acquisitionState;
    private int pos;
    private boolean isSpreads; // 是否是侧滑
//    private String ids;
    private SavePointVo savePointVo;


    public DeletePointLineAsync(Context context,
                                List<SavePointVo> savePointVoList,
                                boolean isSpreads,int pos
    ) throws SQLException {
        this.context = (Activity) context;
        this.savePointVoList =savePointVoList;
        this.pos=pos;
        this.isSpreads=isSpreads;
//        this.ids=ids;
    }
//    public DeletePointLineAsync(Context context,
//                                SavePointVo statisticsChileListVo,
//                                boolean isSpreads,int pos
//    ) throws SQLException {
//        this.context = (Activity) context;
//        this.pos=pos;
//        this.isSpreads=isSpreads;
//        this.savePointVo =statisticsChileListVo;
//    }


    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("删除数据中，请稍后...");
        progressDialog.show();

    }

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        try {
//            if (isSpreads) {
//                if (savePointVo != null) {
//                    if (savePointVo.getFacSubmitStatus().equals("0")) {      //未提交
//                        PoiOperation.DeletePoi(savePointVoList);
////                        if (savePointVo.getDateType().equals(MapMeterMoveScope.POINT)) {
////                            PoiOperation.DeletePoi(savePointVo.getPointId());
////                        } else if (savePointVo.getDateType().equals(MapMeterMoveScope.LINE)) {
////                            PoiOperation.DeletePoi(savePointVo.getLineIds());
////                        }
//                    } else if (savePointVo.getFacSubmitStatus().equals("1")) {        //已提交
//                        OkHttpRequest.DeleteMarkerPointSync(savePointVoList,"");
////                        if (savePointVo.getDateType().equals(MapMeterMoveScope.POINT)) {
////                            //删除百度云
////                            OkHttpRequest.DeleteMarkerPointSync(
////                                    savePointVo.getPointId(), ""
////                            );
////                        } else if (savePointVo.getDateType().equals(MapMeterMoveScope.LINE)) {
////                            //删除百度云
////                            OkHttpRequest.DeleteMarkerPointSync(
////                                    savePointVo.getLineIds(), ""
////                            );
////                        }
//                    }
//                }
//            } else {
//                ids = "";       //提交服务器的ID集合
//                String dateBaseIds = "";       //只在本地删除的ID集合

                List<SavePointVo> chileListVos_BS = new ArrayList<>();  //提服务器数据
                List<SavePointVo> chileListVos_DB = new ArrayList<>();  //本地数据库操作数据


                if (savePointVoList != null && savePointVoList.size() > 0) {
                    //获取ids
                    for (int i = 0; i < savePointVoList.size(); i++) {

                        if (savePointVoList.get(i).getFacSubmitStatus().equals("0")) {
                            chileListVos_DB.add(savePointVoList.get(i));
                        } else if (savePointVoList.get(i).getFacSubmitStatus().equals("1")) {
                            chileListVos_BS.add(savePointVoList.get(i));
                        }
                    }

//                    if (chileListVos_BS.size() > 0) {
//                        for (int i = 0; i < chileListVos_BS.size(); i++) {
//                            if (i == chileListVos_BS.size() - 1) {
////                                ids += chileListVos_BS.get(i).getFacId();
////                                if (chileListVos_BS.get(i).getDateType()
////                                        .equals(MapMeterMoveScope.POINT)) {
////                                    ids += chileListVos_BS.get(i).getPointId();
////                                } else if (chileListVos_BS.get(i).getDateType()
////                                        .equals(MapMeterMoveScope.LINE)) {
////                                    ids += chileListVos_BS.get(i).getLineIds();
////                                }
//                            } else {
////                                ids += chileListVos_BS.get(i).getFacId() + ",";
////                                if (chileListVos_BS.get(i).getDateType()
////                                        .equals(MapMeterMoveScope.POINT)) {
////                                    ids += chileListVos_BS.get(i).getPointId() + ",";
////                                } else if (chileListVos_BS.get(i).getDateType()
////                                        .equals(MapMeterMoveScope.LINE)) {
////                                    ids += chileListVos_BS.get(i).getLineIds() + ",";
////                                }
//                            }
//                        }
//                    }
//
//                    if (chileListVos_DB.size() > 0) {
//                        for (int i = 0; i < chileListVos_DB.size(); i++) {
//                            if (i == chileListVos_DB.size() - 1) {
//                                dateBaseIds += chileListVos_DB.get(i).getFacId();
////                                if (chileListVos_DB.get(i).getDateType()
////                                        .equals(MapMeterMoveScope.POINT)) {
////                                    dateBaseIds += chileListVos_DB.get(i).getPointId();
////                                } else if (chileListVos_DB.get(i).getDateType()
////                                        .equals(MapMeterMoveScope.LINE)) {
////                                    dateBaseIds += chileListVos_DB.get(i).getLineIds();
////                                }
//                            } else {
//                                dateBaseIds += chileListVos_DB.get(i).getFacId() + ",";
////                                if (chileListVos_DB.get(i).getDateType()
////                                        .equals(MapMeterMoveScope.POINT)) {
////                                    dateBaseIds += chileListVos_DB.get(i).getPointId() + ",";
////                                } else if (chileListVos_DB.get(i).getDateType()
////                                        .equals(MapMeterMoveScope.LINE)) {
////                                    dateBaseIds += chileListVos_DB.get(i).getLineIds() + ",";
////                                }
//                            }
//                        }
//                    }

//                    if (chileListVos_DB.size()>0){
//                        PoiOperation.DeletePoi(chileListVos_DB);
//                    }

                    return OkHttpRequest.DeleteMarkerPointSync(
                            chileListVos_BS,chileListVos_DB
                    );
                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "0";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        switch (result){
            case "0":
                Intent intent=new Intent();
                intent.setAction(Constant.DELETE_DATE);
                intent.setPackage(context.getPackageName());
                intent.putExtra("pos", pos);
                intent.putExtra("isSpreads",isSpreads);
                context.sendBroadcast(intent);

                //更新任务列表
                MapViewer.moveProjcetUpdate();

                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                break;
            case "1":
                Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                break;
            case "2":
                Toast.makeText(context, "部分数据删除成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
