package com.realnet.bi.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.realnet.bi.entity.Rn_Dashboard_Widgets;
import com.realnet.bi.service.Rn_dashboard_widget_service;
import com.realnet.fnd.entity.Error;
import com.realnet.fnd.entity.ErrorPojo;
import com.realnet.fnd.entity.Rn_Module_Setup;
import com.realnet.fnd.entity.Success;
import com.realnet.fnd.entity.SuccessPojo;
import com.realnet.fnd.response.CustomResponse;
import com.realnet.fnd.service.Rn_ModuleSetup_Service;
import com.realnet.rb.entity.Rn_report_builder;
import com.realnet.utils.Constant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = { "Rn_dash_builder" })
public class Rn_dashboard_widget_controller {

	@Autowired
	private Rn_dashboard_widget_service widgetService;
	
	
	
	@ApiOperation(value = "List of Widgets", response = CustomResponse.class)
	@GetMapping("/widget-details")
	public CustomResponse getWireframes(@RequestParam(value = "moduleId") Integer moduleId,
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
		List<Rn_Dashboard_Widgets> headers = widgetService.getByModule(moduleId);
		Pageable paging = PageRequest.of(page, size, Sort.by(Constant.SORT_BY_CREATION_DATE).descending());
		Page<Rn_Dashboard_Widgets> result = new PageImpl<>(headers, paging, headers.size());
		CustomResponse resp = new CustomResponse();
		resp.setPageStats(result, true);
		resp.setItems(result.getContent());
		return resp;
	}
	
	@ApiOperation(value = "Add new Widget")
	@PostMapping(value = "/widget-details")
	public ResponseEntity<?> saveReport(@RequestParam(value = "moduleId") Integer moduleId,
			@Valid @RequestBody Rn_Dashboard_Widgets widget) throws IOException {
		
		Rn_Dashboard_Widgets status = widgetService.saveWidget(widget, moduleId);
		if (status.getId()!=0) {
			SuccessPojo successPojo = new SuccessPojo();
			Success success = new Success();
			success.setTitle(Constant.REPORT_API_TITLE);
			success.setMessage(Constant.REPORT_CREATED_SUCCESSFULLY);
			successPojo.setSuccess(success);
			
			return new ResponseEntity<SuccessPojo>(successPojo, HttpStatus.CREATED);
		} else {
			ErrorPojo errorPojo = new ErrorPojo();
			Error error = new Error();
			error.setTitle(Constant.REPORT_API_TITLE);
			error.setMessage(Constant.REPORT_NOT_CREATED);
			errorPojo.setError(error);
			return new ResponseEntity<ErrorPojo>(errorPojo, HttpStatus.EXPECTATION_FAILED);
		}
	}
}
