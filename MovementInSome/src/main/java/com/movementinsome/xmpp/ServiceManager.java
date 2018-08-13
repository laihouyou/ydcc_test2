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
package com.movementinsome.xmpp;

import android.content.Context;
import android.content.Intent;

/**
 * This class is to manage the notificatin service and to load the
 * configuration.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public final class ServiceManager {

	private Context context;
	private String xmppHost;
	private int xmppPort;
	private String userName;
	private String phoneIMEI;

	public ServiceManager(Context context, String xmppHost, int xmppPort,
			String userName, String phoneIMEI) {
		this.context = context;
		this.xmppHost = xmppHost;
		this.xmppPort = xmppPort;
		this.userName = userName;
		this.phoneIMEI = phoneIMEI;
	}

	public void startService() {
		Thread serviceThread = new Thread(new Runnable() {
			@Override
			public void run() {
				NotificationService.init(context,xmppHost, xmppPort, phoneIMEI,
						userName);
				// Intent intent = NotificationService.getIntent();
				Intent intent = new Intent(context,NotificationService.class);
//				intent.setAction(Constants.SERVICE_NAME);
				context.startService(intent);
			}
		});
		serviceThread.start();
	}

	public void stopService() {
		Intent intent = new Intent(context,NotificationService.class);
		context.stopService(intent);
	}

}
