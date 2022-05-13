package com.realnet.ncso.controller1;

import java.util.List;
import java.util.Optional;

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

import com.realnet.ncso.entity1.BeCustomers;
import com.realnet.ncso.service.impl1.BeCustomersService;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value="/ncso1" )
@Api(tags = {"/ncso"})
public class BeCustomersController {
	private BeCustomersService beCustomersService;
	@Autowired
	public BeCustomersController(BeCustomersService beCustomersService) {
		super();
		this.beCustomersService = beCustomersService;
	}
	@GetMapping("/customers/all")
	public ResponseEntity<List<BeCustomers>> getAll(
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "5", required = false) Integer size){
		Pageable paging = PageRequest.of(page, size);
		List<BeCustomers> customers = beCustomersService.getAll(paging);
		return new ResponseEntity<>(customers,HttpStatus.OK);
	}
	@GetMapping("/customers/find/{id}")
	public ResponseEntity<?> getOne(@PathVariable("id") Long id){
		Optional<BeCustomers> c = beCustomersService.getOne(id);
		if(c.get()!=null) {
			return new ResponseEntity<>(c.get(),HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
}
