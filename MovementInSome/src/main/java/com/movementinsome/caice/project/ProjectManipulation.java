package com.movementinsome.caice.project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.baidu.BaiduAppProxy;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.util.Arith;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.okhttp.OkHttpRequest;
import com.movementinsome.caice.okhttp.ProjectRequest;
import com.movementinsome.caice.util.BaiduCoordinateTransformation;
import com.movementinsome.caice.util.BaiduMapUtil;
import com.movementinsome.caice.util.Bd09toArcgis;
import com.movementinsome.caice.util.ConstantDate;
import com.movementinsome.caice.util.DateUtil;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.util.UUIDUtil;
import com.movementinsome.caice.view.CustomDialog;
import com.movementinsome.caice.vo.CityVo;
import com.movementinsome.caice.vo.MoveBaseVo;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.database.vo.DynamicFormVO;
import com.movementinsome.easyform.formengineer.RunForm;
import com.movementinsome.kernel.initial.model.ProjectType;
import com.movementinsome.kernel.location.LocationInfoExt;
import com.movementinsome.kernel.location.coordinate.Gcj022Gps;
import com.movementinsome.kernel.util.ActivityUtil;
import com.movementinsome.map.MapViewer;
import com.movementinsome.map.utils.MapUtil;
import com.pop.android.common.util.ToastUtils;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.movementinsome.kernel.location.coordinate.Gcj022Bd09.bd09Encrypt;

/**
 * 地图操作类(操作放到这里类以减少activity里的代码量)
 * Created by zzc on 2018/1/15.
 */

public class ProjectManipulation {
    private MapView mMapview;
    private BaiduMap mBaiduMap;
    private MapViewer mActivity;
//    private CustomDialog customDialog;
//    private CustomDialog customDialog1;
    private CustomDialog.Builder builder;

    private EditText moveName;
    private EditText moveType;
//    private EditText moveSharedCode;

    private EditText record_name;
    private EditText SDx;
    private EditText SDy;
    private EditText SDz;
    private EditText SQx;
    private EditText SQy;
    private EditText SQz;
    private EditText SScale;
    private EditText FDx;
    private EditText FDy;
    private EditText FScale;
    private EditText FRotateangle;
    private EditText PCentralmeridian;
    private EditText PScale;
    private EditText PConstantx;
    private EditText PConstanty;
    private EditText PBenchmarklatitude;
    private EditText Semimajor;
    private EditText Flattening;

    private Spinner moveSpinnerType;
    public String SpinnerStr;

    private List<String> cityListStr;
    private List<CityVo> cityList;
    public CityVo cityVo;      //当前选中城市七参
    public int cityQicanPosition;  //当前选中城市七参的position
    private Dao<CityVo,Long> cityVoDao;
    private List<ProjectType> projectTypes;
    public List<String> mType;
    private List<String> symbolList;
    private String[] latlngType = {OkHttpParam.WGS84, OkHttpParam.GCJ02};

    private Dao<ProjectVo, Long> miningSurveyVOdao = null;

    private EditText moveLog;
    private EditText moveLat;
    private Spinner moveInputType;

    private boolean isShared=false;

    public ProjectManipulation(Activity activity,MapView mapView) throws SQLException {
        this.mMapview=mapView;
        this.mActivity= (MapViewer) activity;

        mBaiduMap=mMapview.getMap();
        builder = new CustomDialog.Builder(activity);
        miningSurveyVOdao=AppContext.getInstance().getAppDbHelper().getDao(ProjectVo.class);

        mType = new ArrayList<>();
        symbolList = new ArrayList<>();
        projectTypes = AppContext.getInstance().getProjectType();
        if (projectTypes != null && projectTypes != null) {
            for (int i = 0; i < projectTypes.size(); i++) {
                mType.add(projectTypes.get(i).getName());
                symbolList.add(projectTypes.get(i).getSymbol());
            }
        }

        cityVoDao=AppContext.getInstance().getAppDbHelper().getDao(CityVo.class);
        cityVo=new CityVo();
        cityListStr=new ArrayList<>();
        getQiCanCityData();
    }

    /**
     * 新建工程
     */
    public void CreateProject( ) {
        mActivity.customDialog = builder.cancelTouchout(false)
                .view(R.layout.move_mining_item)
                .heightpx(ActivityUtil.getWindowsHetght(mActivity))
                .widthpx(ActivityUtil.getWindowsWidth(mActivity))
                .style(R.style.dialog)
                .addViewOnclick(R.id.confirmBtn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (moveName.getText().toString()!= null && !moveName.getText().toString().equals("") &&
                                    moveType.getText().toString() != null && !moveType.getText().toString().equals("")) {

                                ProjectVo projectVo = new ProjectVo();
                                projectVo.setProjectName(moveName.getText().toString());
                                projectVo.setProjectId(UUID.randomUUID().toString());
                                projectVo.setProjectNum(moveType.getText().toString());
                                projectVo.setProjectType(SpinnerStr);
                                projectVo.setProjectCreateDateStr(DateUtil.getNow());
                                projectVo.setAutoNumber(1);
                                projectVo.setAutoNumberLine(1);
                                projectVo.setUserId(AppContext.getInstance().getCurUser().getUserId());
                                projectVo.setProjectStatus("0");
                                projectVo.setProjectUpdatedDateStr("");
                                projectVo.setProjectEndDateStr("");
                                projectVo.setTaskNum("");
                                projectVo.setProjectSubmitStatus("");
                                if (isShared){
                                    projectVo.setProjectShareCode(UUIDUtil.getUUID());
                                }else {
                                    projectVo.setProjectShareCode("");
                                }

                                MoveBaseVo moveBaseVo=new MoveBaseVo(JSONObject.toJSONString(cityVo));
                                if (moveBaseVo!=null){
                                    projectVo.setContextStr(JSONObject.toJSONString(moveBaseVo));
                                }else {
                                    projectVo.setContextStr("");
                                }
//                                if (cityQicanPosition>=0){
//                                    if (cityVo!=null){
//                                        projectVo.setQicanStr(new Gson().toJson(cityVo));
//                                    }
//                                }else {
//                                    projectVo.setQicanStr("");
//                                }

                                //向服务器请求
                                ProjectRequest.ProjectCreate(projectVo, mActivity, false);
                            } else {
                                Toast.makeText(mActivity, "工程名称或任务编号不能为空", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                    }
                })
                .addViewOnclick(R.id.cancelIm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.customDialog.dismiss();
                    }
                })
                .build();
        mActivity.customDialog.show();

        View view = mActivity.customDialog.getView();

