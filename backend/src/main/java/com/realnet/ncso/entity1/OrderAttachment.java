package com.realnet.ncso.entity1;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="NC_SERVICE_ORDER_ATTACHMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EnableJpaAuditing
@IdClass(OrderAttachmentCompositeKey.class)
@EntityListeners(AuditingEntityListener.class)
@ToString
public class OrderAttachment extends DateColumn implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 4442206719697766681L;
//	@EmbeddedId
//	private OrderAttachmentCompositeKey orderAttachmentCompositeKey;
	@Id
	@JoinColumns({
        @JoinColumn(name="orderId", referencedColumnName="orderId"),
    })
	@ManyToOne
	private OrderDetails orderId;
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private Long attachmentId;
	@Lob
	@Column(nullable = true)
	private byte[] attachment;
	@Column(nullable = true)
	private String attachmentType;
	@Column(nullable = true)
	private String attachmentFilename;
	@Column(nullable = false)
	private String cancelStatus="N";
	private String createdBy;
	@Column(nullable = true)
	private String updatedBy;
	
	private String externalFlag;
//	public OrderAttachment(OrderAttachmentCompositeKey orderAttachmentCompositeKey, byte[] attachment, String attachmentType,
//			String attachmentFilename, String cancelStatus,  String createdBy, 
//			String updatedBy, String externalFlag) {
//		super();
//		//this.orderAttachmentCompositeKey = orderAttachmentCompositeKey;
//		this.attachment = attachment;
//		this.attachmentType = attachmentType;
//		this.attachmentFilename = attachmentFilename;
//		this.cancelStatus = cancelStatus;
//		this.createdOn = new Date();
//		this.createdBy = createdBy;
//		this.updatedOn = new Date();
//		this.updatedBy = updatedBy;
//		this.externalFlag = externalFlag;
//	}
//	
	
}
