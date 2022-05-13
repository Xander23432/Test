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
@Table(name="SHIPS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Ships {
	@Id
	private String id;
	private String name;//yes
	private String sclassId;
	private String lineId;//yes
	private String radioCallSign;
}
