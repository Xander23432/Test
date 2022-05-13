package com.realnet.masters.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuditItemReport {
	private String tableName;
	private String referance;
	private String changedCoulumn;
	private String originalValue;
	private String changedValue;
	private String changedBy;
	private String changedDate;
}
