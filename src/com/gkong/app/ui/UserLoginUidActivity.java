package com.gkong.app.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.gkong.app.MyApplication;
import com.gkong.app.R;
import com.gkong.app.ui.base.BaseFragmentActivity;
import com.gkong.app.ui.fragment.LoginFragment;
import com.gkong.app.ui.fragment.UnLoginFragment;

public class UserLoginUidActivity extends BaseFragmentActivity {
	private final static String TAG = "UserLoginUidActivity";

	public static String SharedName = "login";
	public static String UID = "uid";// 用户名
	public static String PWD = "pwd";// 密码
	private ImageView goHome;
	private MyApplication application;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_login);
		application = (MyApplication) getApplication();
		goHome = (ImageView) findViewById(R.id.show_title_gohome);
		goHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		if (application.loginInfo == null) {
			setLogin();
		} else {
			setUnLogin();
		}
	}

	public void setUnLogin() {
		UnLoginFragment unLoginFragment = new UnLoginFragment();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.add(R.id.login_frame, unLoginFragment);
		transaction.commit();
	}

	public void setLogin() {
		LoginFragment loginFragment = new LoginFragment();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.add(R.id.login_frame, loginFragment);
		transaction.commit();
	}
}