//        final LinearLayout share_lin_account= (LinearLayout) view.findViewById(R.id.share_lin_account);
//        final LinearLayout share_lin_code= (LinearLayout) view.findViewById(R.id.share_lin_code);

        RadioGroup share_radiogroup= (RadioGroup) view.findViewById(R.id.share_radiogroup);
        share_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId==R.id.radioButton_share_no){
                    isShared=false;
                }else if (checkedId==R.id.radioButton_share_yes){
                    isShared=true;
                }
            }
        });

        final Spinner qican_pulldown= (Spinner) view.findViewById(R.id.qican_pulldown);
        final ArrayAdapter<String> qican_adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, cityListStr);
        //设置下拉列表的风格
        qican_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        qican_pulldown.setAdapter(qican_adapter);
        qican_pulldown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityQicanPosition=position-1;
                if (position>0){
                    cityVo=cityList.get(position-1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button manual_write_btn= (Button) view.findViewById(R.id.manual_write_btn);
        manual_write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] areaStr = new String[1];

                mActivity.customDialog1 = builder.cancelTouchout(false)
                        .view(R.layout.qican_write_dialog)
                        .heightpx(ActivityUtil.getWindowsHetght(mActivity))
                        .widthpx(ActivityUtil.getWindowsWidth(mActivity))
                        .style(R.style.dialog)
                        .addViewOnclick(R.id.cancelIm, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mActivity.customDialog1.isShowing()) {
                                    mActivity.customDialog1.dismiss();
                                }
                            }
                        })
                        .addViewOnclick(R.id.confirmBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (ifNoNull()){
                                    try {
                                        Dao<CityVo,Long> cityDao=AppContext.getInstance()
                                                .getAppDbHelper().getDao(CityVo.class);
                                        CityVo cityVo=new CityVo();
                                        cityVo.setId(UUIDUtil.getUUID());
                                        cityVo.setCityName(record_name.getText().toString());
                                        cityVo.setSdx(Double.parseDouble(SDx.getText().toString()));
                                        cityVo.setSdy(Double.parseDouble(SDy.getText().toString()));
                                        cityVo.setSdz(Double.parseDouble(SDz.getText().toString()));
                                        cityVo.setSqx(Double.parseDouble(SQx.getText().toString()));
                                        cityVo.setSqy(Double.parseDouble(SQy.getText().toString()));
                                        cityVo.setSqz(Double.parseDouble(SQz.getText().toString()));
                                        cityVo.setSscale(Double.parseDouble(SScale.getText().toString()));
                                        cityVo.setFdx(Double.parseDouble(FDx.getText().toString()));
                                        cityVo.setFdy(Double.parseDouble(FDy.getText().toString()));
                                        cityVo.setFscale(Double.parseDouble(FScale.getText().toString()));
                                        cityVo.setFrotateangle(Double.parseDouble(FRotateangle.getText().toString()));
                                        cityVo.setPcentralmeridian(Double.parseDouble(PCentralmeridian.getText().toString()));
                                        cityVo.setPscale(Double.parseDouble(PScale.getText().toString()));
                                        cityVo.setPconstantx(Double.parseDouble(PConstantx.getText().toString()));
                                        cityVo.setPconstanty(Double.parseDouble(PConstanty.getText().toString()));
                                        cityVo.setPbenchmarklatitude(Double.parseDouble(PBenchmarklatitude.getText().toString()));
                                        cityVo.setSemimajor(Double.parseDouble(Semimajor.getText().toString()));
                                        cityVo.setFlattening(Double.parseDouble(Flattening.getText().toString()));
                                        cityVo.setPprojectionType(Long.parseLong(areaStr[0]));

                                        int s=cityDao.create(cityVo);
                                        if (s==1){
                                            ToastUtils.showToast(mActivity,"新增成功");
                                            getQiCanCityData();
                                            qican_adapter.notifyDataSetChanged();
                                            for (int i = 0; i < cityList.size(); i++) {
                                                if (cityList.get(i).getId().equals(cityVo.getId())){
                                                    qican_pulldown.setSelection(i);
                                                    break;
                                                }
                                            }
                                            if (mActivity.customDialog1.isShowing()) {
                                                mActivity.customDialog1.dismiss();
                                            }
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    ToastUtils.showToast(mActivity,mActivity.getResources()
                                            .getString(R.string.please_enter_all_parameters));
                                }
                            }

                            private boolean ifNoNull() {
                                if (
                                        !record_name.getText().toString().equals("") &&
                                                !SDx.getText().toString().equals("") &&
                                                !SDy.getText().toString().equals("") &&
                                                !SDz.getText().toString().equals("") &&
                                                !SQx.getText().toString().equals("") &&
                                                !SQy.getText().toString().equals("") &&
                                                !SQz.getText().toString().equals("") &&
                                                !SScale.getText().toString().equals("") &&
                                                !FDx.getText().toString().equals("") &&
                                                !FDy.getText().toString().equals("") &&
                                                !FScale.getText().toString().equals("") &&
                                                !FRotateangle.getText().toString().equals("") &&
                                                !PCentralmeridian.getText().toString().equals("") &&
                                                !PScale.getText().toString().equals("") &&
                                                !PConstantx.getText().toString().equals("") &&
                                                !PConstanty.getText().toString().equals("") &&
                                                !PBenchmarklatitude.getText().toString().equals("") &&
                                                !Semimajor.getText().toString().equals("") &&
                                                !Flattening.getText().toString().equals("")
                                        ) {
                                    return true;
                                }else {
                                    return false;
                                }
                            }
                        })
                        .build();
                mActivity.customDialog1.show();

                View qican_view = mActivity.customDialog1.getView();
                record_name = (EditText) qican_view.findViewById(R.id.record_name);
                SDx = (EditText) qican_view.findViewById(R.id.SDx);
                SDy = (EditText) qican_view.findViewById(R.id.SDy);
                SDz = (EditText) qican_view.findViewById(R.id.SDz);
                SQx = (EditText) qican_view.findViewById(R.id.SQx);
                SQy = (EditText) qican_view.findViewById(R.id.SQy);
                SQz = (EditText) qican_view.findViewById(R.id.SQz);
                SScale = (EditText) qican_view.findViewById(R.id.SScale);
                FDx = (EditText) qican_view.findViewById(R.id.FDx);
                FDy = (EditText) qican_view.findViewById(R.id.FDy);
                FScale = (EditText) qican_view.findViewById(R.id.FScale);
                FRotateangle = (EditText) qican_view.findViewById(R.id.FRotateangle);
                PCentralmeridian = (EditText) qican_view.findViewById(R.id.PCentralmeridian);
                PScale = (EditText) qican_view.findViewById(R.id.PScale);
                PConstantx = (EditText) qican_view.findViewById(R.id.PConstantx);
                PConstanty = (EditText) qican_view.findViewById(R.id.PConstanty);
                PBenchmarklatitude = (EditText) qican_view.findViewById(R.id.PBenchmarklatitude);
                Semimajor = (EditText) qican_view.findViewById(R.id.Semimajor);
                Flattening = (EditText) qican_view.findViewById(R.id.Flattening);


                final Spinner area = (Spinner) qican_view.findViewById(R.id.area);
                area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        areaStr[0] = area.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        });

        moveName = (EditText) view.findViewById(R.id.moveName);
        moveType = (EditText) view.findViewById(R.id.moveType);
