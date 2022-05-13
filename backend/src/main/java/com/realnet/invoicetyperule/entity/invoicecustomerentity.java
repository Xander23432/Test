package com.realnet.invoicetyperule.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name="INVOICE_TYPE_CUSTOMER")
public class invoicecustomerentity {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Sequence")
	@SequenceGenerator(name = "id_Sequence", sequenceName = " id_seq_cust" ,  allocationSize = 1)
	private Integer id;
	private String INVOICE_TYPE;
	private String CUSTOMER_CODE;
	private String UOM;
	private String FIXED_VALUE;
	
	
	
	
	
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
	public String getCUSTOMER_CODE() {
		return CUSTOMER_CODE;
	}
	public void setCUSTOMER_CODE(String cUSTOMER_CODE) {
		CUSTOMER_CODE = cUSTOMER_CODE;
	}
	public String getUOM() {
		return UOM;
	}
	public void setUOM(String uOM) {
		UOM = uOM;
	}
	public String getFIXED_VALUE() {
		return FIXED_VALUE;
	}
	public void setFIXED_VALUE(String fIXED_VALUE) {
		FIXED_VALUE = fIXED_VALUE;
	}
	
}
