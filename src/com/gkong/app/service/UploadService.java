package com.gkong.app.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.gkong.app.config.Api;
import com.gkong.app.data.Bimp;
import com.gkong.app.utils.FileUtils;

public class UploadService extends Service {
	public static final String CONTENT = "content", TITLE = "title",
			UID = "UID", BOARDID = "BoardId";
	private String contentStr;
	private String titleStr;
	private String UIDStr;
	private String BoardIdStr;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		UIDStr = intent.getStringExtra(UID);
//		 BoardIdStr = intent.getStringExtra(BOARDID);
//		 titleStr = intent.getStringExtra(TITLE);
//		 contentStr = intent.getStringExtra(CONTENT);
//		 ToastUtil.show(this, UIDStr + BoardIdStr + contentStr + titleStr);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < Bimp.drr.size(); i++) {
			String Str = Bimp.drr.get(i).substring(
					Bimp.drr.get(i).lastIndexOf("/") + 1,
					Bimp.drr.get(i).lastIndexOf("."));
			list.add(FileUtils.SDPATH + Str + ".JPEG");
		}

		for (String value : list) {
			uploadFile(Api.Upload + UIDStr, value);
		}

		// 高清的压缩图片全部就在 list 路径里面了
		// 高清的压缩过的 bmp 对象 都在 Bimp.bmp里面
		// 完成上传服务器后 .........
		FileUtils.deleteDir();
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

	private void uploadFile(String uploadUrl, String srcPath) {
		String end = "\r\n";
		Log.d("---", uploadUrl);
		URL url;
		try {
			url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			// 设置块大小128K
			httpURLConnection.setChunkedStreamingMode(128 * 1024);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			FileInputStream fis = new FileInputStream(srcPath);
			DataOutputStream dos = new DataOutputStream(
					httpURLConnection.getOutputStream());
			 byte[] buffer = new byte[8192];// 8k
			 int count = 0;
			 while ((count = fis.read(buffer)) != -1) {
			 dos.write(buffer, 0, count);
			 }
			 fis.close();
			 dos.writeBytes(end);
			 dos.flush();
			 InputStream is = httpURLConnection.getInputStream();
			 InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			 BufferedReader br = new BufferedReader(isr);
			 String result = br.readLine();
			 Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			 dos.close();
			 is.close();
		} catch (Exception e) {
			Log.d("---", "错了错了");
		}
	}
}
