package com.gkong.app.model;

public class ClassBoard {
	private String SID;
	private String BoardName;
	private String orders;
	private String BoardID;
	public String getSID() {
		return SID;
	}
	public void setSID(String sID) {
		SID = sID;
	}
	public String getBordName() {
		return BoardName;
	}
	public void setBordName(String boardName) {
		BoardName = boardName;
	}
	public String getOrders() {
		return orders;
	}
	public void setOrders(String orders) {
		this.orders = orders;
	}
	public String getBoardID() {
		return BoardID;
	}
	public void setBoardID(String boardID) {
		BoardID = boardID;
	}
}
