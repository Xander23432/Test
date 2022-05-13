package com.realnet.masters.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="BE_LEASE_MASTER")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BeLeaseMaster {
	@Id
	private Long leaseId;
	private String leaseNum;
	private Long revisionNum;
	private String leaseName;
	private String leaseType;
	private String locationCode;
	private String leaseStatus;
	private Long customerId;
	private Date startDate;
	private Date endDate;
	private Date terminatedDate;
	private String approvalStatus;
	private Long responsibleUserId;
	private String frequencyCode;
	private Long scheduleDay;
	private Long area;
	private Long rate;
	private Long actualAmount;
	private String currencyCode;
	private Long paymentTermId;
	private Long taxCodeId;
	private String taxIncluded;
	private String revenueAccount;
	private String receivablesAccount;
	private String remarks;
	private Long createdBy;
	private Date creationDate;
	private Long lastUpdatedBy;
	private Date lastUpdateDate;
	private Long approvedBy;
	private Date approvalDate;
}
