package com.gkong.app.config;

//public class Api {
//	/*
//	 * ���� post���� ������� username��password ���� data��id��msg��name��sucess
//	 */
//	public static final String Login = "http://www.gkong.com/passport/login.ashx";
//
//	// ��ȡ��̳���
//	public static final String Board = "http://api.local.gkong.com/serviceapi.svc/GetBBSClassBoard";
//	
//	public static final String NewBoard (String type,String sort,int page){
//		return "http://api.local.gkong.com/serviceapi.svc/GetBBSListNew?Type="+type+"&Sort="+sort+"&Page="+page;
//	}
//	
//	// ��ȡ�����б�
//	public static final String List(String boardID, int page) {
//		return "http://api.local.gkong.com/serviceapi.svc/GetBBSListByBoardID"
//				+ "?BoardID=" + boardID + "&Page=" + page;
//	}
//
//	// ��ȡ��������
//
//	public static final String Archive(String TopicID, int page) {
//		return "http://api.local.gkong.com/serviceapi.svc/GetArchive"
//				+ "?TopicID=" + TopicID + "&Page=" + page;
//	}
//
//	// �ϴ��ļ�
//	public static final String Upload = "http://filestools.gkong.com/bbs/appcan_upload.ashx?auth=";
//	// ����
//	public static final String NewTopic = "http://192.168.0.121:7080/API/BBS_New_Topic.ashx";
//	// ����
//	public static final String RepayTopic = "http://192.168.0.121:7080/API/BBS_Repay_Topic.ashx";
//}
public class Api {
	/*
	 * ���� post���� ������� username��password ���� data��id��msg��name��sucess
	 */
	public static final String Login = "http://www.gkong.com/passport/login.ashx";
	
	// ��ȡ��̳���
	public static final String Board = "http://api.gkong.com/serviceapi.svc/GetBBSClassBoard";
	
	// ��ȡ�����б�
	public static final String List(String boardID, int page) {
		return "http://api.gkong.com/serviceapi.svc/GetBBSListByBoardID"
				+ "?BoardID=" + boardID + "&Page=" + page;
	}
	public static final String NewBoard (String type,String sort,int page){
		return "http://api.gkong.com/serviceapi.svc/GetBBSListNew?Type="+type+"&Sort="+sort+"&Page="+page;
	}
	
	// ��ȡ��������
	
	public static final String Archive(String TopicID,int page){
		return "http://api.gkong.com/serviceapi.svc/GetArchive"
				+ "?TopicID=" + TopicID + "&Page=" + page;
	}
	// �ϴ��ļ�
	public static final String Upload = "http://filestools.gkong.com/bbs/appcan_upload.ashx?auth=";
	// ����
	public static final String NewTopic = "http://m.gkong.com/API/BBS_New_Topic.ashx";
	// ����
	public static final String RepayTopic = "http://m.gkong.com//API/BBS_Repay_Topic.ashx";
}
