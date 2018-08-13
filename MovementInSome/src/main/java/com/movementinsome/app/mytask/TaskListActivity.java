package com.movementinsome.app.mytask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.mytask.handle.MainTaskListDateHandle;
import com.movementinsome.app.mytask.handle.MoveTaskListCentreBaseHandle;
import com.movementinsome.app.mytask.handle.MoveTaskListCentreHandle;
import com.movementinsome.app.mytask.handle.MoveTaskListDateBaseHandle;
import com.movementinsome.caice.util.DateUtil;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.commonAdapter.adapterView.CommonListViewAdapter;
import com.movementinsome.commonAdapter.adapterView.ViewHolder;
import com.movementinsome.kernel.activity.ContainActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.movementinsome.R.id.move_finsh;
import static com.movementinsome.R.id.move_surveying;


/**
 * Created by LJQ on 2017/5/19.
 */

public class TaskListActivity extends ContainActivity implements View.OnClickListener{

    private TextView taskTypeWWC;
    private TextView taskTypeYWC;
    private String type;
    private ListView moveTaskListView;
    // 数据处理类
    private List<MoveTaskListDateBaseHandle> mainTaskListDateBaseHandle;
    private TaskListMainReceiver taskListMainReceiver;
    private CommonListViewAdapter moveTaskListAdapter;

    private List<ProjectVo> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tasklist_main_activity);
        inti();
    }

    private void inti() {
        taskListMainReceiver = new TaskListMainReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.MOVE_TASK_LIST_UPDATE_ACTION);
        registerReceiver(taskListMainReceiver, filter);//更新任务广播

        data = new ArrayList<>();

        mainTaskListDateBaseHandle = new ArrayList<MoveTaskListDateBaseHandle>();
        mainTaskListDateBaseHandle.add(new MainTaskListDateHandle(this,data));
        moveTaskListView = (ListView)findViewById(R.id.moveTaskListView);
        taskTypeWWC = (TextView)findViewById(R.id.taskTypeWWC);
        taskTypeYWC = (TextView)findViewById(R.id.taskTypeYWC);
        taskTypeWWC.setOnClickListener(this);
        taskTypeYWC.setOnClickListener(this);
