package com.realnet.department.controller;
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


import com.realnet.department.entity.Departmententity;
import com.realnet.department.response.Departmentresponse;
import com.realnet.department.service.Departmentservice;
import com.realnet.exceptions.ResourceNotFoundException;
import com.google.common.net.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@RestController
@RequestMapping(value="/api",produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "/department" })
public class Departmentcontroller {
	@Autowired
	private Departmentservice departmentservice;
	// get all data
	@ApiOperation(value = "List of department", response = Departmentresponse.class)
	@GetMapping(path = "/getdepartment")
	public Departmentresponse getdata(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
		Departmentresponse resp = new Departmentresponse();
		Pageable paging = PageRequest.of(page, size);
		Page<Departmententity> result =  departmentservice.getlist(paging);
		resp.setPageStats(result, true);
		resp.setDepartmententity(result.getContent());
		return resp;
	}
	// add data
		@ApiOperation(value = "add a department", response = Departmententity.class)
		@PostMapping(path = "/getdepartment")
		public ResponseEntity<?> savedata(
				@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken, @Valid
				@RequestBody Departmententity departmentEntity) {
			// String filename=file.getOriginalFilename();
			// System.err.println(filename);
			// sale.setUploadprofile(filename);
			//
			Departmententity data = departmentservice.createCollageStudent(departmentEntity);
			if (data == null) {
				throw new ResourceNotFoundException(" not saved");
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(data);
		}
		// get data by id
		@ApiOperation(value = "Get a students", response = Departmententity.class)
		@GetMapping("/getdepartment/{id}" )
		public ResponseEntity<Departmententity> getbyid(@PathVariable("id") Integer id) {
			Departmententity getid = departmentservice.getid(id);
			if (getid == null) {
				throw new ResourceNotFoundException("id not found with id " + id);
			}
			return ResponseEntity.ok().body(getid);
		}
		// UPDATE
		@ApiOperation(value = "update a  portal", response = Departmententity.class)
		@PutMapping("/getdepartment/{id}")
		public ResponseEntity<Departmententity> updateTeacher(
				@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
				@PathVariable(value = "id") Integer id, @Valid @RequestBody Departmententity collegeStudentEntity) {
			Departmententity updatedsale = departmentservice.updateById(id, collegeStudentEntity);
			if (updatedsale == null || id != updatedsale.getid()) {
				throw new ResourceNotFoundException(" not found with id " + id);
			}
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedsale);
		}
		
		
		// DELETE
		@DeleteMapping("/getdepartment/{id}")
		public ResponseEntity<Map<String, Boolean>> deleteTeacher(@PathVariable(value = "id") Integer id) {
			boolean delete = departmentservice.deleteById(id);
			Map<String, Boolean> response = new HashMap<>();
			if (delete) {
				response.put("delete", Boolean.TRUE);
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
}
