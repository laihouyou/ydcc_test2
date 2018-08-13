package com.movementinsome.app.pub;

import java.util.HashMap;
import java.util.Map;

public class Constant {

	/*****************************************/
	/*消息体传送标值                                                                                                       */
	/****************************************/
	public final static int MSG_IDEN_RETURN = 1100000;         //地图identify查义消息传输标志
	public final static int MSG_LOCATION_CHANGE = 1200000;           //坐标定位值变化消息传输标志
	public final static int MSG_LOCATION_FAIL = 1200001;             //坐标定位失败
	public final static int MSG_LOCATION_GPS = 1200002;              //已进入到GPS定位模式
	
	/*****************************************/
	/*共享存储KEY指定                                                                                                  */
	/*****************************************/
	public final static String SPF_NAME = "pPrefere";                  //pPrefere
	public final static String SPF_AUTO_REMBER = "autoRember" ;        //是否记录密码
	public final static String SPF_AUTO_LOGIN = "autoLogin";           //是否自动登录
	public final static String SPF_DEF_LOGIN_USER = "userName";        //缺省登录用户，记录用户编码
	public final static String SPF_DEF_LOGIN_PASSWORD = "password";    //登录用户密码
	public final static String SPF_PHONE_IMEI ="phoneIMEI";            //IMEI码
	public final static String SPF_PHONE_NUM = "phoneNum";             //手机号码
	public final static String SPF_VERSION = "versionName";            //系统版本号
	public final static String SPF_DEF_SOLUTION_ID = "defSolutionId";  //缺省solution的ID
	public final static String SPF_INI_CONNECT_URL = "iniConnSvr";     //初始化连接服务器URL
	public final static String SPF_SYSTEM_INITED = "systemInit";       //系统是否已经初始化
	public final static String SPF_CUR_USER_BEAN = "curUserBean";      //当前用户对象保存
	public final static String SPEED_TYPE = "speed_type"; 			   
	public final static String COLLECTION_SCOPE = "CollectionScope_Long"; 		//采测范围
	public final static String DEM = "dem"; 		//地图可示范围

	/**
	 * VRS参考站
	 */
	public final static String ETVRSADDRESS = "etVRSAddress"; 		//网络地址
	public final static String ETVRSPORT = "etVRSPort"; 		//端口号
	public final static String ETVRSUSERNAME = "etVRSUserName"; 		//用户名
	public final static String ETVRSPASSWORD = "etVRSPassword"; 		//密码
	public final static String ETVRSMOUNTPOINT = "etVRSMountPoint"; 		//源节点

	/**
	 * 中海达网络
	 */
	public final static String ETZHDADDRESS = "etZHDAddress"; 		//端口号
	public final static String ETZHDPORT = "etZHDPort"; 		//端口号
	public final static String ETZHDUSERGROUP = "etZHDUserGroup"; 		//用户组
	public final static String ETZHDWORKGROUP = "etZHDWorkGroup"; 		//工作组

	
	// INTENT ACTIONS
	public static final String ACTION_SHOW_NOTIFICATION = "org.androidpn.client.SHOW_NOTIFICATION";

	public static final String ACTION_NOTIFICATION_CLICKED = "org.androidpn.client.NOTIFICATION_CLICKED";

	public static final String ACTION_NOTIFICATION_CLEARED = "org.androidpn.client.NOTIFICATION_CLEARED";
	
	// NOTIFICATION FIELDS

	public static final String NOTIFICATION_ID = "NOTIFICATION_ID";

	public static final String NOTIFICATION_API_KEY = "NOTIFICATION_API_KEY";

	public static final String NOTIFICATION_TITLE = "NOTIFICATION_TITLE";

	public static final String NOTIFICATION_MESSAGE = "NOTIFICATION_MESSAGE";

	public static final String NOTIFICATION_URI = "NOTIFICATION_URI";
	
	public static final String NOTIFICATION_PUSH_STATE = "NOTIFICATION_PUSH_STATE";
	
	// 任务列表更新 ACTIONS
	
	public static final String TASK_LIST_UPDATE_ACTION="task.list.update.action";// 数据更新

	public static final String MOVE_TASK_LIST_UPDATE_ACTION="move.task.list.update.action";// 采测数据更新
	/*****************************************/
	/*推送类型                                                                                              */
	/*****************************************/
	public final static String PUSH_TITLE_TASK="task";// 任务
	public final static String PUSH_TITLE_MSG="msg";// 消息
	public final static String PUSH_TITLE_CONTROL="control";
	
