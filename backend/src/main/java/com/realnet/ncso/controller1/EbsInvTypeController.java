package com.realnet.ncso.controller1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.ncso.entity1.EbsInvType;
import com.realnet.ncso.service.impl1.EbsInvTypeServiceImpl;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value="/ncso1" )
@Api(tags = {"/ncso"})
public class EbsInvTypeController {
	private EbsInvTypeServiceImpl ebsInvTypeServiceImpl;
	@Autowired
	public EbsInvTypeController(EbsInvTypeServiceImpl ebsInvTypeServiceImpl) {
		super();
		this.ebsInvTypeServiceImpl = ebsInvTypeServiceImpl;
	}
	@GetMapping("/getAllEbsInvType")
	public ResponseEntity<?> getAllInvType(){
		List<EbsInvType> l = ebsInvTypeServiceImpl.getAll();
		return new ResponseEntity<>(l,HttpStatus.OK);
	}
	
}
