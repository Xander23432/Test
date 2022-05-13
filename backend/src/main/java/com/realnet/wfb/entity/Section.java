package com.realnet.wfb.entity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Section implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String fieldName;
	private String mapping;
	private String dataType;
	private String type_field;
	private int section_num;
	
	private List<Field> fields;
}
