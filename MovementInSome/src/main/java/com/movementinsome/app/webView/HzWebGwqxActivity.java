package com.movementinsome.app.webView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.movementinsome.R;
import com.movementinsome.kernel.activity.FullActivity;

import java.util.HashMap;
import java.util.Map;

public class HzWebGwqxActivity extends FullActivity implements OnClickListener  {

	private WebView web_specialshow_web;
	private Button web_specialshow_back;
	private String serverUrl;
	private String url;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_specialshow_activity);
		Intent startingIntent = getIntent();
		url = startingIntent.getStringExtra("url");
		web_specialshow_web = (WebView) findViewById(R.id.web_specialshow_web);
		web_specialshow_back = (Button) findViewById(R.id.web_specialshow_back);
		web_specialshow_back.setOnClickListener(this);
		web_specialshow_web.setWebViewClient(new myWebViewClient());
		web_specialshow_web.getSettings().setJavaScriptEnabled(true);//设置webview打开的页面里面的js生效
		web_specialshow_web.setScrollbarFadingEnabled(true);
		web_specialshow_web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		web_specialshow_web.getSettings().setDefaultTextEncodingName("UTF-8");
		web_specialshow_web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		web_specialshow_web.setWebChromeClient(new WebChromeClient());
		
		serverUrl = url;

		String url = serverUrl;
		Map<String, String>m=new HashMap<String, String>();
		m.put("charset", "UTF-8");
		web_specialshow_web.loadUrl(url, m);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.web_specialshow_back:
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
			web_specialshow_web.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
