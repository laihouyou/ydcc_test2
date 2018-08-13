package com.movementinsome.map.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.MotionEvent;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.movementinsome.R;
import com.movementinsome.map.view.MyMapView;

public class DrawGraphicTools {
	public static enum DRAW_GRAPHIC_TYPE {
		PT, FRE_PL, FRE_PG, PL, PG, CIRCLE, ENV, ELLIPSE
	}
	
//	PL 直线
//	PG 多边形
//	ENV 矩形
//	CIRCLE 圆形
	
	private boolean isLongPress = false;
	
	private PictureMarkerSymbol pointSymbol;
	
	private Resources res;
	
	private boolean isFristMove = true;
	
	private GraphicsLayer grapPayer;//点

	private DRAW_GRAPHIC_TYPE drawType;

	private GraphicsLayer tempDrawGpLayer;

	private Context context;

	private MyMapView map;
	
	private MyTouchListener touchListener;

	public DrawGraphicTools(Context context, MyMapView map) {
		this.context = context;
		this.map = map;
		res = context.getResources();

	}

	public DRAW_GRAPHIC_TYPE getDrawType() {
		return drawType;
	}

	public void setDrawType(DRAW_GRAPHIC_TYPE drawType) {
		this.drawType = drawType;
		if (touchListener != null){
			touchListener.rest();
		}
	}

	public void draw(DRAW_GRAPHIC_TYPE drawType) {
		this.drawType = drawType;
		tempDrawGpLayer = new GraphicsLayer();
		map.addLayer(tempDrawGpLayer);

		touchListener = new MyTouchListener(context, map.getMapView());
		map.setOnTouchListener(touchListener);
	}

	public void clear() {
		tempDrawGpLayer.removeAll();
		if (touchListener != null){
			touchListener.rest();
		}
	}

	public void stop() {
		drawType = null;
	}

	public void exit() {
		if (drawType != null) {
			drawType = null;
			tempDrawGpLayer.removeAll();
			try{
				map.removeLayer(tempDrawGpLayer);
			}catch(Exception ex){
				;//地图重新加载后，因为不存该图层，所以不保护时移除会出错
			}
			map.resetTouchListener();
		}
	}

	/*
	 * MapView's touch listener
	 */
	class MyTouchListener extends MapOnTouchListener {
		// ArrayList<Point> polylinePoints = new ArrayList<Point>();
		int uid = 0;
		MultiPath poly;
		// String type = "";
		Point startPoint = null;
		private Polyline polyline = new Polyline();
		private Polygon polygon = new Polygon();
		private Envelope envelope = new Envelope();
		
		public MyTouchListener(Context context, MapView view) {
			super(context, view);
		}

		/*
		 * public void setType(String geometryType) { this.type = geometryType;
		 * }
		 * 
		 * public String getType() { return this.type; }
		 */

		/*
		 * Invoked when user single taps on the map view. This event handler
		 * draws a point at user-tapped location, only after "Draw Point" is
		 * selected from Spinner.
		 * 
		 * @see
		 * com.esri.android.map.MapOnTouchListener#onSingleTap(android.view.
		 * MotionEvent)
		 */
		public void onLongPress(MotionEvent point) {
			isLongPress=true;
		}
		
		public boolean onSingleTap(MotionEvent e) {
			if (drawType!=null && (drawType == DRAW_GRAPHIC_TYPE.PT
					|| drawType == DRAW_GRAPHIC_TYPE.PL
					|| drawType == DRAW_GRAPHIC_TYPE.PG)) {
				Graphic graphic = null;
				Point point = map.toMapPoint(new Point(e.getX(), e.getY()));

				if(point!=null){
					switch (drawType) {
					case PT:
						graphic = new Graphic(point, new SimpleMarkerSymbol(
								Color.RED, 10, STYLE.CIRCLE));
						// graphic.setGeometry();
						tempDrawGpLayer.addGraphic(graphic);

						return true;
						// break;
					case PL:
						if (startPoint == null) {
							startPoint = point;
							polyline.startPath(point);
							pointSymbol = new PictureMarkerSymbol(res.getDrawable(R.drawable.dot0));
							Graphic grahic = new Graphic(point,pointSymbol);

							graphic = new Graphic(startPoint, new SimpleLineSymbol(
									Color.RED, 5));

							/*
							 * add the updated graphic to graphics layer
							 */
							uid = tempDrawGpLayer.addGraphic(graphic);
							int uids = tempDrawGpLayer.addGraphic(grahic);
						} else {
							polyline.lineTo(point);
							tempDrawGpLayer.updateGraphic(uid, polyline);
						}
							pointSymbol = new PictureMarkerSymbol(res.getDrawable(R.drawable.dot0));
							Graphic grahic = new Graphic(point,pointSymbol);
							int uids = tempDrawGpLayer.addGraphic(grahic);
							
						return true;

					case PG:
						if (startPoint == null) {
							startPoint = point;
							polygon.startPath(point);
							pointSymbol = new PictureMarkerSymbol(res.getDrawable(R.drawable.dot0));
							Graphic grahicd = new Graphic(point,pointSymbol);
							graphic = new Graphic(startPoint, new SimpleLineSymbol(
									Color.RED, 5));

							/*
							 * add the updated graphic to graphics layer
							 */
							uid = tempDrawGpLayer.addGraphic(graphic);
							int uidd = tempDrawGpLayer.addGraphic(grahicd);
						} else {
							polygon.lineTo(point);
							tempDrawGpLayer.updateGraphic(uid, polygon);
						}
							pointSymbol = new PictureMarkerSymbol(res.getDrawable(R.drawable.dot0));
							Graphic grahicd = new Graphic(point,pointSymbol);
							int uidd = tempDrawGpLayer.addGraphic(grahicd);
						
						return true;
					}
				}
				
			}

			return false;

		}

