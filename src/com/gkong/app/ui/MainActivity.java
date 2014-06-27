package com.gkong.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gkong.app.R;
import com.gkong.app.adapter.MyArrayAdapter;
import com.gkong.app.config.Api;
import com.gkong.app.model.BBSBoard;
import com.gkong.app.model.ClassBoard;
import com.gkong.app.slidingmenu.SlidingMenu;
import com.gkong.app.ui.base.BaseSlidingFragmentActivity;
import com.gkong.app.utils.ToastUtil;
import com.gkong.app.widget.XListView;
import com.gkong.app.widget.XListView.IXListViewListener;
import com.google.gson.Gson;

public class MainActivity extends BaseSlidingFragmentActivity implements
		OnClickListener, IXListViewListener {
	private Context mContext = MainActivity.this;

	// Value
	private final String LIST_TEXT = "text";
	private final String LIST_URL = "url";
	private int mTag = 1;

	private int page = 1;
	private String url = 114 + "";
	// Data
	private ArrayList<Map<String, Object>> list;
	private ArrayList<BBSBoard> BBSList;
	// View
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
	// Adapter
	private SimpleAdapter lvAdapter;
	private MyArrayAdapter listAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list = new ArrayList<Map<String, Object>>();
		BBSList = new ArrayList<BBSBoard>();
		initSlidingMenu();
		setContentView(R.layout.above_slidingmenu);
		initControl();
		initListView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
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
		aboveTitle.setText("产品体验综合讨论区");
		aboveLoadLayout = (LinearLayout) findViewById(R.id.view_loading);
		aboveLoadFaillayout = (LinearLayout) findViewById(R.id.view_loading_fail);
		mListView = (XListView) findViewById(R.id.list_view);
		imgLogin = (ImageButton) findViewById(R.id.login_login);
		imgLogin.setOnClickListener(this);
		lvTitle = (ListView) findViewById(R.id.behind_list_show);
	}

	// 初始化ListView
	private void initListView() {

		listAdapter = new MyArrayAdapter(mContext, BBSList);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(false);
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
		getList(url, 1);

		lvAdapter = new SimpleAdapter(this, list, R.layout.behind_list_show,
				new String[] { LIST_TEXT },
				new int[] { R.id.textview_behind_title }) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				if (position == mTag) {
					view.setBackgroundResource(R.drawable.back_behind_list);
					lvTitle.setTag(view);
				} else {
					view.setBackgroundColor(Color.TRANSPARENT);
				}
				return view;
			}
		};
		lvTitle.setAdapter(lvAdapter);
		getBoard();
		lvTitle.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mTag = position - 1;
				if (lvTitle.getTag() != null) {
					if (lvTitle.getTag() == view) {
						MainActivity.this.showContent();
						return;
					}
					((View) lvTitle.getTag())
							.setBackgroundColor(Color.TRANSPARENT);
				}
				lvTitle.setTag(view);
				view.setBackgroundResource(R.drawable.back_behind_list);
				MainActivity.this.showContent();
				aboveTitle.setText(list.get(position).get(LIST_TEXT) + "");
				url = "" + list.get(position).get(LIST_URL);
				page = 1;
				aboveLoadLayout.setVisibility(View.VISIBLE);
				getList(url, page);
			}
		});
	}

	// [end]初始化函数

	// [start]数据请求
	private void getList(String boardID, int page) {

		executeRequest(new StringRequest(Method.GET, Api.List(boardID, page),
				responseListener1(), errorListener()));
	}

	private void getBoard() {
		executeRequest(new StringRequest(Method.GET, Api.Board,
				responseListener(), errorListener()));
	}

	// [end]数据请求

	// [start]点击监听
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.imageview_above_more:

			ToastUtil.show(this, "more");
			break;
		case R.id.imageview_above_query:
			ToastUtil.show(this, "query");
			break;
		case R.id.login_login:
			intent = new Intent(MainActivity.this, UserLoginUidActivity.class);
			startActivity(intent);
			break;
		case R.id.linear_above_to_Home:
			showMenu();
			break;
		default:
			break;
		}
	}

	private Response.Listener<String> responseListener1() {
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
					}
					for (int i = 0; i < array.length(); i++) {
						BBSBoard obj = gson.fromJson(array.getString(i),
								BBSBoard.class);
						BBSList.add(obj);
					}
					listAdapter.notifyDataSetChanged();
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

	// [end]点击监听

	// [start]网络请求反馈
	private Response.Listener<String> responseListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject obj1 = new JSONObject(response);
					String str = obj1.getString("d");
					JSONObject obj2 = new JSONObject(str);
					JSONArray array = obj2.getJSONArray("Head");
					Gson gson = new Gson();
					list.clear();
					for (int i = 0; i < array.length(); i++) {
						ClassBoard obj = gson.fromJson(array.getString(i),
								ClassBoard.class);
						Map<String, Object> map = new HashMap<String, Object>();
						map.put(LIST_TEXT, obj.getBordName());
						map.put(LIST_URL, obj.getBoardID());
						list.add(map);
					}
					lvAdapter.notifyDataSetChanged();

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
				Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG)
						.show();
			}
		};
	}

	// [end]网络请求错误

	// [start]连续两次返回就退出
	private int keyBackClickCount = 0;

	@Override
	protected void onResume() {
		super.onResume();
		keyBackClickCount = 0;
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
		mListView.stopRefresh();
		ToastUtil.show(mContext, "Refresh");
	}

	@Override
	public void onLoadMore() {
		page++;
		getList(url, page);
	}
	// [start]XList监听
}
