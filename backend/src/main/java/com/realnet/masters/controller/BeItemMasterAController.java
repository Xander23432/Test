package com.realnet.masters.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.masters.entity.AuditItemReport;
import com.realnet.masters.entity.BeItemMasterA;
import com.realnet.masters.service.BeItemMasterAService;
import com.realnet.masters.service.BeLeaseMasterASevice;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value="/master" )
public class BeItemMasterAController {
	private BeItemMasterAService beItemMasterAService;
	private BeLeaseMasterASevice beLeaseMasterASevice;
	@Autowired
	public BeItemMasterAController(BeItemMasterAService beItemMasterAService,
			BeLeaseMasterASevice beLeaseMasterASevice) {
		super();
		this.beItemMasterAService = beItemMasterAService;
		this.beLeaseMasterASevice=beLeaseMasterASevice;
	}
	@GetMapping("/AuditItemAll")
	public ResponseEntity<List<BeItemMasterA>> getAll(
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "5", required = false) Integer size){
			Pageable p = PageRequest.of(page,size);
			List<BeItemMasterA> l = beItemMasterAService.getAll(p);
			return new ResponseEntity<>(l,HttpStatus.OK);
	}
	@GetMapping("/AuditItemFind/{id}")
	public ResponseEntity<?> getOne(@PathVariable("id") Long id){
		BeItemMasterA b = beItemMasterAService.getOne(id);
		return new ResponseEntity<>(b,HttpStatus.OK);
	}
	@PostMapping("/AuditItemReport")
	public ResponseEntity<List<AuditItemReport>> generater(
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
			@RequestParam("table") String table){
		Pageable p = PageRequest.of(page,size);
		List<AuditItemReport> a = new ArrayList<>();
		if(table.equalsIgnoreCase("ITEM_MASTER")) {
			 a= beItemMasterAService.getAudit(p);
		}else if(table.equalsIgnoreCase("LEASE_MASTER")) {
			 a= beLeaseMasterASevice.getAudit(p);
		}
		
		return new ResponseEntity<>(a,HttpStatus.OK);
	}
	@PostMapping("/AuditItemReportCustom")
	public ResponseEntity<List<AuditItemReport>> generaterCustom(@RequestParam("table") String table
			,@RequestParam String id,@RequestParam String d1,@RequestParam String d2){
		//List<AuditReport> a= beItemMasterAService.getAudit(p);
		List<AuditItemReport> a = new ArrayList<>();
		if(id.equals("") || id.equalsIgnoreCase("null")) {
			if(table.equalsIgnoreCase("ITEM_MASTER")) {
				 a=beItemMasterAService.getAuditCustom(null, d1, d2);
			}else if(table.equalsIgnoreCase("LEASE_MASTER")) {
				 a= beLeaseMasterASevice.getAuditCustom(null,d1,d2);
			}
		}else {
			if(table.equalsIgnoreCase("ITEM_MASTER")) {
				 a=beItemMasterAService.getAuditCustom(id, d1, d2);
			}else if(table.equalsIgnoreCase("LEASE_MASTER")) {
				 a= beLeaseMasterASevice.getAuditCustom(id,d1,d2);
			}
		}
		
		return new ResponseEntity<>(a,HttpStatus.OK);
	}
}
