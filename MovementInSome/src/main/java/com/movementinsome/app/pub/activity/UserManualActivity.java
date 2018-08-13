package com.movementinsome.app.pub.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.adapter.UserHelpAdapter;
import com.movementinsome.kernel.activity.FullActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 用户使用手册
 * @author gddst
 *
 */
public class UserManualActivity extends FullActivity implements OnItemClickListener {

	private ListView helpListHtml;
	private WebView helpWebViewHtml;
	private LinearLayout helpLinearHtml;
	private Button helpBtnBack;
	private Button helpBtnGoBack;
	// http://172.16.0.77:8190/gisApp/index_doc.htm
	private UserHelpAdapter adapter;
	private String url;
	private List<String>urlList;
	private String USER_MANUAL="/USER_MANUAL/index_doc.htm";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_manual_activity);
		urlList=new ArrayList<String>();

		helpListHtml = (ListView) findViewById(R.id.helpListHtml);
		helpListHtml.setVisibility(View.GONE);
		helpWebViewHtml = (WebView) findViewById(R.id.helpWebViewHtml);
		helpBtnBack = (Button) findViewById(R.id.helpBtnBack);
		helpBtnGoBack = (Button)findViewById(R.id.helpBtnGoBack);
		helpLinearHtml = (LinearLayout)findViewById(R.id.helpLinearHtml);
		
		/*adapter = new UserHelpAdapter(this, chapters);
		helpListHtml.setAdapter(adapter);
		helpListHtml.setOnItemClickListener(this);*/
		helpWebViewHtml.setWebViewClient(new myWebViewClient());
		helpBtnBack.setOnClickListener(myclickListener);
		helpBtnGoBack.setOnClickListener(myclickListener);
		helpBtnGoBack.setVisibility(View.INVISIBLE);
		helpWebViewHtml.setVisibility(View.VISIBLE);
		helpWebViewHtml.getSettings().setJavaScriptEnabled(true);//设置webview打开的页面里面的js生效
		helpWebViewHtml.getSettings().setDefaultTextEncodingName("UTF-8");
		helpWebViewHtml.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		//url="http://"+AppContext.getInstance().getIniConnSvr()+"/fisds"+USER_MANUAL;
		url = AppContext.getInstance().getServerUrl();
		url = url.substring(0, url.lastIndexOf('/'))+USER_MANUAL;
		Map<String, String>m=new HashMap<String, String>();
		m.put("charset", "UTF-8");
		helpWebViewHtml.loadUrl(url, m);
	
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		/*String htmlPath = pScardzzSystem+"usermanual/html_" + engItem[arg2]+"/";
		
		File file = new File(htmlPath);
		if(!file.exists()){
			pToastShow("该书本中目前没有资料");
			return;
		}
		
		helpLinearHtml.setVisibility(View.GONE);
		helpBtnGoBack.setVisibility(View.VISIBLE);
		helpBtnBack.setBackgroundResource(R.drawable.btn_bg);
		helpBtnBack.setText("列表");
		
		helpWebViewHtml.setVisibility(View.VISIBLE);
		helpWebViewHtml.loadUrl("file://"+htmlPath+"index.html");*/
		
	}

	private OnClickListener myclickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.helpBtnBack:
				/*if(helpWebViewHtml.isShown()){
					helpWebViewHtml.clearFormData();
					helpBtnBack.setBackgroundResource(R.drawable.back);
					helpBtnBack.setText("");
					helpBtnGoBack.setVisibility(View.INVISIBLE);
					helpWebViewHtml.setVisibility(View.GONE);
					helpLinearHtml.setVisibility(View.VISIBLE);
				}else{
					finish();
				}*/
				int x= urlList.size()-1;
				if(x<0){
					finish();
				}else{
					helpWebViewHtml.goBack();
					urlList.remove(x);
				}
				break;
			case R.id.helpBtnGoBack:
				break;
			default:
				break;
			}
		}
	};
	
	 //Web视图 
    private class myWebViewClient extends WebViewClient { 
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
        	Map<String, String>m=new HashMap<String, String>();
    		m.put("charset", "UTF-8");
    		view.loadUrl(url, m);
    		urlList.add(url);
        //  view.loadUrl(url); 
            return true; 
        } 
        
    } 
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			int x= urlList.size()-1;
			if(x<0){
				finish();
			}else{
				helpWebViewHtml.goBack();
				urlList.remove(x);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
