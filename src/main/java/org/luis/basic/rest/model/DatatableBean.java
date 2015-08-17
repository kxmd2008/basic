package org.luis.basic.rest.model;

import java.util.ArrayList;
import java.util.List;

/**
 * datatable对象，分页，及页面使用
 * @author Luis
 * @date 2015-8-17 下午9:33:23
 * @param <T>
 */
public class DatatableBean<T> {
	private Integer draw;
	private Integer recordsTotal;
	private Integer recordsFiltered;
	private List<T> data = new ArrayList<T>();

	public Integer getDraw() {
		return draw;
	}

	public void setDraw(Integer draw) {
		this.draw = draw;
	}

	public Integer getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(Integer recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public Integer getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(Integer recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

}
