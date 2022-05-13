package com.realnet.qb.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.realnet.qb.entity.Rn_CreateQuery;

public interface Rn_CreateQuery_Service {
	List<Rn_CreateQuery> getAll();
	Page<Rn_CreateQuery> getAll(Pageable p);
	
	Rn_CreateQuery getById(int id);
	Rn_CreateQuery save(Rn_CreateQuery query);
	Rn_CreateQuery updateById(int id, Rn_CreateQuery query);
	boolean deleteById(int id);

}
