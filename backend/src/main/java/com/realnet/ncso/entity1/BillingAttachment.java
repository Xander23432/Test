package com.realnet.ncso.entity1;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="billing_attachment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@IdClass(BillingAttachmentCompositeKey.class)
@EnableJpaAuditing
@EntityListeners(AuditingEntityListener.class)
@ToString
public class BillingAttachment extends DateColumn implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Long billingId;
	@Id
	private Long attachmentId;
	@Lob
	private byte[] attachment;
	private String attachmentType;
	private String attachmentFilename;
	private String createdBy;
	private String updatedBy;
	private String ediFlag;
	private String externalFlag;
	private String cancelStatus;
}
