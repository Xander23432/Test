package com.realnet.masters.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="BE_ITEM_MASTER")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BE_ITEM_MASTER {
	@Id
	private Long itemId;
	private String itemCode;
	private String description;
	private String uom;
	private String taxCode;
	private String costCentre;
	private String accountCode;
	private String activeFlag;
	private Long createdBy;
	private Date creationDate;
	private Long lastUpdatedBy;
	private Date lastUpdateDate;
}
