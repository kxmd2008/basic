package org.luis.basic.domain;

import org.luis.basic.domain.service.BaseService;
import org.luis.basic.util.SpringContextFactory;

/**
 * Service工厂，根据Bean类型产生相应的Service<br/>
 * 例：<br/>
 * private static IGenericService<NgbfDictItem> dictItemService;
 * <p >public static IGenericService<NgbfDictItem> getDictItemService() {</p>
 * <p style="text-indent: 2em">if(dictItemService == null){</p>
 * <p style="text-indent: 3em">dictItemService = GenericServiceBuilder.build(NgbfDictItem.class);</p>
 * <p style="text-indent: 2em">}</p>
 * <p style="text-indent: 2em">return dictItemService;</p>
 * }<br/>
 * @author guoliang.li
 */
public final class BaseServiceBuilder {

	/**
	 * 根据Bean类型产生相应的Service
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BaseEntity> IGenericService<T> build(Class<T> clazz) {
		IGenericService<T> service = SpringContextFactory.getSpringBean(IGenericService.class);
		service.setEntityClass(clazz);
		return service;
	}
	
	/**
	 * 根据Bean类型产生相应的Service
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> BaseService<T> build() {
		BaseService<T> service = SpringContextFactory.getSpringBean(BaseService.class);
		return service;
	}

}
