package com.realnet.ncso.entity1;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EbsInvoiceMixOfAll {
	private BigDecimal OrderId;
	private Date requestDate;
	private String invoiceTypeDescription;
	private String customerCode;
	private String customerName;
	private String vesselCode;
	private String vesselName;
	private String inVoyage;
	private String outVoyage;
	private String preparedBy;
	private String status;
	private String requestDateFormated;
}
