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

import com.realnet.ncso.entity.ItemQueryCriteria;
import com.realnet.ncso.service.ItemQueryCriteriaService;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
//@CrossOrigin("*")
@RequestMapping(value = "/ncso_i", produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"/ncso_i"})
public class ItemQueryCriteriaController {
	
	@Autowired
	private ItemQueryCriteriaService itemQueryCriteriaService;
	
	@PostMapping("/create")
	public ResponseEntity<?> add(@RequestBody ItemQueryCriteria itemQueryCriteria){
		ItemQueryCriteria addToDb = this.itemQueryCriteriaService.addToDb(itemQueryCriteria);
		return ResponseEntity.ok(addToDb);
	}
	
	@PutMapping("/create")
	public ResponseEntity<?> update(@RequestBody ItemQueryCriteria itemQueryCriteria){
		ItemQueryCriteria updateToDb = this.itemQueryCriteriaService.updateToDb(itemQueryCriteria);
		return ResponseEntity.ok(updateToDb);
	}
	
	@GetMapping("/get-one/{id}")
	public ResponseEntity<?> getOne(@PathVariable("id") Long id){
		ItemQueryCriteria oneById = this.itemQueryCriteriaService.getOneById(id);
		return ResponseEntity.ok(oneById);
	}
	
	@GetMapping("/get-all")
	public List<ItemQueryCriteria> getAll(){
		List<ItemQueryCriteria> allFromDb = this.itemQueryCriteriaService.getAllFromDb();
		return allFromDb;
	}
	
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable("id") Long id) {
		this.itemQueryCriteriaService.deleteFromDbById(id);
		System.out.println("Deletion success...");
	}

}
