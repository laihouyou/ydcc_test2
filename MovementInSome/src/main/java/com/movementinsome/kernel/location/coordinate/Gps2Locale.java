package com.movementinsome.kernel.location.coordinate;

import com.movementinsome.kernel.initial.model.CoordParam;

public class Gps2Locale {

	// WGS-84空间直角坐标
	private static double _dOutX, _dOutY, _dOutZ;
	// 国家标准空间直角坐标,转换目标坐标系统的空间直角坐标值
	private static double _x, _y, _z;
	// 国家标准地心大地坐标
	private static double _dOutB, _dOutL, _dOutH;
	// 国家标准常用坐标系
	private static double _X, _Y, _Z;
	private static double _rlon;
	// 地方的坐标系
	@SuppressWarnings("unused")
	public double _dLocalX, _dLocalY, _dLocalZ;
	

	/*
	 * public Point GPS2MapPoint(double lat, double lon, double alt,
	 * TranslateParam param,SpatialReference spRefence){
	 * 
	 * CalculateLocalCoordinate(lat, lon, alt, param); return new
	 * Point(_dLocalX, _dLocalY); }
	 */
	
	// / <summary>xyh
	// / GPS坐标转换成地区标准坐标
	// / 输入：GPS的纬度(Latitude)，GPS的经度(Longitude)，GPS的高程（单位度）
	// / 输出：本地坐标系统X位置，本地坐标系统Y位置，本地坐标系统的高程（单位米）
	// / </summary>
	// / <param name="dInB">GPS的纬度</param>
	// / <param name="dInL">GPS的经度</param>
	// / <param name="dInH">GPS的高程</param>
	// / <param name="translateInfo">坐标转换信息</param>
	// / <param name="dLocalX">地区坐标的X(单位米)</param>
	// / <param name="dLocalY">地区坐标的Y</param>
	// / <param name="dLocalH">地区坐标的高程</param>
	public void CalculateLocalCoordinate(double dInB, double dInL, double dInH,
			CoordParam param) {
		// gps坐标转换成国家标准坐标
		Translate(dInB, dInL, dInH, param);

		// if (param.getM_ITI_F_DX() == 0.00 || param.getM_ITI_F_DY() == 0.00)
		if (param.getFdx() == 0.00 || param.getFdy() == 0.00) {
			_dLocalX = _Y;
			_dLocalY = _X;
			_dLocalZ = _Z;
		} else {
			// 获取相应的四参数
			double c = param.getFdx();// .getM_ITI_F_DX();
			double d = param.getFdy();// .getM_ITI_F_DY();
			double k = param.getFscale();// .getM_ITI_F_Scale();
			double t = param.getFrotateangle();// .getM_ITI_F_RotateAngle();

			// 四参数计算新的XY值
			double a = k * Math.cos(t);
			double b = k * Math.sin(t);
			double xg = c + a * _X - b * _Y;
			double yg = d + b * _X + a * _Y;

			// 原来这段代码，造成XY坐标值倒置，将XY调换，如果以后有问题可以改回来
			// dLocalX = xg;
			// dLocalY = yg;
			_dLocalX = yg == Double.NaN ? 0 : yg;
			_dLocalY = xg == Double.NaN ? 0 : xg;
			_dLocalZ = _Z == Double.NaN ? 0 : _Z;

		}
	}

