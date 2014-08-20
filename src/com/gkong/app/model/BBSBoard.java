package com.gkong.app.model;

public class BBSBoard {
	private String TopicID;
	private String Title;
	private String DateAndTime;
	private String hits;
	private String Child;

	public String getChild() {
		return Child;
	}

	public void setChild(String child) {
		Child = child;
	}

	public String getTopicID() {
		return TopicID;
	}

	public void setTopicID(String topicID) {
		TopicID = topicID;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDateAndTime() {
		return DateAndTime;
	}

	public void setDateAndTime(String dateAndTime) {
		DateAndTime = dateAndTime;
	}

	public String getHits() {
		return hits;
	}

	public void setHits(String hits) {
		this.hits = hits;
	}
}
