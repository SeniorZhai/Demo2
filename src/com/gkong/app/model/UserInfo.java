package com.gkong.app.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class UserInfo {
	private int UserScore;
	private int UserID;
	private int UserBBSTopicCount;
	private int UserBBSRepayCount;
	private String UserName;

	public static UserInfo getUserInfo(String json) {
		JSONObject obj1;
		try {
			obj1 = new JSONObject(json);
			String str = obj1.getString("d");
			return new Gson().fromJson(str, UserInfo.class);
		} catch (JSONException e) {
			return null;
		}
	}

	public int getUserScore() {
		return UserScore;
	}

	public void setUserScore(int userScore) {
		UserScore = userScore;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public int getUserBBSTopicCount() {
		return UserBBSTopicCount;
	}

	public void setUserBBSTopicCount(int userBBSTopicCount) {
		UserBBSTopicCount = userBBSTopicCount;
	}

	public int getUserBBSRepayCount() {
		return UserBBSRepayCount;
	}

	public void setUserBBSRepayCount(int userBBSRepayCount) {
		UserBBSRepayCount = userBBSRepayCount;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}
}
