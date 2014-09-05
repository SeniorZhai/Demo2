package com.gkong.app.ui.fragment;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gkong.app.MyApplication;
import com.gkong.app.R;
import com.gkong.app.config.Api;
import com.gkong.app.data.ApiParams;
import com.gkong.app.data.RequestManager;
import com.gkong.app.model.LoginInfo;
import com.gkong.app.utils.CommonUtil;
import com.gkong.app.view.DeletableEditText;
import com.gkong.app.view.SmoothProgressBar;
import com.google.gson.Gson;

public class UnLoginFragment extends Fragment   {
	private Context mContext;

	private MyApplication application;
	private SharedPreferences share;
	
	public static String SharedName = "login";
	public static String UID = "uid";// 用户名
	public static String PWD = "pwd";// 密码

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		application = (MyApplication)getActivity().getApplication();
		share = mContext.getSharedPreferences(SharedName, Context.MODE_PRIVATE);
		
		View view = inflater.inflate(R.layout.fragment_user_unlogin, container,
				false);
	
		return view;
	}
}
