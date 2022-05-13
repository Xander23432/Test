package com.realnet.ncso.controller;

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

import com.realnet.ncso.entity.QueryCriteria;
import com.realnet.ncso.service.QueryCriteriaService;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
//@CrossOrigin("*")
@RequestMapping(value = "/ncso_q", produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"/ncso_q"})
public class QueryCriteriaController {
	
	@Autowired
	private QueryCriteriaService queryCriteriaService;
	
	@PostMapping("/create")
	public ResponseEntity<?> add(@RequestBody QueryCriteria queryCriteria){
		QueryCriteria addToDB = this.queryCriteriaService.addToDB(queryCriteria);
		return ResponseEntity.ok(addToDB);
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> update(@RequestBody QueryCriteria queryCriteria){
		 QueryCriteria updateToDB = this.queryCriteriaService.updateToDB(queryCriteria);
		return ResponseEntity.ok(updateToDB);
	}
	
	@GetMapping("/get-one/{id}")
	public ResponseEntity<?> getOne(@PathVariable("id") Long id){
		QueryCriteria oneById = this.queryCriteriaService.getOneById(id);
		return ResponseEntity.ok(oneById);
	}
	
	@GetMapping("/get-all")
	public List<?> getAll(){
		List<QueryCriteria> allFromDb = this.queryCriteriaService.getAllFromDb();
		return allFromDb;
	}
	
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable("id") Long id) {
		System.out.println("request came to delete api...");
		this.queryCriteriaService.deleteFromDbById(id);
	}
	
}
