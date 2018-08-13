/* Copyright 2010 ESRI
 *
 * All rights reserved under the copyright laws of the United States
 * and applicable international laws, treaties, and conventions.
 *
 * You may freely redistribute and use this sample code, with or
 * without modification, provided you include the original copyright
 * notice and use restrictions.
 *
 * See the sample code usage restrictions document for further information.
 *
 */

package com.movementinsome.map.facedit.vo;

import android.graphics.Bitmap;

import com.esri.core.symbol.Symbol;

public class Legend {
	private Bitmap bitmap;
	private String name;
	private Symbol symbol;
	private String objectid;

	public Legend(Bitmap bitmap, String name, Symbol symbol,String objectid) {
		this.bitmap = bitmap;
		this.name = name;
		this.symbol = symbol;
		this.objectid=objectid;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSymbol(Symbol symbol)
	{
		this.symbol = symbol;
	}
	
	public Symbol getSymbol()
	{
		return symbol;
	}

	public String getObjectid() {
		return objectid;
	}

	public void setObjectid(String objectid) {
		this.objectid = objectid;
	}
	
}
