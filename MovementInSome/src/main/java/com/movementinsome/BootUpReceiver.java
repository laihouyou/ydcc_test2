package com.movementinsome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootUpReceiver extends BroadcastReceiver {

	public void onReceive(Context context, Intent intent) {
		if(AppContext.getInstance().getAutoStart()!=null){
			if (AppContext.getInstance().getAutoStart().isBoot()){
				Intent i = new Intent(context, Appstart.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);
			}
		}
	}
}
