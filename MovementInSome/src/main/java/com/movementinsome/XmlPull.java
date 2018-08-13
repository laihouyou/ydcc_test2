package com.movementinsome;

import com.esri.core.geometry.Envelope;
import com.esri.core.map.ImageServiceParameters.IMAGE_FORMAT;
import com.movementinsome.database.vo.CoordTransformModel;
import com.movementinsome.database.vo.MapmenuVO;
import com.movementinsome.database.vo.MapparentmenuVO;
import com.movementinsome.database.vo.MapparentmenusVO;
import com.movementinsome.kernel.initial.model.App;
import com.movementinsome.kernel.initial.model.Attribute;
import com.movementinsome.kernel.initial.model.AutoStart;
import com.movementinsome.kernel.initial.model.BaseMap;
import com.movementinsome.kernel.initial.model.City;
import com.movementinsome.kernel.initial.model.CityList;
import com.movementinsome.kernel.initial.model.CommuService;
import com.movementinsome.kernel.initial.model.Configuration;
import com.movementinsome.kernel.initial.model.CoordParam;
import com.movementinsome.kernel.initial.model.Export;
import com.movementinsome.kernel.initial.model.ExtAnaService;
import com.movementinsome.kernel.initial.model.Facility;
import com.movementinsome.kernel.initial.model.FacilityType;
import com.movementinsome.kernel.initial.model.Field;
import com.movementinsome.kernel.initial.model.FileService;
import com.movementinsome.kernel.initial.model.Ftlayer;
import com.movementinsome.kernel.initial.model.LAYERTYPE;
import com.movementinsome.kernel.initial.model.Locate;
import com.movementinsome.kernel.initial.model.Login;
import com.movementinsome.kernel.initial.model.LoginTitle;
import com.movementinsome.kernel.initial.model.MapFacility;
import com.movementinsome.kernel.initial.model.MapParam;
import com.movementinsome.kernel.initial.model.Mapservice;
import com.movementinsome.kernel.initial.model.MenuClassify;
import com.movementinsome.kernel.initial.model.Module;
import com.movementinsome.kernel.initial.model.Movetype;
import com.movementinsome.kernel.initial.model.Navigation;
import com.movementinsome.kernel.initial.model.Number;
import com.movementinsome.kernel.initial.model.Project;
import com.movementinsome.kernel.initial.model.ProjectType;
import com.movementinsome.kernel.initial.model.Push;
import com.movementinsome.kernel.initial.model.Range;
import com.movementinsome.kernel.initial.model.ReportHistory;
import com.movementinsome.kernel.initial.model.SendNewTask;
import com.movementinsome.kernel.initial.model.Setting;
import com.movementinsome.kernel.initial.model.Solution;
import com.movementinsome.kernel.initial.model.Sub;
import com.movementinsome.kernel.initial.model.Table;
import com.movementinsome.kernel.initial.model.TableField;
import com.movementinsome.kernel.initial.model.Task;
import com.movementinsome.kernel.initial.model.UserRight;
import com.movementinsome.kernel.initial.model.View;
import com.movementinsome.kernel.initial.model.Views;
import com.movementinsome.kernel.initial.model.WorkorderManagement;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XmlPull {

	public Solution readSolutionConfig(InputStream inStream) {
		Solution solution = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();//解析文体
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();//解析到这个工具类

			try {

				Document document = builder.parse(inStream);
				Element element = document.getDocumentElement();
				solution = new Solution();
				solution.setDefatult(Integer.parseInt(element
						.getAttribute("default")));

				NodeList subNode = document.getDocumentElement()
						.getChildNodes(); // 获得根结点
				for (int i = 0; i < subNode.getLength(); i++) {
					if (subNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
						Element childElement = (Element) subNode.item(i);

						Sub sub = new Sub();
						sub.setId(Integer.parseInt(childElement
								.getAttribute("id")));
						sub.setFile(childElement.getAttribute("file"));
						sub.setName(childElement.getAttribute("name"));
						sub.setGroupNum(childElement.getAttribute("groupNum"));
						solution.getSubs().add(sub);
					}
				}
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return solution;
	}

	public Configuration readConfig(InputStream inStream) {

		Configuration configuration = null;

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			try {
				Document document = builder.parse(inStream);

				Element element = document.getDocumentElement(); // 获得根结点
				configuration = new Configuration();
				// app设置读取
				App app = new App();
				if (element.getElementsByTagName("locate").getLength() !=0){
					NodeList locateNode = element.getElementsByTagName("locate"); // 根据app获得结点
					Locate locate = new Locate();
					for (int i = 0; i < locateNode.getLength(); i++) {
						Element childElement = (Element) locateNode.item(i);
						locate.setRate(Integer.parseInt(childElement
								.getAttribute("rate")));
						locate.setDistance(Integer.parseInt(childElement
								.getAttribute("distance")));
					}
					app.setLocate(locate);
				}

				if (element.getElementsByTagName("push").getLength() !=0){
					NodeList pushNode = element.getElementsByTagName("push"); // 根据app获得结点
					Push push = new Push();
					for (int i = 0; i < pushNode.getLength(); i++) {
						Element childElement = (Element) pushNode.item(i);
						push.setLocal(childElement.getAttribute("local"));
						push.setForeign(childElement.getAttribute("foreign"));
					}
					app.setPush(push);
				}
				if (element.getElementsByTagName("login").getLength() !=0){
					NodeList loginNode = element.getElementsByTagName("login"); // 根据app获得结点
					Login login =new Login();
					for (int i = 0; i < loginNode.getLength(); i++) {
						Element childElement = (Element) loginNode.item(i);
						LoginTitle loginTitle = new LoginTitle();
						loginTitle.setColor(childElement.getAttribute("titleColor"));
						loginTitle.setLogo(childElement.getAttribute("titleLogo"));
						loginTitle.setText(childElement.getAttribute("titleText"));
						login.setLoginTitle(loginTitle);
					}
					app.setLogin(login);
				}
				
				if (element.getElementsByTagName("commuservice").getLength()!= 0){
					NodeList commuServiceNode = element
							.getElementsByTagName("commuservice"); // 根据app获得结点
					CommuService commuService = new CommuService();
					for (int i = 0; i < commuServiceNode.getLength(); i++) {
						Element childElement = (Element) commuServiceNode.item(i);
						commuService.setLocal(childElement.getAttribute("local"));
						commuService.setForeign(childElement
								.getAttribute("foreign"));
					}
					app.setCommuService(commuService);
				}

				
				if (element.getElementsByTagName("fileservice").getLength()!= 0){
					NodeList fileServiceNode = element
							.getElementsByTagName("fileservice"); // 根据app获得结点
					
					FileService fileService = new FileService();
					for (int i = 0; i < fileServiceNode.getLength(); i++) {
						Element childElement = (Element) fileServiceNode.item(i);
						fileService.setLocal(childElement.getAttribute("local"));
						fileService.setForeign(childElement
								.getAttribute("foreign"));
						
						fileService.setCompress(childElement.getAttribute("compress").equals("")?false:childElement.getAttribute("compress").equalsIgnoreCase("true")?true:false);
						fileService.setType(childElement.getAttribute("type"));
						fileService.setConfig(childElement.getAttribute("config"));
						fileService.setCamreaMessage(childElement.getAttribute("camreaMessage"));
						fileService.setLabelLocation(childElement.getAttribute("labelLocation"));
					}
					app.setFileService(fileService);
				}
				if(element.getElementsByTagName("sendNewTask").getLength()!= 0){
					NodeList sendNewTaskNode = element
							.getElementsByTagName("sendNewTask"); // 根据app获得结点
					
					SendNewTask sendNewTask = new SendNewTask();
					for (int i = 0; i < sendNewTaskNode.getLength(); i++) {
						Element childElement = (Element) sendNewTaskNode.item(i);
						sendNewTask.setLocal(childElement.getAttribute("local"));
						sendNewTask.setForeign(childElement
								.getAttribute("foreign"));
					}
					app.setSendNewTask(sendNewTask);
				}
				if(element.getElementsByTagName("userRight").getLength()!= 0){
					NodeList userRightNode = element
							.getElementsByTagName("userRight"); // 根据app获得结点
					
					UserRight userRight = new UserRight();
					for (int i = 0; i < userRightNode.getLength(); i++) {
						Element childElement = (Element) userRightNode.item(i);
						userRight.setValve(childElement.getAttribute("valve"));
						userRight.setPipeline(childElement
								.getAttribute("pipeline"));
						userRight.setValvePipe(childElement
								.getAttribute("valvePipe"));
					}
					app.setUserRight(userRight);
				}
				
				if (element.getElementsByTagName("workorderManagement").getLength()!= 0){
					NodeList workorderManagementNode = element
							.getElementsByTagName("workorderManagement"); // 根据app获得结点
					WorkorderManagement workorderManagement =new WorkorderManagement();
					for (int i = 0; i < workorderManagementNode.getLength(); i++) {
						Element childElement = (Element) workorderManagementNode.item(i);
						workorderManagement.setForeign(childElement.getAttribute("foreign"));
						workorderManagement.setLocal(childElement.getAttribute("local"));
					}
					app.setWorkorderManagement(workorderManagement);
				}
				
				if (element.getElementsByTagName("autostart").getLength()!= 0){
					NodeList autoStartNode = element
							.getElementsByTagName("autostart"); // 根据app获得结点
					
					AutoStart autoStart = new AutoStart();
					for (int i = 0; i < autoStartNode.getLength(); i++) {
						Element childElement = (Element) autoStartNode.item(i);
						autoStart.setBoot(childElement.getAttribute("boot")!="0"?true:false);
						autoStart.setTiming(childElement.getAttribute("timing"));;
					}
					app.setAutoStart(autoStart);
				}

				//路线、设施导航提醒参数
				if (element.getElementsByTagName("nav").getLength()!= 0){
					NodeList navNode = element
							.getElementsByTagName("nav"); // 根据app获得结点
					
					Navigation nav = new Navigation();
					for (int i = 0; i < navNode.getLength(); i++) {
						Element childElement = (Element) navNode.item(i);
						if (!"".equals(childElement.getAttribute("gpsbuf")))
							nav.setGpsBuf(Integer.valueOf(childElement.getAttribute("gpsbuf"))); //gps缓冲范围，用于判断范围内的目标
						if (!"".equals(childElement.getAttribute("arrive")))
							nav.setArrive(Integer.valueOf(childElement.getAttribute("arrive"))); //用于确定是否进行目标提醒
						if (!"".equals(childElement.getAttribute("roadbuf")))
							nav.setRoadBuf(Integer.valueOf(childElement.getAttribute("roadbuf")));
						if (!"".equals(childElement.getAttribute("accuracy")))
							nav.setAccuracy(Integer.valueOf(childElement.getAttribute("accuracy")));
						if (!"".equals(childElement.getAttribute("twoway")))
							nav.setTwoWay(childElement.getAttribute("twoway")!="0"?true:false);
						if (!"".equals(childElement.getAttribute("tolerance")))
							nav.setTolerance(Double.valueOf(childElement.getAttribute("tolerance")));
					}
					app.setNavigation(nav);
				}
				
				configuration.setApp(app);
				// app读取结束
				// map读取开妈
				MapParam map = new MapParam();
				NodeList mapNode = element.getElementsByTagName("map");

				String[] extents = ((Element) mapNode.item(0)).getAttribute(
						"extent").split(",");
				double xmin = Double.parseDouble(extents[0]);
				double ymin = Double.parseDouble(extents[1]);
				double xmax = Double.parseDouble(extents[2]);
				double ymax = Double.parseDouble(extents[3]);
				map.setExtent(new Envelope(xmin, ymin, xmax, ymax));

				map.setSrid(((Element) mapNode.item(0)).getAttribute("srid")); // 标准坐标系统ID

				// 非标准坐标系统描述,新增
				map.setWkt(((Element) mapNode.item(0)).getAttribute("wkt"));
				//查询范围错误
				if ("".equals(((Element) mapNode.item(0)).getAttribute("tolerance"))){
					map.setTolerance(10);
				}else{
					map.setTolerance(Integer.parseInt(((Element) mapNode.item(0)).getAttribute("tolerance")));
				}

				//底图参数读取
				NodeList basemapNode = element.getElementsByTagName("basemap"); // 根据basemap获得结点
				if (basemapNode .getLength()!= 0){
					BaseMap baseMap = new BaseMap();
					baseMap.setDefault(Integer.parseInt(((Element) basemapNode
							.item(0)).getAttribute("default")));
					NodeList mapserviceNode = basemapNode.item(0).getChildNodes();
					for (int i = 0; i < mapserviceNode.getLength(); i++) {
						if (mapserviceNode.item(i).getNodeType() == Node.ELEMENT_NODE) { // 判断当前结点是否是元素结点
							Element serviceElement = (Element) mapserviceNode
									.item(i);
							Mapservice mapservice = new Mapservice();
							mapservice.setId(Integer.parseInt(serviceElement
									.getAttribute("id")));
							mapservice.setName(serviceElement.getAttribute("name"));
							mapservice.setLabel(serviceElement
									.getAttribute("label"));
							mapservice.setType(getLAYERTYPE(serviceElement
									.getAttribute("type")));
							mapservice.setVisible("true".equals(serviceElement
									.getAttribute("visible")));
							mapservice.setAlpha(Integer.parseInt(serviceElement
									.getAttribute("alpha")));
							mapservice.setFormat(getIMAGE_FORMAT(serviceElement
									.getAttribute("format")));
							mapservice.setLocal(serviceElement
									.getAttribute("local"));
							mapservice.setForeign(serviceElement
									.getAttribute("foreign"));
							baseMap.getMapservices().add(mapservice);
						}
					}
					map.setBaseMap(baseMap);
				}
				//业务地图服务读取
				if (element.getElementsByTagName("bizmap").getLength()!= 0){
					NodeList bizmapNode = (NodeList) element
							.getElementsByTagName("bizmap").item(0).getChildNodes(); // 根据bizmodule获得结点
					for (int i = 0; i < bizmapNode.getLength(); i++) {
						if (bizmapNode.item(i).getNodeType() == Node.ELEMENT_NODE) { // 判断当前结点是否是元素结点
							Element serviceElement = (Element) bizmapNode.item(i);
							Mapservice mapservice = new Mapservice();
							mapservice.setId(Integer.parseInt(serviceElement
									.getAttribute("id")));
							mapservice.setName(serviceElement.getAttribute("name"));
							mapservice.setLabel(serviceElement
									.getAttribute("label"));
							mapservice.setType(getLAYERTYPE(serviceElement
									.getAttribute("type")));
							mapservice.setVisible("true".equals(serviceElement
									.getAttribute("visible")));
							mapservice.setAlpha(Integer.parseInt(serviceElement
									.getAttribute("alpha")));
							mapservice.setFormat(getIMAGE_FORMAT(serviceElement
									.getAttribute("format")));
							mapservice.setLocal(serviceElement
									.getAttribute("local"));
							mapservice.setForeign(serviceElement
									.getAttribute("foreign"));
							map.getBizMap().add(mapservice);
						}
					}
				}

				if (element.getElementsByTagName("ftlayers") .getLength()!= 0){
					NodeList ftlayersNode = (NodeList) element
							.getElementsByTagName("ftlayers").item(0)
							.getChildNodes(); // 根据bizmodule获得结点
					for (int i = 0; i < ftlayersNode.getLength(); i++) {
						if (ftlayersNode.item(i).getNodeType() == Node.ELEMENT_NODE) { // 判断当前结点是否是元素结点
							Element layerElement = (Element) ftlayersNode.item(i);
							Ftlayer ftlayer = new Ftlayer();
							ftlayer.setId(Integer.parseInt(layerElement.getAttribute("id")));
	
							// 指定图层ID串layerids
							ftlayer.setLayerIds(layerElement.getAttribute("layerids"));
	
							ftlayer.setKeyfield(layerElement.getAttribute("keyfield"));
							
							ftlayer.setFeatureServerId(layerElement.getAttribute("featureServerId"));
							try{
								if(!"".equals(layerElement.getAttribute("relationshipId"))){
									ftlayer.setRelationshipId(Integer.parseInt(layerElement.getAttribute("relationshipId")));
								}
							}catch(Exception e){
								
							}
							if (!"".equals(layerElement.getAttribute("write"))){
								ftlayer.setWrite(("1".equals(layerElement.getAttribute("write")))?true:false);
							}else{
								ftlayer.setWrite(false);
							}
							
							ftlayer.setName(layerElement.getAttribute("name"));
							
							ftlayer.setTablename(layerElement.getAttribute("tablename"));
							
							ftlayer.setFields(getFields(layerElement.getAttribute("fields")));
							
							ftlayer.setRelationshipFields(getFields(layerElement.getAttribute("relationshipFields")));
							
							ftlayer.setMapservice(getMapservice(layerElement.getAttribute("mapservice"),map.getBizMap()));
							
							map.getFtlayers().add(ftlayer);
						}
					}
				}

				
/*				if (element.getElementsByTagName("navftlayers").getLength()!= 0){
					NodeList navftlayersNode = (NodeList) element
							.getElementsByTagName("navftlayers").item(0)
							.getChildNodes(); // 根据bizmodule获得结点
					for (int i = 0; i < navftlayersNode.getLength(); i++) {
						if (navftlayersNode.item(i).getNodeType() == Node.ELEMENT_NODE) { // 判断当前结点是否是元素结点
							Element layerElement = (Element) navftlayersNode.item(i);
							Ftlayer ftlayer = new Ftlayer();
							ftlayer.setId(Integer.parseInt(layerElement.getAttribute("id")));
	
							// 指定图层ID串layerids
							ftlayer.setLayerIds(layerElement.getAttribute("layerids"));
	
							ftlayer.setKeyfield(layerElement.getAttribute("keyfield"));
							
							if (!"".equals(layerElement.getAttribute("write"))){
								ftlayer.setWrite(("1".equals(layerElement.getAttribute("write")))?true:false);
							}else{
								ftlayer.setWrite(false);
							}
							
							ftlayer.setName(layerElement.getAttribute("name"));
							
							ftlayer.setTablename(layerElement.getAttribute("tablename"));
							
							ftlayer.setLocal(layerElement.getAttribute("local"));
							
							ftlayer.setForeign(layerElement.getAttribute("foreign"));
							
							ftlayer.setFields(getFields(layerElement.getAttribute("fields")));
							
							ftlayer.setMapservice(getMapservice(
									layerElement.getAttribute("mapservice"),
									map.getBizMap()));
	
							map.getNavFtlayers().add(ftlayer);
						}
					}
				}*/
				
				if (element.getElementsByTagName("tasks").getLength()!= 0){
					NodeList tasksNode = (NodeList) element
							.getElementsByTagName("tasks").item(0).getChildNodes(); // 根据bizmodule获得结点
					for (int i = 0; i < tasksNode.getLength(); i++) {
						if (tasksNode.item(i).getNodeType() == Node.ELEMENT_NODE) { // 判断当前结点是否是元素结点
							Element taskElement = (Element) tasksNode.item(i);
							Task task = new Task();
							task.setId(taskElement.getAttribute("id"));
							task.setLabel(taskElement.getAttribute("mapservice"));
							task.setName(taskElement.getAttribute("name"));
							task.setTolerance(Integer.parseInt(taskElement
									.getAttribute("tolerance")));
							task.setLayers(getFtlayers(map.getFtlayers(),
									taskElement.getAttribute("layers")));
							task.setForeign(taskElement.getAttribute("foreign"));
							task.setLocal(taskElement.getAttribute("local"));
							map.getTasks().add(task);
						}
					}
				}

				if (element.getElementsByTagName("extservices").getLength()!= 0){
					NodeList extAnasNode = (NodeList) element
							.getElementsByTagName("extservices").item(0)
							.getChildNodes(); // 根据bizmodule获得结点
					for (int i = 0; i < extAnasNode.getLength(); i++) {
						if (extAnasNode.item(i).getNodeType() == Node.ELEMENT_NODE) { // 判断当前结点是否是元素结点
							Element extAnaElement = (Element) extAnasNode.item(i);
							ExtAnaService extAna = new ExtAnaService();
							extAna.setId(extAnaElement.getAttribute("id"));
							extAna.setName(extAnaElement.getAttribute("name"));
							extAna.setLocal(extAnaElement.getAttribute("local"));
							extAna.setFoeign(extAnaElement.getAttribute("foreign"));
							map.getExtAnaServices().add(extAna);
						}
					}
				}
				NodeList coordNode = (NodeList) element
						.getElementsByTagName("coordTransform"); // 根据bizmodule获得结点
				if (coordNode .getLength()!= 0){
					CoordParam coordParam = new CoordParam();
	
					coordParam.setCoordinate(((Element) coordNode.item(0)).getAttribute("Coordinate"));
					
					//三参数获取
					if (!"".equals(((Element) coordNode.item(0)).getAttribute("FDx"))){
						coordParam.setFdx(Double.parseDouble(((Element) coordNode.item(0)).getAttribute("FDx")));
					}else{
						coordParam.setFdx(0.00);
					}
					if (!"".equals(((Element) coordNode.item(0)).getAttribute("FDy"))){
						coordParam.setFdy(Double.parseDouble(((Element) coordNode.item(0)).getAttribute("FDy")));
					}else{
						coordParam.setFdy(0.00);
					}
					if (!"".equals(((Element) coordNode.item(0)).getAttribute("FScale"))){
						coordParam.setFscale(Double.parseDouble(((Element) coordNode.item(0)).getAttribute("FScale")));
					}else{
						coordParam.setFscale(0.00);
					}
					if (!"".equals(((Element) coordNode.item(0)).getAttribute("FRotateangle"))){
						coordParam.setFrotateangle(Double.parseDouble(((Element) coordNode.item(0)).getAttribute("FRotateangle")));
					}else{
						coordParam.setFrotateangle(0.00);
					}
					//七参数获取
					if (!"".equals(((Element) coordNode.item(0)).getAttribute("SDx"))){
						coordParam.setSdx(Double.parseDouble(((Element) coordNode.item(0)).getAttribute("SDx")));
					}else{
						coordParam.setSdx(0.00);
					}
					if (!"".equals(((Element) coordNode.item(0)).getAttribute("SDy"))){
						coordParam.setSdy(Double.parseDouble(((Element) coordNode.item(0)).getAttribute("SDy")));
					}else{
						coordParam.setSdy(0.00);
					}
					if (!"".equals(((Element) coordNode.item(0)).getAttribute("SDz"))){
						coordParam.setSdz(Double.parseDouble(((Element) coordNode.item(0)).getAttribute("SDz")));
					}else{
						coordParam.setSdz(0.00);
					}
					if (!"".equals(((Element) coordNode.item(0)).getAttribute("SQx"))){
						coordParam.setSqx(Double.parseDouble(((Element) coordNode.item(0)).getAttribute("SQx")));
					}else{
						coordParam.setSqx(0.00);
					}
					if (!"".equals(((Element) coordNode.item(0)).getAttribute("SQy"))){
						coordParam.setSqy(Double.parseDouble(((Element) coordNode.item(0)).getAttribute("SQy")));
					}else{
						coordParam.setSqy(0.00);
					}
					if (!"".equals(((Element) coordNode.item(0)).getAttribute("SQz"))){
						coordParam.setSqz(Double.parseDouble(((Element) coordNode.item(0)).getAttribute("SQz")));
					}else{
						coordParam.setSqz(0.00);
					}
					if (!"".equals(((Element) coordNode.item(0)).getAttribute("SScale"))){
						coordParam.setSscale(Double.parseDouble(((Element) coordNode.item(0)).getAttribute("SScale")));
					}else{
						coordParam.setSscale(0.00);
					}
					//目标坐标系参数获取
					if (!"".equals(((Element) coordNode.item(0)).getAttribute("PProjectiontype"))){
						coordParam.setPprojectionType(Integer.parseInt(((Element) coordNode.item(0)).getAttribute("PProjectiontype"))); //带宽3,6
					}else{
						coordParam.setPprojectionType(3);
					}
					
					if (!"".equals(((Element) coordNode.item(0)).getAttribute("PBenchmarklatitude"))){
						coordParam.setPbenchmarklatitude(Double.parseDouble(((Element) coordNode.item(0)).getAttribute("PBenchmarklatitude")));
					}else{
						coordParam.setPbenchmarklatitude(0.00);
					}
					if (!"".equals(((Element) coordNode.item(0)).getAttribute("PCentralmeridian"))){
						coordParam.setPcentralmeridian(Double.parseDouble(((Element) coordNode.item(0)).getAttribute("PCentralmeridian")));
					}else{
						coordParam.setPcentralmeridian(114);
					}
					if (!"".equals(((Element) coordNode.item(0)).getAttribute("PConstantx"))){
						coordParam.setPconstantx(Double.parseDouble(((Element) coordNode.item(0)).getAttribute("PConstantx")));
					}else{
						coordParam.setPconstantx(0.00);
					}
					if (!"".equals(((Element) coordNode.item(0)).getAttribute("PConstanty"))){
						coordParam.setPconstanty(Double.parseDouble(((Element) coordNode.item(0)).getAttribute("PConstanty")));
					}else{
						coordParam.setPconstanty(500000);
					}
					if (!"".equals(((Element) coordNode.item(0)).getAttribute("PScale"))){
						coordParam.setPscale(Double.parseDouble(((Element) coordNode.item(0)).getAttribute("PScale")));
					}else{
						coordParam.setPscale(1);
					}
	
					if (!"".equals(((Element) coordNode.item(0)).getAttribute("Semimajor"))){
						coordParam.setSemimajor(Double.parseDouble(((Element) coordNode.item(0)).getAttribute("Semimajor")));
					}else{
						coordParam.setSemimajor(6378245.000);
					}
					if (!"".equals(((Element) coordNode.item(0)).getAttribute("Flattening"))){
						coordParam.setFlattening(Double.parseDouble(((Element) coordNode.item(0)).getAttribute("Flattening")));
					}else{
						coordParam.setFlattening(0.00335281065983501543);
					}
	
					map.setCoordParam(coordParam);
				}
				// 地图菜单
				NodeList mapparentmenusNode = (NodeList) element
				.getElementsByTagName("mapparentmenus");
				MapparentmenusVO mapparentmenusVO=new MapparentmenusVO();// 菜单
				if(mapparentmenusNode.getLength()!=0){
					NodeList mapparentmenuNode=mapparentmenusNode.item(0).getChildNodes();
					List<MapparentmenuVO> mapparentmenuList=new ArrayList<MapparentmenuVO>();
					
					for(int i=0;i<mapparentmenuNode.getLength();++i){
						if (mapparentmenuNode.item(i).getNodeType() == Node.ELEMENT_NODE) { // 判断当前结点是否是元素结点
							MapparentmenuVO mapparentmenuVO=new MapparentmenuVO();
							
							mapparentmenuVO.setId(((Element) mapparentmenuNode.item(i)).getAttribute("id"));
							mapparentmenuVO.setIsdisplay("true".equals(((Element) mapparentmenuNode.item(i)).getAttribute("isdisplay")));
							mapparentmenuVO.setName(((Element) mapparentmenuNode.item(i)).getAttribute("name"));
							List<MapmenuVO> mapmenuList=new ArrayList<MapmenuVO>();
							NodeList mapmenuNode=mapparentmenuNode.item(i).getChildNodes();
							if(mapmenuNode.getLength()!=0){
								
								for(int j=0;j<mapmenuNode.getLength();++j){
									if (mapmenuNode.item(j).getNodeType() == Node.ELEMENT_NODE) { // 判断当前结点是否是元素结点
										MapmenuVO mapmenuVO=new MapmenuVO();
										mapmenuVO.setId(((Element) mapmenuNode.item(j)).getAttribute("id"));
										mapmenuVO.setIsdisplay("true".equals(((Element) mapmenuNode.item(j)).getAttribute("isdisplay")));
										mapmenuVO.setName(((Element) mapmenuNode.item(j)).getAttribute("name"));
										mapmenuList.add(mapmenuVO);
									}
								}
							}
							mapparentmenuVO.setMapmenuList(mapmenuList);
							mapparentmenuList.add(mapparentmenuVO);
						}	
					}
					mapparentmenusVO.setMapparentmenuList(mapparentmenuList);
					map.setMapparentmenusVO(mapparentmenusVO);
				}
				
				configuration.setMap(map);
				// map读取结束
				
				// mainmenu读取开始
				NodeList mainmenuNode = (NodeList) element
						.getElementsByTagName("mainmenu").item(0)
						.getChildNodes(); // 根据mainmenu获得结点
				for (int i = 0; i < mainmenuNode.getLength(); i++) {
					if (mainmenuNode.item(i).getNodeType() == Node.ELEMENT_NODE) { // 判断当前结点是否是元素结点
						Element mainmenu = (Element) mainmenuNode.item(i);

						MenuClassify classify = new MenuClassify();
						classify.setId(Integer.parseInt(mainmenu
								.getAttribute("id")));
						classify.setTitle(mainmenu.getAttribute("name"));
						classify.setClazz(mainmenu.getAttribute("enter"));
						classify.setType(mainmenu.getAttribute("style"));
						classify.setIcon(mainmenu.getAttribute("icon"));
						classify.setIsDisplayTaskCount(mainmenu.getAttribute("isDisplayTaskCount"));
						configuration.getMainmenu().add(classify);
					}
				}

				//设置采集半径
				if (element.getElementsByTagName("setting").getLength()!= 0){
					NodeList settingNode = (NodeList) element
							.getElementsByTagName("setting").item(0)
							.getChildNodes(); // 根据mainmenu获得结点
					Setting setting=new Setting();
					List<Range> ranges=new ArrayList<>();
					for (int i=0;i<settingNode.getLength();i++){
						if (settingNode.item(i).getNodeType() == Node.ELEMENT_NODE){
							Element mainmenu = (Element) settingNode.item(i);
							Range range=new Range();
							range.setId(mainmenu.getAttribute("id"));
							List<Number> numbers=new ArrayList<>();
							NodeList numberNode=mainmenu.getChildNodes();
							if (numberNode!=null&&numberNode.getLength()>0){
								for (int j=0;j<numberNode.getLength();j++){
									if (numberNode.item(j).getNodeType() == Node.ELEMENT_NODE){
										Number number=new Number();
										Element numberEl= (Element) numberNode.item(j);
										number.setId(numberEl.getAttribute("id"));
										number.setValue(numberEl.getAttribute("value"));
										numbers.add(number);
									}
								}
							}
							range.setNumbers(numbers);
							ranges.add(range);
						}
					}
					setting.setRange(ranges);
					configuration.setSetting(setting);
				}

				//设置工程类型
				if (element.getElementsByTagName("project").getLength()!= 0){
					NodeList settingNode = (NodeList) element
							.getElementsByTagName("project").item(0)
							.getChildNodes(); // 根据mainmenu获得结点
					Project project=new Project();
					List<ProjectType> projectTypes=new ArrayList<>();
					for (int i=0;i<settingNode.getLength();i++){
                        if (settingNode.item(i).getNodeType() == Node.ELEMENT_NODE){
                            Element mainmenu = (Element) settingNode.item(i);
                            ProjectType projectType=new ProjectType();
                            projectType.setName(mainmenu.getAttribute("name"));
                            projectType.setSymbol(mainmenu.getAttribute("symbol"));
                            projectTypes.add(projectType);
                        }
					}
					project.setProjectTypes(projectTypes);
					configuration.setProject(project);
				}

				//设置view 是否显示
				if (element.getElementsByTagName("views").getLength()!= 0){
					NodeList settingNode = (NodeList) element
							.getElementsByTagName("views").item(0)
							.getChildNodes(); // 根据mainmenu获得结点
					Views views=new Views();
					List<View> viewList=new ArrayList<>();
					for (int i=0;i<settingNode.getLength();i++){
                        if (settingNode.item(i).getNodeType() == Node.ELEMENT_NODE){
                            Element mainmenu = (Element) settingNode.item(i);
                            View view=new View();
							view.setName(mainmenu.getAttribute("name"));
							view.setIsShow(mainmenu.getAttribute("isshow"));
                            viewList.add(view);
                        }
					}
					views.setViewList(viewList);
					configuration.setViews(views);
				}


				// 上报历史配置
				if (element.getElementsByTagName("reportHistory").getLength()!= 0){
					NodeList tableNode = (NodeList) element
							.getElementsByTagName("reportHistory").item(0)
							.getChildNodes();
					ReportHistory reportHistory =new ReportHistory();
					List<Table> tables =new ArrayList<Table>();
					for (int i = 0; i < tableNode.getLength(); i++) {
						if (tableNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
							Element tableElement = (Element) tableNode.item(i);
							Table table = new Table();
							table.setName(tableElement.getAttribute("name"));
							table.setTableName(tableElement.getAttribute("tablename"));
							List<TableField> tablefields = new ArrayList<TableField>();
							NodeList tableFieldNode =tableElement.getChildNodes();
							if(tableFieldNode.getLength()!=0){
								
								for(int j=0;j<tableFieldNode.getLength();++j){
									if (tableFieldNode.item(j).getNodeType() == Node.ELEMENT_NODE) { // 判断当前结点是否是元素结点
										TableField tableField =new TableField();
										Element fieldElement = (Element) tableFieldNode.item(j);
										tableField.setName(fieldElement.getAttribute("name"));
										tableField.setzName(fieldElement.getAttribute("zName"));
										tableField.setShowDesktop(fieldElement.getAttribute("showDesktop"));
										tablefields.add(tableField);
									}
								}
							}
							table.setFields(tablefields);
							tables.add(table);
						}
					}
					reportHistory.setTables(tables);
					configuration.setReportHistory(reportHistory);
				}

				// 城市列表七参数就
				if (element.getElementsByTagName("citylist").getLength()!= 0){
					NodeList tableNode = (NodeList) element
							.getElementsByTagName("citylist").item(0)
							.getChildNodes();
					CityList cityList=new CityList();
					List<City> cities=new ArrayList<>();
					for (int i = 0; i < tableNode.getLength(); i++) {
						if (tableNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
							Element tableElement = (Element) tableNode.item(i);
							City city=new City();
							city.setCityCode(tableElement.getAttribute("cityCode"));
							city.setCityName(tableElement.getAttribute("cityName"));
							city.setId(tableElement.getAttribute("id"));

							List<CoordTransformModel> coordTransformModelList=new ArrayList<>();
							NodeList tableFieldNode =tableElement.getChildNodes();
							if(tableFieldNode.getLength()!=0){
								for(int j=0;j<tableFieldNode.getLength();++j){
									if (tableFieldNode.item(j).getNodeType() == Node.ELEMENT_NODE) { // 判断当前结点是否是元素结点
										CoordTransformModel coordTransformModel=new CoordTransformModel();
										Element fieldElement = (Element) tableFieldNode.item(j);
										coordTransformModel.setItiCoordinate(fieldElement.getAttribute("Coordinate"));
										if (fieldElement.getAttribute("SDx")!=null&&!fieldElement.getAttribute("SDx").equals("")){
											coordTransformModel.setItiSDx(Double.parseDouble(fieldElement.getAttribute("SDx")));
										}
										if (fieldElement.getAttribute("SDy")!=null&&!fieldElement.getAttribute("SDy").equals("")){
											coordTransformModel.setItiSDy(Double.parseDouble(fieldElement.getAttribute("SDy")));
										}
										if (fieldElement.getAttribute("SDz")!=null&&!fieldElement.getAttribute("SDz").equals("")){
											coordTransformModel.setItiSDz(Double.parseDouble(fieldElement.getAttribute("SDz")));

										}
										if (fieldElement.getAttribute("SQx")!=null&&!fieldElement.getAttribute("SQx").equals("")){
											coordTransformModel.setItiSQx(Double.parseDouble(fieldElement.getAttribute("SQx")));
										}
										if (fieldElement.getAttribute("SQy")!=null&&!fieldElement.getAttribute("SQy").equals("")){
											coordTransformModel.setItiSQy(Double.parseDouble(fieldElement.getAttribute("SQy")));
										}
										if (fieldElement.getAttribute("SQz")!=null&&!fieldElement.getAttribute("SQz").equals("")){
											coordTransformModel.setItiSQz(Double.parseDouble(fieldElement.getAttribute("SQz")));
										}
										if (fieldElement.getAttribute("SScale")!=null&&!fieldElement.getAttribute("SScale").equals("")){
											coordTransformModel.setItiSScale(Double.parseDouble(fieldElement.getAttribute("SScale")));
										}
										if (fieldElement.getAttribute("FScale")!=null&&!fieldElement.getAttribute("FScale").equals("")){
											coordTransformModel.setItiFScale(Double.parseDouble(fieldElement.getAttribute("FScale")));
										}
										if (fieldElement.getAttribute("PScale")!=null&&!fieldElement.getAttribute("PScale").equals("")){
											coordTransformModel.setItiPScale(Double.parseDouble(fieldElement.getAttribute("PScale")));
										}
										if (fieldElement.getAttribute("FDx")!=null&&!fieldElement.getAttribute("FDx").equals("")){
											coordTransformModel.setItiFDx(Double.parseDouble(fieldElement.getAttribute("FDx")));
										}
										if (fieldElement.getAttribute("FDy")!=null&&!fieldElement.getAttribute("FDy").equals("")){
											coordTransformModel.setItiFDy(Double.parseDouble(fieldElement.getAttribute("FDy")));
										}
										if (fieldElement.getAttribute("FRotateangle")!=null&&!fieldElement.getAttribute("FRotateangle").equals("")){
											coordTransformModel.setItiFRotateangle(Double.parseDouble(fieldElement.getAttribute("FRotateangle")));
										}
										if (fieldElement.getAttribute("PProjectiontype")!=null&&!fieldElement.getAttribute("PProjectiontype").equals("")){
											coordTransformModel.setItiPProjectiontype(Long.parseLong(fieldElement.getAttribute("PProjectiontype")));
										}
										if (fieldElement.getAttribute("PCentralmeridian")!=null&&!fieldElement.getAttribute("PCentralmeridian").equals("")){
											coordTransformModel.setItiPCentralmeridian(Double.parseDouble(fieldElement.getAttribute("PCentralmeridian")));
										}
										if (fieldElement.getAttribute("PBenchmarklatitude")!=null&&!fieldElement.getAttribute("PBenchmarklatitude").equals("")){
											coordTransformModel.setItiPBenchmarklatitude(Double.parseDouble(fieldElement.getAttribute("PBenchmarklatitude")));
										}
										if (fieldElement.getAttribute("PConstantx")!=null&&!fieldElement.getAttribute("PConstantx").equals("")){
											coordTransformModel.setItiPConstantx(Double.parseDouble(fieldElement.getAttribute("PConstantx")));
										}
										if (fieldElement.getAttribute("PConstanty")!=null&&!fieldElement.getAttribute("PConstanty").equals("")){
											coordTransformModel.setItiPConstanty(Double.parseDouble(fieldElement.getAttribute("PConstanty")));
										}
										if (fieldElement.getAttribute("Semimajor")!=null&&!fieldElement.getAttribute("Semimajor").equals("")){
											coordTransformModel.setItiSemimajor(Double.parseDouble(fieldElement.getAttribute("Semimajor")));
										}
										if (fieldElement.getAttribute("Flattening")!=null&&!fieldElement.getAttribute("Flattening").equals("")){
											coordTransformModel.setItiFlattening(Double.parseDouble(fieldElement.getAttribute("Flattening")));
										}

										coordTransformModelList.add(coordTransformModel);
									}
								}
							}
							city.setCoordTransformModel(coordTransformModelList.get(0));
							cities.add(city);
						}
					}
					cityList.setCityList(cities);
					configuration.setCityList(cityList);
				}

				// 导出数据
				if (element.getElementsByTagName("export").getLength()!= 0){
					NodeList tableNode = (NodeList) element
							.getElementsByTagName("export").item(0)
							.getChildNodes();
					Export export =new Export();
					List<Movetype> movetypes =new ArrayList<Movetype>();
					for (int i = 0; i < tableNode.getLength(); i++) {
						if (tableNode.item(i).getNodeType() == Node.ELEMENT_NODE) {
							Element tableElement = (Element) tableNode.item(i);
							Movetype movetype = new Movetype();
							movetype.setId(tableElement.getAttribute("id"));
							movetype.setName(tableElement.getAttribute("name"));
							movetype.setIsshow(tableElement.getAttribute("isshow"));
							movetype.setIcon(tableElement.getAttribute("icon"));
							movetype.setTableName(tableElement.getAttribute("tableName"));
							movetype.setType(tableElement.getAttribute("type"));
							List<Attribute> attributes = new ArrayList<Attribute>();
							NodeList tableFieldNode =tableElement.getChildNodes();
							if(tableFieldNode.getLength()!=0){

								for(int j=0;j<tableFieldNode.getLength();++j){
									if (tableFieldNode.item(j).getNodeType() == Node.ELEMENT_NODE) { // 判断当前结点是否是元素结点
										Attribute attribute =new Attribute();
										Element fieldElement = (Element) tableFieldNode.item(j);
										attribute.setName(fieldElement.getAttribute("name"));
										attribute.setValue(fieldElement.getAttribute("value"));
										attribute.setSourceValue(fieldElement.getAttribute("sourceValue"));
										attribute.setType(fieldElement.getAttribute("type"));
										attributes.add(attribute);
									}
								}
							}
							movetype.setAttribute(attributes);
							movetypes.add(movetype);
						}
					}
					export.setMovetype(movetypes);
					configuration.setExport(export);
				}

				// 地图设施列表
				if (element.getElementsByTagName("mapfacility").getLength()!= 0){
					NodeList tableNode = (NodeList) element
							.getElementsByTagName("mapfacility").item(0)
							.getChildNodes();
					MapFacility mapFacility=new MapFacility();
					List<FacilityType> facilityTypes=new ArrayList<>();
					for (int i=0;i<tableNode.getLength();i++){
						if (tableNode.item(i).getNodeType()==Node.ELEMENT_NODE){
							Element tableElement = (Element) tableNode.item(i);
							FacilityType facilityType=new FacilityType();
							facilityType.setName(tableElement.getAttribute("name"));
							List<Facility> facilities=new ArrayList<>();
							NodeList tableFieldNode =tableElement.getChildNodes();
							if (tableFieldNode.getLength()!=0){
								for(int j=0;j<tableFieldNode.getLength();++j){
									if (tableFieldNode.item(j).getNodeType() == Node.ELEMENT_NODE) {		// 判断当前结点是否是元素结点
										Facility facility=new Facility();
										Element fieldElement = (Element) tableFieldNode.item(j);
										facility.setId(fieldElement.getAttribute("id"));
										facility.setName(fieldElement.getAttribute("name"));
										facility.setIsshow(fieldElement.getAttribute("isshow"));
										facility.setIcon(fieldElement.getAttribute("icon"));
										facility.setTableName(fieldElement.getAttribute("tableName"));
										facilities.add(facility);
									}
								}
							}
							facilityType.setFacilities(facilities);
							facilityTypes.add(facilityType);
						}
					}
					mapFacility.setFacilityTypes(facilityTypes);
					configuration.setMapFacility(mapFacility);

				}

				// module读取开始
				if (element.getElementsByTagName("bizmodule").getLength()!= 0){
					NodeList modulesNode = (NodeList) element
							.getElementsByTagName("bizmodule").item(0)
							.getChildNodes(); // 根据bizmodule获得结点
					for (int i = 0; i < modulesNode.getLength(); i++) {
						if (modulesNode.item(i).getNodeType() == Node.ELEMENT_NODE) { // 判断当前结点是否是元素结点
							Element moduleElement = (Element) modulesNode.item(i);
							Module module = new Module();
							module.setId(moduleElement.getAttribute("id"));
							module.setName(moduleElement.getAttribute("name"));
							module.setLayers(getFtlayers(map.getFtlayers(),
									moduleElement.getAttribute("layers")));
							module.setIcon(moduleElement.getAttribute("icon"));
							//动态表单引用
							module.setFormtype(moduleElement.getAttribute("formtype"));//表单类型xml为动态
							module.setTemplate(moduleElement.getAttribute("template"));//设置了表单类型为XML，则需要指定引用模板
							
							module.setFtlayer(moduleElement.getAttribute("ftlayer"));
							module.setTheme(Boolean.valueOf(moduleElement
									.getAttribute("fttlayer")));
							module.setCanwrite(Boolean.valueOf(moduleElement
									.getAttribute("canwrite")));
							module.setHistory(moduleElement.getAttribute("history"));
							module.setUrl(moduleElement.getAttribute("url"));
							module.setIsShowDeleteBnt(moduleElement.getAttribute("isShowDeleteBnt"));
							module.setLinemodule(moduleElement.getAttribute("linkmodule"));
							
							if (moduleElement.getAttribute("menu") != null){
								int mid = Integer.valueOf(moduleElement.getAttribute("menu"));
								for (MenuClassify menu :configuration.getMainmenu()){
									if (menu.getId().equals(mid)){
										module.setMenu(menu);
										break;
									}
								}
							}
							
												
							configuration.getModules().add(module);
						}
					}
				}
				// module读取结束

			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return configuration;
	}

	private LAYERTYPE getLAYERTYPE(String type) {
		LAYERTYPE layertype = LAYERTYPE.dynamic;
		if ("dynamic".equals(type)) {
			layertype = LAYERTYPE.dynamic;
		} else if ("tiled".equals(type)) {
			layertype = LAYERTYPE.tiled;
		} else if ("image".equals(type)) {
			layertype = LAYERTYPE.image;
		} else if ("local".equals(type)) {
			layertype = LAYERTYPE.local;
		}
		return layertype;
	}

	protected IMAGE_FORMAT getIMAGE_FORMAT(String format) {
		if (format == null || format.equals("png24"))
			return IMAGE_FORMAT.PNG24;
		else if (format.equals("png8"))
			return IMAGE_FORMAT.PNG8;
		else if (format.equals("png"))
			return IMAGE_FORMAT.PNG;
		else if (format.equals("bmp"))
			return IMAGE_FORMAT.BMP;
		else if (format.equals("jpg"))
			return IMAGE_FORMAT.JPG;
		else if (format.equals("jpgpng"))
			return IMAGE_FORMAT.JPG_PNG;
		else if (format.equals("gif"))
			return IMAGE_FORMAT.GIF;
		else if (format.equals("tiff"))
			return IMAGE_FORMAT.TIFF;
		else
			return IMAGE_FORMAT.PNG24;
	}

	private List<Field> getFields(String fields) {
		List<Field> fieldList = new ArrayList<Field>();
		if (fields != null) {
			String[] field = fields.split(",");
			if (field != null) {
				for (int i = 0; i < field.length; ++i) {
					Field f = new Field();
					String[] s = field[i].split(":");
					for (int j = 0; j < s.length; ++j) {
						switch (j) {
						case 0:
							f.setName(s[j]);
							break;
						case 1:
							f.setAlias(s[j]);
							break;
						case 2:
							f.setVisible("1".equals(s[j]));
							break;
						case 3:
							if(s[j].contains("|")){
								String []str =s[j].split("\\|");
								f.setEditType(str[0]);
								List<String>dropList = new ArrayList<String>();
								if(str.length>1){
									for(int n=1;n<str.length;++n){
										dropList.add(str[n]);
									}
								}
								f.setDropList(dropList);
							}else{
								f.setEditType(s[j]);
							}
							break;
						case 4:
							f.setSynopsis(s[j]);
							break;

						default:
							break;
						}
					}
					fieldList.add(f);
				}
			}
		}
		return fieldList;
	}

	private ArrayList<Ftlayer> getFtlayers(List<Ftlayer> ftlayers, String ids) {
		ArrayList<Ftlayer> layers = null;
		if (ftlayers != null && ids != null) {
			if (ids.equals("*")) {
				layers = new ArrayList<Ftlayer>();
				for (int i = 0; i < ftlayers.size(); ++i) {
					layers.add(ftlayers.get(i));
				}
			} else {
				String[] id = ids.split(",");
				layers = new ArrayList<Ftlayer>();
				for (int i = 0; i < ftlayers.size(); ++i) {
					for (int j = 0; j > id.length; ++j) {
						if (id[j].equals(ftlayers.get(i).getId() + "")) {
							layers.add(ftlayers.get(i));
						}
					}
				}
			}
		}
		return layers;
	}

	private Mapservice getMapservice(String id, List<Mapservice> mapservices) {
		Mapservice mapservice = null;
		if (mapservices != null && id != null) {
			for (int i = 0; i < mapservices.size(); ++i) {
				if (id.equals(mapservices.get(i).getId() + "")) {
					mapservice = mapservices.get(i);
				}
			}
		}
		return mapservice;
	}

}
