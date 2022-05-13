package com.realnet.ncso.entity1;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="BE_CUST_MASTER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EnableJpaAuditing
@ToString
public class BeCustomers {
	@Id
	private Long customerId;
	private String customerNumber;
	private String customerName;
	private String taxNumber;
	private Character creditHold;
	private String activeFlag;
	private String invCurrency;
	private String priority;
	private Long createdBy;
	private Date creationDate;
}
