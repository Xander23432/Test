package com.realnet.ncso.entity1;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
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
@Table(name="credit_note")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(CreditNoteCompositeKey.class)
@EnableJpaAuditing
@EntityListeners(AuditingEntityListener.class)
@ToString
public class CreditNote extends DateColumn implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//@EmbeddedId
//	private CreditNoteCompositeKey creditNoteCompositeKey;
	@Id
	private Long billingId;
	@Id
	private String creditNoteNo;
	private String requireNewInvoice;
	private Date requestDate;
	private Long disputeId;
	private String tracNo;
	private Date tracDate;
	private String createdBy;
	private String updatedBy;
	@Transient
	private Date invoiceDate;
	@Transient
	private String invoiceNo;
}
