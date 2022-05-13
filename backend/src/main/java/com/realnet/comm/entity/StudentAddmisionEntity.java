package com.realnet.comm.entity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="Student_Addmision")
public class StudentAddmisionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int studentid;
	private String name;
	private String birthdate;
	private int age;
	private String birthplace;
	private String permanentadd;
	private String mainarea;
	private String buildingname;
	private Number pincode;
	private String postaladd;
	private String priviousschool;
	private String otheractivity;
	private Number mark;
	private String grade;
	
	public int getStudentid() {
		return studentid;
	}
	public void setStudentid(int studentid) {
		this.studentid = studentid;
	}
	
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getBirthplace() {
		return birthplace;
	}
	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}
	public String getPermanentadd() {
		return permanentadd;
	}
	public void setPermanentadd(String permanentadd) {
		this.permanentadd = permanentadd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMainarea() {
		return mainarea;
	}
	public void setMainarea(String mainarea) {
		this.mainarea = mainarea;
	}
	public String getBuildingname() {
		return buildingname;
	}
	public void setBuildingname(String buildingname) {
		this.buildingname = buildingname;
	}
	public Number getPincode() {
		return pincode;
	}
	public void setPincode(Number pincode) {
		this.pincode = pincode;
	}
	public String getPostaladd() {
		return postaladd;
	}
	public void setPostaladd(String postaladd) {
		this.postaladd = postaladd;
	}
	public String getPriviousschool() {
		return priviousschool;
	}
	public void setPriviousschool(String priviousschool) {
		this.priviousschool = priviousschool;
	}
	public String getOtheractivity() {
		return otheractivity;
	}
	public void setOtheractivity(String otheractivity) {
		this.otheractivity = otheractivity;
	}
	public Number getMark() {
		return mark;
	}
	public void setMark(Number mark) {
		this.mark = mark;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	
	
}
