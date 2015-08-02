package org.luis.basic.tag;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;
import org.luis.basic.util.DictManager;

public class DictTag extends BaseTag {
	private static final long serialVersionUID = 1042263423621586319L;
	private String code;
	private String itemValue;

	public int doEndTag() throws JspException {
		String label = DictManager.getInstance().getLabel(code, itemValue);
		if(StringUtils.isBlank(label)){
			label = code;
		}
		writeMessage(label);
		return EVAL_PAGE;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

}

