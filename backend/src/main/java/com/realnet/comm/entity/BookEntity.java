package com.realnet.comm.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
public class BookEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    private String booktype;
    private String bookname;
    private String price;
    private String writer;
    
    @Column(length = 8000)
    private String code;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JsonBackReference
	private UniversityEntity author;


}
