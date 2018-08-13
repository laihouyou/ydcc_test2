package com.movementinsome.caice.vo;

import com.alibaba.fastjson.JSONObject;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.movementinsome.AppContext;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.kernel.initial.model.CoordParam;
import com.movementinsome.map.utils.MapMeterScope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = "ProjectVo")
public class ProjectVo implements Serializable {
    // 工程ID
    @DatabaseField(id = true)
    private String projectId;
    // 工程编号			JPAA_XXXXX100    其中AA为任务编号，手动填写的  XXXX为工程编号  100为顺序号
    @DatabaseField
        private String projectNum;
    // 工程类型
    @DatabaseField
    private String projectType;
    // 工程名称
    @DatabaseField
    private String projectName;
    // 工程创建时间
    @DatabaseField
    private String projectCreateDateStr;
    // 工程结束时间
    @DatabaseField
    private String projectEndDateStr;
    // 工程更新时间
    @DatabaseField
    private String projectUpdatedDateStr;
    // 共享码
    @DatabaseField
    private String projectShareCode;
    //工程状态
    //0为未完成
    //1为处理中
    //2为已完成
    //3为已暂停
    @DatabaseField
    private String projectStatus;

    //工程结算状态
    //0  未结算
    //1  已结算
    private String projectSettlementStatus;

    // 工程是否已提交
    //0为未提交x
    //1为已提交
    @DatabaseField
    private String projectSubmitStatus;

    // 自动编号		设施点
    @DatabaseField
    private int autoNumber;

    // 自动编号		管线
    @DatabaseField
    private int autoNumberLine;

    // 任务编号	    推送时对应的任务编号
    @DatabaseField
    private String taskNum;

    // 用户名Id
    @DatabaseField
    private String userId;

    //context    保存其他字段    字段继承自  MoveBaseVo
    @DatabaseField
    private String contextStr;


    public CoordParam getCoordTransform(){
        CoordParam coordParam= JSONObject.parseObject(getMoveBaseVo().getQicanStr(),CoordParam.class);
        return coordParam;
    }

    public CityVo getCityVo(){
        CityVo cityVo=JSONObject.parseObject(getMoveBaseVo().getQicanStr(),CityVo.class);
        return cityVo;
    }

    public MoveBaseVo getMoveBaseVo(){
        MoveBaseVo moveBaseVo= JSONObject.parseObject(contextStr,MoveBaseVo.class);
        return moveBaseVo;
    }

    public List<SavePointVo> getSavePointVoList()  {
        List<SavePointVo> savePointVos = new ArrayList<>();
        try {
            Dao<SavePointVo,Long> savePointVoDao= AppContext.getInstance().getSavePointVoDao();
            savePointVos=savePointVoDao.queryForEq(OkHttpParam.PROJECT_ID,getProjectId());
        }catch (Exception e){
            e.printStackTrace();
        }
        return savePointVos;
    }

    public int getPointSize()  {
        int pos=0;
        List<SavePointVo> savePointVos=getSavePointVoList();
        for (int i = 0; i < savePointVos.size(); i++) {
            if (savePointVos.get(i).getDataType().equals(MapMeterScope.POINT)){
                pos++;
            }
        }
        return pos;
    }

    public double getLineLenght() {
        double lineLenght=0.0;
        List<SavePointVo> savePointVos=getSavePointVoList();
        for (int i = 0; i < savePointVos.size(); i++) {
            if (savePointVos.get(i).getDataType().equals(MapMeterScope.LINE)){
                lineLenght+=savePointVos.get(i).getPipelineLinght();
            }
        }
        return lineLenght;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectNum() {
        return projectNum;
    }

    public void setProjectNum(String projectNum) {
        this.projectNum = projectNum;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectCreateDateStr() {
        return projectCreateDateStr;
    }

    public void setProjectCreateDateStr(String projectCreateDateStr) {
        this.projectCreateDateStr = projectCreateDateStr;
    }

    public String getProjectEndDateStr() {
        return projectEndDateStr;
    }

    public void setProjectEndDateStr(String projectEndDateStr) {
        this.projectEndDateStr = projectEndDateStr;
    }

    public String getProjectUpdatedDateStr() {
        return projectUpdatedDateStr;
    }

    public void setProjectUpdatedDateStr(String projectUpdatedDateStr) {
        this.projectUpdatedDateStr = projectUpdatedDateStr;
    }

    public String getProjectShareCode() {
        return projectShareCode;
    }

    public void setProjectShareCode(String projectShareCode) {
        this.projectShareCode = projectShareCode;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getProjectSubmitStatus() {
        return projectSubmitStatus;
    }

    public void setProjectSubmitStatus(String projectSubmitStatus) {
        this.projectSubmitStatus = projectSubmitStatus;
    }

    public int getAutoNumber() {
        return autoNumber;
    }

    public void setAutoNumber(int autoNumber) {
        this.autoNumber = autoNumber;
    }

    public int getAutoNumberLine() {
        return autoNumberLine;
    }

    public void setAutoNumberLine(int autoNumberLine) {
        this.autoNumberLine = autoNumberLine;
    }

    public String getContextStr() {
        return contextStr;
    }

    public void setContextStr(String contextStr) {
        this.contextStr = contextStr;
    }

    public String getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(String taskNum) {
        this.taskNum = taskNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProjectSettlementStatus() {
        return projectSettlementStatus == null ? "" : projectSettlementStatus;
    }

    public void setProjectSettlementStatus(String projectSettlementStatus) {
        this.projectSettlementStatus = projectSettlementStatus;
    }
}
