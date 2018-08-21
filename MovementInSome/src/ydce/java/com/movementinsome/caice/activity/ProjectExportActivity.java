package com.movementinsome.caice.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Environment;
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
import com.amap.api.maps.model.LatLng;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.dialog.DateTimeDialog;
import com.movementinsome.caice.async.CommitDateTask;
import com.movementinsome.caice.async.CommitDateTask2;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.util.Bd09toArcgis;
import com.movementinsome.caice.util.DateUtil;
import com.movementinsome.caice.util.GPSUtils;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.commonAdapter.adapterView.CommonListViewAdapter;
import com.movementinsome.commonAdapter.adapterView.ViewHolder;
import com.movementinsome.database.vo.DynamicFormVO;
import com.movementinsome.kernel.activity.FullActivity;
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

public class ProjectExportActivity  extends FullActivity implements View.OnClickListener
        ,RadioGroup.OnCheckedChangeListener{
    private TextView projcet_name;
    private TextView start_time;
    private TextView end_time;
    private TextView exportTv;
    private TextView exportSingleTable;
    private RadioGroup radioGroup2;
    private RadioButton radioButtonWg84;
    private RadioButton radioButtonArc;
    private Button resubmit_back;
    private Spinner facType_spinner;    //设施下拉选择

    private Dao<ProjectVo,Long> miningSurveyVoDao;
    private List<ProjectVo> projectVoList;

    private Dao<SavePointVo,Long> savePointVoDao;
    private List<SavePointVo> savePointVoList;


    private Dao<DynamicFormVO, Long> dynamicFormDao;

    private String projectId;
    private String projectName;
    private ProjectVo projectVo;

    private AlertDialog alertDiaChildlog;
    private ListView mListview;

    private String pointType=OkHttpParam.WGS84;

    private  List<Movetype> movetypeList = AppContext.getInstance().getMovetype();
    private Movetype movetype;
    private List<String> facTypeList=new ArrayList<>();

    // 创建生成的文件地址
    private static final String newPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/doc/"+ DateUtil.getNow()+".doc";
    private static final String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/doc";

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
        miningSurveyVoDao= AppContext.getInstance().getAppDbHelper().getDao(ProjectVo.class);
        projectVoList =new ArrayList<>();

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
        exportSingleTable= (TextView) findViewById(R.id.exportSingleTable);
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
        exportSingleTable.setOnClickListener(this);
        exportSingleTable.setVisibility(AppContext.getInstance().getViewIsShow("单表导出"));
        radioGroup2.setOnCheckedChangeListener(this);
        resubmit_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.projcet_name: //选择工程
                try {
                    projectVoList =miningSurveyVoDao.queryForEq(OkHttpParam.USER_ID,
                            AppContext.getInstance().getCurUser().getUserId());
                    mListview=new ListView(this);
                    mListview.setAdapter(new CommonListViewAdapter<ProjectVo>(this,R.layout.textview_item, projectVoList) {
                        @Override
                        protected void convert(ViewHolder viewHolder, ProjectVo item, int position) {
                            viewHolder.setText(R.id.text,
                                    "工程名称："+item.getProjectName()
                            );
                        }
                    });

                    mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            projectId= projectVoList.get(position).getProjectId();
                            projectName= projectVoList.get(position).getProjectName();
                            projectVo = projectVoList.get(position);
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
            case R.id.exportSingleTable:    //导出单表
                try {
                    exportSingleTable();
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
     * 导出单表Word
     */
    private void exportSingleTable() throws SQLException {
        if (projectId != null && !projectId.equals("")) {
            Map<String, Object> map = new HashMap<>();
            map.put(OkHttpParam.PROJECT_ID, projectId);
            if (movetype.getType() != null && movetype.getType().equals(MapMeterMoveScope.POINT)) {
                map.put(OkHttpParam.IMPLEMENTORNAME, movetype.getName());
            } else if (movetype.getType() != null && movetype.getType().equals(MapMeterMoveScope.LINE)) {
                map.put(OkHttpParam.PIP_TYPE, movetype.getName());
            }
            savePointVoList = savePointVoDao.queryForFieldValues(map);

            CommitDateTask commitDateTask=new CommitDateTask(this,savePointVoList
                    ,movetype,pointType,projectName);
            commitDateTask.execute();
        } else {
            Toast.makeText(this, "请选择工程", Toast.LENGTH_SHORT).show();
        }
    }

//    private void text( Map<String,String> textMap) {
//        try {
//
//            //从assets读取我们的Word模板
//            InputStream is = getAssets().open("CensusTab.doc");
//            //创建生成的文件
//            File newFile = new File(newPath);
//            writeDoc(is, newFile, textMap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * newFile 生成文件
//     * map 要填充的数据
//     */
//    public void writeDoc(InputStream in, File newFile, Map<String, String> map) {
//        try {
//
//            File file = new File(filePath);
//            if (!file.exists()) {
//                file.mkdirs();
//            }
//
//            HWPFDocument hdt = new HWPFDocument(in);
//            // Fields fields = hdt.getFields();
//            // 读取word文本内容
//            Range range = hdt.getRange();
//            // System.out.println(range.text());
//
//            // 替换文本内容
//            for (Map.Entry<String, String> entry : map.entrySet()) {
//                range.replaceText(entry.getKey(), entry.getValue());
//            }
//            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
//            FileOutputStream out = new FileOutputStream(newFile, true);
//            hdt.write(ostream);
//            // 输出字节流
//            out.write(ostream.toByteArray());
//            out.close();
//            ostream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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
                    JSONObject context = JSON.parseObject(savePointVo.getContext());     //装有表单填写的字段的json

                    if (movetype.getType().equals(MapMeterMoveScope.LINE)){
                        String facNameList=savePointVo.getFacNameList();
                        String[] facNames=facNameList.split(",");
                        if (facNames.length>0){
                            context.put(OkHttpParam.FIRST_FAC_NAME,facNames[0]);
                            context.put(OkHttpParam.END_FAC_NAME,facNames[facNames.length-1]);
                        }
                    }
                    context.put(OkHttpParam.UPLOAD_TIME,savePointVo.getFacPipBaseVo().getUploadTime());

                    if (pointType.equals(OkHttpParam.WGS84)) {
                        String longitudeWgs84=savePointVo.getLongitudeWg84();
                        String  latitudeWgs84=savePointVo.getLatitudeWg84();

                        if (longitudeWgs84!=null&&!longitudeWgs84.equals("")&&latitudeWgs84!=null&&!latitudeWgs84.equals("")){
                            Double longitudeWgs84_double=Double.parseDouble(longitudeWgs84);
                            Double latitudeWgs84_double=Double.parseDouble(latitudeWgs84);

                            String longitudeWg84_du= GPSUtils.getDegrees(longitudeWgs84_double);
                            String longitudeWg84_fen= GPSUtils.getMinutes(longitudeWgs84_double);
                            String longitudeWg84_miao= GPSUtils.getSeconds(longitudeWgs84_double);

                            String latitudeWg84_du= GPSUtils.getDegrees(latitudeWgs84_double);
                            String latitudeWg84_fen= GPSUtils.getMinutes(latitudeWgs84_double);
                            String latitudeWg84_miao= GPSUtils.getSeconds(latitudeWgs84_double);

                            context.put(OkHttpParam.LONGITUDEWG84_DU,longitudeWg84_du);
                            context.put(OkHttpParam.LONGITUDEWG84_FEN,longitudeWg84_fen);
                            context.put(OkHttpParam.LONGITUDEWG84_MIAO,longitudeWg84_miao);

                            context.put(OkHttpParam.LATITUDEWG84_DU,latitudeWg84_du);
                            context.put(OkHttpParam.LATITUDEWG84_FEN,latitudeWg84_fen);
                            context.put(OkHttpParam.LATITUDEWG84_MIAO,latitudeWg84_miao);

                            context.put(OkHttpParam.LONGITUDE_WG84, longitudeWgs84);
                            context.put(OkHttpParam.LATITUDE_WG84, latitudeWgs84);
                        }else {
                            if (!savePointVo.getLongitude().equals("")&&!savePointVo.getLatitude().equals("")){
                                LatLng latLng=new LatLng(Double.parseDouble(savePointVo.getLatitude())
                                        ,Double.parseDouble(savePointVo.getLongitude()));
                                Map<String,Double> map_ =  Bd09toArcgis.bd09ToWg84(latLng);

                                context.put(OkHttpParam.LONGITUDE_WG84, map_.get("lon"));
                                context.put(OkHttpParam.LATITUDE_WG84, map_.get("lat"));
                            }
                        }

                    } else if (pointType.equals("arc")) {

                        context.put(OkHttpParam.LONGITUDE_WG84, savePointVo.getMapx());
                        context.put(OkHttpParam.LATITUDE_WG84, savePointVo.getMapy());
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
                        if (!pointmapList.contains(pointMap)){
                            pointmapList.add(pointMap);
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
