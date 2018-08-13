package com.movementinsome.app.pub.activity;

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

public class WebCheckPhotoActivity extends FullActivity implements OnClickListener  {

	private WebView web_checkphoto_web;
	private Button web_checkphoto_back;
	private String serverUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_checkphoto_activity);
		web_checkphoto_web = (WebView) findViewById(R.id.web_checkphoto_web);
		web_checkphoto_back = (Button) findViewById(R.id.web_checkphoto_back);
		web_checkphoto_back.setOnClickListener(this);
		web_checkphoto_web.setWebViewClient(new myWebViewClient());
		web_checkphoto_web.getSettings().setJavaScriptEnabled(true);//设置webview打开的页面里面的js生效
		web_checkphoto_web.setScrollbarFadingEnabled(true);
		web_checkphoto_web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		web_checkphoto_web.getSettings().setDefaultTextEncodingName("UTF-8");
		web_checkphoto_web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		web_checkphoto_web.setWebChromeClient(new WebChromeClient());
		
		serverUrl=getIntent().getStringExtra("serverUrl");

		String url = serverUrl;
//		http://172.16.81.222:8091/fileService/img.jsp?findKey=
		Map<String, String>m=new HashMap<String, String>();
		m.put("charset", "UTF-8");
		web_checkphoto_web.loadUrl(url, m);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.web_checkphoto_back:
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
			web_checkphoto_web.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
