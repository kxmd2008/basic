package org.luis.basic.tag;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;

/**
 * 日期、时间格式化标签
 * 
 * @author Guoliang.Li
 * 
 */
public class DatetimeTag extends BaseTag {

	private static final long serialVersionUID = 3048873637581420438L;
	private Object datetime;
	private String pattern;

	public int doEndTag() throws JspException {
		String temp = null;
		sdf.applyPattern(pattern);
		if (datetime instanceof Date) {
			temp = sdf.format(datetime);
		} else if (datetime instanceof Long) {
			temp = sdf.format(new Date((Long) datetime));
		}
		writeMessage(temp);
		return EVAL_PAGE;
	}

	public void setDatetime(Object datetime) {
		this.datetime = datetime;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	private SimpleDateFormat sdf = new SimpleDateFormat();

}
