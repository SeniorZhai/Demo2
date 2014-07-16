package com.gkong.app.data;

import java.util.List;

import android.content.ContentValues;

import com.gkong.app.model.ClassBoard;

public interface BoardDaoInface {
	public boolean addCache(ClassBoard item);
	public boolean deleteCache(String whereClause, String[] whereArgs);
	public boolean updateCache(ContentValues values, String whereClause,
			String[] whereArgs);
	public void clearFeedTable();
	public List<ClassBoard> listCache(String selection,
			String[] selectionArgs);
	public boolean initCache(List<ClassBoard> list);
}
