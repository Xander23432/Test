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

import com.realnet.ncso.entity.NonContainerServiceOrder;
import com.realnet.ncso.service.NCSOService;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
//@CrossOrigin("*")
@RequestMapping(value = "/ncso", produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"/ncso"})
public class NCSOController {
	
	@Autowired
	private NCSOService nCSOService;
	
	@PostMapping("/create")
	public ResponseEntity<?> add(@RequestBody NonContainerServiceOrder nonContainerServiceOrder){
		NonContainerServiceOrder addToDB = this.nCSOService.addToDB(nonContainerServiceOrder);
		return ResponseEntity.ok(addToDB);
	}
	
	@PutMapping("/create")
	public ResponseEntity<?> update(@RequestBody NonContainerServiceOrder nonContainerServiceOrder){
		NonContainerServiceOrder addToDB = this.nCSOService.updateToDB(nonContainerServiceOrder);
		return ResponseEntity.ok(addToDB);
	}
	
	@GetMapping("/get-one/{id}")
	public NonContainerServiceOrder getOne(@PathVariable("id") Long id) {
		NonContainerServiceOrder oneFromDBById = this.nCSOService.getOneFromDBById(id);
		return oneFromDBById;
	}
	
	@GetMapping("/get-all")
	public List<NonContainerServiceOrder> getAll(){
		List<NonContainerServiceOrder> allFromDB = this.nCSOService.getAllFromDB();
		return allFromDB;
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteFormDbById(@PathVariable("id") Long id) {
		System.out.println("Delete request came to controller..");
		this.nCSOService.deleteFromDBById(id);
		
	}

}
