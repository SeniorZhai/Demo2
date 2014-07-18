package com.gkong.app.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.gkong.app.data.ApiParams;
import com.gkong.app.data.Bimp;
import com.gkong.app.data.RequestManager;
import com.gkong.app.model.NewTopicInfo;
import com.gkong.app.model.UploadInfo;
import com.gkong.app.service.UploadService;
import com.gkong.app.utils.FileUtils;
import com.gkong.app.utils.ToastUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class PublishedActivity extends Activity {
	private MyApplication mApplication;
	private Activity mActivity;

	private EditText title, content;
	private String boardID;
	private ProgressDialog dialog;

	private GridView gridview;
	private GridAdapter adapter;
	private TextView activity_selectimg_send;
	private ImageView goHome;
	private StringBuffer buffer;
	private Gson gson;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selecting);
		gson = new Gson();
		buffer = new StringBuffer();
		mApplication = (MyApplication) getApplication();
		mActivity = this;
		boardID = getIntent().getStringExtra("BoardID");
		init();
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	public void init() {
		dialog = new ProgressDialog(this);

		title = (EditText) findViewById(R.id.newtopic_title);
		content = (EditText) findViewById(R.id.newtopic_content);
		goHome = (ImageView) findViewById(R.id.select_gohome);
		goHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		gridview = (GridView) findViewById(R.id.noScrollgridview);
		gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (m.isActive()) {
					m.hideSoftInputFromWindow(PublishedActivity.this
							.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
				if (position == Bimp.bmp.size()) {
					new PopupWindows(mActivity, gridview).show();
				} else {
					Intent intent = new Intent(PublishedActivity.this,
							PhotoActivity.class);
					intent.putExtra("ID", position);
					startActivity(intent);
				}

			}
		});
		activity_selectimg_send = (TextView) findViewById(R.id.activity_selectimg_send);
		activity_selectimg_send.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (mApplication.loginInfo == null) {
					ToastUtil.show(mActivity, "请登入");
					Intent intent = new Intent(mActivity,
							UserLoginUidActivity.class);
					startActivity(intent);
				} else if (title.getText().length() <= 5) {
					ToastUtil.show(mActivity, "标题过短");
				} else if (content.getText().length() <= 10) {
					ToastUtil.show(mActivity, "内容过短");
				} else {
					dialog.show();
					buffer.append(content.getText());
					MyTask task = new MyTask();
					task.execute();
				}
			}
		});
	}

	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		@Override
		public int getCount() {
			return Bimp.bmp.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position == Bimp.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}
			return convertView;
		}

		private class ViewHolder {
			public ImageView image;
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				adapter.notifyDataSetChanged();
				break;
			}
			super.handleMessage(msg);
		}
	};

	public void loading() {
		new Thread(new Runnable() {
			public void run() {
				while (true) {
					if (Bimp.max == Bimp.drr.size()) {
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						break;
					} else {
						try {
							String path = Bimp.drr.get(Bimp.max);
							Bitmap bm = Bimp.revitionImageSize(path);
							Bimp.bmp.add(bm);
							String newStr = path.substring(
									path.lastIndexOf("/") + 1,
									path.lastIndexOf("."));
							FileUtils.saveBitmap(bm, "" + newStr);
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}

	public class PopupWindows {
		private PopupWindow window;
		private View parent;

		public PopupWindows(Context mContext, View parent) {
			this.parent = parent;
			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in));
			window = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					window.dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(PublishedActivity.this,
							BucketActivity.class);
					startActivity(intent);
					window.dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					window.dismiss();
				}
			});
		}

		private void show() {
			window.setFocusable(true);
			window.setOutsideTouchable(true);
			window.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			window.update();
		}
	}

	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";

	public void photo() {
		// 从现有的相机应用中请求一张图片
		Intent intent = new Intent();
		// 指定开启系统相机的Action
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.addCategory(Intent.CATEGORY_DEFAULT);

		File file = new File(FileUtils.SDPATH
				+ String.valueOf(System.currentTimeMillis()) + ".jpg");
		path = file.getPath();
		Uri imageUri = Uri.fromFile(file);
		Log.d("---", path);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, TAKE_PICTURE);

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.drr.size() < 9 && resultCode == -1) {
				Bimp.drr.add(path);
			}
			break;
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
					intent.putExtra("url", info.getTopicId() + "");
					startActivity(intent);
					ToastUtil.show(mActivity, "发送成功");
					mActivity.finish();
				} else {
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
				Toast.makeText(mActivity, error.getMessage(), Toast.LENGTH_LONG)
						.show();
			}
		};
	}

	// [end]网络请求
	@Override
	protected void onDestroy() {
		FileUtils.deleteDir();
		super.onDestroy();
	}

	class MyTask extends AsyncTask<Void, Void, Void> {
		private int flag = 0;

		@Override
		protected Void doInBackground(Void... params) {
			List<String> list = new ArrayList<String>();
			for (int i = 0; i < Bimp.drr.size(); i++) {
				String Str = Bimp.drr.get(i).substring(
						Bimp.drr.get(i).lastIndexOf("/") + 1,
						Bimp.drr.get(i).lastIndexOf("."));
				list.add(FileUtils.SDPATH + Str + ".JPEG");
			}
			if (list.size() > 0) {
				post(Api.Upload + mApplication.loginInfo.getData(), list);
			} else {
				published();
			}
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
					client.post(uploadUrl, params,
							new AsyncHttpResponseHandler() {
								@Override
								public void onSuccess(int statusCode,
										Header[] headers, byte[] responseBody) {
									flag++;
									Log.d("---", "StatusCode:" + statusCode
											+ "\t" + new String(responseBody));
									UploadInfo info = gson.fromJson(new String(
											responseBody), UploadInfo.class);
									buffer.append(info.getResult());
									if (flag == paths.size()) {
										Log.d("---", "全部上传");
										published();
									}
								}

								@Override
								public void onFailure(int statusCode,
										Header[] headers, byte[] responseBody,
										Throwable error) {
									flag++;
									if (responseBody != null) {
										Log.d("---", "StatusCode:" + statusCode
												+ "\n"
												+ new String(responseBody));
									}
									Log.d("---", "StatusCode:" + statusCode
											+ "\n");
								}
							});
				} else {
					ToastUtil.show(PublishedActivity.this, "文件错误");
				}
			}

		}

		private void published() {
			executeRequest(new StringRequest(Method.POST, Api.NewTopic,
					responseListener(), errorListener()) {
				@Override
				protected Map<String, String> getParams() {
					JSONObject json = new JSONObject();
					try {
						json.put("UID", mApplication.loginInfo.getData());
						json.put("Title", title.getText().toString());
						json.put("Body", buffer.toString());
						Log.d("---", buffer.toString());
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
}