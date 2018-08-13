package com.movementinsome.caice.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.model.LatLng;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.dialog.DateTimeDialog;
import com.movementinsome.caice.async.CommitDateTask2;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.util.Bd09toArcgis;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.vo.MiningSurveyVO;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.commonAdapter.adapterView.CommonListViewAdapter;
import com.movementinsome.commonAdapter.adapterView.ViewHolder;
import com.movementinsome.database.vo.DynamicFormVO;
import com.movementinsome.kernel.initial.model.Attribute;
import com.movementinsome.kernel.initial.model.Movetype;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出工程
 * Created by zzc on 2017/6/1.
 */

public class ProjectExportActivity  extends Activity implements View.OnClickListener
        ,RadioGroup.OnCheckedChangeListener{
    private TextView projcet_name;
    private TextView start_time;
    private TextView end_time;
    private TextView exportTv;
    private RadioGroup radioGroup2;
    private RadioButton radioButtonWg84;
    private RadioButton radioButtonArc;
    private Button resubmit_back;
    private Spinner facType_spinner;    //设施下拉选择

    private Dao<MiningSurveyVO,Long> miningSurveyVoDao;
    private List<MiningSurveyVO> miningSurveyVoList;

    private Dao<SavePointVo,Long> savePointVoDao;
    private List<SavePointVo> savePointVoList;


    private Dao<DynamicFormVO, Long> dynamicFormDao;

    private String projectId;
    private String projectName;
    private MiningSurveyVO miningSurveyVO;

    private AlertDialog alertDiaChildlog;
    private ListView mListview;

    private String pointType= OkHttpParam.WGS84;

    private  List<Movetype> movetypeList = AppContext.getInstance().getMovetype();
    private Movetype movetype;
    private List<String> facTypeList=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_export);
        initView();
        try {
            initOath();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initOath() throws SQLException {
        miningSurveyVoDao= AppContext.getInstance().getAppDbHelper().getDao(MiningSurveyVO.class);
        miningSurveyVoList=new ArrayList<>();

        savePointVoDao= AppContext.getInstance().getAppDbHelper().getDao(SavePointVo.class);
        savePointVoList=new ArrayList<>();


        dynamicFormDao = AppContext.getInstance()
                .getAppDbHelper().getDao(DynamicFormVO.class);
    }

    private void initView() {
        projcet_name= (TextView) findViewById(R.id.projcet_name);
        start_time= (TextView) findViewById(R.id.start_time);
        end_time= (TextView) findViewById(R.id.end_time);
        exportTv= (TextView) findViewById(R.id.exportTv);
        radioGroup2= (RadioGroup) findViewById(R.id.radioGroup2);
        radioButtonWg84= (RadioButton) findViewById(R.id.radioButtonWg84);
        radioButtonArc= (RadioButton) findViewById(R.id.radioButtonArc);
        resubmit_back= (Button) findViewById(R.id.resubmit_back);
        facType_spinner= (Spinner) findViewById(R.id.facType_spinner);

        if (movetypeList!=null&&movetypeList.size()>0){
            for (int i = 0; i < movetypeList.size(); i++) {
                facTypeList.add(movetypeList.get(i).getName());
            }
        }
        movetype=movetypeList.get(0);

        final ArrayAdapter<String> qican_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, facTypeList);
        //设置下拉列表的风格
        qican_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        facType_spinner.setAdapter(qican_adapter);
        facType_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                movetype=movetypeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        projcet_name.setOnClickListener(this);
        start_time.setOnClickListener(this);
        end_time.setOnClickListener(this);
        exportTv.setOnClickListener(this);
        radioGroup2.setOnCheckedChangeListener(this);
        resubmit_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.projcet_name: //选择工程
                try {
                    miningSurveyVoList=miningSurveyVoDao.queryForEq("usedName",AppContext.getInstance().getCurUserName());
                    mListview=new ListView(this);
                    mListview.setAdapter(new CommonListViewAdapter<MiningSurveyVO>(this,R.layout.textview_item,miningSurveyVoList) {
                        @Override
                        protected void convert(ViewHolder viewHolder, MiningSurveyVO item, int position) {
                            viewHolder.setText(R.id.text,
                                    "工程名称："+item.getProjectName()
                            );
                        }
                    });

                    mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            projectId=miningSurveyVoList.get(position).getProjectId();
                            projectName=miningSurveyVoList.get(position).getProjectName();
                            miningSurveyVO=miningSurveyVoList.get(position);
                            alertDiaChildlog.dismiss();
                            projcet_name.setText(projectName);
                        }
                    });

                     alertDiaChildlog=new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                            .setView(mListview)
                            .show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.start_time: //开始时间
                DateTimeDialog.showDateDialog(this,start_time);
                break;
            case R.id.end_time: //结束时间
                DateTimeDialog.showDateDialog(this,end_time);
                break;
            case R.id.exportTv: //导出
                try {
                    exportProject();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.resubmit_back:    //返回
                finish();
                break;
        }

    }

    /**
     * 导出工程
     */
    private void exportProject() throws SQLException {
        if (projectId != null && !projectId.equals("")) {

            Map<String, Object> map = new HashMap<>();
            map.put(OkHttpParam.PROJECT_ID, projectId);
            if (movetype.getType()!=null&&movetype.getType().equals(MapMeterMoveScope.POINT)){
                map.put(OkHttpParam.IMPLEMENTORNAME,movetype.getName());
            }else if (movetype.getType()!=null&&movetype.getType().equals(MapMeterMoveScope.LINE)){
                map.put(OkHttpParam.PIP_TYPE,movetype.getName());
            }
            savePointVoList = savePointVoDao.queryForFieldValues(map);
            List<Map<String, String>> pointmapList = new ArrayList<>();
            if (savePointVoList.size() > 0) {
                for (int i = 0; i < savePointVoList.size(); i++) {
                    SavePointVo savePointVo = savePointVoList.get(i);
                    JSONObject context ;
                    if (savePointVo.getContext_leak()!=null&&!savePointVo.getContext_leak().equals("")){
                        context= JSON.parseObject(savePointVo.getContext_leak());     //装有表单填写的字段的json
                        if (pointType.equals(OkHttpParam.WGS84)) {
                            String longitudeWgs84=savePointVo.getLongitudeWg84();
                            String  latitudeWgs84=savePointVo.getLatitudeWg84();
                            if (longitudeWgs84!=null&&!longitudeWgs84.equals("")&&latitudeWgs84!=null&&!latitudeWgs84.equals("")){
                                context.put("longitude", longitudeWgs84);
                                context.put("latitude", latitudeWgs84);
                            }else {
                                LatLng latLng=new LatLng(Double.parseDouble(savePointVo.getLatitude())
                                        ,Double.parseDouble(savePointVo.getLongitude()));
                                Map<String,Double> map_ =  Bd09toArcgis.bd09ToWg84(latLng);

                                context.put("longitude", map_.get("lon"));
                                context.put("latitude", map_.get("lat"));
                            }

                        } else if (pointType.equals("arc")) {

                            context.put("longitude", savePointVo.getMapx());
                            context.put("latitude", savePointVo.getMapy());
                        } else if (pointType.equals("")) {
                            Toast.makeText(this, "请选择坐标类型", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        List<Attribute> attributeList = movetype.getAttribute();
                        if (attributeList != null && attributeList.size() > 0) {
                            Map<String, String> pointMap = new HashMap<>(savePointVoList.size());
                            for (int j = 0; j < attributeList.size(); j++) {
                                if (attributeList.get(j).getSourceValue().equals("json")) {
                                    pointMap.put(attributeList.get(j).getValue(), context.getString(attributeList.get(j).getValue()));
                                } else if (attributeList.get(j).getSourceValue().equals("entity")) {
                                    if (attributeList.get(j).getType() != null && attributeList.get(j).getType().equals("phone_leak")) {
                                        //联系电话
                                        pointMap.put(attributeList.get(j).getValue(), AppContext.getInstance().getCurUser().getPhone());
                                    } else if (attributeList.get(j).getType() != null && attributeList.get(j).getType().equals("uploadName_leak")) {
                                        //联系人 (确认人)
                                        pointMap.put(attributeList.get(j).getValue(),AppContext.getInstance().getCurUser().getUserName());
                                    }else if (attributeList.get(j).getType() != null && attributeList.get(j).getType().equals("uploadTime")) {
                                        //上传时间
                                        pointMap.put(attributeList.get(j).getValue(),savePointVo.getUploadTime_leak());
                                    } else if (attributeList.get(j).getType() != null && attributeList.get(j).getType().equals("firstFacNum")) {
                                        String dateStr = savePointVo.getFacNums();
                                        if (dateStr != null && !dateStr.equals("")) {
                                            String[] dateTimeList = dateStr.split(",");
                                            if (dateTimeList != null && dateTimeList.length > 0) {
                                                pointMap.put(attributeList.get(j).getValue(), dateTimeList[0]);
                                            }
                                        }
                                    } else if (attributeList.get(j).getType() != null && attributeList.get(j).getType().equals("endFacNum")) {
                                        String dateStr = savePointVo.getFacNums();
                                        if (dateStr != null && !dateStr.equals("")) {
                                            String[] dateTimeList = dateStr.split(",");
                                            if (dateTimeList != null && dateTimeList.length > 0) {
                                                pointMap.put(attributeList.get(j).getValue(), dateTimeList[dateTimeList.length - 1]);
                                            }
                                        }
                                    } else {
                                        String con = JSON.toJSONString(savePointVo);
                                        JSONObject jsonObject = JSON.parseObject(con);
                                        pointMap.put(attributeList.get(j).getValue(), (String) jsonObject.get(attributeList.get(j).getValue()));
                                    }
                                }
                            }
                            if (!pointmapList.contains(pointMap)){
                                pointmapList.add(pointMap);
                            }
                        }
                    }
                }
            }
            CommitDateTask2 commit = new CommitDateTask2(this,
                    pointmapList,
                    movetype,
                    projectName,
                    start_time.getText().toString(),
                    end_time.getText().toString()
            );
            commit.execute("");
//            else if (TAG.equals(MapMeterMoveScope.LINE)){
//                Map<String ,Object> map=new HashMap<>();
//                map.put(OkHttpParam.PROJECT_ID,projectId);
//                map.put("dataType",MapMeterMoveScope.LINE);
//                savePointVoList=savePointVoDao.queryForFieldValues(map);
//
//                List<Map<String,String>> linemapList=new ArrayList<>();
//                if (savePointVoList.size()>0){
//                    for (int i=0;i<savePointVoList.size();i++){
//                        SavePointVo savePointVo = savePointVoList.get(i);
//
//                        JSONObject context = JSON.parseObject(savePointVo.getContext());     //装有表单填写的字段的json
//
//                        //获取配置的导出字段名
//                        List<Attribute> attributeList = movetypeList.get(1).getAttribute();
//                        if (attributeList != null && attributeList.size() > 0) {
//                            Map<String, String> lineMap = new HashMap<>(savePointVoList.size());
//                            for (int j = 0; j < attributeList.size(); j++) {
//                                if (attributeList.get(j).getSourceValue().equals("json")) {
//                                    lineMap.put(attributeList.get(j).getValue(), context.getString(attributeList.get(j).getValue()));
//                                } else if (attributeList.get(j).getSourceValue().equals("entity")) {
//                                    if (attributeList.get(j).getType() != null && attributeList.get(j).getFacType().equals("date")) {
//                                        String con = JSON.toJSONString(savePointVo);
//                                        JSONObject jsonObject = JSON.parseObject(con);
//                                        String dateStr = (String) jsonObject.get(attributeList.get(j).getValue());
//                                        String[] dateTimeList = dateStr.split(" ");
//                                        lineMap.put(attributeList.get(j).getValue(), dateTimeList[0]);
//                                    } else if (attributeList.get(j).getType() != null && attributeList.get(j).getFacType().equals("time")) {
//                                        String con = JSON.toJSONString(savePointVo);
//                                        JSONObject jsonObject = JSON.parseObject(con);
//                                        String dateStr = (String) jsonObject.get("uploadTime");
//                                        String[] dateTimeList = dateStr.split(" ");
//                                        lineMap.put(attributeList.get(j).getValue(), dateTimeList[1]);
//                                    } else if (attributeList.get(j).getType() != null && attributeList.get(j).getFacType().equals("firstFacNum")) {
//                                        String dateStr = savePointVo.getFacNums();
//                                        if (dateStr != null && !dateStr.equals("")) {
//                                            String[] dateTimeList = dateStr.split(",");
//                                            if (dateTimeList != null && dateTimeList.length > 0) {
//                                                lineMap.put(attributeList.get(j).getValue(), dateTimeList[0]);
//                                            }
//                                        }
//                                    } else if (attributeList.get(j).getType() != null && attributeList.get(j).getFacType().equals("endFacNum")) {
//                                        String dateStr = savePointVo.getFacNums();
//                                        if (dateStr != null && !dateStr.equals("")) {
//                                            String[] dateTimeList = dateStr.split(",");
//                                            if (dateTimeList != null && dateTimeList.length > 0) {
//                                                lineMap.put(attributeList.get(j).getValue(), dateTimeList[dateTimeList.length - 1]);
//                                            }
//                                        }
//                                    } else {
//                                        String con = JSON.toJSONString(savePointVo);
//                                        JSONObject jsonObject = JSON.parseObject(con);
//                                        String dateStr = (String) jsonObject.get(attributeList.get(j).getValue());
//                                        lineMap.put(attributeList.get(j).getValue(), dateStr);
//                                    }
//                                }
//                            }
//                            if (!linemapList.contains(lineMap)) {
//                                linemapList.add(lineMap);
//                            }
//                        }
//                    }
//                }
//
//                CommitDateTask2 commit=new CommitDateTask2(this,
//                        null,
//                        linemapList,
//                        MapMeterMoveScope.LINE,
//                        projectName,
//                        start_time.getText().toString(),
//                        end_time.getText().toString()
//                );
//                commit.execute("");
//            }
//            else {
//                Toast.makeText(this,"请选择数据类型",Toast.LENGTH_SHORT).show();
//            }
        }else {
            Toast.makeText(this,"请选择工程",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (group.getId()){

            case R.id.radioGroup2:
                if (checkedId==radioButtonWg84.getId()){
                    pointType=OkHttpParam.WGS84;
                }else if (checkedId==radioButtonArc.getId()){
                    pointType="arc";
                }
                break;

        }
    }
}
