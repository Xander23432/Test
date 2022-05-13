package com.realnet.ncso.entity1;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PmodMix {
	private String vesselCode;
	private String vesselName;
	private String vcn;
	private String callNo;
	private String lineCode;
	private BigDecimal loa;
	private BigDecimal gt;
	private Date ata;
	private Date atd;
	private String ataFormated;
	private String atdFormated;
}
