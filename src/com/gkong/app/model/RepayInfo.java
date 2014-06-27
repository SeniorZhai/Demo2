package com.gkong.app.model;

public class RepayInfo {
	private boolean IsSuccess;
	private String Message;
	private int AnnounceId ;
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
	public int getAnnounceId() {
		return AnnounceId;
	}
	public void setAnnounceId(int announceId) {
		AnnounceId = announceId;
	}
}
