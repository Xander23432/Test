package com.realnet.fnd.service;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.realnet.comm.entity.CollegeStudentEntity;
import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.fnd.repository.CollegeStudentRepository;
@Service
public class CollegeStudentServiceImpl implements CollegeStudentService {
	@Autowired
	private	CollegeStudentRepository  collegeStudentRepository;
	public Page<CollegeStudentEntity> getlist(Pageable page) {
		// TODO Auto-generated method stub
		return collegeStudentRepository.findAll(page);
	}
	@Override
	public CollegeStudentEntity createCollageStudent(CollegeStudentEntity data) {
		// TODO Auto-generated method stub
		return collegeStudentRepository.save(data);
	}
	
	@Override
	public CollegeStudentEntity updateById(int id, CollegeStudentEntity collegePortalEntity) {
		
		CollegeStudentEntity student = collegeStudentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("student not found :: " + id));
			
		student.setStudentname(collegePortalEntity.getStudentname());
		
		
		student.setDepartment(collegePortalEntity.getDepartment());
		
		
		student.setJoiningDate(collegePortalEntity.getJoiningDate());
		
		
		student.setPhone(collegePortalEntity.getPhone());
		
		
		student.setEmailId(collegePortalEntity.getEmailId());
		
		collegeStudentRepository.save(student);
		
		return student;
	}
	
	
	
	public boolean deleteById(int id) {
		if (!collegeStudentRepository.existsById(id)) {
			
			throw new ResourceNotFoundException("CollegeStudent  not found :: " + id);
		}
		
		CollegeStudentEntity student = collegeStudentRepository.findById(id).orElse(null);
		collegeStudentRepository.delete(student);
		return true;
	}
	@Override
	public CollegeStudentEntity getid(int id) {
		
		CollegeStudentEntity orElseThrow = this.collegeStudentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rn_Instructor not found :: " + id));
		return orElseThrow;
	}
	
	
	
}