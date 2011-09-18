package com.pss.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pss.domain.model.entity.inventory.Storage;
import com.pss.domain.repository.BaseRepository;
import com.pss.domain.repository.inventory.UnStorageRepository;
import com.pss.service.IStorageService;

/**
 * 
 * @author Aries Zhao
 * 
 */
@Service
public class UnStorageService extends AbstractService<Storage> implements
		IStorageService {

	@Autowired
	private UnStorageRepository unStorageRepository;

	@Override
	public BaseRepository<Storage> repository() {
		return unStorageRepository;
	}
}
