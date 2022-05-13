package com.realnet.comm.entity;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.realnet.fnd.entity.Rn_AuditEntity;

@Entity
@Table(name = "RN_TEACHER_T")
public class Teacher extends Rn_AuditEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@NotNull
	@Size(min=3, message = "{name.minsize}")
	@Column(name = "NAME")
	private String name;
	//@Pattern(regexp = "/^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$/", message = "{email.regex}")
	@NotEmpty(message = "{email.notempty}")
	@Email(message = "{email.valid}")	
	@Column(name = "EMAIL")
	private String email;

	@Column(name = "PHONE_NUMBER")
	private long phoneNumber;

	@Column(name = "SALARY")
	private double salary;

	//@DateTimeFormat(pattern="MM/dd/yyyy")
	//@OneToMany(mappedBy = "teacher", orphanRemoval = true, cascade = CascadeType.PERSIST)
	@OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Student> students;

	public Teacher() {
		super();
	}

	public Teacher(Integer id, String name, String email, long phoneNumber, double salary, List<Student> students) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.salary = salary;
		this.students = students;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getphoneNumber() {
		return phoneNumber;
	}

	public void setphoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

}