		public boolean onDoubleTap(MotionEvent event) {
			if (drawType!=null &&(drawType == DRAW_GRAPHIC_TYPE.PL
					|| drawType == DRAW_GRAPHIC_TYPE.PG)){
				if (startPoint != null) {
					Point point = map.toMapPoint(event.getX(), event.getY());
					//if (drawType == DRAW_GRAPHIC_TYPE.PL
					//		|| drawType == DRAW_GRAPHIC_TYPE.PG) {
						switch (drawType) {
						case PG:
							polygon.lineTo(point);
							tempDrawGpLayer.updateGraphic(uid, polygon);
							
							/*BufferQueryTools nbTools = new BufferQueryTools(context,map);
							nbTools.queryByPolyLine(polygon, 50, null);*/
							break;
						case PL:
							polyline.lineTo(point);
							tempDrawGpLayer.updateGraphic(uid, polyline);
							
							break;
						}
						// sendDrawEndEvent();
						startPoint = null;
					//}
				}
			}
			return true;
		}

		/*
		 * Invoked when user drags finger across screen. Polygon or Polyline is
		 * drawn only when right selected is made from Spinner
		 * 
		 * @see
		 * com.esri.android.map.MapOnTouchListener#onDragPointerMove(android
		 * .view.MotionEvent, android.view.MotionEvent)
		 */
		public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
			if(isLongPress){
					return super.onDragPointerMove(from, to);
			}
			if (drawType != null && (drawType == DRAW_GRAPHIC_TYPE.FRE_PL
						|| drawType == DRAW_GRAPHIC_TYPE.FRE_PG
						|| drawType == DRAW_GRAPHIC_TYPE.CIRCLE
						|| drawType == DRAW_GRAPHIC_TYPE.ENV)) {

					Point mapPt = map.toMapPoint(to.getX(), to.getY());

					/*
					 * if StartPoint is null, create a polyline and start a
					 * path.
					 */
					if (startPoint == null) {
						// tempDrawGpLayer.removeAll();
						Graphic graphic = null;
						startPoint = map.toMapPoint(from.getX(),from.getY());
						
						switch (drawType) {
						case FRE_PL:
						case FRE_PG:
							poly = drawType == DRAW_GRAPHIC_TYPE.FRE_PL ? new Polyline()
									: new Polygon();
							
							poly.startPath((float) startPoint.getX(),
									(float) startPoint.getY());

							/*
							 * Create a Graphic and add polyline geometry
							 */
							graphic = new Graphic(startPoint,
									new SimpleLineSymbol(Color.RED, 5));

							/*
							 * add the updated graphic to graphics layer
							 */
							uid = tempDrawGpLayer.addGraphic(graphic);
							break;
						case CIRCLE:
								double radius = Math
										.sqrt(Math.pow(
												startPoint.getX() - mapPt.getX(), 2)
												+ Math.pow(startPoint.getY()
														- mapPt.getY(), 2));
								getCircle(startPoint, radius, polygon);
								graphic = new Graphic(startPoint,
										new SimpleLineSymbol(Color.RED, 5));
	
								/*
								 * add the updated graphic to graphics layer
								 */
								uid = tempDrawGpLayer.addGraphic(graphic);
							
							break;
						}
					}
					
						if(drawType == DRAW_GRAPHIC_TYPE.CIRCLE){
							if(isFristMove){
								isFristMove = false;
								Graphic graphic = new Graphic(startPoint,
										new SimpleLineSymbol(Color.RED, 5));
								uid = tempDrawGpLayer.addGraphic(graphic);
								startPoint = map.toMapPoint(from.getX(),from.getY());
								}
							
							double radius = Math
									.sqrt(Math.pow(
											startPoint.getX() - mapPt.getX(), 2)
											+ Math.pow(startPoint.getY()
													- mapPt.getY(), 2));
							getCircle(startPoint, radius, polygon);

							/*
							 * add the updated graphic to graphics layer
							 */
							tempDrawGpLayer.updateGraphic(uid, polygon);
						}
					
					if (drawType == DRAW_GRAPHIC_TYPE.ENV){
						if(isFristMove){
							isFristMove = false;
							envelope = new Envelope();
							Graphic graphic = new Graphic(startPoint,
									new SimpleLineSymbol(Color.RED, 5));
							uid = tempDrawGpLayer.addGraphic(graphic);
							startPoint = map.toMapPoint(from.getX(),from.getY());
						}
						Point point = map.toMapPoint(to.getX(), to.getY());
						envelope.setXMin(startPoint.getX() > point.getX() ? point
								.getX() : startPoint.getX());
						envelope.setYMin(startPoint.getY() > point.getY() ? point
								.getY() : startPoint.getY());
						envelope.setXMax(startPoint.getX() < point.getX() ? point
								.getX() : startPoint.getX());
						envelope.setYMax(startPoint.getY() < point.getY() ? point
								.getY() : startPoint.getY());
						tempDrawGpLayer.updateGraphic(uid, envelope);
						}
					
					if (drawType == DRAW_GRAPHIC_TYPE.FRE_PL
							|| drawType == DRAW_GRAPHIC_TYPE.FRE_PG) {
						if(mapPt!=null&&poly!=null){
							poly.lineTo((float) mapPt.getX(), (float) mapPt.getY());

							tempDrawGpLayer.updateGraphic(uid, poly);
						}
					}
					return true;
				//}
			}
			return super.onDragPointerMove(from, to);

		}

