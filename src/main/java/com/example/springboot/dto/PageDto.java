package com.example.springboot.dto;

public class PageDto {

	private int pageSize;
	private int pageNumber;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	@Override
	public String toString() {
		return "PageDto [pageSize=" + pageSize + ", pageNumber=" + pageNumber + "]";
	}
}
