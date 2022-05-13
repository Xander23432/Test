package com.realnet.formio.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "board")
public class Board {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long bId;
	private String bName;
	
	@OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
	@JsonIgnore
	List<Column> columns = new ArrayList<>();
	
	public Board() {}
	
	

	
	public Board(Long bId, String bName) {
		super();
		this.bId = bId;
		this.bName = bName;

	}




	public Long getbId() {
		return bId;
	}
	public void setbId(Long bId) {
		this.bId = bId;
	}
	public String getbName() {
		return bName;
	}
	public void setbName(String bName) {
		this.bName = bName;
	}




	public List<Column> getColumns() {
		return columns;
	}




	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	
	
}
