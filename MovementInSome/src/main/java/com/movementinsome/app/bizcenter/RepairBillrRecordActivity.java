package com.movementinsome.app.bizcenter;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.kernel.activity.FullActivity;

import java.util.HashMap;
import java.util.Map;

public class RepairBillrRecordActivity extends FullActivity implements OnClickListener  {

	private WebView repair_bill_record_web;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.repair_bill_record_activity);
		repair_bill_record_web = (WebView) findViewById(R.id.repair_bill_record_web);
		
		repair_bill_record_web.setWebViewClient(new myWebViewClient());
		repair_bill_record_web.getSettings().setJavaScriptEnabled(true);//设置webview打开的页面里面的js生效
		repair_bill_record_web.setScrollbarFadingEnabled(true);
		repair_bill_record_web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		repair_bill_record_web.getSettings().setDefaultTextEncodingName("UTF-8");
		repair_bill_record_web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		repair_bill_record_web.setWebChromeClient(new WebChromeClient());
		
		String username = AppContext.getInstance().getCurUser().getUserName();
		Long groupid = AppContext.getInstance().getCurUser().getGroupId();
		String userid = AppContext.getInstance().getCurUser().getUserId();
		String url = AppContext.getInstance().getWorkorderManagementUrl()+
				"gisApp/mobileView.html?userName="+username+"&groupId="+groupid+"&userId="+userid;
		
		Map<String, String>m=new HashMap<String, String>();
		m.put("charset", "UTF-8");
		repair_bill_record_web.loadUrl(url, m);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
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
			repair_bill_record_web.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
