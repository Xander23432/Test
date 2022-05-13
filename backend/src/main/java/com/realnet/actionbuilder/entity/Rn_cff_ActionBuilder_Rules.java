package com.realnet.actionbuilder.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.realnet.fnd.entity.Rn_Who_AccId_Column;

import lombok.ToString;

@ToString
@Entity
@Table(name = "RN_CFF_ACTION_BUILDER_RULES_T")
public class Rn_cff_ActionBuilder_Rules extends Rn_Who_AccId_Column {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "ACTION_TYPE1")
	private String actionType1;

	@Column(name = "ACTION_TYPE2")
	private String actionType2;

	@Column(name = "CODE")
	private String code;

	public Rn_cff_ActionBuilder_Rules() {
		super();
	}

	public Rn_cff_ActionBuilder_Rules(Integer id, String actionType1, String actionType2, String code) {
		super();
		this.id = id;
		this.actionType1 = actionType1;
		this.actionType2 = actionType2;
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getActionType1() {
		return actionType1;
	}

	public void setActionType1(String actionType1) {
		this.actionType1 = actionType1;
	}

	public String getActionType2() {
		return actionType2;
	}

	public void setActionType2(String actionType2) {
		this.actionType2 = actionType2;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
