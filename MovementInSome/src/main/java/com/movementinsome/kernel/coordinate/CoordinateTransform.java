package com.movementinsome.kernel.coordinate;

public class CoordinateTransform {

	private CoordType _coordTypeSource;// 源坐标类型
	private CoordType _coordTypeTarget;// 目标坐标类型
	private EllipType _ellipTypeSource;// 源坐标椭球基准
	private EllipType _ellipTypeTarget;// 目标坐标椭球基准
	private ParmsType _parmsType;// 参数类型

	private Beijing54 _beijing54;
	private Xian80 _xian80;
	private WGS84 _WGS84;

	public SevenParams _sevenParams;
	public ThreeParams _threeParams;

	private int zoneWidth = 3;// 3度分带

	public CoordinateTransform(CoordType coordTypeSource,
			CoordType coordTypeTarget, EllipType ellipTypeSource,
			EllipType ellipTypeTarget, ParmsType parmsType,
			SevenParams sevenParams, ThreeParams threeParams) {
		this._coordTypeSource = coordTypeSource;
		this._coordTypeTarget = coordTypeTarget;
		this._ellipTypeSource = ellipTypeSource;
		this._ellipTypeTarget = ellipTypeTarget;
		this._parmsType = parmsType;
		this._sevenParams = sevenParams;
		this._threeParams = threeParams;
		
		_WGS84 = new WGS84();
		_beijing54 = new Beijing54();
	/*	东区
	 * _beijing54.setEast_off(446941.4897788100);
		_beijing54.setNorth_off(-1460947.1250425500);*/
		
		_beijing54.setEast_off(500000);
		_beijing54.setNorth_off(0);
	}

	
	public CoordType getCoordTypeSource() {
		return _coordTypeSource;
	}


	public void setCoordTypeSource(CoordType _coordTypeSource) {
		this._coordTypeSource = _coordTypeSource;
	}


	public CoordType getCoordTypeTarget() {
		return _coordTypeTarget;
	}


	public void setCoordTypeTarget(CoordType _coordTypeTarget) {
		this._coordTypeTarget = _coordTypeTarget;
	}


	public EllipType getEllipTypeSource() {
		return _ellipTypeSource;
	}


	public void setEllipTypeSource(EllipType _ellipTypeSource) {
		this._ellipTypeSource = _ellipTypeSource;
	}


	public EllipType getEllipTypeTarget() {
		return _ellipTypeTarget;
	}


	public void setEllipTypeTarget(EllipType _ellipTypeTarget) {
		this._ellipTypeTarget = _ellipTypeTarget;
	}


	public ParmsType getParmsType() {
		return _parmsType;
	}


	public void setParmsType(ParmsType _parmsType) {
		this._parmsType = _parmsType;
	}


	public int getZoneWidth() {
		return zoneWidth;
	}


	public void setZoneWidth(int zoneWidth) {
		this.zoneWidth = zoneWidth;
	}


	// 平面坐标向大地坐标转换
	public Blh plane2geo(double x, double y) {
		Blh blh = new Blh(0, 0, 0);
		/*
		 * double x = Convert.ToDouble(txtXSource.Text.Trim()); double y =
		 * Convert.ToDouble(txtYSource.Text.Trim());
		 */
		double B = 0, L = 0;

		// 北京54坐标系
		if (_ellipTypeSource == EllipType.Beijing54
				&& _ellipTypeTarget == EllipType.Beijing54) {
			// 高斯反算
			blh = _beijing54.gaussPrjInvCalculate(x, y, zoneWidth);
		}
		// 西安80坐标系
		else if (_ellipTypeSource == EllipType.Xian80
				&& _ellipTypeTarget == EllipType.Xian80) {
			blh = _xian80.gaussPrjInvCalculate(x, y, zoneWidth);
		}
		// WGS84坐标系
		else if (_ellipTypeSource == EllipType.WGS84
				&& _ellipTypeTarget == EllipType.WGS84) {
			blh = _WGS84.gaussPrjInvCalculate(x, y, zoneWidth);
		}

		/*
		 * txtXTarget.Text = FormatConvert.DD2DMS(B); txtYTarget.Text =
		 * FormatConvert.DD2DMS(L); txtZTarget.Text = txtZSource.Text;
		 */

		return blh;
	}

