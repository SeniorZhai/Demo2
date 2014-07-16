package com.gkong.app.ui;

import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.gkong.app.model.Archive;
import com.gkong.app.model.PageInfo;
import com.gkong.app.model.RepayInfo;
import com.gkong.app.model.TopicInfo;
import com.gkong.app.ui.base.BaseActivity;
import com.gkong.app.utils.ToastUtil;
import com.google.gson.Gson;

@SuppressLint("SetJavaScriptEnabled")
public class DetailsActivity extends BaseActivity implements OnClickListener {
	// Context
	private MyApplication mApplication;
	private Activity mActivity;
	// Value
	private int currentPage = 1, pageCout;
	private String url;
	private String detailId;
	private int myPosition = 0;
	// View
	private ImageView imgGoHome;
	private EditText editText;
	private ListView mlistView;
	private View mFooter;
	private TextView title;
	private Button lastBn, nextBn, repayBn;
	private TextView pageInfoView;
	// Adpter
	private DetailsArrayAdapter mAdapter;
	// Data
	private PageInfo pageInfo;
	private TopicInfo topicInfo;
	private ArrayList<Archive> mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_activity);
		mApplication = (MyApplication) getApplication();
		mActivity = this;
		mList = new ArrayList<Archive>();
		initControl();
		netWork();
	}

	private void initControl() {

		mlistView = (ListView) findViewById(R.id.details_listview_show);
		title = (TextView) findViewById(R.id.details_textview_title);

		mAdapter = new DetailsArrayAdapter(mActivity, mList);
		mFooter = getLayoutInflater().inflate(R.layout.details_footer, null);
		mlistView.addFooterView(mFooter);
		mlistView.setAdapter(mAdapter);
		editText = (EditText) findViewById(R.id.details_edittext_discuss);
		lastBn = (Button) mFooter.findViewById(R.id.lastPage);
		lastBn.setOnClickListener(this);
		nextBn = (Button) mFooter.findViewById(R.id.nextPage);
		nextBn.setOnClickListener(this);
		repayBn = (Button) findViewById(R.id.details_button_enter);
		repayBn.setOnClickListener(this);
		pageInfoView = (TextView) mFooter.findViewById(R.id.pageInfo);
		imgGoHome = (ImageView) findViewById(R.id.details_imageview_gohome);
		imgGoHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void netWork() {
		detailId = getIntent().getStringExtra("url");
		Log.d("---", detailId);
		url = Api.Archive(detailId, currentPage);
		Log.d("---", url);
		executeRequest(new StringRequest(Method.GET, url, responseListener(),
				errorListener()));
	}

	// [start]网络请求反馈
	private Response.Listener<String> responseListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				try {
					JSONObject obj1 = new JSONObject(response);
					String str = obj1.getString("d");
					JSONObject obj2 = new JSONObject(str);
					Gson gson = new Gson();
					pageInfo = gson.fromJson(obj2.getString("PageInfo"),
							PageInfo.class);
					topicInfo = gson.fromJson(obj2.getString("TopicInfo"),
							TopicInfo.class);
					JSONArray ob3 = obj2.getJSONObject("BbsList").getJSONArray(
							"Data");
					mList.clear();
					for (int i = 0; i < ob3.length(); i++) {
						Archive archive = gson.fromJson(ob3.getString(i),
								Archive.class);
						mList.add(archive);
					}

					currentPage = Integer.parseInt(pageInfo.getCurrentPage());
					pageCout = Integer.parseInt(pageInfo.getPageCount());
					pageInfoView.setText(currentPage + "/" + pageCout);
					title.setText(topicInfo.getTitle());
					mAdapter.notifyDataSetChanged();
					mlistView.setSelection(0);
					mFooter.setVisibility(View.VISIBLE);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
	}

	private Response.Listener<String> repayResponseListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new Gson();
				RepayInfo info = gson.fromJson(response, RepayInfo.class);
				currentPage = pageCout;
				if (info.isIsSuccess()) {
					String url = Api.Archive(detailId, currentPage);
					executeRequest(new StringRequest(Method.GET, url,
							responseListener(), errorListener()));
					editText.setText("");
				} else {
					Log.d("DetailsActivity", info.getMessage());
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
				ToastUtil.show(mActivity, "网络错误");
			}
		};
	}

	// [end]网络请求
	protected void executeRequest(Request<?> request) {
		RequestManager.addRequest(request, this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lastPage:
			if (currentPage > 1) {
				url = Api.Archive(detailId, --currentPage);
				executeRequest(new StringRequest(Method.GET, url,
						responseListener(), errorListener()));
			} else {
				ToastUtil.show(mActivity, "没有上一页");
			}
			break;
		case R.id.nextPage:
			if (currentPage < pageCout) {
				String url = Api.Archive(detailId, ++currentPage);
				executeRequest(new StringRequest(Method.GET, url,
						responseListener(), errorListener()));
			} else {
				ToastUtil.show(mActivity, "没有下一页");
			}
			break;
		case R.id.details_button_enter:
			if (mApplication.loginInfo == null) {
				ToastUtil.show(mActivity, "请登入");
				Intent intent = new Intent(mActivity,
						UserLoginUidActivity.class);
				startActivity(intent);
			} else if (editText.getText().length() > 0) {
				executeRequest(new StringRequest(Method.POST, Api.RepayTopic,
						repayResponseListener(), errorListener()) {
					@Override
					protected Map<String, String> getParams() {
						JSONObject json = new JSONObject();
						try {
							json.put("UID", mApplication.loginInfo.getData());
							json.put("AnnounceId", mList.get(myPosition).getAnnounceID());
							json.put("Body", editText.getText().toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						String content = String.valueOf(json);
						return new ApiParams().with("d", content);
					}
				});
			}
			break;
		}
	}

	class DetailsArrayAdapter extends BaseAdapter {
		private ArrayList<Archive> list;
		private Context context;

		public DetailsArrayAdapter(Context context, ArrayList<Archive> list) {
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
			final int myPosition = position;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.details_list_item, null);
			} else if (convertView.getHeight() > 200) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.details_list_item, null);
			}
			WebView webView = (WebView) convertView
					.findViewById(R.id.item_webView);
			TextView userName = (TextView) convertView
					.findViewById(R.id.item_username);
			TextView time = (TextView) convertView.findViewById(R.id.item_date);
			TextView reply = (TextView) convertView
					.findViewById(R.id.item_reply);

			Archive obj = list.get(position);
			userName.setText(obj.getUserName());
			time.setText(obj.getDateAndTime());
			reply.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					DetailsActivity.this.myPosition = myPosition;
					if (myPosition == 0)
						ToastUtil.show(context, "回复楼主");
					else
						ToastUtil.show(context, "回复"+myPosition+"楼");
					editText.requestFocus();  
					InputMethodManager imm = (InputMethodManager) context
							  .getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
			});
			webView.getSettings().setJavaScriptEnabled(true);
			webView.addJavascriptInterface(new JavascriptInterface(context),
					"imagelistner");
			webView.loadDataWithBaseURL(
					null,
					obj.getBody()
							+ "    <script type=\"text/javascript\">"
							+ "var objs = document.getElementsByTagName('a');"
							+ "for (var i = 0; i < objs.length; i++) {"
							+ "    objs[i].onclick =  function(){ return url_onclick(this,{});};"
							+ "}"
							+ "function url_onclick(sender, e) {"
							+ "    var rx =  /^http:\\/\\/m\\.gkong\\.com\\/bbs\\/(\\d+)\\./i;"
							+ "     var mc = rx.exec(sender.href);"
							+ "    window.imagelistner.otherDetils( mc[1] );\n return false;"
							+ "}"

							+ "function openImageUrl(url) {\n"
							+ "    window.imagelistner.openImage(url); }\n"
							+ "</script>", "text/html", "UTF-8", null);

			return convertView;
		}

		class JavascriptInterface {

			private Context context;

			public JavascriptInterface(Context context) {
				this.context = context;
			}

			public void openImage(String img) {
				Intent intent = new Intent(context, ShowWebImageActivity.class);
				intent.putExtra("image", img);
				context.startActivity(intent);
			}

			public void otherDetils(String id) {
				Intent intent = new Intent(context, DetailsActivity.class);
				intent.putExtra("url", id);
				context.startActivity(intent);
				finish();
			}
		}
	}
}