	/*****************************************/
	/*推送消息类型                                                                                            */
	/*****************************************/
	public final static String PUSH_TITLE_MSG_COMMON="common";// 普通消息
	public final static String PUSH_TITLE_MSG_FINISH_TASK="FINISH_TASK";// 结束任务
	public final static String PUSH_TITLE_MSG_DRAINAGE="DRAINAGE";// 排涝推送信息
	public final static String PUSH_TITLE_MSG_COORDINATE="COOPERATE";// 配合工作
	/**********/
	/*刷新评估登记 */
	/*************/
	public final static String ASSESS_GRADE="assess_grade";


	public static final String VALVE = "阀门";
	public static final String MUD_VALVE = "排泥阀";
	public static final String VENT_VALVE = "排气阀";
	public static final String WATER_METER = "水表";
	public static final String FIRE_HYDRANT = "消防栓";
	public static final String DISCHARGE_OUTLET = "出水口";
	public static final String PLUG_SEAL = "堵头封板";
	public static final String NODE_BLACK = "节点";
	public static final String POOL = "水池";
	public static final String METER_READING_UNDONE = "水表(未完成)";
	public static final String METER_READING_COMPLETED = "水表(已完成)";


	public static final String DATE = "com.movementinsome.caice.activity.StatisticalActivity";


	public static final String PROJECT_CREATE = "project_create";

	public static final String SERVICE_NAME ="com.movement.locate.TraceService";
	public static final String TRACE_INFO = "location";
	public static final String RECONNECTION = "reconnection";

	public static final String PASSDATE = "passdate";

	public static final String DELETE_DATE = "delete_date";	//删除数据

	/*
	 * 深水
	*/
	// 下载任务类型taskCategory
	public final static String XJFL_YJ="TASK_PATROL";// 巡检管理:应急
	public final static String QSGL_YJQS="TASK_DREDGE";// 清疏管理:应急清疏
	public final static String WXGL_XCQX="TASK_REPAIRING";// 维修管理:现场抢修
	public final static String TASK_VALVE_REPAIRING="TASK_VALVE_REPAIRING";// 阀门管理:现场抢修

	// 下载标示title
	public final static String COMPLAINANT="COMPLAINANT";// 投诉下载
	public final static String PROBLEM="PROBLEM";// 问题下载

	public final static String CYCLE_PLAN="PLAN_PATROL_CYCLE";// 巡检管理:计划巡检(路面)
	public final static String INTERIM_PLAN="PLAN_PATROL_TEMPORARY";// 巡检管理: 临时巡检(路面)
	public final static String QSGL_JHX="PLAN_DREDGE";// 计划清疏任务
	public final static String WXGL_JHXWX="PLAN_REPAIRING";// 计划维修任务
	public final static String FMGL_JHXWX="PLAN_VALVE_REPAIRING";// 计划阀门维修任务

	public final static String XJGL_GJD_CYCLE="PLAN_KEY_POINT_CYCLE";// 计划关键点任务（周期计划）
	public final static String XJGL_GJD_TEMPORARY="PLAN_KEY_POINT_TEMPORARY";// 计划关键点任务（临时计划）

	public final static String PLAN_CONSTRUCTION_CYCLE="PLAN_CONSTRUCTION_CYCLE";// 计划工地巡查（周期计划）任务
	public final static String PLAN_CONSTRUCTION_TEMPORARY="PLAN_CONSTRUCTION_TEMPORARY";// 计划工地巡查（临时计划）任务

	public final static String PLAN_VALVE_CYCLE="PLAN_VALVE_CYCLE";// 阀门计划巡查（周期计划）
	public final static String PLAN_VALVE_TEMPORARY="PLAN_VALVE_TEMPORARY";// 阀门计划巡查（临时计划）

	public final static String PLAN_HYDRANT_CYCLE="PLAN_HYDRANT_CYCLE";// 消火栓计划巡查（周期计划）
	public final static String PLAN_HYDRANT_TEMPORARY="PLAN_HYDRANT_TEMPORARY";// 消火栓计划巡查（临时计划）任务

	public final static String PLAN_FAC_CYCLE = "PLAN_FAC_CYCLE";// 西江计划巡查（周期计划）任务：PLAN_FAC_CYCLE，
	public final static String PLAN_FAC_TEMPORARY = "PLAN_FAC_TEMPORARY";// 西江计划巡查（临时计划）任务：PLAN_FAC_TEMPORARY

	public final static String COOPERATE="COOPERATE";// 配合工作
	public final static String PLAN="PLAN";// 计划性
	public final static String URGENT="URGENT";// 应急派工单

