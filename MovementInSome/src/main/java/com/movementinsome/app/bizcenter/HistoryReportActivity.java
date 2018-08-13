package com.movementinsome.app.bizcenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.mytask.ShowTaskMsgActivity;
import com.movementinsome.app.pub.dialog.DateTimeDialog;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.kernel.initial.model.ReportHistory;
import com.movementinsome.kernel.initial.model.Table;
import com.movementinsome.kernel.initial.model.TableField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryReportActivity extends FullActivity implements OnClickListener {
	private ImageView historyReportImageBack;
	private ListView historyReportLvData;
	private MyAdapter adapter;
	private JSONArray date;
	private String findUploadInfo ="/heartbeatResource/findUploadInfo";
	private Spinner historyReportType;
	private String[] type;
	private String[] typeName;
	private ReportHistory reportHistory =AppContext.getInstance().getReportHistory();
	private List<Table> tables;
	private Table table;
	private TextView updata;
	private TextView dodata;
	private Button chaxun;
	private int fos;
	private Handler mHandler;
	private TextView totalNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.historty_report_activity);
		if(reportHistory!=null){
			tables = reportHistory.getTables();
			if(tables!=null){
				type = new String[tables.size()];
				typeName = new String[tables.size()];
				for(int i=0;i<tables.size();++i){
					type[i]=tables.get(i).getTableName();
					typeName[i]=tables.get(i).getName();
				}
			}
			
		}
		initView();
		
		TextView emptyTextView = new TextView(this);
		emptyTextView.setLayoutParams(new LayoutParams(-1, -1));
		emptyTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER);
		emptyTextView.setText("暂无数据");
		emptyTextView.setVisibility(View.GONE);
		((ViewGroup) historyReportLvData.getParent()).addView(emptyTextView);
		historyReportLvData.setEmptyView(emptyTextView);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");       
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
		String str = formatter.format(curDate);
		String str2=str.replace("年", "-");
		String str3=str2.replace("月", "-");
		String str4=str3.replace("日", "");
		String strDate = str4;
		if(strDate!=null){
			updata.setText(strDate);
			dodata.setText(strDate);
		}
		if(!updata.getText().toString().equals("")&&!dodata.getText().toString().equals("")){
			table = tables.get(fos);
			new MyTask().execute(type[fos]);
		}
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case 1:
						totalNumber.setVisibility(View.VISIBLE);
						Long downloadedAllSize = msg.getData().getLong("num");
						totalNumber.setText("共有“"+downloadedAllSize+"”条记录");
						break;
				}
			}
		};
	}
	private void initView(){
		historyReportImageBack =(ImageView)findViewById(R.id.historyReportImageBack);
		historyReportImageBack.setOnClickListener(this);
		updata=(TextView)findViewById(R.id.updata);
		updata.setOnClickListener(this);
		dodata=(TextView)findViewById(R.id.dodata);
		dodata.setOnClickListener(this);
		chaxun=(Button)findViewById(R.id.chaxun);
		chaxun.setOnClickListener(this);
		historyReportLvData =(ListView)findViewById(R.id.historyReportLvData);
		historyReportType = (Spinner) findViewById(R.id.historyReportType);
		totalNumber = (TextView) findViewById(R.id.totalNumber);
		if(typeName!=null){
			historyReportType.setAdapter(new ArrayAdapter<String>(HistoryReportActivity.this,R.anim.myspinner,typeName));
		}
		historyReportType.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if(!updata.getText().toString().equals("")&&!dodata.getText().toString().equals("")){
					table = tables.get(arg2);
					new MyTask().execute(type[arg2]);
				}
				fos = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (date == null) {
			date = new JSONArray();
		}
		adapter = new MyAdapter(this);
		historyReportLvData.setAdapter(adapter);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.historyReportImageBack:	
			finish();
			break;
		
		case R.id.updata:
			DateTimeDialog.showDateDialog(this, updata);
			break;
		
		case R.id.dodata:
			DateTimeDialog.showDateDialog(this, dodata);
			break;
		
		case R.id.chaxun:
			if(!updata.getText().toString().equals("")&&!dodata.getText().toString().equals("")){
				table = tables.get(fos);
				new MyTask().execute(type[fos]);
			}else{
				
			}
			break;
		
		default:
			break;
		}
	}
	private class MyTask  extends AsyncTask<String, Void, String>{
		private ProgressDialog progre;		//进度条显示
		public MyTask(){
			progre = new ProgressDialog(HistoryReportActivity.this);
			progre.setCancelable(false);
			progre.setCanceledOnTouchOutside(false);
			progre.setMessage("正在查询,请勿退出,耐心等候……");
			progre.show();
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			JSONObject ob=new JSONObject();
			String result =null;
			try {
				ob.put("tableName", arg0[0]);
				ob.put("createUNum", AppContext.getInstance().getCurUser().getUserName());
				ob.put("createDateStart", updata.getText().toString());
				ob.put("createDateEnd", dodata.getText().toString());
				String url = AppContext.getInstance().getServerUrl();
				url+=findUploadInfo;
				result = SpringUtil.postData(url, ob.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(progre != null)
				progre.dismiss();
			if(result!=null){
				try {
					JSONObject jo =new JSONObject(result);
					String code =jo.getString("code");
					if("1".equals(code)){
						String content =jo.getString("content");
						date = new JSONArray(content);
						if(date.length()>0){
							Message message = new Message();
							message.what = 1;
							message.getData().putLong("num", date.length());
							mHandler.sendMessage(message);
						}else{
							totalNumber.setVisibility(View.GONE);
						}
						adapter = new MyAdapter(HistoryReportActivity.this);
						historyReportLvData.setAdapter(adapter);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	class MyAdapter extends BaseAdapter {

		private Context context;

		public MyAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return date.length();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			try {
				return date.get(arg0);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = View.inflate(context, R.layout.history_list_item,
						null);
			}
			LinearLayout history_list_item_ll = (LinearLayout) convertView
					.findViewById(R.id.history_list_item_ll);
			TextView history_list_item_num = (TextView) convertView
					.findViewById(R.id.history_list_item_num);
			history_list_item_num.setText((position + 1) + "");
			TextView history_list_item_content = (TextView) convertView
					.findViewById(R.id.history_list_item_content);
			Button history_list_item_del=(Button) convertView.findViewById(R.id.history_list_item_del);
			try {
				final JSONObject jo = date.getJSONObject(position);
				final List<TableField>tableFields = table.getFields();
				String t = "";
				for(TableField tableField :tableFields){
					if("true".equals(tableField.getShowDesktop())){
						t+=tableField.getzName()+":"+jo.getString(tableField.getName())+"\n";
					}
				}
				history_list_item_content.setText(t);
				history_list_item_del.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String names = "";
						String values = "";
						String guid = "";
						try {
							guid=jo.getString("guid");
						} catch (JSONException e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						for(TableField tableField :tableFields){
							names+=names.equals("")? tableField.getzName():","+tableField.getzName();
							try {
								values+=values.equals("")?jo.getString(tableField.getName()):","+jo.getString(tableField.getName());
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								values+=values.equals("")?" ":","+" ";
							}
						}
						Intent intent =new Intent(HistoryReportActivity.this, ShowTaskMsgActivity.class);
						intent.putExtra("guid", guid);
						intent.putExtra("names", names);
						intent.putExtra("values", values);
						startActivity(intent);
						
					}
				});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				history_list_item_content.setText("");
			}
			
			history_list_item_del.setText("详细");

			
			return convertView;
		}

	}

}
