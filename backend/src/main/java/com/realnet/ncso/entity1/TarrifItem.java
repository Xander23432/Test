package com.realnet.ncso.entity1;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="TARIFF_ITEM")
@Getter
@Setter
@NoArgsConstructor

@EnableJpaAuditing
@EntityListeners(AuditingEntityListener.class)
@ToString
public class TarrifItem extends DateColumn implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private Long id;
	private String active;
	private String invoiceType;
	private Long itemId;
	private String itemCode;
	private String itemDesc;
	private String priceBreakType;
	private String remarks;
	private Long precedence;
	private Long subPrecedence;
	private String createdBy;
	private String updatedBy;
}
