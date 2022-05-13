package com.realnet.fnd.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.realnet.fnd.entity.Error;
import com.realnet.fnd.entity.ErrorPojo;
import com.realnet.fnd.entity.Rn_Lookup_Values;
import com.realnet.fnd.entity.Success;
import com.realnet.fnd.entity.SuccessPojo;
import com.realnet.fnd.response.CustomResponse;
import com.realnet.fnd.service.Rn_LookUp_Service;
import com.realnet.users.entity.User;
import com.realnet.users.service.UserService;
import com.realnet.utils.Constant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "lookup Values" })
public class Rn_LookUpController {

	@Autowired(required=false)
	private UserService userService;

	@Autowired
	private Rn_LookUp_Service lookUpService;

	// GET ALL SORTED AND PAGINATED DATA
	@ApiOperation(value = "List of Lookups", response = CustomResponse.class)
	@GetMapping("/lookup")
	public CustomResponse getLookUpValues(
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
		// sorted data
		Pageable paging = PageRequest.of(page, size, Sort.by(Constant.SORT_BY_CREATION_DATE).descending());
		Page<Rn_Lookup_Values> result = lookUpService.getAll(paging);
		CustomResponse resp = new CustomResponse();
		resp.setPageStats(result, true);
		resp.setItems(result.getContent());
		return resp;

	}

	// GET BY ID
	@ApiOperation(value = "Get A Project", response = Rn_Lookup_Values.class)
	@GetMapping("/lookup/{id}")
	public ResponseEntity<?> getLookupDetails(@PathVariable(value = "id") int id) {
		Rn_Lookup_Values lookup = lookUpService.getById(id);
		// Map<String, Rn_Lookup_Values> extractorMap =
		// Collections.singletonMap("extractior", bcf_extractor);
		// return new ResponseEntity<Map<String, Rn_Lookup_Values>>(extractorMap,
		// HttpStatus.OK);
		// return ResponseEntity.ok().body(bcf_extractor);
		return new ResponseEntity<Rn_Lookup_Values>(lookup, HttpStatus.OK);

	}

	// SAVE LOOKUP
	@ApiOperation(value = "Add new Lookup")
	@PostMapping(value = "/lookup")
	public ResponseEntity<?> saveLookup(@Valid @RequestBody Rn_Lookup_Values lookupRequest) {
		User loggedInUser = userService.getLoggedInUser();
		lookupRequest.setCreatedBy(loggedInUser.getUserId());
		Rn_Lookup_Values savedLookup = lookUpService.save(lookupRequest);

		if (savedLookup == null) {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.LOOKUP_API_TITLE);
			error.setMessage(Constant.LOOKUP_NOT_CREATED);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.LOOKUP_API_TITLE);
		success.setMessage(Constant.LOOKUP_CREATED_SUCCESSFULLY);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.CREATED);
	}
	
	// SAVE LIST OF LOOKUPS
	@ApiOperation(value = "Add new Lookups")
	@PostMapping(value = "/lookups")
	public ResponseEntity<?> saveLookups(@RequestBody List<@Valid Rn_Lookup_Values> lookups) {
		User loggedInUser = userService.getLoggedInUser();
		
		for (Rn_Lookup_Values lookup : lookups) {
			lookup.setCreatedBy(loggedInUser.getUserId());
			Rn_Lookup_Values savedLookup = lookUpService.save(lookup);
			if (savedLookup == null) {
				ErrorPojo errorPojo = new ErrorPojo();
				Error error = new Error();
				error.setTitle(Constant.LOOKUP_API_TITLE);
				error.setMessage(Constant.LOOKUP_NOT_CREATED);
				errorPojo.setError(error);
				return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
			}
		}
		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.LOOKUP_API_TITLE);
		success.setMessage(Constant.LOOKUP_CREATED_SUCCESSFULLY);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.CREATED);
	}

	// UPDATE
	@ApiOperation(value = "Update A Lookup", response = Rn_Lookup_Values.class)
	@PutMapping("/lookup/{id}")
	public ResponseEntity<?> updateLookup(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
			@PathVariable(value = "id") int id, @Valid @RequestBody Rn_Lookup_Values lookupRequest) {
		User loggedInUser = userService.getLoggedInUser();
		lookupRequest.setUpdatedBy(loggedInUser.getUserId());

		Rn_Lookup_Values updatedLookUp = lookUpService.updateById(id, lookupRequest);

		if (lookupRequest.getId() != updatedLookUp.getId()) {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.LOOKUP_API_TITLE);
			error.setMessage(Constant.LOOKUP_NOT_DELETED);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}

		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.LOOKUP_API_TITLE);
		success.setMessage(Constant.LOOKUP_UPDATED_SUCCESSFULLY);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
	}

	// DELETE
	@DeleteMapping("/lookup/{id}")
	public ResponseEntity<?> deleteLookup(@PathVariable(value = "id") int id) {
		boolean deleted = lookUpService.deleteById(id);
		if (deleted) {
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.LOOKUP_API_TITLE);
			success.setMessage(Constant.LOOKUP_DELETED_SUCCESSFULLY);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.LOOKUP_API_TITLE);
			error.setMessage(Constant.LOOKUP_NOT_DELETED);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
	}

	@GetMapping("/lookup_values")
	public ResponseEntity<List<String>> getLookUps() {
		List<String> result = lookUpService.getLookupValues();
		System.out.println(result);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/datatypes")
	public ResponseEntity<List<String>> getDataTypes() {
		List<String> result = lookUpService.getDataTypeValues();
		System.out.println(result);
		return ResponseEntity.ok().body(result);
	}

}
