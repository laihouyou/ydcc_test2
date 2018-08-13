package com.movementinsome.caice.save;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.util.CreateFiles;
import com.movementinsome.caice.util.DateUtil;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.database.vo.DynamicFormVO;
import com.movementinsome.kernel.location.LocationInfoExt;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

/**文件保存类
 *
 * Created by zzc on 2017/5/10.
 */

public class SaveFile {
    private File path;
    private CreateFiles createFiles;
    private Context mContext;

    public SaveFile(Context context){
        this.mContext=context;

        if (createFiles==null){
            createFiles=new CreateFiles();
        }
    }

    public  void SavePointDatabase(SavePointVo savePointVo, HashMap iparams, String dynamicFormVOId, DynamicFormVO dynFormVo) {
        try {
            Dao<SavePointVo, Long> savePointVos = AppContext.getInstance()
                    .getAppDbHelper().getDao(SavePointVo.class);

            if (AppContext.getInstance().getCurLocation() != null) {
                LocationInfoExt location = AppContext.getInstance().getCurLocation();
                String locationStr = location.getCurBd09Position();        //获取bd09坐标
                String[] locationDouble = locationStr.split(" ");

                savePointVo.setMyLongitude(locationDouble[0]);
                savePointVo.setMyLatitude(locationDouble[1]);
                savePointVo.setCoordinateType("bd09ll");
            }
            savePointVo.setUploadName(AppContext.getInstance().getCurUserName());   //上传人
            savePointVo.setUploadTime(DateUtil.getNow());   //上传时间
            savePointVo.setId(UUID.randomUUID().toString());   //用当前毫秒值做ID
            savePointVo.setDynamicFormVOId(dynamicFormVOId);        //原来数据库VO  ID
            savePointVo.setGuid(dynFormVo.getGuid());       //guid  关联附件唯一ID


            if (iparams != null) {
//                savePointVo.setLongitude((String) iparams.get("longitude"));
//                savePointVo.setLatitude((String) iparams.get("latitude"));
//                savePointVo.setHappenAddr((String) iparams.get("happenAddr"));       //地址
//                savePointVo.setFacName((String) iparams.get("facName"));       //表号
//                savePointVo.setImplementorName((String)iparams.get("implementorName"));       //设施类型      前面有设置
                savePointVo.setProjectId((String) iparams.get(OkHttpParam.PROJECT_ID));
                savePointVo.setProjectShare((String) iparams.get("isProjectShare"));
                savePointVo.setShareCode((String) iparams.get(OkHttpParam.SHARE_CODE));
                savePointVo.setProjectName((String) iparams.get(OkHttpParam.PROJECT_NAME));
                savePointVo.setProjectType((String) iparams.get(OkHttpParam.PROJECT_TYPE));
                savePointVo.setGatherType((String) iparams.get("gatherType"));   //采集类型
                savePointVo.setIsSuccession((String) iparams.get("isSuccession"));   //是否是自动采集
                savePointVo.setDataType(MapMeterMoveScope.POINT);       //设置数据类型
                savePointVo.setType(OkHttpParam.PATROL);    //设置为谈漏点

                savePointVo.setMapx((String) iparams.get(OkHttpParam.MAP_X));
                savePointVo.setMapy((String) iparams.get(OkHttpParam.MAP_Y));
                savePointVo.setLongitudeWg84((String) iparams.get(OkHttpParam.LONGITUDE_WG84));
                savePointVo.setLatitudeWg84((String) iparams.get(OkHttpParam.LATITUDE_WG84));
                savePointVo.setLocationJson((String) iparams.get(OkHttpParam.LOCATION_JSON));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void SaveLineDatabase(SavePointVo saveLineVo,HashMap iparams,DynamicFormVO dynFormVo){
        try {
            String dynamicFormVOId=dynFormVo.getId();
            Dao<SavePointVo, Long> savePointVoLongDao =
                    AppContext.getInstance().getAppDbHelper().getDao(SavePointVo.class);
            if (AppContext.getInstance().getCurLocation() != null) {
                LocationInfoExt location = AppContext.getInstance().getCurLocation();
                String locationStr = location.getCurBd09Position();        //获取bd09坐标
                String[] locationDouble = locationStr.split(" ");

                saveLineVo.setMyLongitude(locationDouble[0]);
                saveLineVo.setMyLatitude(locationDouble[1]);
                saveLineVo.setCoordinateType("bd09ll");
            }
            saveLineVo.setUploadName(AppContext.getInstance().getCurUserName());   //上传人
            saveLineVo.setUsedId(AppContext.getInstance().getCurUser().getUserId());   //上传人id
            saveLineVo.setUploadTime(DateUtil.getNow());   //上传时间
            saveLineVo.setDynamicFormVOId(dynamicFormVOId);
            saveLineVo.setGuid(dynFormVo.getGuid());

            if (iparams != null) {
                saveLineVo.setProjectId((String) iparams.get(OkHttpParam.PROJECT_ID));
                saveLineVo.setIsProjectShare((String) iparams.get("isProjectShare"));
                saveLineVo.setShareCode((String) iparams.get(OkHttpParam.SHARE_CODE));
                saveLineVo.setProjectName((String) iparams.get(OkHttpParam.PROJECT_NAME));
                saveLineVo.setProjectType((String) iparams.get(OkHttpParam.PROJECT_TYPE));
                saveLineVo.setPipelineLinght(Double.parseDouble((String) iparams.get("pipelineLinght")));
                saveLineVo.setGatherType((String) iparams.get("gatherType"));
                saveLineVo.setsIds((String) iparams.get("lineMakreIds"));
                saveLineVo.setFacNums((String) iparams.get("facNums"));
                saveLineVo.setIsSuccession((String)iparams.get("isSuccession"));   //是否是自动采集
                saveLineVo.setPointList((String)iparams.get("pointList"));   //坐标集合
                saveLineVo.setMapx((String)iparams.get(OkHttpParam.MAP_X));
                saveLineVo.setMapy((String)iparams.get(OkHttpParam.MAP_Y));
                saveLineVo.setLongitudeWg84((String)iparams.get(OkHttpParam.LONGITUDE_WG84));
                saveLineVo.setLatitudeWg84((String)iparams.get(OkHttpParam.LATITUDE_WG84));
                saveLineVo.setLocationJson((String)iparams.get(OkHttpParam.LOCATION_JSON));

            }

//            int staus = savePointVoLongDao.create(saveLineVo);
//            if (staus == 1) {
//
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