	// / GPS坐标转换成国家标准坐标
	// / <param name="dInB">GPS的纬度</param>
	// / <param name="dInL">GPS的经度</param>
	// / <param name="dInH">GPS的高程</param>
	@SuppressWarnings("unused")
	private static void Translate(double dInB, double dInL, double dInH,
			CoordParam param) {
		// 6378137和1/298.257 分别为WGS-84坐标椭球的长半轴和扁率
		// 将BLH在当前坐标基准下转换为XYZ 坐标还是WGS-84的
		// 将地心大地坐标转换成空间直角坐标 ：_dOutX,_dOutY,_dOutZ
		//dmstorad(dInB)转换为弧度
		//cal_b(6378137, 1 / 298.257224)计算出短轴
		BLhtoXYZ(dmstorad(dInB), dmstorad(dInL), dInH, 6378137,
				cal_b(6378137, 1 / 298.257224));// 已验证 转84

		//a = 6378245.0; f = 1.0/298.3; //54年北京坐标系参数   
		/*BLhtoXYZ(dmstorad(dInB), dmstorad(dInL), dInH, 6378245.0,
				cal_b(6378245.0, 1.0/298.3));// 转54
*/		// 七参数
		double[] Para = new double[7];
		Para[0] = param.getSdx();// .getM_ITI_S_DX();//平移
		Para[1] = param.getSdy();// .getM_ITI_S_DY(); //平移
		Para[2] = param.getSdz();// .getM_ITI_S_DZ(); //平移
		Para[3] = param.getSscale();// .getM_ITI_S_Scale(); //1000000;//比例因子
		Para[4] = param.getSqx();// .getM_ITI_S_QX();//旋转
		Para[5] = param.getSqy();// .getM_ITI_S_QY(); //旋转
		Para[6] = param.getSqz();// .getM_ITI_S_QZ(); //旋转
		// 根据转换参数将当前的的XYZ转换到目的坐标基准的XYZ
		// 将84坐标系下面的大地直角坐标转换成目标椭球体的大地直角坐标
		CoorTran7(_dOutX, _dOutY, _dOutZ, Para);// 已验证

		// 将目的坐标基准的XYZ转换为BLH
		double dSemimajor = param.getSemimajor();// .getM_ITI_Semimajor();
													// //椭球长半轴
		double dSemiminor = cal_b(param.getSemimajor(), param.getFlattening());
		// cal_b(param.getM_ITI_Semimajor(), param.getM_ITI_Flattening());
		// //椭球短半轴

		// 跟据当前椭球体的大地直角坐标计算出当前椭球体的大地地心坐标
		XYZtoBLh(_x, _y, _z, dSemimajor, dSemiminor, 0.00000001);

		_Z = _dOutH;
		int id_Zone = param.getPprojectionType();// .getM_ITI_P_ProjectionType();
													// //投影方式（高斯投影3度带、高斯投影6度带）

		int iOutZone; // 当前经度坐标所处的带数

		double m_dCentral_Meridian = param.getPcentralmeridian();// .getM_ITI_P_CentralMeridian();
																	// //dOutL;
																	// //中央子午线经度
																	// //自己设想并初始化

		int m_bStd_Zone = 1; // 自己设想的值

		if (m_bStd_Zone == 1) {
			iOutZone = lonofcentralmeridian(radtodv(_dOutL), id_Zone);
			m_dCentral_Meridian = _rlon;
		} else {
			iOutZone = lonofcentralmeridian(dmstodv(m_dCentral_Meridian),
					id_Zone);
		}

		// 将当前基准下的BL转换到当前基准的xy，使用高斯投影
		// 使用高斯投影降大地地心坐标转换成平面指教坐标
		// BLtoxy(_dOutB, _dOutL, dvtorad(m_dCentral_Meridian), dSemimajor,
		// param.getM_ITI_Flattening(), param.getM_ITI_P_ConstantX(),
		// param.getM_ITI_P_ConstantY());
		BLtoxy(_dOutB, _dOutL, dvtorad(param.getPcentralmeridian()),
				dSemimajor, param.getFlattening(), param.getPconstantx(),
				param.getPconstanty());

	}

	
	
