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
@Table(name="BE_LEASE_MASTER_A")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BeLeaseMasterA {
	@Id
	private Long leaseId;
	private String oldLeaseType;
	private String oldLocationCode;
	private String oldLeaseStatus;
	private Long oldCustomerId;
	private Date oldStartDate;
	private Date oldEndDate;
	private Date oldTerminatedDate;
	private String oldFrequencyCode;
	private Long oldScheduleDay;
	private Long oldArea;
	private Long oldRate;
	private Long oldActualAmount;
	private Long oldPaymentTermId;
	private Long oldTaxCodeId;
	private String oldTaxIncluded;
	private String oldRevenueAccount;
	private String oldReceivablesAccount;
	private String newLeaseType;
	private String newLocationCode;
	private String newLeaseStatus;
	private Long newCustomerId;
	private Date newStartDate;
	private Date newEndDate;
	private Date newTerminatedDate;
	private String newFrequencyCode;
	private Long newScheduleDay;
	private Long newArea;
	private Long newRate;
	private Long newActualAmount;
	private Long newPaymentTermId;
	private Long newTaxCodeId;
	private String newTaxIncluded;
	private String newRevenueAccount;
	private String newReceivablesAccount;
	private Long changedBy;
	private Date changedDate;
}
