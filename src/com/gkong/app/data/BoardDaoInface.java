package com.gkong.app.data;

import java.util.List;

import android.content.ContentValues;

import com.gkong.app.model.ClassBoardSrc.Item;

public interface BoardDaoInface {
	// 添加板块
	public boolean addCache(Item item);
	// 删除板块
	public boolean deleteCache(String whereClause, String[] whereArgs);
	// 更新板块
	public boolean updateCache(ContentValues values, String whereClause,
			String[] whereArgs);
	// 清空表
	public void clearFeedTable();
	// 获取板块链表
	public List<Item> listCache(String selection,
			String[] selectionArgs);
	// 通过板块链表初始化数据库
	public boolean initCache(List<Item> list);
}
