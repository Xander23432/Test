package com.realnet.actionbuilder.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.realnet.fnd.entity.Rn_Who_AccId_Column;

import lombok.ToString;

@ToString(exclude = "rn_cff_actionBuilderHeader")
@Entity
@Table(name = "RN_CFF_ACTION_BUILDER_LINES_T")
public class Rn_cff_ActionBuilder_Lines extends Rn_Who_AccId_Column {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;

//	@Column(name = "HEADER_ID")
//	private int header_id;

	@Column(name = "ACTION_TYPE1")
	private String actionType1;

	@Column(name = "ACTION_TYPE2")
	private String actionType2;

	@Column(name = "DATA_TYPE")
	private String dataType;

	@Column(name = "VARIABLE_NAME")
	private String variableName;

	@Column(name = "ASSIGNMENT")
	private String assignment;

	@Column(name = "MESSAGE")
	private String message;

	@Column(name = "CONDITIONS")
	private String conditions;

	@Column(name = "FORWARD")
	private String forward;

	@Column(name = "EQUATION")
	private String equation;

	@Column(name = "SEQ")
	private int seq;

	@Column(name = "ACTION")
	private int action;
	
	@Column(name = "GROUP_ID")
	private int groupId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "HEADER_ID")
	@JsonBackReference
	private Rn_cff_ActionBuilder_Header rn_cff_actionBuilderHeader;

	public Rn_cff_ActionBuilder_Lines() {
		super();
	}

	public Rn_cff_ActionBuilder_Lines(Integer id, String actionType1, String actionType2, String dataType,
			String variableName, String assignment, String message, String conditions, String forward, String equation,
			int seq, int action,int groupId, Rn_cff_ActionBuilder_Header rn_cff_actionBuilderHeader) {
		super();
		this.id = id;
		this.actionType1 = actionType1;
		this.actionType2 = actionType2;
		this.dataType = dataType;
		this.variableName = variableName;
		this.assignment = assignment;
		this.message = message;
		this.conditions = conditions;
		this.forward = forward;
		this.equation = equation;
		this.seq = seq;
		this.action = action;
		this.groupId = groupId;
		this.rn_cff_actionBuilderHeader = rn_cff_actionBuilderHeader;
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

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getAssignment() {
		return assignment;
	}

	public void setAssignment(String assignment) {
		this.assignment = assignment;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getConditions() {
		return conditions;
	}

	public void setConditions(String conditions) {
		this.conditions = conditions;
	}

	public String getForward() {
		return forward;
	}

	public void setForward(String forward) {
		this.forward = forward;
	}

	public String getEquation() {
		return equation;
	}

	public void setEquation(String equation) {
		this.equation = equation;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public Rn_cff_ActionBuilder_Header getRn_cff_actionBuilderHeader() {
		return rn_cff_actionBuilderHeader;
	}

	public void setRn_cff_actionBuilderHeader(Rn_cff_ActionBuilder_Header rn_cff_actionBuilderHeader) {
		this.rn_cff_actionBuilderHeader = rn_cff_actionBuilderHeader;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	
	
	

}
