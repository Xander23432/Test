package com.realnet.ncso.entity1;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="service_orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EnableJpaAuditing
@EntityListeners(AuditingEntityListener.class)
@ToString
public class ServiceOrders {
	@Id
	private String gkey;
	private Date created;
	private String creator;
	private String customer;
	private String notes;
	private String visitType;
	private String trainOrShip;
	private String voyage;
	@Column(name = "`call`")
	private String call;
	private String changer;
	private Date changed;
	private Long sgkey;
	private String sstatus;
}