	public final static String PLAN_PATROL_WS = "PLAN_PATROL_WS";// 巡查(武水)
	public final static String PLAN_PATROL_TEMPORARY_CS_WS = "PLAN_PATROL_TEMPORARY_CS_WS";// 超期任务(武水)

	public final static String TASK_DOWNLOAD_N = "TASK_DOWNLOAD_N";// 未下载(粤海)
	public final static String TASK_DOWNLOAD_Y = "TASK_DOWNLOAD_Y";// 已下载(粤海)

	/**
	 * 移动采测
	 */
	public final static String TASK_STUAS_N = "TASK_STUAS_N";// 未完成
	public final static String TASK_STUAS_Y = "TASK_STUAS_Y";// 完成

	public static final String DEVICE_IS_CONNECTED = "device_is_connected";	//设备已连接
	public static final String UNIT_EXCEPTION = "unit_exception";	//设备异常

	public static final String LEAK = "探漏";

	/*
	 * 粤海taskCategory
	*/
	public final static String YH_INS_TASK = "YH_INS_TASK";
	public final static String YH_WORK_ORDER_TASK = "YH_WORK_ORDER_TASK";// 维修单
	/*
	 * 粤海title
	*/
	public final static String ROUTINE_INS = "ROUTINE_INS";// 常规巡查BsSupervisionPoint,BsRoutineInsArea
	public final static String SPECIAL_INS = "SPECIAL_INS";// 专项巡查BsInsFacInfo
	public final static String EMPHASIS_INS = "EMPHASIS_INS";// 重点区域巡查BsEmphasisInsArea
	public final static String LEAK_INS = "LEAK_INS";// 查漏BsLeakInsArea
	public final static String RRWO_TASK = "RRWO_TASK";// 维修单
	public final static String PIPE_PATROL_YH ="PIPE_PATROL_YH";// 管网巡查

	/*
	 * 武汉项目
	*/
	// taskCategory
	// 派工单
	public final static String COMPLAINANT_FORM="COMPLAINANT_FORM";


	/*
	 * 中山
	*/
	public final static String PLAN_FAC_ZS_CYCLE = "PLAN_FAC_ZS_CYCLE";// 设施计划巡查（周期计划）任务：PLAN_FAC_CYCLE，
	public final static String PLAN_FAC_ZS_TEMPORARY = "PLAN_FAC_ZS_TEMPORARY";// 设施计划巡查（临时计划）任务：PLAN_FAC_ZS_TEMPORARY，
	public final static String PLAN_TIEPAI_CYCLE = "PLAN_TIEPAI_CYCLE";// 设施计划贴牌（周期计划）任务：PLAN_TIEPAI_CYCLE，
	public final static String PLAN_TIEPAI_TEMPORARY = "PLAN_TIEPAI_TEMPORARY";// 设施计划贴牌（临时计划）任务：PLAN_TIEPAI_TEMPORARY，

	public final static String PLAN_PATROL_SCHEDULE = "PLAN_PATROL_SCHEDULE";//计划排班巡查任务
	public final static String PLAN_PATROL_ZS_CYCLE = "PLAN_PATROL_ZS_CYCLE";// 计划巡查（周期计划）
	public final static String PLAN_PATROL_ZS_TEMPORARY ="PLAN_PATROL_ZS_TEMPORARY";// 计划巡查（临时计划）任务

	public final static String INSMAPINACCURATE_FORM = "INSMAPINACCURATE_FORM";//图于现场不符
	// 计划巡查（周期计划）任务：PLAN_PATROL_ZS_CYCLE，
	// 计划巡查（临时计划）任务：PLAN_PATROL_ZS_TEMPORARY，
	// 设施计划巡查（周期计划）任务：PLAN_FAC_ZS_CYCLE，
	// 设施计划巡查（临时计划）任务：PLAN_FAC_ZS_TEMPORARY，
	//计划排班巡查任务：PLAN_PATROL_SCHEDULE
	// 接单任务中文名

	/*
     * 隐患排查
     */
	public final static String PLAN_PAICHA_TEMPORARY = "PLAN_PAICHA_TEMPORARY";//隐患排查巡检单


