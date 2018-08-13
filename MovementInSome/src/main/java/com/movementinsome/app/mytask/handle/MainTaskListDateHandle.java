package com.movementinsome.app.mytask.handle;

import android.app.Activity;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.vo.ProjectVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LJQ on 2017/5/20.
 */

public class MainTaskListDateHandle implements MoveTaskListDateBaseHandle {
    private Activity context;
    private List<ProjectVo> projectVos;

    public MainTaskListDateHandle(Activity context,List<ProjectVo> projectVos) {
        this.context = context;
        this.projectVos = projectVos;
    }

    @Override
    public List<ProjectVo> movedateList(String type) {
        Map<String, Object> d = null;

        try {
            Dao<ProjectVo, Long> miningSurveyDao = AppContext
                    .getInstance().getAppDbHelper()
                    .getDao(ProjectVo.class);
            if (Constant.TASK_STUAS_N.equals(type)) {
                Map<String, Object> m = new HashMap<String, Object>();
                m.put(OkHttpParam.USER_ID, AppContext.getInstance().getCurUser().getUserId());
                m.put(OkHttpParam.PROJECT_STATUS, "0");
                List<ProjectVo> MiningSurveyList = miningSurveyDao
                        .queryForFieldValuesArgs(m);
                List<ProjectVo> projectVoList=miningSurveyDao.queryForAll();
                Log.i("tag",projectVoList.toString());
                if(MiningSurveyList!=null&&MiningSurveyList.size()>0) {
                    projectVos.addAll(MiningSurveyList);
                }
                Map<String, Object> m2 = new HashMap<String, Object>();
                m2.put(OkHttpParam.USER_ID, AppContext.getInstance().getCurUser().getUserId());
                m2.put(OkHttpParam.PROJECT_STATUS, OkHttpParam.PROJECT_STATUS_ONE);
                List<ProjectVo> MiningSurveyList2 = miningSurveyDao
                        .queryForFieldValuesArgs(m2);
                if(MiningSurveyList2!=null&&MiningSurveyList2.size()>0) {
                    projectVos.addAll(MiningSurveyList2);
                }

                //获取服务器数据


            }else if(Constant.TASK_STUAS_Y.equals(type)){
                Map<String, Object> m = new HashMap<String, Object>();
                m.put(OkHttpParam.USER_ID, AppContext.getInstance().getCurUser().getUserId());
                m.put(OkHttpParam.PROJECT_STATUS, OkHttpParam.PROJECT_STATUS_TWO);
                List<ProjectVo> MiningSurveyList = miningSurveyDao
                        .queryForFieldValuesArgs(m);
                if(MiningSurveyList!=null&&MiningSurveyList.size()>0) {
                    projectVos.addAll(MiningSurveyList);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return projectVos;
    }

}
