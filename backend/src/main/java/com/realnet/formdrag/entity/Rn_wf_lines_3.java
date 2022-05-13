package com.realnet.formdrag.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "rn_wf_lines_3")
public class Rn_wf_lines_3 {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String header_id;
	
	@Column(length = 5000)
	private String model;
	
	public Rn_wf_lines_3() {}

	public Rn_wf_lines_3(Long id, String header_id, String model) {
		super();
		this.id = id;
		this.header_id = header_id;
		this.model = model;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHeader_id() {
		return header_id;
	}

	public void setHeader_id(String header_id) {
		this.header_id = header_id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	
	
}