//        moveSharedCode = (EditText) view.findViewById(R.id.moveSharedCode);
//        lineGone = (LinearLayout) view.findViewById(R.id.lineGone);
        moveSpinnerType = (Spinner) view.findViewById(R.id.moveSpinnerType);
        SpinnerStr = mType.get(0);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, mType);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        moveSpinnerType.setAdapter(adapter);
        moveSpinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                // TODO Auto-generated method stub
                SpinnerStr = mType.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        final List<String> taskNums = new ArrayList<>();
        try {
            List<ProjectVo> projectVoList = miningSurveyVOdao.
                    queryForAll();
            for (ProjectVo projectVo : projectVoList) {
                taskNums.add(projectVo.getProjectNum());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取七参城市数据
     * @throws SQLException
     */
    private void getQiCanCityData() throws SQLException {
        cityListStr.clear();
        cityList=cityVoDao.queryForAll();
        if (cityList!=null){
            for (int i = 0; i < cityList.size(); i++) {
                if (cityList.get(i).getCityName()!=null&&!cityList.get(i).getCityName().equals("")){
                    cityListStr.add(cityList.get(i).getCityName());
                }
            }
        }
        cityListStr.add(0,"-");
    }

    /**
     * 坐标点 点击事件
     * @param pointChangeLineBtn
     * @param continuity_point
     */
    public void coordinate_pointOnclick(View pointChangeLineBtn, final View continuity_point) {
        switch ((String) pointChangeLineBtn.getTag()) {
            case MapMeterMoveScope.POINT:        //坐标点
                mActivity.customDialog = builder.cancelTouchout(false)
                        .view(R.layout.move_input_point)
                        .heightpx(ActivityUtil.getWindowsHetght(mActivity))
                        .widthpx(ActivityUtil.getWindowsWidth(mActivity))
                        .style(R.style.dialog)
                        .addViewOnclick(R.id.confirmInputBtn, new View.OnClickListener() {    //确定
                            @Override
                            public void onClick(View v) {
                                try {
                                    MapStatus.Builder builder = new MapStatus.Builder();

                                    if (moveLog.getText().toString().equals("") || moveLat.getText().toString().equals("")) {
                                        Toast.makeText(mActivity, "经纬度不能为空", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    ;

                                    double edlog = Double.parseDouble(moveLog.getText().toString());
                                    double edlat = Double.parseDouble(moveLat.getText().toString());
                                    switch (SpinnerStr) {
                                        case OkHttpParam.WGS84:
//                                            Map<String, Double> localHashMap = Gcj022Gps.wgs2gcj(edlog, edlat);
//                                            LatLng latlng1 = new LatLng(localHashMap.get("lat"), localHashMap.get("lon"));
                                            LatLng latlng1=BaiduCoordinateTransformation.wgs84ToBd09(new LatLng(edlat,edlog));

                                            if (continuity_point.getTag().toString().equals("yes")) {    //说明是连续采点
                                                mActivity.consecutiveCollection(latlng1,
                                                        "坐标采点",
                                                        edlog,
                                                        edlat,
                                                        ConstantDate.ISSUCCESSION_YES);
                                            }else {
                                                mActivity.consecutiveCollection(latlng1,
                                                        "坐标采点",
                                                        edlog,
                                                        edlat,
                                                        ConstantDate.ISSUCCESSION_NO);
                                            }

                                            builder.target(latlng1);
                                            builder.zoom(18.0f);
                                            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                                            mActivity.customDialog.dismiss();
                                            break;
                                        case OkHttpParam.GCJ02:
                                            Map<String, Double> localHashMap1 = Gcj022Gps.gcj2wgs(edlog, edlat);
                                            LatLng latlng=BaiduCoordinateTransformation.gcj02ToBd09(new LatLng(edlat,edlog));

                                            if (continuity_point.getTag().toString().equals("yes")) {    //说明是连续采点
                                                mActivity.consecutiveCollection(latlng,
                                                        "坐标采点",
                                                        localHashMap1.get("lon"),
                                                        localHashMap1.get("lat"),
                                                        ConstantDate.ISSUCCESSION_YES);
                                            }else {
                                                mActivity.consecutiveCollection(latlng,
                                                        "坐标采点",
                                                        localHashMap1.get("lon"),
                                                        localHashMap1.get("lat"),
                                                        ConstantDate.ISSUCCESSION_NO);
                                            }

                                            builder.target(latlng);
                                            builder.zoom(18.0f);
                                            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                                            mActivity.customDialog.dismiss();
                                            break;
                                    }
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .addViewOnclick(R.id.cancelInputIm, new View.OnClickListener() {        //取消
                            @Override
                            public void onClick(View v) {
                                mActivity.customDialog.dismiss();
                            }
                        })
                        .build();
                mActivity.customDialog.show();

                View inputView = mActivity.customDialog.getView();
                moveLog = (EditText) inputView.findViewById(R.id.moveLog);
                moveLat = (EditText) inputView.findViewById(R.id.moveLat);
                moveInputType = (Spinner) inputView.findViewById(R.id.moveInputType);

                moveLog.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                moveLat.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                SpinnerStr = latlngType[0];
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, latlngType);
                //设置下拉列表的风格
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //将adapter 添加到spinner中
                moveInputType.setAdapter(adapter1);
                moveInputType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        // TODO Auto-generated method stub
                        SpinnerStr = latlngType[position].toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub

                    }
                });

                break;
            case MapMeterMoveScope.LINE:        //坐标线
                mActivity.customDialog = builder.cancelTouchout(false)
                        .view(R.layout.move_input_point)
                        .heightpx(ActivityUtil.getWindowsHetght(mActivity))
                        .widthpx(ActivityUtil.getWindowsWidth(mActivity))
                        .style(R.style.dialog)
                        .addViewOnclick(R.id.confirmInputBtn, new View.OnClickListener() {    //确定
                            @Override
                            public void onClick(View v) {
                                try {
                                    MapStatus.Builder builder = new MapStatus.Builder();

                                    if (moveLog.getText().toString().equals("") || moveLat.getText().toString().equals("")) {
                                        Toast.makeText(mActivity, "经纬度不能为空", Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                    double edlog = Double.parseDouble(moveLog.getText().toString());
                                    double edlat = Double.parseDouble(moveLat.getText().toString());
                                    switch (SpinnerStr) {
                                        case OkHttpParam.WGS84:
                                            Map<String, Double> localHashMap = Gcj022Gps.wgs2gcj(edlog, edlat);
                                            double lonlat1[] = bd09Encrypt(localHashMap.get("lat"), localHashMap.get("lon"));
                                            LatLng latlng1 = new LatLng(lonlat1[1], lonlat1[0]);

                                            builder.target(latlng1);
                                            builder.zoom(18.0f);
                                            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                                            mActivity.customDialog.dismiss();

                                            //绘制线
                                            mActivity.showLine(latlng1, "坐标采线", "");

                                            break;
                                        case OkHttpParam.GCJ02:
                                            double lonlat[] = bd09Encrypt(edlat, edlog);
                                            LatLng latlng = new LatLng(lonlat[1], lonlat[0]);

                                            builder.target(latlng);
                                            builder.zoom(18.0f);
                                            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                                            mActivity.customDialog.dismiss();

                                            //绘制线
                                            mActivity.showLine(latlng, "坐标采线", "");
                                            break;
                                    }
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .addViewOnclick(R.id.cancelInputIm, new View.OnClickListener() {        //取消
                            @Override
                            public void onClick(View v) {
                                mActivity.customDialog.dismiss();
                            }
                        })
                        .build();
                mActivity.customDialog.show();

                View inputViewline = mActivity.customDialog.getView();
                moveLog = (EditText) inputViewline.findViewById(R.id.moveLog);
                moveLat = (EditText) inputViewline.findViewById(R.id.moveLat);
                moveInputType = (Spinner) inputViewline.findViewById(R.id.moveInputType);

                moveLog.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                moveLat.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                SpinnerStr = latlngType[0];
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, latlngType);
                //设置下拉列表的风格
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //将adapter 添加到spinner中
                moveInputType.setAdapter(adapter2);
                moveInputType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        // TODO Auto-generated method stub
                        SpinnerStr = latlngType[position].toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub

                    }
                });
                break;
        }
    }

    /**
     * 精准点 点击事件
     * @param pointChangeLineBtn
     * @param continuity_point
     * @param continuity_line
     * @param cenpt
     * @param cenpt_wgs84
     */
    public void accuratePointOnclick(View pointChangeLineBtn,View continuity_point,View continuity_line,
                                     View line_add_point,SavePointVo lineAddPointVo,
                                     LatLng cenpt,LatLng cenpt_wgs84){
        switch ((String) pointChangeLineBtn.getTag()) {
            case MapMeterMoveScope.POINT:       //精准采点
                if (cenpt != null) {
                    if (continuity_point.getTag().toString().equals("yes")) {    //说明是连续采点
                        mActivity.consecutiveCollection(cenpt,
                                "精准采点",
                                cenpt_wgs84.longitude,
                                cenpt_wgs84.latitude,
                                ConstantDate.ISSUCCESSION_YES);
                    } else if (continuity_line.getTag().equals("yes")) {        //连续线
                        mActivity.consecutiveCollection(cenpt,
                                "精准采点",
                                cenpt_wgs84.longitude,
                                cenpt_wgs84.latitude,
                                ConstantDate.ISSUCCESSION_YES);
                    } else if (line_add_point.getTag().equals("yes")) {        //管线加点
                        if (lineAddPointVo!=null){
                            mActivity.consecutiveCollection(cenpt,
                                    "精准采点",
                                    cenpt_wgs84.longitude,
                                    cenpt_wgs84.latitude,
                                    ConstantDate.ISSUCCESSION_LINE_ADD_POIN);
                        }else {
                            com.movementinsome.map.nearby.ToastUtils.show("请先选择管线");
                        }
                    } else {             //普通
                        mActivity.consecutiveCollection(cenpt,
                                "精准采点",
                                cenpt_wgs84.longitude,
                                cenpt_wgs84.latitude,
                                ConstantDate.ISSUCCESSION_NO);
                    }
                }
                break;
            case MapMeterMoveScope.LINE:       //精准采线
                if (cenpt != null) {
                    //绘制线
                    mActivity.showLine(cenpt, "精准采线", "");
                }
                break;
        }

        MapStatus.Builder builder1 = new MapStatus.Builder();
        builder1.target(cenpt);
        builder1.zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));
    }

    /**
     * 切换点击事件
     * @param linePointList
     * @param linePointOverlayList
     * @param input_line
     */
    public void pointChangeLineBtn_Onclick(List<LatLng> linePointList, List<Overlay> linePointOverlayList,
                                           View input_line,String isSuccession){
        if (linePointList.size() > 0) {
            cleanLinePoint(R.id.pointChangeLineBtn,linePointList,linePointOverlayList,input_line,isSuccession);
        } else {
            mActivity.pointChangeLineBtn_click();
        }
    }

    /**
     * 手绘点 点击事件
     * @param linePointList
     * @param linePointOverlayList
     * @param input_line
     */
    public void draw_point_Onclick(List<LatLng> linePointList, List<Overlay> linePointOverlayList,
                                   View input_line,String isSuccession){
        if (linePointList.size() > 0) {
            cleanLinePoint(R.id.draw_point,linePointList,linePointOverlayList,input_line,isSuccession);
        } else {
            mActivity.draw_point_click();

        }
    }

    /**
     * 连续采点  点击事件
     * @param linePointList
     * @param linePointOverlayList
     * @param input_line
     */
    public void continuity_point_Onclick(List<LatLng> linePointList, List<Overlay> linePointOverlayList,
                                         View input_line,String isSuccession){
        if (linePointList.size() > 0) {
            cleanLinePoint(R.id.continuity_point,linePointList,linePointOverlayList,input_line,isSuccession);
        } else {
            mActivity.continuity_point_click();
        }
    }

    /**
     * 连续线 点击事件
     * @param linePointList
     * @param linePointOverlayList
     * @param input_line
     */
    public void continuity_line_Onclick(List<LatLng> linePointList, List<Overlay> linePointOverlayList,
                                        View input_line,String isSuccession){
        if (linePointList.size() > 0) {
            cleanLinePoint(R.id.continuity_line,linePointList,linePointOverlayList,input_line,isSuccession);
        } else {
            mActivity.continuity_line_click();
        }
    }

    /**
     * 暂停工程 点击事件
     * @param linePointList
     * @param linePointOverlayList
     * @param input_line
     */
    public void over_projcet_Onclick(List<LatLng> linePointList, List<Overlay> linePointOverlayList,
                                     View input_line,String isSuccession){
        if (linePointList.size() > 0) {
            cleanLinePoint(R.id.over_projcet,linePointList,linePointOverlayList,input_line,isSuccession);
        } else {
            mActivity.over_projcet_click(false);
        }
    }

    /**
     * 管线加点 点击事件
     * @param linePointList
     * @param linePointOverlayList
     * @param input_line
     */
    public void line_add_point_Onclick(List<LatLng> linePointList, List<Overlay> linePointOverlayList,
                                       View input_line,String isSuccession){
        if (linePointList.size() > 0) {
            cleanLinePoint(R.id.line_add_point,linePointList,linePointOverlayList,input_line,isSuccession);
        } else {
            mActivity.line_add_point_click();
        }
    }

    /**
     * view  点击事件
     * @param viewId    viewid
     * @param linePointList
     * @param linePointOverlayList
     * @param input_line
     */
    private void cleanLinePoint(final int viewId, final List<LatLng> linePointList,
                                final List<Overlay> linePointOverlayList, final View input_line,
                                String isSuccession) {
        String msg="是否结束?";
        if (isSuccession.equals(ConstantDate.ISSUCCESSION_YES)){
            msg="是否结束本次连续采集?";
        }else if (isSuccession.equals(ConstantDate.ISSUCCESSION_LINE_ADD_POIN)){
            msg="是否结束本次管线加点?";
        }
        mActivity.customDialog = builder.cancelTouchout(false)
                .view(R.layout.move_mining_over_dlaio)
                .heightpx(ActivityUtil.getWindowsHetght(mActivity))
                .widthpx(ActivityUtil.getWindowsWidth(mActivity))
                .style(R.style.dialog)
                .setTitle("提醒")
                .setMsg(msg)
                .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linePointList.clear();
                        if (linePointOverlayList.size() > 0) {
                            for (int i = 0; i < linePointOverlayList.size(); i++) {
                                linePointOverlayList.get(i).remove();
                            }
                        }
                        input_line.setVisibility(View.GONE);
                        mActivity.customDialog.dismiss();

                        //点击事件的执行事件
                        switch (viewId) {
                            case R.id.continuity_line:  //连续线
                                mActivity.continuity_line_click();
                                break;
                            case R.id.continuity_point:  //连续点
                                mActivity.continuity_point_click();
                                break;
                            case R.id.draw_point:  //手绘点
                                mActivity.draw_point_click();
                                break;
                            case R.id.pointChangeLineBtn:  //点线切换
                                mActivity.pointChangeLineBtn_click();
                                break;
                            case R.id.over_projcet:  //暂停工程
                                mActivity.over_projcet_click(true);
                                break;
                            case R.id.line_add_point:  //管线加点
                                mActivity.line_add_point_click();
                                break;
                            default:
                                break;
                        }
                    }
                })
                .addViewOnclick(R.id.cancelOverBtn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.customDialog.dismiss();
                        return;
                    }
                })
                .addViewOnclick(R.id.cancelOverIm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.customDialog.dismiss();
                        return;
                    }
                })
                .build();
        mActivity.customDialog.show();
    }

    /**
     * 删除设施点 点击事件
     * @param delete_point
     * @param checkPointVoList
     * @param currentProject
     */
    public void detele_point_Onclick(View delete_point, final List<SavePointVo> checkPointVoList,
                                      final ProjectVo currentProject){
        switch ((String) delete_point.getTag()) {
            case MapMeterMoveScope.CHECK:        //删除点
                mActivity.customDialog = builder.cancelTouchout(false)
                        .view(R.layout.move_mining_over_dlaio)
                        .heightpx(ActivityUtil.getWindowsHetght(mActivity))
                        .widthpx(ActivityUtil.getWindowsWidth(mActivity))
                        .setTitle("删除提醒")
                        .setMsg("是否删除？")
                        .style(R.style.dialog)
                        .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (checkPointVoList != null && checkPointVoList.size() == 1) {
//                                    SavePointVo savePointVo = checkPointVoList.get(0);
//                                    String markreId = savePointVo.getFacId();
                                    //删除百度云
                                    OkHttpRequest.DeleteMarkerPoint( checkPointVoList, currentProject);

                                    mActivity.view_gone();

                                    mActivity.customDialog.dismiss();

                                } else {
                                    Toast.makeText(mActivity, "不是本机数据无法删除", Toast.LENGTH_SHORT).show();
                                    mActivity.customDialog.dismiss();
                                }
                            }
                        })
                        .addViewOnclick(R.id.cancelOverIm, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mActivity.customDialog.dismiss();
                            }
                        })
                        .addViewOnclick(R.id.cancelOverBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mActivity.customDialog.dismiss();
                            }
                        })
                        .build();
                mActivity.customDialog.show();
                break;

            case MapMeterMoveScope.DELETELINE:        //删除线
                mActivity.customDialog = builder.cancelTouchout(false)
                        .view(R.layout.move_mining_over_dlaio)
                        .heightpx(ActivityUtil.getWindowsHetght(mActivity))
                        .widthpx(ActivityUtil.getWindowsWidth(mActivity))
                        .setTitle("删除提醒")
                        .setMsg("是否删除？")
                        .style(R.style.dialog)
                        .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

