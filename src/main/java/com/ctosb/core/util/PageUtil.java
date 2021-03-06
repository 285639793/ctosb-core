package com.ctosb.core.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import com.ctosb.core.mybatis.dialet.DialetFactory;
import com.ctosb.core.mybatis.page.Limit;
import com.ctosb.core.mybatis.page.Page;
import com.ctosb.core.mybatis.sort.Sort;

public class PageUtil {
	/**
	 * get page or limit instance from paramterObject
	 * 
	 * @param parameterObj
	 * @return
	 * @author Alan
	 * @date 2016年6月30日 下午9:11:14
	 */
	public static Object getPageOrLimit(Object parameterObj) {
		Object object = null;
		// extract the page object from the input param
		if (Map.class.isInstance(parameterObj)) {
			Map<?, ?> parameterMap = ((Map<?, ?>) parameterObj);
			Collection<?> values = parameterMap.values();
			for (Object obj : values) {
				if (isPageOrLimit(obj)) {
					object = obj;
					break;
				}
			}
		} else if (isPageOrLimit(parameterObj)) {
			object = parameterObj;
		}
		return object;
	}

	@SuppressWarnings("unchecked")
	public static Object getSort(Object parameterObj) {
		Collection<Sort> sorts = new HashSet<Sort>();
		// extract the sort object from the input param
		if (Map.class.isInstance(parameterObj)) {
			//if  the param is Map,then loop its value
			Map<?, ?> parameterMap = ((Map<?, ?>) parameterObj);
			Collection<?> values = parameterMap.values();
			for (Object obj : values) {
				if (Sort.class.isInstance(obj)) {
					//process sort object
					sorts.add((Sort) obj);
				} else if (obj.getClass().isArray() && Sort.class.isAssignableFrom(obj.getClass().getComponentType())) {
					//process sort array
					Collections.addAll(sorts, (Sort[]) obj);
				} else if (Collection.class.isAssignableFrom(obj.getClass())
						&& Sort.class.isAssignableFrom(obj.getClass().getGenericSuperclass().getClass())) {
					//process sort collection
					sorts.addAll((Collection<Sort>) obj);
				}
			}
		} else if (Sort.class.isInstance(parameterObj)) {
			//process sort object
			sorts.add((Sort) parameterObj);
		} else if (parameterObj.getClass().isArray()
				&& Sort.class.isAssignableFrom(parameterObj.getClass().getComponentType())) {
			//process sort array
			Collections.addAll(sorts, (Sort[]) parameterObj);
		} else if (Collection.class.isAssignableFrom(parameterObj.getClass())
				&& Sort.class.isAssignableFrom(parameterObj.getClass().getGenericSuperclass().getClass())) {
			//process sort collection
			sorts.addAll((Collection<Sort>) parameterObj);
		}
		return sorts;
	}

	/**
	 * get paging infomation
	 * 
	 * @param parameterObj
	 * @return
	 * @author Alan
	 * @date 2015年12月12日 上午8:38:59
	 */
	public static Page getPage(Object parameterObj) {
		Page page = null;
		// extract the page object from the input param
		if (Map.class.isInstance(parameterObj)) {
			for (Object obj : ((Map<?, ?>) parameterObj).values()) {
				if (Page.class.isInstance(obj)) {
					page = (Page) obj;
					break;
				}
			}
		} else if (Page.class.isInstance(parameterObj)) {
			page = (Page) parameterObj;
		}
		return page;
	}

	/**
	 * get count sql
	 * 
	 * @param sql
	 * @param page
	 * @param dbType
	 * @return
	 * @author Alan
	 * @date 2015年12月12日 下午1:22:05
	 */
	public static String getCountSql(String sql, String dbType) {
		return DialetFactory.getLimit(dbType).getCountSql(sql);
	}

	/**
	 * get sort sql
	 * 
	 * @param sql
	 * @param dbType
	 * @param sorts
	 * @return
	 * @author Alan
	 * @date 2015年12月12日 下午1:22:05
	 */
	public static String getSortSql(String sql, String dbType, Collection<Sort> sorts) {
		// if the paging object is null，return source sql
		if (sorts == null || sorts.isEmpty()) {
			return sql;
		}
		return DialetFactory.getLimit(dbType).getSortSql(sql, sorts);
	}

	/**
	 * get limit sql
	 * 
	 * @param sql
	 * @param page
	 * @param dbType
	 * @return
	 * @author Alan
	 * @date 2015年12月12日 下午1:22:05
	 */
	public static String getLimitSql(String sql, Page page, String dbType) {
		// if the paging object is null，return source sql
		if (page == null) {
			return sql;
		}
		// join limit sql
		int offset = (page.getPageNum() - 1) * page.getPageSize();
		int limit = page.getPageSize();
		sql = DialetFactory.getLimit(dbType).getLimitSql(sql, offset, limit);
		return sql;
	}

	/**
	 * get limit sql
	 * 
	 * @param sql
	 * @param limit
	 * @param dbType
	 * @return
	 * @author Alan
	 * @date 2016年6月30日 下午2:17:18
	 */
	public static String getLimitSql(String sql, Limit limit, String dbType) {
		// if the paging object is null，return source sql
		if (limit == null) {
			return sql;
		}
		// join limit sql
		sql = DialetFactory.getLimit(dbType).getLimitSql(sql, limit.getOffset(), limit.getCount());
		return sql;
	}

	/**
	 * if object is Page or Limit,return true
	 * 
	 * @param object
	 * @return
	 */
	public static boolean isPageOrLimit(Object object) {
		return Page.class.isInstance(object) || Limit.class.isInstance(object);
	}
}
