package com.realnet.masters.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.masters.entity.BE_ITEM_MASTER;
import com.realnet.masters.service.BeItemMasterService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value="/master" )
public class BeItemMasterController {
	private BeItemMasterService beItemMasterService;
	@Autowired
	public BeItemMasterController(BeItemMasterService beItemMasterService) {
		super();
		this.beItemMasterService = beItemMasterService;
	}
	@GetMapping("/all")
	public ResponseEntity<List<BE_ITEM_MASTER>> getAll(){
		List<BE_ITEM_MASTER> l = beItemMasterService.getAll();
		return new ResponseEntity<>(l,HttpStatus.OK);
	}
	@PostMapping("/add")
	public ResponseEntity<BE_ITEM_MASTER> addOne(@RequestBody BE_ITEM_MASTER be){
		BE_ITEM_MASTER a = beItemMasterService.addOne(be);
		return new ResponseEntity<BE_ITEM_MASTER>(a,HttpStatus.OK);
	}
	@PutMapping("/update")
	public ResponseEntity<BE_ITEM_MASTER> updateOne(@RequestBody BE_ITEM_MASTER be){
		BE_ITEM_MASTER a = beItemMasterService.updateOne(be);
		return new ResponseEntity<BE_ITEM_MASTER>(a,HttpStatus.OK);
	}
	@GetMapping("/find/{id}")
	public ResponseEntity<?> findOne(@PathVariable("id") Long id){
		BE_ITEM_MASTER b = beItemMasterService.getOne(id);
		return new ResponseEntity<>(b,HttpStatus.OK);
	}
}
