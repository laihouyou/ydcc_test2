package com.movementinsome.caice.okhttp;

import com.movementinsome.AppContext;
import com.movementinsome.R;

/**请求参数常量类
 * Created by zzc on 2017/3/21.
 */

public class OkHttpParam {

    public static final String AK = "ak";
    public static final String GEOTABLE_ID = "geotable_id";
    public static final String COORD_TYPE = "coord_type";
    public static final String COORD_TYPE_VALUE = "3";
    public static final String TITLE = "title";         //表示marker是坐标点显示的
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE_WG84 = "latitudeWg84";
    public static final String LATITUDEWG84_DU = "latitudeWg84_du";
    public static final String LATITUDEWG84_FEN = "latitudeWg84_fen";
    public static final String LATITUDEWG84_MIAO = "latitudeWg84_miao";
    public static final String LONGITUDE_WG84 = "longitudeWg84";
    public static final String LONGITUDEWG84_DU = "longitudeWg84_du";
    public static final String LONGITUDEWG84_FEN = "longitudeWg84_fen";
    public static final String LONGITUDEWG84_MIAO = "longitudeWg84_miao";
    public static final String FAC_NUM_LINE = "fac_num_line";
    public static final String LOCATION = "location";
    public static final String BOUNDS = "bounds";
    public static final String PAGE_INDEX = "page_index";
    public static final String PAGE_SIZE = "page_size";
    public static final String TAGS = "tags";

    public static final String DATA_TYPE = "dataType";//数据类型
    public static final String GATHER_TYPE = "gatherType";//采集类型
    public static final String DRAW_NUM= "drawNum";//绘制顺序
    public static final String FAC_NUM= "facName";//表号（自定义字段）
    public static final String FAC_NAME= "facName";//表号
    public static final String FAC_NAME_LIST= "facNameList";//管线上的设施表号集合
    public static final String FIRST_FAC_NAME= "firstFacNum";//本点号
    public static final String END_FAC_NAME= "endFacNum";//下点号
    public static final String PIP_NAME= "pipName";//管线编号
    public static final String PIP_TYPE= "pipType";//管线类型
    public static final String PIP_MATERIAL= "pipMaterial";//管线材质
    public static final String TUBULAR_PRODUCT= "tubularProduct";//管线材质
    public static final String LAYING_TYPE= "layingType";//敷设类型


    public static final String SAVE_POINTVO_ID= "savePointVoId";//手机本地数据库ID
    public static final String UPLOAD_NAME= "uploadName";//上传人
    public static final String UPLOAD_TIME= "uploadTime";//上传时间
    public static final String USER_NAME_ID= "userNameId";//上传人id
    public static final String USER_ID= "userId";//上传人id
//    public static final String USED_ID= "userId";//上传人id
    public static final String INDEX_KEY= "{index key}";//查询指定条件数据
    public static final String FAC_TYPE= "facType";//设施类型
    public static final String TYPE= "type";//设施类型
    public static final String GROUP_NAME= "groupName";//所属公司（班组）
    public static final String CONTEXT_JSON= "context";//所填的字段
    public static final String ID= "id";
    public static final String FAC_ID= "facId";
    public static final String IDS= "ids";
    public static final String IS_TOTAL_DEL= "is_total_del";  //需要批量删除时需要设置为1

    public static final String usUsercode= "usUsercode";  //注册账号
    public static final String usPassword= "usPassword";  //新密码
    public static final String usEmail= "usEmail";  //注册邮箱
    public static final String captcha= "captcha";  //注册邮箱验证码

    public static final String BITMAP_PATH= android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath()+"/MoveEQCodeBitmap/";

    //工程字段
    public static final String PROJECT= "project";//工程
    public static final String PROJECT_ID= "projectId";//工程ID
    public static final String SHARED_PROJECT_ID= "sharedProjectId";//被共享工程ID
    public static final String PROJECT_NAME= "projectName";//工程名字
    public static final String PROJECT_TYPE= "projectType";//工程类型
    public static final String PROJECT_STATUS= "projectStatus";//工程状态
    public static final String PROJECT_SUBMIT_STATUS= "projectSubmitStatus";//工程提交状态
    public static final String PROJECT_STATUS_ZERO= "0";//工程状态  0 未完成
    public static final String PROJECT_STATUS_ONE= "1";//工程状态  1 进行中
    public static final String PROJECT_STATUS_TWO= "2";//工程状态  2 已完成
    public static final String PROJECT_SUBMIT_STATUS_ZERO= "0";//工程提交状态  0 未提交
    public static final String PROJECT_SUBMIT_STATUS_ONE= "1";//工程提交状态  1 已提交
    public static final String IMPLEMENTORNAME= "implementorName";//设备名
    public static final String QICAN_STR= "qicanStr";
    public static final String PROJECT_SDATESTR= "projectSDateStr";
    public static final String PROJECT_EDATESTR= "projectEDateStr";
    public static final String PROJECT_EDATEUPD= "projectEDateUpd";
    public static final String TASK_NUM= "taskNum";
    public static final String IS_PRESENT= "isPresent";  //是否已提交
    public static final String IS_COMPILE= "isCompile"; //是否有编辑
    public static final String IS_SUBMIT= "isSubmit";
    public static final String SHARE_CODE= "shareCode";//共享码
    public static final String PROJECT_SHARE_CODE= "projectShareCode";//工程共享码
    public static final String IS_PROJECT_SHARE= "isProjectShare";//是否共享
    public static final String AUTO_NUMBER= "autoNumber";
    public static final String AUTO_NUMBER_LINE= "autoNumberLine";
//    public static final String USED_NAME= "usedName";
    public static final String PROJCET_LINE_LENGHTS= "projcetLineLenghts";
    public static final String POINT_VOS= "pointVos";
    public static final String IS_END= "isEnd";
    public static final String SAVEPOINTVO= "savePointVo";
    public static final String POINT_LIST= "pointList";
    public static final String TASK_TYPE= "task_type";

