package com.realnet.ncso.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ncso_BillingInformation")
public class BillingInformation {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String billing_id;
	private String invoice_no;
	private String invoice_date;
	private int dispute_id;
	private String tos_code;
	private String customer_code;
	private String special_price;
	private String vessel_code;
	private String in_voyage;
	private String line_code;
	private String loa;
	private String gt;
	private String ata;
	private String location;
	private String remark;
	private String confirm_by;
	private boolean posted = false;
	private String walve;
	private String invoice_type;
	private String invoice_status;
	private String credit_note_no;
	private int order_id;
	private int group_id;
	private int service_id;
	private String vessel_name;
	private String out_voyage;
	private String call_no;
	private String loa_uom;
	private String po_no;
	private String atd;
	private String organisation_id;
	private String confirm_date;
	private String post_date;
	
	private String created_by;
	private String created_on;
	private String updated_by;
	private String updated_on;
	
	public BillingInformation() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBilling_id() {
		return billing_id;
	}

	public void setBilling_id(String billing_id) {
		this.billing_id = billing_id;
	}

	public String getInvoice_no() {
		return invoice_no;
	}

	public void setInvoice_no(String invoice_no) {
		this.invoice_no = invoice_no;
	}

	public String getInvoice_date() {
		return invoice_date;
	}

	public void setInvoice_date(String invoice_date) {
		this.invoice_date = invoice_date;
	}

	public int getDispute_id() {
		return dispute_id;
	}

	public void setDispute_id(int dispute_id) {
		this.dispute_id = dispute_id;
	}

	public String getTos_code() {
		return tos_code;
	}

	public void setTos_code(String tos_code) {
		this.tos_code = tos_code;
	}

	public String getCustomer_code() {
		return customer_code;
	}

	public void setCustomer_code(String customer_code) {
		this.customer_code = customer_code;
	}

	public String getSpecial_price() {
		return special_price;
	}

	public void setSpecial_price(String special_price) {
		this.special_price = special_price;
	}

	public String getVessel_code() {
		return vessel_code;
	}

	public void setVessel_code(String vessel_code) {
		this.vessel_code = vessel_code;
	}

	public String getIn_voyage() {
		return in_voyage;
	}

	public void setIn_voyage(String in_voyage) {
		this.in_voyage = in_voyage;
	}

	public String getLine_code() {
		return line_code;
	}

	public void setLine_code(String line_code) {
		this.line_code = line_code;
	}

	public String getLoa() {
		return loa;
	}

	public void setLoa(String loa) {
		this.loa = loa;
	}

	public String getGt() {
		return gt;
	}

	public void setGt(String gt) {
		this.gt = gt;
	}

	public String getAta() {
		return ata;
	}

	public void setAta(String ata) {
		this.ata = ata;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getConfirm_by() {
		return confirm_by;
	}

	public void setConfirm_by(String confirm_by) {
		this.confirm_by = confirm_by;
	}

	public boolean isPosted() {
		return posted;
	}

	public void setPosted(boolean posted) {
		this.posted = posted;
	}

	public String getWalve() {
		return walve;
	}

	public void setWalve(String walve) {
		this.walve = walve;
	}

	public String getInvoice_type() {
		return invoice_type;
	}

	public void setInvoice_type(String invoice_type) {
		this.invoice_type = invoice_type;
	}

	public String getInvoice_status() {
		return invoice_status;
	}

	public void setInvoice_status(String invoice_status) {
		this.invoice_status = invoice_status;
	}

	public String getCredit_note_no() {
		return credit_note_no;
	}

	public void setCredit_note_no(String credit_note_no) {
		this.credit_note_no = credit_note_no;
	}

	public int getOrder_id() {
		return order_id;
	}

	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}

	public int getGroup_id() {
		return group_id;
	}

	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}

	public int getService_id() {
		return service_id;
	}

	public void setService_id(int service_id) {
		this.service_id = service_id;
	}

	public String getVessel_name() {
		return vessel_name;
	}

	public void setVessel_name(String vessel_name) {
		this.vessel_name = vessel_name;
	}

	public String getOut_voyage() {
		return out_voyage;
	}

	public void setOut_voyage(String out_voyage) {
		this.out_voyage = out_voyage;
	}

	public String getCall_no() {
		return call_no;
	}

	public void setCall_no(String call_no) {
		this.call_no = call_no;
	}

	public String getLoa_uom() {
		return loa_uom;
	}

	public void setLoa_uom(String loa_uom) {
		this.loa_uom = loa_uom;
	}

	public String getPo_no() {
		return po_no;
	}

	public void setPo_no(String po_no) {
		this.po_no = po_no;
	}

	public String getAtd() {
		return atd;
	}

	public void setAtd(String atd) {
		this.atd = atd;
	}

	public String getOrganisation_id() {
		return organisation_id;
	}

	public void setOrganisation_id(String organisation_id) {
		this.organisation_id = organisation_id;
	}

	public String getConfirm_date() {
		return confirm_date;
	}

	public void setConfirm_date(String confirm_date) {
		this.confirm_date = confirm_date;
	}

	public String getPost_date() {
		return post_date;
	}

	public void setPost_date(String post_date) {
		this.post_date = post_date;
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
