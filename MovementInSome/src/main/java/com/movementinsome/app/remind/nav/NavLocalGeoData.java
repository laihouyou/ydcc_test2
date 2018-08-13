package com.movementinsome.app.remind.nav;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.GeometryException;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.Proximity2DResult;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Unit;
import com.movementinsome.AppContext;
import com.movementinsome.database.vo.BsEmphasisInsArea;
import com.movementinsome.database.vo.BsInsFacInfo;
import com.movementinsome.database.vo.BsLeakInsArea;
import com.movementinsome.database.vo.BsRoutineInsArea;
import com.movementinsome.database.vo.BsSupervisionPoint;
import com.movementinsome.database.vo.InsCheckFacRoad;
import com.movementinsome.database.vo.InsKeyPointPatrolData;
import com.movementinsome.database.vo.InsPatrolAreaData;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsSiteManage;
import com.movementinsome.kernel.location.LocationInfo;
import com.movementinsome.kernel.location.LocationInfoExt;
import com.movementinsome.kernel.util.MyDateTools;
import com.movementinsome.map.utils.MapUtil;

public class NavLocalGeoData {

	final static int GPS_BUF;// = AppContext.getInstance().getNavigation().getGpsBuf();  //100
	final static int ROAD_BUF;// = AppContext.getInstance().getNavigation().getRoadBuf();// 40;
	final static int ARRIVE_BUF;// = AppContext.getInstance().getNavigation().getArrive();
	final static double _TORLENCE;
	
	static SpatialReference sr;

/*	public NavLocalGeoData(){
		sr = SpatialReference.create(Integer.valueOf(AppContext.getInstance().getSrid()));
	}*/
	
	static{
		sr = SpatialReference.create(Integer.valueOf(AppContext.getInstance().getSrid()));
		GPS_BUF = AppContext.getInstance().getNavigation().getGpsBuf();  //100
		ROAD_BUF = AppContext.getInstance().getNavigation().getRoadBuf();// 40;
		ARRIVE_BUF = AppContext.getInstance().getNavigation().getArrive();
		_TORLENCE = AppContext.getInstance().getNavigation().getTolerance();
	}
	
	/**
	 * 返回 坐标点与线路之间的最短距离
	 * @param pointXY 坐标点 “X Y”
	 * @param wktGeom 线段字符串描述
	 * @return 点与线段之间的最短距离
	 */
	public static double cacDistance(String pointXY,String wktGeom){
		Point gps = new Point(Double.valueOf(pointXY.split("\\ ")[0]),Double.valueOf(pointXY.split("\\ ")[1]));
		Geometry geoLuDan = MapUtil.wkt2Geometry(wktGeom);
		Proximity2DResult proximity = GeometryEngine.getNearestCoordinate(geoLuDan,gps, true);
		return proximity.getDistance();
	}
	
	/**
	 * 判断路段是否符合完成的要求
	 * @param runTrace 巡查轨迹
	 * @param roadShape 路段
	 * @param tolerance 容差值，未巡长度容忍值  ,值为0表示没有容差，1表示不受限
	 * @return
	 */
	public static boolean isContain(Polyline runTrace,Geometry roadShape,double tolerance){
		Geometry geoLuDan = GeometryEngine.buffer(runTrace,sr, ARRIVE_BUF, Unit.create(LinearUnit.Code.METER));
/*		Envelope env = new Envelope();
		geoLuDan.queryEnvelope(env);*/
		
		Geometry clipGeo = GeometryEngine.intersect(roadShape,geoLuDan, sr);
		if(_TORLENCE!=0){
			return  (1.00 - clipGeo.calculateLength2D()/roadShape.calculateLength2D())<_TORLENCE;
		}
		return  (1.00 - clipGeo.calculateLength2D()/roadShape.calculateLength2D())<tolerance;
	}
	
	public static Geometry hadRunRoad(Polyline runTrace,Geometry roadShape){
		if (null == runTrace || null == roadShape){
			return null;
		}else{
			try{
				Geometry geoLuDan = GeometryEngine.buffer(runTrace,sr, ARRIVE_BUF, Unit.create(LinearUnit.Code.METER));
			/*	Envelope env = new Envelope();
				geoLuDan.queryEnvelope(env);*/
				return GeometryEngine.intersect(roadShape,geoLuDan, sr);
			}catch(GeometryException ex){
				return null;
			}
		}
		
	}
	
	public static Polyline hadRunTrace(Polyline runTrace,Polyline preRunTrace){
		List<Geometry> geoLst = new ArrayList();
		if (null != runTrace){
			geoLst.add(runTrace);
		}
		if (null != preRunTrace){
			geoLst.add(preRunTrace);
		}
		if (geoLst.size() == 0){
			return new Polyline();
		}else{
			Geometry[] geos = (Geometry[])geoLst.toArray(new Geometry[geoLst.size()]);
			Geometry pl = GeometryEngine.union(geos, sr);
			if (null == pl){
				return new Polyline();
			}else{
				return (Polyline)pl;
			}
		}
	}
	
