package com.gkong.app.model;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class ClassBoardSrc {
	private List<Item> Head;

	public static ClassBoardSrc getBoard(String json) {
		JSONObject obj1;
		try {
			obj1 = new JSONObject(json);
			String str = obj1.getString("d");
			return new Gson().fromJson(str, ClassBoardSrc.class);
		} catch (JSONException e) {
			return null;
		}
	}

	public List<Item> getHead() {
		return Head;
	}

	public void setHead(List<Item> head) {
		Head = head;
	}

	public static class Item {
		private long SID;
		private String BoardName;
		private long orders;
		private int BoardID;
		private boolean select = true;
		public boolean isSelect() {
			return select;
		}

		public void setSelect(boolean select) {
			this.select = select;
		}

		public int getBoardID() {
			return BoardID;
		}

		public String getJson() {
			return new Gson().toJson(this);
		}

		public static Item getItem(String json) {
			return new Gson().fromJson(json, Item.class);
		}

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

		public void setBoardID(int boardID) {
			BoardID = boardID;
		}
	}

}
