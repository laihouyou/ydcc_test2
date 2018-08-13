package com.movementinsome.map.nearby;

import android.widget.Toast;

import com.movementinsome.AppContext;

//@SuppressLint("ShowToast")
public class ToastUtils {

	public  static void  show(String showContent){
		Toast.makeText(AppContext.getInstance(),showContent,Toast.LENGTH_LONG).show();
	}

}
