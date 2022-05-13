package com.realnet.fnd.service;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import com.realnet.comm.entity.PlayEntity;
import com.realnet.exceptions.ResourceNotFoundException;

import com.realnet.fnd.repository.PlayRepository;
@Service
public class PlayServiceIMPL implements PlayService {

	@Autowired
	private	PlayRepository  collegeStudentRepository;
	
	@Override
	public Page<PlayEntity> getlist(Pageable page) {
		// TODO Auto-generated method stub
		return collegeStudentRepository.findAll(page);
	}

	@Override
	public PlayEntity createCollageStudent(PlayEntity data) {
		// TODO Auto-generated method stub
		return collegeStudentRepository.save(data);
	}

	@Override
	public PlayEntity getid(int id) {
		// TODO Auto-generated method stub
		PlayEntity orElseThrow = this.collegeStudentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Rn_Instructor not found :: " + id));
		return orElseThrow;
		
	}

	@Override
	public PlayEntity updateById(int id, PlayEntity collegePortalEntity) {
		// TODO Auto-generated method stub
		PlayEntity student = collegeStudentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("student not found :: " + id));
			
		student.setStudentname(collegePortalEntity.getStudentname());
		
		student.setWf_instance_id(collegePortalEntity.getWf_instance_id());
		
		student.setDepartment(collegePortalEntity.getDepartment());
		
		
		student.setJoiningDate(collegePortalEntity.getJoiningDate());
		
		
		student.setPhone(collegePortalEntity.getPhone());
		
		
		student.setEmailId(collegePortalEntity.getEmailId());
		
		collegeStudentRepository.save(student);
		return student;
	}

	@Override
	public boolean deleteById(int id) {
		// TODO Auto-generated method stub
if (!collegeStudentRepository.existsById(id)) {
			
			throw new ResourceNotFoundException("CollegeStudent  not found :: " + id);
		}
		
		PlayEntity student = collegeStudentRepository.findById(id).orElse(null);
		collegeStudentRepository.delete(student);
		return true;
		
	}

	@Override
	public PlayEntity getwf_instance_id(int id) {
		// TODO Auto-generated method stub
		PlayEntity findByWf_instance_id = collegeStudentRepository.findByWf_instance_id(id);
		
		
		
		return findByWf_instance_id;
	}

	@Override
	public PlayEntity updateBywf_instance_id(int id, PlayEntity collegePortalEntity) {
		// TODO Auto-generated method stub
		PlayEntity student = collegeStudentRepository.findByWf_instance_id(id);
				//.orElseThrow(() -> new ResourceNotFoundException("student not found :: " + id));
			
		student.setStudentname(collegePortalEntity.getStudentname());
		
		
		student.setDepartment(collegePortalEntity.getDepartment());
		
		
		student.setJoiningDate(collegePortalEntity.getJoiningDate());
		
		
		student.setPhone(collegePortalEntity.getPhone());
		
		
		student.setEmailId(collegePortalEntity.getEmailId());
		
		collegeStudentRepository.save(student);
		return student;
		
	}

}
