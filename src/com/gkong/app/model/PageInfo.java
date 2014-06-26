package com.gkong.app.model;

public class PageInfo {
	private String CurrentPage;
	private String PageCount;
	private String RecordCount;
	public String getCurrentPage() {
		return CurrentPage;
	}
	public void setCurrentPage(String currentPage) {
		CurrentPage = currentPage;
	}
	public String getPageCount() {
		return PageCount;
	}
	public void setPageCount(String pageCount) {
		PageCount = pageCount;
	}
	public String getRecordCount() {
		return RecordCount;
	}
	public void setRecordCount(String recordCount) {
		RecordCount = recordCount;
	}
}
