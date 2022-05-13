package com.realnet.formdrag.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.formdrag.entity.Rn_wf_lines_3;
import com.realnet.formdrag.services.Rn_wf_lines_3Service;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600) 
//@CrossOrigin("*")
@RequestMapping(value = "/r", produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"/Rn_wf_lines_3"})
public class Rn_wf_lines_3Controller {
	
	@Autowired
	private Rn_wf_lines_3Service impl;
	
	@PostMapping("/create")
	public ResponseEntity<Rn_wf_lines_3> add(@RequestBody Rn_wf_lines_3 r) {
		Rn_wf_lines_3 add = this.impl.addToDB(r);
		return ResponseEntity.ok(add);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Rn_wf_lines_3> update(@RequestBody Rn_wf_lines_3 r) {
		Rn_wf_lines_3 add = this.impl.updateInDB(r);
		return ResponseEntity.ok(add);
	}
	
	@GetMapping("/get-one/{id}")
	public Rn_wf_lines_3 getOne(@PathVariable("id") Long id){
		Rn_wf_lines_3 oneFromDBById = this.impl.getOneFromDBById(id);
		return oneFromDBById;
	}
	
	@GetMapping("/get-all")
	public List<Rn_wf_lines_3> getAll(){
		
		return this.impl.getAll();
	}
	
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable("id") Long id) {
		this.impl.deleteOneFromDBById(id);
	}
}
