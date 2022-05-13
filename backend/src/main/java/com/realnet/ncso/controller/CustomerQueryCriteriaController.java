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

import com.realnet.ncso.entity.CustomerQueryCriteria;
import com.realnet.ncso.service.CustomerQueryCriteriaService;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
//@CrossOrigin("*")
@RequestMapping(value = "/ncso_c", produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"/ncso_c"})
public class CustomerQueryCriteriaController {
	
	@Autowired
	private CustomerQueryCriteriaService customerQueryCriteriaService;
	
	@PostMapping("/create")
	public ResponseEntity<?> add(@RequestBody CustomerQueryCriteria customerQueryCriteria){
		CustomerQueryCriteria addToDb = this.customerQueryCriteriaService.addToDb(customerQueryCriteria);
		return ResponseEntity.ok(addToDb);
	}
	
	@PutMapping("/update")
	public ResponseEntity<?> update(@RequestBody CustomerQueryCriteria customerQueryCriteria){
		CustomerQueryCriteria updateToDb = this.customerQueryCriteriaService.updateToDb(customerQueryCriteria);
		return ResponseEntity.ok(updateToDb);
	}
	
	@GetMapping("/get-one/{id}")
	public ResponseEntity<?> getOne(@PathVariable("id") Long id){
		CustomerQueryCriteria oneById = this.customerQueryCriteriaService.getOneById(id);
		return ResponseEntity.ok(oneById);
	}
	
	@GetMapping("/get-all")
	public List<CustomerQueryCriteria> getAll(){
		List<CustomerQueryCriteria> allFromDb = this.customerQueryCriteriaService.getAllFromDb();
		return allFromDb;
	}
	
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable("id") Long id) {
		this.customerQueryCriteriaService.deleteFromDBById(id);
		System.out.println("deletion seccess...");
	}

}
