package com.movementinsome.caice.project;

import android.app.Activity;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.map.MapViewer;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.movementinsome.app.mytask.handle.MoveTaskListCentreHandle.moveProjcetUpdate;

/**
 * 工程操作类
 * Created by zzc on 2017/8/22.
 */

public  class ProjectOperation {
    /**
     * 创建工程VO
     * @param projectVo    工程VO
     * @param isPresent  是否已提交 0未提交 参数1已提交
     * @param isRecommit    是否是重新提交
     */
    public static void CreateMiningVo(ProjectVo projectVo, String isPresent, boolean isRecommit) {
        try {
            Dao<ProjectVo, Long> miningSurveyVOdao = AppContext.getInstance().getAppDbHelper().getDao(ProjectVo.class);

            projectVo.setProjectSubmitStatus(isPresent);       //是否已提交

            if (!isRecommit){       //不是重提

                int s = miningSurveyVOdao.create(projectVo);
                if (s == 1) {
                    EventBus.getDefault().post(projectVo);
                }
            }else {             //重提
                int s=miningSurveyVOdao.update(projectVo);
                if (s==1){
                    MapViewer.moveProjcetUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void DeteleProject(Map parameterMap, Activity context) throws SQLException {
        Dao<ProjectVo,Long> miningSurveyVOLongDao=AppContext.getInstance()
                .getAppDbHelper().getDao(ProjectVo.class);
        List<ProjectVo> projectVos =miningSurveyVOLongDao.
                queryForEq(OkHttpParam.PROJECT_ID,parameterMap.get(OkHttpParam.PROJECT_ID));
        if (projectVos !=null&& projectVos.size()==1){
            int s=miningSurveyVOLongDao.delete(projectVos.get(0));
            if (s==1){
                Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                moveProjcetUpdate();
                EventBus.getDefault().post(parameterMap.get(OkHttpParam.PROJECT_ID));
            }
        }
    }

    /**
     * 设置工程 提交状态
     * @param projectVo   工程数据集合
     * @param presentCode  状态码  0未提交 1已提交
     * @throws SQLException
     */
    public static void setProjectPresent(ProjectVo projectVo, String presentCode) throws SQLException {
        Dao<ProjectVo, Long> miningSurveyVOLongDao = AppContext.getInstance()
                .getAppDbHelper().getDao(ProjectVo.class);
        if (projectVo != null) {
            projectVo.setProjectSubmitStatus(presentCode);
            int s = miningSurveyVOLongDao.update(projectVo);
            if (s == 1) {

            }
        }
        MapViewer.moveProjcetUpdate();
    }

    /**
     * 设置工程 编辑状态
     * @param projectVo   工程数据集合
     * @throws SQLException
     */
    public static void ProjectUpdate(ProjectVo projectVo) throws SQLException {
        Dao<ProjectVo, Long> miningSurveyVOLongDao = AppContext.getInstance()
                .getAppDbHelper().getDao(ProjectVo.class);
        if (projectVo != null) {
            int s = miningSurveyVOLongDao.update(projectVo);
            if (s == 1) {

            }
        }
        MapViewer.moveProjcetUpdate();
    }

}