	// 大地坐标向平面坐标转换
	public Xyh geo2plane(double B, double L) {
		Xyh xyh = new Xyh(0, 0, 0);
		B = FormatConvert.d2dd(B);
		L = FormatConvert.d2dd(L);
		/*
		 * double B = FormatConvert.DMS2DD(txtXSource.Text.Trim()); double L =
		 * FormatConvert.DMS2DD(txtYSource.Text.Trim());
		 */
		double x = 0, y = 0;

		// 北京54坐标系
		if (_ellipTypeSource == EllipType.Beijing54
				&& _ellipTypeTarget == EllipType.Beijing54) {
			xyh = _beijing54.gaussPrjCalculate(B, L, zoneWidth);
		}
		// 西安80坐标系
		else if (_ellipTypeSource == EllipType.Xian80
				&& _ellipTypeTarget == EllipType.Xian80) {
			xyh = _xian80.gaussPrjCalculate(B, L, zoneWidth);
		}
		// WGS84坐标系
		else if (_ellipTypeSource == EllipType.WGS84
				&& _ellipTypeTarget == EllipType.WGS84) {
			xyh = _WGS84.gaussPrjCalculate(B, L, zoneWidth);
		}

		/*
		 * txtXTarget.Text = x.ToString(); txtYTarget.Text = y.ToString();
		 * txtZTarget.Text = txtZSource.Text;
		 */

		return xyh;
	}

	// 大地坐标向空间直角坐标转换
	public Xyz geo2rect(double B, double L, double H) {
		Xyz xyz = new Xyz(0, 0, 0);
		String sb = FormatConvert.dd2dms(B);
		System.out.print(sb);
		B = FormatConvert.d2dd(B);
		L = FormatConvert.d2dd(L);
		/*
		 * double B = FormatConvert.DMS2DD(txtXSource.Text.Trim());
		 *  double L = FormatConvert.DMS2DD(txtYSource.Text.Trim()); 
		 *  double H =
		 * Convert.ToDouble(txtZSource.Text.Trim());
		 */
		double X = 0, Y = 0, Z = 0;

		// 北京54坐标系
		if (_ellipTypeSource == EllipType.Beijing54
				&& _ellipTypeTarget == EllipType.Beijing54) {
			xyz = _beijing54.blh2xyz(B, L, H);
		}
		// 西安80坐标系
		else if (_ellipTypeSource == EllipType.Xian80
				&& _ellipTypeTarget == EllipType.Xian80) {
			xyz = _xian80.blh2xyz(B, L, H);
		}
		// WGS84坐标系
		else if (_ellipTypeSource == EllipType.WGS84
				&& _ellipTypeTarget == EllipType.WGS84) {
			xyz = _WGS84.blh2xyz(B, L, H);
		}

		/*
		 * txtXTarget.Text = X.ToString(); txtYTarget.Text = Y.ToString();
		 * txtZTarget.Text = Z.ToString();
		 */
		return xyz;
	}

	// 空间直角坐标向大地坐标转换
	public Blh rect2geo(double X, double Y, double Z) {
		Blh blh = new Blh(0, 0, 0);
		/*
		 * double X = Convert.ToDouble(txtXSource.Text.Trim()); double Y =
		 * Convert.ToDouble(txtYSource.Text.Trim()); double Z =
		 * Convert.ToDouble(txtZSource.Text.Trim());
		 */
		double B = 0, L = 0, H = 0;

		// 北京54坐标系
		if (_ellipTypeSource == EllipType.Beijing54
				&& _ellipTypeTarget == EllipType.Beijing54) {
			blh = _beijing54.xyz2blh(X, Y, Z);
		}
		// 西安80坐标系
		else if (_ellipTypeSource == EllipType.Xian80
				&& _ellipTypeTarget == EllipType.Xian80) {
			blh = _xian80.xyz2blh(X, Y, Z);
		}
		// WGS84坐标系
		else if (_ellipTypeSource == EllipType.WGS84
				&& _ellipTypeTarget == EllipType.WGS84) {
			blh = _WGS84.xyz2blh(X, Y, Z);
		}

		/*
		 * txtXTarget.Text = FormatConvert.DD2DMS(B); txtYTarget.Text =
		 * FormatConvert.DD2DMS(L); txtZTarget.Text = H.ToString();
		 */
		return blh;
	}

