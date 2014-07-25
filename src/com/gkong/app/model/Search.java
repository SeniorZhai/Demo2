package com.gkong.app.model;

public class Search {
	private int TopicID;
	private String title;
	private String url;
	private String DateAndTime;
	private String username;
	public int getTopicID() {
		return TopicID;
	}
	public void setTopicID(int topicID) {
		TopicID = topicID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDateAndTime() {
		return DateAndTime;
	}
	public void setDateAndTime(String dateAndTime) {
		DateAndTime = dateAndTime;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}
