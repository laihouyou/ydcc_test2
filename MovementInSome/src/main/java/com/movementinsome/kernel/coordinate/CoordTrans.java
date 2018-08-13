package com.movementinsome.kernel.coordinate;


public abstract class CoordTrans {

	final double PI = Math.PI;
	// / <summary>
	// / 长半径
	// / </summary>
	protected double a;

	// / <summary>
	// / 扁率
	// / </summary>
	protected double f;
	
	// / <summary>
		// / 向东偏移量
		// / </summary>
	protected double east_off=500000;

	//向北偏移
	protected double north_off;
	/**
	 * 向东偏移量
	 * @return
	 */
	public double getEast_off() {
		return east_off;
	}

	public void setEast_off(double east_off) {
		this.east_off = east_off;
	}

	
	public double getNorth_off() {
		return north_off;
	}

	public void setNorth_off(double north_off) {
		this.north_off = north_off;
	}

	// / <summary>
	// / 计算子午线的弧长
	// / </summary>// （参见“任意精度的子午线弧长递归计算”）
	// / <param name="a">椭球长轴</param>
	// / <param name="f">椭球扁率</param>
	// / <param name="lat">大地纬度，单位为弧度</param>
	// / <param name="N">递归项数 建议设为5</param>
	// / <returns>返回弧长 单位为米</returns>
	public double getMeridianArcLength(double lat, int N) {
		int i, n, m;
		double e2, ra, c, ff1, k, ff2, sin2;
		double[] k2 = new double[N];
		for (i = 0; i < N; i++) {
			k2[i] = 0.0;
		}
		e2 = f * (2 - f);
		ra = a * (1 - e2);

		for (c = 1.0, n = 1; n <= N; n++) {
			c *= ((2 * n - 1.0) * (2 * n + 1.0) / (4 * n * n)) * e2;
			for (m = 0; m < n; m++) {
				k2[m] += c;
			}
		}
		ff1 = 1.0 + k2[0];
		ff2 = -k2[0];
		sin2 = Math.sin(lat) * Math.sin(lat);

		for (k = 1.0, n = (int) k; n < N; n++) {
			k *= 2 * n / (2 * n + 1.0) * sin2;
			ff2 += -k2[n] * k;
		}
		return ra * (lat * ff1 + 0.5 * ff2 * Math.sin(2.0 * lat));
	}

	// / <summary>
	// / 高期投影正算
	// / 由经纬度（单位：Decimal Degree）正算到大地坐标（单位：Metre，含带号）
	// / </summary>
	// / <param name="B">纬度 弧度制</param>
	// / <param name="L">经度 弧度制</param>
	// / <param name="x">大地坐标X值，其整数部分7位不含带号</param>
	// / <param name="y">大地坐标Y值，其整数部分8为含带号</param>
	// / <param name="zoneWide">分度带 3分度带 或 6分度带</param>
	public Xyh gaussPrjCalculate(double B, double L, int zoneWide) {
		double x, y;
		double eFirstSquare = f * (2 - f);// 第一偏心率的平方
		double eSecondSquare = eFirstSquare / (1 - eFirstSquare);// 第二偏心率的平方

		int num = 0;// 带号
		double L0 = 0;// 中央子午线
		if (zoneWide == 3)// 三分度带
		{
			num = (int) (((L * 180 / Math.PI) + 1.5) / 3);
			L0 = 3 * num / 180.0 * Math.PI;// 计算中央经度
		} else// 六分度带
		{
			num = (int) ((L * 180 / Math.PI) / 6 + 1);
			L0 = (6 * num - 3) / 180.0 * Math.PI;// 计算中央经度
		}
		double l = L0 - L;// 计算经差 弧度制
		double cosB = Math.cos(B);
		double sinB = Math.sin(B);
		double t = Math.tan(B);
		double X = getMeridianArcLength(B, 5);// 获取子午线长度
		double N = a / Math.sqrt(1 - eFirstSquare * sinB * sinB);// 卯酉圈曲率半径
		double n2 = eSecondSquare * cosB * cosB;
		double n4 = n2 * n2;
		double m = cosB * l;
		double Nt = N * t;
		double t2 = Math.pow(t, 2);
		double t4 = Math.pow(t, 4);
		double t6 = Math.pow(t, 6);

		// 计算x值
		double X1 = Nt * Math.pow(m, 2) / 2.0;
		double X2 = Nt * Math.pow(m, 4) * (5 - t2 + 9 * n2 + 4 * n4) / 24.0;
		double X3 = Nt * Math.pow(m, 6)
				* (61 - 58 * t2 + t4 + 270 * n2 - 330 * t2 * n2) / 720.0;
		double X4 = Nt * Math.pow(m, 8) * (1385 - 3111 * t2 + 543 * t4 - t6)
				/ 40320.0;
		x = X1 + X2 + X3 + X4;
		x += X;// 加上子午线长度

		x += this.north_off; //向北偏移量
		
		// 计算y值
		double Y1 = N * m;
		double Y2 = N * Math.pow(m, 3) * (1 - t2 + n2) / 6.0;
		double Y3 = N * Math.pow(m, 5)
				* (5 - 18 * t2 + t4 + 14 * n2 - 58 * t2 * n2) / 120.0;
		double Y4 = N * Math.pow(m, 7) * (61 - 479 * t2 + 179 * t4 - t6)
				/ 5040.0;
		y = Y1 + Y2 + Y3 + Y4;
		y = this.east_off - y;// 左移500Km
		//y += num * 1000000.00;// 加上带号

		Xyh xyh = new Xyh(x, y, 0);

		return xyh;
	}

