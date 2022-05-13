package com.realnet.comm.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.realnet.comm.entity.Student;

public interface StudentService {
	//List<Student> getAll(int header_id);
	Page<Student> getAll(int header_id, Pageable page);
	Student getByIdAndHeaderId(int id, int header_id);
	Optional<Student> saveByHeaderId(int header_id, Student student);
	//Optional<Student> updateByIdAndHeaderId(int id, int header_id, Student student);
	Student updateByIdAndHeaderId(int id, int header_id, Student student);
	boolean deleteByIdAndHeaderId(int id, int header_id);

}
