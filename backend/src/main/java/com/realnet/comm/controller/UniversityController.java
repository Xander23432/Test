package com.realnet.comm.controller;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;


import com.realnet.comm.entity.UniversityEntity;
import com.realnet.exceptions.ResourceNotFoundException;
import com.realnet.fnd.response.UniversityResponse;
import com.realnet.fnd.service.UniversityService;

import org.springframework.http.MediaType;
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
 
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "Teacher" })
public class UniversityController {
	@Autowired
	private UniversityService authorservice;


	// GET ALL
	@ApiOperation(value = "List of Teachers", response = UniversityResponse.class)
	@GetMapping("/author")
	public UniversityResponse getTeachers(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
		UniversityResponse resp = new UniversityResponse();
		Pageable paging = PageRequest.of(page, size);
		Page<UniversityEntity> result = authorservice.getAll(paging);
		resp.setPageStats(result, true);
		resp.setAuthor(result.getContent());
		return resp;
	}

	// GET BY ID
	@ApiOperation(value = "Get a teacher", response = UniversityEntity.class)
	@GetMapping("/author/{id}")
	public ResponseEntity<UniversityEntity> getTeacherById(@PathVariable(value = "id") Integer id) {
		UniversityEntity teacher = authorservice.getById(id);
		if (teacher == null) {
			throw new ResourceNotFoundException("teacher not found with id " + id);
		}
		return ResponseEntity.ok().body(teacher);
	}
	// SAVE
		@ApiOperation(value = "Add new teacher", response = UniversityEntity.class)
		@PostMapping("/author")
		public ResponseEntity<UniversityEntity> createTeacher(
				@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
				@Valid @RequestBody UniversityEntity teacher) {

			UniversityEntity savedTeacher = authorservice.save(teacher);
			if (savedTeacher == null) {
				throw new ResourceNotFoundException("teacher is not saved");
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(savedTeacher);
		}

		// UPDATE
		@ApiOperation(value = "update a teacher", response = UniversityEntity.class)
		@PutMapping("/author/{id}")
		public ResponseEntity<UniversityEntity> updateTeacher(
				@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
				@PathVariable(value = "id") Integer id, @Valid @RequestBody UniversityEntity teacher) {

			UniversityEntity updatedTeacher = authorservice.updateById(id, teacher);
			if (updatedTeacher == null || id != updatedTeacher.getId()) {
				throw new ResourceNotFoundException("teacher not found with id " + id);
			}
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedTeacher);
		}

		@DeleteMapping("/author/{id}")
		public ResponseEntity<Map<String, Boolean>> deleteTeacher(@PathVariable(value = "id") Integer id) {
			boolean deleted = authorservice.deleteById(id);
			Map<String, Boolean> response = new HashMap<>();
			if (deleted) {
				response.put("deleted", Boolean.TRUE);
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
}
