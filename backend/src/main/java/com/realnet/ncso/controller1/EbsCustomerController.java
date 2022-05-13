package com.realnet.ncso.controller1;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.ncso.entity1.EbsCustomer;
import com.realnet.ncso.service.impl1.EbsCustomerServiceImpl;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value="/ncso1" )
@Api(tags = {"/ncso"})
public class EbsCustomerController {
	private EbsCustomerServiceImpl ebsCustomerServiceImpl;
	@Autowired
	public EbsCustomerController(EbsCustomerServiceImpl ebsCustomerServiceImpl) {
		super();
		this.ebsCustomerServiceImpl = ebsCustomerServiceImpl;
	}
	@GetMapping("/temp")
	public ResponseEntity<?> temp() throws Exception{
		throw new Exception("aaa");
	}
	// with pagination to load data time fast
	@GetMapping("getAllCustomers")
	public ResponseEntity<?> getAll(
			@RequestParam(value="search",required=false) String search){
		List<EbsCustomer> el;
		
		if(search!=null) {
		//	search= "%" + search + "%";
			 el = ebsCustomerServiceImpl.getAll(search);
		}else {
			 el = ebsCustomerServiceImpl.getAll("");
			 
		}
		
		Object obj = new Object() {
			private List<EbsCustomer> content = el;

			public List<EbsCustomer> getContent() {
				return content;
			}
			public void setContent(List<EbsCustomer> content) {
				this.content = content;
			}
		};
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	// getting all data
	@GetMapping("getAllCustomers1")
	public ResponseEntity<List<EbsCustomer>> getAllWithoutPagination(){
		List<EbsCustomer> el = ebsCustomerServiceImpl.getAll1();
		return new ResponseEntity<>(el,HttpStatus.OK);
	}
	// get by id
	
	@GetMapping("/getCustomer/{id}")
	public ResponseEntity<?> getOne(@PathVariable("id") Long id){
		Optional<EbsCustomer> o = ebsCustomerServiceImpl.getOne(id);
		try {
			if(o.get()!=null) {
				return new ResponseEntity<>(o.get(),HttpStatus.OK);
			}else {
				return new ResponseEntity<>("No customer found",HttpStatus.OK);
			}
		}catch(Exception e) {
			return new ResponseEntity<>("No customer found",HttpStatus.OK);
		}
	}
	
}
