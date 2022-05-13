package com.realnet.comm.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.comm.entity.Student;
import com.realnet.comm.response.StudentResponse;
import com.realnet.comm.service.StudentService;
import com.realnet.exceptions.ResourceNotFoundException;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"Student"})
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	
//	@Autowired
//	private TeacherRepository teacherRepo;

	// ==========GET ALL LINES BY HEADER ID=========
		@GetMapping("/teacher/{h_id}/student")
		public StudentResponse getStudents(@PathVariable(value = "h_id") int h_id,
				@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
				@RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
			
			StudentResponse resp = new StudentResponse();
			Pageable paging = PageRequest.of(page, size);
			Page<Student> result = studentService.getAll(h_id, paging);
			resp.setPageStats(result, true);
			resp.setItems(result.getContent());
			return resp;
		}

		// ==========GET LINE BY HEADER ID & LINE ID=========
		@GetMapping("/teacher/{h_id}/student/{l_id}")
		public ResponseEntity<Student> getStudentByTeachrIdAndStudentId(
				@PathVariable(value = "h_id") int h_id,
				@PathVariable(value = "l_id") int l_id) {
			Student student = studentService.getByIdAndHeaderId(l_id, h_id);
			if(student == null || student.getId() != l_id) {
				throw new ResourceNotFoundException("student id not found");
			}
			return ResponseEntity.ok().body(student);
		}

		// ==========CREATE LINES=========
		@PostMapping("/teacher/{h_id}/student")
		public ResponseEntity<Student> createStudent(
				@PathVariable(value = "h_id") int h_id,
				@Valid @RequestBody Student student) {
			
			Optional<Student> savedStudent = studentService.saveByHeaderId(h_id, student);
			if(!savedStudent.isPresent()) {
				throw new ResourceNotFoundException("student not saved");
			}
			Student stu = savedStudent.get();
			System.out.println(stu);
			return ResponseEntity.status(HttpStatus.CREATED).body(stu);
		}

		// ==========UPDATE=========
		@PutMapping("/teacher/{h_id}/student/{l_id}")
		public ResponseEntity<Student> updateStudent(
				@PathVariable(value = "h_id") int h_id,
				@PathVariable(value = "l_id") int l_id,
				@Valid @RequestBody Student student) {
			Student updatedStudent = studentService.updateByIdAndHeaderId(l_id, h_id, student);
			//Student stu = updatedStudent.get();
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedStudent);
		}

		// ==========DELETE=========
		@DeleteMapping("/teacher/{h_id}/student/{l_id}")
		public ResponseEntity<Map<String, Boolean>> deleteStudent(
				@PathVariable(value = "h_id") int h_id,
				@PathVariable(value = "l_id") int l_id) {
			boolean deleted = studentService.deleteByIdAndHeaderId(l_id, h_id);
			System.out.println("deleted ? " + deleted);
			
			Map<String, Boolean> response = new HashMap<>();
			if (deleted) {
				response.put("deleted", Boolean.TRUE);
				return ResponseEntity.status(HttpStatus.OK).body(response);
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}

}
