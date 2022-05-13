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

import com.realnet.ncso.entity.BillingInformation;
import com.realnet.ncso.service.BillingInformationService;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
//@CrossOrigin("*")
@RequestMapping(value = "/ncso_b", produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"/ncso_b"})
public class BillingInformationController {
	
	@Autowired
	private BillingInformationService billingInformationService;
	
	@PostMapping("/create")
	public ResponseEntity<?> add(@RequestBody BillingInformation billingInformation){
		BillingInformation addToDb = this.billingInformationService.addToDb(billingInformation);
		return ResponseEntity.ok(addToDb);
	}
	
	@PutMapping("/create")
	public ResponseEntity<?> update(@RequestBody BillingInformation billingInformation){
		BillingInformation updateToDb = this.billingInformationService.updateToDb(billingInformation);
		return ResponseEntity.ok(updateToDb);
	}
	
	@GetMapping("/get-one/{id}")
	public ResponseEntity<?> getOne(@PathVariable("id") Long id){
		BillingInformation oneById = this.billingInformationService.getOneById(id);
		return ResponseEntity.ok(oneById);
	}
	
	@GetMapping("/get-all")
	public List<BillingInformation> getAll(){
		List<BillingInformation> allFromDb = this.billingInformationService.getAllFromDb();		
		return allFromDb;
	}
	
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable("id") Long id) {
		System.out.println("request came to api");
		this.billingInformationService.deleteFromDbById(id);
	}

}
