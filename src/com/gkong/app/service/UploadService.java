package com.gkong.app.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gkong.app.config.Api;
import com.gkong.app.data.ApiParams;
import com.gkong.app.data.Bimp;
import com.gkong.app.data.RequestManager;
import com.gkong.app.model.NewTopicInfo;
import com.gkong.app.model.UploadInfo;
import com.gkong.app.ui.DetailsActivity;
import com.gkong.app.utils.FileUtils;
import com.gkong.app.utils.ToastUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UploadService extends Service {
	public static final String CONTENT = "content", TITLE = "title",
			UID = "UID", BOARDID = "BoardId";
	private String contentStr;
	private String titleStr;
	private String UIDStr;
	private String BoardIdStr;
	private StringBuffer buffer;
	private Gson gson;

	private int flag = 0;

	@Override
	public void onCreate() {
		buffer = new StringBuffer();
		gson = new Gson();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		UIDStr = intent.getStringExtra(UID);
		BoardIdStr = intent.getStringExtra(BOARDID);
		titleStr = intent.getStringExtra(TITLE);
		contentStr = intent.getStringExtra(CONTENT);
		Log.d("---", UIDStr);
		buffer.append(contentStr);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < Bimp.drr.size(); i++) {
			String Str = Bimp.drr.get(i).substring(
					Bimp.drr.get(i).lastIndexOf("/") + 1,
					Bimp.drr.get(i).lastIndexOf("."));
			list.add(FileUtils.SDPATH + Str + ".JPEG");
		}
		if(list.size() >0){
			post(Api.Upload + UIDStr, list);
		}else{
			published();
		}
		
		
		// 高清的压缩图片全部就在 list 路径里面了
		// 高清的压缩过的 bmp 对象 都在 Bimp.bmp里面
		// 完成上传服务器后 .........
		// FileUtils.deleteDir();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void post(String uploadUrl, final List<String> paths) {
		for (String path : paths) {
			File file = new File(path);
			if (file.exists() && file.length() > 0) {
				AsyncHttpClient client = new AsyncHttpClient();
				RequestParams params = new RequestParams();
				try {
					params.put("profile_picture", file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				client.post(uploadUrl, params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
							byte[] responseBody) {
						flag++;
						Log.d("---", "StatusCode:" + statusCode + "\n"
								+ new String(responseBody));
						UploadInfo info = gson.fromJson(
								new String(responseBody), UploadInfo.class);
						buffer.append(info.getResult());
						if (flag == paths.size()) {
							Log.d("---","全部上传");
							published();
						}
					}

					@Override
					public void onFailure(int statusCode, Header[] headers,
							byte[] responseBody, Throwable error) {
						flag++;
						if (responseBody != null) {
							Log.d("---", "StatusCode:" + statusCode + "\n"
									+ new String(responseBody));
						}
						Log.d("---", "StatusCode:" + statusCode + "\n");
					}
				});
			} else {
				ToastUtil.show(this, "文件错误");
			}
		}

	}
	private void published(){
		executeRequest(new StringRequest(Method.POST,
				Api.NewTopic, responseListener(),
				errorListener()) {
			@Override
			protected Map<String, String> getParams() {
				JSONObject json = new JSONObject();
				try {
					json.put("UID", UIDStr);
					json.put("Title", titleStr);
					json.put("Body", buffer.toString());
					json.put("BoardId", BoardIdStr);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String body = String.valueOf(json);
				return new ApiParams().with("d", body);
			}
		});
		FileUtils.deleteDir();
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
				NewTopicInfo info = gson.fromJson(response, NewTopicInfo.class);
				if (info.isIsSuccess()) {
					Intent intent = new Intent(UploadService.this, DetailsActivity.class);
					intent.putExtra("url", info.getTopicId()+"");
					startActivity(intent);
					ToastUtil.show(UploadService.this, "发送成功");
				}else {
					ToastUtil.show(UploadService.this, info.getMessage());
				}
			}
		};
	}

	// [start]网络请求错误
	protected Response.ErrorListener errorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
			}
		};
	}
	// [end]网络请求
}