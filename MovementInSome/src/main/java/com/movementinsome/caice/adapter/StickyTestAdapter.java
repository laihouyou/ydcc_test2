package com.movementinsome.caice.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.caice.activity.FacilityListActivity;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.okhttp.OkHttpRequest;
import com.movementinsome.caice.okhttp.ProjectRequest;
import com.movementinsome.caice.project.ProjectOperation;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.view.CustomDialog;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.kernel.util.ActivityUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StickyTestAdapter extends RecyclerView.Adapter<StickyTestAdapter.ViewHolder>
         {

    private LayoutInflater mInflater;
    private Activity mContext;
    private List<ProjectVo> projectVos;

    boolean point=true;
    boolean proje=true;

    private CustomDialog customDialog;
    private CustomDialog.Builder builder;

    public StickyTestAdapter(Activity mContext, List<ProjectVo> projectVos) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.projectVos = projectVos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view = mInflater.inflate(R.layout.move_tasklist_item, viewGroup, false);

        return new ViewHolder(view);
         }

    @Override
    public int getItemCount() {
        return projectVos.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final ProjectVo projectVo = projectVos.get(position);

        switch (projectVo.getProjectType()){
            case "电力":
                viewHolder.move_logo.setBackgroundResource(R.drawable.power);
                break;

            case "电信":
                viewHolder.move_logo.setBackgroundResource(R.drawable.telegraphy);
                break;

            case "给水":
                viewHolder.move_logo.setBackgroundResource(R.drawable.water_delivery);
                break;

            case "排水":
                viewHolder.move_logo.setBackgroundResource(R.drawable.drain_away_water);
                break;

            case "燃气":
                viewHolder.move_logo.setBackgroundResource(R.drawable.combustion_gas);
                break;

            case "综合":
                viewHolder.move_logo.setBackgroundResource(R.drawable.synthesis);
                break;

            default:
                viewHolder.move_logo.setBackgroundResource(R.drawable.water_delivery);
                break;
        }

        if (projectVo.getProjectStatus().equals("2")){
            viewHolder.move_surveying.setVisibility(View.GONE);
            viewHolder.move_finsh.setVisibility(View.GONE);
        }
        viewHolder.move_name.setText(projectVo.getProjectName());
        viewHolder.move_type.setText("工程类型:"+ projectVo.getProjectType());

        if (projectVo.getProjectCreateDateStr()!=null&&!projectVo.getProjectCreateDateStr().equals("")){
            viewHolder.move_time.setText(projectVo.getProjectCreateDateStr().split(" ")[0]);      //工程创建时间
        }

        if(projectVo.getProjectStatus().equals("1")){
            viewHolder.move_stuas.setVisibility(View.VISIBLE);
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
//            if (projectVo.getIsCompile().equals("1")){     //已编辑
//                proje=false;
//            }else {     //已提交， 未编辑
//            }
            proje=true;
        }else {
            proje=false;
        }

        //根据工程与设施的提交状态显示提交按钮
        if (proje&&point){
            viewHolder.newest.setText("已同步");
            viewHolder.newest.setVisibility(View.VISIBLE);
            viewHolder.newest.setTextColor(AppContext.getInstance().getResources().getColor(R.color.lightgreen));
            viewHolder.move_sub.setVisibility(View.GONE);
        }else {
            viewHolder.newest.setText("未同步");
            viewHolder.newest.setVisibility(View.VISIBLE);
            viewHolder.newest.setTextColor(AppContext.getInstance().getResources().getColor(R.color.red));
            viewHolder.move_sub.setVisibility(View.VISIBLE);
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
            viewHolder.move_value_num.setText("设施点数量:"+savePointVos.size());
        }catch (Exception e){
            e.printStackTrace();
        }

        /**
         * 点击事件
         */
        viewHolder.move_check.setOnClickListener(new View.OnClickListener() {
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

                Toast.makeText(AppContext.getInstance(),"查看",Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.move_surveying.setOnClickListener(new View.OnClickListener() {
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
        viewHolder.move_delect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new CustomDialog.Builder(mContext);
                customDialog=builder.cancelTouchout(false)
                        .view(R.layout.move_mining_over_dlaio)
                        .heightpx(ActivityUtil.getWindowsHetght(mContext))
                        .widthpx(ActivityUtil.getWindowsWidth(mContext))
                        .style(R.style.dialog)
                        .setTitle("删除提醒")
                        .setMsg("确认删除？")
                        .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    if (projectVo.getProjectStatus().equals("1")){
                                        Toast.makeText(mContext,"该工程正在采测中,请暂停采测后在操作",Toast.LENGTH_SHORT).show();
                                        customDialog.dismiss();
                                        return;
                                    }
                                    Dao<ProjectVo,Long> miningSurveyVOLongDao=AppContext.getInstance()
                                            .getAppDbHelper().getDao(ProjectVo.class);
                                    Dao<SavePointVo, Long> savePointDao = AppContext
                                            .getInstance().getAppDbHelper()
                                            .getDao(SavePointVo.class);
                                    Map<String ,Object> map=new HashMap();
                                    map.put(OkHttpParam.PROJECT_ID, projectVo.getProjectId());
                                    map.put(OkHttpParam.USER_ID,AppContext.getInstance().getCurUser().getUserId());
                                    List<SavePointVo> savePointVoList=savePointDao.queryForFieldValues(map);
                                    if (savePointVoList.size()==0){

                                        //先联网删除服务器工程
                                        String projectId= projectVo.getProjectId();
//                                Map<String,Object> map=new HashMap<>();
//                                map.put(OkHttpParam.PROJECT_ID,projectId);
                                        if (projectVo.getProjectSubmitStatus()!=null){
                                            if (projectVo.getProjectSubmitStatus().equals("0")){      //未提交
                                                ProjectOperation.DeteleProject(map,mContext);
                                            }else if (projectVo.getProjectSubmitStatus().equals("1")){    //已提交
                                                ProjectRequest.ProjectDelete(map, mContext,customDialog);
                                            }
                                        }else {
                                            ProjectOperation.DeteleProject(map,mContext);
                                        }
                                        customDialog.dismiss();
                                    }else {
                                        Toast.makeText(mContext,"请删除所有设施点与管线后再进行操作",Toast.LENGTH_SHORT).show();
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
        //完成
        viewHolder.move_finsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new CustomDialog.Builder(mContext);
                customDialog=builder.cancelTouchout(false)
                        .view(R.layout.move_mining_over_dlaio)
                        .heightpx(ActivityUtil.getWindowsHetght(mContext))
                        .widthpx(ActivityUtil.getWindowsWidth(mContext))
                        .style(R.style.dialog)
                        .setTitle("完成提醒")
                        .setMsg("确认完成？")
                        .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    if (projectVo !=null){
                                        projectVo.setProjectStatus("2");    //已完成
//                                        ProjectRequest.ProjectUpdate(projectVo, mContext);

                                        customDialog.dismiss();
                                        AppContext.getInstance().setProjectVo(projectVo);
                                        Intent intent = new Intent();
                                        intent.setPackage(mContext.getPackageName());
                                        intent.putExtra("parameter", "complete");
                                        intent.setAction(Constant.PASSDATE);
                                        mContext.sendBroadcast(intent);

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
        //同步
        viewHolder.move_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new CustomDialog.Builder(mContext);
                customDialog=builder.cancelTouchout(false)
                        .view(R.layout.move_mining_over_dlaio)
                        .heightpx(ActivityUtil.getWindowsHetght(mContext))
                        .widthpx(ActivityUtil.getWindowsWidth(mContext))
                        .style(R.style.dialog)
                        .setTitle("提交提醒")
                        .setMsg("确认提交？")
                        .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //获取当前工程提交状态
                                if (projectVo.getProjectSubmitStatus()!=null&& projectVo.getProjectSubmitStatus().equals("0")){    //工程未提交
                                    ProjectRequest.ProjectCreate(projectVo,mContext,true);
                                } else if (projectVo.getProjectSubmitStatus() != null && projectVo.getProjectSubmitStatus().equals("1")) {      //已提交
//                                    ProjectRequest.ProjectUpdate(projectVo, mContext);
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

                                if (savePointVos_no_sub.size()>0) {
                                    OkHttpRequest.SubmitPointList(savePointVos_no_sub, mContext);
                                }
                                if (savePointVos_no_com.size()>0){
                                    OkHttpRequest.IsUpdatePio(savePointVos_no_com,mContext, projectVo);
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
        //跳转到设施列表
        viewHolder.facility_Re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, FacilityListActivity.class);
                intent.putExtra("projectVo", projectVo);
                mContext.startActivity(intent);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView move_name;
        public TextView move_type;
        public TextView move_value_num;
        public TextView move_time;
        public TextView newest;
        public LinearLayout move_surveying;
        public LinearLayout move_finsh;
        public LinearLayout move_sub;
        public LinearLayout move_check;
        public LinearLayout move_delect;
        public ImageView move_logo;
        public Button move_stuas;
        private RelativeLayout facility_Re;

        public ViewHolder(View itemView) {
            super(itemView);

            move_name = (TextView) itemView.findViewById(R.id.move_name);
            move_type = (TextView) itemView.findViewById(R.id.move_type);
            move_value_num = (TextView) itemView.findViewById(R.id.move_value_num);
            move_time = (TextView) itemView.findViewById(R.id.move_time);
            newest = (TextView) itemView.findViewById(R.id.newest);
            move_surveying = (LinearLayout) itemView.findViewById(R.id.move_surveying);
            move_finsh = (LinearLayout) itemView.findViewById(R.id.move_finsh);
            move_sub = (LinearLayout) itemView.findViewById(R.id.move_sub);
            move_check = (LinearLayout) itemView.findViewById(R.id.move_check);
            move_delect = (LinearLayout) itemView.findViewById(R.id.move_delect);
            move_logo = (ImageView) itemView.findViewById(R.id.move_logo);
            move_stuas = (Button) itemView.findViewById(R.id.move_stuas);
            facility_Re = (RelativeLayout) itemView.findViewById(R.id.facility_Re);
        }
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView header;

        public HeaderHolder(View itemView) {
            super(itemView);

            header = (TextView) itemView;
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

    public void setData(List<ProjectVo> projectVoList){
            this.projectVos = projectVoList;
            notifyDataSetChanged();
    }
}
