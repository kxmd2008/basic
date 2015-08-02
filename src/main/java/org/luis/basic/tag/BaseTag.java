package org.luis.basic.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class BaseTag extends TagSupport {

	private static final long serialVersionUID = 6383701994336167427L;

	protected void writeMessage(String msg) throws JspException {
		try {
			pageContext.getOut().write(String.valueOf(msg));
		} catch (IOException ioe) {
			throw new JspException(ioe);
		}
	}
}
