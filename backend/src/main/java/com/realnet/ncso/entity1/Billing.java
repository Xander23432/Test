package com.realnet.ncso.entity1;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Entity
@Table(name="Billing")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EnableJpaAuditing
@EntityListeners(AuditingEntityListener.class)
@ToString
public class Billing extends DateColumn implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Long billingId;
	private String invoiceType;
	private String invoiceNo;
	private Date invoiceDate;
	private Date startDate;
	private Date endDate;
	private String orgId;
	private String tosCode;
	private String customerCode;
	private String groupId;
	private String poNumber;
	private String vesselCode;
	private String vesselName;
	private String inVoyage;
	private String outVoyage;
	private String lineCode;
	private String callNo;
	private Long loa;
	private String loaUom;
	private Long gt;
	private Date ata;
	private Date atd;
	private String location;
	private String remarks;
	private Date uploadDate;
	private String confirmStatus;
	private Date confirmDate;
	private String confirmBy;
	private String postStatus;
	private Date postDate;
	private String invoiceStatus;
	private String createdBy;
	private String updatedBy;
	private String serviceId;
	private String tracType;
	private String waive;
	private String specialPrice;
	@Transient
	private String invoiceDateFormated;
	@Transient
	private String confirmDateFormated;
	@Transient
	private String postDateFormated;
	@Transient
	private String createdOnFormated;
	@Transient
	private String updatedOnFormated;
	@Transient
	private String ataFormated;
	@Transient
	private String atdFormated;
	
}