    public static final String ISLEAK= "isLeak";
    public static final String CALIBER_LEAK= "caliber_leak";
    public static final String BURIALDEPTH_LEAK= "burialDepth_leak";
    public static final String REMARKS_LEAK= "remarks_leak";
    public static final String CAMERA_LEAK= "camera_leak";

    public static final String PATROL= "patrol";    //探漏点
    public static final String LEAK= "leak";        //确定点

    public static final String PATROL_FAC_NAME= "patrolFacName";        //探漏点ID

    public static final String INPUT_TYPE= "input_type";        //导入类型
    public static final String STATISTICS_PARENT_VOS= "statisticsParentVos";        //工程统计字段
    public static final String STATISTICS_CHILE_VOS= "statisticsChileVos";        //工程统计字段
    public static final String UPDATE_SOURCE= "updateSource";        //更新来源   mobile  or  pc
    public static final String FORM_NAME= "formName";        //更新来源   mobile  or  pc
    public static final String MOBILE= "mobile";        //更新来源   mobile  手机
    public static final String PC= "pc";        //更新来源   pc  电脑


    //百度语音App id
    public static final String TTS_APP_ID= "9852106";

    //云知声 App  id
    public static final String appKey= "cqxlfpkinqtqknm5rlp3s36eun65k43eao22saqq";
    public static final String secret= "2fe1091be4061c23e73f74ba5ff7496b";


    public static final String YDCE= AppContext.getInstance().getString(R.string.YDCE);

    public static final String WGS84= "WGS84";
    public static final String GCJ02= "GCJ02";

    //城市列表七参字段
    public static final String MANUALLY_OUT= "手动填写七参数";
    public static final String MOBILE_PARAM= "mobileParam";

    public static final int MOVE_UPLOAD= 1;


    public static final String GUID= "guid";
    public static final String FORM_CONTEXT= "formContext";
    public static final String CONTEXT_STR= "contextStr";
    public static final String GROUND_ELEVATION= "groundElevation";
    public static final String BURIAL_DEPTH= "burialDepth";
    public static final String PIPELINE_LINGHT= "pipelineLinght";
    public static final String MAP_X= "mapx";
    public static final String MAP_Y= "mapy";
    public static final String HAPPEN_ADDR= "happenAddr";
    public static final String ADMINISTRATIVE_REGION= "administrativeRegion";
    public static final String CALIBER= "caliber";
    public static final String CAMERA= "camera";
    public static final String VIDEO_CAMERA= "videoCamera";
    public static final String IS_SUCCESSION= "isSuccession";
    public static final String LOCATION_JSON= "locationJson";
    public static final String FAC_PIP_BASE_VO= "FacPipBaseVo";
    public static final String BD09= "bd09";
    public static final String DRAW_TYPE= "drawType";
    public static final String LABEL_VALUE_MAP= "labelValueMap";
    public static final String LABEL_VALUE_MAP_FORMCONTEX= "labelValueMap_formContex";


    public static final String SHOW_ALERT_DIALOG1= "showAlertDialog1";
    public static final String SHOW_ALERT_DIALOG2= "showAlertDialog2";
    public static final String SHOW_ALERT_DIALOG3= "showAlertDialog3";
    public static final String DETELE_POI_VO= "DetelePoiVo";

    public static final String DETELE_LINE= "DeteleLine";
    public static final String SHOW_MARKER= "showMarker";

    //峰哥正式表 （水表采集）
//    public static final String AK_VALUE = "Lvd9dXUFUWGu9tqi4o0ed0BrO5hCdh6f";
//    public static final String GEOIAVLE_VALUE = "163688";
    //测试用表
    public static final String AK_VALUE = "BQ2ZUHyEPZG3ptKfwZX1yEqGCG9GgovI";
    public static final String GEOIAVLE_VALUE= "166492";

    //曾致城正式表
//    public static final String AK_VALUE = "TjcVhNO5fvEbKt5V1yX6egIZSqh8wQ1B";
//    public static final String GEOIAVLE_VALUE= "170035";
}
