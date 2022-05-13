package com.realnet.ncso.entity1;

import java.io.Serializable;

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
@Table(name="billing_attachment")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@IdClass(BiillingSourceCompositeKey.class)
@EnableJpaAuditing
@EntityListeners(AuditingEntityListener.class)
@ToString
public class BillingSource extends DateColumn implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Long billingId;
	@Id
	private String sourceTable;
	@Id
	private Long sourceId;
	@Id
	private Long seqId;
	
	private String creditNoteNo;
	private String creditNoteBillingId;
}
