package com.realnet.ncso.entity1;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BiillingSourceCompositeKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long billingId;
	private String sourceTable;
	private Long sourceId;
	private Long seqId;
		
}