	/**
	 * 通过当前位置点所在路段
	 * 
	 * @param locationInfo
	 * @return
	 */
	public static NearObject findNearLuDuan(LocationInfo locationInfo,Vector<InsPatrolDataVO> lstPatrolData) {
		List<InsPatrolDataVO> lst = new ArrayList<InsPatrolDataVO>();
/*		double minDistance=0.00;
		String nearLD="";*/
				
		NearObject nearObj = new NearObject();
		
		if (lstPatrolData == null){
			return null;
		}
		
		if (locationInfo != null){
			//保存最近对象的当前地图位置
			nearObj.setMapXY(locationInfo.getCurMapPosition());
			//保存最近对象的当前地图位置的位置时间
			nearObj.setDateTime(null==locationInfo.getTime()?MyDateTools.getCurDateTime():locationInfo.getTime());
			
			try{
				Point gps = new Point(locationInfo.getMapx(),locationInfo.getMapy());
			
				Geometry geo = GeometryEngine.buffer(gps,sr, GPS_BUF, Unit.create(LinearUnit.Code.METER));
	
				for (InsPatrolDataVO ins : lstPatrolData){
					
					Geometry geoLuDan = GeometryEngine.buffer(ins.getShape(),sr, ROAD_BUF, Unit.create(LinearUnit.Code.METER));
					
					if (GeometryEngine.crosses(geo, geoLuDan, sr) || GeometryEngine.contains(geoLuDan, geo, sr) || GeometryEngine.touches(geo,geoLuDan, sr) || !GeometryEngine.intersect(geo, geoLuDan, sr).isEmpty()){
						lst.add(ins);
					}
				}
				
				for(InsPatrolDataVO luDun:lst){
					Proximity2DResult proximity = GeometryEngine.getNearestCoordinate(luDun.getShape(),gps, true);//GeometryEngine.getNearestVertex(luDun.getShape(),gps);
					if (null == nearObj.getObjName()){
						nearObj.setObjId(luDun.getGuid());
						nearObj.setObjNum(luDun.getNum()==null?"":luDun.getNum());
						nearObj.setObjName(luDun.getName()==null?luDun.getFacType():luDun.getName());
						nearObj.setMinDistance(proximity.getDistance());				
						nearObj.setWktGeom(luDun.getGeometryStr());
						nearObj.setState(luDun.getState());
						nearObj.setCoordinate(proximity.getCoordinate());
						nearObj.setPreRunTrace(luDun.getRoadPreTrace());
						
					}else{
						if (proximity.getDistance() < nearObj.getMinDistance()){
							nearObj.setObjId(luDun.getGuid());
							nearObj.setObjNum(luDun.getNum()==null?"":luDun.getNum());
							nearObj.setObjName(luDun.getName());
							nearObj.setMinDistance(proximity.getDistance());		
							nearObj.setWktGeom(luDun.getGeometryStr());
							nearObj.setState(luDun.getState());
							nearObj.setCoordinate(proximity.getCoordinate());
							nearObj.setPreRunTrace(luDun.getRoadPreTrace());
						}
					}
				}
			}catch(Exception ex){
				;
			}
		}

		return nearObj;
	} 

	
	/**
	 * 通过当前位置点获取到半径范围内的签到点
	 * 
	 * @param locationInfo
	 * @return
	 */
	public static NearObject findNearQianDaoDian(
			LocationInfo locationInfo,Vector<InsPatrolDataVO> lstPatrolChkPnt) {
		List<InsPatrolDataVO> lst = new ArrayList<InsPatrolDataVO>();
/*		double minDistance=0.00;
		String nearLD="";*/
				
		NearObject nearObj = new NearObject();
		
		if (lstPatrolChkPnt == null){
			return null;
		}
		
		if (locationInfo != null){
			try{
				Point gps = new Point(locationInfo.getMapx(),locationInfo.getMapy());
			
				Geometry geo = GeometryEngine.buffer(gps,sr, GPS_BUF, Unit.create(LinearUnit.Code.METER));
	
				for (InsPatrolDataVO ins : lstPatrolChkPnt){
					
					Geometry geoLuDan = GeometryEngine.buffer(ins.getShape(),sr, ARRIVE_BUF, Unit.create(LinearUnit.Code.METER));
					
					if (GeometryEngine.crosses(geo, geoLuDan, sr) || GeometryEngine.contains(geo, geoLuDan, sr) || GeometryEngine.touches(geo,geoLuDan, sr) || !GeometryEngine.intersect(geo, geoLuDan, sr).isEmpty()){
						lst.add(ins);
					}
				}
				
				for(InsPatrolDataVO chkPnt:lst){
					Proximity2DResult proximity = GeometryEngine.getNearestCoordinate(chkPnt.getShape(),gps, true);//GeometryEngine.getNearestVertex(chkPnt.getShape(),gps);
					if (null == nearObj.getObjName()){
						nearObj.setObjId(chkPnt.getGuid());
						nearObj.setObjNum(chkPnt.getNum()==null?"":chkPnt.getNum());
						nearObj.setObjName(chkPnt.getName());
						nearObj.setMinDistance(proximity.getDistance());		
						nearObj.setWktGeom(chkPnt.getGeometryStr());
					}else{
						if (proximity.getDistance() < nearObj.getMinDistance()){
							nearObj.setObjId(chkPnt.getGuid());
							nearObj.setObjNum(chkPnt.getNum()==null?"":chkPnt.getNum());
							nearObj.setObjName(chkPnt.getName());
							nearObj.setMinDistance(proximity.getDistance());	
							nearObj.setWktGeom(chkPnt.getGeometryStr());
						}
					}
				}
			}catch(Exception ex){
				;
			}
		}

		//保存最近对象的当前地图位置
		nearObj.setMapXY(locationInfo.getCurMapPosition());
		//保存最近对象的当前地图位置的位置时间
		nearObj.setDateTime(locationInfo.getTime());
		return nearObj;
	}


