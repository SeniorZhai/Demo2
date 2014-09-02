package com.gkong.app.utils;

import static android.os.Environment.MEDIA_MOUNTED;
import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

public class CacheUtils {
	/**
	 * 一般保存一些需要长时间保存的数据
	 * @param context
	 * @return /data/data/files目录
	 */
	public static File getFileDirectory(Context context) {
		File appCacheDir = null;
		if (appCacheDir == null) {
			appCacheDir = context.getFilesDir();
		}
		if (appCacheDir == null) {
			String cacheDirPath = "/data/data/" + context.getPackageName()
					+ "/files/";
			appCacheDir = new File(cacheDirPath);
		}
		return appCacheDir;
	}
	// 获取cache目录,perferExternal指定内部或外部内存，为内部时文件夹超过1M的时候，系统会删除文件夹里的数据
	public static File getCacheDirectory(Context context,
			boolean preferExternal, String dirName) {
		File appCacheDir = null;
		// 外部
		if (preferExternal
				&& MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				&& hasExternalStoragePermission(context)) {
			appCacheDir = getExternalCacheDir(context, dirName);
		}
		if (appCacheDir == null) {
			// 内部存储
			appCacheDir = context.getCacheDir();
		}
		if (appCacheDir == null) {
			String cacheDirPath = "/data/data/" + context.getPackageName()
					+ "/cache/";
			Log.w("Can't define system cache directory! '%s' will be used.",
					cacheDirPath);
			appCacheDir = new File(cacheDirPath);
		}
		return appCacheDir;
	}

	private static File getExternalCacheDir(Context context, String dirName) {
		File dataDir = new File(new File(
				Environment.getExternalStorageDirectory(), "Android"), "data");
		File appCacheDir2 = new File(
				new File(dataDir, context.getPackageName()), "cache");
		File appCacheDir = new File(appCacheDir2, dirName);
		if (!appCacheDir.exists()) {
			if (!appCacheDir.mkdirs()) {
				Log.w(TAG, "Unable to create external cache directory");
				return null;
			}
			try {
				new File(appCacheDir, ".nomedia").createNewFile();
			} catch (IOException e) {
				Log.i(TAG,
						"Can't create \".nomedia\" file in application external cache directory");
			}
		}
		return appCacheDir;
	}

	private static final String TAG = "CacheUtils";
	private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

	private static boolean hasExternalStoragePermission(Context context) {
		int perm = context
				.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
		return perm == PackageManager.PERMISSION_GRANTED;
	}

}