	// / GPS坐标转换成国家标准坐标
	// / <param name="x">地方坐标系统的x坐标</param>
	// / <param name="y">地方坐标系统的y坐标</param>
	// / <param name="h">地方坐标的高程</param>
	@SuppressWarnings("unused")
	public static double[] RevTranslate(double x, double y, double h,
			CoordParam param) {
		//如果采用了四参娄，先进行四参反算
		if (param.getFdx() != 0.00 || param.getFdy() != 0.00) {
		// 获取相应的四参数
			double c = param.getFdx();// .getM_ITI_F_DX();
			double d = param.getFdy();// .getM_ITI_F_DY();
			double k = param.getFscale();// .getM_ITI_F_Scale();
			double t = param.getFrotateangle();// .getM_ITI_F_RotateAngle();

			// 四参数计算新的XY值
			double a = k * Math.cos(t);
			double b = k * Math.sin(t);
			
			double a1=1/a;
			double b1=1/b;
			
			_X = (y*a1 - d*a1 - c*b1 + x*b1)/(a*b1 + b*a1);
					
			_Y = (y*b1 + c*a1 - d*b1 - x*a1)/(b*a1 + a*b1);
		}else{
			_X = x;
			_Y = y;
		}
		
		xy2Bl(_X,_Y,param.getSemimajor(),param.getFlattening(),param.getPcentralmeridian(),param.getPconstanty());
		
		
		BLhtoXYZ(dmstorad(_dOutB), dmstorad(_dOutL), 0, param.getSemimajor(),cal_b(param.getSemimajor(), param.getFlattening()));// 转54
		// 七参数
		double[] Para = new double[7];
		Para[0] = param.getSdx();// .getM_ITI_S_DX();//平移
		Para[1] = param.getSdy();// .getM_ITI_S_DY(); //平移
		Para[2] = param.getSdz();// .getM_ITI_S_DZ(); //平移
		Para[3] = param.getSscale();// .getM_ITI_S_Scale(); //1000000;//比例因子
		Para[4] = param.getSqx();// .getM_ITI_S_QX();//旋转
		Para[5] = param.getSqy();// .getM_ITI_S_QY(); //旋转
		Para[6] = param.getSqz();// .getM_ITI_S_QZ(); //旋转
		// 根据转换参数将当前的的XYZ转换到目的坐标基准的XYZ
		// 将84坐标系下面的大地直角坐标转换成目标椭球体的大地直角坐标
		RevCoorTran7(_dOutX, _dOutY, _dOutZ, Para);// 已验证
	
		// 6378137和1/298.257 分别为WGS-84坐标椭球的长半轴和扁率
		// 将目的坐标基准的XYZ转换为BLH
		double dSemimajor = 6378137;//param.getSemimajor();// .getM_ITI_Semimajor();
													// //椭球长半轴
		double dSemiminor =  cal_b(6378137, 1 / 298.257224);// cal_b(param.getSemimajor(), param.getFlattening());
		//cal_b(param.getM_ITI_Semimajor(), param.getM_ITI_Flattening());
		// //椭球短半轴

		// 跟据当前椭球体的大地直角坐标计算出当前椭球体的大地地心坐标
/*		_x=-2253904.0661941655;
		_y=5012042.112249296;
		_z=3226426.031240724;*/
		XYZtoBLh(_x, _y, _z, dSemimajor, dSemiminor, 0.00000001);
		
		double B = Math.toDegrees(_dOutB);
		double L = Math.toDegrees(_dOutL);
		
		double wgslonlat[] = {L,B};
		
		return wgslonlat;

	}
	
