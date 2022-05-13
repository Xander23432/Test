package com.realnet.comm.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.realnet.comm.entity.Teacher;

public interface TeacherService {
	List<Teacher> getAll();
	Page<Teacher> getAll(Pageable p);
	Teacher getById(int id);
	Teacher save(Teacher teacher);
	Teacher updateById(int id, Teacher instructor);
	boolean deleteById(int id);

}
