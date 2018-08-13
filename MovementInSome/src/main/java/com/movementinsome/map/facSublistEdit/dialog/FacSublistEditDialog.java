package com.movementinsome.map.facSublistEdit.dialog;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureEditResult;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.movementinsome.R;
import com.movementinsome.app.pub.util.DensityUtil;
import com.movementinsome.app.pub.view.CreateTableView;
import com.movementinsome.kernel.initial.model.Ftlayer;
import com.movementinsome.map.facSublistEdit.asynctask.FacSublistQueryTask;
import com.movementinsome.map.facedit.vo.FacAttribute;
import com.movementinsome.map.view.MyMapView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FacSublistEditDialog  extends PopupWindow implements OnClickListener{

	private Button fac_sublist_edit_back;// 返回
	private Button fac_sublist_edit_save;// 保存
	private TextView fac_sublist_edit_title; // 标题
	private ViewFlipper fac_sublist_edit_flipper;// 翻页
	private ListView fac_sublist_edit_listview;// 设施列表
	private LinearLayout fac_sublist_operateLl;// 底栏
	private Button fac_sublist_add;// 添加
	private Button fac_sublist_delete;// 删除
	private Button fac_sublist_edit;// 修改
	private Button fac_sublist_query;// 查看
	private LinearLayout fac_sublist_edit_message;// 信息
	private ListView fac_listview;// 设施列表
	
	private Context context;
	// 查询设施数据
	private Map<Integer, FeatureSet> result;
	private List<Graphic> data;
	public FacSublistAdapter facSublistAdapter;
	// 编辑图层
	private FacAttribute facAttribute; 
	// 编制字段配置
	private Ftlayer ftlayer;
	private ArcGISFeatureLayer fl;
	
	
	private Map<String, TextView> upDataView;
	private Map<String, Spinner> upDataSpinner;
	private CreateTableView createTableView;
	
	private boolean isShow=false;//是否显示详细信息容器
	private String editType="";//编辑或添加
	private ProgressDialog progress;
	private int position = 0 ;				//Listview中所选择的position
	private MyMapView map;
	private FeatureSet featureSet;
	private List<Graphic> facData;
	
	private Graphic graphicF;
	
	public FacSublistEditDialog(Context context,MyMapView map, Map<Integer, FeatureSet> result
			,FacAttribute facAttribute,Ftlayer ftlayer){
		this.context = context;
		this.result = result;
		this.facAttribute = facAttribute;
		this.ftlayer = ftlayer;
		this.map = map;
		
		data = new ArrayList<Graphic>();
		updateList(result);
		init();
		fac_sublist_edit_flipper.showNext();
	}
	public FacSublistEditDialog(Context context,MyMapView map, Map<Integer, FeatureSet> result,Graphic graphicF
			,FacAttribute facAttribute,Ftlayer ftlayer){
		this.context = context;
		this.result = result;
		this.facAttribute = facAttribute;
		this.ftlayer = ftlayer;
		this.map = map;
		this.graphicF = graphicF;
		
		data = new ArrayList<Graphic>();
		updateList(result);
		init();
		fac_sublist_edit_flipper.showNext();
	}
	public FacSublistEditDialog(Context context,MyMapView map, FeatureSet result
			,FacAttribute facAttribute,Ftlayer ftlayer){
		this.context = context;
		this.featureSet = result;
		this.facAttribute = facAttribute;
		this.ftlayer = ftlayer;
		this.map = map;
		facData = new ArrayList<Graphic>();
		updateFacList(result);
		init();
	}
	public void updateList(Map<Integer, FeatureSet> result){ 
		data.removeAll(data);
		if(result!=null){
			Collection<FeatureSet> c = result.values();
			Iterator<FeatureSet> it = c.iterator();
			while(it.hasNext()) {
				FeatureSet featureSet = it.next();
				Graphic[] graphics =  featureSet.getGraphics();
				if(graphics!=null&&graphics.length>0){
					for(int i=0;i<graphics.length;++i){
						data.add(graphics[i]);
					}
				}
			}
		}
	}
	public void updateFacList(FeatureSet result){
		Graphic[] graphics =  featureSet.getGraphics();
		if(graphics!=null&&graphics.length>0){
			for(int i=0;i<graphics.length;++i){
				facData.add(graphics[i]);
			}
			
		}
	}
	public void blak(){
		fac_sublist_edit_flipper.showPrevious();//返回属性列表
		isShow=false;
		fac_sublist_edit_save.setVisibility(View.GONE);
	}

	private View view;
	private void init() {
		view = View.inflate(context, R.layout.fac_sublist_edit_dialog,
				null);
		createTableView=new CreateTableView(context);
		
		fac_listview = (ListView) view.findViewById(R.id.fac_listview);// 设施列表
		fac_sublist_edit_back = (Button) view
				.findViewById(R.id.fac_sublist_edit_back);// 返回
		fac_sublist_edit_back.setOnClickListener(this);
		fac_sublist_edit_save = (Button) view
				.findViewById(R.id.fac_sublist_edit_save);// 保存
		fac_sublist_edit_save.setOnClickListener(this);
		fac_sublist_edit_title = (TextView) view
				.findViewById(R.id.fac_sublist_edit_title); // 标题
		fac_sublist_edit_flipper = (ViewFlipper) view
				.findViewById(R.id.fac_sublist_edit_flipper);// 翻页
		fac_sublist_edit_listview = (ListView) view
				.findViewById(R.id.fac_sublist_edit_listview);// 设施列表
		facSublistAdapter = new FacSublistAdapter();
		fac_sublist_edit_listview.setAdapter(facSublistAdapter);
		fac_sublist_operateLl = (LinearLayout) view
				.findViewById(R.id.fac_sublist_operateLl);// 底栏
		fac_sublist_add = (Button) view.findViewById(R.id.fac_sublist_add);// 添加
		fac_sublist_add.setOnClickListener(this);
		fac_sublist_delete = (Button) view
				.findViewById(R.id.fac_sublist_delete);// 删除
		fac_sublist_delete.setOnClickListener(this);
		fac_sublist_edit = (Button) view.findViewById(R.id.fac_sublist_edit);// 修改
		fac_sublist_edit.setOnClickListener(this);
		fac_sublist_query = (Button) view.findViewById(R.id.fac_sublist_query);// 查看
		fac_sublist_query.setOnClickListener(this);
		fac_sublist_edit_message = (LinearLayout) view
				.findViewById(R.id.fac_sublist_edit_message);// 信息

		// 设置SelectPicPopupWindow的View
		this.setContentView(view);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(facSublistAdapter!=null){
			position = facSublistAdapter.getPosition();
		}
		switch (arg0.getId()) {
		case R.id.fac_sublist_edit_back:
			if(isShow){
				fac_sublist_edit_flipper.showPrevious();//返回属性列表
				isShow=false;
				fac_sublist_edit_save.setVisibility(View.GONE);
			}else{
				FacSublistEditDialog.this.dismiss();
			}
			break;
		case R.id.fac_sublist_edit_save:
			updataDialog(position);
			break;
		case R.id.fac_sublist_add:
			showTable(null, "add");
			isShow=true;
			fac_sublist_edit_save.setVisibility(View.VISIBLE);
			editType="add";
			fac_sublist_edit_flipper.showNext();
			break;
		case R.id.fac_sublist_delete:
			if(data!=null&&data.size()>0){
				delDialog(position);
			}
			break;
		case R.id.fac_sublist_edit:
			if(data!=null&&data.size()>0){
				showTable(data.get(position),"updata");
				isShow=true;
				fac_sublist_edit_save.setVisibility(View.VISIBLE);
				editType="updata";
				fac_sublist_edit_flipper.showNext();
			}
			break;
		case R.id.fac_sublist_query:
			if(data!=null&&data.size()>0){
				showMessage(data.get(position));
				fac_sublist_edit_save.setVisibility(View.GONE);
				isShow=true;
				fac_sublist_edit_flipper.showNext();
			}
			break;

		default:
			break;
		}
	}
	private void delDialog(final int idex){
		new AlertDialog.Builder(context)
		.setTitle("提示")
		.setIcon(android.R.drawable.ic_menu_help)
		.setMessage("确定删除此水表")
		.setPositiveButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		})
		.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//				Map<String, Object> attributes= new HashMap<String, Object>(); 
