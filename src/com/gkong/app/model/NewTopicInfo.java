package com.gkong.app.model;

public class NewTopicInfo {
	private boolean IsSuccess;
	private String Message;
	private int TopicId ;
	public boolean isIsSuccess() {
		return IsSuccess;
	}
	public void setIsSuccess(boolean isSuccess) {
		IsSuccess = isSuccess;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public int getTopicId() {
		return TopicId;
	}
	public void setTopicId(int topicId) {
		TopicId = topicId;
	}
}
