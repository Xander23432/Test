package com.realnet.ncso.entity1;



import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="NC_SERVICE_ORDER")
@Getter
@Setter
@NoArgsConstructor

@EnableJpaAuditing
@EntityListeners(AuditingEntityListener.class)
@ToString
public class OrderDetails extends DateColumn implements Serializable {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable=false)
	@GeneratedValue(generator = "OrderDetails_gen")
	@SequenceGenerator(name="OrderDetails_gen", sequenceName="ncso_order_details_seq",initialValue = 109300, allocationSize = 1)
	private Long orderId;	
	@JoinColumns({
        @JoinColumn(name="billingId", referencedColumnName="billingId"),
        @JoinColumn(name="creditNoteNo", referencedColumnName="creditNoteNo")
    })
	@ManyToOne
	@NotFound(action=NotFoundAction.IGNORE)
	private CreditNote creditNote;
	
	@Transient
    private List<OrderItemEntity> orderItems;
	
	@Transient
	private List<OrderAttachment> orderAttachments;
	
	@Column(nullable=false)
	private String invoiceType;
	@Column(nullable=false)
	private Date requestDate=new Date(); //day when request was made auto
	private String customerOrderNo;
	@JsonFormat(shape=JsonFormat.Shape.STRING,pattern="dd MMM yyyy HH:mm:ss")
	private Date serviceRenderedFrom;
	@JsonFormat(shape=JsonFormat.Shape.STRING,pattern="dd MMM yyyy HH:mm:ss")
	private Date serviceRenderedTo;
	private String serviceRequestBy;
	@Column(nullable=false)
	private String departmentCode;
	private String departmentDesc;
	@Column(nullable=false)
	private String customerCode;
	private String poNumber;
	private String vesselCode;
	private String vesselName;
	private String inVoyage;
	private String outVoyage;
	private String lineCode;
	private String callNo;
	private BigDecimal loa;
	private String loaUom;
	private BigDecimal gt;
	@JsonFormat(shape=JsonFormat.Shape.STRING,pattern="dd MMM yyyy HH:mm:ss")
	private Date ata;
	@JsonFormat(shape=JsonFormat.Shape.STRING,pattern="dd MMM yyyy HH:mm:ss")
	private Date atd;
	private String location;
	private String remarks;
	@Column(nullable=false)
	private String preparedBy; //default
	private String preparerName; //default
	private String preparerPositionCode;
	private String preparerPositionDesc; //default
	private String approvedBy; //default
	private String approverName; //default
	private String approverPositionCode;
	private String approverPositionDesc; //default
	private Date approveDate;
	@Column(nullable=false)
	private String status;
	@Column(nullable=false)
	private String notifyStatus="Y";

	@Column(nullable=false)
	private String createdBy;

	private String updatedBy;
	private String vesselTosCode;
	private String serviceId;
	private String specialPrice;
	@Transient
	private String customerName;
	@Transient
	private String createdOnFormated;
	@Transient
	private String requestDateFormated;
	@Transient
	private String updatedOnFormated;
	@Transient
	private String approvedDateFormated;
	@Transient
	private String serviceRenderedFromFormated;
	@Transient 
	private String serviceRenderedToFormated;
	@Transient
	private String ataFormated;
	@Transient
	private String atdFormated;
	@Transient 
	private Billing billing;
	@Transient
	private String description;
	
}
