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
import com.gkong.app.ui.UserLoginUidActivity;
import com.gkong.app.utils.CommonUtil;
import com.gkong.app.view.DeletableEditText;
import com.gkong.app.view.SmoothProgressBar;
import com.google.gson.Gson;

public class LoginFragment extends Fragment {
	private Context mContext;
	private SmoothProgressBar progressBar;
	private DeletableEditText userNameInput;
	private DeletableEditText userPasswordInput;
	private Button loginBn;
	private MyApplication application;
	private SharedPreferences share;

	public static String SharedName = "login";
	public static String UID = "uid";// 用户名
	public static String PWD = "pwd";// 密码

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		application = (MyApplication) getActivity().getApplication();
		share = mContext.getSharedPreferences(SharedName, Context.MODE_PRIVATE);

		View view = inflater.inflate(R.layout.fragment_user_login, container,
				false);
		loginBn = (Button) view.findViewById(R.id.login_button);
		userNameInput = (DeletableEditText) view
				.findViewById(R.id.login_user_name);
		userPasswordInput = (DeletableEditText) view
				.findViewById(R.id.login_user_password);
		progressBar = (SmoothProgressBar) view
				.findViewById(R.id.login_progressbar);
		loginBn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				checkUsername(userNameInput.getText().toString(),
						userPasswordInput.getText().toString());
			}
		});
		if (share.contains(UID)) {
			userNameInput.setText(share.getString(UID, ""));
			userPasswordInput.setText(share.getString(PWD, ""));
		}
		return view;
	}

	@Override
	public void onStop() {
		super.onStop();
		RequestManager.cancelAll(this);
	}

	private void checkUsername(final String name, final String pwd) {
		if (TextUtils.isEmpty(name)) {
			Toast.makeText(mContext,
					getResources().getString(R.string.user_username),
					Toast.LENGTH_LONG).show();
			return;
		} else if (TextUtils.isEmpty(pwd)) {
			Toast.makeText(mContext,
					getResources().getString(R.string.user_pwd),
					Toast.LENGTH_LONG).show();
			return;
		} else if (!CommonUtil.checkNetState(mContext)) {
			Toast.makeText(mContext, "没有网络", Toast.LENGTH_LONG).show();
			return;
		}
		progressBar.setVisibility(View.VISIBLE);
		executeRequest(new StringRequest(Method.POST, Api.Login,
				responseListener(), errorListener()) {
			protected Map<String, String> getParams() {
				return new ApiParams().with("username", name).with("password",
						pwd);
			}
		});
	}

	private Response.Listener<String> responseListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new Gson();
				application.loginInfo = gson
						.fromJson(response, LoginInfo.class);
				if (application.loginInfo.isSuccess()) {
					Editor edit = share.edit();
					edit.putString(UID, userNameInput.getText().toString());
					edit.putString(PWD, userPasswordInput.getText().toString());
					edit.commit();
					progressBar.setVisibility(View.GONE);
					((UserLoginUidActivity) mContext).setUnLogin();
				} else {
					userPasswordInput.setText("");
					Toast.makeText(mContext, "账户名或密码错误", Toast.LENGTH_LONG)
							.show();
					progressBar.setVisibility(View.GONE);
				}
			}
		};
	}

	protected void executeRequest(Request<?> request) {
		RequestManager.addRequest(request, this);
	}

	protected Response.ErrorListener errorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(mContext, "网络错误", Toast.LENGTH_LONG).show();
				progressBar.setVisibility(View.GONE);
			}

		};
	}
}
