package com.gkong.app.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gkong.app.MyApplication;
import com.gkong.app.R;
import com.gkong.app.adapter.ClassAdapter;
import com.gkong.app.adapter.MyArrayAdapter;
import com.gkong.app.config.Api;
import com.gkong.app.model.BBSBoard;
import com.gkong.app.model.ClassBoardSrc.Item;
import com.gkong.app.slidingmenu.SlidingMenu;
import com.gkong.app.ui.base.BaseSlidingFragmentActivity;
import com.gkong.app.utils.SerializeUtils;
import com.gkong.app.utils.ToastUtil;
import com.gkong.app.widget.CircularImage;
import com.gkong.app.widget.XListView;
import com.gkong.app.widget.XListView.IXListViewListener;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

@SuppressLint("SimpleDateFormat")
public class MainActivity extends BaseSlidingFragmentActivity implements
		OnClickListener, IXListViewListener {
	private static final String TAG = "MainActivity";
	private Context mContext = MainActivity.this;
	private MyApplication app;
	// Value
	private int mTag = 0;
	private int page = 1;
	private String boardID;
	private boolean flag = false;
	private String Type = "tech";
	// Data
	private List<Item> subscriboList;
	private List<BBSBoard> BBSList;
	// View
	private Button update, feedback;
	private SlidingMenu sm;
	private ImageView aboveImgQuery;
	private ImageView aboveImgMore;
	private LinearLayout aboveGoHome;
	private TextView aboveTitle;
	private XListView mListView;
	private LinearLayout aboveLoadLayout;
	private LinearLayout aboveLoadFaillayout;
	private ImageButton imgLogin;
	private ListView lvTitle;
	private CircularImage user_avatar;
	// Adapter
	private ClassAdapter classAdapter;
	private MyArrayAdapter listAdapter;
	// 用户反馈
	private FeedbackAgent agent;
	//
	private DisplayImageOptions options;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UmengUpdateAgent.silentUpdate(this);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
		agent = new FeedbackAgent(mContext);
		app = (MyApplication)getApplication();
		subscriboList = app.subscriboList;
		BBSList = new ArrayList<BBSBoard>();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.avatar_default)
				.showImageForEmptyUri(R.drawable.avatar_default)
				.showImageOnFail(R.drawable.avatar_default).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();
		initSlidingMenu();
		setContentView(R.layout.above_slidingmenu);
		initControl();
		initListView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		SerializeUtils.serialization(app.cache.getAbsolutePath(), app.dataGroupList);
	}

	// [start]初始化函数

	private void initSlidingMenu() {
		setBehindContentView(R.layout.behind_slidingmenu);
		sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setShadowDrawable(R.drawable.slidingmenu_shadow);
		sm.setBehindScrollScale(0);
	}

	private void initControl() {
		aboveImgMore = (ImageView) findViewById(R.id.imageview_above_more);
		aboveImgMore.setOnClickListener(this);
		aboveImgQuery = (ImageView) findViewById(R.id.imageview_above_query);
		aboveImgQuery.setOnClickListener(this);
		aboveGoHome = (LinearLayout) findViewById(R.id.linear_above_to_Home);
		aboveGoHome.setOnClickListener(this);
		aboveTitle = (TextView) findViewById(R.id.tv_above_title);
		aboveTitle.setText("技术区最新动态");
		aboveLoadLayout = (LinearLayout) findViewById(R.id.view_loading);
		aboveLoadFaillayout = (LinearLayout) findViewById(R.id.view_loading_fail);
		mListView = (XListView) findViewById(R.id.list_view);
		imgLogin = (ImageButton) findViewById(R.id.behind_edit);
		imgLogin.setOnClickListener(this);
		lvTitle = (ListView) findViewById(R.id.behind_list_show);
		aboveImgMore.setVisibility(View.GONE);
		user_avatar = (CircularImage) findViewById(R.id.behind_avatar);
		user_avatar.setOnClickListener(this);
		update = (Button) findViewById(R.id.umeng_update);
		feedback = (Button) findViewById(R.id.umeng_feedback);
		update.setOnClickListener(this);
		feedback.setOnClickListener(this);
	}

	// 初始化ListView
	private void initListView() {

		listAdapter = new MyArrayAdapter(mContext, BBSList);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setAdapter(listAdapter);
		mListView.setXListViewListener(this);

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mContext, DetailsActivity.class);
				intent.putExtra("url", BBSList.get(position - 1).getTopicID());
				startActivity(intent);
			}
		});
		aboveLoadLayout.setVisibility(View.VISIBLE);
		getNewBoard(Type, "date", page);
		classAdapter = new ClassAdapter(mContext, subscriboList);

		lvTitle.setAdapter(classAdapter);
		lvTitle.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击事件处理
				aboveLoadLayout.setVisibility(View.VISIBLE);

				switch (position) {
				case 0:
					flag = false;
					Type = "tech";
					getNewBoard("tech", "date", 1);
					aboveImgMore.setVisibility(View.GONE);
					break;
				case 1:
					flag = false;
					Type = "notech";
					getNewBoard("notech", "date", 1);
					aboveImgMore.setVisibility(View.GONE);
					break;
				default:
					flag = true;
					getList(boardID, page);
					aboveImgMore.setVisibility(View.VISIBLE);
					break;
				}
			}
		});
	}

	// [end]初始化函数

	// [start]数据请求
	private void getList(String boardID, int page) {
		executeRequest(new StringRequest(Method.GET, Api.List(boardID, page),
				BBSResponseListener(), errorListener()));
	}

	//
	private void getNewBoard(String Type, String Sort, int Page) {
		executeRequest(new StringRequest(Method.GET, Api.NewBoard(Type, Sort,
				Page), BBSResponseListener(), errorListener()));
	}

	// [end]数据请求

	// [start]点击监听
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.imageview_above_more:
			intent = new Intent(mContext, PublishedActivity.class);
			intent.putExtra("BoardID", boardID);
			startActivity(intent);
			break;
		case R.id.imageview_above_query:
			intent = new Intent(MainActivity.this, SearchActivity.class);
			startActivity(intent);
			break;
		case R.id.behind_edit:
			intent = new Intent(mContext, EditActivity.class);
			startActivity(intent);
			break;
		case R.id.linear_above_to_Home:
			showMenu();
			break;
		case R.id.behind_avatar:
			intent = new Intent(MainActivity.this, UserLoginUidActivity.class);
			startActivity(intent);
			break;
		case R.id.umeng_update:
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
				@Override
				public void onUpdateReturned(int updateStatus,
						UpdateResponse updateInfo) {
					switch (updateStatus) {
					case UpdateStatus.No: // has no update
						Toast.makeText(mContext, "已经是最新版本", Toast.LENGTH_SHORT)
								.show();
						break;
					case UpdateStatus.Timeout: // time out
						Toast.makeText(mContext, "超时", Toast.LENGTH_SHORT)
								.show();
						break;
					}
				}
			});
			UmengUpdateAgent.forceUpdate(mContext);
			break;
		case R.id.umeng_feedback:
			agent.startFeedbackActivity();
			break;
		default:
			break;
		}
	}

	// [end]点击监听

	// [start]网络请求反馈
	// 获取文章
	private Response.Listener<String> BBSResponseListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {

				try {
					JSONObject obj1 = new JSONObject(response);
					String str = obj1.getString("d");
					JSONObject obj2 = new JSONObject(str);
					JSONArray array = obj2.getJSONArray("Head");
					Gson gson = new Gson();
					if (page == 1) {
						BBSList.clear();
						SimpleDateFormat sDateFormat = new SimpleDateFormat(
								"MM-dd hh:mm:ss");
						String date = sDateFormat.format(new java.util.Date());
						mListView.setRefreshTime(date);
					}
					for (int i = 0; i < array.length(); i++) {
						BBSBoard obj = gson.fromJson(array.getString(i),
								BBSBoard.class);
						BBSList.add(obj);
					}
					listAdapter.notifyDataSetChanged();
					mListView.stopRefresh();
					mListView.stopLoadMore();
					if (aboveLoadLayout.getVisibility() == View.VISIBLE) {
						aboveLoadLayout.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		};
	}

	// [end]网络请求反馈

	// [start]网络请求错误
	protected Response.ErrorListener errorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				ToastUtil.show(mContext, "网络错误");
			}
		};
	}

	// [end]网络请求错误

	// [start]连续两次返回就退出
	private int keyBackClickCount = 0;

	@Override
	protected void onResume() {
		super.onResume();
		if (((MyApplication) getApplication()).loginInfo != null) {
			ImageLoader.getInstance().displayImage(
					Api.Avatar(((MyApplication) getApplication()).loginInfo
							.getId()), user_avatar, options);
		}
		keyBackClickCount = 0;
		classAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (keyBackClickCount++) {
			case 0:
				Toast.makeText(this,
						getResources().getString(R.string.press_again_exit),
						Toast.LENGTH_SHORT).show();
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						keyBackClickCount = 0;
					}
				}, 2000);
				break;
			case 1:
				finish();
				break;
			default:
				break;
			}
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			showMenu();
		}
		return super.onKeyDown(keyCode, event);
	}

	// [end]连续两次返回就退出

	// [start]XList监听
	@Override
	public void onRefresh() {
		page = 1;
		if (flag) {
			getList(boardID, page);
		} else {
			getNewBoard(Type, "date", page);
		}
	}

	@Override
	public void onLoadMore() {
		page++;
		if (flag) {
			getList(boardID, page);
		} else {
			getNewBoard(Type, "date", page);
		}
	}

	// [start]XList监听

}
