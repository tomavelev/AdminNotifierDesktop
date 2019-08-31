package com.tomata.adminnotifier.dao;

import java.util.List;

public class QueryResult<T> {

	private List<T> list;
	private long count;
	private String message;

	public QueryResult() {
	}

	public QueryResult(List<T> list, long count) {
		super();
		this.list = list;
		this.count = count;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

