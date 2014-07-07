package com.gkong.app.config;

//public class Api {
//	/*
//	 * 登入 post请求 请求参数 username、password 返回 data、id、msg、name、sucess
//	 */
//	public static final String Login = "http://www.gkong.com/passport/login.ashx";
//
//	// 获取论坛版块
//	public static final String Board = "http://api.local.gkong.com/serviceapi.svc/GetBBSClassBoard";
//	
//	public static final String NewBoard (String type,String sort,int page){
//		return "http://api.local.gkong.com/serviceapi.svc/GetBBSListNew?Type="+type+"&Sort="+sort+"&Page="+page;
//	}
//	
//	// 获取帖子列表
//	public static final String List(String boardID, int page) {
//		return "http://api.local.gkong.com/serviceapi.svc/GetBBSListByBoardID"
//				+ "?BoardID=" + boardID + "&Page=" + page;
//	}
//
//	// 获取帖子内容
//
//	public static final String Archive(String TopicID, int page) {
//		return "http://api.local.gkong.com/serviceapi.svc/GetArchive"
//				+ "?TopicID=" + TopicID + "&Page=" + page;
//	}
//
//	// 上传文件
//	public static final String Upload = "http://filestools.gkong.com/bbs/appcan_upload.ashx?auth=";
//	// 发帖
//	public static final String NewTopic = "http://192.168.0.121:7080/API/BBS_New_Topic.ashx";
//	// 回帖
//	public static final String RepayTopic = "http://192.168.0.121:7080/API/BBS_Repay_Topic.ashx";
//}
public class Api {
	/*
	 * 登入 post请求 请求参数 username、password 返回 data、id、msg、name、sucess
	 */
	public static final String Login = "http://www.gkong.com/passport/login.ashx";
	
	// 获取论坛版块
	public static final String Board = "http://api.gkong.com/serviceapi.svc/GetBBSClassBoard";
	
	// 获取帖子列表
	public static final String List(String boardID, int page) {
		return "http://api.gkong.com/serviceapi.svc/GetBBSListByBoardID"
				+ "?BoardID=" + boardID + "&Page=" + page;
	}
	public static final String NewBoard (String type,String sort,int page){
		return "http://api.gkong.com/serviceapi.svc/GetBBSListNew?Type="+type+"&Sort="+sort+"&Page="+page;
	}
	
	// 获取帖子内容
	
	public static final String Archive(String TopicID,int page){
		return "http://api.gkong.com/serviceapi.svc/GetArchive"
				+ "?TopicID=" + TopicID + "&Page=" + page;
	}
	// 上传文件
	public static final String Upload = "http://filestools.gkong.com/bbs/appcan_upload.ashx?auth=";
	// 发帖
	public static final String NewTopic = "http://m.gkong.com/API/BBS_New_Topic.ashx";
	// 回帖
	public static final String RepayTopic = "http://m.gkong.com//API/BBS_Repay_Topic.ashx";
}
