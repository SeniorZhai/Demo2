package com.gkong.app.ui;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.gkong.app.model.NewTopicInfo;
import com.gkong.app.utils.ToastUtil;
import com.google.gson.Gson;

public class NewTopicActivity extends Activity implements OnClickListener {
	// Context
	private MyApplication mApplication;
	private Activity mActivity;

	private ProgressDialog dialog;
	private ImageView goHome;
	private Button newTopicEnter;
	private EditText title, content;
	private String boardID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_topic);
		mApplication = (MyApplication) getApplication();
		mActivity = this;
		initControl();
		boardID = getIntent().getStringExtra("BoardID");
	}

	private void initControl() {
		dialog = new ProgressDialog(this);
		goHome = (ImageView) findViewById(R.id.new_topic_gohome);
		goHome.setOnClickListener(this);
		newTopicEnter = (Button) findViewById(R.id.new_topic_enter);
		newTopicEnter.setOnClickListener(this);
		title = (EditText) findViewById(R.id.newtopic_title);
		content = (EditText) findViewById(R.id.newtopic_content);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.new_topic_gohome:
			finish();
			break;
		case R.id.new_topic_enter:
			enter();
			break;
		default:
			break;
		}
	}

	private void enter() {
		if (mApplication.loginInfo == null) {
			ToastUtil.show(mActivity, "请登入");
			Intent intent = new Intent(mActivity, UserLoginUidActivity.class);
			startActivity(intent);
			 } else if (title.getText().length() <= 5) {
			 ToastUtil.show(mActivity, "标题过短");
			 } else if (content.getText().length() <= 10) {
			 ToastUtil.show(mActivity, "内容过短");
		} else {
			dialog.show();
			executeRequest(new StringRequest(Method.POST, Api.NewTopic,
					responseListener(), errorListener()) {
				@Override
				protected Map<String, String> getParams() {
					JSONObject json = new JSONObject();
					try {
						json.put("UID", mApplication.loginInfo.getData());
						json.put("Title", title.getText().toString());
						json.put("Body", content.getText().toString());
						json.put("BoardId", boardID);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					String body = String.valueOf(json);
					return new ApiParams().with("d", body);
				}
			});
		}
	}

	// [start]网络请求
	protected void executeRequest(Request<?> request) {
		RequestManager.addRequest(request, this);
	}

	private Response.Listener<String> responseListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new Gson();
				dialog.cancel();
				NewTopicInfo info = gson.fromJson(response, NewTopicInfo.class);
				if (info.isIsSuccess()) {
					Intent intent = new Intent(mActivity, DetailsActivity.class);
					intent.putExtra("url", info.getTopicId()+"");
					startActivity(intent);
					ToastUtil.show(mActivity, "发送成功");
					mActivity.finish();
				}else {
					ToastUtil.show(mActivity, info.getMessage());
				}
				
			}
		};
	}

	// [start]网络请求错误
	protected Response.ErrorListener errorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(NewTopicActivity.this, error.getMessage(),
						Toast.LENGTH_LONG).show();
			}
		};
	}
	// [end]网络请求

}
