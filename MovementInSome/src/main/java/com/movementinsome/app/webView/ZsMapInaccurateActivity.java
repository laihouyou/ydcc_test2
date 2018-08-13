package com.movementinsome.app.webView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.kernel.activity.FullActivity;

import java.util.HashMap;
import java.util.Map;

public class ZsMapInaccurateActivity extends FullActivity implements OnClickListener  {

	private WebView web_sendorders_web;
	private Button web_sendorders_back;
	private TextView web_Title;
	private String serverUrl;
	private String url;
	private String name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_sendorders_activity);
		web_sendorders_web = (WebView) findViewById(R.id.web_sendorders_web);
		web_sendorders_back = (Button) findViewById(R.id.web_sendorders_back);
		web_Title = (TextView) findViewById(R.id.web_Title);
		web_Title.setText("GIS派单");
		web_sendorders_back.setOnClickListener(this);
		web_sendorders_web.setWebViewClient(new myWebViewClient());
		web_sendorders_web.getSettings().setJavaScriptEnabled(true);//设置webview打开的页面里面的js生效
		web_sendorders_web.setScrollbarFadingEnabled(true);
		web_sendorders_web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		web_sendorders_web.getSettings().setDefaultTextEncodingName("UTF-8");
		web_sendorders_web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		web_sendorders_web.setWebChromeClient(new WebChromeClient());
		
		url = "http://59.33.37.170:9004/gisApp/solution/mobile/mapInaccurate.jsp?userId=";

		serverUrl = url+AppContext.getInstance().getCurUser().getUserId();

		String url = serverUrl;
		Map<String, String>m=new HashMap<String, String>();
		m.put("charset", "UTF-8");
		web_sendorders_web.loadUrl(url, m);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.web_sendorders_back:
			finish();
			break;

		default:
			break;
		}
	}
	 //Web视图 
    private class myWebViewClient extends WebViewClient { 
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
        	Map<String, String>m=new HashMap<String, String>();
    		m.put("charset", "UTF-8");
    		view.loadUrl(url, m);
            return true; 
        } 
        
    } 
    private class MyWebChromeClient extends WebChromeClient{
    	
    }
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			web_sendorders_web.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
