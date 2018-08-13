package com.movementinsome.app.mytask;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer.MODE;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.FeatureEditResult;
import com.esri.core.map.Graphic;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.dialog.DateTimeDialog;
import com.movementinsome.database.vo.GeometryVO;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.kernel.util.MyDateTools;
import com.movementinsome.map.MapBizViewer;
import com.movementinsome.map.utils.GeometryUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmphasisPatrolAreaActivity extends FullActivity implements OnClickListener{

	private ImageView emphasis_patrol_area_back;
	private Button emphasis_patrol_area_commit;
	private Spinner emphasis_patrol_area_zyjb;// *重要级别
	private Spinner emphasis_patrol_area_yxzt;// *有效状态
	private EditText emphasis_patrol_area_qrmq;// *区域名称
	private EditText emphasis_patrol_area_sgdw;// *施工单位
	private EditText emphasis_patrol_area_gdfzr;// *工地负责人
	private EditText emphasis_patrol_area_sgfdh;// 施工方电话
	private EditText emphasis_patrol_area_xxwz;// *详细位置
	private TextView emphasis_patrol_area_ksrq;// 施工计划开始日期
	private TextView emphasis_patrol_area_jsrq;// 施工计划结束日期
	private EditText emphasis_patrol_area_gcjd;// *工程进度
	private EditText emphasis_patrol_area_bz;// 备注
	
	private Button emphasis_patrol_area_qrxq;// 区域选取
	private JSONObject geometry;
	private Polygon poly;
	
	private void init(){
		emphasis_patrol_area_back =(ImageView) findViewById(R.id.emphasis_patrol_area_back);
		emphasis_patrol_area_back.setOnClickListener(this);
		emphasis_patrol_area_commit =(Button) findViewById(R.id.emphasis_patrol_area_commit);
		emphasis_patrol_area_commit.setOnClickListener(this);
		emphasis_patrol_area_zyjb =(Spinner) findViewById(R.id.emphasis_patrol_area_zyjb);// *重要级别
		emphasis_patrol_area_zyjb.setAdapter(new ArrayAdapter<String>(this, R.anim.myspinner, new String[]{"重要","普通"}));
		emphasis_patrol_area_yxzt =(Spinner) findViewById(R.id.emphasis_patrol_area_yxzt);// *有效状态
		emphasis_patrol_area_yxzt.setAdapter(new ArrayAdapter<String>(this, R.anim.myspinner, new String[]{"是","否"}));
		emphasis_patrol_area_qrmq =(EditText) findViewById(R.id.emphasis_patrol_area_qrmq);// *区域名称
		emphasis_patrol_area_sgdw =(EditText) findViewById(R.id.emphasis_patrol_area_sgdw);// *施工单位
		emphasis_patrol_area_gdfzr =(EditText) findViewById(R.id.emphasis_patrol_area_gdfzr);// *工地负责人
		emphasis_patrol_area_sgfdh =(EditText) findViewById(R.id.emphasis_patrol_area_sgfdh);// 施工方电话
		emphasis_patrol_area_xxwz =(EditText) findViewById(R.id.emphasis_patrol_area_xxwz);// *详细位置
		emphasis_patrol_area_ksrq =(TextView) findViewById(R.id.emphasis_patrol_area_ksrq);// 施工计划开始日期
		emphasis_patrol_area_ksrq.setOnClickListener(this);
		emphasis_patrol_area_jsrq =(TextView) findViewById(R.id.emphasis_patrol_area_jsrq);// 施工计划结束日期
		emphasis_patrol_area_jsrq.setOnClickListener(this);
		emphasis_patrol_area_gcjd =(EditText) findViewById(R.id.emphasis_patrol_area_gcjd);// *工程进度
		emphasis_patrol_area_bz =(EditText) findViewById(R.id.emphasis_patrol_area_bz);// 备注
		
		emphasis_patrol_area_qrxq = (Button) findViewById(R.id.emphasis_patrol_area_qrxq);// 区域选取
		emphasis_patrol_area_qrxq.setOnClickListener(this);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emphasis_patrol_area_activity);
		init();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.emphasis_patrol_area_back:
			finishForm();
			break;
		case R.id.emphasis_patrol_area_commit:
			commit();		
			break;
		case R.id.emphasis_patrol_area_ksrq:
			DateTimeDialog.showDateDialog(this, emphasis_patrol_area_ksrq);
			break;
		case R.id.emphasis_patrol_area_jsrq:
			DateTimeDialog.showDateDialog(this, emphasis_patrol_area_jsrq);
			break;
		case R.id.emphasis_patrol_area_qrxq:
			gotoMap();
			break;

		default:
			break;
		}
	}
	
	private void finishForm(){
		new AlertDialog.Builder(this)
		.setTitle("提示")
		.setMessage("数据未保存，是否退出")
		.setPositiveButton("否",new DialogInterface.OnClickListener()  {
			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				// TODO Auto-generated method stub
			}})
        .setNegativeButton("是", new DialogInterface.OnClickListener() {
        	@Override
			public void onClick(DialogInterface dialog,
					int which) {
				// TODO Auto-generated method stub
				finish();
			}}).show();
	}
	private void commit(){
		
		if("".equals(emphasis_patrol_area_qrmq.getText()+"")){
			Toast.makeText(this, "请填区域名称", Toast.LENGTH_LONG).show();
		}else if("".equals(emphasis_patrol_area_sgdw.getText()+"")){
			Toast.makeText(this, "请填施工单位", Toast.LENGTH_LONG).show();
		}else if("".equals(emphasis_patrol_area_gdfzr.getText()+"")){
			Toast.makeText(this, "请填工地负责人", Toast.LENGTH_LONG).show();
		}else if("".equals(emphasis_patrol_area_sgfdh.getText()+"")){
			Toast.makeText(this, "请填施工方电话", Toast.LENGTH_LONG).show();
		}else if("".equals(emphasis_patrol_area_xxwz.getText()+"")){
			Toast.makeText(this, "请填详细位置", Toast.LENGTH_LONG).show();
		}else if("".equals(emphasis_patrol_area_gcjd.getText()+"")){
			Toast.makeText(this, "请填工程进度", Toast.LENGTH_LONG).show();
		}else if(geometry == null){
			Toast.makeText(this, "请选取区域！", Toast.LENGTH_LONG).show();
		}else{
			new MyAsyncTask().execute("");
		}
	}
	private void gotoMap(){
		// 进入地图画面
		AppContext.getInstance().setmHandle(mHandler);
		Intent intent = new Intent("mapBizContain");
		intent.putExtra("type", MapBizViewer.BIZ_MAP_OPERATE_BOUND);
		startActivity(intent);
	}
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==1){
				poly = new Polygon();
				String mMapBound=AppContext.getInstance().getMapBound();
				GeometryVO geometryVO =GeometryUtility.coordToGeometry(mMapBound);
				geometry = new JSONObject();
				JSONArray rings =new JSONArray();
				JSONArray jr0 = new JSONArray();
				List<Object> l=geometryVO.getPoints();
				for(int i=0;i<l.size();++i){
					List<String> p =(List<String>)l.get(i);
					JSONArray jr = new JSONArray();
					if(i==0){
						poly.startPath(Float.parseFloat(p.get(0)), Float.parseFloat(p.get(1)));
					}else{
						poly.lineTo(Float.parseFloat(p.get(0)), Float.parseFloat(p.get(1)));
					}
					for(int j=0;j<p.size();++j){
						try {
							jr.put(Float.parseFloat(p.get(j)));
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					jr0.put(jr);
				}
				rings.put(jr0);
				try {
					geometry.put("rings", rings);
					JSONObject spatialReference =new JSONObject();
					spatialReference.put("wkid", AppContext.getInstance().getSrid());
					geometry.put("spatialReference",spatialReference);
					emphasis_patrol_area_qrxq.setText("已获去区域");
					emphasis_patrol_area_qrxq.setTextColor(Color.RED);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(msg.what==2){
				Toast.makeText(EmphasisPatrolAreaActivity.this, "编辑成功", Toast.LENGTH_LONG).show();
          	  	finish();
			}else if(msg.what==3){
				Toast.makeText(EmphasisPatrolAreaActivity.this, "编辑失败", Toast.LENGTH_LONG).show();
			}
		};
	};
	private void setGeometry(){
		String mMapBound=AppContext.getInstance().getMapBound();
		GeometryVO geometryVO =GeometryUtility.coordToGeometry(mMapBound);
		JsonObject geometry =new JsonObject();
		JsonArray rings = new JsonArray();
		JsonArray jr0= new JsonArray();
		List<Object> l=geometryVO.getPoints();
		for(int i=0;i<l.size();++i){
			/*List<String> p =(List<String>)l.get(i);
			JsonArray jr =new JsonArray();
			for(int j=0;j<p.size();++j){
				try {
					jr.put(Float.parseFloat(p.get(j)));
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			jr0.put(jr);*/
		}
		
	}
	class MyAsyncTask extends AsyncTask<String, Void, String> {
		private ProgressDialog progressDialog;// 进度条
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(EmphasisPatrolAreaActivity.this);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMessage("正在提交中，请稍后");
			progressDialog.show();
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub、
			String contextUrl= "http://172.16.0.77:8399/arcgis/rest/services/GS_APP_LAYER/FeatureServer/2";
			ArcGISFeatureLayer atteditLayer=new ArcGISFeatureLayer(contextUrl,MODE.ONDEMAND);//属性编辑图层专题
			Map<String, Object> attributes= new HashMap<String, Object>();
			attributes.put("AREA_LEVEL", emphasis_patrol_area_zyjb.getSelectedItem()+"");// *重要级别
			attributes.put("VALID_STATE", (emphasis_patrol_area_yxzt.getSelectedItem()+"").equals("否")? 0:1);// *有效状态
			attributes.put("AREA_NAME", emphasis_patrol_area_qrmq.getText()+"");// *区域名称
			attributes.put("CUNIT", emphasis_patrol_area_sgdw.getText()+"");// *施工单位
			attributes.put("SITE_PERSON", emphasis_patrol_area_gdfzr.getText()+"");// *工地负责人
			attributes.put("TEL", emphasis_patrol_area_sgfdh.getText()+"");// 施工方电话
			attributes.put("ADDR", emphasis_patrol_area_xxwz.getText()+"");// *详细位置
			if(!"".equals(emphasis_patrol_area_ksrq.getText()+"")){
				attributes.put("PLAN_SDATE", MyDateTools.string2long(emphasis_patrol_area_ksrq.getText()+"","yyyy-MM-dd"));// 施工计划开始日期
			}
			if(!"".equals(emphasis_patrol_area_jsrq.getText()+"")){
				attributes.put("PLAN_EDATE",MyDateTools.string2long(emphasis_patrol_area_jsrq.getText()+"","yyyy-MM-dd"));// 施工计划结束日期
			}
			attributes.put("REPORT_DATE",new Date().getTime());// 上报日期
			attributes.put("PRJ_PROGRESS", emphasis_patrol_area_gcjd.getText()+"");// *工程进度
			attributes.put("REMARK", emphasis_patrol_area_bz.getText()+"");// 备注
			attributes.put("RP_DEPT_ID",AppContext.getInstance().getCurUser().getDeptId() );// 上报人部门ID
			attributes.put("RP_DEPT_NUM", AppContext.getInstance().getCurUser().getDeptNum());// 上报人部门编号
			attributes.put("RP_DEPT_NAME", AppContext.getInstance().getCurUser().getDeptName());//上报人部门名称
			attributes.put("RP_TEAM_ID",AppContext.getInstance().getCurUser().getGroupId() );// 上报人班组ID
			attributes.put("RP_TEAM_NUM",AppContext.getInstance().getCurUser().getGroupNum() );//
			attributes.put("RP_TEAM_NAME", AppContext.getInstance().getCurUser().getGroupName());//
			attributes.put("REPORT_P_ID", AppContext.getInstance().getCurUser().getUserId());//
			attributes.put("REPORT_P_NUM", AppContext.getInstance().getCurUser().getUserName());//
			attributes.put("REPORT_P_NAME", AppContext.getInstance().getCurUser().getUserAlias());// *上报人名称
			attributes.put("CREATE_U_ID", AppContext.getInstance().getCurUser().getUserId());// 创建人ID
			attributes.put("CREATE_U_NUM", AppContext.getInstance().getCurUser().getUserName());//
			attributes.put("CREATE_U_NAME", AppContext.getInstance().getCurUser().getUserAlias());//
			attributes.put("CREATE_DATE",new Date().getTime());
			
			Graphic delGraphic=new Graphic(poly, null, attributes);
			Graphic[]add=new Graphic[]{
					delGraphic
				};
			// TODO Auto-generated method stub
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			atteditLayer.applyEdits(add, null, null, new callback());
			
			/*JSONObject data = new JSONObject();
			try {
				data.put("f","json");
				JSONArray adds = new JSONArray();
				JSONObject jo = new JSONObject();
				geometry = new JSONObject();
				geometry.put("x", -118.15);
				geometry.put("y", 33.80);
				JSONObject spatialReference =new JSONObject();
				spatialReference.put("wkid", AppContext.getInstance().getSrid());
				geometry.put("spatialReference",spatialReference);
				jo.put("geometry", geometry);
				JSONObject attributes = new JSONObject();
				attributes.put("AREA_LEVEL", emphasis_patrol_area_zyjb.getSelectedItem()+"");// *重要级别
				attributes.put("VALID_STATE", (emphasis_patrol_area_yxzt.getSelectedItem()+"").equals("否")? 0:1);// *有效状态
				attributes.put("AREA_NAME", emphasis_patrol_area_qrmq.getText()+"");// *区域名称
				attributes.put("CUNIT", emphasis_patrol_area_sgdw.getText()+"");// *施工单位
				attributes.put("SITE_PERSON", emphasis_patrol_area_gdfzr.getText()+"");// *工地负责人
				attributes.put("TEL", emphasis_patrol_area_sgfdh.getText()+"");// 施工方电话
				attributes.put("ADDR", emphasis_patrol_area_xxwz.getText()+"");// *详细位置
				if(!"".equals(emphasis_patrol_area_ksrq.getText()+"")){
					attributes.put("PLAN_SDATE", MyDateTools.string2long(emphasis_patrol_area_ksrq.getText()+"","yyyy-MM-dd"));// 施工计划开始日期
				}
				if(!"".equals(emphasis_patrol_area_jsrq.getText()+"")){
					attributes.put("PLAN_EDATE",MyDateTools.string2long(emphasis_patrol_area_jsrq.getText()+"","yyyy-MM-dd"));// 施工计划结束日期
				}
				attributes.put("REPORT_DATE",new Date().getTime());// 上报日期
				attributes.put("PRJ_PROGRESS", emphasis_patrol_area_gcjd.getText()+"");// *工程进度
				attributes.put("REMARK", emphasis_patrol_area_bz.getText()+"");// 备注
				attributes.put("RP_DEPT_ID",AppContext.getInstance().getCurUser().getDeptId() );// 上报人部门ID
				attributes.put("RP_DEPT_NUM", AppContext.getInstance().getCurUser().getDeptNum());// 上报人部门编号
				attributes.put("RP_DEPT_NAME", AppContext.getInstance().getCurUser().getDeptName());//上报人部门名称
				attributes.put("RP_TEAM_ID",AppContext.getInstance().getCurUser().getGroupId() );// 上报人班组ID
				attributes.put("RP_TEAM_NUM",AppContext.getInstance().getCurUser().getGroupNum() );//
				attributes.put("RP_TEAM_NAME", AppContext.getInstance().getCurUser().getGroupName());//
				attributes.put("REPORT_P_ID", AppContext.getInstance().getCurUser().getUserId());//
				attributes.put("REPORT_P_NUM", AppContext.getInstance().getCurUser().getUserName());//
				attributes.put("REPORT_P_NAME", AppContext.getInstance().getCurUser().getUserAlias());// *上报人名称
				attributes.put("CREATE_U_ID", AppContext.getInstance().getCurUser().getUserId());// 创建人ID
				attributes.put("CREATE_U_NUM", AppContext.getInstance().getCurUser().getUserName());//
				attributes.put("CREATE_U_NAME", AppContext.getInstance().getCurUser().getUserAlias());//
				attributes.put("CREATE_DATE",new Date().getTime());
				attributes.put("SP_NUM", "kkkk");
				attributes.put("SP_NAME","test");
				attributes.put("SP_TYPE", "tttt");
				attributes.put("IS_VALID", "1");
				attributes.put("CREATE_DATE", new Date().getTime());
				attributes.put("CREATE_U_ID",  AppContext.getInstance().getCurUser().getUserId());
				attributes.put("CREATE_U_NUM", AppContext.getInstance().getCurUser().getUserName());
				attributes.put("CREATE_U_NAME", AppContext.getInstance().getCurUser().getUserAlias());
				attributes.put("POINT_LEVEL", "gggg");
				attributes.put("VALID_RADIUS", "12");
				
				jo.put("attributes", attributes);
				adds.put(jo);
				data.put("features", adds);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//String s= "{\"adds\": [{\"attributes\":{\"AREA_LEVEL\":\"重要\",\"AREA_NAME\":\"区域名称TEST002\",\"VALID_STATE\":\"1\",\"CUNIT\":\"施工单位TEST002\",\"SITE_PERSON\":\"工地负责人TEST002\",\"TEL\":\"888888\",\"ADDR\":\"详细位置TEST002\",\"PLAN_SDATE\":1438387200000,\"PLAN_EDATE\":1440979200000,\"REPORT_DATE\":1440384969000,\"REPORT_P_NAME\":\"AAAA\",\"RP_DEPT_ID\":\"21\",\"RP_DEPT_NUM\":\"广东龙泉科技有限公司_002\",\"RP_DEPT_NAME\":\"AAA\"\"REPORT_P_ID\":\"23\",\"REPORT_P_NUM\":\"AAAA\",\"PRJ_PROGRESS\":\"工程进度\",\"REMARK\":\"备注\",\"SHAPE_STR\":true,\"CREATE_DATE\":1440385002468,\"CREATE_U_ID\":2,\"CREATE_U_NUM\":\"sitanwo\",\"CREATE_U_NAME\":\"梁锦峰\",\"AREA_NUM\":\"EIA201508240002\"},\"geometry\":{\"rings\":[[[40450.998671523485,30448.124625989516],[40971.741765989806,30448.124625989516],[40953.78510755993,29801.684922514083],[40540.78196367285,29819.641580943957],[40450.998671523485,30448.124625989516]]],\"spatialReference\":{\"wkid\":2435}}}],\"f\": \"json\"}";
			return SpringUtil.postData(contextUrl, data.toString());*/
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
		}
		 //属性编辑后回调
		 class callback implements CallbackListener<FeatureEditResult[][]>{

			@Override
			public void onCallback(FeatureEditResult[][] arg0) {
				// TODO Auto-generated method stub
				if(progressDialog!=null){
					progressDialog.dismiss();
				}
				for(int i=0;i<arg0.length;++i){
					if(arg0[i]!=null&&arg0[i].length>0){
						if(arg0[i][0]!=null&&arg0[i][0].isSuccess()){
							Message message = new Message();
							message.what = 2;
							mHandler.sendMessage(message);
						}else{
							Message message = new Message();
							message.what = 3;
							mHandler.sendMessage(message);
						}
					}
				}
				
			}
			@Override
			public void onError(Throwable arg0) {
				// TODO Auto-generated method stub
				if(progressDialog!=null){
					progressDialog.dismiss();
				}
				Toast.makeText(EmphasisPatrolAreaActivity.this, "编辑错误", Toast.LENGTH_LONG).show();
			}
			 
		 }
		
	}

}
