package com.realnet.ncso.entity1;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="EBS_CUSTOMER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EbsCustomer {
	@Id
	private Long customerId;
	private String customerCode;
	private String customerName;
	private String status;
}
