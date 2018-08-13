package com.movementinsome.caice.util;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.model.LatLng;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.okhttp.OkHttpRequest;
import com.movementinsome.caice.vo.MiningSurveyVO;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.database.vo.DynamicFormVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import com.gddst.leak.caice.vo.SaveLineVo;

/**
 * Created by zzc on 2017/5/9.
 */

public class SaveData {

    /**
     * 编辑点数据  更新数据
     *
     * @param dataJson  输入的数据集合
     */
    public static boolean EditPointDate(Context context, String dataJson, HashMap iparams
            ,Bundle bundle){
        try {

            MiningSurveyVO miningSurveyVO=null;

            /**
             * 修改本地数据库
             */

            SavePointVo newSavePointVo= JSON.parseObject(dataJson,SavePointVo.class);      //修改过的信息

            SavePointVo savePointVo= (SavePointVo) bundle.getSerializable("editPointVo");   //原来的VO

            //更新如下字段
            /**
             *   //设备名
             @DatabaseField
             private String implementorName;

             //口径
             @DatabaseField
             private String caliber;

             //类型
             @DatabaseField
             private String type;

             //设备点经度
             @DatabaseField
             private String longitude;

             //设备点纬度
             @DatabaseField
             private String latitude;

             //地面高程
             @DatabaseField
             private String groundElevation;

             //埋深
             @DatabaseField
             private String burialDepth;

             //发生地址
             @DatabaseField
             private String happenAddr;

             //行政区
             @DatabaseField
             private String administrativeRegion;

             //照片路径
             @DatabaseField
             private String camera;
             */

            savePointVo.setFacName(newSavePointVo.getFacName());
            savePointVo.setImplementorName(newSavePointVo.getImplementorName());
            savePointVo.setCaliber(newSavePointVo.getCaliber());
            savePointVo.setType(newSavePointVo.getType());
            savePointVo.setLongitude(newSavePointVo.getLongitude());
            savePointVo.setLatitude(newSavePointVo.getLatitude());
            savePointVo.setLongitudeWg84(newSavePointVo.getLongitudeWg84());
            savePointVo.setLatitudeWg84(newSavePointVo.getLatitudeWg84());
            savePointVo.setGroundElevation(newSavePointVo.getGroundElevation());
            savePointVo.setBurialDepth(newSavePointVo.getBurialDepth());
            savePointVo.setHappenAddr(newSavePointVo.getHappenAddr());
            savePointVo.setAdministrativeRegion(newSavePointVo.getAdministrativeRegion());
            savePointVo.setCamera(newSavePointVo.getCamera());
            savePointVo.setContext(dataJson);

            Dao<SavePointVo,Long> savePointVoLongDao= AppContext.getInstance().getAppDbHelper().getDao(SavePointVo.class);
            int s=savePointVoLongDao.update(savePointVo);
            if (s==1){
                //录入数据成功后更新时间
                Dao<MiningSurveyVO,Long> miningSurveyVOLongDao=AppContext.getInstance().getAppDbHelper().getDao(MiningSurveyVO.class);
                List<MiningSurveyVO> miningSurveyVOList = miningSurveyVOLongDao.
                        queryForEq(com.movementinsome.caice.okhttp.OkHttpParam.PROJECT_ID,savePointVo.getProjectId());
                if (miningSurveyVOList!=null&&miningSurveyVOList.size()>0){
                    miningSurveyVO = miningSurveyVOList.get(0);

                }
                if (miningSurveyVO!=null){
                    miningSurveyVO.setProjectEDateUpd(DateUtil.getNow());
                }
                int y=miningSurveyVOLongDao.update(miningSurveyVO);
                Log.i("s",y+"");

            }

            /**
             * 修改百度LBS云
             */
            LatLng myLatLng=new LatLng(Double.parseDouble(savePointVo.getMyLatitude())
                    ,Double.parseDouble(savePointVo.getMyLongitude()));

//            OkHttpRequest.IsUpdateResp(null, savePointVo.getHappenAddr(),
//                    myLatLng, true, savePointVo.getFacName()
//                    , savePointVo.getLongitudeWg84()
//                    ,savePointVo.getLatitudeWg84(), savePointVo.getMarkerId()
//                    , false,false,"","",""
//                    ,savePointVo.getImplementorName()
//            );
            List<SavePointVo> savePointVoList=new ArrayList<>();
            savePointVoList.add(savePointVo);
            OkHttpRequest.IsUpdatePio(savePointVoList,context,miningSurveyVO);

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     *  新增探漏巡查数据  确定数据
     *
     * @param dataJson  输入的数据集合
     */
    public static boolean EditLeakDate(Context context, String dataJson, HashMap iparams
            ,Bundle bundle,DynamicFormVO dynFormVo){
        try {

            Dao<SavePointVo,Long> savePointVoLongDao= AppContext.getInstance().getAppDbHelper().getDao(SavePointVo.class);
            MiningSurveyVO miningSurveyVO=null;

            /**
             * 修改本地数据库
             */

            SavePointVo newSavePointVo= JSON.parseObject(dataJson,SavePointVo.class);      //修改过的信息     确定VO

            SavePointVo savePointVo= (SavePointVo) bundle.getSerializable("editPointVo");   //原来的VO    探漏VO
            SavePointVo savePointVo_leak= (SavePointVo) bundle.getSerializable("savePointVo_leak");   //确定VO 如果已经确定过
            String leakId=savePointVo.getId();

            //更新如下字段
            /**
             //是否爆漏
             //0 为不漏
             //1 为漏点
             @DatabaseField
             private String isLeak;

             //确定口径
             @DatabaseField
             private String caliber_leak;

             //确定埋深
             @DatabaseField
             private String burialDepth_leak;

             //确定照片路径
             @DatabaseField
             private String camera_leak;

             //确定备注
             @DatabaseField
             private String remarks_leak;

             //确定人
             @DatabaseField
             private String uploadName_leak;

             //确定人id
             @DatabaseField
             private String userId_leak;

             //确定时间
             @DatabaseField
             private String uploadTime_leak;

             //确认经度
             @DatabaseField
             private String longitudeBd09_leak;

             //确认纬度
             @DatabaseField
             private String latitudeBd09_leak;

             //填单所填写的字段集合
             @DatabaseField
             private String context_leak;

             //确定定位VO 数据集合
             @DatabaseField
             private String locationJson_leak;
             */

            savePointVo.setIsLeak(newSavePointVo.getIsLeak());
            savePointVo.setCaliber_leak(newSavePointVo.getCaliber_leak());
            savePointVo.setBurialDepth_leak(newSavePointVo.getBurialDepth_leak());
            savePointVo.setLongitudeBd09_leak(newSavePointVo.getLongitudeBd09_leak());
            savePointVo.setLatitudeBd09_leak(newSavePointVo.getLatitudeBd09_leak());
            savePointVo.setCamera_leak(newSavePointVo.getCamera_leak());
            savePointVo.setRemarks_leak(newSavePointVo.getRemarks_leak());
            savePointVo.setWater_leak(newSavePointVo.getWater_leak());
            savePointVo.setContext_leak(dataJson);

            savePointVo.setUserId_leak(AppContext.getInstance().getCurUser().getUserId());
            savePointVo.setUploadName_leak(AppContext.getInstance().getCurUser().getUserName());
            savePointVo.setUploadTime_leak(DateUtil.getNow());
            savePointVo.setLocationJson_leak((String)iparams.get(OkHttpParam.LOCATION_JSON));

            //查询数据库判断该探漏点是否已经 当前用户是否已经 确认过
            if (savePointVo_leak==null){    //数据不存在
                savePointVo.setType(OkHttpParam.LEAK);      //确定点
                savePointVo.setPatrolId(leakId);     //探漏点id  确定点与探漏点 唯一绑定字段
                savePointVo.setId(UUIDUtil.getUUID());
                savePointVo.setFacName(UUIDUtil.getUUID());

                savePointVo.setUploadName(AppContext.getInstance().getCurUserName());   //上传人
                savePointVo.setUploadTime(DateUtil.getNow());   //上传时间
                savePointVo.setDynamicFormVOId(dynFormVo.getId());        //原来数据库VO  ID
                savePointVo.setGuid(dynFormVo.getGuid());       //guid  关联附件唯一ID

                if (iparams!=null){
                    savePointVo.setProjectId((String) iparams.get(OkHttpParam.PROJECT_ID));
                    savePointVo.setProjectShare((String) iparams.get("isProjectShare"));
                    savePointVo.setShareCode((String) iparams.get(OkHttpParam.SHARE_CODE));
                    savePointVo.setProjectName((String) iparams.get(OkHttpParam.PROJECT_NAME));
                    savePointVo.setProjectType((String) iparams.get(OkHttpParam.PROJECT_TYPE));
                }

                int s=savePointVoLongDao.create(savePointVo);
                if (s==1){
                    //录入数据成功后更新时间
                    Dao<MiningSurveyVO,Long> miningSurveyVOLongDao=AppContext.getInstance().getAppDbHelper().getDao(MiningSurveyVO.class);
                    List<MiningSurveyVO> miningSurveyVOList = miningSurveyVOLongDao.
                            queryForEq(OkHttpParam.PROJECT_ID,savePointVo.getProjectId());
                    if (miningSurveyVOList!=null&&miningSurveyVOList.size()>0){
                        miningSurveyVO = miningSurveyVOList.get(0);

                    }
                    if (miningSurveyVO!=null){
                        miningSurveyVO.setProjectEDateUpd(DateUtil.getNow());
                    }
                    int y=miningSurveyVOLongDao.update(miningSurveyVO);
                    Log.i("s",y+"");

                    //修改探漏VO
                    List<SavePointVo> savePointVoList=savePointVoLongDao.queryForEq(OkHttpParam.FAC_ID,leakId);   //原来的VO    探漏VO
                    if (savePointVoList.size()==1){
                        savePointVoList.get(0).setIsLeak("是");
                        int f=savePointVoLongDao.update(savePointVoList.get(0));
                        Log.i("s",f+"");
                    }

                    //更新到服务器上
                    OkHttpRequest.IsUpdatePio(savePointVoList,context,miningSurveyVO);
                }
                /**
                 * 提交服务器
                 */

                List<SavePointVo> savePointVoList=new ArrayList<>();
                savePointVoList.add(savePointVo);
                OkHttpRequest.SubmitPointList(savePointVoList,context);

            }

            else {
                savePointVo_leak.setIsLeak(newSavePointVo.getIsLeak());
                savePointVo_leak.setCaliber_leak(newSavePointVo.getCaliber_leak());
                savePointVo_leak.setBurialDepth_leak(newSavePointVo.getBurialDepth_leak());
                savePointVo_leak.setLongitudeBd09_leak(newSavePointVo.getLongitudeBd09_leak());
                savePointVo_leak.setLatitudeBd09_leak(newSavePointVo.getLatitudeBd09_leak());
                savePointVo_leak.setCamera_leak(newSavePointVo.getCamera_leak());
                savePointVo_leak.setRemarks_leak(newSavePointVo.getRemarks_leak());
                savePointVo_leak.setWater_leak(newSavePointVo.getWater_leak());
                savePointVo_leak.setContext_leak(dataJson);

                savePointVo_leak.setUploadTime_leak(DateUtil.getNow());
                int s=savePointVoLongDao.update(savePointVo_leak);
                if (s==1){
                    //录入数据成功后更新时间
                    Dao<MiningSurveyVO,Long> miningSurveyVOLongDao=AppContext.getInstance().getAppDbHelper().getDao(MiningSurveyVO.class);
                    List<MiningSurveyVO> miningSurveyVOList = miningSurveyVOLongDao.
                            queryForEq(OkHttpParam.PROJECT_ID,savePointVo_leak.getProjectId());
                    if (miningSurveyVOList!=null&&miningSurveyVOList.size()>0){
                        miningSurveyVO = miningSurveyVOList.get(0);

                    }
                    if (miningSurveyVO!=null){
                        miningSurveyVO.setProjectEDateUpd(DateUtil.getNow());
                    }
                    int y=miningSurveyVOLongDao.update(miningSurveyVO);
                    Log.i("s",y+"");

                }

                /**
                 * 修改百度LBS云
                 */

                List<SavePointVo> savePointVoList=new ArrayList<>();
                savePointVoList.add(savePointVo_leak);
                OkHttpRequest.IsUpdatePio(savePointVoList,context,miningSurveyVO);

            }


            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 编辑普通线数据  更新数据
     *
     * @param dataJson  输入的数据集合
     */
    public static boolean EditLine(String dataJson,Bundle bundle,Context context){
        try {

            MiningSurveyVO miningSurveyVO=null;

            /**
             * 修改本地数据库
             */

            Dao<SavePointVo,Long> saveLineVoLongDao= AppContext.getInstance()
                    .getAppDbHelper().getDao(SavePointVo.class);

            SavePointVo savePointVo1= JSON.parseObject(dataJson,SavePointVo.class);      //修改过的信息

//            SavePointVo savePointVo= (SavePointVo) bundle.getSerializable("editLineVo");   //原来的VO

            List<String> ids= (List<String>) bundle.getSerializable("ids");     //原来的设施点ID集合
            if (ids!=null&&!ids.equals("")){
                SavePointVo savePointVo;
                List<SavePointVo> savePointVoList;
                for (int i=0;i<ids.size();i++){
                    String id=ids.get(i);
                    if (id!=null&&!id.equals("")){
                        savePointVoList=saveLineVoLongDao.queryForEq("id",id);
                        if (savePointVoList!=null&&savePointVoList.size()==1){
                            savePointVo=savePointVoList.get(0);

                            //更新如下字段
                            /**
                            //口径
                            @DatabaseField
                            private String caliber;
                            //所在道路
                            @DatabaseField
                            private String happenAddr;
                            //管材
                            @DatabaseField
                            private String tubularProduct;
                            //管线类型
                            @DatabaseField
                            private String pipType;
                            //敷设类型
                            @DatabaseField
                            private String layingType;
                            //行政区
                            @DatabaseField
                            private String administrativeRegion;
                            */
                            savePointVo.setPipName(savePointVo1.getPipName());savePointVo.setContext(dataJson);
                            savePointVo.setCaliber(savePointVo1.getCaliber());
                            savePointVo.setHappenAddr(savePointVo1.getHappenAddr());
                            savePointVo.setTubularProduct(savePointVo1.getTubularProduct());
                            savePointVo.setPipType(savePointVo1.getPipType());
                            savePointVo.setLayingType(savePointVo1.getLayingType());
                            savePointVo.setAdministrativeRegion(savePointVo1.getAdministrativeRegion());

                            int s=saveLineVoLongDao.update(savePointVo);
                            if (s==1){
                                //录入数据成功后更新时间
                                Dao<MiningSurveyVO,Long> miningSurveyVOLongDao=AppContext.getInstance()
                                        .getAppDbHelper().getDao(MiningSurveyVO.class);
                                List<MiningSurveyVO> miningSurveyVOList = miningSurveyVOLongDao.
                                        queryForEq(OkHttpParam.PROJECT_ID,savePointVo.getProjectId());
                                if (miningSurveyVOList!=null&&miningSurveyVOList.size()>0){
                                    miningSurveyVO = miningSurveyVOList.get(0);

                                }
                                if (miningSurveyVO!=null){
                                    miningSurveyVO.setProjectEDateUpd(DateUtil.getNow());
                                }
                                int y=miningSurveyVOLongDao.update(miningSurveyVO);
                                Log.i("s",y+"");

                                /**
                                 * 修改服务器
                                 */
                                List<SavePointVo> savePointVos=new ArrayList<>();
                                savePointVos.add(savePointVo);
                                OkHttpRequest.IsUpdatePio(savePointVos,context,miningSurveyVO);
                            }

                        }
                    }
                }
            }

//            SavePointVo savePointVo= (SavePointVo) bundle.getSerializable("editLineVo");   //原来的VO

            //更新如下字段
            /**

             //口径
             @DatabaseField
             private String caliber;
             //所在道路
             @DatabaseField
             private String happenAddr;
             //管材
             @DatabaseField
             private String tubularProduct;
             //管线类型
             @DatabaseField
             private String pipType;
             //敷设类型
             @DatabaseField
             private String layingType;
             //行政区
             @DatabaseField
             private String administrativeRegion;
             */
//            savePointVo.setPipName(savePointVo1.getPipName());savePointVo.setContext(dataJson);
//            savePointVo.setCaliber(savePointVo1.getCaliber());
//            savePointVo.setHappenAddr(savePointVo1.getHappenAddr());
//            savePointVo.setPipMaterial(savePointVo1.getTubularProduct());
//            savePointVo.setPipType(savePointVo1.getPipType());
//            savePointVo.setLayingType(savePointVo1.getLayingType());
//            savePointVo.setAdministrativeRegion(savePointVo1.getAdministrativeRegion());
//
//            int s=saveLineVoLongDao.update(savePointVo);
//            if (s==1){
//                //录入数据成功后更新时间
//                Dao<ProjectVo,Long> miningSurveyVOLongDao=AppContext.getInstance()
//                        .getAppDbHelper().getDao(ProjectVo.class);
//                ProjectVo miningSurveyVO=null;
//                List<ProjectVo> miningSurveyVOList = miningSurveyVOLongDao.
//                        queryForEq(OkHttpParam.PROJECT_ID,savePointVo.getProjectId());
//                if (miningSurveyVOList!=null&&miningSurveyVOList.size()>0){
//                    miningSurveyVO = miningSurveyVOList.get(0);
//
//                }
//                if (miningSurveyVO!=null){
//                    miningSurveyVO.setProjectEDateUpd(DateUtil.getNow());
//                }
//                int y=miningSurveyVOLongDao.update(miningSurveyVO);
//                Log.i("s",y+"");
//
//                /**
//                 * 修改百度LBS云
//                 */
//                LatLng myLatLng=new LatLng(Double.parseDouble(savePointVo.getMyLatitude())
//                        ,Double.parseDouble(savePointVo.getMyLongitude()));
//
////                String ids=savePointVo.getIds();
////                String[] idsData =ids.split(",");
////                if (idsData!=null&&!idsData[0].equals("")){
////                    for (int i=0;i<idsData.length;i++){
////                        OkHttpRequest.IsUpdateResp(null, "",
////                                myLatLng, true, savePointVo.getPipName()
////                                , ""
////                                ,"", idsData[i]
////                                , true,false,"","",""
////                                ,""
////                        );
////                    }
////                }
//
//                List<SavePointVo> savePointVoList=new ArrayList<>();
//                savePointVoList.add(savePointVo);
//                OkHttpRequest.IsUpdatePio(savePointVoList,context);
//
//                return true;
//            }

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
