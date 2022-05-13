package com.realnet.ncso.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ncso")
public class NonContainerServiceOrder {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	//prepared by variables
	private String p_name;
	private String p_position_descrition;
	private String p_request_time;
	
	//approved by variables
	private String a_name;
	private String a_position_descrition;
	private String a_request_time;
	
	//re-invoice variables
	private int re_id;
	private String re_invoice_no;
	private String re_invoice_date;
	
	// invoice details variables
	private String in_status;
	private String in_invoice_type;
	private String in_customer_order_no;
	private String in_service_request_by;
	private String in_service_rendered_from;
	private String in_service_rendered_to;
	private String in_vessel_code;
	private String in_vessel_name;
	private String in_in_voyage;
	private String in_out_voyage;
	private int in_service_id;
	private String in_line_code;
	private String in_callno_sign;
	private String in_LOA;
	private String in_LOA_UOM;
	private String in_GT;
	private String in_location;
	private String in_ATA;
	private String in_ATD;
	private String in_po_no;
	private String in_customer_code;
	private String in_special_price;
	private String in_customer_name;
	private String in_remark;
	
	//list of items variables
	private String item_code;
	
	// AOP variables
	private String created_by;
	private String created_on;
	private String updated_by;
	private String updated_on;
	
//	@OneToMany(mappedBy = "ncso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JsonIgnore
//	private Attachments attachments;
	
	public NonContainerServiceOrder() {}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getP_name() {
		return p_name;
	}


	public void setP_name(String p_name) {
		this.p_name = p_name;
	}


	public String getP_position_descrition() {
		return p_position_descrition;
	}


	public void setP_position_descrition(String p_position_descrition) {
		this.p_position_descrition = p_position_descrition;
	}


	public String getP_request_time() {
		return p_request_time;
	}


	public void setP_request_time(String p_request_time) {
		this.p_request_time = p_request_time;
	}


	public String getA_name() {
		return a_name;
	}


	public void setA_name(String a_name) {
		this.a_name = a_name;
	}


	public String getA_position_descrition() {
		return a_position_descrition;
	}


	public void setA_position_descrition(String a_position_descrition) {
		this.a_position_descrition = a_position_descrition;
	}


	public String getA_request_time() {
		return a_request_time;
	}


	public void setA_request_time(String a_request_time) {
		this.a_request_time = a_request_time;
	}


	public int getRe_id() {
		return re_id;
	}


	public void setRe_id(int re_id) {
		this.re_id = re_id;
	}


	public String getRe_invoice_no() {
		return re_invoice_no;
	}


	public void setRe_invoice_no(String re_invoice_no) {
		this.re_invoice_no = re_invoice_no;
	}


	public String getRe_invoice_date() {
		return re_invoice_date;
	}


	public void setRe_invoice_date(String re_invoice_date) {
		this.re_invoice_date = re_invoice_date;
	}


	public String getIn_status() {
		return in_status;
	}


	public void setIn_status(String in_status) {
		this.in_status = in_status;
	}


	public String getIn_invoice_type() {
		return in_invoice_type;
	}


	public void setIn_invoice_type(String in_invoice_type) {
		this.in_invoice_type = in_invoice_type;
	}


	public String getIn_customer_order_no() {
		return in_customer_order_no;
	}


	public void setIn_customer_order_no(String in_customer_order_no) {
		this.in_customer_order_no = in_customer_order_no;
	}


	public String getIn_service_request_by() {
		return in_service_request_by;
	}


	public void setIn_service_request_by(String in_service_request_by) {
		this.in_service_request_by = in_service_request_by;
	}


	public String getIn_service_rendered_from() {
		return in_service_rendered_from;
	}


	public void setIn_service_rendered_from(String in_service_rendered_from) {
		this.in_service_rendered_from = in_service_rendered_from;
	}


	public String getIn_service_rendered_to() {
		return in_service_rendered_to;
	}


	public void setIn_service_rendered_to(String in_service_rendered_to) {
		this.in_service_rendered_to = in_service_rendered_to;
	}


	public String getIn_vessel_code() {
		return in_vessel_code;
	}


	public void setIn_vessel_code(String in_vessel_code) {
		this.in_vessel_code = in_vessel_code;
	}


	public String getIn_vessel_name() {
		return in_vessel_name;
	}


	public void setIn_vessel_name(String in_vessel_name) {
		this.in_vessel_name = in_vessel_name;
	}


	public String getIn_in_voyage() {
		return in_in_voyage;
	}


	public void setIn_in_voyage(String in_in_voyage) {
		this.in_in_voyage = in_in_voyage;
	}


	public String getIn_out_voyage() {
		return in_out_voyage;
	}


	public void setIn_out_voyage(String in_out_voyage) {
		this.in_out_voyage = in_out_voyage;
	}


	public int getIn_service_id() {
		return in_service_id;
	}


	public void setIn_service_id(int in_service_id) {
		this.in_service_id = in_service_id;
	}


	public String getIn_line_code() {
		return in_line_code;
	}


	public void setIn_line_code(String in_line_code) {
		this.in_line_code = in_line_code;
	}


	public String getIn_callno_sign() {
		return in_callno_sign;
	}


	public void setIn_callno_sign(String in_callno_sign) {
		this.in_callno_sign = in_callno_sign;
	}


	public String getIn_LOA() {
		return in_LOA;
	}


	public void setIn_LOA(String in_LOA) {
		this.in_LOA = in_LOA;
	}


	public String getIn_LOA_UOM() {
		return in_LOA_UOM;
	}


	public void setIn_LOA_UOM(String in_LOA_UOM) {
		this.in_LOA_UOM = in_LOA_UOM;
	}


	public String getIn_GT() {
		return in_GT;
	}


	public void setIn_GT(String in_GT) {
		this.in_GT = in_GT;
	}


	public String getIn_location() {
		return in_location;
	}


	public void setIn_location(String in_location) {
		this.in_location = in_location;
	}


	public String getIn_ATA() {
		return in_ATA;
	}


	public void setIn_ATA(String in_ATA) {
		this.in_ATA = in_ATA;
	}


	public String getIn_ATD() {
		return in_ATD;
	}


	public void setIn_ATD(String in_ATD) {
		this.in_ATD = in_ATD;
	}


	public String getIn_po_no() {
		return in_po_no;
	}


	public void setIn_po_no(String in_po_no) {
		this.in_po_no = in_po_no;
	}


	public String getIn_customer_code() {
		return in_customer_code;
	}


	public void setIn_customer_code(String in_customer_code) {
		this.in_customer_code = in_customer_code;
	}


	public String getIn_special_price() {
		return in_special_price;
	}


	public void setIn_special_price(String in_special_price) {
		this.in_special_price = in_special_price;
	}


	public String getIn_customer_name() {
		return in_customer_name;
	}


	public void setIn_customer_name(String in_customer_name) {
		this.in_customer_name = in_customer_name;
	}


	public String getIn_remark() {
		return in_remark;
	}


	public void setIn_remark(String in_remark) {
		this.in_remark = in_remark;
	}


	public String getItem_code() {
		return item_code;
	}


	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}


	public String getCreated_by() {
		return created_by;
	}


	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}


	public String getCreated_on() {
		return created_on;
	}


	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}


//	public Attachments getAttachments() {
//		return attachments;
//	}
//
//
//	public void setAttachments(Attachments attachments) {
//		this.attachments = attachments;
//	}


	public String getUpdated_by() {
		return updated_by;
	}


	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}


	public String getUpdated_on() {
		return updated_on;
	}


	public void setUpdated_on(String updated_on) {
		this.updated_on = updated_on;
	}
	
	
	
}
