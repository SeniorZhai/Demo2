package com.gkong.app.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	public static void show(Context context,String str){
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}
	public static void show_long(Context context,String str){
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}
}
