package com.gkong.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "database.db";
	public static final int VERSION = 1;
	public static final String TABLE_CHANNEL = "channel";
	public static final String ID = "SID";
	public static final String JSON = "Json";
	public static final String SELECTED = "Selected";

	private Context context;

	public SQLHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table if not exists " + TABLE_CHANNEL
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + ID
				+ " INTEGER ," + JSON + " TEXT ,"  + SELECTED + " BLOB)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

}
