package com.realnet.ncso.entity1;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="SHIP_VISITS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShipVisits {
	@Id
	private String shipId;//yes
	private String inVoyNbr;//yes
	private String inCallNbr;//yes
	private String outVoyNbr;//yes
	private String outCallNbr;//yes
	private Date eta;
	private Date ata;//yes
	private Date atd;//yes
	private String berth;
	private Date storageCalculationEndtime;

}
