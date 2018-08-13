package com.movementinsome.map.facedit.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.FeatureTemplate;
import com.esri.core.map.FeatureTemplate.DRAWING_TOOL;
import com.esri.core.map.FeatureType;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.symbol.SymbolHelper;
import com.movementinsome.R;
import com.movementinsome.app.pub.util.DensityUtil;
import com.movementinsome.map.facedit.vo.FacAttribute;
import com.movementinsome.map.facedit.vo.Legend;

import java.util.ArrayList;

public class MapEditFacListView {
	public static final int POINT = 0;
	public static final int POLYLINE = 1;
	public static final int POLYGON = 2;
	private int editingmode;
	private ArrayList<EditingStates> editingstates = new ArrayList<EditingStates>();
	private ArrayList<FeatureTemplate> templatelist;

	private ArrayList<FacAttribute> facAttributeList;
	private FacAttribute facAttribute;
	
	private MapView mMapView;
	private Context context;
	private RadioButton rbtnSelect;
	
	public MapEditFacListView(Context context,MapView mMapView){
		this.context=context;
		this.mMapView=mMapView;
	}
	
	public void showFacListNameDialog(final String type){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final AlertDialog dialog = builder.create();
		
		final PopupWindow mPopupWindow;
		
			
			View view = inflater.inflate(R.layout.selectfeaturetype, null);
			mPopupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, true);
			dialog.setView(view);
			ListView listview = (ListView) view.findViewById(R.id.listView1);
			listview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//					rbtnSelect.setChecked(false);
//					rbtnSelect=(RadioButton) view.findViewById(R.id.fac_list_choice);
//					rbtnSelect.setChecked(true);
					facAttribute = facAttributeList.get(position);
					Legend legend = facAttributeList.get(position).getLegend();
					Symbol symbol = legend.getSymbol();
					if (symbol instanceof MarkerSymbol) {
						editingmode = POINT;
					} else if (symbol instanceof LineSymbol) {
						if(type.equals("edit")||type.equals("delect")){
							editingmode = POINT;
						}else{
							editingmode = POLYLINE;
						}
					} else if (symbol instanceof FillSymbol) {
						editingmode = POLYGON;
					}
					mPopupWindow.dismiss();
				}
			});
			listTemplates();

			listview.setAdapter(new BaseAdapter() {

				public int getCount() {
					return facAttributeList.size();
				}

				public Object getItem(int position) {
					return facAttributeList.get(position);
				}

				public long getItemId(int position) {
					return position;
				}

				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					ListViewHolder holder = null;
					if (convertView == null) {
						convertView = inflater.inflate(R.layout.map_fac_listitem, null);
						holder = new ListViewHolder();
						holder.legendview = (ImageView) convertView
								.findViewById(R.id.fac_list_legend);
						holder.textview = (TextView) convertView
								.findViewById(R.id.fac_list_label);
					} else
						holder = (ListViewHolder) convertView.getTag();

					Legend legend = facAttributeList.get(position).getLegend();
					holder.legendview.setImageBitmap(legend.getBitmap());
					holder.textview.setText(facAttributeList.get(position).getLegend().getName());
					convertView.setTag(holder);
					return convertView;
				}
			});
			
			mPopupWindow.setTouchable(true);
			mPopupWindow.setOutsideTouchable(false);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(),
					(Bitmap) null));
			mPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
			//dialog.show();
	}
	public void showFacListNameDialog2(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final AlertDialog dialog = builder.create();
			
			View view = inflater.inflate(R.layout.selectfeaturetype, null);
			 dialog.setView(view);
			ListView listview = (ListView) view.findViewById(R.id.listView1);
			listview.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Legend legend = facAttributeList.get(position).getLegend();
					Symbol symbol = legend.getSymbol();
					if (symbol instanceof MarkerSymbol) {
						editingmode = POINT;
					} else if (symbol instanceof LineSymbol) {
						editingmode = POLYLINE;
					} else if (symbol instanceof FillSymbol) {
						editingmode = POLYGON;
					}
					dialog.dismiss();
				}
			});
			listTemplates();

			listview.setAdapter(new BaseAdapter() {

				public int getCount() {
					return facAttributeList.size();
				}

				public Object getItem(int position) {
					return facAttributeList.get(position);
				}

				public long getItemId(int position) {
					return 0;
				}

				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					ListViewHolder holder = null;
					if (convertView == null) {
						convertView = inflater.inflate(R.layout.map_fac_listitem, null);
						holder = new ListViewHolder();
						holder.legendview = (ImageView) convertView
								.findViewById(R.id.fac_list_legend);
						holder.textview = (TextView) convertView
								.findViewById(R.id.fac_list_label);
						holder.rbtn=(RadioButton) convertView.findViewById(R.id.fac_list_choice);
					} else
						holder = (ListViewHolder) convertView.getTag();

					Legend legend = (Legend) getItem(position);
					holder.legendview.setImageBitmap(legend.getBitmap());
					holder.textview.setText(facAttributeList.get(position).getLegend().getName());
					holder.rbtn.setVisibility(View.VISIBLE);
					convertView.setTag(holder);
					return convertView;
				}
			});
			dialog.show();
	}
	public  synchronized void listTemplates() {
		facAttributeList = new ArrayList<FacAttribute>();
		templatelist = new ArrayList<FeatureTemplate>();

		Layer[] layers = mMapView.getLayers();
		for (Layer l : layers) {

			if (l instanceof ArcGISFeatureLayer) {
				
				ArcGISFeatureLayer featurelayer1 = (ArcGISFeatureLayer) l;
				
				FeatureType[] types = featurelayer1.getTypes();
				boolean isTypes=false;
				if(types==null){
					Toast.makeText(context, "地图加载中", Toast.LENGTH_LONG).show();
					return;
				}
				for (FeatureType featureType : types) {
					FeatureTemplate[] templates = featureType.getTemplates();
					for (FeatureTemplate featureTemplate : templates) {
						//FeatureTemplate.DRAWING_TOOL dt = featureTemplate.getDrawingTool();
						String name = featureTemplate.getName();
						Graphic g = featurelayer1.createFeatureWithTemplate(featureTemplate, null);
						String objectid=g.getAttributeValue("OBJECTID")+"";
						Renderer renderer = featurelayer1.getRenderer();
						Symbol symbol = renderer.getSymbol(g);
						final int WIDTH_IN_DP_UNITS = 18;
						int widthInPixels =  DensityUtil.dip2px(context, WIDTH_IN_DP_UNITS);
					    Bitmap bitmap = SymbolHelper.getLegendImage(symbol,widthInPixels, widthInPixels);
						//Bitmap bitmap = createSymbolBitmap(featurelayer1, featureTemplate);
						//Bitmap bitmap = featurelayer.createSymbolImage(symbol, new Point(20,20), 50, 50, Color.WHITE);
						facAttributeList.add(new FacAttribute(new Legend(bitmap, name,symbol,objectid), featurelayer1, null, null));
						templatelist.add(featureTemplate);
						isTypes=true;
					}
				}
				if (!isTypes) { // no types
					FeatureTemplate[] templates = featurelayer1.getTemplates();
					for (FeatureTemplate featureTemplate : templates) {
						String name = featureTemplate.getName();
						Graphic g = featurelayer1.createFeatureWithTemplate(featureTemplate, null);
						String objectid=g.getAttributeValue("OBJECTID")+"";
						Renderer renderer = featurelayer1.getRenderer();
						Symbol symbol =null;
						if(renderer!=null){
							symbol = renderer.getSymbol(g);
							//Bitmap bitmap = featurelayer.createSymbolImage(symbol, new Point(20,20), 50, 50, Color.WHITE);
							//Bitmap bitmap = createSymbolBitmap(featurelayer1, featureTemplate);
							final int WIDTH_IN_DP_UNITS = 18;
							int widthInPixels =  DensityUtil.dip2px(context, WIDTH_IN_DP_UNITS);
						    Bitmap bitmap = SymbolHelper.getLegendImage(symbol, widthInPixels, widthInPixels);
							facAttributeList.add(new FacAttribute(new Legend(bitmap, name,symbol,objectid), featurelayer1, null, null));
							templatelist.add(featureTemplate);
						}
					}
				}
			}
		}
	}//list templates

	private Bitmap createSymbolBitmap(ArcGISFeatureLayer featurelayer, FeatureTemplate featureTemplate) {
	    // determine feature type
	    FeatureTemplate.DRAWING_TOOL drawing_tool = featureTemplate.getDrawingTool();
	    Geometry geometry = null;
	    if (drawing_tool == DRAWING_TOOL.POLYGON) {
	      Polygon polygon = new Polygon();
	      polygon.startPath(0,0);
	      polygon.lineTo(0,40);
	      polygon.lineTo(40,40);
	      polygon.lineTo(40,0);
	      polygon.lineTo(0,0);
	      geometry = polygon;
	    } else if (drawing_tool == DRAWING_TOOL.LINE) {
	      Polyline polyline = new Polyline();
	      polyline.startPath(1, 1);
	      polyline.lineTo(39, 39);
	      geometry = polyline;
	    } else if (drawing_tool == DRAWING_TOOL.POINT)
	      geometry = new Point(20,20);

	    Graphic g = featurelayer.createFeatureWithTemplate(featureTemplate, null);
	    Renderer renderer = featurelayer.getRenderer();
	    Symbol symbol = renderer.getSymbol(g); //g.getSymbol();
	    final int WIDTH_IN_DP_UNITS = 30;
	    final float scale = context.getResources().getDisplayMetrics().density;
	    final int widthInPixels = (int) (WIDTH_IN_DP_UNITS * scale + 0.5f);
	    Bitmap bitmap = SymbolHelper.getLegendImage(symbol, widthInPixels, widthInPixels);  
	    //Bitmap bitmap = null;//featurelayer.createSymbolImage(symbol, geometry, 40, 40, Color.WHITE);
	    return bitmap;
		}
	class ListViewHolder {
		ImageView legendview;
		TextView textview;
		RadioButton rbtn;
	}
	/**
	 * An instance of this class is created when a new point is to be
	 * added/moved/deleted. Hence we can describe this class as a container of
	 * points selected. Points, vertexes, or mid points.
	 */
	class EditingStates {
		ArrayList<Point> points1 = new ArrayList<Point>();
		boolean midpointselected1 = false;
		boolean vertexselected1 = false;
		int insertingindex1;

		public EditingStates(ArrayList<Point> points, boolean midpointselected,
				boolean vertexselected, int insertingindex) {
			this.points1.addAll(points);
			this.midpointselected1 = midpointselected;
			this.vertexselected1 = vertexselected;
			this.insertingindex1 = insertingindex;
		}
	}
	public int getEditingmode() {
		return editingmode;
	}

	public void setEditingmode(int editingmode) {
		this.editingmode = editingmode;
	}
	public ArrayList<FeatureTemplate> getTemplatelist() {
		return templatelist;
	}

	public void setTemplatelist(ArrayList<FeatureTemplate> templatelist) {
		this.templatelist = templatelist;
	}

	public FacAttribute getFacAttribute() {
		return facAttribute;
	}

	public void setFacAttribute(FacAttribute facAttribute) {
		this.facAttribute = facAttribute;
	}
	
}
