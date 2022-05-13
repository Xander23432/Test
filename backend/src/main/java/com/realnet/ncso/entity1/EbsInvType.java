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
@Table(name="EBS_INV_TYPE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EbsInvType {
	@Id
	private String invoiceType;
	private String description;
}
