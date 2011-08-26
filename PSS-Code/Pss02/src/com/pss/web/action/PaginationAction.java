package com.pss.web.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.pss.exception.BusinessHandleException;

/**
 * 分页Action
 * 
 * @author Aries Zhao
 * 
 * @param <T>
 */
public abstract class PaginationAction<T> extends EntityAction{

	private static final long serialVersionUID = 1L;

	protected int page = 1;
	protected int pageSize = 5;
	protected int totalCount;

	private Map<String, Object> query = new HashMap<String, Object>();

	protected Map<String, Object> getQuery() {
		query.put("tenant", getTenantId());
		query.put("offset", getOffset());
		query.put("limit", getPageSize());
		return query;
	}

	
	public PaginationAction() {
		
	}

	/**
	 * 起始记录数（数据库）
	 * 
	 * @return
	 */
	protected int getOffset() {
		return page * pageSize - pageSize;
	}

	/**
	 * 设置页码
	 * 
	 * @param page
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * 设置每页行数
	 * 
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	
	/**
	 * 当前页码
	 * 
	 * @return
	 */
	public int getPage() {
		return page;
	}

	/**
	 * 总页数
	 * 
	 * @return
	 */
	public int getTotalPage() {
		return (totalCount + pageSize - 1) / pageSize;
	}

	/**
	 * 总行数
	 * 
	 * @return
	 */
	public int getTotalCount() {
		return totalCount;
	}

	/**
	 * 每页显示的行数
	 * 
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	public String queryEntity() {
		// 获得当前登陆用户的tanentId
		String tanentId = getTenantId();
		if (StringUtils.isBlank(tanentId)) {
			return INPUT;
		}
		try {
			getQuery().put("entity", entity);
			totalCount = getService().count(getQuery());
			entity.setTenant(getTenantId());
			items = getService().query(getQuery());
		} catch (BusinessHandleException e) {
			e.printStackTrace();
			return ERROR;
		}
		return SUCCESS;
	}
	
}
