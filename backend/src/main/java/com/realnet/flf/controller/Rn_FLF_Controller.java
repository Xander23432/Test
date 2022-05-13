package com.realnet.flf.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.realnet.flf.entity.Rn_FLF_Header;
import com.realnet.flf.entity.Rn_FLF_Line;
import com.realnet.flf.service.Rn_FLF_Service;
import com.realnet.fnd.entity.Error;
import com.realnet.fnd.entity.ErrorPojo;
import com.realnet.fnd.entity.Success;
import com.realnet.fnd.entity.SuccessPojo;
import com.realnet.fnd.response.CustomResponse;
import com.realnet.utils.Constant;
import com.realnet.wfb.service.Rn_WireFrame_Service;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "FLF Library" })
public class Rn_FLF_Controller {

	@Autowired
	private Rn_FLF_Service flfService;

	@Autowired
	private Rn_WireFrame_Service wireFrameService;

	@Value("${projectPath}")
	private String projectPath;

	// GET ALL SORTED AND PAGINATED DATA
	@ApiOperation(value = "List of FLF Header", response = CustomResponse.class)
	@GetMapping("/flf-header")
	public CustomResponse getFLFHeaders(
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
		// sorted data
		Pageable paging = PageRequest.of(page, size, Sort.by(Constant.SORT_BY_CREATION_DATE).descending());
		Page<Rn_FLF_Header> result = flfService.getAll(paging);
		CustomResponse resp = new CustomResponse();
		resp.setPageStats(result, true);
		resp.setItems(result.getContent());
		return resp;
	}

	// GET BY ID
	@ApiOperation(value = "Get An FLF Header", response = Rn_FLF_Header.class)
	@GetMapping("/flf-header/{id}")
	public ResponseEntity<?> getFLFDetails(@PathVariable(value = "id") int id) {
		Rn_FLF_Header flf_header = flfService.getById(id);

		if (flf_header == null) {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.FLF_API_TITLE);
			error.setMessage(Constant.NOT_FOUND_EXCEPTION);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
		// else {
		return new ResponseEntity<Rn_FLF_Header>(flf_header, HttpStatus.OK);
		// }

	}