	// / <summary>
	// / 将空间直角坐标(X0,Y0,Z0)用七参数Para[]法转换为另一空间直角坐标(X,Y,Z)
	// / </summary>
	// / <returns></returns>
	private static void RevCoorTran7(double X2, double Y2, double Z2, double[] Para) {
		double PI;
		double Rou;
		PI = Math.atan(1) * 4.0;
		Rou = 180.0 / PI * 3600.0;
		
		double b0,b1,b2,b3,b4,b5,b6;
		
		b0=X2-Para[0];
		b1=Y2-Para[1];
		b2=Z2-Para[2];
		b3=1+Para[3];
		b4=Para[4]/Rou;
		b5=Para[5]/Rou;
		b6=Para[6]/Rou;
		
		_x = ((b0*b3+b2*b5)*(b3*b3+b4*b4)-(b3*b6-b4*b5)*(b1*b3-b2*b4))/((b3*b3+b5*b5)*(b3*b3+b4*b4)+(b3*b6-b4*b5)*(b3*b6+b4*b5));
		
		_y = ((b0*b3+b2*b5)*(b3*b6+b4*b5)+(b3*b3+b5*b5)*(b1*b3-b2*b4))/((b3*b3+b5*b5)*(b3*b3+b4*b4)+(b3*b6-b4*b5)*(b3*b6+b4*b5));
		
		_z = (b4*_y-b5*_x+b2)/b3;

	}
	
	
	private static void CoorTran7(double X0, double Y0, double Z0, double[] Para) {
		double PI;
		double Rou;
		PI = Math.atan(1) * 4.0;
		Rou = 180.0 / PI * 3600.0;
		_x = Para[0] + X0 * (1 + Para[3]) + Y0 * Para[6] / Rou - Z0 * Para[5]
				/ Rou;
		_y = Para[1] + Y0 * (1 + Para[3]) - X0 * Para[6] / Rou + Z0 * Para[4]
				/ Rou;
		_z = Para[2] + Z0 * (1 + Para[3]) + X0 * Para[5] / Rou - Y0 * Para[4]
				/ Rou;
	}
	/**
	 * 平面坐标转大地坐标(高斯反算)
	 * @param x 北向坐标值
	 * @param y 东向坐标值
	 * @param a 长轴
	 * @param f 曲率
	 * @param _l 中央子午线
	 * @param _y 向东偏移量
	 */
    private static void xy2Bl(double x, double y, double a, double f,double _l,double _y){
    	//x:B6,y:D6
    	double e2 = 2*f-f*f;  //第一偏心率平方
		double e1 = (1.0-Math.sqrt(1-e2))/(1.0+Math.sqrt(1-e2));  
		//D3
		double ee = e2/(1-e2); //第二偏心率平方0.0067385254147;
    	
		double b = Math.sqrt(a * a * (1 - e2));// 计算短轴
		//B3
		double c = a * a / b;//极曲率半径6399698.90178271;
		//F6
    	double L0 = _l ;// 114;  //中央子午线
    	//D6
    	y=y-_y;//500000;
    	//D10
    	double X = x/1000000;
    	//B9
    	double bf = 27.11115372595+9.02468257083*(X-3)-0.00579740442*Math.pow(X-3, 2)-0.00043532572*Math.pow(X-3, 3)+0.00004857285*Math.pow(X-3, 4)+
    			0.00000215727*Math.pow(X-3, 5)-0.00000019399*Math.pow(X-3, 6);
    	
    	//B10
    	double nf2 = ee*Math.pow(Math.cos(Math.toRadians(bf)), 2);
    	//D9
    	double n= (y*Math.sqrt(1+nf2))/c;
    	//F9
    	double tf=Math.tan(Math.toRadians(bf));
    	
    	//F10
    	double l = 1/(Math.PI*Math.cos(Math.toRadians(bf)))*(180*n-30*(1+2*Math.pow(tf, 2)+nf2)*Math.pow(n, 3)+1.5*(5+28*Math.pow(tf, 2)+24*Math.pow(tf, 4))*Math.pow(n, 5));
    	
    	_dOutL = L0+l;
    	_dOutB = bf-((1+nf2)/Math.PI)*tf*(90*n*n-7.5*(5+3*tf*tf+nf2-9*nf2*tf*tf)*Math.pow(n, 4)+0.25*(61+90*tf*tf+45*Math.pow(tf, 4))*Math.pow(n, 6));;
    }

	// / <summary>
	// / 将大地坐标(B,L,h)(弧度)转换为空间直角坐标(X,Y,Z)(米)，(a,b)为椭球长短半轴(米)
	// / </summary>
	// / <returns></returns>
	private static boolean BLhtoXYZ(double B, double L, double h, double a,
			double b) {
		double N;
		double e;
		e = cal_e(a, b, 1);
		N = cal_N(a, b, B);
		_dOutX = (N + h) * Math.cos(B) * Math.cos(L);
		_dOutY = (N + h) * Math.cos(B) * Math.sin(L);
		_dOutZ = (N * (1 - e * e) + h) * Math.sin(B);
		return false;
	}