		@Override
		public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
			if(isLongPress){
				isLongPress=false;
				return super.onDragPointerUp(from, to);
			}
			if (drawType != null && (drawType == DRAW_GRAPHIC_TYPE.FRE_PL
						|| drawType == DRAW_GRAPHIC_TYPE.FRE_PG
						|| drawType == DRAW_GRAPHIC_TYPE.CIRCLE
						|| drawType == DRAW_GRAPHIC_TYPE.ENV)) {

					/*
					 * When user releases finger, add the last point to
					 * polyline.
					 */
					Point point = map.toMapPoint(to.getX(), to.getY());

					switch(drawType){
					case CIRCLE:
						double radius = Math
								.sqrt(Math.pow(
										startPoint.getX() - point.getX(), 2)
										+ Math.pow(
												startPoint.getY()
														- point.getY(), 2));
						getCircle(startPoint, radius, polygon);
						tempDrawGpLayer.updateGraphic(uid, polygon);
						isFristMove = true;
						startPoint = null;

						break;
					case FRE_PL:
					case FRE_PG:
						if (drawType == DRAW_GRAPHIC_TYPE.FRE_PG) {
							poly.lineTo((float) startPoint.getX(),
									(float) startPoint.getY());
							// tempDrawGpLayer.removeAll();

							SimpleFillSymbol sfs = new SimpleFillSymbol(
									Color.RED);
							sfs.setAlpha(20);
							tempDrawGpLayer.addGraphic(new Graphic(poly, sfs));

						}
						tempDrawGpLayer.addGraphic(new Graphic(poly,
								new SimpleLineSymbol(Color.BLUE, 5)));
						break;
					case ENV:
						envelope.setXMin(startPoint.getX() > point.getX() ? point
						.getX() : startPoint.getX());
						envelope.setYMin(startPoint.getY() > point.getY() ? point
						.getY() : startPoint.getY());
						envelope.setXMax(startPoint.getX() < point.getX() ? point
						.getX() : startPoint.getX());
						envelope.setYMax(startPoint.getY() < point.getY() ? point
						.getY() : startPoint.getY());
						isFristMove = true;
						startPoint = null;
						
						tempDrawGpLayer.updateGraphic(uid, envelope);
					}

					// } else {

					// }
					startPoint = null;
					// clearButton.setEnabled(true);
					return true;
				//}
			}
			return super.onDragPointerUp(from, to);
		}
		
		public void rest(){
			poly = null;
			startPoint = null;
			polyline = new Polyline();
			polygon = new Polygon();
			envelope = new Envelope();
		}
	}

	private void getCircle(Point center, double radius, Polygon circle) {
		circle.setEmpty();
		Point[] points = getPoints(center, radius);
		circle.startPath(points[0]);
		for (int i = 1; i < points.length; i++)
			circle.lineTo(points[i]);
	}

	private Point[] getPoints(Point center, double radius) {
		Point[] points = new Point[50];
		double sin;
		double cos;
		double x;
		double y;
		for (double i = 0; i < 50; i++) {
			sin = Math.sin(Math.PI * 2 * i / 50);
			cos = Math.cos(Math.PI * 2 * i / 50);
			x = center.getX() + radius * sin;
			y = center.getY() + radius * cos;
			points[(int) i] = new Point(x, y);
		}
		return points;
	}

}
