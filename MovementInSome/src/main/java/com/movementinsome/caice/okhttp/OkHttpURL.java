package com.movementinsome.caice.okhttp;

import com.movementinsome.AppContext;
import com.movementinsome.R;

/**请求的URL类
 * Created by zzc on 2017/3/21.
 */

public class OkHttpURL {

    /**
     * 公司BS地址
     */
    public static final String urlGeodata = AppContext.getInstance().getString(R.string.urlGeodata);
    public static final String urlPointListview = AppContext.getInstance().getString(R.string.urlPointListview);// GET请求   查询指定条件的数据（poi）列表接口
    public static final String urlPointListUserId = AppContext.getInstance().getString(R.string.urlPointListUserId);// GET请求   查询指定条件的数据（poi）列表接口
    public static final String urlPoint = AppContext.getInstance().getString(R.string.urlPoint);// GET请求   查询指定id的数据（poi）详情接口
    public static final String urlUpdatePoint = AppContext.getInstance().getString(R.string.urlUpdatePoint);// POST请求   修改数据（poi）接口
    public static final String urlDeletePoint = AppContext.getInstance().getString(R.string.urlDeletePoint);// POST请求   删除数据（poi）接口
    public static final String creatProject = AppContext.getInstance().getString(R.string.creatProject);    //创建工程
    public static final String subAllData = AppContext.getInstance().getString(R.string.subAllData);

    public static final String listProject = AppContext.getInstance().getString(R.string.listProject);// GET请求   查询指定条件的数据（poi）列表接口
    public static final String detailProject = AppContext.getInstance().getString(R.string.detailProject);// GET请求   查询指定id的数据（poi）详情接口
    public static final String updateProject = AppContext.getInstance().getString(R.string.updateProject);// POST请求   修改数据（poi）接口
    public static final String deleteProject = AppContext.getInstance().getString(R.string.deleteProject);// POST请求   删除数据（poi）接口
    public static final String shareProject = AppContext.getInstance().getString(R.string.shareProject);// POST请求   删除数据（poi）接口

    public static final String register = "/appInitResource/registerUser";// POST请求   注册用户接口
    public static final String sendRegisterCaptcha = "/appInitResource/sendRegisterCaptcha?";// POST请求   获取邮箱验证码

    public static final String passswordReset = "/appInitResource/passswordReset";// POST请求   密码重置
    public static final String sendPassswordResetCaptcha = "/appInitResource/sendPassswordResetCaptcha?";// POST请求   修改密码获取邮箱验证码

    public static final String baseUrl= "http://172.16.1.40:8080/gisapp/rest";// 通用URL

    public static final String uploadFiles= "/mobileDetectionResource/uploadFiles";// 上传文件  ()

    public static final String FILE= "file";


//    public static final String serverUrl = "http://172.16.1.34:8080/fisds/rest";//测试中间层地址
//    public static final String serverUrl = "http://172.16.1.21:8080/fisds/rest";//测试中间层地址
    public static final String serverUrl = AppContext.getInstance().getServerUrl() ;//中间层地址
    public static final String fileServer = AppContext.getInstance().getFileServerUrl() ;//中间层文件服务地址
}
