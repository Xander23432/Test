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

import com.realnet.comm.entity.PlayEntity;
import com.realnet.comm.entity.WorkflowEntity;
import com.realnet.fnd.response.PlayResponse;
import com.realnet.fnd.service.PlayService;
import com.realnet.fnd.service.WorkflowService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
@RestController
@RequestMapping(value="/api",produces=org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "/StudentEntity" })
public class PlayController {

	@Autowired
	private PlayService collegeStudentService;
	
	@Autowired
	private WorkflowService workflowservice;
	
	
	// get all data
		@ApiOperation(value = "List of College Students", response = PlayResponse.class)
		@GetMapping(path = "/student")
		public PlayResponse getdata(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
				@RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
			PlayResponse resp = new PlayResponse();
			Pageable paging = PageRequest.of(page, size);
			Page<PlayEntity> result =  collegeStudentService.getlist(paging);
			resp.setPageStats(result, true);
			resp.setStudentEntity(result.getContent());
			return resp;
		}
		
		// add data
		@ApiOperation(value = "add a student", response = PlayEntity.class)
		@PostMapping(path = "/student")
		public ResponseEntity<?> savedata(
				@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken, @Valid
				@RequestBody PlayEntity StudentEntity) {
			
			WorkflowEntity wf=new WorkflowEntity();
			
				wf.setCurrent_json("{\"step0\":{\"state\":\"current\",\"open\":true},\"step1\":{\"state\":\"not-started\",\"open\":false},\"step2\":{\"state\":\"not-started\",\"open\":false,\"failed\":false},\"step3\":{\"state\":\"not-started\",\"open\":false,\"failed\":false},\"step4\":{\"state\":\"not-started\",\"open\":false,\"failed\":false}}");
				wf.setStatus("not_started");
				wf.setWf_id(0);
				
				WorkflowEntity save = workflowservice.save(wf);
				
				System.out.println("workflow data  "+save);
				
				PlayEntity data=null;
				if(save.getId()!=0)
				{
					StudentEntity.setWf_instance_id(save.getId());
					 data = collegeStudentService.createCollageStudent(StudentEntity);
					 
					 

				}
				
				System.out.println("student data "+data);
			
						if (data == null) {
				throw new ResourceNotFoundException("sales not saved");
			}
			return ResponseEntity.status(HttpStatus.CREATED).body(data);
		}
		
		// get data by id
		@ApiOperation(value = "Get a students", response = PlayEntity.class)
		@GetMapping("/student/{id}" )
		public ResponseEntity<PlayEntity> getbyid(@PathVariable("id") Integer id) {
			PlayEntity getid = collegeStudentService.getid(id);
			if (getid == null) {
				throw new ResourceNotFoundException("id not found with id " + id);
			}
			return ResponseEntity.ok().body(getid);
		}
		
		
		// get data by wf_id
				@ApiOperation(value = "Get a students", response = PlayEntity.class)
				@GetMapping("/studentwf_id/{id}" )
				public ResponseEntity<PlayEntity> getbywfid(@PathVariable("id") Integer id) {
					PlayEntity getid = collegeStudentService.getwf_instance_id(id);
					if (getid == null) {
						throw new ResourceNotFoundException("id not found with id " + id);
					}
					return ResponseEntity.ok().body(getid);
				}
		
				// UPDATE wf_id
				@ApiOperation(value = "update a college portal", response = PlayEntity.class)
				@PutMapping("/studentwf_id/{id}")
				public ResponseEntity<PlayEntity> updatewfid(
						@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
						@PathVariable(value = "id") Integer id, @Valid @RequestBody PlayEntity collegeStudentEntity) {
					PlayEntity updatedsale = collegeStudentService.updateBywf_instance_id(id, collegeStudentEntity);
					if (updatedsale == null || id != updatedsale.getWf_instance_id()) {
						throw new ResourceNotFoundException("student not found with id " + id);
					}
					return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedsale);
				}
				
				// UPDATE jason object
				@ApiOperation(value = "update", response = WorkflowEntity.class)
				@PutMapping("/currentObj/{id}")
				public ResponseEntity<WorkflowEntity> updateJasonObject(@PathVariable(value = "id") Integer id,
						@RequestParam(value="current_json") String obj) {
					System.out.println("OBj::"+obj);
					WorkflowEntity currentJason=new WorkflowEntity();
					currentJason.setCurrent_json(obj);
					WorkflowEntity updatedsale = workflowservice.updateById(id, currentJason);
					if (updatedsale == null || id != updatedsale.getId()) {
						throw new ResourceNotFoundException("student not found with id " + id);
					}
					return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedsale);
				}
				
				
		// UPDATE
		@ApiOperation(value = "update a college portal", response = PlayEntity.class)
		@PutMapping("/student/{id}")
		public ResponseEntity<PlayEntity> updateTeacher(
				@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
				@PathVariable(value = "id") Integer id, @Valid @RequestBody PlayEntity collegeStudentEntity) {
			PlayEntity updatedsale = collegeStudentService.updateById(id, collegeStudentEntity);
			if (updatedsale == null || id != updatedsale.getStudentid()) {
				throw new ResourceNotFoundException("student not found with id " + id);
			}
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedsale);
		}
		
		// DELETE
		@DeleteMapping("/student/{id}")
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
