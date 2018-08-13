package com.movementinsome.caice.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.mytask.handle.MainTaskListDateHandle;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.caice.activity.FacilityListActivity;
import com.movementinsome.caice.async.SubAllDataAsync;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.okhttp.OkHttpRequest;
import com.movementinsome.caice.okhttp.ProjectRequest;
import com.movementinsome.caice.project.ProjectOperation;
import com.movementinsome.caice.recycleView.DividerDecoration;
import com.movementinsome.caice.util.DateUtil;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.view.CustomDialog;
import com.movementinsome.caice.vo.DetelePoiVo;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.commonAdapter.recycleView.CommonRecycleViewAdapter;
import com.movementinsome.commonAdapter.recycleView.base.ViewHolder;
import com.movementinsome.kernel.util.ActivityUtil;
import com.movementinsome.kernel.util.FileUtils;
import com.movementinsome.map.nearby.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 未完成任务列表
 * Created by Administrator on 2017/11/12.
 */

public class TaskListFragment extends Fragment implements RecyclerView.OnItemTouchListener,View.OnClickListener{

    // 数据处理类
//    private List<MoveTaskListDateBaseHandle> mainTaskListDateBaseHandle;
    private MainTaskListDateHandle mainTaskListDateHandle;
    private List<ProjectVo> projectVoList;
    private TaskListMainReceiver taskListMainReceiver;
//    private StickyTestAdapter adapter;
    private CommonRecycleViewAdapter adapter;

    boolean point=true;
    boolean proje=true;

    private CustomDialog customDialog;
    private CustomDialog.Builder builder;
    
    private Activity activity;
    private ProjectVo project;

    private LRecyclerView mList;
    protected LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private View view;
    private String taskType;

    private MyHandler myHandler;

