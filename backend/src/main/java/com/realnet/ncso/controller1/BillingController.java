package com.realnet.ncso.controller1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.ncso.entity1.MixOfBilling;
import com.realnet.ncso.service.impl1.BillingService;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value="/ncso1" )
@Api(tags = {"/ncso"})
public class BillingController {
	
	private BillingService billingService;
	@Autowired
	public BillingController(BillingService billingService) {
		super();
		this.billingService = billingService;
	}
	@GetMapping("/newDispute/{disputeId}")
	public ResponseEntity<?> getDispute(@PathVariable("disputeId") Long disputeId){
		 MixOfBilling findDisputeCustom = billingService.findDisputeCustom(disputeId);
		return new ResponseEntity<>(findDisputeCustom,HttpStatus.OK);
	}
}
