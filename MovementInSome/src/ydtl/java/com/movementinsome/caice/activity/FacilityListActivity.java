package com.movementinsome.caice.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.caice.adapter.SwipeMenuAdapter;
import com.movementinsome.caice.async.DeletePointLineAsync;
import com.movementinsome.caice.util.CreateFiles;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.view.CustomDialog;
import com.movementinsome.caice.vo.MiningSurveyVO;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.caice.vo.StatisticsChileListVo;
import com.movementinsome.database.vo.DynamicFormVO;
import com.movementinsome.kernel.util.ActivityUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**设施列表详情页面
 * Created by zzc on 2017/6/10.
 */

public class FacilityListActivity extends Activity implements View.OnClickListener{

    private TextView check_all;
    private TextView tv_cancel;
    private TextView submitTv;
    private TextView noDate_resub;
    private TextView title_fac;
    private LRecyclerView submit_recy;
    private SwipeMenuAdapter adapter;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private Button resubmit_back;

    private RelativeLayout submitFr;
    private List<StatisticsChileListVo> statisticsChileListVoList;      //一个工程里所有的点线数据，没有按天分开

    private StatisticsChileListVo statisticsChileListVo;
    private Dao<SavePointVo,Long> savePointDao;
//    private Dao<SaveLineVo,Long> saveLineVoDao;
    private Dao<DynamicFormVO, Long> dynamicFormDao = null;
    private List<DynamicFormVO> dynamicFormList;


    private String projectId;
    private Context context;

    // 用来控制CheckBox的选中状况
    private static Map<Integer, Boolean> isSelected;

    private CustomDialog customDialog;
    private CustomDialog.Builder builder;

    private TraceReceiver receiver;

