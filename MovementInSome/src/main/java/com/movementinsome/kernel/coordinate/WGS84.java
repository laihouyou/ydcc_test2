package com.movementinsome.kernel.coordinate;

public class WGS84 extends CoordTrans {
	public WGS84() {
		a = 6378137.0;
		f = 1.0 / 298.257223563;
	}
}