	public final static Map<String, String> TASK_TITLE_CN=new HashMap<String, String>();
	static{
		TASK_TITLE_CN.put(CYCLE_PLAN, "路面巡查");
		TASK_TITLE_CN.put(INTERIM_PLAN, "路面巡查");
		TASK_TITLE_CN.put(XJGL_GJD_CYCLE, "关键点巡查");
		TASK_TITLE_CN.put(XJGL_GJD_TEMPORARY, "关键点巡查");
		TASK_TITLE_CN.put(QSGL_JHX, "计划清疏");
		TASK_TITLE_CN.put(WXGL_JHXWX, "计划维修");
		TASK_TITLE_CN.put(FMGL_JHXWX, "计划阀门维修");
		TASK_TITLE_CN.put(PLAN_CONSTRUCTION_CYCLE, "工地巡查");
		TASK_TITLE_CN.put(PLAN_CONSTRUCTION_TEMPORARY, "工地巡查");
		TASK_TITLE_CN.put(XJFL_YJ, "应急巡检");
		TASK_TITLE_CN.put(QSGL_YJQS, "应急清疏");
		TASK_TITLE_CN.put(WXGL_XCQX, "现场抢修");
		TASK_TITLE_CN.put(TASK_VALVE_REPAIRING, "阀门抢修");
		TASK_TITLE_CN.put(COMPLAINANT_FORM, "反馈单");
		TASK_TITLE_CN.put(PLAN_VALVE_CYCLE, "阀门巡查");
		TASK_TITLE_CN.put(PLAN_VALVE_TEMPORARY, "阀门临时巡查");
		TASK_TITLE_CN.put(PLAN_HYDRANT_CYCLE, "消火栓巡查");
		TASK_TITLE_CN.put(PLAN_HYDRANT_TEMPORARY, "消火临时栓巡查");
		TASK_TITLE_CN.put(PLAN_FAC_CYCLE, "设施巡查");
		TASK_TITLE_CN.put(PLAN_FAC_TEMPORARY, "设施临时巡查");
		TASK_TITLE_CN.put(ROUTINE_INS, "常规巡查");
		TASK_TITLE_CN.put(SPECIAL_INS, "专项巡查");
		TASK_TITLE_CN.put(EMPHASIS_INS, "重点区域巡查");
		TASK_TITLE_CN.put(LEAK_INS, "管网查漏");
		TASK_TITLE_CN.put(RRWO_TASK, "管网抢修");
		TASK_TITLE_CN.put(PIPE_PATROL_YH, "管网巡查");

		TASK_TITLE_CN.put(PLAN_FAC_ZS_CYCLE, "设施巡查");
		TASK_TITLE_CN.put(PLAN_FAC_ZS_TEMPORARY, "设施临时巡查");
		TASK_TITLE_CN.put(PLAN_TIEPAI_CYCLE, "设施贴牌");
		TASK_TITLE_CN.put(PLAN_TIEPAI_TEMPORARY, "设施临时贴牌");

		TASK_TITLE_CN.put(PLAN_PATROL_SCHEDULE, "排班巡查");
		TASK_TITLE_CN.put(PLAN_PATROL_ZS_CYCLE, "周期巡查");
		TASK_TITLE_CN.put(PLAN_PATROL_ZS_TEMPORARY, "临时巡查");
		TASK_TITLE_CN.put(PLAN_PAICHA_TEMPORARY, "隐患排查");
		TASK_TITLE_CN.put(INSMAPINACCURATE_FORM, "图形纠错");
	}

	// 任务状态
	public final static Map<String, String> TASK_STATUS=new HashMap<String, String>();
	/*****************************************/
	/*上传状态                                                                                                */
	/*****************************************/
	public final static String UPLOAD_STATUS_ARRIVE="2";// 消息已送达
	public final static String UPLOAD_STATUS_RECEIVE="3";// 已接收
	public final static String UPLOAD_STATUS_WORK="4";// 处理中
	public final static String UPLOAD_STATUS_FINISH="5";// 已完成
	public final static String UPLOAD_STATUS_START="6";// 开始
	public final static String UPLOAD_STATUS_PAUSE="7";// 暂停
	public final static String UPLOAD_STATUS_RETREAT = "8";// 退单
	public final static String UPLOAD_STATUS_DELETE = "10";// 销单
	public final static String UPLOAD_STATUS_IDENTICAL="11";// 同号
	//13:维修前开始,14:维修前结束,15:维修中开始,16:维修中结束,17:维修完成开始,18:维修完成结束
	public final static String UPLOAD_STATUS_WXQ_START = "13";// 维修前开始
	public final static String UPLOAD_STATUS_WXQ_FINISH = "14";// 维修前结束
	public final static String UPLOAD_STATUS_WXZ_START = "15";// 维修中开始
	public final static String UPLOAD_STATUS_WXZ_FINISH = "16";// 维修中结束
	public final static String UPLOAD_STATUS_WXH_START = "17";// 维修完成开始
	public final static String UPLOAD_STATUS_WXH_FINISH = "18";// 维修完成结束
	public final static String UPLOAD_STATUS_ABANDONED = "19";//废弃


