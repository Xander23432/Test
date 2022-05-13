package com.realnet.ncso.controller1;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.ncso.entity1.EbsInvoiceMixOfAll;
import com.realnet.ncso.entity1.EbsInvoiceType;
import com.realnet.ncso.service.impl1.EbsInvoiceTypeServiceImpl;
import com.realnet.users.service1.AppUserServiceImpl;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value="/ncso1" )
@Api(tags = {"/ncso"})
public class EbsInvoiceTypeController {
	private EbsInvoiceTypeServiceImpl ebsInvoiceTypeServiceImpl;
	private AppUserServiceImpl appUserServiceImpl;
	@Autowired
	public EbsInvoiceTypeController(EbsInvoiceTypeServiceImpl ebsInvoiceTypeServiceImpl,
			AppUserServiceImpl appUserServiceImpl) {
		super();
		this.ebsInvoiceTypeServiceImpl = ebsInvoiceTypeServiceImpl;
		this.appUserServiceImpl = appUserServiceImpl;
	}
	@GetMapping("/getallApprovalPending")
	public ResponseEntity<?> getAllApproved(
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "10", required = false) Integer size){
		Pageable paging = PageRequest.of(page, size);
		List<Object> l = ebsInvoiceTypeServiceImpl.getAllApproved(paging);
		DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
		List<EbsInvoiceMixOfAll> pm = new ArrayList<EbsInvoiceMixOfAll>();
		for(Object e:l) {
			Object[] o = (Object[]) e;
			EbsInvoiceMixOfAll p = new EbsInvoiceMixOfAll();
			p.setOrderId((BigDecimal) o[0]);
			p.setRequestDate((Date) o[1]);
			p.setInvoiceTypeDescription((String) o[2]);
			p.setCustomerCode((String) o[3]);
			p.setCustomerName((String) o[4]);
			p.setVesselCode((String) o[5]);
			p.setVesselName((String) o[6]);
			p.setInVoyage((String) o[7]);
			p.setOutVoyage((String) o[8]);
			p.setPreparedBy((String) o[9]);
			String st = (String) o[10];
			String s1 = appUserServiceImpl.getStatus("NC_SERVICE_ORDER_STATUS",st.toUpperCase());
			p.setStatus(s1);
			p.setRequestDateFormated(simple.format(p.getRequestDate()));
			pm.add(p);
		}
		return new ResponseEntity<>(pm,HttpStatus.OK);
	}
	
	@GetMapping("/getAllInvoiceType")
	public ResponseEntity<List<EbsInvoiceType>> getAllInvoices(){
		List<EbsInvoiceType> l = ebsInvoiceTypeServiceImpl.getAll();
		return new ResponseEntity<>(l,HttpStatus.OK);
	}
	
	
	
}
