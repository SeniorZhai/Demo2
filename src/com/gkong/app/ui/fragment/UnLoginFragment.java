package com.gkong.app.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gkong.app.MyApplication;
import com.gkong.app.R;
import com.gkong.app.config.Api;
import com.gkong.app.data.RequestManager;
import com.gkong.app.model.BBSRe;
import com.gkong.app.model.BBSTopic;
import com.gkong.app.model.UserInfo;
import com.gkong.app.ui.DetailsActivity;
import com.gkong.app.ui.UserLoginUidActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class UnLoginFragment extends Fragment {
	private Context mContext;

	private MyApplication application;
	private SharedPreferences share;

	public static String SharedName = "login";
	public static String UID = "uid";// 用户名
	public static String PWD = "pwd";// 密码

	private DisplayImageOptions options;
	private TextView score, repay, topic;
	private ViewPager viewPager;
	private PagerTabStrip pagerTabStrip;
    private String[] Titles = {"我的回帖","我的发帖"};  
	private ListView mListView1, mListView2;
	private BaseAdapter adpAdapter1,adpAdapter2;
	private UserInfo userInfo;

	private List<BBSRe> bbsReList;
	private List<BBSTopic> bbsList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		application = (MyApplication) getActivity().getApplication();
		share = mContext.getSharedPreferences(SharedName, Context.MODE_PRIVATE);
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.avatar_default)
				.showImageForEmptyUri(R.drawable.avatar_default)
				.showImageOnFail(R.drawable.avatar_default).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();
		bbsReList = new ArrayList<BBSRe>();
		bbsList = new ArrayList<BBSTopic>();
		View view = inflater.inflate(R.layout.fragment_user_unlogin, container,
				false);
		initView(view);
		netWork();
		return view;
	}

	private void netWork() {
		executeRequest(new StringRequest(Method.GET,
				Api.UserInfo(application.loginInfo.getData()),
				infoResponseListener(), errorListener()));
		// 我的发帖
		executeRequest(new StringRequest(Method.GET, Api.MyBBS(
				application.loginInfo.getData(), 1), BBSResponseListener(),
				errorListener()));
		executeRequest(new StringRequest(Method.GET,
				Api.MyBBSRe(application.loginInfo.getData()),
				BBSReResponseListener(), errorListener()));
	}

	private void initView(View view) {
		pagerTabStrip = (PagerTabStrip) view.findViewById(R.id.user_pagertab);
		pagerTabStrip.setTabIndicatorColor(Color.BLACK);
		pagerTabStrip.setTextColor(Color.BLACK);
		viewPager = (ViewPager) view.findViewById(R.id.user_pager);
		score = (TextView) view.findViewById(R.id.user_score);
		repay = (TextView) view.findViewById(R.id.user_repay);
		topic = (TextView) view.findViewById(R.id.user_topic);
		LayoutInflater inflater = getActivity().getLayoutInflater();
		mListView1 = (ListView) inflater.inflate(
				R.layout.fragment_user_unlogin_list, null);
		mListView2 = (ListView) inflater.inflate(
				R.layout.fragment_user_unlogin_list, null);
		ArrayList<ListView> mList = new ArrayList<ListView>();
		adpAdapter1 = new BBSReAdapter();
		mListView1.setAdapter(adpAdapter1);
		adpAdapter2 = new BBSAdapter();
		mListView2.setAdapter(adpAdapter2);
		mList.add(mListView2);
		mList.add(mListView1);
		viewPager.setAdapter(new MyViewPagerAdapter(mList));

		mListView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mContext, DetailsActivity.class);
				intent.putExtra("url", bbsList.get(position).getTopicID());
				startActivity(intent);
			}
		});
		mListView2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(mContext, DetailsActivity.class);
				intent.putExtra("url", bbsReList.get(position).getTopicID());
				startActivity(intent);
			}
		});
		
		TextView id = (TextView) view.findViewById(R.id.user_id);
		id.setText("ID:"+application.loginInfo.getId());
		TextView name = (TextView) view.findViewById(R.id.user_name);
		name.setText(application.loginInfo.getName());
		ImageView avatar = (ImageView) view.findViewById(R.id.user_avatar);
		ImageLoader.getInstance().displayImage(
				Api.Avatar(application.loginInfo.getId()), avatar, options);
		Button unLogin = (Button) view.findViewById(R.id.user_unlogin);
		unLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				application.loginInfo = null;
				((UserLoginUidActivity) mContext).setLogin();
			}
		});

	}

	private Response.Listener<String> infoResponseListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				userInfo = UserInfo.getUserInfo(response);
				score.setText("积分\n" + userInfo.getUserScore());
				repay.setText("回帖\n" + userInfo.getUserBBSRepayCount());
				topic.setText("发帖\n" + userInfo.getUserBBSTopicCount());
			}
		};
	}

	private Response.Listener<String> BBSResponseListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				bbsList.addAll(BBSTopic.getList(response));
				adpAdapter2.notifyDataSetChanged();
			}
		};
	}

	private Response.Listener<String> BBSReResponseListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				bbsReList = BBSRe.getList(response);
				adpAdapter1.notifyDataSetChanged();
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
			}
		};
	}
	private class BBSAdapter extends BaseAdapter {

		private Context context;

		public BBSAdapter() {
			this.context = getActivity();
		}

		@Override
		public int getCount() {
			return bbsReList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.above_list_item, null);
			}
			TextView titleView = (TextView) convertView
					.findViewById(R.id.item_title);
			TextView timeView = (TextView) convertView
					.findViewById(R.id.item_time);
			TextView childView = (TextView) convertView
					.findViewById(R.id.item_child);
			TextView hitsView = (TextView) convertView
					.findViewById(R.id.item_hits);
			BBSRe obj = bbsReList.get(position);
			titleView.setText(obj.getTitle());
			timeView.setText(obj.getDateAndTime());
			childView.setText("回复:" + obj.getChild());
			hitsView.setText("人气:" + obj.getHits());
			return convertView;
		}
	}
	private class BBSReAdapter extends BaseAdapter {

		private Context context;

		public BBSReAdapter() {
			this.context = getActivity();
		}

		@Override
		public int getCount() {
			return bbsList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.above_list_item, null);
			}
			TextView titleView = (TextView) convertView
					.findViewById(R.id.item_title);
			TextView timeView = (TextView) convertView
					.findViewById(R.id.item_time);
			TextView childView = (TextView) convertView
					.findViewById(R.id.item_child);
			TextView hitsView = (TextView) convertView
					.findViewById(R.id.item_hits);
			BBSTopic obj = bbsList.get(position);
			titleView.setText(obj.getTitle());
			timeView.setText(obj.getDateAndTime());
			childView.setText("回复:" + obj.getChild());
			hitsView.setText("人气:" + obj.getHits());
			return convertView;
		}
	}

	private class MyViewPagerAdapter extends PagerAdapter {
		private List<ListView> mListView;

		@Override
		public CharSequence getPageTitle(int position) {
			return Titles[position];
		}
	
		public MyViewPagerAdapter(List<ListView> mListView) {
			this.mListView = mListView;// 构造方法，参数是我们的页卡，这样比较方便。
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListView.get(position));// 删除页卡
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
			container.addView(mListView.get(position), 0);// 添加页卡
			return mListView.get(position);
		}

		@Override
		public int getCount() {
			return mListView.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
}
