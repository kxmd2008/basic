package org.luis.basic.tag;

import java.util.Date;

import javax.servlet.jsp.JspException;

/**
 * 日期、时间格式化标签
 * 
 * @author Guoliang.Li
 * 
 */
public class DatetimeTag extends BaseTag {

	private Object datetime;
	private String partten;

	public int doEndTag() throws JspException {
		String temp = null;
		if(datetime instanceof Date){
			System.out.println("Date");
		} else if(datetime instanceof Long){
			System.out.println("Long");
		}
		return EVAL_PAGE;
	}

	public void setDatetime(Object datetime) {
		this.datetime = datetime;
	}

	public void setPartten(String partten) {
		this.partten = partten;
	}

}
