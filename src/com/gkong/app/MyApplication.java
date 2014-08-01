package com.gkong.app;

import java.util.ArrayList;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.gkong.app.config.Api;
import com.gkong.app.data.ApiParams;
import com.gkong.app.data.RequestManager;
import com.gkong.app.model.LoginInfo;
import com.gkong.app.ui.UserLoginUidActivity;
import com.google.gson.Gson;

public class MyApplication extends Application {
	public static LoginInfo loginInfo = null;
	public ArrayList<Map<String, Object>> list = null;
	private SharedPreferences share;
	
	@Override
	public void onCreate() {
		super.onCreate();
		list = new ArrayList<Map<String,Object>>();
		RequestManager.init(this);
		share = getSharedPreferences(UserLoginUidActivity.SharedName, Context.MODE_PRIVATE);
		if (share.contains(UserLoginUidActivity.UID)) {
			executeRequest(new StringRequest(Method.POST, Api.Login, responseListener(),
					errorListener()) {
				protected Map<String, String> getParams() {
					return new ApiParams().with("username", share.getString(UserLoginUidActivity.UID, "")).with("password", share.getString(UserLoginUidActivity.PWD, ""));
				}
			});
		}
	}
	private void executeRequest(Request<?> request) {
		RequestManager.addRequest(request, this);
	}
	private Response.Listener<String> responseListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new Gson();
				LoginInfo json = gson.fromJson(response, LoginInfo.class);
				if (json.isSuccess()) {
					MyApplication.loginInfo = json;
				}else {
					MyApplication.loginInfo = null;
				}
			}
		};
	}
	protected Response.ErrorListener errorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(MyApplication.this, "网络错误", Toast.LENGTH_LONG).show();
			}
		};
	}
}
