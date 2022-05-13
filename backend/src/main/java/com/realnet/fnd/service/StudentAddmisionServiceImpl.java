package com.realnet.fnd.service;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import com.realnet.comm.entity.StudentAddmisionEntity;
import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.fnd.repository.StudentAddmisionRepository;

@Service
public class StudentAddmisionServiceImpl implements StudentAddmisionService {
	@Autowired
	private	StudentAddmisionRepository  studentaddmisionRepository;
	public Page<StudentAddmisionEntity> getlist(Pageable page) {
		// TODO Auto-generated method stub
		return studentaddmisionRepository.findAll(page);
	}


	@Override
	public StudentAddmisionEntity createCollageStudent(StudentAddmisionEntity data) {
		// TODO Auto-generated method stub
		return studentaddmisionRepository.save(data);
	}

	@Override
	public StudentAddmisionEntity getid(int id) {
		// TODO Auto-generated method stub
		StudentAddmisionEntity orElseThrow = this.studentaddmisionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rn_Instructor not found :: " + id));
		return orElseThrow;
	}

	@Override
	public StudentAddmisionEntity updateById(int id, StudentAddmisionEntity studentaddmisionEntity) {
		// TODO Auto-generated method stub
		StudentAddmisionEntity student = studentaddmisionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("student not found :: " + id));
			
		student.setName(studentaddmisionEntity.getName());
		
		
		student.setBirthdate(studentaddmisionEntity.getBirthdate());
		
		
		student.setAge(studentaddmisionEntity.getAge());
		
		
		student.setBirthplace(studentaddmisionEntity.getBirthplace());
		
		
		student.setPermanentadd(studentaddmisionEntity.getPermanentadd());
		
            student.setMainarea(studentaddmisionEntity.getMainarea());
		
		
		student.setBuildingname(studentaddmisionEntity.getBuildingname());
		
		
		student.setPincode(studentaddmisionEntity.getPincode());
		
		
		student.setPostaladd(studentaddmisionEntity.getPostaladd());
		 student.setPriviousschool(studentaddmisionEntity.getPriviousschool());
			
			
			student.setOtheractivity(studentaddmisionEntity.getOtheractivity());
			
			
			student.setMark(studentaddmisionEntity.getMark());
			
			
			student.setGrade(studentaddmisionEntity.getGrade());
		
		studentaddmisionRepository.save(student);
		
		return student;
	}

	@Override
	public boolean deleteById(int id) {
		// TODO Auto-generated method stub
if (!studentaddmisionRepository.existsById(id)) {
			
			throw new ResourceNotFoundException("CollegeStudent  not found :: " + id);
		}
		
		StudentAddmisionEntity student = studentaddmisionRepository.findById(id).orElse(null);
		studentaddmisionRepository.delete(student);
		return true;
	}

}
