package com.realnet.qb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.qb.entity.Rn_CreateQuery;
import com.realnet.qb.repository.Rn_CreateQuery_Repository;
import com.realnet.users.entity.User;
import com.realnet.users.service.UserService;
import com.realnet.utils.Constant;

@Service
public class Rn_CreateQuery_ServiceImpl implements Rn_CreateQuery_Service {

	@Autowired(required=false)
	private UserService userService;

	@Autowired
	private Rn_CreateQuery_Repository createQueryRepository;

	@Override
	public List<Rn_CreateQuery> getAll() {
		return createQueryRepository.findAll();
	}

	@Override
	public Page<Rn_CreateQuery> getAll(Pageable page) {
		return createQueryRepository.findAll(page);
	}

	@Override
	public Rn_CreateQuery getById(int id) {
		Rn_CreateQuery query = createQueryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :: " + id));
		return query;
	}

	@Override
	public Rn_CreateQuery save(Rn_CreateQuery query) {
		User loggedInUser = userService.getLoggedInUser();
		long userId = loggedInUser.getUserId();
		long accId = loggedInUser.getSys_account().getId();
		query.setCreatedBy(userId);
		query.setUpdatedBy(userId);
		query.setAccountId(accId);
		Rn_CreateQuery savedQuery = createQueryRepository.save(query);
		return savedQuery;
	}

	@Override
	public Rn_CreateQuery updateById(int id, Rn_CreateQuery queryRequest) {
		User loggedInUser = userService.getLoggedInUser();
		long userId = loggedInUser.getUserId();
		long accId = loggedInUser.getSys_account().getId();
		Rn_CreateQuery oldQuery = createQueryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :: " + id));
		oldQuery.setProjectId(queryRequest.getProjectId());
		oldQuery.setTableName(queryRequest.getTableName());
		oldQuery.setCreateQuery(queryRequest.getCreateQuery());
		oldQuery.setBuild(queryRequest.isBuild());
		oldQuery.setData(queryRequest.isData());
		oldQuery.setUpdatedBy(userId);
		oldQuery.setAccountId(accId);
		
		final Rn_CreateQuery updatedQuery = createQueryRepository.save(oldQuery);
		return updatedQuery;
	}

	@Override
	public boolean deleteById(int id) {
		if (!createQueryRepository.existsById(id)) {
			throw new ResourceNotFoundException(Constant.NOT_EXIST_EXCEPTION);
		}
		Rn_CreateQuery query = createQueryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Constant.NOT_FOUND_EXCEPTION + " :: " + id));
		createQueryRepository.delete(query);
		return true;
	}
}
