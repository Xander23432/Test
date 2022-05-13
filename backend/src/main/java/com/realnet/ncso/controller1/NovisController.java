package com.realnet.ncso.controller1;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.realnet.ncso.entity1.PmodMix;
import com.realnet.ncso.entity1.ShipMix;
import com.realnet.ncso.service.impl1.ShipServiceImpl;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value="/ncso1" )
@Api(tags = {"/ncso"})
public class NovisController {
	private ShipServiceImpl shipServiceImpl;
	@Autowired
	public NovisController(ShipServiceImpl shipServiceImpl) {
		super();
		this.shipServiceImpl = shipServiceImpl;
	}
	@GetMapping("/getOneNovis/{name}/{inVoyNbr}")
	public ResponseEntity<?> getOneById(@PathVariable("name") String name,@PathVariable("inVoyNbr")String inVoyNbr){
		Optional<Object> s = shipServiceImpl.getOneNovis(name, inVoyNbr);
		try {
			if(s.get()!=null) {
				DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
				Object[] q =(Object[]) s.get();
				ShipMix k = new ShipMix();
				 k.setVesselCode((String) q[0]);
				 k.setVesselName((String) q[1]);
				 k.setInVoyage((String) q[2]);
				 k.setOutVoyage((String) q[3]);
				 k.setCallNo((String) q[4]);
				 k.setLineCode((String) q[5]);
				 k.setLOA((BigDecimal) q[6]);
				 k.setLoaUom((String) q[7]);
				 k.setGT((BigDecimal) q[8]);
				 k.setATA((Date) q[9]);
				 k.setAtaFormated(simple.format(k.getATA()));
				 k.setATD((Date) q[10]);
				 k.setAtdFormated(simple.format(k.getATD()));
				 k.setLOCATION((String) q[11]);
				 k.setServiceId((String) q[12]);
				return new ResponseEntity<>(k,HttpStatus.OK);
			}
			}catch(Exception e) {
				return new ResponseEntity<>("No Novis Found",HttpStatus.OK);
			}
			return new ResponseEntity<>("No Novis Found",HttpStatus.OK);
	}
	@GetMapping("/testNovis/{page}")
	public ResponseEntity<?> getAll(
			@PathVariable int page,
			@RequestParam(value = "size", defaultValue = "5", required = false) Integer size){
		Pageable p =PageRequest.of(page,10);
		DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		Page<Object> s = shipServiceImpl.getAll(p);
		ArrayList<ShipMix> sm = new ArrayList<ShipMix>();
		List<Object> a = s.getContent();
		Iterator i = a.iterator();
		while(i.hasNext()) {
			Object[] q = (Object[]) i.next();
			 ShipMix k = new ShipMix();
			 k.setVesselCode((String) q[0]);
			 k.setVesselName((String) q[1]);
			 k.setInVoyage((String) q[2]);
			 k.setOutVoyage((String) q[3]);
			 k.setCallNo((String) q[4]);
			 k.setLineCode((String) q[5]);
			 k.setLOA((BigDecimal) q[6]);
			 k.setLoaUom((String) q[7]);
			 k.setGT((BigDecimal) q[8]);
			 k.setATA((Date) q[9]);
			 k.setAtaFormated(simple.format(k.getATA()));
			 k.setATD((Date) q[10]);
			 k.setAtdFormated(simple.format(k.getATD()));
			 k.setLOCATION((String) q[11]);
			 k.setServiceId((String) q[12]);
			 sm.add(k);
		}
		Object obj = new Object() {
			private List<ShipMix> content = sm;
			private int totalPages = s.getTotalPages();
			private int currentPage = s.getNumber();
			private long totalElements = s.getTotalElements();
			private int pageSize = s.getSize();
			public List<ShipMix> getContent() {
				return content;
			}
			public void setContent(List<ShipMix> content) {
				this.content = content;
			}
			public int getTotalPages() {
				return totalPages;
			}
			public void setTotalPages(int totalPages) {
				this.totalPages = totalPages;
			}
			public int getCurrentPage() {
				return currentPage;
			}
			public void setCurrentPage(int currentPage) {
				this.currentPage = currentPage;
			}
			
			public long getTotalElements() {
				return totalElements;
			}
			public void setTotalElements(long totalElements) {
				this.totalElements = totalElements;
			}
			public int getPageSize() {
				return pageSize;
			}
			public void setPageSize(int pageSize) {
				this.pageSize = pageSize;
			}
		};
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	@GetMapping("/getAllPmod")
	public ResponseEntity<?> getAlPmodsl(
			@RequestParam(value="search",required=false) String search){
		DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		List<Object> s;
		System.out.println(search);
		if(search==null) {
			s =shipServiceImpl.getAllPmod("");
		}else {
			//search = "%"+search+"%";
			s = shipServiceImpl.getAllPmod(search);
		}
		ArrayList<PmodMix> pm = new ArrayList<PmodMix>();
		List<Object> a = s;
		Iterator i = a.iterator();
		while(i.hasNext()) {
			Object[] q = (Object[]) i.next();
			PmodMix k = new PmodMix();
			k.setVesselCode((String) q[0]);
			k.setVesselName((String) q[1]);
			k.setVcn((String) q[2]);
			k.setCallNo((String) q[3]);
			k.setLineCode((String) q[4]);
			k.setLoa((BigDecimal) q[5]);
			k.setGt((BigDecimal) q[6]);
			k.setAta((Date) q[7]);
			k.setAtaFormated(k.getAta()!=null?simple.format(k.getAta()):null);
			k.setAtd((Date) q[8]);
			k.setAtdFormated(k.getAtd()!=null?simple.format(k.getAtd()):null);
			pm.add(k);
		}
		Object obj = new Object() {
			private List<PmodMix> content = pm;

			public List<PmodMix> getContent() {
				return content;
			}
			public void setContent(List<PmodMix> content) {
				this.content = content;
			}

		};
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	@GetMapping("/getOnePmod/{vcn}")
	public ResponseEntity<?> getOnePmod(@PathVariable("vcn") String vcn){
		Optional<Object> s = shipServiceImpl.getOnePmod(vcn);
		try {
		if(s.get()!=null) {
			DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
			Object[] q =(Object[]) s.get();
			System.out.println(q[0]);
			PmodMix k = new PmodMix();
			k.setVesselCode((String) q[0]);
			k.setVesselName((String) q[1]);
			k.setVcn((String) q[2]);
			k.setCallNo((String) q[3]);
			k.setLineCode((String) q[4]);
			k.setLoa((BigDecimal) q[5]);
			k.setGt((BigDecimal) q[6]);
			k.setAta((Date) q[7]);
			k.setAtaFormated(simple.format(k.getAta()));
			k.setAtd((Date) q[8]);
			k.setAtdFormated(simple.format(k.getAtd()));
			return new ResponseEntity<>(k,HttpStatus.OK);
		}
		}catch(Exception e) {
			return new ResponseEntity<>("No Pmod Found",HttpStatus.OK);
		}
		return new ResponseEntity<>("No Pmod Found",HttpStatus.OK);
	}
	
	@GetMapping("/getAllNovis")
	public ResponseEntity<?> testit(@RequestParam(value="search",required=false) String search){
		DateFormat simple =new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		System.out.println(search);
		List<Object> p1;
		if(search==null) {
			p1 =shipServiceImpl.getAllSearch("");
		}else {
		//	search = "%"+search+"%";
			p1 = shipServiceImpl.getAllSearch( search);
		}
		
		ArrayList<ShipMix> sm = new ArrayList<ShipMix>();
		List<Object> a = p1;
		Iterator i = a.iterator();
		while(i.hasNext()) {
			Object[] q = (Object[]) i.next();
			 ShipMix k = new ShipMix();
			 k.setVesselCode((String) q[0]);
			 k.setVesselName((String) q[1]);
			 k.setInVoyage((String) q[2]);
			 k.setOutVoyage((String) q[3]);
			 k.setCallNo((String) q[4]);
			 k.setLineCode((String) q[5]);
			 k.setLOA((BigDecimal) q[6]);
			 k.setLoaUom((String) q[7]);
			 k.setGT((BigDecimal) q[8]);
			 k.setATA((Date) q[9]);
			 k.setAtaFormated(k.getATA()!=null?simple.format(k.getATA()):null);
			 k.setATD((Date) q[10]);
			 k.setAtdFormated(k.getATD()!=null?simple.format(k.getATD()):null);
			 k.setLOCATION((String) q[11]);
			 k.setServiceId((String) q[12]);
			 sm.add(k);
		}
		Object obj = new Object() {
			private List<ShipMix> content = sm;

			public List<ShipMix> getContent() {
				return content;
			}
			public void setContent(List<ShipMix> content) {
				this.content = content;
			}

		};
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
}
