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
@Table(name="BE_ITEM_MASTER_A")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BeItemMasterA {
	@Id
	private Long itemId;
	private String oldItemCode;
	private String oldDescription;
	private String oldTaxCode;
	private String oldCostCentre;
	private String oldAccountCode;
	private String oldActiveFlag;
	private String oldUom;
	private String newItemCode;
	private String newDescription;
	private String newTaxCode;
	private String newCostCentre;
	private String newAccountCode;
	private String newActiveFlag;
	private String newUom;
	private Long changedBy;
	private Date changedDate;
}
