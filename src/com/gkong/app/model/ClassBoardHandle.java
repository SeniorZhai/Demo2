package com.gkong.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gkong.app.model.ClassBoardSrc.Item;

public class ClassBoardHandle {
	public static class GroupItem implements Serializable{
		private static final long serialVersionUID = -113751092900269375L;
		public long SID;
		public String BoardName;
		public long orders;
		public int BoardId;
		public List<Item> items = new ArrayList<Item>();

		public long getSID() {
			return SID;
		}

		public void setSID(long sID) {
			SID = sID;
		}

		public String getBoardName() {
			return BoardName;
		}

		public void setBoardName(String boardName) {
			BoardName = boardName;
		}

		public long getOrders() {
			return orders;
		}

		public void setOrders(long orders) {
			this.orders = orders;
		}

		public int getBoardId() {
			return BoardId;
		}

		public void setBoardId(int boardId) {
			BoardId = boardId;
		}

		public List<Item> getItems() {
			return items;
		}

		public void setItems(List<Item> items) {
			this.items = items;
		}

		public GroupItem(Item item) {
			this.SID = item.getSID();
			this.BoardName = item.getBoardName();
			this.orders = item.getOrders();
			this.BoardId = item.getBoardID();
		}
	}

	public static List<GroupItem> getList(List<Item> board) {
		List<GroupItem> list = new ArrayList<GroupItem>();
		GroupItem groupItem = null;
		for (Item item : board) {
			if (item.getBoardID() == 0) {
				groupItem = new GroupItem(item);
				list.add(groupItem);
			} else{
				groupItem.items.add(item);
			}
		}
		return list;
	}
}
