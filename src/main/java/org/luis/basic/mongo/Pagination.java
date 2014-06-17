package org.luis.basic.mongo;

import java.util.List;

/**
 * 分页
 * @author guoliang.li
 * @date 2013-12-23
 */
public class Pagination {
	public Pagination(){
		
	}
	
	public Pagination(int total, int currPage){
		this.total = total;
		this.currPage = currPage;
	}
	
	private int total = 0;
	private int currPage;

	private List<MongoItem> items;

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<MongoItem> getItems() {
		return items;
	}

	public void setItems(List<MongoItem> items) {
		this.items = items;
	}

}
