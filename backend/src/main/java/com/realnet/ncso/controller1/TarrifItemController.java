package com.realnet.ncso.controller1;

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
import com.realnet.ncso.entity1.TarrifItem;
import com.realnet.ncso.repository1.tarrifItemRepository;
import com.realnet.ncso.service.impl1.TarrifItemServiceImpl;

import io.swagger.annotations.Api;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value="/ncso1" )
@Api(tags = {"/ncso"})
public class TarrifItemController {
	private TarrifItemServiceImpl tarrifItemServiceImpl;
	private tarrifItemRepository tariifitemRepository;
	@Autowired
	public TarrifItemController(TarrifItemServiceImpl tarrifItemServiceImpl) {
		super();
		this.tarrifItemServiceImpl = tarrifItemServiceImpl;
	}
	@GetMapping("/getAllTarrifItems/{page}")
	public ResponseEntity<?> getAllItems(
		@PathVariable int page,
		@RequestParam(value = "size", defaultValue = "30", required = false) Integer size){
		Pageable p = PageRequest.of(page,10);
		//List<TarrifItem> t = tarrifItemServiceImpl.getAll(p);
		Page<TarrifItem> t = tarrifItemServiceImpl.getAll(p);
		Object obj = new Object() {
			private List<TarrifItem> content = t.getContent();
			private int totalPages = t.getTotalPages();
			private int currentPage = t.getNumber();
			private long totalElements = t.getTotalElements();
			private int pageSize = t.getSize();
			public List<TarrifItem> getContent() {
				return content;
			}
			public void setContent(List<TarrifItem> content) {
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
//	@GetMapping("/getTarrifItem/{id}")
//	public ResponseEntity<?> getOneItem(@PathVariable("id") Long id){
//		Optional<TarrifItem> o = tarrifItemServiceImpl.getOne(id);
//		return new ResponseEntity<>(o.get(),HttpStatus.OK);
//	}
	// get by item code
//	@GetMapping("/getTarrifItem/{id}")
//	public ResponseEntity<?> getOneItem(@PathVariable("id") String id){
//		Optional<TarrifItem> o = tarrifItemServiceImpl.getOne(id);
//		try {
//			if(o.get()!=null) {
//				return new ResponseEntity<>(o.get(),HttpStatus.OK);
//			}else {
//				return new ResponseEntity<>("No item found",HttpStatus.OK);
//			}
//		}catch(Exception e) {
//			return new ResponseEntity<>("No item found",HttpStatus.OK);
//		}
		
		@GetMapping("/getTarrifItem/{id}")
		public ResponseEntity<?> getOneItem(@PathVariable("id") Long id){
			Optional<TarrifItem> o = tarrifItemServiceImpl.getOne(id);
			try {
				if(o.get()!=null) {
					return new ResponseEntity<>(o.get(),HttpStatus.OK);
				}else {
					return new ResponseEntity<>("No item found",HttpStatus.OK);
				}
			}catch(Exception e) {
				return new ResponseEntity<>("No item found",HttpStatus.OK);
			}
		}
		
		@GetMapping("/getTarrifItemByItemCode/{itemCode}")
		public ResponseEntity<?> getAllTarrif(@PathVariable("itemCode") String itemCode,
				@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
				@RequestParam(value = "size", defaultValue = "30", required = false) Integer size){
			Pageable pageable = PageRequest.of(page,size);
			List<TarrifItem> l = tarrifItemServiceImpl.getAllByItemCode(pageable,itemCode);
			return new ResponseEntity<>(l,HttpStatus.OK);
		}
}
	




