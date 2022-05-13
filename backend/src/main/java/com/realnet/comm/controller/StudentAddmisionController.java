package com.realnet.comm.controller;
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

import com.realnet.comm.entity.StudentAddmisionEntity;
import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.fnd.response.StudentAddmisionResponse;
import com.realnet.fnd.service.StudentAddmisionService;
import com.google.common.net.MediaType;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@RestController
@RequestMapping(value="/api",produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "/StudentEntity" })
public class StudentAddmisionController {
	@Autowired
	private StudentAddmisionService StudentService;
	// get all data
	@ApiOperation(value = "List of College Students", response = StudentAddmisionResponse.class)
	@GetMapping(path = "/getaddmision")
	public StudentAddmisionResponse getdata(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
		StudentAddmisionResponse resp = new StudentAddmisionResponse();
		Pageable paging = PageRequest.of(page, size);
		Page<StudentAddmisionEntity> result =  StudentService.getlist(paging);
		resp.setPageStats(result, true);
		resp.setStudentAddmisionEntity(result.getContent());
		return resp;
	}
	// @RequestParam("file") MultipartFile file ,
	// add data
	@ApiOperation(value = "add a student", response = StudentAddmisionEntity.class)
	@PostMapping(path = "/getaddmision")
	public ResponseEntity<?> savedata(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken, @Valid
			@RequestBody StudentAddmisionEntity CollegeStudentEntity) {
		// String filename=file.getOriginalFilename();
		// System.err.println(filename);
		// sale.setUploadprofile(filename);
		//
		StudentAddmisionEntity data = StudentService.createCollageStudent(CollegeStudentEntity);
		if (data == null) {
			throw new ResourceNotFoundException("sales not saved");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(data);
	}
	// get data by id
	@ApiOperation(value = "Get a students", response = StudentAddmisionEntity.class)
	@GetMapping("/getaddmision/{id}" )
	public ResponseEntity<StudentAddmisionEntity> getbyid(@PathVariable("id") Integer id) {
		StudentAddmisionEntity getid = StudentService.getid(id);
		if (getid == null) {
			throw new ResourceNotFoundException("id not found with id " + id);
		}
		return ResponseEntity.ok().body(getid);
	}
	// UPDATE
	@ApiOperation(value = "update a college portal", response = StudentAddmisionEntity.class)
	@PutMapping("/getaddmision/{id}")
	public ResponseEntity<StudentAddmisionEntity> updateTeacher(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
			@PathVariable(value = "id") Integer id, @Valid @RequestBody StudentAddmisionEntity collegeStudentEntity) {
		StudentAddmisionEntity updatedsale = StudentService.updateById(id, collegeStudentEntity);
		if (updatedsale == null || id != updatedsale.getStudentid()) {
			throw new ResourceNotFoundException("student not found with id " + id);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedsale);
	}
	
	
	// DELETE
	@DeleteMapping("/getaddmision/{id}")
	public ResponseEntity<Map<String, Boolean>> delete(@PathVariable(value = "id") Integer id) {
		boolean delete = StudentService.deleteById(id);
		Map<String, Boolean> response = new HashMap<>();
		if (delete) {
			response.put("delete", Boolean.TRUE);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
}
