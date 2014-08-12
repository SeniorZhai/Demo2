package com.gkong.app.model;

import java.util.ArrayList;
import java.util.List;

import com.gkong.app.model.ClassBoardSrc.Item;

public class ClassBoardHandle {
	public static class GroupItem {
		public long SID;
		public String BoardName;
		public long orders;
		public int BoardId;
		public List<Item> items = new ArrayList<Item>();

		public GroupItem(Item item) {
			this.SID = item.getSID();
			this.BoardName = item.getBoardName();
			this.orders = item.getOrders();
			this.BoardId = item.getBoardId();
		}
	}

	public static List<GroupItem> getList(ClassBoardSrc board) {
		List<GroupItem> list = new ArrayList<GroupItem>();
		GroupItem groupItem =null;
		for (Item item : board.getHead()) {
			if (item.getBoardId() == 0) {
				groupItem = new GroupItem(item);
				list.add(groupItem);
			} else{
				groupItem.items.add(item);
			}
		}
		return list;
	}
}
