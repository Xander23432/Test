package com.realnet.masters.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.masters.entity.AuditItemReport;
import com.realnet.masters.entity.BeLeaseMasterA;
import com.realnet.masters.service.BeLeaseMasterASevice;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value="/master" )
public class BeLeaseMasterAController {
	private BeLeaseMasterASevice beLeaseMasterASevice;
	@Autowired
	public BeLeaseMasterAController(BeLeaseMasterASevice beLeaseMasterASevice) {
		super();
		this.beLeaseMasterASevice = beLeaseMasterASevice;
	}
	@GetMapping("/AuditLeaseAll")
	public ResponseEntity<List<BeLeaseMasterA>> getAll(
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "5", required = false) Integer size){
			Pageable p = PageRequest.of(page,size);
			List<BeLeaseMasterA> l = beLeaseMasterASevice.getAll(p);
			return new ResponseEntity<>(l,HttpStatus.OK);
	}
	@GetMapping("/AuditLeaseFind/{id}")
	public ResponseEntity<?> getOne(@PathVariable("id") Long id){
		BeLeaseMasterA b = beLeaseMasterASevice.getOne(id);
		return new ResponseEntity<>(b,HttpStatus.OK);
	}
//	@GetMapping("/AuditLeaseReport")
//	public ResponseEntity<List<AuditItemReport>> generater(
//			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
//			@RequestParam(value = "size", defaultValue = "10", required = false) Integer size){
//		Pageable p = PageRequest.of(page,size);
//		List<AuditItemReport> a= beLeaseMasterASevice.getAudit(p);
//		return new ResponseEntity<>(a,HttpStatus.OK);
//	}
//	@GetMapping("/AuditLeaseReportCustom")
//	public ResponseEntity<List<AuditItemReport>> generaterCustom(@RequestParam Long id,@RequestParam String d1,@RequestParam String d2){
//		//List<AuditReport> a= beItemMasterAService.getAudit(p);
//		List<AuditItemReport> a = beLeaseMasterASevice.getAuditCustom(id, d1, d2);
//		return new ResponseEntity<>(a,HttpStatus.OK);
//	}
}
