package com.gkong.app.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gkong.app.R;
import com.gkong.app.config.Api;
import com.gkong.app.model.Search;
import com.gkong.app.ui.base.BaseActivity;
import com.gkong.app.utils.ToastUtil;
import com.gkong.app.widget.XListView;
import com.gkong.app.widget.XListView.IXListViewListener;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class SearchActivity extends BaseActivity implements OnClickListener,
		IXListViewListener {
	private ImageView imgGoHome;
	private ImageView query;
	private EditText editText;
	private XListView mListView;
	private LinearLayout aboveLoadLayout;
	private List<Search> list;
	private MyArrayAdapter listAdapter;
	private int page = 1;
	private String url = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list = new ArrayList<Search>();
		setContentView(R.layout.activity_search);
		aboveLoadLayout = (LinearLayout) findViewById(R.id.view_loading);
		mListView = (XListView) findViewById(R.id.search_list_view);
		editText = (EditText) findViewById(R.id.search_edit);
		query = (ImageView) findViewById(R.id.search_imageview_query);
		imgGoHome = (ImageView) findViewById(R.id.search_imageview_gohome);
		query.setOnClickListener(this);
		imgGoHome.setOnClickListener(this);
		listAdapter = new MyArrayAdapter(this, list);
		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(false);
		mListView.setAdapter(listAdapter);
		mListView.setXListViewListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(SearchActivity.this, DetailsActivity.class);
				intent.putExtra("url", list.get(position - 1).getTopicID()+"");
				startActivity(intent);
			}
		});
	}

	@Override
	public void onRefresh() {
		// page = 1;
	}

	@Override
	public void onLoadMore() {
		page++;
		getSeach(Api.Search(url, page));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_imageview_gohome:
			finish();
			break;
		case R.id.search_imageview_query:
			if (TextUtils.isEmpty(editText.getText())) {
				ToastUtil.show(this, "关键词不能为空");
			} else {
				aboveLoadLayout.setVisibility(View.VISIBLE);
				page = 1;
				url = editText.getText().toString();
				getSeach(Api.Search(url, page));
			}
			break;

		default:
			break;
		}

	}

	private void getSeach(String url) {
		
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				super.onSuccess(statusCode, headers, responseBody);
				String result = new String(responseBody);
				try {
					JSONObject obj1 = new JSONObject(result);
					String str = obj1.getString("d");
					JSONObject obj2 = new JSONObject(str);
					JSONArray array = obj2.getJSONArray("Head");
					Gson gson = new Gson();
					if (page == 1) {
						list.clear();
						SimpleDateFormat sDateFormat = new SimpleDateFormat(
								"MM-dd hh:mm:ss");
						String date = sDateFormat.format(new java.util.Date());
						mListView.setRefreshTime(date);
					}
					for (int i = 0; i < array.length(); i++) {
						Search obj = gson.fromJson(array.getString(i),
								Search.class);
						list.add(obj);
					}
					listAdapter.notifyDataSetChanged();
					mListView.stopRefresh();
					mListView.stopLoadMore();
					aboveLoadLayout.setVisibility(View.GONE);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				super.onFailure(statusCode, headers, responseBody, error);
				Log.i("SearchActivity", error.getMessage());
				aboveLoadLayout.setVisibility(View.GONE);
			}
		});
	}
}

class MyArrayAdapter extends BaseAdapter {

	private List<Search> list;
	private Context context;

	public MyArrayAdapter(Context context, List<Search> list) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
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
		TextView timeView = (TextView) convertView.findViewById(R.id.item_time);
		Search obj = list.get(position);
		titleView.setText(obj.getTitle());
		timeView.setText(obj.getDateAndTime());
		return convertView;
	}

}
