package org.luis.basic.domain;

public abstract class AbstractListFilter implements IListFilter{
	protected FilterAttributes attrs;
	protected OrderAttributes orderAttrs;
	

	@Override
	public void append(IListFilter filter) {
	}

	// 缺省返回false
	protected boolean canAppendable = Boolean.FALSE;

	@Override
	public boolean isCanAppendable() {
		return canAppendable;
	}

	public void setCanAppendable(boolean canAppendable) {
		this.canAppendable = canAppendable;
	}
	
	public FilterAttributes getAttrs(){
		return attrs;
	}
	
	public OrderAttributes getOrderAttrs(){
		return orderAttrs;
	}
		
}