	// / <summary>
	// / 将空间直角坐标(X,Y,Z)转换为大地坐标(B,L,h),(a,b)为椭球长短半轴(米) dB
	// 为纬度迭代容许精度，比如0.00001秒，注意单位
	// / </summary>
	private static boolean XYZtoBLh(double X, double Y, double Z, double a,
			double b, double dB) {
		double N0;
		double B0;
		double e1;
		double Bi;
		double t1, t2;
		double p2;// 用来存储判断
		int times = 0;// 迭代次数
		// CString str;
		double PI;
		PI = Math.atan(1) * 4.0;
		if (X == 0 && Y == 0) {
			if (Z > 0) {
				_dOutB = PI / 2;
				_dOutL = 0;
				_dOutH = Z - b;
				return true;
			}
			if (Z < 0) {
				_dOutB = -PI / 2;
				_dOutL = 0;
				_dOutH = Z - b;
				return true;
			}
		}
		if (Z == 0) {
			_dOutL = azimuth(X, Y);
			_dOutB = 0;
			_dOutH = Math.sqrt(X * X + Y * Y) - a;
			return true;
		}
		t2 = Z;
		if (Z < 0)
			t2 = -Z;
		// 先计算经度
		_dOutL = azimuth(X, Y);

		e1 = cal_e(a, b, 1);
		t1 = Math.sqrt(X * X + Y * Y);

		// 迭代计算

		B0 = Math.atan(t2 / t1);
		N0 = 1 - e1 * e1 * Math.sin(B0) * Math.sin(B0);
		N0 = Math.sqrt(N0);
		N0 = a / N0;

		do {
			Bi = t2 + N0 * e1 * e1 * Math.sin(B0);
			Bi = Bi / t1;
			Bi = Math.atan(Bi);
			p2 = Bi - B0;
			p2 = radtodv(p2) * 3600;

			times = times + 1;

			B0 = Bi;
			N0 = 1 - e1 * e1 * Math.sin(B0) * Math.sin(B0);
			N0 = Math.sqrt(N0);
			N0 = a / N0;
			if (times > 1000)
				break;
		} while (p2 > dB);

		// if(times>=1000)
		// return false;
		_dOutB = Bi;
		if (Z < 0)
			_dOutB = -Bi;
		_dOutH = t2 / Math.sin(Bi) - N0 * (1 - e1 * e1);

		return true;
	}

	// /// 高期正算
	// /// 采用高斯投影将BL转换到xy，角度均为弧度
	// /// edit by gordon 2012/02/09
	// /// 补充加上X偏移
	// /// </summary>
	// /// <returns></returns>

	// / <summary>
	// / 采用高斯投影将BL转换到xy，角度均为弧度
	// / </summary>
	// / <param name="B">大地纬度</param>
	// / <param name="L">大地经度</param>
	// / <param name="L0">中央子午线经度</param>
	// / <param name="a">椭球长半轴</param>
	// / <param name="f">椭球偏率</param>
	// / <param name="off_X">X平移参数</param>
	// / <param name="off_Y">Y平移参数</param>
	// / <param name="x">平面纵轴 我们在软件中的Y</param>
	// / <param name="y">平面横轴 我们软件中的X</param>
	// / <returns></returns>
	private static boolean BLtoxy(double B, double L, double L0, double a,
			double f, double off_X, double off_Y) {
		double l, t, nt;
		double l2, l4, l6;
		double l3, l5;
		double t2, t4;
		double N, b, e2;
		double X;
		double cos3, cos5;
		b = cal_b(a, f);
		N = cal_N(a, b, B);
		t = Math.tan(B);
		t2 = t * t;
		t4 = t2 * t2;
		e2 = cal_e(a, b, 2);
		nt = e2 * e2 * Math.cos(B) * Math.cos(B);
		X = meridianDis(B, a, f);
		l = L - L0;
		l2 = l * l;
		l3 = l2 * l;
		l4 = l3 * l;
		l5 = l4 * l;
		l6 = l5 * l;
		cos3 = Math.cos(B) * Math.cos(B) * Math.cos(B);
		cos5 = cos3 * Math.cos(B) * Math.cos(B);
		_X = 0.5 * N * Math.sin(B) * Math.cos(B) * l2 + N / 24 * Math.sin(B)
				* cos3 * (5 - t2 + 9 * nt + 4 * nt * nt) * l4 + N / 720
				* Math.sin(B) * cos5 * (61 - 58 * t2 + t4) * l6;
		_X = X + _X;
		// 加上X偏移
		_X = _X + off_X;
		_Y = N * Math.cos(B) * l + N / 6 * cos3 * (1 - t2 + nt) * l3 + N / 120
				* cos5 * (5 - 18 * t2 + t4 + 14 * nt - 58 * nt * t2) * l5;
		_Y = _Y + off_Y;
		return true;
	}
	
