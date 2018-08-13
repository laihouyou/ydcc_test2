/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.movementinsome.kernel.location.trace;

import android.content.Context;
import android.content.Intent;

import com.movementinsome.AppContext;
import com.movementinsome.app.pub.Constant;


/**
 * This class is to manage the XMPP connection between client and server.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class TraceManager {

	
	private Context context;
	private long minTime;
	private float minDistance;
	
	public TraceManager(Context context,long minTime,float minDistance){
		this.context = context;
		this.minTime = minTime;
		this.minDistance = minDistance;
	}

	public void startService() {
		Thread serviceThread = new Thread(new Runnable() {
			@Override
			public void run() {
				TraceService.init(minTime,minDistance,context);
				Intent intent = new Intent();
				intent.setPackage(AppContext.getInstance().getPackageName());
				intent.setAction(Constant.SERVICE_NAME);
				context.startService(intent);
			}
		});
		serviceThread.start();
	}

	public void stopService() {
		Intent intent = new Intent();
		intent.setPackage(AppContext.getInstance().getPackageName());
		intent.setAction(Constant.SERVICE_NAME);
		context.stopService(intent);
	}
}