	// save actions
	@ApiOperation(value = "Add new FLF")
	@PostMapping(value = "/flf-header")
	public ResponseEntity<?> saveAction(@Valid @RequestBody Rn_FLF_Header actionReq) {
		Rn_FLF_Header savedHeader = flfService.save(actionReq);
		if (savedHeader != null) {
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.FLF_API_TITLE);
			success.setMessage(Constant.FLF_CREATED_SUCCESSFULLY);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.CREATED);
		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.FLF_API_TITLE);
			error.setMessage(Constant.FLF_CREATED_SUCCESSFULLY);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
	}

	// UPDATE
	@ApiOperation(value = "Update A FLF", response = Rn_FLF_Header.class)
	@PutMapping("/flf-header/{id}")
	public ResponseEntity<?> updateFLFHeader(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
			@PathVariable(value = "id") int id, @Valid @RequestBody Rn_FLF_Header headerRequest) {

		Rn_FLF_Header updatedHeader = flfService.updateById(id, headerRequest);

		if (headerRequest.getId() != updatedHeader.getId()) {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.FLF_API_TITLE);
			error.setMessage(Constant.FLF_NOT_DELETED);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.FLF_API_TITLE);
		success.setMessage(Constant.FLF_UPDATED_SUCCESSFULLY);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
	}

	// DELETE
	@DeleteMapping("/flf-header/{id}")
	public ResponseEntity<?> deleteFLFHeader(@PathVariable(value = "id") int id) {
		boolean deleted = flfService.deleteById(id);
		if (deleted) {
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.ACTION_BUILDER_API_TITLE);
			success.setMessage(Constant.ACTION_DELETED_SUCCESSFULLY);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.ACTION_BUILDER_API_TITLE);
			error.setMessage(Constant.ACTION_NOT_DELETED);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
	}

	// ============================ LINE PART ===========================

	/*
	 * This is using pageImpl class please note down that will be useful to sort
	 * list of data
	 */
	// GET ALL SORTED AND PAGINATED DATA
	@ApiOperation(value = "List of Action Lines", response = CustomResponse.class)
	@GetMapping("/flf-line")
	public CustomResponse getActionLines(@RequestParam("headerId") Integer headerId,
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
		// sorted data
		Pageable paging = PageRequest.of(page, size, Sort.by(Constant.SORT_BY_CREATION_DATE_NATIVE_QUERY).descending());
		Page<Rn_FLF_Line> result = flfService.getLinesByHeaderId(headerId, paging);
		// else
//		Rn_FLF_Header header = flfService.getById(headerId);
//		List<Rn_FLF_Line> lines = header.getActionBuilderLines();
//		Pageable paging = PageRequest.of(page, size, Sort.by(Constant.SORT_BY_CREATION_DATE).descending());
//		Page<Rn_FLF_Line> result = new PageImpl<>(lines, paging, lines.size());
		CustomResponse resp = new CustomResponse();
		resp.setPageStats(result, true);
		resp.setItems(result.getContent());
		return resp;
	}

	// GET LINE BY ID
	@ApiOperation(value = "Get An FLF Line", response = Rn_FLF_Line.class)
	@GetMapping("/flf-line/{id}")
	public ResponseEntity<?> getFLFLineDetails(@PathVariable(value = "id") int id) {
		Rn_FLF_Line flf_line = flfService.getLineById(id);

		if (flf_line == null) {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.FLF_API_TITLE);
			error.setMessage(Constant.NOT_FOUND_EXCEPTION);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}

		return new ResponseEntity<Rn_FLF_Line>(flf_line, HttpStatus.OK);
	}

	@ApiOperation(value = "Add new FLF Line")
	@PostMapping(value = "/flf-line")
	public ResponseEntity<?> saveFLFLine(@RequestParam("headerId") Integer headerId,
			@Valid @RequestBody Rn_FLF_Line lineReq) {
		Rn_FLF_Line savedLine = flfService.saveLine(headerId, lineReq);
		if (savedLine == null) {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.FLF_API_TITLE);
			error.setMessage(Constant.FLF_CREATED_SUCCESSFULLY);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.FLF_API_TITLE);
		success.setMessage(Constant.FLF_CREATED_SUCCESSFULLY);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.CREATED);
	}

	// UPDATE
	@ApiOperation(value = "Update A FLF Line", response = Rn_FLF_Line.class)
	@PutMapping("/flf-line/{id}")
	public ResponseEntity<?> updateProject(
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authToken,
			@PathVariable(value = "id") int id, @Valid @RequestBody Rn_FLF_Line lineRequest) {

		Rn_FLF_Line updatedLine = flfService.updateLineById(id, lineRequest);

		if (lineRequest.getId() != updatedLine.getId()) {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.FLF_API_TITLE);
			error.setMessage(Constant.FLF_NOT_DELETED);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}

		SuccessPojo successPojo = new SuccessPojo();
		Success success = new Success();
		success.setTitle(Constant.FLF_API_TITLE);
		success.setMessage(Constant.FLF_UPDATED_SUCCESSFULLY);
		successPojo.setSuccess(success);
		return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
	}

	// DELETE
	@DeleteMapping("/flf-line/{id}")
	public ResponseEntity<?> deleteFLFLine(@PathVariable(value = "id") int id) {
		boolean deleted = flfService.deleteLineById(id);
		if (deleted) {
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.FLF_API_TITLE);
			success.setMessage(Constant.FLF_DELETED_SUCCESSFULLY);
			successPojo.setSuccess(success);
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.OK);
		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.FLF_API_TITLE);
			error.setMessage(Constant.FLF_NOT_DELETED);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
	}

}
