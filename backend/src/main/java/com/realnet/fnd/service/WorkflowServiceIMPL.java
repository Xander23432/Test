package com.realnet.fnd.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.realnet.comm.entity.WorkflowEntity;
import com.realnet.comm.entity.UniversityEntity;
import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.fnd.repository.WorkflowRepository;


@Service
public class WorkflowServiceIMPL implements WorkflowService {

	@Autowired
	private WorkflowRepository authrepo;
	
	@Override
	public Page<WorkflowEntity> getAll(Pageable page) {
		// TODO Auto-generated method stub
		return authrepo.findAll(page);
	}

	@Override
	public WorkflowEntity getById(int id) {
		// TODO Auto-generated method stub
		WorkflowEntity student = authrepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("student not found :: " + id));
		return student;
	}

	@Override
	public WorkflowEntity save(WorkflowEntity teacher) {
		// TODO Auto-generated method stub
		WorkflowEntity saved = authrepo.save(teacher);
		return saved;
	}

	@Override
	public WorkflowEntity updateById(int id, WorkflowEntity teacherRequest) {
		// TODO Auto-generated method stub
		Optional<WorkflowEntity> findById = authrepo.findById(id);
		WorkflowEntity oldworkflowEntity = findById.get();
		oldworkflowEntity.setCurrent_json(teacherRequest.getCurrent_json());
		oldworkflowEntity.setStatus(teacherRequest.getStatus());
		oldworkflowEntity.setWf_id(teacherRequest.getWf_id());
		
		WorkflowEntity save = authrepo.save(oldworkflowEntity);
		return save;
	}

	@Override
	public boolean deleteById(int id) {
		// TODO Auto-generated method stub
		if (!authrepo.existsById(id)) {
			throw new ResourceNotFoundException("author not found :: " + id);
		}
		
		WorkflowEntity teacher = authrepo.findById(id).orElse(null);
		authrepo.delete(teacher);
		return true;
	}

}
