package com.gkong.app.model;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import u.fb.o;

import android.util.Log;

import com.google.gson.Gson;

public class BBSRe {
	private String TopicID;
	private String Title;
	private String DateAndTime;
	private String hits;
	private String Child;
	private String LastDateAndTime;
	private String PostUserName;
	
	public static List<BBSRe> getList(String json){	
		try {
			JSONObject obj1;
			obj1 = new JSONObject(json);
			String str = obj1.getString("d");
			obj1 = new JSONObject(str);
			str = obj1.getString("Head");
			BBSRe[] topics = new Gson().fromJson(str, BBSRe[].class);
			return Arrays.asList(topics);
		} catch (JSONException e) {
			return null;
		}
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
	public String getChild() {
		return Child;
	}
	public void setChild(String child) {
		Child = child;
	}
	public String getLastDateAndTime() {
		return LastDateAndTime;
	}
	public void setLastDateAndTime(String lastDateAndTime) {
		LastDateAndTime = lastDateAndTime;
	}
	public String getPostUserName() {
		return PostUserName;
	}
	public void setPostUserName(String postUserName) {
		PostUserName = postUserName;
	}
}
