package com.movementinsome.caice.project;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.movementinsome.AppContext;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.util.CreateFiles;
import com.movementinsome.caice.util.DateUtil;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.database.vo.DynamicFormVO;
import com.movementinsome.map.MapViewer;

import org.greenrobot.eventbus.EventBus;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static com.movementinsome.map.MapViewer.MOVE_MINING_LINE;

/**设施操作类
 * Created by zzc on 2017/8/25.
 */

public class PoiOperation {

    public static void PoiCreate(SavePointVo savePointVo,Map map) throws SQLException {
        Dao<SavePointVo, Long> savePointVoLongDao =
                AppContext.getInstance().getAppDbHelper().getDao(SavePointVo.class);

        savePointVo.setFacSubmitStatus((String) map.get(OkHttpParam.IS_PRESENT));      //提交状态

        if (savePointVo.getDataType().equals(MapMeterMoveScope.POINT)){
            int s = savePointVoLongDao.create(savePointVo);
            if (s == 1) {
                EventBus.getDefault().post(savePointVo);

                //录入数据成功后设置工程自动编号
                Dao<ProjectVo, Long> miningSurveyVOLongDao = AppContext.getInstance().getAppDbHelper().getDao(ProjectVo.class);
                List<ProjectVo> projectVo = miningSurveyVOLongDao.queryForEq("projectId", savePointVo.getProjectId());
                if (projectVo != null && projectVo.size() > 0) {
                    projectVo.get(0).setAutoNumber(projectVo.get(0).getAutoNumber() + 1);     //每次成功创建数据自动编号加1
                    projectVo.get(0).setProjectUpdatedDateStr(DateUtil.getNow());
                }
                int y = miningSurveyVOLongDao.update(projectVo.get(0));
                Log.i("s", y + "");
            }
        }else if (savePointVo.getDataType().equals(MapMeterMoveScope.LINE)){

            int s = savePointVoLongDao.create(savePointVo);
            if (s == 1) {
//                if (isEnd){
                    //录入数据成功后设置工程自动编号
                    Dao<ProjectVo, Long> miningSurveyVOLongDao = AppContext.getInstance().getAppDbHelper().getDao(ProjectVo.class);
                    List<ProjectVo> projectVo = miningSurveyVOLongDao.queryForEq("projectId", savePointVo.getProjectId());
                    if (projectVo != null && projectVo.size() > 0) {
                        projectVo.get(0).setAutoNumberLine(projectVo.get(0).getAutoNumberLine() + 1);     //每次成功创建数据自动编号加1
                        projectVo.get(0).setProjectUpdatedDateStr(DateUtil.getNow());
                    }
                    int y = miningSurveyVOLongDao.update(projectVo.get(0));
                    Log.i("s", y + "");
//                }
            }
        }
    }

    public static void DeletePoi(List<SavePointVo> savePointVoList_DB) throws SQLException {

        Dao<ProjectVo, Long> miningSurveyVOLongDao = AppContext.getInstance().
                getAppDbHelper().getDao(ProjectVo.class);


        Dao<DynamicFormVO, Long> dynamicFormDao = AppContext.getInstance().
                getAppDbHelper().getDao(DynamicFormVO.class);

        if (savePointVoList_DB != null) {
            for (int i = 0; i < savePointVoList_DB.size(); i++) {
                SavePointVo savePointVo=savePointVoList_DB.get(i);
                String guid = savePointVo.getGuid();
                String projectId = savePointVo.getProjectId();
                ProjectVo projectVo = new ProjectVo();
                List<ProjectVo> projectVoList = miningSurveyVOLongDao.
                        queryForEq(OkHttpParam.PROJECT_ID, projectId);
                if (projectVoList != null && projectVoList.size() > 0) {
                    projectVo = projectVoList.get(0);

                }
                //删除数据库
                int s = AppContext.getInstance().getSavePointVoDao().delete(savePointVo);
                if (s == 1) {

                }

                List<DynamicFormVO> dynamicFormList ;

                //删除原来数据库内容
                QueryBuilder<DynamicFormVO, Long> queryBuilder = dynamicFormDao.queryBuilder();
                Where<DynamicFormVO, Long> where = queryBuilder.where();
                where.isNotNull("form");
                where.and();
                where.eq(OkHttpParam.GUID, guid);
                dynamicFormList = dynamicFormDao.query(queryBuilder.prepare());
                if (dynamicFormList != null && dynamicFormList.size() == 1) {
                    int w = dynamicFormDao.delete(dynamicFormList.get(0));
                    if (w == 1) {
                        if (projectVo != null) {
                            projectVo.setProjectUpdatedDateStr(DateUtil.getNow());
                        }
                        int y = miningSurveyVOLongDao.update(projectVo);
                        Log.i("s", y + "");
                    }
                }

                //删除本地照片数据
                if (CreateFiles.deleteDirectory(MOVE_MINING_LINE + savePointVo.getFacPipBaseVo().getUserName() + "/"
                        + savePointVo.getFacPipBaseVo().getProjectName() + "/"
                        + savePointVo.getPipName())) {
                }

                if (i == savePointVoList_DB.size() - 1) {
                    MapViewer.moveProjcetUpdate();

                }
            }
        }
    }

    /**
     * 设置设施数据 提交状态
     * @param savePointVoList   设施数据集合
     * @param presentCode  状态码  0未提交 1已提交
     * @throws SQLException
     */
    public static void setPointPresent(List<SavePointVo> savePointVoList,String presentCode) throws SQLException {
        Dao<SavePointVo, Long> savePointVoLongDao = AppContext.getInstance()
                .getAppDbHelper().getDao(SavePointVo.class);
        if (savePointVoList.size() > 0) {
            for (int i = 0; i < savePointVoList.size(); i++) {
                savePointVoList.get(i).setFacSubmitStatus(presentCode);
                int s = savePointVoLongDao.update(savePointVoList.get(i));
                if (s == 1) {

                }
            }
            MapViewer.moveProjcetUpdate();
        }
    }

    /**
     * 设置设施数据 编辑状态
     * @param savePointVoList   设施数据集合
     * @throws SQLException
     */
    public static void setPointCompile(List<SavePointVo> savePointVoList) throws SQLException {
        Dao<SavePointVo, Long> savePointVoLongDao = AppContext.getInstance()
                .getAppDbHelper().getDao(SavePointVo.class);
        if (savePointVoList.size() > 0) {
            for (int i = 0; i < savePointVoList.size(); i++) {
                int s = savePointVoLongDao.update(savePointVoList.get(i));
                if (s == 1) {

                }
            }
            MapViewer.moveProjcetUpdate();
        }
    }

}