    private boolean isShowCheckBox=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resubmit);
        context=this;
        try {
            initDate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initView();
        initOath();
    }

    private void initOath() {
        // 注册广播
        receiver = new TraceReceiver();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(Constant.DELETE_DATE);
        registerReceiver(receiver, filter1);
    }

    private void initDate() throws SQLException {
        builder = new CustomDialog.Builder(this);
        dynamicFormDao = AppContext.getInstance()
                .getAppDbHelper().getDao(DynamicFormVO.class);

        Intent intent=getIntent();
        MiningSurveyVO miningSurveyVO= (MiningSurveyVO) intent.getSerializableExtra("miningSurveyVO");
        if (miningSurveyVO!=null){
            projectId=miningSurveyVO.getProjectId();
            savePointDao= AppContext.getInstance().getAppDbHelper().getDao(SavePointVo.class);

            setPointLineData(projectId);
        }
    }

    private void setPointLineData(String projectId) throws SQLException {
        statisticsChileListVoList=new ArrayList<>();
        List<SavePointVo> saveLineList=new ArrayList<>();
        List<SavePointVo> savePointList=savePointDao.queryForEq(OkHttpParam.PROJECT_ID,projectId);
        if (savePointList!=null&&savePointList.size()>0){
            //获取设施数据
            for (int i=0;i<savePointList.size();i++){
                statisticsChileListVo=new StatisticsChileListVo();
                SavePointVo savePointVo=savePointList.get(i);
                if (savePointVo.getDataType()!=null&&savePointVo.getDataType().equals(MapMeterMoveScope.POINT)){
                    statisticsChileListVo.setPointDatabaseId(savePointVo.getId());
                    statisticsChileListVo.setFacName(savePointVo.getFacName());
                    statisticsChileListVo.setDateType(MapMeterMoveScope.POINT);
                    statisticsChileListVo.setType(savePointVo.getImplementorName());
                    statisticsChileListVo.setGatherType(savePointVo.getGatherType());
                    statisticsChileListVo.setHappenAddr(savePointVo.getHappenAddr());
                    statisticsChileListVo.setUploadTime(savePointVo.getUploadTime());
                    statisticsChileListVo.setLongitude(savePointVo.getLongitude());
                    statisticsChileListVo.setLatitude(savePointVo.getLatitude());
                    statisticsChileListVo.setPointId(savePointVo.getId());
                    statisticsChileListVo.setIsPresent(savePointVo.getIsPresent());
                    statisticsChileListVo.setIsLeak(savePointVo.getIsLeak());
                    statisticsChileListVoList.add(statisticsChileListVo);
                }else if (savePointVo.getDataType()!=null&&savePointVo.getDataType().equals(MapMeterMoveScope.LINE)){
                    saveLineList.add(savePointVo);
                }
            }
            //获取管线数据
            if (saveLineList.size()>0){
                List<String> pipName=new ArrayList<>();
                for (int i=0;i<saveLineList.size();i++){
                    pipName.add(saveLineList.get(i).getPipName());
                }
                pipName= CreateFiles.removeDuplicateWithList(pipName);

                if (pipName.size()>0){
                    for (int j=0;j<pipName.size();j++){
                        saveLineList=savePointDao.queryForEq("pipName",pipName.get(j));
                        if (saveLineList.size()>0){
                            //获取每条管线的IDS集合
                            String ids="";
                            for (int k = 0; k<saveLineList.size(); k++){
                                if (k==saveLineList.size()-1){
                                    ids+=saveLineList.get(k).getId();
                                }else {
                                    ids+=saveLineList.get(k).getId()+",";
                                }
                            }
                            for (int i=0;i<saveLineList.size();i++){
                                if (saveLineList.get(i).getDrawNum()!=null&&
                                        Integer.parseInt(saveLineList.get(i).getDrawNum())==
                                                (saveLineList.size()-1)){      //判断是不是管线最后一个点
                                    statisticsChileListVo=new StatisticsChileListVo();
                                    statisticsChileListVo.setLineDatabaseId(saveLineList.get(i).getId());
                                    statisticsChileListVo.setFacName(saveLineList.get(i).getPipName());
                                    statisticsChileListVo.setDateType(MapMeterMoveScope.LINE);
                                    statisticsChileListVo.setType(saveLineList.get(i).getPipType());
                                    statisticsChileListVo.setGatherType(saveLineList.get(i).getGatherType());
                                    statisticsChileListVo.setHappenAddr(saveLineList.get(i).getHappenAddr());
                                    statisticsChileListVo.setUploadTime(saveLineList.get(i).getUploadTime());
                                    statisticsChileListVo.setPointList(saveLineList.get(i).getPointList());
                                    statisticsChileListVo.setLineIds(ids);
                                    statisticsChileListVo.setIsPresent(saveLineList.get(i).getIsPresent());
                                    statisticsChileListVoList.add(statisticsChileListVo);
                                }
                            }
                        }
                    }
                }
            }

        }

    }

    private void initView() {
        check_all= (TextView) findViewById(R.id.check_all);
        tv_cancel= (TextView) findViewById(R.id.tv_cancel);
        submitTv= (TextView) findViewById(R.id.submitTv);
        noDate_resub= (TextView) findViewById(R.id.noDate_resub);
        submit_recy= (LRecyclerView) findViewById(R.id.submit_recy);
        resubmit_back= (Button) findViewById(R.id.resubmit_back);
        submitFr= (RelativeLayout) findViewById(R.id.submitFr);
        title_fac= (TextView) findViewById(R.id.title_fac);

        check_all.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        submitTv.setOnClickListener(this);
        resubmit_back.setOnClickListener(this);

        submitTv.setText("删除");
        title_fac.setText("设施列表");

        submit_recy.setEmptyView(noDate_resub);

        submit_recy.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.dp_1)
                .setPadding(R.dimen.dp_4)
                .setColorResource(R.color.split)
                .build();

        submit_recy.setHasFixedSize(true);
        submit_recy.addItemDecoration(divider);


        isSelected = new HashMap<>();
        if (statisticsChileListVoList.size()>0){
            for (int i=1;i<=statisticsChileListVoList.size();i++){
                isSelected.put(i,false);
            }
        }
        Map<String,View> views=new HashMap<>();
        views.put("check_all",check_all);
        views.put("submitFr",submitFr);
        views.put("tv_cancel",tv_cancel);
        adapter=new SwipeMenuAdapter(this,isSelected,isShowCheckBox,views);
        adapter.addAll(statisticsChileListVoList);
        adapter.setOnDelListener(new SwipeMenuAdapter.onSwipeListener() {
            @Override
            public void onDel(final int pos) {

                customDialog = builder.cancelTouchout(false)
                        .view(R.layout.move_mining_over_dlaio)
                        .heightpx(ActivityUtil.getWindowsHetght(FacilityListActivity.this))
                        .widthpx(ActivityUtil.getWindowsWidth(FacilityListActivity.this))
                        .setTitle("删除提醒")
                        .setMsg("是否删除？")
                        .style(R.style.dialog)
                        .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    if (statisticsChileListVoList.get(pos).getDateType()
                                            .equals(MapMeterMoveScope.POINT)){
                                        DeletePointLineAsync asyn = new DeletePointLineAsync(
                                                context,
                                                statisticsChileListVoList.get(pos),
                                                true,
                                                pos
                                        );
                                        asyn.execute("");
                                    }else if (statisticsChileListVoList.get(pos).getDateType()
                                            .equals(MapMeterMoveScope.LINE)){
                                        DeletePointLineAsync asyn = new DeletePointLineAsync(
                                                context,
                                                statisticsChileListVoList.get(pos),
                                                true,
                                                pos
                                        );
                                        asyn.execute("");
                                    }

                                    customDialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .addViewOnclick(R.id.cancelOverIm, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialog.dismiss();
                            }
                        })
                        .addViewOnclick(R.id.cancelOverBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialog.dismiss();
                            }
                        })
                        .build();
                customDialog.show();

            }

            @Override
            public void onTop(int pos) {

            }
        });
        lRecyclerViewAdapter=new LRecyclerViewAdapter(adapter);
        submit_recy.setAdapter(lRecyclerViewAdapter);


        //禁用下拉刷新与加载更多
        submit_recy.setPullRefreshEnabled(false);
        submit_recy.setLoadMoreEnabled(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.check_all:    //全选
                switch ((String) check_all.getTag()) {
                    case "check_all":   //全选
                        // 遍历list的长度，将MyAdapter中的map值全部设为true
                        for (int i = 1; i <= statisticsChileListVoList.size(); i++) {
                            isSelected.put(i, true);
                        }
                        // 刷新listview和TextView的显示
                        adapter.notifyDataSetChanged();
                        check_all.setTag("uncheck_all");
                        check_all.setText("全不选");
                        break;
                    case "uncheck_all":     //全不选
                        // 遍历list的长度，将MyAdapter中的map值全部设为true
                        for (int i = 1; i <= statisticsChileListVoList.size(); i++) {
                            isSelected.put(i, false);
                        }
                        // 刷新listview和TextView的显示
                        adapter.notifyDataSetChanged();
                        check_all.setTag("check_all");
                        check_all.setText("全选");
                        break;
                }
                break;

            case R.id.tv_cancel:    //取消
                // 遍历list的长度，将MyAdapter中的map值全部设为true
                for (int i = 1; i <= statisticsChileListVoList.size(); i++) {
                    isSelected.put(i, false);
                }

                isShowCheckBox=false;
                adapter.setShowCheckBox(isShowCheckBox);
                // 刷新listview和TextView的显示
                adapter.notifyDataSetChanged();

                check_all.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
                submitFr.setVisibility(View.GONE);


                break;

            case R.id.submitTv:     //删除

                if (isSelected!=null&&isSelected.size()>0){
                    if (!isSelected.containsValue(true)){
                        Toast.makeText(context,"请选择要删除的设施或管线",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                customDialog = builder.cancelTouchout(false)
                        .view(R.layout.move_mining_over_dlaio)
                        .heightpx(ActivityUtil.getWindowsHetght(this))
                        .widthpx(ActivityUtil.getWindowsWidth(this))
                        .setTitle("删除提醒")
                        .setMsg("是否删除？")
                        .style(R.style.dialog)
                        .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (isSelected != null && isSelected.size() > 0) {
                                    String ids = "";

                                    try {
                                        List<StatisticsChileListVo> statisticsChileListVos=new ArrayList<StatisticsChileListVo>();
                                        for (int i = 1; i <= isSelected.size(); i++) {
                                            if (isSelected.get(i)) {
                                                statisticsChileListVos.add(statisticsChileListVoList.get(i-1));
                                            }
                                        }
                                        DeletePointLineAsync asyn = new DeletePointLineAsync(
                                                context,
                                                statisticsChileListVos,
                                                false,
                                                -1
                                        );
                                        asyn.execute("");

                                        customDialog.dismiss();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        })
                        .addViewOnclick(R.id.cancelOverIm, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialog.dismiss();
                            }
                        })
                        .addViewOnclick(R.id.cancelOverBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialog.dismiss();
                            }
                        })
                        .build();
                customDialog.show();

                break;
            case R.id.resubmit_back:    //返回
                this.finish();
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private class TraceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if (action.equals(Constant.DELETE_DATE)){
                int pos=intent.getIntExtra("pos",-1);
                boolean isSpreads=intent.getBooleanExtra("isSpreads",false);

                //删除成功后刷新列表
                try {
                    setPointLineData(projectId);
                    if (statisticsChileListVoList.size()>0){
                        isSelected.clear();
                        for (int i=1;i<=statisticsChileListVoList.size();i++){
                            isSelected.put(i,false);
                        }
                    }else {
                        isSelected.clear();
                    }
                    adapter.setIsSelected(isSelected);

                    if (pos!=-1&&isSpreads){
                        //RecyclerView关于notifyItemRemoved的那点小事 参考：http://blog.csdn.net/jdsjlzx/article/details/52131528
                        adapter.getDataList().remove(pos);
                        adapter.notifyItemRemoved(pos);//推荐用这个

                        if(pos != (adapter.getDataList().size())){ // 如果移除的是最后一个，忽略 注意：这里的mDataAdapter.getDataList()不需要-1，因为上面已经-1了
                            adapter.notifyItemRangeChanged(pos, adapter.getDataList().size() - pos);
                        }
                        //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    }else {
                        adapter.setDataList(statisticsChileListVoList);
                    }

                    if (!isSpreads){
                        if (statisticsChileListVoList!=null&&statisticsChileListVoList.size()==0){
                            submitFr.setVisibility(View.GONE);
                            check_all.setVisibility(View.GONE);
                        }else {
                            submitFr.setVisibility(View.VISIBLE);
                            check_all.setVisibility(View.VISIBLE);
                        }
                    }else {
                        submitFr.setVisibility(View.GONE);
                        check_all.setVisibility(View.GONE);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
