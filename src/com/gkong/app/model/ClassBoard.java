package com.gkong.app.model;

public class ClassBoard {
	private int SID;
	private String BoardName;
	private int orders;
	private int BoardID;

	public ClassBoard() {
	}

	public ClassBoard(int SID, String BoardName, int oders, int BoardID) {
		this.SID = SID;
		this.BoardName = BoardName;
		this.orders = oders;
		this.BoardID = BoardID;
	}

	public int getSID() {
		return SID;
	}

	public void setSID(int sID) {
		SID = sID;
	}

	public String getBoardName() {
		return BoardName;
	}

	public void setBoardName(String boardName) {
		BoardName = boardName;
	}

	public int getOrders() {
		return orders;
	}

	public void setOrders(int orders) {
		this.orders = orders;
	}

	public int getBoardID() {
		return BoardID;
	}

	public void setBoardID(int boardID) {
		BoardID = boardID;
	}
}
