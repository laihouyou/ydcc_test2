package com.movementinsome.caice.save;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.vo.CityVo;
import com.movementinsome.kernel.initial.model.City;

import java.sql.SQLException;
import java.util.List;

/**
 * 保存城市列表七参数据
 * Created by zzc on 2017/10/16.
 */

public class SaveCity {
    public static void SaveCityList() throws SQLException {
        Dao<CityVo,Long>  cityVoLongDao= AppContext.getInstance()
                .getAppDbHelper().getDao(CityVo.class);
        List<City> cityList=AppContext.getInstance().getCity();
        if (cityList!=null&&cityList.size()>0){
            for (int i = 0; i < cityList.size(); i++) {
                CityVo cityVo=new CityVo();
                City city=cityList.get(i);
                List<CityVo> cityVos=cityVoLongDao.queryForEq(OkHttpParam.ID,city.getId());
                if (cityVos.size()==0){
                    cityVo.setId(city.getId());
                    cityVo.setCityName(city.getCityName());
                    cityVo.setCityCode(city.getCityCode());

                    cityVo.setCoordinate(city.getCoordTransformModel().getItiCoordinate()==null
                            ?"":city.getCoordTransformModel().getItiCoordinate());
                    cityVo.setSdx(city.getCoordTransformModel().getItiSDx()==null?0:city.getCoordTransformModel().getItiSDx());
                    cityVo.setSdy(city.getCoordTransformModel().getItiSDy()==null?0:city.getCoordTransformModel().getItiSDy());
                    cityVo.setSdz(city.getCoordTransformModel().getItiSDz()==null?0:city.getCoordTransformModel().getItiSDz());
                    cityVo.setSqx(city.getCoordTransformModel().getItiSQx()==null?0:city.getCoordTransformModel().getItiSQx());
                    cityVo.setSqy(city.getCoordTransformModel().getItiSQy()==null?0:city.getCoordTransformModel().getItiSQy());
                    cityVo.setSqz(city.getCoordTransformModel().getItiSQz()==null?0:city.getCoordTransformModel().getItiSQz());
                    cityVo.setSscale(city.getCoordTransformModel().getItiSScale()==null?0:city.getCoordTransformModel().getItiSScale());
                    cityVo.setFdx(city.getCoordTransformModel().getItiFDx()==null?0:city.getCoordTransformModel().getItiFDx());
                    cityVo.setFdy(city.getCoordTransformModel().getItiFDy()==null?0:city.getCoordTransformModel().getItiFDy());
                    cityVo.setFscale(city.getCoordTransformModel().getItiFScale()==null?0:city.getCoordTransformModel().getItiFScale());
                    cityVo.setFrotateangle(city.getCoordTransformModel().getItiFRotateangle()==null?0:city.getCoordTransformModel().getItiFRotateangle());
                    cityVo.setPprojectionType(city.getCoordTransformModel().getItiPProjectiontype()==null?0:city.getCoordTransformModel().getItiPProjectiontype());
                    cityVo.setPcentralmeridian(city.getCoordTransformModel().getItiPCentralmeridian()==null?0:city.getCoordTransformModel().getItiPCentralmeridian());
                    cityVo.setPscale(city.getCoordTransformModel().getItiPScale()==null?0:city.getCoordTransformModel().getItiPScale());
                    cityVo.setPconstantx(city.getCoordTransformModel().getItiPConstantx()==null?0:city.getCoordTransformModel().getItiPConstantx());
                    cityVo.setPconstanty(city.getCoordTransformModel().getItiPConstanty()==null?0:city.getCoordTransformModel().getItiPConstanty());
                    cityVo.setPbenchmarklatitude(city.getCoordTransformModel().getItiPBenchmarklatitude()==null?0:city.getCoordTransformModel().getItiPBenchmarklatitude());
                    cityVo.setSemimajor(city.getCoordTransformModel().getItiSemimajor()==null?0:city.getCoordTransformModel().getItiSemimajor());
                    cityVo.setFlattening(city.getCoordTransformModel().getItiFlattening()==null?0:city.getCoordTransformModel().getItiFlattening());
                    int s=cityVoLongDao.create(cityVo);
                    if (s==1){

                    }
                }
            }
        }
    }
}
