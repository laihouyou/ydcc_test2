package com.movementinsome.caice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.util.DateUtil;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.caice.vo.StatisticsParentListVo;
import com.movementinsome.kernel.activity.FullActivity;

import java.text.ParseException;
import java.util.ArrayList;

import sam.android.utils.viewhelper.CommonExpandableListAdapter;

/**工程统计详情
 * Created by LJQ on 2017/6/2.
 */

public class StatisticalFacActivity extends FullActivity {

    private TextView stl_fac_name;
    private TextView noDate_fac;
    private ExpandableListView stl_fac_list;
    private Button stl_fac_break;

//    private StatisticsParentVo statisticsParentVo;
//    private StatisticsChileVo statisticsChileVo;
//
//    private List<StatisticsChileListVo> statisticsChileListVoList;      //一个工程里所有的点线数据，没有按天分开
//    private List<StatisticsParentListVo> statisticsParentListVoList;
    private ArrayList<StatisticsParentListVo> statisticsParentListVos;      //所有天数
    private ArrayList<ArrayList<SavePointVo>> savePointVoList;          //按天分开的  子数据 包含点线

//    private List<List<StatisticsChileListVo>> chileList;

    private CommonExpandableListAdapter<SavePointVo, StatisticsParentListVo> commonExpandableListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistical_fac_list);
        try {
            inti();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void inti() throws ParseException {

        stl_fac_name = (TextView) findViewById(R.id.stl_fac_name);
        noDate_fac = (TextView) findViewById(R.id.noDate_fac);
        stl_fac_list = (ExpandableListView) findViewById(R.id.stl_fac_list);
        stl_fac_list.setEmptyView(noDate_fac);
        stl_fac_break = (Button) findViewById(R.id.stl_fac_break);
        stl_fac_break.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        String projectName = intent.getStringExtra(OkHttpParam.PROJECT_NAME);
        stl_fac_name.setText(projectName);

        commonExpandableListAdapter =
                new CommonExpandableListAdapter<
                        SavePointVo,
                        StatisticsParentListVo>(
                        this,
                        R.layout.statictical_fac_item,
                        R.layout.statictical_parent_item
                ) {
                    @Override
                    protected void getChildView(ViewHolder viewHolder, int i, int i1, boolean b, SavePointVo statisticsChileListVos) {
                        TextView stl_fac_time = viewHolder.getView(R.id.stl_fac_time);
                        String fac_date = statisticsChileListVos.getFacPipBaseVo().getUploadTime();
                        String[] fac_date1 = fac_date.split(" ");
                        if (fac_date1.length == 2) {
                            fac_date = fac_date1[1];
                        }
                        stl_fac_time.setText(fac_date);

                        TextView stl_fac_device = viewHolder.getView(R.id.stl_fac_device);
                        stl_fac_device.setText("设备:" + statisticsChileListVos.getFacType());

                        TextView stl_fac_movetype = viewHolder.getView(R.id.stl_fac_movetype);
                        stl_fac_movetype.setText("采集方式:" + statisticsChileListVos.getGatherType());

                        TextView stl_fac_addr = viewHolder.getView(R.id.stl_fac_addr);
                        stl_fac_addr.setText("地点:" + statisticsChileListVos.getHappenAddr());

                        TextView pip_line_lenght = viewHolder.getView(R.id.pip_line_lenght);
                        TextView fac_name = viewHolder.getView(R.id.fac_name);

                        if (statisticsChileListVos.getDataType().equals(MapMeterMoveScope.POINT)) {
                            pip_line_lenght.setVisibility(View.GONE);

                            fac_name.setVisibility(View.VISIBLE);
                            fac_name.setText("设备编号:" + statisticsChileListVos.getFacName());
                        } else if (statisticsChileListVos.getDataType().equals(MapMeterMoveScope.LINE)) {
                            pip_line_lenght.setVisibility(View.VISIBLE);
                            pip_line_lenght.setText("管线长度 :" + statisticsChileListVos.getLineLenght() + "米");

                            fac_name.setVisibility(View.VISIBLE);
                            fac_name.setText("管线编号:" + statisticsChileListVos.getFacName());
                        } else {
                            pip_line_lenght.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    protected void getGroupView(ViewHolder viewHolder, int i, boolean b, StatisticsParentListVo statisticsParentListVo) {
                        TextView date_tv = viewHolder.getView(R.id.date_tv);
                        String fac_date = statisticsParentListVo.getParentDate();
                        String[] fac_date1 = fac_date.split("-");
                        if (fac_date1.length == 3) {
                            fac_date = fac_date1[1] + "-" + fac_date1[2];
                        }
                        date_tv.setText(fac_date);

                        TextView pointNum_tv = viewHolder.getView(R.id.pointNum_tv);
                        pointNum_tv.setText(statisticsParentListVo.getPointNum() + "");

                        TextView lineLenght_tv = viewHolder.getView(R.id.lineLenght_tv);
                        lineLenght_tv.setText(statisticsParentListVo.getLintLenght() + "");

                        TextView week_tv = viewHolder.getView(R.id.week_tv);
                        week_tv.setText(DateUtil.getWeekOfDate(statisticsParentListVo.getParentDate()));

                        ImageView arrows_im = viewHolder.getView(R.id.arrows_im);
                        arrows_im.setBackgroundResource(b ? R.drawable.main_mine_down : R.drawable.main_mine_arrows);
                    }
                };


        // 消息推送解析的值
//        statisticsParentVo= (StatisticsParentVo) intent.getSerializableExtra(OkHttpParam.STATISTICS_PARENT_VOS);
//        statisticsChileVo= (StatisticsChileVo) intent.getSerializableExtra(OkHttpParam.STATISTICS_CHILE_VOS);

        statisticsParentListVos = (ArrayList<StatisticsParentListVo>) intent.getSerializableExtra(OkHttpParam.STATISTICS_PARENT_VOS);
        savePointVoList = (ArrayList<ArrayList<SavePointVo>>) intent.getSerializableExtra(OkHttpParam.STATISTICS_CHILE_VOS);

        for (int i = 0; i < statisticsParentListVos.size(); i++) {
            commonExpandableListAdapter.getGroupData().add(statisticsParentListVos.get(i));
        }
        for (int i = 0; i < savePointVoList.size(); i++) {
            commonExpandableListAdapter.getChildrenData().add(savePointVoList.get(i));
        }

        stl_fac_list.setAdapter(commonExpandableListAdapter);

//        if (statisticsParentVo != null) {
//            statisticsParentListVoList = new ArrayList<>();
//            StatisticsParentListVo statisticsParentListVo;
//
//            List<String> parentDates = statisticsParentVo.getParentDates();
//            List<Double> lintLenghts = statisticsParentVo.getLintLenghts();
//            List<Integer> pointNums = statisticsParentVo.getPointNums();
//            if (parentDates != null && parentDates.size() > 0) {
//                for (int q = 0; q < parentDates.size(); q++) {
//                    statisticsParentListVo = new StatisticsParentListVo();
//                    statisticsParentListVo.setParentDate(parentDates.get(q));
//                    statisticsParentListVo.setLintLenght(lintLenghts.get(q));
//                    statisticsParentListVo.setPointNum(pointNums.get(q));
//                    statisticsParentListVoList.add(statisticsParentListVo);
//
//                    commonExpandableListAdapter.getGroupData().add((statisticsParentListVo));
//                }
//            }
//        }
//
//        if (statisticsChileVo != null) {       //每一天的点线数据集合
//            statisticsChileListVoList = new ArrayList<>();
//            StatisticsChileListVo statisticsChileListVo;
//
//            List<SavePointVo> savePointVoList = statisticsChileVo.getSavePointVos();
//            if (savePointVoList != null && savePointVoList.size() > 0) {
//                for (int i = 0; i < savePointVoList.size(); i++) {
//                    statisticsChileListVo = new StatisticsChileListVo();
//                    if (savePointVoList.get(i).getDataType().equals(MapMeterMoveScope.POINT)) {
//                        statisticsChileListVo.setDateType(MapMeterMoveScope.POINT);
//                        statisticsChileListVo.setType(savePointVoList.get(i).getImplementorName());
//                        statisticsChileListVo.setGatherType(savePointVoList.get(i).getGatherType());
//                        statisticsChileListVo.setHappenAddr(savePointVoList.get(i).getHappenAddr());
//                        statisticsChileListVo.setUploadTime(savePointVoList.get(i).getFacPipBaseVo().getUploadTime());
//                        statisticsChileListVo.setFacName(savePointVoList.get(i).getFacName());
//                    } else if (savePointVoList.get(i).getDataType().equals(MapMeterMoveScope.LINE)) {
//                        statisticsChileListVo.setDateType(MapMeterMoveScope.LINE);
//                        statisticsChileListVo.setType(savePointVoList.get(i).getPipType());
//                        statisticsChileListVo.setGatherType(savePointVoList.get(i).getGatherType());
//                        statisticsChileListVo.setHappenAddr(savePointVoList.get(i).getHappenAddr());
//                        statisticsChileListVo.setUploadTime(savePointVoList.get(i).getFacPipBaseVo().getUploadTime());
//                        statisticsChileListVo.setPipelineLinght(savePointVoList.get(i).getPipelineLinght());
//                        statisticsChileListVo.setFacName(savePointVoList.get(i).getPipName());
//                    }
//                    statisticsChileListVoList.add(statisticsChileListVo);
//                }
//            }
//
////            List<SavePointVo> saveLineVoList=statisticsChileVo.getSaveLineVos();
////            if (saveLineVoList!=null&&saveLineVoList.size()>0){
////                for (int j=0;j<saveLineVoList.size();j++){
////                    statisticsChileListVo=new StatisticsChileListVo();
////                    statisticsChileListVo.setDateType(MapMeterMoveScope.LINE);
////                    statisticsChileListVo.setType(saveLineVoList.get(j).getPipType());
////                    statisticsChileListVo.setGatherType(saveLineVoList.get(j).getGatherType());
////                    statisticsChileListVo.setHappenAddr(saveLineVoList.get(j).getHappenAddr());
////                    statisticsChileListVo.setUploadTime(saveLineVoList.get(j).getUploadTime());
////                    statisticsChileListVo.setPipelineLinght(saveLineVoList.get(j).getPipelineLinght());
////                    statisticsChileListVo.setFacName(saveLineVoList.get(j).getPipName());
////                    statisticsChileListVoList.add(statisticsChileListVo);
////                }
////            }
//        }
//
//        //statisticsChileListVoList  一个工程里所有的点线数据，没有按天分开
//        //这里要对statisticsChileListVoList进行按天分数据
//        if (statisticsParentListVoList != null && statisticsParentListVoList.size() > 0) {
//            chileList = new ArrayList<>();
//            SimpleDateFormat formater1 = new SimpleDateFormat(LONG_DATE_FORMAT);
//            for (int w = 0; w < statisticsParentListVoList.size(); w++) {
//                Date gather = formater1.parse(statisticsParentListVoList.get(w).getParentDate());
//                List<StatisticsChileListVo> statisticsChileListVoList1 = new ArrayList<>();
//                for (int e = 0; e < statisticsChileListVoList.size(); e++) {
//                    Date chileDate = formater1.parse(statisticsChileListVoList.get(e).getUploadTime());
//                    if (gather.getTime() == chileDate.getTime()) {
//                        statisticsChileListVoList1.add(statisticsChileListVoList.get(e));
//                    }
//                }
//                chileList.add(statisticsChileListVoList1);
//                commonExpandableListAdapter.getChildrenData().add(statisticsChileListVoList1);
//            }
//        }

        commonExpandableListAdapter.notifyDataSetChanged();

    }
}
