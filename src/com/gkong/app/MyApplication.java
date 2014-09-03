package com.gkong.app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.gkong.app.config.Api;
import com.gkong.app.data.ApiParams;
import com.gkong.app.data.RequestManager;
import com.gkong.app.model.ClassBoardHandle;
import com.gkong.app.model.ClassBoardHandle.GroupItem;
import com.gkong.app.model.ClassBoardSrc;
import com.gkong.app.model.ClassBoardSrc.Item;
import com.gkong.app.model.LoginInfo;
import com.gkong.app.ui.UserLoginUidActivity;
import com.gkong.app.utils.CacheUtils;
import com.gkong.app.utils.SerializeUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application {
	public static LoginInfo loginInfo = null;
	private SharedPreferences share;
	private List<Item> dataList;
	public List<GroupItem> dataGroupList;
	public List<Item> subscriboList;

	public File cache;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate() {
		super.onCreate();
		RequestManager.init(this);
		// 用户登入
		share = getSharedPreferences(UserLoginUidActivity.SharedName,
				Context.MODE_PRIVATE);
		if (share.contains(UserLoginUidActivity.UID)) {
			executeRequest(new StringRequest(Method.POST, Api.Login,
					responseListener(), errorListener()) {
				protected Map<String, String> getParams() {
					return new ApiParams().with("username",
							share.getString(UserLoginUidActivity.UID, ""))
							.with("password",
									share.getString(UserLoginUidActivity.PWD,
											""));
				}
			});
		}

		File cacheDir = CacheUtils.getCacheDirectory(getApplicationContext(),
				true, "gkong");
		cache = new File(cacheDir, "DataList");
		if (!cache.exists()) {
			Log.d("---", cache.getAbsolutePath());
			try {
				cache.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 从文件中获取
		if (cache.length() > 0) {
			getList();
		}

		if (dataGroupList == null) {
			// 从网络获取数据
			executeRequest(new StringRequest(Method.GET, Api.Board,
					ClassResponseListener(), errorListener()));
		} else {
			subscriboList = new ArrayList<ClassBoardSrc.Item>();
			for (int i = 0; i < dataGroupList.size(); i++) {
				for (Item item : dataGroupList.get(i).getItems()) {
					if (item.isSelect()) {
						subscriboList.add(item);
					}
				}
			}
		}
		initImageLoader(getApplicationContext());
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

	private void executeRequest(Request<?> request) {
		RequestManager.addRequest(request, this);
	}

	private Response.Listener<String> ClassResponseListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				dataList = ClassBoardSrc.getBoard(response).getHead();
				dataGroupList = ClassBoardHandle.getList(dataList);
				subscriboList = new ArrayList<Item>();
				for (int i = 0; i < dataGroupList.size(); i++) {
					for (Item item : dataGroupList.get(i).getItems()) {
						subscriboList.add(item);
					}
				}
				saveList();
			}
		};
	}

	public void saveList() {
		try {
			cache.delete();
			cache.createNewFile();
			SerializeUtils.serialization(cache.getAbsolutePath(), dataGroupList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void getList() {
		try {
			dataGroupList = (ArrayList<GroupItem>) SerializeUtils
					.deserialization(cache.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Response.Listener<String> responseListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Gson gson = new Gson();
				LoginInfo json = gson.fromJson(response, LoginInfo.class);
				if (json.isSuccess()) {
					MyApplication.loginInfo = json;
				} else {
					MyApplication.loginInfo = null;
				}
			}
		};
	}

	protected Response.ErrorListener errorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(MyApplication.this, "网络错误", Toast.LENGTH_LONG)
						.show();
			}
		};
	}

}
