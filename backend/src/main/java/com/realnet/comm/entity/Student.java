package com.realnet.comm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.realnet.fnd.entity.Rn_AuditEntity;

@Entity
@Table(name = "RN_STUDENT_T")
public class Student extends Rn_AuditEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DEPERTMENT")
	private String depertment;

	@Column(name = "ROLL_NUMBER")
	private int rollNumber;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "TEACHER_ID") // TEACHER_ID - teacher_id
	@JsonBackReference
	private Teacher teacher;

	public Student() {
		super();
	}

	public Student(Integer id, String name, String depertment, int rollNumber, Teacher teacher) {
		super();
		this.id = id;
		this.name = name;
		this.depertment = depertment;
		this.rollNumber = rollNumber;
		this.teacher = teacher;
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

	public String getDepertment() {
		return depertment;
	}

	public void setDepertment(String depertment) {
		this.depertment = depertment;
	}

	public int getRollNumber() {
		return rollNumber;
	}

	public void setRollNumber(int rollNumber) {
		this.rollNumber = rollNumber;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

}
