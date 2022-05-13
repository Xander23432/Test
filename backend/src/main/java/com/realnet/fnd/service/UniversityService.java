package com.realnet.fnd.service;

import com.realnet.comm.entity.UniversityEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UniversityService {
	public Page<UniversityEntity> getAll(Pageable page);
	public UniversityEntity getById(int id);
	public UniversityEntity save(UniversityEntity teacher);
	public UniversityEntity updateById(int id, UniversityEntity teacherRequest);
	public boolean deleteById(int id);
}
