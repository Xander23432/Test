package com.realnet.fnd.service;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.realnet.comm.entity.CollegeStudentEntity;
public interface CollegeStudentService {
	
	    public Page<CollegeStudentEntity> getlist(Pageable page);
	    public CollegeStudentEntity createCollageStudent(CollegeStudentEntity data);
	    public CollegeStudentEntity getid(int id);
	    public CollegeStudentEntity updateById(int id, CollegeStudentEntity collegePortalEntity);
	    public boolean deleteById(int id);
}