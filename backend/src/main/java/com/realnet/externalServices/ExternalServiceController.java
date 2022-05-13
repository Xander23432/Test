package com.realnet.externalServices;

import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value="/callApi" )
public class ExternalServiceController {
	
	@Autowired
	private ExternalService externalService;
//	@Inject
//	private OAuth2RestOperations oauth2RestTemplate;


	public ExternalServiceController(ExternalService externalService) {
		this.externalService = externalService;
	}
	@GetMapping("/testJWT")
	public ResponseEntity<?> test(@RequestBody ServiceModel serviceModel){
		externalService.jwtService(serviceModel);
		return new ResponseEntity<>("done",HttpStatus.OK);
	}
	@GetMapping("/testBasic")
	public ResponseEntity<?> testit(@RequestBody ServiceModel serviceModel){
		Object basicAuthService = externalService.basicAuthService(serviceModel);
		return new ResponseEntity<>(basicAuthService,HttpStatus.OK);
	}
	@GetMapping("/testOauth")
	public ResponseEntity<?> testing(@RequestBody ServiceModel serviceModel){
		ResponseEntity<?> oAuth2Service = externalService.oAuth2Service(serviceModel);
		return new ResponseEntity<>(oAuth2Service.getBody(),HttpStatus.OK);
	}
	@GetMapping("/redirect")
	public void tes() {
		System.out.println("Here");
	}
//	@RequestMapping(value = "/oauth2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<?> getProducts() {
//
//	    ResponseEntity<?> forEntity = oauth2RestTemplate
//	            .getForEntity("http://sercverUsingOAuth2/rest/resourceToConsume",
//	                   String.class);
//	    return new ResponseEntity<>(forEntity.getBody(),HttpStatus.OK);
//	}
	
}
