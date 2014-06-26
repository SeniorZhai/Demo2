package com.gkong.app.model;

public class Archive {
	private String UserName;
	private String DateAndTime;
	private String Body;
	private String AnnounceID;
	public String getAnnounceID() {
		return AnnounceID;
	}
	public void setAnnounceID(String announceID) {
		AnnounceID = announceID;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getDateAndTime() {
		return DateAndTime;
	}
	public void setDateAndTime(String dateAndTime) {
		DateAndTime = dateAndTime;
	}
	public String getBody() {
		return Body;
	}
	public void setBody(String body) {
		Body = body;
	}
}
