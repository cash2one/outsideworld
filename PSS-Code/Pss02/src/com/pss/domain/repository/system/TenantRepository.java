/*
 *
 * ========================================================================
 * 版权:   Travelsky  版权所有  (c) 2010 - 2030
 * 所含类(文件):  com.pss.domain.repository.system.TenantRepository.java
 *
 *
 * 修改记录：
 * 日期                       作者                              内容
 * ========================================================================
 * Jun 27, 2011       Travelsky         新建文件
 * ========================================================================
 */

package com.pss.domain.repository.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pss.dao.system.TenantMapper;
import com.pss.domain.model.entity.sys.Tenant;
import com.pss.exception.BusinessHandleException;

/**
 * <p>
 * 类说明
 * </p>
 * <p>
 * Copyright: 版权所有 (c) 2010 - 2030
 * </p>
 * <p>
 * Company: Travelsky
 * </p>
 * 
 * @author Travelsky
 * @version 1.0
 * @since Jun 27, 2011
 */
@Repository
@Transactional
public class TenantRepository {

	@Autowired
	private TenantMapper tenantMapper;

	public void add(Tenant tenant) throws BusinessHandleException {
		tenantMapper.addTenant(tenant);
	}

	public Tenant query(String tenantId) throws BusinessHandleException {
		return null;
	}
}