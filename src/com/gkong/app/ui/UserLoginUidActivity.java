package com.gkong.app.ui;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.gkong.app.ui.base.BaseActivity;
import com.gkong.app.utils.CommonUtil;
import com.google.gson.Gson;

public class UserLoginUidActivity extends BaseActivity implements
		OnClickListener {
	private final static String TAG = "UserLoginUidActivity";

	public static String SharedName = "login";
	public static String UID = "uid";// �û���
	public static String PWD = "pwd";// ����

	private MyApplication application;
	
	private ImageView goHome;
	private EditText editUserID;
	private EditText editPwd;
	private Button btnEnter;
	private Button btnUnLogin;
	private View unloginLinear;
	private SharedPreferences share;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_login_activity);
		application = (MyApplication)getApplication();
		initControl();
		initSharePreferences();
	}

	private void initSharePreferences() {
		share = getSharedPreferences(SharedName, Context.MODE_PRIVATE);
		if (share.contains(UID)) {
			editUserID.setText(share.getString(UID, ""));
			editPwd.setText(share.getString(PWD, ""));
		}
	}

	private void initControl() {
		unloginLinear = (View)findViewById(R.id.unlogin_linear);
		if (application.loginInfo != null) {
			unloginLinear.setVisibility(View.VISIBLE);
		}else {
			unloginLinear.setVisibility(View.GONE);
		}
		editUserID = (EditText) findViewById(R.id.edittext_user_username);
		editPwd = (EditText) findViewById(R.id.edittext_user_pwd);
		btnEnter = (Button) findViewById(R.id.button_user_login);
		btnEnter.setOnClickListener(this);
		btnUnLogin = (Button) findViewById(R.id.button_user_unlogin);
		btnUnLogin.setOnClickListener(this);
		goHome = (ImageView) findViewById(R.id.show_title_gohome);
		goHome.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_user_login:
			checkUsername(editUserID.getText().toString(), editPwd.getText()
					.toString());
			break;
		case R.id.button_user_unlogin:
			unloginLinear.setVisibility(View.GONE);
			application.loginInfo = null;
			break;
		case R.id.show_title_gohome:
				finish();
			break;
		}
	}
	private void checkUsername(final String name, final String pwd) {
		if (TextUtils.isEmpty(name)) {
			showShortToast(getResources().getString(R.string.user_username));
			return;
		} else if (TextUtils.isEmpty(pwd)) {
			showShortToast(getResources().getString(R.string.user_pwd));
			return;
		} else if (!CommonUtil.checkNetState(this)) {
			showLongToast("û������");
			return ;
		}
		executeRequest(new StringRequest(Method.POST, Api.Login, responseListener(),
				errorListener()) {
			protected Map<String, String> getParams() {
				return new ApiParams().with("username", name).with("password", pwd);
			}
		});
	}
	@Override
	public void onStop() {
		super.onStop();
		RequestManager.cancelAll(this);
	}
	private Response.Listener<String> responseListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new Gson();
				application.loginInfo = gson.fromJson(response, LoginInfo.class);
				if (application.loginInfo.isSuccess()) {
					Editor edit = share.edit();
					edit.putString(UID, editUserID.getText().toString());
					edit.putString(PWD, editPwd.getText().toString());
					edit.commit();
					showLongToast("����ɹ�");
					finish();
				}else {
					editPwd.setText("");
					showLongToast("�˻������������");
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
				Toast.makeText(UserLoginUidActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
			}
		};
	}
}
