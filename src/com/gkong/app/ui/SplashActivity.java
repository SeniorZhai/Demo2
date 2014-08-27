package com.gkong.app.ui;

import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.gkong.app.MyApplication;
import com.gkong.app.R;
import com.gkong.app.data.BoardDao;
import com.gkong.app.model.ClassBoardSrc.Item;
import com.gkong.app.ui.base.BaseActivity;

public class SplashActivity extends BaseActivity {
	public List<Item> myList;
	private Handler mHandler = new Handler();
	private BoardDao dao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(this, R.layout.start_activity, null);
		setContentView(view);
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
		view.setAnimation(animation);
		// 用于列表存储
		dao = new BoardDao(this);
		myList = ((MyApplication)getApplication()).myList;
		myList = dao.listCache("_id" + "> ?",
				new String[] { "0" });
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						goHome();
					}
				}, 500);
			}
		});
	}

	private void goHome() {
		openActivity(MainActivity.class);
		defaultFinish();
	};

}