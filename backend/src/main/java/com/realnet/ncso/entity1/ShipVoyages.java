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
@Table(name="SHIP_VOYAGES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShipVoyages {
	@Id
	private String shipId;
	private String nbr;
	private String serviceId;//yes
	private String ditFlag;
}
