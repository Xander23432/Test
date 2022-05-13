package com.realnet.icon.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.realnet.fnd.entity.Rn_Project_Setup;
import com.realnet.fnd.entity.Rn_Who_AccId_Column;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(exclude = {"project"})
@Entity
@Table(name="aged")
@AllArgsConstructor 
@NoArgsConstructor
@Getter
@Setter
public class AgedEntity extends Rn_Who_AccId_Column{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private int id;
	@Column(name = "TYPE")
	private String type;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="object_id",referencedColumnName = "id")
	private Rn_Project_Setup project;	
	
	@Transient
	private int objectId;

}