//        moveTaskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (data!=null&&data.size()>0){
//                    Intent intent=new Intent(TaskListActivity.this, FacilityListActivity.class);
//                    ProjectVo miningSurveyVO = (ProjectVo) data
//                            .get(position).get("ProjectVo");
//                    intent.putExtra("miningSurveyVO",miningSurveyVO);
//                    startActivity(intent);
//                }
//            }
//        });

        type = Constant.TASK_STUAS_N;
        updateList(type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.taskTypeWWC:
                taskTypeYWC.setBackgroundResource(0);
                taskTypeWWC.setBackgroundResource(R.drawable.tv_green_bg);
                taskTypeYWC.setTextColor(Color.parseColor("#737373"));
                taskTypeWWC.setTextColor(Color.parseColor("#3f63ce"));
                type = Constant.TASK_STUAS_N;
                updateList(type);
                break;

            case R.id.taskTypeYWC:
                taskTypeWWC.setBackgroundResource(0);
                taskTypeYWC.setBackgroundResource(R.drawable.tv_green_bg);
                taskTypeWWC.setTextColor(Color.parseColor("#737373"));
                taskTypeYWC.setTextColor(Color.parseColor("#3f63ce"));
                type = Constant.TASK_STUAS_Y;
                updateList(type);
                break;

            default:
                break;
        }
    }

    private void updateList(String type) {
        if(Constant.TASK_STUAS_N.equals(type)
                ||Constant.TASK_STUAS_Y.equals(type)){
            data.clear();
            for (int i = 0; i < mainTaskListDateBaseHandle.size(); ++i) {
                mainTaskListDateBaseHandle.get(i).movedateList(type);
            }

            Collections.sort(data, new Comparator<ProjectVo>() {
                @Override
                public int compare(ProjectVo lhs, ProjectVo rhs) {
                    return (int)DateUtil.stringtoDate2(lhs.getProjectCreateDateStr()).getTime()
                            -(int)DateUtil.stringtoDate2(rhs.getProjectCreateDateStr()).getTime();
                }
            });

            moveTaskListAdapter = new CommonListViewAdapter<ProjectVo>(this,R.layout.move_tasklist_item,data) {
                @Override
                protected void convert(ViewHolder viewHolder, ProjectVo item, int position) {

                    final List<MoveTaskListCentreBaseHandle> moveTaskListCentreBaseHandle = new ArrayList<MoveTaskListCentreBaseHandle>();
                    moveTaskListCentreBaseHandle.add(new MoveTaskListCentreHandle(TaskListActivity.this, item, this));

                    Map<String, View> vs = new HashMap<String, View>();
                    vs.put("move_name", viewHolder.getView(R.id.move_name));
                    vs.put("move_type", viewHolder.getView(R.id.move_type));
                    vs.put("move_value_num", viewHolder.getView(R.id.move_value_num));
                    vs.put("move_time", viewHolder.getView(R.id.move_time));
                    vs.put("move_surveying", viewHolder.getView(move_surveying));
                    vs.put("move_finsh", viewHolder.getView(move_finsh));
                    vs.put("move_stuas", viewHolder.getView(R.id.move_stuas));
                    vs.put("newest", viewHolder.getView(R.id.newest));
                    vs.put("move_sub", viewHolder.getView(R.id.move_sub));  //提交
                    vs.put("move_logo", viewHolder.getView(R.id.move_logo));  //图标

                    for (int i = 0; i < moveTaskListCentreBaseHandle.size(); ++i) {
                        moveTaskListCentreBaseHandle.get(i).controlUI(vs);
                    }

                    viewHolder.setOnClickListener(R.id.move_check, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(int i=0;i<moveTaskListCentreBaseHandle.size();++i){
                                moveTaskListCentreBaseHandle.get(i).showMsgHandler();
                            }
                        }
                    });
                    viewHolder.setOnClickListener(move_surveying, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(int i=0;i<moveTaskListCentreBaseHandle.size();++i){
                                moveTaskListCentreBaseHandle.get(i).startWorkHandler();
                            }
                        }
                    });
                    viewHolder.setOnClickListener(R.id.move_delect, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(int i=0;i<moveTaskListCentreBaseHandle.size();++i){
                                try {
                                    moveTaskListCentreBaseHandle.get(i).delect();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    viewHolder.setOnClickListener(move_finsh, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(int i=0;i<moveTaskListCentreBaseHandle.size();++i){
                                moveTaskListCentreBaseHandle.get(i).finishHandler();
                            }
                        }
                    });
                    viewHolder.setOnClickListener(R.id.facility_Re, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(int i=0;i<moveTaskListCentreBaseHandle.size();++i){
                                moveTaskListCentreBaseHandle.get(i).facilityList();
                            }
                        }
                    });
                    viewHolder.setOnClickListener(R.id.move_sub, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for(int i=0;i<moveTaskListCentreBaseHandle.size();++i){
                                moveTaskListCentreBaseHandle.get(i).subHandler();
                            }
                        }
                    });
                }
            };
            moveTaskListView.setAdapter(moveTaskListAdapter);
        }
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (data!=null&&data.size()>0){
//            Intent intent=new Intent(TaskListActivity.this, FacilityListActivity.class);
//            ProjectVo miningSurveyVO = (ProjectVo) data
//                    .get(position).get("ProjectVo");
//            intent.putExtra("miningSurveyVO",miningSurveyVO);
//            startActivity(intent);
//        }
//    }

    private class TaskListMainReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
//            if (Constant.MOVE_TASK_LIST_UPDATE_ACTION
//                    .equals(action)) {
//                updateList(type);
//            }
        }

    }
}
