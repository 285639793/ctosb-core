package com.ctosb.core.mybatis.dialet;

import java.util.Collection;

import com.ctosb.core.mybatis.sort.Sort;

public abstract class AbstractDialet implements Dialet {

	@Override
	public String getCountSql(String sql) {
		return "SELECT COUNT(1) FROM (" + sql + ") TMP";
	}
	
	@Override
	public String getSortSql(String sql,Collection<Sort> sorts) {
		if (sorts == null || sorts.isEmpty()) {
			return sql;
		}
		// join limit sql
		StringBuilder sb = new StringBuilder();
		for (Sort sort : sorts) {
			sb.append(",").append(sort.getFieldName()).append(" ").append(sort.getSortType());
		}
		return sql + " ORDER BY " + sb.substring(1);
	}

}
