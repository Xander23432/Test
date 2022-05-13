package com.realnet.fnd.service;
import com.realnet.comm.entity.WorkflowEntity;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface WorkflowService {
	public Page<WorkflowEntity> getAll(Pageable page);
	public WorkflowEntity getById(int id);
	public WorkflowEntity save(WorkflowEntity teacher);
	public WorkflowEntity updateById(int id, WorkflowEntity teacherRequest);
	public boolean deleteById(int id);
	
}
