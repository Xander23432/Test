package com.realnet.comm.entity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="student_play")
public class PlayEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int studentid;
	private String studentname;
	private String department;
	private String JoiningDate;
	private Long phone;
	private String emailId;
	private int wf_instance_id;
	
	public int getStudentid() {
		return studentid;
	}
	public void setStudentid(int studentid) {
		this.studentid = studentid;
	}
	public String getStudentname() {
		return studentname;
	}
	public void setStudentname(String studentname) {
		this.studentname = studentname;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getJoiningDate() {
		return JoiningDate;
	}
	public void setJoiningDate(String joiningDate) {
		JoiningDate = joiningDate;
	}
	public Long getPhone() {
		return phone;
	}
	public void setPhone(Long phone) {
		this.phone = phone;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public int getWf_instance_id() {
		return wf_instance_id;
	}
	public void setWf_instance_id(int wf_instance_id) {
		this.wf_instance_id = wf_instance_id;
	}
	@Override
	public String toString() {
		return "PlayEntity [studentid=" + studentid + ", studentname=" + studentname + ", department=" + department
				+ ", JoiningDate=" + JoiningDate + ", phone=" + phone + ", emailId=" + emailId + ", wf_instance_id="
				+ wf_instance_id + "]";
	}
	
	
}