	static{
		TASK_STATUS.put(UPLOAD_STATUS_ARRIVE, "消息已送达");
		TASK_STATUS.put(UPLOAD_STATUS_RECEIVE, "已接收");
		TASK_STATUS.put(UPLOAD_STATUS_WORK, "处理中");
		TASK_STATUS.put(UPLOAD_STATUS_FINISH, "已完成");
		TASK_STATUS.put(UPLOAD_STATUS_START, "开始");
		TASK_STATUS.put(UPLOAD_STATUS_PAUSE, "暂停");

		TASK_STATUS.put(UPLOAD_STATUS_WXQ_START, "维修前开始");
		TASK_STATUS.put(UPLOAD_STATUS_WXQ_FINISH, "维修前结束");
		TASK_STATUS.put(UPLOAD_STATUS_WXZ_START, "维修中开始");
		TASK_STATUS.put(UPLOAD_STATUS_WXZ_FINISH, "维修中结束");
		TASK_STATUS.put(UPLOAD_STATUS_WXH_START, "维修完成开始");
		TASK_STATUS.put(UPLOAD_STATUS_WXH_FINISH, "维修完成结束");
	}
	// 表单id
	public final static String REPAIRCOVERT_ID="biz.repaircovert";// 补盖记录
	public final static String INSWORKREGEDT_ID="biz.insworkregedt";// 巡查工作登记表
	public final static String INSWORKLINK_ID="biz.insworklink";// 巡查工作联络单
	public final static String BUIDERRPT_ID="biz.buiderrpt";// 工地上报
	public final static String BIZ_DEMOLITIONRPT="biz.demolitionrpt";// 拆迁工地上报
	public final static String GISCORRECT_ID="biz.giscorrect";// 图形纠错
	public final static String GISCORRECT_IDS="biz.giscorrect2_zs";// 图于现场不符
	public final static String INSDREDGEWORK_ID="biz.insdredgework";// 管道清疏记录
	public final static String REPAIRREC_PIPE_ID="biz.repairrec_pipe";// 管道事故记录
	public final static String REPAIRREC_VALVE_ID="biz.repairrec_valve";// 阀门事故记录
	public final static String REPAIRREC_FIRE_ID="biz.repairrec_fire";//消防栓任务记录
	public final static String REPAIRREC_FAC_ID="biz.repairrec_fac";// 设施更换记录
	public final static String REPAIRREC_WELL_ID="biz.repairrec_well";// 井座(箅)维修记录
	public final static String FLOODFB_ID="biz.floodfb";//洪涝反馈
	public final static String COORDINATE_ID="biz.coordinate";//配合工作
	public final static String HID_DENRPT="biz.hiddenrpt";// 设施隐患上报
	public final static String BIZ_CONSTRUCTION="biz.construction";// 工地巡检
	public final static String BIZ_CONSTRUCTION_DISCLOSURE = "biz.construction.disclosure";// 工地交底
	public final static String BIZ_CHECKPOINT="biz.checkpoint";// 签到点
	public final static String BIZ_VALUECOVERT="biz.ins_facility_troubleshoot_gs";// 阀门记录
	public final static String BIZ_KEYPOINT="biz.keypoint";// 关键点
	public final static String BIZ_CONSTRUCTION_TRACK="biz.construction.track";
	public final static String BIZ_FAC_MSG_FORM="biz.fac_msg_form";// 设施搜索
	public final static String BIZ_OFF_VALVE_PROJECT = "biz.off_valve_project";// 关阀方案
	// 业务模块
	public final static String TSK_INSPECTION="tsk.inspection";// 巡检管理
	public final static String TSK_DREGING="tsk.dreging";// 清疏管理
	public final static String TSK_REPAIR="tsk.repair";// 维修管理
	public final static String TSK_VALVE="tsk.valve";// 阀门管理
	public final static String TSK_WS="tsk.ws";// 武水任务管理
	public final static String TSK_YH="tsk.yh";// 粤海任务管理

	//
	public final static int FROM_REQUEST_CODE=141025;
	public final static int FROM_RESULT_CODE=141025;

	// 防洪排涝广播Action
	public final static String FHPL_ACTION="fhpl.action";
	// 配合工作广播Action
	public final static String COORDINATE_ACTION="coordinate.action";


	public final static String FAC_LIST_UPDATA="facList.updata";

}