	/**
	 * 通过当前位置点所在路段
	 * 
	 * @param locationInfo
	 * @return
	 */
	public static NearObject findNearSite(LocationInfo locationInfo,Vector<InsSiteManage> lstPatrolData) {
		List<InsSiteManage> lst = new ArrayList<InsSiteManage>();
/*		double minDistance=0.00;
		String nearLD="";*/
				
		NearObject nearObj = new NearObject();
		
		if (lstPatrolData == null){
			return null;
		}
		
		if (locationInfo != null){
			try{
				Point gps = new Point(locationInfo.getMapx(),locationInfo.getMapy());
			
				Geometry geo = GeometryEngine.buffer(gps,sr, GPS_BUF, Unit.create(LinearUnit.Code.METER));
	
				for (InsSiteManage ins : lstPatrolData){
					//考虑到工地本身为面，所以缓冲范围可以定义得比较小，暂定为1m
					Geometry geoLuDan = GeometryEngine.buffer(ins.getShape(),sr, 1, Unit.create(LinearUnit.Code.METER));
					
					if (GeometryEngine.crosses(geo, geoLuDan, sr) || GeometryEngine.contains(geoLuDan, geo, sr) || GeometryEngine.touches(geo,geoLuDan, sr) || !GeometryEngine.intersect(geo, geoLuDan, sr).isEmpty()){
						lst.add(ins);
					}
				}
				
				for(InsSiteManage site:lst){
					Proximity2DResult proximity = GeometryEngine.getNearestCoordinate(site.getShape(),gps, true);// GeometryEngine.getNearestVertex(site.getShape(),gps);
					if (null == nearObj.getObjName()){
						nearObj.setObjId(site.getId());
						nearObj.setObjNum(site.getProjectNum()==null?"":site.getProjectNum());
						nearObj.setObjName(site.getProjectName());
						nearObj.setMinDistance(proximity.getDistance());		
						nearObj.setWktGeom(site.getPrjBound());
					}else{
						if (proximity.getDistance() < nearObj.getMinDistance()){
							nearObj.setObjId(site.getId());
							nearObj.setObjNum(site.getProjectNum()==null?"":site.getProjectNum());
							nearObj.setObjName(site.getProjectName());
							nearObj.setMinDistance(proximity.getDistance());	
							nearObj.setWktGeom(site.getPrjBound());
						}
					}
				}
			}catch(Exception ex){
				;
			}
		}

		//保存最近对象的当前地图位置
		nearObj.setMapXY(locationInfo.getCurMapPosition());
		//保存最近对象的当前地图位置的位置时间
		nearObj.setDateTime(locationInfo.getTime());
		return nearObj;
	} 
	public static NearObject findNearArea(LocationInfo locationInfo,Vector<InsPatrolAreaData> lstPatrolData) {
		List<InsPatrolAreaData> lst = new ArrayList<InsPatrolAreaData>();
/*		double minDistance=0.00;
		String nearLD="";*/
				
		NearObject nearObj = new NearObject();
		
		if (lstPatrolData == null){
			return null;
		}
		
		if (locationInfo != null){
			try{
				Point gps = new Point(locationInfo.getMapx(),locationInfo.getMapy());
			
				Geometry geo = GeometryEngine.buffer(gps,sr, GPS_BUF, Unit.create(LinearUnit.Code.METER));
	
				for (InsPatrolAreaData ins : lstPatrolData){
					//考虑到工地本身为面，所以缓冲范围可以定义得比较小，暂定为1m
					Geometry geoLuDan = GeometryEngine.buffer(ins.getShape(),sr, 1, Unit.create(LinearUnit.Code.METER));
					
					if (GeometryEngine.crosses(geo, geoLuDan, sr) || GeometryEngine.contains(geoLuDan, geo, sr) || GeometryEngine.touches(geo,geoLuDan, sr) || !GeometryEngine.intersect(geo, geoLuDan, sr).isEmpty()){
						lst.add(ins);
					}
				}
				
				for(InsPatrolAreaData site:lst){
					Proximity2DResult proximity = GeometryEngine.getNearestCoordinate(site.getShape(),gps, true);// GeometryEngine.getNearestVertex(site.getShape(),gps);
					if (null == nearObj.getObjName()){
						nearObj.setObjId(site.getId()+"");
						nearObj.setObjNum(site.getAreaNum()==null?"":site.getAreaNum());
						nearObj.setObjName(site.getAreaName());
						nearObj.setMinDistance(proximity.getDistance());		
						nearObj.setWktGeom(site.getGeometryStr());
					}else{
						if (proximity.getDistance() < nearObj.getMinDistance()){
							nearObj.setObjId(site.getId()+"");
							nearObj.setObjNum(site.getAreaNum()==null?"":site.getAreaNum());
							nearObj.setObjName(site.getAreaName());
							nearObj.setMinDistance(proximity.getDistance());	
							nearObj.setWktGeom(site.getGeometryStr());
						}
					}
				}
			}catch(Exception ex){
				;
			}
		}

		//保存最近对象的当前地图位置
		nearObj.setMapXY(locationInfo.getCurMapPosition());
		//保存最近对象的当前地图位置的位置时间
		nearObj.setDateTime(locationInfo.getTime());
		return nearObj;
	} 
	/**
	 * 通过当前位置点所在路段
	 * 
	 * @param locationInfo
	 * @return
	 */
	public static NearObject findNearKeypnt(LocationInfo locationInfo,Vector<InsKeyPointPatrolData> lstPatrolData) {
		List<InsKeyPointPatrolData> lst = new ArrayList<InsKeyPointPatrolData>();
/*		double minDistance=0.00;
		String nearLD="";*/
				
		NearObject nearObj = new NearObject();
		
		if (lstPatrolData == null){
			return null;
		}
		
		if (locationInfo != null){
			try{
				Point gps = new Point(locationInfo.getMapx(),locationInfo.getMapy());
			
				Geometry geo = GeometryEngine.buffer(gps,sr, GPS_BUF, Unit.create(LinearUnit.Code.METER));
	
				for (InsKeyPointPatrolData ins : lstPatrolData){
					
					Geometry geoLuDan = GeometryEngine.buffer(ins.getShape(),sr, ROAD_BUF, Unit.create(LinearUnit.Code.METER));
					
					if (GeometryEngine.crosses(geo, geoLuDan, sr) || GeometryEngine.contains(geoLuDan, geo, sr) || GeometryEngine.touches(geo,geoLuDan, sr) || !GeometryEngine.intersect(geo, geoLuDan, sr).isEmpty()){
						lst.add(ins);
					}
				}
				
				for(InsKeyPointPatrolData site:lst){
					Proximity2DResult proximity = GeometryEngine.getNearestCoordinate(site.getShape(),gps, true);//GeometryEngine.getNearestVertex(site.getShape(),gps);
					if (null == nearObj.getObjName()){
						nearObj.setObjId(site.getId());
						nearObj.setObjNum(site.getArchiveNum()==null?"":site.getArchiveNum());
						nearObj.setObjName(site.getKpaName());
						nearObj.setMinDistance(proximity.getDistance());		
						nearObj.setWktGeom(site.getKpaPosition());
					}else{
						if (proximity.getDistance() < nearObj.getMinDistance()){
							nearObj.setObjId(site.getId());
							nearObj.setObjNum(site.getArchiveNum()==null?"":site.getArchiveNum());
							nearObj.setObjName(site.getKpaName());
							nearObj.setMinDistance(proximity.getDistance());	
							nearObj.setWktGeom(site.getKpaPosition());
						}
					}
				}
			}catch(Exception ex){
				;
			}
		}

		//保存最近对象的当前地图位置
		nearObj.setMapXY(locationInfo.getCurMapPosition());
		//保存最近对象的当前地图位置的位置时间
		nearObj.setDateTime(locationInfo.getTime());
		return nearObj;
	} 
	public static NearObject findNearValve(LocationInfo locationInfo,Vector<InsPatrolDataVO> lstPatrolData) {
		List<InsPatrolDataVO> lst = new ArrayList<InsPatrolDataVO>();
/*		double minDistance=0.00;
		String nearLD="";*/
				
		NearObject nearObj = new NearObject();
		
		if (lstPatrolData == null){
			return null;
		}
		
		if (locationInfo != null){
			try{
				Point gps = new Point(locationInfo.getMapx(),locationInfo.getMapy());
			
				Geometry geo = GeometryEngine.buffer(gps,sr, GPS_BUF, Unit.create(LinearUnit.Code.METER));
	
				for (InsPatrolDataVO ins : lstPatrolData){
					
					Geometry geoLuDan = GeometryEngine.buffer(ins.getShape(),sr, ROAD_BUF, Unit.create(LinearUnit.Code.METER));
					
					if (GeometryEngine.crosses(geo, geoLuDan, sr) || GeometryEngine.contains(geoLuDan, geo, sr) || GeometryEngine.touches(geo,geoLuDan, sr) || !GeometryEngine.intersect(geo, geoLuDan, sr).isEmpty()){
						lst.add(ins);
					}
				}
				
				for(InsPatrolDataVO luDun:lst){
					Proximity2DResult proximity = GeometryEngine.getNearestCoordinate(luDun.getShape(),gps, true);//GeometryEngine.getNearestVertex(luDun.getShape(),gps);
					if (null == nearObj.getObjName()){
						nearObj.setObjId(luDun.getGuid());
						nearObj.setObjNum(luDun.getNum()==null?"":luDun.getNum());
						nearObj.setObjName(luDun.getName()+luDun.getFacType());
						nearObj.setMinDistance(proximity.getDistance());		
						nearObj.setWktGeom(luDun.getGeometryStr());
					}else{
						if (proximity.getDistance() < nearObj.getMinDistance()){
							nearObj.setObjId(luDun.getGuid());
							nearObj.setObjNum(luDun.getNum()==null?"":luDun.getNum());
							nearObj.setObjName(luDun.getName());
							nearObj.setMinDistance(proximity.getDistance());	
							nearObj.setWktGeom(luDun.getGeometryStr());
						}
					}
				}
			}catch(Exception ex){
				;
			}
		}

		//保存最近对象的当前地图位置
		nearObj.setMapXY(locationInfo.getCurMapPosition());
		//保存最近对象的当前地图位置的位置时间
		nearObj.setDateTime(locationInfo.getTime());
		return nearObj;
	} 
	public static NearObject findNearBsSupervisionPoint(LocationInfo locationInfo,Vector<BsSupervisionPoint> lstPatrolData) {
		List<BsSupervisionPoint> lst = new ArrayList<BsSupervisionPoint>();
/*		double minDistance=0.00;
		String nearLD="";*/
				
		NearObject nearObj = new NearObject();
		
		if (lstPatrolData == null){
			return null;
		}
		
		if (locationInfo != null){
			try{
				Point gps = new Point(locationInfo.getMapx(),locationInfo.getMapy());
			
				Geometry geo = GeometryEngine.buffer(gps,sr, GPS_BUF, Unit.create(LinearUnit.Code.METER));
	
				for (BsSupervisionPoint ins : lstPatrolData){
					
					Geometry geoLuDan = GeometryEngine.buffer(ins.getShape(),sr, ROAD_BUF, Unit.create(LinearUnit.Code.METER));
					
					if (GeometryEngine.crosses(geo, geoLuDan, sr) || GeometryEngine.contains(geoLuDan, geo, sr) || GeometryEngine.touches(geo,geoLuDan, sr) || !GeometryEngine.intersect(geo, geoLuDan, sr).isEmpty()){
						lst.add(ins);
					}
				}
				
				for(BsSupervisionPoint luDun:lst){
					Proximity2DResult proximity = GeometryEngine.getNearestCoordinate(luDun.getShape(),gps, true);//GeometryEngine.getNearestVertex(luDun.getShape(),gps);
					if (null == nearObj.getObjName()){
						nearObj.setObjId(luDun.getSpId()+"");
						nearObj.setObjNum(luDun.getSpNum()==null?"":luDun.getSpNum());
						nearObj.setObjName(luDun.getSpName()+luDun.getSpType());
						nearObj.setMinDistance(proximity.getDistance());		
						nearObj.setWktGeom(luDun.getSpCoordinate());
					}else{
						if (proximity.getDistance() < nearObj.getMinDistance()){
							nearObj.setObjId(luDun.getSpId()+"");
							nearObj.setObjNum(luDun.getSpNum()==null?"":luDun.getSpNum());
							nearObj.setObjName(luDun.getSpName());
							nearObj.setMinDistance(proximity.getDistance());	
							nearObj.setWktGeom(luDun.getSpCoordinate());
						}
					}
				}
			}catch(Exception ex){
				;
			}
		}

		//保存最近对象的当前地图位置
		nearObj.setMapXY(locationInfo.getCurMapPosition());
		//保存最近对象的当前地图位置的位置时间
		nearObj.setDateTime(locationInfo.getTime());
		return nearObj;
	} 
	public static NearObject findNearBsRoutineInsArea(LocationInfo locationInfo,Vector<BsRoutineInsArea> lstPatrolData) {
		List<BsRoutineInsArea> lst = new ArrayList<BsRoutineInsArea>();
/*		double minDistance=0.00;
		String nearLD="";*/
				
		NearObject nearObj = new NearObject();
		
		if (lstPatrolData == null){
			return null;
		}
		
		if (locationInfo != null){
			try{
				Point gps = new Point(locationInfo.getMapx(),locationInfo.getMapy());
			
				Geometry geo = GeometryEngine.buffer(gps,sr, GPS_BUF, Unit.create(LinearUnit.Code.METER));
	
				for (BsRoutineInsArea ins : lstPatrolData){
					
					Geometry geoLuDan = GeometryEngine.buffer(ins.getShape(),sr, ROAD_BUF, Unit.create(LinearUnit.Code.METER));
					
					if (GeometryEngine.crosses(geo, geoLuDan, sr) || GeometryEngine.contains(geoLuDan, geo, sr) || GeometryEngine.touches(geo,geoLuDan, sr) || !GeometryEngine.intersect(geo, geoLuDan, sr).isEmpty()){
						lst.add(ins);
					}
				}
				
				for(BsRoutineInsArea luDun:lst){
					Proximity2DResult proximity = GeometryEngine.getNearestCoordinate(luDun.getShape(),gps, true);//GeometryEngine.getNearestVertex(luDun.getShape(),gps);
					if (null == nearObj.getObjName()){
						nearObj.setObjId(luDun.getRiaId()+"");
						nearObj.setObjNum(luDun.getAreaNum()==null?"":luDun.getAreaNum());
						nearObj.setObjName(luDun.getAreaName());
						nearObj.setMinDistance(proximity.getDistance());		
						nearObj.setWktGeom(luDun.getRiaCoordinate());
					}else{
						if (proximity.getDistance() < nearObj.getMinDistance()){
							nearObj.setObjId(luDun.getRiaId()+"");
							nearObj.setObjNum(luDun.getAreaNum()==null?"":luDun.getAreaNum());
							nearObj.setObjName(luDun.getAreaName());
							nearObj.setMinDistance(proximity.getDistance());	
							nearObj.setWktGeom(luDun.getRiaCoordinate());
						}
					}
				}
			}catch(Exception ex){
				;
			}
		}

		//保存最近对象的当前地图位置
		nearObj.setMapXY(locationInfo.getCurMapPosition());
		//保存最近对象的当前地图位置的位置时间
		nearObj.setDateTime(locationInfo.getTime());
		return nearObj;
	} 
	public static NearObject findNearBsLeakInsArea(LocationInfo locationInfo,Vector<BsLeakInsArea> lstPatrolData) {
		List<BsLeakInsArea> lst = new ArrayList<BsLeakInsArea>();
/*		double minDistance=0.00;
		String nearLD="";*/
				
		NearObject nearObj = new NearObject();
		
		if (lstPatrolData == null){
			return null;
		}
		
		if (locationInfo != null){
			try{
				Point gps = new Point(locationInfo.getMapx(),locationInfo.getMapy());
			
				Geometry geo = GeometryEngine.buffer(gps,sr, GPS_BUF, Unit.create(LinearUnit.Code.METER));
	
				for (BsLeakInsArea ins : lstPatrolData){
					
					Geometry geoLuDan = GeometryEngine.buffer(ins.getShape(),sr, ROAD_BUF, Unit.create(LinearUnit.Code.METER));
					
					if (GeometryEngine.crosses(geo, geoLuDan, sr) || GeometryEngine.contains(geoLuDan, geo, sr) || GeometryEngine.touches(geo,geoLuDan, sr) || !GeometryEngine.intersect(geo, geoLuDan, sr).isEmpty()){
						lst.add(ins);
					}
				}
				
				for(BsLeakInsArea luDun:lst){
					Proximity2DResult proximity = GeometryEngine.getNearestCoordinate(luDun.getShape(),gps, true);//GeometryEngine.getNearestVertex(luDun.getShape(),gps);
					if (null == nearObj.getObjName()){
						nearObj.setObjId(luDun.getLiaId()+"");
						nearObj.setObjNum(luDun.getAreaNum()==null?"":luDun.getAreaNum());
						nearObj.setObjName(luDun.getAreaName());
						nearObj.setMinDistance(proximity.getDistance());		
						nearObj.setWktGeom(luDun.getLiaCoordinate());
					}else{
						if (proximity.getDistance() < nearObj.getMinDistance()){
							nearObj.setObjId(luDun.getLiaId()+"");
							nearObj.setObjNum(luDun.getAreaNum()==null?"":luDun.getAreaNum());
							nearObj.setObjName(luDun.getAreaName());
							nearObj.setMinDistance(proximity.getDistance());	
							nearObj.setWktGeom(luDun.getLiaCoordinate());
						}
					}
				}
			}catch(Exception ex){
				;
			}
		}

		//保存最近对象的当前地图位置
		nearObj.setMapXY(locationInfo.getCurMapPosition());
		//保存最近对象的当前地图位置的位置时间
		nearObj.setDateTime(locationInfo.getTime());
		return nearObj;
	} 
	public static NearObject findNearBsInsFacInfo(LocationInfo locationInfo,Vector<BsInsFacInfo> lstPatrolData) {
		List<BsInsFacInfo> lst = new ArrayList<BsInsFacInfo>();
/*		double minDistance=0.00;
		String nearLD="";*/
				
		NearObject nearObj = new NearObject();
		
		if (lstPatrolData == null){
			return null;
		}
		
		if (locationInfo != null){
			try{
				Point gps = new Point(locationInfo.getMapx(),locationInfo.getMapy());
			
				Geometry geo = GeometryEngine.buffer(gps,sr, GPS_BUF, Unit.create(LinearUnit.Code.METER));
	
				for (BsInsFacInfo ins : lstPatrolData){
					
					Geometry geoLuDan = GeometryEngine.buffer(ins.getShape(),sr, ROAD_BUF, Unit.create(LinearUnit.Code.METER));
					
					if (GeometryEngine.crosses(geo, geoLuDan, sr) || GeometryEngine.contains(geoLuDan, geo, sr) || GeometryEngine.touches(geo,geoLuDan, sr) || !GeometryEngine.intersect(geo, geoLuDan, sr).isEmpty()){
						lst.add(ins);
					}
				}
				
				for(BsInsFacInfo luDun:lst){
					Proximity2DResult proximity = GeometryEngine.getNearestCoordinate(luDun.getShape(),gps, true);//GeometryEngine.getNearestVertex(luDun.getShape(),gps);
					if (null == nearObj.getObjName()){
						nearObj.setObjId(luDun.getIfiId()+"");
						nearObj.setObjNum(luDun.getIfiFacNum()==null?"":luDun.getIfiFacNum());
						nearObj.setObjName(luDun.getIfiFacType());
						nearObj.setMinDistance(proximity.getDistance());		
						nearObj.setWktGeom(luDun.getIfiFacCoordinate());
					}else{
						if (proximity.getDistance() < nearObj.getMinDistance()){
							nearObj.setObjId(luDun.getIfiId()+"");
							nearObj.setObjNum(luDun.getIfiFacNum()==null?"":luDun.getIfiFacNum());
							nearObj.setObjName(luDun.getIfiFacType());
							nearObj.setMinDistance(proximity.getDistance());	
							nearObj.setWktGeom(luDun.getIfiFacCoordinate());
						}
					}
				}
			}catch(Exception ex){
				;
			}
		}

		//保存最近对象的当前地图位置
		nearObj.setMapXY(locationInfo.getCurMapPosition());
		//保存最近对象的当前地图位置的位置时间
		nearObj.setDateTime(locationInfo.getTime());
		return nearObj;
	} 
	public static NearObject findNearBsEmphasisInsArea(LocationInfo locationInfo,Vector<BsEmphasisInsArea> lstPatrolData) {
		List<BsEmphasisInsArea> lst = new ArrayList<BsEmphasisInsArea>();
/*		double minDistance=0.00;
		String nearLD="";*/
				
		NearObject nearObj = new NearObject();
		
		if (lstPatrolData == null){
			return null;
		}
		
		if (locationInfo != null){
			try{
				Point gps = new Point(locationInfo.getMapx(),locationInfo.getMapy());
			
				Geometry geo = GeometryEngine.buffer(gps,sr, GPS_BUF, Unit.create(LinearUnit.Code.METER));
	
				for (BsEmphasisInsArea ins : lstPatrolData){
					
					Geometry geoLuDan = GeometryEngine.buffer(ins.getShape(),sr, ROAD_BUF, Unit.create(LinearUnit.Code.METER));
					
					if (GeometryEngine.crosses(geo, geoLuDan, sr) || GeometryEngine.contains(geoLuDan, geo, sr) || GeometryEngine.touches(geo,geoLuDan, sr) || !GeometryEngine.intersect(geo, geoLuDan, sr).isEmpty()){
						lst.add(ins);
					}
				}
				
				for(BsEmphasisInsArea luDun:lst){
					Proximity2DResult proximity = GeometryEngine.getNearestCoordinate(luDun.getShape(),gps, true);//GeometryEngine.getNearestVertex(luDun.getShape(),gps);
					if (null == nearObj.getObjName()){
						nearObj.setObjId(luDun.getEiaId()+"");
						nearObj.setObjNum(luDun.getAreaNum()==null?"":luDun.getAreaNum());
						nearObj.setObjName(luDun.getAreaName());
						nearObj.setMinDistance(proximity.getDistance());		
						nearObj.setWktGeom(luDun.getEiaCoordinate());
					}else{
						if (proximity.getDistance() < nearObj.getMinDistance()){
							nearObj.setObjId(luDun.getEiaId()+"");
							nearObj.setObjNum(luDun.getAreaNum()==null?"":luDun.getAreaNum());
							nearObj.setObjName(luDun.getAreaName());
							nearObj.setMinDistance(proximity.getDistance());	
							nearObj.setWktGeom(luDun.getEiaCoordinate());
						}
					}
				}
			}catch(Exception ex){
				;
			}
		}

		//保存最近对象的当前地图位置
		nearObj.setMapXY(locationInfo.getCurMapPosition());
		//保存最近对象的当前地图位置的位置时间
		nearObj.setDateTime(locationInfo.getTime());
		return nearObj;
	}

