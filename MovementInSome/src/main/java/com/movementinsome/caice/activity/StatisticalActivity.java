package com.movementinsome.caice.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.ItemDecoration.GridItemDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.pub.dialog.DateTimeDialog;
import com.movementinsome.app.pub.util.Arith;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.util.CreateFiles;
import com.movementinsome.caice.util.DateUtil;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.caice.vo.StatisticsChileVo;
import com.movementinsome.caice.vo.StatisticsParentListVo;
import com.movementinsome.caice.vo.StatisticsParentVo;
import com.movementinsome.commonAdapter.adapterView.CommonListViewAdapter;
import com.movementinsome.commonAdapter.recycleView.CommonRecycleViewAdapter;
import com.movementinsome.commonAdapter.recycleView.base.ViewHolder;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.kernel.initial.model.ProjectType;
import com.movementinsome.map.utils.MapMeterScope;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.movementinsome.caice.util.DateUtil.LONG_DATE_FORMAT;

/**工程统计
 * Created by LJQ on 2017/5/26.
 */

public class StatisticalActivity extends FullActivity implements View.OnClickListener,AdapterView.OnItemClickListener {
    private Button stl_break;
    private TextView stl_start_time;
    private TextView stl_stopt_time;

    private TextView noDateTv;
    private View listview_handerview;
    private LRecyclerView mRecyclerView;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private CommonRecycleViewAdapter commonAdapter;
    private List<String> projectTypeList;

    private List<ProjectVo> miningSurveyList;

    private int selectedPos = 0;

    private String moveType;
    private ListView stl_listview;
    private CommonListViewAdapter statisticalAdapter;

    private List<StatisticsParentVo> statisticsParentVos=new ArrayList<>();
    private List<StatisticsChileVo> statisticsChileVos=new ArrayList<>();

