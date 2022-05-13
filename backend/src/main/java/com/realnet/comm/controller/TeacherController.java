package com.realnet.comm.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.realnet.comm.entity.Teacher;
import com.realnet.comm.response.TeacherResponse;
import com.realnet.comm.service.TeacherService;
import com.realnet.exceptions.ResourceNotFoundException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "Teacher" })
public class TeacherController {

	@Autowired
	private TeacherService teacherService;

//	@Autowired
//	private TokenUtil tokenUtil;

	// GET ALL
	@ApiOperation(value = "List of Teachers", response = TeacherResponse.class)
	@GetMapping("/teacher")
	public TeacherResponse getTeachers(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
		TeacherResponse resp = new TeacherResponse();
		Pageable paging = PageRequest.of(page, size);
		Page<Teacher> result = teacherService.getAll(paging);
		resp.setPageStats(result, true);
		resp.setItems(result.getContent());
		return resp;
	}

	// GET BY ID
	@ApiOperation(value = "Get a teacher", response = Teacher.class)
	@GetMapping("/teacher/{id}")
	public ResponseEntity<Teacher> getTeacherById(@PathVariable(value = "id") Integer id) {
		Teacher teacher = teacherService.getById(id);
		if (teacher == null) {
			throw new ResourceNotFoundException("teacher not found with id " + id);
		}
		return ResponseEntity.ok().body(teacher);
	}

	// SAVE
	@ApiOperation(value = "Add new teacher", response = Teacher.class)
	@PostMapping("/teacher")
	public ResponseEntity<Teacher> createTeacher(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
			@Valid @RequestBody Teacher teacher) {
//		String userId = tokenUtil.getUserId(authToken);
//		teacher.setCreatedBy(userId);
//		teacher.setAccountId(userId);
		Teacher savedTeacher = teacherService.save(teacher);
		if (savedTeacher == null) {
			throw new ResourceNotFoundException("teacher is not saved");
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(savedTeacher);
	}

	// UPDATE
	@ApiOperation(value = "update a teacher", response = Teacher.class)
	@PutMapping("/teacher/{id}")
	public ResponseEntity<Teacher> updateTeacher(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
			@PathVariable(value = "id") Integer id, @Valid @RequestBody Teacher teacher) {

		Teacher updatedTeacher = teacherService.updateById(id, teacher);
		if (updatedTeacher == null || id != updatedTeacher.getId()) {
			throw new ResourceNotFoundException("teacher not found with id " + id);
		}
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedTeacher);
	}

	// DELETE
	@DeleteMapping("/teacher/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteTeacher(@PathVariable(value = "id") Integer id) {
		boolean deleted = teacherService.deleteById(id);
		Map<String, Boolean> response = new HashMap<>();
		if (deleted) {
			response.put("deleted", Boolean.TRUE);
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

}