//                                final ArrayList<String> ids = lineBundle.getStringArrayList("ids");
//
//                                String idStr = "";
//                                if (ids != null && ids.size() > 0) {
//                                    for (int i = 0; i < ids.size(); i++) {
//                                        if (i == ids.size() - 1) {
//                                            idStr += ids.get(i);
//                                        } else {
//                                            idStr += ids.get(i) + ",";
//                                        }
//                                    }
//                                }

                                OkHttpRequest.DeleteMarkerPoint(checkPointVoList, currentProject);
                                mActivity.view_gone();


                                mActivity.customDialog.dismiss();

                            }
                        })
                        .addViewOnclick(R.id.cancelOverIm, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mActivity.customDialog.dismiss();
                            }
                        })
                        .addViewOnclick(R.id.cancelOverBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mActivity.customDialog.dismiss();
                            }
                        })
                        .build();
                mActivity.customDialog.show();
                break;

        }

    }

    /**
     * 到这去 点击事件
     * @param cenpt
     * @param markerLatlng
     * @param progressDialog
     */
    public void go_to_the_Onclick(final LatLng cenpt,final LatLng markerLatlng,
                                  final ProgressDialog progressDialog){
        mActivity.customDialog1 = builder.cancelTouchout(false)
                .view(R.layout.item_nav_dialog)
                .heightpx(ActivityUtil.getWindowsHetght(mActivity))
                .widthpx(ActivityUtil.getWindowsWidth(mActivity))
                .style(R.style.dialog)
                .addViewOnclick(R.id.tv_driving_search, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mActivity.customDialog1.isShowing()) {
                            mActivity.customDialog1.dismiss();
                        }
                        if (cenpt != null) {
                            progressDialog.setMessage("加载中,请稍后...");
                            progressDialog.show();
                            BaiduAppProxy.CallBaiduNavigationLatLng(
                                    (Activity) mActivity,
                                    BNRoutePlanNode.CoordinateType.BD09LL,
                                    cenpt,
                                    markerLatlng);
                        }
                    }
                })
                .addViewOnclick(R.id.tv_riding_search, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mActivity.customDialog1.isShowing()) {
                            mActivity.customDialog1.dismiss();
                        }
                        if (cenpt != null) {
                            progressDialog.setMessage("加载中,请稍后...");
                            progressDialog.show();
                            BaiduAppProxy.CyclingNavigation(mActivity, cenpt, markerLatlng);
                        }
                    }
                })
                .addViewOnclick(R.id.tv_foot_search, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mActivity.customDialog1.isShowing()) {
                            mActivity.customDialog1.dismiss();
                        }
                        if (cenpt != null) {
                            progressDialog.setMessage("加载中,请稍后...");
                            progressDialog.show();
                            BaiduAppProxy.PedestrianNavigation(mActivity, cenpt, markerLatlng);
                        }
                    }
                })
                .addViewOnclick(R.id.cancelInputIm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mActivity.customDialog1.isShowing()) {
                            mActivity.customDialog1.dismiss();
                        }
                    }
                })
                .build();

        mActivity.customDialog1.show();
    }

    /**
     * 导入 点击事件
     */
    public void input_data_Onclick(){
        mActivity.customDialog1 = builder.cancelTouchout(false)
                .view(R.layout.item_input_data_dialog)
                .heightpx(ActivityUtil.getWindowsHetght(mActivity))
                .widthpx(ActivityUtil.getWindowsWidth(mActivity))
                .style(R.style.dialog)
                .addViewOnclick(R.id.tv_intput_facilities, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFileChooser(MapViewer.POINT_SELECT_CODE);
                    }
                })
                .addViewOnclick(R.id.tv_intput_pipeline_table, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showFileChooser(MapViewer.LINE_SELECT_CODE);
                    }
                })
                .addViewOnclick(R.id.cancelInputIm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mActivity.customDialog1.isShowing()) {
                            mActivity.customDialog1.dismiss();
                        }
                    }
                })
                .build();

        mActivity.customDialog1.show();
    }

    /**
     * 点连线 点击事件
     * @param point_connect_line
     * @param draw_point
     * @param markerOverlayList
     * @param lineOverlayList
     * @param linePointOverlayList
     * @param lineList
     * @param marker
     */
    public void point_connect_line_Onclick(TextView point_connect_line,TextView draw_point,
                                           List<Overlay> markerOverlayList, List<Overlay> lineOverlayList,
                                           List<Overlay> linePointOverlayList, List<LatLng> lineList,
                                           Marker marker
                                           ){
        switch ((String) point_connect_line.getTag()) {
            case "yes":     //设施点连线

                /**
                 * 百度事件
                 */

                mBaiduMap.setOnMapClickListener(null);
                mBaiduMap.setOnPolylineClickListener(null);
                mBaiduMap.removeMarkerClickListener(mActivity);

                point_connect_line.setTag("no");
                point_connect_line.setBackgroundResource(R.drawable.point_connect_line_blck);
                point_connect_line.setTextColor(mActivity.getResources().getColor(R.color.cornflowerblue8));

                //将采集模式改为查看模式
                mActivity.parameter = MapMeterMoveScope.CHECK;

                if (markerOverlayList != null) {
                    for (int i = 0; i < markerOverlayList.size(); i++) {
                        markerOverlayList.get(i).remove();
                    }
                }
                if (lineOverlayList != null) {
                    for (int i = 0; i < lineOverlayList.size(); i++) {
                        lineOverlayList.get(i).remove();
                    }
                }
                if (linePointOverlayList != null) {
                    for (int i = 0; i < linePointOverlayList.size(); i++) {
                        linePointOverlayList.get(i).remove();
                    }
                }
                lineOverlayList.clear();
                linePointOverlayList.clear();
                lineList.removeAll(lineList);

                if (marker != null) {
                    marker.remove();
                    mBaiduMap.hideInfoWindow();
                }

                break;

            case "no":      //普通

                mBaiduMap.setOnMapClickListener(null);
                mBaiduMap.setOnPolylineClickListener(null);
                mBaiduMap.setOnMarkerClickListener(mActivity);

                point_connect_line.setTag("yes");
                point_connect_line.setBackgroundResource(R.drawable.point_connect_line_blue);
                point_connect_line.setTextColor(mActivity.getResources().getColor(R.color.cornflowerblue9));

                draw_point.setTag("no");
                draw_point.setBackgroundResource(R.drawable.draw_point);
                draw_point.setTextColor(mActivity.getResources().getColor(R.color.cornflowerblue8));

                //将采集模式改为采集模式
                mActivity.parameter = MapMeterMoveScope.MOVE;

                if (markerOverlayList != null) {
                    for (int i = 0; i < markerOverlayList.size(); i++) {
                        markerOverlayList.get(i).remove();
                    }
                }
                if (lineOverlayList != null) {
                    for (int i = 0; i < lineOverlayList.size(); i++) {
                        lineOverlayList.get(i).remove();
                    }
                }
                if (linePointOverlayList != null) {
                    for (int i = 0; i < linePointOverlayList.size(); i++) {
                        linePointOverlayList.get(i).remove();
                    }
                }
                lineOverlayList.clear();
                linePointOverlayList.clear();
                lineList.removeAll(lineList);

                if (marker != null) {
                    marker.remove();
                    mBaiduMap.hideInfoWindow();
                }

                break;
        }

    }

    /**
     * 录入连续采集管线信息 点击事件
     * @param linePointList
     * @param pointVoMap
     * @param pointAddress
     * @param locationInfoExt
     * @param isSuccession
     * @param gatherType
     */
    public void input_line_Onclick(final List<LatLng> linePointList, Map<String, SavePointVo> pointVoMap,
                                   String pointAddress, LocationInfoExt locationInfoExt,
                                   String isSuccession, String gatherType, final ProjectVo projectVo,
                                   final List<String> facLines, final SavePointVo line_add_pointVo, final Activity activity)  {
        if (isSuccession.equals(ConstantDate.ISSUCCESSION_LINE_ADD_POIN)){
            mActivity.customDialog = builder.cancelTouchout(false)
                    .view(R.layout.move_mining_over_dlaio)
                    .heightpx(ActivityUtil.getWindowsHetght(mActivity))
                    .widthpx(ActivityUtil.getWindowsWidth(mActivity))
                    .style(R.style.dialog)
                    .setTitle("提醒")
                    .setMsg("该操作将删除原数据,是否确认?")
                    .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                subNewLine(linePointList, projectVo, facLines, line_add_pointVo,activity);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .addViewOnclick(R.id.cancelOverBtn, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mActivity.customDialog.dismiss();
                            return;
                        }
                    })
                    .addViewOnclick(R.id.cancelOverIm, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mActivity.customDialog.dismiss();
                            return;
                        }
                    })
                    .build();
            mActivity.customDialog.show();
        }else {
            if (linePointList != null && linePointList.size() >= 2) {
                subLine(linePointList,pointVoMap,pointAddress,
                        locationInfoExt,isSuccession,gatherType,projectVo,facLines);
            }
        }
    }

    private void subNewLine(List<LatLng> linePointList, ProjectVo projectVo,
                            List<String> facLines, SavePointVo line_add_pointVo,Activity activity)
            throws JSONException {
        if (line_add_pointVo!=null){
            int pointSize ;
            List<SavePointVo> savePointVoList=new ArrayList<>();
            if (linePointList != null && linePointList.size() >= 2) {
                for (int i = 1; i < linePointList.size(); i++) {
                    //创建管线数据
                    /**
                     * 以下字段需要更新
                     * 设施id
                     * 管线坐标集合
                     * 管线编号
                     * 上传时间
                     * facNameList (设施点编号集合)
                     */
                    String startLatlng=linePointList.get(i-1).longitude+" "+linePointList.get(i-1).latitude;
                    String endLatlng=linePointList.get(i).longitude+" "+linePointList.get(i).latitude;
                    String contextStr=line_add_pointVo.getContextStr();
                    SavePointVo savePointVo=new SavePointVo();
                    savePointVo.setFacId(UUIDUtil.getUUID());
                    savePointVo.setPointList(startLatlng+","+endLatlng);
                    org.json.JSONObject contextStr_json=new org.json.JSONObject(contextStr);

                    //管线编号
                    org.json.JSONObject formContext_json=new org.json.JSONObject(
                            contextStr_json.getString(OkHttpParam.FORM_CONTEXT));
                    String projectType = projectVo.getProjectType();
                    String taskNum = projectVo.getProjectNum();
                    pointSize = projectVo.getAutoNumberLine()+i-1;
                    if (projectType != null) {
                        int pos = mType.lastIndexOf(projectType);
                        formContext_json.put(OkHttpParam.PIP_NAME, symbolList.get(pos)
                                + "L" + taskNum + pointSize);
                        savePointVo.setPipName(symbolList.get(pos) + "L" + taskNum + pointSize);
                    }

                    //坐标集合（contextStr里面）
                    formContext_json.put(OkHttpParam.POINT_LIST,savePointVo.getPointList());
                    contextStr_json.put(OkHttpParam.FORM_CONTEXT,formContext_json.toString());

                    //上传时间
                    org.json.JSONObject facPipBaseVo_json=new org.json.JSONObject(
                            contextStr_json.getString(OkHttpParam.FAC_PIP_BASE_VO));
                    facPipBaseVo_json.put(OkHttpParam.UPLOAD_TIME, DateUtil.getNow());
                    contextStr_json.put(OkHttpParam.FAC_PIP_BASE_VO,facPipBaseVo_json.toString());

                    //定位json
                    if (AppContext.getInstance().getCurLocation()!=null){
                        String locationJson= new Gson().toJson(AppContext.getInstance().getCurLocation());
                        contextStr_json.put(OkHttpParam.LOCATION_JSON,
                               locationJson);
                    }

                    //facNameList (设施点编号集合)
                    String startFacName=facLines.get(i-1);
                    String endFacName=facLines.get(i);
                    contextStr_json.put(OkHttpParam.FAC_NAME_LIST,startFacName+","+endFacName);

                    //pipelineLinght (管线长度)
                    contextStr_json.put(OkHttpParam.PIPELINE_LINGHT,
                            MapUtil.getDistance(
                                    linePointList.get(i-1).longitude,
                                    linePointList.get(i-1).latitude,
                                    linePointList.get(i).longitude,
                                    linePointList.get(i).latitude));

                    savePointVo.setContextStr(contextStr_json.toString());

                    //赋值之前管线的数据
                    savePointVo.setDataType(line_add_pointVo.getDataType());     //管线
                    savePointVo.setUserId(line_add_pointVo.getUserId());   //上传人id
                    savePointVo.setGuid(line_add_pointVo.getGuid());

                    savePointVo.setPipType(line_add_pointVo.getPipType());
                    savePointVo.setPipMaterial(line_add_pointVo.getPipMaterial());
                    savePointVo.setLayingType(line_add_pointVo.getLayingType());
                    savePointVo.setProjectId(line_add_pointVo.getProjectId());
                    savePointVo.setShareCode(line_add_pointVo.getShareCode());
                    savePointVo.setGatherType(line_add_pointVo.getGatherType());
                    savePointVo.setLongitudeWg84(line_add_pointVo.getLongitudeWg84());
                    savePointVo.setLatitudeWg84(line_add_pointVo.getLatitudeWg84());
                    Log.i("tag",savePointVo.toString());
                    savePointVoList.add(savePointVo);
                }
            }
            if (savePointVoList.size()>0){
                OkHttpRequest.SubmitPointListCreateAsyn(savePointVoList,line_add_pointVo,activity,projectVo);
            }
        }else {
            com.movementinsome.map.nearby.ToastUtils.show("提交失败");
        }
    }


    /**
     * 填写属性  点击事件
     * @param property
     * @param pointChangeLineBtn
     * @param pointLatlng
     * @param longitude_wg84
     * @param latitude_wg84
     * @param currentProject
     * @param locationInfoExt
     * @param pointAddress
     * @param gatherType
     * @param isSuccession
     * @param implementorName
     * @param continuity_point
     * @param dynamicFormList
     * @param solutionName
     * @param continuity_line
     * @param lineList
     * @param pointVoMap
     */
    public void property_Onclick(View property, View pointChangeLineBtn,LatLng pointLatlng,
                                 double longitude_wg84, double latitude_wg84, ProjectVo currentProject,
                                 LocationInfoExt locationInfoExt, String pointAddress, String gatherType,
                                 String isSuccession, String implementorName, ProjectVo projectVo,
                                 View continuity_point, List<DynamicFormVO> dynamicFormList, String solutionName,
                                 View continuity_line, List<LatLng> lineList,
                                 Map<String, SavePointVo> pointVoMap,
                                 List<SavePointVo> checkPointVoList, Dao<DynamicFormVO, Long> dynamicFormDao,
                                 Bundle lineBundle, List<String> facLines
    ) throws SQLException {
        Intent newFormInfo;
        HashMap<String, String> params;

        switch ((String) property.getTag()) {
            case MapMeterMoveScope.MOVE:    //采集模式
                switch ((String) pointChangeLineBtn.getTag()) {
                    case MapMeterMoveScope.POINT:        //采集点
                        newFormInfo = new Intent(mActivity, RunForm.class);
                        params = new HashMap<String, String>();
                        params.put(OkHttpParam.LONGITUDE_WG84, longitude_wg84 + "");
                        params.put(OkHttpParam.LATITUDE_WG84, latitude_wg84 + "");

                        params.put(OkHttpParam.LONGITUDE, pointLatlng.longitude + "");
                        params.put(OkHttpParam.LATITUDE, pointLatlng.latitude + "");

                        //地方坐标
                        com.esri.core.geometry.Point point = Bd09toArcgis
                                .wgs84ToArcgis(currentProject.getCoordTransform(),longitude_wg84, latitude_wg84,0);
                        params.put(OkHttpParam.MAP_X, point.getX() + "");
                        params.put(OkHttpParam.MAP_Y, point.getY() + "");

                        params.put(OkHttpParam.GROUND_ELEVATION, locationInfoExt.getAltitude() + "");        //海拔，地面高程
                        params.put(OkHttpParam.DRAW_TYPE, MapMeterMoveScope.POINT);        //绘制类型
                        params.put(OkHttpParam.HAPPEN_ADDR, pointAddress);        //地址
                        params.put("isEdit", MapMeterMoveScope.GATHER);        //是否编辑
                        params.put(OkHttpParam.GATHER_TYPE, gatherType);        //采集类型
                        params.put("isSuccession", isSuccession);        //是否是连续采集

                        params.put(OkHttpParam.IMPLEMENTORNAME, implementorName);        //采集类型
                        params.put(OkHttpParam.LOCATION_JSON, new Gson().toJson(locationInfoExt));        //定位vo 数据集合
                        if (!projectVo.getProjectId().equals("")) {
                            params.put(OkHttpParam.PROJECT_ID, projectVo.getProjectId());        //工程ID

                            params.put(OkHttpParam.SHARE_CODE, projectVo.getProjectShareCode());        //共享码
                            params.put(OkHttpParam.IS_PROJECT_SHARE, projectVo.getProjectShareCode());    //表示共享
                            params.put(OkHttpParam.PROJECT_NAME, projectVo.getProjectName());    //工程名字
                            params.put(OkHttpParam.PROJECT_TYPE, projectVo.getProjectType());    //工程类型

                            String projectType = projectVo.getProjectType();
                            String taskNum = projectVo.getProjectNum();

                            int pointSize = 0;
                            pointSize = projectVo.getAutoNumber();        //自动编号

                            if (projectType != null) {

                                int pos = mType.lastIndexOf(projectType);
//                                params.put(OkHttpParam.FAC_NUM, symbolList.get(pos) + taskNum + pointSize);      // 原来的逻辑
                                params.put(OkHttpParam.FAC_NUM, pointSize+"");     //现在只需要编顺序号
                            }
                        }
                        newFormInfo.putExtra("iParams", params);
//                                newFormInfo.putExtra("template", solutionName);

                        if (continuity_point.getTag().equals("yes")) {
                            if (dynamicFormList != null && dynamicFormList.size() > 0) {
                                newFormInfo.putExtra("isPointLinkLine", true);   //用来判断是不是点连线，是则需要
                                newFormInfo.putExtra("id", dynamicFormList.get(0).getId());
                                newFormInfo.putExtra("template", dynamicFormList.get(0).getForm());
                            } else {
                                newFormInfo.putExtra("template", solutionName);
                            }
                        } else if (continuity_line.getTag().equals("yes")) {
                            if (dynamicFormList != null && dynamicFormList.size() > 0) {
                                newFormInfo.putExtra("isPointLinkLine", true);   //用来判断是不是点连线，是则需要
                                newFormInfo.putExtra("id", dynamicFormList.get(0).getId());
                                newFormInfo.putExtra("template", dynamicFormList.get(0).getForm());
                            } else {
                                newFormInfo.putExtra("template", solutionName);
                            }
                        } else {
                            newFormInfo.putExtra("template", solutionName);
                        }
                        int requestCode = 5;
                        if (isSuccession.equals(ConstantDate.ISSUCCESSION_LINE_ADD_POIN)){
                            requestCode=6;
                        }else if (isSuccession.equals(ConstantDate.ISSUCCESSION_YES)){
                            requestCode=5;
                        }
                        mActivity.startActivityForResult(newFormInfo, requestCode);
                        break;
                    case MapMeterMoveScope.LINE:        //采集线
                        if (lineList.size() == 1) {
                            Toast.makeText(mActivity, R.string.move_msg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (lineList != null && lineList.size() >= 2) {
                            subLine(lineList, pointVoMap,pointAddress,
                                    locationInfoExt, isSuccession, gatherType, projectVo,facLines);
                        }
                        break;

                }

                break;

            case MapMeterMoveScope.CHECK:    //查看点属性
                try {
                    if (checkPointVoList != null && checkPointVoList.size() == 1) {
                        SavePointVo savePointVo = checkPointVoList.get(0);
                        if (checkPointVoList.get(0).getUserId().equals(AppContext.getInstance().getCurUser().getUserId())) {
                            QueryBuilder<DynamicFormVO, Long> queryBuilder = dynamicFormDao.queryBuilder();
                            Where<DynamicFormVO, Long> where = queryBuilder.where();
                            where.isNotNull("form");
                            where.and();
                            where.eq(OkHttpParam.GUID, checkPointVoList.get(0).getGuid());
                            dynamicFormList = dynamicFormDao.query(queryBuilder.prepare());
                        } else {
                            dynamicFormList = null;
                        }
                        if (dynamicFormList != null && dynamicFormList.size() > 0) {

                            newFormInfo = new Intent(mActivity, RunForm.class);
                            params = new HashMap<String, String>();
                            params.put("drawType", MapMeterMoveScope.POINT);        //绘制类型
                            params.put("isEdit", MapMeterMoveScope.ISEDIT);  //是否是编辑数据
                            newFormInfo.putExtra("iParams", params);

                            Bundle bundle = new Bundle();
                            bundle.putSerializable(OkHttpParam.SAVEPOINTVO, savePointVo);
                            newFormInfo.putExtra("pointLineBundle", bundle);

                            newFormInfo.putExtra(OkHttpParam.SAVEPOINTVO, savePointVo);
                            newFormInfo.putExtra("id", dynamicFormList.get(0).getId());
                            newFormInfo.putExtra("template", dynamicFormList.get(0).getForm());
                            newFormInfo.putExtra("projectVo", projectVo != null ? projectVo : new ProjectVo());
                            mActivity.startActivity(newFormInfo);
                        } else {     //说明本地表单为空，不是本机保存的，是从服务器下来的数据

                            //入本地表单数据库
                            DynamicFormVO dynamicFormVO = new DynamicFormVO();
                            dynamicFormVO.setId(UUIDUtil.getUUID());
                            dynamicFormVO.setGuid(savePointVo.getGuid());
                            dynamicFormVO.setContent(savePointVo.getContext());
                            if (savePointVo.getFormName().equals("")) {
                                dynamicFormVO.setForm(solutionName);
                            } else {
                                dynamicFormVO.setForm(savePointVo.getFormName());
                            }
                            int s = dynamicFormDao.create(dynamicFormVO);
                            if (s == 1) {
                                newFormInfo = new Intent(mActivity, RunForm.class);
                                params = new HashMap<String, String>();
                                params.put("drawType", MapMeterMoveScope.POINT);        //绘制类型
                                params.put("isEdit", MapMeterMoveScope.ISEDIT);  //是否是编辑数据
                                newFormInfo.putExtra("iParams", params);

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("editPointVo", savePointVo);
                                newFormInfo.putExtra("pointLineBundle", bundle);

                                newFormInfo.putExtra(OkHttpParam.SAVEPOINTVO, savePointVo);
                                newFormInfo.putExtra("id", dynamicFormVO.getId());
                                newFormInfo.putExtra("template", dynamicFormVO.getForm());
                                newFormInfo.putExtra("projectVo", projectVo != null ? projectVo : new ProjectVo());
                                mActivity.startActivity(newFormInfo);
                            }

                        }
//                        } else {
//                            Toast.makeText(mActivity, "不是本机数据无法编辑", Toast.LENGTH_SHORT).show();
//                        }
                    } else {
                        Toast.makeText(mActivity, "不是本机数据无法编辑", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MapMeterMoveScope.MODIFYLIE:    //查看线属性
                SavePointVo savePointVo= (SavePointVo) lineBundle.getSerializable(OkHttpParam.SAVEPOINTVO);
                //直接跳转
//                Intent newFormInfo;
//                HashMap<String, String> params;
//                if (savePointVo.getUserId().equals(AppContext.getInstance().getCurUser().getUserId())){
                    QueryBuilder<DynamicFormVO, Long> queryBuilder = dynamicFormDao.queryBuilder();
                    Where<DynamicFormVO, Long> where = queryBuilder.where();
                    where.isNotNull("form");
                    where.and();
                    where.eq(OkHttpParam.GUID, savePointVo.getGuid());
                    dynamicFormList = dynamicFormDao.query(queryBuilder.prepare());
                    if (dynamicFormList != null && dynamicFormList.size() == 1) {
                        newFormInfo = new Intent(mActivity, RunForm.class);
                        params = new HashMap<String, String>();
                        params.put("isEdit", MapMeterMoveScope.ISEDIT);  //是否是编辑数据params.put("savePointVoId",savePointVo.getId());  //编辑对应的数据ID
                        params.put("drawType", MapMeterMoveScope.LINE);        //绘制类型
                        newFormInfo.putExtra("iParams", params);

                        Bundle bundle = new Bundle();
//                bundle.putStringArrayList("ids", ids);
                        bundle.putSerializable(OkHttpParam.SAVEPOINTVO, savePointVo);
                        bundle.putString("lineOrpointline", MapMeterMoveScope.POINT);      //判断提交的是线还是点线
                        newFormInfo.putExtra("pointLineBundle", bundle);

                        newFormInfo.putExtra(OkHttpParam.SAVEPOINTVO,savePointVo);
                        newFormInfo.putExtra(OkHttpParam.ID, dynamicFormList.get(0).getId());
                        newFormInfo.putExtra("template", dynamicFormList.get(0).getForm());
                        mActivity.startActivityForResult(newFormInfo, 5);

                    } else {
                        DynamicFormVO dynamicFormVO=new DynamicFormVO();
                        dynamicFormVO.setId(UUIDUtil.getUUID());
                        dynamicFormVO.setGuid(savePointVo.getGuid());
                        dynamicFormVO.setContent(savePointVo.getContext());
                        if (savePointVo.getFormName().equals("")){
                            dynamicFormVO.setForm(solutionName);
                        }else {
                            dynamicFormVO.setForm(savePointVo.getFormName());
                        }
                        int s=dynamicFormDao.create(dynamicFormVO);
                        if (s==1){
                            newFormInfo = new Intent(mActivity, RunForm.class);
                            params = new HashMap<String, String>();
                            params.put("isEdit", MapMeterMoveScope.ISEDIT);  //是否是编辑数据params.put("savePointVoId",savePointVo.getId());  //编辑对应的数据ID
                            params.put("drawType", MapMeterMoveScope.LINE);        //绘制类型
                            newFormInfo.putExtra("iParams", params);

                            Bundle bundle = new Bundle();
//                    bundle.putStringArrayList("ids", ids);
                            bundle.putSerializable("editLineVo", savePointVo);
                            bundle.putString("lineOrpointline", MapMeterMoveScope.POINT);      //判断提交的是线还是点线
                            newFormInfo.putExtra("pointLineBundle", bundle);

                            newFormInfo.putExtra(OkHttpParam.SAVEPOINTVO,savePointVo);
                            newFormInfo.putExtra(OkHttpParam.ID, dynamicFormVO.getId());
                            newFormInfo.putExtra("template", dynamicFormVO.getForm());
                            mActivity.startActivityForResult(newFormInfo, 5);
                        }

                    }
//                }else {
//                    Toast.makeText(mActivity, "不是本机数据无法编辑", Toast.LENGTH_SHORT).show();
//                }

//                final ArrayList<String> ids = lineBundle.getStringArrayList("ids");
//
//                List<SavePointVo> saveLineVoList = null;
//                if (ids != null && ids.size() > 0) {
//                    for (int i = 0; i < ids.size(); i++) {
//                        try {
//                            saveLineVoList = savePointVoLongDao.queryForEq("id", ids.get(i));
////									saveLineVoList=saveLineVoLongDao.queryForAll();
//                            if (saveLineVoList != null && saveLineVoList.size() == 1) {
//                                break;
//                            }
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (saveLineVoList != null && saveLineVoList.size() == 1) {
//                        try {
//                            SavePointVo savePointVo = saveLineVoList.get(0);
//                            if (savePointVo.getUserId().equals(AppContext.getInstance().getCurUser().getUserId())) {
//                                QueryBuilder<DynamicFormVO, Long> queryBuilder = dynamicFormDao.queryBuilder();
//                                Where<DynamicFormVO, Long> where = queryBuilder.where();
//                                where.isNotNull("form");
//                                where.and();
//                                where.eq(OkHttpParam.GUID, savePointVo.getGuid());
//                                dynamicFormList = dynamicFormDao.query(queryBuilder.prepare());
//                                if (dynamicFormList != null && dynamicFormList.size() == 1) {
//                                    newFormInfo = new Intent(mActivity, RunForm.class);
//                                    params = new HashMap<String, String>();
//                                    params.put("isEdit", MapMeterMoveScope.ISEDIT);  //是否是编辑数据params.put("savePointVoId",savePointVo.getId());  //编辑对应的数据ID
//                                    params.put("drawType", MapMeterMoveScope.LINE);        //绘制类型
//                                    newFormInfo.putExtra("iParams", params);
//
//                                    Bundle bundle = new Bundle();
//                                    bundle.putStringArrayList("ids", ids);
//                                    bundle.putSerializable("editLineVo", savePointVo);
//                                    bundle.putString("lineOrpointline", MapMeterMoveScope.POINT);      //判断提交的是线还是点线
//                                    newFormInfo.putExtra("pointLineBundle", bundle);
//
//                                    newFormInfo.putExtra("id", dynamicFormList.get(0).getId());
//                                    newFormInfo.putExtra("template", dynamicFormList.get(0).getForm());
//                                    mActivity.startActivityForResult(newFormInfo, 5);
//
//                                } else {
//                                    DynamicFormVO dynamicFormVO = new DynamicFormVO();
//                                    dynamicFormVO.setId(UUIDUtil.getUUID());
//                                    dynamicFormVO.setGuid(savePointVo.getGuid());
//                                    dynamicFormVO.setContent(savePointVo.getContext());
//                                    dynamicFormVO.setForm(solutionName);
//                                    int s = dynamicFormDao.create(dynamicFormVO);
//                                    if (s == 1) {
//                                        newFormInfo = new Intent(mActivity, RunForm.class);
//                                        params = new HashMap<String, String>();
//                                        params.put("isEdit", MapMeterMoveScope.ISEDIT);  //是否是编辑数据params.put("savePointVoId",savePointVo.getId());  //编辑对应的数据ID
//                                        params.put("drawType", MapMeterMoveScope.LINE);        //绘制类型
//                                        newFormInfo.putExtra("iParams", params);
//
//                                        Bundle bundle = new Bundle();
//                                        bundle.putStringArrayList("ids", ids);
//                                        bundle.putSerializable("editLineVo", savePointVo);
//                                        bundle.putString("lineOrpointline", MapMeterMoveScope.POINT);      //判断提交的是线还是点线
//                                        newFormInfo.putExtra("pointLineBundle", bundle);
//
//                                        newFormInfo.putExtra("id", dynamicFormVO.getId());
//                                        newFormInfo.putExtra("template", dynamicFormVO.getForm());
//                                        mActivity.startActivityForResult(newFormInfo, 5);
//                                    }
//
//                                }
//                            } else {
//                                Toast.makeText(mActivity, "不是本机数据无法编辑", Toast.LENGTH_SHORT).show();
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        Toast.makeText(mActivity, "不是本机数据无法编辑", Toast.LENGTH_SHORT).show();
//                    }
//                }
                break;
        }
    }

    /**
     * 连接线
     * @param lineList
     */
    private void subLine(List<LatLng> lineList, Map<String, SavePointVo> pointVoMap,
                         String pointAddress,LocationInfoExt locationInfoExt,
                         String isSuccession,String gatherType,ProjectVo projectVo,
                         List<String> facLines) {
        SavePointVo savePointVo = pointVoMap.get("savePointVo");
        String lineLatlngList = "";
        double pipelineLinght = 0; //管线长度
        for (int i = 0; i < lineList.size(); i++) {
            if (i == lineList.size() - 1) {
                lineLatlngList += lineList.get(i).longitude + " " + lineList.get(i).latitude;
            } else {
                lineLatlngList += lineList.get(i).longitude + " " + lineList.get(i).latitude + ",";
            }
            if (i >= 1) {
                pipelineLinght = Arith.add(pipelineLinght, BaiduMapUtil.getDistanceOfMeter(
                        lineList.get(i - 1).latitude,
                        lineList.get(i - 1).longitude,
                        lineList.get(i).latitude,
                        lineList.get(i).longitude));
            }
        }

        String facNums = "";
        if (facLines != null && facLines.size() > 0) {
            for (int i = 0; i < facLines.size(); i++) {
                if (i == facLines.size() - 1) {
                    facNums += facLines.get(i);
                } else {
                    facNums += facLines.get(i) + ",";
                }
            }
        }

        Intent newFormInfo = new Intent(mActivity, RunForm.class);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(OkHttpParam.FAC_NAME_LIST, facNums);    //管线上设施编号集合
        params.put("happenAddr", pointAddress);        //地址
        params.put("pointList", lineLatlngList);        //坐标集合
        params.put("drawType", MapMeterMoveScope.LINE);        //绘制类型
        params.put("isEdit", MapMeterMoveScope.GATHER);        //绘制类型
        params.put(OkHttpParam.LOCATION_JSON, new Gson().toJson(locationInfoExt));        //定位vo 数据集合

//        //根据是否是连续采集根据设施的采集类型确定管线的采集类型
//        if (isSuccession.equals(ConstantDate.ISSUCCESSION_YES)) {
//            switch (gatherType) {
//                case "手绘采点":
//                    params.put("gatherType", "手绘采线");        //采集类型
//
//                    break;
//
//                case "精准采点":
//                    params.put("gatherType", "精准采线");        //采集类型
//
//                    break;
//
//                case "坐标采点":
//                    params.put("gatherType", "坐标采线");        //采集类型
//
//                    break;
//
//                default:
//                    params.put("gatherType", gatherType);        //采集类型
//                    break;
//            }
//        } else {
//            params.put("gatherType", gatherType);        //采集类型
//        }

        params.put("gatherType", gatherType);        //采集类型
        params.put("isSuccession", isSuccession);        //是否是连续采集
        params.put("pipelineLinght", pipelineLinght + "");        //管线长度

        //设置提交页面默认值
        params.put("tubularProduct", "铸铁");        //管材
        params.put("pipType", "普通供水管线");        //管线类型
        params.put("layingType", "直埋");        //敷设类型
        if (savePointVo == null) {
            params.put("caliber", "20");        //口径
            params.put("administrativeRegion", "");    //行政区
        } else {
            params.put("caliber", savePointVo.getCaliber() == null ? "20" : savePointVo.getCaliber());        //口径
            params.put("administrativeRegion", savePointVo.getAdministrativeRegion() == null
                    ? "" : savePointVo.getAdministrativeRegion());    //行政区
        }


        if (projectVo != null) {
            params.put(OkHttpParam.PROJECT_ID, projectVo.getProjectId());        //工程ID

            int pointSize = 0;
            //点击时查询当前工程有多少数据
            if (projectVo.getProjectShareCode() != null &&
                    !projectVo.getProjectShareCode().equals("")) {
                params.put(OkHttpParam.SHARE_CODE, projectVo.getProjectShareCode());        //共享码
                params.put(OkHttpParam.IS_PROJECT_SHARE, "0");    //表示共享
                params.put(OkHttpParam.PROJECT_NAME, projectVo.getProjectName());    //工程名字
                params.put(OkHttpParam.PROJECT_TYPE, projectVo.getProjectType());    //工程类型
            } else {
                params.put(OkHttpParam.SHARE_CODE, projectVo.getProjectShareCode());        //共享码
                params.put(OkHttpParam.IS_PROJECT_SHARE, "1");    //表示不共享
                params.put(OkHttpParam.PROJECT_NAME, projectVo.getProjectName());    //工程名字
                params.put(OkHttpParam.PROJECT_TYPE, projectVo.getProjectType());    //工程类型
            }
            String projectType = projectVo.getProjectType();
            String taskNum = projectVo.getProjectNum();

            pointSize = projectVo.getAutoNumberLine();

            if (projectType != null) {
                int pos = mType.lastIndexOf(projectType);
                params.put(OkHttpParam.PIP_NAME, symbolList.get(pos) + "L" + taskNum + pointSize);
            }
        }
        newFormInfo.putExtra("iParams", params);
        newFormInfo.putExtra("template", "ins_data_entry_line_cc.xml");     //连续点连续线需要写死这个配置
//        newFormInfo.putExtra("template", solutionName);
        Bundle bundle = new Bundle();
        bundle.putString("lineOrpointline", MapMeterMoveScope.POINT);      //判断提交的是线还是点线
        newFormInfo.putExtra("pointLineBundle", bundle);
        mActivity.startActivityForResult(newFormInfo, 5);
    }


    /**
     * 撤销 点击事件
     * @param continuity_point
     * @param marker
     * @param revocation_line
     * @param property
     * @param input_line
     * @param linePointList
     * @param markerOverlayList
     * @param lineOverlayList
     * @param lineList
     */
    public void revocation_line_Onclick(View continuity_point,Marker marker,View revocation_line,
                                        View property,View input_line,List<LatLng> linePointList,
                                        List<Overlay> markerOverlayList,List<Overlay> lineOverlayList,
                                        List<LatLng> lineList,List<String> facLines){
        if (continuity_point.getTag().equals("yes")) {
            mBaiduMap.hideInfoWindow();

            if (marker != null) {
                marker.remove();
            }

            revocation_line.setVisibility(View.GONE);
            property.setVisibility(View.GONE);

            if (linePointList.size() >= 2) {
                input_line.setVisibility(View.VISIBLE);
            }
        } else {
            if (markerOverlayList != null && markerOverlayList.size() > 0) {
                markerOverlayList.get(markerOverlayList.size() - 1).remove();//移除集合的marker点
                markerOverlayList.remove(markerOverlayList.size() - 1);
            }
            if (lineList != null && lineList.size() > 0) {
                lineList.remove(lineList.size() - 1);//移除集合里的坐标点
            }
            if (facLines != null && facLines.size() > 0) {
                facLines.remove(facLines.size() - 1);//移除管线上的设施表号集合
            }
            if (lineOverlayList != null && lineOverlayList.size() > 0) {
                lineOverlayList.get(lineOverlayList.size() - 1).remove();
                lineOverlayList.remove(lineOverlayList.size() - 1);
            }
            mBaiduMap.hideInfoWindow();
            if (lineList.size() < 1) {
                revocation_line.setVisibility(View.GONE);
                property.setVisibility(View.GONE);

            }
        }
    }

    /**
     * 调用系统选择文件
     */
    private void showFileChooser(int inputType) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            mActivity.startActivityForResult( Intent.createChooser(intent, "Select a File to Upload"), inputType);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(mActivity, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

}