    private DateBroadcastReceiver dateReceiver;
    private DateTimeDialog dateDialog;

//    private List<StatisticsParentListVo> statisticsParentListVos;
    private ArrayList<List<StatisticsParentListVo>> listStatisticsParentListVos;
//    private ArrayList<List<SavePointVo>> listSavePointList;
    private ArrayList<ArrayList<ArrayList<SavePointVo>>> listArrayListSavePointList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistical_activity);
        inti();
        initOath();
    }

    private void initOath() {
        dateDialog=new DateTimeDialog();

        // 注册广播
        dateReceiver = new DateBroadcastReceiver();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(Constant.DATE);
        registerReceiver(dateReceiver, filter1);
    }

    private void inti() {
        listview_handerview= getLayoutInflater().inflate(R.layout.listview_hadler,null);

        projectTypeList=new ArrayList<>();
        final List<ProjectType> projectTypes=AppContext.getInstance().getProjectType();
        if (projectTypes!=null&&projectTypes.size()>0){
            for (int i=0;i<projectTypes.size();i++){
                projectTypeList.add(projectTypes.get(i).getName());
            }
        }
        projectTypeList.add(0,"全部");

        mRecyclerView = (LRecyclerView) listview_handerview.findViewById(R.id.recyView);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(manager);

        GridItemDecoration divider = new GridItemDecoration.Builder(this)
                .setHorizontal(R.dimen.dp_10)
                .setVertical(R.dimen.dp_0)
                .setColorResource(R.color.point_facility7)
                .build();
        mRecyclerView.addItemDecoration(divider);

        mRecyclerView.setHasFixedSize(true);

        commonAdapter=new CommonRecycleViewAdapter<String>(this,R.layout.item_recycler_grid_text,projectTypeList) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.item_recycler_grid_tv,s);
                if (selectedPos+1==position){
                    holder.setBackgroundRes(R.id.item_recycler_grid_tv,R.drawable.blue_button_y);
                    holder.setTextColor(R.id.item_recycler_grid_tv,R.color.white);
                }else {
                    holder.setBackgroundRes(R.id.item_recycler_grid_tv,R.drawable.while_button_y);
                    holder.setTextColor(R.id.item_recycler_grid_tv,R.color.point_facility10);
                }
            }
        };
        lRecyclerViewAdapter=new LRecyclerViewAdapter(commonAdapter);
        mRecyclerView.setAdapter(lRecyclerViewAdapter);

        //禁用下拉刷新与加载更多
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadMoreEnabled(false);

        lRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mRecyclerView.scrollToPosition(position);
                commonAdapter.notifyItemChanged(selectedPos);
                selectedPos=position;
                commonAdapter.notifyItemChanged(position);

                moveType=projectTypeList.get(position);
                updateListView(stl_start_time.getText().toString(),stl_stopt_time.getText().toString(), moveType);
            }
        });

        stl_start_time = (TextView) listview_handerview.findViewById(R.id.stl_start_time);
        stl_start_time.setOnClickListener(this);
        stl_stopt_time = (TextView) listview_handerview. findViewById(R.id.stl_stopt_time);
        stl_stopt_time.setOnClickListener(this);
        stl_break = (Button) findViewById(R.id.stl_break);
        stl_break.setOnClickListener(this);
        noDateTv = (TextView) findViewById(R.id.noDateTv);
        stl_listview = (ListView) findViewById(R.id.stl_listview);
        stl_listview.setEmptyView(noDateTv);
        stl_listview.addHeaderView(listview_handerview);
        moveType = "全部";
        updateListView(stl_start_time.getText().toString(),stl_stopt_time.getText().toString(), moveType);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.stl_break:
                finish();
                break;

            case R.id.stl_start_time:
                dateDialog.showDateDialog2(this,stl_start_time);
                break;

            case R.id.stl_stopt_time:
                dateDialog.showDateDialog2(this,stl_stopt_time);
                break;

            default:
                break;
        }
    }

    private void updateListView(String startDate,String endDate, String type) {
        try {
            Dao<ProjectVo, Long> miningSurveyVODao = AppContext.getInstance()
                    .getAppDbHelper().getDao(ProjectVo.class);

            if ((startDate!=null&&!startDate.equals(""))
                    &&(endDate!=null&&!endDate.equals(""))){
                if (DateUtil.differentDays(stl_start_time.getText().toString(),stl_stopt_time.getText().toString())>=0){
                    Date beforeDay = DateUtil.stringtoDate(stl_start_time.getText().toString(), LONG_DATE_FORMAT);
                    Date afterDay = DateUtil.stringtoDate(stl_stopt_time.getText().toString(), LONG_DATE_FORMAT);
                    List<Date> dateList=DateUtil.getBetweenDates(beforeDay,afterDay);
                    if (dateList.size()>0){
                        if (type.equals("全部")) {
                            miningSurveyList = miningSurveyVODao.queryForEq(OkHttpParam.USER_ID,
                                    AppContext.getInstance().getCurUser().getUserId());
                            List<ProjectVo> miningSurveyList2=new ArrayList<>();
                            if (miningSurveyList!=null&&miningSurveyList.size()>0){
                                SimpleDateFormat formater = new SimpleDateFormat(LONG_DATE_FORMAT);
                                for (int i=0;i<dateList.size();i++){
                                    for (int j=0;j<miningSurveyList.size();j++){
                                        if (dateList.get(i).getTime()==
                                                formater.parse(miningSurveyList.get(j).getProjectCreateDateStr()).getTime()){
                                            miningSurveyList2.add(miningSurveyList.get(j));
                                        }
                                    }
                                }
                            }
                            setProjcetDateText(miningSurveyList2);
                        } else {
                            Map<String ,Object> map=new HashMap<>();
                            map.put(OkHttpParam.USER_ID,AppContext.getInstance().getCurUser().getUserId());
                            map.put("projectType",type);
                            miningSurveyList = miningSurveyVODao.queryForFieldValues(map);
                            List<ProjectVo> miningSurveyList2=new ArrayList<>();
                            if (miningSurveyList!=null&&miningSurveyList.size()>0){
                                SimpleDateFormat formater = new SimpleDateFormat(LONG_DATE_FORMAT);
                                for (int i=0;i<dateList.size();i++){
                                    for (int j=0;j<miningSurveyList.size();j++){
                                        if (dateList.get(i).getTime()==
                                                formater.parse(miningSurveyList.get(j).getProjectCreateDateStr()).getTime()){
                                            miningSurveyList2.add(miningSurveyList.get(j));
                                        }
                                    }
                                }
                            }
                            setProjcetDateText(miningSurveyList2);
                        }

                    }
                }else {
                    stl_start_time.setText("");
                    stl_stopt_time.setText("");
                    stl_start_time.setHint("开始日期");
                    stl_stopt_time.setHint("结束日期");
                    if (type.equals("全部")) {
                        miningSurveyList = miningSurveyVODao.queryForEq(OkHttpParam.USER_ID,
                                AppContext.getInstance().getCurUser().getUserId());
                        setProjcetDateText(miningSurveyList);
                    } else {
                        Map<String ,Object> map=new HashMap<>();
                        map.put(OkHttpParam.USER_ID,AppContext.getInstance().getCurUser().getUserId());
                        map.put("projectType",type);
                         miningSurveyList = miningSurveyVODao.queryForFieldValues(map);
                        setProjcetDateText(miningSurveyList);
                    }
                    Toast.makeText(this,"日期选择错误，请重新选择",Toast.LENGTH_SHORT).show();
                }
            }else {
                if (type.equals("全部")) {
                    miningSurveyList = miningSurveyVODao.queryForEq(OkHttpParam.USER_ID,
                            AppContext.getInstance().getCurUser().getUserId());
                    setProjcetDateText(miningSurveyList);
                } else {
                    Map<String ,Object> map=new HashMap<>();
                    map.put(OkHttpParam.USER_ID,AppContext.getInstance().getCurUser().getUserId());
                    map.put("projectType",type);
                    miningSurveyList = miningSurveyVODao.queryForFieldValues(map);
                    setProjcetDateText(miningSurveyList);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setProjcetDateText(final List<ProjectVo> miningSurveyList)throws java.sql.SQLException, ParseException{
        if (miningSurveyList.size()>0){
            Dao<SavePointVo, Long> savePointDao = AppContext
                    .getInstance().getSavePointVoDao();
//            statisticsParentListVos=new ArrayList<>();
//            listSavePointList=new ArrayList<>();
            listArrayListSavePointList=new ArrayList<>();
            listStatisticsParentListVos=new ArrayList<>();
            SimpleDateFormat formater1 = new SimpleDateFormat(LONG_DATE_FORMAT);
            for (int i = 0; i < miningSurveyList.size(); i++) {
                List<String> gatherDates=new ArrayList<>();
                StatisticsParentListVo statisticsParentListVo;
                Integer facDateNum = 0;
                Double lineLenght=0.0;

//                Integer facDateNumAll = 0;
//                Double lineLenghtAll=0.0;

                ProjectVo projectVo =miningSurveyList.get(i);
                Map<String,Object> map=new HashMap<>();
                map.put(OkHttpParam.PROJECT_ID, projectVo.getProjectId());
                List<SavePointVo> savePointVos=savePointDao.
                        queryForFieldValues(map);
                if (savePointVos.size()>0){
                    for (int j = 0; j < savePointVos.size(); j++) {
                        //提取每一个工程的所有设施点的采集日期，按天分开
                        String gatherDate=savePointVos.get(j).getFacPipBaseVo().getUploadTime();
                        String dataStr=gatherDate.split(" ")[0];
                        if (!gatherDates.contains(dataStr)){        //不包含
                            gatherDates.add(dataStr);

                        }

//                        //获取工程设施点数量及管线长度
//                        if (savePointVos.get(j).getDataType().equals(MapMeterScope.POINT)){
//                            facDateNumAll++;
//                        }else if (savePointVos.get(j).getDataType().equals(MapMeterScope.LINE)){
//                            lineLenghtAll+=savePointVos.get(j).getLineLenght();
//                        }
                    }


                    ArrayList<StatisticsParentListVo> statisticsParentListVos=new ArrayList<>();
                    ArrayList<ArrayList<SavePointVo>> listSavePointList=new ArrayList<>();
                    for (int j = 0; j < gatherDates.size(); j++) {      //每一个采集日期
                        statisticsParentListVo=new StatisticsParentListVo();
                        ArrayList<SavePointVo> savePointVoList=new ArrayList<>();
                        Date gather=formater1.parse(gatherDates.get(j));
                        for (int k = 0; k < savePointVos.size(); k++) {
                            SavePointVo savePointVo=savePointVos.get(k);
                            Date facDate=formater1.parse(savePointVo.getFacPipBaseVo().getUploadTime());
                            if (facDate.getTime()==gather.getTime()){
                                savePointVoList.add(savePointVo);
                                if (savePointVo.getDataType().equals(MapMeterScope.POINT)){
                                    facDateNum++;
                                }else if (savePointVo.getDataType().equals(MapMeterScope.LINE)){
                                    lineLenght+=savePointVo.getPipelineLinght();
                                }
                            }
                        }

                        statisticsParentListVo.setParentDate(gatherDates.get(j)); //日期
                        statisticsParentListVo.setPointNum(facDateNum);
                        statisticsParentListVo.setLintLenght(lineLenght);
                        facDateNum=0;
                        lineLenght=0.0;

                        statisticsParentListVos.add(statisticsParentListVo);
                        listSavePointList.add(savePointVoList);
                    }
                    listStatisticsParentListVos.add(statisticsParentListVos);
                    listArrayListSavePointList.add(listSavePointList);
                }else {
                    listStatisticsParentListVos.add(new ArrayList<StatisticsParentListVo>());
                    listArrayListSavePointList.add(new ArrayList<ArrayList<SavePointVo>>());
                }
            }
            if (statisticalAdapter==null){
                setAdapterData(miningSurveyList);
            }else {
                statisticalAdapter.setmDatas(miningSurveyList);
            }
        }else {
            statisticalAdapter.setmDatas(miningSurveyList);
            stl_listview.setEmptyView(null);
        }
    }

    private void setProjcetDate(final List<ProjectVo> miningSurveyList) throws java.sql.SQLException, ParseException {
        if (miningSurveyList.size() > 0) {

            statisticsParentVos.clear();
            statisticsChileVos.clear();

            //查询每个工程管线总长度
            Dao<SavePointVo, Long> savePointDao = AppContext
                    .getInstance().getAppDbHelper()
                    .getDao(SavePointVo.class);
            List<Double> projcetLineLenghts = new ArrayList<>();
            List<Integer> pointVoList=new ArrayList<>();
            int pointVos=0;
            List<String> gatherDates;   //当个工程日期集合
            List<SavePointVo> savePointVoList;       //每一天的点数据集合
//            List<SaveLineVo> saveLineVoList;            //每一天的线数据集合
            StatisticsParentVo statisticsParentVo;     //一个工程的父数据集合
            StatisticsChileVo statisticsChileVo;        //一个工程的孩子数据集合

            for (int i = 0; i < miningSurveyList.size(); i++) {
                ProjectVo projectVo =miningSurveyList.get(i);
                gatherDates=new ArrayList<>();
                savePointVoList=new ArrayList<>();
//                saveLineVoList=new ArrayList<>();
                statisticsParentVo=new StatisticsParentVo();
                statisticsChileVo=new StatisticsChileVo();
                List<SavePointVo> savePointVoList1 = null;
//                Map<String,Object> map=new HashMap<>();
//                map.put(OkHttpParam.PROJECT_ID, projectVo.getProjectId());
//                map.put("dataType", MapMeterMoveScope.LINE);
//                List<SavePointVo> saveLineVos=savePointDao.
//                        queryForFieldValues(map);
//                double projcetLineLenght = 0;
//                if (saveLineVos != null && saveLineVos.size() > 0) {
////                    //获取管线名字集合
////                    List<String > pipNameList=new ArrayList<>();
////                    savePointVoList1=new ArrayList<>();
////                    for (int k=0;k<saveLineVos.size();k++){
////                        if (!pipNameList.contains(saveLineVos.get(k).getPipName())){
////                            pipNameList.add(saveLineVos.get(k).getPipName());
////                            savePointVoList1.add(saveLineVos.get(k));
////                        }
////                    }
////
////                    for (int j = 0; j < savePointVoList1.size(); j++) {
////                        projcetLineLenght = Arith.add(projcetLineLenght, savePointVoList1.get(j).getPipelineLinght());
////
////                        //提取每一个工程的所有设施点的采集日期，按天分开
////                        String gatherDate=savePointVoList1.get(j).getFacPipBaseVo().getUploadTime();
////                        gatherDates.add(gatherDate.split(" ")[0]);
////                    }
//
//                    for (int j = 0; j < saveLineVos.size(); j++) {
//                        projcetLineLenght = Arith.add(projcetLineLenght, saveLineVos.get(j).getPipelineLinght());
//                    }
//                }else {
//                    savePointVoList1=new ArrayList<>();
//                }
//                projcetLineLenghts.add(projcetLineLenght);
//                projectVo.setProjcetLineLenghts(projcetLineLenght);

                //查询每个工程设施点数量
                Map<String,Object> map2=new HashMap<>();
                map2.put(OkHttpParam.PROJECT_ID, projectVo.getProjectId());
                List<SavePointVo> pointVos1=savePointDao.
                        queryForFieldValues(map2);
                if (pointVos1!=null&&pointVos1.size()>0){
                    pointVos=pointVos1.size();
                    pointVoList.add(pointVos);
                    for (int k=0;k<pointVos1.size();k++){
                        //提取每一个工程的所有设施点的采集日期，按天分开
                        String gatherDate=pointVos1.get(k).getFacPipBaseVo().getUploadTime();
                        String dataStr=gatherDate.split(" ")[0];
                        if (!gatherDates.contains(dataStr)){
                            gatherDates.add(dataStr);

                            statisticsParentVo.setParentDates(gatherDates);
                        }
                    }
                }else {
                    pointVoList.add(0);
                }

                List<SavePointVo> savePointVos=new ArrayList<>();
                savePointVos.addAll(pointVos1);
                savePointVos.addAll(savePointVoList1);

                //每个工程日期去重
                gatherDates= CreateFiles.removeDuplicateWithList(gatherDates);
                statisticsParentVo.setParentDates(gatherDates);

                List<Double> dayLineLenghts=new ArrayList<>();
                List<Integer> dayPointNums=new ArrayList<>();
                for (int w=0;w<statisticsParentVo.getParentDates().size();w++){     //每一天
                    double dayLineLenght=0;
                    SimpleDateFormat formater1 = new SimpleDateFormat(LONG_DATE_FORMAT);
                    Date gather=formater1.parse(gatherDates.get(w));
                    for (int q=0;q<savePointVos.size();q++){
                        Date lineDate=formater1.parse(savePointVos.get(q).getFacPipBaseVo().getUploadTime());
                        if (gather!=null){
                            if (gather.getTime()==lineDate.getTime()){
                                if (savePointVos.get(q).getDataType().equals(MapMeterMoveScope.LINE)){
                                    savePointVoList.add(savePointVos.get(q));
                                    dayLineLenght= Arith.add(dayLineLenght,savePointVos.get(q).getPipelineLinght());
                                }
                            }
                        }
                    }
                    statisticsChileVo.setSaveLineVos(savePointVoList);   //每一天线数据
                    dayLineLenghts.add(dayLineLenght);
                    statisticsParentVo.setLintLenghts(dayLineLenghts);

                    int dayPointNum=0;
                    for (int e=0;e<savePointVos.size();e++){
                        Date pointDate=formater1.parse(savePointVos.get(e).getFacPipBaseVo().getUploadTime());
                        if (gather!=null){
                            if (gather.getTime()==pointDate.getTime()){
                                if (savePointVos.get(e).getDataType().equals(MapMeterMoveScope.POINT)){
                                    savePointVoList.add(savePointVos.get(e));
                                    dayPointNum=dayPointNum+1;
                                }
                            }
                        }
                    }
                    statisticsChileVo.setSavePointVos(savePointVoList);
                    dayPointNums.add(new Integer(dayPointNum));
                    statisticsParentVo.setPointNums(dayPointNums);

                }
                statisticsParentVos.add(statisticsParentVo);
                statisticsChileVos.add(statisticsChileVo);

            }
            if (statisticalAdapter==null){
                setAdapterData(miningSurveyList);
            }else {
                statisticalAdapter.setmDatas(miningSurveyList);
            }

        }else {

            statisticalAdapter.setmDatas(miningSurveyList);
            stl_listview.setEmptyView(null);
        }
    }

    private void setAdapterData(final List<ProjectVo> miningSurveyList) throws SQLException {
        //获取每一个工程下面 设施点与管线  gatherDateList.get(q) 日期下的集合
        statisticalAdapter = new CommonListViewAdapter<ProjectVo>(StatisticalActivity.this,R.layout.statictical_item,miningSurveyList) {
            @Override
            protected void convert(com.movementinsome.commonAdapter.adapterView.ViewHolder viewHolder, ProjectVo item, int position) {
                viewHolder.setText(R.id.stl_item_proName,item.getProjectName());
                viewHolder.setText(R.id.stl_item_proType,item.getProjectType());
                viewHolder.setText(R.id.stl_item_time,item.getProjectCreateDateStr());
                viewHolder.setText(R.id.stl_item_facNum,item.getPointSize()+"个");
                viewHolder.setText(R.id.stl_item_faclength,item.getLineLenght()+"米");
            }
        };
        stl_listview.setAdapter(statisticalAdapter);
        stl_listview.setOnItemClickListener(this);
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(dateReceiver);
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (i==0){
            return;
        }
        Intent intent = new Intent(StatisticalActivity.this, StatisticalFacActivity.class);
        intent.putExtra(OkHttpParam.STATISTICS_PARENT_VOS, (Serializable) listStatisticsParentListVos.get(i-1));
        intent.putExtra(OkHttpParam.STATISTICS_CHILE_VOS,  listArrayListSavePointList.get(i-1));
        startActivity(intent);
    }

    private class DateBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constant.DATE)){

                updateListView(stl_start_time.getText().toString(),stl_stopt_time.getText().toString(), moveType);

            }
        }
    }

}
