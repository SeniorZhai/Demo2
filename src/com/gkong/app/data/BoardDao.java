package com.gkong.app.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gkong.app.model.ClassBoardSrc.Item;

public class BoardDao implements BoardDaoInface {
	public SQLHelper helper = null;

	public BoardDao(Context context) {
		helper = new SQLHelper(context);
	}

	// 添加cache
	@Override
	public boolean addCache(Item item) {
		boolean flag = false;
		SQLiteDatabase database = null;
		long id = -1;
		try {
			database = helper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(SQLHelper.ID, item.getSID());
			values.put(SQLHelper.JSON, item.getJson());
			values.put(SQLHelper.SELECTED, item.isSelect());
			id = database.insert(SQLHelper.TABLE_CHANNEL, null, values);
			flag = (id != -1 ? true : false);
		} catch (Exception e) {

		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public boolean deleteCache(String whereClause, String[] whereArgs) {
		boolean flag = false;
		SQLiteDatabase database = null;
		int count = 0;
		try {
			database = helper.getWritableDatabase();
			count = database.delete(SQLHelper.TABLE_CHANNEL, whereClause,
					whereArgs);
			flag = (count > 0 ? true : false);
		} catch (Exception e) {

		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public boolean updateCache(ContentValues values, String whereClause,
			String[] whereArgs) {
		boolean flag = false;
		SQLiteDatabase database = null;
		int count = 0;
		try {
			database = helper.getWritableDatabase();
			count = database.update(SQLHelper.TABLE_CHANNEL, values,
					whereClause, whereArgs);
			flag = (count > 0 ? true : false);
		} catch (Exception e) {

		} finally {
			if (database != null) {
				database.close();
			}
		}
		return flag;
	}

	@Override
	public List<Item> listCache(String selection, String[] selectionArgs) {
		List<Item> list = new ArrayList<Item>();
		SQLiteDatabase database = null;
		Cursor cursor = null;
		String json = null;
		try {
			database = helper.getReadableDatabase();
			cursor = database.query(false, SQLHelper.TABLE_CHANNEL, null,
					selection, selectionArgs, null, null, null, null);
			cursor.moveToFirst();
			while (cursor.moveToNext()) {
				json = cursor.getString(cursor
						.getColumnIndex(SQLHelper.JSON));
				Item item = Item.getItem(json);
				item.setSelect((cursor.getInt(cursor.getColumnIndex(SQLHelper.SELECTED))== 1 ? true:false));
				list.add(item);
			}

		} catch (Exception e) {

		} finally {
			if (database != null) {
				database.close();
			}
		}
		return list;
	}

	// 删除表
	@Override
	public void clearFeedTable() {
		String sql = "DELETE FROM " + SQLHelper.TABLE_CHANNEL + ";";
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql);
		revertSeq();
	}

	// sqlite_sequence 自动编号所在的表 把表重置需要清空后，把编号归0
	private void revertSeq() {
		String sql = "update sqlite_sequence set seq=0 where name='"
				+ SQLHelper.TABLE_CHANNEL + "'";
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL(sql);
	}

	// 根据List初始化
	@Override
	public boolean initCache(List<Item> list) {
		this.deleteCache("_id" + ">= ?", new String[] { "0" });
		for (Item item : list) {
			this.addCache(item);
		}
		return false;
	}
}
