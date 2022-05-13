package com.realnet.invoicetyperule.entity;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.realnet.comm.entity.BookEntity;
@Entity
@Table(name="INVOICE_TYPE")
public class invoicetyperuleentity {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = " id_seq" ,  allocationSize = 1)
	private Integer id;
	//private Integer id;
	private String INVOICE_TYPE;
	private String REQUIRE_PAIRING_EVENT;
	private String REQUIRE_SCHEDULE;
	private String APPLY_GROUP_ID;
	private String UOM;
	private Integer FIXED_VALUE;
	private String DESCRIPTION;
	private String CREATED_ON;
	private String CREATED_BY;
	private String UPDATED_ON;
	private String UPDATED_BY;
	private String ACTIVE;
	private String REQUIRE_CONFIRM;
	
	
	
	public Integer getid() {
		return id;
	}
	public void setid(Integer id) {
		this.id = id;
	}
	public String getINVOICE_TYPE() {
		return INVOICE_TYPE;
	}
	public void setINVOICE_TYPE(String iNVOICE_TYPE) {
		INVOICE_TYPE = iNVOICE_TYPE;
	}
	public String getREQUIRE_PAIRING_EVENT() {
		return REQUIRE_PAIRING_EVENT;
	}
	public void setREQUIRE_PAIRING_EVENT(String rEQUIRE_PAIRING_EVENT) {
		REQUIRE_PAIRING_EVENT = rEQUIRE_PAIRING_EVENT;
	}
	public String getREQUIRE_SCHEDULE() {
		return REQUIRE_SCHEDULE;
	}
	public void setREQUIRE_SCHEDULE(String rEQUIRE_SCHEDULE) {
		REQUIRE_SCHEDULE = rEQUIRE_SCHEDULE;
	}
	public String getAPPLY_GROUP_ID() {
		return APPLY_GROUP_ID;
	}
	public void setAPPLY_GROUP_ID(String aPPLY_GROUP_ID) {
		APPLY_GROUP_ID = aPPLY_GROUP_ID;
	}
	public String getUOM() {
		return UOM;
	}
	public void setUOM(String uOM) {
		UOM = uOM;
	}
	public Integer getFIXED_VALUE() {
		return FIXED_VALUE;
	}
	public void setFIXED_VALUE(Integer fIXED_VALUE) {
		FIXED_VALUE = fIXED_VALUE;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public String getCREATED_ON() {
		return CREATED_ON;
	}
	public void setCREATED_ON(String cREATED_ON) {
		CREATED_ON = cREATED_ON;
	}
	public String getCREATED_BY() {
		return CREATED_BY;
	}
	public void setCREATED_BY(String cREATED_BY) {
		CREATED_BY = cREATED_BY;
	}
	public String getUPDATED_BY() {
		return UPDATED_BY;
	}
	public void setUPDATED_BY(String uPDATED_BY) {
		UPDATED_BY = uPDATED_BY;
	}
	public String getACTIVE() {
		return ACTIVE;
	}
	public void setACTIVE(String aCTIVE) {
		ACTIVE = aCTIVE;
	}
	public String getREQUIRE_CONFIRM() {
		return REQUIRE_CONFIRM;
	}
	public void setREQUIRE_CONFIRM(String rEQUIRE_CONFIRM) {
		REQUIRE_CONFIRM = rEQUIRE_CONFIRM;
	}
	public String getUPDATED_ON() {
		return UPDATED_ON;
	}
	public void setUPDATED_ON(String uPDATED_ON) {
		UPDATED_ON = uPDATED_ON;
	}
	
}