//				 attributes.put("OBJECTID", data.get(idex).getAttributes().get("OBJECTID"));
////				 attributes.put("LINKGID", data.get(idex).getAttributes().get("LINKGID"));
//				 attributes.put("GLOBALID", data.get(idex).getAttributes().get("GLOBALID"));
//				Graphic delGraphic=new Graphic(null, null, attributes);
//				delGraphic=data.get(idex);
				fl= new ArcGISFeatureLayer("http://59.33.37.170:8399/arcgis/rest/services/edit/FeatureServer/14", ArcGISFeatureLayer.MODE.ONDEMAND);
				facAttribute.setFeaturelayer(fl);
				Graphic[]del=new Graphic[]{
						data.get(idex)
				};
				wtEdit(null, del, null);
			}
		}).show();
 }
	private void showMessage(Graphic attr){
		LinearLayout.LayoutParams paramsImage = new LinearLayout.LayoutParams( 
				DensityUtil.dip2px(context, 10), ViewGroup.LayoutParams.MATCH_PARENT); 
		
		LinearLayout.LayoutParams paramsLayout = null;
		/*paramsLayout = new LinearLayout.LayoutParams( 
					ViewGroup.LayoutParams.MATCH_PARENT, 
					DensityUtil.dip2px(context, 35));*/ 
		paramsLayout = new LinearLayout.LayoutParams( 
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT);
		LinearLayout.LayoutParams paramsPLayout = new LinearLayout.LayoutParams( 
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT); 

		LinearLayout pushPLayout = new LinearLayout(context);
		pushPLayout.setOrientation(LinearLayout.VERTICAL);
		pushPLayout.setLayoutParams(paramsPLayout);
		pushPLayout.setGravity(Gravity.CENTER_VERTICAL);
		List<com.movementinsome.kernel.initial.model.Field> fields =ftlayer.getRelationshipFields();
		if(fields!=null){
			for(int i=0;i<fields.size();++i){
				String str = fields.get(i).getName();
				String value="";
				if(attr!=null&& attr.getAttributes()!=null&&attr.getAttributes().get(str)!=null){
					 value = attr.getAttributes().get(str)+"";
				}
				TextView tvValue = new TextView(context);
				tvValue.setTextColor(Color.BLACK);
				tvValue.setPadding(5, 0, 0, 0);
				tvValue.setGravity(Gravity.CENTER_VERTICAL);
				tvValue.setText(value);
				
				TextView tvKey = new TextView(context);
				tvKey.setTextColor(Color.BLACK);
				tvKey.setGravity(Gravity.CENTER_VERTICAL);
				tvKey.setText(fields.get(i).getAlias());
				tvKey.setEms(4);
				
				
				TextView tvImage = new TextView(context);
				tvImage.setLayoutParams(paramsImage);
				tvImage.setBackgroundResource(R.drawable.task_fg);
				
				LinearLayout cLayout = new LinearLayout(context);
				cLayout.setBackgroundResource(R.drawable.input_bg);
				cLayout.setGravity(Gravity.CENTER_VERTICAL);
				cLayout.setLayoutParams(paramsLayout);
				cLayout.addView(tvKey);
				cLayout.addView(tvImage);
				cLayout.addView(tvValue);
				cLayout.setPadding(10, 5, 5, 5);
				
				pushPLayout.addView(cLayout);
			}
			fac_sublist_edit_message.removeAllViews();
			fac_sublist_edit_message.addView(pushPLayout);
		}else{
			Toast.makeText(context, "查询出错", 4).show();
		}
	}
	private void showTable(Graphic attr,String type){
		fac_sublist_edit_message.removeAllViews();
		 int height=40;
		 int titelTextSize=7;
		 int etTextSize=7;
		 final float scale = context.getResources().getDisplayMetrics().density;
		 height = (int) (height*scale+0.5);
		 titelTextSize = (int) (titelTextSize*scale+0.5);
		 etTextSize = (int) (etTextSize*scale+0.5);
			if(ftlayer!=null){
				if(upDataView==null){
					upDataView=new HashMap<String, TextView>();
				}else{
					upDataView.clear();
				}
				if(upDataSpinner==null){
					upDataSpinner=new HashMap<String, Spinner>();
				}else{
					upDataSpinner.clear();
				}
				List<com.movementinsome.kernel.initial.model.Field> fields=ftlayer.getRelationshipFields();
				int j=0;
				for(int i=0;i<fields.size();++i){
					
						List<String> items=fields.get(i).getDropList();
						String fieldname1=fields.get(i).getName();
						String fieldcn=fields.get(i).getAlias();
						int inputType=1;
						String dateType="";
						if(items!=null&&items.size()>0){
							dateType="spinner";
						}else{
							if("I".equals(fields.get(i).getEditType())){
								inputType=InputType.TYPE_CLASS_NUMBER;
							}else if("F".equals(fields.get(i).getEditType())){
								inputType=InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL;
							}else if("C".equals(fields.get(i).getEditType())){
								inputType=InputType.TYPE_CLASS_TEXT;
							}else if("T".equals(fields.get(i).getEditType())){
								dateType="dateType";
							}else{
								continue;
							}
						}
						View v=null;
						String text=null;
						if("updata".equals(type)){
							  text=attr.getAttributeValue(fieldname1)==null?""
									  :attr.getAttributeValue(fieldname1)+"";
						}
						if("dateType".equals(dateType)){
							v=createTableView.getTextView(fieldcn, inputType
									,15.0, ++j, height, titelTextSize, etTextSize, text,"dateType",++j);
							TextView tv1=(TextView) v.findViewById(j-1);
							TextView tv2=(TextView) v.findViewById(j);
							upDataView.put(fieldname1+0, tv1);
							upDataView.put(fieldname1+1, tv2);
									 
						}else if("spinner".equals(dateType)){
							v=createTableView.getSpinner(fieldcn, titelTextSize, items, height, ++j);
							Spinner spinner=(Spinner) v.findViewById(j);
							upDataSpinner.put(fieldname1, spinner);
						}else{
							v=createTableView.getTextView(fieldcn, inputType
									,15.0, ++j, height, titelTextSize, etTextSize, text,"",0);
							TextView tv=(TextView) v.findViewById(j);
							upDataView.put(fieldname1, tv);
						}	
						fac_sublist_edit_message.addView(v);
					
				}
			}
			
	 }
	 private void wtEdit(final Graphic[] adds,final Graphic[]deletes,final Graphic[]updates){
		 
//		 facAttribute.getFeaturelayer().applyEdits(adds, deletes, updates, new callback());
		 new Thread(new Runnable() {
			
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					facAttribute.getFeaturelayer().applyEdits(adds, deletes, updates, new callback());
				}
			}).start();
		 if (progress == null) {
			progress = new ProgressDialog(context);
			progress.setIndeterminate(true);
		 }
		progress.setMessage("正在编辑");
		progress.show();
			
		   
	 }  
	//属性编辑后回调
		 class callback implements CallbackListener<FeatureEditResult[][]>{

			@Override
			public void onCallback(FeatureEditResult[][] arg0) {
				// TODO Auto-generated method stub
				
				MyHandler.sendEmptyMessage(3);
				for(int i=0;i<arg0.length;++i){
					if(arg0[i]!=null&&arg0[i].length>0){
						if(arg0[i][0]!=null&&arg0[i][0].isSuccess()){
							MyHandler.sendEmptyMessage(1);
						}else{
							MyHandler.sendEmptyMessage(0);
						}
					}
				}
			}
			@Override
			public void onError(Throwable arg0) {
				// TODO Auto-generated method stub
				MyHandler.sendEmptyMessage(2);
			}
		 }
		 private void updataDialog(final int idex){
			 String msg="";
			    if("updata".equals(editType)){
			    	 msg="确定更新此设施";
			    }else if("add".equals(editType)){
			    	 msg="确定添加此设施";
			    }
				new AlertDialog.Builder(context)
				.setTitle("提示")
				.setIcon(android.R.drawable.ic_menu_help)
				.setMessage(msg)
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				})
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Map<String, Object> attributes= new HashMap<String, Object>();
						if("updata".equals(editType)){ 
							attributes.put("OBJECTID", data.get(idex).getAttributes().get("OBJECTID"));
						}else if("add".equals(editType)){
							if(graphicF!=null){
								attributes.put("LINKGID", graphicF.getAttributes().get("GID"));
							}else{
								attributes.put("LINKGID", data.get(idex).getAttributes().get("LINKGID"));
							}
						}
						if(ftlayer!=null){
							List<com.movementinsome.kernel.initial.model.Field> fields=ftlayer.getRelationshipFields();
							for(int i=0;i<fields.size();++i){
								
									// 字段名
									String fieldname=fields.get(i).getName();
									if("T".equals(fields.get(i).getEditType())){
										try{
											if(upDataSpinner.get(fieldname)!=null){
												String datetime=upDataSpinner.get(fieldname).getSelectedItem()+"";
												Date d=new Date(datetime);
												attributes.put(fieldname,d.getTime());
											}else{
												String date=(upDataView.get(fieldname+0).getText()+"").replace("-", "/");
												String time=upDataView.get(fieldname+1).getText()+"";
												if((!"".equals(time))&&(!"".equals(date))){
													Date d=new Date(date+" "+time);
													attributes.put(fieldname,  d.getTime());
												}
											}
											
										}catch (Exception e) {
											// TODO: handle exception
										}
										
									}else if("F".equals(fields.get(i).getEditType())){
										try{
											String vauleF="";
											if(upDataSpinner.get(fieldname)!=null){
												vauleF=upDataSpinner.get(fieldname).getSelectedItem()+"";
											}else{
												vauleF=upDataView.get(fieldname).getText()+"";
											}
											if(!"".equals(vauleF)){
												Double f=Double.parseDouble(vauleF);
												attributes.put(fieldname, f);
											}
										}catch (Exception e) {
											// TODO: handle exception
										}
									}else if("I".equals(fields.get(i).getEditType())){
										try{
											String vauleI="";
											if(upDataSpinner.get(fieldname)!=null){
												vauleI=upDataSpinner.get(fieldname).getSelectedItem()+"";
												
											}else{
												vauleI=upDataView.get(fieldname).getText()+"";
											}
											if(!"".equals(vauleI)){
												Integer I=Integer.parseInt(vauleI);
												attributes.put(fieldname,I);	
											}
										}catch (Exception e) {
											// TODO: handle exception
										}
									}else {
										try{
											if(upDataSpinner.get(fieldname)!=null){
												attributes.put(fieldname, upDataSpinner.get(fieldname).getSelectedItem()+"");
											}else{
												if(!"".equals(upDataView.get(fieldname).getText()+"")){
													attributes.put(fieldname, upDataView.get(fieldname).getText()+"");
												}
											}	
										}catch (Exception e) {
											// TODO: handle exception
										}
										
									}
								
							}
						} 
						Graphic upDataGraphic = new Graphic(null, null, attributes);
						Graphic[]upData=new Graphic[]{
								upDataGraphic
						};
						if("updata".equals(editType)){  
							fl= new ArcGISFeatureLayer("http://59.33.37.170:8399/arcgis/rest/services/edit/FeatureServer/14", ArcGISFeatureLayer.MODE.ONDEMAND);
							facAttribute.setFeaturelayer(fl);
							wtEdit(null, null, upData);	
							 
						}else if("add".equals(editType)){
							fl= new ArcGISFeatureLayer("http://59.33.37.170:8399/arcgis/rest/services/edit/FeatureServer/14", ArcGISFeatureLayer.MODE.ONDEMAND);
							facAttribute.setFeaturelayer(fl);
							wtEdit(upData, null, null); 
							 
						}
					}
				}).show();
		 }
	private Handler MyHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(context, "编辑失败", Toast.LENGTH_LONG).show();
				break;
			case 1:
				Toast.makeText(context, "编辑成功", Toast.LENGTH_LONG).show();
				FacSublistQueryTask facSublistQueryTask = new FacSublistQueryTask(context, map, facAttribute, ftlayer);
				facSublistQueryTask.setFacSublistEditDialog(FacSublistEditDialog.this);
				facSublistQueryTask.execute("*");
				break;
			case 2:
				if(progress!=null){
					progress.dismiss();
				}
				Toast.makeText(context, "编辑错误", Toast.LENGTH_LONG).show();
				break;
			case 3:
				if(progress!=null){
					progress.dismiss();
				}
				break;

			default:
				break;
			}
		}
		
	};
	public class FacSublistAdapter extends BaseAdapter{

		private int temp = 0;
		private int position = 0;
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return data.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			
			arg1 = View.inflate(context, R.layout.fac_sublist_edit_item, null);
			
			TextView fac_sublist_item_tv = (TextView) arg1.findViewById(R.id.fac_sublist_item_tv);
			Graphic graphic = data.get(arg0);
			String text = "";
			if(ftlayer!=null){
				List<com.movementinsome.kernel.initial.model.Field> fields=ftlayer.getRelationshipFields();
				for(int i=0;i<fields.size();++i){
					if("isSynopsis".equals(fields.get(i).getSynopsis())){
						text+=text.equals("") ? 
								fields.get(i).getAlias()+":"+graphic.getAttributeValue(fields.get(i).getName())
								: "\n"+fields.get(i).getAlias()+":"+graphic.getAttributeValue(fields.get(i).getName());
					}
				}
			}
			fac_sublist_item_tv.setText(text);
			
			RadioButton wt_query_list_radio = (RadioButton) arg1.findViewById(R.id.fac_sublist_item_radio);
			wt_query_list_radio.setId(arg0);
			if(arg0 == temp) wt_query_list_radio.setChecked(true);
			wt_query_list_radio.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					try {
						RadioButton radioButton = (RadioButton) view.findViewById(temp);
						if(radioButton!=null){
							radioButton.setChecked(false);
						}
						temp = buttonView.getId();
						setPosition(temp);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});
			return arg1;
		}
		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}
		
	}
	public class FacAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return facData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return facData.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(arg1==null){
				arg1 = View.inflate(context, R.layout.sublist_edit_fac_item, null);
			}
			TextView sublist_edit_fac_item_num = (TextView) arg1.findViewById(R.id.sublist_edit_fac_item_num);
			sublist_edit_fac_item_num.setText("("+arg0+1+")");
			TextView sublist_edit_fac_item_text = (TextView) arg1.findViewById(R.id.sublist_edit_fac_item_text);
			sublist_edit_fac_item_text.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			TextView sublist_edit_fac_item_msg = (TextView) arg1.findViewById(R.id.sublist_edit_fac_item_msg);
			sublist_edit_fac_item_msg.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
				}
			});

			return arg1;
		}
		
	}
	public FacSublistAdapter getFacSublistAdapter() {
		return facSublistAdapter;
	}
	public void setFacSublistAdapter(FacSublistAdapter facSublistAdapter) {
		this.facSublistAdapter = facSublistAdapter;
	}
	public boolean isShow() {
		return isShow;
	}
	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}
}
