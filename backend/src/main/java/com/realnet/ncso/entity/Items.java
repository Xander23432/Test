package com.realnet.ncso.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ncso_items")
public class Items {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long item_id;
	private String item_code;
	private String item_description;
	private String total_units;
	private String unit_price;
	private String line_total;
	private boolean cancel = false;
	
	public Items() {}

	public Long getItem_id() {
		return item_id;
	}

	public void setItem_id(Long item_id) {
		this.item_id = item_id;
	}

	public String getItem_code() {
		return item_code;
	}

	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}

	public String getItem_description() {
		return item_description;
	}

	public void setItem_description(String item_description) {
		this.item_description = item_description;
	}

	public String getTotal_units() {
		return total_units;
	}

	public void setTotal_units(String total_units) {
		this.total_units = total_units;
	}

	public String getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(String unit_price) {
		this.unit_price = unit_price;
	}

	public String getLine_total() {
		return line_total;
	}

	public void setLine_total(String line_total) {
		this.line_total = line_total;
	}

	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}
	
	

}