	// 空间直角坐标向空间直角坐标转换
	public Xyz rect2rect(double X1, double Y1, double Z1) {
		Xyz xyz = new Xyz(0, 0, 0);
		/*
		 * double X1 = Convert.ToDouble(txtXSource.Text.Trim()); double Y1 =
		 * Convert.ToDouble(txtYSource.Text.Trim()); double Z1 =
		 * Convert.ToDouble(txtZSource.Text.Trim());
		 */
		double X2 = 0, Y2 = 0, Z2 = 0;

		if (_parmsType == ParmsType.SevenParms && _sevenParams != null) {
			xyz = CoordTrans.coordTrandBySevenParams(_sevenParams, X1, Y1, Z1);
		} else if (_parmsType == ParmsType.ThreeParms && _threeParams != null) {
			xyz = CoordTrans.coordTrandByThreeParams(_threeParams, X1, Y1, Z1);
		}

		/*
		 * txtXTarget.Text = X2.ToString(); txtYTarget.Text = Y2.ToString();
		 * txtZTarget.Text = Z2.ToString();
		 */
		return xyz;
	}

	// 平面坐标向空间坐标转换
	public Xyz plane2rect(double x, double y, double h) {
		Xyz xyz = new Xyz(0, 0, 0);
		Blh blh = new Blh(0, 0, 0);
		/*
		 * double x = Convert.ToDouble(txtXSource.Text.Trim()); double y =
		 * Convert.ToDouble(txtYSource.Text.Trim()); double h =
		 * Convert.ToDouble(txtZSource.Text.Trim());
		 */
		double B = 0, L = 0, H = h, X = 0, Y = 0, Z = 0;

		// 北京54坐标系
		if (_ellipTypeSource == EllipType.Beijing54
				&& _ellipTypeTarget == EllipType.Beijing54) {
			// 高斯反算
			blh = _beijing54.gaussPrjInvCalculate(x, y, zoneWidth);
			xyz = _beijing54.blh2xyz(blh.getB(), blh.getL(),blh.getH());
		}
		// 西安80坐标系
		else if (_ellipTypeSource == EllipType.Xian80
				&& _ellipTypeTarget == EllipType.Xian80) {
			blh = _xian80.gaussPrjInvCalculate(x, y, zoneWidth);
			xyz = _xian80.blh2xyz(blh.getB(), blh.getL(),blh.getH());
		}
		// WGS84坐标系
		else if (_ellipTypeSource == EllipType.WGS84
				&& _ellipTypeTarget == EllipType.WGS84) {
			blh = _WGS84.gaussPrjInvCalculate(x, y, zoneWidth);
			xyz = _WGS84.blh2xyz(blh.getB(), blh.getL(),blh.getH());
		}

		/*
		 * txtXTarget.Text = X.ToString(); txtYTarget.Text = Y.ToString();
		 * txtZTarget.Text = Z.ToString();
		 */
		return xyz;
	}

