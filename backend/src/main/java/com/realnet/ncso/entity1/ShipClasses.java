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
@Table(name="SHIP_CLASSES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ShipClasses {
	@Id
	private String id;
	private Long loa;//yes
	private String loaUnits;
	private Long grt;
}
