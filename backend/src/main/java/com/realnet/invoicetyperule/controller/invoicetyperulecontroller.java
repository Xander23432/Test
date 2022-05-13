package com.realnet.invoicetyperule.controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.invoicetyperule.entity.invoicetyperuleentity;
import com.realnet.invoicetyperule.response.invoicetyperuleresponse;
import com.realnet.invoicetyperule.service.invoicetyperuleservice;
import com.google.common.net.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@RestController
@RequestMapping(value="/api",produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "/invoiceruletype" })
public class invoicetyperulecontroller {
	@Autowired
	private invoicetyperuleservice invoiceservice;
	// get all data
	@ApiOperation(value = "List of department", response = invoicetyperuleresponse.class)
	@GetMapping(path = "/getinvoicerule")
	public invoicetyperuleresponse getdata(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
		invoicetyperuleresponse resp = new invoicetyperuleresponse();
		Pageable paging = PageRequest.of(page, size);
		Page<invoicetyperuleentity> result =  invoiceservice.getlist(paging);
		resp.setPageStats(result, true);
		resp.setInvoiceentity(result.getContent());
		return resp;
	}
	// add data
			@ApiOperation(value = "add a data", response = invoicetyperuleentity.class)
			@PostMapping(path = "/getinvoicerule")
			public ResponseEntity<?> savedata(
					@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken, @Valid
					@RequestBody invoicetyperuleentity invoiceEntity) {
				// String filename=file.getOriginalFilename();
				// System.err.println(filename);
				// sale.setUploadprofile(filename);
				//
				invoicetyperuleentity data = invoiceservice.createCollageStudent(invoiceEntity);
				if (data == null) {
					throw new ResourceNotFoundException(" not saved");
				}
				return ResponseEntity.status(HttpStatus.CREATED).body(data);
			}
			// get data by id
			@ApiOperation(value = "Get a students", response = invoicetyperuleentity.class)
			@GetMapping("/getinvoicerule/{id}" )
			public ResponseEntity<invoicetyperuleentity> getbyid(@PathVariable("id") Integer id) {
				invoicetyperuleentity getid = invoiceservice.getid(id);
				if (getid == null) {
					throw new ResourceNotFoundException("id not found with id " + id);
				}
				return ResponseEntity.ok().body(getid);
			}
			// UPDATE
			@ApiOperation(value = "update a  portal", response = invoicetyperuleentity.class)
			@PutMapping("/getinvoicerule/{id}")
			public ResponseEntity<invoicetyperuleentity> updateTeacher(
					@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
					@PathVariable(value = "id") Integer id, @Valid @RequestBody invoicetyperuleentity invoiceEntity) {
				invoicetyperuleentity updated = invoiceservice.updateById(id, invoiceEntity);
				if (updated == null || id != updated.getid()) {
					throw new ResourceNotFoundException(" not found with id " + id);
				}
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(updated);
			}
			
			
			// DELETE
			@DeleteMapping("/getinvoicerule/{id}")
			public ResponseEntity<Map<String, Boolean>> delete(@PathVariable(value = "id") Integer id) {
				boolean delete = invoiceservice.deleteById(id);
				Map<String, Boolean> response = new HashMap<>();
				if (delete) {
					response.put("delete", Boolean.TRUE);
					return ResponseEntity.status(HttpStatus.OK).body(response);
				}
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
			}
}
