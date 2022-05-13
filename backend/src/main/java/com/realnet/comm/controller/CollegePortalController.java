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
import com.realnet.exceptions.ResourceNotFoundException;
import com.google.common.net.MediaType;
import com.realnet.comm.entity.CollegeStudentEntity;
import com.realnet.fnd.response.CollegePortalResponse;
import com.realnet.fnd.service.CollegeStudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@RestController
@RequestMapping(value="/api",produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "/CollegeStudentEntity" })
public class CollegePortalController {
	@Autowired
	private CollegeStudentService collegeStudentService;
	// get all data
	@ApiOperation(value = "List of College Students", response = CollegePortalResponse.class)
	@GetMapping(path = "/getstudent")
	public CollegePortalResponse getdata(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
		CollegePortalResponse resp = new CollegePortalResponse();
		Pageable paging = PageRequest.of(page, size);
		Page<CollegeStudentEntity> result =  collegeStudentService.getlist(paging);
		resp.setPageStats(result, true);
		resp.setCollegeStudentEntity(result.getContent());
		return resp;
	}
	// @RequestParam("file") MultipartFile file ,
	// add data
	@ApiOperation(value = "add a student", response = CollegeStudentEntity.class)
	@PostMapping(path = "/getstudent")
	public ResponseEntity<?> savedata(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken, @Valid
			@RequestBody CollegeStudentEntity CollegeStudentEntity) {
		// String filename=file.getOriginalFilename();
		// System.err.println(filename);
		// sale.setUploadprofile(filename);
		//
		CollegeStudentEntity data = collegeStudentService.createCollageStudent(CollegeStudentEntity);
		if (data == null) {
			throw new ResourceNotFoundException("sales not saved");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(data);
	}
	// get data by id
	@ApiOperation(value = "Get a students", response = CollegeStudentEntity.class)
	@GetMapping("/getstudent/{id}" )
	public ResponseEntity<CollegeStudentEntity> getbyid(@PathVariable("id") Integer id) {
		 CollegeStudentEntity getid = collegeStudentService.getid(id);
		if (getid == null) {
			throw new ResourceNotFoundException("id not found with id " + id);
		}
		return ResponseEntity.ok().body(getid);
	}
	// UPDATE
	@ApiOperation(value = "update a college portal", response = CollegeStudentEntity.class)
	@PutMapping("/getstudent/{id}")
	public ResponseEntity<CollegeStudentEntity> updateTeacher(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
			@PathVariable(value = "id") Integer id, @Valid @RequestBody CollegeStudentEntity collegeStudentEntity) {
		CollegeStudentEntity updatedsale = collegeStudentService.updateById(id, collegeStudentEntity);
		if (updatedsale == null || id != updatedsale.getStudentid()) {
			throw new ResourceNotFoundException("student not found with id " + id);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedsale);
	}
	
	
	// DELETE
	@DeleteMapping("/getstudent/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteTeacher(@PathVariable(value = "id") Integer id) {
		boolean delete = collegeStudentService.deleteById(id);
		Map<String, Boolean> response = new HashMap<>();
		if (delete) {
			response.put("delete", Boolean.TRUE);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
}