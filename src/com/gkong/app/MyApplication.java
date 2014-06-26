package com.gkong.app;

import com.gkong.app.data.RequestManager;
import com.gkong.app.model.LoginInfo;

import android.app.Application;

public class MyApplication extends Application {
	public LoginInfo loginInfo = null;
	@Override
	public void onCreate() {
		super.onCreate();
		RequestManager.init(this);
	}
}
