package com.movementinsome.caice.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.movementinsome.app.mytask.handle.MoveTaskListDateBaseHandle;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.caice.activity.FacilityListActivity;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.okhttp.OkHttpRequest;
import com.movementinsome.caice.okhttp.ProjectRequest;
import com.movementinsome.caice.project.ProjectOperation;
import com.movementinsome.caice.recycleView.DividerDecoration;
import com.movementinsome.caice.util.DateUtil;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.view.CustomDialog;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.commonAdapter.recycleView.CommonRecycleViewAdapter;
import com.movementinsome.commonAdapter.recycleView.base.ViewHolder;
import com.movementinsome.kernel.util.ActivityUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/12.
 */

public class TaskListFragment2 extends Fragment implements RecyclerView.OnItemTouchListener{


    // 数据处理类
    private List<MoveTaskListDateBaseHandle> mainTaskListDateBaseHandle;
    private List<ProjectVo> projectVoList;
    private TaskListMainReceiver taskListMainReceiver;
    //    private StickyTestAdapter adapter;
    private CommonRecycleViewAdapter adapter;

    boolean point=true;
    boolean proje=true;

    private CustomDialog customDialog;
    private CustomDialog.Builder builder;

    private Activity activity;

    private LRecyclerView mList;
    protected LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private String taskType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)  {
//        adapter = new StickyTestAdapter(this.getActivity(),projectVoList);
        Bundle bundle=getArguments();
        taskType=bundle.getString(OkHttpParam.TASK_TYPE);
        final View view = inflater.inflate(R.layout.fragment_recycler, container, false);
        activity=this.getActivity();
        mList = (LRecyclerView) view.findViewById(R.id.list);
        mList.setPullRefreshEnabled(false);

        final DividerDecoration divider = new DividerDecoration.Builder(this.getActivity())
                .setHeight(R.dimen.dp_10)
                .setPadding(R.dimen.dp_4)
                .setColorResource(R.color.default_header_color2)
                .build();

        mList.setHasFixedSize(true);
        mList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mList.addItemDecoration(divider);

        taskListMainReceiver = new TaskListMainReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.MOVE_TASK_LIST_UPDATE_ACTION);
        this.getActivity().registerReceiver(taskListMainReceiver, filter);//更新任务广播

        projectVoList = new ArrayList<>();

        mainTaskListDateBaseHandle = new ArrayList<>();
        mainTaskListDateBaseHandle.add(new MainTaskListDateHandle(this.getActivity(), projectVoList));

        updateList();

        return view;
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
        for (int i = 0; i < mainTaskListDateBaseHandle.size(); ++i) {
            mainTaskListDateBaseHandle.get(i).movedateList(taskType);
        }

        Collections.sort(projectVoList, new Comparator<ProjectVo>() {
            @Override
            public int compare(ProjectVo lhs, ProjectVo rhs) {
                return (int) DateUtil.stringtoDate2(lhs.getProjectCreateDateStr()).getTime()
                        -(int)DateUtil.stringtoDate2(rhs.getProjectCreateDateStr()).getTime();
            }
        });

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
                    holder.setText(R.id.move_time, projectVo.getProjectCreateDateStr().split(" ")[0]);
                }

                if(projectVo.getProjectStatus().equals("1")){
                    holder.setVisible(R.id.move_stuas,View.VISIBLE);
                }


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
                                break;
                            }
                        }else {
                            //没有字段说明是旧版本数据
                            point=false;
                            break;
                        }
                    }
                }

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

//                holder.setVisible(R.id.move_surveying,View.GONE);
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

//                holder.setVisible(R.id.move_finsh,View.GONE);
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
                                                projectVo.setProjectStatus("2");    //已完成
//                                                ProjectRequest.ProjectUpdate(projectVo, activity);

                                                customDialog.dismiss();
                                                AppContext.getInstance().setProjectVo(projectVo);
                                                Intent intent = new Intent();
                                                intent.setPackage(activity.getPackageName());
                                                intent.putExtra("parameter", "complete");
                                                intent.setAction(Constant.PASSDATE);
                                                activity.sendBroadcast(intent);

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
//                                            ProjectRequest.ProjectUpdate(projectVo,activity);
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
