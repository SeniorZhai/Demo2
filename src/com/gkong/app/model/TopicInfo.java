package com.gkong.app.model;

public class TopicInfo {
	private String Title;
	private String BoardID;
	private String hits;
	private String allChild;
	private String board_title;
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getBoardID() {
		return BoardID;
	}
	public void setBoardID(String boardID) {
		BoardID = boardID;
	}
	public String getHits() {
		return hits;
	}
	public void setHits(String hits) {
		this.hits = hits;
	}
	public String getAllChild() {
		return allChild;
	}
	public void setAllChild(String allChild) {
		this.allChild = allChild;
	}
	public String getBoard_title() {
		return board_title;
	}
	public void setBoard_title(String board_title) {
		this.board_title = board_title;
	}
}
