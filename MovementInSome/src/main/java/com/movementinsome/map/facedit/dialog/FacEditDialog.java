package com.movementinsome.map.facedit.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureEditResult;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.util.DensityUtil;
import com.movementinsome.app.pub.view.CreateTableView;
import com.movementinsome.kernel.initial.model.Ftlayer;
import com.movementinsome.kernel.util.WebAccessTools.NET_STATE;
import com.movementinsome.map.facSublistEdit.asynctask.FacSublistQueryTask2;
import com.movementinsome.map.facedit.MapEditFac;
import com.movementinsome.map.facedit.ObtainGid;
import com.movementinsome.map.facedit.vo.FacAttribute;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FacEditDialog extends PopupWindow implements OnClickListener{
	
	private Button wtQbtn_Back;//返回
	private Button wtQSave;//保存
	private Button wtQAdd;//添加
	private ViewFlipper wtQFlipper;
	private LinearLayout wtQLinearMessage;//详细信息容器
	private TextView wtQtv_Table_Title;//标题
	
	private Map<String, TextView> upDataView;
	private Map<String, Spinner> upDataSpinner;
	
	
	private boolean isShow=false;//是否显示详细信息容器
	private ProgressDialog progress;
	private CreateTableView createTableView;
	private int x;// 当前项
	private String editType="";//编辑或添加
	
	private ListView facListview;//设施列表
	private String type;// 设施类型
	private Context context;
	private FacAttribute facAttribute;
	private MapEditFac mapEditFac;
	private FeatureSet featureSet ;
	private Graphic[] facData;
	private Ftlayer ftlayer;
	
	public Handler gidHandler = new Handler(){
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				String gid = "";
				Bundle bundle=msg.getData();
				gid=bundle.getString("gid");
				try {
					JSONObject jsongid = new JSONObject(gid);
					JSONObject number = jsongid.getJSONObject("result");
					JSONArray rows=number.getJSONArray("rows");
					if(rows.length()>0){
						JSONObject rows0=rows.getJSONObject(0);
						gid=rows0.getString("GID");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				chaxungid(gid);
			}else if(msg.what == 2){
				Toast.makeText(context, "获取失败", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	public FacEditDialog(Context context,FacAttribute facAttribute,String type,Ftlayer ftlayer,MapEditFac mapEditFac,FeatureSet featureSet) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.facAttribute=facAttribute;
		this.type=type;
		this.mapEditFac=mapEditFac;
		this.featureSet=featureSet;
		this.ftlayer = ftlayer;
		facData = featureSet.getGraphics();
		init();
	}
	
	public FacEditDialog(Context context,String type,Ftlayer ftlayer,FacAttribute facAttribute,MapEditFac mapEditFac){
		super(context);
		// TODO Auto-generated constructor stub
		this.facAttribute=facAttribute;
		this.context=context;
		this.type=type;
		this.ftlayer=ftlayer;
		this.mapEditFac= mapEditFac;
		init();
	}
	private void init(){
		LinearLayout v = (LinearLayout) View.inflate(context, R.layout.fc_edit_dialog, null);
		createTableView=new CreateTableView(context);
		wtQbtn_Back=(Button) v.findViewById(R.id.wtQbtn_Back);//返回
		wtQSave=(Button) v.findViewById(R.id.wtQSave);//保存
		wtQtv_Table_Title=(TextView) v.findViewById(R.id.wtQtv_Table_Title);//标题
		facListview=(ListView) v.findViewById(R.id.wtQListview);//列表
		wtQFlipper=(ViewFlipper) v.findViewById(R.id.wtQFlipper);
		wtQLinearMessage=(LinearLayout) v.findViewById(R.id.wtQLinearMessage);//详细信息容器
		wtQAdd=(Button) v.findViewById(R.id.wtQAdd);
		
		wtQbtn_Back.setOnClickListener(this);
		wtQSave.setOnClickListener(this);
		wtQAdd.setOnClickListener(this);
		if(facData!=null){
			facListview.setAdapter(new FacAdapter());
		}
		facListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				x=arg2;
				if("edit".equals(type)){
					wtQListviewDialog3(arg2);
				}else if("delect".equals(type)){
					wtQListviewDialog4(arg2);
				}
			}
		});
		wtQAdd.setVisibility(View.GONE);
		wtQtv_Table_Title.setText(facAttribute.getLegend().getName());
		if("add".equals(type)){
			showTable(null, "add");
			isShow=false;
			wtQSave.setVisibility(View.VISIBLE);
			editType="add";
			wtQFlipper.showNext();
		}
		//设置SelectPicPopupWindow的View
		this.setContentView(v);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
	}
	 //设施属性编辑
	 private void wtQListviewDialog3(final int idxe){
		 String[] item = new String[]{"查看","修改"};
		 if(ftlayer.getRelationshipId()!=0){
			 item = new String[]{"查看","修改","编辑子表"};
		 }
			new AlertDialog.Builder(context)
			.setTitle("操作选择")
			.setItems(item, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					switch (which) {
					case 0:			
						showMessage(facData[idxe]);
						wtQSave.setVisibility(View.GONE);
						isShow=true;
						wtQFlipper.showNext();
						break;
					case 1:
						showTable(facData[idxe],"updata");
						isShow=true;
						wtQSave.setVisibility(View.VISIBLE);
						editType="updata";
						wtQFlipper.showNext();
						break;
						case 2:
							int[] OBJECTID = new int[] { (Integer) facData[idxe]
									.getAttributes().get("OBJECTID") };
							new FacSublistQueryTask2(context, mapEditFac
									.getMap(), facAttribute, ftlayer,
									facData[idxe]).execute("");
							break;
					default:
						break;
					}
				}
			}).create().show();
	 }
	 private void wtQListviewDialog4(final int idxe){
		 String[] item = new String[]{"查看","删除"};
			new AlertDialog.Builder(context)
			.setTitle("操作选择")
			.setItems(item, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					switch (which) {
					case 0:			
						showMessage(facData[idxe]);
						wtQSave.setVisibility(View.GONE);
						isShow=true;
						wtQFlipper.showNext();
						break;
					case 1:
						delDialog2(idxe);
						break;

					default:
						break;
					}
				}
			}).create().show();
	 }
	
	 private void delDialog2(final int idex){
			new AlertDialog.Builder(context)
			.setTitle("提示")
			.setIcon(android.R.drawable.ic_menu_help)
			.setMessage("确定删除此设施")
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
					/*Map<String, Object> attributes= new HashMap<String, Object>(); 
					attributes.put("OBJECTID", data.get(idex).getAttributes().get("OBJECTID"));
					Graphic delGraphic=new Graphic(null, null, attributes);*/
					//delGraphic=data.get(idex);
					Graphic[]del=new Graphic[]{
							facData[idex]
					};
					wtEdit(null, del, null);
				}
			}).show();
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
					if("add".equals(editType)){
						String value="";
						String url = "";
						String tablename="";
						if (AppContext.getInstance().ACCESS_NET_STATE == NET_STATE.NULL){
							value = ftlayer.getMapservice().getForeign();
						}else if (AppContext.getInstance().ACCESS_NET_STATE == NET_STATE.FOEIGN ){
							value = ftlayer.getMapservice().getForeign();
						}else{
							value = ftlayer.getMapservice().getLocal();
						}
						tablename=ftlayer.getTablename();
						url = value+"/exts/SysManager/dsdb/GetGID?tablename="+tablename+"&f=pjson";
						ObtainGid obtainGid=new ObtainGid(context,FacEditDialog.this);
						obtainGid.execute(url);
					}else {
						chaxungid("");
					}
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
			List<com.movementinsome.kernel.initial.model.Field> fields =ftlayer.getFields();
			if(fields!=null){
				for(int i=0;i<fields.size();++i){
					String str = fields.get(i).getName();
					String value="";
					if(attr!=null&& attr.getAttributes()!=null&&attr.getAttributes().get(str)!=null){
						 value = attr.getAttributes().get(str)+"";
						 if(str.equals("FINISH_DATE")){
							 value=TimeStamp2Date(value);
						 }
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
				wtQLinearMessage.removeAllViews();
				wtQLinearMessage.addView(pushPLayout);
			}else{
				Toast.makeText(context, "查询出错", 4).show();
			}
		}
	 
	 public String TimeStamp2Date(String timestampString){  
			 Long timestamp = Long.parseLong(timestampString)*1;  
			 String date = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(timestamp));  
			 return date;  
		 }  
	 
	 private void showTable(Graphic attr,String type){
		 wtQLinearMessage.removeAllViews();
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
				List<com.movementinsome.kernel.initial.model.Field> fields=ftlayer.getFields();
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
						wtQLinearMessage.addView(v);
					
				}
			}
			
	 }
	 
	 private void chaxungid(String gid){
		 Map<String, Object> attributes= new HashMap<String, Object>();
			if("updata".equals(editType)){ 
				attributes.put("OBJECTID", facData[x].getAttributes().get("OBJECTID"));
			}
			if(ftlayer!=null){
				List<com.movementinsome.kernel.initial.model.Field> fields=ftlayer.getFields();
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
			if("add".equals(editType)){
				attributes.put("GID", gid);
			}
			Graphic upDataGraphic=null;
			if("updata".equals(editType)){ 
				upDataGraphic=new Graphic(null, null, attributes);
			}else if("add".equals(editType)){
				
				if(facAttribute.getGeom()!=null){
					upDataGraphic=new Graphic(facAttribute.getGeom(), facAttribute.getLegend().getSymbol(), attributes);
				}else{
					upDataGraphic=new Graphic(facAttribute.getEditPoint(), facAttribute.getLegend().getSymbol(), attributes);
				}
				
			}
			
			Graphic[]upData=new Graphic[]{
					upDataGraphic
			};
			if("updata".equals(editType)){ 
				wtEdit(null, null, upData);	 
			}else if("add".equals(editType)){
				wtEdit(upData, null, null); 
			}
		}
	 
	 private void wtEdit(final Graphic[] adds,final Graphic[]deletes,final Graphic[]updates){
		 facAttribute.getFeaturelayer().applyEdits(adds, deletes, updates, new callback());
			
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
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.wtQbtn_Back:
			if(isShow){
				wtQFlipper.showPrevious();//返回属性列表
				isShow=false;
				wtQSave.setVisibility(View.GONE);
			}else{
				FacEditDialog.this.dismiss();//退出对话框
			}
			break;
		case R.id.wtQSave:
			updataDialog(x);
			break;
		case R.id.wtQAdd:
			showTable(null, "add");
			isShow=true;
			wtQSave.setVisibility(View.VISIBLE);
			editType="add";
			wtQFlipper.showNext();
			break;

		default:
			break;
		}
		
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
				mapEditFac.clear();
				dismiss();
				break;
			case 2:
				if(progress!=null){
					progress.dismiss();
				}Toast.makeText(context, "编辑错误", Toast.LENGTH_LONG).show();
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
	private class FacAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return facData.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return facData[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@SuppressLint("NewApi")
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(arg1==null){
				arg1 = View.inflate(context, R.layout.fac_edit_list_item, null);
			}
			ImageView fac_edit_list_item_img = (ImageView) arg1.findViewById(R.id.fac_edit_list_item_img);
			TextView fac_edit_list_item_text = (TextView) arg1.findViewById(R.id.fac_edit_list_item_text);
			Graphic graphic = facData[arg0];
			String text = "";
			if(ftlayer!=null){
				List<com.movementinsome.kernel.initial.model.Field> fields=ftlayer.getFields();
				for(int i=0;i<fields.size();++i){
					if("isSynopsis".equals(fields.get(i).getSynopsis())){
						text+=text.equals("") ? 
								fields.get(i).getAlias()+":"+graphic.getAttributeValue(fields.get(i).getName())
								: "\n"+fields.get(i).getAlias()+":"+graphic.getAttributeValue(fields.get(i).getName());
					}
				}
			}
			fac_edit_list_item_text.setText(text);
			//fac_edit_list_item_img.setImageBitmap(facAttribute.getLegend().getBitmap());
			fac_edit_list_item_img.setBackground(new BitmapDrawable(context.getResources(), facAttribute.getLegend().getBitmap()));
			return arg1;
		}
		
	}
	

}