	// / <summary>
	// / 高斯投影反算
	// / </summary>// 大地坐标（单位：Metre，含带号）反算到经纬度坐标（单位，Decimal Degree）
	// / <param name="X">大地坐标X值，其整数部分7位不含带号</param>
	// / <param name="y">大地坐标Y值，其整数部分8为含带号</param>
	// / <param name="B">纬度 弧度制</param>
	// / <param name="L">经度 弧度制</param>
	// / <param name="zoneWide">分度带 3分度带 或 6分度带</param>
	public Blh gaussPrjInvCalculate(double x, double y, int zoneWide) {
		double B, L;

		double eFirstSquare = f * (2 - f);// 第一偏心率的平方
		double b = Math.sqrt(a * a * (1 - eFirstSquare));// 计算短轴
		double c = a * a / b;// 极曲率半径
		double eSecond = Math.sqrt((a * a - b * b)) / b;// 计算第二偏心率
		double L0 = 0;// 中央子午线

		// 计算y实际值（即去除带号再减去500Km）
		int num = (int) y / 1000000;// 计算带号
		y = y - num * 1000000 - this.east_off;// 500000;// 不加带号的Y值

		// 根据分带方式计算中央子午线
		if (zoneWide == 3) {
			L0 = num * 3 * PI / 180;// 以弧度制表示
		} else {
			L0 = (6 * num - 3) * PI / 180;
		}

		double m0 = a * (1 - eFirstSquare);// 即b*b/a
		double m2 = 3.0 / 2.0 * eFirstSquare * m0;
		double m4 = 5.0 / 4.0 * eFirstSquare * m2;
		double m6 = 7.0 / 6.0 * eFirstSquare * m4;
		double m8 = 9.0 / 8.0 * eFirstSquare * m6;

		double a0 = m0 + m2 / 2.0 + 3.0 / 8.0 * m4 + 5.0 / 16.0 * m6 + 35.0
				/ 128.0 * m8;
		double a2 = m2 / 2 + m4 / 2 + 15.0 / 32.0 * m6 + 7.0 / 16.0 * m8;
		double a4 = m4 / 8.0 + 3.0 / 16.0 * m6 + 7.0 / 32.0 * m8;
		double a6 = m6 / 32.0 + m8 / 16.0;
		double a8 = m8 / 128.0;

		// 迭代计算地点纬度Bf
		double Bf = x / a0;
		double tempBf = 0.0;
		while (Math.abs(Bf - tempBf) > 1E-12) {
			tempBf = Bf;
			double sb = Math.sin(tempBf);
			double cb = Math.cos(tempBf);
			double s2b = sb * cb * 2;
			double s4b = s2b * (1 - 2 * sb * sb) * 2;
			double s6b = s2b * Math.sqrt(1 - s4b * s4b) + s4b
					* Math.sqrt(1 - s2b * s2b);

			Bf = (x - (-a2 / 2.0 * s2b + a4 / 4.0 * s4b - a6 / 6.0 * s6b)) / a0;
		}

		double cosBf = Math.cos(Bf);
		double sinBf = Math.sin(Bf);
		double nf = eSecond * cosBf;
		double nf2 = Math.pow(nf, 2);

		double tf = Math.tan(Bf);
		double tf2 = Math.pow(tf, 2);
		double tf4 = Math.pow(tf, 4);

		double Vf = Math.sqrt(1 + eSecond * eSecond * cosBf * cosBf);
		double Vf2 = Math.pow(Vf, 2);

		double Nf = c / Vf;// P102
		double yNf = y / Nf;
		double yNf2 = Math.pow(yNf, 2);
		double yNf3 = Math.pow(yNf, 3);
		double yNf4 = Math.pow(yNf, 4);
		double yNf5 = Math.pow(yNf, 5);
		double yNf6 = Math.pow(yNf, 6);

		B = Bf
				- 1.0
				/ 2.0
				* Vf2
				* tf
				* (yNf2 - 1.0 / 12.0 * yNf4
						* (5 + 3 * tf2 + nf2 - 9 * nf2 * tf2) + 1.0 / 360.0
						* (61 + 90 * tf2 + 45 * tf4) * yNf6);
		L = (yNf - (1 + 2 * tf2 + nf2) * yNf3 / 6.0 + (5 + 28 * tf2 + 24 * tf4
				+ 6 * nf2 + 8 * nf2 * tf2)
				* yNf5 / 120.0)
				/ cosBf;
		L = L + L0;

		Blh blh = new Blh(B, L, 0);
		return blh;
	}
	
