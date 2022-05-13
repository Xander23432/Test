package com.realnet.ncso.entity1;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
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
@Table(name="NC_SERVICE_ORDER_ITEM")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EnableJpaAuditing
@IdClass(OrderItemCompositeKey.class)
@EntityListeners(AuditingEntityListener.class)
@ToString
public class OrderItemEntity extends DateColumn implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@JoinColumns({
        @JoinColumn(name="orderId", referencedColumnName="orderId"),
    })
	@ManyToOne
	private OrderDetails orderId;
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private Long lineId;
	
	@Column(nullable = false)
	private String itemCode;
	@Column(nullable = false)
	private String itemDesc;
	@Column(nullable = false)
	private BigDecimal totalUnit;
	private BigDecimal unitPrice;
	@Column(nullable = false)
	private String uom="Q";
	private String remarks;
	@Column(nullable = false)
	private String cancelStatus="N";

	@Column(nullable = false)
	private String createdBy="NORASMAHLELA";
	private String updatedBy;
	@Transient
	private String totalUnitString;
	@Transient
	private String unitPriceString;
//	public OrderItemEntity(OrderDetails orderId,Long lineId, String itemCode, String itemDesc, Long totalUnit,
//			Long unitPrice, String uom, String remarks, String cancelStatus, String createdBy, String updatedBy) {
//		super();
//	//	this.orderItemCompositeKey = orderItemCompositeKey;
//		this.orderId=orderId;
//		this.lineId=lineId;
//		this.itemCode = itemCode;
//		this.itemDesc = itemDesc;
//		this.totalUnit = totalUnit;
//		this.unitPrice = unitPrice;
//		this.uom = uom;
//		this.remarks = remarks;
//		this.cancelStatus = cancelStatus;
//		this.createdBy = createdBy;
//		this.updatedBy = updatedBy;
//	}
	
}
