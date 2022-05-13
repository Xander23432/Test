package com.realnet.fnd.service;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.realnet.comm.entity.StudentAddmisionEntity;
public interface StudentAddmisionService {
	 public Page<StudentAddmisionEntity> getlist(Pageable page);
	    public StudentAddmisionEntity createCollageStudent(StudentAddmisionEntity data);
	    public StudentAddmisionEntity getid(int id);
	    public StudentAddmisionEntity updateById(int id, StudentAddmisionEntity studentaddmisionEntity);
	    public boolean deleteById(int id);
}