	 /// <summary>
    /// 大地坐标转换为空间直角坐标
    /// </summary>
    /// <param name="B">纬度 弧度制</param>
    /// <param name="L">经度 弧度制</param>
    /// <param name="H">高程 单位 米</param>        
    /// <param name="X">X值 单位 米</param>
    /// <param name="Y">Y值 单位 米</param>
    /// <param name="Z">Z值 单位 米</param>  
    public Xyz blh2xyz(double B, double L, double H)
    {
    	double X = 0,  Y = 0, Z = 0;   	
        double e2 = f * (2 - f);
        double sinB = Math.sin(B);
        double cosB = Math.cos(B);
        double sinL = Math.sin(L);
        double cosL = Math.cos(L);
        double N = a / Math.sqrt(1 - e2 * sinB * sinB);

        X = (N + H) * cosB * cosL;
        Y = (N + H) * cosB * sinL;
        Z = ((1 - e2) * N + H) * sinB;
        
        Xyz xyz = new Xyz(X,Y,Z);
        return xyz;
    }
    
  /// <summary>
    /// 空间直角坐标转换为大地坐标
    /// </summary>
    /// <param name="X">X值 单位 米</param>
    /// <param name="Y">Y值 单位 米</param>
    /// <param name="Z">Z值 单位 米</param>        
    /// <param name="B">纬度 弧度制</param>
    /// <param name="L">经度 弧度制</param>
    /// <param name="H">高程 单位 米</param>
    public Blh xyz2blh(double X, double Y, double Z)
    {
    	double B, L, H;
    	
        double eFirstSquare = f * (2 - f);// 第一偏心率的平方
        double xy = Math.sqrt(X * X + Y * Y);
        double B0 = Math.atan(Z / xy);
        double N = a / Math.sqrt(1 - eFirstSquare * Math.sin(B0) * Math.sin(B0));// 卯酉圈曲率半径
        double H0 = xy / Math.cos(B0) - N;
        do
        {
            // 迭代计算B
            B = B0;
            B0 = Math.atan((Z + N * eFirstSquare * Math.sin(B0)) / xy);
            // 迭代计算H
            H = H0;
            N = a / Math.sqrt(1 - eFirstSquare * Math.sin(B0) * Math.sin(B0));
            H0 = xy / Math.cos(B0) - N;
        } while (Math.abs(B - B0) > 0.000000000001 && Math.abs(H - H0) > 0.00000000001);

        L = Math.PI + Math.atan(Y / X);
        
        Blh blh = new Blh(B,L,H);
        
        return blh;

    }
    
    /// <summary>
    /// 计算三参数
    /// </summary>
    /// <param name="value"></param>
    /// <param name="rowCount"></param>
    /// <returns></returns>
    public static ThreeParams calculateThreeParams(double[][] value, int rowCount)
    {
        ThreeParams threeParams = new ThreeParams();

        for (int i = 0; i < rowCount; i++)
        {
            threeParams.setDx(value[i][3] - value[i][0]);
            threeParams.setDx(value[i][4] - value[i][1]);
            threeParams.setDz(value[i][5] - value[i][2]);
        }
        threeParams.dx /= rowCount;
        threeParams.dy /= rowCount;
        threeParams.dz /= rowCount;

        return threeParams;
    }
    
  /// <summary>
    /// 三参转换
    /// </summary>
    /// <param name="threeParams">三参数</param>
    /// <param name="X1">源坐标X</param>
    /// <param name="Y1">源坐标Y</param>
    /// <param name="Z1">源坐标Z</param>
    /// <param name="X2">目标坐标X</param>
    /// <param name="Y2">目标坐标Y</param>
    /// <param name="Z2">目标坐标Z</param>
    public static Xyz coordTrandByThreeParams(ThreeParams threeParams, double X1, double Y1, double Z1)
    {
    	double X2, Y2, Z2;
    	
        X2 = X1 + threeParams.dx;
        Y2 = Y1 + threeParams.dy;
        Z2 = Z1 + threeParams.dz;
        
        Xyz xyz = new Xyz(X2,Y2,Z2);
        return xyz;
    }
    
