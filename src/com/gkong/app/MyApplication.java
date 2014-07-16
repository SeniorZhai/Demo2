package com.gkong.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gkong.app.data.RequestManager;
import com.gkong.app.model.ClassBoard;
import com.gkong.app.model.LoginInfo;

import android.app.Application;

public class MyApplication extends Application {
	public LoginInfo loginInfo = null;
	public ArrayList<Map<String, Object>> list = null;
	@Override
	public void onCreate() {
		super.onCreate();
		list = new ArrayList<Map<String,Object>>();
		RequestManager.init(this);
	}
}