	/*
	 * 缺少计算公式：
	 * 1、平面坐标转换为空间直角坐标xy2XYZ
	 * 2、高斯反算xy2BLH
	 * 
	 */
	
	// / <summary>
	// / 将角度的度分秒转换为弧度
	// / </summary>
	// / <param name="x">度分秒类型的角度值</param>
	// / <returns></returns>
	private static double dmstorad(double x) {

		double t;
		// t=dmstodv(x); //输入时已经为度
		t = dvtorad(x);
		return t;
	}

	// / <summary>
	// / 将角度（度.分秒）转换为度的小数形式（度）
	// / </summary>
	// / <param name="x">度.分秒角度</param>
	// / <returns>度</returns>
	private static double dmstodv(double x) {
		int sign;
		double Degree, Miniute;
		double Second;
		if (x >= 0)
			sign = 1;
		else
			sign = -1;
		x = Math.abs(x);
		Degree = Math.floor(x);
		Miniute = Math.floor((x * 100.0) % (100.0));
		Second = (x * 10000.0) % (100.0);
		return (sign * (Degree + Miniute / 60.0 + Second / 3600.0));

	}

	// / <summary>
	// / 将角度的小数形式转换为弧度
	// / </summary>
	// / <param name="x"></param>
	// / <returns></returns>
	private static double dvtorad(double x) {
		double PI;
		PI = Math.atan(1) * 4.0;
		return (x * PI / 180.0);
	}

	// / <summary>
	// / 根据椭球长半轴单位米和椭球扁率计算椭球短半轴
	// / </summary>
	// / <param name="a"></param>
	// / <param name="f"></param>
	// / <returns></returns>
	private static double cal_b(double a, double f) {
		return (a * (1 - f));
	}

	// / <summary>
	// / 根据椭球长短轴计算椭球第一偏心率，计算结果不是平方值
	// / </summary>
	// / <returns></returns>
	private static double cal_e(double a, double b, int Id) {
		double tp = 0;
		if (Id == 1)
			tp = Math.sqrt((a * a - b * b) / (a * a));
		if (Id == 2)
			tp = Math.sqrt((a * a - b * b) / (b * b));
		return tp;
	}

	// / <summary>
	// / 根据椭球的长短半轴，计算纬度B处的卯酉圈曲率半径
	// / </summary>
	// / <returns></returns>
	private static double cal_N(double a, double b, double B) {
		double tp;
		double e1;
		e1 = cal_e(a, b, 1);
		tp = 1 - e1 * e1 * Math.sin(B) * Math.sin(B);
		tp = Math.sqrt(tp);
		tp = a / tp;
		return tp;
	}

	// / <summary>
	// / 根据两个点的坐标坐标差（单位米）计算方位角（单位弧度）
	// / </summary>
	// / <param name="dx">X方向坐标偏差（米）</param>
	// / <param name="dy">Y方向坐标偏差（米）</param>
	// / <returns>方位角（弧度）</returns>
	private static double azimuth(double dx, double dy) {
		// dx,dy不能都为0
		double s;
		double t;
		double ang;
		double PI;
		PI = Math.atan(1) * 4.0;
		t = dy;
		if (dy < 0)
			t = -dy;
		s = dx * dx + dy * dy;
		s = Math.sqrt(s);
		if (s == 0) {
			// 如果等于零，返回一个大于360的角，那么此方位角无效。
			ang = 3 * PI;
			return ang;
		}
		ang = Math.asin(t / s);
		if (dy == 0 && dx > 0)
			ang = 0;
		if (dy == 0 && dx < 0)
			ang = PI;
		if (dx >= 0 && dy > 0)
			// ang = ang; mutou 改动
			if (dx >= 0 && dy < 0)
				ang = 2 * PI - ang;
		if (dx < 0 && dy > 0)
			ang = PI - ang;
		if (dx < 0 && dy < 0)
			ang = PI + ang;
		return ang;
	}

