package com.gkong.app;

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
import com.gkong.app.data.BoardDao;
import com.gkong.app.data.RequestManager;
import com.gkong.app.model.ClassBoardHandle;
import com.gkong.app.model.ClassBoardHandle.GroupItem;
import com.gkong.app.model.ClassBoardSrc;
import com.gkong.app.model.ClassBoardSrc.Item;
import com.gkong.app.model.LoginInfo;
import com.gkong.app.ui.UserLoginUidActivity;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.update.net.f;

public class MyApplication extends Application {
	public static LoginInfo loginInfo = null;
	private SharedPreferences share;
	public List<Item> dataList;
	public List<GroupItem> dataGroupList;
	public List<Item> subscriboList;
	public BoardDao dao;

	@Override
	public void onCreate() {
		super.onCreate();
		RequestManager.init(this);
		dao = new BoardDao(this);
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
		dataList = dao.listCache("_id " + " >=? ", new String[] { "0" });
		if (dataList.size() <= 0) {
			// 从网络获取数据
			executeRequest(new StringRequest(Method.GET, Api.Board,
					ClassResponseListener(), errorListener()));
		}else {
			subscriboList = new ArrayList<Item>();
			dataGroupList = new ArrayList<GroupItem>();
			for(Item item : dataList){
				if (item.getBoardID()==0) {
					Log.d("---", "---");
					GroupItem grop = new GroupItem(item);
					dataGroupList.add(grop);
				}
				if (item.isSelect()) {
					subscriboList.add(item);
				}
			}
			int index=0,jx = 0;
			for(Item item : dataList){
				index++;
				if (item.getBoardID() != 0) {
					jx ++;
				for (GroupItem grop : dataGroupList) {
					
					if (grop.getSID()/1000000 == item.getSID()/1000000) {
						grop.items.add(item);
					}
				}
				}
			}
			Log.d("---", index+"\t"+jx);
		}
		initImageLoader(getApplicationContext());
	}

	@Override
	public void onTerminate() {
		if (dao == null) {
			dao.helper.close();
		}
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
				dao.initCache(dataList);
				subscriboList = new ArrayList<Item>();
				for (int i = 0; i < dataGroupList.size();i++)
				{
					for (Item item : dataGroupList.get(i).getItems()) {
						subscriboList.add(item);
					}
				}
			}
		};
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
