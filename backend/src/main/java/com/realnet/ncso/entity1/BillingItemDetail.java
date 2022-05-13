package com.realnet.ncso.entity1;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="billing_item_detail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@IdClass(BillingItemDetailCompositeKey.class)
@EnableJpaAuditing
@EntityListeners(AuditingEntityListener.class)
@ToString
public class BillingItemDetail extends DateColumn implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Long billingId;
	@Id
	private Long lineId;
	@Id
	private Long detailId;
	private String recordType;
	private String forwardingAgent;
	private String vesselOperator;
	private String boxOwner;
	private String bookingNo;
	private String containerCode;
	private String containerType;
	private String event;
	private String category;
	private String status;
	private String hazardClass;
	private String sizeHeight;
	private Long sizeWeight;
	private String sizeLength;
	private Long tempRequired;
	private String oog;
	private String shipper;
	private String consignee;
	private String customer;
	private String startShipId;
	private String startVoyNbr;
	private String startCallNbr;
	private String startFromLocType;
	private String startFromLocId;;
	private String startFromPosId;
	private String startToLocType;
	private String startToLocId;
	private String startToPosId;
	private String fromLocType;
	private String fromLocId;
	private String fromPosId;
	private Date activityStartDate;
	private Date activityEndDate;
	private Date chargeStartDate;
	private Date chargeEndDate;
	private Long duration;
	private Long totalUnit;
	private Date performDate;
	private Long equseGkey;
	private String sourceCustomer;
	private String sourceCategory;
	private Long sourceId1;
	private Long sourceId2;
	private Long rangeStart;
	private Long rangeEnd;
	private String discrepancyRemarks;
	private String discrepancyReason;
	private String discrepancyType;
	private String createdBy;
	private String updatedBy;
	private String ownerId;
}
