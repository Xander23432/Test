package com.realnet.comm.entity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
@Entity
@Table(name="header_t")
public class Wf_defination_header_t_Entity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private int wf_id;
	private String current_json;
	private String Status;
	
	
	
	
	
	
	public String getCurrent_json() {
		return current_json;
	}
	public void setCurrent_json(String current_json) {
		this.current_json = current_json;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getWf_id() {
		return wf_id;
	}
	public void setWf_id(int wf_id) {
		this.wf_id = wf_id;
	}
	
}
