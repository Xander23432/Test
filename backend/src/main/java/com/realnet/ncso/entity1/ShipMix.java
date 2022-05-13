package com.realnet.ncso.entity1;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShipMix {
	private String vesselCode;
	private String vesselName;//yes
	private String lineCode;//yes
	private String inVoyage;//yes
	private String outVoyage;//yes
	private String callNo;//yes	
	private Date ATA;//yes
	private Date ATD;//yes
	private String LOCATION;//yes
	private String serviceId;//yes
	private BigDecimal LOA;//yes
	private String loaUom;
	private BigDecimal GT;
	private String ataFormated;
	private String atdFormated;
}