	// / <summary>
	// / 将弧度转换为度的小数形式
	// / </summary>
	// / <param name="x">弧度</param>
	// / <returns>度</returns>
	private static double radtodv(double x) {
		double PI;
		PI = Math.atan(1) * 4.0;
		return (x * 180 / PI);
	}

	// / <summary>
	// / 计算子午线弧长，北纬计算弧长为正，南纬弧长为负
	// / </summary>
	// / <param name="B">纬度，单位为弧度</param>
	// / <param name="a">椭球长半径</param>
	// / <param name="f">椭球扁率</param>
	// / <returns></returns>
	private static double meridianDis(double B, double a, double f) {
		double A1, B1, C1, D1, E1, F1;
		double e1, e12, e14, e16, e18, e10, e102;
		double b;
		double sin2, sin4, sin6, sin8, sin10;
		double s;

		b = cal_b(a, f);// z

		e1 = cal_e(a, b, 1);
		e12 = e1 * e1;
		e14 = e12 * e12;
		e16 = e12 * e14;
		e18 = e14 * e14;
		e10 = e18 * e12;
		e102 = e12 * e10;

		A1 = 1 + 0.75 * e12 + 45.0 / 64 * e14 + 175.0 / 256 * e16 + 11025.0
				/ 16384 * e18 + 43659.0 / 65536 * e102;
		B1 = 0.75 * e12 + 15.0 / 16 * e14 + 525.0 / 512 * e16 + 2205.0 / 2048
				* e18 + 72765.0 / 65536 * e102;
		C1 = 15.0 / 64 * e14 + 105.0 / 256 * e16 + 2205.0 / 4096 * e18
				+ 10395.0 / 16384 * e102;
		D1 = 35.0 / 512 * e16 + 315.0 / 2048 * e18 + 31185.0 / 131072 * e102;
		E1 = 315.0 / 16384 * e18 + 3465.0 / 65536 * e102;
		F1 = 693.0 / 131072 * e102;
		sin2 = Math.sin(2 * B);
		sin4 = Math.sin(4 * B);
		sin6 = Math.sin(6 * B);
		sin8 = Math.sin(8 * B);
		sin10 = Math.sin(10 * B);
		s = a
				* (1 - e12)
				* (A1 * B - 0.5 * B1 * sin2 + 0.25 * C1 * sin4 - D1 * sin6 / 6
						+ E1 * sin8 / 8 - F1 * sin10 / 10);
		return s;
	}

	// / <summary>
	// / 计算经度longitude(单位纬度的小数形式)所对应的中央子午线经度(度的小数形式)
	// / </summary>
	// / <param name="longitude">输入经度 单位:度的小数形式</param>
	// / <param name="rlon">返回中央子午线经度</param>
	// / <param name="id">所需要计算是三度带还是六度带,三度带3，六度带6</param>
	// / <returns></returns>
	private static int lonofcentralmeridian(double longitude, int id) {

		int n;
		double lon1;

		if (id == 6) {
			n = (int) Math.floor(longitude / 6);
			lon1 = (longitude % 6);
			if (lon1 > 0)
				n = n + 1;
			_rlon = 6 * n - 3;

		} else // if (id == 3)
		{
			n = (int) Math.floor((longitude - 1.5) / 3);
			lon1 = ((longitude - 1) % 3);
			if (lon1 > 0)
				n = n + 1;
			_rlon = 3 * n;

		}

		return n;
	}
}