	// 空间坐标向平面坐标转换
	public Xyh rect2plane(double X, double Y, double Z) {
		Xyh xyh = new Xyh(0, 0, 0);
		Blh blh = new Blh(0, 0, 0);
		/*
		 * double X = Convert.ToDouble(txtXSource.Text.Trim()); double Y =
		 * Convert.ToDouble(txtYSource.Text.Trim()); double Z =
		 * Convert.ToDouble(txtZSource.Text.Trim());
		 */
		double B = 0, L = 0, H = 0, x = 0, y = 0, h = 0;

		// 北京54坐标系
		if (_ellipTypeSource == EllipType.Beijing54
				&& _ellipTypeTarget == EllipType.Beijing54) {
			blh = _beijing54.xyz2blh(X, Y, Z);
			xyh = _beijing54.gaussPrjCalculate(blh.getB(), blh.getL(), zoneWidth);
		}
		// 西安80坐标系
		else if (_ellipTypeSource == EllipType.Xian80
				&& _ellipTypeTarget == EllipType.Xian80) {
			blh = _xian80.xyz2blh(X, Y, Z);
			xyh = _xian80.gaussPrjCalculate(blh.getB(), blh.getL(), zoneWidth);
		}
		// WGS84坐标系
		else if (_ellipTypeSource == EllipType.WGS84
				&& _ellipTypeTarget == EllipType.WGS84) {
			blh = _WGS84.xyz2blh(X, Y, Z);
			xyh = _WGS84.gaussPrjCalculate(blh.getB(), blh.getL(), zoneWidth);
		}

		/*
		 * h = H; txtXTarget.Text = x.ToString(); txtYTarget.Text =
		 * y.ToString(); txtZTarget.Text = h.ToString();
		 */
		return xyh;
	}
	/*
	 * public void tran(){ // 平面坐标向大地坐标转换 if (_coordTypeSource ==
	 * CoordType.PlaneCoord && _coordTypeTarget == CoordType.GeodeticCoord) {
	 * plane2geo(double x,double y); double x =
	 * Convert.ToDouble(txtXSource.Text.Trim()); double y =
	 * Convert.ToDouble(txtYSource.Text.Trim()); double B = 0, L = 0;
	 * 
	 * // 北京54坐标系 if (_ellipTypeSource == EllipType.Beijing54 &&
	 * _ellipTypeTarget == EllipType.Beijing54) { // 高斯反算
	 * _beijing54.GaussPrjInvCalculate(x, y, out B, out L, zoneWidth); } //
	 * 西安80坐标系 else if (_ellipTypeSource == EllipType.Xian80 && _ellipTypeTarget
	 * == EllipType.Xian80) { _xian80.GaussPrjInvCalculate(x, y, out B, out L,
	 * zoneWidth); } // WGS84坐标系 else if (_ellipTypeSource == EllipType.WGS84 &&
	 * _ellipTypeTarget == EllipType.WGS84) { _WGS84.GaussPrjInvCalculate(x, y,
	 * out B, out L, zoneWidth); }
	 * 
	 * txtXTarget.Text = FormatConvert.DD2DMS(B); txtYTarget.Text =
	 * FormatConvert.DD2DMS(L); txtZTarget.Text = txtZSource.Text; } //
	 * 大地坐标向平面坐标转换 else if (_coordTypeSource == CoordType.GeodeticCoord &&
	 * _coordTypeTarget == CoordType.PlaneCoord) { double B =
	 * FormatConvert.DMS2DD(txtXSource.Text.Trim()); double L =
	 * FormatConvert.DMS2DD(txtYSource.Text.Trim()); double x = 0, y = 0;
	 * 
	 * // 北京54坐标系 if (_ellipTypeSource == EllipType.Beijing54 &&
	 * _ellipTypeTarget == EllipType.Beijing54) {
	 * _beijing54.GaussPrjCalculate(B, L, out x, out y, zoneWidth); } // 西安80坐标系
	 * else if (_ellipTypeSource == EllipType.Xian80 && _ellipTypeTarget ==
	 * EllipType.Xian80) { _xian80.GaussPrjCalculate(B, L, out x, out y,
	 * zoneWidth); } // WGS84坐标系 else if (_ellipTypeSource == EllipType.WGS84 &&
	 * _ellipTypeTarget == EllipType.WGS84) { _WGS84.GaussPrjCalculate(B, L, out
	 * x, out y, zoneWidth); }
	 * 
	 * txtXTarget.Text = x.ToString(); txtYTarget.Text = y.ToString();
	 * txtZTarget.Text = txtZSource.Text; gep2plane(double B,double L); } //
	 * 大地坐标向空间直角坐标转换 else if (_coordTypeSource == CoordType.GeodeticCoord &&
	 * _coordTypeTarget == CoordType.RectSpaceCoord) { double B =
	 * FormatConvert.DMS2DD(txtXSource.Text.Trim()); double L =
	 * FormatConvert.DMS2DD(txtYSource.Text.Trim()); double H =
	 * Convert.ToDouble(txtZSource.Text.Trim()); double X = 0, Y = 0, Z = 0;
	 * 
	 * // 北京54坐标系 if (_ellipTypeSource == EllipType.Beijing54 &&
	 * _ellipTypeTarget == EllipType.Beijing54) { _beijing54.BLH2XYZ(B, L, H,
	 * out X, out Y, out Z); } // 西安80坐标系 else if (_ellipTypeSource ==
	 * EllipType.Xian80 && _ellipTypeTarget == EllipType.Xian80) {
	 * _xian80.BLH2XYZ(B, L, H, out X, out Y, out Z); } // WGS84坐标系 else if
	 * (_ellipTypeSource == EllipType.WGS84 && _ellipTypeTarget ==
	 * EllipType.WGS84) { _WGS84.BLH2XYZ(B, L, H, out X, out Y, out Z); }
	 * 
	 * txtXTarget.Text = X.ToString(); txtYTarget.Text = Y.ToString();
	 * txtZTarget.Text = Z.ToString(); geo2rect(double B,double L,double H) } //
	 * 空间直角坐标向大地坐标转换 else if (_coordTypeSource == CoordType.RectSpaceCoord &&
	 * _coordTypeTarget == CoordType.GeodeticCoord) { double X =
	 * Convert.ToDouble(txtXSource.Text.Trim()); double Y =
	 * Convert.ToDouble(txtYSource.Text.Trim()); double Z =
	 * Convert.ToDouble(txtZSource.Text.Trim()); double B = 0, L = 0, H = 0;
	 * 
	 * // 北京54坐标系 if (_ellipTypeSource == EllipType.Beijing54 &&
	 * _ellipTypeTarget == EllipType.Beijing54) { _beijing54.XYZ2BLH(X, Y, Z,
	 * out B, out L, out H); } // 西安80坐标系 else if (_ellipTypeSource ==
	 * EllipType.Xian80 && _ellipTypeTarget == EllipType.Xian80) {
	 * _xian80.XYZ2BLH(X, Y, Z, out B, out L, out H); } // WGS84坐标系 else if
	 * (_ellipTypeSource == EllipType.WGS84 && _ellipTypeTarget ==
	 * EllipType.WGS84) { _WGS84.XYZ2BLH(X, Y, Z, out B, out L, out H); }
	 * 
	 * txtXTarget.Text = FormatConvert.DD2DMS(B); txtYTarget.Text =
	 * FormatConvert.DD2DMS(L); txtZTarget.Text = H.ToString(); rect2geo(double
	 * X,double Y,double Z) } // 空间直角坐标向空间直角坐标转换 else if (_coordTypeSource ==
	 * CoordType.RectSpaceCoord && _coordTypeTarget == CoordType.RectSpaceCoord)
	 * { double X1 = Convert.ToDouble(txtXSource.Text.Trim()); double Y1 =
	 * Convert.ToDouble(txtYSource.Text.Trim()); double Z1 =
	 * Convert.ToDouble(txtZSource.Text.Trim()); double X2 = 0, Y2 = 0, Z2 = 0;
	 * 
	 * if (_parmsType == ParmsType.SevenParms && _sevenParams != null) {
	 * CoordTrans.CoordTrandBySevenParams(_sevenParams, X1, Y1, Z1, out X2, out
	 * Y2, out Z2); } else if (_parmsType == ParmsType.ThreeParms &&
	 * _threeParams != null) { CoordTrans.CoordTrandByThreeParams(_threeParams,
	 * X1, Y1, Z1, out X2, out Y2, out Z2); }
	 * 
	 * txtXTarget.Text = X2.ToString(); txtYTarget.Text = Y2.ToString();
	 * txtZTarget.Text = Z2.ToString(); geo2geo(double X1,double Y1,double Z1) }
	 * // 平面坐标向空间坐标转换 else if (_coordTypeSource == CoordType.PlaneCoord &&
	 * _coordTypeTarget == CoordType.RectSpaceCoord) { double x =
	 * Convert.ToDouble(txtXSource.Text.Trim()); double y =
	 * Convert.ToDouble(txtYSource.Text.Trim()); double h =
	 * Convert.ToDouble(txtZSource.Text.Trim()); double B = 0, L = 0, H = h, X =
	 * 0, Y = 0, Z = 0;
	 * 
	 * // 北京54坐标系 if (_ellipTypeSource == EllipType.Beijing54 &&
	 * _ellipTypeTarget == EllipType.Beijing54) { // 高斯反算
	 * _beijing54.GaussPrjInvCalculate(x, y, out B, out L, zoneWidth);
	 * _beijing54.BLH2XYZ(B, L, H, out X, out Y, out Z); } // 西安80坐标系 else if
	 * (_ellipTypeSource == EllipType.Xian80 && _ellipTypeTarget ==
	 * EllipType.Xian80) { _xian80.GaussPrjInvCalculate(x, y, out B, out L,
	 * zoneWidth); _xian80.BLH2XYZ(B, L, H, out X, out Y, out Z); } // WGS84坐标系
	 * else if (_ellipTypeSource == EllipType.WGS84 && _ellipTypeTarget ==
	 * EllipType.WGS84) { _WGS84.GaussPrjInvCalculate(x, y, out B, out L,
	 * zoneWidth); _WGS84.BLH2XYZ(B, L, H, out X, out Y, out Z); }
	 * 
	 * txtXTarget.Text = X.ToString(); txtYTarget.Text = Y.ToString();
	 * txtZTarget.Text = Z.ToString(); plane2geo(double x,double y,double h); }
	 * // 空间坐标向平面坐标转换 else if (_coordTypeSource == CoordType.RectSpaceCoord &&
	 * _coordTypeTarget == CoordType.PlaneCoord) { double X =
	 * Convert.ToDouble(txtXSource.Text.Trim()); double Y =
	 * Convert.ToDouble(txtYSource.Text.Trim()); double Z =
	 * Convert.ToDouble(txtZSource.Text.Trim()); double B = 0, L = 0, H = 0, x =
	 * 0, y = 0, h = 0;
	 * 
	 * // 北京54坐标系 if (_ellipTypeSource == EllipType.Beijing54 &&
	 * _ellipTypeTarget == EllipType.Beijing54) { _beijing54.XYZ2BLH(X, Y, Z,
	 * out B, out L, out H); _beijing54.GaussPrjCalculate(B, L, out x, out y,
	 * zoneWidth); } // 西安80坐标系 else if (_ellipTypeSource == EllipType.Xian80 &&
	 * _ellipTypeTarget == EllipType.Xian80) { _xian80.XYZ2BLH(X, Y, Z, out B,
	 * out L, out H); _xian80.GaussPrjCalculate(B, L, out x, out y, zoneWidth);
	 * } // WGS84坐标系 else if (_ellipTypeSource == EllipType.WGS84 &&
	 * _ellipTypeTarget == EllipType.WGS84) { _WGS84.XYZ2BLH(X, Y, Z, out B, out
	 * L, out H); _WGS84.GaussPrjCalculate(B, L, out x, out y, zoneWidth); }
	 * 
	 * h = H; txtXTarget.Text = x.ToString(); txtYTarget.Text = y.ToString();
	 * txtZTarget.Text = h.ToString(); rect2plane(double X,double Y,double Z); }
	 * }
	 */
}