	public static NearObject findNearLuDuan(LocationInfoExt locationInfo,
			Vector<InsCheckFacRoad> lstPatrolData) {
		// TODO Auto-generated method stub
		List<InsCheckFacRoad> lst = new ArrayList<InsCheckFacRoad>();
		/*		double minDistance=0.00;
				String nearLD="";*/
						
				NearObject nearObj = new NearObject();
				
				if (lstPatrolData == null){
					return null;
				}
				
				if (locationInfo != null){
					//保存最近对象的当前地图位置
					nearObj.setMapXY(locationInfo.getCurMapPosition());
					//保存最近对象的当前地图位置的位置时间
					nearObj.setDateTime(null==locationInfo.getTime()?MyDateTools.getCurDateTime():locationInfo.getTime());
					
					try{
						Point gps = new Point(locationInfo.getMapx(),locationInfo.getMapy());
					
						Geometry geo = GeometryEngine.buffer(gps,sr, GPS_BUF, Unit.create(LinearUnit.Code.METER));
			
						for (InsCheckFacRoad ins : lstPatrolData){
							
							Geometry geoLuDan = GeometryEngine.buffer(ins.getShape(),sr, ROAD_BUF, Unit.create(LinearUnit.Code.METER));
							
							if (GeometryEngine.crosses(geo, geoLuDan, sr) || GeometryEngine.contains(geoLuDan, geo, sr) || GeometryEngine.touches(geo,geoLuDan, sr) || !GeometryEngine.intersect(geo, geoLuDan, sr).isEmpty()){
								lst.add(ins);
							}
						}
						
						for(InsCheckFacRoad luDun:lst){
							Proximity2DResult proximity = GeometryEngine.getNearestCoordinate(luDun.getShape(),gps, true);//GeometryEngine.getNearestVertex(luDun.getShape(),gps);
							if (null == nearObj.getObjName()){
								nearObj.setObjId(luDun.getGuid());
								nearObj.setObjNum(luDun.getFacNum()==null?"":luDun.getFacNum());
								nearObj.setObjName(luDun.getFacNum()==null?"此设施":luDun.getFacNum());
								nearObj.setMinDistance(proximity.getDistance());				
								nearObj.setWktGeom(luDun.getShapeStr());
								nearObj.setState(String.valueOf(luDun.getState()));
								nearObj.setCoordinate(proximity.getCoordinate());
								nearObj.setPreRunTrace(luDun.getRoadPreTrace());
								
							}else{
								if (proximity.getDistance() < nearObj.getMinDistance()){
									nearObj.setObjId(luDun.getGuid());
									nearObj.setObjNum(luDun.getFacNum()==null?"":luDun.getFacNum());
									nearObj.setObjName(luDun.getFacNum()==null?"此设施":luDun.getFacNum());
									nearObj.setMinDistance(proximity.getDistance());		
									nearObj.setWktGeom(luDun.getShapeStr());
									nearObj.setState(String.valueOf(luDun.getState()));
									nearObj.setCoordinate(proximity.getCoordinate());
									nearObj.setPreRunTrace(luDun.getRoadPreTrace());
								}
							}
						}
					}catch(Exception ex){
						ex.printStackTrace();
						;
					}
				}

				return nearObj;
	} 
	
	
	public static NearObject findNearPoint(LocationInfoExt locationInfo,
			Vector<InsCheckFacRoad> lstPatrolData) {
		// TODO Auto-generated method stub
		List<InsCheckFacRoad> lst = new ArrayList<InsCheckFacRoad>();
		/*		double minDistance=0.00;
				String nearLD="";*/
						
				NearObject nearObj = new NearObject();
				
				if (lstPatrolData == null){
					return null;
				}
				
				if (locationInfo != null){
					//保存最近对象的当前地图位置
					nearObj.setMapXY(locationInfo.getCurMapPosition());
					//保存最近对象的当前地图位置的位置时间
					nearObj.setDateTime(null==locationInfo.getTime()?MyDateTools.getCurDateTime():locationInfo.getTime());
					
					try{
						Point gps = new Point(locationInfo.getMapx(),locationInfo.getMapy());
					
						Geometry geo = GeometryEngine.buffer(gps,sr, GPS_BUF, Unit.create(LinearUnit.Code.METER));
			
						for (InsCheckFacRoad ins : lstPatrolData){
							
							Geometry geoLuDan = GeometryEngine.buffer(ins.getShape(),sr, ROAD_BUF, Unit.create(LinearUnit.Code.METER));
							
							if (GeometryEngine.crosses(geo, geoLuDan, sr) || GeometryEngine.contains(geoLuDan, geo, sr) || GeometryEngine.touches(geo,geoLuDan, sr) || !GeometryEngine.intersect(geo, geoLuDan, sr).isEmpty()){
								lst.add(ins);
							}
						}
						
						for(InsCheckFacRoad luDun:lst){
							Proximity2DResult proximity = GeometryEngine.getNearestCoordinate(luDun.getShape(),gps, true);//GeometryEngine.getNearestVertex(luDun.getShape(),gps);
							if (null == nearObj.getObjName()){
								nearObj.setObjId(luDun.getGuid());
								nearObj.setObjNum(luDun.getFacNum()==null?"":luDun.getFacNum());
								nearObj.setObjName(luDun.getFacType());
								nearObj.setMinDistance(proximity.getDistance());				
								nearObj.setWktGeom(luDun.getShapeStr());
								nearObj.setState(String.valueOf(luDun.getState()));
								nearObj.setCoordinate(proximity.getCoordinate());
								nearObj.setPreRunTrace(luDun.getRoadPreTrace());
								
							}else{
								if (proximity.getDistance() < nearObj.getMinDistance()){
									nearObj.setObjId(luDun.getGuid());
									nearObj.setObjNum(luDun.getFacNum()==null?"":luDun.getFacNum());
									nearObj.setObjName(luDun.getFacType());
									nearObj.setMinDistance(proximity.getDistance());		
									nearObj.setWktGeom(luDun.getShapeStr());
									nearObj.setState(String.valueOf(luDun.getState()));
									nearObj.setCoordinate(proximity.getCoordinate());
									nearObj.setPreRunTrace(luDun.getRoadPreTrace());
								}
							}
						}
					}catch(Exception ex){
						;
					}
				}

				return nearObj;
	}
}