    /// <summary>
    /// 计算七参数
    /// </summary>
    /// <param name="value"></param>
    /// <param name="rowCount"></param>
    /// <returns></returns>
    public static SevenParams calculateSevenParams(double[][] value, int rowCount)
    {
        SevenParams sevenParams = new SevenParams();
        // 构造L=B*X
        Matrix B = new Matrix(3 * rowCount, 7);
        Matrix L = new Matrix(3 * rowCount, 1);
        for (int i = 0; i < rowCount; i++)
        {
            double[] a = new double[] { 1, 0, 0, value[i][0], 0, -value[i][2], value[i][1] };
            double[] b = new double[] { 0, 1, 0, value[i][1], value[i][2], 0, -value[i][0] };
            double[] c = new double[] { 0, 0, 1, value[i][2], -value[i][1], value[i][0], 0 };
            B.setRow(3 * i + 0, a);
            B.setRow(3 * i + 1, b);
            B.setRow(3 * i + 2, c);
            L.setElement(3 * i + 0, 0, value[i][3]);
            L.setElement(3 * i + 1, 0, value[i][4]);
            L.setElement(3 * i + 2, 0, value[i][5]);
        }
        Matrix X = new Matrix(7, 1);
        Matrix BT = B.T();
        X = Matrix._mul(Matrix._mul(Matrix._mul(BT,B).inv() ,BT), L);
        sevenParams.dx = -X.get(0, 0);
        sevenParams.dy = -X.get(1, 0);
        sevenParams.dz = -X.get(2, 0);
        sevenParams.m = 1 - X.get(3, 0);
        sevenParams.rx = -X.get(4, 0) / X.get(3, 0);
        sevenParams.ry = -X.get(5, 0) / X.get(3, 0);
        sevenParams.rz = -X.get(6, 0) / X.get(3, 0);

        return sevenParams;
    }
    
    /// <summary>
    /// 七参转换
    /// </summary>
    /// <param name="threeParams">七参数</param>
    /// <param name="X1">源坐标X</param>
    /// <param name="Y1">源坐标Y</param>
    /// <param name="Z1">源坐标Z</param>
    /// <param name="X2">目标坐标X</param>
    /// <param name="Y2">目标坐标Y</param>
    /// <param name="Z2">目标坐标Z</param>
    public static Xyz coordTrandBySevenParams(SevenParams sevenParams, double X1, double Y1, double Z1)
    {
    	double X2, Y2, Z2;
    	double Rou = 180.0 / Math.PI * 3600.0;
/*    	
        Para[0] = param.getM_ITI_S_DX();//平移
        Para[1] = param.getM_ITI_S_DY(); //平移
        Para[2] = param.getM_ITI_S_DZ(); //平移
        Para[3] = param.getM_ITI_S_Scale(); //1000000;//比例因子
        Para[4] = param.getM_ITI_S_QX();//旋转
        Para[5] = param.getM_ITI_S_QY(); //旋转
        Para[6] = param.getM_ITI_S_QZ(); //旋转
*/        
        X2 = sevenParams.dx + X1 * (1 + sevenParams.m) + Y1 * sevenParams.rz / Rou - Z1 * sevenParams.ry / Rou;//(1 + sevenParams.m) * (X1 + sevenParams.rz * Y1 - sevenParams.ry * Z1) + sevenParams.dx;
        Y2 = sevenParams.dy + Y1 * (1 + sevenParams.m) - X1 * sevenParams.rz / Rou + Z1 * sevenParams.rx / Rou;//(1 + sevenParams.m) * (-sevenParams.rz * X1 + Y1 + sevenParams.rx * Z1) + sevenParams.dy;
        Z2 = sevenParams.dz + Z1 * (1 + sevenParams.m) + X1 * sevenParams.ry / Rou - Y1 * sevenParams.rx / Rou;//(1 + sevenParams.m) * (sevenParams.ry * X1 - sevenParams.rx * Y1 + Z1) + sevenParams.dz;
        
        
     /*   PI = Math.atan(1) * 4.0;
        Rou = 180.0 / PI * 3600.0;
        _x = Para[0] + X0 * (1 + Para[3]) + Y0 * Para[6] / Rou - Z0 * Para[5] / Rou;
        _y = Para[1] + Y0 * (1 + Para[3]) - X0 * Para[6] / Rou + Z0 * Para[4] / Rou;
        _z = Para[2] + Z0 * (1 + Para[3]) + X0 * Para[5] / Rou - Y0 * Para[4] / Rou;*/
        
        Xyz xyz = new Xyz(X2,Y2,Z2);
        return xyz;
    }
}
