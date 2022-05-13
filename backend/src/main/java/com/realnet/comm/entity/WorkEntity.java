package com.realnet.comm.entity;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
@Entity
//@Table(name="workflow")
public class WorkEntity {
	@Id
	
	private  Integer wf_id;
	
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	
	@JsonBackReference
	private WorkflowEntity studententity;


	public Integer getWf_id() {
		return wf_id;
	}


	public void setWf_id(Integer wf_id) {
		this.wf_id = wf_id;
	}


	public WorkflowEntity getStudententity() {
		return studententity;
	}


	public void setStudententity(WorkflowEntity studententity) {
		this.studententity = studententity;
	}
	
	
}