    private FrameLayout subAllData;
    private int subAllDataIsShow;
    private static ProgressDialog progressDialog;
    private static Activity context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)  {
        context=this.getActivity();
        Bundle bundle=getArguments();
        taskType=bundle.getString(OkHttpParam.TASK_TYPE);
        view = inflater.inflate(R.layout.fragment_recycler, container, false);
        projectVoList = new ArrayList<>();

        initview();

        initOath();

//        mainTaskListDateBaseHandle = new ArrayList<>();
//        mainTaskListDateBaseHandle.add(new MainTaskListDateHandle(this.getActivity(),projectVoList));


        updateList();

        return view;
    }

    private void initview() {
        subAllData=view.findViewById(R.id.subAllData);
        subAllData.setOnClickListener(this);

        List<com.movementinsome.kernel.initial.model.View> viewList = AppContext.getInstance().getViews();
        if (viewList != null && viewList.size() > 0) {
            for (int i = 0; i < viewList.size(); i++) {
                com.movementinsome.kernel.initial.model.View view = viewList.get(i);
                if (view.getName().equals("一键重提")) {
                    if (view.getIsShow().equals("true")) {
                        subAllDataIsShow = View.VISIBLE;
                    } else if (view.getIsShow().equals("false")) {
                        subAllDataIsShow = View.GONE;
                    }
                }
            }
        }
        subAllData.setVisibility(subAllDataIsShow);

        activity=this.getActivity();
        mList = view.findViewById(R.id.list);
        mList.setPullRefreshEnabled(false);

        final DividerDecoration divider = new DividerDecoration.Builder(this.getActivity())
                .setHeight(R.dimen.dp_10)
                .setPadding(R.dimen.dp_4)
                .setColorResource(R.color.default_header_color2)
                .build();

        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mList.addItemDecoration(divider);

        adapter=new CommonRecycleViewAdapter<ProjectVo>(
                this.getActivity()
                ,R.layout.move_tasklist_item
                , projectVoList) {

            @Override
            protected void convert(ViewHolder holder, final ProjectVo projectVo, int position) {
                switch (projectVo.getProjectType()){
                    case "电力":
                        holder.setBackgroundRes(R.id.move_logo,R.drawable.power);
                        break;

                    case "电信":
                        holder.setBackgroundRes(R.id.move_logo,R.drawable.telegraphy);
                        break;

                    case "给水":
                        holder.setBackgroundRes(R.id.move_logo,R.drawable.water_delivery);
                        break;

                    case "排水":
                        holder.setBackgroundRes(R.id.move_logo,R.drawable.drain_away_water);
                        break;

                    case "燃气":
                        holder.setBackgroundRes(R.id.move_logo,R.drawable.combustion_gas);
                        break;

                    case "综合":
                        holder.setBackgroundRes(R.id.move_logo,R.drawable.synthesis);
                        break;

                    default:
                        holder.setBackgroundRes(R.id.move_logo,R.drawable.water_delivery);
                        break;
                }

                if (projectVo.getProjectStatus().equals("2")){
                    holder.setVisible(R.id.move_surveying,View.GONE);
                    holder.setVisible(R.id.move_finsh,View.GONE);
                }
                holder.setText(R.id.move_name, projectVo.getProjectName());
                holder.setText(R.id.move_type,"工程类型:"+ projectVo.getProjectType());

                if (projectVo.getProjectCreateDateStr()!=null&&!projectVo.getProjectCreateDateStr().equals("")){
                    String dateStr= DateUtil.dateToString(DateUtil.stringtoDate2(projectVo.getProjectCreateDateStr())
                            ,DateUtil.LONG_DATE_FORMAT);
                    holder.setText(R.id.move_time,dateStr);
                }

                if(projectVo.getProjectStatus().equals("1")){
                    holder.setVisible(R.id.move_stuas,View.VISIBLE);
                }else {
                    holder.setVisible(R.id.move_stuas,View.GONE);
                }

                point=true;
                //获取工程设施数据的提交情况
                List<SavePointVo> savePointVoList=getSavePointVoList(projectVo);
                if (savePointVoList!=null&&savePointVoList.size()>0){
                    for (int i=0;i<savePointVoList.size();i++){
                        SavePointVo savePointVo=savePointVoList.get(i);
                        if (savePointVo.getFacSubmitStatus()!=null){
                            if (savePointVo.getFacSubmitStatus().equals("0")){
                                //未提交
                                point=false;
                                break;
                            }else if (savePointVo.getFacSubmitStatus().equals("1")){
                                point=true;
                            }
                        }else {
                            //没有字段说明是旧版本数据
                            point=false;
                            break;
                        }
                    }
                }

                proje=true;
                if (projectVo.getProjectSubmitStatus()!=null&& projectVo.getProjectSubmitStatus().equals("0")){     //未提交
                    proje=false;
                }else if (projectVo.getProjectSubmitStatus()!=null&& projectVo.getProjectSubmitStatus().equals("1")){       //已提交
//                    if (projectVo.getIsCompile().equals("1")){     //已编辑
//                        proje=false;
//                    }else {     //已提交， 未编辑
//                    }
                    proje=true;
                }else {
                    proje=false;
                }

                //根据工程与设施的提交状态显示提交按钮
                if (proje&&point){
                    holder.setText(R.id.newest,"已同步");
                    holder.setVisible(R.id.newest,View.VISIBLE);
                    holder.setTextColor(R.id.newest,AppContext.getInstance().getResources().getColor(R.color.lightgreen));
                    holder.setVisible(R.id.move_sub,View.GONE);
                }else {
                    holder.setText(R.id.newest,"未同步");
                    holder.setVisible(R.id.newest,View.VISIBLE);
                    holder.setTextColor(R.id.newest,AppContext.getInstance().getResources().getColor(R.color.red));
                    holder.setVisible(R.id.move_sub,View.VISIBLE);
                }

                try{
                    Dao<SavePointVo, Long> savePointDao = AppContext
                            .getInstance().getAppDbHelper()
                            .getDao(SavePointVo.class);
                    Map<String, Object> v2 = new HashMap<String, Object>();
                    v2.put("projectId", projectVo.getProjectId());
                    List<SavePointVo> savePointList = savePointDao.queryForFieldValuesArgs(v2);
                    List<SavePointVo> savePointVos=new ArrayList<>();
                    if (savePointList.size()>0){
                        for (int i=0;i<savePointList.size();i++){
                            if (savePointList.get(i).getDataType().equals(MapMeterMoveScope.POINT)){
                                savePointVos.add(savePointList.get(i));
                            }
                        }
                    }
                    holder.setText(R.id.move_value_num,"设施点数量:"+savePointVos.size());
                }catch (Exception e){
                    e.printStackTrace();
                }

                holder.setOnClickListener(R.id.move_check, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TabHost s = AppContext.getInstance().getmHost();
                        s.setCurrentTabByTag("2");
                        List<TextView> tvs = AppContext.getInstance().getTvs();
                        List<ImageView> imgs = AppContext.getInstance().getImgs();
                        for (int i = 0; i < tvs.size(); i++) {
                            if (i == 0) {
                                tvs.get(i).setSelected(true);
                                imgs.get(i).setBackgroundResource(R.drawable.main_map_icon1);
                            } else if (i == 1) {
                                tvs.get(i).setSelected(false);
                                imgs.get(i).setBackgroundResource(R.drawable.main_task_icon0);
                            } else if (i == 2) {
                                tvs.get(i).setSelected(false);
                                imgs.get(i).setBackgroundResource(R.drawable.main_mine_icon0);
                            }
                        }
                        AppContext.getInstance().setProjectVo(projectVo);
                        Intent intent = new Intent();
                        intent.setPackage(AppContext.getInstance().getPackageName());
                        intent.putExtra("parameter","check");
                        intent.setAction(Constant.PASSDATE);
                        AppContext.getInstance().sendBroadcast(intent);
                    }
                });
                holder.setVisible(R.id.move_surveying, projectVo.getProjectStatus().equals("2")?View.GONE:View.VISIBLE);
                holder.setOnClickListener(R.id.move_surveying, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TabHost s = AppContext.getInstance().getmHost();
                        s.setCurrentTabByTag("2");
                        List<TextView> tvs = AppContext.getInstance().getTvs();
                        List<ImageView> imgs = AppContext.getInstance().getImgs();
                        for (int i = 0; i < tvs.size(); i++) {
                            if (i == 0) {
                                tvs.get(i).setSelected(true);
                                imgs.get(i).setBackgroundResource(R.drawable.main_map_icon1);
                            } else if (i == 1) {
                                tvs.get(i).setSelected(false);
                                imgs.get(i).setBackgroundResource(R.drawable.main_task_icon0);
                            } else if (i == 2) {
                                tvs.get(i).setSelected(false);
                                imgs.get(i).setBackgroundResource(R.drawable.main_mine_icon0);
                            }
                        }
                        AppContext.getInstance().setProjectVo(projectVo);
                        Intent intent = new Intent();
                        intent.setPackage(AppContext.getInstance().getPackageName());
                        intent.setAction(Constant.PASSDATE);
                        intent.putExtra("parameter","move");
                        AppContext.getInstance().sendBroadcast(intent);
                    }
                });
                holder.setOnClickListener(R.id.move_delect, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder = new CustomDialog.Builder(activity);
                        customDialog = builder.cancelTouchout(false)
                                .view(R.layout.move_mining_over_dlaio)
                                .heightpx(ActivityUtil.getWindowsHetght(activity))
                                .widthpx(ActivityUtil.getWindowsWidth(activity))
                                .style(R.style.dialog)
                                .setTitle("删除提醒")
                                .setMsg("确认删除？")
                                .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            if (projectVo.getProjectStatus().equals("1")) {
                                                Toast.makeText(activity, "该工程正在采测中,请暂停采测后在操作", Toast.LENGTH_SHORT).show();
                                                customDialog.dismiss();
                                                return;
                                            }
                                            Dao<ProjectVo, Long> miningSurveyVOLongDao = AppContext.getInstance()
                                                    .getAppDbHelper().getDao(ProjectVo.class);
                                            Dao<SavePointVo, Long> savePointDao = AppContext
                                                    .getInstance().getAppDbHelper()
                                                    .getDao(SavePointVo.class);
                                            Map<String, Object> map = new HashMap();
                                            map.put(OkHttpParam.PROJECT_ID, projectVo.getProjectId());
                                            map.put(OkHttpParam.USER_ID, AppContext.getInstance().getCurUser().getUserId());
                                            List<SavePointVo> savePointVoList = savePointDao.queryForFieldValues(map);
                                            if (savePointVoList.size() == 0) {

                                                //先联网删除服务器工程
                                                String projectId = projectVo.getProjectId();
//                                Map<String,Object> map=new HashMap<>();
//                                map.put(OkHttpParam.PROJECT_ID,projectId);
                                                if (projectVo.getProjectSubmitStatus() != null) {
                                                    if (projectVo.getProjectSubmitStatus().equals("0")) {      //未提交
                                                        ProjectOperation.DeteleProject(map, activity);
                                                    } else if (projectVo.getProjectSubmitStatus().equals("1")) {    //已提交
                                                        ProjectRequest.ProjectDelete(map, activity, customDialog);
                                                    }
                                                } else {
                                                    ProjectOperation.DeteleProject(map, activity);
                                                }
                                                customDialog.dismiss();
                                            } else {
                                                Toast.makeText(activity, "请删除所有设施点与管线后再进行操作", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
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
                                .build()
                        ;
                        customDialog.show();
                    }
                });
                holder.setVisible(R.id.move_finsh, projectVo.getProjectStatus().equals("2")?View.GONE:View.VISIBLE);
                holder.setOnClickListener(R.id.move_finsh, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder = new CustomDialog.Builder(activity);
                        customDialog=builder.cancelTouchout(false)
                                .view(R.layout.move_mining_over_dlaio)
                                .heightpx(ActivityUtil.getWindowsHetght(activity))
                                .widthpx(ActivityUtil.getWindowsWidth(activity))
                                .style(R.style.dialog)
                                .setTitle("完成提醒")
                                .setMsg("确认完成？")
                                .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            if (projectVo !=null){
                                                //完成前先判断工程是否已经提交
                                                if (projectVo.getProjectSubmitStatus()
                                                        .equals(OkHttpParam.PROJECT_SUBMIT_STATUS_ZERO)){
                                                    ToastUtils.show(getString(R.string.complete_msg));
                                                }else if (projectVo.getProjectSubmitStatus()
                                                        .equals(OkHttpParam.PROJECT_SUBMIT_STATUS_ONE)){
                                                    projectVo.setProjectStatus("2");    //已完成
                                                    Map<String,Object> parameterMap=new HashMap<>();
                                                    parameterMap.put(OkHttpParam.PROJECT_ID,projectVo.getProjectId());
                                                    parameterMap.put(OkHttpParam.PROJECT_STATUS,"2");
                                                    ProjectRequest.ProjectUpdate(parameterMap,projectVo, activity);

                                                    AppContext.getInstance().setProjectVo(projectVo);
                                                    Intent intent = new Intent();
                                                    intent.setPackage(activity.getPackageName());
                                                    intent.putExtra("parameter", "complete");
                                                    intent.setAction(Constant.PASSDATE);
                                                    activity.sendBroadcast(intent);
                                                }
                                                customDialog.dismiss();
                                            }
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
                                .build()
                        ;
                        customDialog.show();
                    }
                });
                holder.setVisible(R.id.move_upload, projectVo.getProjectStatus().equals("2")?View.GONE:View.VISIBLE);
                holder.setOnClickListener(R.id.move_upload, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        project=projectVo;
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult( Intent.createChooser(intent, "Select a File to Upload"),OkHttpParam.MOVE_UPLOAD);
                    }
                });

                holder.setOnClickListener(R.id.move_sub, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder = new CustomDialog.Builder(activity);
                        customDialog=builder.cancelTouchout(false)
                                .view(R.layout.move_mining_over_dlaio)
                                .heightpx(ActivityUtil.getWindowsHetght(activity))
                                .widthpx(ActivityUtil.getWindowsWidth(activity))
                                .style(R.style.dialog)
                                .setTitle("提交提醒")
                                .setMsg("确认提交？")
                                .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //获取当前工程提交状态
                                        if (projectVo.getProjectSubmitStatus()!=null&& projectVo.getProjectSubmitStatus().equals("0")){    //工程未提交
                                            ProjectRequest.ProjectCreate(projectVo,activity,true);
                                        }else if (projectVo.getProjectSubmitStatus()!=null&& projectVo.getProjectSubmitStatus().equals("1")){      //已提交
                                            Map<String,Object> parameterMap=new HashMap<>();
                                            parameterMap.put(OkHttpParam.PROJECT_ID,projectVo.getProjectId());
                                            parameterMap.put(OkHttpParam.PROJECT_SUBMIT_STATUS,"1");
                                            ProjectRequest.ProjectUpdate(parameterMap,projectVo,activity);
                                        }

                                        //获取未提交的设施数据
                                        List<SavePointVo> savePointVoList=getSavePointVoList(projectVo);
                                        List<SavePointVo> savePointVos_no_sub=new ArrayList<SavePointVo>();
                                        List<SavePointVo> savePointVos_no_com=new ArrayList<SavePointVo>();
                                        for (int i=0;i<savePointVoList.size();i++){
                                            SavePointVo savePointVo=savePointVoList.get(i);
                                            if (savePointVo.getFacSubmitStatus()!=null){
                                                if (savePointVo.getFacSubmitStatus().equals("0")){
                                                    //未提交
                                                    savePointVos_no_sub.add(savePointVoList.get(i));
                                                }
                                            }else {
                                                //没有字段说明是旧版本数据
                                                savePointVos_no_sub.add(savePointVoList.get(i));
                                            }
                                        }

                                        if (savePointVos_no_sub.size()>0){
                                            OkHttpRequest.SubmitPointList(savePointVos_no_sub,activity);
                                        }
                                        if (savePointVos_no_com.size()>0){
                                            OkHttpRequest.IsUpdatePio(savePointVos_no_com,activity, projectVo);
                                        }

                                        customDialog.dismiss();
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
                                .build()
                        ;
                        customDialog.show();
                    }
                });
                holder.setOnClickListener(R.id.facility_Re, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, FacilityListActivity.class);
                        intent.putExtra("projectVo", projectVo);
                        activity.startActivity(intent);
                    }
                });

            }
        };
        setHasOptionsMenu(true);

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        mList.setAdapter(mLRecyclerViewAdapter);
    }

    private void initOath() {
        taskListMainReceiver = new TaskListMainReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.MOVE_TASK_LIST_UPDATE_ACTION);
        this.getActivity().registerReceiver(taskListMainReceiver, filter);//更新任务广播

        mainTaskListDateHandle=new MainTaskListDateHandle(getActivity(), projectVoList);

        myHandler=new MyHandler(this.getActivity());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.subAllData:   //重提
                String title = "提醒";
                String msg = "是否重提所有数据";
                builder = new CustomDialog.Builder(activity);
                customDialog = builder.cancelTouchout(false)
                        .view(R.layout.move_mining_over_dlaio)
                        .heightpx(ActivityUtil.getWindowsHetght(activity))
                        .widthpx(ActivityUtil.getWindowsWidth(activity))
                        .style(R.style.dialog)
                        .setTitle(title)
                        .setMsg(msg)
                        .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(activity,"重提",Toast.LENGTH_SHORT).show();
                                try {
                                    Dao<SavePointVo,Long> savePointVoLongDao=AppContext
                                            .getInstance().getSavePointVoDao();
                                    List<SavePointVo> savePointVoList=savePointVoLongDao.queryForEq(
                                            OkHttpParam.USER_ID,
                                            AppContext.getInstance().getCurUser().getUserId());
                                    if (savePointVoList.size()>0){
                                        SubAllDataAsync subAllDataAsync=new SubAllDataAsync(activity,savePointVoList,myHandler);
                                        subAllDataAsync.execute();
                                        customDialog.dismiss();
                                    }
                                } catch (SQLException e) {
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
                break;
        }
    }

    private static class MyHandler extends Handler{
        private final WeakReference<Activity> mActivity;
        public MyHandler(Activity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle;
            DetelePoiVo detelePoiVo;
            switch (msg.what){
                case 1:
                    bundle=msg.getData();
                    detelePoiVo= (DetelePoiVo) bundle.getSerializable(OkHttpParam.DETELE_POI_VO);
                    if (detelePoiVo.getCode()==0){
                        ToastUtils.show("上传数据成功");
                    }
                    break;
                case 2:
                    bundle=msg.getData();
                    detelePoiVo= (DetelePoiVo) bundle.getSerializable(OkHttpParam.DETELE_POI_VO);
                    if (detelePoiVo.getCode()!=0){
                        ToastUtils.show(detelePoiVo.getMsg());
                    }
                    break;
                case 3:
                    ToastUtils.show(AppContext.getInstance().getString(R.string.error_message2));
                    break;
                case 4:
                    bundle=msg.getData();
                    int pos=bundle.getInt("pos");
                    if (progressDialog==null){
                        progressDialog = new ProgressDialog(context);
                    }
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("一共"+pos+"条数据待提交，请稍后...");
                    progressDialog.show();
                    break;
                case 5:
                    bundle=msg.getData();
                    int i=bundle.getInt("i");
                    int pos2=bundle.getInt("pos");
                    progressDialog.setMessage("正在提交"+i+"/"+pos2+"条数据，请稍后...");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
//                    progressDialog.setProgress(i/pos2);
                    progressDialog.show();
                    break;
                case 6:
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    bundle=msg.getData();
                    int po=bundle.getInt("pos");
                    if (po==1){
                        AlertDialog.Builder bd = new AlertDialog.Builder(context);
                        AlertDialog ad = bd.create();
                        ad.setTitle("失败");
                        ad.setMessage("数据提交失败");
                        ad.show();
                    }else if (po==0){
                        AlertDialog.Builder bd = new AlertDialog.Builder(context);
                        AlertDialog ad = bd.create();
                        ad.setTitle("成功");
                        ad.setMessage("所有数据提交成功");
                        ad.show();
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        // really bad click detection just for demonstration purposes
        // it will not allow the list to scroll if the swipe motion starts
        // on top of a header
        View v = rv.findChildViewUnder(e.getX(), e.getY());
        return v == null;
//        return rv.findChildViewUnder(e.getX(), e.getY()) != null;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        // only use the "UP" motion event, discard all others
        if (e.getAction() != MotionEvent.ACTION_UP) {
            return;
        }

        // find the header that was clicked
//        View view = decor.findHeaderViewUnder(e.getX(), e.getY());
//
//        if (view instanceof TextView) {
//            Toast.makeText(this.getActivity(), ((TextView) view).getText() + " clicked", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // do nothing
    }

    public void updateList(){
        projectVoList.clear();
//        getObservable()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(getObserver());

        projectVoList=mainTaskListDateHandle.movedateList(taskType);
//        for (int i = 0; i < mainTaskListDateBaseHandle.size(); ++i) {
//            mainTaskListDateBaseHandle.get(i).movedateList(Constant.TASK_STUAS_N);
//        }

        adapter.setmDatas(projectVoList);

    }

    private Observable<String> getObservable(){
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("");
                projectVoList =mainTaskListDateHandle.movedateList(taskType);
                e.onComplete();
            }
        });
    }

    private Observer<String> getObserver(){
        final ProgressDialog progressDialog = new ProgressDialog(this.getActivity());
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                progressDialog.setMessage(getString(R.string.load_msg));
            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(getString(R.string.load_error_mag));
            }

            @Override
            public void onComplete() {
                ToastUtils.show(getString(R.string.load_msg2));
                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case OkHttpParam.MOVE_UPLOAD:
                if (data != null) {
                    try {
                        ProjectVo projectVo = project;
                        Uri fileUri = data.getData();
                        String filePath = FileUtils.getPath(AppContext.getInstance(), fileUri);
                        File file = new File(filePath);
                        if (file.exists() && projectVo != null) {
                            OkHttpRequest.UploadPoint(projectVo.getProjectId(), file, this.getActivity(),myHandler);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


//                    Observable.create(new ObservableOnSubscribe<Integer>() {
//                        @Override
//                        public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//                            ProjectVo projectVo = project;
//                            Uri fileUri = data.getData();
//                            String filePath = FileUtils.getPath(AppContext.getInstance(), fileUri);
//                            if (filePath!=null){
//                                File file = new File(filePath);
//                                if (file.exists() && projectVo != null) {
//                                    if (FileUtils.isTxt(file)){
//                                        //读取文件做更新操作
//                                        List<String> lineStrList=FileUtils.readText(file);
//
//
//                                    }else {
//                                        e.onNext(-1);
//                                    }
//                                }
//                            }else {
//                                e.onNext(-1);
//                            }
//                        }
//                    })
//                            .compose(NetUtil.<Integer>io_main())
//                            .subscribe(new CommonObserver<Integer>() {
//                                @Override
//                                protected void onResponse(Integer integer) throws Exception {
//                                    switch (integer){
//                                        case -1:
//                                            ToastUtils.show(getString(R.string.input_msg));
//                                            break;
//                                    }
//                                }
//
//                                @Override
//                                protected void onError(Throwable e, boolean isNetWorkError) throws Exception {
//
//                                }
//                            });

                }
                break;
            default:

                break;
        }
    }

    private class TaskListMainReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (com.movementinsome.app.pub.Constant.MOVE_TASK_LIST_UPDATE_ACTION
                    .equals(action)) {
                updateList();
            }
        }
    }

    public List<SavePointVo> getSavePointVoList(ProjectVo projectVo) {
        Dao<SavePointVo, Long> savePointDao = null;
        try {
            savePointDao = AppContext
                    .getInstance().getAppDbHelper()
                    .getDao(SavePointVo.class);
            List<SavePointVo> savePointVoList=savePointDao.queryForEq(OkHttpParam.PROJECT_ID, projectVo.getProjectId());
            return savePointVoList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
